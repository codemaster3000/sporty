package at.sporty.team1.communication.facades.api;

import at.sporty.team1.shared.exceptions.RemoteCommunicationException;
import at.sporty.team1.shared.dtos.DepartmentDTO;
import at.sporty.team1.shared.dtos.MemberDTO;
import at.sporty.team1.shared.dtos.SessionDTO;
import at.sporty.team1.shared.dtos.TeamDTO;
import at.sporty.team1.shared.exceptions.NotAuthorisedException;
import at.sporty.team1.shared.exceptions.UnknownEntityException;

import java.util.List;

/**
 * Created by f00 on 03.11.15.
 */
public interface IDepartmentControllerUniversal extends IControllerUniversal {

    /**
     * Search for all Departments.
     *
     * @return List<DepartmentDTO> List of all departments
     * @throws RemoteCommunicationException
     */
    List<DepartmentDTO> searchAllDepartments()
    throws RemoteCommunicationException;

    /**
     * Returns a list of all teams assigned to the given department.
     *
     * @param departmentId target department (will be used for search)
     * @return List<TeamDTO> List of all teams
     * @throws RemoteCommunicationException
     * @throws UnknownEntityException
     */
    List<TeamDTO> loadDepartmentTeams(Integer departmentId)
    throws RemoteCommunicationException, UnknownEntityException;

    /**
     * Returns a MemberDTO (department head) assigned to the given department.
     *
     * @param departmentId target department (will be used for search)
     * @param session Session object.
     * @return MemberDTO department head, assigned to the given department.
     * @throws RemoteCommunicationException
     * @throws UnknownEntityException
     * @throws NotAuthorisedException
     */
    MemberDTO loadDepartmentHead(Integer departmentId, SessionDTO session)
    throws RemoteCommunicationException, UnknownEntityException, NotAuthorisedException;
}
