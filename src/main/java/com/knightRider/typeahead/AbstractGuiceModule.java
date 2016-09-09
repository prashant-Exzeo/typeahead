package com.knightRider.typeahead;

import com.knightRider.typeahead.settings.TypeaheadSettings;
import com.google.inject.AbstractModule;

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
 * Extends Guice module building process with a
 * settings validation
 *
 * @author Prashant
 */
public abstract class AbstractGuiceModule extends AbstractModule {

    private final TypeaheadSettings properties;

    protected AbstractGuiceModule(TypeaheadSettings settings) {
        this.properties = settings;

        if (!sanitizeSettings(settings)) {
            throw new TypeaheadException(String.format("Module %s settings validation failed", this.getClass().getSimpleName()));
        }
    }

    public abstract boolean sanitizeSettings(TypeaheadSettings properties);

    public TypeaheadSettings getSettings() {
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
