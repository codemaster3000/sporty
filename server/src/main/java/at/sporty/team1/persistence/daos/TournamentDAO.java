package at.sporty.team1.persistence.daos;


import at.sporty.team1.domain.Tournament;
import at.sporty.team1.persistence.api.ITournamentDAO;

/**
 * Created by f00 on 16.11.15.
 */
public class TournamentDAO extends HibernateGenericDAO<Tournament> implements ITournamentDAO {

    /**
     * Creates a new TournamentDAO.
     */
    public TournamentDAO() {
        super(Tournament.class);
    }
}
