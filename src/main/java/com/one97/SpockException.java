package com.one97;

/*
 * _____________________________________________________________________________________________
 *
 * 		Copyright (c)  2015. Prashant Kumar - All Rights Reserved.
 * 			-	Unauthorized copying of this file, via any medium is strictly prohibited.
 * 			-	This file is Proprietary and Confidential.
 *
 * _____________________________________________________________________________________________
 */

public class SpockException extends RuntimeException {

    public SpockException(String s) {
        super(s);
    }

    public SpockException (String s, Throwable t) {
        super(s, t);
    }
}


