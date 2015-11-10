package at.sporty.team1.application.controller;

import at.sporty.team1.domain.Member;
import at.sporty.team1.persistence.daos.MemberDAO;
import at.sporty.team1.rmi.api.ILoginController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;


/**
 * Represents a controller to handle the login.
 * TODO
 */
public class LoginController extends UnicastRemoteObject implements ILoginController {
    private static final Logger LOGGER = LogManager.getLogger();
    private static LoginController _loginController;

    public LoginController() throws RemoteException {
        super();
    }

    /**
     ****************************************************************************************************
     *  @brief   checks if a login is valid bye comparing the login information to the database
     *          if the login is valid it prompts the default screen associated with the employees class
     *          if the login is invalid it logs the failed login attempt and prompts the loginscreen again
     *
     *          -1 false
     *          0 admin
     *          1 undefiedUserrole1
     *          2 userrole3
     *          .....
     *
     * @param[in] username Users Username
     * @param[in] password Users Password
     *
     * @return Integer Code to distinguish which default screen to load; -1 if denied
     ****************************************************************************************************/
    @Override
    public int authorize(String username, String password)
    throws RemoteException {
//        InputSanitizer sanitizer = new InputSanitizer();
//
//        if (!username.equals("") && !password.equals("") &&
//                sanitizer.isValid(username, InputSanitizer.DataType.username)
//                && sanitizer.isValid(password, InputSanitizer.DataType.password)) {

            LOGGER.info("Login by: " + username);

            // user "testuser"
            // password "testpw" as sha512 hex-string:
            // F4A92ED38B74B373E60B16176A8E19CA0220CD21BF73E46E68C74C0CA77A8CBA3F6738B264000D894F7EFF5CA17F8CDD01C7BEB2CCC2BA2553987C01DF152729

//            String userpwHash = Crypto.bytesToHex(Crypto.sha512(password));

            MemberDAO memberDAO = new MemberDAO();
            List<Member> user = null;

//            try {
//                user = memberDAO.findByName(username, true); // TODO
//            } catch (SQLException e) {
//                LOGGER.info("{} caused while logging in.", e.toString(), e);
//            }

            if (user == null)
                return -1;

            //There is no password field in our DB anymore

//            String databasepwHash = user.get(0).getPassword();
//
//            if (password.equals(databasepwHash)) {
//                LOGGER.info("User's Password does match");
//
//                // UserRight doc = new UserRight();
//                // Collection<UserRole> users = user.get(0).getUserRoles();
//
//                // TODO Userroles
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