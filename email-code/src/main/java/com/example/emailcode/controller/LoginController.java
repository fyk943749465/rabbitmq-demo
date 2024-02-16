package com.example.emailcode.controller;

import com.example.emailcode.mailutil.MailMsg;
import com.example.emailcode.rest.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mail/")
@Slf4j
public class LoginController {



    @Autowired
    private MailMsg mailMsg;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @GetMapping(value = "/send/{email}")
    public Result sendCode(@PathVariable String email) {

        log.info("邮箱{}", email);
        String code = redisTemplate.opsForValue().get(email);

        if (!StringUtils.isEmpty(code)) {
            return Result.ok(email + ":" + code + "已经存在，还未过期");
        }
        try {
            boolean sended = mailMsg.mail(email);
            if (sended) {
                return Result.ok("验证码已发送");
            }
            return Result.fail("验证码发送失败，请稍后再试");
        } catch (Exception e) {
            return Result.fail("验证码发送失败，请稍后再试");
        }
    }
}
