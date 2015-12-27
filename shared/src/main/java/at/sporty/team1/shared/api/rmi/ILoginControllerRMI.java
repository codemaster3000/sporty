package at.sporty.team1.shared.api.rmi;

import at.sporty.team1.shared.dtos.AuthorisationDTO;
import at.sporty.team1.shared.dtos.SessionDTO;
import at.sporty.team1.shared.exceptions.SecurityException;

import java.rmi.RemoteException;

/**
 * Created by f00 on 10.11.15.
 */
public interface ILoginControllerRMI extends IRemoteControllerRMI {

    /**
     * Returns server public key required for authorisation.
     *
     * @return byte[] server public key.
     * @throws RemoteException
     */
    byte[] getServerPublicKey()
    throws RemoteException;

    /**
     * Checks if received user credentials are equal to the
     * credentials that are stored in an auth system.
     *
     * @param authorisationDTO dto object for user credentials and client's public key in encrypted form.
     * @return session object encrypted with client's public key.
     * @throws RemoteException
     * @throws SecurityException
     */
    SessionDTO authorize(AuthorisationDTO authorisationDTO)
    throws RemoteException, SecurityException;
}
