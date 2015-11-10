package at.sporty.team1.persistence.api;

import at.sporty.team1.domain.Member;
import at.sporty.team1.domain.Team;

import java.util.List;

import javax.persistence.PersistenceException;

/**
 * Created by sereGkaluv on 06-Nov-15.
 */
public interface ITeamDAO {

    /**
     * Find team(s) by name.
     *
     * @param teamName name of the team to be searched
     * @return List<Team>
     */
    List<Team> findTeamsByName(String teamName);
    
    
}
