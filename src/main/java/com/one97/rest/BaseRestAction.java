package com.one97.rest;

import com.one97.SpockException;
import com.one97.http.*;
import org.jboss.netty.handler.codec.http.HttpResponseStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.google.common.base.Strings.isNullOrEmpty;

/*
 * ---------------------------------------------------------------------------------------------
 *
 * 		Copyright (c)  2015. Prashant Kumar - All Rights Reserved.
 * 			-	Unauthorized copying of this file, via any medium is strictly prohibited.
 * 			-	This file is Proprietary and Confidential.
 *
 * ---------------------------------------------------------------------------------------------
 */

public abstract class BaseRestAction implements HttpRestHandler {

    private static Logger LOGGER = LoggerFactory.getLogger(BaseRestAction.class);

    protected final HttpRestController controller;

    protected BaseRestAction(HttpRestController controller
    ) {

        this.controller = controller;
    }

    @Override
    public void handleRequest(BaseRequest request, BaseChannel channel) {

        /*
            NOTICE:
            Note for anyone who would try to change anything in this method.
            Please only change when you know what you know clearly.

            You will start getting into trouble, when you're "setting" a private
            variable within this method. Any class inheriting this class will
            be initialized only once. So NO THREAD SAFE here.
         */

        long contextId = request.paramAsLong("context", -1);
        long indexId = request.paramAsLong("index", -1);
        boolean pretty = request.hasParam("pretty");

        if (contextId <= -1 || indexId <= -1) {
            sendJsonFailure(channel, HttpResponseStatus.BAD_REQUEST, indexId <= -1 ? "Index is not specified, check the url" : "contextId is expected", pretty);
            return;
        }

        OutputMode mode = detectOutputMode(request.param("output", null));
        RequestContext context = new RequestContext(
                contextId,
                mode,
                request,
                channel,
                pretty);

        try {
            executeAction(context);
        } catch (SpockException se) {
            LOGGER.error("Exception occurred while executing - {}", request.path(), se);
            sendJsonInternalError(context, "internal error, logs may comes to rescue");
        }
    }

    protected void sendJsonValidationError(RequestContext context, String errorMessage) {
        sendJsonFailure(context, HttpResponseStatus.EXPECTATION_FAILED, errorMessage);
    }

    protected void sendJsonInternalError(RequestContext context, String errorMessage) {
        sendJsonFailure(context, HttpResponseStatus.INTERNAL_SERVER_ERROR, errorMessage);
    }

    protected void sendJsonCreated(RequestContext context, String errorMessage) {
        sendJsonFailure(context, HttpResponseStatus.CREATED, errorMessage);
    }

    protected void sendJsonForbidden(RequestContext context, String errorMessage) {
        sendJsonFailure(context, HttpResponseStatus.FORBIDDEN, errorMessage);
    }

    protected void sendJsonNotFound(RequestContext context, String errorMessage) {
        sendJsonFailure(context, HttpResponseStatus.NOT_FOUND, errorMessage);
    }

    protected void sendJsonBadRequest(RequestContext context, String errorMessage) {
        sendJsonFailure(context, HttpResponseStatus.BAD_REQUEST, errorMessage);
    }

    protected void sendJsonFailure(RequestContext context, HttpResponseStatus status, String errorMessage) {
        sendJsonFailure(context.channel(), status, errorMessage, context.prettify());
    }

    protected void sendJsonFailure(BaseChannel channel, HttpResponseStatus status, String errorMessage, boolean pretty) {

        HttpRestResponse failureResponse = new HttpRestResponse(
                status,
                HttpRestResponse.JSON_CONTENT_TYPE
        );

        if (pretty) {
            failureResponse.prettify(true);
        }

        failureResponse
                .writeStartObject()
                .writeFieldName("error")
                .writeString(errorMessage)
                .writeFieldName("status")
                .writeString(String.valueOf(status.getCode()))
                .writeEndObject();
        channel.sendResponse(failureResponse);

    }

    protected void registerContextHandler(BaseRequest.Method method, String handlerName, HttpRestHandler handler) {
        controller.registerHandler(method, makeHandler("{index}/{context}", handlerName), handler);
    }

    private String makeHandler(String root, String name) {

        if (name.startsWith("/")) {
            return "/" + root + name;
        } else {
            return "/{index}/{context}/" + name;
        }
    }

    private OutputMode detectOutputMode(String output) {

        if (isNullOrEmpty(output)) {
            return OutputMode.VERBOSE;
        }

        if (output.equalsIgnoreCase("short") || output.equalsIgnoreCase("s")) {
            return OutputMode.SHORT;
        }

        return OutputMode.VERBOSE;
    }

    protected abstract void executeAction(final RequestContext context);
}
