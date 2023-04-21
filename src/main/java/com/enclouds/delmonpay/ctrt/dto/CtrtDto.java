package com.enclouds.delmonpay.ctrt.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class CtrtDto{

    private String apiKey;
    private int seq;
    private int agentCode;
    private String userId;
    private String ctrtName;
    private String address;
    private String addressDtl;
    private String depositName;
    private String rcvUserNm;
    private int rcvPrice;
    private String rcvPriceStr;
    private String bankCd;
    private String bankNm;
    private String bankNum;
    private String tranReqDtVal;
    private String tranReqDt;

    private byte[] img01;
    List<MultipartFile> file01;
    private String file01Yn;
    private String file01Name;

    private byte[] img02;
    List<MultipartFile> file02;
    private String file02Yn;
    private String file02Name;

    private byte[] img03;
    List<MultipartFile> file03;
    private String file03Yn;
    private String file03Name;

    private String statusCd;

    private String regDate;
    private String uptDate;

    private String wolsaeGbn;

    private String fileNum;

    private String reqCardNo;
    private String cardExpiryYm;
    private String cardSellMm;

    private String sendGbn;

    private String schYear;
    private String schGbn;

    private String regDateTime;
    private String errCode;
    private String cardtxt;
    private String orderReqAmt;
    private String apprAmt;
    private String totalCnt;

    private int minusPrice;
    private int minusPriceList;
    private int defPrice;

    private int payCount;

    private String referer;

    private String resultCd;
    private String rentResultCd;

    private String approvalNo;
    private String rentSendDate;

    private String reqDt;
    private String reqTm;
    private String reqSeq;
    private String wpsSeq;

    private String cancelYn;

    private String taxGbn;
    private String taxNum;

    private int file01Seq;
    private int file02Seq;
    private int file03Seq;

    private String rsnMemo;

    private String date;

    private String birthday;
    private String cardPw;

}
