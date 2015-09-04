package com.one97;

import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.node.Node;
import org.elasticsearch.node.NodeBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * Created by prashant on 4/9/15.
 */
@Component
public class ElasticSearchInitializer {

    @Autowired
    private Node node;

    @Autowired
    private Client client;

    private static final Logger LOGGER = LoggerFactory.getLogger(ElasticSearchInitializer.class);

    public ElasticSearchInitializer() {
        LOGGER.info(" Calling ElasticSearchInitializer constructor ");
    }

    @Bean
    public Node getNode() {
        if (node == null) {
            node = NodeBuilder.nodeBuilder()
                    .settings(ImmutableSettings.settingsBuilder().put("http.enabled", false))
                    .local(true)
                    .node();
        }
        LOGGER.info("calling getNode ");
        return node;
    }


    @Bean
    public Client getClient() {
        if (node == null) {
            node = getNode();
        }
        client = node.client();
        LOGGER.debug("calling getClient ");
        return client;
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
