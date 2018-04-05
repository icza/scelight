/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight.gui;

import hu.scelight.Consts;
import hu.scelight.action.Actions;
import hu.scelight.gui.comp.MemoryStatus;
import hu.scelight.gui.comp.XMenu;
import hu.scelight.gui.comp.XMenuItem;
import hu.scelight.gui.comp.XPopupMenu;
import hu.scelight.gui.help.Helps;
import hu.scelight.gui.icon.Icons;
import hu.scelight.gui.page.MultiRepAnalyzersPage;
import hu.scelight.gui.page.RepAnalyzersPage;
import hu.scelight.gui.page.repfolders.RepFoldersPage;
import hu.scelight.gui.setting.SettingsGui;
import hu.scelight.sc2.rep.cache.RepProcCache;
import hu.scelight.service.env.Env;
import hu.scelight.service.sc2reg.Sc2RegMonitor;
import hu.scelight.service.settings.Settings;
import hu.scelight.service.sound.Sounds;
import hu.scelight.util.RHtml;
import hu.scelight.util.Utils;
import hu.scelight.util.gui.GuiUtils;
import hu.scelight.util.sc2rep.RepUtils;
import hu.scelightapi.gui.IMainFrame;
import hu.scelightapi.service.sc2monitor.GameChangeAdapter;
import hu.scelightapi.service.sc2monitor.IGameChangeEvent;
import hu.scelightapi.service.sc2monitor.IGameStatus;
import hu.scelightapibase.bean.settings.ISettingChangeEvent;
import hu.scelightapibase.bean.settings.ISettingChangeListener;
import hu.scelightapibase.gui.comp.multipage.IPage;
import hu.scelightapibase.util.iface.Consumer;
import hu.sllauncher.ScelightLauncher;
import hu.sllauncher.action.XAction;
import hu.sllauncher.bean.settings.type.Setting;
import hu.sllauncher.gui.comp.BorderPanel;
import hu.sllauncher.gui.comp.HelpIcon;
import hu.sllauncher.gui.comp.Link;
import hu.sllauncher.gui.comp.XButton;
import hu.sllauncher.gui.comp.XFrame;
import hu.sllauncher.gui.comp.XLabel;
import hu.sllauncher.gui.comp.XToolBar;
import hu.sllauncher.gui.comp.multipage.MultiPageComp;
import hu.sllauncher.gui.help.LHelps;
import hu.sllauncher.service.settings.LSettings;
import hu.sllauncher.service.sound.Sound;
import hu.sllauncher.util.RelativeDate;
import hu.sllauncher.util.SkillLevel;
import hu.sllauncher.util.gui.adapter.ActionAdapter;
import hu.sllauncher.util.gui.adapter.PropertyChangeAdapter;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JMenuBar;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

/**
 * The main GUI frame of the application.
 * 
 * @author Andras Belicza
 */
@SuppressWarnings( "serial" )
public class MainFrame extends XFrame implements IMainFrame {
	
	/** Color indicating activity / being active. */
	private static final Color           COLOR_ACTIVE          = Color.MAGENTA;
	
	
	/** Custom content panel to be used, so the "real" content panel remains available for relocating the tool bar. */
	private final BorderPanel            ccp                   = new BorderPanel();
	
	/** Reference to the train icon. */
	private TrayIcon                     trayIcon;
	
	/** Multi page component: the main content. */
	public final MultiPageComp           multiPageComp         = new MultiPageComp( new ArrayList< IPage< ? > >( 0 ), null, getLayeredPane() );
	
	/** Replay folders node page. */
	private final RepFoldersPage         repFoldersPage        = new RepFoldersPage();
	
	/** Replay analyzers node page. */
	public final RepAnalyzersPage        repAnalyzersPage      = new RepAnalyzersPage();
	
	/** Multi-Replay analyzers node page. */
	public final MultiRepAnalyzersPage   multiRepAnalyzersPage = new MultiRepAnalyzersPage();
	
	/** Setting change listener (to keep reference to it). */
	private final ISettingChangeListener scl;
	
	/** Button to show updates available info. */
	private final XButton                updatesAvailabeButton = new XButton( "Updates Available!", Icons.F_ARROW_CIRCLE_DOUBLE.get() );
	
	/**
	 * Creates a new {@link MainFrame}, and makes it visible.
	 */
	public MainFrame() {
		scl = new ISettingChangeListener() {
			@Override
			public void valuesChanged( final ISettingChangeEvent event ) {
				if ( event.affected( LSettings.PERSON_NAME_FORMAT ) ) {
					setTitle( Consts.APP_NAME_FULL );
				}
			}
		};
		Env.LAUNCHER_SETTINGS.addAndExecuteChangeListener( LSettings.PERSON_NAME_FORMAT, scl );
		
		setIconImage( Icons.MY_APP_ICON.get().getImage() );
		
		installTrayIcon();
		
		buildGui();
		
		// Window positioning
		if ( Env.APP_SETTINGS.get( Settings.RESTORE_LAST_WIN_POS_ON_START ) ) {
			setBounds( Env.APP_SETTINGS.get( Settings.STORED_WINDOW_POSITION_X ), Env.APP_SETTINGS.get( Settings.STORED_WINDOW_POSITION_Y ),
			        Env.APP_SETTINGS.get( Settings.STORED_WINDOW_WIDTH ), Env.APP_SETTINGS.get( Settings.STORED_WINDOW_HEIGHT ) );
		} else
			restoreDefaultWinPos();
		
		if ( Env.APP_SETTINGS.get( Settings.MAXIMIZE_WINDOW_ON_START ) )
			setExtendedState( MAXIMIZED_BOTH );
		
		// If parameters were passed, do not minimize to tray
		final String[] arguments = ScelightLauncher.getArguments();
		
		if ( arguments.length == 0 && Env.APP_SETTINGS.get( Settings.START_MINIMIZED_TO_TRAY ) && Actions.MINIMIZE_TO_TRAY.isEnabled() ) {
			if ( Env.APP_SETTINGS.get( Settings.SHOW_INFO_WHEN_STARTED_MINIMIZED ) )
				trayIcon.displayMessage( null, Consts.APP_NAME_FULL + " has been started minimized.", MessageType.INFO );
		} else
			setVisible( true );
		
		// Register component listener to store changed position
		addComponentListener( new ComponentAdapter() {
			@Override
			public void componentMoved( final ComponentEvent event ) {
				if ( ( getExtendedState() & MAXIMIZED_BOTH ) == MAXIMIZED_BOTH )
					return;
				storePosition();
			}
			
			@Override
			public void componentResized( final ComponentEvent event ) {
				if ( ( getExtendedState() & MAXIMIZED_BOTH ) == MAXIMIZED_BOTH )
					return;
				storeDimension();
			};
		} );
		
		// openArguments() already uses invokeLater(), but we embed it in another invokeLater()
		// to let Scelight initialize everything (main frame build, resize etc.)
		SwingUtilities.invokeLater( new Runnable() {
			@Override
			public void run() {
				openArguments( arguments );
			}
		} );
	}
	
	/**
	 * Stores the current window position in the settings.
	 */
	private void storePosition() {
		Env.APP_SETTINGS.set( Settings.STORED_WINDOW_POSITION_X, getX() );
		Env.APP_SETTINGS.set( Settings.STORED_WINDOW_POSITION_Y, getY() );
	}
	
	/**
	 * Stores the current window dimension in the settings.
	 */
	private void storeDimension() {
		Env.APP_SETTINGS.set( Settings.STORED_WINDOW_WIDTH, getWidth() );
		Env.APP_SETTINGS.set( Settings.STORED_WINDOW_HEIGHT, getHeight() );
	}
	
	/**
	 * Opens the application arguments.
	 * 
	 * @param arguments arguments to be opened
	 */
	public void openArguments( final String[] arguments ) {
		SwingUtilities.invokeLater( new Runnable() {
			@Override
			public void run() {
				for ( final String arg : arguments ) {
					if ( RepUtils.hasRepExt( arg ) )
						Env.MAIN_FRAME.repAnalyzersPage.newRepAnalyzerPage( Paths.get( arg ), true );
				}
			}
		} );
	}
	
	@Override
	public void close() {
		final XAction action = Env.APP_SETTINGS.get( Settings.MINIMIZE_TO_TRAY_ON_CLOSE ) ? Actions.MINIMIZE_TO_TRAY : Actions.EXIT;
		action.actionPerformed( null );
	}
	
	/**
	 * Installs a system tray icon.
	 */
	private void installTrayIcon() {
		if ( !SystemTray.isSupported() )
			return;
		
		final TrayIcon trayIcon = new TrayIcon( Icons.MY_APP_ICON.get().getImage(), Consts.APP_NAME_FULL + " is running." );
		trayIcon.setImageAutoSize( true );
		
		try {
			SystemTray.getSystemTray().add( trayIcon );
			
			this.trayIcon = trayIcon;
			
			trayIcon.addActionListener( Actions.SHOW_MAIN_FRAME );
			
			final PopupMenu popup = new PopupMenu();
			final MenuItem restoreMenuItem = new MenuItem( "Show Main Window" );
			restoreMenuItem.addActionListener( Actions.SHOW_MAIN_FRAME );
			popup.add( restoreMenuItem );
			final MenuItem hideMenuItem = new MenuItem( "Hide Main Window" );
			hideMenuItem.addActionListener( Actions.MINIMIZE_TO_TRAY );
			popup.add( hideMenuItem );
			popup.addSeparator();
			final MenuItem restoreDefPosMenuItem = new MenuItem( "Restore Main Window to defaults" );
			restoreDefPosMenuItem.addActionListener( new ActionAdapter() {
				@Override
				public void actionPerformed( final ActionEvent e ) {
					// First ensure it's visible and active:
					Actions.SHOW_MAIN_FRAME.actionPerformed( null );
					// And the default position:
					Actions.RESTORE_DEF_WIN_POSITION.actionPerformed( null );
				}
			} );
			popup.add( restoreDefPosMenuItem );
			popup.addSeparator();
			final MenuItem exitMenuItem = new MenuItem( "Exit" );
			exitMenuItem.addActionListener( Actions.EXIT );
			popup.add( exitMenuItem );
			trayIcon.setPopupMenu( popup );
			
			Actions.MINIMIZE_TO_TRAY.setEnabled( true );
		} catch ( final AWTException ae ) {
			Env.LOGGER.debug( "Failed to install tray icon!", ae );
		}
	}
	
	@Override
	public void restoreDefaultWinPos() {
		// First un-maximize:
		setExtendedState( NORMAL );
		
		GuiUtils.maximizeWindowWithMargin( this, 20, new Dimension( 1200, 750 ) );
		storePosition();
		storeDimension();
		
		// And maximize it:
		setExtendedState( MAXIMIZED_BOTH );
	}
	
	@Override
	public void restoreMainFrame() {
		if ( !isVisible() )
			setVisible( true );
		
		if ( getExtendedState() == ICONIFIED )
			setExtendedState( NORMAL );
		
		setState( NORMAL ); // Restores minimized window
		
		toFront();
	}
	
	/**
	 * Builds the GUI of the main frame.
	 */
	private void buildGui() {
		cp.addCenter( ccp );
		
		buildMenuBar();
		
		buildToolBar();
		
		buildMultiPage();
		
		buildStatusBar();
	}
	
	/**
	 * Builds the menu bar of the main frame.
	 */
	private void buildMenuBar() {
		final JMenuBar mb = new JMenuBar();
		
		final XMenu fileMenu = new XMenu( "_File", Icons.F_DISK.get() );
		Actions.OPEN_REPLAY.addToMenu( fileMenu );
		Actions.QUICK_OPEN_LAST_REP.addToMenu( fileMenu );
		fileMenu.addSeparator();
		Actions.SETTINGS.addToMenu( fileMenu );
		fileMenu.addSeparator();
		Actions.START_SC2.addToMenu( fileMenu );
		Actions.START_SC2_EDITOR.addToMenu( fileMenu );
		fileMenu.addSeparator();
		final XMenu recentReplaysMenu = new XMenu( "_Recent Replays", Icons.MY_CHARTS.get() );
		fileMenu.add( recentReplaysMenu );
		fileMenu.addSeparator();
		Actions.EXIT.addToMenu( fileMenu );
		mb.add( fileMenu );
		
		final XMenu viewMenu = new XMenu( "_View", Icons.F_PICTURE_SUNSET.get() );
		Actions.SHOW_MAIN_TOOL_BAR.addToMenu( viewMenu );
		Actions.SHOW_STATUS_BAR.addToMenu( viewMenu );
		mb.add( viewMenu );
		
		final XMenu toolsMenu = new XMenu( "_Tools", Icons.F_TOOLBOX.get() );
		Actions.LIVE_APM_OVERLAY.addToMenu( toolsMenu );
		Actions.LAST_GAME_INFO_OVERLAY.addToMenu( toolsMenu );
		toolsMenu.addSeparator();
		toolsMenu.addSeparator();
		Actions.RUNNING_JOBS.addToMenu( toolsMenu );
		mb.add( toolsMenu );
		
		final XMenu windowMenu = new XMenu( "_Window", Icons.F_APPLICATION_BLUE.get() );
		Actions.MINIMIZE_TO_TRAY.addToMenu( windowMenu );
		Actions.RESTORE_DEF_WIN_POSITION.addToMenu( windowMenu );
		windowMenu.addSeparator();
		Actions.MINIMIZE_TO_TRAY_ON_CLOSE.addToMenu( windowMenu );
		Actions.START_MINIMIZED_TO_TRAY.addToMenu( windowMenu );
		Actions.MAXIMIZE_WINDOW_ON_START.addToMenu( windowMenu );
		Actions.RESTORE_LAST_WIN_POS_ON_START.addToMenu( windowMenu );
		mb.add( windowMenu );
		
		final XMenu helpMenu = new XMenu( "_Help", Icons.F_QUESTION.get() );
		Actions.HOME_PAGE.addToMenu( helpMenu );
		helpMenu.addSeparator();
		Actions.ABOUT_NEWS.addToMenu( helpMenu );
		Actions.ABOUT_LICENSE.addToMenu( helpMenu );
		Actions.ABOUT_LOGS.addToMenu( helpMenu );
		Actions.ABOUT_SYS_INFO.addToMenu( helpMenu );
		helpMenu.addSeparator();
		Actions.CHECK_FOR_UPDATES.addToMenu( helpMenu );
		helpMenu.addSeparator();
		Actions.ABOUT_ABOUT.addToMenu( helpMenu );
		mb.add( helpMenu );
		
		setJMenuBar( mb );
		
		// Listened settings
		final ISettingChangeListener scl = new ISettingChangeListener() {
			@Override
			public void valuesChanged( final ISettingChangeEvent event ) {
				if ( event.affected( Settings.RECENT_REPLAYS_BEAN ) ) {
					// Rebuild recent replays menu content
					recentReplaysMenu.removeAll();
					createAndAddReplayItems( recentReplaysMenu );
				}
			}
		};
		final Set< Setting< ? > > listendSettingSet = Utils.< Setting< ? > > asNewSet( Settings.RECENT_REPLAYS_BEAN );
		SettingsGui.addBindExecuteScl( scl, Env.APP_SETTINGS, listendSettingSet, mb );
	}
	
	/**
	 * Creates and adds a menu item for each of the recent replays to the specified menu.
	 * 
	 * @param menu menu to add the menu items to; either an {@link XMenu} or an {@link XPopupMenu}
	 * @throws IllegalArgumentException if the menu is not an {@link XMenu} nor an {@link XPopupMenu}
	 */
	public void createAndAddReplayItems( final JComponent menu ) throws IllegalArgumentException {
		int i = 0;
		for ( final Path path : Env.APP_SETTINGS.get( Settings.RECENT_REPLAYS_BEAN ).getReplayList() ) {
			final String text = ++i < 10 ? "_" + i + "   " + path : i + "   " + path;
			
			final XMenuItem mi = new XMenuItem( text, Icons.F_CHART.get() );
			
			mi.addActionListener( new ActionAdapter() {
				@Override
				public void actionPerformed( final ActionEvent event ) {
					repAnalyzersPage.newRepAnalyzerPage( path, true );
				}
			} );
			
			menu.add( mi );
		}
		
		if ( i == 0 )
			menu.add( new XLabel( "<html><span style='font-style:italic;color:#555555'>No recent replays.</span></html>" ).horizontalBorder( 10 ) );
		else {
			if ( menu instanceof XMenu ) {
				( (XMenu) menu ).addSeparator();
				Actions.CLEAR_RECENT_REPS.addToMenu( (XMenu) menu );
			} else if ( menu instanceof XPopupMenu ) {
				( (XPopupMenu) menu ).addSeparator();
				Actions.CLEAR_RECENT_REPS.addToMenu( (XPopupMenu) menu );
			} else
				throw new IllegalArgumentException( "Unsupported menu type: " + menu.getClass().getName() );
		}
	}
	
	/**
	 * Builds the menu bar of the main frame.
	 */
	private void buildToolBar() {
		final XToolBar toolBar = new XToolBar();
		cp.addNorth( toolBar );
		
		toolBar.add( Actions.SETTINGS );
		
		toolBar.addSeparator();
		toolBar.add( Actions.OPEN_REPLAY );
		toolBar.add( Actions.QUICK_OPEN_LAST_REP );
		toolBar.add( new HelpIcon( Helps.QUICK_OPEN_LAST_REPLAY ) );
		
		toolBar.addSeparator();
		toolBar.add( Actions.START_SC2 );
		
		toolBar.addSeparator();
		toolBar.add( Actions.HOME_PAGE );
		toolBar.add( Actions.ABOUT_ABOUT );
		
		toolBar.addSeparator();
		toolBar.add( new XLabel( LSettings.SKILL_LEVEL.name + ":" ) );
		toolBar.add( SettingsGui.createSkillLevelComboBox( null ) );
		toolBar.add( new HelpIcon( LHelps.COMP_SKILL_LEVEL ) );
		
		toolBar.addSeparator();
		toolBar.add( new XLabel( LHelps.HELP_LEGEND.title ) );
		toolBar.add( new HelpIcon( LHelps.HELP_LEGEND ).leftBorder( 2 ) );
		
		toolBar.finalizeLayout();
		
		// Bind floating and displaying to setting
		final ISettingChangeListener scl = new ISettingChangeListener() {
			@Override
			public void valuesChanged( final ISettingChangeEvent event ) {
				// BUG: If a floating tool bar (detached) is changed not to be floatable and is closed after that,
				// it disappears (doesn't get docked back to the main frame).
				// To prevent this, we always dock back the tool bar if it is made hidden or not floatable.
				if ( event.affectedAny( Settings.MAIN_TOOL_BAR_FLOTABLE, Settings.SHOW_MAIN_TOOL_BAR ) ) {
					if ( !event.get( Settings.MAIN_TOOL_BAR_FLOTABLE ) || !event.get( Settings.SHOW_MAIN_TOOL_BAR ) ) {
						final Window parentWindow = SwingUtilities.getWindowAncestor( toolBar );
						if ( parentWindow != null && !MainFrame.this.equals( parentWindow ) ) { // Is it floating now?
							parentWindow.dispose();
							// Reset orientation because we attach it to north
							toolBar.setOrientation( SwingConstants.HORIZONTAL );
							cp.addNorth( toolBar );
						}
					}
				}
				if ( event.affected( Settings.MAIN_TOOL_BAR_FLOTABLE ) )
					toolBar.setFloatable( event.get( Settings.MAIN_TOOL_BAR_FLOTABLE ) );
				if ( event.affected( Settings.SHOW_MAIN_TOOL_BAR ) )
					toolBar.setVisible( event.get( Settings.SHOW_MAIN_TOOL_BAR ) );
			}
		};
		final Set< Setting< ? > > listendSettingSet = Utils.< Setting< ? > > asNewSet( Settings.MAIN_TOOL_BAR_FLOTABLE, Settings.SHOW_MAIN_TOOL_BAR );
		SettingsGui.addBindExecuteScl( scl, Env.APP_SETTINGS, listendSettingSet, toolBar );
	}
	
	/**
	 * Builds the Main multi-page component.
	 */
	private void buildMultiPage() {
		final List< IPage< ? > > pageList = new ArrayList<>();
		pageList.add( repFoldersPage );
		pageList.add( repAnalyzersPage );
		pageList.add( multiRepAnalyzersPage );
		multiPageComp.setPageList( pageList );
		
		ccp.addCenter( multiPageComp );
		
		final ISettingChangeListener scl = new ISettingChangeListener() {
			@Override
			public void valuesChanged( final ISettingChangeEvent event ) {
				if ( event.affected( Settings.MAIN_NAV_TREE_INITIAL_WIDTH ) ) {
					multiPageComp.setDividerLocation( event.get( Settings.MAIN_NAV_TREE_INITIAL_WIDTH ) );
					multiPageComp.resizeNavTree();
				}
				
				if ( event.affected( Settings.MAIN_NAV_TREE_AUTO_RESIZE ) ) {
					multiPageComp.setNavTreeAutoResize( event.get( Settings.MAIN_NAV_TREE_AUTO_RESIZE ) );
					if ( !event.get( Settings.MAIN_NAV_TREE_AUTO_RESIZE ) )
						multiPageComp.setDividerLocation( Env.APP_SETTINGS.get( Settings.MAIN_NAV_TREE_INITIAL_WIDTH ) );
					multiPageComp.resizeNavTree();
				}
				
				if ( event.affected( Settings.MAIN_NAV_TREE_MAX_AUTO_WIDTH ) ) {
					multiPageComp.setNavTreeMaxAutoWidth( event.get( Settings.MAIN_NAV_TREE_MAX_AUTO_WIDTH ) );
					multiPageComp.resizeNavTree();
				}
			}
		};
		final Set< Setting< ? > > listendSettingSet = Utils.< Setting< ? > > asNewSet( Settings.MAIN_NAV_TREE_INITIAL_WIDTH,
		        Settings.MAIN_NAV_TREE_AUTO_RESIZE, Settings.MAIN_NAV_TREE_MAX_AUTO_WIDTH );
		SettingsGui.addBindExecuteScl( scl, Env.APP_SETTINGS, listendSettingSet, multiPageComp );
		
		// JSplitPane.setDividerLocation() only works if the component is visible and has non-zero size, so set it later (too).
		SwingUtilities.invokeLater( new Runnable() {
			@Override
			public void run() {
				multiPageComp.setDividerLocation( Env.APP_SETTINGS.get( Settings.MAIN_NAV_TREE_INITIAL_WIDTH ) );
				multiPageComp.resizeNavTree();
			}
		} );
	}
	
	/**
	 * Builds the status bar of the main frame.
	 */
	private void buildStatusBar() {
		final Box statusBar = Box.createHorizontalBox();
		
		// Do not init jobsCountButton with action, we only need its actionPerformed code...
		// (no tool tip, no mnemonic, no big and auto-scaled icon needed...)
		final XButton jobsCountButton = new XButton();
		jobsCountButton.addActionListener( Actions.RUNNING_JOBS );
		Env.JOBS.addListener( new PropertyChangeAdapter( true ) {
			@Override
			public void propertyChange( final PropertyChangeEvent event ) {
				SwingUtilities.invokeLater( new Runnable() {
					@Override
					public void run() {
						final int jobsCount = Env.JOBS.getJobsCount();
						jobsCountButton.setText( "Running Jobs: " + Env.LANG.formatNumber( jobsCount ) );
						jobsCountButton.setIcon( jobsCount > 0 ? Icons.F_HARD_HAT_MILITARY.get() : Icons.F_HARD_HAT.get() );
						jobsCountButton.setForeground( jobsCount > 0 ? COLOR_ACTIVE : null );
					}
				} );
			}
		} );
		
		statusBar.add( jobsCountButton );
		
		statusBar.add( new JSeparator( SwingConstants.VERTICAL ) );
		final XButton repProcCacheButton = new XButton( Icons.F_COMPILE.get() );
		repProcCacheButton.addActionListener( new ActionAdapter() {
			@Override
			public void actionPerformed( final ActionEvent event ) {
				final Link emptyCacheLink = new Link( "Empty cache...", new Consumer< MouseEvent >() {
					@Override
					public void consume( final MouseEvent me ) {
						if ( GuiUtils.confirm( "Are you sure you want to empty the Replay Processor Cache?" ) )
							RepProcCache.clear();
					}
				} );
				emptyCacheLink.setIcon( Icons.F_CROSS.get() );
				GuiUtils.showInfoMsg(
				        new RHtml( "Replay Processor Cache", "html/statusbar/rep-processor-cache.html", "cachedCount", Env.LANG.formatNumber( RepProcCache
				                .size() ) ).get(), " ", SettingsGui.createSettingLink( Settings.NODE_REP_PROCESSOR ), " ", emptyCacheLink );
			}
		} );
		SettingsGui.bindVisibilityToSkillLevel( repProcCacheButton, SkillLevel.NORMAL );
		RepProcCache.addListener( new PropertyChangeAdapter( true ) {
			/** Timer used to clear the ACTIVE status. */
			Timer   timer;
			
			/** Are we called first. */
			boolean first = true;
			
			@Override
			public void propertyChange( final PropertyChangeEvent event ) {
				SwingUtilities.invokeLater( new Runnable() {
					@Override
					public void run() {
						repProcCacheButton.setText( "RepProc Cache: " + Env.LANG.formatNumber( RepProcCache.size() ) );
						if ( first ) { // Cannot use duringInit property due to invokeLater()!
							first = false;
							return;
						}
						if ( timer == null ) {
							repProcCacheButton.setForeground( COLOR_ACTIVE );
							// Stay active for 2.5 seconds
							timer = new Timer( 2500, new ActionAdapter() {
								@Override
								public void actionPerformed( final ActionEvent event ) {
									repProcCacheButton.setForeground( null );
									timer = null;
								}
							} );
							timer.setRepeats( false );
							timer.start();
						} else
							timer.restart();
					}
				} );
			}
		} );
		statusBar.add( repProcCacheButton );
		
		statusBar.add( new JSeparator( SwingConstants.VERTICAL ) );
		final XButton mapDlsButton = new XButton( Icons.F_MAP.get() );
		mapDlsButton.addActionListener( new ActionAdapter() {
			@Override
			public void actionPerformed( final ActionEvent event ) {
				GuiUtils.showInfoMsg(
				        new RHtml( "Map Download Manager", "html/statusbar/map-download-manager.html", "activeCount", Env.LANG
				                .formatNumber( Env.MAP_DOWNLOAD_MANAGER.getActiveCount() ), "waitingCount", Env.LANG.formatNumber( Env.MAP_DOWNLOAD_MANAGER
				                .getQueuedCount() ) ).get(), " ", SettingsGui.createSettingLink( Settings.NODE_SC2_INSTALLATION ) );
			}
		} );
		SettingsGui.bindVisibilityToSkillLevel( mapDlsButton, SkillLevel.NORMAL );
		Env.MAP_DOWNLOAD_MANAGER.addListener( new PropertyChangeAdapter( true ) {
			@Override
			public void propertyChange( final PropertyChangeEvent event ) {
				SwingUtilities.invokeLater( new Runnable() {
					@Override
					public void run() {
						final boolean active = Env.MAP_DOWNLOAD_MANAGER.getActiveCount() > 0;
						mapDlsButton.setIcon( active ? Icons.F_DRIVE_DOWNLOAD.get() : Icons.F_MAP.get() );
						mapDlsButton.setForeground( active ? COLOR_ACTIVE : null );
						mapDlsButton.setText( "Map downloads: " + Env.LANG.formatNumber( Env.MAP_DOWNLOAD_MANAGER.getActiveCount() ) + " / "
						        + Env.LANG.formatNumber( Env.MAP_DOWNLOAD_MANAGER.getQueuedCount() ) );
					}
				} );
			}
		} );
		statusBar.add( mapDlsButton );
		
		if ( Sc2RegMonitor.isSupported() ) {
			statusBar.add( new JSeparator( SwingConstants.VERTICAL ) );
			final XButton gameStatusButton = new XButton();
			gameStatusButton.addActionListener( new ActionAdapter() {
				@Override
				public void actionPerformed( final ActionEvent event ) {
					final long gameStatusSince = Env.SC2_REG_MONITOR.getGameStatusSince();
					GuiUtils.showInfoMsg( new RHtml( "SC2 Game Monitor", "html/statusbar/sc2-game-monitor.html", "status", Sc2RegMonitor.getGameStatus().text,
					        "since", ( gameStatusSince == 0 ? "N/A" : new RelativeDate( gameStatusSince, true, 2, true ).toString() ) ).get(), " ", SettingsGui
					        .createSettingLink( Settings.NODE_SC2_GAME_MONITOR ) );
				}
			} );
			Env.SC2_REG_MONITOR.addGameChangeListener( new GameChangeAdapter() {
				{
					gameChanged( null ); // Init
				}
				
				@Override
				public void gameChanged( final IGameChangeEvent event ) {
					final IGameStatus status = event == null ? Sc2RegMonitor.getGameStatus() : event.getNewStatus();
					SwingUtilities.invokeLater( new Runnable() {
						@Override
						public void run() {
							gameStatusButton.setIcon( status.getRicon().get() );
							gameStatusButton.setText( "SC2: " + status );
						}
					} );
				}
			} );
			statusBar.add( gameStatusButton );
		}
		
		statusBar.add( new JSeparator( SwingConstants.VERTICAL ) );
		
		statusBar.add( new BorderPanel() ); // Consume remaining space
		
		if ( Env.ONLINE ) {
			GuiUtils.boldFont( updatesAvailabeButton );
			updatesAvailabeButton.setForeground( Color.MAGENTA );
			updatesAvailabeButton.addActionListener( new ActionAdapter() {
				boolean dialogVisible;
				
				@Override
				public void actionPerformed( final ActionEvent event ) {
					// If computer is left alone, do not stack popups, but still play the sound!
					Sound.play( Sounds.UPDATES_AVAILABLE );
					if ( !dialogVisible ) {
						dialogVisible = true;
						if ( GuiUtils.confirm( new RHtml( "Offline Mode", "html/statusbar/updates-available.html" ).get() ) )
							Actions.EXIT.actionPerformed( null );
						dialogVisible = false;
					}
				}
			} );
			updatesAvailabeButton.setVisible( false );
			statusBar.add( updatesAvailabeButton );
		} else {
			final XButton offlineButton = new XButton( "Offline", Icons.F_NETWORK_STATUS_OFFLINE.get() );
			offlineButton.addActionListener( new ActionAdapter() {
				@Override
				public void actionPerformed( final ActionEvent event ) {
					GuiUtils.showWarningMsg( new RHtml( "Offline Mode", "html/statusbar/offline-mode.html", "launcherName", Consts.LAUNCHER_NAME,
					        "operatorName", Consts.APP_OPERATOR_NAME ).get() );
				}
			} );
			statusBar.add( offlineButton );
		}
		
		statusBar.add( new JSeparator( SwingConstants.VERTICAL ) );
		final MemoryStatus memoryStatus = new MemoryStatus();
		SettingsGui.bindVisibilityToSkillLevel( memoryStatus, SkillLevel.NORMAL );
		statusBar.add( memoryStatus );
		
		ccp.addSouth( statusBar );
		
		SettingsGui.bindVisibilityToSetting( statusBar, Settings.SHOW_STATUS_BAR, Env.APP_SETTINGS );
	}
	
	/**
	 * Activates the updates available button. Also "clicks" on it.
	 */
	public void activateUpdatesAvailable() {
		updatesAvailabeButton.setVisible( true );
		updatesAvailabeButton.doClick( 0 );
	}
	
	/**
	 * Rebuilds the main page tree.
	 * <p>
	 * Must be called if the page tree changes (e.g. a new page was added or one was removed).
	 * </p>
	 */
	public void rebuildMainPageTree() {
		multiPageComp.rebuildPageTree( false );
	}
	
	/**
	 * Returns the replay folders node page.
	 * 
	 * @return the replay folders node page
	 */
	public RepFoldersPage getRepFoldersPage() {
		return repFoldersPage;
	}
	
	/**
	 * Returns the Multi-replay analyzers node page.
	 * 
	 * @return the Multi-replay analyzers node page
	 */
	public MultiRepAnalyzersPage getMultiRepAnalyzersPage() {
		return multiRepAnalyzersPage;
	}
	
	@Override
	public MainFrame asFrame() {
		return this;
	}
	
	@Override
	public MultiPageComp getMultiPageComp() {
		return multiPageComp;
	}
	
}
