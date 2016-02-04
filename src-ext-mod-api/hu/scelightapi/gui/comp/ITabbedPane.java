/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelightapi.gui.comp;

import hu.scelightapi.gui.setting.ISettingsGui;
import hu.scelightapi.service.IGuiFactory;
import hu.scelightapibase.bean.settings.ISettingChangeListener;
import hu.scelightapibase.gui.comp.IBorderPanel;
import hu.scelightapibase.gui.icon.IRIcon;
import hu.scelightapibase.util.iface.Producer;

import javax.swing.JComponent;
import javax.swing.JTabbedPane;

/**
 * An extended {@link JTabbedPane}.
 * 
 * <p>
 * Supports lazily created tab components which is very useful because only non-lazy tabs are created on first show which might be significantly faster, also if
 * a user never clicks on a tab, it is never created.
 * </p>
 * 
 * <p>
 * Supports hiding tabs in a way that hidden tabs (both tab title and content components) remain in the component tree (which means bounded
 * {@link ISettingChangeListener}s will properly be removed if {@link ISettingsGui#removeAllBoundedScl(JComponent)} is called on a parent.
 * </p>
 * 
 * @author Andras Belicza
 * 
 * @see IGuiFactory#newTabbedPane()
 */
public interface ITabbedPane extends IBorderPanel {
	
	/**
	 * Casts this instance to {@link IBorderPanel}.
	 * 
	 * @return <code>this</code> as a {@link IBorderPanel}
	 */
	IBorderPanel asBorderPanel();
	
	/**
	 * Returns the wrapped {@link JTabbedPane}.
	 * 
	 * @return the wrapped {@link JTabbedPane}
	 */
	JTabbedPane getWrappedTabbedPane();
	
	/**
	 * Adds a new, lazily created non-closeable tab.
	 * 
	 * @param title title of the tab
	 * @param ricon ricon of the tab
	 * @param tabProducer tab component producer
	 * @param addTabMnemonic tells if a tab mnemonic has to be added
	 */
	void addTab( String title, IRIcon ricon, Producer< JComponent > tabProducer, boolean addTabMnemonic );
	
	/**
	 * Adds a new, lazily created tab.
	 * 
	 * @param title title of the tab
	 * @param ricon ricon of the tab
	 * @param tabProducer tab component producer
	 * @param addTabMnemonic tells if a tab mnemonic has to be added
	 * @param closeable tells if the tab is closeable (adds a close icon if it is)
	 * @param beforeCloseTask optional task to be executed before close in case of closeable tabs
	 */
	void addTab( String title, IRIcon ricon, Producer< JComponent > tabProducer, boolean addTabMnemonic, boolean closeable, Runnable beforeCloseTask );
	
	/**
	 * Adds a new non-closeable tab.
	 * 
	 * @param title title of the tab
	 * @param ricon ricon of the tab
	 * @param tab tab component to be added
	 * @param addTabMnemonic tells if a tab mnemonic has to be added
	 */
	void addTab( String title, IRIcon ricon, JComponent tab, boolean addTabMnemonic );
	
	/**
	 * Adds a new tab.
	 * 
	 * @param title title of the tab
	 * @param ricon ricon of the tab
	 * @param tab tab component to be added
	 * @param addTabMnemonic tells if a tab mnemonic has to be added
	 * @param closeable tells if the tab is closeable (adds a close icon if it is)
	 * @param beforeCloseTask optional task to be executed before close in case of closeable tabs
	 */
	void addTab( String title, IRIcon ricon, JComponent tab, boolean addTabMnemonic, boolean closeable, Runnable beforeCloseTask );
	
	/**
	 * Hides the tab at the specified index.
	 * 
	 * @param idx index of the tab to be hidden
	 */
	void hideTab( int idx );
	
	/**
	 * Unhides a tab specified by its hidden index, and inserts it to the specified index.
	 * 
	 * @param hiddenIdx index of the tab to be unhidden, amongst the hidden tabs
	 * @param toIdx index to show the tab at
	 */
	void unhideTab( int hiddenIdx, int toIdx );
	
	/**
	 * Returns the visible tab count.
	 * 
	 * @return the visible tab count
	 */
	int getTabCount();
	
	/**
	 * Returns the hidden tab count.
	 * 
	 * @return the hidden tab count
	 */
	int getHiddenTabCount();
	
}
