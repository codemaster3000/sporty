package at.sporty.team1.persistence.daos;

import at.sporty.team1.persistence.GenericDAO;
import org.hibernate.criterion.Criterion;

import java.util.List;
import java.util.Map;

/**
 * Created by f00 on 27.10.15.
 */
public class MemberDAO extends GenericDAO {


    @Override
    public List getAll() {
        return null;
    }

    @Override
    public Object get(String id) {
        return null;
    }

    @Override
    public void save(Object o) {

    }

    @Override
    public void delete(Object o) {

    }

    @Override
    public List findByCriteria(Criterion... criterion) {
        return null;
    }

    @Override
    public List findByHQL(String hql) {
        return null;
    }

    @Override
    public List findByHQL(String hql, Map map) {
        return null;
    }
}
