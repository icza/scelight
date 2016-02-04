/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight.sc2.rep.s2prot;

import hu.belicza.andras.util.StructView;
import hu.belicza.andras.util.type.XString;
import hu.scelight.gui.icon.Icons;
import hu.scelight.util.Utils;
import hu.scelightapi.sc2.rep.model.IEvent;
import hu.scelightapi.sc2.rep.repproc.IRepProcessor;
import hu.scelightapibase.gui.icon.IRIcon;
import hu.sllauncher.util.Pair;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * Base event type.
 * 
 * @author Andras Belicza
 */
public class Event extends StructView implements IEvent {
	
	/** Set of general field names. */
	private static final Set< String > GENERAL_FIELD_NAMES = Utils.asNewSet( F_NAME, F_ID, F_LOOP, F_PLAYER_ID, F_USER_ID );
	
	
	/** Id of the event. */
	public final int                   id;
	
	/** Name of the event. */
	public final String                name;
	
	/** Game loop when the event occurred. */
	public final int                   loop;
	
	/** User id causing the event. */
	public final int                   userId;
	
	/**
	 * Creates a new {@link Event}.
	 * 
	 * @param struct event data structure
	 * @param id id of the event
	 * @param name type name of the event
	 * @param loop game loop when the event occurred
	 * @param userId user id causing the event
	 */
	public Event( final Map< String, Object > struct, final int id, final String name, final int loop, final int userId ) {
		super( struct );
		
		this.id = id;
		this.name = name;
		this.loop = loop;
		this.userId = userId;
	}
	
	@Override
	public int getId() {
		return id;
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public int getLoop() {
		return loop;
	}
	
	@Override
	public Integer getPlayerId() {
		return get( F_PLAYER_ID );
	}
	
	@Override
	public int getUserId() {
		return userId;
	}
	
	/**
	 * Default implementation, returns the empty icon. To be overridden.
	 */
	@Override
	public IRIcon getRicon() {
		return Icons.MY_EMPTY;
	}
	
	/**
	 * This default implementation returns the raw parameters ({@link #getRawParameters()}). To be overridden in subclasses.
	 */
	@Override
	public String getParameters( final IRepProcessor repProc ) {
		return getRawParameters();
	}
	
	@Override
	public String getRawParameters() {
		final StringBuilder sb = new StringBuilder();
		
		for ( final Entry< String, Object > entry : struct.entrySet() ) {
			if ( GENERAL_FIELD_NAMES.contains( entry.getKey() ) || entry.getValue() == null )
				continue;
			sb.append( entry.getKey() ).append( '=' );
			appendValue( sb, entry.getValue() );
		}
		
		return sb.toString();
	}
	
	/**
	 * Appends the specified object to the specified string builder.
	 * 
	 * @param sb string builder to append to
	 * @param value object to be appended
	 */
	@SuppressWarnings( "unchecked" )
	protected static void appendValue( final StringBuilder sb, final Object value ) {
		if ( value instanceof Map ) {
			sb.append( '{' );
			for ( final Entry< Object, Object > entry : ( (Map< Object, Object >) value ).entrySet() ) {
				if ( entry.getValue() == null )
					continue;
				sb.append( entry.getKey() ).append( '=' );
				appendValue( sb, entry.getValue() );
			}
			sb.append( "}; " );
		} else if ( value instanceof Object[] ) {
			sb.append( '[' );
			for ( final Object e : (Object[]) value )
				appendValue( sb, e ); // This will terminate/separate with a semicolon hence no comma is appended
			sb.append( "]; " );
		} else if ( value instanceof StructView ) {
			appendValue( sb, ( (StructView) value ).getStruct() );
		} else if ( value instanceof Pair ) {
			sb.append( ( (Pair< String, Object >) value ).value1 ).append( '=' );
			appendValue( sb, ( (Pair< String, Object >) value ).value2 );
		} else if ( value instanceof XString ) {
			// Remove zero characters else the text is not copiable (copy terminates when encountering a zero char)
			final String s = value.toString();
			sb.append( s.indexOf( '\0' ) >= 0 ? s.replace( "\0", "" ) : s ).append( "; " );
		} else
			sb.append( value ).append( "; " );
	}
	
}
