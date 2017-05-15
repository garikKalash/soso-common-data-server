package com.soso;

import org.apache.catalina.authenticator.jaspic.AuthConfigFactoryImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.support.SpringBootServletInitializer;

import javax.security.auth.message.config.AuthConfigFactory;

@SpringBootApplication
public class CommonDataServiceApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        if (AuthConfigFactory.getFactory() == null) {
            AuthConfigFactory.setFactory(new AuthConfigFactoryImpl());
        }
        SpringApplication.run(CommonDataServiceApplication.class, args);
    }

}
