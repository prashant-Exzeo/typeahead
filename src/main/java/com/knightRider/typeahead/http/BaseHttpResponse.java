package com.knightRider.typeahead.http;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.handler.codec.http.HttpResponseStatus;

import java.util.ArrayList;
import java.util.HashMap;
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

public abstract class BaseHttpResponse implements AutoCloseable {

    protected Map<String, List<String>> customHeaders;
    protected ChannelBuffer buffer;

    protected BaseHttpResponse() {
        buffer = ChannelBuffers.dynamicBuffer();
    }

    public void addHeader(String name, String value) {
        if (customHeaders == null) {
            customHeaders = new HashMap<>(2);
        }
        List<String> header = customHeaders.get(name);
        if (header == null) {
            header = new ArrayList<>();
            customHeaders.put(name, header);
        }
        header.add(value);
    }

    public Map<String, List<String>> getHeaders() {
        return customHeaders;
    }

    public abstract HttpResponseStatus status();

    public abstract String contentType();

    public abstract void prettify(boolean flag);

    @Override
    public void close() throws Exception {

        if (buffer != null) {
            buffer.clear();
            buffer = null;
        }
    }

    public ChannelBuffer content() {
        return buffer;
    }


}
