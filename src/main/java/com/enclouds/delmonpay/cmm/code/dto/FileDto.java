package com.enclouds.delmonpay.cmm.code.dto;

import lombok.Data;

@Data
public class FileDto {

    private int seq;
    private String fileName;
    private String fileSysName;
    private long fileSize;
    private String fileExt;
    private String filePath;
    private String regDate;

}
