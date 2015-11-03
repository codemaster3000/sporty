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
    public Integer teamId;
    public Integer trainerId;
    public Integer departmentId;
    public Integer leagueId;
    public String teamname;
    public List<Member> memberList;

    @Override
    @Id
    @Column(name = "teamId")
    public Integer getTeamId() {
        return teamId;
    }

    @Override
    public void setTeamId(Integer teamId) {
        this.teamId = teamId;
    }

    @Override
    @Basic
    @Column(name = "trainerId")
    public Integer getTrainerId() {
        return trainerId;
    }

    public void setTrainerId(Integer trainerId) {
        this.trainerId = trainerId;
    }

    @Override
    @Basic
    @Column(name = "departmentId") // TODO many to one && return: List<Department>
    public Integer getDepartmentId() {
        return departmentId;
    }

    @Override
    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }

    @Override
    @Basic
    @Column(name = "leagueId")
    public Integer getLeagueId() {
        return leagueId;
    }

    @Override
    public void setLeagueId(Integer leagueId) {
        this.leagueId = leagueId;
    }

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
    @OneToMany(mappedBy="teamId", fetch = FetchType.LAZY) // TODO check
    public List<Member> getMemberList() {
        return memberList;
    }

    @Override
    public void setMemberList(List<Member> memberList) {
        this.memberList = memberList;
    }
}
