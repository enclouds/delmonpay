package com.enclouds.delmonpay.ctrt.service;

import com.enclouds.delmonpay.ctrt.dto.CtrtDto;
import com.enclouds.delmonpay.ctrt.dto.SPCDto;
import com.enclouds.delmonpay.user.dto.UserDto;

import java.util.List;

public interface CtrtService {

    int insertCtrt(CtrtDto ctrtDto) throws Exception;

    int updateCtrt(CtrtDto ctrtDto) throws Exception;

    int deleteCtrt(CtrtDto ctrtDto) throws Exception;

    List<CtrtDto> selectCtrtList(int agentCode) throws Exception;

    CtrtDto selectCtrtInfo(CtrtDto ctrtDto) throws Exception;

    int deleteFileAjax(CtrtDto ctrtDto) throws Exception;

    String paymentCtrt(CtrtDto ctrtDto, UserDto userDto) throws Exception;

    String selectUseDayCheck(CtrtDto ctrtDto) throws Exception;

    List<CtrtDto> selectCtrtPayList(CtrtDto ctrtDto) throws Exception;

    void inertReCtrtInfo(CtrtDto ctrtDto) throws Exception;



}
