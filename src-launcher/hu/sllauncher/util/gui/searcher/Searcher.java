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

/**
 * Interface for searcher logic.
 * 
 * @author Andras Belicza
 */
public interface Searcher {
	
	/**
	 * Performs a search.
	 * 
	 * @param searchComp reference to the text search component
	 * @param forward tells if search is to be performed forward
	 * @param searchLastMatch tells if last search match must be searched/included
	 */
	void performSearch( TextSearchComp searchComp, boolean forward, boolean searchLastMatch );
	
}
