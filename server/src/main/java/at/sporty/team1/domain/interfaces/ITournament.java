package at.sporty.team1.domain.interfaces;

import at.sporty.team1.domain.Department;
import at.sporty.team1.domain.League;
import at.sporty.team1.domain.Match;
import at.sporty.team1.domain.readonly.IRTournament;

import java.util.List;

/**
 * Created by f00 on 16.11.15.
 */
public interface ITournament extends IRTournament {
    void setDate(String date);

    void setDepartment(Department department);

    void setLeague(League league);

    void setMatches(List<Match> matches);

    void setTeams(List<String> teams);

    void setId(String id);

    void addTeam(ITeam team);

    void removeTeam(ITeam team);

	void setLocation(String location);

}
