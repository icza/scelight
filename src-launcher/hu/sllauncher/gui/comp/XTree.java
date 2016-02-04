/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.sllauncher.gui.comp;

import hu.scelightapibase.gui.comp.ITree;

import java.awt.Component;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.ToolTipManager;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

/**
 * An improved {@link JTree} which allows clicking (selecting/deselecting) rows on the full row length (not just within the row bounds which is the renderer's
 * preferred size).
 * <p>
 * Sets {@link TreeSelectionModel#SINGLE_TREE_SELECTION}, and only work properly with this selection model (modifier keys like SHIFT and CTRL are not handled
 * properly for other selection modes).
 * </p>
 * 
 * @author Andras Belicza
 */
@SuppressWarnings( "serial" )
public class XTree extends JTree implements ITree {
	
	/**
	 * Creates a new {@link XTree}.
	 * 
	 * @param root root node
	 */
	public XTree( final TreeNode root ) {
		super( root );
		
		ToolTipManager.sharedInstance().registerComponent( this );
		
		getSelectionModel().setSelectionMode( TreeSelectionModel.SINGLE_TREE_SELECTION );
		
		// Tree nodes are rendered by the tree cell renderer, they do not fill the whole row.
		// Make the whole row clickable (to select/deselect the row's node):
		addMouseListener( new MouseAdapter() {
			@Override
			public void mousePressed( final MouseEvent event ) {
				int row = getRowForLocation( event.getX(), event.getY() );
				if ( row >= 0 )
					return; // Exact click on a node, default behavior is good
					
				row = getRowForEventExtended( event );
				if ( row < 0 )
					return;
				
				if ( isRowSelected( row ) ) {
					if ( event.isControlDown() )
						clearSelection();
				} else
					setSelectionRow( row );
			}
		} );
	}
	
	@Override
	public XTree asTree() {
		return this;
	}
	
	/**
	 * Returns the row on which the specified mouse event happened, accepting the full width of a row not just the bounds of the rendered component.
	 * 
	 * @param event event to search the row for
	 * @return the row on which the specified mouse event happened; or -1 if event is not on a node
	 */
	private int getRowForEventExtended( final MouseEvent event ) {
		final int x = event.getX(), y = event.getY();
		
		int row = getRowForLocation( x, y );
		if ( row >= 0 )
			return row; // Exact match on a node
			
		row = getClosestRowForLocation( x, y );
		if ( row < 0 )
			return -1;
		
		// Row bounds is the bounds of the rendered JLabel!
		final Rectangle bounds = getRowBounds( row );
		
		// Check if contained, regardless of the width of the bounds => full width
		if ( y >= bounds.y && y < bounds.y + bounds.height && x >= bounds.x ) {
			return row;
		}
		
		return -1;
	}
	
	/**
	 * Returns the path on which the specified mouse event happened, accepting the full width of a row not just the bounds of the rendered component.
	 * 
	 * @param event event to search the path for
	 * @return the path on which the specified mouse event happened; or <code>null</code> if event is not on a node
	 */
	private TreePath getPathForEventExtended( final MouseEvent event ) {
		return getPathForRow( getRowForEventExtended( event ) );
	}
	
	@Override
    public void expandPathRecursive( final TreePath path ) {
		expandPathRecursive( path, Integer.MAX_VALUE );
	}
	
	@Override
	public void expandPathRecursive( final TreePath path, int maxDepth ) {
		expandPath( path );
		if ( maxDepth <= 0 )
			return;
		
		maxDepth--;
		
		final TreeNode node = (TreeNode) path.getLastPathComponent();
		
		// Going upward is faster because expanding needs to update row indices of rows that follow
		// and this way there are less rows to follow when expanding a node
		for ( int i = 0; i < node.getChildCount(); i++ )
			expandPathRecursive( path.pathByAddingChild( node.getChildAt( i ) ), maxDepth );
	}
	
	@Override
	public void collapsePathRecursive( final TreePath path ) {
		final TreeNode node = (TreeNode) path.getLastPathComponent();
		
		// Going downward is faster because collapsing needs to update row indices of rows that follow
		// and this way there are less rows to follow when collapsing a node
		for ( int i = node.getChildCount() - 1; i >= 0; i-- )
			collapsePathRecursive( path.pathByAddingChild( node.getChildAt( i ) ) );
		
		collapsePath( path );
	}
	
	/**
	 * Overridden to return node text value as tool tip if it's truncated (due to not fitting into the view).
	 */
	@Override
	public String getToolTipText( final MouseEvent event ) {
		// First check if super implementation returns a tool tip:
		// (super implementation checks if a tool tip is set on the renderer for the node,
		// and if no, also checks if a tool tip has been set on the tree itself)
		final String tip = super.getToolTipText( event );
		if ( tip != null )
			return tip;
		
		// No tool tip from the renderer, and no own tool tip has been set.
		// So let's check if node doesn't fit into the view, and if so provide it's full text as the tool tip
		final TreePath path = getPathForEventExtended( event );
		if ( path == null ) // Event is not on a node
			return null;
		
		final Rectangle pathBounds = getPathBounds( path );
		// If the renderer component is a JLabel, only check if the rendered text is visible excluding the icon
		// because the tool tip only shows the text (and not the icon)
		final TreeNode value = (TreeNode) path.getLastPathComponent();
		final Component comp = getCellRenderer().getTreeCellRendererComponent( this, value, isPathSelected( path ), isExpanded( path ), value.isLeaf(),
		        getRowForPath( path ), false );
		if ( comp instanceof JLabel ) {
			final Icon icon = ( (JLabel) comp ).getIcon();
			if ( icon != null ) {
				pathBounds.x += icon.getIconWidth();
				pathBounds.width -= icon.getIconWidth();
			}
		}
		if ( !getVisibleRect().contains( pathBounds ) )
			return path.getLastPathComponent().toString();
		
		return null;
	}
	
	/**
	 * Positions tool tips exactly over the node they belong to, also avoids showing empty tool tips (by returning <code>null</code>).
	 */
	@Override
	public Point getToolTipLocation( final MouseEvent event ) {
		// If tool tip is provided by the renderer or the tree itself, use default location:
		if ( super.getToolTipText( event ) != null )
			return super.getToolTipLocation( event );
		
		// If no tool tip, return null to prevent displaying an empty tool tip
		if ( getToolTipText( event ) == null )
			return null;
		
		// Else align to the node:
		final TreePath path = getPathForEventExtended( event );
		if ( path == null ) // Event is not on a node
			return null;
		
		// Adjust due to the tool tip border and padding:
		final Point location = getPathBounds( path ).getLocation();
		location.y -= 2;
		// Adjust due to icon (if present)
		final TreeNode value = (TreeNode) path.getLastPathComponent();
		final Component comp = getCellRenderer().getTreeCellRendererComponent( this, value, isPathSelected( path ), isExpanded( path ), value.isLeaf(),
		        getRowForPath( path ), false );
		if ( comp instanceof JLabel ) {
			final Icon icon = ( (JLabel) comp ).getIcon();
			if ( icon != null )
				location.x += icon.getIconWidth();
		}
		
		return location;
	}
	
}
