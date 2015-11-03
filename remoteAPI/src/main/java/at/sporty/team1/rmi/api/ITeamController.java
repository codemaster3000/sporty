package at.sporty.team1.rmi.api;

import at.sporty.team1.rmi.dtos.TeamDTO;
import at.sporty.team1.rmi.exceptions.ValidationException;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by sereGkaluv on 02-Nov-15.
 */
public interface ITeamController extends Remote, Serializable {
    void createOrSaveTeam(TeamDTO teamDTO) throws RemoteException, ValidationException;
}
