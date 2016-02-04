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

import java.awt.BorderLayout;

import javax.swing.JComponent;

/**
 * An {@link IPage} view component creator.
 * 
 * <p>
 * Pages that wish to get notified about them being selected or deselected must create and return a page component which implements
 * {@link IPageSelectedListener} .
 * </p>
 * 
 * <p>
 * Closeable pages (if {@link IPage#isCloseable()} returns true) can be notified about being closed (and can even veto it) by creating and returning a page
 * component which implements the {@link IPageClosingListener}.
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
 * @see IPage
 */
public interface IPageCompCreator< T extends JComponent > {
	
	/**
	 * Creates and returns a new page view component. The component will be wrapped in a scroll pane and added to the center of a panel having
	 * {@link BorderLayout}.
	 * 
	 * <p>
	 * If the returned component implements listeners such as {@link IPageSelectedListener}, {@link IPageClosingListener} or {@link IPageDisposedListener}, they
	 * will be called properly.
	 * </p>
	 * 
	 * @return a new page view component
	 */
	T createPageComp();
	
}
