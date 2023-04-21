package com.enclouds.delmonpay.cmm.code.service;

import com.enclouds.delmonpay.cmm.code.dto.BankCodeDto;
import com.enclouds.delmonpay.cmm.code.dto.CodeDto;

import java.util.List;

public interface CodeService {

    List<CodeDto> selectCodeList(CodeDto codeDto) throws Exception;

    CodeDto selectDtlCodeInfo(CodeDto codeDto) throws Exception;

    CodeDto selectDtlCodeInfoAsVal(CodeDto codeDto) throws Exception;

    List<BankCodeDto> selectBankList() throws Exception;

    List<BankCodeDto> selectBankListLets() throws Exception;

}
