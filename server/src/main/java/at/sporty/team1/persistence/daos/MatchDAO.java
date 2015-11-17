package at.sporty.team1.persistence.daos;


import at.sporty.team1.domain.Match;
import at.sporty.team1.domain.Tournament;
import at.sporty.team1.persistence.api.IMatchDAO;

import javax.persistence.PersistenceException;
import java.util.List;

/**
 * Created by sereGkaluv on 17.11.15.
 */
public class MatchDAO extends HibernateGenericDAO<Match> implements IMatchDAO {
    /**
     * Creates a new MatchDAO.
     */
    public MatchDAO() {
        super(Match.class);
    }


    @Override
    public List<Match> findByTournament(Tournament tournament) throws PersistenceException {
        System.out.println("findByTournament IS NOT implemented yet!");
        return null;
    }
}
