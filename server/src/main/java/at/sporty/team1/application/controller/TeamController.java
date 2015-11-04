package at.sporty.team1.application.controller;

import at.sporty.team1.domain.Team;
import at.sporty.team1.domain.readonly.IRTeam;
import at.sporty.team1.misc.DataType;
import at.sporty.team1.misc.InputSanitizer;
import at.sporty.team1.persistence.PersistenceFacade;
import at.sporty.team1.rmi.api.ITeamController;
import at.sporty.team1.rmi.dtos.TeamDTO;
import at.sporty.team1.rmi.exceptions.ValidationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.persistence.PersistenceException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by sereGkaluv on 27-Oct-15.
 */
public class TeamController extends UnicastRemoteObject implements ITeamController{
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = LogManager.getLogger();

    public TeamController() throws RemoteException {
        super();
    }


    @Override
    public void createOrSaveTeam(TeamDTO teamDTO)
    throws RemoteException, ValidationException {

        if (teamDTO == null) return;

        /* Validating Input */
        InputSanitizer inputSanitizer = new InputSanitizer();

        //TODO further validation
        if (inputSanitizer.check(teamDTO.getTeamName(), DataType.TEXT)) {

            try {
	             /* pulling a TeamDAO and save the Team */
                PersistenceFacade.getNewTeamDAO().saveOrUpdate(
                        convertDTOToTeam(teamDTO)
                );

                LOGGER.info("Team \"{}\" was successfully saved.", teamDTO.getTeamName());

            } catch (PersistenceException e) {
                LOGGER.error("Error occurs while communicating with DB.", e);
            }


        } else {
            // There has been bad Input, throw the Exception
            LOGGER.error("Wrong Input creating Team: {}", inputSanitizer.getLastFailedValidation());

            ValidationException validationException = new ValidationException();
            validationException.setReason(inputSanitizer.getLastFailedValidation());

            throw validationException;
        }
    }


    /**
     * A helping method, converts all Team objects to TeamDTO.
     *
     * @param team Team to be converted to a TeamDTO
     * @return TeamDTO representation of the given Team.
     */
    private static TeamDTO convertTeamToDTO (IRTeam team){
        if (team != null) {
            return new TeamDTO()
                .setTeamId(team.getTeamId())
                .setTrainerId(team.getTrainer().getMemberId())
                .setDepartmentId(team.getDepartment().getDepartmentId())
//                .setLeagueId(team.getLeague().getLeagueID())
                .setTeamName(team.getTeamName());
//                .setMemberList(List<Member> memberList);
        }
        return null;
    }

    /**
     * A helping method, converts all TeamDTO to Team objects.
     *
     * @param teamDTO TeamDTO to be converted to a Team
     * @return Team representation of the given TeamDTO.
     */
    private static Team convertDTOToTeam (TeamDTO teamDTO){
        if (teamDTO != null) {
            Team team = new Team();

            team.setTeamId(teamDTO.getTeamId());
//            team.setTrainer(teamDTO.getTrainerId());
//            team.setDepartment(teamDTO.getDepartmentId());
//            team.setLeague(teamDTO.getLeagueId());
            team.setTeamName(teamDTO.getTeamName());
//            team.setMemberList(teamDTO.getMemberList());

            return team;
        }
        return null;
    }
}
