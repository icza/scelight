/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight.action;

import hu.scelightapibase.bean.settings.ISettingChangeEvent;
import hu.scelightapibase.bean.settings.ISettingChangeListener;
import hu.sllauncher.action.XAction;
import hu.sllauncher.bean.settings.SettingsBean;
import hu.sllauncher.bean.settings.type.BoolSetting;
import hu.sllauncher.gui.icon.LRIcon;

import java.awt.event.ActionEvent;

import javax.swing.Action;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.KeyStroke;

/**
 * An action which binds the {@link Action#SELECTED_KEY} to the specified {@link BoolSetting}.
 * 
 * <p>
 * Note: since action instances are used for the lifetime of the app, the registered {@link ISettingChangeListener} is never removed!
 * </p>
 * 
 * @author Andras Belicza
 */
@SuppressWarnings( "serial" )
public class BoolSettingAction extends XAction {
	
	/** Bool setting to bind the {@link Action#SELECTED_KEY} to. */
	private final BoolSetting  setting;
	
	/** Settings bean storing the setting. */
	private final SettingsBean settings;
	
	/**
	 * Creates a new {@link BoolSettingAction}.
	 * 
	 * @param ricon optional ricon of the action
	 * @param text text of the action
	 * @param setting bool setting to bind the {@link Action#SELECTED_KEY} to
	 * @param settings settings bean storing the setting
	 */
	public BoolSettingAction( final LRIcon ricon, final String text, final BoolSetting setting, final SettingsBean settings ) {
		this( null, ricon, text, setting, settings );
	}
	
	/**
	 * Creates a new {@link BoolSettingAction}.
	 * 
	 * @param keyStroke optional hotkey of the action
	 * @param ricon optional ricon of the action
	 * @param text text of the action
	 * @param setting bool setting to bind the {@link Action#SELECTED_KEY} to
	 * @param settings settings bean storing the setting
	 */
	public BoolSettingAction( final KeyStroke keyStroke, final LRIcon ricon, final String text, final BoolSetting setting, final SettingsBean settings ) {
		super( keyStroke, ricon, text );
		
		this.setting = setting;
		this.settings = settings;
		
		final ISettingChangeListener scl = new ISettingChangeListener() {
			@Override
			public void valuesChanged( final ISettingChangeEvent event ) {
				if ( event.affected( setting ) )
					putValue( SELECTED_KEY, event.get( setting ) );
			}
		};
		settings.addAndExecuteChangeListener( setting, scl );
		
		// Retain listener reference:
		putValue( "boundedScl", scl );
	}
	
	/**
	 * Overrides {@link XAction#addToMenu(JMenu)} to add a {@link JCheckBoxMenuItem} instead of a {@link JMenuItem}.
	 */
	@Override
	public JMenuItem addToMenu( final JMenu menu ) {
		final JCheckBoxMenuItem mi = new JCheckBoxMenuItem();
		
		menu.add( mi );
		mi.setAction( this );
		mi.setToolTipText( null ); // We don't want tool tip on menu items...
		
		return mi;
	}
	
	/**
	 * Overrides {@link XAction#addToMenu(JPopupMenu)} to add a {@link JCheckBoxMenuItem} instead of a {@link JMenuItem}.
	 */
	@Override
	public JMenuItem addToMenu( final JPopupMenu menu ) {
		final JCheckBoxMenuItem mi = new JCheckBoxMenuItem();
		
		menu.add( mi );
		mi.setAction( this );
		mi.setToolTipText( null ); // We don't want tool tip on menu items...
		
		return mi;
	}
	
	@Override
	public void actionPerformed( final ActionEvent event ) {
		settings.set( setting, !settings.get( setting ) );
	}
	
}
