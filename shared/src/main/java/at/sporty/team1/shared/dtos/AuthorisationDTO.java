package at.sporty.team1.shared.dtos;

import at.sporty.team1.shared.api.entity.IDTO;

/**
 * This IDTO Object implements Object Builder pattern.
 */
public class AuthorisationDTO implements IDTO {
    private static final long serialVersionUID = 1L;

    private byte[] _clientPublicKey;
    private byte[] _encryptedUserLogin;
    private byte[] _encryptedUserPassword;

    public AuthorisationDTO() {
    }

    public byte[] getClientPublicKey() {
        return _clientPublicKey;
    }

    public AuthorisationDTO setClientPublicKey(byte[] clientPublicKey) {
        _clientPublicKey = clientPublicKey;
        return this;
    }

    public byte[] getEncryptedUserLogin() {
        return _encryptedUserLogin;
    }

    public AuthorisationDTO setEncryptedUserLogin(byte[] encryptedUserLogin) {
        _encryptedUserLogin = encryptedUserLogin;
        return this;
    }

    public byte[] getEncryptedUserPassword() {
        return _encryptedUserPassword;
    }

    public AuthorisationDTO setEncryptedUserPassword(byte[] encryptedUserPassword) {
        _encryptedUserPassword = encryptedUserPassword;
        return this;
    }
}
