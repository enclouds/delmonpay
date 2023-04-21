package com.enclouds.delmonpay.paymo.util;

import org.springframework.security.crypto.codec.Hex;
import org.springframework.security.crypto.codec.Utf8;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class WpAES256Cipher {

    private static final String AES = "AES";
    public static final String AES_CBC_ALGORITHM = AES + "/CBC/PKCS5Padding";

    public static String SECRET_KEY;
    public static String AES_CBC_IV;

    public WpAES256Cipher(String key, String iv){
        SECRET_KEY = key;
        AES_CBC_IV = iv;
    }

    private void setKeyValue(String key){
        SECRET_KEY = key;
    }

    private void setIvValue(String iv){
        AES_CBC_IV = iv;
    }

    private static SecretKey getSecretKey(String keyStr) {
        byte [] keyData = Hex.decode(keyStr);
        return new SecretKeySpec(keyData, AES);
    }

    private static Cipher getCipherInstance() throws NoSuchAlgorithmException, NoSuchPaddingException {
        return Cipher.getInstance(AES_CBC_ALGORITHM);
    }

    /**
     * 내부 데이터 암호화용 메소드 (내부의 정해진 key, iv 를 이용해 암호화한다.)
     * @param plainText
     * @return
     */
    public static String encrypt(CharSequence plainText) {
        return encrypt(SECRET_KEY, AES_CBC_IV, plainText);
    }

    /**
     * @param keyStr
     * @param ivStr
     * @param plainText
     * @return
     */
    public static String encrypt(String keyStr, String ivStr, CharSequence plainText) {
        if(plainText == null || plainText.length() == 0) {
            return null;
        }
        try {
            Cipher c = getCipherInstance();
            c.init(Cipher.ENCRYPT_MODE, getSecretKey(keyStr), new IvParameterSpec(Hex.decode(ivStr)));

            byte [] encrypted = c.doFinal(Utf8.encode(plainText));
            return new String(Hex.encode(encrypted));
        } catch(Exception e) {
            throw new CryptoException("암호화 오류", e);
        }
    }

    /**
     * 내부 데이터 복호화용 메소드 (내부의 정해진 key, iv 를 이용해 복호화한다.)
     * @param encryptedText
     * @return
     */
    public static String decrypt(CharSequence encryptedText) {
        return decrypt(SECRET_KEY, AES_CBC_IV, encryptedText);
    }

    /**
     * @param keyStr
     * @param ivStr
     * @param encryptedText
     * @return
     */
    public static String decrypt(String keyStr, String ivStr, CharSequence encryptedText) {
        if(encryptedText == null || encryptedText.length() == 0) {
            return null;
        }
        try {
            Cipher c = getCipherInstance();
            c.init(Cipher.DECRYPT_MODE, getSecretKey(keyStr), new IvParameterSpec(Hex.decode(ivStr)));

            byte [] decrypted = c.doFinal(Hex.decode(encryptedText));
            return Utf8.decode(decrypted);
        } catch(Exception e) {
            throw new CryptoException("복호화 오류", e);
        }
    }

    /**
     * @return Char Length : 64 , Byte Length : 32 리턴한다.
     */
    public static String generateAesKey() {
        KeyGenerator generator = null;
        try {
            generator = KeyGenerator.getInstance(AES);
        } catch (NoSuchAlgorithmException e) {
            throw new CryptoException("키생성 오류", e);
        }
        generator.init(256);
        return new String(Hex.encode(generator.generateKey().getEncoded()));
    }

    /**
     * 키와 iv 의 변경이 필요할 시 실행함.
     * <p>주의 : 실행은 상관없으나, static final 값 변경시 전체 데이터에 영향을 미치므로 주의바람.</p>
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        KeyGenerator generator = KeyGenerator.getInstance(AES);
        generator.init(256);

        Key key = generator.generateKey();

        byte[] keyBytes = key.getEncoded();
        String keyStr = generateAesKey();

        System.out.println(String.format("Key : %s , Length : %d , Byte Length : %d", keyStr, keyStr.length(), keyBytes.length));

        byte[] ivBytes = new SecureRandom().generateSeed(16);
        String ivStr = new String(Hex.encode(ivBytes));
        System.out.println(String.format("IV : %s , Length : %d , Byte Length : %d", ivStr, ivStr.length(), ivBytes.length));

        String qkey = "c464da52d423ae08b1f5674c0d5b574943c6825bf2e1b5cfe57181fd7b45a79e";
        String qiv = "a44e09478ad3d38fb10ccf3a6b9eb11c";
        // test
        String a = WpAES256Cipher.encrypt(qkey, qiv, "{\n" +
                "\t\"bizSeqno\" : 0,\n" +
                "\t\"bizName\" : \"aaa\",\n" +
                "\t\"ceoName\" : \"adada\"\n" +
                "\t\n" +
                "}");
        System.out.println("encrypt=" + a);
        String b = WpAES256Cipher.decrypt(qkey, qiv, a);
        System.out.println("decrypt=" + b);

    }
}
