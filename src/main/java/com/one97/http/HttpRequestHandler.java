package com.one97.http;

import org.jboss.netty.channel.*;
import org.jboss.netty.handler.codec.http.HttpRequest;

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
 * Handles every inbound http request.
 *
 * All the received messages and exceptions are forwarded back
 * to the {@link com.one97.http.HttpServerTransport} class.
 *
 */
public class HttpRequestHandler extends SimpleChannelUpstreamHandler {

    private final HttpServerTransport transport;

    public HttpRequestHandler(HttpServerTransport transport) {
        this.transport = transport;
    }

    @Override
    public void exceptionCaught(
            ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
        transport.exceptionCaught(ctx, e);
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {

        HttpRequest request = (HttpRequest) e.getMessage();
        Channel c = e.getChannel();

        BaseRequest restRequest = new HttpRestRequest(request, c);
        BaseChannel restChannel = new HttpRestChannel(transport, c, restRequest);

        transport.dispatchRequest(restRequest, restChannel);

        super.messageReceived(ctx, e);
    }
}