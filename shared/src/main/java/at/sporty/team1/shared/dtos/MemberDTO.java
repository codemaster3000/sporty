package at.sporty.team1.shared.dtos;

import at.sporty.team1.shared.api.entity.IDTO;

/**
 * This IDTO Object implements Object Builder pattern.
 */
public class MemberDTO implements IDTO {
    private static final long serialVersionUID = 1L;

    private Integer _memberId;
    private String _firstName;
    private String _lastName;
    private String _gender;
    private String _dateOfBirth;
    private String _email;
    private String _address;
    private String _squad;
    private String _role;
    private String _username;
    private Boolean _isFeePaid;

    public MemberDTO() {
    }

    public Integer getMemberId() {
        return _memberId;
    }

    public MemberDTO setMemberId(Integer memberId) {
        _memberId = memberId;
        return this;
    }

    public String getFirstName() {
        return _firstName;
    }

    public MemberDTO setFirstName(String firstName) {
        _firstName = firstName;
        return this;
    }

    public String getLastName() {
        return _lastName;
    }

    public MemberDTO setLastName(String lastName) {
        _lastName = lastName;
        return this;
    }

    public String getGender() {
        return _gender;
    }

    public MemberDTO setGender(String gender) {
        _gender = gender;
        return this;
    }

    public String getDateOfBirth() {
        return _dateOfBirth;
    }

    public MemberDTO setDateOfBirth(String dateOfBirth) {
        _dateOfBirth = dateOfBirth;
        return this;
    }

    public String getEmail() {
        return _email;
    }

    public MemberDTO setEmail(String email) {
        _email = email;
        return this;
    }

    public String getAddress() {
        return _address;
    }

    public MemberDTO setAddress(String address) {
        _address = address;
        return this;
    }

    public String getSquad() {
        return _squad;
    }

    public MemberDTO setSquad(String squad) {
        _squad = squad;
        return this;
    }

    public String getRole() {
        return _role;
    }

    public MemberDTO setRole(String role) {
        _role = role;
        return this;
    }

    public String getUsername() {
        return _username;
    }

    public MemberDTO setUsername(String username) {
        _username = username;
        return this;
    }

    public Boolean getIsFeePaid() { return _isFeePaid;}

    public MemberDTO setIsFeePaid(Boolean feePaid) {
        _isFeePaid = feePaid;
        return this;
    }
}
