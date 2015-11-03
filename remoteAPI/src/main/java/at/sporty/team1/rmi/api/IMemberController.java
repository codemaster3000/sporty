package at.sporty.team1.rmi.api;


import at.sporty.team1.rmi.dtos.MemberDTO;
import at.sporty.team1.rmi.exceptions.ValidationException;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface IMemberController extends Remote, Serializable {
    void createOrSaveMember(MemberDTO memberDTO) throws RemoteException, ValidationException;

    MemberDTO loadMemberById(int memberId) throws RemoteException;

    List<MemberDTO> searchForMembers(String searchQuery) throws RemoteException;
}
