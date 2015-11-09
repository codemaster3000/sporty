package at.sporty.team1.persistence.api;

import at.sporty.team1.domain.Department;

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
    Department findBySport(String sport) throws PersistenceException;
}
