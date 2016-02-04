/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelightapibase.gui.comp;

import javax.swing.JTree;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

/**
 * An improved {@link JTree} which allows clicking (selecting/deselecting) rows on the full row length (not just within the row bounds which is the renderer's
 * preferred size).
 * 
 * <p>
 * Sets {@link TreeSelectionModel#SINGLE_TREE_SELECTION}, and only work properly with this selection model (modifier keys like SHIFT and CTRL are not handled
 * properly for other selection modes).
 * </p>
 * 
 * <p>
 * Overrides tool tip creation and placement to return the node text value as tool tip if it's truncated (due to not fitting into the view). Positions tool tips
 * exactly over the nodes' text.
 * </p>
 * 
 * 
 * @author Andras Belicza
 * 
 * @see hu.scelightapi.service.IGuiFactory#newTree(javax.swing.tree.TreeNode)
 */
public interface ITree {
	
	/**
	 * Casts this instance to {@link JTree}.
	 * 
	 * @return <code>this</code> as a {@link JTree}
	 */
	JTree asTree();
	
	/**
	 * Recursively expands the specified path.
	 * 
	 * @param path path to be expanded recursively
	 */
	void expandPathRecursive( TreePath path );
	
	/**
	 * Recursively expands the specified path.
	 * 
	 * @param path path to be expanded recursively
	 * @param maxDepth specifies the max depth to expand (0=only the specified path); you can pass {@link Integer#MAX_VALUE} for unlimited depth
	 */
	void expandPathRecursive( final TreePath path, int maxDepth );
	
	/**
	 * Recursively collapses the specified path.
	 * 
	 * @param path path to be collapsed recursively
	 */
	void collapsePathRecursive( TreePath path );
	
}
