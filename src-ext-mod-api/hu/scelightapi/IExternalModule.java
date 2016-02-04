/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelightapi;

import hu.scelightapi.service.IFactory;
import hu.scelightapibase.bean.IExtModManifestBean;
import hu.scelightapibase.service.job.IJob;
import hu.scelightapibase.service.job.IProgressJob;
import hu.scelightapibase.util.IControlledThread;
import hu.scelightapibase.util.INormalThread;

/**
 * Entry point of external modules.
 * 
 * <p>
 * Main classes of external modules must implement this interface.<br>
 * Main classes are also required to have a no-arg constructor.
 * </p>
 * 
 * <p>
 * Recommended to extend the {@link BaseExtModule} class instead of implementing this yourself as the {@link BaseExtModule} class provides you some utilities.
 * </p>
 * 
 * <p>
 * See <a href="https://sites.google.com/site/scelight/external-modules">https://sites.google.com/site/scelight/external-modules</a> for general info about
 * external modules.
 * </p>
 * 
 * @version External Module API version:  See {@link XConsts#EXT_MOD_API_VERSION}
 * 
 * @author Andras Belicza
 * 
 * @see BaseExtModule
 * @see IExtModManifestBean
 * @see IServices
 */
public interface IExternalModule {
	
	/**
	 * Called when the external module is started.
	 * 
	 * <p>
	 * If this method throws an {@link Exception}, the {@link #destroy()} method will be called and the module will be discarded.
	 * </p>
	 * <p>
	 * If initialization fails (the module cannot initialize itself), an {@link Exception} must be thrown to indicate the failure.
	 * </p>
	 * 
	 * <p>
	 * <b>This method must not perform lengthy operations!</b> If initialization task takes long, they must to be performed in separate threads / jobs. For
	 * example if a module accesses network resources (e.g. connects to an external web server), since the connection time and the availability / reliability of
	 * an independent server cannot be assured, it must be performed in a new thread.<br>
	 * The <i>"Scelight way"</i> to create and use threads is via using {@link INormalThread}, {@link IControlledThread}, {@link IJob} and {@link IProgressJob}.
	 * Instances of these advanced threads can be acquired by the {@link IFactory} interface.
	 * </p>
	 * 
	 * @param manifest external module manifest loaded from the <code>"Scelight-mod-x-manifest.xml"</code> file from the version folder of the external module
	 *            root folder; should be stored by the module if the module intends to use it later
	 * @param services module-independent services provided for the module; should be stored by the module if the module intends to use it later
	 * @param modEnv external module environment, can be used to acquire external module specific info
	 * @throws Exception must be thrown if initialization fails
	 */
	void init( IExtModManifestBean manifest, IServices services, IModEnv modEnv ) throws Exception;
	
	/**
	 * Called when the external module is destroyed.
	 * 
	 * <p>
	 * The module should release all allocated resources and stop all background jobs and threads created by it.<br>
	 * The module cannot perform any tasks after returning from this method because the application may be terminated at any time.
	 * </p>
	 * 
	 * <p>
	 * This method will be called if the module is instantiated successfully, even if the {@link #init(IExtModManifestBean, IServices, IModEnv)} method throws
	 * an exception.
	 * </p>
	 * 
	 * <p>
	 * Any thrown {@link Exception} will be silently discarded.
	 * </p>
	 */
	void destroy();
	
}
