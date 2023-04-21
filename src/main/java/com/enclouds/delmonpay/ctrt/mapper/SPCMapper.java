package com.enclouds.delmonpay.ctrt.mapper;

import com.enclouds.delmonpay.ctrt.dto.ApiCancelDto;
import com.enclouds.delmonpay.ctrt.dto.ApiCancelRtnDto;
import com.enclouds.delmonpay.ctrt.dto.SPCDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SPCMapper {

    String selectReqSeqInfo() throws Exception;

    int insertSpcInfo(SPCDto spcDto) throws Exception;

    SPCDto selectSpcInfo(SPCDto spcDto) throws Exception;

    void updateCancelInfo(SPCDto spcDto) throws Exception;

    ApiCancelDto selectCardApprovalInfo(SPCDto spcDto) throws Exception;

    void insertCardCancelInfo(ApiCancelDto apiCancelDto) throws Exception;

    void insertCancelRtnInfo(ApiCancelRtnDto apiCancelRtnDto) throws Exception;

}
