/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight.util.gui;

import hu.scelightapi.util.gui.ITableIcon;
import hu.scelightapibase.util.gui.HasRIcon;

import java.awt.Component;
import java.awt.Graphics;

import javax.swing.Icon;

/**
 * Icon for tables which overrides {@link Object#toString()} for copying and table search / filter operations, and which implements {@link Comparable} for
 * sorting.
 * 
 * @param <T> type of the represented object
 * 
 * @author Andras Belicza
 */
public class TableIcon< T extends HasRIcon & Comparable< T > > implements ITableIcon< T > {
	
	/** The represented object. */
	public final T    represented;
	
	/** Icon. */
	public final Icon icon;
	
	/**
	 * Creates a new {@link TableIcon}.
	 * 
	 * @param represented the represented object, it is the base of comparison and the text provider for {@link Object#toString()}
	 */
	public TableIcon( final T represented ) {
		this( represented, null );
	}
	
	/**
	 * Creates a new {@link TableIcon}.
	 * 
	 * @param represented the represented object, it is the base of comparison and the text provider for {@link Object#toString()}
	 * @param size optional size if the ricon has to be sized
	 */
	public TableIcon( final T represented, final Integer size ) {
		this.represented = represented;
		icon = size == null ? represented.getRicon().get() : represented.getRicon().size( size );
	}
	
	@Override
	public T getRepresented() {
		return represented;
	}
	
	@Override
	public Icon getIcon() {
		return icon;
	}
	
	@Override
	public void paintIcon( final Component c, final Graphics g, final int x, final int y ) {
		icon.paintIcon( c, g, x, y );
	}
	
	@Override
	public int getIconWidth() {
		return icon.getIconWidth();
	}
	
	@Override
	public int getIconHeight() {
		return icon.getIconHeight();
	}
	
	@Override
	public int compareTo( final ITableIcon< T > ti ) {
		return represented.compareTo( ti.getRepresented() );
	}
	
	@Override
	public String toString() {
		return represented.toString();
	}
	
}
