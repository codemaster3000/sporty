package at.sporty.team1.application.controller;

import at.sporty.team1.rmi.api.ITeamController;
import at.sporty.team1.rmi.dtos.TeamDTO;
import at.sporty.team1.rmi.exceptions.ValidationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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

    }
}
