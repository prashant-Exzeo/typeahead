package com.knightRider.typeahead.common.conf;

import java.io.IOException;
import java.io.InputStream;

/*
 * _____________________________________________________________________________________________
 *
 * 		Copyright (c)  2015. Prashant Kumar - All Rights Reserved.
 * 			-	Unauthorized copying of this file, via any medium is strictly prohibited.
 * 			-	This file is Proprietary and Confidential.
 *
 * _____________________________________________________________________________________________
 */

/**
 * An interface for objects that can create an {@link InputStream} to represent the application
 * configuration.
 */
public interface ConfigurationSourceProvider {
    /**
     * Returns an {@link InputStream} that contains the source of the configuration for the
     * application. The caller is responsible for closing the result.
     *
     * @return an {@link InputStream}
     * @throws IOException if there is an error reading the data at {@code path}
     */
    InputStream open() throws IOException;
}
