package com.one97.common.util;

import com.google.common.collect.Lists;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

import java.util.Iterator;
import java.util.List;

/*
 * _____________________________________________________________________________________________
 *
 * 		Copyright (c)  2015. Prashant Kumar - All Rights Reserved.
 * 			-	Unauthorized copying of this file, via any medium is strictly prohibited.
 * 			-	This file is Proprietary and Confidential.
 *
 * _____________________________________________________________________________________________
 */

public class ServiceBuilder implements Iterable<Module> {

	private final List<Module> modules = Lists.newArrayList( );

	public ServiceBuilder(Module... modules) {
		for ( Module m : modules ) {
			add( m );
		}
	}

	public ServiceBuilder add( Module module ) {
		modules.add( module );
		return this;
	}

	public Iterator<Module> iterator( ) {
		return modules.iterator( );
	}

	public Injector createInjector( ) {
		return Guice.createInjector( modules );
	}

	public Injector createChildInjector( Injector injector ) {
		return injector.createChildInjector( modules );
	}
}
