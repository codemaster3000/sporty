package at.sporty.team1.shared.api.rmi;

import at.sporty.team1.shared.dtos.AuthorisationDTO;
import at.sporty.team1.shared.dtos.SessionDTO;
import at.sporty.team1.shared.exceptions.SecurityException;

import java.rmi.RemoteException;

/**
 * Created by f00 on 10.11.15.
 */
public interface ILoginController extends IRemoteController {

    /**
     * Returns server public key required for authorisation.
     *
     * @return byte[] server public key.
     * @throws RemoteException
     * @throws SecurityException
     */
    byte[] getServerPublicKey()
    throws RemoteException, SecurityException;

    /**
     * *************************************************************************
     * @return Enum to distinguish which default screen to load;
     *         UNSUCCESSFUL_LOGIN if not authorized *
     * *************************************************************************
     * @apiNote  checks if a login is valid by comparing the login information to
     *           the database if the login is valid it prompts the default screen
     *           associated with the employees class if the login is invalid it
     *           logs the failed login attempt and prompts the login screen again
     *           <p>
     *           UNSUCCESSFUL_LOGIN false ADMIN MEMBER TRAINER DEPARTMENT_HEAD
     *           MANAGER .....
     * @param authorisationDTO Contains user data in encrypted form
     */
    SessionDTO authorize(AuthorisationDTO authorisationDTO)
    throws RemoteException;
}
