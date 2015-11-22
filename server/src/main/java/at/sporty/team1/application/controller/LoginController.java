package at.sporty.team1.application.controller;

import at.sporty.team1.domain.Member;
import at.sporty.team1.misc.InputSanitizer;
import at.sporty.team1.persistence.PersistenceFacade;
import at.sporty.team1.rmi.api.ILoginController;
import at.sporty.team1.rmi.enums.UserRole;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.naming.AuthenticationException;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.InitialDirContext;
import javax.persistence.PersistenceException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Hashtable;
import java.util.List;

/**
 * Represents a controller to handle the login.
 */
public class LoginController extends UnicastRemoteObject implements ILoginController {
	private static final Logger LOGGER = LogManager.getLogger();

	public LoginController() throws RemoteException {
		super();
	}

	/**
	 * *************************************************************************
	 * **************************
	 *
	 * @return Enum to distinguish which default screen to load;
	 *         UNSUCCESSFUL_LOGIN if not authorized *
	 * *************************************************************************
	 * ************************
	 * @brief checks if a login is valid by comparing the login information to
	 *        the database if the login is valid it prompts the default screen
	 *        associated with the employees class if the login is invalid it
	 *        logs the failed login attempt and prompts the loginscreen again
	 *        <p>
	 *        UNSUCCESSFUL_LOGIN false ADMIN MEMBER TRAINER DEPARTMENT_HEAD
	 *        MANAGER .....
	 * @param[in] username Users Username
	 * @param[in] password Users Password
	 */
	@Override
	public UserRole authorize(String username, String password) throws RemoteException {

		/*
		 * check if username and password are given and the format of this
		 * strings is OK
		 */
		if (InputSanitizer.isNullOrEmpty(username) && InputSanitizer.isNullOrEmpty(password)) {
			return UserRole.UNSUCCESSFUL_LOGIN;
		}
		
		try {

			Hashtable<String, String> env = new Hashtable<>();
			env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
			env.put(Context.PROVIDER_URL, "ldaps://ldap.fhv.at:636/dc=uclv,dc=net");
			env.put(Context.SECURITY_AUTHENTICATION, "simple");
			env.put(Context.SECURITY_PRINCIPAL, "uid=" + username + ",ou=fhv,ou=People,dc=uclv,dc=net");
			env.put(Context.SECURITY_CREDENTIALS, password);
			env.put(Context.SECURITY_PROTOCOL, "ssl"); //use SSL

			/* the next line tries to login to LDAP */
			InitialDirContext context = new InitialDirContext(env);
			
			LOGGER.info("Successful login of {}.", username);
			context.close();

			// get role of current user from database
			UserRole currentRole = getUserRole(username);
			if (currentRole != null) return currentRole;

		} catch (AuthenticationException e) {
			LOGGER.error("Invalid login attempt by {}.", username, e);
		} catch (NamingException e) {
			LOGGER.error("LDAP protocol communication error.");
			LOGGER.debug("LDAP protocol communication error.", e);
		}

		return UserRole.UNSUCCESSFUL_LOGIN;
	}

	private UserRole getUserRole(String username) {

		/* get the role of this user from our db */

		try{
			
			List<Member> members = PersistenceFacade.getNewMemberDAO().findByUsername(username);
	
			if (members == null || members.isEmpty())
				return UserRole.UNSUCCESSFUL_LOGIN;
	
			String role = members.get(0).getRole();
	
			/* return according to userrole */
			switch (role) {
				case "admin": return UserRole.ADMIN;
				case "member": return UserRole.MEMBER;
				case "trainer": return UserRole.TRAINER;
				case "departmentHead": return UserRole.DEPARTMENT_HEAD;
				case "manager": return UserRole.MANAGER;
			}
			
		} catch(PersistenceException e) {
			LOGGER.error("Error occurred while getting/parsing user role.", e);
		}

		return UserRole.UNSUCCESSFUL_LOGIN;
	}
}