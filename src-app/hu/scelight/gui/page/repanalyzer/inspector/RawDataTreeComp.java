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

import hu.belicza.andras.util.StructView;
import hu.belicza.andras.util.type.BitArray;
import hu.belicza.andras.util.type.XString;
import hu.scelight.gui.comp.ToolBarForTree;
import hu.scelight.gui.icon.Icons;
import hu.scelight.gui.page.repanalyzer.BaseRepAnalTabComp;
import hu.scelight.sc2.rep.model.Replay;
import hu.scelight.sc2.rep.repproc.RepProcessor;
import hu.scelight.sc2.rep.s2prot.type.Attribute;
import hu.scelight.service.env.Env;
import hu.scelight.service.settings.Settings;
import hu.scelight.util.Utils;
import hu.sllauncher.action.XAction;
import hu.sllauncher.gui.comp.BorderPanel;
import hu.sllauncher.gui.comp.ModestLabel;
import hu.sllauncher.gui.comp.TextSearchComp;
import hu.sllauncher.gui.comp.XScrollPane;
import hu.sllauncher.gui.comp.XTextField;
import hu.sllauncher.gui.comp.XTree;
import hu.sllauncher.util.Pair;
import hu.sllauncher.util.gui.adapter.TreeSelectionAdapter;
import hu.sllauncher.util.gui.searcher.TreeSearcher;

import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.Box;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

/**
 * Raw data tree component of the game info tab.
 * 
 * @author Andras Belicza
 */
@SuppressWarnings( "serial" )
public class RawDataTreeComp extends BaseRepAnalTabComp {
	
	/** Expand selected nodes. */
	private final XAction                expandSelAction   = new XAction( Icons.F_PLUS_CIRCLE_FRAME, "Expand Selected", this ) {
		                                                       @Override
		                                                       public void actionPerformed( final ActionEvent event ) {
			                                                       final int maxDepth = Env.APP_SETTINGS.get( Settings.RAW_TREE_EXPAND_DEPTH ) - 1;
			                                                       
			                                                       final TreePath[] selectionPaths = tree.getSelectionPaths();
			                                                       for ( final TreePath tp : selectionPaths ) {
				                                                       if ( tp.equals( gameEventsTreePath ) || tp.equals( trackerEventsTreePath ) ) {
					                                                       if ( maxDepth >= 0 )
						                                                       tree.expandPathRecursive( tp, maxDepth );
				                                                       } else
					                                                       tree.expandPathRecursive( tp );
			                                                       }
		                                                       }
	                                                       };
	
	/** Collapse selected nodes. */
	private final XAction                collapseSelAction = new XAction( Icons.F_MINUS_CIRCLE_FRAME, "Collapse Selected", this ) {
		                                                       @Override
		                                                       public void actionPerformed( final ActionEvent event ) {
			                                                       final TreePath[] selectionPaths = tree.getSelectionPaths();
			                                                       for ( int i = selectionPaths.length - 1; i >= 0; i-- )
				                                                       tree.collapsePathRecursive( selectionPaths[ i ] );
		                                                       }
	                                                       };
	
	/** Expand all nodes. */
	private final XAction                expandAllAction   = new XAction( Icons.F_PLUS_BUTTON, "Expand All", this ) {
		                                                       @Override
		                                                       public void actionPerformed( final ActionEvent event ) {
			                                                       // Max expand depth for game events and tracker events
			                                                       final int maxDepth = Env.APP_SETTINGS.get( Settings.RAW_TREE_EXPAND_DEPTH ) - 1;
			                                                       
			                                                       tree.expandRow( 0 );
			                                                       for ( int i = 0; i < root.getChildCount(); i++ ) {
				                                                       final TreePath tp = new TreePath(
				                                                               ( (DefaultMutableTreeNode) root.getChildAt( i ) ).getPath() );
				                                                       if ( tp.equals( gameEventsTreePath ) || tp.equals( trackerEventsTreePath ) ) {
					                                                       if ( maxDepth >= 0 )
						                                                       tree.expandPathRecursive( tp, maxDepth );
				                                                       } else
					                                                       tree.expandPathRecursive( tp );
			                                                       }
		                                                       }
	                                                       };
	
	/** Collapse all nodes. */
	private final XAction                collapseAllAction = new XAction( Icons.F_MINUS_BUTTON, "Collapse All", this ) {
		                                                       @Override
		                                                       public void actionPerformed( final ActionEvent event ) {
			                                                       // A new model clears the expanded state cache
			                                                       // (which implies root expanded and nothing else).
			                                                       tree.setModel( new DefaultTreeModel( root ) );
			                                                       
			                                                       // Normal implementation would be:
			                                                       // tree.expandRow( 0 ); // Ensure root expanded
			                                                       // Leave root (row=0) expanded
			                                                       // for ( int i = tree.getRowCount() - 1; i > 0; i-- )
			                                                       // tree.collapseRow( i );
			                                                       
		                                                       }
	                                                       };
	
	
	
	/** Text search component. */
	private final TextSearchComp         searchComp        = new TextSearchComp( true );
	
	/** Searcher logic. */
	private final TreeSearcher           searcher;
	
	/** Root of the data tree. */
	private final DefaultMutableTreeNode root              = new DefaultMutableTreeNode( new TreeUserObject( "Replay", null ) );
	
	/** Game events node. */
	private TreePath                     gameEventsTreePath;
	
	/** Tracker events node.. */
	private TreePath                     trackerEventsTreePath;
	
	/** Tree visualizing data. */
	private final XTree                  tree              = new XTree( root );
	
	/**
	 * Creates a new {@link RawDataTreeComp}.
	 * 
	 * @param repProc replay processor
	 */
	public RawDataTreeComp( final RepProcessor repProc ) {
		super( repProc );
		
		searcher = new TreeSearcher( tree ) {
			@Override
			public boolean matches() {
				if ( Utils.containsIngoreCase( ( (TreeUserObject) searchPos.getUserObject() ).title, searchText ) ) {
					final TreePath tp = new TreePath( searchPos.getPath() );
					tree.setSelectionPath( tp );
					tree.scrollPathToVisible( tp );
					return true;
				}
				return false;
			}
		};
		
		buildGui();
	}
	
	/**
	 * Builds the GUI of the tab.
	 */
	private void buildGui() {
		final Box northBox = Box.createVerticalBox();
		addNorth( northBox );
		
		final ToolBarForTree toolBar = new ToolBarForTree( tree );
		northBox.add( toolBar );
		toolBar.addSelectInfoLabel( "Select a Node." );
		toolBar.addSelEnabledButton( expandSelAction );
		toolBar.addSelEnabledButton( collapseSelAction );
		toolBar.addSeparator();
		toolBar.add( expandAllAction );
		toolBar.add( collapseAllAction );
		
		toolBar.addSeparator();
		toolBar.add( searchComp );
		searchComp.registerFocusHotkey( this );
		searchComp.setSearcher( searcher );
		
		toolBar.finalizeLayout();
		
		final BorderPanel selectionInfoPanel = new BorderPanel();
		northBox.add( selectionInfoPanel );
		selectionInfoPanel.addWest( new ModestLabel( "Selection path:" ) );
		final XTextField selectionPathTextField = new XTextField( 10 );
		selectionPathTextField.setFont( new Font( Font.MONOSPACED, 0, 12 ) );
		selectionPathTextField.setEditable( false );
		selectionInfoPanel.addCenter( selectionPathTextField );
		
		tree.addTreeSelectionListener( new TreeSelectionAdapter( true ) {
			@Override
			public void valueChanged( final TreeSelectionEvent event ) {
				final TreePath path = tree.getSelectionPath();
				if ( path == null ) {
					selectionPathTextField.setText( null );
					return;
				}
				final StringBuilder sb = new StringBuilder();
				for ( final Object o : path.getPath() ) {
					if ( sb.length() > 0 )
						sb.append( " > " );
					sb.append( o );
				}
				selectionPathTextField.setText( sb.toString() );
			}
		} );
		
		tree.setLargeModel( true );
		// Renderer which renders different leaf icons based on the data type
		tree.setCellRenderer( new DefaultTreeCellRenderer() {
			@Override
			public Component getTreeCellRendererComponent( final JTree tree, final Object value, final boolean sel, final boolean expanded, final boolean leaf,
			        final int row, final boolean hasFocus ) {
				super.getTreeCellRendererComponent( tree, value, sel, expanded, leaf, row, hasFocus );
				
				// Set leaf icon based on the data type
				if ( !leaf )
					return this;
				
				final TreeUserObject treeUserObject = (TreeUserObject) ( (DefaultMutableTreeNode) value ).getUserObject();
				if ( treeUserObject.data instanceof Integer )
					setIcon( Icons.F_DOCUMENT_ATTRIBUTE_I.get() );
				else if ( treeUserObject.data instanceof Boolean )
					setIcon( Icons.F_DOCUMENT_ATTRIBUTE_B.get() );
				else if ( treeUserObject.data instanceof XString || treeUserObject.data instanceof Attribute || treeUserObject.data instanceof String )
					setIcon( Icons.F_DOCUMENT_ATTRIBUTE_S.get() );
				else if ( treeUserObject.data instanceof Long )
					setIcon( Icons.F_DOCUMENT_ATTRIBUTE_L.get() );
				else if ( treeUserObject.data instanceof BitArray )
					setIcon( Icons.F_DOCUMENT_BINARY.get() );
				else
					setIcon( Icons.F_DOCUMENT.get() );
				
				return this;
			}
		} );
		
		// Create a lazily loaded Tree
		final Replay replay = repProc.replay;
		
		root.add( new DefaultMutableTreeNode( new TreeUserObject( "HEADER", replay.header.getStruct() ) ) );
		root.add( new DefaultMutableTreeNode( new TreeUserObject( "DETAILS", replay.details.getStruct() ) ) );
		root.add( new DefaultMutableTreeNode( new TreeUserObject( "INIT DATA", replay.initData.getStruct() ) ) );
		// Note the use of AttributesEvents.getStringStruct() instead of AttributesEvents.getStruct()
		root.add( new DefaultMutableTreeNode( new TreeUserObject( "ATTRIBUTES EVENTS", replay.attributesEvents.getStringStruct() ) ) );
		root.add( new DefaultMutableTreeNode( new TreeUserObject( "MESSAGE EVENTS", replay.messageEvents.events ) ) );
		DefaultMutableTreeNode node;
		root.add( node = new DefaultMutableTreeNode( new TreeUserObject( "GAME EVENTS", replay.gameEvents.events ) ) );
		gameEventsTreePath = new TreePath( node.getPath() );
		if ( replay.trackerEvents != null ) {
			root.add( node = new DefaultMutableTreeNode( new TreeUserObject( "TRACKER EVENTS", replay.trackerEvents.events ) ) );
			trackerEventsTreePath = new TreePath( node.getPath() );
		}
		
		buildNode( root );
		
		tree.getSelectionModel().setSelectionMode( TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION );
		
		tree.expandRow( 0 ); // Ensure root expanded
		
		searcher.clearLastSearchPos();
		
		addCenter( new XScrollPane( tree, true, false ) );
	}
	
	
	/**
	 * Builds the descendants of the specified node recursively.<br>
	 * Node's children must already exists!
	 * 
	 * @param node node to be built
	 */
	private static void buildNode( final DefaultMutableTreeNode node ) {
		for ( int i = 0; i < node.getChildCount(); i++ ) {
			final DefaultMutableTreeNode child = (DefaultMutableTreeNode) node.getChildAt( i );
			createNodeChilds( child );
			buildNode( child );
		}
	}
	
	/**
	 * Creates the child nodes of the specified node.
	 * 
	 * @param node node whose child nodes to be created
	 */
	@SuppressWarnings( "unchecked" )
	private static void createNodeChilds( final DefaultMutableTreeNode node ) {
		final Object value = ( (TreeUserObject) node.getUserObject() ).data;
		if ( value instanceof Map ) {
			for ( final Entry< Object, Object > entry : ( (Map< Object, Object >) value ).entrySet() )
				node.add( new DefaultMutableTreeNode( createTreeUserObject( entry.getKey().toString(), entry.getValue() ) ) );
		} else if ( value instanceof Object[] ) {
			int i = 0;
			for ( final Object e : (Object[]) value )
				node.add( new DefaultMutableTreeNode( createTreeUserObject( Integer.toString( i++ ), e ) ) );
		}
	}
	
	/**
	 * Creates a tree user object in a way that if the specified data is a "single" value, the node title will be in the form of <code>title=value</code>; else
	 * just the <code>title</code> and the "complex" value will be the data.
	 * 
	 * @param title title of the represented data
	 * @param data value of the represented data
	 * @return a tree user object
	 */
	@SuppressWarnings( "unchecked" )
	private static TreeUserObject createTreeUserObject( final String title, final Object data ) {
		if ( data instanceof StructView )
			return new TreeUserObject( title, ( (StructView) data ).getStruct() );
		
		if ( data instanceof Pair )
			return new TreeUserObject( title + " = " + ( (Pair< String, Object >) data ).value1, ( (Pair< String, Object >) data ).value2 );
		
		return new TreeUserObject( data == null || data instanceof Map || data instanceof Object[] ? title : title + " = " + data, data );
	}
	
}
