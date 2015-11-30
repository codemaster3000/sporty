package at.sporty.team1.domain;

import at.sporty.team1.domain.interfaces.IMember;
import at.sporty.team1.domain.interfaces.ITeam;

import javax.persistence.*;
import java.util.List;

/**
 *
 */
@Entity
@Table(name = "team")
public class Team implements ITeam {
    private Integer teamId;
    private Member trainer;
    private Department department;
    private League league;
    private String teamname;
    private Boolean isTournamentSquad;
    private List<Member> memberList;

    public Team() {
    }

    @Override
    @Id
    @GeneratedValue
    @Column(name = "teamId")
    public Integer getTeamId() {
        return teamId;
    }

    @Override
    public void setTeamId(Integer teamId) {
        this.teamId = teamId;
    }

    @Override
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "trainerId")
    public Member getTrainer() {
        return trainer;
    }

    @Override
    public void setTrainer(Member trainer) {
        this.trainer = trainer;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "departmentId")
    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    //TODO League table in db
//    @Override
//    @ManyToOne(fetch = FetchType.EAGER)
//    @JoinColumn(name = "leagueId")
//    public League getLeague() {
//        return league;
//    }
//
//    @Override
//    public void setLeague(League league) {
//        this.league = league;
//    }

    @Override
    @Basic
    @Column(name = "teamName")
    public String getTeamName() {
        return teamname;
    }

    @Override
    public void setTeamName(String teamName) {
        this.teamname = teamName;
    }

    @Override
    @Basic
    @Column(name = "isTournamentSquad")
    public Boolean getIsTournamentSquad() {
        return isTournamentSquad;
    }

    @Override
    public void setIsTournamentSquad(Boolean isTournamentSquad) {
        this.isTournamentSquad = isTournamentSquad;
    }

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
            name = "teamMembers",
            joinColumns = @JoinColumn(name = "teamId"),
            inverseJoinColumns = @JoinColumn(name = "memberId")
    )
    public List<Member> getMembers() {
        return memberList;
    }

    public void setMembers(List<Member> memberList) {
        this.memberList = memberList;
    }

    @Override
    public void addMember(IMember member) {

        if (!(member instanceof Member)) {
            throw new IllegalArgumentException();
        }

        if (!memberList.contains(member)) {
            memberList.add((Member) member);
        }
    }

    @Override
    public void removeMember(IMember member) {

        if (!(member instanceof Member)) {
            throw new IllegalArgumentException();
        }

        if (memberList.isEmpty()) {
            memberList.remove(member);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Team team = (Team) o;

        if (teamId != null ? !teamId.equals(team.teamId) : team.teamId != null) return false;
        if (teamname != null ? !teamname.equals(team.teamname) : team.teamname != null) return false;
        if (isTournamentSquad != null ? !isTournamentSquad.equals(team.isTournamentSquad) : team.isTournamentSquad != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = teamId != null ? teamId.hashCode() : 0;
        result = 31 * result + (teamname != null ? teamname.hashCode() : 0);
        result = 31 * result + (isTournamentSquad != null ? isTournamentSquad.hashCode() : 0);
        return result;
    }
}
