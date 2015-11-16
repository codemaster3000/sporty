package at.sporty.team1.rmi.dtos;

import at.sporty.team1.rmi.api.IDTO;

public class MatchDTO implements IDTO {

    private String _team1;
    private String _team2;
    private String _referee;
    private String _court;
	private String _time;
	private String _result;
   
    public MatchDTO() {   	
    }

    public String getCourt() {
		return _court;
	}

	public void setCourt(String court) {
		this._court = court;
	}
    
    public String getTime() {
        return _time;
    }

    public void setTime(String time) {
        this._time = time;
    }

    public String getTeam2() {
        return _team2;
    }

    public void setTeam2(String team2) {
        this._team2 = team2;
    }
    
    public String getTeam1(){
    	return _team1;
    }

    public void setTeam1(String team1) {
        this._team1 = team1;
    }

    public String getReferee() {
        return _referee;
    }

    public void setReferee(String referee) {
        this._referee = referee;
    }

	public String getResult() {
		return _result;
	}

	public void setResult(String _result) {
		this._result = _result;
	}
}
