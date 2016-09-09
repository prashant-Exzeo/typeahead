package com.knightRider.typeahead.common.util;

import com.knightRider.typeahead.http.BaseChannel;
import com.knightRider.typeahead.http.HttpRestResponse;
import com.knightRider.typeahead.http.RequestContext;
import org.jboss.netty.handler.codec.http.HttpResponseStatus;

/*
 * _____________________________________________________________________________________________
 *
 * 		Copyright (c)  2015. Prashant Kumar - All Rights Reserved.
 * 			-	Unauthorized copying of this file, via any medium is strictly prohibited.
 * 			-	This file is Proprietary and Confidential.
 *
 * _____________________________________________________________________________________________
 */

public class ResponseUtils {

    public static void sendJsonValidationError(RequestContext context, String errorMessage) {
        sendJsonFailure(context, HttpResponseStatus.EXPECTATION_FAILED, errorMessage);
    }

    public static void sendJsonInternalError(RequestContext context, String errorMessage) {
        sendJsonFailure(context, HttpResponseStatus.INTERNAL_SERVER_ERROR, errorMessage);
    }

    public static void sendJsonInternalError(RequestContext context) {
        sendJsonInternalError(context, "internal error, logs may comes to rescue");
    }

    public static void sendJsonCreated(RequestContext context, String errorMessage) {
        sendJsonFailure(context, HttpResponseStatus.CREATED, errorMessage);
    }

    public static void sendJsonForbidden(RequestContext context, String errorMessage) {
        sendJsonFailure(context, HttpResponseStatus.FORBIDDEN, errorMessage);
    }

    public static void sendJsonNotFound(RequestContext context, String errorMessage) {
        sendJsonFailure(context, HttpResponseStatus.NOT_FOUND, errorMessage);
    }

    public static void sendJsonNotAcceptable(RequestContext context, String errorMessage) {
        sendJsonFailure(context, HttpResponseStatus.NOT_ACCEPTABLE, errorMessage);
    }

    public static void sendJsonBadRequest(RequestContext context, String errorMessage) {
        sendJsonFailure(context, HttpResponseStatus.BAD_REQUEST, errorMessage);
    }

    public static void sendJsonFailure(RequestContext context, HttpResponseStatus status, String errorMessage) {
        sendJsonFailure(context.channel(), status, errorMessage, context.prettify());
    }

    public static void sendJsonFailure(BaseChannel channel, HttpResponseStatus status, String errorMessage, boolean pretty) {

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

}
