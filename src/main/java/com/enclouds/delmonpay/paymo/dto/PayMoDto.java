package com.enclouds.delmonpay.paymo.dto;

import lombok.Data;

@Data
public class PayMoDto {

    private String apiKey;
    private String userId;

    private String amountStr;
    private String email;
    private String mobTel;
    private String userNm;
    private String rcvUserNm;
    private String bankNum;
    private String bankCd;

    private String type;
    private String mid;
    private String buyerid;
    private String orderNumber;
    private String payAmt;
    private String buyerName;
    private String buyerTel;
    private String buyerEmail;
    private String goodsName;
    private String returnUrl;
    private String ediDate;
    private String receiverName;
    private String receiverAddress;
    private String signature;
    private String charSet;
    private String remark;
    private String tranBankCode;
    private String tranBankName;
    private String tranBankAccount;

    private String resultCode;
    private String resultMsg;
    private String otid;
    private String custGuid;

}
