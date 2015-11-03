package at.sporty.team1.persistence.daos;

import at.sporty.team1.domain.Department;

/**
 * Created by f00 on 30.10.15.
 */
public class DepartmentDAO extends HibernateGenericDAO<Department> {

    /**
     * Constructor
     */
    public DepartmentDAO() {
        super(Department.class);
    }

}
