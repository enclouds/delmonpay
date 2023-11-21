package com.enclouds.delmonpay.cmm.slack.sevice;

import com.enclouds.delmonpay.cmm.slack.dto.SlackDto;

public interface SlackService {

    int insertSlackMessage(SlackDto slackDto) throws Exception;

}
