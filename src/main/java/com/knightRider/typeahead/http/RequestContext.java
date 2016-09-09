package com.knightRider.typeahead.http;

import com.knightRider.typeahead.rest.OutputMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
 * _____________________________________________________________________________________________
 *
 * 		Copyright (c)  2015. Prashant Kumar - All Rights Reserved.
 * 			-	Unauthorized copying of this file, via any medium is strictly prohibited.
 * 			-	This file is Proprietary and Confidential.
 *
 * _____________________________________________________________________________________________
 */

public class RequestContext {

    private static Logger LOGGER = LoggerFactory.getLogger(RequestContext.class);

    private final long contextId;
    private final OutputMode mode;
    private final BaseRequest request;
    private final BaseChannel channel;
    private final ActionRequest requestBody;
    private final boolean prettify;

    public RequestContext(long contextId,
                          OutputMode mode,
                          BaseRequest request,
                          BaseChannel channel,
                          boolean prettify) {


        this(contextId, mode, request, channel, prettify, null);
    }


    public RequestContext(long contextId,
                          OutputMode mode,
                          BaseRequest request,
                          BaseChannel channel,
                          boolean prettify,
                          ActionRequest requestBody

    ) {
        this.contextId = contextId;
        this.mode = mode;
        this.request = request;
        this.channel = channel;
        this.prettify = prettify;
        this.requestBody = requestBody;

        //TODO: Check setting up thread name hits performance
        try {
        } catch (Exception e) {
            LOGGER.warn(e.getMessage(), e);
        }
    }

    public long contextId() {
        return contextId;
    }

    public OutputMode mode() {
        return mode;
    }

    public BaseRequest request() {
        return request;
    }

    public BaseChannel channel() {
        return channel;
    }


    public boolean prettify() {
        return prettify;
    }


    public ActionRequest actionRequest() {
        return requestBody;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RequestContext)) {
            return false;
        }

        RequestContext that = (RequestContext) o;

        if (contextId != that.contextId) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return (int) (contextId ^ (contextId >>> 32));
    }
    
    public RequestContext withActionRequest(ActionRequest actionRequest) {
        return new RequestContext(contextId, mode, request, channel, prettify, actionRequest);
    }
}
