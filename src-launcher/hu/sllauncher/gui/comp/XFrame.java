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

import hu.scelightapibase.bean.settings.ISettingChangeEvent;
import hu.scelightapibase.bean.settings.ISettingChangeListener;
import hu.sllauncher.gui.setting.LSettingsGui;
import hu.sllauncher.service.env.LEnv;
import hu.sllauncher.service.settings.LSettings;

import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 * A customized {@link JFrame} class.
 * 
 * @author Andras Belicza
 */
@SuppressWarnings( "serial" )
public class XFrame extends JFrame {
	
	/** Custom border panel functioning as the content panel. */
	protected final BorderPanel cp = new BorderPanel();
	
	/** Component to be focused on first show. */
	protected JComponent        focusTargetOnFirstShow;
	
	/**
	 * Creates a new {@link XFrame}.
	 */
	public XFrame() {
		setDefaultCloseOperation( DO_NOTHING_ON_CLOSE );
		
		addWindowListener( new WindowAdapter() {
			@Override
			public void windowClosing( final WindowEvent event ) {
				close();
			}
		} );
		
		setContentPane( cp );
		
		// Register window listener to focus intended component on first activation
		addWindowListener( new WindowAdapter() {
			@Override
			public void windowActivated( final WindowEvent event ) {
				removeWindowListener( this ); // One time focusing, remove ourselves
				if ( focusTargetOnFirstShow != null ) {
					// TODO need both?
					focusTargetOnFirstShow.requestFocusInWindow();
					SwingUtilities.invokeLater( new Runnable() {
						@Override
						public void run() {
							focusTargetOnFirstShow.requestFocusInWindow();
						}
					} );
				}
			}
		} );
		
		// Register a setting change listener to repaint if icon scaling quality changes
		ISettingChangeListener scl = new ISettingChangeListener() {
			@Override
			public void valuesChanged( final ISettingChangeEvent event ) {
				if ( event.affected( LSettings.ICON_SCALING_QUALITY ) )
					cp.repaint();
			}
		};
		LSettingsGui.addBindExecuteScl( scl, LEnv.LAUNCHER_SETTINGS, LSettings.ICON_SCALING_QUALITY.SELF_SET, cp );
	}
	
	/**
	 * Sets the component to be focused on first show (when dialog is first activated).
	 * 
	 * @param focusTarget focus target component on first show to be set
	 */
	protected void setFocusTargetOnFirstShow( final JComponent focusTarget ) {
		focusTargetOnFirstShow = focusTarget;
	}
	
	/**
	 * Closes the frame.
	 * 
	 * @see #customOnClose()
	 */
	public void close() {
		// First close all child windows
		for ( final Window w : getOwnedWindows() ) {
			if ( w instanceof XFrame )
				( (XFrame) w ).close();
			if ( w instanceof XDialog )
				( (XDialog) w ).close();
		}
		
		customOnClose();
		LSettingsGui.removeAllBoundedScl( cp );
		dispose();
	}
	
	/**
	 * This method is called before the dialog is closed.<br>
	 * This implementation does nothing, it's here to be overridden if needed.
	 * 
	 * @see #close()
	 */
	protected void customOnClose() {
		// Subclasses will optionally do something here.
	}
	
}
