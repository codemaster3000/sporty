package at.sporty.team1.persistence.api;

import at.sporty.team1.domain.Department;
import at.sporty.team1.domain.Member;

import javax.persistence.PersistenceException;

/**
 * Created by sereGkaluv on 09-Nov-15.
 */
public interface IDepartmentDAO extends IGenericDAO<Department> {

    /**
     * Find department by sport.
     *
     * @param sport name of the given team
     * @return Department Department assigned to given sport.
     * @throws PersistenceException
     */
    Department findBySport(String sport)
    throws PersistenceException;

    /**
     * Checks if given member is a head of a given department.
     *
     * @param member member to be checked
     * @param departmentId department to be checked
     * @return Boolean result of the check, true if is a head, false if not.
     * @throws PersistenceException
     */
    Boolean isDepartmentHead(Member member, Integer departmentId)
    throws PersistenceException;

    /**
     * Checks if given department head is a department head of a given member.
     *
     * @param departmentHead departmentHead to be checked
     * @param memberId member to be checked
     * @return Boolean result of the check, true if is a department head, false if not.
     * @throws PersistenceException
     */
    Boolean isDepartmentHeadOfMember(Member departmentHead, Integer memberId)
    throws PersistenceException;

    /**
     * Checks if given department head is a department head of a given team.
     *
     * @param departmentHead departmentHead to be checked
     * @param teamId team to be checked
     * @return Boolean result of the check, true if is a department head, false if not.
     * @throws PersistenceException
     */
    Boolean isDepartmentHeadOfTeam(Member departmentHead, Integer teamId)
    throws PersistenceException;

    /**
     * Checks if given department head is a department head of a given tournament.
     *
     * @param departmentHead departmentHead to be checked
     * @param tournamentId tournament to be checked
     * @return Boolean result of the check, true if is a department head, false if not.
     * @throws PersistenceException
     */
    Boolean isDepartmentHeadOfTournament(Member departmentHead, Integer tournamentId)
    throws PersistenceException;
}
