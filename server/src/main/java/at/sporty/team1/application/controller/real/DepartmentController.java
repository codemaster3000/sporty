package at.sporty.team1.application.controller.real;

import at.sporty.team1.application.auth.BasicAccessPolicies;
import at.sporty.team1.shared.api.real.IDepartmentController;
import at.sporty.team1.domain.Department;
import at.sporty.team1.domain.Member;
import at.sporty.team1.domain.interfaces.IDepartment;
import at.sporty.team1.domain.interfaces.ITeam;
import at.sporty.team1.persistence.PersistenceFacade;
import at.sporty.team1.shared.dtos.DepartmentDTO;
import at.sporty.team1.shared.dtos.MemberDTO;
import at.sporty.team1.shared.dtos.SessionDTO;
import at.sporty.team1.shared.dtos.TeamDTO;
import at.sporty.team1.shared.enums.UserRole;
import at.sporty.team1.shared.exceptions.NotAuthorisedException;
import at.sporty.team1.shared.exceptions.UnknownEntityException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;

import javax.persistence.PersistenceException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by f00 on 03.11.15.
 */
public class DepartmentController implements IDepartmentController {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = LogManager.getLogger();
    private static final Mapper MAPPER = new DozerBeanMapper();

    public DepartmentController() {
    }

    @Override
    public List<DepartmentDTO> searchAllDepartments() {

        try {
            //pulling a DepartmentDAO and searching for all departments
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
    public List<TeamDTO> loadDepartmentTeams(Integer departmentId)
    throws UnknownEntityException {

        /* Validating Input */
        if (departmentId == null) throw new UnknownEntityException(IDepartment.class);

        /* Is valid, moving forward */
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
    throws UnknownEntityException, NotAuthorisedException {

        /* Checking access permissions */
        if (!LoginController.hasEnoughPermissions(
            session,
            BasicAccessPolicies.isInPermissionBound(UserRole.MEMBER)
        )) throw new NotAuthorisedException();

        /* Validating Input */
        if (departmentId == null) throw new UnknownEntityException(IDepartment.class);

        /* Is valid, moving forward */
        try {

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
