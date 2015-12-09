package at.sporty.team1.rmi.security;

import at.sporty.team1.rmi.exceptions.SecurityException;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

/**
 * Created by sereGkaluv on 25-Nov-15.
 */
public class SecurityModule {

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
