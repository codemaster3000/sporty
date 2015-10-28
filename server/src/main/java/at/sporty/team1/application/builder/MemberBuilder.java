package at.sporty.team1.application.builder;

import at.sporty.team1.domain.Member;
import at.sporty.team1.domain.interfaces.IMember;
import at.sporty.team1.persistence.PersistenceFacade;

import javax.persistence.PersistenceException;
import java.sql.Date;

/**
 * Created by sereGkaluv on 27-Oct-15.
 */
public class MemberBuilder {
    private String _firstName;
    private String _lastName;
    private String _gender;
    private Date _dateOfBirth;
    private String _email;
    private String _address;
    private String _department;
    private String _sport;
    private String _role;
    private String _username;
    private String _password;

    public MemberBuilder setFirstName(String firstName) {
        _firstName = firstName;
        return this;
    }

    public MemberBuilder setLastName(String lastName) {
        _lastName = lastName;
        return this;
    }

    public MemberBuilder setGender(String gender) {
        _gender = gender;
        return this;
    }

    public MemberBuilder setDateOfBirth(Date dateOfBirth) {
        _dateOfBirth = dateOfBirth;
        return this;
    }

    public MemberBuilder setEmail(String email) {
        _email = email;
        return this;
    }

    public MemberBuilder setAddress(String address) {
        _address = address;
        return this;
    }

    public MemberBuilder setDepartment(String department) {
        _department = department;
        return this;
    }

    public MemberBuilder setSport(String sport) {
        _sport = sport;
        return this;
    }

    public MemberBuilder setRole(String role) {
        _role = role;
        return this;
    }

    public MemberBuilder setUsername(String username) {
        _username = username;
        return this;
    }

    public MemberBuilder setPassword(String password) {
        _password = password;
        return this;
    }

    public void save() {
        //TODO conditions validation: if member exists

        IMember member = new Member();
        member.setFirstName(_firstName);
        member.setLastName(_lastName);
        member.setGender(_gender);
        member.setDateOfBirth(_dateOfBirth);
        member.setEmail(_email);
        member.setAddress(_address);
        member.setDepartment(_department);
        member.setRole(_role);
        member.setSport(_sport);
        member.setUsername(_username);
        member.setPassword(_password);

        // use IRMember for checking if new member already exists?
        
        try {
            PersistenceFacade.getNewGenericDAO(Member.class).saveOrUpdate((Member) member);

            //TODO replace with logger.
            System.out.println("NEW MEMBER WAS CREATED.");
        } catch (PersistenceException e) {
            //TODO: LOGGER.error(e);
        }
    }
}
