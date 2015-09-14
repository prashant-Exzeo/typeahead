package com.one97.rest.actions;

import com.one97.http.HttpRestController;
import com.one97.http.HttpRestResponse;
import com.one97.http.RequestContext;
import com.one97.rest.BaseRestAction;

import javax.inject.Inject;

import static com.one97.http.BaseRequest.Method.GET;
import static com.one97.http.BaseRequest.Method.POST;
import static com.one97.http.HttpRestResponse.OkJsonResponse;

/*
 * _____________________________________________________________________________________________
 *
 * 		Copyright (c)  2015. Prashant Kumar - All Rights Reserved.
 * 			-	Unauthorized copying of this file, via any medium is strictly prohibited.
 * 			-	This file is Proprietary and Confidential.
 *
 * _____________________________________________________________________________________________
 */

public class TestAction extends BaseRestAction {

    //private static Logger LOGGER = LoggerFactory.getLogger(FieldListAction.class);


    @Inject
    protected TestAction(HttpRestController controller
    ) {
        super(controller);

        registerContextHandler(POST, "field/{tt}", this);
        registerContextHandler(GET, "field/tt/{tt}", this);
        registerContextHandler(GET, "field/test", this);
    }

    @Override
    protected void executeAction(RequestContext context) {

        HttpRestResponse restResponse = OkJsonResponse();

        final String fieldName = context.request().param("tt", null);
        restResponse.writeStartObject().writeFieldName("variable").writeString(fieldName).writeEndObject();

        context.channel().sendResponse(restResponse);
    }
}
