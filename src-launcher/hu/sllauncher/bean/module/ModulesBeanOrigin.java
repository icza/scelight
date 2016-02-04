/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.sllauncher.bean.module;

import hu.sllauncher.service.updater.Updater;

/**
 * {@link ModulesBean} origin.
 * 
 * @author Andras Belicza
 */
public enum ModulesBeanOrigin {
	
	/** Received from the Application Operator. */
	APP_OPERATOR,
	
	/** Created in Eclipse from the projects' <code>modules.xml</code> file. */
	ECLIPSE_MODULES_XML,
	
	/** Created by the {@link Updater} (when registered but no internet connection is available). */
	UPDATER_FAKE,
	
}
