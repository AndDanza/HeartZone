package air1715.pillcare.Utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * The type Encryption utils.
 */
public class EncryptionUtils {
    /**
     * Sha 1 string.
     *
     * @param input the input
     * @return the string
     * @throws NoSuchAlgorithmException the no such algorithm exception
     */
    public static String sha1(String input) throws NoSuchAlgorithmException {
        MessageDigest mDigest = MessageDigest.getInstance("SHA1");
        byte[] result = mDigest.digest(input.getBytes());
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < result.length; i++) {
            sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
        }

        return sb.toString();
    }
}
