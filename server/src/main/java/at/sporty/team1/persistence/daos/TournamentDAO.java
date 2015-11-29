package at.sporty.team1.persistence.daos;


import at.sporty.team1.domain.Member;
import at.sporty.team1.domain.Tournament;
import at.sporty.team1.persistence.api.ITournamentDAO;
import at.sporty.team1.persistence.util.PropertyPair;
import at.sporty.team1.persistence.util.Util;
import org.hibernate.criterion.Restrictions;

import javax.persistence.PersistenceException;
import java.util.List;

/**
 * Created by f00 on 16.11.15.
 */
public class TournamentDAO extends HibernateGenericDAO<Tournament> implements ITournamentDAO {

    private static final String PROP_DEPARTMENT = "department";
    private static final String PROP_DEPARTMENT_SPORT = "sport";
    private static final String PROP_EVENT_DATE = "date";
    private static final String PROP_LOCATION = "location";
    private static final String PROP_TRAINER_ID = "trainerId";
    private static final String PROP_TOURNAMENT_ID = "tournamentId";

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

    @Override
    public Boolean isTrainerOfTournament(Member trainer, Integer tournamentId)
    throws PersistenceException {

        if (trainer == null || tournamentId == null) return false;

        String rawQuery =
            "SELECT tournament.* " +
            "FROM tournament " +
            "INNER JOIN team " +
            "WHERE " +
            "tournament.departmentId = team.departmentId " +
            "AND " +
            "tournament.tournamentId = :" + PROP_TOURNAMENT_ID + " " +
            "AND " +
            "team.trainerId = :" + PROP_TRAINER_ID;

        List<Tournament> tournaments = findBySQLQuery(
            rawQuery,
            new PropertyPair<>(PROP_TOURNAMENT_ID, tournamentId),
            new PropertyPair<>(PROP_TRAINER_ID, trainer.getMemberId())
        );

        return tournaments != null && !tournaments.isEmpty();
    }
}
