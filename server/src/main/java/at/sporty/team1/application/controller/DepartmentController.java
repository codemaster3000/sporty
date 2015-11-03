package at.sporty.team1.application.controller;

import at.sporty.team1.domain.Department;
import at.sporty.team1.domain.readonly.IRDepartment;
import at.sporty.team1.misc.DataType;
import at.sporty.team1.misc.InputSanitizer;
import at.sporty.team1.persistence.PersistenceFacade;
import at.sporty.team1.rmi.dtos.DepartmentDTO;
import at.sporty.team1.rmi.dtos.MemberDTO;
import at.sporty.team1.rmi.exceptions.ValidationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.persistence.PersistenceException;
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

    /**
     * Create or Save a Department
     *
     * @param departmentDTO
     *
     * @throws RemoteException
     *
     * @throws ValidationException
     */
    @Override
    public void createOrSaveDepartment(DepartmentDTO departmentDTO)
            throws RemoteException, ValidationException {

        if (departmentDTO == null) return;

        /* Validating Input */
        InputSanitizer inputSanitizer = new InputSanitizer();

        //TODO
        if (inputSanitizer.check(departmentDTO.getFirstName(), DataType.NAME) &&
                inputSanitizer.check(departmentDTO.getLastName(), DataType.NAME) &&
                inputSanitizer.check(departmentDTO.getDateOfBirth(), DataType.SQL_DATE) &&
                inputSanitizer.check(departmentDTO.getEmail(), DataType.EMAIL) &&
                inputSanitizer.check(departmentDTO.getAddress(), DataType.ADDRESS) &&
                inputSanitizer.check(departmentDTO.getGender(), DataType.GENDER))
        {

            try {
	             /* pulling a DepartmentDAO and saving the Department */
                PersistenceFacade.getNewGenericDAO(Department.class).saveOrUpdate(
                        convertDTOToDepartment(departmentDTO)
                );
        //TODO
                LOGGER.info("New Department \"{} {}\" was created.", departmentDTO.getFirstName(), departmentDTO.getLastName());

            } catch (PersistenceException e) {
                LOGGER.error("Error occurs while communicating with DB.", e);
            }


        } else {
            // There has been bad Input, throw the Exception
            LOGGER.error("Wrong Input creating Member: {}", inputSanitizer.getLastFailedValidation());

            ValidationException validationException = new ValidationException();
            validationException.setReason(inputSanitizer.getLastFailedValidation());

            throw validationException;
        }


    }


    /**
     * Load Department as DTO by departmentId
     */
    @Override
    public DepartmentDTO loadDepartmentById(int departmentId)
            throws RemoteException {
        //TODO DAO always returns LIST of member, not a single member !
        return convertDepartmentToDTO(PersistenceFacade.getNewGenericDAO(Department.class
        ).findById(departmentId));
    }

    /**
     * A helping method, converts all Department objects to DepartmentDTO
     *
     * @param department Member to be converted to a DepartmentDTO
     *
     * @return Department representation of the given Department
     */
    private static MemberDTO convertDepartmentToDTO(IRDepartment department){
        if (department != null) {
            //TODO
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
     * @param memberDTO
     *
     * @return
     */
    private static Department convertDTOToDepartment(DepartmentDTO memberDTO){
        if (memberDTO != null) {
            Department department = new Department();

            //TODO
            department.setMemberId(memberDTO.getMemberId());
            department.setFirstName(memberDTO.getFirstName());
            department.setLastName(memberDTO.getLastName());
            department.setGender(memberDTO.getGender());
            department.setDateOfBirth(parseDate(memberDTO.getDateOfBirth()));
            department.setEmail(memberDTO.getEmail());
            department.setAddress(memberDTO.getAddress());
            department.setDepartment(memberDTO.getDepartment());
            department.setTeamId(memberDTO.getTeamId());
            department.setSquad(memberDTO.getSquad());
            department.setRole(memberDTO.getRole());
            department.setUsername(memberDTO.getUsername());

            return department;
        }
        return null;
    }
}
