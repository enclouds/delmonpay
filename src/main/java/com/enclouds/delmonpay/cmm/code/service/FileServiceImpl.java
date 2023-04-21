package com.enclouds.delmonpay.cmm.code.service;

import com.enclouds.delmonpay.cmm.code.dto.FileDto;
import com.enclouds.delmonpay.cmm.code.mapper.FileMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FileServiceImpl implements FileService{

    @Autowired
    private FileMapper fileMapper;

    @Override
    public int uploadFileData(FileDto fileDto) throws Exception {
        return fileMapper.uploadFileData(fileDto);
    }

}
