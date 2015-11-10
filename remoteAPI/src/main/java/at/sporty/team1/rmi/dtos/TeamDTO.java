package at.sporty.team1.rmi.dtos;

import at.sporty.team1.rmi.api.IDTO;

import java.util.List;

/**
 * This IDTO Object implements Object Builder pattern.
 */
public class TeamDTO implements IDTO {
    public Integer _teamId;
    public MemberDTO _trainer;
    public DepartmentDTO _department;
    public Integer _league; //TODO replace with league DTO
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

    public MemberDTO getTrainer() {
        return _trainer;
    }

    public TeamDTO setTrainer(MemberDTO trainer) {
        _trainer = trainer;
        return this;
    }

    @Deprecated // not supported yet
    public DepartmentDTO getDepartment() {
        return _department;
    }

    @Deprecated // not supported yet
    public TeamDTO setDepartment(DepartmentDTO department) {
        _department = department;
        return this;
    }

    @Deprecated // not supported yet
    public Integer getLeague() {
        return _league;
    }

    @Deprecated // not supported yet
    public TeamDTO setLeague(Integer league) {
        _league = league;
        return this;
    }

    public String getTeamName() {
        return _teamName;
    }

    public TeamDTO setTeamName(String teamName) {
        _teamName = teamName;
        return this;
    }

    @Deprecated // not supported yet
    public List<MemberDTO> getMemberList() {
        return _memberList;
    }

    @Deprecated // not supported yet
    public TeamDTO setMemberList(List<MemberDTO> memberList) {
        _memberList = memberList;
        return this;
    }
}
