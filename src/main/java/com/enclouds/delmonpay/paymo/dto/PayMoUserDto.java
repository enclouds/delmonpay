package com.enclouds.delmonpay.paymo.dto;

import lombok.Data;

@Data
public class PayMoUserDto {

    private String userId;
    private String userNm;
    private String mobTel;
    private String email;
    private int lastPayAmt;
    private int lastBankCd;
    private String lastBankNm;

}
