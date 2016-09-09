package com.knightRider.typeahead.http;

import com.knightRider.typeahead.TypeaheadException;
import com.knightRider.typeahead.common.service.AbstractLifecycleService;

import javax.inject.Inject;

/*
 * _____________________________________________________________________________________________
 *
 * 		Copyright (c)  2015. Prashant Kumar - All Rights Reserved.
 * 			-	Unauthorized copying of this file, via any medium is strictly prohibited.
 * 			-	This file is Proprietary and Confidential.
 *
 * _____________________________________________________________________________________________
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
    protected void doStart() throws TypeaheadException {
        this.transport.start();
    }

    @Override
    protected void doStop() throws TypeaheadException {
        this.transport.stop();
    }

    @Override
    protected void doClose() throws TypeaheadException {

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
