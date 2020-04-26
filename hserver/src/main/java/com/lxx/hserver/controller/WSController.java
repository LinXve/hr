package com.lxx.hserver.controller;

import com.lxx.hserver.entity.ChatResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;


@Controller
@Slf4j
public class WSController {
    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;

    /**
     * 单聊
     */
    @MessageMapping("/ws/chat")
    public void handleChat(String msg) {
        //发送者;content;接收者
        String from = msg.substring(0, msg.indexOf(";"));
        String content = msg.substring(msg.indexOf(";")+1, msg.lastIndexOf(";"));
        String to = msg.substring(msg.lastIndexOf(";")+1, msg.length());
        simpMessagingTemplate.convertAndSendToUser(to, "/queue/chat", new ChatResp(content, from));
    }

    @MessageMapping("/ws/nf")
    @SendTo("/topic/nf")
    public String handleNf() {
        return "系统消息";
    }
}
