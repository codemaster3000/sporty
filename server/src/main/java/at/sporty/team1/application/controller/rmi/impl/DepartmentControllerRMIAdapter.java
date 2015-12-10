package at.sporty.team1.application.controller.rmi.impl;

import at.sporty.team1.shared.api.rmi.ILoginController;
import at.sporty.team1.shared.dtos.AuthorisationDTO;
import at.sporty.team1.shared.dtos.SessionDTO;
import at.sporty.team1.shared.exceptions.SecurityException;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by sereGkaluv on 10-Dec-15.
 */
public class DepartmentControllerRMIAdapter extends UnicastRemoteObject implements ILoginController {

    public DepartmentControllerRMIAdapter()
    throws RemoteException {
        super();
    }

    @Override
    public byte[] getServerPublicKey() throws RemoteException, SecurityException {
        return new byte[0];
    }

    @Override
    public SessionDTO authorize(AuthorisationDTO authorisationDTO) throws RemoteException {
        return null;
    }
}
