package at.sporty.team1.application.controller;

import at.sporty.team1.domain.Member;
import at.sporty.team1.misc.InputSanitizer;
import at.sporty.team1.persistence.PersistenceFacade;
import at.sporty.team1.rmi.api.ILoginController;
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
     * @return Integer Code to distinguish which default screen to load; -1 if denied
     * **************************************************************************************************
     * @brief checks if a login is valid bye comparing the login information to the database
     * if the login is valid it prompts the default screen associated with the employees class
     * if the login is invalid it logs the failed login attempt and prompts the loginscreen again
     * <p>
     * -1 false
     * 0 admin
     * 1 member
     * 2 trainer
     * 3 departmentHead
     * 4 manager
     * .....
     * @param[in] username Users Username
     * @param[in] password Users Password
     */
    @Override
    public int authorize(String username, String password) throws RemoteException {

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
                    return -1;
                }

                /* IF no AuthenticationException is thrown, login was successfull */

            } catch (NamingException ne) {
                ne.printStackTrace();
                return -1;
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
                    return 0;

                if (role.equals("member"))
                    return 1;

                if (role.equals("trainer"))
                    return 2;

                if (role.equals("departmentHead"))
                    return 3;

                if (role.equals("manager"))
                    return 4;

            } catch (PersistenceException pe) {
                pe.printStackTrace();
                return -1;
            }

        } /* pw or username do not match InputSanitizers check.. */

        LOGGER.warn("Login attempt failed: Inputsanitizercheck went bad");
        // user "testuser"
        // password "testpw" as sha512 hex-string:
        // F4A92ED38B74B373E60B16176A8E19CA0220CD21BF73E46E68C74C0CA77A8CBA3F6738B264000D894F7EFF5CA17F8CDD01C7BEB2CCC2BA2553987C01DF152729

        return -1;

    }
}