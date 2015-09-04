package com.one97.searcher;

import com.one97.indexer.BulkIndexer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Created by prashant on 4/9/15.
 */
@Component
public class Searcher {

    @Autowired
    @Resource
    private BulkIndexer bulkIndexer;

    private static final Logger LOGGER = LoggerFactory.getLogger(Searcher.class);

}
