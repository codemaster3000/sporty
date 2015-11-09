package at.sporty.team1.domain;

import at.sporty.team1.domain.interfaces.ITeam;

import javax.persistence.*;
import java.util.List;

/**
 *
 */
@Entity
@Table(name = "team")
public class Team implements ITeam {
    public Integer teamId;
    public Member trainer;
    public Department department;
    public League league;
    public String teamname;
    public List<Member> memberList;



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
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trainerId")
    public Member getTrainer() {
        return trainer;
    }

    @Override
    public void setTrainer(Member trainer) {
        this.trainer = trainer;
    }

//    @Override
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "departmentId")
//    public Department getDepartment() {
//        return department;
//    }
//
//    @Override
//    public void setDepartment(Department department) {
//        this.department = department;
//    }

    //TODO League table in db
//    @Override
//    @ManyToOne(fetch = FetchType.LAZY)
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

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId")
    public List<Member> getMemberList() {
        return memberList;
    }

    public void setMemberList(List<Member> memberList) {
        this.memberList = memberList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Team team = (Team) o;

        if (teamId != null ? !teamId.equals(team.teamId) : team.teamId != null) return false;
        if (trainer != null ? !trainer.equals(team.trainer) : team.trainer != null) return false;
        if (department != null ? !department.equals(team.department) : team.department != null) return false;
//        if (league != null ? !league.equals(team.league) : team.league != null) return false;
        if (teamname != null ? !teamname.equals(team.teamname) : team.teamname != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = teamId != null ? teamId.hashCode() : 0;
        result = 31 * result + (trainer != null ? trainer.hashCode() : 0);
        result = 31 * result + (department != null ? department.hashCode() : 0);
//        result = 31 * result + (league != null ? league.hashCode() : 0);
        result = 31 * result + (teamname != null ? teamname.hashCode() : 0);
        return result;
    }
}
