package com.enclouds.delmonpay.cmm.util;

import com.enclouds.delmonpay.cmm.code.dto.FileDto;
import com.enclouds.delmonpay.cmm.code.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

public class FileUploadUtil {

    @Autowired
    private FileService fileService;

    private static final String EXTERNAL_FILE_PATH = "C:/delmonpay/delmonUploadedFiles/";

    //\\10.10.18.199\delmonUploadedFiles

    public int uploadFile(MultipartFile file) throws Exception{
        String fileName = file.getOriginalFilename();
        String fileRealName = fileName.substring(0, fileName.lastIndexOf("."));
        String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1);
        String fileSysName = System.currentTimeMillis() + fileExt;
        long fileSize = file.getSize();

        File fileFolder = new File(EXTERNAL_FILE_PATH);

        if(!fileFolder.exists()){
            if(fileFolder.mkdir()){
                System.out.println("[file.mkdirs] : SUCCESS");
            }else {
                System.out.println("[file.mkdirs] : Fail");
            }
        }

        file.transferTo(new File(EXTERNAL_FILE_PATH + fileSysName));

        FileDto fileDto = new FileDto();
        fileDto.setFileName(fileRealName);
        fileDto.setFilePath(EXTERNAL_FILE_PATH);
        fileDto.setFileSize(fileSize);
        fileDto.setFileExt(fileExt);
        fileDto.setFileSysName(fileSysName);

        int fileSeq = fileService.uploadFileData(fileDto);

        return  fileSeq;
    }

}
