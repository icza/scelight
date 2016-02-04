/*
 * Project Abdra Commander
 * 
 * Copyright (c) 2012 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.sllauncher.service.lang;

/**
 * Predefined person name format.
 * 
 * @author Andras Belicza
 */
public enum PersonNameFormat {
	
	/** <code>First Middle "Nick" Last</code> */
	FIRST_MIDDLE_NICK_LAST( "First Middle \"Nick\" Last" ),
	
	/** <code>Last "Nick" First Middle</code> */
	LAST_NICK_FIRST_MIDDLE( "Last \"Nick\" First Middle" ),
	
	
	/** <code>First Middle Last "Nick"</code> */
	FIRST_MIDDLE_LAST_NICK( "First Middle Last \"Nick\"" ),
	
	/** <code>First Middle "Nick" Last</code> */
	FIRST_NICK_MIDDLE_LAST( "First \"Nick\" Middle Last" ),
	
	
	/** <code>Last Middle "Nick" First</code> */
	LAST_MIDDLE_NICK_FIRST( "Last Middle \"Nick\" First" ),
	
	/** <code>Last First Middle "Nick"</code> */
	LAST_FIRST_MIDDLE_NICK( "Last First Middle \"Nick\"" ),
	
	/** <code>Last Middle First "Nick"</code> */
	LAST_MIDDLE_FIRST_NICK( "Last Middle First \"Nick\"" ),
	
	/** <code>First Middle "Nick" Last</code> */
	LAST_NICK_MIDDLE_FIRST( "Last \"Nick\" Middle First" ),
	
	
	/** <code>"Nick" First Middle Last</code> */
	NICK_FIRST_MIDDLE_LAST( "\"Nick\" First Middle Last" ),
	
	/** <code>"Nick" Last First Middle</code> */
	NICK_LAST_FIRST_MIDDLE( "\"Nick\" Last First Middle" ),
	
	/** <code>"Nick" First Last Middle</code> */
	NICK_FIRST_LAST_MIDDLE( "\"Nick\" First Last Middle" ),
	
	/** <code>"Nick" Last Middle First</code> */
	NICK_LAST_MIDDLE_FIRST( "\"Nick\" Last Middle First" );
	
	
	/** Text value of the person name format. */
	public final String text;
	
	/** String value of the person name format. */
	public final String stringValue;
	
	/** Index of the first name (in the range of 0..3). */
	public final int    firstIdx;
	
	/** Index of the middle name (in the range of 0..3). */
	public final int    middleIdx;
	
	/** Index of the nick name (in the range of 0..3). */
	public final int    nickIdx;
	
	/** Index of the last name (in the range of 0..3). */
	public final int    lastIdx;
	
	
	/**
	 * Creates a new {@link PersonNameFormat}.
	 * 
	 * @param text text value
	 */
	private PersonNameFormat( String text ) {
		this.text = text;
		stringValue = "<html>" + text.replace( "\"Nick\"", "<i>\"Nick\"</i>" ) + "</html>";
		
		int firstIdx = -1, middleIdx = -1, nickIdx = -1, lastIdx = -1;
		int fromTextIdx = 0;
		for ( int i = 0; i < 4; i++ ) {
			if ( text.startsWith( "First", fromTextIdx ) )
				firstIdx = i;
			else if ( text.startsWith( "Middle", fromTextIdx ) )
				middleIdx = i;
			else if ( text.startsWith( "\"Nick\"", fromTextIdx ) )
				nickIdx = i;
			else if ( text.startsWith( "Last", fromTextIdx ) )
				lastIdx = i;
			else
				throw new RuntimeException( "Invalid Person name format: " + text );
			
			fromTextIdx = text.indexOf( ' ', fromTextIdx ) + 1;
		}
		
		this.firstIdx = firstIdx;
		this.middleIdx = middleIdx;
		this.nickIdx = nickIdx;
		this.lastIdx = lastIdx;
	}
	
	@Override
	public String toString() {
		return stringValue;
	}
	
}
