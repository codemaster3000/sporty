package at.sporty.team1.persistence.api;

import at.sporty.team1.domain.Department;
import at.sporty.team1.domain.Member;
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

    /**
     * Checks if given trainer is a trainer of a given member.
     *
     * @param trainer trainer to be checked
     * @param memberId member to be checked
     * @return Boolean result of the check, true if is a trainer, false if not.
     * @throws PersistenceException
     */
    Boolean isTrainerOfMember(Member trainer, Integer memberId)
    throws PersistenceException;

    /**
     * Checks if given trainer is a trainer of a given team.
     *
     * @param trainer trainer to be checked
     * @param teamId team to be checked
     * @return Boolean result of the check, true if is a trainer, false if not.
     * @throws PersistenceException
     */
    Boolean isTrainerOfTeam(Member trainer, Integer teamId)
    throws PersistenceException;

    /**
     * Checks if given member is a member of a given team.
     *
     * @param member member to be checked
     * @param teamId team to be checked
     * @return Boolean result of the check, true if is a member, false if not.
     * @throws PersistenceException
     */
    Boolean isMemberOfTeam(Member member, Integer teamId)
    throws PersistenceException;
}
