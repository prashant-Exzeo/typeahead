package com.knightRider.typeahead.common.conf;

import java.io.*;

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
 * An implementation of {@link ConfigurationSourceProvider} that reads the configuration from the
 * local file system.
 */
public class FileConfigurationSourceProvider implements ConfigurationSourceProvider {

    private String path;

    public FileConfigurationSourceProvider(String path) {
        this.path = path;
    }

    @Override
    public InputStream open() throws IOException {
        final File file = new File(path);

        if (!file.exists()) {
            throw new FileNotFoundException("File " + file.getAbsolutePath() + " not found");
        }

        return new FileInputStream(file);
    }

}
