/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight.gui.page.repanalyzer.mapinfo;

import hu.scelight.gui.page.repanalyzer.BaseRepAnalTabComp;
import hu.scelight.gui.setting.SettingsGui;
import hu.scelight.gui.tip.Tips;
import hu.scelight.sc2.map.MapParser;
import hu.scelight.sc2.map.model.MapInfo;
import hu.scelight.sc2.rep.repproc.RepProcessor;
import hu.scelight.service.env.Env;
import hu.scelight.service.settings.Settings;
import hu.scelight.util.Utils;
import hu.scelightapibase.bean.settings.ISettingChangeEvent;
import hu.scelightapibase.bean.settings.ISettingChangeListener;
import hu.sllauncher.gui.comp.BorderPanel;
import hu.sllauncher.gui.comp.GridBagPanel;
import hu.sllauncher.gui.comp.TipIcon;
import hu.sllauncher.gui.comp.XCheckBox;
import hu.sllauncher.gui.comp.XLabel;
import hu.sllauncher.gui.comp.XScrollPane;
import hu.sllauncher.gui.comp.XTextArea;
import hu.sllauncher.gui.comp.XTextField;
import hu.sllauncher.gui.comp.XToolBar;
import hu.sllauncher.gui.comp.combobox.XComboBox;
import hu.sllauncher.gui.comp.table.XTable;
import hu.sllauncher.service.env.LEnv;
import hu.sllauncher.service.settings.LSettings;
import hu.sllauncher.util.SkillLevel;
import hu.sllauncher.util.gui.adapter.ActionAdapter;

import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Collections;
import java.util.Map;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JComponent;

/**
 * Map info tab component.
 * 
 * @author Andras Belicza
 */
@SuppressWarnings( "serial" )
public class MapInfoComp extends BaseRepAnalTabComp {
	
	/** Table displaying general map info. */
	private final XTable table = new XTable();
	
	/**
	 * Creates a new {@link MapInfoComp}.
	 * 
	 * @param repProc replay processor
	 */
	public MapInfoComp( final RepProcessor repProc ) {
		super( repProc );
		
		buildGui();
	}
	
	/**
	 * Builds the GUI of the tab.
	 */
	private void buildGui() {
		final BorderPanel centerPanel = new BorderPanel();
		
		// General Map Info
		final JComponent infoWrapper = table.createWrapperBox( false );
		infoWrapper.setBorder( BorderFactory.createTitledBorder( "General info" ) );
		centerPanel.addWest( infoWrapper );
		
		// Map Image
		final BorderPanel imagePanel = new BorderPanel();
		final XToolBar toolBar = new XToolBar();
		toolBar.add( new XLabel( "Zoom:" ) );
		final XComboBox< Zoom > zoomComboBox = SettingsGui.createSettingComboBox( Settings.MAP_INFO_IMAGE_ZOOM, Env.APP_SETTINGS, null );
		toolBar.add( zoomComboBox );
		toolBar.addSeparator();
		toolBar.add( new TipIcon( Tips.MAP_IMAGE_ZOOMING_SCROLLING ) );
		toolBar.addSeparator();
		final XCheckBox showPlayerNamesCheckBox = SettingsGui.createSettingCheckBox( Settings.MAP_INFO_SHOW_PLAYER_NAMES, Env.APP_SETTINGS, null );
		toolBar.add( showPlayerNamesCheckBox );
		toolBar.finalizeLayout();
		imagePanel.addNorth( toolBar );
		imagePanel.setBorder( BorderFactory.createTitledBorder( "Map image" ) );
		final MapCanvas mapCanvas = new MapCanvas( repProc, zoomComboBox );
		final XScrollPane mapScrollPane = new XScrollPane( mapCanvas );
		imagePanel.add( mapScrollPane );
		// componentResized() will be called when the component is made displayable,
		// so the map canvas will be configured properly.
		mapScrollPane.addComponentListener( new ComponentAdapter() {
			@Override
			public void componentResized( final ComponentEvent event ) {
				mapCanvas.reconfigureZoom();
			}
		} );
		centerPanel.addCenter( imagePanel );
		
		zoomComboBox.addActionListener( new ActionAdapter() {
			@Override
			public void actionPerformed( final ActionEvent event ) {
				mapCanvas.reconfigureZoom();
			}
		} );
		showPlayerNamesCheckBox.addActionListener( new ActionAdapter( true ) {
			@Override
			public void actionPerformed( final ActionEvent event ) {
				mapCanvas.setShowPlayerNames( showPlayerNamesCheckBox.isSelected() );
				mapCanvas.repaint();
			}
		} );
		
		addCenter( centerPanel );
		
		// Map Attributes
		
		final GridBagPanel ap = new GridBagPanel();
		ap.setBorder( BorderFactory.createTitledBorder( "Map attributes" ) );
		final Map< String, Map< String, String > > localeMapAttributes = MapParser.getMapAttributes( repProc );
		final XComboBox< String > localeComboBox;
		if ( localeMapAttributes != null ) {
			final Vector< String > localeVector = new Vector<>( localeMapAttributes.keySet() );
			Collections.sort( localeVector );
			localeComboBox = new XComboBox<>( localeVector );
			localeComboBox.setMaximumRowCount( localeComboBox.getItemCount() ); // Display all available locales in the popup
			localeComboBox.setSelectedItem( Env.APP_SETTINGS.get( Settings.MAP_LOCALE ) );
		} else
			localeComboBox = new XComboBox<>();
		ap.nextRow();
		ap.addSingle( new XLabel( "Choose a locale:" ) );
		ap.c.weightx = 1;
		ap.addRemainder( localeComboBox );
		ap.c.weightx = 0;
		ap.nextRow();
		ap.addSingle( new XLabel( "Name:" ) );
		final XTextField nameTextField = new XTextField();
		nameTextField.setEditable( false );
		ap.addRemainder( nameTextField );
		ap.nextRow();
		ap.addSingle( new XLabel( "Author:" ) );
		final XTextField authorTextField = new XTextField();
		authorTextField.setEditable( false );
		ap.addRemainder( authorTextField );
		ap.nextRow();
		ap.addSingle( new XLabel( "Short description:" ) );
		final XTextField shortDescTextField = new XTextField();
		shortDescTextField.setEditable( false );
		ap.addRemainder( shortDescTextField );
		ap.nextRow();
		ap.addSingle( new XLabel( "Long description:" ) );
		ap.c.weighty = 1;
		final XTextArea longDescTextArea = new XTextArea();
		longDescTextArea.setWrapStyleWord( true );
		longDescTextArea.setLineWrap( true );
		longDescTextArea.setEditable( false );
		ap.addRemainder( new XScrollPane( longDescTextArea, false ) );
		ap.c.weighty = 0;
		localeComboBox.addActionListener( new ActionAdapter( true ) {
			@Override
			public void actionPerformed( final ActionEvent event ) {
				final String locale = localeComboBox.getSelectedItem();
				if ( locale == null )
					return;
				if ( !duringInit )
					Env.APP_SETTINGS.set( Settings.MAP_LOCALE, locale );
				final Map< String, String > mapAttributes = localeMapAttributes.get( locale );
				nameTextField.setText( mapAttributes.get( "DocInfo/Name" ) );
				authorTextField.setText( mapAttributes.get( "DocInfo/Author" ) );
				shortDescTextField.setText( mapAttributes.get( "DocInfo/DescShort" ) );
				longDescTextArea.setText( mapAttributes.get( "DocInfo/DescLong" ) );
				longDescTextArea.setCaretPosition( 0 );
			}
		} );
		addSouth( ap );
		
		// Rebuild table data if skill level changes
		final ISettingChangeListener scl = new ISettingChangeListener() {
			@Override
			public void valuesChanged( final ISettingChangeEvent event ) {
				if ( event.affected( LSettings.SKILL_LEVEL ) )
					rebuildTableData();
			}
		};
		SettingsGui.addBindExecuteScl( scl, LEnv.LAUNCHER_SETTINGS, LSettings.SKILL_LEVEL.SELF_SET, this );
	}
	
	/**
	 * Rebuilds the table data.
	 */
	private void rebuildTableData() {
		final Vector< Vector< Object > > data = new Vector<>();
		
		data.add( Utils.vector( "Map name", repProc.replay.details.title ) );
		
		final MapInfo mapInfo = repProc.getMapInfo();
		if ( mapInfo != null ) {
			data.add( Utils.vector( "Map size", mapInfo.getSizeString() ) );
			data.add( Utils.vector( "Map playable size", mapInfo.getPlayableSizeString() ) );
			data.add( Utils.vector( "Tile set", mapInfo.tileSet ) );
			if ( SkillLevel.ADVANCED.isAtLeast() ) {
				data.add( Utils.vector( "Fog type", mapInfo.fogType ) );
				data.add( Utils.vector( "Boundary left", mapInfo.boundaryLeft ) );
				data.add( Utils.vector( "Boundary right", mapInfo.boundaryRight ) );
				data.add( Utils.vector( "Boundary bottom", mapInfo.boundaryBottom ) );
				data.add( Utils.vector( "Boundary top", mapInfo.boundaryTop ) );
			}
			if ( SkillLevel.DEVELOPER.isAtLeast() )
				
				data.add( Utils.vector( "File format version", mapInfo.version ) );
		}
		
		table.getXTableModel().setDataVector( data, Utils.vector( "Property", "Value" ) );
		table.pack();
	}
	
}
