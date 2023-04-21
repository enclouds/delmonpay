package com.enclouds.delmonpay.ctrt.dto;

import lombok.Data;

@Data
public class ApiCancelRtnDto {

    private String result_message;
    private String result_code;
    private String pay_type;
    private String transaction_no;
    private String amount;
    private String order_no;
    private String cancel_amount;
    private String remain_amount;
    private String cancel_ymdhms;

}
