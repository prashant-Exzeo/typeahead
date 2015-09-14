package com.one97.http;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;

/*
 * _____________________________________________________________________________________________
 *
 * 		Copyright (c)  2015. Prashant Kumar - All Rights Reserved.
 * 			-	Unauthorized copying of this file, via any medium is strictly prohibited.
 * 			-	This file is Proprietary and Confidential.
 *
 * _____________________________________________________________________________________________
 */

public class HttpBasicAuthHandler extends SimpleChannelHandler {

    private final HttpServerTransport transport;

    public HttpBasicAuthHandler(HttpServerTransport transport) {
        this.transport = transport;
    }

    @Override
    public void exceptionCaught(
            ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
        transport.exceptionCaught(ctx, e);
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        //TODO: Implement Basic Auth here.
    }
}