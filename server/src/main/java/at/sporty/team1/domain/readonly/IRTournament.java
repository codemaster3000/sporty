package at.sporty.team1.domain.readonly;

import at.sporty.team1.domain.Match;
import at.sporty.team1.domain.Department;

import java.util.List;

/**
 * Created by f00 on 16.11.15.
 */
public interface IRTournament {
    Integer getTournamentId();

    String getDate();

    String getLocation();

    Department getDepartment();

//    League getLeague();

    List<String> getTeams();

    List<Match> getMatches();
}
