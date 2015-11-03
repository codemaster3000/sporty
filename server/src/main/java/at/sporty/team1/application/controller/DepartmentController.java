package at.sporty.team1.application.controller;

import at.sporty.team1.domain.Department;
import at.sporty.team1.persistence.PersistenceFacade;
import at.sporty.team1.rmi.dtos.MemberDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by f00 on 03.11.15.
 */
public class DepartmentController extends UnicastRemoteObject implements IDepartmentController {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = LogManager.getLogger();

    /**
     * Constructor
     * @throws RemoteException
     */
    public DepartmentController() throws RemoteException {
        super();
    }

    @Override
    public DepartmentDTO loadMemberById(int departmenId)
            throws RemoteException {
        //TODO DAO always returns LIST of member, not a single member !
        return convertDepartmentToDTO(PersistenceFacade.getNewGenericDAO(Department.class
        ).findById(departmenId));
    }
}
