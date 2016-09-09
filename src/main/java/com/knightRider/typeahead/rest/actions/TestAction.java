package com.knightRider.typeahead.rest.actions;

import com.knightRider.typeahead.http.BaseRequest;
import com.knightRider.typeahead.http.HttpRestController;
import com.knightRider.typeahead.rest.BaseRestAction;
import com.knightRider.typeahead.http.HttpRestResponse;
import com.knightRider.typeahead.http.RequestContext;

import javax.inject.Inject;

import static com.knightRider.typeahead.http.HttpRestResponse.OkJsonResponse;

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

        registerContextHandler(BaseRequest.Method.GET,"/",this);
        registerContextHandler(BaseRequest.Method.POST, "field/{tt}", this);
        registerContextHandler(BaseRequest.Method.GET, "field/tt/{tt}", this);
        registerContextHandler(BaseRequest.Method.GET, "field/test", this);
    }

    @Override
    protected void executeAction(RequestContext context) {

        HttpRestResponse restResponse = OkJsonResponse();

        final String fieldName = context.request().param("tt", null);
        restResponse.writeStartObject().writeFieldName("variable").writeString(fieldName).writeEndObject();

        context.channel().sendResponse(restResponse);
    }
}
