package at.sporty.team1.application.controller;

import at.sporty.team1.domain.Member;
import at.sporty.team1.domain.interfaces.IMember;
import at.sporty.team1.persistence.PersistenceFacade;
import at.sporty.team1.persistence.util.TooManyResultsException;
import at.sporty.team1.rmi.api.ILoginController;
import at.sporty.team1.rmi.dtos.AuthorisationDTO;
import at.sporty.team1.rmi.dtos.SessionDTO;
import at.sporty.team1.rmi.enums.UserRole;
import at.sporty.team1.rmi.exceptions.SecurityException;
import at.sporty.team1.rmi.security.SecurityModule;
import org.apache.commons.collections4.map.PassiveExpiringMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.naming.AuthenticationException;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.InitialDirContext;
import javax.persistence.PersistenceException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.PublicKey;
import java.util.Hashtable;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Represents a controller to handle the login.
 */
public class LoginController extends UnicastRemoteObject implements ILoginController {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = LogManager.getLogger();
    private static final PassiveExpiringMap<String, Integer> SESSION_REGISTRY = new PassiveExpiringMap<>(1, TimeUnit.HOURS);

    private static Cipher _cipher;
    private static KeyPair _serverKeyPair;
    private static byte[] _encodedPublicServerKey;

	public LoginController() throws RemoteException {
		super();
	}

    @Override
    public byte[] getServerPublicKey()
    throws RemoteException, SecurityException {
        if (_encodedPublicServerKey == null) {
            _encodedPublicServerKey = SecurityModule.getEncodedRSAPublicKey(getServerKeyPair());
        }
        return _encodedPublicServerKey;
    }

	@Override
	public SessionDTO authorize(AuthorisationDTO authorisationDTO)
    throws RemoteException {

		/*
		 * Check if username and password are present in authorisationDTO
		 */
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
            env.put(Context.SECURITY_AUTHENTICATION, "simple");
            env.put(Context.SECURITY_PRINCIPAL, "uid=" + username + ",ou=fhv,ou=People,dc=uclv,dc=net");
            env.put(Context.SECURITY_CREDENTIALS, password);
            env.put(Context.SECURITY_PROTOCOL, "ssl");

			//Trying to login via LDAP
            InitialDirContext context = new InitialDirContext(env);

            //Successful login
            LOGGER.info("Successful login of {}.", username);
            context.close();

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
                        .setMemberId(member.getMemberId())
                        .setClientFingerprint(clientFingerprint);
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

    public static boolean hasEnoughPermissions(SessionDTO session, UserRole requiredRoleLevel) {
        try {

            Cipher cipher = getCipher();
            KeyPair serverKeyPair = getServerKeyPair();

            //Normally this two values should be not null
            if (cipher != null && serverKeyPair != null && session != null) {

                //Decrypting client fingerprint
                cipher.init(Cipher.DECRYPT_MODE, serverKeyPair.getPrivate());
                String decryptedSession = new String(
                    cipher.doFinal(session.getClientFingerprint())
                );

                Integer assignedMemberId = SESSION_REGISTRY.get(decryptedSession);

                //Check if user in session object is assigned to current fingerprint
                if (assignedMemberId != null && assignedMemberId.equals(session.getMemberId())) {
                    //Loading member from data store.
                    IMember member = PersistenceFacade.getNewMemberDAO().findById(assignedMemberId);

                    if (member != null && isInPermissionBound(member.getRole(), requiredRoleLevel)) {
                        //Resetting session timeout
                        updateSessionTimeout(decryptedSession, member.getMemberId());
                        return true;
                    }
                }
            }

        } catch (PersistenceException e) {
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

    private static boolean isInPermissionBound(String isRole, UserRole requiredRoleLevel) {
        /* Return according to user role */
        switch (isRole) {
            case "admin": {
                return UserRole.ADMIN.isInBound(requiredRoleLevel);
            }

            case "member": {
                return UserRole.MEMBER.isInBound(requiredRoleLevel);
            }

            case "trainer": {
                return UserRole.TRAINER.isInBound(requiredRoleLevel);
            }

            case "departmentHead": {
                return UserRole.DEPARTMENT_HEAD.isInBound(requiredRoleLevel);
            }

            case "manager": {
                return UserRole.MANAGER.isInBound(requiredRoleLevel);
            }
        }

        return false;
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