/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight.gui.comp;

import hu.scelight.gui.icon.Icons;
import hu.scelight.gui.setting.SettingsGui;
import hu.scelight.util.gui.GuiUtils;
import hu.scelightapi.gui.comp.ITabbedPane;
import hu.scelightapibase.bean.settings.ISettingChangeListener;
import hu.scelightapibase.gui.icon.IRIcon;
import hu.scelightapibase.util.iface.Producer;
import hu.sllauncher.gui.comp.BorderPanel;
import hu.sllauncher.gui.comp.XLabel;
import hu.sllauncher.util.Holder;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.ToolTipManager;

/**
 * An extended {@link JTabbedPane}.
 * 
 * <p>
 * Supports hiding tabs in a way that hidden tabs (both tab title and content components) remain in the component tree (which means bounded
 * {@link ISettingChangeListener}s will properly be removed if {@link SettingsGui#removeAllBoundedScl(JComponent)} is called on a parent.
 * </p>
 * 
 * @author Andras Belicza
 */
@SuppressWarnings( "serial" )
public class XTabbedPane extends BorderPanel implements ITabbedPane {
	
	/** Wrapped {@link JTabbedPane}. */
	public final JTabbedPane wrappedTabbedPane = new JTabbedPane();
	
	/** Container to "park" title components of hidden tabs. */
	private final Box        titlesPark        = Box.createHorizontalBox();
	
	/** Container to "park" content components of hidden tabs. */
	private final Box        contentsPark      = Box.createHorizontalBox();
	
	/**
	 * Creates a new {@link XTabbedPane}.
	 */
	public XTabbedPane() {
		addCenter( wrappedTabbedPane );
		
		titlesPark.setVisible( false );
		contentsPark.setVisible( false );
		
		addNorth( titlesPark );
		addSouth( contentsPark );
	}
	
	@Override
	public XTabbedPane asBorderPanel() {
		return this;
	}
	
	@Override
	public JTabbedPane getWrappedTabbedPane() {
		return wrappedTabbedPane;
	}
	
	@Override
	public void addTab( final String title, final IRIcon ricon, final Producer< JComponent > tabProducer, final boolean addTabMnemonic ) {
		addTab( title, ricon, tabProducer, addTabMnemonic, false, null );
	}
	
	@Override
	public void addTab( final String title, final IRIcon ricon, final Producer< JComponent > tabProducer, final boolean addTabMnemonic,
	        final boolean closeable, final Runnable beforeCloseTask ) {
		
		// Wrapper component that listens to being shown first
		final BorderPanel p = new BorderPanel();
		p.addComponentListener( new ComponentAdapter() {
			@Override
			public void componentShown( final ComponentEvent event ) {
				p.addCenter( tabProducer.produce() );
				p.removeComponentListener( this );
				p.validate();
			}
		} );
		
		addTab( title, ricon, p, addTabMnemonic, closeable, beforeCloseTask );
	}
	
	@Override
	public void addTab( final String title, final IRIcon ricon, final JComponent tab, final boolean addTabMnemonic ) {
		addTab( title, ricon, tab, addTabMnemonic, false, null );
	}
	
	@Override
	public void addTab( final String title, final IRIcon ricon, final JComponent tab, final boolean addTabMnemonic, final boolean closeable,
	        final Runnable beforeCloseTask ) {
		
		wrappedTabbedPane.addTab( null, tab );
		
		final XLabel titleLabel;
		Character mnemonicChar = null;
		if ( addTabMnemonic ) {
			final int tabCount = wrappedTabbedPane.getTabCount();
			if ( tabCount == 1 )
				mnemonicChar = '1';
			else {
				final int lastMnemonic = wrappedTabbedPane.getMnemonicAt( tabCount - 2 );
				if ( lastMnemonic >= 0 && lastMnemonic < '9' )
					mnemonicChar = (char) ( lastMnemonic + 1 );
			}
		}
		if ( mnemonicChar != null ) {
			titleLabel = new XLabel( mnemonicChar + " " + title, ricon == null ? null : ricon.get(), SwingConstants.LEFT );
			titleLabel.setDisplayedMnemonicIndex( 0 );
			wrappedTabbedPane.setMnemonicAt( wrappedTabbedPane.getTabCount() - 1, mnemonicChar );
		} else
			titleLabel = new XLabel( title, ricon == null ? null : ricon.get(), SwingConstants.LEFT );
		
		if ( closeable ) {
			final Box titleBox = Box.createHorizontalBox();
			
			titleBox.add( titleLabel );
			final Holder< MouseListener > middleClickCloseListener = new Holder<>();
			final Action closeTabAction = new AbstractAction() {
				@Override
				public void actionPerformed( final ActionEvent event ) {
					if ( beforeCloseTask != null )
						beforeCloseTask.run();
					// Component is removed from the hierarchy, remove bounded setting change listeners!
					SettingsGui.removeAllBoundedScl( tab );
					wrappedTabbedPane.remove( tab );
					wrappedTabbedPane.removeMouseListener( middleClickCloseListener.value );
				}
			};
			
			final XLabel closeLabel = new XLabel( Icons.F_CROSS_SMALL.get() );
			closeLabel.setToolTipText( "Close tab (CTRL+W)" );
			closeLabel.setCursor( Cursor.getPredefinedCursor( Cursor.HAND_CURSOR ) );
			closeLabel.addMouseListener( new MouseAdapter() {
				@Override
				public void mousePressed( final MouseEvent event ) {
					closeTabAction.actionPerformed( null );
				};
			} );
			titleBox.add( closeLabel );
			
			// Middle click on tab title should close the tab
			middleClickCloseListener.value = new MouseAdapter() {
				@Override
				public void mousePressed( final MouseEvent event ) {
					if ( event.getButton() == GuiUtils.MOUSE_BTN_MIDDLE
					        && titleBox.contains( SwingUtilities.convertPoint( XTabbedPane.this, event.getPoint(), titleBox ) ) )
						closeTabAction.actionPerformed( null );
				};
			};
			wrappedTabbedPane.addMouseListener( middleClickCloseListener.value );
			
			// Register CTRL+W for tab close
			final Object closeTabActionKey = new Object();
			tab.getInputMap( JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT ).put( KeyStroke.getKeyStroke( KeyEvent.VK_W, InputEvent.CTRL_MASK ),
			        closeTabActionKey );
			tab.getActionMap().put( closeTabActionKey, closeTabAction );
			
			wrappedTabbedPane.setTabComponentAt( wrappedTabbedPane.getTabCount() - 1, titleBox );
		} else {
			wrappedTabbedPane.setTabComponentAt( wrappedTabbedPane.getTabCount() - 1, titleLabel );
		}
		
		// Tabs (tab labels) are not clickable if there are mouse listeners attached to it (JTabbedPane bug?)
		// Since XLabel registers itself at the tool tip manager which adds mouse listeners, unregister it to work.
		ToolTipManager.sharedInstance().unregisterComponent( titleLabel );
		
		// Move focus to the tab component, so hotkeys (key strokes) of the tab content can be used right away
		SwingUtilities.invokeLater( new Runnable() {
			@Override
			public void run() {
				tab.requestFocusInWindow();
			}
		} );
	}
	
	@Override
	public void hideTab( final int idx ) {
		// Adding a component to a container first removes it from its current parent.
		// To avoid complications, first store references, remove, then add to the new parent.
		
		final Component titleComp = wrappedTabbedPane.getTabComponentAt( idx );
		final Component contentComp = wrappedTabbedPane.getComponentAt( idx );
		
		wrappedTabbedPane.removeTabAt( idx );
		
		titlesPark.add( titleComp );
		contentsPark.add( contentComp );
	}
	
	@Override
	public void unhideTab( final int hiddenIdx, final int toIdx ) {
		// Adding a component to a container first removes it from its current parent.
		// To avoid complications, first store references, remove, then add to the new parent.
		
		final Component titleComp = titlesPark.getComponent( hiddenIdx );
		final Component contentComp = contentsPark.getComponent( hiddenIdx );
		
		titlesPark.remove( hiddenIdx );
		contentsPark.remove( hiddenIdx );
		
		wrappedTabbedPane.insertTab( null, null, contentComp, null, toIdx );
		wrappedTabbedPane.setTabComponentAt( toIdx, titleComp );
	}
	
	@Override
	public int getTabCount() {
		return wrappedTabbedPane.getTabCount();
	}
	
	@Override
	public int getHiddenTabCount() {
		return titlesPark.getComponentCount();
	}
	
}
