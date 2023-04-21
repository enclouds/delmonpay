package com.enclouds.delmonpay.ctrt.service;

import com.enclouds.delmonpay.cmm.util.EncryptUtil;
import com.enclouds.delmonpay.ctrt.dto.*;
import com.enclouds.delmonpay.ctrt.mapper.SPCMapper;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

@Service
public class SPCServiceImpl implements SPCService{

    private static final String SPC_PG_DEV_URL = "https://cardsales.kr";
    private static final String SPC_PG_REAL_URL = "https://delipay.co.kr";
    private static final String SEND_APPROVAL = "/api/rentRequest";
    private static final String CHECK_CANCEL = "/api/rentCancelCheck";
    private static final String KS_APIKEY = "0e657b6bbc49ef05959c8a450f1f0f5c255dae475b0bfef86b4a4ae96ad5518f";
    private static final int CONN_TIME = 4000;
    private static final int READ_TIME = 4000;

    private final static String T_NEW_WOLSAE_MID = "wel000663m";
    private final static String T_NEW_WOLSAE_WELCOME_API_KEY = "66d14a4b4f81ea466efcb9ce2ecdf739";

    @Autowired
    private SPCMapper spcMapper;

    @Override
    public String selectReqSeqInfo() throws Exception {
        return spcMapper.selectReqSeqInfo();
    }


    @Override
    public int insertSpcInfo(SPCDto spcDto) throws Exception {
        return spcMapper.insertSpcInfo(spcDto);
    }

    private String getServerIp() {
        InetAddress local = null;

        try {
            local = InetAddress.getLocalHost();
        }
        catch ( UnknownHostException e ) {
            e.printStackTrace();
        }

        if( local == null ) {
            return "";
        }
        else {
            String ip = local.getHostAddress();
            return ip;
        }
    }

    @Override
    public SPCDto rentCancelCheck(SPCDto spcDto) throws Exception {

        SPCDto spcInfo = new SPCDto();

        String sendJson = "";
        String rcvJson = "";
        StringBuilder sb = new StringBuilder();

        try {
            URL urlCon = new URL(SPC_PG_REAL_URL + CHECK_CANCEL);
            HttpURLConnection httpCon = (HttpURLConnection)urlCon.openConnection();
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            sendJson = mapper.writeValueAsString(spcDto);

            httpCon.setRequestProperty("Accept", "application/json");
            httpCon.setRequestProperty("Content-type", "application/json;charset=UTF-8");
            httpCon.setRequestProperty("KS_APIKEY", KS_APIKEY);
            httpCon.setDoOutput(true);
            httpCon.setDoInput(true);
            httpCon.setConnectTimeout(CONN_TIME);
            httpCon.setReadTimeout(READ_TIME);
            httpCon.setDoOutput(true);
            httpCon.setDoInput(true);
            httpCon.setUseCaches(false);
            httpCon.setDefaultUseCaches(false);
            OutputStream os = httpCon.getOutputStream();
            os.write(sendJson.getBytes("utf-8"));
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
                    spcInfo = (SPCDto)mapperRtn.readValue(sb.toString(), SPCDto.class);
                } else {
                    spcInfo.setResultCd("-1");
                    spcInfo.setResultDtl("SPC 연동에러 : (" + httpCon.getResponseCode() + ") " + httpCon.getResponseMessage());
                }
            } catch (IOException e) {
                spcInfo.setResultCd("-1");
                spcInfo.setResultDtl("SPC 연동중 에러 : " + e.getMessage());
            } finally {
                httpCon.disconnect();
            }
        } catch (IOException e) {
            spcInfo.setResultCd("-1");
            spcInfo.setResultDtl("SPC 연동중 에러 : " + e.getMessage());
        }

        return spcInfo;
    }

    @Override
    public SPCDto rentCancelRequest(SPCDto spcDto) throws Exception {
        SPCDto spcInfo = new SPCDto();

        String sendJson = "";
        String rcvJson = "";
        StringBuilder sb = new StringBuilder();
        SPCDto totalSpcInfo = new SPCDto();

        try {
            totalSpcInfo = spcMapper.selectSpcInfo(spcDto);
            totalSpcInfo.setMid(T_NEW_WOLSAE_MID);
            totalSpcInfo.setDealCd("DC02");

            URL urlCon = new URL(SPC_PG_REAL_URL + SEND_APPROVAL);
            HttpURLConnection httpCon = (HttpURLConnection)urlCon.openConnection();
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            sendJson = mapper.writeValueAsString(totalSpcInfo);

            httpCon.setRequestProperty("Accept", "application/json");
            httpCon.setRequestProperty("Content-type", "application/json;charset=UTF-8");
            httpCon.setRequestProperty("KS_APIKEY", KS_APIKEY);
            httpCon.setDoOutput(true);
            httpCon.setDoInput(true);
            httpCon.setConnectTimeout(CONN_TIME);
            httpCon.setReadTimeout(READ_TIME);
            httpCon.setDoOutput(true);
            httpCon.setDoInput(true);
            httpCon.setUseCaches(false);
            httpCon.setDefaultUseCaches(false);
            OutputStream os = httpCon.getOutputStream();
            os.write(sendJson.getBytes("utf-8"));
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
                    spcInfo = (SPCDto)mapperRtn.readValue(sb.toString(), SPCDto.class);
                } else {
                    spcInfo.setResultCd("-1");
                    spcInfo.setResultDtl("SPC 연동에러 : (" + httpCon.getResponseCode() + ") " + httpCon.getResponseMessage());
                }
            } catch (IOException e) {
                spcInfo.setResultCd("-1");
                spcInfo.setResultDtl("SPC 연동중 에러 : " + e.getMessage());
            } finally {
                httpCon.disconnect();
            }
        } catch (IOException e) {
            spcInfo.setResultCd("-1");
            spcInfo.setResultDtl("SPC 연동중 에러 : " + e.getMessage());
        }

        //케이솔루션 취소요청 완료 후 카드 취소
        if(spcInfo.getResultCd().equals("0000")){
            String rtnJson = "";
            ApiCancelRtnDto apiCancelRtnDto = new ApiCancelRtnDto();
            Gson gson = new Gson();

            spcMapper.updateCancelInfo(totalSpcInfo);

            ApiCancelDto apiCancelDto = spcMapper.selectCardApprovalInfo(totalSpcInfo);

            try {
                String cancelApiUrl = "https://payapi.welcomepayments.co.kr/api/payment/cancel";
                ResponseEntity<String> response = apiExchangeCancel(cancelApiUrl, getCancelParameter(apiCancelDto));

                rtnJson = response.getBody();

                spcMapper.insertCardCancelInfo(apiCancelDto);

            }catch (HttpClientErrorException e) {
                // HttpStatusCode 400 오류
                System.out.println("Status :" + e.getStatusCode());
                System.out.println("body :" + e.getResponseBodyAsString());
                rtnJson = e.getResponseBodyAsString();
                apiCancelRtnDto = gson.fromJson(rtnJson, ApiCancelRtnDto.class);
            } catch (HttpServerErrorException e) {
                // HttpStatusCode 500 오류
                System.out.println("Status :" + e.getStatusCode());
                System.out.println("body :" + e.getResponseBodyAsString());
                rtnJson = e.getResponseBodyAsString();
                apiCancelRtnDto = gson.fromJson(rtnJson, ApiCancelRtnDto.class);
            } catch (Exception e) {
                System.out.println("Exception :" + e.getMessage());
            }

            Gson gson2 = new Gson();
            ApiCancelRtnDto cancelRtnDto = new ApiCancelRtnDto();
            cancelRtnDto = (ApiCancelRtnDto)gson2.fromJson(rtnJson, ApiCancelRtnDto.class);

            spcMapper.insertCancelRtnInfo(cancelRtnDto);

            if(cancelRtnDto.getResult_code().equals("0000")){
                spcInfo.setResultCd("0000");
                spcInfo.setResultDtl("정상 취소");
            }else {
                spcInfo.setResultCd(cancelRtnDto.getResult_code());
                spcInfo.setResultDtl(cancelRtnDto.getResult_message());
            }

        }

        return spcInfo;
    }

    public String getCancelParameter(ApiCancelDto apiCancelDto) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        String mid = "";
        String apiKey = "";

        mid = T_NEW_WOLSAE_MID;
        apiKey = T_NEW_WOLSAE_WELCOME_API_KEY;

        String payType = "CREDIT_CARD";
        String transactionNo = apiCancelDto.getTransactionNo();     //거래번호
        String transactionType = "CANCEL"; //전체취소 : CANCEL, 부분취소 : PART_CANCEL
        String amount = apiCancelDto.getAmount();                   //취소금액
        String millis = String.valueOf(System.currentTimeMillis()); //매 요청시마다 생성
        String userId = apiCancelDto.getUserId();                   //취소 유저 아이디
        String cancelReason = "사용자 취소";       //취소 사유
        String ipAddress = getServerIp();             //취소 요청자 IP
        //암호화 hash_value
        String hash_value = EncryptUtil.sha256(mid + transactionType + transactionNo + amount + millis + apiKey);

        Map<String, Object> param = new HashMap<>();
        param.put("mid", mid);
        param.put("pay_type", payType);
        param.put("transaction_type", transactionType);
        param.put("user_id", userId);
        param.put("transaction_no", transactionNo);
        param.put("amount", amount);
        param.put("cancel_reason", cancelReason);
        param.put("ip_address", ipAddress);
        param.put("millis", millis);
        param.put("hash_value", hash_value);

        return getJsonString(param);
    }

    public ResponseEntity<String> apiExchangeCancel(String url, String body){
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);

        HttpEntity<String> entity = new HttpEntity<>(body, headers);

        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
        return response;
    }

    public static String getJsonString(Map<String, Object> map ){
        JSONObject jsonObject = new JSONObject();
        map.forEach((key, value) -> jsonObject.put(key, value));
        return jsonObject.toJSONString();
    }

}
