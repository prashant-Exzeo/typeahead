package com.one97.kernel;

import com.one97.SpockException;
import com.one97.common.service.AbstractLifecycleService;
import com.one97.common.util.ServiceBuilder;
import com.one97.http.HttpServer;
import com.one97.http.HttpServerModule;
import com.one97.rest.RestActionsModule;
import com.one97.settings.SpockSettings;
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

    public KernelServiceImpl(SpockSettings settings) {
        initialize(settings);
    }

    @Override
    protected void doStart() throws SpockException {

        try {
            if (!lifecycle.started()) {
                parent.getInstance(HttpServer.class).start();
            }
        }catch(SpockException e) {
            lifecycle.moveToStopped();
            throw e;
        }

        lifecycle.moveToStarted();

    }

    @Override
    protected void doStop() throws SpockException {

        if (lifecycle.started()) {
            parent.getInstance(HttpServer.class).stop();
        }

        lifecycle.moveToStopped();

    }

    @Override
    protected void doClose() throws SpockException {

    }

    public void initialize(SpockSettings settings) {

        ServiceBuilder builder = new ServiceBuilder();
        builder.add(new HttpServerModule(settings));
        builder.add(new RestActionsModule(settings));
        parent = builder.createInjector();

    }
}
