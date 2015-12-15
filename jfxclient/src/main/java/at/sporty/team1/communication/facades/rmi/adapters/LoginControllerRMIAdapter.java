package at.sporty.team1.communication.facades.rmi.adapters;

import at.sporty.team1.communication.facades.api.ILoginControllerUniversal;
import at.sporty.team1.communication.util.RemoteCommunicationException;
import at.sporty.team1.shared.api.rmi.ILoginControllerRMI;
import at.sporty.team1.shared.dtos.AuthorisationDTO;
import at.sporty.team1.shared.dtos.SessionDTO;
import at.sporty.team1.shared.exceptions.SecurityException;

import java.rmi.RemoteException;

public class LoginControllerRMIAdapter implements ILoginControllerUniversal {

	private final ILoginControllerRMI _iLoginControllerRMI;
	
	public LoginControllerRMIAdapter(ILoginControllerRMI iLoginControllerRMI) {
		_iLoginControllerRMI = iLoginControllerRMI;
	}

	@Override
	public byte[] getServerPublicKey()
    throws RemoteCommunicationException {
		try {
			return _iLoginControllerRMI.getServerPublicKey();
		} catch (RemoteException e) {
			throw new RemoteCommunicationException(e);
		}
	}

	@Override
	public SessionDTO authorize(AuthorisationDTO authorisationDTO)
	throws RemoteCommunicationException, SecurityException {
		try {
			return _iLoginControllerRMI.authorize(authorisationDTO);
		} catch (RemoteException e) {
			throw new RemoteCommunicationException(e);
		}
	}
}
