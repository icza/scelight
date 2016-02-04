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

import hu.sllauncher.gui.comp.XTree;

import javax.swing.tree.DefaultMutableTreeNode;

/**
 * A {@link BaseSearcher} implementation which searches a {@link XTree}, position type is {@link DefaultMutableTreeNode}.
 * 
 * @author Andras Belicza
 */
public abstract class TreeSearcher extends BaseSearcher< DefaultMutableTreeNode > {
	
	/** The tree to be searched. */
	protected final XTree                  tree;
	
	/** Root of the tree to be searched. */
	protected final DefaultMutableTreeNode root;
	
	/**
	 * Creates a new {@link TreeSearcher}.
	 * 
	 * @param tree the tree to be searched
	 */
	public TreeSearcher( final XTree tree ) {
		this.tree = tree;
		root = (DefaultMutableTreeNode) tree.getModel().getRoot();
	}
	
	@Override
	public DefaultMutableTreeNode getFirstPos() {
		return root;
	}
	
	@Override
	public DefaultMutableTreeNode getLastPos() {
		return root.getLastLeaf();
	}
	
	@Override
	public DefaultMutableTreeNode getStartPos() {
		return tree.getSelectionPath() == null ? null : (DefaultMutableTreeNode) tree.getSelectionPath().getLastPathComponent();
	}
	
	@Override
	public DefaultMutableTreeNode incPos() {
		return searchPos = searchPos.getNextNode();
	}
	
	@Override
	public DefaultMutableTreeNode decPos() {
		return searchPos = searchPos.getPreviousNode();
	}
	
}
