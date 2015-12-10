package at.sporty.team1.application.controller.real.api;


import at.sporty.team1.shared.dtos.*;
import at.sporty.team1.shared.exceptions.NotAuthorisedException;
import at.sporty.team1.shared.exceptions.UnknownEntityException;
import at.sporty.team1.shared.exceptions.ValidationException;

import java.util.List;

public interface IMemberController extends IController {

    /**
     * Search for Member with a given id.
     *
     * @param memberId target member (will be used for search).
     * @param session Session object.
     * @return MemberDTO searched member.
     * @throws NotAuthorisedException
     */
    MemberDTO findMemberById(Integer memberId, SessionDTO session)
    throws UnknownEntityException, NotAuthorisedException;

    /**
     * Search for all Members.
     *
     * @param isFeePaid will search all members who paid the fee if value = true and not paid if = false.
     * @param session Session object.
     * @return List<MemberDTO> List of all members.
     * @throws NotAuthorisedException
     */
    List<MemberDTO> searchAllMembers(Boolean isFeePaid, SessionDTO session)
    throws NotAuthorisedException;

    /**
     * Creates new or saves old member in data storage with data from the DTO.
     *
     * @param memberDTO DTO for member creation or save.
     * @param session Session object.
     * @return Integer Id of the updated or saved entity.
     * @throws ValidationException
     * @throws NotAuthorisedException
     */
    Integer createOrSaveMember(MemberDTO memberDTO, SessionDTO session)
    throws ValidationException, NotAuthorisedException;

    /**
     * Search for memberList by String (first name and last name, first name or last name).
     *
     * @param searchString String to be searched.
     * @param isFeePaid will search all members who paid the fee if value = true and not paid if = false.
     * @param session Session object.
     * @return List<MemberDTO> List of all members who's full name matched given data, or null.
     * @throws ValidationException
     * @throws NotAuthorisedException
     */
    List<MemberDTO> searchMembersByNameString(String searchString, Boolean isFeePaid, SessionDTO session)
    throws ValidationException, NotAuthorisedException;


    /**
     * Search for memberList by common team name.
     *
     * @param teamName Team name to be searched.
     * @param isFeePaid will search all members who paid the fee if value = true and not paid if = false.
     * @param session Session object.
     * @return List<MemberDTO> List of all members who are assigned to the given team, or null.
     * @throws ValidationException
     * @throws NotAuthorisedException
     */
    List<MemberDTO> searchMembersByCommonTeamName(String teamName, Boolean isFeePaid, SessionDTO session)
    throws ValidationException, NotAuthorisedException;

    /**
     * Search for memberList by tournament team name.
     *
     * @param teamName Team name to be searched.
     * @param isFeePaid will search all members who paid the fee if value = true and not paid if = false.
     * @param session Session object.
     * @return List<MemberDTO> List of all members who are assigned to the given team, or null.
     * @throws ValidationException
     * @throws NotAuthorisedException
     */
    List<MemberDTO> searchMembersByTournamentTeamName(String teamName, Boolean isFeePaid, SessionDTO session)
    throws ValidationException, NotAuthorisedException;


    /**
     * Search for memberList by date of birth.
     *
     * @param dateOfBirth Date of birth to be searched.
     * @param isFeePaid will search all members who paid the fee if value = true and not paid if = false.
     * @param session Session object.
     * @return List<MemberDTO> List of all members who's date of birth matched given data, or null.
     * @throws ValidationException
     * @throws NotAuthorisedException
     */
    List<MemberDTO> searchMembersByDateOfBirth(String dateOfBirth, Boolean isFeePaid, SessionDTO session)
    throws ValidationException, NotAuthorisedException;

    /**
     * Returns a fetched list of all departments and teams to which given member is assigned.
     *
     * @param memberId target member (will be used for search).
     * @param session Session object.
     * @return List<DTOPair> Fetched list of all departments ad teams to which given member is assigned.
     * @throws UnknownEntityException
     */
    List<DTOPair<DepartmentDTO, TeamDTO>> loadFetchedDepartmentTeamList(Integer memberId, SessionDTO session)
    throws UnknownEntityException, NotAuthorisedException;

    /**
     * Returns a list of all departments to which given member is assigned.
     *
     * @param memberId target member (will be used for search).
     * @param session Session object.
     * @return List<DepartmentDTO> List of all departments to which given member is assigned.
     * @throws UnknownEntityException
     * @throws NotAuthorisedException
     */
    List<DepartmentDTO> loadMemberDepartments(Integer memberId, SessionDTO session)
    throws UnknownEntityException, NotAuthorisedException;

    /**
     * Assigns member to selected department.
     *
     * @param memberId target member (will be used to update departments list).
     * @param departmentId department to which member with given id will be assigned.
     * @param session Session object.
     * @throws UnknownEntityException
     * @throws NotAuthorisedException
     */
    void assignMemberToDepartment(Integer memberId, Integer departmentId, SessionDTO session)
    throws UnknownEntityException, NotAuthorisedException;

    /**
     * Removes member from selected department.
     *
     * @param memberId target member (will be used to update departments list).
     * @param departmentId department from which member with given id will be removed.
     * @param session Session object.
     * @throws UnknownEntityException
     * @throws NotAuthorisedException
     */
    void removeMemberFromDepartment(Integer memberId, Integer departmentId, SessionDTO session)
    throws UnknownEntityException, NotAuthorisedException;

    /**
     * Returns a list of all teams to which given member is assigned.
     *
     * @param memberId target member (will be used for search).
     * @param session Session object.
     * @return List<TeamDTO> List of all teams to which given member is assigned.
     * @throws UnknownEntityException
     * @throws NotAuthorisedException
     */
    List<TeamDTO> loadMemberTeams(Integer memberId, SessionDTO session)
    throws UnknownEntityException, NotAuthorisedException;

    /**
     * Assigns member to selected team.
     *
     * @param memberId target member (will be used to update teams list).
     * @param teamId team to which member with given id will be assigned.
     * @param session Session object.
     * @throws UnknownEntityException
     * @throws NotAuthorisedException
     */
    void assignMemberToTeam(Integer memberId, Integer teamId, SessionDTO session)
    throws UnknownEntityException, NotAuthorisedException;

    /**
     * Removes member from selected team.
     *
     * @param memberId target member (will be used to update teams list).
     * @param teamId team from which member with given id will be removed.
     * @param session Session object.
     * @throws UnknownEntityException
     * @throws NotAuthorisedException
     */
    void removeMemberFromTeam(Integer memberId, Integer teamId, SessionDTO session)
    throws UnknownEntityException, NotAuthorisedException;

    /**
     * Deletes member form the data storage with data from the DTO.
     *
     * @param memberId Id of a member who will be deleted.
     * @param session Session object.
     * @throws UnknownEntityException
     * @throws NotAuthorisedException
     */
    void deleteMember(Integer memberId, SessionDTO session)
    throws UnknownEntityException, NotAuthorisedException;
}
