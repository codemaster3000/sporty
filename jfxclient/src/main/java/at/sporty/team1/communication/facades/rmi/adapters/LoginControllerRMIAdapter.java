package at.sporty.team1.communication.facades.rmi.adapters;

import at.sporty.team1.communication.facades.api.ILoginControllerUniversal;
import at.sporty.team1.communication.util.RemoteCommunicationException;
import at.sporty.team1.shared.api.rmi.ILoginControllerRMI;
import at.sporty.team1.shared.dtos.AuthorisationDTO;
import at.sporty.team1.shared.dtos.SessionDTO;
import at.sporty.team1.shared.exceptions.SecurityException;

public class LoginControllerRMIAdapter implements ILoginControllerUniversal {

	private final ILoginControllerRMI _iLoginControllerRMI;
	
	public LoginControllerRMIAdapter(ILoginControllerRMI iLoginControllerRMI) {
		_iLoginControllerRMI = iLoginControllerRMI;
	}
	@Override
	public byte[] getServerPublicKey() throws RemoteCommunicationException {
		
		return _iLoginControllerRMI.getServerPublicKey();
	}

	@Override
	public SessionDTO authorize(AuthorisationDTO authorisationDTO)
			throws RemoteCommunicationException, SecurityException {
		return _iLoginControllerRMI.authorize(authorisationDTO);
	}
}
