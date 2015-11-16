package at.sporty.team1.domain.readonly;

/**
 * Created by f00 on 03.11.15.
 */
public interface IRMatch  {
    String getDate();

    String getLocation();

    String getReferee();

	String getTeam1();

	String getTeam2();

	String getResult();
}
