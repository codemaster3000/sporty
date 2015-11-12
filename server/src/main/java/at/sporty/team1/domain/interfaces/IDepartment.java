package at.sporty.team1.domain.interfaces;

import at.sporty.team1.domain.Member;
import at.sporty.team1.domain.Team;
import at.sporty.team1.domain.readonly.IRDepartment;

import java.util.List;

/**
 * Created by sereGkaluv on 27-Oct-15.
 */
public interface IDepartment extends IRDepartment {
    void setDepartmentId(Integer departmentId);

    void setSport(String sport);
    
    void setDepartmentHead(Member head);

    void setTeams(List<Team> teamList);
}
