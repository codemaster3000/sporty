package at.sporty.team1.domain.readonly;

import at.sporty.team1.domain.Department;
import at.sporty.team1.domain.Gender;
import at.sporty.team1.domain.Team;

import java.util.List;
import java.util.Set;

/**
 * Created by sereGkaluv on 27-Oct-15.
 */
public interface IRMember {

    Integer getMemberId();

    String getFirstName();

    String getLastName();

    Gender getGender();

    String getDateOfBirth();

    String getEmail();

    String getAddress();

    String getSquad();

    String getRole();

    String getUsername();

    Boolean getIsFeePaid();

    Set<Department> getDepartments();

    Set<Team> getTeams();
}
