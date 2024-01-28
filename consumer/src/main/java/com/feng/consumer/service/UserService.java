package com.feng.consumer.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserService {
    public void processMsg(String msg) {

        log.info("收到消息:{}", msg);
    }
}
