package at.sporty.team1.rmi.api;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IMemberController extends Remote {
    void createNewMember(
        String fName,
        String lName,
        String bday,
        String email,
        String phone,
        String address,
        String sport,
        String gender
    ) throws RemoteException;
}
