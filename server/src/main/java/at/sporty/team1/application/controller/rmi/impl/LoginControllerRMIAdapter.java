package at.sporty.team1.application.controller.rmi.impl;

import at.sporty.team1.application.controller.real.api.ILoginController;
import at.sporty.team1.application.controller.real.impl.LoginController;
import at.sporty.team1.shared.api.rmi.ILoginControllerRMI;
import at.sporty.team1.shared.dtos.AuthorisationDTO;
import at.sporty.team1.shared.dtos.SessionDTO;
import at.sporty.team1.shared.exceptions.SecurityException;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by sereGkaluv on 10-Dec-15.
 */
public class LoginControllerRMIAdapter extends UnicastRemoteObject implements ILoginControllerRMI {
    private static final long serialVersionUID = 1L;
    private final ILoginController _controller;

    public LoginControllerRMIAdapter()
    throws RemoteException {
        super();

        _controller = new LoginController();
    }

    @Override
    public byte[] getServerPublicKey()
    throws RemoteException {

        return _controller.getServerPublicKey();
    }

    @Override
    public SessionDTO authorize(AuthorisationDTO authorisationDTO)
    throws RemoteException, SecurityException {

        return _controller.authorize(authorisationDTO);
    }
}
