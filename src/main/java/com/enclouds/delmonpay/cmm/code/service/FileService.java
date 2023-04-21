package com.enclouds.delmonpay.cmm.code.service;

import com.enclouds.delmonpay.cmm.code.dto.FileDto;

public interface FileService {

    int uploadFileData(FileDto fileDto) throws Exception;

}
