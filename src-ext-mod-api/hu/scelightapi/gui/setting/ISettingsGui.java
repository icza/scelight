/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelightapi.gui.setting;

import hu.scelightapi.bean.settings.type.ITemplateSetting;
import hu.scelightapi.bean.settings.type.IValidatedMultilineStringSetting;
import hu.scelightapi.gui.comp.IIndicatorTextArea;
import hu.scelightapi.gui.comp.ITemplateField;
import hu.scelightapibase.bean.settings.ISettingChangeListener;
import hu.scelightapibase.bean.settings.ISettingsBean;
import hu.scelightapibase.bean.settings.type.IBoolSetting;
import hu.scelightapibase.bean.settings.type.IFixedValuesSetting;
import hu.scelightapibase.bean.settings.type.IIntSetting;
import hu.scelightapibase.bean.settings.type.IMultilineStringSetting;
import hu.scelightapibase.bean.settings.type.INodeSetting;
import hu.scelightapibase.bean.settings.type.IPathSetting;
import hu.scelightapibase.bean.settings.type.ISetting;
import hu.scelightapibase.bean.settings.type.IStringSetting;
import hu.scelightapibase.bean.settings.type.IValidatedStringSetting;
import hu.scelightapibase.gui.comp.ICheckBox;
import hu.scelightapibase.gui.comp.IIndicatorTextField;
import hu.scelightapibase.gui.comp.ILink;
import hu.scelightapibase.gui.comp.IPathField;
import hu.scelightapibase.gui.comp.ISpinner;
import hu.scelightapibase.gui.comp.ITextArea;
import hu.scelightapibase.gui.comp.ITextField;
import hu.scelightapibase.gui.comp.combobox.IComboBox;
import hu.scelightapibase.util.ISkillLevel;

import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.lang.ref.WeakReference;
import java.util.Set;

import javax.swing.AbstractButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.event.DocumentListener;

/**
 * GUI utilities for settings visualization and edit.
 * 
 * @author Andras Belicza
 * 
 * @see ISettingsUtils
 */
public interface ISettingsGui {
	
	/**
	 * Creates a link which when clicked opens the settings dialog having the specified node setting selected.
	 * 
	 * @param nodeSetting node setting to select by default
	 * @return a link which when clicked opens the settings dialog having the specified node setting selected
	 */
	ILink createSettingLink( INodeSetting nodeSetting );
	
	/**
	 * Creates a link which when clicked opens the settings dialog having the specified node setting selected.
	 * 
	 * @param nodeSetting node setting to select by default
	 * @param text text of the link
	 * @return a link which when clicked opens the settings dialog having the specified node setting selected
	 */
	ILink createSettingLink( INodeSetting nodeSetting, String text );
	
	/**
	 * Checks if editing the specified setting requires registration and if so and registration is not OK, disables the specified setting component and sets a
	 * tool tip to it stating that editing it requires registration.
	 * 
	 * @param setting setting to be checked
	 * @param settingComponent setting component to be disabled if registration requirement is not met
	 */
	void checkRegistration( ISetting< ? > setting, JComponent settingComponent );
	
	/**
	 * Creates an {@link IComboBox} whose values and initial selected value is initialized from the specified {@link IFixedValuesSetting} and settings bean.
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
	< T > IComboBox< T > createSettingComboBox( IFixedValuesSetting< T > setting, ISettingsBean settings, ActionListener customListener );
	
	/**
	 * Creates an {@link ICheckBox} whose initial value is initialized from the specified {@link IBoolSetting} and settings bean.
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
	ICheckBox createSettingCheckBox( IBoolSetting setting, ISettingsBean settings, ActionListener customListener );
	
	/**
	 * Creates an {@link ISpinner} whose initial value is initialized from the specified {@link IIntSetting} and settings bean.
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
	ISpinner createSettingSpinner( IIntSetting setting, ISettingsBean settings, ActionListener customListener );
	
	/**
	 * Creates an {@link ITextField} whose initial value is initialized from the specified {@link IStringSetting} and settings bean.
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
	ITextField createSettingTextField( IStringSetting setting, ISettingsBean settings, ActionListener customListener );
	
	/**
	 * Creates an {@link ITextArea} whose initial value is initialized from the specified {@link IMultilineStringSetting} and settings bean.
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
	ITextArea createSettingTextArea( IMultilineStringSetting setting, ISettingsBean settings, ActionListener customListener );
	
	/**
	 * Creates an {@link IPathField} whose initial value is initialized from the specified {@link IPathSetting} and settings bean.
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
	IPathField createSettingPathField( IPathSetting setting, ISettingsBean settings, ActionListener customListener );
	
	/**
	 * Creates an {@link IIndicatorTextField} whose initial value is initialized from the specified {@link IValidatedStringSetting} and settings bean.
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
	IIndicatorTextField createSettingIndicatorTextField( IValidatedStringSetting setting, ISettingsBean settings, ActionListener customListener );
	
	/**
	 * Creates an {@link ITemplateField} whose initial value is initialized from the specified {@link ITemplateSetting} and settings bean.
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
	ITemplateField createSettingTemplateField( ITemplateSetting setting, ISettingsBean settings, ActionListener customListener );
	
	/**
	 * Creates an {@link IIndicatorTextArea} whose initial value is initialized from the specified {@link IValidatedStringSetting} and settings bean.
	 * 
	 * <p>
	 * Changing the value of the indicator text area will update the setting value at the specified settings bean.
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
	 * @return an indicator text field for the specified setting
	 * 
	 * @since 1.4
	 */
	IIndicatorTextArea createSettingIndicatorTextArea( IValidatedMultilineStringSetting setting, ISettingsBean settings, ActionListener customListener );
	
	/**
	 * Creates an {@link IComboBox} whose values and initial selected value is initialized from the specified {@link IFixedValuesSetting} and settings bean.
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
	< T > IComboBox< T > createBoundedSettingComboBox( IFixedValuesSetting< T > setting, ISettingsBean settings, ActionListener customListener );
	
	/**
	 * Creates an {@link ICheckBox} whose initial value is initialized from the specified {@link IBoolSetting} and settings bean.
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
	ICheckBox createBoundedSettingCheckBox( IBoolSetting setting, ISettingsBean settings, ActionListener customListener );
	
	/**
	 * Creates a setting combo box, bounded to the skill level setting.
	 * 
	 * <p>
	 * The {@link ISkillLevel#DEVELOPER} will optionally be enabled (if the user passed the required test).
	 * </p>
	 * 
	 * @param customListener action listener which is guaranteed to be called <b>after</b> the skill level setting has been updated
	 * @return a setting combo box, bounded to the skill level setting
	 */
	IComboBox< ? extends ISkillLevel > createSkillLevelComboBox( ActionListener customListener );
	
	/**
	 * Binds the visibility of the specified component to the user's computer skill level. <strong>Also executes the listener!</strong>
	 * 
	 * @param comp component whose visibility to be controlled
	 * @param minSkillLevel minimal skill level required for the component to be visible
	 * 
	 * @see #bindVisibilityToSkillLevel(JComponent, ISkillLevel, Boolean)
	 */
	void bindVisibilityToSkillLevel( JComponent comp, ISkillLevel minSkillLevel );
	
	/**
	 * Binds the visibility of the specified component to the user's computer skill level. <strong>Also executes the listener!</strong>
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
	void bindVisibilityToSkillLevel( JComponent comp, ISkillLevel minSkillLevel, Boolean hiddenSelected );
	
	/**
	 * Binds the visibility of the specified component to the specified {@link IBoolSetting} from the specified {@link ISettingsBean}.
	 * 
	 * @param comp component whose visibility to be bounded
	 * @param setting setting to control the visibility of the component
	 * @param settings settings bean storing the setting
	 */
	void bindVisibilityToSetting( JComponent comp, IBoolSetting setting, ISettingsBean settings );
	
	/**
	 * Binds the selection state of the specified abstract button (which can be e.g. a {@link ICheckBox} or a {@link JCheckBoxMenuItem}) to the specified
	 * {@link IBoolSetting} from the specified {@link ISettingsBean}.
	 * 
	 * @param button button whose state to be bounded
	 * @param setting setting to control the visibility of the component
	 * @param settings settings bean storing the setting
	 */
	void bindSelectionToSetting( AbstractButton button, IBoolSetting setting, ISettingsBean settings );
	
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
	void addBindExecuteScl( ISettingChangeListener scl, ISettingsBean settings, Set< ? extends ISetting< ? > > settingSet, JComponent comp );
	
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
	void bindScl( ISettingChangeListener scl, ISettingsBean settings, Set< ? extends ISetting< ? > > settingSet, JComponent comp );
	
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
	void removeAllBoundedScl( JComponent container );
	
}
