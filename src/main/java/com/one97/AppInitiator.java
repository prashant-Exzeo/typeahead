package com.one97;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

/*
 * ---------------------------------------------------------------------------------------------
 *
 * 		Copyright (c)  2015. Prashant Kumar - All Rights Reserved.
 * 			-	Unauthorized copying of this file, via any medium is strictly prohibited.
 * 			-	This file is Proprietary and Confidential.
 *
 * ---------------------------------------------------------------------------------------------
 */

/**
 * Created by prashant on 4/9/15.
 */
@EnableAutoConfiguration
@ComponentScan(basePackages = "com.one97")
public class AppInitiator {

//    TODO: Create a BootStrap Class to BootStrap all the components.
    private static final Logger LOGGER = LoggerFactory.getLogger(AppInitiator.class);

    public static void main(String[] args) {
        SpringApplication.run(AppInitiator.class, args);
        LOGGER.info(" Application Boot Initiated......");
    }
}
