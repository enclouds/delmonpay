package com.enclouds.delmonpay.ctrt.dto;

import lombok.Data;

@Data
public class SPCDto {

    private Integer ctrtSeq;
    private String cardOrderNo;
    private String mid;
    private String reqDt;
    private String reqTm;
    private String tranReqDt;
    private String reqSeq;
    private String wpsSeq;
    private String dealCd;
    private String delionNo;
    private String bizRegNo;
    private String custNm;
    private String approvalNo;
    private String bankCd;
    private String virAccNo;
    private String depositor;
    private String remittor;
    private Integer apprAmt;
    private Double delionFee;
    private Double rentAmt;
    private String resultCd;
    private String resultDtl;
    private Double rentSendAmt;
    private String rentResultCd;
    private String rentResultDtl;
    private String sendGbn;
    private Integer minusPrice;
    private String cardtxt;
    private int defPrice;
    private String reqCardNo;

    private String transactionNo;

    private String taxGbn;
    private String taxNum;

}
