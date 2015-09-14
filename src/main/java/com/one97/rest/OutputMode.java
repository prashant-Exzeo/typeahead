package com.one97.rest;

/*
 * _____________________________________________________________________________________________
 *
 * 		Copyright (c)  2015. Prashant Kumar - All Rights Reserved.
 * 			-	Unauthorized copying of this file, via any medium is strictly prohibited.
 * 			-	This file is Proprietary and Confidential.
 *
 * _____________________________________________________________________________________________
 */

public enum OutputMode {
    /**
     * A short response prepared, not a default option for action implementations
     */
    SHORT,
    /**
     * A detailed response, by default all actions should implement response
     * returns that are detailed and too verbose for requester.
     */
    VERBOSE
}
