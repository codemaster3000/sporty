package at.sporty.team1.rmi.dtos;

import at.sporty.team1.rmi.api.IDTO;

/**
 * This IDTO Object implements Object Builder pattern.
 */
public class TeamDTO implements IDTO {
    private Integer _teamId;
    private MemberDTO _trainer;
    private DepartmentDTO _department;
    private Integer _league; //TODO replace with league DTO
    private String _teamName;
    private Boolean _isTournamentSquad;

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

    public DepartmentDTO getDepartment() {
        return _department;
    }

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

    public Boolean getIsTournamentSquad() {
        return _isTournamentSquad;
    }

    public TeamDTO setIsTournamentSquad(Boolean isTournamentSquad) {
        _isTournamentSquad = isTournamentSquad;
        return this;
    }
}
