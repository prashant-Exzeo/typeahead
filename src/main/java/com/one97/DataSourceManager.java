package com.one97;

import org.apache.tomcat.dbcp.dbcp.BasicDataSource;
import org.elasticsearch.common.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

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
 * @author prashant
 * @description DataSourceManager class is responsible for providing all the configuration,
 * required for accessing any data from a given datasource.
 */
@Component
@Configuration
@PropertySource("classpath:applications.properties")
public class DataSourceManager {

    @Autowired
    private Environment environment;

    private BasicDataSource dataSource;

    private static final Logger LOGGER = LoggerFactory.getLogger(DataSourceManager.class);

    /**
     * @return
     * @deescription this method provide the dataSource Bean.
     */
    @Bean
    public DataSource getDataSource() {
        LOGGER.info(" Calling DataSourceManager getDataSource() method.");
        this.dataSource = new BasicDataSource();
        this.dataSource.setDriverClassName(Preconditions.checkNotNull(environment.getProperty("jdbc.driverClassName")));
        this.dataSource.setUrl(Preconditions.checkNotNull(environment.getProperty("jdbc.url")));
        this.dataSource.setUsername(Preconditions.checkNotNull(environment.getProperty("jdbc.user")));
        this.dataSource.setPassword(Preconditions.checkNotNull(environment.getProperty("jdbc.pass")));
        return this.dataSource;
    }

    @Bean
    public JdbcTemplate getJdbcTemplate() {
        return new JdbcTemplate(getDataSource());
    }
}
