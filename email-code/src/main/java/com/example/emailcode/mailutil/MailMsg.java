package com.example.emailcode.mailutil;

import jakarta.annotation.Resource;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@Slf4j
public class MailMsg {


    @Value("${spring.mail.username}")
    private String fromMail;
    @Resource
    private JavaMailSenderImpl javaMailSender;

    @Autowired
    private StringRedisTemplate redisTemplate;

    public boolean mail(String toMail) throws MessagingException {

        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            // 生成随机码
            String code = CodeGeneratorUtil.generateCode(6);
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);

            // 设置邮件格式
            mimeMessageHelper.setText("<p style='color: blue'>邮箱验证码: " + code + "(有效期为一分钟)</p>", true);
            // 设置邮件主题名
            mimeMessageHelper.setSubject("测试邮箱验证码");
            // 邮件目的地址
            mimeMessageHelper.setTo(toMail);
            mimeMessageHelper.setFrom(fromMail);

            // 将邮箱验证码以邮件地址为key存入redis中，1分钟有效期
            redisTemplate.opsForValue().set(toMail, code, Duration.ofMinutes(1));
            javaMailSender.send(mimeMessage);
        } catch (Exception e) {
            log.info("异常: {}", e.getMessage());
        }
        return true;

    }
}
