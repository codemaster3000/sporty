package at.sporty.team1.rmi.dtos;

import at.sporty.team1.rmi.api.IDTO;

import java.io.Serializable;
import java.util.List;

/**
 * Created by sereGkaluv on 02-Nov-15.
 * TODO!
 */
public class TeamDTO implements Serializable, IDTO {
    public Integer _teamId;
    public Integer _trainerId;
    public Integer _departmentId;
    public Integer _leagueId;
    private String _teamName;
    private List<MemberDTO> _memberList;

    public TeamDTO() {
    }

    public Integer getTeamId() {
        return _teamId;
    }

    public TeamDTO setTeamId(Integer teamId) {
        _teamId = teamId;
        return this;
    }

    public Integer getTrainerId() {
        return _trainerId;
    }

    public TeamDTO setTrainerId(Integer trainerId) {
        _trainerId = trainerId;
        return this;
    }

    public Integer getDepartmentId() {
        return _departmentId;
    }

    public TeamDTO setDepartmentId(Integer departmentId) {
        _departmentId = departmentId;
        return this;
    }

    public Integer getLeagueId() {
        return _leagueId;
    }

    public TeamDTO setLeagueId(Integer leagueId) {
        _leagueId = leagueId;
        return this;
    }

    public String getTeamName() {
        return _teamName;
    }

    public TeamDTO setTeamName(String teamName) {
        _teamName = teamName;
        return this;
    }

    public List<MemberDTO> getMemberList() {
        return _memberList;
    }

    public TeamDTO setMemberList(List<MemberDTO> memberList) {
        _memberList = memberList;
        return this;
    }
}
