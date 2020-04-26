package com.lxx.hserver.utils;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EmailRunnableTest {
    @Autowired
    JavaMailSender javaMailSender;
    @Test
    public void sendmail() {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("1043138485@qq.com");
        message.setTo("1043138485@qq.com");
        message.setCc("1043138485@qq.com");
        message.setSubject("主题");
        message.setText("内容");
        javaMailSender.send(message);
    }

}


