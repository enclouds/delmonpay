package com.enclouds.delmonpay.ctrt.service;

import com.enclouds.delmonpay.cmm.mail.MailSend;
import com.enclouds.delmonpay.cmm.util.DateUtils;
import com.enclouds.delmonpay.cmm.util.EncryptUtil;
import com.enclouds.delmonpay.ctrt.dto.*;
import com.enclouds.delmonpay.ctrt.mapper.CtrtMapper;
import com.enclouds.delmonpay.user.dto.UserDto;
import com.enclouds.delmonpay.user.service.UserService;
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
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CtrtServiceImpl implements CtrtService{

    private final static String LETS_DOMAIN = "http://pay.letspay.co.kr/reqApproval.utf.ASP";
    private final static String LETS_SHOP_CODE = "220003610";
    private final static String LETS_LOGIN_ID = "delion";
    private final static String LETS_SERVICE_CODE = "LETS";

    //비인증
    private final static String T_NEW_WOLSAE_MID = "wel000556m";
    private final static String T_NEW_WOLSAE_WELCOME_API_KEY = "1e015d613af434f4badbaf9efe5902ee";
    private final static String T_NEW_WOLSAE_IV_KEY = "358240ef674f43f470bf1a1ed71e797c";

    //private final static String T_NEW_WOLSAE_MID = "wel000663m";
    //private final static String T_NEW_WOLSAE_WELCOME_API_KEY = "66d14a4b4f81ea466efcb9ce2ecdf739";
    //private final static String T_NEW_WOLSAE_IV_KEY = "868c4de36a068d3ad05a139ab35670c1";

    @Autowired
    private CtrtMapper ctrtMapper;

    @Autowired
    private SPCService spcService;

    @Autowired
    private UserService userService;

    @Override
    public int insertCtrt(CtrtDto ctrtDto) throws Exception {
        if(ctrtDto.getFile01() != null){
            ctrtDto.setImg01(ctrtDto.getFile01().get(0).getBytes());
            ctrtDto.setFile01Yn("Y");
            ctrtDto.setFile01Name(ctrtDto.getFile01().get(0).getOriginalFilename());
        }

        if(ctrtDto.getFile02() != null){
            ctrtDto.setImg02(ctrtDto.getFile02().get(0).getBytes());
            ctrtDto.setFile02Yn("Y");
            ctrtDto.setFile02Name(ctrtDto.getFile02().get(0).getOriginalFilename());
        }

        if(ctrtDto.getFile03() != null){
            ctrtDto.setImg03(ctrtDto.getFile03().get(0).getBytes());
            ctrtDto.setFile03Yn("Y");
            ctrtDto.setFile03Name(ctrtDto.getFile03().get(0).getOriginalFilename());
        }

        int result = ctrtMapper.insertCtrt(ctrtDto);

        UserDto userDto = userService.getUserInfo(ctrtDto.getUserId());

        //신청 메일 발송
        MailSend mailSend = new MailSend();
        HashMap<String, Object> mailData = new HashMap<>();
        mailData.put("fromAddress", "delion1111@naver.com");
        mailData.put("toAddress", "delion0823@naver.com");
        mailData.put("ccAddress", "paybank8001@daum.net;jangsos1111@naver.com;moumaleang2@gmail.com;bangmarket@naver.com;inhwanbuis@gmail.com;delion28@naver.com;delion96@naver.com;tnsgh8@naver.com;baeseongu@naver.com;mmkkop417@gmail.com");
        mailData.put("title", "매장비서페이 신청");
        mailData.put("content", "매장비서페이 신청\n\n ·계정: " + ctrtDto.getUserId() + "\n ·상호명: " + userDto.getStoreNm() + "\n ·계약명: " + ctrtDto.getCtrtName());
        mailSend.MailSend(mailData);

        return result;
    }

    @Override
    public int updateCtrt(CtrtDto ctrtDto) throws Exception {

        if(ctrtDto.getFile01Yn().equals("Y")){
        }else {
            if(!ctrtDto.getFile01().get(0).getOriginalFilename().equals("")){
                ctrtDto.setImg01(ctrtDto.getFile01().get(0).getBytes());
                ctrtDto.setFile01Yn("U");
                ctrtDto.setFile01Name(ctrtDto.getFile01().get(0).getOriginalFilename());
            }else {
                ctrtDto.setFile01Yn("N");
            }
        }

        if(ctrtDto.getFile02Yn().equals("Y")){
        }else {
            if (!ctrtDto.getFile02().get(0).getOriginalFilename().equals("")) {
                ctrtDto.setImg02(ctrtDto.getFile02().get(0).getBytes());
                ctrtDto.setFile02Yn("U");
                ctrtDto.setFile02Name(ctrtDto.getFile02().get(0).getOriginalFilename());
            } else {
                ctrtDto.setFile02Yn("N");
            }
        }

        if(ctrtDto.getFile03Yn().equals("Y")){
        }else {
            if (!ctrtDto.getFile03().get(0).getOriginalFilename().equals("")) {
                ctrtDto.setImg03(ctrtDto.getFile03().get(0).getBytes());
                ctrtDto.setFile03Yn("U");
                ctrtDto.setFile03Name(ctrtDto.getFile03().get(0).getOriginalFilename());
            } else {
                ctrtDto.setFile03Yn("N");
            }
        }

        int result = ctrtMapper.updateCtrt(ctrtDto);

        return result;
    }

    @Override
    public List<CtrtDto> selectCtrtList(int agentCode) throws Exception{
        return ctrtMapper.selectCtrtList(agentCode);
    }

    @Override
    public CtrtDto selectCtrtInfo(CtrtDto ctrtDto) throws Exception {
        return ctrtMapper.selectCtrtInfo(ctrtDto);
    }

    @Override
    public int deleteFileAjax(CtrtDto ctrtDto) throws Exception {
        int resultCode = 0;

        if(ctrtDto.getFileNum().equals("1")){
            resultCode = ctrtMapper.deleteFile01(ctrtDto);
        }else if(ctrtDto.getFileNum().equals("2")){
            resultCode = ctrtMapper.deleteFile02(ctrtDto);
        }else if(ctrtDto.getFileNum().equals("3")){
            resultCode = ctrtMapper.deleteFile03(ctrtDto);
        }

        return resultCode;
    }

    @Override
    public int deleteCtrt(CtrtDto ctrtDto) throws Exception {
        return ctrtMapper.deleteCtrt(ctrtDto);
    }

    @Override
    public String paymentCtrt(CtrtDto ctrtDto, UserDto userDto) throws Exception {
        String rtnJson = "";
        StringBuilder sb = new StringBuilder();
        LetsApprDto letsApprDto = new LetsApprDto();
        LetsApprDto letsApprRtnDto = new LetsApprDto();

        CtrtDto ctrtInfo = this.selectCtrtInfo(ctrtDto);

        if(userDto.getWolsaeGbn().equals("L")){
            try {
                letsApprDto.setCtrtSeq(ctrtInfo.getSeq());
                letsApprDto.setShopcode(LETS_SHOP_CODE);
                letsApprDto.setLoginid(LETS_LOGIN_ID);
                letsApprDto.setServicecode(LETS_SERVICE_CODE);

                Double fee = 1 - (4.4 / 100);
                Double payAmt = ctrtDto.getRcvPrice() / fee;
                int payRealAmt = (int)Math.floor(payAmt);

                letsApprDto.setOrderReqAmt(payRealAmt);
                letsApprDto.setReceiveType("J");
                letsApprDto.setOrderGoodsname(ctrtInfo.getCtrtName());
                letsApprDto.setOrderName(ctrtInfo.getDepositName());
                letsApprDto.setOrderHp("02-1522-7651");
                letsApprDto.setOrderEmail("");
                letsApprDto.setReqCardNo(ctrtDto.getReqCardNo());
                letsApprDto.setReqCardmonth(ctrtDto.getCardExpiryYm().substring(0,2));
                letsApprDto.setReqCardyear(ctrtDto.getCardExpiryYm().substring(2,4));
                letsApprDto.setReqInstallment(ctrtDto.getCardSellMm());
                letsApprDto.setCompOrderno(userDto.getAgentCode() + "-" + String.valueOf(System.currentTimeMillis()));
                letsApprDto.setCompMemno(userDto.getUserId());
                letsApprDto.setShBisno(userDto.getBizNo());
                letsApprDto.setShBisname(userDto.getBizNm());
                letsApprDto.setShBankcd(ctrtInfo.getBankCd());
                letsApprDto.setShBankno(ctrtInfo.getBankNum());
                letsApprDto.setSendGbn(ctrtInfo.getSendGbn());
                letsApprDto.setDefPrice(ctrtDto.getRcvPrice());
                letsApprDto.setMinusPrice(ctrtDto.getMinusPrice() - ctrtDto.getRcvPrice());

                URL urlCon = new URL(LETS_DOMAIN);
                HttpURLConnection httpCon = (HttpURLConnection)urlCon.openConnection();
                ObjectMapper mapper = new ObjectMapper();
                mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

                httpCon.setRequestProperty("Content-type", "application/x-www-form-urlencoded");
                httpCon.setDoOutput(true);
                httpCon.setDoInput(true);
                httpCon.setConnectTimeout(5000);
                httpCon.setReadTimeout(5000);
                httpCon.setDoOutput(true);
                httpCon.setDoInput(true);
                httpCon.setUseCaches(false);
                httpCon.setDefaultUseCaches(false);
                httpCon.setRequestMethod("POST");

                StringBuffer stringBuffer = new StringBuffer();
                stringBuffer.append("shopcode").append("=").append(this.replaceLetsParams(letsApprDto.getShopcode())).append("&");
                stringBuffer.append("loginid").append("=").append(this.replaceLetsParams(letsApprDto.getLoginid())).append("&");
                stringBuffer.append("servicecode").append("=").append(this.replaceLetsParams(letsApprDto.getServicecode())).append("&");
                stringBuffer.append("order_req_amt").append("=").append(letsApprDto.getOrderReqAmt()).append("&");
                stringBuffer.append("receiveType").append("=").append(this.replaceLetsParams(letsApprDto.getReceiveType())).append("&");
                stringBuffer.append("order_goodsname").append("=").append(this.replaceLetsParams(letsApprDto.getOrderGoodsname())).append("&");
                stringBuffer.append("order_name").append("=").append(this.replaceLetsParams(letsApprDto.getOrderName())).append("&");
                stringBuffer.append("order_hp").append("=").append(this.replaceLetsParams(letsApprDto.getOrderHp())).append("&");
                stringBuffer.append("order_email").append("=").append(this.replaceLetsParams(letsApprDto.getOrderEmail())).append("&");
                stringBuffer.append("req_cardNo").append("=").append(this.replaceLetsParams(letsApprDto.getReqCardNo())).append("&");
                stringBuffer.append("req_cardmonth").append("=").append(this.replaceLetsParams(letsApprDto.getReqCardmonth())).append("&");
                stringBuffer.append("req_cardyear").append("=").append(this.replaceLetsParams(letsApprDto.getReqCardyear())).append("&");
                stringBuffer.append("req_installment").append("=").append(this.replaceLetsParams(letsApprDto.getReqInstallment())).append("&");
                stringBuffer.append("comp_orderno").append("=").append(this.replaceLetsParams(letsApprDto.getCompOrderno())).append("&");
                stringBuffer.append("comp_memno").append("=").append(this.replaceLetsParams(letsApprDto.getCompMemno())).append("&");
                stringBuffer.append("sh_bisno").append("=").append(this.replaceLetsParams(letsApprDto.getShBisno())).append("&");
                stringBuffer.append("sh_bisname").append("=").append(this.replaceLetsParams(letsApprDto.getShBisname())).append("&");
                stringBuffer.append("sh_bankcd").append("=").append(this.replaceLetsParams(letsApprDto.getShBankcd())).append("&");
                stringBuffer.append("sh_bankno").append("=").append(this.replaceLetsParams(letsApprDto.getShBankno()));

                PrintWriter pw = new PrintWriter(new OutputStreamWriter(httpCon.getOutputStream(), "utf-8"));
                pw.write(stringBuffer.toString());
                pw.flush();

                try {
                    if (httpCon.getResponseCode() == 200) {
                        BufferedReader br = new BufferedReader(new InputStreamReader(httpCon.getInputStream(), "UTF-8"));
                        while ((rtnJson = br.readLine()) != null)
                            sb.append(rtnJson + "\n");
                        br.close();
                        ObjectMapper mapperRtn = new ObjectMapper();
                        mapperRtn.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                        letsApprRtnDto = (LetsApprDto) mapperRtn.readValue(sb.toString(), LetsApprDto.class);
                        rtnJson = mapperRtn.writeValueAsString(letsApprRtnDto);

                        letsApprDto.setErrCode(letsApprRtnDto.getErrCode());
                        letsApprDto.setErrMessage(letsApprRtnDto.getErrMessage());
                        letsApprDto.setOrderno(letsApprRtnDto.getOrderno());
                        letsApprDto.setApprNo(letsApprRtnDto.getApprNo());
                        letsApprDto.setApprTranNo(letsApprRtnDto.getApprTranNo());
                        letsApprDto.setApprShopCode(letsApprRtnDto.getApprShopCode());
                        letsApprDto.setApprDate(letsApprRtnDto.getApprDate());
                        letsApprDto.setApprTime(letsApprRtnDto.getApprTime());
                        letsApprDto.setCardtxt(letsApprRtnDto.getCardtxt());
                        letsApprDto.setCardcode(letsApprRtnDto.getCardcode());
                        letsApprDto.setCompAcctamt(letsApprRtnDto.getCompAcctamt());
                        letsApprDto.setMembAcctamt(letsApprRtnDto.getMembAcctamt());

                        if(letsApprRtnDto.getErrCode().equals("0000") || letsApprRtnDto.getErrCode().equals("00") || letsApprRtnDto.getErrCode().equals("")){
                            ctrtMapper.paymentCtrt(letsApprDto);

                            //결제 완료 변경
                            if(ctrtInfo.getMinusPrice() < 1){
                                ctrtMapper.ctrtStatusConfirm(ctrtDto);
                            }
                            rtnJson = "{\"result_code\" : \""+letsApprRtnDto.getErrCode()+"\", \"result_message\" : \"" + letsApprRtnDto.getErrMessage() + "\"}";
                        }else {
                            rtnJson = "{\"result_code\" : \""+letsApprRtnDto.getErrCode()+"\", \"result_message\" : \"" + letsApprRtnDto.getErrMessage() + "\"}";
                        }

                    } else {
                        letsApprDto.setOrderno(letsApprRtnDto.getCompOrderno());
                        letsApprDto.setErrCode("-1");
                        letsApprDto.setErrMessage("LETS 연동에러 : (" + httpCon.getResponseCode() + ") " + httpCon.getResponseMessage());
                        rtnJson = "{\"result_code\" : \"E999\", \"result_message\" : \"" + "LETS 연동에러 : (" + httpCon.getResponseCode() + ") " + httpCon.getResponseMessage() + "\"}";
                    }
                } catch (IOException e) {
                    letsApprDto.setOrderno(letsApprRtnDto.getCompOrderno());
                    letsApprDto.setErrCode("-1");
                    letsApprDto.setErrMessage("LETS 연동에러 : (" + httpCon.getResponseCode() + ") " + httpCon.getResponseMessage());
                    rtnJson = "{\"result_code\" : \"E999\", \"result_message\" : \"" + "LETS 연동에러 : (" + httpCon.getResponseCode() + ") " + httpCon.getResponseMessage() + "\"}";
                } finally {
                    httpCon.disconnect();
                }
            } catch (IOException e) {
                letsApprDto.setOrderno(letsApprRtnDto.getCompOrderno());
                letsApprDto.setErrCode("-1");
                letsApprDto.setErrMessage("LETS 연동중 에러 : " + e.getMessage());
                rtnJson = "{\"result_code\" : \"E999\", \"result_message\" : \"" + "LETS 연동중 에러 : " + e.getMessage() + "\"}";
            }
        }else if(userDto.getWolsaeGbn().equals("W")){
            //카드 결제
            try {
                ApiApprovalDto apiApprovalDto = new ApiApprovalDto();

                Double fee = 4.4;
                Double payAmt = (ctrtDto.getRcvPrice() * (fee / 100))  + ctrtDto.getRcvPrice();
                int payRealAmt = (int)Math.floor(payAmt);

                apiApprovalDto.setAmount(String.valueOf(payRealAmt));
                apiApprovalDto.setCardNo(ctrtDto.getReqCardNo());
                apiApprovalDto.setOrderNo(userDto.getAgentCode() + "-" + String.valueOf(System.currentTimeMillis()));
                apiApprovalDto.setCardExpiryYm(ctrtDto.getCardExpiryYm().substring(2,4)+ctrtDto.getCardExpiryYm().substring(0,2));
                apiApprovalDto.setUserName(userDto.getBizNm());
                apiApprovalDto.setProductName(ctrtInfo.getCtrtName());
                apiApprovalDto.setCardSellMm(ctrtDto.getCardSellMm());
                apiApprovalDto.setAgentCode(userDto.getAgentCode().toString());
                apiApprovalDto.setProductName("임대료결제");
                apiApprovalDto.setUserId(userDto.getUserId());
                apiApprovalDto.setPayRoot("STORESEC");
                //apiApprovalDto.setBirthday(ctrtDto.getBirthday());
                //apiApprovalDto.setCardPw(ctrtDto.getCardPw());

                String approvalApiUrl = "https://payapi.welcomepayments.co.kr/api/payment/approval";
                String sendJson = getApprovalParameter(apiApprovalDto);
                ResponseEntity<String> response = apiExchange(approvalApiUrl, sendJson);

                ctrtMapper.insertApprovalMonInfo(apiApprovalDto);

                rtnJson = (String)response.getBody();

                System.out.println("RTN JSON = " + rtnJson);
            } catch (HttpClientErrorException e) {
                System.out.println("Status :" + e.getStatusCode());
                System.out.println("body :" + e.getResponseBodyAsString());
                rtnJson = e.getResponseBodyAsString();
            } catch (HttpServerErrorException e) {
                System.out.println("Status :" + e.getStatusCode());
                System.out.println("body :" + e.getResponseBodyAsString());
                rtnJson = e.getResponseBodyAsString();
            } catch (Exception e) {
                rtnJson = "{\"result_code\" : \"E999\", \"result_message\" : \"" + e.getMessage() + "\"}";
            }

            Gson gson = new Gson();
            ApiApprovalRtnDto apiApprovalRtnDto = new ApiApprovalRtnDto();

            System.out.println(rtnJson);

            apiApprovalRtnDto = (ApiApprovalRtnDto)gson.fromJson(rtnJson, ApiApprovalRtnDto.class);

            ctrtMapper.insertApprovalMonInfoRtn(apiApprovalRtnDto);

            //정상 카드 결제시
            if (apiApprovalRtnDto.getResult_code().equals("0000")) {
                //케이솔루션 이체 요청
                SPCDto spcDto = new SPCDto();

                spcDto.setCardOrderNo(apiApprovalRtnDto.getOrder_no());
                spcDto.setCtrtSeq(ctrtInfo.getSeq());
                spcDto.setMid(T_NEW_WOLSAE_MID);
                spcDto.setReqDt(DateUtils.getToday());
                spcDto.setReqTm(DateUtils.getTime("HHmmss"));
                spcDto.setTranReqDt(ctrtDto.getTranReqDtVal().replaceAll("-", ""));
                spcDto.setReqSeq(spcService.selectReqSeqInfo());
                spcDto.setWpsSeq(apiApprovalRtnDto.getTransaction_no());
                spcDto.setDealCd("DC01");
                spcDto.setDelionNo(userDto.getCmpGbn() + "_" + userDto.getAgentCode());
                spcDto.setBizRegNo(userDto.getBizNo());
                spcDto.setCustNm(userDto.getStoreNm());
                spcDto.setApprovalNo(apiApprovalRtnDto.getApproval_no());
                spcDto.setBankCd(String.valueOf(ctrtInfo.getBankCd()));
                spcDto.setVirAccNo(ctrtInfo.getBankNum());
                spcDto.setDepositor(ctrtInfo.getRcvUserNm());
                spcDto.setRemittor(ctrtInfo.getDepositName());
                spcDto.setApprAmt(Integer.valueOf(Integer.parseInt(apiApprovalRtnDto.getAmount())));

                double feeAmt = (Integer.valueOf(ctrtDto.getRcvPrice()) * (4.4 / 100));

                spcDto.setDelionFee(Double.valueOf(Math.round(feeAmt)));
                spcDto.setRentAmt(Integer.valueOf(Integer.parseInt(apiApprovalRtnDto.getAmount())) - Double.valueOf(Math.round(feeAmt)));
                spcDto.setSendGbn(ctrtDto.getSendGbn());
                spcDto.setDefPrice(ctrtDto.getRcvPrice());
                spcDto.setCardtxt(apiApprovalRtnDto.getCard_name());
                spcDto.setReqCardNo(ctrtDto.getReqCardNo());
                spcDto.setMinusPrice(ctrtDto.getMinusPrice() - ctrtDto.getRcvPrice());
                spcDto.setTaxGbn(ctrtDto.getTaxGbn());
                spcDto.setTaxNum(ctrtDto.getTaxNum().replaceAll(",",""));

                spcService.insertSpcInfo(spcDto);

                if(apiApprovalRtnDto.getResult_code().equals("0000")){
                    rtnJson = "{\"result_code\" : \"0000\", \"result_message\" : \"카드결제 정상처리 및 입금요청 완료\"}";
                }else {
                    rtnJson = "{\"result_code\" : \""+apiApprovalRtnDto.getResult_code()+"\", \"result_message\" : \"" + apiApprovalRtnDto.getResult_message() + "\"}";
                }

                System.out.println("SPC RTN JSON = " + rtnJson);

            }else {
                rtnJson = "{\"result_code\" : \""+apiApprovalRtnDto.getResult_code()+"\", \"result_message\" : \"" + apiApprovalRtnDto.getResult_message() + "\"}";
            }
        }

        return rtnJson;
    }

    @Override
    public String selectUseDayCheck(CtrtDto ctrtDto) throws Exception{
        String rtnJson = "";

        HoliDto holiDto = ctrtMapper.selectUseDayCheck(ctrtDto);

        if(holiDto != null){
            rtnJson = "{\"result_code\" : \"-1\", \"result_message\" : \""+holiDto.getHolidayNm()+"\"}";
        }else {
            rtnJson = "{\"result_code\" : \"00\", \"result_message\" : \"사용가능일자\"}";
        }

        return rtnJson;
    }

    public String replaceLetsParams(String str) throws Exception{
        String rtnStr = "";

        rtnStr = str.replaceAll("&", "").replaceAll("=", "");

        return rtnStr;
    }

    @Override
    public List<CtrtDto> selectCtrtPayList(CtrtDto ctrtDto) throws Exception {
        return ctrtMapper.selectCtrtPayList(ctrtDto);
    }

    @Override
    public void inertReCtrtInfo(CtrtDto ctrtDto) throws Exception {
        ctrtMapper.inertReCtrtInfo(ctrtDto);

        UserDto userDto = userService.getUserInfo(ctrtDto.getUserId());

        //신청 메일 발송
        MailSend mailSend = new MailSend();
        HashMap<String, Object> mailData = new HashMap<>();
        mailData.put("fromAddress", "delion1111@naver.com");
        mailData.put("toAddress", "delion0823@naver.com");
        mailData.put("ccAddress", "paybank8001@daum.net;jangsos1111@naver.com;moumaleang2@gmail.com;bangmarket@naver.com;inhwanbuis@gmail.com;delion28@naver.com;delion96@naver.com;tnsgh8@naver.com;baeseongu@naver.com;mmkkop417@gmail.com");
        mailData.put("title", "월세페이 재심사 신청");
        mailData.put("content", "월세페이 재심사 신청\n\n ·계정: [" + ctrtDto.getUserId() + "]\n ·상호명: [" + userDto.getStoreNm() + "]\n ·계약명: [" + ctrtDto.getCtrtName() + "]");
        mailSend.MailSend(mailData);
    }

    /**
     * 승인 정보
     *
     * @return
     * @throws UnsupportedEncodingException
     * @throws NoSuchAlgorithmException
     */
    public String getApprovalParameter(ApiApprovalDto apiApprovalDto) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        String mid = "";
        String apiKey = "";
        String ivValue = "";

        mid = T_NEW_WOLSAE_MID;
        apiKey = T_NEW_WOLSAE_WELCOME_API_KEY;
        ivValue = T_NEW_WOLSAE_IV_KEY;

        String payType = "CREDIT_CARD";
        String payMethod = "CREDIT_UNAUTH_API";
        String amount = apiApprovalDto.getAmount();                 //결제금액
        String millis = String.valueOf(System.currentTimeMillis());
        String cardNo = apiApprovalDto.getCardNo();                 //카드번호
        //String birthday = apiApprovalDto.getBirthday();                                       //카드소유주 생년월일 6자리 (yymmdd)
        //String cardPw = apiApprovalDto.getCardPw();                                         //카드 패스워드 앞 2자리
        String orderNo = apiApprovalDto.getOrderNo();
        String cardExpiryYm = apiApprovalDto.getCardExpiryYm();     //카드 유효기간 yymm
        String userName = StringReplace(apiApprovalDto.getUserName());             //구매자 이름
        String productName = apiApprovalDto.getProductName();       //상품 이름
        String cardSellMm = apiApprovalDto.getCardSellMm();         //할부개월수 (일시불 : 00, 2개월 : 02, 3개월 : 03 ... 두자리숫자)
        String echo = "";                                           //요청데이터 그대로 반환(필요없을시 공백으로)
        //암호화 hash_value
        String hash_value = EncryptUtil.sha256(mid + payType + payMethod + orderNo + amount + millis + apiKey);
        //암호화 카드 번호
        String card_no = EncryptUtil.aes256Encrypt(apiKey, ivValue, cardNo);
        //암호화 카드 소유주 생년월일
        //String encBirthday = EncryptUtil.aes256Encrypt(apiKey, ivValue, birthday);
        //암호화 카드 패스워드 앞 2자리
        //String encCardPw = EncryptUtil.aes256Encrypt(apiKey, ivValue, cardPw);

        Map<String, Object> param = new HashMap<>();
        param.put("mid", mid);
        param.put("pay_type", payType);
        param.put("pay_method", payMethod);
        param.put("card_no", card_no);
        //param.put("card_holder_ymd", encBirthday);
        //param.put("card_pw", encCardPw);
        param.put("card_expiry_ym", cardExpiryYm);
        param.put("order_no", orderNo);
        param.put("user_name", userName);
        param.put("amount", amount);
        param.put("product_name", productName);
        param.put("card_sell_mm", cardSellMm);
        param.put("echo", echo);
        param.put("millis", millis);
        param.put("hash_value", hash_value);

        return getJsonString(param);
    }

    public static String getJsonString(Map<String, Object> map ){
        JSONObject jsonObject = new JSONObject();
        map.forEach((key, value) -> jsonObject.put(key, value));
        return jsonObject.toJSONString();
    }

    public static String StringReplace(String str){
        String match = "[^\uAC00-\uD7A3xfe0-9a-zA-Z\\s]";
        str =str.replaceAll(match, "");
        return str;
    }

    /**
     * API 호출부분
     *
     * @param url
     * @param body
     * @return
     */
    public ResponseEntity<String> apiExchange(String url, String body){
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);

        HttpEntity<String> entity = new HttpEntity<>(body, headers);

        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
        return response;
    }

}
