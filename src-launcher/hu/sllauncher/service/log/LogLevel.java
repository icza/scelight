/*
 * Project Abdra Commander
 * 
 * Copyright (c) 2012 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.sllauncher.service.log;

import hu.scelightapibase.util.gui.HasIcon;
import hu.sllauncher.gui.comp.ColorIcon;

import java.awt.Color;
import java.util.logging.Level;

import javax.swing.Icon;

/**
 * Used log {@link Level levels}.
 * 
 * @author Andras Belicza
 */
public enum LogLevel implements HasIcon {
	
	// The logged value must not be changed in the future, else previous logs will not be parseable!
	
	/** ALL, {@link Level#ALL}. */
	ALL( Level.ALL, "ALL", 'A', new Color( 255, 255, 255 ) ),
	
	/** Trace, e.g. variables/beans or program state. Associated with {@link Level#FINE}. */
	TRACE( Level.FINE, "TRACE", 'T', new Color( 255, 144, 255 ) ),
	
	/** Debug, e.g. what is happening in the background. Associated with {@link Level#CONFIG}. */
	DEBUG( Level.CONFIG, "DEBUG", 'D', new Color( 168, 168, 255 ) ),
	
	/** Info, e.g. results of visible jobs and tasks. Associated with {@link Level#INFO}. */
	INFO( Level.INFO, "INFO", 'I', new Color( 204, 204, 204 ) ),
	
	/** Warning, e.g. potential problems or malfunctioning. Associated with {@link Level#WARNING}. */
	WARNING( Level.WARNING, "WARNING", 'W', new Color( 255, 255, 128 ) ),
	
	/** Error, e.g. something is not right, not how/what it should be. Associated with {@link Level#SEVERE}. */
	ERROR( Level.SEVERE, "ERROR", 'E', new Color( 255, 112, 112 ) ),
	
	/** None, {@link Level#OFF}. */
	NONE( Level.OFF, "NONE", 'N', new Color( 0, 0, 0 ) );
	
	
	/** The associated {@link Level}. */
	public final Level  level;
	
	/** Text value of the log level. */
	public final String text;
	
	/** Logged value of the level. */
	public final char   loggedValue;
	
	/** Color of the level. */
	public final Color  color;
	
	/** An icon which is filled with the color. */
	public final Icon   icon;
	
	/**
	 * Creates a new {@link LogLevel}.
	 * 
	 * @param level the associated {@link Level}
	 * @param text text value of the log level, without HTML formatting
	 * @param loggedValue logged value of the level
	 * @param color color of the log level
	 */
	private LogLevel( final Level level, final String text, final char loggedValue, final Color color ) {
		this.level = level;
		this.text = text;
		this.loggedValue = loggedValue;
		this.color = color;
		icon = new ColorIcon( color, text );
	}
	
	@Override
	public Icon getIcon() {
		return icon;
	}
	
	@Override
	public String toString() {
		return text;
	}
	
	
	/** Cache of the values array. */
	public static final LogLevel[] VALUES = values();
	
}
