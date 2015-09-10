package com.one97.common.util;

import java.net.ConnectException;
import java.nio.channels.ClosedChannelException;

/*
 * ---------------------------------------------------------------------------------------------
 *
 * 		Copyright (c)  2015. Prashant Kumar - All Rights Reserved.
 * 			-	Unauthorized copying of this file, via any medium is strictly prohibited.
 * 			-	This file is Proprietary and Confidential.
 *
 * ---------------------------------------------------------------------------------------------
 */

public class ExceptionHelper {

    public static boolean isConnectException(Throwable e) {
        if (e instanceof ConnectException) {
            return true;
        }
        return false;
    }

    public static boolean isCloseConnectionException(Throwable e) {
        if (e instanceof ClosedChannelException) {
            return true;
        }
        if (e.getMessage() != null) {
            // UGLY!, this exception messages seems to represent closed connection
            if (e.getMessage().contains("Connection reset by peer")) {
                return true;
            }
            if (e.getMessage().contains("connection was aborted")) {
                return true;
            }
            if (e.getMessage().contains("forcibly closed")) {
                return true;
            }
            if (e.getMessage().contains("Broken pipe")) {
                return true;
            }
            if (e.getMessage().contains("Connection timed out")) {
                return true;
            }
        }
        return false;
    }

}
