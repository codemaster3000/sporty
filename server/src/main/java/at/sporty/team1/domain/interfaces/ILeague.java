package at.sporty.team1.domain.interfaces;

import at.sporty.team1.domain.Match;
import at.sporty.team1.domain.Team;
import at.sporty.team1.domain.readonly.IRLeague;

import java.util.List;

/**
 * Created by f00 on 03.11.15.
 */
public interface ILeague extends IRLeague {

    void setMatches(List<Match> matches);

    void setName(String name);

    void setOrder(List<Team> order);

    void setTeams(List<Team> teams);
}
