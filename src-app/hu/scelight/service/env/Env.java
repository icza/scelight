/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight.service.env;

import hu.scelight.Consts;
import hu.scelight.action.Actions;
import hu.scelight.bean.repfolders.RepFolderBean;
import hu.scelight.bean.repfolders.RepFolderOrigin;
import hu.scelight.bean.repfolders.RepFoldersBean;
import hu.scelight.gui.MainFrame;
import hu.scelight.r.R;
import hu.scelight.sc2.rep.cache.RepProcCache;
import hu.scelight.service.extmod.ExtModManager;
import hu.scelight.service.instance.InstanceMonitor;
import hu.scelight.service.mapdl.MapDownloadManager;
import hu.scelight.service.sc2reg.Sc2RegMonitor;
import hu.scelight.service.settings.Settings;
import hu.scelight.service.watcher.Watcher;
import hu.scelight.util.Utils;
import hu.scelightapi.r.XR;
import hu.sllauncher.ScelightLauncher;
import hu.sllauncher.bean.BuildInfoBean;
import hu.sllauncher.bean.module.ModulesBeanOrigin;
import hu.sllauncher.bean.settings.SettingsBean;
import hu.sllauncher.service.env.LEnv;
import hu.sllauncher.service.env.OpSys;
import hu.sllauncher.util.LUtils;
import hu.sllibs.r.AR;
import hu.slsc2balancedata.r.BR;
import hu.slsc2textures.r.TR;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import javax.swing.Timer;
import javax.xml.bind.JAXB;

import com.ice.jni.registry.Registry;

/**
 * Application environment.
 * 
 * @author Andras Belicza
 */
public class Env extends LEnv {
	
	static {
		APP_STARTED.set( true );
		UTILS_IMPL.set( new Utils() );
	}
	
	/** Tells if we're in online mode, meaning we were able to reach the Application Operator on startup. */
	public static final boolean            ONLINE                      = ScelightLauncher.INSTANCE().getModules().getOrigin() == ModulesBeanOrigin.APP_OPERATOR;
	
	
	/** Application Operator User Page URL. */
	public static final URL                URL_APP_OP_USER_PAGE        = Utils.createUrl( URL_APP_OPERATOR, "User.html" );
	
	/** Application Operator Regfile servlet URL. */
	public static final URL                URL_APP_OP_REGFILE          = Utils.createUrl( URL_APP_OPERATOR, "regfile" );
	
	/** Modules bean digest URL. */
	public static final URL                URL_MODULES_BEAN_DIGEST     = LUtils.createUrl( URL_APP_OPERATOR, "bean/modules.xml.digest" );
	
	
	/** App module path, version included. */
	public static final Path               PATH_MOD_APP_VER            = PATH_MODS.resolve( ECLIPSE_MODE ? "app/0.0/" : "app/" + Consts.APP_VERSION.toString() );
	
	/** App-libs module path, version included. */
	public static final Path               PATH_MOD_APP_LIBS_VER       = PATH_MODS.resolve( ECLIPSE_MODE ? "app-libs/0.0/" : "app-libs/"
	                                                                           + Consts.APP_LIBS_VERSION.toString() );
	
	static {
		// os.arch: "x86" | "amd64"
		final String arch = System.getProperty( "os.arch" ).contains( "64" ) ? "64" : "32";
		
		// Load native libraries
		if ( OS == OpSys.WINDOWS ) {
			// Windows registry library: JNIRegistry
			// Original: http://www.trustice.com/java/jnireg/
			//
			// 64-bit version: http://ganoro.blogspot.hu/2011/05/windows-registry-api-for-windows-plus.html
			// http://sourceforge.net/apps/trac/openwitheclipse/browser/com.ice.jni.registry
			
			// Library path ("java.library.path" system property) cannot be changed at runtime,
			// so we load app libraries manually here.
			System.load( PATH_MOD_APP_LIBS_VER.resolve( "ICE_JNIRegistry" + arch + ".dll" ).toString() );
			
			// Initializing the Registry class will fail to load its library (see reason above), but it's not a problem
			// since we already loaded it manually.
			// Initialize the Registry class here and suppress printed errors.
			final PrintStream oldErr = System.err;
			try {
				System.setErr( new PrintStream( new ByteArrayOutputStream() ) );
				Class.forName( Registry.class.getName() );
			} catch ( final Exception e ) {
				// Don't print to err: it's hijacked...
				LOGGER.error( "Unexpected error loading ICE_JNIRegistry library!", e );
			} finally {
				System.setErr( oldErr );
			}
		}
	}
	
	
	/** SC2 Textures release build info. */
	public static final BuildInfoBean      SC2_TEXTURES_BUILD_INFO     = JAXB.unmarshal( TR.get( "bean/build-info.xml" ), BuildInfoBean.class );
	
	
	/** SC2 Balance Data release build info. */
	public static final BuildInfoBean      SC2_BALANCE_DATA_BUILD_INFO = JAXB.unmarshal( BR.get( "bean/build-info.xml" ), BuildInfoBean.class );
	
	
	/** App Libs release build info. */
	public static final BuildInfoBean      APP_LIBS_BUILD_INFO         = JAXB.unmarshal( AR.get( "bean/build-info.xml" ), BuildInfoBean.class );
	
	
	/** App release build info. */
	public static final BuildInfoBean      APP_BUILD_INFO              = JAXB.unmarshal( R.get( "bean/build-info.xml" ), BuildInfoBean.class );
	
	
	/** External Module API build info. */
	public static final BuildInfoBean      EXT_MOD_API_BUILD_INFO      = JAXB.unmarshal( XR.get( "bean/build-info.xml" ), BuildInfoBean.class );
	
	
	/** App settings path. */
	public static final Path               PATH_APP_SETTINGS           = PATH_WORKSPACE.resolve( "app-settings.xml" );
	
	
	/** App settings bean. */
	public static final SettingsBean       APP_SETTINGS;
	static {
		// First backup settings
		if ( Files.exists( PATH_APP_SETTINGS ) )
			try {
				Files.copy( PATH_APP_SETTINGS, PATH_APP_SETTINGS.resolveSibling( PATH_APP_SETTINGS.getFileName() + "_" ), StandardCopyOption.REPLACE_EXISTING );
			} catch ( final IOException ie ) {
				LEnv.LOGGER.warning( "Failed to backup " + Consts.APP_NAME + " settings!", ie );
			}
		
		// Ensure the class listing the launcher settings is loaded and initialized, so those settings will be found (by their full id).
		try {
			Class.forName( Settings.class.getName() );
		} catch ( final ClassNotFoundException cnfe ) {
			// Never to happen.
		}
		
		SettingsBean settings = null;
		try {
			settings = JAXB.unmarshal( PATH_APP_SETTINGS.toFile(), SettingsBean.class );
		} catch ( final Exception e ) {
			LOGGER.warning( "Could not read " + Consts.APP_NAME + " settings, the default settings will be used: " + PATH_APP_SETTINGS );
			if ( Files.exists( PATH_APP_SETTINGS ) ) // Only log exception if file exists but failed to read it.
				LOGGER.debug( "Reason:", e );
			else
				LOGGER.debug( "Reason: File does not exist: " + PATH_APP_SETTINGS );
			
			settings = new SettingsBean();
		}
		APP_SETTINGS = settings;
		APP_SETTINGS.configureSave( Consts.APP_NAME, Consts.APP_VERSION, PATH_APP_SETTINGS );
		APP_SETTINGS.setValidSettingList( SettingsBean.createSettingListFromFields( Settings.class ) );
		
		// Default SC2 replay folder is user dependent (user home dependent)
		// Synchronize the path of the replay folder associated with the default SC2 replay folder
		RepFoldersBean rfsBean = APP_SETTINGS.get( Settings.REPLAY_FOLDERS_BEAN );
		RepFolderBean rf = rfsBean.getRepFolderForOrigin( RepFolderOrigin.DEF_SC2_REP_FOLDER );
		if ( !rf.getPath().equals( OS.getDefSc2ReplayPath() ) ) {
			LOGGER.debug( "Switching default SC2 Replay folder from \"" + rf.getPath() + "\" to \"" + OS.getDefSc2ReplayPath() + "\"" );
			// To change something in a bean setting, we have to clone it!
			rfsBean = rfsBean.cloneBean();
			rf = rfsBean.getRepFolderForOrigin( RepFolderOrigin.DEF_SC2_REP_FOLDER );
			rf.setPath( OS.getDefSc2ReplayPath() );
			APP_SETTINGS.set( Settings.REPLAY_FOLDERS_BEAN, rfsBean );
		}
		
		if ( APP_SETTINGS.getSaveTime() == null )
			APP_SETTINGS.save();
	}
	
	
	/** Map Download Manager service. */
	public static final MapDownloadManager MAP_DOWNLOAD_MANAGER        = new MapDownloadManager();
	
	
	/** Watcher service. */
	public static final Watcher            WATCHER;
	static {
		Watcher w = null;
		try {
			w = new Watcher();
		} catch ( final IOException ie ) {
			LOGGER.error( "Could not obtain native IO watcher service!", ie );
		}
		WATCHER = w;
		if ( WATCHER != null )
			WATCHER.start();
	}
	
	
	/** SC2 registry monitor. */
	public static final Sc2RegMonitor      SC2_REG_MONITOR;
	static {
		if ( Sc2RegMonitor.isSupported() ) {
			SC2_REG_MONITOR = new Sc2RegMonitor();
			SC2_REG_MONITOR.start();
		} else
			SC2_REG_MONITOR = null;
	}
	
	
	// Initialize Replay cache
	static {
		RepProcCache.getRepProc( "", null );
	}
	
	
	
	// Init everything else first and the main frame last, because if error occurs, we don't want the main frame to appear!
	
	/** Application main frame. */
	public static final MainFrame          MAIN_FRAME                  = new MainFrame();
	static {
		CURRENT_GUI_FRAME.set( MAIN_FRAME );
	}
	
	
	/** Instance monitor. */
	public static final InstanceMonitor    INSTANCE_MONITOR            = new InstanceMonitor();
	static {
		INSTANCE_MONITOR.start();
	}
	
	
	// EXTERNAL MODULE MANAGER SHOULD BE THE LAST (AS EXT MODS MIGHT USE SERVICES DEFINED/STARTED BEFORE)
	
	/** External module manager. */
	public static final ExtModManager      EXT_MOD_MANAGER             = new ExtModManager();
	
	
	
	/** Application start time. */
	public static final long               APPLICATION_START           = System.currentTimeMillis();
	
	/**
	 * Timer to schedule automatic update checks.<br>
	 * Schedules update check in every hour. Swing timer => executes in EDT.<br>
	 * If computer is up from a sleep and elapsed time is greater than then between-event delay, the timer will fire immediately.
	 */
	private static final Timer             UPDATE_CHECKER_TIMER        = ONLINE ? new Timer( Consts.SCHEDULED_UPDATE_CHECK_DELAY, Actions.CHECK_FOR_UPDATES )
	                                                                           : null;
	static {
		if ( UPDATE_CHECKER_TIMER != null ) {
			// We want to fire immediately
			UPDATE_CHECKER_TIMER.setInitialDelay( 0 );
			UPDATE_CHECKER_TIMER.start();
		}
	}
	
}
