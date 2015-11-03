package at.sporty.team1.domain;

import java.util.Date;
import java.util.regex.MatchResult;

/**
 * Created by f00 on 03.11.15.
 *  TODO hibernatemapping
 */
public class Match {

    public Date date;
    public Team homeTeam;
    public Team guestTeam;
    public MatchResult matchResult;
    public String location;
    public String referee;
}
