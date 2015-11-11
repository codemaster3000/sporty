package at.sporty.team1.application.controller;

import at.sporty.team1.domain.interfaces.IDepartment;
import at.sporty.team1.persistence.PersistenceFacade;
import at.sporty.team1.rmi.api.IDepartmentController;
import at.sporty.team1.rmi.dtos.DepartmentDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;

import javax.persistence.PersistenceException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by f00 on 03.11.15.
 */
public class DepartmentController extends UnicastRemoteObject implements IDepartmentController {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = LogManager.getLogger();
    private static final Mapper MAPPER = new DozerBeanMapper();

    public DepartmentController() throws RemoteException {
        super();
    }

    @Override
    public List<DepartmentDTO> searchAllDepartments()
    throws RemoteException {

        try {

            /* pulling a DepartmentDAO and searching for all departments */
            List<? extends IDepartment> rawResults = PersistenceFacade.getNewDepartmentDAO().findAll();

            //checking if there are an results
            if (rawResults == null || rawResults.isEmpty()) return null;

            //Converting results to DepartmentDTO
            return rawResults.stream()
                    .map(department -> MAPPER.map(department, DepartmentDTO.class))
                    .collect(Collectors.toList());

        } catch (PersistenceException e) {
            LOGGER.error("An error occurs while searching for \"all Departments\".", e);
            return null;
        }
    }
}
