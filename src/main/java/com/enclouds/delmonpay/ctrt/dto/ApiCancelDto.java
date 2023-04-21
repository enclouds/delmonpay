package com.enclouds.delmonpay.ctrt.dto;

import lombok.Data;

@Data
public class ApiCancelDto {

    private String apiKey;
    private String transactionNo;
    private String transactionType;
    private String amount;
    private String userId;
    private String cancelReason;
    private String ipAddress;
    private String agentCode;
    private String approvalNo;

    private String cmpGbn;

}
