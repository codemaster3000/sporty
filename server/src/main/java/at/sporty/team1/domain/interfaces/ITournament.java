package at.sporty.team1.domain.interfaces;

import at.sporty.team1.domain.Department;
import at.sporty.team1.domain.readonly.IRTournament;

import java.util.List;

/**
 * Created by f00 on 16.11.15.
 */
public interface ITournament extends IRTournament {

    void setTournamentId(Integer id);

    void setDate(String date);

    void setLocation(String location);

    void setDepartment(Department department);

//    void setLeague(League league);
//
//    void setMatches(List<Match> matches);
//
//    void setTeams(List<String> teams);

    void addTeam(String team);

    void removeTeam(String team);

    void addMatch(IMatch match);

    void removeMatch(IMatch match);

	void setTeams(List<String> teams);
}
