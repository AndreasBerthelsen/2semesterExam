package dk.easv.Util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.IllegalFormatCodePointException;

public class PasswordHash {

    /**
     * Method that hashes the given password
     * @param password
     * @param salt
     * @return
     */

    public static String hashPassword(String password, byte[] salt) {
        try {
            String base = password;
            MessageDigest MD = null;
            MD = MessageDigest.getInstance("SHA-512");
            MD.update(salt);
            byte[] hash = MD.digest(base.getBytes(StandardCharsets.UTF_8));
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if (hex.length() == 1) hexString.append(0); {
                    hexString.append(hex);
                }
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return password;
    }

    /**
     * If a given text password matches a hashed password then the method returns true
     * @param plainTextPassword
     * @param hashedPassword
     * @param salt
     * @return
     */

    public static boolean verifyPassword(String plainTextPassword, String hashedPassword, byte[] salt) {
        return hashPassword(plainTextPassword, salt).equals(hashedPassword);
    }

    public static byte[] getSalt() throws NoSuchAlgorithmException {
        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[16];
        secureRandom.nextBytes(salt);
        return salt;
    }

}
