/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight.service.watcher;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.swing.SwingUtilities;

import hu.belicza.andras.util.ListenerRegistry;
import hu.scelight.gui.icon.Icons;
import hu.scelight.gui.overlaycard.LastGameInfoOverlay;
import hu.scelight.sc2.rep.factory.RepParserEngine;
import hu.scelight.sc2.rep.repproc.RepProcessor;
import hu.scelight.service.env.Env;
import hu.scelight.service.settings.Settings;
import hu.scelight.service.sound.Sounds;
import hu.scelight.template.InvalidTemplateException;
import hu.scelight.template.TemplateEngine;
import hu.scelight.util.Utils;
import hu.scelight.util.gui.GuiUtils;
import hu.scelightapi.service.repfoldermonitor.INewRepListener;
import hu.scelightapibase.gui.comp.multipage.IPage;
import hu.sllauncher.service.job.Job;
import hu.sllauncher.service.sound.Sound;
import hu.sllauncher.util.RelativeDate;

/**
 * Handles a detected new replay file.
 * 
 * @author Andras Belicza
 */
public class NewReplayHandlerJob extends Job {
	
	/** Registry of new rep listeners. */
	private static final ListenerRegistry< INewRepListener, NewRepEvent > listenerRegistry = new ListenerRegistry< INewRepListener, NewRepEvent >() {
		
		@Override
		protected void notify( final INewRepListener listener, final NewRepEvent event ) {
			listener.newRepDetected( event );
		}
		
	};
	
	/**
	 * Adds an {@link INewRepListener} which will be called when new replays are detected in monitored replay folders.
	 * 
	 * @param listener listener to be added
	 */
	public static void addNewRepListener( final INewRepListener listener ) {
		listenerRegistry.addListener( listener );
	}
	
	/**
	 * Removes an {@link INewRepListener}.
	 * 
	 * @param listener listener to be removed
	 */
	public static void removeNewRepListener( final INewRepListener listener ) {
		listenerRegistry.removeListener( listener );
	}
	
	
	/** Default replay backup name format. */
	private static final DateFormat	DEFAULT_REPLAY_NAME_FORMAT = new SimpleDateFormat( "yy-MM-dd HH_mm_ss'.SC2Replay'" );
															   
	/** Original detected new replay file to handle. */
	private Path					originalFile;
									
	/** Detected new replay file after potential renaming, moving. */
	private Path					file;
									
	/** Folder info in which the file as detected. */
	private final FolderInfo		folderInfo;
									
	/**
	 * Creates a new {@link NewReplayHandlerJob}.
	 * 
	 * @param file detected new replay file to handle
	 * @param folderInfo folder info in which the file as detected
	 */
	public NewReplayHandlerJob( final Path file, final FolderInfo folderInfo ) {
		super( "New Replay Handler: " + file, Icons.F_BLUE_FOLDER_SEARCH_RESULT );
		
		this.originalFile = file;
		this.file = file;
		this.folderInfo = folderInfo;
	}
	
	@Override
	public void jobRun() {
		// Wait a little, let SC2 finish saving the game...
		checkedSleep( 1500L );
		
		// Check if only "just played" reps needs to be handled
		if ( Env.APP_SETTINGS.get( Settings.ONLY_PICK_UP_JUST_PLAYED_REPS ) ) {
			final RepProcessor repProc = RepParserEngine.getRepProc( file );
			// If replay can't be parsed to determine date, process the replay.
			if ( repProc != null ) {
				final RelativeDate rd = new RelativeDate( repProc.replay.details.getTime() );
				if ( rd.deltaMs > Utils.MS_IN_MIN * 2 * 60 ) {
					// Just log and return
					Env.LOGGER.debug( "Detected new replay but not picking up because it's too old (played " + rd.toString() + ")." );
					return;
				}
			}
		}
		
		// Rename new replay
		if ( Env.APP_SETTINGS.get( Settings.RENAME_NEW_REPLAYS ) )
			renameReplay();
			
		// First backup replay and only open after that because original replay might get deleted
		
		// Backup new replay
		if ( Env.APP_SETTINGS.get( Settings.BACKUP_REPLAYS ) )
			backupReplay();
			
		// Show Game Info Overlay
		if ( Env.APP_SETTINGS.get( Settings.SHOW_GAME_INFO_FOR_NEW_REPS ) )
			showGameInfo();
			
		// Open new replay in the Replay Analyzer
		if ( Env.APP_SETTINGS.get( Settings.OPEN_NEW_REPLAYS ) )
			openReplay();
			
		// Notify new rep listeners
		listenerRegistry.fireEvent( new NewRepEvent( file, originalFile ) );
	}
	
	/**
	 * Renames the new replay in its original folder.
	 */
	private void renameReplay() {
		Path targetFile;
		// Try the rename new reps name template
		try {
			final TemplateEngine engine = new TemplateEngine( Env.APP_SETTINGS.get( Settings.RENAME_NEW_REPS_TEMPLATE ) );
			targetFile = engine.apply( file );
		} catch ( final InvalidTemplateException ite ) {
			Env.LOGGER.error( "Failed to rename new replay, template is invalid: " + Env.APP_SETTINGS.get( Settings.RENAME_NEW_REPS_TEMPLATE ), ite );
			
			// No default naming, if failed to apply name template simply return
			return;
		}
		
		// If replay parsing fails (e.g. new, not yet supported version), TemplateEngine.apply() returns null.
		if ( targetFile == null ) {
			return;
		}
		
		// Unify file name if already exists
		targetFile = Utils.uniqueFile( targetFile );
		
		// Renaming a replay in its original folder generates StandardWatchEventKinds.ENTRY_DELETE and
		// StandardWatchEventKinds.ENTRY_CREATE.
		// To avoid re-processing the renamed replay, we have to put it on ignore list.
		//
		// Warning! When name template is applied, it might add subfolders.
		// we have to check if subfolders are also monitored, and only add the target file to ignore list if so!
		boolean ignore = true;
		if ( !file.getParent().equals( targetFile.getParent() ) && !folderInfo.recursive )
			ignore = false;
		if ( ignore )
			Env.WATCHER.addIgnoredPath( targetFile );
			
		// Create potential sub-folders introduced by the name template
		if ( !Files.exists( targetFile.getParent() ) )
			try {
				Files.createDirectories( targetFile.getParent() );
			} catch ( final IOException ie ) {
				Env.LOGGER.error( "Failed to create subfolders, new replay renaming will probably fail: " + targetFile.getParent(), ie );
			}
			
		// Removing it from the ignore list will be done by the Watcher itself.
		// We can't remove it here (e.g. in a finally block) because creation and/or processing the ENTRY_CREATE event
		// usually happens at a later time (after Files.move() returns).
		// We only have to remove it if renaming fails because then there won't be a StandardWatchEventKinds.ENTRY_DELETE event.
		
		try {
			Files.move( file, targetFile );
			Env.LOGGER.info( "Renamed new replay: \"" + file + "\" to \"" + targetFile + "\"" );
			file = targetFile;
		} catch ( final IOException ie ) {
			Env.LOGGER.error( "Failed to rename new replay: \"" + file + "\" to \"" + targetFile + "\"", ie );
			if ( ignore )
				Env.WATCHER.removeIgnoredPath( targetFile );
		}
	}
	
	/**
	 * Backs up the replay.
	 */
	private void backupReplay() {
		final Path backupFolder = Env.APP_SETTINGS.get( Settings.REPLAY_BACKUP_FOLDER );
		
		if ( !Files.exists( backupFolder ) )
			try {
				Files.createDirectories( backupFolder );
			} catch ( final IOException ie ) {
				Env.LOGGER.error( "Failed to create replay backup folder: " + backupFolder, ie );
				Sound.play( Sounds.FAILED_TO_SAVE_REPLAY );
				return;
			}
			
		Path targetFile = null;
		// Try the backup name template first
		try {
			final TemplateEngine engine = new TemplateEngine( Env.APP_SETTINGS.get( Settings.REPLAY_BACKUP_TEMPLATE ) );
			targetFile = engine.apply( file, backupFolder );
		} catch ( final InvalidTemplateException ite ) {
			Env.LOGGER.debug( "Failed to apply Replay Backup Template: template is invalid!", ite );
		}
		// Revert to default naming if template didn't work out
		if ( targetFile == null )
			targetFile = backupFolder.resolve( DEFAULT_REPLAY_NAME_FORMAT.format( new Date() ) );
			
		// Unify file name if already exists
		targetFile = Utils.uniqueFile( targetFile );
		
		// Create potential sub-folders introduced by the name template
		if ( !Files.exists( targetFile.getParent() ) )
			try {
				Files.createDirectories( targetFile.getParent() );
			} catch ( final IOException ie ) {
				Env.LOGGER.error( "Failed to create subfolders, replay backup will probably fail: " + targetFile.getParent(), ie );
			}
			
		try {
			Files.copy( file, targetFile, StandardCopyOption.COPY_ATTRIBUTES );
		} catch ( final IOException ie ) {
			Env.LOGGER.error( "Failed to backup new replay: \"" + file + "\" to \"" + targetFile + "\"", ie );
			Sound.play( Sounds.FAILED_TO_SAVE_REPLAY );
			return;
		}
		
		// Success!
		Env.LOGGER.info( "New replay saved: " + targetFile );
		if ( Env.APP_SETTINGS.get( Settings.PLAY_REPLAY_SAVED_VOICE ) )
			Sound.play( Sounds.REPLAY_SAVED );
			
		// Only attempt deleting if backing up was successful (if we reached this far, it was)
		if ( Env.APP_SETTINGS.get( Settings.DELETE_BACKED_UP_REPLAYS ) )
			try {
				Files.delete( file );
				// Original file no longer available:
				file = targetFile;
			} catch ( final IOException ie ) {
				Env.LOGGER.error( "Failed to delete successfully backed up new replay: " + file, ie );
			}
	}
	
	/**
	 * Show Game Info Overlay.
	 */
	private void showGameInfo() {
		if ( Env.APP_SETTINGS.get( Settings.SHOW_GAME_INFO_FOR_NEW_REPS ) )
			SwingUtilities.invokeLater( new Runnable() {
				@Override
				public void run() {
					// EDT needed!
					if ( LastGameInfoOverlay.INSTANCE() == null )
						new LastGameInfoOverlay( file );
				}
			} );
	}
	
	/**
	 * Opens the replay in the Replay Analyzer.
	 */
	private void openReplay() {
		final List< IPage< ? > > repAnalyzerList = Env.MAIN_FRAME.repAnalyzersPage.getChildList();
		if ( repAnalyzerList != null && repAnalyzerList.size() >= Env.APP_SETTINGS.get( Settings.MAX_REP_ANALYZERS_FOR_NEW_REPS ) )
			return;
			
		GuiUtils.runInEDT( new Runnable() {
			@Override
			public void run() {
				Env.MAIN_FRAME.repAnalyzersPage.newRepAnalyzerPage( file, true );
			}
		} );
	}
	
}
