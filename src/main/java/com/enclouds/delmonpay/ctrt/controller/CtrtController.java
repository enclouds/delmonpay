package com.enclouds.delmonpay.ctrt.controller;

import com.enclouds.delmonpay.cmm.code.dto.BankCodeDto;
import com.enclouds.delmonpay.cmm.code.service.CodeService;
import com.enclouds.delmonpay.cmm.util.DateUtils;
import com.enclouds.delmonpay.ctrt.dto.CtrtDto;
import com.enclouds.delmonpay.ctrt.dto.SPCDto;
import com.enclouds.delmonpay.ctrt.service.CtrtService;
import com.enclouds.delmonpay.ctrt.service.SPCService;
import com.enclouds.delmonpay.user.dto.UserDto;
import com.enclouds.delmonpay.user.service.UserService;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/ctrt")
public class CtrtController {

    @Autowired
    private UserService userService;

    @Autowired
    private CodeService codeService;

    @Autowired
    private CtrtService ctrtService;

    @Autowired
    private SPCService spcService;

    @RequestMapping(value="/regView")
    public ModelAndView regView(HttpServletRequest request, HttpServletResponse response, @RequestParam String userId) throws Exception{
        ModelAndView mv = new ModelAndView("register");
        String referer = (String)request.getHeader("REFERER");

        UserDto userDto = userService.getUserInfo(userId);

        if(userDto != null){
            List<BankCodeDto> bankList = new ArrayList<>();

            if(userDto.getWolsaeGbn() == null){
                response.setContentType("text/html; charset=UTF-8");
                PrintWriter out = response.getWriter();
                out.println("<script>alert('사용자 정보에 PG사가 지정되지 않았습니다.'); window.close();</script>");
                out.flush();
            }

            if(userDto.getWolsaeGbn().equals("W")){
                bankList = codeService.selectBankList();
            }else {
                bankList = codeService.selectBankListLets();
            }

            SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
            Date todayYear = format.parse(DateUtils.getToday());
            SimpleDateFormat format2 = new SimpleDateFormat("yyyy");
            String today = format2.format(todayYear);

            mv.addObject("bankList", bankList);
            mv.addObject("userDto", userDto);
            mv.addObject("referer", referer);
            mv.addObject("schYear", today);
        }else {
            response.setContentType("text/html; charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.println("<script>alert('사용자 정보가 없습니다.'); window.close();</script>");
            out.flush();
        }

        return mv;
    }

    @PostMapping("/insertCtrt")
    public @ResponseBody
    Map<String, Object> insertCtrt(HttpServletRequest request, HttpServletResponse response, @ModelAttribute("ctrtDto") CtrtDto ctrtDto) throws Exception{
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("resultCode", ctrtService.insertCtrt(ctrtDto));
        result.put("referer", ctrtDto.getReferer());

        return result;
    }

    @PostMapping("/updateCtrt")
    public @ResponseBody
    Map<String, Object> updateCtrt(HttpServletRequest request, HttpServletResponse response, @ModelAttribute("ctrtDto") CtrtDto ctrtDto) throws Exception{
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("resultCode", ctrtService.updateCtrt(ctrtDto));
        result.put("referer", ctrtDto.getReferer());

        return result;
    }

    @PostMapping("/deleteCtrt")
    public @ResponseBody
    Map<String, Object> deleteCtrt(HttpServletRequest request, HttpServletResponse response, @ModelAttribute("ctrtDto") CtrtDto ctrtDto) throws Exception{
        String referer = (String)request.getHeader("REFERER");
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("resultCode", ctrtService.deleteCtrt(ctrtDto));

        return result;
    }

    @RequestMapping("/uptView")
    public ModelAndView uptView(@ModelAttribute CtrtDto ctrtDto, HttpServletRequest request, HttpServletResponse response) throws Exception{
        ModelAndView mv = new ModelAndView("modify");
        String referer = (String)request.getHeader("REFERER");

        UserDto userDto = userService.getUserInfo(ctrtDto.getUserId());

        if(userDto != null){
            List<BankCodeDto> bankList = new ArrayList<>();

            if(userDto.getWolsaeGbn() == null){
                response.setContentType("text/html; charset=UTF-8");
                PrintWriter out = response.getWriter();
                out.println("<script>alert('사용자 정보에 PG사가 지정되지 않았습니다.'); window.close();</script>");
                out.flush();
            }

            if(userDto.getWolsaeGbn().equals("W")){
                bankList = codeService.selectBankList();
            }else {
                bankList = codeService.selectBankListLets();
            }
            ctrtDto.setWolsaeGbn(userDto.getWolsaeGbn());

            CtrtDto ctrtInfo = ctrtService.selectCtrtInfo(ctrtDto);
            mv.addObject("ctrtInfo", ctrtInfo);
            mv.addObject("userDto", userDto);
            mv.addObject("bankList", bankList);
            mv.addObject("referer", referer);
        } else {
            response.setContentType("text/html; charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.println("<script>alert(''); window.location('"+referer+"');</script>");
            out.flush();
        }

        return mv;
    }

    @RequestMapping("/insertReCtrt")
    public ModelAndView insertReCtrt(@ModelAttribute CtrtDto ctrtDto, HttpServletRequest request, HttpServletResponse response) throws Exception{
        ModelAndView mv = new ModelAndView("redirect:"+ctrtDto.getReferer());

        UserDto userDto = userService.getUserInfo(ctrtDto.getUserId());

        if(userDto != null){
            if(userDto.getWolsaeGbn() == null){
                response.setContentType("text/html; charset=UTF-8");
                PrintWriter out = response.getWriter();
                out.println("<script>alert('사용자 정보에 PG사가 지정되지 않았습니다.'); window.close();</script>");
                out.flush();
            }

            ctrtDto.setWolsaeGbn(userDto.getWolsaeGbn());

            //재등록
            ctrtDto.setUserId(userDto.getUserId());
            ctrtService.inertReCtrtInfo(ctrtDto);
        } else {
            response.setContentType("text/html; charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.println("<script>alert(''); window.location('"+ctrtDto.getReferer()+"');</script>");
            out.flush();
        }

        return mv;
    }

    @RequestMapping("/contractView")
    public ModelAndView contractView(@ModelAttribute CtrtDto ctrtDto, HttpServletRequest request, HttpServletResponse response) throws Exception{
        ModelAndView mv = new ModelAndView("contract");
        String referer = (String)request.getHeader("REFERER");

        UserDto userDto = userService.getUserInfo(ctrtDto.getUserId());

        if(userDto != null){
            List<BankCodeDto> bankList = new ArrayList<>();

            if(userDto.getWolsaeGbn() == null){
                response.setContentType("text/html; charset=UTF-8");
                PrintWriter out = response.getWriter();
                out.println("<script>alert('사용자 정보에 PG사가 지정되지 않았습니다.'); window.close();</script>");
                out.flush();
            }

            if(userDto.getWolsaeGbn().equals("W")){
                bankList = codeService.selectBankList();
            }else {
                bankList = codeService.selectBankListLets();
            }
            ctrtDto.setWolsaeGbn(userDto.getWolsaeGbn());

            CtrtDto ctrtInfo = ctrtService.selectCtrtInfo(ctrtDto);
            mv.addObject("ctrtInfo", ctrtInfo);

            Double fee = 4.4;

            Double payAmt = (ctrtInfo.getMinusPrice() * (fee / 100))  + ctrtInfo.getMinusPrice();
            int payRealAmt = (int)Math.floor(payAmt);
            mv.addObject("payRealAmt", payRealAmt);

            mv.addObject("userDto", userDto);
            mv.addObject("bankList", bankList);
            mv.addObject("referer", referer);
            mv.addObject("today", DateUtils.formatDate(DateUtils.addDay(DateUtils.getToday(), 1), "-"));
        } else {
            response.setContentType("text/html; charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.println("<script>alert('사용자 정보가 없습니다.'); window.close();</script>");
            out.flush();
        }

        return mv;
    }

    @RequestMapping("/reRegisterView")
    public ModelAndView reRegisterView(@ModelAttribute CtrtDto ctrtDto, HttpServletRequest request, HttpServletResponse response) throws Exception{
        ModelAndView mv = new ModelAndView("reRegister");
        String referer = (String)request.getHeader("REFERER");

        UserDto userDto = userService.getUserInfo(ctrtDto.getUserId());

        if(userDto != null){
            List<BankCodeDto> bankList = new ArrayList<>();

            if(userDto.getWolsaeGbn() == null){
                response.setContentType("text/html; charset=UTF-8");
                PrintWriter out = response.getWriter();
                out.println("<script>alert('사용자 정보에 PG사가 지정되지 않았습니다.'); window.close();</script>");
                out.flush();
            }

            if(userDto.getWolsaeGbn().equals("W")){
                bankList = codeService.selectBankList();
            }else {
                bankList = codeService.selectBankListLets();
            }
            ctrtDto.setWolsaeGbn(userDto.getWolsaeGbn());

            CtrtDto ctrtInfo = ctrtService.selectCtrtInfo(ctrtDto);
            mv.addObject("ctrtInfo", ctrtInfo);
            mv.addObject("userDto", userDto);
            mv.addObject("bankList", bankList);
            mv.addObject("referer", referer);
        } else {
            response.setContentType("text/html; charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.println("<script>alert('사용자 정보가 없습니다.'); window.close();</script>");
            out.flush();
        }

        return mv;
    }

    @PostMapping("/deleteFileAjax")
    public @ResponseBody
    Map<String, Object> deleteFileAjax(HttpServletRequest request, HttpServletResponse response, @ModelAttribute("ctrtDto") CtrtDto ctrtDto) throws Exception{
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("resultCode", ctrtService.deleteFileAjax(ctrtDto));

        return result;
    }

    @PostMapping("/paymentCtrt")
    public @ResponseBody
    Map<String, Object> paymentCtrt(HttpServletRequest request, HttpServletResponse response, @ModelAttribute("ctrtDto") CtrtDto ctrtDto) throws Exception{
        Map<String, Object> result = new HashMap<String, Object>();
        UserDto userDto = userService.getUserInfo(ctrtDto.getUserId());

        String strJson = ctrtService.paymentCtrt(ctrtDto, userDto);

        JSONParser jsonParser = new JSONParser();
        Object obj = jsonParser.parse(strJson);
        JSONObject jsonObj = (JSONObject) obj;

        result.put("resultCode", jsonObj.get("result_code"));
        result.put("resultMessage", jsonObj.get("result_message"));
        result.put("referer", ctrtDto.getReferer());

        return result;
    }

    @PostMapping("/api/rentCancelCheck")
    public @ResponseBody
    Map<String, Object> rentCancelCheck(HttpServletRequest request, HttpServletResponse response, @ModelAttribute("spcDto") SPCDto spcDto) throws Exception{
        Map<String, Object> result = new HashMap<String, Object>();
        SPCDto spcInfo = spcService.rentCancelCheck(spcDto);

        result.put("resultCode", spcInfo.getResultCd());
        result.put("resultMessage", spcInfo.getResultDtl());

        return result;
    }

    @PostMapping("/api/rentCancelRequest")
    public @ResponseBody
    Map<String, Object> rentCancelRequest(HttpServletRequest request, HttpServletResponse response, @ModelAttribute("spcDto") SPCDto spcDto) throws Exception{
        Map<String, Object> result = new HashMap<String, Object>();
        SPCDto spcInfo = spcService.rentCancelRequest(spcDto);

        result.put("resultCode", spcInfo.getResultCd());
        result.put("resultMessage", spcInfo.getResultDtl());

        return result;
    }

    @PostMapping("/selectUseDayCheck")
    public @ResponseBody
    Map<String, Object> selectUseDayCheck(HttpServletRequest request, HttpServletResponse response, @ModelAttribute("ctrtDto") CtrtDto ctrtDto) throws Exception{
        Map<String, Object> result = new HashMap<String, Object>();

        String strJson = ctrtService.selectUseDayCheck(ctrtDto);

        JSONParser jsonParser = new JSONParser();
        Object obj = jsonParser.parse(strJson);
        JSONObject jsonObj = (JSONObject) obj;

        result.put("resultCode", jsonObj.get("result_code"));
        result.put("resultMessage", jsonObj.get("result_message"));
        result.put("referer", ctrtDto.getReferer());

        return result;
    }

}
