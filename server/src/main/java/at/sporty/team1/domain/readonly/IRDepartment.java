package at.sporty.team1.domain.readonly;

import at.sporty.team1.domain.Member;
import at.sporty.team1.domain.Team;

import java.util.List;

/**
 * Created by sereGkaluv on 27-Oct-15.
 */
public interface IRDepartment {
    Integer getDepartmentId();

    String getSport();
    
    Member getDepartmentHead();

    List<Team> getTeams();

    List<Member> getMembers();
}
