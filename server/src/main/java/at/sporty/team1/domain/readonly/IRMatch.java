package at.sporty.team1.domain.readonly;

/**
 * Created by f00 on 03.11.15.
 */
public interface IRMatch {

    Integer getMatchId();

    String getMatchResult();

    String getDate();

    String getTeam2();

    String getTeam1();

    String getLocation();

    String getResult();

    String getReferee();
}
