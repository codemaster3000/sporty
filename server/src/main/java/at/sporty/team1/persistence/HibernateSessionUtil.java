package at.sporty.team1.persistence;

import at.sporty.team1.misc.functional.ThrowingConsumer;
import at.sporty.team1.misc.functional.ThrowingFunction;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import javax.persistence.PersistenceException;
import java.util.Properties;

public class HibernateSessionUtil {
    public static final String HIBERNATE_CONFIG_FILE = "/hibernate.cfg.xml";
    private final SessionFactory SESSION_FACTORY;
    private static HibernateSessionUtil INSTANCE;

    public static HibernateSessionUtil getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new HibernateSessionUtil();
        }
        return INSTANCE;
    }

    public Session openSession() throws HibernateException {
        return SESSION_FACTORY.openSession();
    }

    public Session getCurrentSession() throws HibernateException {
        return SESSION_FACTORY.getCurrentSession();
    }

    public void close() {
        SESSION_FACTORY.close();
    }

    public <T> T makeSimpleTransaction(ThrowingFunction<Session, T, PersistenceException> transactionFunction)
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

    public void makeSimpleTransaction(ThrowingConsumer<Session, PersistenceException> transactionFunction)
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

//            SESSION_FACTORY = configuration.buildSessionFactory(
//                new StandardServiceRegistryBuilder().applySettings(properties).build()
//            );

            ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
            SESSION_FACTORY = configuration.buildSessionFactory(serviceRegistry);
        } catch (Throwable ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }
}
