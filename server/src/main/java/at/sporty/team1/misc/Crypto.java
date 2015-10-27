package at.sporty.team1.misc;

import at.sporty.team1.logging.Loggers;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * Created by Jasper Reichardt on 05.04.15.
 * Used to perform various hashing operations on Data
 * e.g. Doctors PW is stored as sha512 hash in the db, on login the typed-in PW is then sha512-hashed and compared to
 * the PW-hash in the DB, if they match, the password is correct
 */

public class Crypto {

    /**
     * returns a sha512 hashed Byte[] from the input String
     * used to hash passwords in a safe way
     *
     * @param input string to be hashed
     * @return Byte[] resulting hash
     */
    public static byte[] sha512(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-512");
            return digest.digest(input.getBytes());

        } catch (NoSuchAlgorithmException e) {
            //sample of how to use Loggers class
            Loggers.DEBUG.error("Error, sha512ing " + input
                    + ". Algorithm for SHA-512 not found.");
            throw new UnsupportedOperationException(e);
        }
    }

    public static String md5(String input) {

        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            Loggers.DEBUG.error("Error, md5ing " + input
                    + ". Algorithm for md5ing not found.");
            throw new UnsupportedOperationException(e);
        }

        byte[] bytes = md.digest(input.getBytes());

        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b & 0xff));
        }

        return sb.toString();
    }

    /**
     * utility tool; byte[] to HexString
     *
     * @param bytes byte[] to be parsed to hex-String
     * @return HexString representing the sha512(byte[])
     */

    public static String bytesToHex(byte[] bytes) {
        final char[] hexArray = "0123456789ABCDEF".toCharArray();

        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    // Various Encoders
    public static byte[] utf8Encode(String toUtf8) {
        return Charset.forName("UTF-8").encode(toUtf8).array();
    }

    public static byte[] base64Decode(String input) {
        return Base64.getDecoder().decode(input);
    }

    public static byte[] base64Decode(byte[] input) {
        return Base64.getDecoder().decode(input);
    }

    public static String base64Encode(byte[] input) {
        return Base64.getEncoder().encodeToString(input);
    }

//    // for the HMAC
//    public static byte[] sha512HMAC(byte[] input, byte[] bytesKey) {
//        return doHMAC(input, bytesKey, "HmacSHA512");
//    }
//
//    public static byte[] doHMAC(byte[] input, byte[] bytesKey, String algorithm) {
//        try {
//            Mac mac;
//
//            final SecretKeySpec secretKey = new SecretKeySpec(bytesKey,
//                    algorithm);
//            mac = Mac.getInstance(algorithm);
//            mac.init(secretKey);
//
//            return mac.doFinal(input);
//
//        } catch (final NoSuchAlgorithmException e) {
//            Loggers.DEBUG.error("Error, " + algorithm + "-HMACing " + input
//                    + ". Algorithm for " + algorithm + " not found.");
//            throw new UnsupportedOperationException(e);
//
//        } catch (InvalidKeyException e) {
//            Loggers.DEBUG.error("Error, " + algorithm + " " + input
//                    + ". InvalidKey .");
//            throw new UnsupportedOperationException(e);
//        }
//
//    }
}