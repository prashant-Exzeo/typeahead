package com.one97.common.conf;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

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
 * An implementation of {@link ConfigurationSourceProvider} that reads the configuration from a
 * {@link URL}.
 */
public class UrlConfigurationSourceProvider implements ConfigurationSourceProvider {

    private final String path;

    public UrlConfigurationSourceProvider(String path) {
        this.path = path;
    }

    @Override
    public InputStream open() throws IOException {
        return new URL(path).openStream();
    }
}
