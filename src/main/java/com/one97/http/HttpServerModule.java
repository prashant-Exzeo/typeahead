package com.one97.http;

import com.one97.AbstractGuiceModule;
import com.one97.settings.SpockSettings;

/*
 * ---------------------------------------------------------------------------------------------
 *
 * 		Copyright (c)  2015. Prashant Kumar - All Rights Reserved.
 * 			-	Unauthorized copying of this file, via any medium is strictly prohibited.
 * 			-	This file is Proprietary and Confidential.
 *
 * ---------------------------------------------------------------------------------------------
 */

public class HttpServerModule extends AbstractGuiceModule {

    public HttpServerModule(SpockSettings settings) {
        super(settings);
    }

    @Override
    public boolean sanitizeSettings(SpockSettings properties) {
        return true;
    }

    @Override
    protected void configure() {
        bind(HttpServer.class).asEagerSingleton();
        bind(HttpServerTransport.class).asEagerSingleton();
        bind(HttpRestController.class).asEagerSingleton();
    }
}
