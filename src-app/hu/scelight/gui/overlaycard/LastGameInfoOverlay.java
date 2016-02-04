/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight.gui.overlaycard;

import hu.scelight.action.Actions;
import hu.scelight.gui.icon.Icons;
import hu.scelight.gui.page.repanalyzer.UserColoredTableRenderer;
import hu.scelight.sc2.rep.factory.RepParserEngine;
import hu.scelight.sc2.rep.repproc.RepProcessor;
import hu.scelight.sc2.rep.repproc.User;
import hu.scelight.service.env.Env;
import hu.scelight.service.settings.Settings;
import hu.scelight.util.Utils;
import hu.scelightapi.gui.overlaycard.OverlayCardParams;
import hu.sllauncher.gui.comp.BorderPanel;
import hu.sllauncher.gui.comp.XButton;
import hu.sllauncher.gui.comp.XLabel;
import hu.sllauncher.gui.comp.table.XTable;
import hu.sllauncher.util.gui.RenderablePair;
import hu.sllauncher.util.gui.adapter.ActionAdapter;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.nio.file.Path;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicReference;

import javax.swing.Box;
import javax.swing.JPanel;
import javax.swing.Timer;


/**
 * Last game info overlay card.
 * 
 * @author Andras Belicza
 */
@SuppressWarnings( "serial" )
public class LastGameInfoOverlay extends OverlayCard {
	
	/** Instance reference. */
	private static final AtomicReference< LastGameInfoOverlay > INSTANCE = new AtomicReference<>();
	
	/**
	 * Returns the instance reference.
	 * 
	 * @return the instance reference
	 */
	public static LastGameInfoOverlay INSTANCE() {
		return INSTANCE.get();
	}
	
	
	/** Overlay card parameters. */
	private static final OverlayCardParams PARAMS       = new OverlayCardParams();
	static {
		PARAMS.settings = Env.APP_SETTINGS;
		PARAMS.centerXSetting = Settings.LGI_OVERLAY_CENTER_X;
		PARAMS.centerYSetting = Settings.LGI_OVERLAY_CENTER_Y;
		PARAMS.opacitySetting = Settings.LGI_OVERLAY_OPACITY;
		PARAMS.lockedSetting = Settings.LGI_OVERLAY_LOCKED;
		PARAMS.focusableSetting = Settings.LGI_OVERLAY_FOCUSABLE;
		PARAMS.allowOutMainScrSetting = Settings.LGI_OVERLAY_ALLOW_OUT_MAIN_SCR;
	}
	
	/** Last replay file whose info to show. */
	private final Path                     replayFile;
	
	/** Timer to close the overlay. */
	private final Timer                    timer;
	
	/** (Auto) closer button. */
	private final XButton                  closeButton  = new XButton( Icons.F_CROSS.get() );
	
	/** Time remaining to auto-close the overlay. */
	private int                            autoCloseSec = 13;
	
	
	/**
	 * Creates a new {@link LastGameInfoOverlay}.
	 * 
	 * @param replayFile last replay file whose info to show
	 */
	public LastGameInfoOverlay( final Path replayFile ) {
		super( PARAMS );
		
		INSTANCE.set( this );
		
		this.replayFile = replayFile;
		
		titleLabel.setIcon( Icons.F_INFORMATION_BALLOON.get() );
		titleLabel.setText( "Last Game Info" );
		titleLabel.allBorder( 3 );
		
		buildGui();
		
		packAndPosition();
		
		setVisible( true );
		
		timer = new Timer( 1000, new ActionAdapter( true ) {
			@Override
			public void actionPerformed( final ActionEvent event ) {
				// We're in the EDT (due to swing Timer)
				if ( autoCloseSec == 0 ) {
					close();
					return;
				}
				closeButton.setText( "_Closing in " + autoCloseSec-- + " sec" );
			}
		} );
		timer.start();
	}
	
	/**
	 * Builds the GUI of the tab.
	 */
	private void buildGui() {
		// Stop the count down if clicked
		final MouseListener stopperMouseListener = new MouseAdapter() {
			@Override
			public void mousePressed( final MouseEvent event ) {
				if ( !timer.isRunning() )
					return;
				timer.stop();
				closeButton.setText( "_Close" );
			}
		};
		
		final RepProcessor repProc = RepParserEngine.getRepProc( replayFile );
		
		if ( repProc == null ) {
			final XLabel label = new XLabel( "Failed to parse replay!" ).allBorder( 15 ).boldFont().italicFont().color( Color.RED );
			cp.addCenter( label );
			label.setCursor( Cursor.getDefaultCursor() );
			label.addMouseListener( stopperMouseListener );
		} else {
			autoCloseSec += repProc.playerUsers.length;
			
			final XTable table = new XTable();
			
			final Vector< Object > columns = Utils.vector( "User Color", "T", "Player", "", "", "", "APM", "SPM", "SQ", "SC%", "Lvl", "Dir" );
			
			final int userColorColIdx = 0;
			table.setTableCellRenderer( new UserColoredTableRenderer( table, userColorColIdx ) );
			
			final Vector< Vector< Object > > data = new Vector<>();
			for ( final User u : repProc.playerUsers ) {
				final Vector< Object > row = new Vector<>( columns.size() );
				
				row.add( u.getPlayerColor().darkerColor );
				row.add( u.slot.teamId + 1 );
				row.add( new RenderablePair<>( u.getPlayerColor().icon, u.fullName ) );
				row.add( u.player.race.ricon.get() );
				row.add( u.player.getResult().ricon.get() );
				row.add( u.uid == null ? null : u.uid.getHighestLeague().ricon.get() );
				row.add( u.apm );
				row.add( u.spm );
				row.add( u.sq );
				row.add( Env.LANG.formatNumber( u.supplyCappedPercent, 2 ) + '%' );
				row.add( u.uid == null ? null : u.uid.getCombinedRaceLevels() );
				row.add( u.startDirection );
				
				data.add( row );
			}
			table.getXTableModel().setDataVector( data, columns );
			table.pack();
			table.setSortable( false );
			table.removeColumn( table.getColumnModel().getColumn( userColorColIdx ) ); // Hide color column
			table.getTableHeader().addMouseListener( stopperMouseListener );
			table.addMouseListener( stopperMouseListener );
			
			final Box box = Box.createVerticalBox();
			final XLabel infoLabel = new XLabel( "Length: " + repProc.formatLoopTime( repProc.replay.header.getElapsedGameLoops() ) );
			infoLabel.addMouseListener( stopperMouseListener );
			box.add( new BorderPanel( infoLabel ) );
			box.setCursor( Cursor.getDefaultCursor() );
			box.add( table.getTableHeader() );
			box.add( table );
			cp.addCenter( box );
		}
		
		final JPanel buttonsPanel = new JPanel( new FlowLayout( FlowLayout.RIGHT, 0, 0 ) );
		if ( repProc != null ) {
			final XButton openButton = new XButton( "_Open", Icons.F_CHART.get() );
			openButton.setCursor( Cursor.getDefaultCursor() );
			openButton.addActionListener( new ActionAdapter() {
				@Override
				public void actionPerformed( final ActionEvent event ) {
					Actions.SHOW_MAIN_FRAME.actionPerformed( event );
					Env.MAIN_FRAME.repAnalyzersPage.newRepAnalyzerPage( replayFile, true );
					close();
				}
			} );
			buttonsPanel.add( openButton );
		}
		closeButton.setCursor( Cursor.getDefaultCursor() );
		closeButton.addActionListener( new ActionAdapter() {
			@Override
			public void actionPerformed( final ActionEvent event ) {
				close();
			}
		} );
		buttonsPanel.add( closeButton );
		cp.addSouth( buttonsPanel );
		
		addMouseListener( stopperMouseListener );
		configButton.addMouseListener( stopperMouseListener );
	}
	
	@Override
	protected void customOnClose() {
		timer.stop();
		INSTANCE.set( null );
	}
	
}
