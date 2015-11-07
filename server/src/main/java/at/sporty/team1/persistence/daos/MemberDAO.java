package at.sporty.team1.persistence.daos;

import at.sporty.team1.domain.Member;
import at.sporty.team1.persistence.Util;
import at.sporty.team1.persistence.api.IMemberDAO;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

import javax.persistence.PersistenceException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Represents a concrete DAO for Members
 */
public class MemberDAO extends HibernateGenericDAO<Member> implements IMemberDAO {

    private static final String PROP_FIRST_NAME = "firstName";
    private static final String PROP_LAST_NAME = "lastName";
    private static final String PROP_DATE_OF_BIRTH = "dateOfBirth";
    private static final String PROP_EMAIL = "email";
    private static final String PROP_TEAM_NAME = "teamId";
    private static final String DELIMITER = " ";

    /**
     * Creates a new patient DAO.
     */
    public MemberDAO() {
        super(Member.class);
    }

    @Override
    public List<Member> findByNameString(String searchString) throws PersistenceException {
        //Set is used to avoid duplication
        Set<Member> results = new LinkedHashSet<>();
        String[] split = searchString.split(DELIMITER);

        switch (split.length) {
            case 1: {
                //last or first name
                results.addAll(findByFirstName(split[0]));
                results.addAll(findByLastName(split[0]));

                return new LinkedList<>(results);
            }

            case 2: {
                //full name with first and last name
                results.addAll(findByFullName(split[0], split[1]));
                results.addAll(findByFullName(split[1], split[0]));

                return new LinkedList<>(results);
            }

            default: return null;
        }
    }

    @Override
    public List<Member> findByFullName(String firstName, String lastName) throws PersistenceException {
        String searchFirst = Util.wrapInWildcard(firstName);
        String searchLast = Util.wrapInWildcard(lastName);

        return findByCriteria(Restrictions.and(
            Restrictions.like(PROP_FIRST_NAME, searchFirst, MatchMode.ANYWHERE),
            Restrictions.like(PROP_LAST_NAME, searchLast, MatchMode.ANYWHERE)
        ));
    }

    @Override
    public List<Member> findByFirstName(String firstName) throws PersistenceException {
        String searchString = Util.wrapInWildcard(firstName);

        return findByCriteria(Restrictions.like(PROP_FIRST_NAME, searchString));
    }

    @Override
    public List<Member> findByLastName(String lastName) throws PersistenceException {
        String searchString = Util.wrapInWildcard(lastName);

        return findByCriteria(Restrictions.like(PROP_LAST_NAME, searchString));
    }

    @Override
    public List<Member> findByDateOfBirth(String dateOfBirth) throws PersistenceException {
        Date date = Date.valueOf(dateOfBirth);

        return findByCriteria(Restrictions.eq(PROP_DATE_OF_BIRTH, date));
    }

    @Override
    public List<Member> findByEmail(String email) throws PersistenceException {
        String searchString = Util.wrapInWildcard(email);

        return findByCriteria(Restrictions.like(PROP_EMAIL, searchString));
    }

    @Override
    public List<Member> findByTeamName(String teamName) throws PersistenceException {
        String searchString = Util.wrapInWildcard(teamName);

        return findByCriteria(Restrictions.like(PROP_TEAM_NAME, searchString, MatchMode.ANYWHERE));
    }
}