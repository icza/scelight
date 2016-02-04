/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight.gui.page.repanalyzer.buildorders;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import hu.scelight.gui.page.repanalyzer.BaseRepAnalTabComp;
import hu.scelight.sc2.balancedata.BalanceData;
import hu.scelight.sc2.balancedata.BdUtil;
import hu.scelight.sc2.balancedata.model.Entity;
import hu.scelight.sc2.rep.model.trackerevents.TrackerEvents;
import hu.scelight.sc2.rep.model.trackerevents.UnitBornEvent;
import hu.scelight.sc2.rep.model.trackerevents.UnitInitEvent;
import hu.scelight.sc2.rep.model.trackerevents.UpgradeEvent;
import hu.scelight.sc2.rep.repproc.RepProcessor;
import hu.scelight.sc2.rep.repproc.User;
import hu.scelight.sc2.rep.s2prot.Event;
import hu.scelight.service.env.Env;
import hu.scelight.service.settings.Settings;
import hu.scelight.util.Utils;
import hu.scelightapi.sc2.rep.model.trackerevents.ITrackerEvents;
import hu.sllauncher.gui.comp.XLabel;
import hu.sllauncher.gui.comp.table.XTable;
import hu.sllauncher.util.gui.RenderablePair;

/**
 * Build orders tab component.
 * 
 * @author Andras Belicza
 */
@SuppressWarnings( "serial" )
public class BuildOrdersComp extends BaseRepAnalTabComp {
	
	/** Unit type names to exclude. */
	private static final Set< String > EXCLUDE_UNIT_SET	= Utils.asNewSet( BdUtil.UNIT_BROODLING, BdUtil.UNIT_BROODLING_ESCORT, BdUtil.UNIT_LARVA,
	        BdUtil.UNIT_LOCUST, BdUtil.UNIT_REAPER_PLACEHOLDER );
			
	/** Table displaying the build order elements. */
	private XTable					   table;
									   
	/**
	 * Creates a new {@link BuildOrdersComp}.
	 * 
	 * @param repProc replay processor
	 */
	public BuildOrdersComp( final RepProcessor repProc ) {
		super( repProc );
		
		buildGui();
	}
	
	/**
	 * Builds the GUI of the tab.
	 */
	private void buildGui() {
		if ( repProc.replay.trackerEvents == null ) {
			addNorth( new XLabel( "Precise Build Order information is available only from replay version 2.0.8. This replay has version "
			        + repProc.replay.header.versionString( false ) + "." ).boldFont().allBorder( 10 ).color( Color.RED ) );
			return;
		}
		
		if ( repProc.replay.getBalanceData() == null ) {
			addNorth(
			        new XLabel( "Precise Build Order information requires Balance Data which is not available for this replay version. This replay has version "
			                + repProc.replay.header.versionString( false ) + "." ).boldFont().allBorder( 10 ).color( Color.RED ) );
			return;
		}
		
		table = new XTable();
		addCenter( table.createWrapperBox( true, table.createToolBarParams( this ) ) );
		
		rebuildTableData();
	}
	
	/**
	 * Rebuilds the table data.
	 */
	private void rebuildTableData() {
		final TrackerEvents trackerEvents = repProc.replay.trackerEvents;
		final BalanceData balanceData = repProc.replay.getBalanceData();
		
		final boolean archon = repProc.isArchon();
		
		final Vector< Object > columns = new Vector< >();
		columns.add( "Time" );
		if ( archon ) {
			for ( int team = 0; team < 2; team++ ) {
				StringBuilder sb = null;
				User leader = null;
				for ( final User u : repProc.playerUsers ) {
					if ( u.slot.teamId != team )
						continue;
					if ( sb == null ) {
						sb = new StringBuilder( "TEAM " ).append( team + 1 ).append( " &lt;" );
						leader = u.slot.getTandemLeaderUserId() == null ? u : repProc.usersByUserId[ u.slot.getTandemLeaderUserId() ];
					} else
						sb.append( ", " );
					sb.append( u.fullName );
				}
				sb.append( "&gt;" );
				columns.add( "<html><div style='" + leader.player.race.ricon.getCSS() + "'/>" + sb.toString() + "&nbsp;<tt style='background:"
				        + leader.getPlayerColor().cssColor + "'>&nbsp;&nbsp;</tt></html" );
			}
		} else {
			for ( final User u : repProc.playerUsers ) {
				columns.add( "<html><div style='" + u.player.race.ricon.getCSS() + "'/>" + u.fullName + "&nbsp;<tt style='background:"
				        + u.getPlayerColor().cssColor + "'>&nbsp;&nbsp;</tt></html" );
			}
		}
		
		// TODO also display current/max supply, like "7/9 Overlord"
		final int rowHeight = Env.APP_SETTINGS.get( Settings.BUILD_ORDERS_ROW_HEIGHT );
		
		// First gather Build orders elements
		final List< BoElement > boelList = new ArrayList< >();
		for ( final Event e : trackerEvents.events ) {
			// Neutral units like Mineral field are enumerated at loop = 0
			if ( e.loop == 0 )
				continue;
				
			final Integer playerId;
			final Entity entity;
			int loop = 0;
			
			// TODO subtracting the costTime does not take Chrono Boosts into effect for example!
			switch ( e.id ) {
				case ITrackerEvents.ID_UNIT_INIT : {
					loop = e.loop;
					final UnitInitEvent uie = (UnitInitEvent) e;
					playerId = uie.getControlPlayerId();
					entity = balanceData.getUnit( uie.getUnitTypeName().toString() );
					break;
				}
				case ITrackerEvents.ID_UPGRADE : {
					playerId = e.getPlayerId();
					entity = balanceData.getUpgradeCmd( ( (UpgradeEvent) e ).getUpgradeTypeName().toString() );
					if ( entity != null )
						loop = e.loop - (int) ( entity.costTime * 16 );
					break;
				}
				case ITrackerEvents.ID_UNIT_BORN : {
					final UnitBornEvent ube = (UnitBornEvent) e;
					playerId = ube.getControlPlayerId();
					if ( playerId == 0 ) // Neutral unit, e.g. "ForceField"
						continue;
					if ( EXCLUDE_UNIT_SET.contains( ube.getUnitTypeName().toString() ) )
						continue;
					entity = balanceData.getUnit( ube.getUnitTypeName().toString() );
					if ( entity != null )
						loop = e.loop - (int) ( entity.costTime * 16 );
					break;
				}
					// TODO unit type change? (e.g. Overseer, but not all, e.g. supply depot lowered)
				default :
					continue;
			}
			
			if ( playerId == null || entity == null )
				continue;
				
			final BoElement boel = new BoElement();
			boel.loop = loop;
			boel.playerId = playerId;
			boel.entity = entity;
			boelList.add( boel );
		}
		
		// Sort elements by time
		Collections.sort( boelList );
		
		// Now construct table data
		final Vector< Vector< Object > > data = new Vector< >( boelList.size() );
		for ( final BoElement boel : boelList ) {
			final User user = repProc.usersByPlayerId[ boel.playerId ];
			// If user is null, we would not know to which column to put this BO item.
			// Observed on map: "SC2 Micro Tournament EU"
			if ( user == null )
				continue;
				
			final Vector< Object > row = new Vector< >( columns.size() );
			
			row.add( repProc.formatLoopTime( boel.loop ) );
			if ( archon ) {
				// 2 columns only, playerId is either 1 or 2 
				if ( boel.playerId > 1 )
					row.add( null );
			} else {
				for ( int i = user.playerUserIdx; i > 0; i-- )
					row.add( null );
			}
			row.add( new RenderablePair< >( boel.entity.getRicon().size( rowHeight ), boel.entity.text ) );
			
			data.add( row );
		}
		
		table.getXTableModel().setDataVector( data, columns );
		table.setRowHeight( rowHeight );
		table.packColumns( 0 );
		table.setSortable( false ); // Disable sorting
	}
	
}
