package com.one97;

import com.one97.settings.SpockSettings;
import com.google.inject.AbstractModule;

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
 * Extends Guice module building process with a
 * settings validation
 *
 * @author Prashant
 */
public abstract class AbstractGuiceModule extends AbstractModule {

    private final SpockSettings properties;

    protected AbstractGuiceModule(SpockSettings settings) {
        this.properties = settings;

        if (!sanitizeSettings(settings)) {
            throw new SpockException(String.format("Module %s settings validation failed", this.getClass().getSimpleName()));
        }
    }

    public abstract boolean sanitizeSettings(SpockSettings properties);

    public SpockSettings getSettings() {
        return properties;
    }


    /**
     * HttpModule
     * RestModule
     * IndiceModule -> ?
     * SearchModule
     * PrefetchModule
     * ParserModule
     * DefinitionsModule
     * SearchEngineModule -> EsModule
     */
}
