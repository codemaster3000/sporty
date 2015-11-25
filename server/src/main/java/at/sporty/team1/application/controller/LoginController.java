package at.sporty.team1.application.controller;

import at.sporty.team1.rmi.security.SecurityModule;
import at.sporty.team1.domain.Member;
import at.sporty.team1.rmi.exceptions.SecurityException;
import at.sporty.team1.persistence.PersistenceFacade;
import at.sporty.team1.persistence.util.TooManyResultsException;
import at.sporty.team1.rmi.api.ILoginController;
import at.sporty.team1.rmi.dtos.AuthorisationDTO;
import at.sporty.team1.rmi.dtos.MemberDTO;
import at.sporty.team1.rmi.dtos.SessionDTO;
import at.sporty.team1.rmi.enums.UserRole;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;

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

/**
 * Represents a controller to handle the login.
 */
public class LoginController extends UnicastRemoteObject implements ILoginController {
	private static final Logger LOGGER = LogManager.getLogger();
    private static final Mapper MAPPER = new DozerBeanMapper();

    private final Cipher _cipher;
    private final KeyPair _keyPair;
    private final byte[] _encodedPublicKey;

	public LoginController() throws RemoteException, SecurityException {
		super();

        _cipher = SecurityModule.getNewRSACipher();
        _keyPair = SecurityModule.generateNewRSAKeyPair(512);
        _encodedPublicKey = SecurityModule.getEncodedRSAPublicKey(_keyPair);
	}

    @Override
    public byte[] getServerPublicKey()
    throws RemoteException {
        return _encodedPublicKey;
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

            _cipher.init(Cipher.DECRYPT_MODE, _keyPair.getPrivate());

            //Decrypting username
            String username = new String(
                _cipher.doFinal(authorisationDTO.getEncryptedUserLogin())
            );

            //Decrypting password
            String password = new String(
                _cipher.doFinal(authorisationDTO.getEncryptedUserPassword())
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

            //Preparing client fingerprint
            PublicKey clientKey = SecurityModule.getDecodedRSAPublicKey(authorisationDTO.getClientPublicKey());

            _cipher.init(Cipher.ENCRYPT_MODE, clientKey);

            byte[] sessionId = UUID.randomUUID().toString().getBytes();
            byte[] clientFingerprint = _cipher.doFinal(sessionId);

            //Sending session object for client
            return new SessionDTO()
                    .setMember(MAPPER.map(member, MemberDTO.class))
                    .setClientFingerprint(clientFingerprint);

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
            LOGGER.error("Error occurs while generating client fingerprint", e);
        }

        return null;
	}

	@Deprecated
	private UserRole getUserRole(String username) {

		/* Get the role of this user from our db */
		try{
			
			Member member = PersistenceFacade.getNewMemberDAO().findByUsername(username);
	
			if (member == null) return UserRole.UNSUCCESSFUL_LOGIN;

			/* Return according to user role */
			switch (member.getRole()) {
				case "admin": return UserRole.ADMIN;
				case "member": return UserRole.MEMBER;
				case "trainer": return UserRole.TRAINER;
				case "departmentHead": return UserRole.DEPARTMENT_HEAD;
				case "manager": return UserRole.MANAGER;
			}
			
		} catch(PersistenceException e) {
			LOGGER.error("Error occurred while getting/parsing user role.", e);
		} catch (TooManyResultsException e) {
            LOGGER.error("Too many authentication results were received.", e);
        }

        return UserRole.UNSUCCESSFUL_LOGIN;
	}
}