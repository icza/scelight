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
import hu.scelightapibase.gui.comp.ISpinner;
import hu.sllauncher.gui.setting.LSettingsGui;
import hu.sllauncher.service.env.LEnv;
import hu.sllauncher.service.lang.DateFormatE;
import hu.sllauncher.service.lang.NumberFormatE;
import hu.sllauncher.service.settings.LSettings;

import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

/**
 * An extended {@link JSpinner}.
 * 
 * <p>
 * Sets / applies the number and date formats of the settings.
 * </p>
 * 
 * @author Andras Belicza
 */
@SuppressWarnings( "serial" )
public class XSpinner extends JSpinner implements ISpinner {
	
	/**
	 * Creates a new {@link XSpinner} with an {@link Integer} {@link SpinnerNumberModel} with initial value 0 and no minimum or maximum limits.
	 */
	public XSpinner() {
		this( new SpinnerNumberModel( Integer.valueOf( 0 ), null, null, Integer.valueOf( 1 ) ) );
	}
	
	/**
	 * Creates a new {@link XSpinner}.
	 * 
	 * @param model model to create the spinner with
	 */
	public XSpinner( final SpinnerModel model ) {
		super( model );
		
		if ( getEditor() instanceof JSpinner.NumberEditor ) {
			
			// Setting listener to listen to number format changes
			final ISettingChangeListener scl = new ISettingChangeListener() {
				@Override
				public void valuesChanged( final ISettingChangeEvent event ) {
					if ( event.affected( LSettings.NUMBER_FORMAT ) ) {
						// Apply number format
						final NumberFormatE numberFormatE = event.get( LSettings.NUMBER_FORMAT );
						numberFormatE.configureDecimalFormat( ( (NumberEditor) getEditor() ).getFormat() );
						fireStateChanged();
					}
				}
			};
			LSettingsGui.addBindExecuteScl( scl, LEnv.LAUNCHER_SETTINGS, LSettings.NUMBER_FORMAT.SELF_SET, this );
			
		} else if ( getEditor() instanceof JSpinner.DateEditor ) {
			
			// Setting listener to listen to date format changes
			final ISettingChangeListener scl = new ISettingChangeListener() {
				@Override
				public void valuesChanged( final ISettingChangeEvent event ) {
					if ( event.affected( LSettings.DATE_FORMAT ) ) {
						// Apply date format
						final DateFormatE dateFormatE = event.get( LSettings.DATE_FORMAT );
						( (DateEditor) getEditor() ).getFormat().applyPattern( dateFormatE.pattern + " HH:mm:ss" );
						fireStateChanged();
					}
				}
			};
			LSettingsGui.addBindExecuteScl( scl, LEnv.LAUNCHER_SETTINGS, LSettings.DATE_FORMAT.SELF_SET, this );
			
		}
	}
	
	@Override
	public XSpinner asSpinner() {
		return this;
	}
	
}
