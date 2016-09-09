package com.knightRider.typeahead.http;

import com.knightRider.typeahead.AbstractGuiceModule;
import com.knightRider.typeahead.settings.TypeaheadSettings;

/*
 * _____________________________________________________________________________________________
 *
 * 		Copyright (c)  2015. Prashant Kumar - All Rights Reserved.
 * 			-	Unauthorized copying of this file, via any medium is strictly prohibited.
 * 			-	This file is Proprietary and Confidential.
 *
 * _____________________________________________________________________________________________
 */

public class HttpServerModule extends AbstractGuiceModule {

    public HttpServerModule(TypeaheadSettings settings) {
        super(settings);
    }

    @Override
    public boolean sanitizeSettings(TypeaheadSettings properties) {
        return true;
    }

    @Override
    protected void configure() {
        bind(HttpServer.class).asEagerSingleton();
        bind(HttpServerTransport.class).asEagerSingleton();
        bind(HttpRestController.class).asEagerSingleton();
    }
}
