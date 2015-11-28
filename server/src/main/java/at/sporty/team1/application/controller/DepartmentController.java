package at.sporty.team1.application.controller;

import at.sporty.team1.domain.Department;
import at.sporty.team1.domain.Member;
import at.sporty.team1.domain.interfaces.IDepartment;
import at.sporty.team1.domain.interfaces.ITeam;
import at.sporty.team1.persistence.PersistenceFacade;
import at.sporty.team1.rmi.api.IDepartmentController;
import at.sporty.team1.rmi.dtos.DepartmentDTO;
import at.sporty.team1.rmi.dtos.MemberDTO;
import at.sporty.team1.rmi.dtos.SessionDTO;
import at.sporty.team1.rmi.dtos.TeamDTO;
import at.sporty.team1.rmi.enums.UserRole;
import at.sporty.team1.rmi.exceptions.NotAuthorisedException;
import at.sporty.team1.rmi.exceptions.UnknownEntityException;
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
    public List<DepartmentDTO> searchAllDepartments(SessionDTO session)
    throws RemoteException, NotAuthorisedException {

        if (!LoginController.hasEnoughPermissions(session, UserRole.MEMBER)) throw new NotAuthorisedException();

        try {

            /* pulling a DepartmentDAO and searching for all departments */
            List<? extends IDepartment> rawResults = PersistenceFacade.getNewDepartmentDAO().findAll();

            //checking if there are any results
            if (rawResults == null || rawResults.isEmpty()) return null;

            //Converting results to DepartmentDTO
            return rawResults.stream()
                    .map(department -> MAPPER.map(department, DepartmentDTO.class))
                    .collect(Collectors.toList());

        } catch (PersistenceException e) {
            LOGGER.error("An error occurred while searching for \"all Departments\".", e);
            return null;
        }
    }

    @Override
    public List<TeamDTO> loadDepartmentTeams(Integer departmentId, SessionDTO session)
    throws RemoteException, UnknownEntityException, NotAuthorisedException {

        if (!LoginController.hasEnoughPermissions(session, UserRole.MEMBER)) throw new NotAuthorisedException();

        if (departmentId == null) throw new UnknownEntityException(IDepartment.class);

        try {

            Department department = PersistenceFacade.getNewDepartmentDAO().findById(departmentId);
            if (department == null) throw new UnknownEntityException(IDepartment.class);

            //getting all members for this entity
            PersistenceFacade.forceLoadLazyProperty(department, Department::getTeams);
            List<? extends ITeam> rawResults = department.getTeams();

            //checking if there are any results
            if (rawResults == null || rawResults.isEmpty()) return null;

            //Converting results to MemberDTO
            return rawResults.stream()
                    .map(team -> MAPPER.map(team, TeamDTO.class))
                    .collect(Collectors.toList());

        } catch (PersistenceException e) {
            LOGGER.error(
                "An error occurred while getting \"all Teams for Department #{}\".",
                departmentId,
                e
            );
            return null;
        }
    }

    @Override
    public MemberDTO loadDepartmentHead(Integer departmentId, SessionDTO session)
    throws RemoteException, UnknownEntityException, NotAuthorisedException {
        try {

            if (!LoginController.hasEnoughPermissions(session, UserRole.MEMBER)) throw new NotAuthorisedException();

            if (departmentId == null) throw new UnknownEntityException(IDepartment.class);

            Department department = PersistenceFacade.getNewDepartmentDAO().findById(departmentId);
            if (department == null) throw new UnknownEntityException(IDepartment.class);

            //getting all members for this entity
            PersistenceFacade.forceLoadLazyProperty(department, Department::getDepartmentHead);
            Member departmentHead = department.getDepartmentHead();

            //checking if there are any results
            if (departmentHead == null) return null;

            //Converting results to MemberDTO
            return MAPPER.map(departmentHead, MemberDTO.class);

        } catch (PersistenceException e) {
            LOGGER.error(
                "An error occurred while searching department head by Department #{}.",
                departmentId,
                e
            );
            return null;
        }
    }
}
