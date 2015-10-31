package at.sporty.team1.persistence;

import at.sporty.team1.persistence.api.IGenericDAO;
import at.sporty.team1.persistence.api.IMemberDAO;
import at.sporty.team1.persistence.daos.HibernateGenericDAO;
import at.sporty.team1.persistence.daos.MemberDAO;

/**
 * Created by sereGkaluv on 27-Oct-15.
 */
public class PersistenceFacade {
    /**
     * Returns an implementation of the IGenericDAO interface for the specified
     * class.
     * @param domainClass the class of the domain object
     * @param <T> the type of the domain object
     * @return an instance of IGenericDAO with operations for the specified class
     */
    public static <T> IGenericDAO<T> getNewGenericDAO(Class<T> domainClass) {
        return new HibernateGenericDAO<>(domainClass);
    }

    /**
     * Returns an implementation of the MemberDAO interface providing
     * further operations with members.
     * @return an instance of MemberDAO
     */
    public static MemberDAO getNewMemberDAO() {
        return new MemberDAO();
    }
}
