package at.sporty.team1.domain.readonly;

import at.sporty.team1.domain.Department;
import at.sporty.team1.domain.League;
import at.sporty.team1.domain.Match;

import java.util.List;

/**
 * Created by f00 on 16.11.15.
 */
public interface IRTournament {
    String getDate();

    Department getDepartment();

    League getLeague();

    List<Match> getMatches();

    List<String> getTeams();

    String getId();
}
