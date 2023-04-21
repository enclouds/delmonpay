package com.enclouds.delmonpay.paymo.service;

import com.enclouds.delmonpay.paymo.dto.PayMoDto;
import com.enclouds.delmonpay.paymo.dto.PayMoUserDto;

import java.util.HashMap;
import java.util.Map;

public interface PayMoService {

    int selectIntegUserCnt(PayMoDto payMoDto) throws Exception;

    PayMoUserDto selectIntegUserInfo(PayMoDto payMoDto) throws Exception;

    PayMoUserDto selectPayMoUserInfo(PayMoDto payMoDto) throws Exception;

    void insertPayMoUserInfo(HashMap<String, Object> hm) throws Exception;

    void insertPaymoTranLog(HashMap<String, Object> hm) throws Exception;

    void insertPaymoTranRcvLog(HashMap<String, Object> hm) throws Exception;

    Map<String, Object> getBankList() throws Exception;

    Map<String, Object> getBankNumChk(String bankNum, String bankCd, String userNm) throws Exception;

}
