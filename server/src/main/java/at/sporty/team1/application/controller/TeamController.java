package at.sporty.team1.application.controller;

import at.sporty.team1.domain.Team;
import at.sporty.team1.domain.interfaces.ITeam;
import at.sporty.team1.rmi.dtos.MemberDTO;
import at.sporty.team1.rmi.exceptions.DataType;
import at.sporty.team1.misc.InputSanitizer;
import at.sporty.team1.persistence.PersistenceFacade;
import at.sporty.team1.rmi.api.ITeamController;
import at.sporty.team1.rmi.dtos.TeamDTO;
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
            LOGGER.error("Error occurs while communicating with DB.", e);
        }
    }

    @Override
    public List<TeamDTO> searchBySport(String sport)
    throws RemoteException {

        if (sport == null) return null;

        /* Is valid, moving forward */
        try {
             /* pulling a TeamDAO and save the Team */
            List<? extends ITeam> rawResults = PersistenceFacade.getNewTeamDAO().findTeamsBySport(sport);

            //Converting results to MemberDTO
            return rawResults.stream()
                    .map(team -> MAPPER.map(team, TeamDTO.class))
                    .collect(Collectors.toList());

        } catch (PersistenceException e) {
            LOGGER.error("An error occurs while searching for \"{}\".", sport, e);
            return null;
        }
    }
}
