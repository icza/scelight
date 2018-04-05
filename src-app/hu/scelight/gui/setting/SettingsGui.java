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
import hu.scelight.service.env.Env;
import hu.scelightapi.bean.settings.type.ITemplateSetting;
import hu.scelightapi.bean.settings.type.IValidatedMultilineStringSetting;
import hu.scelightapibase.bean.settings.ISettingChangeEvent;
import hu.scelightapibase.bean.settings.ISettingChangeListener;
import hu.scelightapibase.bean.settings.ISettingsBean;
import hu.scelightapibase.bean.settings.type.IBoolSetting;
import hu.scelightapibase.bean.settings.type.INodeSetting;
import hu.scelightapibase.util.iface.Consumer;
import hu.sllauncher.gui.comp.Link;
import hu.sllauncher.gui.comp.XCheckBox;
import hu.sllauncher.gui.setting.LSettingsGui;
import hu.sllauncher.util.gui.adapter.DocumentAdapter;

import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

import javax.swing.AbstractButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * GUI utilities for settings visualization and edit.
 * 
 * @author Andras Belicza
 */
public class SettingsGui extends LSettingsGui {
	
	/**
	 * Creates a {@link TemplateField} whose initial value is initialized from the specified {@link ITemplateSetting} and settings bean.
	 * 
	 * <p>
	 * Changing the value of the template field will update the setting value at the specified settings bean.
	 * </p>
	 * 
	 * <p>
	 * <i>Warning!</i> Updating the setting is done via a registered {@link DocumentListener}. In Swing the execution order of registered listeners is not
	 * specified. Custom listeners added to the returned template field might be called before the setting is updated! If it is required that the new setting be
	 * updated when a custom listener is called, pass the custom listener as the <code>customListener</code> argument here.
	 * </p>
	 * 
	 * @param setting setting to create the template field for
	 * @param settings settings bean storing the setting value
	 * @param customListener action listener which is guaranteed to be called <b>after</b> the specified setting has been updated
	 * @return a template field for the specified setting
	 */
	public static TemplateField createSettingTemplateField( final ITemplateSetting setting, final ISettingsBean settings, final ActionListener customListener ) {
		
		final TemplateField templateField = new TemplateField( settings.get( setting ) );
		templateField.textField.setColumns( 20 );

		
		templateField.textField.getDocument().addDocumentListener( new DocumentAdapter() {
			@Override
			public void handleEvent( final DocumentEvent event ) {
				settings.set( setting, templateField.getTemplate() );
				
				if ( customListener != null )
					customListener.actionPerformed( null );
			}
		} );
		
		return templateField;
	}
	
	/**
	 * Creates an {@link IndicatorTextArea} whose initial value is initialized from the specified {@link IValidatedMultilineStringSetting} and settings bean.
	 * 
	 * <p>
	 * Changing the value of the text area will update the setting value at the specified settings bean.
	 * </p>
	 * 
	 * <p>
	 * <i>Warning!</i> Updating the setting is done via a registered {@link DocumentListener}. In Swing the execution order of registered listeners is not
	 * specified. Custom listeners added to the returned text area might be called before the setting is updated! If it is required that the new setting be
	 * updated when a custom listener is called, pass the custom listener as the <code>customListener</code> argument here.
	 * </p>
	 * 
	 * @param setting setting to create the indicator text area for
	 * @param settings settings bean storing the setting value
	 * @param customListener action listener which is guaranteed to be called <b>after</b> the specified setting has been updated
	 * @return a text field for the specified setting
	 */
	public static IndicatorTextArea createSettingIndicatorTextArea( final IValidatedMultilineStringSetting setting, final ISettingsBean settings,
	        final ActionListener customListener ) {
		
		final IndicatorTextArea indicatorTextArea = new IndicatorTextArea( settings.get( setting ) );
		indicatorTextArea.textArea.setRows( 3 );
		indicatorTextArea.textArea.setColumns( 20 );
		
		
		indicatorTextArea.textArea.getDocument().addDocumentListener( new DocumentAdapter() {
			@Override
			public void handleEvent( final DocumentEvent event ) {
				settings.set( setting, indicatorTextArea.getText() );
				
				if ( customListener != null )
					customListener.actionPerformed( null );
			}
		} );
		
		return indicatorTextArea;
	}
	
	/**
	 * Creates a link which when clicked opens the settings dialog having the specified node setting selected.
	 * 
	 * @param nodeSetting node setting to select by default
	 * @return a link which when clicked opens the settings dialog having the specified node setting selected
	 */
	public static Link createSettingLink( final INodeSetting nodeSetting ) {
		return createSettingLink( nodeSetting, nodeSetting.getName() + " settings..." );
	}
	
	/**
	 * Creates a link which when clicked opens the settings dialog having the specified node setting selected.
	 * 
	 * @param nodeSetting node setting to select by default
	 * @param text text of the link
	 * @return a link which when clicked opens the settings dialog having the specified node setting selected
	 */
	public static Link createSettingLink( final INodeSetting nodeSetting, final String text ) {
		final Link link = new Link( text, new Consumer< MouseEvent >() {
			@Override
			public void consume( final MouseEvent event ) {
				new SettingsDialog( Env.MAIN_FRAME, nodeSetting ).setVisible( true );
			}
		} );
		link.setIcon( nodeSetting.getViewHints().getRicon().get() );
		
		return link;
	}
	
	/**
	 * Creates an {@link XCheckBox} whose initial value is initialized from the specified {@link IBoolSetting} and settings bean.
	 * 
	 * <p>
	 * Changing the value of the check box will update the setting value at the specified settings bean.
	 * </p>
	 * 
	 * <p>
	 * The created and returned check box will also be bound to the setting value: if the setting value is modified outside at the specified settings bean, the
	 * selected value of the check box will be changed accordingly.<br>
	 * This bounding is implemented with a {@link ISettingChangeListener} stored as a client property. To remove the bound, call
	 * {@link #removeAllBoundedScl(JComponent)}.
	 * </p>
	 * 
	 * <p>
	 * <i>Warning!</i> Updating the setting is done via a registered {@link ActionListener}. In Swing the execution order of registered listeners is not
	 * specified. Custom listeners added to the returned check box might be called before the setting is updated! If it is required that the new setting be
	 * updated when a custom listener is called, pass the custom listener as the <code>customListener</code> argument here.
	 * </p>
	 * 
	 * @param setting setting to create the check box for
	 * @param settings settings bean storing the setting value
	 * @param customListener action listener which is guaranteed to be called <b>after</b> the specified setting has been updated
	 * @return a check box for the specified setting
	 * 
	 * @see #removeAllBoundedScl(JComponent)
	 */
	public static XCheckBox createBoundedSettingCheckBox( final IBoolSetting setting, final ISettingsBean settings, final ActionListener customListener ) {
		
		final XCheckBox checkBox = createSettingCheckBox( setting, settings, customListener );
		
		bindSelectionToSetting( checkBox, setting, settings );
		
		return checkBox;
	}
	
	/**
	 * Binds the visibility of the specified component to the specified {@link IBoolSetting} from the specified {@link ISettingsBean}.
	 * 
	 * @param comp component whose visibility to be bounded
	 * @param setting setting to control the visibility of the component
	 * @param settings settings bean storing the setting
	 */
	public static void bindVisibilityToSetting( final JComponent comp, final IBoolSetting setting, final ISettingsBean settings ) {
		final ISettingChangeListener scl = new ISettingChangeListener() {
			@Override
			public void valuesChanged( final ISettingChangeEvent event ) {
				if ( event.affected( setting ) )
					comp.setVisible( event.get( setting ) );
			}
		};
		addBindExecuteScl( scl, settings, setting.selfSet(), comp );
	}
	
	/**
	 * Binds the selection state of the specified abstract button (which can be e.g. a {@link XCheckBox} or a {@link JCheckBoxMenuItem}) to the specified
	 * {@link IBoolSetting} from the specified {@link ISettingsBean}.
	 * 
	 * @param button button whose state to be bounded
	 * @param setting setting to control the visibility of the component
	 * @param settings settings bean storing the setting
	 */
	public static void bindSelectionToSetting( final AbstractButton button, final IBoolSetting setting, final ISettingsBean settings ) {
		final ISettingChangeListener scl = new ISettingChangeListener() {
			@Override
			public void valuesChanged( final ISettingChangeEvent event ) {
				if ( event.affected( setting ) )
					button.setSelected( event.get( setting ) );
			}
		};
		addBindExecuteScl( scl, settings, setting.selfSet(), button );
	}
	
}
