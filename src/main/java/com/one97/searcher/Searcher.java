package com.one97.searcher;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.one97.ElasticSearchInitializer;
import com.one97.dataSource.MySqlDataProvider;
import com.one97.helper.ProductMapper;
import com.one97.indexer.BulkIndexer;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RegexpQueryBuilder;
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
public class Searcher {

    @Autowired
    @Resource
    Client elasticClient;

    @Autowired
    @Resource
    ElasticSearchInitializer initializer;


    @Autowired
    @Resource
    BulkRequestBuilder bulkRequestBuilder;
    private static final Logger LOGGER = LoggerFactory.getLogger(Searcher.class);

    public String searchData(String term) {
        LOGGER.debug("Check all instances are available ElasticSearchInitializer : {}, ElasticSearchClient : {}, bulkRequestBuilder : {}"
                , initializer, elasticClient, bulkRequestBuilder);

        SearchResponse response = elasticClient
                .prepareSearch()
                .setQuery(
                      QueryBuilders.wildcardQuery("pd_name", "*"+term+"*")
                ).setFrom(0)
                .setSize(20)
                .execute()
                .actionGet();


        LOGGER.info(" SearchResponse : {}", response);
        return response.toString();
    }

}
