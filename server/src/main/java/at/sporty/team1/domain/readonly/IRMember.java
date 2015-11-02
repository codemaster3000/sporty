package at.sporty.team1.domain.readonly;

import at.sporty.team1.domain.Team;

import java.sql.Date;
import java.util.List;

/**
 * Created by sereGkaluv on 27-Oct-15.
 */
public interface IRMember {
    int getMemberId();

    String getFirstName();

    String getLastName();

    String getGender();

    Date getDateOfBirth();

    String getEmail();

    String getAddress();

    String getDepartment();

    Team getTeam();

    String getSquad();

    String getRole();

    String getUsername();
}
