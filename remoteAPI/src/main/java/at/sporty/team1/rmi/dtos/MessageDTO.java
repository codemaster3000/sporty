package at.sporty.team1.rmi.dtos;

import at.sporty.team1.rmi.api.IDTO;

/**
 * Created by sereGkaluv on 27-Nov-15.
 */
public class MessageDTO implements IDTO {
    private static final long serialVersionUID = 1L;

    private Integer _memberId;
    private byte[] _clientFingerprint;

    public Integer getMemberId() {
        return _memberId;
    }

    public MessageDTO setMemberId(Integer memberId) {
        _memberId = memberId;
        return this;
    }

    public byte[] getClientFingerprint() {
        return _clientFingerprint;
    }

    public MessageDTO setClientFingerprint(byte[] clientFingerprint) {
        _clientFingerprint = clientFingerprint;
        return this;
    }
}
