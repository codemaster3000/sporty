package at.sporty.team1.persistence.hibernate;

import at.sporty.team1.persistence.api.IGenericDao;

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