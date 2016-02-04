/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight.gui.page.repanalyzer.inspector;

/**
 * User object used in dynamically, lazily created tree.
 * 
 * @author Andras Belicza
 */
class TreeUserObject {
	
	/** Display text of the node. */
	public final String title;
	
	/** Represented data. */
	public final Object data;
	
	/**
	 * Creates a new {@link TreeUserObject}.
	 * 
	 * @param title display text of the node
	 * @param data represented data
	 */
	public TreeUserObject( final String title, final Object data ) {
		this.title = title;
		this.data = data;
	}
	
	@Override
	public String toString() {
		return title;
	}
	
}
