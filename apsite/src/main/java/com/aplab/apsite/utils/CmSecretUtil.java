package com.aplab.apsite.utils;

import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class CmSecretUtil {

    private Cipher AES_ENC_CIPHER;
    private Cipher AES_DEC_CIPHER;

    private static CmSecretUtil instance;

    private CmSecretUtil() {
        try {
            String aesKey = "ishift7150tfihsi";
            String transformation = "AES/ECB/PKCS5PADDING";
            SecretKeySpec skeySpec = new SecretKeySpec(aesKey.getBytes(), "AES");

            AES_ENC_CIPHER = Cipher.getInstance(transformation);
            AES_ENC_CIPHER.init(Cipher.ENCRYPT_MODE, skeySpec);

            AES_DEC_CIPHER = Cipher.getInstance(transformation);
            AES_DEC_CIPHER.init(Cipher.DECRYPT_MODE, skeySpec);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static synchronized CmSecretUtil getInstance() {
        if (instance == null) {
            instance = new CmSecretUtil();
        }
        return instance;
    }

    /**
     * hex to byte[] : 16진수 문자열을 바이트 배열로 변환한다.
     * 
     * @param hex
     * @return
     */
    public byte[] hexToByteArray(String hex) {
        if (hex == null || hex.length() == 0) {
            return null;
        }

        byte[] ba = new byte[hex.length() / 2];

        for (int i = 0; i < ba.length; i++) {
            ba[i] = (byte)Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
        }
        return ba;
    }

    /**
     * byte[] to hex : unsigned byte 배열을 16진수 문자열로 바꾼다.
     * @param ba
     * @return
     */
    public String byteArrayToHex(byte[] ba) {
        if (ba == null || ba.length == 0) {
            return null;
        }

        StringBuilder sb = new StringBuilder();
        String hexNumber;
        for (int x = 0; x < ba.length; x++) {
            hexNumber = "0" + Integer.toHexString(0xff & ba[x]);
            sb.append(hexNumber.substring(hexNumber.length() - 2));
        }
        return sb.toString();
    }

    /**
     * AES 방식의 암호화
     * 
     * @param message
     * @return
     * @throws Exception
     */
    public String encodeAES(String message) throws Exception {
        if (message == null || message.trim().equals("")) {
            return "";
        }

        byte[] encrypted = AES_ENC_CIPHER.doFinal(message.getBytes());
        return byteArrayToHex(encrypted);
    }

    /**
     * AES 방식의 복호화
     * 
     * @param message
     * @return
     * @throws Exception
     */
    public String decodeAES(String message) throws Exception {
        if (message == null || message.trim().equals("")) {
            return "";
        }

        byte[] original = AES_DEC_CIPHER.doFinal(hexToByteArray(message));
        return new String(original);
    }

    /**
     * SHA-512
     * 
     * @param str
     * @return
     */
    public String encodeSha512(String str) {
        MessageDigest md;
        StringBuilder sb = new StringBuilder();
        try {
            md = MessageDigest.getInstance("SHA-512");
            md.update(str.getBytes());
            byte[] bytes = md.digest();
            byte b;
            String s;
            int len = bytes.length;
            for (int i = 0; i < len; i++) {
                b = bytes[i];
                s = Integer.toHexString(b);
                while (s.length() < 2) {
                    s = "0" + s;
                }
                s = s.substring(s.length() - 2);
                sb.append(s);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
}
