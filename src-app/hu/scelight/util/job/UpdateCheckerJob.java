/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight.util.job;

import hu.scelight.gui.icon.Icons;
import hu.scelight.service.env.Env;
import hu.scelightopapi.ScelightOpApi;
import hu.sllauncher.ScelightLauncher;
import hu.sllauncher.service.env.LEnv;
import hu.sllauncher.service.job.Job;
import hu.sllauncher.service.settings.LSettings;
import hu.sllauncher.util.UrlBuilder;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Objects;

import javax.swing.SwingUtilities;

/**
 * Update checker job.
 * 
 * @author Andras Belicza
 */
public class UpdateCheckerJob extends Job {
	
	/**
	 * Type of the update check.
	 * 
	 * @author Andras Belicza
	 */
	public enum Type {
		
		/** Scheduled, automatic. */
		SCHEDULED,
		
		/** Manually triggered. */
		MANUAL;
		
	}
	
	/** Type of the update check. */
	private final Type type;
	
	/** Uptime miniutes to be reported. */
	private final int  minsToReport;
	
	/**
	 * Creates a new {@link UpdateCheckerJob}.
	 * 
	 * @param type type of the update check
	 * @param minsToReport uptime minutes to be reported
	 */
	public UpdateCheckerJob( final Type type, final int minsToReport ) {
		super( "Update checker", Icons.F_ARROW_CIRCLE_DOUBLE );
		
		this.type = type;
		this.minsToReport = minsToReport;
	}
	
	@Override
	public void jobRun() {
		// Check for updates.
		
		// Try again a few times, because if computer is back from sleep and the check time is passed, timer will fire immediately
		// but in Windows 8 for example network is not yet up for a few seconds.
		for ( int retries = 1;; retries++ ) {
			Env.LOGGER.info( "Checking for updates (" + type + ( retries == 1 ? ")..." : ") (" + retries + ")..." ) );
			
			final UrlBuilder urlBuilder = new UrlBuilder( Env.URL_MODULES_BEAN_DIGEST );
			urlBuilder.addParam( ScelightOpApi.PARAM_MODULES_DIGEST_TYPE, type.name() );
			urlBuilder.addParam( ScelightOpApi.PARAM_MODULES_DIGEST_MINS, minsToReport );
			urlBuilder.addParam( ScelightOpApi.PARAM_MODULES_BEAN_SKILL_LEVEL, Env.LAUNCHER_SETTINGS.get( LSettings.SKILL_LEVEL ).name() );

			urlBuilder.addTimestamp();
			
			try ( final BufferedReader in = new BufferedReader( new InputStreamReader( urlBuilder.toUrl().openStream(), Env.UTF8 ) ) ) {
				final String digest = in.readLine();
				Objects.requireNonNull( digest );
				
				if ( !ScelightLauncher.INSTANCE().getModules().getDigest().equals( digest ) ) {
					SwingUtilities.invokeLater( new Runnable() {
						@Override
						public void run() {
							Env.LOGGER.info( "Updates available!" );
							Env.MAIN_FRAME.activateUpdatesAvailable();
						}
					} );
				}
				
				break; // Update check succeeded, no need to retry again
				
			} catch ( final Exception e ) {
				if ( retries >= 3 ) {
					LEnv.LOGGER.error( "Failed to check updates! Giving up.", e );
					break;
				}
				Env.LOGGER.warning( "Failed to check updates! Retrying shortly.", e );
				checkedSleep( 20_000 );
			}
		}
	}
	
}
