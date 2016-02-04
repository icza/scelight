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

import hu.sllauncher.gui.icon.LIcons;
import hu.sllauncher.util.gui.adapter.DocumentAdapter;

import java.awt.Color;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import javax.swing.event.DocumentEvent;

/**
 * A handy unified text filter component.
 * 
 * <p>
 * It has a text field to input filter term, and a clear icon to clear the filter. Also registers some key combo like clearing the filter for ESC and focusing
 * the filter text field for CTRL+F.
 * </p>
 * 
 * @author Andras Belicza
 */
@SuppressWarnings( "serial" )
public class TextFilterComp extends Box {
	
	/** Text field to input filter term. */
	public final XTextField textField = new XTextField( true );
	
	/** Filter task to be called when the filter term changes. */
	private Runnable        filterTask;
	
	/**
	 * Creates a new {@link TextFilterComp} with the default text label <code>"Filter"</code>.
	 */
	public TextFilterComp() {
		this( "Filter" );
	}
	
	/**
	 * Creates a new {@link TextFilterComp}.
	 * 
	 * @param text text label to display before the filter text field
	 */
	public TextFilterComp( final String text ) {
		super( BoxLayout.X_AXIS );
		
		final XLabel clearFilterLabel = new XLabel( LIcons.F_CROSS.get() );
		
		add( new XLabel( text + ":" ) );
		textField.getDocument().addDocumentListener( new DocumentAdapter( true ) {
			@Override
			public void handleEvent( final DocumentEvent event ) {
				// Indicate with green background if filter is active!
				final String filterText = textField.getText();
				final boolean filterActive = !filterText.isEmpty();
				textField.setBackground( filterActive ? Color.GREEN : null );
				clearFilterLabel.setEnabled( filterActive );
				
				if ( filterTask != null )
					filterTask.run();
			}
		} );
		add( textField );
		
		clearFilterLabel.setToolTipText( "Clear Filter (ESC)" );
		clearFilterLabel.addMouseListener( new MouseAdapter() {
			@Override
			public void mousePressed( final MouseEvent event ) {
				textField.setText( null );
			}
		} );
		add( clearFilterLabel );
	}
	
	/**
	 * Registers the CTRL+F key stroke at the specified root component to focus the filter text field when the key stroke is pressed.
	 * 
	 * @param rootComponent root component to register the CTRL+F key stroke at
	 * 
	 * @see #registerFocusHotkey(JComponent, KeyStroke)
	 */
	public void registerFocusHotkey( final JComponent rootComponent ) {
		registerFocusHotkey( rootComponent, KeyStroke.getKeyStroke( KeyEvent.VK_F, InputEvent.CTRL_MASK ) );
	}
	
	/**
	 * Registers the specified key stroke at the specified root component to focus the filter text field when the key stroke is pressed.
	 * 
	 * @param rootComponent root component to register the specified stroke at
	 * @param keyStroke key stroke to be registered to focus the filter text field when pressed
	 */
	public void registerFocusHotkey( final JComponent rootComponent, final KeyStroke keyStroke ) {
		textField.registerFocusHotkey( rootComponent, keyStroke );
	}
	
	/**
	 * Returns the filter task.
	 * 
	 * @return the filter task
	 */
	public Runnable getFilterTask() {
		return filterTask;
	}
	
	/**
	 * Sets the filter task.
	 * 
	 * @param filterTask the filter task to be set
	 */
	public void setFilterTask( final Runnable filterTask ) {
		this.filterTask = filterTask;
	}
	
}
