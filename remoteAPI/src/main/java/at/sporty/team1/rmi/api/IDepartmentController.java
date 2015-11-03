package at.sporty.team1.rmi.api;

import at.sporty.team1.rmi.dtos.DepartmentDTO;
import at.sporty.team1.rmi.exceptions.ValidationException;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by f00 on 03.11.15.
 */
public interface IDepartmentController extends Remote, Serializable {

    void createOrSaveDepartment(DepartmentDTO departmentDTO)
            throws RemoteException, ValidationException;

    DepartmentDTO loadDepartmentById(int departmentId)
                    throws RemoteException;
}
