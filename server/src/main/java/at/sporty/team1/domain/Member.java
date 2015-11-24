package at.sporty.team1.domain;

import at.sporty.team1.domain.interfaces.IDepartment;
import at.sporty.team1.domain.interfaces.IMember;
import at.sporty.team1.domain.interfaces.ITeam;
import at.sporty.team1.misc.converters.SQLDateConverter;

import javax.persistence.*;
import java.util.List;


/**
 * Created by f00 on 27.10.15.
 */

@Entity
@Table(name = "member")
public class Member implements IMember {
    private Integer memberId;
    private String firstname;
    private String lastname;
    private Gender gender;
    private String dateOfBirth;
    private String eMail;
    private String address;
    private String squad;
    private String role;
    private String username;
    private Boolean isFeePaid;
    private List<Department> departmentList;
    private List<Team> teamList;

    public Member() {
    }

    @Override
    @Id
    @GeneratedValue
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
    @Basic(optional = false)
    @Enumerated(EnumType.STRING)
    @Column(name = "gender", columnDefinition = "ENUM('M', 'F')")
    public Gender getGender() {
        return gender;
    }

    @Override
    public void setGender(Gender gender) {
        this.gender = gender;
    }

    @Override
    @Basic
    @Convert(converter = SQLDateConverter.class)
    @Column(name = "dateOfBirth")
    public String getDateOfBirth() {
        return dateOfBirth;
    }

    @Override
    public void setDateOfBirth(String dateOfBirth) {
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
    @Basic
    @Column(name = "isFeePaid", columnDefinition = "false")
    public Boolean getIsFeePaid() { return isFeePaid;}

    @Override
    public void setIsFeePaid(Boolean isFeePaid) {
        this.isFeePaid = isFeePaid;
    }

    @Override
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
        name = "memberDepartments",
        joinColumns = @JoinColumn(name = "memberId"),
        inverseJoinColumns = @JoinColumn(name = "departmentId")
    )
    public List<Department> getDepartments() {
        return departmentList;
    }

    public void setDepartments(List<Department> departmentList) {
        this.departmentList = departmentList;
    }

    @Override
    public void addDepartment(IDepartment department) {

        if (!(department instanceof Department)) {
            throw new IllegalArgumentException();
        }

        if (!departmentList.contains(department)) {
            departmentList.add((Department) department);
        }
    }

    @Override
    public void removeDepartment(IDepartment department) {

        if (!(department instanceof Department)) {
            throw new IllegalArgumentException();
        }

        if (departmentList.isEmpty()) {
            departmentList.remove(department);
        }
    }

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
        name = "teamMembers",
        joinColumns = @JoinColumn(name = "memberId"),
        inverseJoinColumns = @JoinColumn(name = "teamId")
    )
    public List<Team> getTeams() {
        return teamList;
    }

    public void setTeams(List<Team> teamList) {
        this.teamList = teamList;
    }

    @Override
    public void addTeam(ITeam team) {

        if (!(team instanceof Team)) {
            throw new IllegalArgumentException();
        }

        if (!teamList.contains(team)) {
            teamList.add((Team) team);
        }
    }

    @Override
    public void removeTeam(ITeam team) {

        if (!(team instanceof Team)) {
            throw new IllegalArgumentException();
        }

        if (teamList.isEmpty()) {
            teamList.remove(team);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Member member = (Member) o;

        if (memberId != null ? !memberId.equals(member.memberId) : member.memberId != null) return false;
        if (firstname != null ? !firstname.equals(member.firstname) : member.firstname != null) return false;
        if (lastname != null ? !lastname.equals(member.lastname) : member.lastname != null) return false;
        if (gender != null ? !gender.equals(member.gender) : member.gender != null) return false;
        if (dateOfBirth != null ? !dateOfBirth.equals(member.dateOfBirth) : member.dateOfBirth != null) return false;
        if (eMail != null ? !eMail.equals(member.eMail) : member.eMail != null) return false;
        if (address != null ? !address.equals(member.address) : member.address != null) return false;
        if (squad != null ? !squad.equals(member.squad) : member.squad != null) return false;
        if (role != null ? !role.equals(member.role) : member.role != null) return false;
        if (username != null ? !username.equals(member.username) : member.username != null) return false;
        if (isFeePaid != null ? isFeePaid.equals(member.isFeePaid) : member.isFeePaid != null) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = memberId != null ? memberId.hashCode() : 0;
        result = 31 * result + (firstname != null ? firstname.hashCode() : 0);
        result = 31 * result + (lastname != null ? lastname.hashCode() : 0);
        result = 31 * result + (gender != null ? gender.hashCode() : 0);
        result = 31 * result + (dateOfBirth != null ? dateOfBirth.hashCode() : 0);
        result = 31 * result + (eMail != null ? eMail.hashCode() : 0);
        result = 31 * result + (address != null ? address.hashCode() : 0);
        result = 31 * result + (squad != null ? squad.hashCode() : 0);
        result = 31 * result + (role != null ? role.hashCode() : 0);
        result = 31 * result + (username != null ? username.hashCode() : 0);
        result = 31 * result + (isFeePaid != null ? isFeePaid.hashCode() : 0);
        return result;
    }
}
