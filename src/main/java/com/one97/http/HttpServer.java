package com.one97.http;

import com.one97.SpockException;
import com.one97.common.service.AbstractLifecycleService;

import javax.inject.Inject;

/*
 * ---------------------------------------------------------------------------------------------
 *
 * 		Copyright (c)  2015. Prashant Kumar - All Rights Reserved.
 * 			-	Unauthorized copying of this file, via any medium is strictly prohibited.
 * 			-	This file is Proprietary and Confidential.
 *
 * ---------------------------------------------------------------------------------------------
 */

public class HttpServer extends AbstractLifecycleService<HttpServer> {

    private final HttpServerTransport transport;
    private final HttpRestController restController;

    @Inject
    public HttpServer(HttpServerTransport transport,
                      HttpRestController restController) {

        this.transport = transport;
        this.restController = restController;

        this.transport.httpAdapter(new Dispatcher(this));
    }

    @Override
    protected void doStart() throws SpockException {
        this.transport.start();
    }

    @Override
    protected void doStop() throws SpockException {
        this.transport.stop();
    }

    @Override
    protected void doClose() throws SpockException {

    }

    static class Dispatcher implements HttpServerAdapter {

        private final HttpServer server;

        Dispatcher(HttpServer server) {
            this.server = server;
        }

        @Override
        public void dispatchRequest(BaseRequest request, BaseChannel channel) {
            this.server.internalDispatch(request, channel);
        }
    }

    private void internalDispatch(BaseRequest request, BaseChannel channel) {
        restController.dispatchRequest(request, channel);
    }
}
