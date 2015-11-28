package at.sporty.team1.persistence.api;

import at.sporty.team1.persistence.util.PropertyPair;
import at.sporty.team1.persistence.util.TooManyResultsException;
import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;

import javax.persistence.PersistenceException;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public interface IGenericDAO<T> {
    /**
     * Returns all existing objects of the given type in the data store.
     * @return a list of all objects of the type {@code T}.
     * @throws PersistenceException
     */
    List<T> findAll()
    throws PersistenceException;

    /**
     * Returns a specific object identified according to the specified criterion(s) from the data store.
     * @param criterion criterion to be matched.
     * @return Object of the type {@code T} that match the given criterion(s), or null if n results,
     * in case if more than one result will be found, this method will throw TooManyResultsException.
     * @throws PersistenceException
     * @throws TooManyResultsException
     */
    T findSingleResultByCriteria(Criterion... criterion)
    throws PersistenceException, TooManyResultsException;

    /**
     * Returns all existing objects of the given type in the data store that
     * match the given criterion(s).
     * @param criterion criterion to be matched.
     * @return a list of all objects of the type {@code T} that match the
     * given criterion(s).
     * @throws PersistenceException
     */
    List<T> findByCriteria(Criterion... criterion)
    throws PersistenceException;

    /**
     * Returns all existing objects of the given type in the data store that
     * match the given criterion(s).
     * @param aliasApplier applies aliases to the end criteria.
     * @param criterion criterion to be matched.
     * @return a list of all objects of the type {@code T} that match the
     * given criterion(s).
     * @throws PersistenceException
     */
    List<T> findByCriteriaWithAlias(Consumer<Criteria> aliasApplier, Criterion... criterion)
    throws PersistenceException;

    /**
     * Returns a list of objects that are matched by a given sql query.
     * @param sql The sql query with named parameters.
     * @param propertyPairs for sql query.
     * @return The found objects.
     * @throws PersistenceException
     */
    List<T> findBySQLQuery(String sql, PropertyPair<?>... propertyPairs)
    throws PersistenceException;

    /**
     * Returns a list of objects that are matched by a given hql query.
     * @param hql The hibernate query with named parameters.
     * @return The found objects.
     * @throws PersistenceException
     */
    List<T> findByHQL(String hql)
    throws PersistenceException;

    /**
     * Returns a list of objects that are matched by a given hql query.
     * @param hql The hibernate query with named parameters.
     * @param map The map with the named parameters. Example: {@code HashMap<String,Object>}
     * @return The found objects.
     * @throws PersistenceException
     */
    List<T> findByHQL(String hql, Map<?, ?> map)
    throws PersistenceException;

    /**
     * Returns a specific object identified by its id from the data store.
     * @param id an object representing the id of the specific entity
     * (usually it is a unique {@code Integer} value).
     * @return the entity with the matching id.
     * @throws PersistenceException
     */
    T findById(Serializable id)
    throws PersistenceException;

    /**
     * Refreshes the given object to the actual state from the data store.
     * All dirty (unsaved) changes may be lost.
     * @param object the object to be refreshed.
     * @throws PersistenceException
     */
    void refreshToActualState(T object)
    throws PersistenceException;

    /**
     * Saves the given object to the data store or updates the entry, if it
     * already exists.
     * @param object the object to be saved or updated.
     * @throws PersistenceException
     */
    void saveOrUpdate(T object)
    throws PersistenceException;

    /**
     * Deletes an existing object from the data store.
     * @param object the object to be deleted from the data store.
     * @throws PersistenceException
     */
    void delete(T object)
    throws PersistenceException;
}
