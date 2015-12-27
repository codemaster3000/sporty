package at.sporty.team1.shared.dtos;

import at.sporty.team1.shared.api.entity.IDTO;

/**
 * This IDTO Object implements Object Builder pattern.
 */
public class TeamDTO implements IDTO {
    private static final long serialVersionUID = 1L;

    private Integer _teamId;
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
