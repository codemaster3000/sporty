package at.sporty.team1.executable;

import java.rmi.RemoteException;

/**
 * Created by sereGkaluv on 26-Oct-15.
 */

public class LocateRegistry {
    public static void main(String[] args) {
        try {
            java.rmi.registry.LocateRegistry.createRegistry(1099);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
