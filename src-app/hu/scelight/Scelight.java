/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight;

import hu.scelight.action.Actions;
import hu.scelight.gui.icon.Icons;
import hu.scelight.sc2.balancedata.BalanceData;
import hu.scelight.sc2.rep.s2prot.Protocol;
import hu.scelight.service.env.Env;
import hu.scelight.service.extmod.ModEnv;
import hu.scelight.service.settings.GlobalSettingChangeListener;
import hu.scelight.util.Utils;
import hu.scelight.util.gui.GuiUtils;
import hu.scelightapibase.bean.settings.ISettingChangeListener;
import hu.scelightapibase.bean.settings.ISettingsBean;
import hu.sllauncher.service.job.Job;
import hu.sllauncher.util.NormalThread;

/**
 * This is the main class of Scelight.
 * 
 * <p>
 * This is a singleton implementation.
 * </p>
 * 
 * @author Andras Belicza
 */
public class Scelight {
	
	/** Reference to the one and only instance. */
	private static Scelight INSTANCE;
	
	/**
	 * Returns the one and only reference.
	 * 
	 * @return the one and only reference
	 */
	public static Scelight INSTANCE() {
		return INSTANCE;
	}
	
	
	/** Global (application wide) app setting change listener. */
	private final ISettingChangeListener globalSettingChangeListener = new GlobalSettingChangeListener();
	
	/**
	 * Creates a new {@link Scelight}.
	 */
	public Scelight() {
		// Note: we're in the EDT.
		
		if ( INSTANCE != null )
			throw new RuntimeException( "Scelight has already been instantiated!" );
		
		INSTANCE = this;
		
		// Ensure app environment is initialized:
		try {
			Class.forName( Env.class.getName() );
		} catch ( final ClassNotFoundException cne ) {
			// This should never happen, but if so, we will fail later anyway.
		}
		
		// Init app global settings
		Env.APP_SETTINGS.addAndExecuteChangeListener( GlobalSettingChangeListener.SETTING_SET, globalSettingChangeListener );
		
		// Ensure Env class is loaded and initialized along with the main frame
		Env.MAIN_FRAME.getTitle();
		
		// Do some other pre-loading / caching, but not in the EDT!
		new Job( "Preloader", Icons.F_HOURGLASS ) {
			@Override
			public void jobRun() {
				Protocol.DEFAULT.getClass(); // Load default protocol
				
				// Load latest balance data
				try {
					Class.forName( BalanceData.class.getName() );
				} catch ( final ClassNotFoundException cne ) {
					// This should never happen, but if so, we will fail later anyway.
				}
			}
		}.start();
		
		Env.LOGGER.debug( "Application started." );
	}
	
	/**
	 * Tells if exit is allowed.
	 * 
	 * @return true if exit is allowed; false otherwise
	 */
	private static boolean allowedToExit() {
		// Check background jobs
		final int jobsCount = Env.JOBS.getJobsCount();
		
		if ( jobsCount > 0 )
			if ( !GuiUtils.confirm(
			        ( jobsCount == 1 ? "There is" : "There are" ) + Utils.plural( " %s running job%s! Are you sure you want to exit?", jobsCount ), " ",
			        GuiUtils.linkForAction( "View Running Jobs...", Actions.RUNNING_JOBS ) ) )
				return false;
		
		return true;
	}
	
	/**
	 * Called before exit to properly end/finish pending jobs and do things that must be done before shut down (e.g. save settings).
	 */
	private void onExit() {
		Env.JOBS.cancelJobs();
		
		saveSettings();
		
		Env.EXT_MOD_MANAGER.destroyStartedExtMods();
		
		Env.JOBS.closeJobs();
	}
	
	/**
	 * Saves the settings.
	 */
	public void saveSettings() {
		// No need to save boot settings (they can only be modified in the Settings dialog which saves them when applied).
		Env.LAUNCHER_SETTINGS.save();
		Env.APP_SETTINGS.save();
		
		// Save external module settings
		for ( final ModEnv modEnv : Env.EXT_MOD_MANAGER.getStartedExtModEnvList() )
			for ( final ISettingsBean settings : modEnv.getSettingsBeanList() )
				settings.save();
	}
	
	/**
	 * Exits from the app.
	 */
	public void exit() {
		if ( !allowedToExit() )
			return;
		
		// Shutdown in a new thread because we're most likely called from the EDT,
		// cancelling jobs will want to remove themselves which refreshes jobs count which needs the EDT
		// => would be deadlock!
		new NormalThread( Consts.APP_NAME + " shutdown..." ) {
			@Override
			public void run() {
				onExit();
				
				// TODO http://stackoverflow.com/questions/7769885/interruptedexception-after-cancel-file-open-dialog-1-6-0-26
				// Goal: avoid occasional Exception while removing reference: java.lang.InterruptedException
				Env.MAIN_FRAME.dispose();
				
				System.exit( 0 );
			}
		}.start();
	}
	
}
