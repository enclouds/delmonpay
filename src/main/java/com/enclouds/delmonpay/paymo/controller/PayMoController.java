package com.enclouds.delmonpay.paymo.controller;

import com.enclouds.delmonpay.cmm.code.dto.BankCodeDto;
import com.enclouds.delmonpay.cmm.code.dto.CodeDto;
import com.enclouds.delmonpay.cmm.code.service.CodeService;
import com.enclouds.delmonpay.cmm.util.StringUtils;
import com.enclouds.delmonpay.paymo.dto.PayMoDto;
import com.enclouds.delmonpay.paymo.dto.PayMoUserDto;
import com.enclouds.delmonpay.paymo.service.PayMoService;
import com.enclouds.delmonpay.paymo.util.SignatureUtil;
import com.enclouds.delmonpay.paymo.util.WpAES256Cipher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/paymo")
public class PayMoController {

    private final String CALL_URL = "https://pay.welcomefin.com/weltranpay";

    private final String SITE_DOMAIN = "https://paymoa.storesec.co.kr";
    private final String SITE_DOMAIN_DEV = "http://localhost:8081";

    private final String KEY_HASH_DEV = "DiqFx7Cae8GMJBjTr0WvkfkjYplZ2ssH";         // 가맹점에 제공된 웹 표준 사인키(가맹점 수정후 고정)
    private final String KEY_HASH = "j06NBVe4tluruVLPmTViQnl3sElnPAFp";         // 가맹점에 제공된 웹 표준 사인키(가맹점 수정후 고정)

    private final String Q_KEY_DEV = "a4edd7356e8f12e614727249eac25478d6ff79e9615f8673c3c9229f0ea51a86"; // 가앵점에 제공된 Encrypt_key
    private final String Q_KEY = "e5e645bf0da1a858abc54e3bd50c01987183d354cbf0cd07f16df930b37d35ec"; // 가앵점에 제공된 Encrypt_key

    private final String IV_DEV = "04c1c025f700dea08d885494ca120016"; // 가맹점에서 제공된 Initial_vector
    private final String IV = "64f8b2454865c30258235dc2e080a6f0"; // 가맹점에서 제공된 Initial_vector

    private final static String API_KEY = "Cv9[+r#ny$t89NmBDhYk";

    @Autowired
    private PayMoService payMoService;

    @Autowired
    private CodeService codeService;

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public ModelAndView index(HttpServletResponse response, @ModelAttribute PayMoDto payMoDto) throws Exception{
        ModelAndView mv = new ModelAndView("paymo/index2");
        PayMoUserDto payMoUserDto = payMoService.selectIntegUserInfo(payMoDto);

        //Map<String, Object> bankMap = payMoService.getBankList();
        //ArrayList bankList = (ArrayList)bankMap.get("data");

        //List<BankCodeDto> bankList = codeService.selectBankList();

        //mv.addObject("bankCodeList", bankList);
        mv.addObject("payMoUserDto", payMoUserDto);

        return mv;
    }

    @RequestMapping(value = "/index2", method = RequestMethod.GET)
    public ModelAndView index2(HttpServletResponse response, @ModelAttribute PayMoDto payMoDto) throws Exception{
        ModelAndView mv = new ModelAndView("paymo/index");
        PayMoUserDto payMoUserDto = payMoService.selectIntegUserInfo(payMoDto);

        //Map<String, Object> bankMap = payMoService.getBankList();
        //ArrayList bankList = (ArrayList)bankMap.get("data");

        //List<BankCodeDto> bankList = codeService.selectBankList();

        //mv.addObject("bankCodeList", bankList);
        mv.addObject("payMoUserDto", payMoUserDto);

        return mv;
    }

    @RequestMapping(value = "/bankPopup", method = RequestMethod.GET)
    public ModelAndView bankPopup(HttpServletResponse response, @ModelAttribute PayMoDto payMoDto) throws Exception{
        ModelAndView mv = new ModelAndView("paymo/bankPopup");

        List<BankCodeDto> bankList = codeService.selectBankList();
        mv.addObject("bankList", bankList);

        return mv;
    }

    @RequestMapping(value = "/tran", method = RequestMethod.POST)
    public ModelAndView tranreq(HttpServletResponse response, @ModelAttribute PayMoDto payMoDto) throws Exception{
        ModelAndView mv = new ModelAndView("paymo/tranRequest");

        HashMap<String, Object> hm = new HashMap<>();

        //############################################
        // 1.호출 URL 설정(***가맹점 개발수정***)
        // 운영 https://pay.welcomefin.com
        //############################################
        String call_url = CALL_URL;
        hm.put("call_url", call_url);

        //############################################
        // 2.전문 필드 값 설정(***가맹점 개발수정***)
        //############################################
        //인증
        String keyHash = KEY_HASH;
        String qkey = Q_KEY;
        String iv = IV;
        hm.put("keyHash", keyHash);
        hm.put("qkey", qkey);
        hm.put("iv", iv);

        // 여기에 설정된 값은 Form 필드에 동일한 값으로 설정
        String reqType    = "T"; // 결제 : P, 송금 : T
        String mid	   	  = "welcome02";		// 가맹점 ID(가맹점 수정후 고정) : welcome01
        String ediDate	  = SignatureUtil.getTimestamp();			// util에 의해서 자동생성
        String orderNumber = mid+"_"+SignatureUtil.getTimestamp();	// 가맹점 주문번호(가맹점에서 직접 설정)
        String payAmt	  = payMoDto.getAmountStr().replaceAll(",", "");
        String buyerTel = WpAES256Cipher.encrypt(qkey,iv,payMoDto.getMobTel());
        String buyerEmail = WpAES256Cipher.encrypt(qkey,iv,payMoDto.getEmail());
        String tranBankAccount = WpAES256Cipher.encrypt(qkey,iv,payMoDto.getBankNum());

        hm.put("reqType", reqType);
        hm.put("mid", mid);
        hm.put("ediDate", ediDate);
        hm.put("orderNumber", orderNumber);
        hm.put("payAmt", payAmt);
        hm.put("buyerTel", buyerTel);
        hm.put("buyerEmail", buyerEmail);
        hm.put("tranBankAccount", tranBankAccount);

        CodeDto codeDto = new CodeDto();
        codeDto.setGrpCode(9);
        codeDto.setCodeValue01(StringUtils.padLeftZeros(payMoDto.getBankCd(), 3));
        CodeDto bankInfo = codeService.selectDtlCodeInfoAsVal(codeDto);

        hm.put("bankCd", StringUtils.padLeftZeros(payMoDto.getBankCd(), 3));
        hm.put("userNm", payMoDto.getRcvUserNm());
        hm.put("userId", payMoDto.getUserId());
        hm.put("mobTel", payMoDto.getMobTel());
        hm.put("email", payMoDto.getEmail());
        hm.put("bankNm", bankInfo.getCodeNm());
        hm.put("bankNum", payMoDto.getBankNum());
        hm.put("goodsName", "송금");
        hm.put("returnUrl", SITE_DOMAIN + "/paymo/result");

        //###############################################
        // 3. 가맹점 확인을 위한 keyHash를 해시값으로 변경 (SHA-256방식 사용)
        //###############################################
        String mKey = SignatureUtil.hash(keyHash, "SHA-256");
        hm.put("mKey", mKey);

        //###############################################
        // 4.signature 생성
        //###############################################
        // signature 데이터 생성 (key값을 기준으로 알파벳 순으로 정렬후 NVP 방식으로 나열해 hash)
        String signString="mKey="+mKey+"&reqType="+reqType+"&mid="+mid+"&orderNumber="+orderNumber+"&payAmt="+payAmt+"&ediDate="+ediDate;
        String signature = SignatureUtil.hash(signString, "SHA-256");
        hm.put("signString", signString);
        hm.put("signature", signature);

        /* 기타 */
        String siteDomain = SITE_DOMAIN; //가맹점 도메인 입력

        // 페이지 URL에서 고정된 부분을 적는다.
        // Ex) returnURL이 http://localhost:8080/WelPayMoStdSample/WelPayMoStdReturn.jsp 라면
        // http://localhost:8080/WelPayMoStdSample 까지만 기입한다.
        hm.put("siteDomain", siteDomain);

        mv.addObject("hm", hm);

        //송금정보 저장
        payMoService.insertPayMoUserInfo(hm);

        //송금 로그 저장
        payMoService.insertPaymoTranLog(hm);

        return mv;
    }

    @RequestMapping(value = "/result", method = RequestMethod.POST)
    public ModelAndView result(HttpServletRequest request, HttpServletResponse response) throws Exception{
        ModelAndView mv = new ModelAndView("paymo/result");

        Enumeration params = request.getParameterNames();

        HashMap<String, Object> hm = new HashMap<String, Object>();

        while(params.hasMoreElements()){
            String paramName = (String) params.nextElement();
            String paramValue =new String(request.getParameter(paramName).getBytes("UTF-8"), "UTF-8");
            hm.put(paramName, paramValue);
        }

        mv.addObject("hm", hm);

        //수신 로그 저장
        payMoService.insertPaymoTranRcvLog(hm);

        return mv;
    }

    @RequestMapping(value = "/result/test", method = RequestMethod.GET)
    public ModelAndView resultTest(HttpServletRequest request, HttpServletResponse response) throws Exception{
        ModelAndView mv = new ModelAndView("paymo/result_test");

        //Enumeration params = request.getParameterNames();

        HashMap<String, Object> hm = new HashMap<String, Object>();
        hm.put("resultCode", "0000");
        hm.put("resultMsg", "완료");

        mv.addObject("hm", hm);

        //수신 로그 저장
        //payMoService.insertPaymoTranRcvLog(hm);

        return mv;
    }

    /**
     * 계좌 확인
     *
     * @param bankNum
     * @param bankCd
     * @param rcvUserNm
     * @return
     * @throws Exception
     */
    @GetMapping("/bankNumChk")
    public @ResponseBody
    Map<String, Object> bankNumChk(@RequestParam String bankNum, String bankCd, String rcvUserNm) throws Exception{
        Map<String, Object> chkMap = payMoService.getBankNumChk(bankNum, bankCd, rcvUserNm);

        return (Map<String, Object>)chkMap.get("data");
    }



}
