package com.knightRider.typeahead.http;

import org.jboss.netty.handler.codec.http.HttpResponse;

import java.net.SocketAddress;

/*
 * _____________________________________________________________________________________________
 *
 * 		Copyright (c)  2015. Prashant Kumar - All Rights Reserved.
 * 			-	Unauthorized copying of this file, via any medium is strictly prohibited.
 * 			-	This file is Proprietary and Confidential.
 *
 * _____________________________________________________________________________________________
 */

public abstract class BaseChannel {
    private final BaseRequest request;

    protected BaseChannel(BaseRequest request) {
        this.request = request;
    }

    public abstract void sendResponse(HttpResponse httpResponse);

    public abstract void sendResponse(BaseHttpResponse response);

    public abstract SocketAddress remoteAddress();

    public abstract SocketAddress localAddress();

}
