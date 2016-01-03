package at.sporty.team1.shared.security;

import at.sporty.team1.shared.api.IBiThrowingFunction;
import at.sporty.team1.shared.dtos.AuthorisationDTO;
import at.sporty.team1.shared.dtos.SessionDTO;
import at.sporty.team1.shared.enums.EncryptionMethod;
import at.sporty.team1.shared.exceptions.RemoteCommunicationException;
import at.sporty.team1.shared.exceptions.SecurityException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

/**
 * Created by sereGkaluv on 25-Nov-15.
 */
public class SecurityModule {
    public static final int DEFAULT_KEY_SIZE = 512;

    public static SessionDTO authorize(
        String username,
        String password,
        KeyPair clientKeyPair,
        PublicKey serverPublicKey,
        IBiThrowingFunction<AuthorisationDTO, SessionDTO, RemoteCommunicationException, SecurityException> authorisationProxy
    ) throws SecurityException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, RemoteCommunicationException {

        //Reading username and password
        byte[] rawUsername = username.getBytes();
        byte[] rawPassword = password.getBytes();

        //Preparing for data encryption
        Cipher cipher = SecurityModule.getNewRSACipher();
        cipher.init(Cipher.ENCRYPT_MODE, serverPublicKey);

        AuthorisationDTO authorisationDTO = new AuthorisationDTO()
            .setEncryptedUserLogin(cipher.doFinal(rawUsername))
            .setEncryptedUserPassword(cipher.doFinal(rawPassword))
            .setClientPublicKey(SecurityModule.getEncodedRSAPublicKey(clientKeyPair));

        //Getting authorisation result
        SessionDTO session = authorisationProxy.apply(authorisationDTO);
        if (session != null) {
            //Decrypting client fingerprint for client side
            cipher.init(Cipher.DECRYPT_MODE, clientKeyPair.getPrivate());
            byte[] decodedFingerprint = cipher.doFinal(session.getClientFingerprint());

            //Encrypting client fingerprint for server side
            cipher.init(Cipher.ENCRYPT_MODE, serverPublicKey);
            byte[] clientFingerprint = cipher.doFinal(decodedFingerprint);

            session.setClientFingerprint(clientFingerprint);
        }

        return session;
    }

    public static KeyPair generateNewRSAKeyPair(int keySize)
    throws SecurityException {
        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance(EncryptionMethod.RSA.getMethod());
            generator.initialize(keySize, new SecureRandom());

            return generator.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            throw new SecurityException(e.getMessage());
        }
    }

    public static Cipher getNewRSACipher()
    throws SecurityException {
        try {
            return Cipher.getInstance(EncryptionMethod.RSA.getMethod());
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new SecurityException(e.getMessage());
        }
    }

    public static byte[] getEncodedRSAPublicKey(KeyPair keyPair) {
        return new X509EncodedKeySpec(keyPair.getPublic().getEncoded()).getEncoded();
    }

    public static PublicKey getDecodedRSAPublicKey(byte[] encodedPublicKey)
    throws SecurityException {
        try {
            return KeyFactory.getInstance(EncryptionMethod.RSA.getMethod())
                    .generatePublic(new X509EncodedKeySpec(encodedPublicKey));
        } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
            throw new SecurityException(e.getMessage());
        }
    }

    /**
     * returns a sha512 hashed Byte[] from the input String
     * used to hash passwords in a safe way
     *
     * @param input string to be hashed
     * @return Byte[] resulting hash
     */
    public static byte[] getSHA512Value(String input)
    throws SecurityException {
        try {

            MessageDigest digest = MessageDigest.getInstance(EncryptionMethod.SHA512.getMethod());
            return digest.digest(input.getBytes());

        } catch (NoSuchAlgorithmException e) {
            throw new SecurityException(e.getMessage());
        }
    }
}
