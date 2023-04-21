package com.enclouds.delmonpay.cmm.code.mapper;

import com.enclouds.delmonpay.cmm.code.dto.BankCodeDto;
import com.enclouds.delmonpay.cmm.code.dto.CodeDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CodeMapper {

    List<CodeDto> selectCodeList(CodeDto codeDto) throws Exception;

    CodeDto selectDtlCodeInfo(CodeDto codeDto) throws Exception;

    CodeDto selectDtlCodeInfoAsVal(CodeDto codeDto) throws Exception;

    List<BankCodeDto> selectBankList() throws Exception;

    List<BankCodeDto> selectBankListLets() throws Exception;

}
