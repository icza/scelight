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

/**
 * A {@link BaseSearcher} implementation whose position type is {@link Integer} and the positions are in the range of <code>0..{@link #maxPos}</code> inclusive.
 * 
 * @author Andras Belicza
 */
public abstract class IntPosBaseSearcher extends BaseSearcher< Integer > {
	
	/**
	 * Max value of the position. This <strong>must</strong> be set during {@link #prepareNew()}.<br>
	 * Negative value indicates there is no content to search.
	 */
	protected int maxPos;
	
	
	@Override
	public Integer getFirstPos() {
		return maxPos < 0 ? null : Integer.valueOf( 0 );
	}
	
	@Override
	public Integer getLastPos() {
		return maxPos < 0 ? null : Integer.valueOf( maxPos );
	}
	
	@Override
	public Integer incPos() {
		final int pos = searchPos;
		return searchPos = pos < maxPos ? Integer.valueOf( pos + 1 ) : null;
	}
	
	@Override
	public Integer decPos() {
		final int pos = searchPos;
		return searchPos = pos > 0 ? Integer.valueOf( pos - 1 ) : null;
	}
	
}
