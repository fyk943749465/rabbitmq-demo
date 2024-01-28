package com.feng.producer.controller;

import com.feng.producer.product.RegisterProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {


    @Autowired
    private RegisterProducer registerProducer;


    @GetMapping
    public String register() {

        String uuid = UUID.randomUUID().toString();
        log.info("发送的消息:{}", uuid);
        registerProducer.putRegister(uuid);
        return "消息发送成功";
    }
}
