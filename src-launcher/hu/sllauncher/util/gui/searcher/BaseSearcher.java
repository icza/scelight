/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.sllauncher.util.gui.searcher;

import hu.sllauncher.gui.comp.TextSearchComp;
import hu.sllauncher.service.sound.Sound;

/**
 * A general, generic base {@link Searcher} implementation.
 * 
 * @param <P> type of the search position
 * 
 * @author Andras Belicza
 */
public abstract class BaseSearcher< P > implements Searcher {
	
	/** Text being searched. */
	protected String  searchText;
	
	
	/** Tells if the current search is performed forward. */
	protected boolean forward;
	
	/** Tells if last search match must be searched/included in the current search. */
	protected boolean searchLastMatch;
	
	/** Cached first position. */
	protected P       firstPos;
	
	/** Cached last position. */
	protected P       lastPos;
	
	/** Last searched position. */
	protected P       lastSearchedPos;
	
	/** Current search position during a search (during performing a search). */
	protected P       searchPos;
	
	/**
	 * Clears the last search position.
	 */
	public void clearLastSearchPos() {
		lastSearchedPos = null;
	}
	
	@Override
	public void performSearch( final TextSearchComp searchComp, boolean forward, boolean searchLastMatch ) {
		searchText = searchComp.textField.getText().toLowerCase();
		
		if ( searchText.isEmpty() )
			return;
		
		this.forward = forward;
		this.searchLastMatch = searchLastMatch;
		
		prepareNew();
		
		if ( lastSearchedPos == null ) {
			lastSearchedPos = forward ? lastPos : firstPos;
			searchLastMatch = false;
		}
		
		if ( lastSearchedPos == null )
			return; // No content to search
			
		searchPos = getStartPos();
		if ( searchPos == null )
			searchPos = lastSearchedPos;
		else
			lastSearchedPos = searchPos;
		
		if ( searchLastMatch ) {
			// The last search match has to be searched again, so step back 1 in the proper direction...
			if ( forward ) {
				if ( decPos() == null )
					searchPos = lastPos;
			} else {
				if ( incPos() == null )
					searchPos = firstPos;
			}
		}
		
		// The search cycle
		while ( true ) {
			if ( forward ) {
				if ( incPos() == null )
					searchPos = firstPos;
			} else {
				if ( decPos() == null )
					searchPos = lastPos;
			}
			
			if ( matches() ) {
				lastSearchedPos = searchPos;
				return;
			}
			
			if ( searchPos.equals( lastSearchedPos ) ) {
				if ( searchLastMatch )
					searchLastMatch = false; // This was just the first position we checked
				else {
					// We searched
					Sound.beepOnEmptyTxtSearchRslt();
					return;
				}
			}
		}
	}
	
	/**
	 * Prepares for a new search. Called from the beginning of {@link #performSearch(TextSearchComp, boolean, boolean)}.
	 * <p>
	 * This implementation caches the values returned by {@link #getFirstPos()} and {@link #getLastPos()}. If overridden to precalculate/cache some things for
	 * the search, this implementation must be called (by <code>super.prepareNew()</code>).
	 * </p>
	 */
	protected void prepareNew() {
		firstPos = getFirstPos();
		lastPos = getLastPos();
	}
	
	/**
	 * Returns the position of the first element.<br>
	 * If there is no content to search, <code>null</code> must be returned.
	 * 
	 * @return the position of the first element
	 */
	public abstract P getFirstPos();
	
	/**
	 * Returns the position of the last element.<br>
	 * If there is no content to search, <code>null</code> must be returned.
	 * 
	 * @return the position of the last element
	 */
	public abstract P getLastPos();
	
	/**
	 * Returns the start position the search should begin from.<br>
	 * This is usually the position of the selected line or entry of the component whose content is being searched.
	 * 
	 * @return the start position the search should begin from
	 */
	public abstract P getStartPos();
	
	/**
	 * Steps the current search position ({@link #searchPos}) forward.<br>
	 * If there is no next position, <code>null</code> must be returned.
	 * 
	 * @return the current search position after stepping forward
	 */
	public abstract P incPos();
	
	/**
	 * Steps the current search position ({@link #searchPos}) backward.<br>
	 * If there is no previous position, <code>null</code> must be returned.
	 * 
	 * @return the current search position after stepping forward
	 */
	public abstract P decPos();
	
	/**
	 * Returns if the current search position matches the search text ({@link #searchText}).
	 * <p>
	 * If the result is true, the implementation should also handle the match (e.g. select the item). If true is return, it will end the search.
	 * </p>
	 * 
	 * @return true if the current search position matches the search text
	 */
	public abstract boolean matches();
	
}
