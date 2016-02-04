/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight.gui.page.repanalyzer.charts.eventstable;

import hu.scelight.gui.page.repanalyzer.BaseRepAnalTabComp;
import hu.scelight.gui.page.repanalyzer.UserColoredTableRenderer;
import hu.scelight.gui.page.repanalyzer.charts.ChartsComp;
import hu.scelight.gui.setting.SettingsGui;
import hu.scelight.sc2.rep.repproc.RepProcessor;
import hu.scelight.sc2.rep.repproc.User;
import hu.scelight.sc2.rep.s2prot.Event;
import hu.scelight.service.env.Env;
import hu.scelight.service.settings.Settings;
import hu.scelight.util.Utils;
import hu.scelightapi.sc2.rep.model.gameevents.IGameEvents;
import hu.scelightapibase.bean.settings.ISettingChangeEvent;
import hu.scelightapibase.bean.settings.ISettingChangeListener;
import hu.sllauncher.bean.settings.type.Setting;
import hu.sllauncher.gui.comp.XCheckBox;
import hu.sllauncher.gui.comp.XToolBar;
import hu.sllauncher.gui.comp.table.XTable;
import hu.sllauncher.gui.comp.table.XTableModel;
import hu.sllauncher.service.settings.LSettings;
import hu.sllauncher.util.gui.adapter.ActionAdapter;
import hu.sllauncher.util.gui.adapter.ListSelectionAdapter;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;

/**
 * Interactive events table component.
 * 
 * @author Andras Belicza
 */
@SuppressWarnings( "serial" )
public class EventsTableComp extends BaseRepAnalTabComp {
	
	/** Set of Other - Essentials event ids. */
	private static final Set< Integer > OTHER_ESSENTIALS_ID_SET = Utils.asNewSet( IGameEvents.ID_CAMERA_SAVE, IGameEvents.ID_SAVE_GAME,
	                                                                    IGameEvents.ID_SAVE_GAME_DONE, IGameEvents.ID_PLAYER_LEAVE, IGameEvents.ID_GAME_CHEAT,
	                                                                    IGameEvents.ID_RESOURCE_TRADE, IGameEvents.ID_SET_ABSOLUTE_GAME_SPEED,
	                                                                    IGameEvents.ID_ADD_ABSOLUTE_GAME_SPEED, IGameEvents.ID_ALLIANCE,
	                                                                    IGameEvents.ID_RESOURCE_REQUEST, IGameEvents.ID_RESOURCE_REQUEST_FULFILL,
	                                                                    IGameEvents.ID_RESOURCE_REQUEST_CANCEL, IGameEvents.ID_GAME_USER_LEAVE );
	
	/** Set of Other Rest event ids. */
	private static final Set< Integer > OTHER_REST_ID_SET       = new HashSet<>();
	static {
		// Everything is Other - Rest which is not standard and not Other - Essentials.
		final Set< Integer > mainIdSet = Utils.asNewSet( IGameEvents.ID_CMD, IGameEvents.ID_SELECTION_DELTA, IGameEvents.ID_CONTROL_GROUP_UPDATE,
		        IGameEvents.ID_CAMERA_UPDATE );
		
		for ( int i = 0; i < 128; i++ ) {
			final Integer id = i;
			if ( !OTHER_ESSENTIALS_ID_SET.contains( id ) && !mainIdSet.contains( id ) )
				OTHER_REST_ID_SET.add( id );
		}
	}
	
	/** Reference to the charts tab. */
	private final ChartsComp            chartsComp;
	
	
	// Game event filter check boxes
	
	/** Show Cmd game events. */
	private final XCheckBox             showCmdCheckBox;
	
	/** Show SelectionDelta game events. */
	private final XCheckBox             showSelectionDeltaCheckBox;
	
	/** Show ControlGroupUpdate game events. */
	private final XCheckBox             showControlGroupUpdateCheckBox;
	
	/** Show CameraUpdate game events. */
	private final XCheckBox             showCameraUpdateCheckBox;
	
	/** Show Other - Essentials game events. */
	private final XCheckBox             showOtherEssentialsCheckBox;
	
	/** Show Other - Rest game events. */
	private final XCheckBox             showOtherRestCheckBox;
	
	/** Table to display user events. */
	private final XTable                table                   = new XTable();
	
	/** User color column model index holding {@link Color}s. */
	private int                         userColorColIdx         = 0;
	
	/** Event column model index holding the {@link Event}s. */
	private int                         eventColIdx             = 1;
	
	/** Tells if selection is being changed programmatically. */
	private boolean                     programmaticSelection;
	
	
	/**
	 * Creates a new {@link EventsTableComp}.
	 * 
	 * @param repProc replay processor
	 * @param chartsComp reference to the charts tab
	 */
	public EventsTableComp( final RepProcessor repProc, final ChartsComp chartsComp ) {
		super( repProc );
		this.chartsComp = chartsComp;
		
		final ActionAdapter rebuilder = new ActionAdapter() {
			@Override
			public void actionPerformed( final ActionEvent event ) {
				rebuildEventsTable();
			}
		};
		showCmdCheckBox = SettingsGui.createSettingCheckBox( Settings.SHOW_CMD_EVENTS, Env.APP_SETTINGS, rebuilder );
		showCmdCheckBox.setText( "Cmd" );
		showSelectionDeltaCheckBox = SettingsGui.createSettingCheckBox( Settings.SHOW_SELECTION_DELTA_EVENTS, Env.APP_SETTINGS, rebuilder );
		showSelectionDeltaCheckBox.setText( "SelectionDelta" );
		showControlGroupUpdateCheckBox = SettingsGui.createSettingCheckBox( Settings.SHOW_CTRL_GROUP_UPDATE_EVENTS, Env.APP_SETTINGS, rebuilder );
		showControlGroupUpdateCheckBox.setText( "ControlGroupUpdate" );
		showCameraUpdateCheckBox = SettingsGui.createSettingCheckBox( Settings.SHOW_CAMERA_UPDATE_EVENTS, Env.APP_SETTINGS, rebuilder );
		showCameraUpdateCheckBox.setText( "CameraUpdate" );
		showOtherEssentialsCheckBox = SettingsGui.createSettingCheckBox( Settings.SHOW_OTHER_ESSENTIALS_EVENTS, Env.APP_SETTINGS, rebuilder );
		showOtherEssentialsCheckBox.setText( "Other (Essentials)" );
		showOtherRestCheckBox = SettingsGui.createSettingCheckBox( Settings.SHOW_OTHER_REST_EVENTS, Env.APP_SETTINGS, rebuilder );
		showOtherRestCheckBox.setText( "Other (Rest)" );
		SettingsGui.bindVisibilityToSkillLevel( showOtherRestCheckBox, Settings.SHOW_OTHER_REST_EVENTS.skillLevel, Boolean.FALSE );
		
		// Listened launcher settings
		// Show Other Rest check box is only visible on ADVANCED level, make sure it is unchecked:
		final ISettingChangeListener scll = new ISettingChangeListener() {
			@Override
			public void valuesChanged( final ISettingChangeEvent event ) {
				if ( event.affected( LSettings.SKILL_LEVEL ) )
					rebuilder.actionPerformed( null );
			}
		};
		SettingsGui.addBindExecuteScl( scll, Env.LAUNCHER_SETTINGS, LSettings.SKILL_LEVEL.SELF_SET, showOtherRestCheckBox );
		
		// Listened app settings
		final ISettingChangeListener scla = new ISettingChangeListener() {
			@Override
			public void valuesChanged( final ISettingChangeEvent event ) {
				if ( event.affected( Settings.TIME_IN_SECONDS ) )
					rebuildEventsTable();
			}
		};
		final Set< Setting< ? > > listenedSettingSet = Utils.< Setting< ? > > asNewSet( Settings.TIME_IN_SECONDS );
		SettingsGui.addBindExecuteScl( scla, Env.APP_SETTINGS, listenedSettingSet, this );
		
		buildGui();
		
		// Custom renderer to change the foreground and background based on the user color
		table.setTableCellRenderer( new UserColoredTableRenderer( table, userColorColIdx ) );
		table.getSelectionModel().addListSelectionListener( new ListSelectionAdapter() {
			@Override
			public void valueChanged( final ListSelectionEvent event ) {
				if ( programmaticSelection )
					return;
				if ( event != null && event.getValueIsAdjusting() )
					return;
				final int row = table.getSelectedRow();
				if ( row < 0 )
					return;
				
				final Event e = (Event) table.getModel().getValueAt( table.convertRowIndexToModel( row ), eventColIdx );
				chartsComp.getChartsCanvas().setMarkerLoop1( e.loop );
			}
		} );
	}
	
	/**
	 * Builds the GUI of the tab.
	 */
	private void buildGui() {
		final XToolBar toolBar = new XToolBar();
		addNorth( toolBar );
		toolBar.add( showCmdCheckBox );
		toolBar.add( showSelectionDeltaCheckBox );
		toolBar.add( showControlGroupUpdateCheckBox );
		toolBar.add( showCameraUpdateCheckBox );
		toolBar.add( showOtherEssentialsCheckBox );
		toolBar.addSeparator();
		toolBar.add( showOtherRestCheckBox );
		toolBar.finalizeLayout();
		
		// Use charts tab as the root component to register key strokes at
		addCenter( table.createWrapperBox( true, table.createToolBarParams( chartsComp ) ) );
	}
	
	/**
	 * Rebuilds the events table.
	 */
	public void rebuildEventsTable() {
		// Allowed (not filtered) event ids
		final Set< Integer > allowedIds = new HashSet<>();
		
		if ( showCmdCheckBox.isSelected() )
			allowedIds.add( IGameEvents.ID_CMD );
		if ( showSelectionDeltaCheckBox.isSelected() )
			allowedIds.add( IGameEvents.ID_SELECTION_DELTA );
		if ( showControlGroupUpdateCheckBox.isSelected() )
			allowedIds.add( IGameEvents.ID_CONTROL_GROUP_UPDATE );
		if ( showCameraUpdateCheckBox.isSelected() )
			allowedIds.add( IGameEvents.ID_CAMERA_UPDATE );
		
		if ( showOtherEssentialsCheckBox.isSelected() )
			allowedIds.addAll( OTHER_ESSENTIALS_ID_SET );
		if ( showOtherRestCheckBox.isSelected() )
			allowedIds.addAll( OTHER_REST_ID_SET );
		
		
		final boolean seconds = Env.APP_SETTINGS.get( Settings.TIME_IN_SECONDS );
		
		final XTableModel model = table.getXTableModel();
		
		final boolean showRawParams = Settings.SHOW_RAW_PARAMETERS.skillLevel.isAtLeast() && Env.APP_SETTINGS.get( Settings.SHOW_RAW_PARAMETERS );
		
		final Vector< String > columns = Utils.asNewVector( "User Color", "Event", "I", "Time", "User", "Event", "Parameters" );
		if ( showRawParams )
			columns.add( "Raw Parameters" );
		final Vector< Vector< Object > > data = new Vector<>();
		
		final User[] userIdUsers = repProc.usersByUserId;
		final boolean[] userByIdEnableds = chartsComp.getUserByUserIdEnableds();
		
		final int rowheight = Env.APP_SETTINGS.get( Settings.EVENTS_TABLE_ROW_HEIGHT );
		for ( final Event e : repProc.replay.gameEvents.events )
			if ( userByIdEnableds[ e.userId ] && allowedIds.contains( e.id ) ) {
				final Vector< Object > row = new Vector<>( columns.size() );
				final User u = userIdUsers[ e.userId ];
				
				row.add( u == null ? null : u.getPlayerColor().darkerColor );
				row.add( e );
				row.add( e.getRicon().size( rowheight ) );
				row.add( seconds ? repProc.formatLoopTime( e.loop ) : Env.LANG.formatNumber( e.loop ) );
				row.add( u == null ? null : u.uid.fullName );
				row.add( e.name );
				row.add( e.getParameters( repProc ) );
				if ( showRawParams )
					row.add( e.getRawParameters() );
				
				data.add( row );
			}
		
		model.setDataVector( data, columns );
		table.removeColumn( table.getColumnModel().getColumn( eventColIdx ) ); // Hide Event column
		table.removeColumn( table.getColumnModel().getColumn( userColorColIdx ) ); // Hide Color column
		table.setRowHeight( rowheight );
		table.setSortable( false ); // Disable sorting
		
		if ( Env.APP_SETTINGS.get( Settings.EVENTS_STRETCH_TABLE ) ) {
			table.setAutoResizeMode( JTable.AUTO_RESIZE_ALL_COLUMNS );
			// Pack all columns except the parameter(s) column(s)
			for ( int i = table.getColumnCount() - ( showRawParams ? 3 : 2 ); i >= 0; i-- )
				table.packColumns( i );
		} else {
			table.setAutoResizeMode( JTable.AUTO_RESIZE_OFF );
			table.pack();
		}
	}
	
	/**
	 * Selects the event closest to the specified loop.
	 * 
	 * @param loop loop whose closest event to select
	 */
	public void selectByLoop( final int loop ) {
		final XTableModel model = table.getXTableModel();
		
		// Iterate over visible rows!
		for ( int i = table.getRowCount() - 1; i >= 0; i-- ) {
			final Event e = (Event) model.getValueAt( table.convertRowIndexToModel( i ), eventColIdx );
			if ( e.loop <= loop ) {
				programmaticSelection = true;
				table.getSelectionModel().setSelectionInterval( i, i );
				table.scrollRectToVisible( table.getCellRect( i, i, true ) );
				programmaticSelection = false;
				break;
			}
		}
	}
	
	/**
	 * Selects the specified event.
	 * 
	 * @param event event to be selected
	 */
	public void selectEvent( final Event event ) {
		final XTableModel model = table.getXTableModel();
		
		// Iterate over visible rows!
		for ( int i = table.getRowCount() - 1; i >= 0; i-- ) {
			final Event e = (Event) model.getValueAt( table.convertRowIndexToModel( i ), eventColIdx );
			if ( e == event ) {
				programmaticSelection = true;
				table.getSelectionModel().setSelectionInterval( i, i );
				table.scrollRectToVisible( table.getCellRect( i, i, true ) );
				programmaticSelection = false;
				break;
			}
		}
	}
	
}
