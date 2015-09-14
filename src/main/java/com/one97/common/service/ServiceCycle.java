package com.one97.common.service;

import com.one97.SpockException;

/*
 * _____________________________________________________________________________________________
 *
 * 		Copyright (c)  2015. Prashant Kumar - All Rights Reserved.
 * 			-	Unauthorized copying of this file, via any medium is strictly prohibited.
 * 			-	This file is Proprietary and Confidential.
 *
 * _____________________________________________________________________________________________
 */

/**
 *
 */
public interface ServiceCycle<T> {

    Lifecycle.State lifecycleState();

    void addLifecycleListener(LifecycleListener listener);

    void removeLifecycleListener(LifecycleListener listener);

    T start() throws SpockException;

    T stop() throws SpockException;

    void close() throws SpockException;
}
