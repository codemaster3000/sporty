package at.sporty.team1.domain.readonly;

import java.sql.Date;

/**
 * Created by sereGkaluv on 27-Oct-15.
 */
public interface IRMember {

    boolean getIsFeePayed();

    Integer getMemberId();

    Integer getTeamId();

    Integer getDepartment();

    String getFirstName();

    String getLastName();

    String getGender();

    Date getDateOfBirth();

    String getEmail();

    String getAddress();

    String getSquad();

    String getRole();

    String getUsername();
}
