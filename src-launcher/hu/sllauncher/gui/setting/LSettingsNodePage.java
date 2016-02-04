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

import hu.scelightapibase.bean.settings.ISettingsBean;
import hu.scelightapibase.bean.settings.type.IBoolSetting;
import hu.scelightapibase.bean.settings.type.IFixedValuesSetting;
import hu.scelightapibase.bean.settings.type.IIntSetting;
import hu.scelightapibase.bean.settings.type.IMultilineStringSetting;
import hu.scelightapibase.bean.settings.type.INodeSetting;
import hu.scelightapibase.bean.settings.type.IPathSetting;
import hu.scelightapibase.bean.settings.type.ISetting;
import hu.scelightapibase.bean.settings.type.ISettingsGroup;
import hu.scelightapibase.bean.settings.type.IStringSetting;
import hu.scelightapibase.bean.settings.type.IValidatedStringSetting;
import hu.scelightapibase.bean.settings.type.viewhints.IViewHints;
import hu.sllauncher.gui.comp.GridBagPanel;
import hu.sllauncher.gui.comp.HelpIcon;
import hu.sllauncher.gui.comp.IndicatorTextField;
import hu.sllauncher.gui.comp.PathField;
import hu.sllauncher.gui.comp.XButton;
import hu.sllauncher.gui.comp.XCheckBox;
import hu.sllauncher.gui.comp.XLabel;
import hu.sllauncher.gui.comp.XScrollPane;
import hu.sllauncher.gui.comp.XSpinner;
import hu.sllauncher.gui.comp.XTextArea;
import hu.sllauncher.gui.comp.XTextField;
import hu.sllauncher.gui.comp.combobox.XComboBox;
import hu.sllauncher.gui.comp.multipage.BasePage;
import hu.sllauncher.gui.icon.LIcons;
import hu.sllauncher.service.settings.LSettings;
import hu.sllauncher.util.LRHtml;
import hu.sllauncher.util.LUtils;
import hu.sllauncher.util.Pair;
import hu.sllauncher.util.SkillLevel;
import hu.sllauncher.util.gui.LGuiUtils;
import hu.sllauncher.util.gui.adapter.ActionAdapter;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JPanel;

/**
 * A node page displaying settings view/edit components.
 * 
 * @author Andras Belicza
 */
public class LSettingsNodePage extends BasePage< JComponent > {
	
	/** Map from {@link SkillLevel} to their tool tip. */
	private static Map< SkillLevel, String >               skillLevelToolTipMap = new EnumMap<>( SkillLevel.class );
	static {
		for ( final SkillLevel skillLevel : LSettings.SKILL_LEVEL.values )
			skillLevelToolTipMap.put( skillLevel, "<html>This setting requires <span style='" + LRHtml.STYLE_STRESSED + "'>" + skillLevel
			        + "</span> skill level.</html>" );
	}
	
	/** Node setting the page is associated with. */
	public final INodeSetting                              nodeSetting;
	
	/**
	 * List of settings to be displayed on the page.<br>
	 * Elements are {@link Pair}s, the pair consists of the setting and the temp settings bean to work with until changes are applied.
	 */
	protected List< Pair< ISetting< ? >, ISettingsBean > > settingList;
	
	/**
	 * Creates a new {@link LSettingsNodePage}.
	 * 
	 * @param nodeSetting node setting the page is associated with
	 */
	public LSettingsNodePage( final INodeSetting nodeSetting ) {
		super( nodeSetting.getName(), nodeSetting.getViewHints().getRicon() );
		
		this.nodeSetting = nodeSetting;
	}
	
	/**
	 * Adds a setting to this page.
	 * 
	 * @param setting setting to be added
	 */
	public void addSetting( final Pair< ISetting< ? >, ISettingsBean > setting ) {
		if ( settingList == null )
			settingList = new ArrayList<>();
		
		settingList.add( setting );
	}
	
	@Override
	public String getDisplayName() {
		if ( SkillLevel.BASIC.isAtMost() )
			return super.getDisplayName();
		
		// Indicate settings count (of the node) in the title
		// Impl note: without HTML encoding page title is wrapped into multilines if not enough space
		// instead of truncating and appending "...". This is because it's HTML text.
		return "<html>" + LUtils.safeForHtml( super.getDisplayName() ) + "&nbsp;<font color='#b0b0b0'>(" + ( settingList == null ? 0 : settingList.size() )
		        + ")</font></html>";
	}
	
	@Override
	public JComponent createPageComp() {
		final GridBagPanel p = new GridBagPanel();
		final GridBagConstraints c = p.c;
		
		ISettingsGroup lastGroup = null;
		
		// Setting list might be null because the setting tree might contain nodes with no settings
		for ( final Pair< ISetting< ? >, ISettingsBean > settingPair : settingList == null ? Collections.< Pair< ISetting< ? >, ISettingsBean > > emptyList()
		        : settingList ) {
			
			final ISetting< ? > setting = settingPair.value1;
			final ISettingsBean tempSettings = settingPair.value2;
			final IViewHints viewHints = setting.getViewHints();
			
			p.nextRow();
			
			if ( lastGroup != setting.getGroup() ) {
				// Start a new group
				c.insets.top = 15;
				final Box titleBox = Box.createHorizontalBox();
				titleBox.setBorder( BorderFactory.createMatteBorder( 0, 0, 1, 0, Color.GRAY ) );
				final XLabel groupTitle = LGuiUtils.boldFont( new XLabel( setting.getGroup().getName() ) ).rightBorder( 10 );
				groupTitle.setForeground( Color.BLUE );
				titleBox.add( groupTitle );
				if ( setting.getGroup().gethelpRhtml() != null )
					titleBox.add( new HelpIcon( setting.getGroup().gethelpRhtml() ) );
				p.addRemainder( titleBox );
				c.insets.top = 2;
				p.nextRow();
				lastGroup = setting.getGroup();
			}
			
			// Setting icons:
			final JPanel iconsPanel = new JPanel( new FlowLayout( FlowLayout.RIGHT, 0, 0 ) );
			// Edit registration requirement info icon
			if ( viewHints.isEditRequiresRegistration() ) {
				final XLabel regInfoLabel = new XLabel( LIcons.F_LICENCE_KEY.get() );
				regInfoLabel.setToolTipText( "Editing this setting requires registration!" );
				iconsPanel.add( regInfoLabel );
			}
			// Icon representing the associated skill level
			final XLabel skillLevelLabel = new XLabel( setting.getSkillLevel().getRicon().get() );
			skillLevelLabel.setToolTipText( skillLevelToolTipMap.get( setting.getSkillLevel() ) );
			iconsPanel.add( skillLevelLabel );
			p.addSingle( iconsPanel );
			
			// Setting label
			if ( !( setting instanceof IBoolSetting ) ) // In case of BoolSetting an XCheckBox will be used with the name...
				p.addSingle( new XLabel( setting.getName() + ":" ) );
			
			// Setting component
			final XButton defaultButton = new XButton( LIcons.F_ARROW_RETURN_180_LEFT.get() );
			final ActionListener defaultButtonEnabler = new ActionAdapter( true ) {
				@Override
				public void actionPerformed( final ActionEvent event ) {
					defaultButton.setEnabled( !setting.getDefaultValue().equals( tempSettings.get( setting ) ) );
				}
			};
			final JComponent settingComp = addSettingComponent( p, setting, tempSettings, defaultButtonEnabler );
			if ( viewHints.getCompConfigurer() != null )
				viewHints.getCompConfigurer().configure( settingComp, setting, tempSettings );
			
			// Subsequent text
			p.addSingle( new XLabel( viewHints.getSubsequentText() ) );
			
			// Custom subsequent component
			p.addSingle( viewHints.getSsCompFactory() == null ? new XLabel() : viewHints.getSsCompFactory().create( settingComp, setting, tempSettings ) );
			
			// Help icon
			p.addSingle( viewHints.getHelpRhtml() == null ? new XLabel() : new HelpIcon( viewHints.getHelpRhtml() ) );
			
			// Restore default value button
			c.insets.left += 5;
			defaultButton.setToolTipText( "Restore default value" );
			defaultButton.addActionListener( new ActionAdapter() {
				@Override
				public void actionPerformed( final ActionEvent event ) {
					setComponentValue( settingComp, setting.getDefaultValue() );
					// Setting the value of the setting component might not trigger events (so reset setting value manually):
					tempSettings.reset( setting );
					defaultButtonEnabler.actionPerformed( null );
				}
			} );
			p.addSingle( defaultButton );
			c.insets.left -= 5;
		}
		
		return LGuiUtils.wrapInPanel( p );
	}
	
	/**
	 * Adds a proper setting viewer/editor component for the specified setting to the specified panel.
	 * 
	 * @param p panel to add the component to
	 * @param setting setting to create the component for
	 * @param settings settings bean storing the setting value
	 * @param customListener custom listener to be called when the value of the returned setting component changes
	 * @return the created and added component
	 */
	protected JComponent addSettingComponent( final GridBagPanel p, final ISetting< ? > setting, final ISettingsBean settings,
	        final ActionListener customListener ) {
		JComponent settingComp;
		
		if ( setting instanceof IFixedValuesSetting ) {
			
			// Combo box
			p.addSingle( settingComp = LSettingsGui.createSettingComboBox( (IFixedValuesSetting< ? >) setting, settings, customListener ) );
			/*
			 * TODO Not sure why but not enough space is allocated for a combo box being on the default page (displayed first) causing the selected longest
			 * value rendered with last characters cut off and post-pended with 3 dots at the end (maybe because window is not yet visible?). Add a little extra
			 * to solve this bug.
			 */
			settingComp.setPreferredSize( new Dimension( settingComp.getPreferredSize().width + 5, settingComp.getPreferredSize().height ) );
			
		} else if ( setting instanceof IBoolSetting ) {
			
			// Check box
			p.c.fill = GridBagConstraints.NONE;
			p.addDouble( settingComp = LSettingsGui.createSettingCheckBox( (IBoolSetting) setting, settings, customListener ) );
			p.c.fill = GridBagConstraints.HORIZONTAL;
			
		} else if ( setting instanceof IIntSetting ) {
			
			// Spinner
			p.addSingle( settingComp = LSettingsGui.createSettingSpinner( (IIntSetting) setting, settings, customListener ) );
			
		} else if ( setting instanceof IValidatedStringSetting ) { // This must be checked first (it extends IStringSetting)!
		
			// Indicator Text field
			p.addSingle( settingComp = LSettingsGui.createSettingIndicatorTextField( (IValidatedStringSetting) setting, settings, customListener ) );
			if ( setting.getViewHints().getColumns() != null )
				( (IndicatorTextField) settingComp ).textField.setColumns( setting.getViewHints().getColumns() );
			
		} else if ( setting instanceof IMultilineStringSetting ) { // This must be checked first (it extends IStringSetting)!
		
			// Text area
			p.addSingle( new XScrollPane( settingComp = LSettingsGui.createSettingTextArea( (IMultilineStringSetting) setting, settings, customListener ),
			        false ) );
			if ( setting.getViewHints().getRows() != null )
				( (XTextArea) settingComp ).setRows( setting.getViewHints().getRows() );
			if ( setting.getViewHints().getColumns() != null )
				( (XTextArea) settingComp ).setColumns( setting.getViewHints().getColumns() );
			
		} else if ( setting instanceof IStringSetting ) {
			
			// Text field
			p.addSingle( settingComp = LSettingsGui.createSettingTextField( (IStringSetting) setting, settings, customListener ) );
			if ( setting.getViewHints().getColumns() != null )
				( (XTextField) settingComp ).setColumns( setting.getViewHints().getColumns() );
			
		} else if ( setting instanceof IPathSetting ) {
			
			// Path field
			p.addSingle( settingComp = LSettingsGui.createSettingPathField( (IPathSetting) setting, settings, customListener ) );
			if ( setting.getViewHints().getDialogTitle() != null )
				( (PathField) settingComp ).fileChooser.setDialogTitle( setting.getViewHints().getDialogTitle() );
			if ( setting.getViewHints().getColumns() != null )
				( (PathField) settingComp ).textField.setColumns( setting.getViewHints().getColumns() );
			
		} else
			throw new RuntimeException( "Unhandled setting type: " + setting.getClass() );
		
		return settingComp;
	}
	
	/**
	 * Sets the value of the specified setting component.
	 * 
	 * @param settingComp setting component whose value to be set
	 * @param value value to be set
	 */
	protected void setComponentValue( final JComponent settingComp, final Object value ) {
		if ( settingComp instanceof XCheckBox )
			( (XCheckBox) settingComp ).setSelected( (Boolean) value );
		else if ( settingComp instanceof XComboBox )
			( (XComboBox< ? >) settingComp ).setSelectedItem( value );
		else if ( settingComp instanceof XSpinner )
			( (XSpinner) settingComp ).setValue( value );
		else if ( settingComp instanceof XTextField )
			( (XTextField) settingComp ).setText( (String) value );
		else if ( settingComp instanceof XTextArea )
			( (XTextArea) settingComp ).setText( (String) value );
		else if ( settingComp instanceof PathField ) // This must be checked first (it extends IndicatorTextField)!
			( (PathField) settingComp ).setPath( (Path) value );
		else if ( settingComp instanceof IndicatorTextField )
			( (IndicatorTextField) settingComp ).setText( (String) value );
		else
			throw new RuntimeException( "Unhandled setting component type: " + settingComp.getClass() );
	}
	
}
