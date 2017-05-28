package joseph.com.antinasakeyboard;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;

/**
 * Created by Brian on 5/27/2017.
 */

public class Encryption {

    static AesCbcWithIntegrity.SecretKeys keys;
    static final String defaultKey = "Ecz9kAlliIFfzDvUuXrCnw==:8agdR5rNWomnfubeo8XXTciNtQc4BTd2Wsbqsx2uJPs=";

    public static void initializeEncryption() {
        try {
            keys = AesCbcWithIntegrity.keys(defaultKey);
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }

    }

    public static String encrypt(String plainText) {
        AesCbcWithIntegrity.CipherTextIvMac cipherTextIvMac = null;
        try {
            cipherTextIvMac = AesCbcWithIntegrity.encrypt(plainText, keys);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        //store or send to server
        String ciphertextString = cipherTextIvMac.toString();
        return ciphertextString;
    }

    public static String decrypt(String encryptedText) {
        AesCbcWithIntegrity.CipherTextIvMac cipherTextIvMac = new AesCbcWithIntegrity.CipherTextIvMac(encryptedText);
        try {
            String plainText = AesCbcWithIntegrity.decryptString(cipherTextIvMac, keys);
            return plainText;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        return "";
    }


}
