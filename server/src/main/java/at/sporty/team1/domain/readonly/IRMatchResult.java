package at.sporty.team1.domain.readonly;

import at.sporty.team1.domain.Team;

/**
 * Created by f00 on 03.11.15.
 */
public interface IRMatchResult {

    String getComment();

    Integer getPointsA();

    Integer getPointsB();

    Team getTeamA();

    Team getWinner();

    Team getTeamB();
}
