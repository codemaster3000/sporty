package at.sporty.team1.persistence.api;

import at.sporty.team1.domain.Tournament;

import javax.persistence.PersistenceException;
import java.util.List;

/**
 * Created by f00 on 16.11.15.
 */
public interface ITournamentDAO extends IGenericDAO<Tournament> {

    /**
     * Find tournament(s) by sport (Department).
     *
     * @param sport sport that will be used to return all tournaments assigned to it.
     * @return List<Tournament> List of all tournaments that are assigned to the given sport.
     * @throws PersistenceException
     */
    List<Tournament> findBySport(String sport)
    throws PersistenceException;

    /**
     * Find team(s) by Department.
     *
     * @param eventDate date of the event that will be used to return all tournaments assigned to it
     * @return List<Tournament> List of all tournaments that are assigned to the given date of the event.
     * @throws PersistenceException
     */
    List<Tournament> findByEventDate(String eventDate)
    throws PersistenceException;

    /**
     * Find team(s) by Department.
     *
     * @param location location that will be used to return all tournaments assigned to it
     * @return List<Tournament> List of all tournaments that are assigned to the given location.
     * @throws PersistenceException
     */
    List<Tournament> findByLocation(String location)
    throws PersistenceException;
}
