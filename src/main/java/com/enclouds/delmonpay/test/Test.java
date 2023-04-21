package com.enclouds.delmonpay.test;

import com.enclouds.delmonpay.DelMonPayApplication;
import com.enclouds.delmonpay.paymo.util.WpAES256Cipher;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.SpringApplication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class Test {

    private static final String WELCOME_DEV_SERVER = "https://stgftapi.welcomepayments.co.kr/";
    private static final String WELCOME_DEV_KEY = "dc533ae5d52f4f1c2d0d690ab949d65940c7f1fd09da16e54c15082f41414a36";
    private static final String WELCOME_DEV_IV = "451e2904676cad9e74b259ac224e3ac5";
    private static final String WELCOME_DEV_API_KEY = "230";
    private static final String WELCOME_DEV_API_KEY_HASH = "ku0pH4rFQqjrAAfjxWkG8bQObkAV7fw5";

    public static void main(String[] args) throws  Exception{
        String rcvJson = "";
        StringBuilder sb = new StringBuilder();
        Map<String, Object> rtnMap = new HashMap<String, Object>();

        String bankCd = "090";
        String bankNum = "3333035612535";
        String userNm = "";

        String json = "{ \"bankId\": \""+bankCd+"\","
                + " \"memAccntno\": \""+bankNum+"\","
                + " \"bankOwnerName\": \""+userNm+"\""
                + "}";

        String encryptStr = WpAES256Cipher.encrypt(WELCOME_DEV_KEY, WELCOME_DEV_IV, json);

        URL urlCon = new URL(WELCOME_DEV_SERVER+"wp/api/inquiry/selectinquiry/account/name");
        HttpURLConnection httpCon = (HttpURLConnection)urlCon.openConnection();
        try {

            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            httpCon.setDoOutput(true);
            httpCon.setDoInput(true);
            httpCon.setUseCaches(false);
            httpCon.setDefaultUseCaches(false);
            httpCon.setRequestProperty("Accept", "application/json");
            httpCon.setRequestProperty("Content-type", "application/json");
            httpCon.setRequestProperty("WP_KEY", WELCOME_DEV_API_KEY);
            httpCon.setRequestProperty("WP_HASH", WELCOME_DEV_API_KEY_HASH);
            httpCon.setRequestProperty("Encrypt", "TRUE");

            httpCon.setConnectTimeout(4000);
            httpCon.setReadTimeout(4000);

            OutputStream os = httpCon.getOutputStream();
            //os.write(json.getBytes("UTF-8"));
            os.write(encryptStr.getBytes(StandardCharsets.UTF_8));
            os.flush();
            os.close();

            try {
                if (httpCon.getResponseCode() == 200) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(httpCon.getInputStream(), "UTF-8"));
                    while ((rcvJson = br.readLine()) != null)
                        sb.append(rcvJson + "\n");
                    br.close();
                    ObjectMapper mapperRtn = new ObjectMapper();
                    mapperRtn.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                    rtnMap = mapper.readValue(sb.toString(), Map.class);

                }
            } catch (IOException e) {
                System.out.println("Error : " + e.getMessage());
            } finally {
                httpCon.disconnect();
            }
        } catch (IOException e) {
            System.out.println("Error : " + e.getMessage());
            httpCon.disconnect();
        }

        System.out.println(rtnMap);
    }

}
