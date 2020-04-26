package com.lxx.hserver.config;

import com.lxx.hserver.utils.DateConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
@Slf4j
public class WebMvcConfig implements WebMvcConfigurer {
    /**
     * 跨域配置
     */
    private CorsConfiguration buildCorsConfiguration(){
        CorsConfiguration corsConfiguration=new CorsConfiguration();
        log.info("********WebMvcConfig********");
        corsConfiguration.addAllowedOrigin("*"); //允许所有的域
        corsConfiguration.addAllowedHeader("*");//允许所有的请求头
        corsConfiguration.addAllowedMethod("*");//允许所有的方法(post,delete,get,put..)
        corsConfiguration.setAllowCredentials(true);
        return corsConfiguration;
    }
    /**
     * 跨域过滤器
     */
    @Bean
    public CorsFilter corsFilter(){
        UrlBasedCorsConfigurationSource source=new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**",buildCorsConfiguration());
        return new CorsFilter(source);
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new DateConverter());
    }
//线程池托管给spring
    @Bean
    public ExecutorService executorService(){
        return Executors.newCachedThreadPool();
    }
}
