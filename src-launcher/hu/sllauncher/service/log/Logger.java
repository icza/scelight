/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.sllauncher.service.log;

import hu.scelightapibase.service.log.ILogger;
import hu.sllauncher.ScelightLauncher;
import hu.sllauncher.service.env.LEnv;
import hu.sllauncher.service.settings.LSettings;
import hu.sllauncher.util.LUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.FileVisitOption;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.EnumSet;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.StreamHandler;

/**
 * Launcher and application logger.
 * 
 * <p>
 * Log methods dispatch to an internal {@link java.util.logging.Logger} which is thread-safe, so we're thread safe too.
 * </p>
 * 
 * @author Andras Belicza
 */
public class Logger implements ILogger {
	
	/** Extension of log files starting with a dot (<code>'.'</code>). */
	public static final String             LOG_EXTENSION = ".log";
	
	/** Current log file name to be used. Without extension. */
	public static final String             LOG_FILE_NAME = new SimpleDateFormat( "yyyy-MM-dd HH_mm_ss" ).format( new Date() ) + LOG_EXTENSION;
	
	
	/** Formatter to format log records. */
	private final LogFormatter             formatter     = new LogFormatter();
	
	/** java.util.logging.Logger. */
	private final java.util.logging.Logger logger;
	
	/** Path of the active log file. */
	public final Path                      activeLogPath;
	
	/** Current log level. */
	private LogLevel                       logLevel;
	
	/**
	 * Creates a new {@link Logger}.
	 */
	public Logger() {
		logger = java.util.logging.Logger.getLogger( ScelightLauncher.class.getName() );
		
		logger.setUseParentHandlers( false ); // We use our own handlers
		
		Path logPath = null;
		
		logPath = LEnv.PATH_LOGS.resolve( LOG_FILE_NAME );
		try {
			Files.createDirectories( LEnv.PATH_LOGS );
			logger.addHandler( new FileHandler( logPath.toString() ) );
		} catch ( final Exception e ) {
			System.err.println( "Failed to setup file logger, reverting to console logging: " + logPath );
			e.printStackTrace();
			logPath = null;
		}
		activeLogPath = logPath;
		// In dev mode also log to console. If file logger cannot be set up, at least log to console.
		if ( LEnv.DEV_MODE || logPath == null )
			logger.addHandler( new ConsoleHandler() );
		
		for ( final Handler h : logger.getHandlers() ) {
			h.setFormatter( formatter );
			
			if ( h instanceof StreamHandler ) {
				try {
					( (StreamHandler) h ).setEncoding( LEnv.UTF8.name() );
				} catch ( final SecurityException | UnsupportedEncodingException e ) {
					// Never to happen
					e.printStackTrace();
				}
			}
		}
		
		setLogLevel( LEnv.DEV_MODE ? LogLevel.TRACE : LogLevel.DEBUG );
	}
	
	/**
	 * Deletes old log files which have become too old.<br>
	 * Uses {@link LSettings#LOG_FILES_LIFETIME} setting to determine which files are classified old (this means it can only be called once launcher settings
	 * are loaded).
	 */
	public void deleteOldLogFiles() {
		if ( !Files.exists( LEnv.PATH_LOGS ) )
			return;
		
		final long oldestMs = System.currentTimeMillis() - LUtils.MS_IN_DAY * LEnv.LAUNCHER_SETTINGS.get( LSettings.LOG_FILES_LIFETIME );
		
		try {
			Files.walkFileTree( LEnv.PATH_LOGS, EnumSet.noneOf( FileVisitOption.class ), 1, new SimpleFileVisitor< Path >() {
				@Override
				public FileVisitResult visitFile( final Path file, final BasicFileAttributes attrs ) throws IOException {
					if ( attrs.isDirectory() )
						return FileVisitResult.CONTINUE;
					
					final String fileName = file.getFileName().toString();
					// We save logs in *.log files, Java logger also uses *.lck lock files
					// which remain if VM is terminated abruptly.
					final boolean isLock = fileName.endsWith( ".lck" );
					if ( !fileName.endsWith( LOG_EXTENSION ) && !isLock )
						return FileVisitResult.CONTINUE;
					
					if ( isLock || attrs.lastModifiedTime().toMillis() < oldestMs )
						try {
							Files.delete( file );
						} catch ( final IOException e ) {
							// Attempting to delete the lock file of the active log path will obviously fail,
							// do not log that.
							if ( !isLock )
								LEnv.LOGGER.warning( "Could not delete old log file: " + file, e );
						}
					
					return FileVisitResult.CONTINUE;
				}
			} );
		} catch ( final IOException e ) {
			LEnv.LOGGER.warning( "Error while deleting old log files!", e );
		}
	}
	
	/**
	 * Sets the specified {@link LogLevel}.
	 * 
	 * @param logLevel log level to be set
	 */
	public synchronized void setLogLevel( final LogLevel logLevel ) {
		this.logLevel = logLevel;
		
		logger.setLevel( logLevel.level );
		
		for ( final Handler h : logger.getHandlers() )
			h.setLevel( logLevel.level );
	}
	
	/**
	 * Logs a message with the specified log level.
	 * 
	 * @param logLevel log level for the log
	 * @param msg message to be logged
	 */
	public void log( final LogLevel logLevel, final String msg ) {
		logger.log( logLevel.level, msg );
	}
	
	/**
	 * Logs a message with the specified log level.
	 * 
	 * @param logLevel log level for the log
	 * @param msg message to be logged
	 * @param t exception to be logged
	 */
	public void log( final LogLevel logLevel, final String msg, final Throwable t ) {
		logger.log( logLevel.level, msg, t );
	}
	
	@Override
	public void error( final String msg ) {
		logger.log( LogLevel.ERROR.level, msg );
	}
	
	@Override
	public void error( final String msg, final Throwable t ) {
		logger.log( LogLevel.ERROR.level, msg, t );
	}
	
	@Override
	public void warning( final String msg ) {
		logger.log( LogLevel.WARNING.level, msg );
	}
	
	@Override
	public void warning( final String msg, final Throwable t ) {
		logger.log( LogLevel.WARNING.level, msg, t );
	}
	
	@Override
	public void info( final String msg ) {
		logger.log( LogLevel.INFO.level, msg );
	}
	
	@Override
	public void info( final String msg, final Throwable t ) {
		logger.log( LogLevel.INFO.level, msg, t );
	}
	
	@Override
	public void debug( final String msg ) {
		logger.log( LogLevel.DEBUG.level, msg );
	}
	
	@Override
	public void debug( final String msg, final Throwable t ) {
		logger.log( LogLevel.DEBUG.level, msg, t );
	}
	
	@Override
	public void trace( final String msg ) {
		logger.log( LogLevel.TRACE.level, msg );
	}
	
	@Override
	public void trace( final String msg, final Throwable t ) {
		logger.log( LogLevel.TRACE.level, msg, t );
	}
	
	/**
	 * Tests if current log level is low enough for {@link LogLevel#TRACE} messages to be logged.
	 * <p>
	 * If this method returns false, then {@link LogLevel#TRACE} message logs will be discarded.
	 * </p>
	 * 
	 * <p>
	 * Significance of this method is that if a {@link LogLevel#TRACE} message is expensive to build, it's recommended to first check if it will eventually be
	 * logged, and if not, it should be avoided to build it.
	 * </p>
	 * 
	 * @return true if {@link LogLevel#TRACE} messages are logged; false otherwise
	 */
	@Override
	public boolean testTrace() {
		return logLevel.compareTo( LogLevel.TRACE ) <= 0;
	}
	
	/**
	 * Tests if current log level is low enough for {@link LogLevel#DEBUG} messages to be logged.
	 * <p>
	 * If this method returns false, then {@link LogLevel#DEBUG} message logs will be discarded.
	 * </p>
	 * 
	 * <p>
	 * Significance of this method is that if a {@link LogLevel#DEBUG} message is expensive to build, it's recommended to first check if it will eventually be
	 * logged, and if not, it should be avoided to build it.
	 * </p>
	 * 
	 * @return true if {@link LogLevel#DEBUG} messages are logged; false otherwise
	 */
	@Override
	public boolean testDebug() {
		return logLevel.compareTo( LogLevel.DEBUG ) <= 0;
	}
	
}
