package com.enclouds.delmonpay.cmm.code.service;

import com.enclouds.delmonpay.cmm.code.dto.BankCodeDto;
import com.enclouds.delmonpay.cmm.code.dto.CodeDto;
import com.enclouds.delmonpay.cmm.code.mapper.CodeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CodeServiceImpl implements CodeService{

    @Autowired
    private CodeMapper codeMapper;

    @Override
    public List<CodeDto> selectCodeList(CodeDto codeDto) throws Exception {
        return codeMapper.selectCodeList(codeDto);
    }

    @Override
    public CodeDto selectDtlCodeInfo(CodeDto codeDto) throws Exception{
        return codeMapper.selectDtlCodeInfo(codeDto);
    }

    @Override
    public CodeDto selectDtlCodeInfoAsVal(CodeDto codeDto) throws Exception{
        return codeMapper.selectDtlCodeInfoAsVal(codeDto);
    }

    @Override
    public List<BankCodeDto> selectBankList() throws Exception {
        return codeMapper.selectBankList();
    }

    @Override
    public List<BankCodeDto> selectBankListLets() throws Exception {
        return codeMapper.selectBankListLets();
    }

}
