/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.sllauncher;

import hu.scelightapibase.bean.settings.ISettingChangeListener;
import hu.sllauncher.bean.BuildInfoBean;
import hu.sllauncher.bean.VersionBean;
import hu.sllauncher.bean.module.FileBean;
import hu.sllauncher.bean.module.ModuleBean;
import hu.sllauncher.bean.module.ModulesBean;
import hu.sllauncher.gui.LauncherFrame;
import hu.sllauncher.gui.comp.StatusLabel.StatusType;
import hu.sllauncher.service.env.LEnv;
import hu.sllauncher.service.settings.LGlobalSettingChangeListener;
import hu.sllauncher.service.updater.Updater;
import hu.sllauncher.util.LoggerUncaughtExceptionHandler;
import hu.sllauncher.util.gui.LGuiUtils;

import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.zip.ZipFile;

import javax.xml.bind.JAXB;

/**
 * This is the launcher of the application.
 * 
 * <p>
 * Functions:
 * </p>
 * <ul>
 * <li>Displays latest news.</li>
 * <li>Acquires the modules bean.</li>
 * <li>Updates the internal modules and the enabled official external modules.</li>
 * <li>Performs modules content validation.</li>
 * <li>Starts the application.</li>
 * </ul>
 * 
 * @author Andras Belicza
 */
public class ScelightLauncher {
	
	/** Application arguments passed on from the running environment (args passed to Java are not included). */
	private static String[] arguments;
	
	/**
	 * Entry point of the launcher.
	 * 
	 * @param arguments application arguments passed on from the running environment (args passed to Java are not included)
	 */
	public static void main( final String[] arguments ) {
		ScelightLauncher.arguments = arguments;
		
		// To fix a swing bug introduced in Java 7.0
		// http://stackoverflow.com/questions/13575224/comparison-method-violates-its-general-contract-timsort-and-gridlayout
		System.setProperty( "java.util.Arrays.useLegacyMergeSort", "true" );
		
		// Reset (clear) time zone if user tried to override it (e.g. by passing a -Duser.timezone=... JVM parameter)
		System.setProperty( "user.timezone", "" );
		
		// Install an uncaught exception logger for the EDT:
		LGuiUtils.runInEDT( new Runnable() {
			@Override
			public void run() {
				// Current thread is EDT
				Thread.currentThread().setUncaughtExceptionHandler( LoggerUncaughtExceptionHandler.INSTANCE );
			}
		} );
		
		setUserAgent();
		
		final Exception e = LGuiUtils.setLaf( "Nimbus" );
		if ( e != null )
			LEnv.LOGGER.error( "Failed to set Nimbus Look and Feel!", e );
		
		new ScelightLauncher( arguments );
	}
	
	/**
	 * Returns the application arguments passed on from the running environment (args passed to Java are not included).
	 * 
	 * @return the application arguments passed on from the running environment (args passed to Java are not included)
	 */
	public static String[] getArguments() {
		return arguments;
	}
	
	/**
	 * Sets the user agent string for outgoing HTTP and HTTPS requests.
	 */
	private static void setUserAgent() {
		// User agent ("http.agent" system property) is only checked once (when first used), and cannot be changed later.
		// So I try to squeeze as much info into it now as possible => Detect and include app version too.
		
		final StringBuilder sb = new StringBuilder();
		
		// Launcher info
		sb.append( LConsts.LAUNCHER_NAME.replace( " ", "" ) ).append( '/' ).append( LConsts.LAUNCHER_VERSION.toString( true ) ).append( '.' )
		        .append( LEnv.LAUNCHER_BUILD_INFO.getBuildNumber() );
		
		// System info
		sb.append( " (" ).append( System.getProperty( "os.name" ) ).append( "; " ).append( System.getProperty( "os.version" ) ).append( "; " )
		        .append( System.getProperty( "os.arch" ) ).append( ')' );
		
		// App info
		final Path modAppPath = LEnv.PATH_MODS.resolve( "app" );
		if ( Files.exists( modAppPath ) )
			try {
				final Path latestAppVersionPath = Updater.getLatestVersionSubfolder( LEnv.PATH_MODS.resolve( "app" ) );
				
				if ( latestAppVersionPath != null ) {
					final VersionBean appVersion = VersionBean.fromString( latestAppVersionPath.getFileName().toString() );
					sb.append( ' ' ).append( LConsts.APP_NAME.replace( " ", "" ) ).append( '/' ).append( appVersion.toString( true ) );
					
					// Detect build number
					// I could simply use a URL with a spec:
					// "jar:"+latestAppVersionPath.resolve("scelight.sldat").toUri().toURL()+"!/hu/scelight/r/bbean/build-info.xml"
					// But that uses JarURLConnection which locks the file!
					try ( final ZipFile zipFile = new ZipFile( latestAppVersionPath.resolve( "scelight.sldat" ).toFile() );
					        final InputStream in = zipFile.getInputStream( zipFile.getEntry( "hu/scelight/r/bean/build-info.xml" ) ) ) {
						final BuildInfoBean appBuildInfo = JAXB.unmarshal( in, BuildInfoBean.class );
						sb.append( '.' ).append( appBuildInfo.getBuildNumber() );
					}
				}
			} catch ( final Exception e ) {
				// Do not log anything
			}
		
		System.setProperty( "http.agent", sb.toString() );
	}
	
	/** Reference to the one and only instance. */
	private static ScelightLauncher INSTANCE;
	
	/**
	 * Returns the one and only reference.
	 * 
	 * @return the one and only reference
	 */
	public static ScelightLauncher INSTANCE() {
		return INSTANCE;
	}
	
	
	/** Global (application wide) launcher setting change listener. */
	private final ISettingChangeListener   globalSettingChangeListener = new LGlobalSettingChangeListener();
	
	
	/** Action to be taken when proceed button is pressed. */
	private AtomicReference< Runnable >    proceedAction               = new AtomicReference<>();
	
	/** Modules bean. */
	private AtomicReference< ModulesBean > modules                     = new AtomicReference<>();
	
	/**
	 * Creates a new {@link ScelightLauncher} (and starts it).
	 * 
	 * @param arguments application arguments passed on from the running environment (args passed to Java are not included)
	 */
	public ScelightLauncher( final String[] arguments ) {
		if ( INSTANCE != null )
			throw new RuntimeException( "Scelight Launcher has already been instantiated!" );
		
		INSTANCE = this;
		
		// Ensure launcher environment is initialized:
		try {
			Class.forName( LEnv.class.getName() );
		} catch ( final ClassNotFoundException cne ) {
			// This should never happen, but if so, we will fail later anyway.
		}
		
		// Init launcher global settings
		LEnv.LAUNCHER_SETTINGS.addAndExecuteChangeListener( LGlobalSettingChangeListener.SETTING_SET, globalSettingChangeListener );
		
		// Construct launcher frame in the EDT, else sometimes (actually quite often)
		// a deadlock occurs (JEditorPane's HTML loading/rendering).
		LGuiUtils.runInEDT( new Runnable() {
			@Override
			public void run() {
				LEnv.CURRENT_GUI_FRAME.set( LEnv.LAUNCHER_FRAME = new LauncherFrame() );
			}
		} );
		
		// But of course launch updater outside of the EDT! (Main thread is fine for this.)
		new Updater();
	}
	
	/** Start proceed action. */
	public final Runnable PROCEED_ACTION_START   = new Runnable() {
		                                             @Override
		                                             public void run() {
			                                             start();
		                                             }
	                                             };
	
	/** Restart proceed action. */
	public final Runnable PROCEED_ACTION_RESTART = new Runnable() {
		                                             @Override
		                                             public void run() {
			                                             restart();
		                                             }
	                                             };
	
	/** Exit proceed action. */
	public final Runnable PROCEED_ACTION_EXIT    = new Runnable() {
		                                             @Override
		                                             public void run() {
			                                             exit();
		                                             }
	                                             };
	
	
	
	/**
	 * Sets the proceed action.
	 * 
	 * @param proceedAction proceed action to be set
	 */
	public void setProceedAction( final Runnable proceedAction ) {
		this.proceedAction.set( proceedAction );
	}
	
	/**
	 * Sets the modules bean.
	 * 
	 * @param modules modules bean to be set
	 */
	public void setModules( final ModulesBean modules ) {
		this.modules.set( modules );
	}
	
	/**
	 * Proceeds with the current proceed action.
	 */
	public void proceed() {
		final Runnable proceedAction = this.proceedAction.get();
		if ( proceedAction != null )
			proceedAction.run();
	}
	
	/**
	 * Starts the application.
	 */
	private void start() {
		// Note: we're in the EDT.
		
		if ( modules.get() == null ) {
			LEnv.LOGGER.error( "No modules bean, cannot proceed to Start!" );
			return;
		}
		
		try {
			// Collect class path entries from all internal mods
			final List< URL > cpUrlList = new ArrayList<>();
			for ( final ModuleBean mod : modules.get().getModList() ) {
				for ( final FileBean file : mod.getFileList() ) {
					if ( !isClassPathEntry( file ) )
						continue;
					
					Path filePath = Paths.get( file.getPath() );
					filePath = LEnv.PATH_APP.resolve( filePath.subpath( 1, filePath.getNameCount() ) );
					cpUrlList.add( filePath.toUri().toURL() );
				}
			}
			
			// The next class loader is an app-lifetime class loader, no need to ever close it,
			// so it's safe to ignore the resource leak warning.
			@SuppressWarnings( "resource" )
			final URLClassLoader ucl = new URLClassLoader( cpUrlList.toArray( new URL[ cpUrlList.size() ] ), getClass().getClassLoader() );
			
			ucl.loadClass( "hu.scelight.Scelight" ).getConstructor().newInstance();
			
			// No Exception thrown => app launch was successful.
			// Close launcher frame.
			LEnv.LAUNCHER_FRAME.close();
			
		} catch ( final Exception e ) {
			LEnv.LOGGER.error( "Failed to launch " + LConsts.APP_NAME + "!", e );
			LEnv.LAUNCHER_FRAME.setStatus( StatusType.ERROR, "Failed to start " + LConsts.APP_NAME + "! See the Logs for details! You must restart "
			        + LConsts.LAUNCHER_NAME + " to retry!" );
			
			// Disable proceed button because MainFrame is singleton, and if creating it fails (Exception),
			// we cannot allow to start again!
			LEnv.LAUNCHER_FRAME.setProceedEnabled( false );
			
			// Select the Logs page:
			LEnv.LAUNCHER_FRAME.selectLogsPage();
		}
	}
	
	/**
	 * Tells if the specified file is a class path entry.
	 * 
	 * @param file file to be tested
	 * @return true if the specified file is a class path entry; false otherwise
	 */
	public boolean isClassPathEntry( final FileBean file ) {
		return isClassPathEntry( file.getPath() );
	}
	
	/**
	 * Tells if the specified file name is a class path entry.
	 * 
	 * @param fileName file name to be tested
	 * @return true if the specified file name is a class path entry; false otherwise
	 */
	public boolean isClassPathEntry( final String fileName ) {
		return fileName.endsWith( ".sldat" ) || fileName.endsWith( ".jar" );
	}
	
	/**
	 * Restarts the launcher (app).
	 */
	private static void restart() {
		// TODO INCOMPLETE
		onExit();
		
		System.exit( 0 );
	}
	
	/**
	 * Exits from the launcher (app).
	 */
	public void exit() {
		onExit();
		
		System.exit( 0 );
	}
	
	/**
	 * Called before exit to properly end/finish pending jobs and do things that must be done before shut down (e.g. save settings).
	 */
	private static void onExit() {
		// No need to save boot settings (they can only be modified in the Settings dialog which saves them when applied).
		LEnv.LAUNCHER_SETTINGS.save();
	}
	
	
	
	/**
	 * Returns the modules bean.
	 * 
	 * @return the modules bean
	 */
	public ModulesBean getModules() {
		return modules.get();
	}
	
}
