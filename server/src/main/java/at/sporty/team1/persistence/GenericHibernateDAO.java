package at.sporty.team1.persistence;

/**
 * Represents a implementation of the generic DAO.
 * https://developer.jboss.org/wiki/GenericDataAccessObjects?_sscc=t
 * Created by Mathias Nigsch on 04.04.2015.
 */
public class GenericHibernateDAO<T> implements GenericDAO<T> {
    private Class<T> _persistentClass;
    private Logger _logger = Loggers.DATABASE;

    /**
     * Creates a new generic hibernate DAO object.
     */
    public GenericHibernateDAO(Class<T> entityClass) {
        _persistentClass = entityClass;
    }

    /**
     * Gets the actual persistent class. E.g.: patient.class
     *
     * @return The actual persistent class.
     */
    public synchronized Class<T> getPersistentClass() {
        return _persistentClass;
    }

    @Override
    public synchronized List<T> getAll() {
        Session session = getSession();
        Transaction tx = null;
        List<T> list = null;

        try {
            tx = session.beginTransaction();

            _logger.info("Loading all entries of " + getPersistentClass() + ".");
            list = getSession().createCriteria(getPersistentClass()).list();
            Hibernate.initialize(list);
            _logger.info("Found " + list.size() + " entries of " + getPersistentClass() + ".");

            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            _logger.error("Not able to get all objects. " + e.getMessage(), e);
            throw e;
        }

        return list;
    }

    @Override
    public synchronized T get(String id) {
        Session session = getSession();
        Transaction tx = null;
        T t = null;

        try {
            tx = session.beginTransaction();

            _logger.info("Loading object with ID " + id + " from " + getPersistentClass() + ".");
            t = (T) getSession().load(getPersistentClass(), id);
            Hibernate.initialize(t);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            _logger.error("Not able to get object by id. " + e.getMessage(), e);
            throw e;
        }

        return t;
    }


    /**
     * Refreshes an object.
     *
     * @param t The object to refresh.
     * @throws SQLException
     */
    public synchronized void refresh(Object t) {
        Session session = getSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.refresh(t);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            Loggers.DATABASE.error("Not able to get object by criteria. " + e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public synchronized void save(T t) {
        Session session = getSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            session.saveOrUpdate(t);
            _logger.info("Saved object (" + getPersistentClass() + ") into database.");
            session.flush();
            tx.commit();

        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            _logger.error("Not able to save object. " + e.getMessage(), e);
            throw e;
        }

        // If the object is a change entry object
        if (!(t instanceof ChangeEntry)) {
            // Save change entry
            DBChangeHelper.getInstance().saveChange((AbstractPersistentObject) t);
        }
    }

    @Override
    public synchronized void delete(T t) {
        Session session = getSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            session.delete(t);
            _logger.info("Deleted object (" + getPersistentClass() + ") from database.");

            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            _logger.error("Not able to get object by id. " + e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public synchronized List<T> findByCriteria(Criterion... criterion) {
        Session session = getSession();
        Transaction tx = null;
        List<T> list = null;
        try {
            tx = session.beginTransaction();

            Criteria criteria = getSession().createCriteria(getPersistentClass());
            for (Criterion c : criterion) {
                criteria.add(c);
            }

            list = criteria.list();
            Hibernate.initialize(list);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            _logger.error("Not able to get object by criteria. " + e.getMessage(), e);
            throw e;
        }

        return list;
    }

    @Override
    public synchronized List<T> findByHQL(String hql) {
        return findByHQL(hql, null);
    }

    @Override
    public synchronized List<T> findByHQL(String hql, Map map) {
        Session session = getSession();
        Transaction tx = null;
        List<T> list = null;
        try {
            tx = session.beginTransaction();

            Query query = session.createQuery(hql);
            if (map != null) {
                query.setProperties(map);
            }
            list = query.list();
            Hibernate.initialize(list);
            session.flush();
            _logger.info("Found objects (" + list.size() + ") from database.");
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            _logger.error("Not able to get object by criteria. " + e.getMessage(), e);
            throw e;
        }

        return list;
    }

    /**
     * Gets a session from the database facade.
     *
     * @return The created session.
     */
    private Session getSession() {
        return HibernateUtil.getInstance().getFactory().getCurrentSession();
    }
}