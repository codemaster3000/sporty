package at.sporty.team1.rmi.dtos;

import at.sporty.team1.rmi.api.IDTO;

/**
 * Created by sereGkaluv on 25-Nov-15.
 */
public class SessionDTO implements IDTO {
    private static final long serialVersionUID = 1L;

    private Integer _memberId;
    private byte[] _clientFingerprint;

    public Integer getMemberId() {
        return _memberId;
    }

    public SessionDTO setMemberId(Integer memberId) {
        _memberId = memberId;
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
