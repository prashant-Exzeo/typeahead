package com.one97.indexer;

import com.one97.ElasticSearchInitializer;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * Created by prashant on 4/9/15.
 */
@Component
public class BulkIndexer {

    @Autowired
    @Resource
    Client elasticClient;

    @Autowired
    @Resource
    ElasticSearchInitializer initializer;

    private static final Logger LOGGER = LoggerFactory.getLogger(BulkIndexer.class);

    public GetResponse testing() throws IOException {
        LOGGER.info(" calling BulkIndexer constructor . : {}", initializer);
        LOGGER.info(" calling BulkIndexer constructor . : {}", elasticClient);
        elasticClient.prepareIndex("twitter", "tweet", "1")
                .setSource(XContentFactory.jsonBuilder()
                                .startObject()
                                .field("user", "kimchy")
                                .field("postDate", "123")
                                .field("message", "trying out Elasticsearch")
                                .endObject()
                )
                .execute()
                .actionGet();

        GetResponse response = elasticClient.prepareGet("twitter", "tweet", "1")
                .execute()
                .actionGet();

        LOGGER.info("Result found from ElasticSearch : {}", response.getIndex());
        return response;
    }


}
