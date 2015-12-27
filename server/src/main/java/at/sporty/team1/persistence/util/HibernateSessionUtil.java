package at.sporty.team1.persistence.util;

import at.sporty.team1.shared.api.entity.IThrowingConsumer;
import at.sporty.team1.shared.api.entity.IThrowingFunction;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import javax.persistence.PersistenceException;
import java.util.Properties;

public class HibernateSessionUtil {
    private static final Logger LOGGER = LogManager.getLogger();
    public static final String HIBERNATE_CONFIG_FILE = "/hibernate.cfg.xml";
    private final SessionFactory SESSION_FACTORY;
    private static HibernateSessionUtil _instance;

    public static HibernateSessionUtil getInstance() {
        if (_instance == null) {
            _instance = new HibernateSessionUtil();
        }
        return _instance;
    }

    public Session openSession() throws HibernateException {
        return SESSION_FACTORY.openSession();
    }

    public Session getCurrentSession() throws HibernateException {
        if (!SESSION_FACTORY.getCurrentSession().isOpen()) openSession();
        return SESSION_FACTORY.getCurrentSession();
    }

    public void close() {
        SESSION_FACTORY.close();
    }

    public <T> T makeSimpleTransaction(IThrowingFunction<Session, T, PersistenceException> transactionFunction)
    throws HibernateException, PersistenceException {
        Session session = getCurrentSession();
        try {
            session.beginTransaction();

            T result = transactionFunction.apply(session);
            session.getTransaction().commit();

            return result;
        } catch (Exception e) {
            session.getTransaction().rollback();
            throw e;
        }
    }

    public void makeSimpleTransaction(IThrowingConsumer<Session, PersistenceException> transactionFunction)
    throws HibernateException, PersistenceException {
        Session session = getCurrentSession();
        try {
            session.beginTransaction();

            transactionFunction.accept(session);
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            throw e;
        }
    }

    private HibernateSessionUtil() {
        try {

            Configuration configuration = new Configuration().configure(getClass().getResource(HIBERNATE_CONFIG_FILE));
            Properties properties = configuration.getProperties();

            SESSION_FACTORY = configuration.buildSessionFactory(
                new StandardServiceRegistryBuilder().applySettings(properties).build()
            );

        } catch (Throwable e) {

            LOGGER.fatal(
                "Something went wrong by initializing hibernate. Check config files for errors and DB availability."
            );
            throw new ExceptionInInitializerError(e);
        }
    }
}
