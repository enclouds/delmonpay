package com.enclouds.delmonpay.ctrt.service;

import com.enclouds.delmonpay.ctrt.dto.CtrtDto;
import com.enclouds.delmonpay.ctrt.dto.SPCDto;

public interface SPCService {

    /**
     * 요청일렬번호 생성
     * SPC
     *
     * @return
     * @throws Exception
     */
    String selectReqSeqInfo() throws Exception;

    /**
     * 케이솔루션 발송 batch 등록
     *
     * @param spcDto
     * @return
     * @throws Exception
     */
    int insertSpcInfo(SPCDto spcDto) throws Exception;

    SPCDto rentCancelCheck(SPCDto spcDto) throws Exception;

    SPCDto rentCancelRequest(SPCDto spcDto) throws Exception;

}
