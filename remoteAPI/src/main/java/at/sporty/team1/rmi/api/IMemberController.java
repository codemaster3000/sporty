package at.sporty.team1.rmi.api;

import at.sporty.team1.rmi.dtos.MemberDTO;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface IMemberController extends Remote {
    boolean createNewMember(MemberDTO memberDTO) throws RemoteException;

    MemberDTO loadMemberById(int memberId) throws RemoteException;

    List<MemberDTO> searchForMembers(String searchQuery) throws RemoteException;
}
