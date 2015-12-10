package at.sporty.team1.util;

import at.sporty.team1.shared.dtos.MemberDTO;
import at.sporty.team1.shared.dtos.SessionDTO;

/**
 * Created by sereGkaluv on 28-Nov-15.
 */
public class CachedSession {
    private final MemberDTO _memberDTO;
    private final SessionDTO _sessionDTO;

    public CachedSession(MemberDTO user, SessionDTO sessionDTO) {
        _memberDTO = user;
        _sessionDTO = sessionDTO;
    }

    public MemberDTO getUser() {
        return _memberDTO;
    }

    public SessionDTO getSessionDTO() {
        return _sessionDTO;
    }
}
