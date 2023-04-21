package com.enclouds.delmonpay.paymo.mapper;

import com.enclouds.delmonpay.paymo.dto.PayMoDto;
import com.enclouds.delmonpay.paymo.dto.PayMoUserDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;

@Mapper
public interface PayMoMapper {

    int selectIntegUserCnt(PayMoDto payMoDto) throws Exception;

    PayMoUserDto selectIntegUserInfo(PayMoDto payMoDto) throws Exception;

    PayMoUserDto selectPayMoUserInfo(PayMoDto payMoDto) throws Exception;

    void insertPayMoUserInfo(HashMap<String, Object> hm) throws Exception;

    void insertPaymoTranLog(HashMap<String, Object> hm) throws Exception;

    void insertPaymoTranRcvLog(HashMap<String, Object> hm) throws Exception;

}
