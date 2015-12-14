package at.sporty.team1.domain.readonly;

import at.sporty.team1.domain.Tournament;

/**
 * Created by f00 on 03.11.15.
 */
public interface IRMatch {

    Integer getMatchId();

    String getLocation();

    String getDate();

    String getTeam1();

    String getTeam2();

    String getReferee();

    String getResultTeam1();

    String getResultTeam2();

    Tournament getTournament();
}
