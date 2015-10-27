package at.sporty.team1.persistence;

import org.hibernate.criterion.Criterion;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Represents a generic DAO for common CRUD functionality.
 * https://developer.jboss.org/wiki/GenericDataAccessObjects?_sscc=t
 * Created by Mathias on 04.04.2015.
 */
public interface GenericDAO<T> {
    /**
     * Get all objects.
     *
     * @return All objects.
     */
    public List<T> getAll();

    /**
     * Gets an object by id.
     *
     * @param id The id to use to search.
     * @return The found object.
     */
    public T get(String id);

    /**
     * Creates or updates an object.
     *
     * @param t The object to create or save.
     */
    public void save(T t);

    /**
     * Deletes an object.
     *
     * @param t The object to delete.
     */
    public void delete(T t);

    /**
     * Gets a list of objects by criteria.
     *
     * @param criterion
     * @return
     */
    public List<T> findByCriteria(Criterion... criterion);

    /**
     * Gets a list of objects by a hql query.
     * IMPORTANT: Not generic! HQL string must fit to the generic class type.
     *
     * @param hql The hibernate query language string.
     * @return The found objects.
     * @throws SQLException
     */
    public List<T> findByHQL(String hql);

    /**
     * Gets a list of objects by a hql query.
     *
     * @param hql The hibernate query with named parameters.
     * @param map The map with the named parameters. Example: HashMap<String,Object>
     * @return The found objects.
     * @throws SQLException
     */
    public List<T> findByHQL(String hql, Map map);
}