package at.sporty.team1.persistence.daos;

import at.sporty.team1.persistence.HibernateSessionUtil;
import at.sporty.team1.persistence.api.IGenericDAO;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.criterion.Criterion;

import javax.persistence.PersistenceException;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class HibernateGenericDAO<T> implements IGenericDAO<T> {

    protected final Class<T> _domainClass;

    public HibernateGenericDAO(Class<T> domainClass) {
        _domainClass = domainClass;
    }

    @SuppressWarnings("unchecked") //NON Generic Hibernate method.
    @Override
    public List<T> findAll() throws PersistenceException {
        try {
            return (List<T>) HibernateSessionUtil.getInstance().makeSimpleTransaction(session -> {
                return session.createCriteria(_domainClass).list();
            });
        } catch (HibernateException e) {
            throw new PersistenceException(e);
        }
    }

    @SuppressWarnings("unchecked") //NON Generic Hibernate method.
    @Override
    public List<T> findByCriteria(Criterion... criterion) throws PersistenceException {
        return (List<T>) HibernateSessionUtil.getInstance().makeSimpleTransaction(session -> {
            Criteria criteria = session.createCriteria(_domainClass);
            for (Criterion c : criterion) {
                criteria.add(c);
            }
            return criteria.list();
        });
    }

    @Override
    public List<T> findByHQL(String hql) {
        return findByHQL(hql, null);
    }

    @SuppressWarnings("unchecked") //NON Generic Hibernate method.
    @Override
    public List<T> findByHQL(String hql, Map<?, ?> map) {
        return (List<T>) HibernateSessionUtil.getInstance().makeSimpleTransaction(session -> {
            Query query = session.createQuery(hql);
            if (map != null) {
                query.setProperties(map);
            }
            return query.list();
        });
    }

    @SuppressWarnings("unchecked") //NON Generic Hibernate method.
    @Override
    public T findById(Serializable id) throws PersistenceException {
        try {
            List<T> results = (List<T>) HibernateSessionUtil.getInstance().makeSimpleTransaction(session ->
                (T) session.get(_domainClass, id)
            );

            if (results != null && results.size() == 1) {
                return results.get(0);
            }
            throw new PersistenceException("Many results in find by id. (" + id + ")");

        } catch (HibernateException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public void refreshToActualState(T object) throws PersistenceException {
        try {
            HibernateSessionUtil.getInstance().makeSimpleTransaction(session -> {
                session.refresh(object);
            });
        } catch (HibernateException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public void saveOrUpdate(T object) throws PersistenceException {
        try {
            HibernateSessionUtil.getInstance().makeSimpleTransaction(session -> {
                session.saveOrUpdate(object);
            });
        } catch (HibernateException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public void delete(T object) throws PersistenceException {
        try {
            HibernateSessionUtil.getInstance().makeSimpleTransaction(session -> {
                session.delete(object);
            });
        } catch (HibernateException e) {
            throw new PersistenceException(e);
        }
    }
}