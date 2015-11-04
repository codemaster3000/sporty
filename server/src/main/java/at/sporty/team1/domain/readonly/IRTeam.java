package at.sporty.team1.domain.readonly;

import at.sporty.team1.domain.Department;
import at.sporty.team1.domain.League;
import at.sporty.team1.domain.Member;

/**
 * Created by f00 on 30.10.15.
 */
public interface IRTeam {
    Integer getTeamId();

    Member getTrainer();

    Department getDepartment();

//    League getLeagueId();

    String getTeamName();
}
