package at.sporty.team1.domain;

import at.sporty.team1.domain.interfaces.IMember;

import javax.persistence.*;
import java.sql.Date;


/**
 * Created by f00 on 27.10.15.
 */

@Entity
@Table(name = "member")
public class Member implements IMember {
    private Integer memberId;
    private Integer teamId;
    private Integer department;
    private String firstname;
    private String lastname;
    private String gender;
    private Date dateOfBirth;
    private String eMail;
    private String address;
    private String squad;
    private String role;
    private String username;
    private boolean isFeePayed; //TODO hibernate

    @Override
    @Basic
    @Column(name = "isFeePayed")
    public boolean getIsFeePayed() { return isFeePayed;}

    @Override
    public void setIsFeePayed(boolean feePayed) {
        this.isFeePayed = feePayed;
    }

    @Override
    @Id
    @Column(name = "memberId")
    public Integer getMemberId() {
        return memberId;
    }

    @Override
    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }

    @Override
    @Basic
    @Column(name = "teamId")
    public Integer getTeamId() {
        return teamId;
    }

    @Override
    public void setTeamId(Integer teamId) {
        this.teamId = teamId;
    }

    /**
     * many to many? TODO
     * @return
     */
    @Override
    @Basic
    @Column(name = "departmentId")
    public Integer getDepartment() {
        return department;
    }

    @Override
    public void setDepartment(Integer department) {
        this.department = department;
    }

    @Override
    @Basic
    @Column(name = "fname")
    public String getFirstName() {
        return firstname;
    }

    @Override
    public void setFirstName(String firstname) {
        this.firstname = firstname;
    }

    @Override
    @Basic
    @Column(name = "lname")
    public String getLastName() {
        return lastname;
    }

    @Override
    public void setLastName(String lastname) {
        this.lastname = lastname;
    }

    @Override
    @Basic
    @Column(name = "gender")
    public String getGender() {
        return gender;
    }

    @Override
    public void setGender(String gender) {
        this.gender = gender;
    }

    @Override
    @Basic
    @Column(name = "dateOfBirth")
    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    @Override
    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    @Override
    @Basic
    @Column(name = "email")
    public String getEmail() {
        return eMail;
    }

    @Override
    public void setEmail(String email) {
        this.eMail = email;
    }

    @Override
    @Basic
    @Column(name = "address")
    public String getAddress() {
        return address;
    }

    @Override
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * many to many? TODO
     * @return TODO
     */
    @Override
    @Basic
    @Column(name = "role")
    public String getRole() {
        return role;
    }

    @Override
    public void setRole(String role) {
        this.role = role;
    }

    @Override
    @Basic
    @Column(name = "squad")
    public String getSquad() {
        return squad;
    }

    @Override
    public void setSquad(String squad) {
        this.squad = squad;
    }

    @Override
    @Basic
    @Column(name = "username")
    public String getUsername() {
        return username;
    }

    @Override
    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Member member = (Member) o;

        if (memberId != null ? !memberId.equals(member.memberId) : member.memberId != null) return false;
        if (teamId != null ? !teamId.equals(member.teamId) : member.teamId != null) return false;
        if (department != null ? !department.equals(member.department) : member.department != null) return false;
        if (firstname != null ? !firstname.equals(member.firstname) : member.firstname != null) return false;
        if (lastname != null ? !lastname.equals(member.lastname) : member.lastname != null) return false;
        if (gender != null ? !gender.equals(member.gender) : member.gender != null) return false;
        if (dateOfBirth != null ? !dateOfBirth.equals(member.dateOfBirth) : member.dateOfBirth != null) return false;
        if (eMail != null ? !eMail.equals(member.eMail) : member.eMail != null) return false;
        if (address != null ? !address.equals(member.address) : member.address != null) return false;
        if (squad != null ? !squad.equals(member.squad) : member.squad != null) return false;
        if (role != null ? !role.equals(member.role) : member.role != null) return false;
        if (username != null ? !username.equals(member.username) : member.username != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = memberId != null ? memberId.hashCode() : 0;
        result = 31 * result + (teamId != null ? teamId.hashCode() : 0);
        result = 31 * result + (department != null ? department.hashCode() : 0);
        result = 31 * result + (firstname != null ? firstname.hashCode() : 0);
        result = 31 * result + (lastname != null ? lastname.hashCode() : 0);
        result = 31 * result + (gender != null ? gender.hashCode() : 0);
        result = 31 * result + (dateOfBirth != null ? dateOfBirth.hashCode() : 0);
        result = 31 * result + (eMail != null ? eMail.hashCode() : 0);
        result = 31 * result + (address != null ? address.hashCode() : 0);
        result = 31 * result + (squad != null ? squad.hashCode() : 0);
        result = 31 * result + (role != null ? role.hashCode() : 0);
        result = 31 * result + (username != null ? username.hashCode() : 0);
        return result;
    }
}
