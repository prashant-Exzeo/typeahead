package com.one97;

import com.one97.dataSource.MySqlDataProvider;
import com.one97.indexer.BulkIndexer;
import com.one97.searcher.Searcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

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
@RestController
public class SearchResource {

    @Autowired
    BulkIndexer bulkIndexer;

    @Autowired
    MySqlDataProvider dataProvider;

    @Autowired
    Searcher searcher;


    @RequestMapping(value = "/search", method = RequestMethod.GET)
    String search(@RequestParam(value = "term", required = true) String term) throws IOException {

//        TODO: Create Seprate Profile for indexing & searching.
//        TODO: remove all indexing code from search Method.
        return searcher.searchData(term);
    }

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    String indexData() throws IOException {
        bulkIndexer.indexData(dataProvider);
        return String.valueOf(bulkIndexer.indexData(dataProvider));
    }

}
