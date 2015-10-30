package at.sporty.team1.communication;

import at.sporty.team1.rmi.api.IMemberController;
import at.sporty.team1.rmi.RemoteObject;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class CommunicationFacade {
    private static final String DEFAULT_RMI = "rmi://localhost/%s";

    private CommunicationFacade() {
    }

    public static IMemberController lookupForMemberController(RemoteObject rObj)
    throws RemoteException, NotBoundException, MalformedURLException {
        return (IMemberController) Naming.lookup(String.format(DEFAULT_RMI, rObj.getNaming()));
    }
}
