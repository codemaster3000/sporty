package at.sporty.team1.domain.interfaces;

import at.sporty.team1.domain.readonly.IRMatch;

/**
 * Created by f00 on 03.11.15.
 */
public interface IMatch extends IRMatch {

    void setMatchId(Integer matchId);


    void setMatchResult(String matchResult);


    void setDate(String date);


    void setTeam2(String team2);


    void setTeam1(String team1);


    void setLocation(String location);


    void setResult(String matchResult);


    void setReferee(String referee);


}
