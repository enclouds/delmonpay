package com.enclouds.delmonpay.ctrt.dto;

import lombok.Data;

@Data
public class LetsApprDto {

    private Integer ctrtSeq;
    private String shopcode;
    private String loginid;
    private String servicecode;
    private int orderReqAmt;
    private int defPrice;
    private String receiveType;
    private String orderGoodsname;
    private String orderName;
    private String orderHp;
    private String orderEmail;
    private String reqCardNo;
    private String reqCardmonth;
    private String reqCardyear;
    private String reqInstallment;
    private String compOrderno;
    private String compMemno;
    private String shBisno;
    private String shBisname;
    private String shBankcd;
    private String shBankno;

    private String errCode;
    private String errMessage;
    private String orderno;
    private String apprNo;
    private String apprTranNo;
    private String apprShopCode;
    private String apprDate;
    private String apprTime;
    private String cardtxt;
    private String cardcode;
    private String compAcctamt;
    private String membAcctamt;

    private String sendGbn;

    private Integer minusPrice;

}
