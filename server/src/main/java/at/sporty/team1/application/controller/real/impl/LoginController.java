package at.sporty.team1.application.controller.real.impl;

import at.sporty.team1.application.auth.AccessPolicy;
import at.sporty.team1.application.controller.real.api.ILoginController;
import at.sporty.team1.domain.Member;
import at.sporty.team1.domain.interfaces.IMember;
import at.sporty.team1.persistence.PersistenceFacade;
import at.sporty.team1.persistence.util.TooManyResultsException;
import at.sporty.team1.shared.dtos.AuthorisationDTO;
import at.sporty.team1.shared.dtos.SessionDTO;
import at.sporty.team1.shared.enums.UserRole;
import at.sporty.team1.shared.exceptions.SecurityException;
import at.sporty.team1.shared.security.SecurityModule;
import org.apache.commons.collections4.map.PassiveExpiringMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.naming.AuthenticationException;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.persistence.PersistenceException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.PublicKey;
import java.util.Hashtable;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Represents a controller to handle the login.
 */
public abstract class LoginController implements ILoginController {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = LogManager.getLogger();
    private static final PassiveExpiringMap<String, Integer> SESSION_REGISTRY = new PassiveExpiringMap<>(1, TimeUnit.HOURS);

    private static Cipher _cipher;
    private static KeyPair _serverKeyPair;
    private static byte[] _encodedPublicServerKey;

	protected LoginController() {
	}

    @Override
    public byte[] getServerPublicKey()
    throws SecurityException {
        if (_encodedPublicServerKey == null) {
            _encodedPublicServerKey = SecurityModule.getEncodedRSAPublicKey(getServerKeyPair());
        }
        return _encodedPublicServerKey;
    }

	@Override
	public SessionDTO authorize(AuthorisationDTO authorisationDTO) {

        //Check if username and password are present in authorisationDTO
		if (authorisationDTO == null) return null;
        if (authorisationDTO.getClientPublicKey() == null) return null;
        if (authorisationDTO.getEncryptedUserLogin() == null) return null;
        if (authorisationDTO.getEncryptedUserPassword() == null) return null;

        try {

            Cipher cipher = getCipher();
            KeyPair serverKeyPair = getServerKeyPair();

            cipher.init(Cipher.DECRYPT_MODE, serverKeyPair.getPrivate());

            //Decrypting username
            String username = new String(
                cipher.doFinal(authorisationDTO.getEncryptedUserLogin())
            );

            //Decrypting password
            String password = new String(
                cipher.doFinal(authorisationDTO.getEncryptedUserPassword())
            );

            //Preparing environment for LDAP
            Hashtable<String, String> env = new Hashtable<>();
            env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
            env.put(Context.PROVIDER_URL, "ldaps://ldap.fhv.at:636/dc=uclv,dc=net");

            //Searching for user
            InitialDirContext searchContext = new InitialDirContext(env);
            NamingEnumeration<SearchResult> searchResults = searchContext.search(
                "ou=fhv, ou=People",
                "(&(uid=" + username + ")(cn=*))",
                new SearchControls()
            );

            //Closing context
            searchContext.close();

            if (searchResults.hasMoreElements()) {
                SearchResult searchResult = searchResults.next();
                String principal = searchResult.getNameInNamespace();

                if (!searchResults.hasMoreElements()) {

                    //Setting environment properties
                    env.put(Context.SECURITY_AUTHENTICATION, "simple");
                    env.put(Context.SECURITY_PRINCIPAL, principal);
                    env.put(Context.SECURITY_CREDENTIALS, password);
                    env.put(Context.SECURITY_PROTOCOL, "ssl");

                    //Trying to login via LDAP
                    InitialDirContext loginContext = new InitialDirContext(env);

                    //Successful login
                    LOGGER.info("Successful login of {}.", username);
                    loginContext.close();

                    //Receiving member object from db
                    Member member = PersistenceFacade.getNewMemberDAO().findByUsername(username);

                    //If member was not found -> authorize will be declined
                    if (member != null) {

                        //Preparing client fingerprint
                        PublicKey clientKey = SecurityModule.getDecodedRSAPublicKey(authorisationDTO.getClientPublicKey());

                        //Generating new session id for client
                        String sessionId = UUID.randomUUID().toString();
                        SESSION_REGISTRY.put(sessionId, member.getMemberId());

                        //Encrypting rawFingerprint expressly for target client
                        cipher.init(Cipher.ENCRYPT_MODE, clientKey);
                        byte[] clientFingerprint = cipher.doFinal(sessionId.getBytes());

                        //Sending session object for client
                        return new SessionDTO()
                                .setUserId(member.getMemberId())
                                .setClientFingerprint(clientFingerprint);
                    }
                }
            }

        } catch (InvalidKeyException e) {
            LOGGER.error("Private key is not suitable.", e);
        } catch (BadPaddingException | IllegalBlockSizeException e) {
            LOGGER.error("Received data is corrupted.", e);
        } catch (AuthenticationException e) {
			LOGGER.error("Not successful login attempt detected.", e);
		} catch (NamingException e) {
			LOGGER.error("LDAP protocol communication error.");
			LOGGER.debug("LDAP protocol communication error.", e);
		} catch (PersistenceException e) {
            LOGGER.error("Error occurred while getting member from data store.", e);
        } catch (TooManyResultsException e) {
            LOGGER.error("Too many authentication results from data store were received.", e);
        } catch (SecurityException e) {
            LOGGER.error("Error occurs while generating client fingerprint.", e);
        }

        return null;
	}

    public static boolean hasEnoughPermissions(SessionDTO session, AccessPolicy<IMember> policy) {
        try {

            Cipher cipher = getCipher();
            KeyPair serverKeyPair = getServerKeyPair();

            //Normally this two values should be not null
            if (cipher != null && serverKeyPair != null && session != null && session.getClientFingerprint() != null) {

                //Decrypting client fingerprint
                cipher.init(Cipher.DECRYPT_MODE, serverKeyPair.getPrivate());
                String decryptedSession = new String(
                    cipher.doFinal(session.getClientFingerprint())
                );

                Integer assignedMemberId = SESSION_REGISTRY.get(decryptedSession);

                //Check if user in session object is assigned to current fingerprint
                if (assignedMemberId != null && assignedMemberId.equals(session.getUserId())) {

                    //Loading member from data store.
                    IMember member = PersistenceFacade.getNewMemberDAO().findById(assignedMemberId);

                    //Check if member fulfill given policies
                    if (member != null && policy.isFollowedBy(member)) {
                        //Resetting session timeout
                        updateSessionTimeout(decryptedSession, member.getMemberId());
                        return true;
                    }

                } else if (assignedMemberId != null) {

                    //Auth attempt from not expected member id, session is compromised
                    SESSION_REGISTRY.remove(decryptedSession);

                    LOGGER.warn(
                        "Compromised session \"{}\" was removed. Auth attempt from #{}",
                        decryptedSession,
                        assignedMemberId
                    );

                } else {

                    //Auth attempt with unknown/expired session
                    LOGGER.warn(
                        "Auth attempt from #\"{}\" with unknown/expired session \"{}\" was detected.",
                        session.getUserId(),
                        decryptedSession
                    );
                }
            }

        } catch (PersistenceException | NullPointerException e) {
            LOGGER.error("Error occurred while getting/parsing user role.", e);
        } catch (InvalidKeyException e) {
            LOGGER.error("Private key is not suitable.", e);
        } catch (BadPaddingException | IllegalBlockSizeException e) {
            LOGGER.error("Received data is corrupted.", e);
        } catch (SecurityException e) {
            LOGGER.error("Error occurs while checking client fingerprint.", e);
        }

        return false;
    }

    private static void updateSessionTimeout(String fingerprint, Integer memberId) {
        //Remove value from registry.
        SESSION_REGISTRY.remove(fingerprint);

        //Restarting timeout for given session.
        SESSION_REGISTRY.put(fingerprint, memberId);
    }

    public static boolean isInPermissionBound(IMember member, UserRole requiredRoleLevel) {
        return member != null && isInPermissionBound(member.getRole(), requiredRoleLevel);
    }

    public static boolean isInPermissionBound(String isRole, UserRole requiredRoleLevel) {
        UserRole isUserRole = parseUserRole(isRole);

        return isUserRole.isInBound(requiredRoleLevel);
    }

    public static boolean isNotEscalatedPermissionBound(String newRole, String isRole) {
        UserRole isUserRole = parseUserRole(isRole);
        UserRole newUserRole = parseUserRole(newRole);

        return isUserRole.isInBound(newUserRole);
    }

    private static UserRole parseUserRole(String role) {
        /* Return according to user role */
        if (role == null) return UserRole.GUEST;

        switch (role) {
            case "member": return UserRole.MEMBER;
            case "trainer": return UserRole.TRAINER;
            case "departmentHead": return UserRole.DEPARTMENT_HEAD;
            case "admin": return UserRole.ADMIN;
            default: return UserRole.GUEST;
        }
    }

    private static KeyPair getServerKeyPair() throws SecurityException {
        if (_serverKeyPair == null) {
            _serverKeyPair = SecurityModule.generateNewRSAKeyPair(512);
        }
        return _serverKeyPair;
    }

    private static Cipher getCipher() throws SecurityException {
        if (_cipher == null) {
            _cipher = SecurityModule.getNewRSACipher();
        }
        return _cipher;
    }
}