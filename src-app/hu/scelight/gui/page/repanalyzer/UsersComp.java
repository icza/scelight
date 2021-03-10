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

import hu.scelight.sc2.rep.model.details.Race;
import hu.scelight.sc2.rep.model.details.Toon;
import hu.scelight.sc2.rep.model.initdata.lobbystate.Controller;
import hu.scelight.sc2.rep.model.initdata.lobbystate.PlayerColor;
import hu.scelight.sc2.rep.model.initdata.lobbystate.Role;
import hu.scelight.sc2.rep.model.initdata.userinitdata.League;
import hu.scelight.sc2.rep.repproc.RepProcessor;
import hu.scelight.sc2.rep.repproc.User;
import hu.scelight.service.env.Env;
import hu.scelight.util.Utils;
import hu.scelight.util.gui.TableIcon;
import hu.sllauncher.gui.comp.table.XTable;
import hu.sllauncher.gui.comp.table.renderer.ProgressBarView;
import hu.sllauncher.util.gui.RenderablePair;

import java.awt.Color;
import java.awt.Dimension;
import java.util.List;
import java.util.Vector;

/**
 * Users tab component.
 * 
 * @author Andras Belicza
 */
@SuppressWarnings( "serial" )
public class UsersComp extends BaseRepAnalTabComp {
	
	/**
	 * Creates a new {@link UsersComp}.
	 * 
	 * @param repProc replay processor
	 */
	public UsersComp( final RepProcessor repProc ) {
		super( repProc );
		
		buildGui();
	}
	
	/**
	 * Builds the GUI of the tab.
	 */
	private void buildGui() {
		// TODO: Add EAPM
		
		// TODO add an option to the tool bar: "Transposed table"
		// If checked, make users the columns, and properties the rows
		// Also see: http://tips4java.wordpress.com/2008/11/18/row-number-table/
		
		final XTable table = new XTable();
		
		table.setRowHeightForProgressBar();
		
		final Vector< Object > columns = Utils.vector( "User Color", "Team", "Color", "Name", "Race", "Result", "<html>Highest<br>League</html>", "MMR", "APM", "SPM",
		        "SQ", "<html>Supply-<br>capped %</html>", "<html>Swarm<br>Levels</html>", "<html>Start<br>Dir</html>", "Role", "Control", "Toon", "Slot",
		        "Handicap" );
		final List< Class< ? > > columnClasses = Utils.< Class< ? > > asNewList( Color.class, Integer.class, PlayerColor.class, String.class,
		        RenderablePair.class, RenderablePair.class, TableIcon.class, Integer.class, ProgressBarView.class, ProgressBarView.class, ProgressBarView.class,
		        ProgressBarView.class, ProgressBarView.class, Integer.class, Role.class, Controller.class, Toon.class, Integer.class, Integer.class );
		
		final int userColorColIdx = 0;
		final int resultColIdx = 5;
		final int leagueColIdx = 6;
		final int apmColIdx = 7;
		final int spmColIdx = 8;
		final int sqColIdx = 9;
		final int supplyCappedColIdx = 10;
		final int levelsColIdx = 11;
		final int startDirColIdx = 12;
		final int handicapColIdx = 17;
		
		table.setTableCellRenderer( new UserColoredTableRenderer( table, userColorColIdx ) );
		
		boolean areReducedHandicaps = false;
		
		// Determine max values for progress bar cells
		int maxLevels = 0;
		int maxApm = 0;
		double maxSpm = 0;
		int maxSq = 0;
		double maxSuppCapped = 0;
		for ( final User u : repProc.users ) {
			if ( u.uid != null && u.uid.getCombinedRaceLevels() != null )
				maxLevels = Math.max( maxLevels, u.uid.getCombinedRaceLevels() );
			maxApm = Math.max( maxApm, u.apm );
			maxSpm = Math.max( maxSpm, u.spm );
			maxSq = Math.max( maxSq, u.sq );
			maxSuppCapped = Math.max( maxSuppCapped, u.supplyCappedPercent );
		}
		
		final Vector< Vector< Object > > data = new Vector<>();
		for ( final User u : repProc.users ) {
			final Vector< Object > row = new Vector<>( columns.size() );
			
			row.add( u.getPlayerColor().darkerColor );
			row.add( u.slot.teamId + 1 );
			row.add( u.getPlayerColor() );
			row.add( u.fullName );
			row.add( u.player == null ? null : new RenderablePair<>( u.player.race.ricon.get(), u.player.race.text
			        + ( u.slot.getChosenRace() == Race.RANDOM ? " (Random)" : "" ) ) );
			row.add( u.player == null ? null : new RenderablePair<>( u.player.getResult().ricon.get(), u.player.getResult().text
			        + ( u.player.isResultDeduced() ? " (Deduced)" : "" ) ) );
			row.add( u.uid == null ? null : u.uid.getHighestLeague().tableIcon );
			
			row.add( u.mmr );
			
			row.add( new ProgressBarView( u.apm, maxApm ) );
			row.add( new ProgressBarView( (int) ( u.spm * 100 ), (int) ( maxSpm * 100 ), Env.LANG.formatNumber( u.spm, 2 ) ) );
			row.add( new ProgressBarView( u.sq, maxSq ) );
			row.add( new ProgressBarView( (int) ( u.supplyCappedPercent * 100 ), (int) ( maxSuppCapped * 100 ), Env.LANG
			        .formatNumber( u.supplyCappedPercent, 2 ) + "%" ) );
			row.add( u.uid == null || u.uid.getCombinedRaceLevels() == null ? null : new ProgressBarView( u.uid.getCombinedRaceLevels(), maxLevels ) );
			row.add( u.startDirection );
			row.add( u.slot.getRole() );
			row.add( u.slot.getController() );
			row.add( u.getToon() );
			row.add( u.slotIdx + 1 );
			final Integer handicap = u.slot.getHandicap();
			row.add( handicap );
			if ( handicap != null && handicap < 100 )
				areReducedHandicaps = true;
			
			data.add( row );
		}
		table.getXTableModel().setDataVector( data, columns );
		table.getXTableModel().setColumnClasses( columnClasses );
		// Custom comparators and sort orders
		table.getXTableRowSorter().setComparator( leagueColIdx, League.TABLE_ICON_COMPARATOR );
		table.getXTableRowSorter().setColumnDefaultDescs( true, resultColIdx, leagueColIdx, levelsColIdx, apmColIdx, spmColIdx, sqColIdx );
		table.pack();
		
		// Remove columns going downward!
		if ( !areReducedHandicaps ) // All players have 100% handicap, hide the Handicap column
			table.removeColumn( table.getColumnModel().getColumn( handicapColIdx ) );
		if ( repProc.replay.trackerEvents == null )
			table.removeColumn( table.getColumnModel().getColumn( startDirColIdx ) );
		if ( repProc.replay.header.major < 2 )
			table.removeColumn( table.getColumnModel().getColumn( levelsColIdx ) );
		if ( repProc.replay.trackerEvents == null )
			table.removeColumn( table.getColumnModel().getColumn( supplyCappedColIdx ) );
		if ( repProc.replay.trackerEvents == null )
			table.removeColumn( table.getColumnModel().getColumn( sqColIdx ) );
		if ( repProc.replay.header.major < 2 )
			table.removeColumn( table.getColumnModel().getColumn( leagueColIdx ) );
		table.removeColumn( table.getColumnModel().getColumn( userColorColIdx ) ); // Hide color column
		
		table.getTableHeader().setPreferredSize( new Dimension( 10, table.getTableHeader().getPreferredSize().height * 2 ) );
		
		addCenter( table.createWrapperBox( true, table.createToolBarParams( this ) ) );
	}
	
}
