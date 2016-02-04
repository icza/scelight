/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.sllauncher.action;

import hu.scelightapibase.action.IAction;
import hu.scelightapibase.bean.settings.ISettingChangeEvent;
import hu.scelightapibase.bean.settings.ISettingChangeListener;
import hu.scelightapibase.gui.icon.IRIcon;
import hu.sllauncher.gui.setting.LSettingsGui;
import hu.sllauncher.service.env.LEnv;
import hu.sllauncher.service.settings.LSettings;
import hu.sllauncher.util.gui.LGuiUtils;
import hu.sllauncher.util.gui.TextWithMnemonic;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.KeyStroke;

/**
 * Base of actions.
 * 
 * @author Andras Belicza
 */
@SuppressWarnings( "serial" )
public abstract class XAction extends AbstractAction implements IAction {
	
	/** Ricon of the action. */
	public final IRIcon                  ricon;
	
	/** To retain a reference to the setting change listener. */
	private final ISettingChangeListener scl;
	
	/**
	 * Creates a new {@link XAction}.
	 * 
	 * @param ricon optional ricon of the action
	 * @param text text of the action
	 */
	public XAction( final IRIcon ricon, final String text ) {
		this( null, ricon, text );
	}
	
	/**
	 * Creates a new {@link XAction}.
	 * 
	 * @param ricon optional ricon of the action
	 * @param text text of the action
	 * @param comp component to bind the action's setting change listener to; basically in this case the lifetime of the scl will be bound to the specified
	 *            component's lifetime
	 */
	public XAction( final IRIcon ricon, final String text, final JComponent comp ) {
		this( null, ricon, text, comp );
	}
	
	/**
	 * Creates a new {@link XAction}.
	 * 
	 * @param keyStroke optional hotkey of the action
	 * @param ricon optional ricon of the action
	 * @param text text of the action
	 */
	public XAction( final KeyStroke keyStroke, final IRIcon ricon, final String text ) {
		this( keyStroke, ricon, text, null );
	}
	
	/**
	 * Creates a new {@link XAction}.
	 * 
	 * @param keyStroke optional hotkey of the action
	 * @param ricon optional ricon of the action
	 * @param text text of the action
	 * @param comp component to bind the action's setting change listener to; basically in this case the lifetime of the scl will be bound to the specified
	 *            component's lifetime
	 */
	public XAction( final KeyStroke keyStroke, final IRIcon ricon, final String text, final JComponent comp ) {
		this.ricon = ricon;
		
		if ( keyStroke != null )
			putValue( ACCELERATOR_KEY, keyStroke );
		
		final TextWithMnemonic twm = new TextWithMnemonic( text );
		
		// Visible text for buttons or menu items
		putValue( NAME, twm.text );
		
		// Tool tip text, include key stroke
		putValue( SHORT_DESCRIPTION, keyStroke == null ? twm.text : twm.text + LGuiUtils.keyStrokeToString( keyStroke ) );
		
		// Mnemonic key
		if ( twm.hasMnemonic() )
			putValue( MNEMONIC_KEY, (int) twm.mnemonic );
		
		if ( ricon != null ) {
			// Icon for menu items
			putValue( SMALL_ICON, ricon.get() );
			
			// Icon for buttons (we use it for tool bar buttons)
			// Use a size specified by the tool bar icon size setting
			// Register a setting change listener to change the large icon size
			scl = new ISettingChangeListener() {
				@Override
				public void valuesChanged( final ISettingChangeEvent event ) {
					if ( event.affected( LSettings.TOOL_BAR_ICON_SIZE ) ) {
						// Changing the value will fire a property change listener, and tool bar buttons are listening to those
						// and will change their icons automatically, and repaint themselves automatically!
						putValue( AbstractAction.LARGE_ICON_KEY, ricon.size( event.get( LSettings.TOOL_BAR_ICON_SIZE ) ) );
					}
				}
			};
			LEnv.LAUNCHER_SETTINGS.addAndExecuteChangeListener( LSettings.TOOL_BAR_ICON_SIZE, scl );
		} else
			scl = null;
		
		// Bind scl to a component if specified
		if ( comp != null )
			bindScl( comp );
	}
	
	/**
	 * Binds the action's {@link ISettingChangeListener} (if there is any) to a component.
	 * 
	 * <p>
	 * This method must be called for all actions that has a shorter lifetime than the app's lifetime!<br>
	 * Also this method must be called only once (if it would be bounded to multiple components, disposing one would remove the listener...).
	 * </p>
	 * 
	 * <p>
	 * Background: when a dialog or frame is closed, all {@link ISettingChangeListener}s bounded to that dialog or frame are automatically removed (by calling
	 * {@link LSettingsGui#removeAllBoundedScl(JComponent)}). This only removes scl's bounded to components, it does not / cannot remove scl's of actions.<br>
	 * This method binds the action's scl to a component so it will also be removed when the aforementioned cleanup code runs.
	 * </p>
	 * 
	 * @param comp component to bind the action's setting change listener to
	 */
	private void bindScl( final JComponent comp ) {
		if ( scl != null )
			LSettingsGui.bindScl( scl, LEnv.LAUNCHER_SETTINGS, LSettings.TOOL_BAR_ICON_SIZE.SELF_SET, comp );
	}
	
	@Override
	public IRIcon getRicon() {
		return ricon;
	}
	
	@Override
	public String getName() {
		return (String) getValue( NAME );
	}
	
	@Override
	public String getShortDescription() {
		return (String) getValue( SHORT_DESCRIPTION );
	}
	
	@Override
	public JMenuItem addToMenu( final JMenu menu ) { // XMenu is not part of the launcher...
		final JMenuItem mi = menu.add( this );
		mi.setToolTipText( null ); // We don't want tool tip on menu items...
		return mi;
	}
	
	@Override
	public JMenuItem addToMenu( final JPopupMenu menu ) { // XPopupMenu is not part of the launcher...
		final JMenuItem mi = menu.add( this );
		mi.setToolTipText( null ); // We don't want tool tip on menu items...
		return mi;
	}
	
}
