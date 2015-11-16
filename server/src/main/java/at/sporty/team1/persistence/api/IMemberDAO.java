package at.sporty.team1.persistence.api;

import at.sporty.team1.domain.Member;

import javax.persistence.PersistenceException;
import java.util.List;

/**
 * Created by sereGkaluv on 27-Oct-15.
 */
public interface IMemberDAO extends IGenericDAO<Member> {

    /**
     * Find by name String.
     *
     * @param searchString first name and last name, first name or last Name
     * @return List<Member> List of all Members who's full name matched given data.
     * @throws PersistenceException
     */
    List<Member> findByNameString(String searchString) throws PersistenceException;

    /**
     * Find member by full name.
     *
     * @param firstName member's first name
     * @param lastName member's last name
     * @return List<Member> List of all Members who's full name matched given data.
     */
    List<Member> findByFullName(String firstName, String lastName) throws PersistenceException;

    /**
     * Find member by first name.
     *
     * @param firstName member's first name
     * @return List<Member> List of all Members who's first name matched given data.
     * @throws PersistenceException
     */
    List<Member> findByFirstName(String firstName) throws PersistenceException;

    /**
     * Find member by last name.
     *
     * @param lastName member's last name
     * @return List<Member> List of all Members who's last name matched given data.
     * @throws PersistenceException
     */
    List<Member> findByLastName(String lastName) throws PersistenceException;

    /**
     * Find member(s) by date of birth.
     *
     * @param dateOfBirth  member's date of birth (SQL_DATE format: yyyy-mm-dd)
     * @return List<Member> List of all Members who were born at the given date.
     * @throws PersistenceException
     */
    List<Member> findByDateOfBirth(String dateOfBirth) throws PersistenceException;

    /**
     * Find member(s) by email.
     *
     * @param email  member's email
     * @return List<Member> List of all Members who's email matched given data.
     * @throws PersistenceException
     */
    List<Member> findByEmail(String email) throws PersistenceException;

    /**
     * Find member(s) by team name.
     *
     * @param teamName name of the given team (common)
     * @return List<Member> List of all Members who are assigned to given team.
     * @throws PersistenceException
     */
	List<Member> findByCommonTeamName(String teamName) throws PersistenceException;

    /**
     * Find member(s) by team name.
     *
     * @param teamName name of the given team (tournament)
     * @return List<Member> List of all Members who are assigned to given team.
     * @throws PersistenceException
     */
    List<Member> findByTournamentTeamName(String teamName) throws PersistenceException;
}
