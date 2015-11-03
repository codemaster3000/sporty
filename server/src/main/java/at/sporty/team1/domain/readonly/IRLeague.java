package at.sporty.team1.domain.readonly;

import at.sporty.team1.domain.Match;
import at.sporty.team1.domain.Team;

import java.util.List;

/**
 * Created by f00 on 03.11.15.
 */
public interface IRLeague {

    List<Match> getMatches();

    String getName();

    List<Team> getOrder();

    List<Team> getTeams();
}
