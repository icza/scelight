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

import hu.scelightapibase.util.iface.Func;
import hu.sllauncher.util.LUtils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;

/**
 * Log record formatter.
 * 
 * @author Andras Belicza
 */
public class LogFormatter extends Formatter {
	
	// Date pattern and separator strings cannot be changed in the future else old logs will not be parseable!
	
	/** Date format pattern used in record formatting. */
	public static final String                DATE_FORMAT_PATTERN = "yyMMdd HHmmss.SSS";
	
	
	/** Date+time format. */
	private static final DateFormat           DATE_FORMAT         = new SimpleDateFormat( DATE_FORMAT_PATTERN );
	
	/** Newline separator string. */
	public static final String                NEW_LINE            = System.getProperty( "line.separator" );
	
	/** Logged values of log levels pre- and post-pended with separator strings. */
	private static final String[]             LOG_LEVEL_VALUES    = new String[ LogLevel.VALUES.length ];
	
	/** A map from level to logged level value. */
	private static final Map< Level, String > LEVEL_NAME_MAP      = LUtils.newHashMap( LogLevel.VALUES.length );
	
	static {
		for ( final LogLevel logLevel : LogLevel.VALUES ) {
			LOG_LEVEL_VALUES[ logLevel.ordinal() ] = " " + logLevel.loggedValue + " ";
			LEVEL_NAME_MAP.put( logLevel.level, LOG_LEVEL_VALUES[ logLevel.ordinal() ] );
		}
	}
	
	
	/** Date to be formatted. */
	private final Date                        date                = new Date( 0 );
	
	/** Builder to build the output. */
	private final StringBuilder               builder             = new StringBuilder();
	
	/** String buffer used to format dates, for better performance. */
	private final StringBuffer                dateStringBuffer    = new StringBuffer();
	
	/** Field position required by the date formatting. */
	// Use the year field because it is the first in our pattern, so field position will be handled early and cost of
	// further checks will be minimized.
	private final FieldPosition               dfFieldPosition     = new FieldPosition( DateFormat.YEAR_FIELD );
	
	@Override
	public synchronized String format( final LogRecord record ) {
		date.setTime( record.getMillis() );
		builder.setLength( 0 );
		
		dateStringBuffer.setLength( 0 );
		builder.append( DATE_FORMAT.format( date, dateStringBuffer, dfFieldPosition ) ).append( LEVEL_NAME_MAP.get( record.getLevel() ) );
		
		builder.append( record.getMessage() ).append( NEW_LINE );
		
		final Throwable thrown = record.getThrown();
		if ( thrown != null ) {
			final StringWriter sw = new StringWriter();
			try ( final PrintWriter pw = new PrintWriter( sw ) ) {
				thrown.printStackTrace( pw );
			}
			builder.append( sw.toString() );
		}
		
		return builder.toString();
	}
	
	/**
	 * Returns a log parser which is capable of parsing the formatted log lines (produced by {@link #format(LogRecord)} ).
	 * 
	 * <p>
	 * The returned parser's {@link Func#exec(Object) run()} method accepts log lines, and returns a parsed log record if the line could be parsed, or
	 * <code>null</code> otherwise.
	 * </p>
	 * 
	 * <b>Notes:</b>
	 * <ol>
	 * <li>For performance reasons the same log record instance is returned each time apply() is called and a line is successfully parsed.
	 * <li>Since not all fields might be written to the formatted record, therefore not all fields might be parsed back and set in the record. Fields that are
	 * not parsed leaved untouched.<br>
	 * The log level ({@link LogRecord#getLevel()}), the time ({@link LogRecord#getMillis()}) and the message ( {@link LogRecord#getMessage()}) is guaranteed to
	 * be parsed out.
	 * <li>If a message was formatted into multiple lines (e.g. contained an exception), only the first one will be parsable by the returned parser, parsing the
	 * subsequent lines will return <code>null</code> which indicates that the whole log line is part of the previous record's message.
	 * </ol>
	 * 
	 * <p>
	 * The returned parser is not thread-safe, should only be used by one thread.
	 * </p>
	 * 
	 * @return a log parser which is capable of parsing the formatted log lines
	 */
	public static Func< String, LoggedRecord > getParser() {
		return new Func< String, LoggedRecord >() {
			private final DateFormat    df = new SimpleDateFormat( DATE_FORMAT_PATTERN );
			
			private final LoggedRecord  lr = new LoggedRecord();
			
			private final ParsePosition pp = new ParsePosition( 0 );
			
			@Override
			public LoggedRecord exec( final String line ) {
				try {
					// Parse time
					pp.setIndex( 0 );
					final Date d = df.parse( line, pp );
					if ( d == null )
						return null;
					lr.time = d;
					
					// Parse level
					LogLevel l = null;
					final int pos = pp.getIndex();
					int i;
					for ( i = 0; i < LOG_LEVEL_VALUES.length; i++ )
						if ( line.startsWith( LOG_LEVEL_VALUES[ i ], pos ) ) {
							l = LogLevel.VALUES[ i ];
							break;
						}
					if ( l == null )
						return null;
					lr.logLevel = l;
					
					// Rest is the message
					lr.message = line.substring( pos + LOG_LEVEL_VALUES[ i ].length() );
				} catch ( final Exception e ) {
					return null;
				}
				return lr;
			}
		};
	}
	
}
