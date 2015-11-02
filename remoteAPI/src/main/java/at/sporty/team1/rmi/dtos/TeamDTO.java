package at.sporty.team1.rmi.dtos;

import at.sporty.team1.rmi.api.IDTO;

import java.io.Serializable;
import java.util.List;

/**
 * Created by sereGkaluv on 02-Nov-15.
 */
public class TeamDTO implements Serializable, IDTO {
    private List<MemberDTO> _memberList;

    private String _teamName;

    public TeamDTO() {
    }

    public List<MemberDTO> getMemberList() {
        return _memberList;
    }

    public void setMemberList(List<MemberDTO> memberList) {
        _memberList = memberList;
    }

    public String getTeamName() {
        return _teamName;
    }

    public void setTeamName(String teamName) {
        _teamName = teamName;
    }
}
