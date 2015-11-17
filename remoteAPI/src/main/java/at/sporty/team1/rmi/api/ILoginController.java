package at.sporty.team1.rmi.api;

import at.sporty.team1.rmi.enums.UserRole;

import java.rmi.RemoteException;

/**
 * Created by f00 on 10.11.15.
 */
public interface ILoginController {
    UserRole authorize(String username, String password) throws RemoteException;
}
