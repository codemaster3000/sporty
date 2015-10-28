package at.sporty.team1.application.dtos;

/**
 * Created by sereGkaluv on 27-Oct-15.
 */
public class MemberDTO {
    private String _firstName;
    private String _lastName;
    private String _gender;
    private String _dateOfBirth;
    private String _email;
    private String _address;
    private String _department;
    private String _sport;
    private String _role;
    private String _username;
    private String _password;

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

    public String getDepartment() {
        return _department;
    }

    public MemberDTO setDepartment(String department) {
        _department = department;
        return this;
    }

    public String getSport() {
        return _sport;
    }

    public MemberDTO setSport(String sport) {
        _sport = sport;
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

    public String getPassword() {
        return _password;
    }

    public MemberDTO setPassword(String password) {
        _password = password;
        return this;
    }
}
