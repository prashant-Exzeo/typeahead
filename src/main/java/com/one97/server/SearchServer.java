package com.one97.server;

/*
 * ---------------------------------------------------------------------------------------------
 *
 * 		Copyright (c)  2015. Prashant Kumar - All Rights Reserved.
 * 			-	Unauthorized copying of this file, via any medium is strictly prohibited.
 * 			-	This file is Proprietary and Confidential.
 *
 * ---------------------------------------------------------------------------------------------
 */

public class SearchServer {
    public static void main(String... args) {
        SearchBootstrap boot = new SearchBootstrap(args);
        boot.start();
    }

}
