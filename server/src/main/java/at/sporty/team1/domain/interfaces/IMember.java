package at.sporty.team1.domain.interfaces;

import at.sporty.team1.domain.Department;
import at.sporty.team1.domain.Gender;
import at.sporty.team1.domain.Team;
import at.sporty.team1.domain.readonly.IRMember;

import java.util.List;
import java.util.Set;

/**
 * Created by sereGkaluv on 27-Oct-15.
 */
public interface IMember extends IRMember {

    void setMemberId(Integer memberId);

    void setFirstName(String firstname);

    void setLastName(String lastname);

    void setGender(Gender gender);

    void setDateOfBirth(String dateOfBirth);

    void setEmail(String eMail);

    void setAddress(String address);

    void setSquad(String squad);

    void setRole(String role);

    void setUsername(String username);

    void setIsFeePaid(Boolean isFeePaid);

    void setDepartments(Set<Department> departmentList);

    void addDepartment(IDepartment department);

    void removeDepartment(IDepartment department);

    void setTeams(Set<Team> teamList);

    void addTeam(ITeam team);

    void removeTeam(ITeam team);
}
