package com.one97.http;

import com.one97.common.path.PathSupport;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import org.elasticsearch.rest.support.RestUtils;
import org.jboss.netty.handler.codec.http.HttpResponseStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
 * ---------------------------------------------------------------------------------------------
 *
 * 		Copyright (c)  2015. Prashant Kumar - All Rights Reserved.
 * 			-	Unauthorized copying of this file, via any medium is strictly prohibited.
 * 			-	This file is Proprietary and Confidential.
 *
 * ---------------------------------------------------------------------------------------------
 */

public class HttpRestController {

    private static Logger LOGGER = LoggerFactory.getLogger(HttpRestController.class);
    private final PathSupport.Decoder REST_DECODER = new InternalPathDecoder();
    private final PathSupport<HttpRestHandler> getHandlers = new PathSupport<>(REST_DECODER);
    private final PathSupport<HttpRestHandler> postHandlers = new PathSupport<>(REST_DECODER);
    private ImmutableMultimap<BaseRequest.Method, String> allHandlers = ImmutableMultimap.of();

    public synchronized void registerHandler(BaseRequest.Method method,
                                             String path,
                                             HttpRestHandler handler) {
        switch (method) {
            case GET:
                getHandlers.insert(path, handler);
                break;
            case POST:
                postHandlers.insert(path, handler);
                break;
            default:
                throw new IllegalArgumentException("Cannot handle " + method + " for path " + path);
        }

        allHandlers = ImmutableMultimap.<BaseRequest.Method, String>builder()
                .putAll(allHandlers)
                .put(method, path)
                .build();
    }

    private HttpRestHandler getHandler(BaseRequest request) {
        String path = getPath(request);
        BaseRequest.Method method = request.method();

        switch (method) {
            case GET:
                return getHandlers.retrieve(path, request.params());
            case POST:
                return postHandlers.retrieve(path, request.params());
            default:
                throw new IllegalArgumentException("Cannot handle " + method + " for path " + path);
        }
    }

    private String getPath(BaseRequest request) {
        return request.path();
    }

    public void dispatchRequest(BaseRequest request, BaseChannel channel) {

        HttpRestHandler httpRestHandler = getHandler(request);

        if (httpRestHandler != null) {

            if (LOGGER.isTraceEnabled()) {
                LOGGER.trace("{} {} {} {}",
                        request.method(),
                        request.uri(),
                        channel.remoteAddress(),
                        channel.localAddress());
            }
            httpRestHandler.handleRequest(request, channel);
        } else {
            handleUnsupportedCalls(request, channel);
        }
    }

    private void handleUnsupportedCalls(BaseRequest request, BaseChannel channel) {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Request {} cannot be responded due to unsupported verb {}", request.uri(), request.method());
        }

        channel.sendResponse(new HttpRestResponse(HttpResponseStatus.BAD_REQUEST));
    }

    public Multimap<BaseRequest.Method, String> paths() {
        return allHandlers;
    }

    static class InternalPathDecoder implements PathSupport.Decoder {
        @Override
        public String decode(String value) {
            return RestUtils.decodeComponent(value);
        }
    }

}
