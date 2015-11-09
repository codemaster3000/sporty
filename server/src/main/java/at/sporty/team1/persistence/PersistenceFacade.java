package at.sporty.team1.persistence;

import at.sporty.team1.persistence.api.IDepartmentDAO;
import at.sporty.team1.persistence.api.IGenericDAO;
import at.sporty.team1.persistence.api.IMemberDAO;
import at.sporty.team1.persistence.api.ITeamDAO;
import at.sporty.team1.persistence.daos.DepartmentDAO;
import at.sporty.team1.persistence.daos.HibernateGenericDAO;
import at.sporty.team1.persistence.daos.MemberDAO;
import at.sporty.team1.persistence.daos.TeamDAO;

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
     * Returns an implementation of the IMemberDAO interface providing
     * further operations with memberList.
     * @return an instance of IMemberDAO
     */
    public static IMemberDAO getNewMemberDAO() {
        return new MemberDAO();
    }

    /**
     * Returns an implementation of the ITeamDAO interface providing
     * further operations with teams.
     * @return an instance of ITeamDAO
     */
    public static ITeamDAO getNewTeamDAO() {
        return new TeamDAO();
    }

    /**
     * Returns an implementation of the IDepartmentDAO interface providing
     * further operations with teams.
     * @return an instance of IDepartmentDAO
     */
    public static IDepartmentDAO getNewDepartmentDAO() {
        return new DepartmentDAO();
    }
}
