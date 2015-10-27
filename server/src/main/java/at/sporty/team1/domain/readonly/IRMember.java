package at.sporty.team1.domain.readonly;

import java.sql.Date;

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

    String getSport();

    String getRole();

    String getUsername();

    String getPassword();
}
