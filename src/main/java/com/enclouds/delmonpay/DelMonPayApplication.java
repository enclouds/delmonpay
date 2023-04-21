package com.enclouds.delmonpay;

import com.enclouds.delmonpay.config.SessionListener;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;

import javax.servlet.http.HttpSessionListener;

@SpringBootApplication
public class DelMonPayApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(DelMonPayApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder){
        return builder.sources(DelMonPayApplication.class);
    }

    @Bean
    public HttpSessionListener httpSessionListener(){
        return new SessionListener();
    }
}