/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelightapi.gui.comp;

import hu.scelightapi.service.IGuiFactory;

import javax.swing.JList;

/**
 * An extended {@link JList} which supports drag-selecting items (while the mouse is dragged, all "hovered" items are selected or deselected).
 * 
 * @param <E> type of the elements in the list
 * 
 * @author Andras Belicza
 * 
 * @see IGuiFactory#newList()
 */
public interface IList< E > {
	
	/**
	 * Casts this instance to {@link JList}.
	 * 
	 * @return <code>this</code> as a {@link JList}
	 */
	JList< E > asList();
	
}
