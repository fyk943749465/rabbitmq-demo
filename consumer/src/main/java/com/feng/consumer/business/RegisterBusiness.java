package com.feng.consumer.business;

import com.feng.consumer.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Component
public class RegisterBusiness {

    @Autowired
    private UserService userService;

    @Async
    public void execute(String msg) {

        userService.processMsg(msg);

    }
}
