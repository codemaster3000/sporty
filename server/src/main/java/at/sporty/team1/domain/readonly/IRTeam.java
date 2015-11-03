package at.sporty.team1.domain.readonly;

import at.sporty.team1.domain.Member;

import java.util.List;

/**
 * Created by f00 on 30.10.15.
 */
public interface IRTeam {
    Integer getTeamId();

    Integer getTrainerId();

    Integer getDepartmentId();

    Integer getLeagueId();

    String getTeamName();

    List<Member> getMemberList();
}
