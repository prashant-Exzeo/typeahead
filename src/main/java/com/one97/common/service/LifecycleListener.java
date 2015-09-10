package com.one97.common.service;

/*
 * ---------------------------------------------------------------------------------------------
 *
 * 		Copyright (c)  2015. Prashant Kumar - All Rights Reserved.
 * 			-	Unauthorized copying of this file, via any medium is strictly prohibited.
 * 			-	This file is Proprietary and Confidential.
 *
 * ---------------------------------------------------------------------------------------------
 */

/**
 *
 */
public interface LifecycleListener {

    void beforeStart();

    void afterStart();

    void beforeStop();

    void afterStop();

    void beforeClose();

    void afterClose();
}
