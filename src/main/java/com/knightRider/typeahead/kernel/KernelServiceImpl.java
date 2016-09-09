package com.knightRider.typeahead.kernel;

import com.knightRider.typeahead.TypeaheadException;
import com.knightRider.typeahead.common.service.AbstractLifecycleService;
import com.knightRider.typeahead.common.util.ServiceBuilder;
import com.knightRider.typeahead.http.HttpServer;
import com.knightRider.typeahead.http.HttpServerModule;
import com.knightRider.typeahead.rest.RestActionsModule;
import com.knightRider.typeahead.settings.TypeaheadSettings;
import com.google.inject.Injector;

/*
 * _____________________________________________________________________________________________
 *
 * 		Copyright (c)  2015. Prashant Kumar - All Rights Reserved.
 * 			-	Unauthorized copying of this file, via any medium is strictly prohibited.
 * 			-	This file is Proprietary and Confidential.
 *
 * _____________________________________________________________________________________________
 */

public class KernelServiceImpl extends AbstractLifecycleService<KernelService> implements KernelService {

    private Injector parent = null;

    public KernelServiceImpl(TypeaheadSettings settings) {
        initialize(settings);
    }

    @Override
    protected void doStart() throws TypeaheadException {

        try {
            if (!lifecycle.started()) {
                parent.getInstance(HttpServer.class).start();
            }
        }catch(TypeaheadException e) {
            lifecycle.moveToStopped();
            throw e;
        }

        lifecycle.moveToStarted();

    }

    @Override
    protected void doStop() throws TypeaheadException {

        if (lifecycle.started()) {
            parent.getInstance(HttpServer.class).stop();
        }

        lifecycle.moveToStopped();

    }

    @Override
    protected void doClose() throws TypeaheadException {

    }

    public void initialize(TypeaheadSettings settings) {

        ServiceBuilder builder = new ServiceBuilder();
        builder.add(new HttpServerModule(settings));
        builder.add(new RestActionsModule(settings));
        parent = builder.createInjector();

    }
}
