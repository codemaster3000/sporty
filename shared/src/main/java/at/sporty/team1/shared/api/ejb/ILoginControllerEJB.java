package at.sporty.team1.shared.api.ejb;

import at.sporty.team1.shared.dtos.AuthorisationDTO;
import at.sporty.team1.shared.dtos.SessionDTO;
import at.sporty.team1.shared.exceptions.SecurityException;

import javax.ejb.Local;
import javax.ejb.Remote;

/**
 * Created by f00 on 10.12.15.
 */
@Remote
public interface ILoginControllerEJB extends IRemoteControllerEJB {

    /**
     * Returns server public key required for authorisation.
     *
     * @return byte[] server public key.
     */
    byte[] getServerPublicKey();

    /**
     * Checks if received user credentials are equal to the
     * credentials that are stored in an auth system.
     *
     * @param authorisationDTO dto object for user credentials and client's public key in encrypted form.
     * @return session object encrypted with client's public key.
     * @throws SecurityException
     */
    SessionDTO authorize(AuthorisationDTO authorisationDTO)
    throws SecurityException;
}
