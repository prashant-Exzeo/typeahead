package com.one97;

import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.node.Node;
import org.elasticsearch.node.NodeBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

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
@Configuration
public class ElasticSearchInitializer {

    @Autowired
    private Node node;

    @Autowired
    private Client client;


//    TODO: Try to use TransportClient.

    private static final Logger LOGGER = LoggerFactory.getLogger(ElasticSearchInitializer.class);

    public ElasticSearchInitializer() {
        LOGGER.info(" Calling ElasticSearchInitializer constructor ");
    }

    @Bean
    public Node getNode() {
        if (node == null) {
            node = NodeBuilder.nodeBuilder()
                    .settings(ImmutableSettings.settingsBuilder().put("http.enabled", true))
                    .local(true)
                    .node();
        }
        return node;
    }


    @Bean
    public Client getClient() {
        if (node == null) {
            node = getNode();
        }
        client = node.client();
        return client;
    }


    @Bean
    public BulkRequestBuilder bulkRequestBuilder() {
        if (client == null) {
            client = getClient();
        }
        return client.prepareBulk();
    }

    @Override
    protected void finalize() throws Throwable {
        if (node != null) {
            client.close();
            node.close();
        }
        super.finalize();
    }
}
