package com.one97.rest;

import com.one97.AbstractGuiceModule;
import com.one97.rest.actions.TestAction;
import com.one97.settings.SpockSettings;

/*
 * ---------------------------------------------------------------------------------------------
 *
 * 		Copyright (c)  2015. Prashant Kumar - All Rights Reserved.
 * 			-	Unauthorized copying of this file, via any medium is strictly prohibited.
 * 			-	This file is Proprietary and Confidential.
 *
 * ---------------------------------------------------------------------------------------------
 */

public class RestActionsModule extends AbstractGuiceModule {

    public RestActionsModule(SpockSettings settings) {
        super(settings);
    }

    @Override
    public boolean sanitizeSettings(SpockSettings properties) {
        return true;
    }

    @Override
    protected void configure() {
        bind(TestAction.class).asEagerSingleton();
    }
}
