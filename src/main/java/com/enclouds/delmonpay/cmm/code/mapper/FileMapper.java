package com.enclouds.delmonpay.cmm.code.mapper;

import com.enclouds.delmonpay.cmm.code.dto.FileDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FileMapper {

    int uploadFileData(FileDto fileDto) throws Exception;

}
