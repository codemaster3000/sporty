package at.sporty.team1.rmi.api;

import at.sporty.team1.rmi.enums.UserRole;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by f00 on 10.11.15.
 */
public interface ILoginController extends Remote, Serializable {
    UserRole authorize(String username, String password) throws RemoteException;
}
