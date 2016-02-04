/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.sllauncher.gui.comp.multipage;

import hu.scelightapibase.bean.settings.ISettingChangeEvent;
import hu.scelightapibase.bean.settings.ISettingChangeListener;
import hu.scelightapibase.gui.comp.multipage.IMultiPageComp;
import hu.scelightapibase.gui.comp.multipage.IPage;
import hu.scelightapibase.gui.comp.multipage.IPageClosingListener;
import hu.scelightapibase.gui.comp.multipage.IPageDisposedListener;
import hu.scelightapibase.gui.comp.multipage.IPageSelectedListener;
import hu.sllauncher.bean.settings.type.Setting;
import hu.sllauncher.gui.comp.BorderPanel;
import hu.sllauncher.gui.comp.XButton;
import hu.sllauncher.gui.comp.XLabel;
import hu.sllauncher.gui.comp.XScrollPane;
import hu.sllauncher.gui.comp.XSplitPane;
import hu.sllauncher.gui.comp.XTree;
import hu.sllauncher.gui.icon.LIcons;
import hu.sllauncher.gui.setting.LSettingsGui;
import hu.sllauncher.service.env.LEnv;
import hu.sllauncher.service.settings.LSettings;
import hu.sllauncher.util.LUtils;
import hu.sllauncher.util.gui.LGuiUtils;
import hu.sllauncher.util.gui.adapter.ActionAdapter;
import hu.sllauncher.util.gui.adapter.TreeSelectionAdapter;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JToolTip;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

/**
 * A multi-page component.
 * 
 * <p>
 * This component is a manager component for multiple {@link IPage}s.<br/>
 * A hierarchical {@link XTree} component is displayed on the left for navigation and page list.<br/>
 * It also has some controls like "Previous Page" and "Next Page" to help the navigation.
 * </p>
 * 
 * @author Andras Belicza
 * 
 * @see IPage
 */
@SuppressWarnings( "serial" )
public class MultiPageComp extends XSplitPane implements IMultiPageComp {
	
	/** Page list. */
	private List< IPage< ? > >              pageList;
	
	/** Page to select by default. */
	private IPage< ? >                      defaultPage;
	
	/** Root of the navigation tree. */
	private final DefaultMutableTreeNode    root                = new DefaultMutableTreeNode();
	
	/** Navigation tree of pages. */
	private final XTree                     tree                = new XTree( root ) {
		                                                            /**
		                                                             * Create a tool tip with the same font that is used in the tree.
		                                                             */
		                                                            @Override
		                                                            public JToolTip createToolTip() {
			                                                            final JToolTip tt = super.createToolTip();
			                                                            tt.setFont( getFont().deriveFont(
			                                                                    (float) LEnv.LAUNCHER_SETTINGS.get( LSettings.PAGE_LIST_FONT_SIZE ) ) );
			                                                            return tt;
		                                                            }
	                                                            };
	
	/** Scroll pane of the navigation tree. */
	private final XScrollPane               treeScrollPane      = new XScrollPane( tree, false );
	
	/** History Back button. */
	private final XButton                   backButton          = new XButton( "_Back", LIcons.F_ARROW_180.get() );
	
	/** Previous page button. */
	private final XButton                   prevPageButton      = new XButton( "_Previous", LIcons.F_ARROW_090.get() );
	
	/** Next page button. */
	private final XButton                   nextPageButton      = new XButton( "_Next", LIcons.F_ARROW_270.get() );
	
	/** History Forward button. */
	private final XButton                   forwardButton       = new XButton( "Forwar_d", LIcons.F_ARROW.get() );
	
	/** Scroll pane wrapping the active page. */
	private final XScrollPane               pageScrollPane      = new XScrollPane();
	
	/** Cache of already displayed page components. Mapped from page to page component. */
	private final Map< Object, JComponent > pageCompCache       = new HashMap<>();
	
	/** Currently selected page. */
	private IPage< ? >                      selectedPage;
	
	
	/** Tells if the navigation tree is to be auto-resized when the tree is rebuilt. Default value is true. */
	private boolean                         navTreeAutoResize   = true;
	
	/** Optional max auto-width of the navigation tree. */
	private Integer                         navTreeMaxAutoWidth;
	
	
	/** Page history. This list stores the selected pages, up to a certain number. */
	private List< IPage< ? > >              pageHistoryList     = new ArrayList<>();
	
	/**
	 * Tells if page history update is enabled.<br>
	 * (It has to be disabled during a page list rebuild for example which clears the selection and causes the first node to be selected.)
	 */
	private boolean                         enableHistoryUpdate = true;
	
	
	/**
	 * Creates a new {@link MultiPageComp}.
	 * 
	 * @param pageList initial page list to be set
	 * @param defaultPage optional page to select by default; if <code>null</code>, the first page will be selected
	 * @param rootComponent root component to register key strokes at
	 */
	public MultiPageComp( final List< IPage< ? > > pageList, final IPage< ? > defaultPage, final JComponent rootComponent ) {
		this.pageList = pageList;
		this.defaultPage = defaultPage;
		
		tree.setRootVisible( false );
		// Renderer which renders the page properties (icon and display name)
		final DefaultTreeCellRenderer pageListCellRenderer = new DefaultTreeCellRenderer() {
			@Override
			public Component getTreeCellRendererComponent( final JTree tree, final Object value, final boolean sel, final boolean expanded, final boolean leaf,
			        final int row, final boolean hasFocus ) {
				super.getTreeCellRendererComponent( tree, value, sel, expanded, leaf, row, hasFocus );
				
				final Object userObject = ( (DefaultMutableTreeNode) value ).getUserObject();
				if ( userObject instanceof IPage ) {
					final IPage< ? > page = (IPage< ? >) userObject;
					setText( page.getDisplayName() );
					// Have at least 16x16 icon:
					setIcon( page.getRicon().size( Math.max( 16, LEnv.LAUNCHER_SETTINGS.get( LSettings.PAGE_LIST_FONT_SIZE ) ) ) );
				}
				
				return this;
			}
		};
		tree.setCellRenderer( pageListCellRenderer );
		setLeftComponent( treeScrollPane );
		
		final BorderPanel contentPanel = new BorderPanel();
		
		final Box northBox = Box.createVerticalBox();
		final JPanel controlsPanel = new JPanel( new GridLayout( 1, 3, 5, 0 ) );
		backButton.setToolTipText( "Go Back in History" );
		backButton.addActionListener( new ActionAdapter() {
			@Override
			public void actionPerformed( final ActionEvent event ) {
				enableHistoryUpdate = false;
				final int idx = selectedPage == null ? pageHistoryList.size() - 1 : pageHistoryList.indexOf( selectedPage ) - 1;
				selectPage( pageHistoryList.get( idx ) );
				enableHistoryUpdate = true;
			}
		} );
		controlsPanel.add( backButton );
		prevPageButton.setToolTipText( "Select Previous Page" );
		prevPageButton.addActionListener( new ActionAdapter() {
			@Override
			public void actionPerformed( final ActionEvent event ) {
				final TreePath selectionPath = tree.getSelectionPath();
				DefaultMutableTreeNode node = selectionPath == null ? root : ( (DefaultMutableTreeNode) selectionPath.getLastPathComponent() )
				        .getPreviousNode();
				if ( node == root )
					node = root.getLastLeaf();
				selectNode( node );
			}
		} );
		controlsPanel.add( prevPageButton );
		nextPageButton.setToolTipText( "Select Next Page" );
		nextPageButton.setHorizontalTextPosition( SwingConstants.LEFT );
		nextPageButton.addActionListener( new ActionAdapter() {
			@Override
			public void actionPerformed( final ActionEvent event ) {
				final TreePath selectionPath = tree.getSelectionPath();
				DefaultMutableTreeNode node = selectionPath == null ? null : ( (DefaultMutableTreeNode) selectionPath.getLastPathComponent() ).getNextNode();
				if ( node == null )
					node = (DefaultMutableTreeNode) root.getFirstChild();
				selectNode( node );
			}
		} );
		controlsPanel.add( nextPageButton );
		forwardButton.setToolTipText( "Go Forward in History" );
		forwardButton.setHorizontalTextPosition( SwingConstants.LEFT );
		forwardButton.addActionListener( new ActionAdapter() {
			@Override
			public void actionPerformed( final ActionEvent event ) {
				enableHistoryUpdate = false;
				final int idx = selectedPage == null ? pageHistoryList.size() - 1 : pageHistoryList.indexOf( selectedPage ) + 1;
				selectPage( pageHistoryList.get( idx ) );
				enableHistoryUpdate = true;
			}
		} );
		controlsPanel.add( forwardButton );
		final XScrollPane controlsScrollPane = new XScrollPane( LGuiUtils.wrapInPanel( controlsPanel, new FlowLayout( FlowLayout.CENTER, 5, 0 ) ), false );
		northBox.add( controlsScrollPane );
		final BorderPanel titlePanel = new BorderPanel();
		final XLabel titleLabel = new XLabel( " ", SwingConstants.CENTER );
		titleLabel.setMinimumSize( new Dimension( 0, titleLabel.getMinimumSize().height ) );
		titlePanel.addCenter( titleLabel );
		final XButton closeButton = new XButton();
		closeButton.configureAsIconButton();
		closeButton.setBorder( BorderFactory.createEmptyBorder( 0, 0, 0, 5 ) );
		closeButton.setToolTipText( "Close Page (CTRL+F4)" );
		// Register CTRL+F4 to close selected page
		final Action closeAction;
		final Object actionKey = new Object();
		rootComponent.getInputMap( JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT ).put( KeyStroke.getKeyStroke( KeyEvent.VK_F4, InputEvent.CTRL_MASK ),
		        actionKey );
		rootComponent.getActionMap().put( actionKey, closeAction = new AbstractAction() {
			@Override
			public void actionPerformed( final ActionEvent event ) {
				if ( !closeButton.isEnabled() )
					return;
				removeSelectedPage();
			}
		} );
		closeButton.addActionListener( closeAction );
		titlePanel.addEast( closeButton );
		northBox.add( titlePanel );
		contentPanel.addNorth( northBox );
		
		contentPanel.addCenter( pageScrollPane );
		
		setRightComponent( contentPanel );
		
		// Selection listener which "activates" the selected page
		tree.addTreeSelectionListener( new TreeSelectionAdapter() {
			@Override
			public void valueChanged( final TreeSelectionEvent event ) {
				if ( event.isAddedPath() ) {
					// Page selected
					final IPage< ? > page = (IPage< ? >) ( (DefaultMutableTreeNode) event.getPath().getLastPathComponent() ).getUserObject();
					
					// Cache created page component
					JComponent pageComp = pageCompCache.get( page );
					if ( pageComp == null )
						pageCompCache.put( page, pageComp = page.createPageComp() );
					
					selectedPage = page;
					titleLabel.setText( page.getDisplayName() );
					// Have at least 16x16 icon:
					final int iconSize = Math.max( 16, LEnv.LAUNCHER_SETTINGS.get( LSettings.PAGE_TITLE_FONT_SIZE ) );
					titleLabel.setIcon( page.getRicon().size( iconSize ) );
					closeButton.setEnabled( page.isCloseable() );
					pageScrollPane.setViewportView( pageComp );
					
					// Update page history:
					if ( enableHistoryUpdate ) {
						pageHistoryList.remove( page );
						pageHistoryList.add( page );
						while ( pageHistoryList.size() > LEnv.LAUNCHER_SETTINGS.get( LSettings.PAGE_HISTORY_SIZE ) )
							pageHistoryList.remove( 0 );
					}
					backButton.setEnabled( pageHistoryList.indexOf( selectedPage ) > 0 );
					// This forward button enabler condition also works if history is empty:
					forwardButton.setEnabled( pageHistoryList.indexOf( selectedPage ) < pageHistoryList.size() - 1 );
					
					if ( pageComp instanceof IPageSelectedListener )
						( (IPageSelectedListener) pageComp ).pageSelected();
				} else {
					// Page deselected. Single selection model => nothing is selected
					selectedPage = null;
					titleLabel.setText( " " ); // Set space string instead of null or empty string to reserve space
					titleLabel.setIcon( null );
					closeButton.setEnabled( false );
					pageScrollPane.setViewportView( null );
					forwardButton.setEnabled( !pageHistoryList.isEmpty() );
				}
			}
		} );
		
		// Listened settings
		final Font titleFont = titleLabel.getFont().deriveFont( Font.BOLD );
		final Font pageListFont = titleLabel.getFont();
		ISettingChangeListener scl = new ISettingChangeListener() {
			@Override
			public void valuesChanged( final ISettingChangeEvent event ) {
				if ( event.affected( LSettings.PAGE_TITLE_FONT_SIZE ) ) {
					titleLabel.setFont( titleFont.deriveFont( event.get( LSettings.PAGE_TITLE_FONT_SIZE ).floatValue() ) );
					// Have at least 16x16 icon:
					final int iconSize = Math.max( 16, event.get( LSettings.PAGE_TITLE_FONT_SIZE ) );
					if ( selectedPage != null )
						titleLabel.setIcon( selectedPage.getRicon().size( iconSize ) );
					// Have at least 16x16 icon:
					closeButton.setIcon( LIcons.F_CROSS_BUTTON.size( iconSize ) );
					closeButton.setPressedIcon( LIcons.F_CROSS_BUTTON.size( iconSize - 2 ) );
					closeButton.setDisabledIcon( LIcons.F_CROSS_BUTTON.size( iconSize, true, true ) );
				}
				if ( event.affected( LSettings.PAGE_LIST_FONT_SIZE ) ) {
					pageListCellRenderer.setFont( pageListFont.deriveFont( event.get( LSettings.PAGE_LIST_FONT_SIZE ).floatValue() ) );
					rebuildPageTree( false );
				}
				if ( event.affected( LSettings.MULTI_PAGE_DIVIDER_SIZE ) )
					setDividerSize( event.get( LSettings.MULTI_PAGE_DIVIDER_SIZE ) );
				if ( event.affected( LSettings.SHOW_CONTROL_BAR ) ) {
					controlsScrollPane.setVisible( event.get( LSettings.SHOW_CONTROL_BAR ) );
					northBox.revalidate();
				}
			}
		};
		final Set< Setting< ? > > listenedSettingSet = LUtils.< Setting< ? > > asNewSet( LSettings.PAGE_TITLE_FONT_SIZE, LSettings.PAGE_LIST_FONT_SIZE,
		        LSettings.MULTI_PAGE_DIVIDER_SIZE, LSettings.SHOW_CONTROL_BAR );
		LSettingsGui.addBindExecuteScl( scl, LEnv.LAUNCHER_SETTINGS, listenedSettingSet, this );
		
		// Clear component cache to remove bounded scls from the cached components when we are disposed:
		addPropertyChangeListener( LSettingsGui.PROP_BOUNDED_SCL_LIST, new PropertyChangeListener() {
			@Override
			public void propertyChange( final PropertyChangeEvent event ) {
				clearComponentCache();
			}
		} );
	}
	
	@Override
	public void addPage( final IPage< ? > page ) {
		pageList.add( page );
		rebuildPageTree( false );
	}
	
	@Override
	public void setPageList( final List< IPage< ? > > pageList ) {
		this.pageList = pageList;
		rebuildPageTree( true );
	}
	
	@Override
	public void clearHistory() {
		pageHistoryList.clear();
		backButton.setEnabled( false );
		forwardButton.setEnabled( false );
	}
	
	@Override
	public void rebuildPageTree( final boolean clearComponentCache ) {
		enableHistoryUpdate = false;
		
		// Store selected page and restore if possible
		final IPage< ? > oldSelectedPage = selectedPage;
		
		tree.clearSelection();
		
		root.removeAllChildren();
		
		buildPageTree( root, pageList );
		
		( (DefaultTreeModel) tree.getModel() ).reload();
		
		if ( clearComponentCache )
			clearComponentCache();
		
		// Expand all rows
		for ( int i = 0; i < tree.getRowCount(); i++ )
			tree.expandRow( i );
		
		prevPageButton.setEnabled( !pageList.isEmpty() );
		nextPageButton.setEnabled( !pageList.isEmpty() );
		
		// Resize navigation tree "later" when the scroll pane surely knows its preferred size (on first show it might not).
		SwingUtilities.invokeLater( new Runnable() {
			@Override
			public void run() {
				resizeNavTree();
			}
		} );
		
		// Restore selection
		selectPage( oldSelectedPage );
		
		enableHistoryUpdate = true;
		
		// Select default page
		if ( !pageList.isEmpty() && selectedPage == null )
			selectPage( defaultPage == null ? pageList.get( 0 ) : defaultPage );
		
	}
	
	/**
	 * Builds the page tree from the specified page list, adding the nodes to the specified parent node.
	 * 
	 * @param parentNode parent node to add page nodes to
	 * @param pageList page list to build the page tree from
	 */
	private void buildPageTree( final DefaultMutableTreeNode parentNode, final List< IPage< ? > > pageList ) {
		for ( final IPage< ? > page : pageList ) {
			final DefaultMutableTreeNode pageNode = new DefaultMutableTreeNode( page );
			parentNode.add( pageNode );
			
			if ( page.getChildList() != null && !page.getChildList().isEmpty() )
				buildPageTree( pageNode, page.getChildList() );
		}
	}
	
	/**
	 * Selects the specified node in the tree. Also scrolls to it if it's not visible.
	 * 
	 * @param node node to be selected
	 */
	private void selectNode( final DefaultMutableTreeNode node ) {
		final TreePath treePath = new TreePath( node.getPath() );
		tree.setSelectionPath( treePath );
		
		// I could use tree.scrollPathToVisible( treePath ), but the problem with it is that it also scrolls horizontally
		// which I don't want to. So I scroll to a rectangle where x = 0
		final Rectangle bounds = tree.getPathBounds( treePath );
		bounds.x = 0;
		tree.scrollRectToVisible( bounds );
	}
	
	@Override
	public boolean selectPage( final IPage< ? > page ) {
		DefaultMutableTreeNode node = root;
		
		while ( ( node = node.getNextNode() ) != null )
			if ( node.getUserObject() == page ) {
				selectNode( node );
				return true;
			}
		
		return false;
	}
	
	/**
	 * Removes the selected page.
	 */
	private void removeSelectedPage() {
		if ( selectedPage.getChildList() != null && !selectedPage.getChildList().isEmpty() )
			throw new UnsupportedOperationException(); // Would require recursive remove (recursive removeAllBoundedScl())
			
		final JComponent pageComp = pageCompCache.get( selectedPage );
		if ( pageComp instanceof IPageClosingListener )
			if ( !( (IPageClosingListener) pageComp ).pageClosing() )
				return; // Close vetoed
				
		// Remove from its parent if has one
		final IPage< ? > parent = selectedPage.getParent();
		int childIndex = 0;
		if ( parent != null ) {
			childIndex = parent.getChildList().indexOf( selectedPage );
			parent.getChildList().remove( childIndex );
		} else
			pageList.remove( selectedPage );
		
		disposePageComp( pageComp );
		pageCompCache.remove( selectedPage );
		
		pageHistoryList.remove( selectedPage );
		// Determine the page to be selected next
		if ( pageHistoryList.isEmpty() ) {
			if ( parent != null ) {
				// Select the (preferably) previous sibling or the parent if no siblings
				selectPage( parent.getChildList().isEmpty() ? parent : parent.getChildList().get( childIndex > 0 ? childIndex - 1 : 0 ) );
			}
		} else
			selectPage( pageHistoryList.get( pageHistoryList.size() - 1 ) );
		
		rebuildPageTree( false );
	}
	
	/**
	 * Returns the cached page component for the specified page.
	 * 
	 * @param <T> type of the page component of the specified page
	 * @param page page whose cached component to return
	 * @return the cached page component for the specified page
	 */
	@SuppressWarnings( "unchecked" )
	public < T extends JComponent > T getCachedPageComponent( final IPage< T > page ) {
		// It's safe to suppress unchecked warning here, because the cached page component
		// is set to a value returned by the page's createPage() method which is of type T
		return (T) pageCompCache.get( page );
	}
	
	@Override
	public IPage< ? > getSelectedPage() {
		return selectedPage;
	}
	
	/**
	 * Clears the component cache, also removes bounded {@link ISettingChangeListener}s from the cached components.
	 */
	public void clearComponentCache() {
		for ( final JComponent comp : pageCompCache.values() )
			disposePageComp( comp );
		
		pageCompCache.clear();
	}
	
	/**
	 * Disposes the specified page component.
	 * 
	 * <p>
	 * If the page component implements {@link IPageDisposedListener}, it will be called. Also removes bounded {@link ISettingChangeListener}s from the
	 * component.
	 * </p>
	 * 
	 * @param pageComp page component to be disposed
	 */
	private static void disposePageComp( final JComponent pageComp ) {
		if ( pageComp instanceof IPageDisposedListener )
			( (IPageDisposedListener) pageComp ).pageDisposed();
		
		LSettingsGui.removeAllBoundedScl( pageComp );
	}
	
	@Override
	public void resizeNavTree() {
		if ( navTreeAutoResize ) {
			// +16 so if vertical scroll bar appears, still no need of horizontal scroll bar
			// TODO Note if vertical scroll bar is already visible (or required to be visible), should not add that extra 16.
			int dividerLocation = treeScrollPane.getPreferredSize().width + 16;
			
			if ( navTreeMaxAutoWidth != null && navTreeMaxAutoWidth < dividerLocation )
				dividerLocation = navTreeMaxAutoWidth;
			
			setDividerLocation( dividerLocation );
		}
	}
	
	@Override
	public void setNavTreeAutoResize( final boolean navTreeAutoResize ) {
		this.navTreeAutoResize = navTreeAutoResize;
	}
	
	@Override
	public boolean isNavTreeAutoResize() {
		return navTreeAutoResize;
	}
	
	@Override
	public void setNavTreeMaxAutoWidth( final Integer navTreeMaxAutoWidth ) {
		this.navTreeMaxAutoWidth = navTreeMaxAutoWidth;
	}
	
	@Override
	public Integer getNavTreeMaxAutoWidth() {
		return navTreeMaxAutoWidth;
	}
	
	@Override
	public MultiPageComp asComponent() {
		return this;
	}
	
}
