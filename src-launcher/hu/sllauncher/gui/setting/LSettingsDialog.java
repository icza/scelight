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
import hu.scelightapibase.bean.settings.type.INodeSetting;
import hu.scelightapibase.bean.settings.type.ISetting;
import hu.scelightapibase.gui.comp.multipage.IPage;
import hu.sllauncher.bean.settings.SettingsBean;
import hu.sllauncher.gui.comp.HelpIcon;
import hu.sllauncher.gui.comp.ModestLabel;
import hu.sllauncher.gui.comp.TextFilterComp;
import hu.sllauncher.gui.comp.TipIcon;
import hu.sllauncher.gui.comp.XButton;
import hu.sllauncher.gui.comp.XDialog;
import hu.sllauncher.gui.comp.XLabel;
import hu.sllauncher.gui.comp.XToolBar;
import hu.sllauncher.gui.comp.multipage.MultiPageComp;
import hu.sllauncher.gui.help.LHelps;
import hu.sllauncher.gui.icon.LIcons;
import hu.sllauncher.gui.tip.LTips;
import hu.sllauncher.service.env.LEnv;
import hu.sllauncher.service.settings.LSettings;
import hu.sllauncher.service.sound.Sound;
import hu.sllauncher.util.LUtils;
import hu.sllauncher.util.Pair;
import hu.sllauncher.util.SkillLevel;
import hu.sllauncher.util.gui.adapter.ActionAdapter;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * A dialog to view/edit settings.
 * 
 * @author Andras Belicza
 */
@SuppressWarnings( "serial" )
public class LSettingsDialog extends XDialog {
	
	/** Optional node setting to select by default, only once. */
	private INodeSetting           requestedDefaultNodeSetting;
	
	/** Sources of settings to be displayed in the dialog. */
	private final ISettingsBean[]  settingss;
	
	/** Sources of settings to be displayed in the dialog. */
	private final ISettingsBean[]  tempSettingss;
	
	/** Text filter component to filter the displayed settings. */
	private final TextFilterComp   filterComp         = new TextFilterComp();
	
	/** Multi page component displaying the settings hierarchy and pages. */
	private final MultiPageComp    multiPageComp      = new MultiPageComp( new ArrayList< IPage< ? > >(), null, getLayeredPane() );
	
	/** Label to display settings count. */
	private final ModestLabel      settingsCountLabel = new ModestLabel();
	
	/**
	 * Setting change listener controlling the enabled status of the Apply button. Stored to retain reference to the listener.<br>
	 * No need to properly add and bound this change listener, because both the temp settings and the apply button is discarded upon closing this dialog (so no
	 * need to remove the listener...).
	 */
	private ISettingChangeListener scl;
	
	/**
	 * Creates a new {@link LSettingsDialog}.
	 * 
	 * <p>
	 * <b>Does not make the dialog visible!</b>
	 * </p>
	 * 
	 * @param owner reference to the owner frame
	 * @param defaultNodeSetting optional node setting to select by default
	 * @param settingss setting beans whose valid settings to displayed in the dialog
	 */
	public LSettingsDialog( final Frame owner, final INodeSetting defaultNodeSetting, final ISettingsBean... settingss ) {
		super( owner );
		
		this.requestedDefaultNodeSetting = defaultNodeSetting;
		
		this.settingss = settingss;
		tempSettingss = new SettingsBean[ settingss.length ];
		for ( int i = 0; i < settingss.length; i++ ) {
			tempSettingss[ i ] = settingss[ i ].cloneSettings();
			tempSettingss[ i ].setTrackChanges( true );
		}
		
		setTitle( "Settings" );
		setIconImage( LIcons.F_GEAR.get().getImage() );
		
		buildGui();
		
		restoreDefaultSize();
	}
	
	/**
	 * Builds the GUI of the settings dialog.
	 */
	private void buildGui() {
		// Tool bar
		final XToolBar toolBar = new XToolBar();
		toolBar.add( new XLabel( LSettings.SKILL_LEVEL.name + ":" ) );
		final ActionListener rebuildPageTreeListener = new ActionAdapter() {
			@Override
			public void actionPerformed( final ActionEvent event ) {
				rebuildSettingsPageList();
			}
		};
		toolBar.add( LSettingsGui.createSkillLevelComboBox( rebuildPageTreeListener ) );
		toolBar.add( new HelpIcon( LHelps.COMP_SKILL_LEVEL ) );
		toolBar.addSeparator();
		filterComp.setFilterTask( new Runnable() {
			@Override
			public void run() {
				rebuildPageTreeListener.actionPerformed( null );
			}
		} );
		filterComp.registerFocusHotkey( getLayeredPane() );
		toolBar.add( filterComp );
		toolBar.add( new HelpIcon( LHelps.SETTINGS_FILTER ) );
		toolBar.addSeparator();
		settingsCountLabel.setToolTipText( "<html><b>Displayed</b> settings count / <b>All</b> settings count" );
		toolBar.add( settingsCountLabel );
		toolBar.addSeparator();
		toolBar.add( new TipIcon( LTips.SETTINGS ) );
		toolBar.finalizeLayout();
		cp.addNorth( toolBar );
		
		// Settings pages
		cp.addCenter( multiPageComp );
		
		// Buttons
		final XButton okButton = new XButton( "_OK" );
		getButtonsPanel().add( okButton );
		getRootPane().setDefaultButton( okButton );
		final XButton applyButton = new XButton( "_Apply" );
		applyButton.setEnabled( false );
		final ActionAdapter okApplyActionListener = new ActionAdapter() {
			@Override
			public void actionPerformed( final ActionEvent event ) {
				for ( int i = 0; i < settingss.length; i++ ) {
					final ISettingsBean settings = settingss[ i ];
					final ISettingsBean tempSettings = tempSettingss[ i ];
					tempSettings.copyChangedSettingsTo( settings );
					settings.save();
				}
				
				// Changed settings might cause visual appearance change (e.g. a changed date format), repaint owner window
				getOwner().repaint();
				
				if ( event.getSource() == okButton ) {
					close();
				} else if ( event.getSource() == applyButton ) {
					for ( final ISettingsBean tempSettings : tempSettingss )
						tempSettings.clearTrackedChanges();
					applyButton.setEnabled( false );
				}
			}
		};
		okButton.addActionListener( okApplyActionListener );
		applyButton.addActionListener( okApplyActionListener );
		getButtonsPanel().add( applyButton );
		addCloseButton( "_Cancel" );
		
		// Init
		// Focus filter text field when window is first activated:
		setFocusTargetOnFirstShow( filterComp.textField );
		// Build initial settings content.
		rebuildPageTreeListener.actionPerformed( null );
		
		// Only enable Apply button when there are changes!
		scl = new ISettingChangeListener() {
			@Override
			public void valuesChanged( final ISettingChangeEvent event ) {
				applyButton.setEnabled( true );
			}
		};
		for ( final ISettingsBean tempSettings : tempSettingss )
			tempSettings.addChangeListener( new HashSet<>( tempSettings.getValidSettingList() ), scl );
	}
	
	/**
	 * Rebuilds the settings page list (hierarchy) filtered by the entered search term and {@link LSettings#SKILL_LEVEL} taken from
	 * {@link LEnv#LAUNCHER_SETTINGS}.
	 */
	@SuppressWarnings( "unchecked" )
	private void rebuildSettingsPageList() {
		// Store selected page and restore if possible
		final INodeSetting selectedNodeSetting;
		if ( requestedDefaultNodeSetting == null )
			selectedNodeSetting = multiPageComp.getSelectedPage() == null ? null : ( (LSettingsNodePage) multiPageComp.getSelectedPage() ).nodeSetting;
		else {
			// Select requested default node setting...
			selectedNodeSetting = requestedDefaultNodeSetting;
			// ...but only once:
			requestedDefaultNodeSetting = null;
		}
		
		final String searchText = filterComp.textField.getText().isEmpty() ? null : filterComp.textField.getText().toLowerCase();
		
		final SkillLevel skillLevel = LEnv.LAUNCHER_SETTINGS.get( LSettings.SKILL_LEVEL );
		
		final List< LSettingsNodePage > pageList = new ArrayList<>();
		
		final List< ISetting< ? > > settingPath = new ArrayList<>();
		
		LSettingsNodePage selectablePage = null;
		
		int allCount = 0, count = 0;
		for ( int i = 0; i < settingss.length; i++ ) {
			final ISettingsBean settings = settingss[ i ];
			final ISettingsBean tempSettings = tempSettingss[ i ];
			
			for ( final ISetting< ? > setting : settings.getValidSettingList() ) {
				if ( setting.getSkillLevel() != SkillLevel.HIDDEN )
					allCount++;
				
				if ( skillLevel.ordinal() - setting.getSkillLevel().ordinal() < 0 )
					continue;
				
				if ( searchText != null ) {
					// Search in setting name
					boolean matches = LUtils.containsIngoreCase( setting.getName(), searchText );
					// Search in subsequent text of the setting component
					if ( !matches && setting.getViewHints().getSubsequentText() != null )
						matches = LUtils.containsIngoreCase( setting.getViewHints().getSubsequentText(), searchText );
					if ( !matches )
						continue;
				}
				
				// Setting is to be displayed
				
				// Search for and ensure settings parent node pages exist.
				// Do this one by one going down the setting path.
				settingPath.clear();
				// Get setting path from the parent. Parent do exist because valid setting list excludes node settings.
				setting.getParent().getSettingPath( settingPath );
				
				LSettingsNodePage settingsNodePage = null, prevSettingsNodePage;
				List< LSettingsNodePage > searchingPageList = pageList;
				
				for ( int j = 0; j < settingPath.size(); j++ ) {
					prevSettingsNodePage = settingsNodePage;
					settingsNodePage = null;
					for ( final LSettingsNodePage nodePage : searchingPageList ) {
						if ( nodePage.nodeSetting == settingPath.get( j ) ) {
							settingsNodePage = nodePage;
							break;
						}
					}
					
					if ( settingsNodePage == null ) {
						searchingPageList.add( settingsNodePage = createNewSettingsNodePage( (INodeSetting) settingPath.get( j ) ) );
						settingsNodePage.setParent( prevSettingsNodePage );
						// Is this the page that was selected?
						if ( selectedNodeSetting == settingsNodePage.nodeSetting )
							selectablePage = settingsNodePage;
					}
					
					searchingPageList = (List< LSettingsNodePage >) (List< ? >) settingsNodePage.getNonNullChildList();
				}
				
				settingsNodePage.addSetting( new Pair< ISetting< ? >, ISettingsBean >( setting, tempSettings ) );
				count++;
			}
			
		}
		
		if ( searchText != null && pageList.isEmpty() )
			Sound.beepOnEmptyTxtSearchRslt();
		
		settingsCountLabel.setText( "Settings: " + LEnv.LANG.formatNumber( count ) + " / " + LEnv.LANG.formatNumber( allCount ) );
		
		multiPageComp.clearHistory();
		
		multiPageComp.setPageList( (List< IPage< ? > >) (List< ? >) pageList );
		
		// Select a non-empty page, a page which does contain settings. Due to a filter, node settings page might be empty
		// and only added/contained because it has a non-empty descendant.
		
		if ( selectablePage == null && !pageList.isEmpty() )
			selectablePage = pageList.get( 0 );
		
		if ( selectablePage != null ) {
			// A non-empty descendant surely exists because selectablePage is only set if the settings node page is added.
			// (And a node page is only added if a setting is added to it or to one of its descendants.)
			selectablePage = findFirstNonEmptyDescendant( selectablePage );
			if ( !multiPageComp.getSelectedPage().equals( selectablePage ) )
				multiPageComp.clearHistory();
			multiPageComp.selectPage( selectablePage );
		}
	}
	
	/**
	 * Creates a new {@link LSettingsNodePage}.
	 * 
	 * <p>
	 * Primary goal of this factory method is to make it overridable in descendants (to return an instance of a custom / overridden type).
	 * </p>
	 * 
	 * @param nodeSetting node setting the page is associated with
	 * @return the created settings node page
	 */
	protected LSettingsNodePage createNewSettingsNodePage( final INodeSetting nodeSetting ) {
		return new LSettingsNodePage( nodeSetting );
	}
	
	/**
	 * Finds and returns the first non-empty descendant settings node page using depth-first search.
	 * 
	 * @param settingsNodePage settings node page to start the search from (the node itself is searched too)
	 * @return the first non-empty descendant settings node page using depth-first search; or <code>null</code> if no such descendant exists
	 */
	private static LSettingsNodePage findFirstNonEmptyDescendant( LSettingsNodePage settingsNodePage ) {
		if ( settingsNodePage.settingList != null && !settingsNodePage.settingList.isEmpty() )
			return settingsNodePage;
		
		final List< IPage< ? > > childList = settingsNodePage.getChildList();
		if ( childList == null || childList.isEmpty() )
			return null;
		
		for ( final IPage< ? > page : childList ) {
			settingsNodePage = findFirstNonEmptyDescendant( (LSettingsNodePage) page );
			if ( settingsNodePage != null )
				return settingsNodePage;
		}
		
		return null;
	}
	
}
