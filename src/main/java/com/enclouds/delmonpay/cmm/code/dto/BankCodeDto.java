package com.enclouds.delmonpay.cmm.code.dto;

import lombok.Data;

@Data
public class BankCodeDto {

    private int seq;
    private String bankCd;
    private String bankNm;
    private int orderNo;
    private String imgNm;

}
