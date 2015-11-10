package at.sporty.team1.rmi.api;

import java.rmi.RemoteException;

/**
 * Created by f00 on 10.11.15.
 */
public interface ILoginController {
    int authorize(String username, String password) throws RemoteException;
}
