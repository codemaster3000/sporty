package at.sporty.team1.persistence.daos;

import at.sporty.team1.domain.Department;
import at.sporty.team1.persistence.api.IDepartmentDAO;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

import javax.persistence.PersistenceException;
import java.util.List;

/**
 * Created by f00 on 30.10.15.
 */
public class DepartmentDAO extends HibernateGenericDAO<Department> implements IDepartmentDAO {

    private static final String PROP_SPORT = "sport";

    /**
     * Creates a new department DAO.
     */
    public DepartmentDAO() {
        super(Department.class);
    }

    @Override
    public Department findBySport(String sport)
    throws PersistenceException {

        List<Department> dept = findByCriteria(Restrictions.like(PROP_SPORT, sport, MatchMode.ANYWHERE));
        if (dept != null && dept.size() == 1) {
            return dept.get(0);
        }
        throw new PersistenceException("Unexpected results amount (size > 1), while searching department by sport.");
    }
}
