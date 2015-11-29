package at.sporty.team1.rmi.api;

import at.sporty.team1.rmi.dtos.DepartmentDTO;
import at.sporty.team1.rmi.dtos.MemberDTO;
import at.sporty.team1.rmi.dtos.SessionDTO;
import at.sporty.team1.rmi.dtos.TeamDTO;
import at.sporty.team1.rmi.exceptions.NotAuthorisedException;
import at.sporty.team1.rmi.exceptions.UnknownEntityException;

import java.rmi.RemoteException;
import java.util.List;

/**
 * Created by f00 on 03.11.15.
 */
public interface IDepartmentController extends IRemoteController {

    /**
     * Search for all Departments.
     *
     * @return List<DepartmentDTO> List of all departments
     * @throws RemoteException
     */
    List<DepartmentDTO> searchAllDepartments()
    throws RemoteException;

    /**
     * Returns a list of all teams assigned to the given department.
     *
     * @param departmentId target department (will be used for search)
     * @return List<TeamDTO> List of all teams
     * @throws RemoteException
     * @throws UnknownEntityException
     */
    List<TeamDTO> loadDepartmentTeams(Integer departmentId)
    throws RemoteException, UnknownEntityException;

    /**
     * Returns a MemberDTO (department head) assigned to the given department.
     *
     * @param departmentId target department (will be used for search)
     * @param session Session object.
     * @return MemberDTO department head, assigned to the given department.
     * @throws RemoteException
     * @throws UnknownEntityException
     * @throws NotAuthorisedException
     */
    MemberDTO loadDepartmentHead(Integer departmentId, SessionDTO session)
    throws RemoteException, UnknownEntityException, NotAuthorisedException;
}
