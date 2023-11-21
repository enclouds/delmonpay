package com.enclouds.delmonpay.cmm.slack.sevice;

import com.enclouds.delmonpay.cmm.slack.dto.SlackDto;
import com.enclouds.delmonpay.cmm.slack.mapper.SlackMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SlackServiceImpl implements SlackService {

    @Autowired
    private SlackMapper slackMapper;

    @Override
    public int insertSlackMessage(SlackDto slackDto) throws Exception {
        return slackMapper.insertSlackMessage(slackDto);
    }

}
