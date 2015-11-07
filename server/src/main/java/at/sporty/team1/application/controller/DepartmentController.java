package at.sporty.team1.application.controller;

import at.sporty.team1.domain.Department;
import at.sporty.team1.domain.readonly.IRDepartment;
import at.sporty.team1.rmi.exceptions.DataType;
import at.sporty.team1.misc.InputSanitizer;
import at.sporty.team1.persistence.PersistenceFacade;
import at.sporty.team1.rmi.api.IDepartmentController;
import at.sporty.team1.rmi.dtos.DepartmentDTO;
import at.sporty.team1.rmi.exceptions.ValidationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.persistence.PersistenceException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by f00 on 03.11.15.
 */
public class DepartmentController extends UnicastRemoteObject implements  IDepartmentController {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = LogManager.getLogger();

    /**
     * Constructor
     *
     * @throws RemoteException
     */
    public DepartmentController() throws RemoteException {
        super();
    }

    /**
     * Create or Save a Department
     *
     * @param departmentDTO
     * @throws RemoteException
     * @throws ValidationException
     */
    @Override
    public void createOrSaveDepartment(DepartmentDTO departmentDTO)
    throws RemoteException, ValidationException {

        if (departmentDTO == null) return;

        /* Validating Input */     //TODO isValid IDs?
        InputSanitizer inputSanitizer = new InputSanitizer();
        if (!inputSanitizer.isValid(departmentDTO.getSport(), DataType.TEXT)) {
            throw inputSanitizer.getPreparedValidationException();
        }

        /* Is valid, moving forward */
        try {
             /* pulling a DepartmentDAO and saving the Department */
            PersistenceFacade.getNewGenericDAO(Department.class).saveOrUpdate(
                    convertDTOToDepartment(departmentDTO)
            );
            //TODO
            LOGGER.info("New Department \"{} {}\" was created.", departmentDTO.getSport(), departmentDTO.getDepartmentId());

        } catch (PersistenceException e) {
            LOGGER.error("Error occurs while communicating with DB.", e);
        }
    }


    /**
     * Load Department as DTO by departmentId
     */
    @Override
    public DepartmentDTO loadDepartmentById(int departmentId)
    throws RemoteException {
        //TODO DAO always returns LIST of member, not a single member !
        return convertDepartmentToDTO(PersistenceFacade.getNewGenericDAO(Department.class).findById(departmentId));
    }

    /**
     * A helping method, converts all Department objects to DepartmentDTO
     *
     * @param department Member to be converted to a DepartmentDTO
     * @return Department representation of the given Department
     */
    private static DepartmentDTO convertDepartmentToDTO(IRDepartment department) {
        if (department != null) {
            //TODO replace with Dozer mapping
//            return new DepartmentDTO()
//                    .setMemberId(department.getMemberId())
//                    .setFirstName(department.getFirstName())
//                    .setLastName(department.getLastName());
//
        }
        return null;
    }

    /**
     * Helping Method. Converts a DTO to a *real* Domain Object
     *
     * @param departmentDTO
     * @return
     */
    private static Department convertDTOToDepartment(DepartmentDTO departmentDTO) {
        if (departmentDTO != null) {
            Department department = new Department();

            department.setDepartmentId(departmentDTO.getDepartmentId());
            department.setSport(departmentDTO.getSport());
            department.setDepartmentHeadId(departmentDTO.getDepartmentHeadId());

            return department;
        }
        return null;
    }
}
