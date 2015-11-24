package at.sporty.team1.rmi.api;


import at.sporty.team1.rmi.dtos.DepartmentDTO;
import at.sporty.team1.rmi.dtos.MemberDTO;
import at.sporty.team1.rmi.dtos.TeamDTO;
import at.sporty.team1.rmi.exceptions.UnknownEntityException;
import at.sporty.team1.rmi.exceptions.ValidationException;

import java.rmi.RemoteException;
import java.util.List;

public interface IMemberController extends IRemoteController {

    /**
     * Search for all Members.
     *
     * @param feePaid will search all members who paid the fee.
     * @param feeNotPaid will search all members who did not pay the fee.
     * @return List<MemberDTO> List of all members.
     * @throws RemoteException
     */
    List<MemberDTO> searchAllMembers(boolean feePaid, boolean feeNotPaid)
    throws RemoteException;

    /**
     * Creates new or saves old member in data storage with data from the DTO.
     *
     * @param memberDTO DTO for member creation or save.
     * @return Integer Id of the updated or saved entity.
     * @throws RemoteException
     * @throws ValidationException
     */
    Integer createOrSaveMember(MemberDTO memberDTO)
    throws RemoteException, ValidationException;

    /**
     * Search for memberList by String (first name and last name, first name or last name).
     *
     * @param searchString String to be searched.
     * @param feePaid will search all members who paid the fee.
     * @param feeNotPaid will search all members who did not pay the fee.
     * @return List<MemberDTO> List of all members who's full name matched given data, or null.
     * @throws RemoteException
     * @throws ValidationException
     */
    List<MemberDTO> searchMembersByNameString(String searchString, boolean feePaid, boolean feeNotPaid)
    throws RemoteException, ValidationException;


    /**
     * Search for memberList by common team name.
     *
     * @param teamName Team name to be searched.
     * @param feePaid will search all members who paid the fee.
     * @param feeNotPaid will search all members who did not pay the fee.
     * @return List<MemberDTO> List of all members who are assigned to the given team, or null.
     * @throws RemoteException
     * @throws ValidationException
     */
    List<MemberDTO> searchMembersByCommonTeamName(String teamName, boolean feePaid, boolean feeNotPaid)
    throws RemoteException, ValidationException;

    /**
     * Search for memberList by tournament team name.
     *
     * @param teamName Team name to be searched.
     * @param feePaid will search all members who paid the fee.
     * @param feeNotPaid will search all members who did not pay the fee.
     * @return List<MemberDTO> List of all members who are assigned to the given team, or null.
     * @throws RemoteException
     * @throws ValidationException
     */
    List<MemberDTO> searchMembersByTournamentTeamName(String teamName, boolean feePaid, boolean feeNotPaid)
    throws RemoteException, ValidationException;


    /**
     * Search for memberList by date of birth.
     *
     * @param dateOfBirth Date of birth to be searched.
     * @param feePaid will search all members who paid the fee.
     * @param feeNotPaid will search all members who did not pay the fee.
     * @return List<MemberDTO> List of all members who's date of birth matched given data, or null.
     * @throws RemoteException
     * @throws ValidationException
     */
    List<MemberDTO> searchMembersByDateOfBirth(String dateOfBirth, boolean feePaid, boolean feeNotPaid)
    throws RemoteException, ValidationException;

    /**
     * Returns a list of all departments to which given member is assigned.
     *
     * @param memberId target member (will be used for search).
     * @return List<DepartmentDTO> List of all departments to which given member is assigned.
     * @throws RemoteException
     * @throws UnknownEntityException
     */
    List<DepartmentDTO> loadMemberDepartments(Integer memberId)
    throws RemoteException, UnknownEntityException;

    /**
     * Assigns member to selected department.
     *
     * @param memberId target member (will be used to update departments list).
     * @param departmentId department to which member with given id will be assigned.
     * @throws RemoteException
     * @throws UnknownEntityException
     */
    void assignMemberToDepartment(Integer memberId, Integer departmentId)
    throws RemoteException, UnknownEntityException;

    /**
     * Removes member from selected department.
     *
     * @param memberId target member (will be used to update departments list).
     * @param departmentId department from which member with given id will be removed.
     * @throws RemoteException
     * @throws UnknownEntityException
     */
    void removeMemberFromDepartment(Integer memberId, Integer departmentId)
    throws RemoteException, UnknownEntityException;

    /**
     * Returns a list of all teams to which given member is assigned.
     *
     * @param memberId target member (will be used for search).
     * @return List<TeamDTO> List of all teams to which given member is assigned.
     * @throws RemoteException
     * @throws UnknownEntityException
     */
    List<TeamDTO> loadMemberTeams(Integer memberId)
    throws RemoteException, UnknownEntityException;

    /**
     * Assigns member to selected team.
     *
     * @param memberId target member (will be used to update teams list).
     * @param teamId team to which member with given id will be assigned.
     * @throws RemoteException
     * @throws UnknownEntityException
     */
    void assignMemberToTeam(Integer memberId, Integer teamId)
    throws RemoteException, UnknownEntityException;

    /**
     * Removes member from selected team.
     *
     * @param memberId target member (will be used to update teams list).
     * @param teamId team from which member with given id will be removed.
     * @throws RemoteException
     * @throws UnknownEntityException
     */
    void removeMemberFromTeam(Integer memberId, Integer teamId)
    throws RemoteException, UnknownEntityException;

    /**
     * Deletes member form the data storage with data from the DTO.
     *
     * @param memberId Id of a member who will be deleted.
     * @throws RemoteException
     * @throws UnknownEntityException
     */
    void deleteMember(Integer memberId)
    throws RemoteException, UnknownEntityException;
}
