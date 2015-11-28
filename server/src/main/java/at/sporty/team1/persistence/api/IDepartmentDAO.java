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
}
