/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight.gui.comp;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.nio.file.Paths;

import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import hu.scelight.gui.icon.Icons;
import hu.scelight.sc2.map.cache.MapImageCache;
import hu.scelight.sc2.rep.factory.RepParserEngine;
import hu.scelight.sc2.rep.model.Replay;
import hu.scelight.sc2.rep.model.initdata.gamedesc.Region;
import hu.scelight.sc2.rep.repproc.RepProcessor;
import hu.scelight.service.env.Env;
import hu.scelight.util.gui.GuiUtils;
import hu.scelight.util.sc2rep.RepUtils;
import hu.sllauncher.gui.comp.GridBagPanel;
import hu.sllauncher.gui.comp.XFileChooser;
import hu.sllauncher.gui.comp.XLabel;
import hu.sllauncher.gui.icon.LRIcon;

/**
 * A replay info box to be used for {@link XFileChooser} as an accessory ({@link XFileChooser#setAccessory(javax.swing.JComponent)}).
 * 
 * @author Andras Belicza
 */
@SuppressWarnings( "serial" )
public class RepInfoBox extends GridBagPanel {
	
	/** Replay info title label. */
	private XLabel repInfoLabel	  = new XLabel( "Select a replay file." );
								  
	/** Label to display replay date. */
	private XLabel dateLabel	  = new XLabel();
								  
	/** Label to display replay version. */
	private XLabel versionLabel	  = new XLabel();
								  
	/** Label to display game mode. */
	private XLabel modeLabel	  = new XLabel();
								  
	/** Label to display game length. */
	private XLabel lengthLabel	  = new XLabel();
								  
	/** Label to display average league. */
	private XLabel avgLeagueLabel = new XLabel();
								  
	/** Label to display region. */
	private XLabel regionLabel	  = new XLabel();
								  
	/** Label to display matchup. */
	private XLabel matchupLabel	  = new XLabel();
								  
	/** Label to display players. */
	private XLabel playersLabel	  = new XLabel();
								  
	/** Label to display map title. */
	private XLabel mapTitleLabel  = new XLabel();
								  
	/** Label to display map image. */
	private XLabel mapImageLabel  = new XLabel( null, SwingConstants.CENTER );
								  
	/**
	 * Creates a new {@link RepInfoBox}.
	 * 
	 * @param fileChooser file chooser whose selected file to listen
	 */
	public RepInfoBox( final XFileChooser fileChooser ) {
		fileChooser.addPropertyChangeListener( new PropertyChangeListener() {
			@Override
			public void propertyChange( final PropertyChangeEvent event ) {
				if ( XFileChooser.SELECTED_FILE_CHANGED_PROPERTY.equals( event.getPropertyName() ) ) {
					final File file = (File) event.getNewValue();
					if ( file == null || file.isDirectory() )
						return;
					setReplayFile( file.getAbsolutePath() );
				}
			}
		} );
		
		buildGui( fileChooser );
		
		setPreferredSize( new Dimension( 310, 450 ) );
	}
	
	/**
	 * Builds the GUI of the rep info box.
	 * 
	 * @param fileChooser file chooser whose selected file to listen
	 */
	private void buildGui( final XFileChooser fileChooser ) {
		final Dimension mindim = new Dimension( 16, 16 );
		
		final GridBagConstraints c = this.c;
		c.insets.set( 1, 1, 0, 1 );
		
		nextRow();
		addDouble( RepUtils.createRepFoldersShortcutCombo( fileChooser ) );
		
		nextRow();
		addDouble( new JSeparator() );
		
		nextRow();
		addDouble( GuiUtils.boldFont( repInfoLabel ) );
		
		nextRow();
		addSingle( new XLabel( "Date:" ) );
		c.weightx = 1; // Specify that the first column is compact and 2nd column gets the extra space
		addSingle( dateLabel );
		c.weightx = 0;
		
		nextRow();
		addSingle( new XLabel( "Version:" ) );
		versionLabel.setPreferredSize( mindim );
		versionLabel.setMaximumSize( mindim );
		versionLabel.setMinimumSize( mindim );
		addSingle( versionLabel );
		
		nextRow();
		addSingle( new XLabel( "Mode:" ) );
		addSingle( modeLabel );
		
		nextRow();
		addSingle( new XLabel( "Length:" ) );
		addSingle( lengthLabel );
		
		nextRow();
		addSingle( new XLabel( "Avg League:" ) );
		avgLeagueLabel.setPreferredSize( mindim );
		avgLeagueLabel.setMaximumSize( mindim );
		avgLeagueLabel.setMinimumSize( mindim );
		addSingle( avgLeagueLabel );
		
		nextRow();
		addSingle( new XLabel( "Matchup:" ) );
		addSingle( matchupLabel );
		
		nextRow();
		addSingle( new XLabel( "Region:" ) );
		addSingle( regionLabel );
		
		nextRow();
		c.anchor = GridBagConstraints.NORTHWEST;
		addSingle( new XLabel( "Players:" ) );
		addSingle( playersLabel );
		c.anchor = GridBagConstraints.LINE_START;
		
		nextRow();
		addSingle( new XLabel( "Map:" ) );
		addSingle( mapTitleLabel );
		
		nextRow();
		c.weighty = 1; // Remaining space for the map image
		addDouble( mapImageLabel );
		c.weighty = 0;
	}
	
	/**
	 * Sets the specified replay file to provide info about.
	 * 
	 * @param fileName replay file name to provide info about
	 */
	public void setReplayFile( final String fileName ) {
		final RepProcessor repProc = RepParserEngine.getRepProc( Paths.get( fileName ) );
		
		if ( repProc == null ) {
			
			repInfoLabel.setText( "Failed to parse replay!" );
			dateLabel.setText( null );
			versionLabel.setIcon( null );
			versionLabel.setText( null );
			modeLabel.setText( null );
			lengthLabel.setText( null );
			avgLeagueLabel.setIcon( null );
			matchupLabel.setText( null );
			regionLabel.setText( null );
			regionLabel.setIcon( null );
			playersLabel.setText( null );
			mapTitleLabel.setText( null );
			mapImageLabel.setIcon( null );
			
		} else {
			
			final Replay replay = repProc.replay;
			
			repInfoLabel.setText( "Replay info:" );
			
			dateLabel.setText( Env.LANG.formatDateTime( replay.details.getTime() ) );
			versionLabel.setIcon( replay.initData.getGameDescription().getExpansionLevel().ricon.size( 24 ) );
			versionLabel.setText( replay.header.versionString() );
			modeLabel.setText( replay.attributesEvents.getGameMode().text );
			lengthLabel.setText( repProc.formatLoopTime( replay.header.getElapsedGameLoops() ) );
			avgLeagueLabel.setIcon( repProc.getAvgLeague().ricon.get() );
			final Region region = replay.initData.getGameDescription().getRegion();
			regionLabel.setText( region.text );
			regionLabel.setIcon( region.ricon == null ? null : region.ricon.get() );
			matchupLabel.setText( repProc.getRaceMatchup() + ( repProc.isArchon() ? " (Archon)" : "" ) );
			// Wrap player names in HTML paragraph so it can break lines if too long
			playersLabel.setText( "<html><p>" + repProc.getPlayersString() + "</p></html>" );
			mapTitleLabel.setText( replay.details.title );
			
			final LRIcon ricon = MapImageCache.getMapImage( repProc );
			mapImageLabel.setIcon( ricon == null ? Icons.F_HOURGLASS.size( 64 ) : ricon.get() );
		}
	}
	
}
