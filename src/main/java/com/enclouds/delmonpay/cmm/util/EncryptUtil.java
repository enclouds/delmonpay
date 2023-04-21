package com.enclouds.delmonpay.cmm.util;

import org.springframework.security.crypto.codec.Hex;
import org.springframework.security.crypto.codec.Utf8;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class EncryptUtil {

    public static final String CHARSET_UTF8 = "UTF-8";
    public static final String DEFAULT_CHARSET = CHARSET_UTF8;

    private static final String AES = "AES";
    public static final String AES_CBC_ALGORITHM = AES + "/CBC/PKCS5Padding";

    public static String sha256(String valueStr) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        digest.reset();
        digest.update(valueStr.getBytes(DEFAULT_CHARSET));

        return bytesToHex(digest.digest());
    }

    public static String bytesToHex(byte[] bytes) {
        StringBuilder builder = new StringBuilder();
        for (byte b : bytes) {
            builder.append(String.format("%02x", b));
        }
        return builder.toString();
    }

    public static String generateKey(final int keyLen) throws NoSuchAlgorithmException {

        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(keyLen);
        SecretKey secretKey = keyGen.generateKey();
        byte[] encoded = secretKey.getEncoded();
        return DatatypeConverter.printHexBinary(encoded).toLowerCase();
    }

    public static String aes256Encrypt(String keyStr, String ivStr, CharSequence plainText) {
        if (plainText == null || plainText.length() == 0) {
            return null;
        }
        try {
            Cipher c = Cipher.getInstance(AES_CBC_ALGORITHM);
            c.init(Cipher.ENCRYPT_MODE, getSecretKey(keyStr), new IvParameterSpec(Hex.decode(ivStr)));

            byte[] encrypted = c.doFinal(Utf8.encode(plainText));
            return new String(Hex.encode(encrypted));
        } catch (Exception e) {
            e.printStackTrace();
            // 필요에 따라 예외처리 수행
            return null;
        }
    }

    public static String aes256Decrypt(String keyStr, String ivStr, CharSequence encryptedText) {
        if (encryptedText == null || encryptedText.length() == 0) {
            return null;
        }
        try {
            Cipher c = Cipher.getInstance(AES_CBC_ALGORITHM);
            c.init(Cipher.DECRYPT_MODE, getSecretKey(keyStr), new IvParameterSpec(Hex.decode(ivStr)));

            byte[] decrypted = c.doFinal(Hex.decode(encryptedText));
            return Utf8.decode(decrypted);
        } catch (Exception e) {
            e.printStackTrace();
            // 필요에 따라 예외처리 수행
            return null;
        }
    }

    private static SecretKey getSecretKey(String keyStr) {
        byte[] keyData = Hex.decode(keyStr);
        return new SecretKeySpec(keyData, AES);
    }
}
