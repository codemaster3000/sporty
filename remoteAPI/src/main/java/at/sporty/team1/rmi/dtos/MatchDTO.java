package at.sporty.team1.rmi.dtos;

import at.sporty.team1.rmi.api.IDTO;

public class MatchDTO implements IDTO {
    
	private static final long serialVersionUID = 1L;
	
	private String _team1;
    private String _team2;
    private String _referee;
    private String _location;
	private String _time; //TODO why 'time'? isn't this rather 'date' -> in Matches it is only 'time'. Tournament have a date.
	private String _result;
   
    public MatchDTO() {   	
    }

    public String getLocation() {
		return _location;
	}

	public void setLocation(String location) {
		_location = location;
	}
    
    public String getTime() {
        return _time;
    }

    public void setTime(String time) {
        _time = time;
    }

    public String getTeam2() {
        return _team2;
    }

    public void setTeam2(String team2) {
        _team2 = team2;
    }
    
    public String getTeam1(){
    	return _team1;
    }

    public void setTeam1(String team1) {
        _team1 = team1;
    }

    public String getReferee() {
        return _referee;
    }

    public void setReferee(String referee) {
        _referee = referee;
    }

	public String getResult() {
		return _result;
	}

	public void setResult(String result) {
		_result = result;
	}
}
