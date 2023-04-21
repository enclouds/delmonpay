package com.enclouds.delmonpay.ctrt.dto;

import lombok.Data;

@Data
public class ApiApprovalRtnDto {

    private String result_code;
    private String result_message;
    private String mid;
    private String order_no;
    private String user_name;
    private String amount;
    private String product_name;
    private String card_sell_mm;
    private String echo;
    private String approval_no;
    private String transaction_no;
    private String apporval_ymdhms;
    private String card_code;
    private String card_name;

}
