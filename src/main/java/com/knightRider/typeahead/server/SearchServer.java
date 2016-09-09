package com.knightRider.typeahead.server;

/*
 * _____________________________________________________________________________________________
 *
 * 		Copyright (c)  2015. Prashant Kumar - All Rights Reserved.
 * 			-	Unauthorized copying of this file, via any medium is strictly prohibited.
 * 			-	This file is Proprietary and Confidential.
 *
 * _____________________________________________________________________________________________
 */

public class SearchServer {
    public static void main(String... args) {
        SearchBootstrap boot = new SearchBootstrap(args);
        boot.start();
    }

}
