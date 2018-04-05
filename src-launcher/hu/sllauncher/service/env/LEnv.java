/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.sllauncher.service.env;

import hu.sllauncher.LConsts;
import hu.sllauncher.ScelightLauncher;
import hu.sllauncher.bean.BuildInfoBean;
import hu.sllauncher.bean.settings.SettingsBean;
import hu.sllauncher.gui.LauncherFrame;
import hu.sllauncher.gui.comp.XFrame;
import hu.sllauncher.r.LR;
import hu.sllauncher.service.env.bootsettings.BootSettings;
import hu.sllauncher.service.env.bootsettings.WorkspaceLocationType;
import hu.sllauncher.service.job.Jobs;
import hu.sllauncher.service.lang.Language;
import hu.sllauncher.service.log.Logger;
import hu.sllauncher.service.settings.LSettings;
import hu.sllauncher.util.LUtils;
import hu.sllauncher.util.SkillLevel;
import hu.sllauncher.util.gui.LGuiUtils;

import java.awt.Cursor;
import java.awt.Window;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.Socket;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.xml.bind.JAXB;

/**
 * Launcher environment.
 * 
 * @author Andras Belicza
 */
public class LEnv {
	
	/** {@link LUtils} implementation. */
	public static final AtomicReference< LUtils > UTILS_IMPL         = new AtomicReference<>( new LUtils() );
	
	
	/** UTF-8 charset used generally. */
	public static final Charset                   UTF8               = StandardCharsets.UTF_8;
	
	
	/** Application path. */
	public static final Path                      PATH_APP           = Paths.get( "mod" ).toAbsolutePath().getParent();
	
	
	/** Boot settings path. */
	public static final Path                      PATH_BOOT_SETTINGS = PATH_APP.resolve( "boot-settings.xml" );
	
	
	/** Boot settings bean. */
	public static final SettingsBean              BOOT_SETTINGS;
	static {
		// First backup settings
		if ( Files.exists( PATH_BOOT_SETTINGS ) )
			try {
				Files.copy( PATH_BOOT_SETTINGS, PATH_BOOT_SETTINGS.resolveSibling( PATH_BOOT_SETTINGS.getFileName() + "_" ),
				        StandardCopyOption.REPLACE_EXISTING );
			} catch ( final IOException ie ) {
				System.out.println( "Failed to backup Boot settings!" );
			}
		
		// Ensure the class listing the boot settings is loaded and initialized, so those settings will be found (by their full id).
		try {
			Class.forName( BootSettings.class.getName() );
		} catch ( final ClassNotFoundException cnfe ) {
			// Never to happen.
		}
		
		SettingsBean settings = null;
		try {
			settings = JAXB.unmarshal( PATH_BOOT_SETTINGS.toFile(), SettingsBean.class );
		} catch ( final Exception e ) {
			System.out.println( "Warning! Could not read Boot settings, the default settings will be used: " + PATH_BOOT_SETTINGS );
			settings = new SettingsBean();
		}
		BOOT_SETTINGS = settings;
		BOOT_SETTINGS.configureSave( LConsts.LAUNCHER_NAME, LConsts.LAUNCHER_VERSION, PATH_BOOT_SETTINGS );
		BOOT_SETTINGS.setValidSettingList( SettingsBean.createSettingListFromFields( BootSettings.class ) );
		
		// Save settings when logger is up (when saving fails, it uses the logger!)
	}
	
	
	/** Workspace location root path. */
	public static final Path                      PATH_WORKSPACE;
	
	/** Workspace meta data location root path (e.g. lock files). */
	public static final Path                      PATH_WORKSPACE_META;
	
	/** Workspace lock file to detect if workspace is already in use. */
	protected static final RandomAccessFile       WORKSPACE_LOCK;
	
	/** Instance Monitor info bean path. */
	public static final Path                      PATH_INSTANCE_MONITOR_INFO;
	
	static {
		Path PATH_WORKSPACE_ = null;
		Path PATH_WORKSPACE_META_ = null;
		RandomAccessFile WORKSPACE_LOCK_ = null;
		
		while ( WORKSPACE_LOCK_ == null ) {
			final WorkspaceLocationType wlt = BOOT_SETTINGS.get( BootSettings.WS_LOCATION_TYPE );
			switch ( wlt ) {
				case USER_HOME :
					PATH_WORKSPACE_ = Paths.get( System.getProperty( "user.home" ), ".Scelight/workspace" );
					break;
				case APP_FOLDER :
					PATH_WORKSPACE_ = PATH_APP.resolve( "workspace" );
					break;
				case CUSTOM_FOLDER :
					PATH_WORKSPACE_ = BOOT_SETTINGS.get( BootSettings.WS_CUSTOM_FOLDER );
					break;
				default :
					throw new RuntimeException( "Unhandled workspace location type: " + wlt );
			}
			PATH_WORKSPACE_META_ = PATH_WORKSPACE_.resolve( ".metadata" );
			
			// Check if workspace is writable
			if ( !Files.exists( PATH_WORKSPACE_META_ ) )
				try {
					Files.createDirectories( PATH_WORKSPACE_META_ );
				} catch ( final IOException e ) {
					// Whether workspace folder is writable is about to be tested, so ignore this.
				}
			try {
				WORKSPACE_LOCK_ = new RandomAccessFile( PATH_WORKSPACE_META_.resolve( ".lock" ).toFile(), "rw" );
			} catch ( final FileNotFoundException ffe ) {
				// Since mode is "rw", ending up here means workspace folder is not writable!!
				LGuiUtils.setLaf( "Nimbus" );
				if ( wlt == WorkspaceLocationType.USER_HOME ) {
					JOptionPane.showMessageDialog( null,
					        "<html><font color='red'><b>The Workspace folder is not writable:</b></font><pre>" + PATH_WORKSPACE_.toString() + "</pre><br>"
					                + LConsts.LAUNCHER_NAME + " will exit now.</html>", LConsts.LAUNCHER_NAME, JOptionPane.ERROR_MESSAGE );
					System.exit( 0 );
				} else {
					JOptionPane.showMessageDialog( null,
					        "<html><font color='red'><b>The Workspace folder is not writable:</b></font><pre>" + PATH_WORKSPACE_.toString()
					                + "</pre><br>The default Workspace in User Home will be used.</html>", LConsts.LAUNCHER_NAME, JOptionPane.ERROR_MESSAGE );
					BOOT_SETTINGS.set( BootSettings.WS_LOCATION_TYPE, WorkspaceLocationType.USER_HOME );
				}
			}
		}
		
		PATH_WORKSPACE = PATH_WORKSPACE_;
		PATH_WORKSPACE_META = PATH_WORKSPACE_META_;
		WORKSPACE_LOCK = WORKSPACE_LOCK_;
		
		PATH_INSTANCE_MONITOR_INFO = PATH_WORKSPACE_META.resolve( ".inst-mon-inf.xml" );
		
		// We have a writable workspace now. Check if workspace is in use.
		
		final Path lockInfoFile = PATH_WORKSPACE_META.resolve( ".lockInfo" );
		
		try {
			// Try lock
			if ( WORKSPACE_LOCK.getChannel().tryLock() == null ) {
				// Failed to acquire lock, workspace is already in use
				handleWorkspaceInUse( lockInfoFile );
			}
		} catch ( final IOException ie ) {
			// Logger is not yet initialized
			ie.printStackTrace();
		}
		
		// Workspace is not in use, delete existing instance monitor info file
		try {
			Files.deleteIfExists( PATH_INSTANCE_MONITOR_INFO );
		} catch ( final IOException ie ) {
			// Logger is not yet initialized
			ie.printStackTrace();
		}
		
		try {
			final String lockInfo = "<html><font color='green'><b>Workspace lock details:</b></font><table>" + "\n<tr><td><b>Locked by user:</b><td>"
			        + System.getProperty( "user.name" ) + "\n<tr><td><b>Application path:</b><td>" + PATH_APP + "\n<tr><td><b>Locked at:</b><td>"
			        + new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" ).format( new Date() ) + "</table></html>";
			Files.write( lockInfoFile, lockInfo.getBytes( UTF8 ) );
		} catch ( final IOException ie ) {
			// Logger is not yet initialized
			ie.printStackTrace();
		}
	}
	
	/**
	 * Handles the workspace is in use situation.
	 * 
	 * @param lockInfoFile workspace lock info file
	 */
	private static void handleWorkspaceInUse( final Path lockInfoFile ) {
		// Try contacting the running instance and forward the arguments if possible
		if ( Files.exists( PATH_INSTANCE_MONITOR_INFO ) ) {
			try {
				final InstMonInfBean imi = JAXB.unmarshal( PATH_INSTANCE_MONITOR_INFO.toFile(), InstMonInfBean.class );
				if ( imi.getComProtVer() != 1 )
					throw new Exception( "Unsupported Instance Monitor communication protocol version: " + imi.getComProtVer() );
				
				try ( final Socket s = new Socket( "localhost", imi.getPort() ) ) {
					try ( final DataOutputStream out = new DataOutputStream( s.getOutputStream() ) ) {
						out.writeInt( 1 );
						out.writeInt( ScelightLauncher.getArguments().length );
						
						for ( final String arg : ScelightLauncher.getArguments() )
							out.writeUTF( arg );
					}
				}
			} catch ( final Exception e ) {
				// Logger is not yet initialized
				e.printStackTrace();
			}
			
			System.exit( 0 ); // Out task here has ended.
		}
		
		LGuiUtils.setLaf( "Nimbus" );
		String details;
		try {
			details = new String( Files.readAllBytes( lockInfoFile ), UTF8 );
		} catch ( final Exception e ) {
			details = "<html><i>No details available.</i></html>";
		}
		
		final JLabel detailsLabel = new JLabel( details );
		detailsLabel.setVisible( false );
		
		final JLabel showDetailsLabel = new JLabel( "<html><a href='#'>Show details...</a></html>" );
		showDetailsLabel.setCursor( Cursor.getPredefinedCursor( Cursor.HAND_CURSOR ) );
		showDetailsLabel.addMouseListener( new MouseAdapter() {
			@Override
			public void mousePressed( final MouseEvent event ) {
				showDetailsLabel.setVisible( false );
				detailsLabel.setVisible( true );
				final Window w = SwingUtilities.getWindowAncestor( detailsLabel );
				if ( w != null ) {
					SwingUtilities.getWindowAncestor( detailsLabel ).pack();
					w.setLocationRelativeTo( null );
				}
			}
		} );
		
		JOptionPane.showMessageDialog( null,
		        new Object[] {
		                "<html><font color='red'><b>The Workspace is already in use:</b></font><pre>" + PATH_WORKSPACE.toString()
		                        + "</pre><br>Another instance of " + LConsts.LAUNCHER_NAME + " is already running, and is configured to use this Workspace."
		                        + "<br>A Workspace can only be used by one " + LConsts.LAUNCHER_NAME + " instance at a time.<br><br></html>", showDetailsLabel,
		                detailsLabel }, LConsts.LAUNCHER_NAME, JOptionPane.WARNING_MESSAGE );
		
		System.exit( 0 );
	}
	
	
	/** Tells if the application has been started; else only the launcher is running. */
	public static final AtomicBoolean             APP_STARTED            = new AtomicBoolean();
	
	
	/** Launcher build info. */
	public static final BuildInfoBean             LAUNCHER_BUILD_INFO    = JAXB.unmarshal( LR.get( "bean/build-info.xml" ), BuildInfoBean.class );
	
	/** Copyright first year. */
	public static final int                       COPYRIGHT_FIRST_YEAR   = 2013;
	
	/** Copyright last year. */
	public static final int                       COPYRIGHT_LAST_YEAR;
	static {
		final Calendar c = Calendar.getInstance();
		c.setTime( LAUNCHER_BUILD_INFO.getDate() );
		COPYRIGHT_LAST_YEAR = c.get( Calendar.YEAR );
	}
	
	/** Copyright years string. */
	public static final String                    COPYRIGHT_YEARS        = COPYRIGHT_FIRST_YEAR == COPYRIGHT_LAST_YEAR ? Integer
	                                                                             .toString( COPYRIGHT_FIRST_YEAR ) : COPYRIGHT_FIRST_YEAR + "-"
	                                                                             + COPYRIGHT_LAST_YEAR;
	
	
	/** Reported script/app that started the launcher. */
	public static final String                    LAUNCHED_WITH          = System.getProperty( "hu.scelight.launched-with" );
	
	/** Tells whether we run in dev mode. */
	public static final boolean                   DEV_MODE               = System.getProperty( "hu.scelight.dev-mode" ) != null;
	
	
	/** Running operating system. */
	public static final OpSys                     OS                     = OpSys.detect();
	
	
	
	/** Application Operator URL. */
	public static final URL                       URL_APP_OPERATOR       = LUtils.createUrl( DEV_MODE ? "https://dev-dot-scelightop.appspot.com/"
	                                                                             : "https://scelightop.appspot.com/" );
	
	/** Modules bean URL, GZip compressed content. */
	public static final URL                       URL_MODULES_BEAN_GZ    = LUtils.createUrl( URL_APP_OPERATOR, "bean/modules.xml.gz" );
	
	/** News URL, GZip compressed content. */
	public static final URL                       URL_NEWS_GZ            = LUtils.createUrl( URL_APP_OPERATOR, "news/news.html.gz" );
	
	
	
	/** Internal mods path. */
	public static final Path                      PATH_MODS              = PATH_APP.resolve( "mod" );
	
	/** External mods path. */
	public static final Path                      PATH_EXT_MODS          = PATH_APP.resolve( "mod-x" );
	
	
	/** Logs path. */
	public static final Path                      PATH_LOGS              = PATH_WORKSPACE.resolve( "logs" );
	
	
	/** Launcher and application logger. */
	public static final Logger                    LOGGER                 = new Logger();
	// Deleting old log files depends on a setting, so only call it when launcher settings are loaded
	
	static {
		// Now that the logger is up, save boot settings if it didn't exist.
		if ( BOOT_SETTINGS.getSaveTime() == null )
			BOOT_SETTINGS.save();
		
		// Boot settings will be "traced" when launcher settings are loaded and log level is set...
	}
	
	
	/** Tells if {@link SkillLevel#DEVELOPER} is enabled. */
	public static final boolean                   DEV_SKILL_LEVEL_ENABLED;
	static {
		final Path ticketFile = PATH_WORKSPACE.resolve( "dev-skill-level-ticket.txt" );
		
		boolean DEV_SKILL_LEVEL_ALLOWED_ = false;
		if ( Files.exists( ticketFile ) ) {
			try {
				// Expected content:
				// SHA-256 checksum of the decimal representation of the max value of an 8-byte signed integer shifted right
				// by a value of 0x1a and XOR-ed with the hex value CafeBabe.
				final List< String > readAllLines = Files.readAllLines( ticketFile, UTF8 );
				DEV_SKILL_LEVEL_ALLOWED_ = readAllLines.size() == 1
				        && "e0dd2851076357e801e8647b9d8ba4cab3ba8b612e7d247be1fd5dbd854197bb".equalsIgnoreCase( readAllLines.get( 0 ) );
				if ( DEV_SKILL_LEVEL_ALLOWED_ )
					LOGGER.debug( "Found Developer skill level ticket file with matching content, Developer level unlocked." );
				else
					LOGGER.debug( "Found Developer skill level ticket file but it has not the expected content: " + ticketFile );
			} catch ( final IOException ie ) {
				// Do not pass the exception for logging, it contains stack trace which would lead curious eyes
				// right here revealing the expected value...
				LOGGER.error( "Failed to read file: " + ticketFile );
			}
		}
		
		DEV_SKILL_LEVEL_ENABLED = DEV_SKILL_LEVEL_ALLOWED_;
	}
	
	
	/** Launcher settings path. */
	public static final Path                      PATH_LAUNCHER_SETTINGS = PATH_WORKSPACE.resolve( "launcher-settings.xml" );
	
	
	/** Launcher settings bean. */
	public static final SettingsBean              LAUNCHER_SETTINGS;
	static {
		// First backup settings
		if ( Files.exists( PATH_LAUNCHER_SETTINGS ) )
			try {
				Files.copy( PATH_LAUNCHER_SETTINGS, PATH_LAUNCHER_SETTINGS.resolveSibling( PATH_LAUNCHER_SETTINGS.getFileName() + "_" ),
				        StandardCopyOption.REPLACE_EXISTING );
			} catch ( final IOException ie ) {
				LEnv.LOGGER.warning( "Failed to backup " + LConsts.LAUNCHER_NAME + " settings!", ie );
			}
		
		// Ensure the class listing the launcher settings is loaded and initialized, so those settings will be found (by their full id).
		try {
			Class.forName( LSettings.class.getName() );
		} catch ( final ClassNotFoundException cnfe ) {
			// Never to happen.
		}
		
		SettingsBean settings = null;
		try {
			settings = JAXB.unmarshal( PATH_LAUNCHER_SETTINGS.toFile(), SettingsBean.class );
		} catch ( final Exception e ) {
			LOGGER.warning( "Could not read " + LConsts.LAUNCHER_NAME + " settings, the default settings will be used: " + PATH_LAUNCHER_SETTINGS );
			if ( Files.exists( PATH_LAUNCHER_SETTINGS ) ) // Only log exception if file exists but failed to read it.
				LOGGER.debug( "Reason:", e );
			else
				LOGGER.debug( "Reason: File does not exist: " + PATH_LAUNCHER_SETTINGS );
			
			settings = new SettingsBean();
		}
		LAUNCHER_SETTINGS = settings;
		LAUNCHER_SETTINGS.configureSave( LConsts.LAUNCHER_NAME, LConsts.LAUNCHER_VERSION, PATH_LAUNCHER_SETTINGS );
		LAUNCHER_SETTINGS.setValidSettingList( SettingsBean.createSettingListFromFields( LSettings.class ) );
		
		if ( LAUNCHER_SETTINGS.getSaveTime() == null )
			LAUNCHER_SETTINGS.save(); // Logger is up, safe to save
			
		// If skill level is Developer but it is not allowed, change it:
		if ( !DEV_SKILL_LEVEL_ENABLED && SkillLevel.DEVELOPER.isAtLeast() )
			LAUNCHER_SETTINGS.set( LSettings.SKILL_LEVEL, SkillLevel.ADVANCED );
		
		LOGGER.setLogLevel( LAUNCHER_SETTINGS.get( LSettings.LOG_LEVEL ) );
		
		if ( LOGGER.testTrace() )
			LOGGER.trace( "Boot settings: " + BOOT_SETTINGS.toString() );
		
		if ( LOGGER.testTrace() )
			LOGGER.trace( "Launcher settings: " + LAUNCHER_SETTINGS.toString() );
		
		LOGGER.deleteOldLogFiles();
	}
	
	
	
	/** Tells if the running environment is Eclipse. */
	public static final boolean                   ECLIPSE_MODE;
	static {
		// Check should be "strong" because Eclipse mode allows bypassing registration requirement for example!
		// So passing a system property like "-Dhu.scelight.eclipse-mode" is out of the question.
		
		// I don't use getParent().resolve() because parent might be null.
		final Path ticketFile = PATH_APP.resolve( "../dev-data/SL-ECLIPSE-MODE-TICKET.txt" );
		if ( Files.exists( ticketFile ) ) {
			ECLIPSE_MODE = "f39eaee57d50863b239b9c836a6f54e0004b99e5890f779f8aa24d2370069333".equals( LUtils.calculateFileSha256( ticketFile ) );
			if ( !ECLIPSE_MODE )
				LOGGER.debug( "Found Eclipse Mode Ticket file but it has invalid content: " + ticketFile );
		} else
			ECLIPSE_MODE = false;
	}

	
	
	/** Launcher and application language. */
	public static final Language                  LANG                   = new Language();
	
	
	/** Jobs scheduler and executor. */
	public static final Jobs                      JOBS                   = new Jobs();
	
	
	
	/** GUI (Frame) of the launcher. */
	public static LauncherFrame                   LAUNCHER_FRAME;
	
	
	/** Current GUI frame to be used as dialogs' parent. */
	public static final AtomicReference< XFrame > CURRENT_GUI_FRAME      = new AtomicReference<>();
	
}
