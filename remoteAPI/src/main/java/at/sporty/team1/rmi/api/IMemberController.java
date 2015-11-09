package at.sporty.team1.rmi.api;


import at.sporty.team1.rmi.dtos.MemberDTO;
import at.sporty.team1.rmi.exceptions.ValidationException;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface IMemberController extends Remote, Serializable {

    /**
     * Creates new or saves old member in data storage with data from the DTO.
     *
     * @param memberDTO DTO for member creation or save
     * @throws RemoteException
     * @throws ValidationException
     */
    void createOrSaveMember(MemberDTO memberDTO) throws RemoteException, ValidationException;

    /**
     * Search for memberList by String (first name and last name, first name or last name).
     *
     * @param searchString String to be searched.
     * @return List<MemberDTO> List of all members who's full name matched given data, or null.
     * @throws RemoteException
     */
    List<MemberDTO> searchMembersByNameString(String searchString) throws RemoteException, ValidationException;


    /**
     * Search for memberList by team name.
     *
     * @param teamName Team name to be searched.
     * @return List<MemberDTO> List of all members who is assigned to the given team, or null.
     * @throws RemoteException
     */
    List<MemberDTO> searchMembersByTeamName(String teamName) throws RemoteException, ValidationException;


    /**
     * Search for memberList by date of birth.
     *
     * @param dateOfBirth Date of birth to be searched.
     * @return List<MemberDTO> List of all members who's date of birth matched given data, or null.
     * @throws RemoteException
     */
    List<MemberDTO> searchMembersByDateOfBirth(String dateOfBirth) throws RemoteException, ValidationException;

    /**
     * Deletes member form the data storage with data from the DTO.
     *
     * @param memberDTO DTO of the member who will be deleted
     * @throws RemoteException
     */
    void deleteMember(MemberDTO memberDTO) throws RemoteException;

    /**
     * Search for all Members.
     *
     * @param 
     * @return List<MemberDTO> List of all members 
     * @throws RemoteException
     */
	List<MemberDTO> searchAllMembers() throws RemoteException;
}
