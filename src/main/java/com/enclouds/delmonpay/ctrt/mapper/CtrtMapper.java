package com.enclouds.delmonpay.ctrt.mapper;

import com.enclouds.delmonpay.ctrt.dto.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CtrtMapper {

    int insertCtrt(CtrtDto ctrtDto) throws Exception;

    int updateCtrt(CtrtDto ctrtDto) throws Exception;

    int deleteCtrt(CtrtDto ctrtDto) throws Exception;

    List<CtrtDto> selectCtrtList(int agentCode) throws Exception;

    CtrtDto selectCtrtInfo(CtrtDto ctrtDto) throws Exception;

    int deleteFile01(CtrtDto ctrtDto) throws Exception;
    int deleteFile02(CtrtDto ctrtDto) throws Exception;
    int deleteFile03(CtrtDto ctrtDto) throws Exception;

    int paymentCtrt(LetsApprDto letsApprDto) throws Exception;

    void ctrtStatusConfirm(CtrtDto ctrtDto) throws Exception;

    List<CtrtDto> selectCtrtPayList(CtrtDto ctrtDto) throws Exception;

    void inertReCtrtInfo(CtrtDto ctrtDto) throws Exception;

    void insertApprovalMonInfo(ApiApprovalDto apiApprovalDto) throws Exception;
    void insertApprovalMonInfoRtn(ApiApprovalRtnDto apiApprovalRtnDto) throws Exception;

    HoliDto selectUseDayCheck(CtrtDto ctrtDto) throws Exception;

}
