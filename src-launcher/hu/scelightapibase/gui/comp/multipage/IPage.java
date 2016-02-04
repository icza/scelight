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

import hu.scelightapibase.gui.icon.IRIcon;
import hu.scelightapibase.util.gui.HasRIcon;

import java.util.List;

import javax.swing.JComponent;

/**
 * A page in an {@link IMultiPageComp}.
 * 
 * <p>
 * Pages that wish to get notified about them being selected or deselected must create and return a page component which implements
 * {@link IPageSelectedListener} .
 * </p>
 * 
 * <p>
 * Closeable pages (if {@link #isCloseable()} returns true) can be notified about being closed (and can even veto it) by creating and returning a page component
 * which implements the {@link IPageClosingListener}.
 * </p>
 * 
 * <p>
 * If a returned page component implements the {@link IPageDisposedListener} interface, {@link IPageDisposedListener#pageDisposed()} will be properly called
 * before the page component is disposed.
 * </p>
 * 
 * @param <T> type of the page component
 * 
 * @author Andras Belicza
 * 
 * @see IMultiPageComp
 * @see hu.scelightapi.service.IGuiFactory#newPage(String, IRIcon, boolean, IPageCompCreator)
 * @see IPageCompCreator
 */
public interface IPage< T extends JComponent > extends IPageCompCreator< T >, HasRIcon {
	
	/**
	 * Returns the display name of the page. This will appear in the tree.
	 * 
	 * @return the display name of the page
	 */
	String getDisplayName();
	
	/**
	 * Returns the ricon of the page. This will appear in the tree as well as in the page title.
	 * 
	 * @return the ricon of the page
	 */
	@Override
	IRIcon getRicon();
	
	/**
	 * Tells if the page is closeable.
	 * 
	 * @return true if the page is closeable, false otherwise
	 */
	boolean isCloseable();
	
	/**
	 * Sets the parent of this page.
	 * 
	 * @param parent parent page to be set
	 */
	void setParent( final IPage< ? > parent );
	
	/**
	 * Returns the parent page of the page (if there is any).
	 * 
	 * @return the parent page of the page (if there is any)
	 */
	IPage< ? > getParent();
	
	/**
	 * Returns the child page list of this page.<br>
	 * If there are child pages returned, this page will be a node in the tree and the returned child pages will appear under this page.
	 * 
	 * @return the child page list of this page
	 */
	List< IPage< ? > > getChildList();
	
}
