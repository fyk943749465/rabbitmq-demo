package com.event.service;

import com.event.event.UserRegisterEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailService implements ApplicationListener<UserRegisterEvent> {
    @Override
    @Async("taskExecutor")
    public void onApplicationEvent(UserRegisterEvent event) {
        log.info("给用户[{}]发送邮件", event.getUsername());
    }
}
