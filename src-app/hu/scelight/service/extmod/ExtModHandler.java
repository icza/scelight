/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight.service.extmod;

import java.net.URLClassLoader;

import hu.scelightapi.IExternalModule;
import hu.sllauncher.bean.module.ExtModManifestBean;

/**
 * Handler of a loaded and started external module instance.
 * 
 * @author Andras Belicza
 */
class ExtModHandler {
	
	/** Manifest of the external module. */
	final ExtModManifestBean                 manifest;
	
	/** External module environment. */
	final ModEnv                             modEnv;
	
	/** Class loader used to load the module's main class; also the one that is used by the module. */
	final URLClassLoader                     classLoader;
	
	/** Main class of the external module. */
	final Class< ? extends IExternalModule > extModClass;
	
	/** Instance of the module's main class. */
	final IExternalModule                    extModInstance;
	
	/**
	 * Creates a new {@link ExtModHandler}.
	 * 
	 * @param manifest manifest of the external module
	 * @param modEnv external module environment
	 * @param classLoader class loader used to load the module's main class; also the one that is used by the module
	 * @param extModClass main class of the external module
	 * @param extModInstance instance of the module's main class
	 */
	public ExtModHandler( final ExtModManifestBean manifest, final ModEnv modEnv, final URLClassLoader classLoader,
	        final Class< ? extends IExternalModule > extModClass, final IExternalModule extModInstance ) {
		this.manifest = manifest;
		this.modEnv = modEnv;
		this.classLoader = classLoader;
		this.extModClass = extModClass;
		this.extModInstance = extModInstance;
	}
	
}
