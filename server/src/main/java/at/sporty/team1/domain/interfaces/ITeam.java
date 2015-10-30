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
    void setTeamId(String id);
    String getTeamId();

    void setTrainer(Member trainer);
    Member getTrainer();

    void setDepartment(Department department);
    Department getDepartment();

    void setLeague(League league);
    League getLeague();

    void setMembers(List<Member> members);
    List<Member> getMembers();

    void setTeamname(String teamname);
    String getTeamname();

}
