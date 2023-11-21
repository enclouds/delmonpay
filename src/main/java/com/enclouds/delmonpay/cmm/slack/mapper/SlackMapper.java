package com.enclouds.delmonpay.cmm.slack.mapper;

import com.enclouds.delmonpay.cmm.slack.dto.SlackDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SlackMapper {

    int insertSlackMessage(SlackDto slackDto) throws Exception;

}
