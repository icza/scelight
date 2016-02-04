/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelightapi.gui;

import hu.scelightapi.IServices;
import hu.scelightapibase.gui.comp.multipage.IMultiPageComp;

import javax.swing.JFrame;

/**
 * Interface of the main frame of the application.
 * 
 * @author Andras Belicza
 * 
 * @see IServices#getMainFrame()
 */
public interface IMainFrame {
	
	/**
	 * Casts this instance to {@link JFrame}.
	 * 
	 * @return <code>this</code> as a {@link JFrame}
	 */
	JFrame asFrame();
	
	/**
	 * Returns the {@link IMultiPageComp} of the main frame, the main multi-page component.
	 * 
	 * @return the {@link IMultiPageComp} of the main frame, the main multi-page component
	 */
	IMultiPageComp getMultiPageComp();
	
	/**
	 * Restores the default window position and maximizes it.
	 */
	void restoreDefaultWinPos();
	
	/**
	 * Restores the state of the main frame from minimized to tray state.
	 */
	void restoreMainFrame();
	
}
