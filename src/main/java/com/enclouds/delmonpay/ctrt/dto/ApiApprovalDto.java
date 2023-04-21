package com.enclouds.delmonpay.ctrt.dto;

import lombok.Data;

@Data
public class ApiApprovalDto {

    private String userId;
    private String compGbnCode;
    private String agentCode;
    private String apiKey;
    private String orderNo;
    private String cardNo;
    private String amount;
    private String cardExpiryYm;
    private String userName;
    private String productName;
    private String cardSellMm;
    private String shopCd;
    private Double fee;

    //private, biz
    private String cardGbn;
    private String cardPwd;
    private String juminNo;

    private String payRoot;

    private String birthday;
    private String cardPw;

}
