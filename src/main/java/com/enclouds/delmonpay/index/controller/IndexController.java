package com.enclouds.delmonpay.index.controller;

import com.enclouds.delmonpay.cmm.util.DateUtils;
import com.enclouds.delmonpay.ctrt.dto.CtrtDto;
import com.enclouds.delmonpay.ctrt.service.CtrtService;
import com.enclouds.delmonpay.user.dto.UserDto;
import com.enclouds.delmonpay.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
public class IndexController {

    @Autowired
    private UserService userService;

    @Autowired
    private CtrtService ctrtService;

    @RequestMapping(value="/")
    public ModelAndView index(HttpServletRequest request, HttpServletResponse response, @RequestParam String userId, @RequestParam String schYear, @RequestParam String schGbn) throws Exception{
        ModelAndView mv = new ModelAndView("wolsae-skeleton");
        mv.addObject("userId", userId);
        mv.addObject("schYear", schYear);
        mv.addObject("schGbn", schGbn);

        return mv;
    }

    @RequestMapping(value="/index")
    public ModelAndView index2(HttpServletRequest request, HttpServletResponse response, @RequestParam String userId, @RequestParam String schYear, @RequestParam String schGbn) throws Exception{
        ModelAndView mv = new ModelAndView("index");

        UserDto userDto = userService.getUserInfo(userId);

        if(userDto != null){

            if(userDto.getWolsaeGbn() == null){
                response.setContentType("text/html; charset=UTF-8");
                PrintWriter out = response.getWriter();
                out.println("<script>alert('사용자 정보에 PG사가 지정되지 않았습니다.'); window.close();</script>");
                out.flush();
            }

            SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
            Date todayYear = format.parse(DateUtils.getToday());
            SimpleDateFormat format2 = new SimpleDateFormat("yyyy");
            String today = format2.format(todayYear);

            CtrtDto ctrtDto = new CtrtDto();

            if(StringUtils.isEmpty(schYear)){
                ctrtDto.setSchYear(today);
            }else {
                ctrtDto.setSchYear(schYear);
            }

            if(StringUtils.isEmpty(schGbn)){
                ctrtDto.setSchGbn("DESC");
            }else {
                ctrtDto.setSchGbn(schGbn);
            }

            if(StringUtils.isEmpty(ctrtDto.getWolsaeGbn())){
                ctrtDto.setWolsaeGbn(userDto.getWolsaeGbn());
            }

            List<CtrtDto> ctrtList = ctrtService.selectCtrtList(userDto.getAgentCode());

            ctrtDto.setAgentCode(userDto.getAgentCode());
            List<CtrtDto> ctrtPayList = ctrtService.selectCtrtPayList(ctrtDto);

            mv.addObject("ctrtList", ctrtList);
            mv.addObject("userInfo", userDto);
            mv.addObject("IP", getServerIp());
            mv.addObject("ctrtDto", ctrtDto);
            mv.addObject("ctrtPayList", ctrtPayList);
        }else {
            response.setContentType("text/html; charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.println("<script>alert('사용자 정보가 없습니다.'); JavascriptInterface.jsBack();</script>");
            out.flush();
        }

        return mv;
    }

    @RequestMapping(value="/skeleton")
    public ModelAndView skeleton(HttpServletRequest request) throws Exception{
        ModelAndView mv = new ModelAndView("test/wolsae-skeleton");

        return mv;
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

}
