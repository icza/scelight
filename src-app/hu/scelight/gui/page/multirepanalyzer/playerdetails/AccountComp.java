/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight.gui.page.multirepanalyzer.playerdetails;

import hu.scelight.gui.icon.Icons;
import hu.scelight.gui.page.multirepanalyzer.model.Game;
import hu.scelight.gui.page.multirepanalyzer.model.GeneralStats;
import hu.scelight.gui.page.multirepanalyzer.model.Part;
import hu.scelight.gui.page.multirepanalyzer.model.PlayerStats;
import hu.scelight.sc2.rep.model.details.Toon;
import hu.scelight.service.env.Env;
import hu.scelight.util.Utils;
import hu.scelightapi.sc2.rep.model.details.IToon;
import hu.sllauncher.action.XAction;

import java.awt.event.ActionEvent;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Account component.
 * 
 * @author Andras Belicza
 */
@SuppressWarnings( "serial" )
public class AccountComp extends GeneralTableStatsComp< IToon, IToon > {
	
	/**
	 * Creates a new {@link AccountComp}.
	 * 
	 * @param displayName display name to be used when replays of stats rows are opened
	 * @param playerStats player stats to show account stats for
	 */
	public AccountComp( final String displayName, final PlayerStats playerStats ) {
		super( displayName );
		
		toolBar.addSeparator();
		
		toolBar.addSelEnabledButton( new XAction( Icons.SC2_PROFILE, "View Character Profile of the Selected Player", this ) {
			@Override
			public void actionPerformed( final ActionEvent event ) {
				final IToon toon = (IToon) model.getValueAt( table.getSelectedModelRow(), objColIdx );
				if ( toon.isZero() )
					return;
				
				// Cut off clan tag
				// This toon contains player name as we constructed it!
				final String name = toon.getPlayerName();
				final URL profileUrl = toon.getProfileUrl( name.indexOf( ']' ) >= 0 ? name.substring( name.indexOf( ']' ) + 1 ) : name );
				if ( profileUrl != null )
					Env.UTILS_IMPL.get().showURLInBrowser( profileUrl );
			}
		} );
		
		toolBar.addSelEnabledButton( new XAction( Icons.F_CLIPBOARD_SIGN, "Copy Toon of the Selected Player", this ) {
			@Override
			public void actionPerformed( final ActionEvent event ) {
				final IToon toon = (IToon) model.getValueAt( table.getSelectedModelRow(), objColIdx );
				if ( toon.isZero() )
					return;
				
				Utils.copyToClipboard( toon.toString() );
			}
		} );
		
		buildGui( playerStats, "<html><br>Account</html>", IToon.class );
	}
	
	@Override
	protected Collection< GeneralStats< IToon > > calculateStats( final PlayerStats playerStats ) {
		// Calculate stats
		final Map< IToon, GeneralStats< IToon > > generalStatsMap = new HashMap<>();
		for ( final Game g : playerStats.gameList )
			for ( final Part refPart : g.parts ) { // Have to go through all participants as the zero-toon might be present multiple times
				if ( !playerStats.obj.equals( refPart.toon ) )
					continue;
				
				GeneralStats< IToon > gs = generalStatsMap.get( refPart.originalToon );
				if ( gs == null ) {
					final String name = playerStats.mergedToonNameMap == null ? playerStats.name
					        : playerStats.mergedToonNameMap.get( refPart.originalToon ).name;
					generalStatsMap.put( refPart.originalToon, gs = new GeneralStats< IToon >( new Toon( refPart.originalToon.toString() + "-" + name ) ) );
				}
				gs.updateWithPartGame( refPart, g );
			}
		
		return generalStatsMap.values();
	}
	
}
