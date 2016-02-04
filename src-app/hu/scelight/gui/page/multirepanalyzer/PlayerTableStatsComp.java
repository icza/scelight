/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight.gui.page.multirepanalyzer;

import hu.scelight.gui.icon.Icons;
import hu.scelight.gui.page.multirepanalyzer.model.PlayerStats;
import hu.scelight.sc2.rep.model.details.Toon;
import hu.scelight.service.env.Env;
import hu.scelight.util.Utils;
import hu.sllauncher.action.XAction;

import java.awt.event.ActionEvent;
import java.net.URL;
import java.util.Map;

/**
 * Table stats component showing stats related to players.
 * 
 * @author Andras Belicza
 */
@SuppressWarnings( "serial" )
public class PlayerTableStatsComp extends TableStatsComp {
	
	/** Toon model column index. */
	protected int toonColIdx;
	
	/**
	 * Creates a new {@link PlayerTableStatsComp}.
	 * 
	 * @param displayName display name to be used when replays of stats rows are opened
	 */
	public PlayerTableStatsComp( final String displayName ) {
		super( displayName );
	}
	
	/**
	 * Builds the tool bar: adds player specific actions.
	 * 
	 * @param multiRepAnalyzerComp reference to the Multi-replay Analyzer component
	 * @param playerStatsMap reference to the map of all player stats; required when the details of a playmate is to be opened and to get the newest name can be
	 *            used to construct bnet URLs
	 */
	protected void buildToolBar( final MultiRepAnalyzerComp multiRepAnalyzerComp, final Map< Toon, PlayerStats > playerStatsMap ) {
		final XAction openAction = new XAction( Icons.F_CHART_UP, "Open the Details of the Selected Player (Enter)", this ) {
			@Override
			public void actionPerformed( final ActionEvent event ) {
				final PlayerStats stats = (PlayerStats) model.getValueAt( table.getSelectedModelRow(), statsColIdx );
				multiRepAnalyzerComp.addPlayerDetailsTab( playerStatsMap.get( stats.obj ), playerStatsMap );
			}
		};
		toolBar.addSelEnabledButton( openAction );
		
		toolBar.addSeparator();
		
		toolBar.addSelEnabledButton( new XAction( Icons.SC2_PROFILE, "View Character Profile of the Selected Player", this ) {
			@Override
			public void actionPerformed( final ActionEvent event ) {
				final PlayerStats stats = (PlayerStats) model.getValueAt( table.getSelectedModelRow(), statsColIdx );
				if ( stats.obj.isZero() )
					return;
				
				// Cut off clan tag
				final URL profileUrl = stats.obj.getProfileUrl( stats.name.indexOf( ']' ) >= 0 ? stats.name.substring( stats.name.indexOf( ']' ) + 1 )
				        : stats.name );
				if ( profileUrl != null )
					Env.UTILS_IMPL.get().showURLInBrowser( profileUrl );
			}
		} );
		
		toolBar.addSelEnabledButton( new XAction( Icons.F_CLIPBOARD_SIGN, "Copy Toon of the Selected Player", this ) {
			@Override
			public void actionPerformed( final ActionEvent event ) {
				final PlayerStats stats = (PlayerStats) model.getValueAt( table.getSelectedModelRow(), statsColIdx );
				if ( stats.obj.isZero() )
					return;
				
				Utils.copyToClipboard( stats.obj + "-" + stats.name );
			}
		} );
		
		toolBar.finalizeLayout();
		
		table.setOpenAction( openAction );
	}
	
}
