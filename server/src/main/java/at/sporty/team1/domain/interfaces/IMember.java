package at.sporty.team1.domain.interfaces;

import at.sporty.team1.domain.readonly.IRMember;

import java.sql.Date;

/**
 * Created by sereGkaluv on 27-Oct-15.
 */
public interface IMember extends IRMember {
    void setMemberId(int memberId);

    void setFirstname(String firstname);

    void setLastname(String lastname);

    void setGender(String gender);

    void setDateOfBirth(Date dateOfBirth);

    void setEMail(String eMail);

    void setAddress(String address);

    void setDepartment(String department);

    void setSport(String sport);

    void setRole(String role);

    void setUsername(String username);

    void setPassword(String password);
}
