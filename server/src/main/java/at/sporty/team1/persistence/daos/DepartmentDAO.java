package at.sporty.team1.persistence.daos;

import at.sporty.team1.domain.Department;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

import java.util.List;

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

    public List<Department> getDepartmentBySport(String sport) {
        Criterion criterion;

        criterion = Restrictions.or(Restrictions.like("sport", sport, MatchMode.ANYWHERE));

        return super.findByCriteria(criterion);
    }

}
