package com.enclouds.delmonpay.paymo.service;

import com.enclouds.delmonpay.paymo.dto.PayMoDto;
import com.enclouds.delmonpay.paymo.dto.PayMoUserDto;
import com.enclouds.delmonpay.paymo.mapper.PayMoMapper;
import com.enclouds.delmonpay.paymo.util.WpAES256Cipher;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Service
public class PayMoServiceImpl implements PayMoService {

    private static final String WELCOME_DEV_SERVER = "https://stgftapi.welcomepayments.co.kr/";
    private static final String WELCOME_DEV_KEY = "dc533ae5d52f4f1c2d0d690ab949d65940c7f1fd09da16e54c15082f41414a36";
    private static final String WELCOME_DEV_IV = "451e2904676cad9e74b259ac224e3ac5";
    private static final String WELCOME_DEV_API_KEY = "230";
    private static final String WELCOME_DEV_API_KEY_HASH = "ku0pH4rFQqjrAAfjxWkG8bQObkAV7fw5";

    private static final String WELCOME_REAL_SERVER = "https://openapi.welcomefin.com/";
    private static final String WELCOME_KEY = "1bb54d3572616e6f86c3cab37debfacbd27f0546eb7190b85d0f7e6f39580de3";
    private static final String WELCOME_IV = "74cfe3e4b0c5b4d3cb9f5d53becce60c";
    private static final String WELCOME_API_KEY = "218";
    private static final String WELCOME_API_KEY_HASH = "tHbefp4jU0yD1GBVGUTbE7gZuhN4jdvl";

    @Autowired
    private PayMoMapper payMoMapper;

    @Override
    public int selectIntegUserCnt(PayMoDto payMoDto) throws Exception {
        return payMoMapper.selectIntegUserCnt(payMoDto);
    }

    @Override
    public PayMoUserDto selectIntegUserInfo(PayMoDto payMoDto) throws Exception{
        return payMoMapper.selectIntegUserInfo(payMoDto);
    }

    @Override
    public PayMoUserDto selectPayMoUserInfo(PayMoDto payMoDto) throws Exception{
        return payMoMapper.selectPayMoUserInfo(payMoDto);
    }

    @Override
    public void insertPayMoUserInfo(HashMap<String, Object> hm) throws Exception{
        payMoMapper.insertPayMoUserInfo(hm);
    }

    @Override
    public void insertPaymoTranLog(HashMap<String, Object> hm) throws Exception{
        payMoMapper.insertPaymoTranLog(hm);
    }

    @Override
    public void insertPaymoTranRcvLog(HashMap<String, Object> hm) throws Exception{
        payMoMapper.insertPaymoTranRcvLog(hm);
    }

    @Override
    public Map<String, Object> getBankList() throws Exception{
        String rcvJson = "";
        StringBuilder sb = new StringBuilder();
        Map<String, Object> rtnMap = new HashMap<String, Object>();
        URL urlCon = new URL(WELCOME_REAL_SERVER+"wp/api/common/withdrawaccount/banks");
        HttpURLConnection httpCon = (HttpURLConnection)urlCon.openConnection();
        try {

            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            httpCon.setDefaultUseCaches(false);
            httpCon.setRequestMethod("GET");
            httpCon.setRequestProperty("WP_KEY", WELCOME_API_KEY);
            httpCon.setRequestProperty("WP_HASH", WELCOME_API_KEY_HASH);
            httpCon.setConnectTimeout(4000);
            httpCon.setReadTimeout(4000);
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

        return rtnMap;
    }

    @Override
    public Map<String, Object> getBankNumChk(String bankNum, String bankCd, String userNm) throws Exception{

        String rcvJson = "";
        StringBuilder sb = new StringBuilder();
        Map<String, Object> rtnMap = new HashMap<String, Object>();

        String json = "{ \"bankId\": \""+bankCd+"\","
                + " \"memAccntno\": \""+bankNum+"\","
                + " \"bankOwnerName\": \""+userNm+"\""
                + "}";

        String encryptStr = WpAES256Cipher.encrypt(WELCOME_KEY, WELCOME_IV, json);

        URL urlCon = new URL(WELCOME_REAL_SERVER+"wp/api/inquiry/selectinquiry/account/name");
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
            httpCon.setRequestProperty("WP_KEY", WELCOME_API_KEY);
            httpCon.setRequestProperty("WP_HASH", WELCOME_API_KEY_HASH);
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

        return rtnMap;
    }

}
