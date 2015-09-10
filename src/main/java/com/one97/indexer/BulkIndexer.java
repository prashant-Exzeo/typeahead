package com.one97.indexer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.one97.ElasticSearchInitializer;
import com.one97.dataSource.MySqlDataProvider;
import com.one97.helper.ProductMapper;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.client.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
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
 * Created by prashant on 4/9/15.
 */
@Component
public class BulkIndexer {

    @Autowired
    @Resource
    Client elasticClient;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    @Resource
    MySqlDataProvider dataProvider;

    @Autowired
    @Resource
    ElasticSearchInitializer initializer;


    @Autowired
    @Resource
    BulkRequestBuilder bulkRequestBuilder;

    private static final Logger LOGGER = LoggerFactory.getLogger(BulkIndexer.class);

    public String indexData(MySqlDataProvider mySqlDataProvider) {
        LOGGER.debug("Check all instances are available ElasticSearchInitializer : {}, ElasticSearchClient : {}, DataProvider : {}, bulkRequestBuilder : {}"
                , initializer, elasticClient, dataProvider, bulkRequestBuilder);

        BulkResponse response;
        List<ProductMapper> productsList = dataProvider.getAllData();
        if (!productsList.isEmpty()) {
            for (ProductMapper product : productsList) {
                try {
                    bulkRequestBuilder.add(
                            elasticClient
                                    .prepareIndex("products", "product")
                                    .setSource(
                                            objectMapper.writeValueAsBytes(product)
                                    )
                    );
                } catch (JsonProcessingException errorCause) {
                    LOGGER.error("Errror while transforming ProductMapper as Byte. ", errorCause);
                }
            }
            response = bulkRequestBuilder.execute().actionGet();
            if (response.hasFailures()) {
                LOGGER.error(" Error while processing some requests : {}", response.hasFailures());
                return response.toString();
            } else {
                LOGGER.error(" BulkRequest :) : {}", response.hasFailures());
                return response.toString();
            }
        }
        return "No data indexed.";
    }
}
