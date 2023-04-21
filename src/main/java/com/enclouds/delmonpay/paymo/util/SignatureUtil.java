package com.enclouds.delmonpay.paymo.util;

import java.security.MessageDigest;
import java.security.SignatureException;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

/**
 * @FileName    : SignatureUtil.java
 * @ClassName   : SignatureUtil
 * @Description : SignatureUtil
 */
public class SignatureUtil {

    protected static final String UTF_8_Encoding = "UTF-8";

    public static final String SIGNATURE_KEYNAME = "signKey";

    public static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";
    public static final String HMAC_SHA256_ALGORITHM = "HmacSHA256";

    // Constants used when constructing the string to sign for v2
    public static final String NewLine = "\n";
    public static final String EmptyUriPath = "/";
    public static final String Equals = "=";
    public static final String And = "&";


    /**
     * @MethodName  : getTimestamp
     * @Description : getTimestamp
     */
    public static String getTimestamp() {
        Calendar cal = Calendar.getInstance();
        return cal.getTimeInMillis()+"";
    }


    /**
     * @MethodName  : makeSignature
     * @Description : 암호화
     */
    public static String makeSignature(Map<String, String> parameters) throws Exception {

        if (parameters == null || parameters.isEmpty()) {
            throw new RuntimeException("Parameters can not be empty.");
        }

        String parametersString = calculateString(parameters);
        String signature = hash(parametersString, "SHA-256");

        return signature;
    }


    /**
     * @MethodName  : makeSignatureAuth
     * @Description : 암호화
     * fake server 막기 위해 추가
     */
    public static String makeSignatureAuth(Map<String, String> parameters) throws Exception {

        if (parameters == null || parameters.isEmpty()) {
            throw new RuntimeException("Parameters can not be empty.");
        }

        String stringToSign = "";																	//반환용 text
        String mid = parameters.get("mid");															//mid
        String tstamp = parameters.get("tstamp");													//auth timestamp
        String MOID = parameters.get("MOID");														//OID
        String TotPrice = parameters.get("TotPrice");												//total price
        String tstampKey = parameters.get("tstamp").substring(parameters.get("tstamp").length()-1);	// timestamp 마지막 자리 1자리 숫자


        switch (Integer.parseInt(tstampKey)){
            case 1 :
                stringToSign = "MOID=" + MOID + "&mid=" + mid + "&tstamp=" + tstamp ;
                break;
            case 2 :
                stringToSign = "MOID=" + MOID + "&tstamp=" + tstamp + "&mid=" + mid ;
                break;
            case 3 :
                stringToSign = "mid=" + mid + "&MOID=" + MOID + "&tstamp=" + tstamp ;
                break;
            case 4 :
                stringToSign = "mid=" + mid + "&tstamp=" + tstamp + "&MOID=" + MOID ;
                break;
            case 5 :
                stringToSign = "tstamp=" + tstamp + "&mid=" + mid + "&MOID=" + MOID ;
                break;
            case 6 :
                stringToSign = "tstamp=" + tstamp + "&MOID=" + MOID + "&mid=" + mid ;
                break;
            case 7 :
                stringToSign = "TotPrice=" + TotPrice + "&mid=" + mid + "&tstamp=" + tstamp ;
                break;
            case 8 :
                stringToSign = "TotPrice=" + TotPrice + "&tstamp=" + tstamp + "&mid=" + mid ;
                break;
            case 9 :
                stringToSign = "TotPrice=" + TotPrice + "&MOID=" + MOID + "&tstamp=" + tstamp ;
                break;
            case 0 :
                stringToSign = "TotPrice=" + TotPrice + "&tstamp=" + tstamp + "&MOID=" + MOID ;
                break;
        }


        System.out.println("stringToSign="+stringToSign.toString()) ;
        System.out.println("tstampKey,tstamp="+tstampKey+","+tstamp) ;

        String signature = hash(stringToSign, "SHA-256");		// sha256 처리하여 hash 암호화

        return signature;
    }


    /**
     * @MethodName  : hash
     * @Description : hash 생성
     */
    public static String hash(String data, String algorithm) throws Exception {


        // SHA를 사용하기 위해 MessageDigest 클래스로부터 인스턴스를 얻는다.
        MessageDigest md = MessageDigest.getInstance(algorithm);

        // 해싱할 byte배열을 넘겨준다.
        // SHA-256의 경우 메시지로 전달할 수 있는 최대 bit 수는 2^64-1개 이다.
        md.update(data.getBytes("UTF-8"));

        // 해싱된 byte 배열을 digest메서드의 반환값을 통해 얻는다.
        byte[] hashbytes = md.digest();

        // 보기 좋게 16진수로 만드는 작업
        StringBuilder sbuilder = new StringBuilder();
        for(int i=0 ; i<hashbytes.length ; i++){

            // %02x 부분은 0 ~ f 값 까지는 한자리 수이므로 두자리 수로 보정하는 역할을 한다.
            sbuilder.append(String.format("%02x", hashbytes[i] & 0xff));

        }

        return sbuilder.toString();
    }


    /**
     * @MethodName  : calculateString
     * @Description : 암호화 대상 데이터 생성
     */
    private static String calculateString(Map<String, String> parameters) throws SignatureException {
        StringBuffer stringToSign = new StringBuffer("");

        Map<String, String> sortedParamMap = new TreeMap<String, String>();
        sortedParamMap.putAll(parameters);
        Iterator<Map.Entry<String, String>> pairs = sortedParamMap.entrySet().iterator();
        while (pairs.hasNext()) {
            Map.Entry<String, String> pair = pairs.next();

            stringToSign.append(pair.getKey().trim());
            stringToSign.append(Equals);
            stringToSign.append(pair.getValue().trim());

            if (pairs.hasNext()) stringToSign.append(And);
        }
        System.out.println("stringToSign.toString()="+stringToSign.toString()) ;
        return stringToSign.toString();
    }
}