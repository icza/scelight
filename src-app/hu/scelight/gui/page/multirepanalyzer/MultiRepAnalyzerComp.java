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

import hu.scelight.bean.repfilters.RepFiltersBean;
import hu.scelight.bean.repfolders.RepFolderBean;
import hu.scelight.gui.comp.XTabbedPane;
import hu.scelight.gui.dialog.RepFiltersEditorDialog;
import hu.scelight.gui.help.Helps;
import hu.scelight.gui.icon.Icons;
import hu.scelight.gui.page.multirepanalyzer.model.Game;
import hu.scelight.gui.page.multirepanalyzer.model.PlayerStats;
import hu.scelight.gui.page.multirepanalyzer.playerdetails.PlayerDetailsComp;
import hu.scelight.gui.setting.SettingsGui;
import hu.scelight.sc2.rep.factory.RepParserEngine;
import hu.scelight.sc2.rep.model.details.Toon;
import hu.scelight.sc2.rep.repproc.RepProcessor;
import hu.scelight.search.RepSearchEngine;
import hu.scelight.service.env.Env;
import hu.scelight.service.settings.Settings;
import hu.scelight.util.Utils;
import hu.scelight.util.gui.GuiUtils;
import hu.scelight.util.sc2rep.RepCounterJob;
import hu.scelight.util.sc2rep.RepCrawlerJob;
import hu.scelightapi.bean.repfilters.IRepFiltersBean;
import hu.scelightapibase.gui.comp.multipage.IPageClosingListener;
import hu.scelightapibase.util.iface.Producer;
import hu.sllauncher.action.XAction;
import hu.sllauncher.gui.comp.BorderPanel;
import hu.sllauncher.gui.comp.HelpIcon;
import hu.sllauncher.gui.comp.Link;
import hu.sllauncher.gui.comp.XButton;
import hu.sllauncher.gui.comp.XCheckBox;
import hu.sllauncher.gui.comp.XProgressBar;
import hu.sllauncher.gui.comp.XToolBar;
import hu.sllauncher.service.job.Job;
import hu.sllauncher.util.DurationFormat;
import hu.sllauncher.util.gui.adapter.ActionAdapter;

import java.awt.event.ActionEvent;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

import javax.swing.Box;
import javax.swing.JComponent;

/**
 * Component of the Multi-Replay Analyzer page.
 * 
 * @author Andras Belicza
 */
@SuppressWarnings( "serial" )
public class MultiRepAnalyzerComp extends BorderPanel implements IPageClosingListener {
	
	/** Edit filters. */
	private final XAction          editFiltersAction = new XAction( Icons.MY_CHART_UP_COLOR_MAGNIFIER,
	                                                         "View / Edit Filters and Repeat Multi-Replay Analyze...", this ) {
		                                                 @Override
		                                                 public void actionPerformed( final ActionEvent event ) {
			                                                 final RepFiltersEditorDialog filtersEditorDialog = new RepFiltersEditorDialog(
			                                                         repFiltersBean == null ? null : repFiltersBean.< RepFiltersBean > cloneBean() );
			                                                 if ( !filtersEditorDialog.isOk() )
				                                                 return;
			                                                 
			                                                 // Open a new MultiRepAnalyzerPage with the edited filters bean and select it:
			                                                 final MultiRepAnalyzerPage mrap = repFolderBean == null ? new MultiRepAnalyzerPage( repFiles,
			                                                         filtersEditorDialog.getRepFiltersBean() ) : new MultiRepAnalyzerPage( repFolderBean,
			                                                         filtersEditorDialog.getRepFiltersBean() );
			                                                 Env.MAIN_FRAME.getMultiRepAnalyzersPage().addChild( mrap );
			                                                 Env.MAIN_FRAME.rebuildMainPageTree();
			                                                 Env.MAIN_FRAME.multiPageComp.selectPage( mrap );
		                                                 }
	                                                 };
	
	/** Button to abort analysing. */
	private final XButton          abortButton       = new XButton( "_Abort", Icons.F_CROSS_OCTAGON.get() );
	
	/** Progress bar to display analysing progress and general info. */
	private final XProgressBar     progressBar       = new XProgressBar();
	
	/** Replay folder bean to analyze. */
	private final RepFolderBean    repFolderBean;
	
	/** Replay files to analyze. */
	private final Path[]           repFiles;
	
	/** Replay filters to be used. */
	private final IRepFiltersBean  repFiltersBean;
	
	/** Search engine if replays are to be filtered. */
	private final RepSearchEngine  searchEngine;
	
	/** Tabbed pane holding the statistics tabs. */
	private final XTabbedPane      tp                = new XTabbedPane();
	
	
	/** Cached value of the {@link Settings#USE_REAL_TIME} setting at the creation of the Multi-Replay Analyzer. */
	public final boolean           useRealTime       = Env.APP_SETTINGS.get( Settings.USE_REAL_TIME );
	
	/** Merged account map: the value is the virtual account, the key is the account that has to be merged into the virtual account. */
	public final Map< Toon, Toon > mergedAccountsMap = new HashMap<>();
	
	/**
	 * Creates a new {@link MultiRepAnalyzerComp}.
	 * 
	 * @param repFolderBean replay folder bean to analyze
	 * @param repFiltersBean replay filters to be used
	 */
	public MultiRepAnalyzerComp( final RepFolderBean repFolderBean, final IRepFiltersBean repFiltersBean ) {
		this( repFolderBean, null, repFiltersBean );
	}
	
	/**
	 * Creates a new {@link MultiRepAnalyzerComp}.
	 * 
	 * @param repFiles replay files to analyze
	 * @param repFiltersBean replay filters to be used
	 */
	public MultiRepAnalyzerComp( final Path[] repFiles, final IRepFiltersBean repFiltersBean ) {
		this( null, repFiles, repFiltersBean );
	}
	
	/**
	 * Creates a new {@link MultiRepAnalyzerComp}.
	 * 
	 * @param repFolderBean replay folder bean to analyze
	 * @param repFiles replay files to analyze
	 * @param repFiltersBean replay filters to be used
	 */
	private MultiRepAnalyzerComp( final RepFolderBean repFolderBean, final Path[] repFiles, final IRepFiltersBean repFiltersBean ) {
		this.repFolderBean = repFolderBean;
		this.repFiles = repFiles;
		this.repFiltersBean = repFiltersBean;
		this.searchEngine = repFiltersBean == null ? null : new RepSearchEngine( repFiltersBean );
		
		initMergedAccountsMap();
		
		buildGui();
		
		performAnalysis();
	}
	
	/**
	 * Initializes the merged accounts map: parses the value of the {@link Settings#MERGED_ACCOUNTS} setting.
	 */
	private void initMergedAccountsMap() {
		final StringTokenizer lines = new StringTokenizer( Env.APP_SETTINGS.get( Settings.MERGED_ACCOUNTS ), "\r\n" );
		
		while ( lines.hasMoreTokens() ) {
			final StringTokenizer st = new StringTokenizer( lines.nextToken(), "," );
			if ( !st.hasMoreTokens() )
				continue;
			
			Toon virtualToon = null;
			while ( st.hasMoreTokens() ) {
				String toon = null;
				try {
					toon = st.nextToken().trim();
					if ( !toon.isEmpty() ) {
						final Toon parsedToon = new Toon( toon, false );
						// No exception means valid toon
						if ( virtualToon == null )
							virtualToon = parsedToon;
						// Also add the virtual toon: map from virtualToon to virtualToon because the name might be different than the one found in the replay
						mergedAccountsMap.put( parsedToon, virtualToon );
					}
				} catch ( final IllegalArgumentException iae ) {
					// Invalid toon
					if ( virtualToon == null ) {
						// The virtual toon is invalid, skip the whole line
						Env.LOGGER.warning( "Invalid first toon in the Merged Accounts, skipping the whole line: " + toon, iae );
						break;
					} else {
						// Else just skip the invalid toon and process the rest
						Env.LOGGER.warning( "Invalid toon in the Merged Accounts: " + toon, iae );
					}
				}
			}
		}
	}
	
	/**
	 * Builds the GUI of the page.
	 */
	private void buildGui() {
		final Box northBox = Box.createVerticalBox();
		addNorth( northBox );
		
		final Box progressBox = Box.createHorizontalBox();
		progressBar.setStringPainted( true );
		progressBox.add( progressBar );
		abortButton.addActionListener( new ActionAdapter() {
			@Override
			public void actionPerformed( final ActionEvent event ) {
				if ( repCounterJob != null )
					repCounterJob.requestCancel();
				if ( repSearcherJob != null )
					repSearcherJob.requestCancel();
			}
		} );
		progressBox.add( abortButton );
		northBox.add( progressBox );
		
		final XToolBar toolBar = new XToolBar();
		northBox.add( toolBar );
		final XCheckBox autoOpenFirstPlayerCheckBox = SettingsGui.createBoundedSettingCheckBox( Settings.MULTI_REP_AUTO_OPEN_FIRST_PLAYER, Env.APP_SETTINGS,
		        null );
		autoOpenFirstPlayerCheckBox.setText( "Auto-open first player" );
		autoOpenFirstPlayerCheckBox.setToolTipText( Settings.MULTI_REP_AUTO_OPEN_FIRST_PLAYER.name );
		toolBar.add( autoOpenFirstPlayerCheckBox );
		final XCheckBox stretchToWindowCheckBox = SettingsGui.createBoundedSettingCheckBox( Settings.MULTI_REP_STRETCH_TABLES, Env.APP_SETTINGS, null );
		stretchToWindowCheckBox.setText( "Stretch to window" );
		stretchToWindowCheckBox.setToolTipText( Settings.MULTI_REP_STRETCH_TABLES.name );
		toolBar.add( stretchToWindowCheckBox );
		toolBar.addSeparator();
		toolBar.add( editFiltersAction );
		toolBar.addSeparator();
		toolBar.add( new HelpIcon( Helps.MULTI_REPLAY_ANALYZER ) );
		toolBar.addSeparator();
		toolBar.add( SettingsGui.createSettingLink( Settings.NODE_MULTI_REP_ANALYZER ) );
		toolBar.addSeparator();
		final Link playerAliasesSettingLink = SettingsGui.createSettingLink( Settings.NODE_MERGED_ACCOUNTS );
		toolBar.add( playerAliasesSettingLink );
		SettingsGui.bindVisibilityToSkillLevel( playerAliasesSettingLink, Settings.MERGED_ACCOUNTS.skillLevel );
		toolBar.finalizeLayout();
	}
	
	
	/** Cached time limit in loops to be included in analysis. */
	private final int     timeLimitLoop = Env.APP_SETTINGS.get( Settings.MULTI_REP_TIME_LIMIT ) * 16;
	
	/** Currently running rep counter job. */
	private RepCounterJob repCounterJob;
	
	/** Currently running rep searcher job. */
	private Job           repSearcherJob;
	
	/**
	 * Performs the analysis.
	 */
	private void performAnalysis() {
		if ( repFolderBean == null ) {
			progressBar.setMaximum( repFiles.length );
			analyzeReplays();
			return;
		}
		
		progressBar.setIndeterminate( true );
		progressBar.setString( "Counting replays..." );
		
		repCounterJob = new RepCounterJob( repFolderBean );
		repCounterJob.setEdtCallback( new Runnable() {
			@Override
			public void run() {
				progressBar.setIndeterminate( false );
				
				if ( repCounterJob.isCancelRequested() ) {
					progressBar.setAborted();
					return;
				}
				
				final Integer count = repCounterJob.getCount();
				if ( count == null ) {
					progressBar.setString( "ERROR! Could not count replays in folder: " + repFolderBean.getPath() );
					abortButton.setVisible( false );
					return;
				}
				if ( count < 0 ) {
					progressBar.setString( "ERROR! Replay folder does not exist: " + repFolderBean.getPath() );
					abortButton.setVisible( false );
					return;
				}
				progressBar.setMaximum( count );
				repCounterJob = null;
				analyzeReplays();
			}
		} );
		repCounterJob.start();
	}
	
	/** Game list. */
	private final List< Game > gameList = new ArrayList<>();
	
	/**
	 * Crawls the replay folder and analyzes replays it founds.
	 */
	private void analyzeReplays() {
		final long total = progressBar.getMaximum();
		
		final String analyzeJobName = "Multi-Replay Analyzer: " + getDisplayName();
		
		repSearcherJob = new RepCrawlerJob( analyzeJobName, Icons.F_BLUE_FOLDER_OPEN_TABLE, repFolderBean, repFiles, "analyze" ) {
			final boolean         showTime       = Env.APP_SETTINGS.get( Settings.REP_LIST_SHOW_TIME_DURING_SEARCH );
			
			final String          totalString    = ";   Total: " + Env.LANG.formatNumber( total ) + "   (";
			
			final double          totalHundredth = total / 100.0;
			
			final AtomicInteger   analyzed       = new AtomicInteger();
			
			final AtomicInteger   filtered       = new AtomicInteger();
			
			final AtomicInteger   errors         = new AtomicInteger();
			
			final ExecutorService es             = Utils.createExecutorService( getName() );
			
			
			@Override
			public void jobRun() {
				maximumProgress.set( (int) total );
				publishStatus( false ); // Publish now (required anyway if total = 0)
				
				super.jobRun();
				
				GuiUtils.runInEDT( new Runnable() {
					@Override
					public void run() {
						publishStatus( true );
						if ( cancelRequested )
							progressBar.setAborted();
						repSearcherJob = null;
						abortButton.setVisible( false );
					}
				} );
			}
			
			@Override
			protected void onEnd() {
				Utils.shutdownExecutorService( es );
				GuiUtils.runInEDT( new Runnable() {
					@Override
					public void run() {
						buildReport();
					}
				} );
			}
			
			/** Number of reps that have been enqueued for processing. It is only accessed from the job's executing thread. */
			int enqueued;
			
			@Override
			protected void onFoundRepFile( final Path repFile ) {
				// So situation is the following:
				// We work with multiple threads, but mayContinue() and waitIfPaused() can only be called from the job's thread.
				// But we also want to respect the paused state. So we have to call/check that here in the job's thread,
				// and in the workers thread we have to use guestMayContinue().
				// Also since enqueuing reps for processing is done really fast, we have to maximize the waiting reps,
				// else we lose control over paused state (because once all reps are queued, we will not be called anymore).
				while ( mayContinue() && enqueued - currentProgress.get() > 16 && checkedSleep( 1 ) == null )
					;
				if ( cancelRequested )
					return;
				enqueued++;
				
				// Schedule rep for processing.
				es.execute( new Runnable() {
					@Override
					public void run() {
						// We are a "guest" thread, not the job's executing thread:
						if ( !guestMayContinue() )
							return;
						
						final RepProcessor repProc = RepParserEngine.getRepProc( repFile );
						
						if ( repProc == null )
							errors.incrementAndGet();
						else {
							// Check if filters match
							if ( repProc.replay.header.getElapsedGameLoops() < timeLimitLoop || searchEngine != null && !searchEngine.isIncluded( repProc ) )
								filtered.incrementAndGet();
							else {
								final Game game = new Game( repProc, MultiRepAnalyzerComp.this );
								synchronized ( gameList ) {
									gameList.add( game );
								}
								analyzed.incrementAndGet();
							}
						}
						currentProgress.set( analyzed.get() + filtered.get() + errors.get() );
						publishStatus( false );
					}
				} );
			}
			
			private void publishStatus( final boolean ended ) {
				GuiUtils.runInEDT( new Runnable() {
					@Override
					public void run() {
						final int processed = currentProgress.get();
						
						final StringBuilder sb = new StringBuilder( 80 );
						sb.append( "Analyzed: " ).append( Env.LANG.formatNumber( analyzed.get() ) ).append( ";   Filtered out: " )
						        .append( Env.LANG.formatNumber( filtered.get() ) ).append( ";   Errors: " ).append( Env.LANG.formatNumber( errors.get() ) )
						        .append( totalString ).append( Env.LANG.formatNumber( total == 0 ? 0 : processed / totalHundredth, 2 ) ).append( "%)" );
						
						if ( showTime && !ended ) {
							final long time = getExecTimeMs();
							sb.append( "   [Time: " ).append( DurationFormat.AUTO.formatDuration( time ) ).append( ";  ETA: " )
							        .append( processed == 0 ? "- " : DurationFormat.AUTO.formatDuration( time * ( total - processed ) / processed ) )
							        .append( ']' );
						}
						
						progressBar.setString( sb.toString() );
						
						progressBar.setValue( processed );
					}
				} );
			}
		};
		repSearcherJob.start();
	}
	
	/**
	 * Returns a display name based on the input replays. It is used when replays of stats rows are opened and in the name of jobs.
	 * 
	 * @return a display name based on the input replays
	 */
	private String getDisplayName() {
		return repFolderBean == null ? Utils.plural( "%s replay%s", repFiles.length ) : repFolderBean.getPath().toString();
	}
	
	/**
	 * Builds report from the analysis.
	 */
	private void buildReport() {
		final String displayName = "MRA - " + getDisplayName() + " \u00d7 Global Stats \u00d7 ";
		
		final XTabbedPane gstp = new XTabbedPane();
		gstp.addTab( "Player", Icons.F_USERS, new PlayerComp( displayName + "Player", this, gameList ), true );
		gstp.addTab( "Calendar", Icons.F_CALENDAR_BLUE, new Producer< JComponent >() {
			@Override
			public JComponent produce() {
				return new CalendarComp( displayName + "Calendar", MultiRepAnalyzerComp.this, gameList );
			}
		}, true );
		gstp.addTab( "Map", Icons.F_MAPS_STACK, new Producer< JComponent >() {
			@Override
			public JComponent produce() {
				return new MapComp( displayName + "Map", gameList );
			}
		}, true );
		
		tp.addTab( "Global Stats", Icons.F_SUM, gstp, false );
		
		addCenter( tp );
	}
	
	/**
	 * Adds a new player details tab.
	 * 
	 * @param playerStats player stats to add a details tab for
	 * @param playerStatsMap reference to the map of all player stats; required when the details of a playmate is to be opened
	 */
	public void addPlayerDetailsTab( final PlayerStats playerStats, final Map< Toon, PlayerStats > playerStatsMap ) {
		tp.addTab( playerStats.getName(), Icons.F_USER, new PlayerDetailsComp( "MRA - " + getDisplayName() + " \u00d7 " + playerStats.getName(), this,
		        playerStats, playerStatsMap ), true, true, null );
		tp.wrappedTabbedPane.setSelectedIndex( tp.getTabCount() - 1 );
	}
	
	@Override
	public boolean pageClosing() {
		// Take care of running jobs...
		abortButton.doClick( 0 );
		
		return true;
	}
	
}
