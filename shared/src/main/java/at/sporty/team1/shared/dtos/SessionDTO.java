package at.sporty.team1.shared.dtos;

import at.sporty.team1.shared.api.entity.IDTO;

/**
 * Created by sereGkaluv on 25-Nov-15.
 */
public class SessionDTO implements IDTO {
    private static final long serialVersionUID = 1L;

    private Integer _userId;
    private byte[] _clientFingerprint;

    public Integer getUserId() {
        return _userId;
    }

    public SessionDTO setUserId(Integer userId) {
        _userId = userId;
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
