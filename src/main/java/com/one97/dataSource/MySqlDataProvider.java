package com.one97.dataSource;

import com.one97.helper.ProductMapper;
import org.elasticsearch.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

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
 * Created by prashant on 7/9/15.
 */
@Component
public class MySqlDataProvider {

    @Autowired
    JdbcTemplate jdbcTemplate;

    private static final String SELECT_ALL_PRODUCTS = "Select * from tbl_product";
    public static final Logger LOGGER = LoggerFactory.getLogger(MySqlDataProvider.class);

    public List<ProductMapper> getAllData() {
        List<ProductMapper> resultFromDb = jdbcTemplate.query(SELECT_ALL_PRODUCTS, new BeanPropertyRowMapper(ProductMapper.class));
        if (!resultFromDb.isEmpty()) {
            LOGGER.debug("Records found from Mysql : {}", resultFromDb);
            return resultFromDb;
        }
        LOGGER.info("Unable to find any record from Mysql Database. :(");
        return Lists.newArrayList();
    }

}
