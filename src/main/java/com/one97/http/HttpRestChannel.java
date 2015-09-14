package com.one97.http;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.handler.codec.http.*;

import java.net.SocketAddress;
import java.util.List;
import java.util.Map;

/*
 * _____________________________________________________________________________________________
 *
 * 		Copyright (c)  2015. Prashant Kumar - All Rights Reserved.
 * 			-	Unauthorized copying of this file, via any medium is strictly prohibited.
 * 			-	This file is Proprietary and Confidential.
 *
 * _____________________________________________________________________________________________
 */

public class HttpRestChannel extends BaseChannel {

    private final HttpServerTransport transport;
    private final Channel channel;
    private final HttpRequest request;

    public HttpRestChannel(HttpServerTransport transport,
                           Channel channel,
                           BaseRequest request) {
        super(request);

        this.transport = transport;
        this.channel = channel;
        this.request = request.request();
    }

    @Override
    public void sendResponse(HttpResponse httpResponse) {
        ChannelFuture future = channel.write(httpResponse);
        future.addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void sendResponse(BaseHttpResponse response) {

        // Whether the incoming request follows HTTP 1.0 or has Connection:Close header
        // we will immediately close the underlying tcp connection
        boolean http10 = request.getProtocolVersion().equals(HttpVersion.HTTP_1_0);
        boolean close =
                HttpHeaders.Values.CLOSE.equalsIgnoreCase(request.headers().get(HttpHeaders.Names.CONNECTION)) ||
                        (http10 && !HttpHeaders.Values.KEEP_ALIVE.equalsIgnoreCase(request.headers().get(HttpHeaders.Names.CONNECTION)));

        HttpResponseStatus status = response.status();
        HttpResponse httpResponse = new DefaultHttpResponse(http10 ? HttpVersion.HTTP_1_0 : HttpVersion.HTTP_1_1, status);

        if (!close) {
            httpResponse.headers().add(HttpHeaders.Names.CONNECTION, "Keep-Alive");
        }

        Map<String, List<String>> customHeaders = response.getHeaders();

        if (customHeaders != null) {
            for (Map.Entry<String, List<String>> headerEntry : customHeaders.entrySet()) {
                for (String headerValue : headerEntry.getValue()) {
                    httpResponse.headers().add(headerEntry.getKey(), headerValue);
                }
            }
        }

        httpResponse.setContent(response.content());
        httpResponse.headers().add(HttpHeaders.Names.CONTENT_TYPE, response.contentType());
        httpResponse.headers().add(HttpHeaders.Names.CONTENT_LENGTH, String.valueOf(response.content().readableBytes()));

        ChannelFuture future = channel.write(httpResponse);
        future.addListener(ChannelFutureListener.CLOSE);

    }

    @Override
    public SocketAddress remoteAddress() {
        return channel.getRemoteAddress();
    }

    @Override
    public SocketAddress localAddress() {
        return channel.getLocalAddress();
    }
}
