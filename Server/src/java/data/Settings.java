/*
 * This code is created for one purpose only. And should not be used for any 
 * other purposes unless the author of this file has apporved. 
 * 
 * This code is a piece of a project in the course DT142G on Mid. Sweden university
 * Created by students for this projekt only
 */
package data;

import java.security.Key;
import java.util.Date;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * This class has the information about the server attached to it
 *
 * @author Johannes Lindén
 * @since 2014-10-07
 * @version 1.0
 */
public class Settings {
    static final public String imagePath = "/var/dt142g/uploads/";
    static final public String[] allowedKeys = new String[]{"dt142g-awesome"};
    static final public String cryptionValue = "dt142g!Key()##!3"; // 128 bit key
    static final public String tempPrefixKey = "enroligtemporarnyckel";

    static final public class AuthCode {

        public static final int accept = 0;
        public static final int deny = 1;
        public static final int expired = 2;
    }

    public static String asHex(byte buf[]) {
        StringBuilder strbuf = new StringBuilder(buf.length * 2);
        int i;

        for (i = 0; i < buf.length; i++) {
            if (((int) buf[i] & 0xff) < 0x10) {
                strbuf.append("0");
            }

            strbuf.append(Long.toString((int) buf[i] & 0xff, 16));
        }

        return strbuf.toString();
    }

    public static byte[] fromHexString(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

    public static int isAutorised(String parameter) {
        try {
            System.out.println(parameter);
            Key aesKey = new SecretKeySpec(Settings.cryptionValue.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, aesKey);
            String decrypted = new String(cipher.doFinal(Settings.fromHexString(parameter)));

            String timeStr = decrypted.substring(Settings.tempPrefixKey.length());
            if (decrypted.indexOf(Settings.tempPrefixKey) == 0) {
                if (Long.parseLong(timeStr) + 1000 * 3600 >= new Date().getTime()) {
                    return AuthCode.accept;
                } else {
                    return AuthCode.expired;
                }
            }
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
        return AuthCode.deny;
    }
}
