/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelightapibase.service.log;

/**
 * Logger interface.
 * 
 * @author Andras Belicza
 */
public interface ILogger {
	
	/**
	 * Logs an error message.
	 * 
	 * @param msg message to be logged
	 */
	void error( String msg );
	
	/**
	 * Logs an error message.
	 * 
	 * @param msg message to be logged
	 * @param t exception to be logged
	 */
	void error( String msg, Throwable t );
	
	/**
	 * Logs a warning message.
	 * 
	 * @param msg message to be logged
	 */
	void warning( String msg );
	
	/**
	 * Logs a warning message.
	 * 
	 * @param msg message to be logged
	 * @param t exception to be logged
	 */
	void warning( String msg, Throwable t );
	
	/**
	 * Logs an info message.
	 * 
	 * @param msg message to be logged
	 */
	void info( String msg );
	
	/**
	 * Logs an info message.
	 * 
	 * @param msg message to be logged
	 * @param t exception to be logged
	 */
	void info( String msg, Throwable t );
	
	/**
	 * Logs a debug message.
	 * 
	 * @param msg message to be logged
	 * 
	 * @see #testDebug()
	 */
	void debug( String msg );
	
	/**
	 * Logs a debug message.
	 * 
	 * @param msg message to be logged
	 * @param t exception to be logged
	 * 
	 * @see #testDebug()
	 */
	void debug( String msg, Throwable t );
	
	/**
	 * Logs a trace message.
	 * 
	 * @param msg message to be logged
	 * 
	 * @see #testTrace()
	 */
	void trace( String msg );
	
	/**
	 * Logs a trace message.
	 * 
	 * @param msg message to be logged
	 * @param t exception to be logged
	 * 
	 * @see #testTrace()
	 */
	void trace( String msg, Throwable t );
	
	/**
	 * Tests if current log level is low enough for <code>TRACE</code> messages to be logged.
	 * <p>
	 * If this method returns false, then <code>TRACE</code> message logs will be discarded.
	 * </p>
	 * 
	 * <p>
	 * Purpose of this method is that if a <code>TRACE</code> message is expensive to build, it's recommended to first check if it will eventually be logged,
	 * and if not, it should be avoided to build it.
	 * </p>
	 * 
	 * @return true if <code>TRACE</code> messages are logged; false otherwise
	 */
	boolean testTrace();
	
	/**
	 * Tests if current log level is low enough for <code>DEBUG</code> messages to be logged.
	 * <p>
	 * If this method returns false, then <code>DEBUG</code> message logs will be discarded.
	 * </p>
	 * 
	 * <p>
	 * Purpose of this method is that if a <code>DEBUG</code> message is expensive to build, it's recommended to first check if it will eventually be logged,
	 * and if not, it should be avoided to build it.
	 * </p>
	 * 
	 * @return true if <code>DEBUG</code> messages are logged; false otherwise
	 */
	boolean testDebug();
	
}
