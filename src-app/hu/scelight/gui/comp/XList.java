/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight.gui.comp;

import hu.scelightapi.gui.comp.IList;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JList;

/**
 * An extended {@link JList} which supports drag-selecting items (while the mouse is dragged, all "hovered" items are selected or deselected).
 * 
 * @param <E> type of the elements in the list
 * 
 * @author Andras Belicza
 */
@SuppressWarnings( "serial" )
public class XList< E > extends JList< E > implements IList< E > {
	
	/**
	 * Creates a new {@link XList}.
	 */
	public XList() {
		final MouseAdapter mouseHandler = new MouseAdapter() {
			/** Dragging start index. */
			private int     startIdx;
			
			/** Selection state to set (select or deselect). */
			private boolean selectionState;
			
			@Override
			public void mousePressed( final MouseEvent event ) {
				// Drag start
				startIdx = locationToIndex( event.getPoint() );
				selectionState = isSelectedIndex( startIdx );
			}
			
			@Override
			public void mouseDragged( final MouseEvent event ) {
				final int idx = locationToIndex( event.getPoint() );
				
				if ( selectionState )
					addSelectionInterval( startIdx, idx );
				else
					removeSelectionInterval( startIdx, idx );
			}
		};
		
		addMouseListener( mouseHandler );
		addMouseMotionListener( mouseHandler );
	}
	
	@Override
	public JList< E > asList() {
		return this;
	}
	
}
