package at.sporty.team1.domain;

import at.sporty.team1.domain.interfaces.ITeam;

import javax.persistence.*;
import java.util.List;

/**
 *
 */
@Entity
@PrimaryKeyJoinColumn(name = "teamId", referencedColumnName = "teamId")
@Table(name = "team")
public class Team implements ITeam {
    public int teamId;
    public String teamname;
    public Department department;
    public Member trainer;
    public League league;
    public List<Member> members;

    @Override
    @Id
    @Column(name = "teamId")
    public int getTeamId() {
        return teamId;
    }

    @Override
    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }

    @Override
    @OneToMany(mappedBy="owner", fetch = FetchType.LAZY)
    public List<Member> getMembers() {
        return members;
    }

    @Override
    public void setMembers(List<Member> members) {
        this.members = members;
    }

    @Override
    @Basic
    @Column(name = "department") //TODO refactor
    public Department getDepartment() {
        return department;
    }

    @Override
    public void setDepartment(Department department) {
        this.department = department;
    }

    @Override
    @Basic
    @Column(name = "teamName")
    public String getTeamName() {
        return teamname;
    }

    @Override
    public void setTeamName(String teamname) {
        this.teamname = teamname;
    }

    @Override
    @Basic
    @Column(name = "trainer")
    public Member getTrainer() {
        return trainer;
    }

    public void setTrainer(Member trainer) {
        this.trainer = trainer;
    }


    @Override
    @Basic
    @Column(name = "league")
    public League getLeague() {
        return league;
    }

    @Override
    public void setLeague(League league) {
        this.league = league;
    }

}
