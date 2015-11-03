package at.sporty.team1.domain.readonly;

import at.sporty.team1.domain.MatchResult;
import at.sporty.team1.domain.Team;

import java.util.Date;

/**
 * Created by f00 on 03.11.15.
 */
public interface IRMatch  {
    Date getDate();

    Team getGuestTeam();

    Team getHomeTeam();

    String getLocation();

    MatchResult getMatchResult();

    String getReferee();
}
