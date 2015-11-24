package at.sporty.team1.application.controller;

import at.sporty.team1.domain.Department;
import at.sporty.team1.domain.Member;
import at.sporty.team1.domain.Team;
import at.sporty.team1.domain.interfaces.IDepartment;
import at.sporty.team1.domain.interfaces.IMember;
import at.sporty.team1.domain.interfaces.ITeam;
import at.sporty.team1.misc.InputSanitizer;
import at.sporty.team1.persistence.PersistenceFacade;
import at.sporty.team1.rmi.api.ITeamController;
import at.sporty.team1.rmi.dtos.DepartmentDTO;
import at.sporty.team1.rmi.dtos.MemberDTO;
import at.sporty.team1.rmi.dtos.TeamDTO;
import at.sporty.team1.rmi.exceptions.DataType;
import at.sporty.team1.rmi.exceptions.UnknownEntityException;
import at.sporty.team1.rmi.exceptions.ValidationException;
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
 * Created by sereGkaluv on 27-Oct-15.
 */
public class TeamController extends UnicastRemoteObject implements ITeamController{
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = LogManager.getLogger();
    private static final Mapper MAPPER = new DozerBeanMapper();

    public TeamController() throws RemoteException {
        super();
    }

    @Override
    public void createOrSaveTeam(TeamDTO teamDTO)
    throws RemoteException, ValidationException {

        if (teamDTO == null) return;

        /* Validating Input */   //TODO further validation
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

        } catch (PersistenceException e) {
            LOGGER.error("Error occurred while communicating with DB.", e);
        }
    }

    @Override
    public List<TeamDTO> searchTeamsByMember(Integer memberId)
    throws RemoteException, UnknownEntityException {

        if (memberId == null) throw new UnknownEntityException(IMember.class);

        try {

            /* pulling a TeamDAO and searching for all teams assigned to given Member */
            List<? extends ITeam> rawResults = PersistenceFacade.getNewTeamDAO().findTeamsByMemberId(memberId);

            //checking if there are an results
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
    public List<MemberDTO> loadTeamMembers(Integer teamId)
    throws RemoteException, UnknownEntityException {

        if (teamId == null) throw new UnknownEntityException(ITeam.class);

        try {

            Team team = PersistenceFacade.getNewTeamDAO().findById(teamId);
            if (team == null) throw new UnknownEntityException(ITeam.class);

            //getting all members for this entity
            PersistenceFacade.forceLoadLazyProperty(team, Team::getMembers);
            List<? extends IMember> rawResults = team.getMembers();

            //checking if there are an results
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
    public DepartmentDTO loadTeamDepartment(Integer teamId)
    throws RemoteException, UnknownEntityException {
        try {

            if (teamId == null) throw new UnknownEntityException(ITeam.class);

            Team team = PersistenceFacade.getNewTeamDAO().findById(teamId);
            if (team == null) throw new UnknownEntityException(ITeam.class);

            //getting all members for this entity
            PersistenceFacade.forceLoadLazyProperty(team, Team::getDepartment);
            Department department = team.getDepartment();

            //checking if there are an results
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
