package at.sporty.team1.domain.readonly;

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

	List<String> getTeams();

//    League getLeague();
//
//    List<Match> getMatches();
//
//    List<String> getTeams();
}
