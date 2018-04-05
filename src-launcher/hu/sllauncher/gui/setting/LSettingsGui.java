/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.sllauncher.gui.setting;

import hu.scelightapibase.bean.settings.ISettingChangeEvent;
import hu.scelightapibase.bean.settings.ISettingChangeListener;
import hu.scelightapibase.bean.settings.ISettingsBean;
import hu.scelightapibase.bean.settings.type.IBoolSetting;
import hu.scelightapibase.bean.settings.type.IFixedValuesSetting;
import hu.scelightapibase.bean.settings.type.IIntSetting;
import hu.scelightapibase.bean.settings.type.IMultilineStringSetting;
import hu.scelightapibase.bean.settings.type.IPathSetting;
import hu.scelightapibase.bean.settings.type.ISetting;
import hu.scelightapibase.bean.settings.type.IStringSetting;
import hu.scelightapibase.bean.settings.type.IValidatedStringSetting;
import hu.scelightapibase.util.ISkillLevel;
import hu.sllauncher.bean.settings.type.Setting;
import hu.sllauncher.gui.comp.IndicatorTextField;
import hu.sllauncher.gui.comp.PathField;
import hu.sllauncher.gui.comp.XCheckBox;
import hu.sllauncher.gui.comp.XSpinner;
import hu.sllauncher.gui.comp.XTextArea;
import hu.sllauncher.gui.comp.XTextField;
import hu.sllauncher.gui.comp.combobox.XComboBox;
import hu.sllauncher.service.env.LEnv;
import hu.sllauncher.service.settings.LSettings;
import hu.sllauncher.util.Pair;
import hu.sllauncher.util.SkillLevel;
import hu.sllauncher.util.gui.adapter.ActionAdapter;
import hu.sllauncher.util.gui.adapter.DocumentAdapter;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.swing.AbstractButton;
import javax.swing.JComponent;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * GUI utilities for settings visualization and edit.
 * 
 * @author Andras Belicza
 */
public class LSettingsGui {
	

	
	/**
	 * Creates an {@link XComboBox} whose values and initial selected value is initialized from the specified {@link IFixedValuesSetting} and settings bean.
	 * 
	 * <p>
	 * Changing the selected value of the combo box will update the setting value at the specified settings bean.
	 * </p>
	 * 
	 * <p>
	 * <i>Warning!</i> Updating the setting is done via a registered {@link ActionListener}. In Swing the execution order of registered listeners is not
	 * specified. Custom listeners added to the returned combo box might be called before the setting is updated! If it is required that the new setting be
	 * updated when a custom listener is called, pass the custom listener as the <code>customListener</code> argument here.
	 * </p>
	 * 
	 * @param <T> type of the setting values
	 * @param setting setting to create the combo box for
	 * @param settings settings bean storing the setting value
	 * @param customListener action listener which is guaranteed to be called <b>after</b> the specified setting has been updated
	 * @return a combo box for the specified setting
	 */
	public static < T > XComboBox< T > createSettingComboBox( final IFixedValuesSetting< T > setting, final ISettingsBean settings,
	        final ActionListener customListener ) {
		
		final XComboBox< T > comboBox = new XComboBox<>( setting.getValues() );

		
		comboBox.setSelectedItem( settings.get( setting ) );
		comboBox.addActionListener( new ActionAdapter() {
			@Override
			public void actionPerformed( final ActionEvent event ) {
				settings.set( setting, comboBox.getSelectedItem() );
				
				if ( customListener != null )
					customListener.actionPerformed( event );
			}
		} );
		
		return comboBox;
	}
	
	/**
	 * Creates an {@link XCheckBox} whose initial value is initialized from the specified {@link IBoolSetting} and settings bean.
	 * 
	 * <p>
	 * Changing the value of the check box will update the setting value at the specified settings bean.
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
	 */
	public static XCheckBox createSettingCheckBox( final IBoolSetting setting, final ISettingsBean settings, final ActionListener customListener ) {
		
		final XCheckBox checkBox = new XCheckBox( setting.getName(), settings.get( setting ) );
		
		checkBox.addActionListener( new ActionAdapter() {
			@Override
			public void actionPerformed( final ActionEvent event ) {
				settings.set( setting, checkBox.isSelected() );
				
				if ( customListener != null )
					customListener.actionPerformed( event );
			}
		} );
		
		return checkBox;
	}
	
	/**
	 * Creates an {@link XSpinner} whose initial value is initialized from the specified {@link IIntSetting} and settings bean.
	 * 
	 * <p>
	 * Changing the value of the spinner will update the setting value at the specified settings bean.
	 * </p>
	 * 
	 * <p>
	 * <i>Warning!</i> Updating the setting is done via a registered {@link ActionListener}. In Swing the execution order of registered listeners is not
	 * specified. Custom listeners added to the returned spinner might be called before the setting is updated! If it is required that the new setting be
	 * updated when a custom listener is called, pass the custom listener as the <code>customListener</code> argument here.
	 * </p>
	 * 
	 * @param setting setting to create the spinner for
	 * @param settings settings bean storing the setting value
	 * @param customListener action listener which is guaranteed to be called <b>after</b> the specified setting has been updated
	 * @return a spinner for the specified setting
	 */
	public static XSpinner createSettingSpinner( final IIntSetting setting, final ISettingsBean settings, final ActionListener customListener ) {
		
		final XSpinner spinner = new XSpinner( new SpinnerNumberModel( settings.get( setting ).intValue(), setting.getMinValue().intValue(), setting
		        .getMaxValue().intValue(), 1 ) );
		
		// TODO should register a change listener to NumberFormat, and when that changes, reset the tool tip?
		spinner.setToolTipText( "Valid range: " + LEnv.LANG.formatNumber( setting.getMinValue() ) + ".." + LEnv.LANG.formatNumber( setting.getMaxValue() )
		        + "; Default: " + LEnv.LANG.formatNumber( setting.getDefaultValue() ) );
		
		
		spinner.addChangeListener( new ChangeListener() {
			@Override
			public void stateChanged( final ChangeEvent event ) {
				settings.set( setting, ( (Number) spinner.getValue() ).intValue() );
				
				if ( customListener != null )
					customListener.actionPerformed( null );
			}
		} );
		
		return spinner;
	}
	
	/**
	 * Creates an {@link XTextField} whose initial value is initialized from the specified {@link IStringSetting} and settings bean.
	 * 
	 * <p>
	 * Changing the value of the text field will update the setting value at the specified settings bean.
	 * </p>
	 * 
	 * <p>
	 * <i>Warning!</i> Updating the setting is done via a registered {@link DocumentListener}. In Swing the execution order of registered listeners is not
	 * specified. Custom listeners added to the returned text field might be called before the setting is updated! If it is required that the new setting be
	 * updated when a custom listener is called, pass the custom listener as the <code>customListener</code> argument here.
	 * </p>
	 * 
	 * @param setting setting to create the text field for
	 * @param settings settings bean storing the setting value
	 * @param customListener action listener which is guaranteed to be called <b>after</b> the specified setting has been updated
	 * @return a text field for the specified setting
	 */
	public static XTextField createSettingTextField( final IStringSetting setting, final ISettingsBean settings, final ActionListener customListener ) {
		
		final XTextField textField = new XTextField( settings.get( setting ), 20 );
		
		
		textField.getDocument().addDocumentListener( new DocumentAdapter() {
			@Override
			public void handleEvent( final DocumentEvent event ) {
				settings.set( setting, textField.getText() );
				
				if ( customListener != null )
					customListener.actionPerformed( null );
			}
		} );
		
		return textField;
	}
	
	/**
	 * Creates an {@link XTextArea} whose initial value is initialized from the specified {@link IMultilineStringSetting} and settings bean.
	 * 
	 * <p>
	 * Changing the value of the text field will update the setting value at the specified settings bean.
	 * </p>
	 * 
	 * <p>
	 * <i>Warning!</i> Updating the setting is done via a registered {@link DocumentListener}. In Swing the execution order of registered listeners is not
	 * specified. Custom listeners added to the returned text field might be called before the setting is updated! If it is required that the new setting be
	 * updated when a custom listener is called, pass the custom listener as the <code>customListener</code> argument here.
	 * </p>
	 * 
	 * @param setting setting to create the text field for
	 * @param settings settings bean storing the setting value
	 * @param customListener action listener which is guaranteed to be called <b>after</b> the specified setting has been updated
	 * @return a text field for the specified setting
	 */
	public static XTextArea createSettingTextArea( final IMultilineStringSetting setting, final ISettingsBean settings, final ActionListener customListener ) {
		
		final XTextArea textArea = new XTextArea( settings.get( setting ), 3, 20 );
		
		textArea.getDocument().addDocumentListener( new DocumentAdapter() {
			@Override
			public void handleEvent( final DocumentEvent event ) {
				settings.set( setting, textArea.getText() );
				
				if ( customListener != null )
					customListener.actionPerformed( null );
			}
		} );
		
		return textArea;
	}
	
	/**
	 * Creates a {@link PathField} whose initial value is initialized from the specified {@link IPathSetting} and settings bean.
	 * 
	 * <p>
	 * Changing the value of the path field will update the setting value at the specified settings bean.
	 * </p>
	 * 
	 * <p>
	 * <i>Warning!</i> Updating the setting is done via a registered {@link DocumentListener}. In Swing the execution order of registered listeners is not
	 * specified. Custom listeners added to the returned path field might be called before the setting is updated! If it is required that the new setting be
	 * updated when a custom listener is called, pass the custom listener as the <code>customListener</code> argument here.
	 * </p>
	 * 
	 * @param setting setting to create the path field for
	 * @param settings settings bean storing the setting value
	 * @param customListener action listener which is guaranteed to be called <b>after</b> the specified setting has been updated
	 * @return a path field for the specified setting
	 */
	public static PathField createSettingPathField( final IPathSetting setting, final ISettingsBean settings, final ActionListener customListener ) {
		
		final PathField pathField = new PathField( settings.get( setting ) );
		pathField.textField.setColumns( 20 );
		
		pathField.textField.getDocument().addDocumentListener( new DocumentAdapter() {
			@Override
			public void handleEvent( final DocumentEvent event ) {
				settings.set( setting, pathField.getPath() );
				
				if ( customListener != null )
					customListener.actionPerformed( null );
			}
		} );
		
		return pathField;
	}
	
	/**
	 * Creates an {@link IndicatorTextField} whose initial value is initialized from the specified {@link IValidatedStringSetting} and settings bean.
	 * 
	 * <p>
	 * Changing the value of the indicator text field will update the setting value at the specified settings bean.
	 * </p>
	 * 
	 * <p>
	 * <i>Warning!</i> Updating the setting is done via a registered {@link DocumentListener}. In Swing the execution order of registered listeners is not
	 * specified. Custom listeners added to the returned text field might be called before the setting is updated! If it is required that the new setting be
	 * updated when a custom listener is called, pass the custom listener as the <code>customListener</code> argument here.
	 * </p>
	 * 
	 * @param setting setting to create the indicator text field for
	 * @param settings settings bean storing the setting value
	 * @param customListener action listener which is guaranteed to be called <b>after</b> the specified setting has been updated
	 * @return an indicator text field for the specified setting
	 */
	public static IndicatorTextField createSettingIndicatorTextField( final IValidatedStringSetting setting, final ISettingsBean settings,
	        final ActionListener customListener ) {
		
		final IndicatorTextField indicatorTextField = new IndicatorTextField( settings.get( setting ) );
		indicatorTextField.textField.setColumns( 20 );
		
		
		indicatorTextField.textField.getDocument().addDocumentListener( new DocumentAdapter() {
			@Override
			public void handleEvent( final DocumentEvent event ) {
				settings.set( setting, indicatorTextField.getText() );
				
				if ( customListener != null )
					customListener.actionPerformed( null );
			}
		} );
		
		return indicatorTextField;
	}
	
	
	/** Name of the client property storing the list of setting change listeners bounded to a component. */
	public static final String PROP_BOUNDED_SCL_LIST = "boundedSclList";
	
	/**
	 * Creates an {@link XComboBox} whose values and initial selected value is initialized from the specified {@link IFixedValuesSetting} and settings bean.
	 * <strong>Also executes the listener!</strong>
	 * 
	 * <p>
	 * Changing the selected value of the combo box will update the setting value at the specified settings bean.
	 * </p>
	 * <p>
	 * The created and returned combo box will also be bound to the setting value: if the setting value is modified outside at the specified settings bean, the
	 * selected value of the combo box will be changed accordingly.<br>
	 * This bounding is implemented with a {@link ISettingChangeListener} stored as a client property. To remove the bound, call
	 * {@link #removeAllBoundedScl(JComponent)}.
	 * </p>
	 * 
	 * <p>
	 * <i>Warning!</i> Updating the setting is done via a registered {@link ActionListener}. In Swing the execution order of registered listeners is not
	 * specified. Custom listeners added to the returned combo box might be called before the setting is updated! If it is required that the new setting be
	 * updated when a custom listener is called, pass the custom listener as the <code>customListener</code> argument here.
	 * </p>
	 * 
	 * @param <T> type of the setting values
	 * @param setting setting to create the combo box for and to bound to
	 * @param settings settings bean storing the setting value
	 * @param customListener action listener which is guaranteed to be called <b>after</b> the specified setting has been updated
	 * @return a combo box for the specified setting
	 * 
	 * @see #removeAllBoundedScl(JComponent)
	 */
	public static < T > XComboBox< T > createBoundedSettingComboBox( final IFixedValuesSetting< T > setting, final ISettingsBean settings,
	        final ActionListener customListener ) {
		
		final XComboBox< T > comboBox = createSettingComboBox( setting, settings, customListener );
		
		final ISettingChangeListener scl = new ISettingChangeListener() {
			@Override
			public void valuesChanged( final ISettingChangeEvent event ) {
				// Note when setting is modified outside of this combo box, we will be called.
				// We change the combo box value, which will fire the registered action listener
				// which will set the selected value again at the setting, but that will not trigger another setting change event
				// because the same value will be set and the settings bean only notifies listeners if the value does change.
				// So this will not cause an endless loop.
				if ( event.affected( setting ) )
					comboBox.setSelectedItem( event.get( setting ) );
			}
		};
		
		addBindExecuteScl( scl, settings, setting.selfSet(), comboBox );
		
		return comboBox;
	}
	
	/**
	 * Creates a setting combo box, bounded to the {@link LSettings#SKILL_LEVEL} setting.
	 * 
	 * <p>
	 * The {@link SkillLevel#DEVELOPER} will only be enabled if {@link LEnv#DEV_SKILL_LEVEL_ENABLED} is true.
	 * </p>
	 * 
	 * @param customListener action listener which is guaranteed to be called <b>after</b> the {@link LSettings#SKILL_LEVEL} has been updated
	 * @return a setting combo box, bounded to the {@link LSettings#SKILL_LEVEL} setting
	 */
	public static XComboBox< SkillLevel > createSkillLevelComboBox( final ActionListener customListener ) {
		final XComboBox< SkillLevel > cb = createBoundedSettingComboBox( LSettings.SKILL_LEVEL, LEnv.LAUNCHER_SETTINGS, customListener );
		
		if ( !LEnv.DEV_SKILL_LEVEL_ENABLED )
			cb.markDisabledItems( SkillLevel.DEVELOPER );
		
		return cb;
	}
	
	/**
	 * Binds the visibility of the specified component to the user's computer skill level. The skill level is the {@link LSettings#SKILL_LEVEL} taken from
	 * {@link LEnv#LAUNCHER_SETTINGS}. <strong>Also executes the listener!</strong>
	 * 
	 * @param comp component whose visibility to be controlled
	 * @param minSkillLevel minimal skill level required for the component to be visible
	 * 
	 * @see #bindVisibilityToSkillLevel(JComponent, ISkillLevel, Boolean)
	 */
	public static void bindVisibilityToSkillLevel( final JComponent comp, final ISkillLevel minSkillLevel ) {
		bindVisibilityToSkillLevel( comp, minSkillLevel, null );
	}
	
	/**
	 * Binds the visibility of the specified component to the user's computer skill level. The skill level is the {@link LSettings#SKILL_LEVEL} taken from
	 * {@link LEnv#LAUNCHER_SETTINGS}. <strong>Also executes the listener!</strong>
	 * 
	 * @param comp component whose visibility to be controlled
	 * @param minSkillLevel minimal skill level required for the component to be visible
	 * 
	 * @param hiddenSelected if not <code>null</code>, and if the component (which must be an {@link AbstractButton} in this case) has a different selection
	 *            state than this value case upon being hidden), the component selection will be inverted with a {@link AbstractButton#doClick()} call (which
	 *            also fires an event)
	 * 
	 * @throws IllegalArgumentException if <code>hiddenSelected</code> is not <code>null</code> and comp is not an {@link AbstractButton}
	 * 
	 * @see #bindVisibilityToSkillLevel(JComponent, ISkillLevel)
	 */
	public static void bindVisibilityToSkillLevel( final JComponent comp, final ISkillLevel minSkillLevel, final Boolean hiddenSelected ) {
		if ( hiddenSelected != null && !( comp instanceof AbstractButton ) )
			throw new IllegalArgumentException( "hiddenSelected can only be specified if component is an AbstractButton!" );
		
		final ISettingChangeListener scl = new ISettingChangeListener() {
			@Override
			public void valuesChanged( final ISettingChangeEvent event ) {
				if ( event.affected( LSettings.SKILL_LEVEL ) ) {
					comp.setVisible( minSkillLevel.isAtLeast() );
					if ( hiddenSelected != null && !comp.isVisible() && hiddenSelected != ( (AbstractButton) comp ).isSelected() )
						( (AbstractButton) comp ).doClick( 0 );
				}
			}
		};
		addBindExecuteScl( scl, LEnv.LAUNCHER_SETTINGS, LSettings.SKILL_LEVEL.SELF_SET, comp );
	}
	
	/**
	 * Adds the specified {@link ISettingChangeListener} to the specified {@link ISettingsBean} for the specified settings, and binds it to the specified
	 * component. <strong>Also executes the listener!</strong>
	 * 
	 * <p>
	 * Please also see {@link #bindScl(ISettingChangeListener, ISettingsBean, Set, JComponent)}.
	 * </p>
	 * 
	 * @param scl setting change listener to be added and bounded
	 * @param settings settings to add the listener to
	 * @param settingSet set of settings to add the listener for
	 * @param comp component to bind to
	 * 
	 * @see #bindScl(ISettingChangeListener, ISettingsBean, Set, JComponent)
	 * @see #removeAllBoundedScl(JComponent)
	 */
	public static void addBindExecuteScl( final ISettingChangeListener scl, final ISettingsBean settings, final Set< ? extends ISetting< ? > > settingSet,
	        final JComponent comp ) {
		
		settings.addAndExecuteChangeListener( settingSet, scl );
		
		bindScl( scl, settings, settingSet, comp );
	}
	
	/**
	 * Binds the specified {@link ISettingChangeListener} to the specified component.
	 * 
	 * <p>
	 * <i>Binding</i> means storing the reference of the setting change listener as a client property to retain a reference to it (because it is only stored as
	 * a {@link WeakReference} in the settings bean), and adding a {@link PropertyChangeListener} to the component to remove the setting change listener if the
	 * client property to which it was stored for changes.
	 * </p>
	 * 
	 * @param scl setting change listener to be added and bounded
	 * @param settings settings the listener was added to
	 * @param settingSet set of settings the listener was added for
	 * @param comp component to bind to
	 * 
	 * @see #addBindExecuteScl(ISettingChangeListener, ISettingsBean, Set, JComponent)
	 * @see #removeAllBoundedScl(JComponent)
	 */
	public static void bindScl( final ISettingChangeListener scl, final ISettingsBean settings, final Set< ? extends ISetting< ? > > settingSet,
	        final JComponent comp ) {
		
		// Retain listener reference
		// Store both the setting set and the listener as a pair
		@SuppressWarnings( "unchecked" )
		List< Pair< Set< ? extends ISetting< ? > >, ISettingChangeListener > > sclList = (List< Pair< Set< ? extends ISetting< ? > >, ISettingChangeListener > >) comp
		        .getClientProperty( PROP_BOUNDED_SCL_LIST );
		if ( sclList == null ) {
			comp.putClientProperty( PROP_BOUNDED_SCL_LIST, sclList = new ArrayList<>( 2 ) );
			
			// And auto-remove it if the client property is changed
			comp.addPropertyChangeListener( PROP_BOUNDED_SCL_LIST, new PropertyChangeListener() {
				@Override
				public void propertyChange( final PropertyChangeEvent event ) {
					@SuppressWarnings( "unchecked" )
					final List< Pair< Set< Setting< ? > >, ISettingChangeListener > > sclList = (List< Pair< Set< Setting< ? > >, ISettingChangeListener > >) event
					        .getOldValue();
					for ( final Pair< Set< Setting< ? > >, ISettingChangeListener > pair : sclList )
						settings.removeChangeListener( pair.value1, pair.value2 );
				}
			} );
		}
		
		sclList.add( new Pair< Set< ? extends ISetting< ? > >, ISettingChangeListener >( settingSet, scl ) );
	}
	
	/**
	 * Removes previously bounded {@link ISettingChangeListener}s from the specified component, and also from all of its child components recursively.
	 * 
	 * <p>
	 * Implementation simply clears the client property to which the bounded setting change listener was stored for to trigger the previously registered
	 * property change listener to take care of the rest.
	 * </p>
	 * 
	 * @param container container of components whose bounded setting change listener to remove
	 * 
	 * @see #addBindExecuteScl(ISettingChangeListener, ISettingsBean, Set, JComponent)
	 */
	public static void removeAllBoundedScl( final JComponent container ) {
		// Just for the short name...
		final JComponent c = container;
		
		if ( c.getClientProperty( PROP_BOUNDED_SCL_LIST ) != null )
			c.putClientProperty( PROP_BOUNDED_SCL_LIST, null );
		
		for ( int i = c.getComponentCount() - 1; i >= 0; i-- ) {
			final Component comp = c.getComponent( i );
			if ( comp instanceof JComponent )
				removeAllBoundedScl( (JComponent) comp );
		}
	}
	
}
