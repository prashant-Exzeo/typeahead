package com.one97.dataSource;

import java.util.List;

/**
 * Created by prashant on 4/9/15.
 */
public interface DataProvider {

    <T> List<T> bulkDataFetcher(int numberOfRecordsToSkip, int fetchSize, Class<T> clazz);

}
