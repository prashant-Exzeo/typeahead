package com.one97;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

/**
 * Created by prashant on 4/9/15.
 */
@ComponentScan(basePackages = "com.one97")
@EnableAutoConfiguration
public class AppInitiator {

    private static final Logger LOGGER = LoggerFactory.getLogger(AppInitiator.class);

    public static void main(String[] args) {
        SpringApplication.run(AppInitiator.class, args);
        LOGGER.debug(" Application Boot Initiated......");
    }
}
