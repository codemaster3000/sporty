package at.sporty.team1.application.controller.ejb;

import at.sporty.team1.application.controller.real.LoginController;
import at.sporty.team1.application.controller.real.api.ILoginController;
import at.sporty.team1.shared.api.ejb.ILoginControllerEJB;
import at.sporty.team1.shared.dtos.AuthorisationDTO;
import at.sporty.team1.shared.dtos.SessionDTO;
import at.sporty.team1.shared.exceptions.SecurityException;

import javax.ejb.Stateless;

/**
 * Created by f00 on 13.12.15.
 */
@Stateless(name = "LOGIN_CONTROLLER_EJB")
public class LoginControllerEJBAdapter implements ILoginControllerEJB {
    private static final long serialVersionUID = 1L;
    private final ILoginController _controller;

    public LoginControllerEJBAdapter() {
        super();
        _controller = new LoginController();
    }

    @Override
    public byte[] getServerPublicKey() {
        return _controller.getServerPublicKey();
    }

    @Override
    public SessionDTO authorize(AuthorisationDTO authorisationDTO)
            throws SecurityException {
        return _controller.authorize(authorisationDTO);
    }
}
