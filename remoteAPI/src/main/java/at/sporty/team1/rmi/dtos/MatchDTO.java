package at.sporty.team1.rmi.dtos;

import at.sporty.team1.rmi.api.IDTO;

public class MatchDTO implements IDTO {
	private static final long serialVersionUID = 1L;

    private Integer _matchId;
	private String _team1;
    private String _team2;
    private String _referee;
    private String _location;
	private String _date;
    private String _result;
   
    public MatchDTO() {   	
    }

    public Integer getMatchId() {
        return _matchId;
    }

    public MatchDTO setMatchId(Integer matchId) {
        _matchId = matchId;
        return this;
    }

    public String getLocation() {
		return _location;
	}

	public MatchDTO setLocation(String location) {
		_location = location;
		return this;
	}
    
    public String getDate() {
        return _date;
    }

    public MatchDTO setDate(String date) {
        _date = date;
        return this;
    }

    public String getTeam2() {
        return _team2;
    }

    public MatchDTO setTeam2(String team2) {
        _team2 = team2;
        return this;
    }
    
    public String getTeam1(){
    	return _team1;
    }

    public MatchDTO setTeam1(String team1) {
        _team1 = team1;
        return this;
    }

    public String getReferee() {
        return _referee;
    }

    public MatchDTO setReferee(String referee) {
        _referee = referee;
        return this;
    }

	public String getResult() {
		return _result;
	}

	public MatchDTO setResult(String result) {
		_result = result;
		return this;
	}
}
