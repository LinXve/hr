package com.lxx.hserver.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.scheduling.concurrent.DefaultManagedTaskScheduler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import java.util.List;

/**
 * @EnableWebSocketMessageBroker开启websocket消息代理
 *
 */

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer{


    /**
     * 注册消息连接点
     * withSockJS():SockJS是webSocket的通信js库。。
     * @param registry
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws/endpointChat").setAllowedOrigins("*").withSockJS();
    }

    /**
     *MessageBrokerRegistry：消息代理注册
     * @param registry
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setUserDestinationPrefix("/user");
        registry.enableSimpleBroker("/queue","/topic")
                .setTaskScheduler(new DefaultManagedTaskScheduler()) //使用一个线程发送心跳
                .setHeartbeatValue(new  long[]{20000,20000});//设置心跳
    }

}
