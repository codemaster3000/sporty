package at.sporty.team1.domain.interfaces;

import at.sporty.team1.domain.Tournament;
import at.sporty.team1.domain.readonly.IRMatch;

/**
 * Created by f00 on 03.11.15.
 */
public interface IMatch extends IRMatch {

    void setMatchId(Integer matchId);

    void setLocation(String location);

    void setDate(String date);

    void setTeam1(String team1);

    void setTeam2(String team2);

    void setResultTeam1(String resultTeam1);

    void setResultTeam2(String resultTeam2);

    void setReferee(String referee);

    void setTournament(Tournament tournament);
}
