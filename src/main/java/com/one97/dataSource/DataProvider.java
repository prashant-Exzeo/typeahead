package com.one97.dataSource;

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
public interface DataProvider {

    <T> List<T> bulkDataFetcher(int numberOfRecordsToSkip, int fetchSize, Class<T> clazz);

}
