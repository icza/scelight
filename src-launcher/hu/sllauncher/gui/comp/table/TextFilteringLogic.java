/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.sllauncher.gui.comp.table;

import hu.sllauncher.util.LUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class to provide case-insensitive text filtering logic based on filter criteria.
 * <p>
 * Supports applying include and exclude filters where the exclude filter has higher priority.<br>
 * The filter texts can contain logical operators: "OR" and "AND". Multiple words are connected with AND if not specified.
 * </p>
 * 
 * @author Andras Belicza
 */
class TextFilteringLogic {
	
	/** Include filter groups to apply. */
	private final String[][] incFilterGroups;
	
	/** Exclude filter groups to apply. */
	private final String[][] excFilterGroups;
	
	/**
	 * Creates a new {@link TextFilteringLogic}.
	 * 
	 * @param incFilterText include filter text
	 * @param excFilterText exclude filter text
	 */
	public TextFilteringLogic( final String incFilterText, final String excFilterText ) {
		incFilterGroups = createFilterGroups( incFilterText );
		excFilterGroups = createFilterGroups( excFilterText );
	}
	
	/**
	 * Tells if filters were specified (the include filter or the exclude filter is active).
	 * 
	 * @return true if filters were specified; false otherwise
	 */
	public boolean isFilterSpecified() {
		return incFilterGroups != null || excFilterGroups != null;
	}
	
	/**
	 * Tells if the include filter is active.
	 * 
	 * @return true if the include filter is active; false otherwise
	 */
	public boolean isIncludeFilterActive() {
		return incFilterGroups != null;
	}
	
	/**
	 * Tells if the exclude filter is active.
	 * 
	 * @return true if the exclude filter is active; false otherwise
	 */
	public boolean isExcludeFilterActive() {
		return excFilterGroups != null;
	}
	
	/**
	 * Tests if the specified text is included by the filters.
	 * 
	 * @param text text to be tested; no need to be lower cased
	 * @return true if the specified text is included by the filters; false otherwise
	 */
	public boolean isIncluded( final String text ) {
		// First check the exclude filter
		if ( excFilterGroups != null ) {
			for ( final String[] filterOutGroup : excFilterGroups ) {
				boolean filterGroupApplies = true;
				
				for ( final String filter : filterOutGroup )
					if ( !LUtils.containsIngoreCase( text, filter ) ) {
						filterGroupApplies = false;
						break;
					}
				
				if ( filterGroupApplies )
					return false;
			}
		}
		
		// If still in business, check the filter include filter
		if ( incFilterGroups != null ) {
			for ( final String[] filterGroup : incFilterGroups ) {
				boolean filterGroupApplies = true;
				
				for ( final String filter : filterGroup )
					if ( !LUtils.containsIngoreCase( text, filter ) ) {
						filterGroupApplies = false;
						break;
					}
				
				if ( filterGroupApplies )
					return true;
			}
			
			// None of the filters applied:
			return false;
		}
		
		// None of the filters is active:
		return true;
	}
	
	/**
	 * Creates the text filter groups from the specified filter text.
	 * <p>
	 * Words in the filter text are connected with logical AND condition by default even it it's not written explicitly. Logical "and" and "or" can be provided
	 * (case in-sensitive).
	 * </p>
	 * 
	 * <p>
	 * Filters in a group are connected with logical AND condition, and the groups are connected with logical OR condition.
	 * </p>
	 * 
	 * <p>
	 * Exact phrases which contain spaces or the words 'or' 'and' must be put in quotes.
	 * </p>
	 * 
	 * @param filterText filter text to create filter groups from
	 * @return the filter group created from <code>filterText</code>; or <code>null</code> if no filter is specified by the filter text
	 */
	private static String[][] createFilterGroups( String filterText ) {
		if ( filterText == null )
			return null;
		filterText = filterText.trim();
		if ( filterText.isEmpty() )
			return null;
		
		final List< List< String > > filterGroupList = new ArrayList<>();
		
		// First broke into tokens. Can't use StringTokenizer because I want to allow quoted phrases.
		filterText = filterText.toLowerCase().replace( '\t', ' ' );
		final List< String > tokenList = new ArrayList<>();
		char ch = 0;
		for ( int i = 0, j; i < filterText.length(); i++ ) {
			// Skip multiple space separators
			while ( i < filterText.length() && ( ch = filterText.charAt( i ) ) == ' ' )
				i++;
			if ( i >= filterText.length() )
				break;
			// Find end of current token
			if ( ch == '"' ) {
				// Quoted token
				int finishIdx = filterText.indexOf( '"', i + 1 );
				if ( finishIdx < 0 )
					finishIdx = filterText.length(); // No closing quote, consider all rest as the token
				tokenList.add( filterText.substring( i + 1, finishIdx ) );
				i = finishIdx;
			} else {
				// Normal (non-quoted) token
				for ( j = i + 1; j < filterText.length(); j++ ) {
					ch = filterText.charAt( j );
					if ( ch == '"' || ch == ' ' )
						break;
				}
				// Separate quoted and non-quoted OR and AND words by upper-casing non-quoted ones:
				String s = filterText.substring( i, j );
				if ( "or".equals( s ) )
					s = "OR";
				else if ( "and".equals( s ) )
					s = "AND";
				tokenList.add( s );
				i = j;
			}
		}
		
		List< String > filterGroup = null;
		for ( final String filterToken : tokenList ) {
			if ( filterToken.equals( "OR" ) ) {
				// Next token is in a new filter group
				filterGroup = null;
			} else if ( filterToken.equals( "AND" ) ) {
				// Do nothing, next token is in the same group
			} else {
				if ( filterGroup == null ) {
					filterGroup = new ArrayList<>( 2 );
					filterGroupList.add( filterGroup );
				}
				filterGroup.add( filterToken );
			}
		}
		
		final String[][] filterGroups = new String[ filterGroupList.size() ][];
		for ( int i = 0; i < filterGroups.length; i++ )
			filterGroups[ i ] = filterGroupList.get( i ).toArray( new String[ filterGroupList.get( i ).size() ] );
		
		return filterGroups;
	}
	
}
