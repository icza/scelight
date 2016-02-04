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

import hu.sllauncher.action.XAction;
import hu.sllauncher.gui.icon.LIcons;
import hu.sllauncher.util.gui.LGuiUtils;
import hu.sllauncher.util.gui.adapter.ActionAdapter;
import hu.sllauncher.util.gui.adapter.DocumentAdapter;
import hu.sllauncher.util.gui.searcher.Searcher;

import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import javax.swing.event.DocumentEvent;

/**
 * A handy unified text search component.
 * 
 * <p>
 * It has a text field to input search term, and Next and Previous buttons. Also registers some key combo like clearing the search for ESC and focusing the
 * search text field for CTRL+S.
 * </p>
 * 
 * @author Andras Belicza
 */
@SuppressWarnings( "serial" )
public class TextSearchComp extends XToolBar {
	
	/** Text field to input search term. */
	public final XTextField textField = new XTextField( true );
	
	/** Search Forward action. */
	public final XAction    searchNextAction;
	
	/** Search Backward action. */
	public final XAction    searchPrevAction;
	
	
	/** Searcher logic to be called when the search term changes. */
	private Searcher        searcher;
	
	
	/**
	 * Creates a new {@link TextSearchComp} with the default text label <code>"Search"</code>.
	 * 
	 * @param scaleNextPrevButtons tells if the Next and Previous buttons should be created in a way that they follow the tool bar icon size setting; if false,
	 *            Next and Previous buttons will be normal buttons
	 */
	public TextSearchComp( final boolean scaleNextPrevButtons ) {
		this( scaleNextPrevButtons, "Search" );
	}
	
	/**
	 * Creates a new {@link TextSearchComp}.
	 * 
	 * @param scaleNextPrevButtons tells if the Next and Previous buttons should be created in a way that they follow the tool bar icon size setting; if false,
	 *            Next and Previous buttons will be normal buttons
	 * @param text text label to display before the search text field
	 */
	public TextSearchComp( final boolean scaleNextPrevButtons, final String text ) {
		super( false );
		
		add( new XLabel( text + ":" ) );
		// Search next when pressing Enter
		textField.addActionListener( new ActionAdapter() {
			@Override
			public void actionPerformed( final ActionEvent event ) {
				if ( searcher != null )
					searcher.performSearch( TextSearchComp.this, true, false );
			}
		} );
		textField.setToolTipText( "Case insensitive search" );
		// Search previous when pressing Shift+Enter
		// SHIFT+Enter does not generate action even, so:
		final Object actionKey = new Object();
		textField.getInputMap( JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT ).put( KeyStroke.getKeyStroke( KeyEvent.VK_ENTER, InputEvent.SHIFT_MASK ),
		        actionKey );
		textField.getActionMap().put( actionKey, new AbstractAction() {
			@Override
			public void actionPerformed( final ActionEvent event ) {
				if ( searcher != null )
					searcher.performSearch( TextSearchComp.this, false, false );
			}
		} );
		add( textField );
		
		
		// Create actions differently based on whether scaling is needed
		// Create with ricon (which will auto-scale) if scaling is needed
		searchNextAction = new XAction( scaleNextPrevButtons ? LIcons.F_ARROW_270 : null, "Next - Search Forward (Enter)", this ) {
			@Override
			public void actionPerformed( final ActionEvent event ) {
				if ( searcher != null )
					searcher.performSearch( TextSearchComp.this, true, false );
			}
		};
		searchPrevAction = new XAction( scaleNextPrevButtons ? LIcons.F_ARROW_090 : null, "Previous - Search Backward (Shift+Enter)", this ) {
			@Override
			public void actionPerformed( final ActionEvent event ) {
				if ( searcher != null )
					searcher.performSearch( TextSearchComp.this, false, false );
			}
		};
		if ( !scaleNextPrevButtons ) {
			// Add now the ricon (as the small icon) if scaling is not needed
			searchNextAction.putValue( Action.SMALL_ICON, LIcons.F_ARROW_270.get() );
			searchPrevAction.putValue( Action.SMALL_ICON, LIcons.F_ARROW_090.get() );
		}
		// List of buttons bounded to search text
		final List< JButton > searchEnabledButtonList = new ArrayList<>();
		searchEnabledButtonList.add( add( searchNextAction ) );
		searchEnabledButtonList.add( add( searchPrevAction ) );
		if ( scaleNextPrevButtons ) {
			for ( final JButton button : searchEnabledButtonList )
				LGuiUtils.autoCreateDisabledImage( button );
		}
		
		textField.getDocument().addDocumentListener( new DocumentAdapter( true ) {
			@Override
			public void handleEvent( final DocumentEvent event ) {
				final boolean hasSearchText = !textField.getText().isEmpty();
				
				for ( final JButton button : searchEnabledButtonList )
					button.setEnabled( hasSearchText );
				
				if ( searcher != null )
					searcher.performSearch( TextSearchComp.this, true, true );
			}
		} );
	}
	
	/**
	 * Registers the CTRL+S key stroke at the specified root component to focus the search text field when the key stroke is pressed.
	 * 
	 * @param rootComponent root component to register the CTRL+S key stroke at
	 * 
	 * @see #registerFocusHotkey(JComponent, KeyStroke)
	 * @see XTextField#registerFocusHotkey(JComponent, KeyStroke)
	 */
	public void registerFocusHotkey( final JComponent rootComponent ) {
		registerFocusHotkey( rootComponent, KeyStroke.getKeyStroke( KeyEvent.VK_S, InputEvent.CTRL_MASK ) );
	}
	
	/**
	 * Registers the specified key stroke at the specified root component to focus the search text field when the key stroke is pressed.
	 * 
	 * @param rootComponent root component to register the specified stroke at
	 * @param keyStroke key stroke to be registered to focus the search text field when pressed
	 * 
	 * @see XTextField#registerFocusHotkey(JComponent, KeyStroke)
	 */
	public void registerFocusHotkey( final JComponent rootComponent, final KeyStroke keyStroke ) {
		textField.registerFocusHotkey( rootComponent, keyStroke );
	}
	
	/**
	 * Returns the filter task.
	 * 
	 * @return the filter task
	 */
	public Searcher getSearcher() {
		return searcher;
	}
	
	/**
	 * Sets the searcher.
	 * 
	 * @param searcher the searcher to be set
	 */
	public void setSearcher( final Searcher searcher ) {
		this.searcher = searcher;
	}
	
}
