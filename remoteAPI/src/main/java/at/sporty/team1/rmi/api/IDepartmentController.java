package at.sporty.team1.rmi.api;

import at.sporty.team1.rmi.dtos.DepartmentDTO;
import at.sporty.team1.rmi.dtos.TeamDTO;
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
     */
    List<TeamDTO> loadDepartmentTeams(Integer departmentId)
    throws RemoteException, UnknownEntityException;
}
