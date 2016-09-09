package com.knightRider.typeahead.rest;

import com.knightRider.typeahead.AbstractGuiceModule;
import com.knightRider.typeahead.rest.actions.TestAction;
import com.knightRider.typeahead.settings.TypeaheadSettings;

/*
 * _____________________________________________________________________________________________
 *
 * 		Copyright (c)  2015. Prashant Kumar - All Rights Reserved.
 * 			-	Unauthorized copying of this file, via any medium is strictly prohibited.
 * 			-	This file is Proprietary and Confidential.
 *
 * _____________________________________________________________________________________________
 */

public class RestActionsModule extends AbstractGuiceModule {

    public RestActionsModule(TypeaheadSettings settings) {
        super(settings);
    }

    @Override
    public boolean sanitizeSettings(TypeaheadSettings properties) {
        return true;
    }

    @Override
    protected void configure() {
        bind(TestAction.class).asEagerSingleton();
    }
}
