package at.sporty.team1.application.controller;

import at.sporty.team1.domain.Member;
import at.sporty.team1.misc.InputSanitizer;
import at.sporty.team1.persistence.PersistenceFacade;
import at.sporty.team1.rmi.api.ILoginController;
import at.sporty.team1.rmi.enums.UserRole;
import at.sporty.team1.rmi.exceptions.DataType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.naming.AuthenticationException;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
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
    private static LoginController _loginController;

    public LoginController() throws RemoteException {
        super();
    }

    /**
     * ***************************************************************************************************
     *
     * @return Enum to distinguish which default screen to load; FALSE_LOGIN if not authorized
     * **************************************************************************************************
     * @brief checks if a login is valid bye comparing the login information to the database
     * if the login is valid it prompts the default screen associated with the employees class
     * if the login is invalid it logs the failed login attempt and prompts the loginscreen again
     *
     * FALSE_LOGIN false
     * ADMIN
     * MEMBER
     * TRAINER
     * DEPARTMENT_HEAD
     * MANAGER
     * .....
     * @param[in] username Users Username
     * @param[in] password Users Password
     */
    @Override
    public UserRole authorize(String username, String password) throws RemoteException {

        InputSanitizer sanitizer = new InputSanitizer();
        /* check if username and password are given and the format of this strings is OK */
        if (!username.equals("") && !password.equals("") &&
                sanitizer.isValid(username, DataType.USERNAME)
                && sanitizer.isValid(password, DataType.PASSWORD)) {

            try {

                Hashtable<String, String> env = new Hashtable<>();
                env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
                env.put(Context.PROVIDER_URL, "ldaps://ldap.fhv.at:636/dc=uclv,dc=net");
                env.put(Context.SECURITY_AUTHENTICATION, "simple");
                env.put(Context.SECURITY_PRINCIPAL, "uid=" + username + ", ou=fhv, ou=People, dc=uclv, dc=net");
                env.put(Context.SECURITY_CREDENTIALS, password);

                try {
                    /* the next line tries to login to LDAP */
                    DirContext ctx = new InitialDirContext(env);
                    Attributes attrs = ctx.getAttributes("userid=007,ou=staff,o=mi6");
                } catch (AuthenticationException ae) {
                    /* login unsuccessful */
                    LOGGER.error("Invalid login attempt by " + username + " with password " + password);
                    return UserRole.FALSE_LOGIN;
                }

                /* IF no AuthenticationException is thrown, login was successfull */

            } catch (NamingException ne) {
                ne.printStackTrace();
                return UserRole.FALSE_LOGIN;
            }

            List<Member> members;
            String role;

            /* get the role of this user from our db */
            try {
                members = PersistenceFacade.getNewMemberDAO().findByUsername(username);
                role = members.get(0).getRole();

                LOGGER.info("Login by: " + username);

                /* return according to userrole */
                if (role.equals("admin"))
                    return UserRole.ADMIN;

                if (role.equals("member"))
                    return UserRole.MEMBER;

                if (role.equals("trainer"))
                    return UserRole.TRAINER;

                if (role.equals("departmentHead"))
                    return UserRole.DEPARTMENT_HEAD;

                if (role.equals("manager"))
                    return UserRole.MANAGER;

            } catch (PersistenceException pe) {
                pe.printStackTrace();
                return UserRole.FALSE_LOGIN;
            }

        } /* pw or username do not match InputSanitizers check.. */

        LOGGER.warn("Login attempt failed: Inputsanitizercheck went bad");
        // user "testuser"
        // password "testpw" as sha512 hex-string:
        // F4A92ED38B74B373E60B16176A8E19CA0220CD21BF73E46E68C74C0CA77A8CBA3F6738B264000D894F7EFF5CA17F8CDD01C7BEB2CCC2BA2553987C01DF152729

        return UserRole.FALSE_LOGIN;

    }
}