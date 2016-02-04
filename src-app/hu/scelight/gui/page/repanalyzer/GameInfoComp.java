/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight.gui.page.repanalyzer;

import hu.scelight.gui.setting.SettingsGui;
import hu.scelight.sc2.rep.model.Replay;
import hu.scelight.sc2.rep.model.initdata.gamedesc.CacheHandle;
import hu.scelight.sc2.rep.model.initdata.gamedesc.GameDescription;
import hu.scelight.sc2.rep.repproc.RepProcessor;
import hu.scelight.service.env.Env;
import hu.scelight.util.Utils;
import hu.scelightapibase.bean.settings.ISettingChangeEvent;
import hu.scelightapibase.bean.settings.ISettingChangeListener;
import hu.sllauncher.gui.comp.table.XTable;
import hu.sllauncher.service.env.LEnv;
import hu.sllauncher.service.settings.LSettings;
import hu.sllauncher.util.DateFormat;
import hu.sllauncher.util.DateValue;
import hu.sllauncher.util.DurationValue;
import hu.sllauncher.util.SizeValue;
import hu.sllauncher.util.SkillLevel;
import hu.sllauncher.util.gui.RenderablePair;

import java.io.IOException;
import java.nio.file.Files;
import java.util.Vector;

/**
 * Game info tab component.
 * 
 * @author Andras Belicza
 */
@SuppressWarnings( "serial" )
public class GameInfoComp extends BaseRepAnalTabComp {
	
	/** Table displaying game info. */
	private final XTable table = new XTable();
	
	/**
	 * Creates a new {@link GameInfoComp}.
	 * 
	 * @param repProc replay processor
	 */
	public GameInfoComp( final RepProcessor repProc ) {
		super( repProc );
		
		buildGui();
	}
	
	/**
	 * Builds the GUI of the tab.
	 */
	private void buildGui() {
		addCenter( table.createWrapperBox( true, table.createToolBarParams( this ) ) );
		
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
		final Replay r = repProc.replay;
		final GameDescription gd = r.initData.getGameDescription();
		
		final Vector< Vector< Object > > data = new Vector< >();
		
		// Basic skill level
		data.add( Utils.vector( "Time values converted to real-time?", repProc.realTime ) );
		data.add( Utils.vector( "File", repProc.file ) );
		try {
			data.add( Utils.vector( "File size", new SizeValue( Files.size( repProc.file ) ) ) );
		} catch ( final IOException ie ) {
			data.add( Utils.vector( "File size", null ) );
		}
		data.add( Utils.vector( "Expansion level", new RenderablePair< >( gd.getExpansionLevel().ricon.size( 24 ), gd.getExpansionLevel().fullText ) ) );
		data.add( Utils.vector( "Version", r.header.versionString() ) );
		if ( SkillLevel.ADVANCED.isAtLeast() )
			data.add( Utils.vector( "Base build", Integer.toString( r.header.baseBuild ) ) ); // No need number formatting
		data.add( Utils.vector( "Date", new DateValue( r.details.getTime(), DateFormat.DATE_TIME_REL_LONG ) ) );
		if ( SkillLevel.NORMAL.isAtLeast() )
			data.add( Utils.vector( "Recorder time zone",
			        ( r.details.getTimeLocalOffsetHour() >= 0 ? "+" : "" ) + Env.LANG.formatNumber( r.details.getTimeLocalOffsetHour(), 2 ) ) );
		data.add( Utils.vector( "Game mode", r.attributesEvents.getGameMode() ) );
		data.add( Utils.vector( "Game speed", gd.getGameSpeed() ) );
		data.add( Utils.vector( "Length", new DurationValue( repProc.getLengthMs() ) ) );
		data.add( Utils.vector( "Format", repProc.getFormat() ) );
		data.add( Utils.vector( "Race Matchup", repProc.getRaceMatchup() + ( repProc.isArchon() ? " (Archon)" : "" ) ) );
		data.add( Utils.vector( "League Matchup", repProc.getLeagueMatchup() ) );
		data.add( Utils.vector( "Region", gd.getRegion() ) );
		data.add( Utils.vector( "Map title", r.details.title ) );
		data.add( Utils.vector( "Map size", gd.getMapSizeX() + "x" + gd.getMapSizeY() ) );
		data.add( Utils.vector( "Is single player?", r.initData.getLobbyState().getIsSinglePlayer() ) );
		if ( r.initData.getLobbyState().getHostUserId() != null )
			data.add( Utils.vector( "Host user", repProc.getUser( r.initData.getLobbyState().getHostUserId() ).uid.fullName ) );
		final CacheHandle[] handles = gd.cacheHandles;
		if ( SkillLevel.BASIC.isAbove() && handles.length > 0 ) {
			data.add( Utils.vector( "Map file name", handles[ handles.length - 1 ] ) );
		}
		data.add( Utils.vector( "Max players", gd.getMaxPlayers() ) );
		data.add( Utils.vector( "Max observers", gd.getObservers() ) );
		data.add( Utils.vector( "Max teams", gd.getMaxTeams() ) );
		data.add( Utils.vector( "Is cooperative mode?", gd.getIsCoopMode() ) );
		data.add( Utils.vector( "Is Blizzard map?", gd.getIsBlizzardMap() ) );
		data.add( Utils.vector( "Is premade FFA?", gd.getIsPremadeFfa() ) );
		data.add( Utils.vector( "AutoMM?", gd.getAmm() ) );
		data.add( Utils.vector( "Competitive?", gd.getCompetitive() ) );
		data.add( Utils.vector( "Battle.net?", gd.getBattleNet() ) );
		
		// Normal skill level
		if ( SkillLevel.NORMAL.isAtLeast() ) {
			// data.add( Utils.vector( "Privacy option", "TODO" ) ); // TODO
			data.add( Utils.vector( "Advanced shared controls?", gd.getAdvancedSharedControls() ) );
			data.add( Utils.vector( "Teams together?", gd.getTeamsTogether() ) );
			data.add( Utils.vector( "Random races?", gd.getRandomRaces() ) );
			data.add( Utils.vector( "Lock teams?", gd.getLockTeams() ) );
			data.add( Utils.vector( "No victory or defeat?", gd.getNoVictoryDefeat() ) );
			data.add( Utils.vector( "Map author", gd.getMapAuthorName() ) );
		}
		
		// Developer skill level
		if ( SkillLevel.DEVELOPER.isAtLeast() ) {
			for ( int i = 0; i < handles.length; i++ )
				data.add( Utils.vector( "Dependency #" + ( i + 1 ), handles[ i ] ) );
		}
		
		table.getXTableModel().setDataVector( data, Utils.vector( "Property", "Value" ) );
		table.packColumnsExceptLast();
	}
}
