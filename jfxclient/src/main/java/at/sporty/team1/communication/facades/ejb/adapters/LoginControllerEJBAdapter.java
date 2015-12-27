package at.sporty.team1.communication.facades.ejb.adapters;

import at.sporty.team1.communication.facades.api.ILoginControllerUniversal;
import at.sporty.team1.shared.exceptions.RemoteCommunicationException;
import at.sporty.team1.shared.api.ejb.ILoginControllerEJB;
import at.sporty.team1.shared.dtos.AuthorisationDTO;
import at.sporty.team1.shared.dtos.SessionDTO;
import at.sporty.team1.shared.exceptions.SecurityException;

public class LoginControllerEJBAdapter implements ILoginControllerUniversal {

	private final ILoginControllerEJB _iLoginController;
	
	public LoginControllerEJBAdapter(ILoginControllerEJB iLoginControllerEJB) {
		_iLoginController = iLoginControllerEJB;
	}

	@Override
	public byte[] getServerPublicKey()
	throws RemoteCommunicationException {
		return _iLoginController.getServerPublicKey();
	}

	@Override
	public SessionDTO authorize(AuthorisationDTO authorisationDTO)
	throws RemoteCommunicationException, SecurityException {
		return _iLoginController.authorize(authorisationDTO);
	}
}
