package at.sporty.team1.application.controller.real;

import at.sporty.team1.application.auth.AccessPolicy;
import at.sporty.team1.application.auth.BasicAccessPolicies;
import at.sporty.team1.application.controller.real.api.ITeamController;
import at.sporty.team1.domain.Department;
import at.sporty.team1.domain.Team;
import at.sporty.team1.domain.interfaces.IMember;
import at.sporty.team1.domain.interfaces.ITeam;
import at.sporty.team1.misc.InputSanitizer;
import at.sporty.team1.persistence.PersistenceFacade;
import at.sporty.team1.shared.dtos.DepartmentDTO;
import at.sporty.team1.shared.dtos.MemberDTO;
import at.sporty.team1.shared.dtos.SessionDTO;
import at.sporty.team1.shared.dtos.TeamDTO;
import at.sporty.team1.shared.enums.DataType;
import at.sporty.team1.shared.enums.UserRole;
import at.sporty.team1.shared.exceptions.NotAuthorisedException;
import at.sporty.team1.shared.exceptions.UnknownEntityException;
import at.sporty.team1.shared.exceptions.ValidationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;

import javax.persistence.PersistenceException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by sereGkaluv on 27-Oct-15.
 */
public class TeamController implements ITeamController {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = LogManager.getLogger();
    private static final Mapper MAPPER = new DozerBeanMapper();

    public TeamController() {
    }

    @Override
    public Integer createOrSaveTeam(TeamDTO teamDTO, SessionDTO session)
    throws ValidationException, NotAuthorisedException {

        /* Checking access permissions */
        //1 STEP
        if (teamDTO == null) throw new NotAuthorisedException();

        //2 STEP
        if (!LoginController.hasEnoughPermissions(
            session,
            AccessPolicy.or(
                BasicAccessPolicies.isInPermissionBound(UserRole.ADMIN),

                AccessPolicy.and(
                    BasicAccessPolicies.isInPermissionBound(UserRole.TRAINER),

                    AccessPolicy.or(
                        //create new team (new teams doesn't have id))
                        AccessPolicy.simplePolicy(user -> teamDTO.getTeamId() == null),
                        BasicAccessPolicies.isTrainerOfTeam(teamDTO.getTeamId()),
                        BasicAccessPolicies.isDepartmentHeadOfTeam(teamDTO.getTeamId())
                    )
                )
            )
        )) throw new NotAuthorisedException();

        /* Validating Input */
        InputSanitizer inputSanitizer = new InputSanitizer();
        if (!inputSanitizer.isValid(teamDTO.getTeamName(), DataType.TEXT)) {
            // There has been bad Input, throw the Exception
            throw inputSanitizer.getPreparedValidationException();
        }

        /* Is valid, moving forward */
        try {
             /* pulling a TeamDAO and save the Team */
            PersistenceFacade.getNewTeamDAO().saveOrUpdate(
                MAPPER.map(teamDTO, Team.class)
            );

            LOGGER.info("Team \"{}\" was successfully saved.", teamDTO.getTeamName());
            return teamDTO.getTeamId();
        } catch (PersistenceException e) {
            LOGGER.error("Error occurred while communicating with DB.", e);
            return null;
        }
    }

    @Override
    public List<TeamDTO> searchTeamsByMember(Integer memberId, SessionDTO session)
    throws UnknownEntityException, NotAuthorisedException {

        /* Checking access permissions */
        if (!LoginController.hasEnoughPermissions(
            session,
            AccessPolicy.or(
                BasicAccessPolicies.isInPermissionBound(UserRole.ADMIN),
                BasicAccessPolicies.isDepartmentHeadOfMember(memberId)
            )
        )) throw new NotAuthorisedException();

        /* Validating Input */
        if (memberId == null) throw new UnknownEntityException(IMember.class);

        /* Is valid, moving forward */
        try {

            /* pulling a TeamDAO and searching for all teams assigned to given Member */
            List<? extends ITeam> rawResults = PersistenceFacade.getNewTeamDAO().findTeamsByMemberId(memberId);

            //checking if there are any results
            if (rawResults == null || rawResults.isEmpty()) return null;

            //Converting results to TeamDTO
            return rawResults.stream()
                    .map(team -> MAPPER.map(team, TeamDTO.class))
                    .collect(Collectors.toList());

        } catch (PersistenceException e) {
            LOGGER.error(
                "An error occurred while searching for \"all Teams by Member #{}\".",
                memberId,
                e
            );
            return null;
        }
    }

    @Override
    public List<MemberDTO> loadTeamMembers(Integer teamId, SessionDTO session)
    throws UnknownEntityException, NotAuthorisedException {

        /* Checking access permissions */
        if (!LoginController.hasEnoughPermissions(
            session,
            AccessPolicy.or(
                BasicAccessPolicies.isInPermissionBound(UserRole.ADMIN),
                BasicAccessPolicies.isMemberOfTeam(teamId),
                BasicAccessPolicies.isTrainerOfTeam(teamId),
                BasicAccessPolicies.isDepartmentHeadOfTeam(teamId)
            )
        )) throw new NotAuthorisedException();

        /* Validating Input */
        if (teamId == null) throw new UnknownEntityException(ITeam.class);

        /* Is valid, moving forward */
        try {

            Team team = PersistenceFacade.getNewTeamDAO().findById(teamId);
            if (team == null) throw new UnknownEntityException(ITeam.class);

            //getting all members for this entity
            PersistenceFacade.forceLoadLazyProperty(team, Team::getMembers);
            List<? extends IMember> rawResults = team.getMembers();

            //checking if there are any results
            if (rawResults == null || rawResults.isEmpty()) return null;

            //Converting results to MemberDTO
            return rawResults.stream()
                    .map(member -> MAPPER.map(member, MemberDTO.class))
                    .collect(Collectors.toList());

        } catch (PersistenceException e) {
            LOGGER.error(
                "An error occurred while getting \"all Members for Team #{}\".",
                teamId,
                e
            );
            return null;
        }
    }

    @Override
    public DepartmentDTO loadTeamDepartment(Integer teamId, SessionDTO session)
    throws UnknownEntityException, NotAuthorisedException {

        /* Checking access permissions */
        if (!LoginController.hasEnoughPermissions(
            session,
            AccessPolicy.or(
                BasicAccessPolicies.isInPermissionBound(UserRole.ADMIN),
                BasicAccessPolicies.isMemberOfTeam(teamId),
                BasicAccessPolicies.isTrainerOfTeam(teamId),
                BasicAccessPolicies.isDepartmentHeadOfTeam(teamId)
            )
        )) throw new NotAuthorisedException();

        /* Validating Input */
        if (teamId == null) throw new UnknownEntityException(ITeam.class);

        /* Is valid, moving forward */
        try {

            Team team = PersistenceFacade.getNewTeamDAO().findById(teamId);
            if (team == null) throw new UnknownEntityException(ITeam.class);

            //getting all members for this entity
            PersistenceFacade.forceLoadLazyProperty(team, Team::getDepartment);
            Department department = team.getDepartment();

            //checking if there are any results
            if (department == null) return null;

            //Converting results to DepartmentDTO
            return MAPPER.map(department, DepartmentDTO.class);

        } catch (PersistenceException e) {
            LOGGER.error(
                "An error occurred while searching Department assigned to Team #{}.",
                teamId,
                e
            );
            return null;
        }
    }
}
