package at.sporty.team1.domain.interfaces;

import at.sporty.team1.domain.Department;
import at.sporty.team1.domain.League;
import at.sporty.team1.domain.Member;
import at.sporty.team1.domain.readonly.IRTeam;

import java.util.List;

/**
 * Created by f00 on 30.10.15.
 */
public interface ITeam extends IRTeam {
    void setTeamId(Integer teamId);

    void setTrainer(Member trainer);

    void setDepartment(Department department);

//    void setLeague(League league);

    void setTeamName(String teamName);
}
