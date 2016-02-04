/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelightapibase.gui.comp.multipage;

import java.util.List;

import javax.swing.JComponent;

/**
 * A multi-page component.
 * 
 * <p>
 * This component is a manager component for multiple {@link IPage}s.<br/>
 * A hierarchical tree component is displayed on the left for navigation and page list.<br/>
 * It also has some controls like "Previous Page" and "Next Page" to help the navigation.
 * </p>
 * 
 * @author Andras Belicza
 * 
 * @see IPage
 * @see hu.scelightapi.service.IGuiFactory#newMultiPageComp(List, IPage, javax.swing.JComponent)
 */
public interface IMultiPageComp {
	
	/**
	 * Casts this instance to {@link JComponent}.
	 * 
	 * @return <code>this</code> as a {@link JComponent}
	 */
	JComponent asComponent();
	
	/**
	 * Adds a new top-level page and rebuilds the page tree (cached page components will NOT be discarded).
	 * 
	 * @param page page to be added
	 */
	void addPage( IPage< ? > page );
	
	/**
	 * Sets the page list and rebuilds the page tree (cached page components will be discarded).
	 * 
	 * @param pageList page list to be set
	 */
	void setPageList( List< IPage< ? > > pageList );
	
	/**
	 * Clears the page history.
	 */
	void clearHistory();
	
	/**
	 * Rebuilds the page tree.
	 * 
	 * @param clearComponentCache tells if cached components have to be discarded
	 */
	void rebuildPageTree( boolean clearComponentCache );
	
	/**
	 * Selects the specified page.
	 * 
	 * @param page page to be selected
	 * @return true if the page was found and selected; false otherwise
	 * 
	 * @see #getSelectedPage()
	 */
	boolean selectPage( IPage< ? > page );
	
	/**
	 * Returns the currently selected page.
	 * 
	 * @return the currently selected page; or <code>null</code> if no page is selected
	 * 
	 * @see #selectPage(IPage)
	 */
	IPage< ? > getSelectedPage();
	
	/**
	 * Performs a navigation tree auto-resize.
	 * <p>
	 * Note: if navigation tree auto-resize is not enabled, this method will do nothing.
	 * </p>
	 * 
	 * @see #setNavTreeAutoResize(boolean)
	 */
	void resizeNavTree();
	
	/**
	 * Sets the navigation tree auto-resize property.<br>
	 * The default value is <code>true</code>.
	 * 
	 * @param navTreeAutoResize navigation tree auto-resize property to be set
	 * 
	 * @see #isNavTreeAutoResize()
	 */
	void setNavTreeAutoResize( boolean navTreeAutoResize );
	
	/**
	 * Tells if the navigation tree is auto-resized.
	 * 
	 * @return true if the navigation tree is auto-resized; false otherwise
	 * 
	 * @see #setNavTreeAutoResize(boolean)
	 */
	boolean isNavTreeAutoResize();
	
	/**
	 * Sets the max auto-width of the navigation tree.<br>
	 * The default value is <code>null</code>.
	 * 
	 * @param navTreeMaxAutoWidth optional max auto-width of the navigation tree to be set; a <code>null</code> value means no maximum
	 * 
	 * @see #getNavTreeMaxAutoWidth()
	 */
	void setNavTreeMaxAutoWidth( Integer navTreeMaxAutoWidth );
	
	/**
	 * Returns the the max auto-width of the navigation tree.
	 * 
	 * @return the the max auto-width of the navigation tree; <code>null</code> means no maximum
	 * 
	 * @see #setNavTreeMaxAutoWidth(Integer)
	 */
	Integer getNavTreeMaxAutoWidth();
	
}
