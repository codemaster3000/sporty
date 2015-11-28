package at.sporty.team1.persistence.daos;

import at.sporty.team1.persistence.api.IGenericDAO;
import at.sporty.team1.persistence.util.HibernateSessionUtil;
import at.sporty.team1.persistence.util.PropertyPair;
import at.sporty.team1.persistence.util.TooManyResultsException;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Criterion;

import javax.persistence.PersistenceException;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class HibernateGenericDAO<T> implements IGenericDAO<T> {

    protected final Class<T> _domainClass;

    public HibernateGenericDAO(Class<T> domainClass) {
        _domainClass = domainClass;
    }

    @SuppressWarnings("unchecked") //NON Generic Hibernate method.
    @Override
    public List<T> findAll()
    throws PersistenceException {

        try {
            return (List<T>) HibernateSessionUtil.getInstance().makeSimpleTransaction(session -> {
                return session.createCriteria(_domainClass).list();
            });
        } catch (HibernateException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public T findSingleResultByCriteria(Criterion... criterion)
    throws TooManyResultsException {
        List<T> list = findByCriteria(criterion);
        if (list.isEmpty()) {
            return null;
        } else if (list.size() > 1) {
            throw new TooManyResultsException();
        }
        return list.get(0);
    }

    @SuppressWarnings("unchecked") //NON Generic Hibernate method.
    @Override
    public List<T> findByCriteria(Criterion... criterion)
    throws PersistenceException {

        return (List<T>) HibernateSessionUtil.getInstance().makeSimpleTransaction(session -> {
            Criteria criteria = session.createCriteria(_domainClass);
            for (Criterion c : criterion) {
                criteria.add(c);
            }
            return criteria.list();
        });
    }

    @SuppressWarnings("unchecked") //NON Generic Hibernate method.
    @Override
    public List<T> findByCriteriaWithAlias(Consumer<Criteria> aliasApplier, Criterion... criterion)
    throws PersistenceException {
        return (List<T>) HibernateSessionUtil.getInstance().makeSimpleTransaction(session -> {
            Criteria criteria = session.createCriteria(_domainClass);
            aliasApplier.accept(criteria);
            for (Criterion c : criterion) {
                criteria.add(c);
            }
            return criteria.list();
        });
    }

    @SuppressWarnings("unchecked") //NON Generic Hibernate method.
    @Override
    public List<T> findBySQLQuery(String sql, PropertyPair<?>... propertyPairs)
    throws PersistenceException {

        return (List<T>) HibernateSessionUtil.getInstance().makeSimpleTransaction(session -> {
            SQLQuery query = session.createSQLQuery(sql).addEntity(_domainClass);
            for (PropertyPair<?> pair : propertyPairs) {
                query.setParameter(pair.getProperty(), pair.getValue());
            }

            return query.list();
        });
    }

    @Override
    public List<T> findByHQL(String hql) throws PersistenceException {
        return findByHQL(hql, null);
    }

    @SuppressWarnings("unchecked") //NON Generic Hibernate method.
    @Override
    public List<T> findByHQL(String hql, Map<?, ?> map)
    throws PersistenceException {

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
    public T findById(Serializable id)
    throws PersistenceException {

        try {
            return HibernateSessionUtil.getInstance().makeSimpleTransaction(session ->
                (T) session.get(_domainClass, id)
            );
        } catch (HibernateException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public void refreshToActualState(T object)
    throws PersistenceException {

        try {
            HibernateSessionUtil.getInstance().makeSimpleTransaction(session -> {
                session.refresh(object);
            });
        } catch (HibernateException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public void saveOrUpdate(T object)
    throws PersistenceException {

        try {
            HibernateSessionUtil.getInstance().makeSimpleTransaction(session -> {
                session.saveOrUpdate(object);
            });
        } catch (HibernateException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public void delete(T object)
    throws PersistenceException {

        try {
            HibernateSessionUtil.getInstance().makeSimpleTransaction(session -> {
                session.delete(object);
            });
        } catch (HibernateException e) {
            throw new PersistenceException(e);
        }
    }
}