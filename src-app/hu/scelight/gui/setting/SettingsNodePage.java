/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight.gui.setting;

import hu.scelight.gui.comp.IndicatorTextArea;
import hu.scelight.gui.comp.TemplateField;
import hu.scelightapi.bean.settings.type.ITemplateSetting;
import hu.scelightapi.bean.settings.type.IValidatedMultilineStringSetting;
import hu.scelightapibase.bean.settings.ISettingsBean;
import hu.scelightapibase.bean.settings.type.INodeSetting;
import hu.scelightapibase.bean.settings.type.ISetting;
import hu.sllauncher.gui.comp.GridBagPanel;
import hu.sllauncher.gui.setting.LSettingsNodePage;

import java.awt.event.ActionListener;

import javax.swing.JComponent;

/**
 * A node page displaying settings view/edit components of the application.
 * 
 * @author Andras Belicza
 */
public class SettingsNodePage extends LSettingsNodePage {
	
	/**
	 * Creates a new {@link SettingsNodePage}.
	 * 
	 * @param nodeSetting node setting the page is associated with
	 */
	public SettingsNodePage( final INodeSetting nodeSetting ) {
		super( nodeSetting );
	}
	
	/**
	 * Overridden to handle new setting types of the application.
	 */
	@Override
	protected JComponent addSettingComponent( final GridBagPanel p, final ISetting< ? > setting, final ISettingsBean settings,
	        final ActionListener customListener ) {
		final JComponent settingComp;
		
		if ( setting instanceof ITemplateSetting ) {
			
			// Template field
			p.addSingle( settingComp = SettingsGui.createSettingTemplateField( (ITemplateSetting) setting, settings, customListener ) );
			if ( setting.getViewHints().getDialogTitle() != null )
				( (TemplateField) settingComp ).setDialogTitle( setting.getViewHints().getDialogTitle() );
			if ( setting.getViewHints().getColumns() != null )
				( (TemplateField) settingComp ).textField.setColumns( setting.getViewHints().getColumns() );
			
		} else if ( setting instanceof IValidatedMultilineStringSetting ) {
			
			// Indicator Text area
			p.addSingle( settingComp = SettingsGui.createSettingIndicatorTextArea( (IValidatedMultilineStringSetting) setting, settings, customListener ) );
			if ( setting.getViewHints().getRows() != null )
				( (IndicatorTextArea) settingComp ).textArea.setRows( setting.getViewHints().getRows() );
			if ( setting.getViewHints().getColumns() != null )
				( (IndicatorTextArea) settingComp ).textArea.setColumns( setting.getViewHints().getColumns() );
						
		} else
			settingComp = super.addSettingComponent( p, setting, settings, customListener );
		
		return settingComp;
	}
	
	/**
	 * Overridden to handle components of new setting types of the application.
	 */
	@Override
	protected void setComponentValue( final JComponent settingComp, final Object value ) {
		if ( settingComp instanceof TemplateField )
			( (TemplateField) settingComp ).setTemplate( (String) value );
		else if ( settingComp instanceof IndicatorTextArea )
			( (IndicatorTextArea) settingComp ).setText( (String) value );
		else
			super.setComponentValue( settingComp, value );
	}
	
}
