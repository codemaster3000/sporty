package at.sporty.team1.domain.interfaces;

import at.sporty.team1.domain.Gender;
import at.sporty.team1.domain.readonly.IRMember;

import java.sql.Date;

/**
 * Created by sereGkaluv on 27-Oct-15.
 */
public interface IMember extends IRMember {

    void setMemberId(Integer memberId);

    void setFirstName(String firstname);

    void setLastName(String lastname);

    void setGender(Gender gender);

    void setDateOfBirth(Date dateOfBirth);

    void setEmail(String eMail);

    void setAddress(String address);

    void setSquad(String squad);

    void setRole(String role);

    void setUsername(String username);

    void setIsFeePayed(Boolean feePayed);
}
