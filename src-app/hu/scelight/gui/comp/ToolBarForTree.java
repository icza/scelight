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

import hu.scelightapi.gui.comp.IToolBarForTree;
import hu.scelightapibase.gui.comp.ITree;
import hu.sllauncher.gui.comp.SelectionListenerToolBar;
import hu.sllauncher.gui.comp.XTree;
import hu.sllauncher.util.gui.adapter.TreeSelectionAdapter;

import javax.swing.JTree;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

/**
 * A {@link SelectionListenerToolBar} which targets an {@link XTree} and listens for its selection changes.
 * 
 * <p>
 * The {@link #finalizeLayout()} method must be called in order to install the {@link TreeSelectionListener}.
 * </p>
 * 
 * @author Andras Belicza
 */
@SuppressWarnings( "serial" )
public class ToolBarForTree extends SelectionListenerToolBar implements IToolBarForTree {
	
	/** The targeted tree. */
	private final JTree tree;
	
	/**
	 * Creates a new {@link ToolBarForTree}.
	 * 
	 * @param tree targeted tree whose selection changes to listen
	 */
	public ToolBarForTree( final ITree tree ) {
		this.tree = tree.asTree();
	}
	
	/**
	 * Overridden to register {@link ListSelectionListener} to the targeted tree and also to invoke it.
	 */
	@Override
	public void finalizeLayout() {
		super.finalizeLayout();
		
		tree.addTreeSelectionListener( new TreeSelectionAdapter( true ) {
			@Override
			public void valueChanged( final TreeSelectionEvent event ) {
				selectionChanged( tree.getMinSelectionRow() >= 0 );
			}
		} );
	}
	
}
