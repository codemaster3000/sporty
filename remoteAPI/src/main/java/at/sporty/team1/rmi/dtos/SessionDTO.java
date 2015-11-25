package at.sporty.team1.rmi.dtos;

import at.sporty.team1.rmi.api.IDTO;

/**
 * Created by sereGkaluv on 25-Nov-15.
 */
public class SessionDTO implements IDTO {
    private static final long serialVersionUID = 1L;

    private MemberDTO _member;
    private byte[] _clientFingerprint;

    public MemberDTO getMember() {
        return _member;
    }

    public SessionDTO setMember(MemberDTO member) {
        _member = member;
        return this;
    }

    public byte[] getClientFingerprint() {
        return _clientFingerprint;
    }

    public SessionDTO setClientFingerprint(byte[] clientFingerprint) {
        _clientFingerprint = clientFingerprint;
        return this;
    }
}
