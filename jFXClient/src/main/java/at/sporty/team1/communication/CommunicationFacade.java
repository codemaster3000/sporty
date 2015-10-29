package at.sporty.team1.communication;

import at.sporty.team1.rmi.stubs.CommunicationStub;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public class CommunicationFacade {
    private static final String DEFAULT_RMI = "rmi://localhost/%s";

    private CommunicationFacade() {
    }

    @SuppressWarnings("unchecked")
    public static <T extends Remote> T lookupForStub(CommunicationStub stub)
    throws RemoteException, NotBoundException, MalformedURLException {
        return (T) Naming.lookup(String.format(DEFAULT_RMI, stub.getNaming()));
    }
}
