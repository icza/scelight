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

import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;

/**
 * An extended {@link JSplitPane}.
 * 
 * @author Andras Belicza
 * 
 * @see hu.scelightapi.service.IGuiFactory#newSplitPane()
 * @see hu.scelightapi.service.IGuiFactory#newSplitPane(int)
 */
public interface ISplitPane {
	
	/**
	 * Casts this instance to {@link JSplitPane}.
	 * 
	 * @return <code>this</code> as a {@link JSplitPane}
	 */
	JSplitPane asSplitPane();
	
	/**
	 * Calls the {@link JSplitPane#setDividerLocation(double)} method "later" ({@link SwingUtilities#invokeLater(Runnable)}.
	 * 
	 * <p>
	 * Since {@link JSplitPane#setDividerLocation(double)} resizes immediately, it has only effect if the component is visible - hence the call "later" gain.
	 * </p>
	 * 
	 * <p>
	 * Note: in most cases setting the resize weight ({@link JSplitPane#setResizeWeight(double)}) is enough.
	 * </p>
	 * 
	 * @param proportionalLocation location to be passed
	 * @see JSplitPane#setResizeWeight(double)
	 */
	void setDividerLocationLater( double proportionalLocation );
	
}
