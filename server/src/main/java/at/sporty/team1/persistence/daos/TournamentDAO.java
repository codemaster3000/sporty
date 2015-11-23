package at.sporty.team1.persistence.daos;


import at.sporty.team1.domain.Tournament;
import at.sporty.team1.misc.functional.AliasContainer;
import at.sporty.team1.persistence.api.ITournamentDAO;
import at.sporty.team1.persistence.util.Util;
import org.hibernate.Criteria;
import org.hibernate.criterion.*;
import org.hibernate.hql.internal.ast.util.AliasGenerator;
import org.hibernate.sql.Alias;

import javax.persistence.PersistenceException;
import javax.persistence.Tuple;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;
import javax.security.auth.callback.TextOutputCallback;
import java.util.List;

/**
 * Created by f00 on 16.11.15.
 */
public class TournamentDAO extends HibernateGenericDAO<Tournament> implements ITournamentDAO {

    private static final String PROP_DEPARTMENT = "department";
    private static final String PROP_DEPARTMENT_SPORT = "sport";
    private static final String PROP_EVENT_DATE = "date";
    private static final String PROP_LOCATION = "location";

    /**
     * Creates a new TournamentDAO.
     */
    public TournamentDAO() {
        super(Tournament.class);
    }

    @Override
    public List<Tournament> findBySport(String sport)
    throws PersistenceException {

        String searchString = Util.wrapInWildcard(sport);

        return findByCriteriaWithAlias(
            criteria -> criteria.createAlias(PROP_DEPARTMENT, PROP_DEPARTMENT),
            Restrictions.like(
                Util.buildAliasReference(PROP_DEPARTMENT, PROP_DEPARTMENT_SPORT),
                searchString
            )
        );
    }

    @Override
    public List<Tournament> findByEventDate(String eventDate)
    throws PersistenceException {
        return findByCriteria(Restrictions.eq(PROP_EVENT_DATE, eventDate));
    }

    @Override
    public List<Tournament> findByLocation(String location)
    throws PersistenceException {

        String searchString = Util.wrapInWildcard(location);

        return findByCriteria(Restrictions.like(PROP_LOCATION, searchString));
    }
}
