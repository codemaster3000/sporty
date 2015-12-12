package at.sporty.team1.communication.facades.api;

import at.sporty.team1.communication.util.RemoteCommunicationException;
import at.sporty.team1.shared.dtos.DepartmentDTO;
import at.sporty.team1.shared.dtos.MemberDTO;
import at.sporty.team1.shared.dtos.SessionDTO;
import at.sporty.team1.shared.dtos.TeamDTO;
import at.sporty.team1.shared.exceptions.NotAuthorisedException;
import at.sporty.team1.shared.exceptions.UnknownEntityException;
import at.sporty.team1.shared.exceptions.ValidationException;

import java.util.List;

/**
 * Created by sereGkaluv on 02-Nov-15.
 */
public interface ITeamControllerUniversal extends IControllerUniversal {

    /**
     * Creates new or saves old team in data storage with data from the DTO.
     *
     * @param teamDTO DTO for team creation or save.
     * @param session Session object.
     * @return Integer Id of the updated or saved entity.
     * @throws RemoteCommunicationException
     * @throws ValidationException
     * @throws NotAuthorisedException
     */
    Integer createOrSaveTeam(TeamDTO teamDTO, SessionDTO session)
    throws RemoteCommunicationException, ValidationException, NotAuthorisedException;

    /**
     * Returns a list of all teams to which member with a given id is assigned.
     *
     * @param memberId id of the target member (will be used for search).
     * @param session Session object.
     * @return List<TeamDTO> List of all teams that contain given member in the member list, or null.
     * @throws RemoteCommunicationException
     * @throws UnknownEntityException
     * @throws NotAuthorisedException
     */
    List<TeamDTO> searchTeamsByMember(Integer memberId, SessionDTO session)
    throws RemoteCommunicationException, UnknownEntityException, NotAuthorisedException;

    /**
     * Returns a list of all members assigned to the given team.
     *
     * @param teamId target team (will be used for search).
     * @param session Session object.
     * @return List<MemberDTO> List of all members assigned to the given team.
     * @throws RemoteCommunicationException
     * @throws UnknownEntityException
     * @throws NotAuthorisedException
     */
    List<MemberDTO> loadTeamMembers(Integer teamId, SessionDTO session)
    throws RemoteCommunicationException, UnknownEntityException, NotAuthorisedException;

    /**
     * Returns a Department assigned to the given team.
     *
     * @param teamId target team (will be used for search).
     * @param session Session object.
     * @return DepartmentDTO Department assigned to the given team.
     * @throws RemoteCommunicationException
     * @throws UnknownEntityException
     * @throws NotAuthorisedException
     */
    DepartmentDTO loadTeamDepartment(Integer teamId, SessionDTO session)
    throws RemoteCommunicationException, UnknownEntityException, NotAuthorisedException;
}
