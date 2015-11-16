package at.sporty.team1.application.controller;

import at.sporty.team1.misc.InputSanitizer;
import at.sporty.team1.rmi.api.ILoginController;
import at.sporty.team1.rmi.exceptions.DataType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Hashtable;


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
     * 1 undefiedUserrole1
     * 2 userrole3
     * .....
     * @param[in] username Users Username
     * @param[in] password Users Password
     */
    @Override
    public int authorize(String username, String password)
            throws RemoteException {

        InputSanitizer sanitizer = new InputSanitizer();
        /* check if username and password are given and the format of this strings is OK */
        if (!username.equals("") && !password.equals("") &&
                sanitizer.isValid(username, DataType.USERNAME)
                && sanitizer.isValid(password, DataType.PASSWORD)) {

            LOGGER.info("Login by: " + username);

            try {
                //TODO LDAP!
                Hashtable<String, String> env = new Hashtable<>();
                env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
                env.put(Context.PROVIDER_URL, "ldaps://ldap.fhv.at:636/dc=uclv,dc=net");
                env.put(Context.SECURITY_AUTHENTICATION, "simple");
                env.put(Context.SECURITY_PRINCIPAL, "uid=" + username + ", ou=fhv, ou=People, dc=uclv, dc=net");
                env.put(Context.SECURITY_CREDENTIALS, password);

                DirContext ctx = new InitialDirContext(env);
                Attributes attrs = ctx.getAttributes("userid=007,ou=staff,o=mi6");

                /* TODO handle the login */
//                if(/*login successfull */) {
//                    return 1;
//                }
//                if(/*login unsucessfull */) {
//                    LOGGER.error("Invalid login attempt by "+ username+ " with password "+password);
//                    /* return -1 -> not authorized */
//                    return -1;
//                }

                /* meanwhile: always successfull */
                return 1;

            } catch (NamingException ne) {
                ne.printStackTrace();
            }


        }


        // user "testuser"
        // password "testpw" as sha512 hex-string:
        // F4A92ED38B74B373E60B16176A8E19CA0220CD21BF73E46E68C74C0CA77A8CBA3F6738B264000D894F7EFF5CA17F8CDD01C7BEB2CCC2BA2553987C01DF152729

////            String userpwHash = Crypto.bytesToHex(Crypto.sha512(password));
//
//            MemberDAO memberDAO = new MemberDAO();
//            List<Member> user = null;
//
////            try {
////                user = memberDAO.findByName(username, true);
////            } catch (SQLException e) {
////                LOGGER.info("{} caused while logging in.", e.toString(), e);
////            }
//
//            if (user == null)
//                return -1;

        //There is no password field in our DB anymore

//            String databasepwHash = user.get(0).getPassword();
//
//            if (password.equals(databasepwHash)) {
//                LOGGER.info("User's Password does match");
//
//                // UserRight doc = new UserRight();
//                // Collection<UserRole> users = user.get(0).getUserRoles();
//
//
//                if (false/* Admin*/)
//                    return 1;
//                if (false/*Userrole2*/)
//                    return 0;
//                if (false/*Uerrole3*/)
//                    return 3;
//                // meanwhile return 0 (sportler view)
//                return 0;
//
//        } else {
//            LOGGER.warn("Login by: \"{}\" failed due to bad credentials.",  username);
//            // do nothing for a 2 second timespan
//        }

        return -1;

    }
}