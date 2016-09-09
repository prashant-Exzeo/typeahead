package com.knightRider.typeahead.common.service;

import com.knightRider.typeahead.TypeaheadException;

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

    T start() throws TypeaheadException;

    T stop() throws TypeaheadException;

    void close() throws TypeaheadException;
}
