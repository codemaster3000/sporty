package at.sporty.team1.application.controller;

import at.sporty.team1.domain.Team;
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

        /* Validating Input */
        InputSanitizer inputSanitizer = new InputSanitizer();

        //TODO further validation
        if (inputSanitizer.isValid(teamDTO.getTeamName(), DataType.TEXT)) {

            try {
	             /* pulling a TeamDAO and save the Team */
                PersistenceFacade.getNewTeamDAO().saveOrUpdate(
                    MAPPER.map(teamDTO, Team.class)
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
}
