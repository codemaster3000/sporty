package at.sporty.team1.persistence.api;

import at.sporty.team1.domain.Match;
import at.sporty.team1.domain.Tournament;

import javax.persistence.PersistenceException;
import java.util.List;

/**
 * Created by sereGkaluv on 17.11.15.
 */
public interface IMatchDAO extends IGenericDAO<Match>{

    /**
     * Find by name tournament.
     *
     * @param tournament tournament from which all the matches will be returned
     * @return List<Match> List of all Matches that are assigned to the given tournament.
     * @throws PersistenceException
     */
    List<Match> findByTournament(Tournament tournament) throws PersistenceException;
}
