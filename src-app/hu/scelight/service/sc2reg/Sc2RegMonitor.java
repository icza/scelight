/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight.service.sc2reg;

import hu.belicza.andras.util.ListenerRegistry;
import hu.scelight.gui.overlaycard.ApmOverlay;
import hu.scelight.service.env.Env;
import hu.scelight.service.settings.Settings;
import hu.scelight.service.sound.Sounds;
import hu.scelightapi.service.sc2monitor.GameChangeAdapter;
import hu.scelightapi.service.sc2monitor.IGameChangeEvent;
import hu.scelightapi.service.sc2monitor.IGameChangeListener;
import hu.sllauncher.service.env.OpSys;
import hu.sllauncher.service.sound.Sound;
import hu.sllauncher.util.ControlledThread;

import javax.swing.SwingUtilities;

import com.ice.jni.registry.RegDWordValue;
import com.ice.jni.registry.RegStringValue;
import com.ice.jni.registry.Registry;
import com.ice.jni.registry.RegistryKey;
import com.ice.jni.registry.RegistryValue;

/**
 * StarCraft II Windows Registry monitor and utilities.
 * 
 * <p>
 * Detects game status changes and implements APM Alert.
 * </p>
 * 
 * @author Andras Belicza
 */
public class Sc2RegMonitor extends ControlledThread {
	
	/** Windows registry key for reading <code>HKEY_CURRENT_USER\\Software\\Razer\\Starcraft2</code>. */
	private static final RegistryKey SC2_KEY;
	static {
		if ( Env.OS == OpSys.WINDOWS ) {
			// If the key does not exist, null will be returned => isSupported() will return false.
			SC2_KEY = Registry.openSubkey( Registry.HKEY_CURRENT_USER, "Software\\Razer\\Starcraft2", RegistryKey.ACCESS_READ );
		} else
			SC2_KEY = null;
	}
	
	/**
	 * Tells if {@link Sc2RegMonitor} service is supported on the current platform.
	 * 
	 * @return true if {@link Sc2RegMonitor} service is supported on the current platform; false otherwise
	 */
	public static boolean isSupported() {
		return SC2_KEY != null;
	}
	
	
	
	/** Registry of game change listeners. */
	private final ListenerRegistry< IGameChangeListener, GameChangeEvent > listenerRegistry = new ListenerRegistry< IGameChangeListener, GameChangeEvent >() {
		                                                                                        
		                                                                                        @Override
		                                                                                        protected void notify( final IGameChangeListener listener,
		                                                                                                final GameChangeEvent event ) {
			                                                                                        listener.gameChanged( event );
			                                                                                        
			                                                                                        if ( event.oldStatus == GameStatus.ENDED
			                                                                                                && event.newStatus == GameStatus.STARTED )
				                                                                                        listener.gameStarted( event );
			                                                                                        else if ( event.oldStatus == GameStatus.STARTED
			                                                                                                && event.newStatus == GameStatus.ENDED )
				                                                                                        listener.gameEnded( event );
		                                                                                        }
		                                                                                        
	                                                                                        };
	
	/** Last checked game status. */
	private GameStatus                                                     lastGameStatus   = getGameStatus();
	
	/** Time when current game status detected. */
	private long                                                           gameStatusSince;
	
	/** Last APM OK status. */
	private boolean                                                        lastApmOk        = true;
	
	/** Time of the last Low APM alert. */
	private long                                                           lastApmAlertTime;
	
	/**
	 * Time to start the APM warm-up time from.
	 * <p>
	 * Default value is <code>null</code> which means if a game is already in progress when the application starts, the warm up time will be skipped (we have no
	 * info when the game started, we can't do "better").
	 * </p>
	 */
	private Long                                                           warmUpTimeStart;
	
	/**
	 * Creates a new {@link Sc2RegMonitor}.
	 */
	public Sc2RegMonitor() {
		super( "SC2 Registry Monitor" );
		
		if ( !isSupported() )
			throw new RuntimeException( "SC2 Registry Monitor is not supported on this platform!" );
		
		addGameChangeListener( new GameChangeAdapter() {
			@Override
			public void gameStarted( final IGameChangeEvent event ) {
				Env.LOGGER.debug( "SC2 Game started." );
				
				if ( Env.APP_SETTINGS.get( Settings.PLAY_GAME_STARTED_VOICE ) )
					Sound.play( Sounds.GAME_STARTED );
				
				if ( Env.APP_SETTINGS.get( Settings.SHOW_APM_OVERLAY_WHEN_STARTED ) )
					SwingUtilities.invokeLater( new Runnable() {
						@Override
						public void run() {
							// EDT needed!
							if ( ApmOverlay.INSTANCE() == null )
								new ApmOverlay();
						}
					} );
			}
			
			@Override
			public void gameEnded( final IGameChangeEvent event ) {
				Env.LOGGER.debug( "SC2 Game ended." );
				
				if ( Env.APP_SETTINGS.get( Settings.PLAY_GAME_ENDED_VOICE ) )
					Sound.play( Sounds.GAME_ENDED );
				
				if ( Env.APP_SETTINGS.get( Settings.HIDE_APM_OVERLAY_WHEN_ENDED ) ) {
					SwingUtilities.invokeLater( new Runnable() {
						@Override
						public void run() {
							// EDT needed!
							final ApmOverlay apmOverlay = ApmOverlay.INSTANCE();
							if ( apmOverlay != null )
								apmOverlay.close();
						}
					} );
				}
			}
		} );
	}
	
	/**
	 * Adds an {@link IGameChangeListener} which will be called when the game (status) changes.
	 * 
	 * @param listener listener to be added
	 * 
	 * @see #removeGameChangeListener(IGameChangeListener)
	 */
	public void addGameChangeListener( final IGameChangeListener listener ) {
		listenerRegistry.addListener( listener );
	}
	
	/**
	 * Removes an {@link IGameChangeListener}.
	 * 
	 * @param listener listener to be removed
	 * 
	 * @see #addGameChangeListener(IGameChangeListener)
	 */
	public void removeGameChangeListener( final IGameChangeListener listener ) {
		listenerRegistry.removeListener( listener );
	}
	
	@Override
	public void customRun() {
		while ( mayContinue() ) {
			
			final long currentTime = System.currentTimeMillis();
			
			final GameStatus newStatus = getGameStatus();
			
			// Handle game status changes
			if ( lastGameStatus != newStatus ) {
				final GameChangeEvent event = new GameChangeEvent( lastGameStatus, newStatus );
				
				// Notify game change listeners in a new thread in case some of them would execute slowly
				// and might make us miss a state change...
				new ControlledThread( "Game Change Listeners Notifier" ) {
					@Override
					public void customRun() {
						listenerRegistry.fireEvent( event );
					}
				}.start();
				
				// Initialize APM alert
				if ( lastGameStatus != GameStatus.STARTED && newStatus == GameStatus.STARTED ) {
					// A game just started now.
					
					// When a game start is detected, clear the "cached" APM alert info.
					// This is done by setting the last APM OK status to true
					// (so if APM is low, lastApmAlertTime will be overwritten).
					lastApmOk = true;
					// If last status is not ended (therefore Unknown), we will (have to) skip the warm-up time.
					warmUpTimeStart = lastGameStatus == GameStatus.ENDED ? currentTime : null;
				}
				
				lastGameStatus = newStatus;
				gameStatusSince = currentTime;
			}
			
			// Handle APM alerts
			if ( Env.APP_SETTINGS.get( Settings.ENABLE_APM_ALERT ) && lastGameStatus == GameStatus.STARTED
			        && ( warmUpTimeStart == null || currentTime > warmUpTimeStart + Env.APP_SETTINGS.get( Settings.APM_ALERT_WARMUP_TIME ) * 1000 ) ) {
				
				Integer actualApm = getApm();
				if ( actualApm != null ) {
					if ( actualApm >= Env.APP_SETTINGS.get( Settings.APM_ALERT_LEVEL ) ) {
						// APM is OK now.
						if ( !lastApmOk ) {
							// If it was low before
							if ( Env.APP_SETTINGS.get( Settings.APM_ALERT_WHEN_OK ) )
								Sound.play( Sounds.APM_OK );
							lastApmOk = true;
						}
					} else {
						// APM is low now!
						if ( lastApmOk ) {
							// If it was OK before
							lastApmAlertTime = currentTime;
							Sound.play( Sounds.LOW_APM );
							lastApmOk = false;
						} else {
							// It was low before, too!
							final int lowApmRepetitionInterval = Env.APP_SETTINGS.get( Settings.LOW_APM_ALERT_REPETITION );
							if ( currentTime > lastApmAlertTime + lowApmRepetitionInterval * 1000 ) {
								// Been low for too long, have to repeat the Low APM alert
								lastApmAlertTime = currentTime;
								Sound.play( Sounds.LOW_APM );
							}
						}
					}
				}
			}
			
			checkedSleep( 500 );
		}
	}
	
	/**
	 * Returns the time when the current game status was detected.<br>
	 * The returned value is the number of milliseconds since January 1, 1970, 00:00:00 GMT.<br>
	 * <code>0L</code> is returned if it is unknown when current game status took effect (this is the case when the application starts).
	 * 
	 * @return the time when the current game status was detected
	 */
	public long getGameStatusSince() {
		return gameStatusSince;
	}
	
	/**
	 * Returns the current game status from the Windows Registry.
	 * 
	 * @return the current game status
	 */
	public static GameStatus getGameStatus() {
		try {
			final RegistryValue startModuleValue = SC2_KEY.getValue( "StartModule" );
			
			if ( startModuleValue instanceof RegDWordValue )
				return GameStatus.fromRegistryValue( ( (RegDWordValue) startModuleValue ).getData() );
			
		} catch ( final Exception e ) {
			// Silently ignore, do not print stack trace
		}
		
		return GameStatus.UNKNOWN;
	}
	
	/**
	 * Returns the current APM originating from the Windows Registry.
	 * 
	 * Note: since StarCraft II version 2.0 SC2 outputs real-time APM instead of game-time APM, so no conversion is performed to convert it anymore.
	 * 
	 * <p>
	 * <b>How to interpret the values in the registry?</b><br>
	 * The digits of the result: 5 has to be subtracted from the first digit (in decimal representation), 4 has to be subtracted from the second digit, 3 from
	 * the third etc. If the result of a subtraction is negative, 10 has to be added. Examples: 64 &rArr; 10 APM; 40 &rArr; 96 APM; 8768 &rArr; 3336 APM; 38
	 * &rArr; 84 APM
	 * </p>
	 * 
	 * @return the current APM or <code>null</code> if some error occurred
	 */
	public static Integer getApm() {
		try {
			final RegistryValue apmValue = SC2_KEY.getValue( "APMValue" );
			if ( !( apmValue instanceof RegStringValue ) )
				return null;
			
			final String apmString = ( (RegStringValue) apmValue ).getData();
			int apm = 0;
			
			for ( int i = 0; i < apmString.length(); i++ ) {
				int digit = apmString.charAt( i ) - '0' - ( 5 - i );
				if ( digit < 0 )
					digit += 10;
				apm = apm * 10 + digit;
			}
			
			return apm;
		} catch ( final Exception e ) {
			// Silently ignore, do not print stack trace
			return null;
		}
	}
	
}
