/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.sllauncher.service.sound;

import hu.scelightapibase.bean.settings.ISettingChangeEvent;
import hu.scelightapibase.bean.settings.ISettingChangeListener;
import hu.sllauncher.bean.settings.type.BoolSetting;
import hu.sllauncher.gui.icon.LIcons;
import hu.sllauncher.service.env.LEnv;
import hu.sllauncher.service.job.Job;
import hu.sllauncher.service.settings.LSettings;

import java.awt.Toolkit;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.SourceDataLine;

/**
 * Background sound player and sound utilities.
 * 
 * @author Andras Belicza
 */
public class Sound {
	
	/**
	 * Unconditional beep.
	 */
	public static void doBeep() {
		Toolkit.getDefaultToolkit().beep();
	}
	
	/**
	 * Beeps if beeps are allowed ({@link LSettings#ENABLE_BEEPS}).
	 */
	public static void beep() {
		if ( LEnv.LAUNCHER_SETTINGS.get( LSettings.ENABLE_BEEPS ) )
			doBeep();
	}
	
	/**
	 * Beeps if the specified {@link BoolSetting} is enabled in the launcher settings.
	 * 
	 * @param setting bool setting to tell if been is allowed
	 */
	private static void beepIfEnabled( final BoolSetting setting ) {
		if ( LEnv.LAUNCHER_SETTINGS.get( setting ) )
			beep();
	}
	
	/**
	 * Beeps if error beeps are allowed ({@link LSettings#BEEP_ON_ERROR}).
	 */
	public static void beepOnError() {
		beepIfEnabled( LSettings.BEEP_ON_ERROR );
	}
	
	/**
	 * Beeps if warning beeps are allowed ({@link LSettings#BEEP_ON_WARNING}).
	 */
	public static void beepOnWarning() {
		beepIfEnabled( LSettings.BEEP_ON_WARNING );
	}
	
	/**
	 * Beeps if confirmation beeps are allowed ({@link LSettings#BEEP_ON_CONFIRMATION}).
	 */
	public static void beepOnConfirmation() {
		beepIfEnabled( LSettings.BEEP_ON_CONFIRMATION );
	}
	
	/**
	 * Beeps if input beeps are allowed ({@link LSettings#BEEP_ON_INPUT}).
	 */
	public static void beepOnInput() {
		beepIfEnabled( LSettings.BEEP_ON_INPUT );
	}
	
	/**
	 * Beeps if input beeps are allowed ({@link LSettings#BEEP_ON_INFO}).
	 */
	public static void beepOnInfo() {
		beepIfEnabled( LSettings.BEEP_ON_INFO );
	}
	
	/**
	 * Beeps if empty text search result beeps are allowed ({@link LSettings#BEEP_EMPTY_TXT_SEARCH_RSLT}).
	 */
	public static void beepOnEmptyTxtSearchRslt() {
		beepIfEnabled( LSettings.BEEP_EMPTY_TXT_SEARCH_RSLT );
	}
	
	
	
	/**
	 * Plays a sound if voice notifications are allowed ({@link LSettings#ENABLE_VOICE} setting).
	 * 
	 * <p>
	 * The sound will be played by a {@link Job} in the background, this method returns immediately.
	 * </p>
	 * 
	 * @param sound sound resource to be played
	 * @return true if the file was started playing; false if error occurred or voice notifications are not enabled
	 */
	public static boolean play( final LRSound sound ) {
		return play( sound, null, null );
	}
	
	/**
	 * Plays a sound if voice notifications are allowed ({@link LSettings#ENABLE_VOICE} setting).
	 * <p>
	 * The sound will be played by a {@link Job} in the background, this method returns immediately.
	 * </p>
	 * 
	 * @param sound sound resource to be played
	 * @param soundTheme sound theme whose specified sound to play
	 * @return true if the file was started playing; false if error occurred or voice notifications are not enabled
	 */
	public static boolean play( final LRSound sound, final SoundTheme soundTheme ) {
		return play( sound, soundTheme, null );
	}
	
	/**
	 * Plays a sound if voice notifications are allowed ({@link LSettings#ENABLE_VOICE} setting).
	 * 
	 * <p>
	 * The sound will be played by a {@link Job} in the background, this method returns immediately.
	 * </p>
	 * 
	 * @param sound sound resource to be played
	 * @param volume optional volume, must be in the range of 0..100 (inclusive); if <code>null</code>, the current voice volume from the settings will be used
	 *            (and also will be followed if it is changed during playing)
	 * @return true if the file was started playing; false if error occurred or voice notifications are not enabled
	 */
	public static boolean play( final LRSound sound, final Integer volume ) {
		return play( sound, null, volume );
	}
	
	/**
	 * Plays a sound if voice notifications are allowed ({@link LSettings#ENABLE_VOICE} setting).
	 * 
	 * <p>
	 * The sound will be played by a {@link Job} in the background, this method returns immediately.
	 * </p>
	 * 
	 * @param sound sound resource to be played
	 * @param soundTheme optional sound theme whose specified sound to play; if <code>null</code>, current voice them from the settings will be used
	 * @param volume optional volume, must be in the range of 0..100 (inclusive); if <code>null</code>, the current voice volume from the settings will be used
	 *            (and also will be followed if it is changed during playing)
	 * @return true if the file was started playing; false if error occurred or voice notifications are not enabled
	 */
	public static boolean play( final LRSound sound, final SoundTheme soundTheme, final Integer volume ) {
		if ( !LEnv.LAUNCHER_SETTINGS.get( LSettings.ENABLE_VOICE ) )
			return false;
		
		InputStream stream = sound.getStream( soundTheme == null ? LEnv.LAUNCHER_SETTINGS.get( LSettings.VOICE_THEME ) : soundTheme );
		
		if ( stream == null ) // Revert to the GOOGLE_US sound theme
			stream = sound.getStream( SoundTheme.DEFAULT );
		
		return play( sound.name, stream, volume, false );
	}
	
	/**
	 * Plays a sound file if voice notifications are allowed ({@link LSettings#ENABLE_VOICE} setting).
	 * 
	 * <p>
	 * Supported file formats include <b>MP3</b>, WAV, AIFF, AU.
	 * </p>
	 * 
	 * <p>
	 * The sound will be played by a {@link Job}.
	 * </p>
	 * 
	 * @param file sound file to be played
	 * @param wait tells if have to wait the end of the play or return immediately
	 * @return true if the file was started playing; false if error occurred
	 */
	public static boolean play( final Path file, final boolean wait ) {
		if ( !LEnv.LAUNCHER_SETTINGS.get( LSettings.ENABLE_VOICE ) )
			return false;
		
		try {
			return play( file.getFileName().toString(), Files.newInputStream( file ), null, wait );
		} catch ( final IOException ie ) {
			LEnv.LOGGER.error( "Could not read file: " + file, ie );
			return false;
		}
	}
	
	/**
	 * Plays a sound if voice notifications are allowed ({@link LSettings#ENABLE_VOICE} setting).
	 * 
	 * <p>
	 * Supported stream formats include <b>MP3</b>, WAV, AIFF, AU.
	 * </p>
	 * 
	 * <p>
	 * The sound will be played by a {@link Job}. The <code>dataStream</code> will be closed at the end of play.
	 * </p>
	 * 
	 * @param name name of the sound to be played
	 * @param dataStream data stream to read the sound data from
	 * @param wait tells if have to wait the end of the play or return immediately
	 * @return true if the file was started playing; false if error occurred
	 */
	public static boolean play( final String name, InputStream dataStream, final boolean wait ) {
		if ( !LEnv.LAUNCHER_SETTINGS.get( LSettings.ENABLE_VOICE ) )
			return false;
		
		return play( name, dataStream, null, wait );
	}
	
	/**
	 * Plays a sound even if voice notifications are not allowed.
	 * 
	 * <p>
	 * Supported stream formats include <b>MP3</b>, WAV, AIFF, AU.
	 * </p>
	 * 
	 * <p>
	 * The sound will be played by a {@link Job}. The <code>dataStream</code> will be closed at the end of play.
	 * </p>
	 * 
	 * @param name name of the sound to be played
	 * @param dataStream data stream to read the sound data from
	 * @param volume optional volume, must be in the range of 0..100 (inclusive); if <code>null</code>, the current voice volume from the settings will be used
	 *            (and also will be followed if it changes while playing)
	 * @param wait tells if have to wait the end of the play or return immediately
	 * @return true if the file was started playing; false if error occurred
	 */
	public static boolean play( final String name, InputStream dataStream, final Integer volume, final boolean wait ) {
		try {
			// AudioSystem requires the stream to support marks:
			if ( !dataStream.markSupported() )
				dataStream = new BufferedInputStream( dataStream );
			
			final AudioInputStream audioInputStream = AudioSystem.getAudioInputStream( dataStream );
			final AudioFormat audioFormat = audioInputStream.getFormat();
			
			// We have to create a target format because the MP3 format (variable bit rate) is not supported by the audio system
			final AudioFormat targetFormat = new AudioFormat( AudioFormat.Encoding.PCM_SIGNED, audioFormat.getSampleRate(), 16, audioFormat.getChannels(),
			        audioFormat.getChannels() * 2, audioFormat.getSampleRate(), false );
			final AudioInputStream wrappedAudioInputStream = AudioSystem.getAudioInputStream( targetFormat, audioInputStream );
			final SourceDataLine audioLine = (SourceDataLine) AudioSystem.getLine( new DataLine.Info( SourceDataLine.class, targetFormat ) );
			
			audioLine.open( targetFormat );
			
			final Job playerJob = new Job( "Sound player: " + name, LIcons.F_MUSIC ) {
				/**
				 * Sets the volume used to play the sound.
				 * 
				 * @param volume volume to be set
				 */
				private void setVolume( final int volume ) {
					if ( audioLine.isControlSupported( FloatControl.Type.MASTER_GAIN ) ) {
						final FloatControl volumeControl = (FloatControl) audioLine.getControl( FloatControl.Type.MASTER_GAIN );
						volumeControl.setValue( 20.0f * (float) Math.log10( volume / 100.0 ) );
					}
				}
				
				@Override
				public void jobRun() {
					final ISettingChangeListener scl;
					if ( volume == null ) {
						// If no volume is provided, use and follow the current setting
						scl = new ISettingChangeListener() {
							@Override
							public void valuesChanged( final ISettingChangeEvent event ) {
								if ( event.affected( LSettings.VOICE_VOLUME ) )
									setVolume( event.get( LSettings.VOICE_VOLUME ) );
							}
						};
						LEnv.LAUNCHER_SETTINGS.addAndExecuteChangeListener( LSettings.VOICE_VOLUME, scl );
					} else {
						scl = null;
						setVolume( volume );
					}
					
					try {
						audioLine.start();
						
						final byte[] buffer = new byte[ 64 * 1024 ];
						int bytesRead;
						
						while ( mayContinue() && ( bytesRead = wrappedAudioInputStream.read( buffer ) ) > 0 )
							audioLine.write( buffer, 0, bytesRead );
					} catch ( final Exception e ) {
						LEnv.LOGGER.error( "Error while playing sound.", e );
					} finally {
						if ( scl != null )
							LEnv.LAUNCHER_SETTINGS.removeChangeListener( LSettings.VOICE_VOLUME, scl );
						
						audioLine.drain();
						audioLine.stop();
						audioLine.close();
						try {
							wrappedAudioInputStream.close();
						} catch ( final IOException ie ) {
							// We're done, just ignore.
						}
						try {
							audioInputStream.close();
						} catch ( final IOException ie ) {
							// We're done, just ignore.
						}
					}
				}
			};
			playerJob.start();
			
			if ( wait )
				playerJob.waitToFinish();
			
			return true;
		} catch ( final Exception e ) {
			LEnv.LOGGER.error( "Could not play sound: " + name, e );
			return false;
		}
	}
	
}
