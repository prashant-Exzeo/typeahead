package com.one97;

import com.one97.indexer.BulkIndexer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * Created by prashant on 4/9/15.
 */
@RestController
public class SearchResource {

    @Autowired
    BulkIndexer bulkIndexer;


    @RequestMapping(value = "/search", method = RequestMethod.GET)
    String search() throws IOException {
        bulkIndexer.testing();
        return "Calling Api";
    }

}
