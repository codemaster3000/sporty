package at.sporty.team1.rmi.api;

import at.sporty.team1.rmi.dtos.DepartmentDTO;
import at.sporty.team1.rmi.dtos.MemberDTO;
import at.sporty.team1.rmi.dtos.SessionDTO;
import at.sporty.team1.rmi.dtos.TeamDTO;
import at.sporty.team1.rmi.exceptions.NotAuthorisedException;
import at.sporty.team1.rmi.exceptions.UnknownEntityException;
import at.sporty.team1.rmi.exceptions.ValidationException;

import java.rmi.RemoteException;
import java.util.List;

/**
 * Created by sereGkaluv on 02-Nov-15.
 */
public interface ITeamController extends IRemoteController  {

    /**
     * Creates new or saves old team in data storage with data from the DTO.
     *
     * @param teamDTO DTO for team creation or save.
     * @param session Session object.
     * @return Integer Id of the updated or saved entity.
     * @throws RemoteException
     * @throws ValidationException
     * @throws NotAuthorisedException
     */
    Integer createOrSaveTeam(TeamDTO teamDTO, SessionDTO session)
    throws RemoteException, ValidationException, NotAuthorisedException;

    /**
     * Returns a list of all teams to which member with a given id is assigned.
     *
     * @param memberId id of the target member (will be used for search).
     * @param session Session object.
     * @return List<TeamDTO> List of all teams that contain given member in the member list, or null.
     * @throws RemoteException
     * @throws UnknownEntityException
     * @throws NotAuthorisedException
     */
    List<TeamDTO> searchTeamsByMember(Integer memberId, SessionDTO session)
    throws RemoteException, UnknownEntityException, NotAuthorisedException;

    /**
     * Returns a list of all members assigned to the given team.
     *
     * @param teamId target team (will be used for search).
     * @param session Session object.
     * @return List<MemberDTO> List of all members assigned to the given team.
     * @throws RemoteException
     * @throws UnknownEntityException
     * @throws NotAuthorisedException
     */
    List<MemberDTO> loadTeamMembers(Integer teamId, SessionDTO session)
    throws RemoteException, UnknownEntityException, NotAuthorisedException;

    /**
     * Returns a Department assigned to the given team.
     *
     * @param teamId target team (will be used for search).
     * @param session Session object.
     * @return DepartmentDTO Department assigned to the given team.
     * @throws RemoteException
     * @throws UnknownEntityException
     * @throws NotAuthorisedException
     */
    DepartmentDTO loadTeamDepartment(Integer teamId, SessionDTO session)
    throws RemoteException, UnknownEntityException, NotAuthorisedException;
}
