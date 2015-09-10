package com.one97.http;

import org.elasticsearch.rest.support.RestUtils;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.handler.codec.http.HttpMethod;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpVersion;

import java.util.HashMap;
import java.util.Map;

/*
 * ---------------------------------------------------------------------------------------------
 *
 * 		Copyright (c)  2015. Prashant Kumar - All Rights Reserved.
 * 			-	Unauthorized copying of this file, via any medium is strictly prohibited.
 * 			-	This file is Proprietary and Confidential.
 *
 * ---------------------------------------------------------------------------------------------
 */

public class HttpRestRequest extends BaseRequest {

    private final HttpRequest httpRequest;
    private final Map<String, String> params;
    private final String rawPath;
    private final ChannelBuffer buffer;


    public HttpRestRequest(HttpRequest httpRequest, Channel channel) {
        this.httpRequest = httpRequest;
        this.params = new HashMap<>();

        //TODO: Copying could be a costly process and not required as this method is thread safe, Need to check.
        if (httpRequest.getContent().readable()) {
            this.buffer = ChannelBuffers.copiedBuffer(httpRequest.getContent());
        } else {
            this.buffer = ChannelBuffers.EMPTY_BUFFER;
        }

        String uri = httpRequest.getUri();
        int pathEndPos = uri.indexOf('?');
        if (pathEndPos < 0) {
            this.rawPath = uri;
        } else {
            this.rawPath = uri.substring(0, pathEndPos);
            //TODO: RestUtils is part of elasticsearch jar, move it to spocks' own jar
            RestUtils.decodeQueryString(uri, pathEndPos + 1, params);
        }

    }


    @Override
    public HttpVersion getProtocolVersion() {
        return httpRequest.getProtocolVersion();
    }

    @Override
    public HttpRequest request() {
        return httpRequest;
    }

    @Override
    public Method method() {

        HttpMethod httpMethod = httpRequest.getMethod();

        if (httpMethod == HttpMethod.GET)
            return Method.GET;

        if (httpMethod == HttpMethod.POST)
            return Method.POST;

        if (httpMethod == HttpMethod.PUT)
            return Method.PUT;

        if (httpMethod == HttpMethod.DELETE)
            return Method.DELETE;

        if (httpMethod == HttpMethod.HEAD) {
            return Method.HEAD;
        }

        if (httpMethod == HttpMethod.OPTIONS) {
            return Method.OPTIONS;
        }

        return Method.GET;

    }

    /**
     * Complete URI of a request with querystring
     * @return URI
     */
    @Override
    public String uri() {
        return httpRequest.getUri();
    }

    /**
     * Complete URI of a request without querystring
     * @return URI
     */
    @Override
    public String path() {
        return rawPath;
    }

    @Override
    public boolean hasContent() {
        return this.buffer.readable();
    }

    @Override
    public Iterable<Map.Entry<String, String>> headers() {
        return httpRequest.headers().entries();
    }

    @Override
    public boolean hasParam(String key) {
        return params.containsKey(key);
    }

    @Override
    public String header(String name) {
        return httpRequest.headers().get(name);
    }

    @Override
    public String param(String name) {
        return params.get(name);
    }

    @Override
    public String param(String name, String defaultValue) {
        String value = param(name);

        if (value == null)
            return defaultValue;

        return params.get(name);
    }

    @Override
    public Map<String, String> params() {
        return params;
    }


}
