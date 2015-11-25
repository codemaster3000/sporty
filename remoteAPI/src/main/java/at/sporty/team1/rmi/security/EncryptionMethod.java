package at.sporty.team1.rmi.security;

/**
 * Created by sereGkaluv on 25-Nov-15.
 */
public enum EncryptionMethod {
    RSA("RSA"),
    SHA512("SHA-512");

    private final String _encryptionMethod;

    EncryptionMethod(String encryptionMethod) {
        _encryptionMethod = encryptionMethod;
    }

    public String getMethod() {
        return _encryptionMethod;
    }
}