package com.enclouds.delmonpay.user.dto;

import lombok.Data;

/**
 * 사용자 정보
 * @author Enclouds
 * @since 2020.12.11
 */

@Data
public class UserDto {

    private Integer agentCode;
    private String userId;
    private String password;
    private String agentNm;
    private String roleCd;
    private String bizNo;
    private String ownerNm;
    private String ownerIdNum;
    private String telNum;
    private String loginIp;
    private Integer agentUpCode;
    private Integer agentGbnCd;
    private String ownerAddress;
    private String bizNm;
    private String corpNum;
    private String bizAddress;
    private String bizCond;
    private String bizKind;
    private Integer constStatusCd;
    private String fee;
    private String bankCd;
    private String bankNum;
    private String bankOwner;
    private String regDate;
    private String regUserId;
    private String uptDate;
    private String uptUserId;
    private String storeNm;
    private String storeTel;
    private String mobileTel;
    private String userGbn;
    private String agreeYn;
    private String cmpGbn;
    private String wolsaeGbn;

}
