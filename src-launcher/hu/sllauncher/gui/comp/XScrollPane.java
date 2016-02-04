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
import hu.scelightapibase.gui.comp.IScrollPane;
import hu.sllauncher.gui.setting.LSettingsGui;
import hu.sllauncher.service.env.LEnv;
import hu.sllauncher.service.settings.LSettings;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JScrollPane;

/**
 * An improved {@link JScrollPane}.
 * 
 * @author Andras Belicza
 */
@SuppressWarnings( "serial" )
public class XScrollPane extends JScrollPane implements IScrollPane {
	
	/**
	 * Creates a new {@link XScrollPane} with small preferred size.
	 */
	public XScrollPane() {
		this( null );
	}
	
	/**
	 * Creates a new {@link XScrollPane} with small preferred size.
	 * 
	 * @param view the component to be scrolled
	 */
	public XScrollPane( final Component view ) {
		this( view, true, true );
	}
	
	/**
	 * Creates a new {@link XScrollPane}.
	 * 
	 * @param view the component to be scrolled
	 * @param smallSize tells if small preferred size is to be set to prefer vertical scrolling instead of horizontal
	 */
	public XScrollPane( final Component view, final boolean smallSize ) {
		this( view, smallSize, true );
	}
	
	/**
	 * Creates a new {@link XScrollPane}.
	 * 
	 * @param view the component to be scrolled
	 * @param smallSize tells if small preferred size is to be set to prefer vertical scrolling instead of horizontal
	 * @param followScrollingSetting tells if the vertical scrolling amount setting is to be followed ({@link LSettings#VERTICAL_SCROLL_AMOUNT})
	 */
	public XScrollPane( final Component view, final boolean smallSize, final boolean followScrollingSetting ) {
		super( view );
		
		// TODO when called with smallSize = false, in some cases scrolling with mouse wheel scroll does not work!!!!!!
		
		if ( smallSize )
			setPreferredSize( new Dimension( 1, 1 ) );
		
		if ( followScrollingSetting ) {
			// In some cases, the vertical scrolling is very slow.
			// Calling getVerticalScrollBar().setUnitIncrement() solves it. (This also affects the scroll bar's arrows. Good.)
			final ISettingChangeListener scl = new ISettingChangeListener() {
				@Override
				public void valuesChanged( final ISettingChangeEvent event ) {
					if ( event.affected( LSettings.VERTICAL_SCROLL_AMOUNT ) )
						getVerticalScrollBar().setUnitIncrement( event.get( LSettings.VERTICAL_SCROLL_AMOUNT ) / 3 );
				}
			};
			LSettingsGui.addBindExecuteScl( scl, LEnv.LAUNCHER_SETTINGS, LSettings.VERTICAL_SCROLL_AMOUNT.SELF_SET, this );
		}
	}
	
	@Override
	public XScrollPane asScrollPane() {
		return this;
	}
	
}
