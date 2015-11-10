package at.sporty.team1.rmi.api;

import at.sporty.team1.rmi.dtos.TeamDTO;
import at.sporty.team1.rmi.exceptions.ValidationException;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * Created by sereGkaluv on 02-Nov-15.
 */
public interface ITeamController extends Remote, Serializable {

    /**
     * Creates new or saves old team in data storage with data from the DTO.
     *
     * @param teamDTO DTO for team creation or save
     * @throws RemoteException
     * @throws ValidationException
     */
    void createOrSaveTeam(TeamDTO teamDTO) throws RemoteException, ValidationException;

    /**
     * Search for teamList by sport.
     *
     * @param sport Team name to be searched.
     * @return List<TeamDTO> List of all teams who are assigned to the given sport, or null.
     * @throws RemoteException
     */
    List<TeamDTO> searchBySport(String sport) throws RemoteException;
}
