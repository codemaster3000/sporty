package at.sporty.team1.domain.interfaces;

import at.sporty.team1.domain.Member;
import at.sporty.team1.domain.readonly.IRTeam;

import java.util.List;

/**
 * Created by f00 on 30.10.15.
 */
public interface ITeam extends IRTeam {
    
    void setTeamId(Integer teamId);

    void setTrainerId(Integer trainerId);

    void setDepartmentId(Integer departmentId);

    void setLeagueId(Integer leagueId);

    void setTeamName(String teamName);

    void setMemberList(List<Member> memberList);
}
