package com.knightRider.typeahead.http;

/*
 * _____________________________________________________________________________________________
 *
 * 		Copyright (c)  2015. Prashant Kumar - All Rights Reserved.
 * 			-	Unauthorized copying of this file, via any medium is strictly prohibited.
 * 			-	This file is Proprietary and Confidential.
 *
 * _____________________________________________________________________________________________
 */

public interface HttpServerAdapter {
    void dispatchRequest(BaseRequest request, BaseChannel channel);
}
