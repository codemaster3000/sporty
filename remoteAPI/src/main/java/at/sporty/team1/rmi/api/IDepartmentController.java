package at.sporty.team1.rmi.api;

import at.sporty.team1.rmi.dtos.DepartmentDTO;
import at.sporty.team1.rmi.dtos.TeamDTO;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * Created by f00 on 03.11.15.
 */
public interface IDepartmentController extends Remote, Serializable {

    /**
     * Search for all Departments.
     *
     * @return List<DepartmentDTO> List of all departments
     * @throws RemoteException
     */
    List<DepartmentDTO> searchAllDepartments() throws RemoteException;
}
