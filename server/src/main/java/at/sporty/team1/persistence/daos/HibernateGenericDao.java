package at.sporty.team1.persistence.daos;

import at.sporty.team1.persistence.api.IGenericDao;
import org.hibernate.criterion.Criterion;

import javax.persistence.PersistenceException;
import java.io.Serializable;
import java.util.List;

public class HibernateGenericDao<T> implements IGenericDao<T> {

    protected final Class<T> _domainClass;

    public HibernateGenericDao(Class<T> domainClass) {
        _domainClass = domainClass;
    }

    @Override
    public List<T> findAll() throws PersistenceException {
        return null;
    }

    @Override
    public List<T> findByCriteria(Criterion... criterion) throws PersistenceException {
        return null;
    }

    @Override
    public T getById(Serializable id) throws PersistenceException {
        return null;
    }

    @Override
    public void refreshToActualState(T object) throws PersistenceException {

    }

    @Override
    public void saveOrUpdate(T object) throws PersistenceException {

    }

    @Override
    public void delete(T object) throws PersistenceException {

    }
}