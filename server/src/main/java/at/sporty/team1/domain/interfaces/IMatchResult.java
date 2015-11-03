package at.sporty.team1.domain.interfaces;

import at.sporty.team1.domain.Team;
import at.sporty.team1.domain.readonly.IRMatchResult;

/**
 * Created by f00 on 03.11.15.
 */
public interface IMatchResult extends IRMatchResult {
    void setComment(String comment);

    void setPointsA(Integer pointsA);

    void setPointsB(Integer pointsB);

    void setTeamA(Team teamA);

    void setWinner(Team winner);

    void setTeamB(Team teamB);
}
