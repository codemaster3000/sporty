package at.sporty.team1.persistence.api;

import at.sporty.team1.domain.Department;
import at.sporty.team1.domain.Team;

import javax.persistence.PersistenceException;
import java.util.List;

/**
 * Created by sereGkaluv on 06-Nov-15.
 */
public interface ITeamDAO extends IGenericDAO<Team> {

    /**
     * Find team(s) by name.
     *
     * @param teamName name of the team to be searched
     * @return List<Team>
     * @throws PersistenceException
     */
    List<Team> findTeamsByName(String teamName)
    throws PersistenceException;

    /**
     * Find team(s) by Department.
     *
     * @param department department will be used to return all teams that are assigned to it
     * @return List<Team>
     * @throws PersistenceException
     */
    List<Team> findTeamsByDepartment(Department department)
    throws PersistenceException;

    /**
     * Find team(s) by Member.
     *
     * @param memberId id of the member to be searched
     * @return List<Team>
     * @throws PersistenceException
     */
    List<Team> findTeamsByMemberId(Integer memberId)
    throws PersistenceException;
}
