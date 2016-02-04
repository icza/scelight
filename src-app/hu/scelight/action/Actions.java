/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight.action;

import hu.scelight.Consts;
import hu.scelight.Scelight;
import hu.scelight.gui.dialog.AboutDialog;
import hu.scelight.gui.dialog.JobsDialog;
import hu.scelight.gui.dialog.dlregfile.DlRegistrationFileDialog;
import hu.scelight.gui.icon.Icons;
import hu.scelight.gui.overlaycard.ApmOverlay;
import hu.scelight.gui.overlaycard.LastGameInfoOverlay;
import hu.scelight.gui.page.about.logs.LogsPage;
import hu.scelight.gui.page.about.sysinfo.SysInfoPage;
import hu.scelight.gui.setting.SettingsDialog;
import hu.scelight.gui.setting.SettingsGui;
import hu.scelight.service.env.Env;
import hu.scelight.service.sc2reg.Sc2RegMonitor;
import hu.scelight.service.settings.Settings;
import hu.scelight.service.sound.Sounds;
import hu.scelight.util.Utils;
import hu.scelight.util.gui.GuiUtils;
import hu.scelight.util.job.UpdateCheckerJob;
import hu.scelight.util.sc2rep.LatestRepSearchCoordinatorJob;
import hu.scelight.util.sc2rep.RepUtils;
import hu.sllauncher.action.UrlAction;
import hu.sllauncher.action.XAction;
import hu.sllauncher.gui.comp.XFileChooser;
import hu.sllauncher.gui.page.LicensePage;
import hu.sllauncher.gui.page.NewsPage;
import hu.sllauncher.gui.page.RegInfoPage;
import hu.sllauncher.service.env.OpSys;
import hu.sllauncher.service.sound.Sound;

import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.KeyStroke;
import javax.swing.Timer;

/**
 * Actions collection.
 * 
 * <p>
 * <i>Implementation note:</i> This is an interface instead of a class because constants defined in an interface are implicitly <code>public</code>,
 * <code>static</code> and <code>final</code>, and so these keywords can be omitted!
 * </p>
 * 
 * @author Andras Belicza
 */
@SuppressWarnings( "serial" )
public interface Actions {
	
	// FILE MENU ACTIONS
	
	/** Open a Replay. */
	XAction OPEN_REPLAY                   = new XAction( KeyStroke.getKeyStroke( KeyEvent.VK_O, InputEvent.CTRL_MASK ), Icons.F_CHART, "_Open a Replay..." ) {
		                                      @Override
		                                      public void actionPerformed( final ActionEvent event ) {
			                                      final XFileChooser fileChooser = RepUtils.createReplayChooserDialog( false );
			                                      if ( XFileChooser.APPROVE_OPTION != fileChooser.showOpenDialog( Env.MAIN_FRAME ) )
				                                      return;
			                                      
			                                      Env.MAIN_FRAME.repAnalyzersPage.newRepAnalyzerPage( fileChooser.getSelectedPath(), true );
		                                      }
	                                      };
	
	/** Quick open last replay. */
	XAction QUICK_OPEN_LAST_REP           = new XAction( KeyStroke.getKeyStroke( KeyEvent.VK_O, InputEvent.CTRL_MASK | InputEvent.SHIFT_MASK ),
	                                              Icons.F_CHART_ARROW, "Quick Open _Last Replay" ) {
		                                      @Override
		                                      public void actionPerformed( final ActionEvent event ) {
			                                      final LatestRepSearchCoordinatorJob lj = new LatestRepSearchCoordinatorJob();
			                                      lj.setEdtCallback( new Runnable() {
				                                      @Override
				                                      public void run() {
					                                      if ( lj.getLatestReplay() != null )
						                                      Env.MAIN_FRAME.repAnalyzersPage.newRepAnalyzerPage( lj.getLatestReplay(), true );
					                                      else
						                                      GuiUtils.showWarningMsg( "Could not find any replays in the monitored Replay Folders!" );
				                                      }
			                                      } );
			                                      lj.start();
		                                      }
	                                      };
	
	/** Open Settings dialog. */
	XAction SETTINGS                      = new XAction( KeyStroke.getKeyStroke( KeyEvent.VK_P, InputEvent.CTRL_MASK ), Icons.F_GEAR, "_Settings..." ) {
		                                      @Override
		                                      public void actionPerformed( final ActionEvent event ) {
			                                      new SettingsDialog( Env.MAIN_FRAME, Settings.NODE_UI ).setVisible( true );
		                                      }
	                                      };
	
	/** Start SC2. */
	XAction START_SC2                     = new XAction( Icons.SC2_ICON, "S_tart StarCraft II" ) {
		                                      @Override
		                                      public void actionPerformed( final ActionEvent event ) {
			                                      Utils.launchExternalApp(
			                                              Env.APP_SETTINGS.get( Settings.SC2_INSTALL_FOLDER ).resolve(
			                                                      Env.OS == OpSys.OS_X ? "StarCraft II.app/Contents/MacOS/StarCraft II" : "StarCraft II.exe" ),
			                                              null, " ", "Is your StarCraft II install folder setting correct?",
			                                              SettingsGui.createSettingLink( Settings.NODE_SC2_INSTALLATION ) );
		                                      }
	                                      };
	
	/** Start SC2 editor. */
	XAction START_SC2_EDITOR              = new XAction( Icons.SC2_EDITOR, "Start StarCraft II _Editor" ) {
		                                      @Override
		                                      public void actionPerformed( final ActionEvent event ) {
			                                      Utils.launchExternalApp(
			                                              Env.APP_SETTINGS.get( Settings.SC2_INSTALL_FOLDER ).resolve(
			                                                      Env.OS == OpSys.OS_X ? "StarCraft II Editor.app/Contents/MacOS/StarCraft II Editor"
			                                                              : "StarCraft II Editor.exe" ), null, " ",
			                                              "Is your StarCraft II install folder setting correct?", SettingsGui
			                                                      .createSettingLink( Settings.NODE_SC2_INSTALLATION ) );
		                                      }
	                                      };
	
	/** Clear Recent Replays. */
	XAction CLEAR_RECENT_REPS             = new XAction( Icons.F_CROSS, "_Clear Recent Replays" ) {
		                                      @Override
		                                      public void actionPerformed( final ActionEvent event ) {
			                                      Env.APP_SETTINGS.reset( Settings.RECENT_REPLAYS_BEAN );
		                                      }
	                                      };
	
	/** Exit. */
	XAction EXIT                          = new XAction( KeyStroke.getKeyStroke( KeyEvent.VK_X, InputEvent.ALT_MASK ), Icons.F_DOOR_OPEN_IN, "E_xit" ) {
		                                      @Override
		                                      public void actionPerformed( final ActionEvent event ) {
			                                      Scelight.INSTANCE().exit();
		                                      }
	                                      };
	
	/** Visit home page. */
	XAction HOME_PAGE                     = new UrlAction( "Visit home page", Consts.URL_HOME_PAGE );
	
	
	// VIEW MENU ACTIONS
	
	/** Show Tool bar. */
	XAction SHOW_MAIN_TOOL_BAR            = new BoolSettingAction( Icons.F_UI_TOOLBAR, "Show Main _Tool Bar", Settings.SHOW_MAIN_TOOL_BAR, Env.APP_SETTINGS );
	
	/** Show Status bar. */
	XAction SHOW_STATUS_BAR               = new BoolSettingAction( Icons.F_UI_STATUS_BAR, "Show _Status Bar", Settings.SHOW_STATUS_BAR, Env.APP_SETTINGS );
	
	
	// TOOLS MENU ACTIONS
	
	/** Show / Hide Live APM Overlay. */
	XAction LIVE_APM_OVERLAY              = new XAction( Icons.F_COUNTER, "Live _APM Overlay" ) {
		                                      {
			                                      if ( !Sc2RegMonitor.isSupported() )
				                                      setEnabled( false );
		                                      }
		                                      
		                                      @Override
		                                      public void actionPerformed( final ActionEvent event ) {
			                                      final ApmOverlay apmOverlay = ApmOverlay.INSTANCE();
			                                      if ( apmOverlay == null )
				                                      new ApmOverlay();
			                                      else
				                                      apmOverlay.close();
		                                      }
	                                      };
	
	/** Show / Hide Last Game Info Overlay. */
	XAction LAST_GAME_INFO_OVERLAY        = new XAction( Icons.F_INFORMATION_BALLOON, "Last Game _Info Overlay" ) {
		                                      @Override
		                                      public void actionPerformed( final ActionEvent event ) {
			                                      final LastGameInfoOverlay lastGameInfoOverlay = LastGameInfoOverlay.INSTANCE();
			                                      if ( lastGameInfoOverlay != null ) {
				                                      lastGameInfoOverlay.close();
				                                      return;
			                                      }
			                                      
			                                      final LatestRepSearchCoordinatorJob lj = new LatestRepSearchCoordinatorJob();
			                                      lj.setEdtCallback( new Runnable() {
				                                      @Override
				                                      public void run() {
					                                      if ( lj.getLatestReplay() != null )
						                                      new LastGameInfoOverlay( lj.getLatestReplay() );
					                                      else
						                                      GuiUtils.showWarningMsg( "Could not find any replays in the monitored Replay Folders!" );
				                                      }
			                                      } );
			                                      lj.start();
		                                      }
	                                      };
	
	/** Download Registration file. */
	XAction DL_REGISTRATION_FILE          = new XAction( Icons.F_LICENCE_KEY, "_Download Registration File..." ) {
		                                      @Override
		                                      public void actionPerformed( final ActionEvent event ) {
			                                      if ( Env.REG_MANAGER.isOk() ) {
				                                      Sound.play( Sounds.THANKS_FOR_REGISTERING );
				                                      GuiUtils.showInfoMsg( "You already have a valid registration file.", " ",
				                                              GuiUtils.linkForAction( "View Registration info...", ABOUT_REGINFO ) );
				                                      return;
			                                      }
			                                      new DlRegistrationFileDialog();
		                                      }
	                                      };
	
	/** Running jobs. */
	XAction RUNNING_JOBS                  = new XAction( KeyStroke.getKeyStroke( KeyEvent.VK_ESCAPE, InputEvent.SHIFT_MASK ), Icons.F_HARD_HAT,
	                                              "Running _Jobs..." ) {
		                                      @Override
		                                      public void actionPerformed( final ActionEvent event ) {
			                                      new JobsDialog();
		                                      }
	                                      };
	
	
	// WINDOW MENU ACTIONS
	
	/** Minimizes the main frame to the system tray. */
	XAction MINIMIZE_TO_TRAY              = new XAction( Icons.F_ARROW_STOP_270, "_Minimize to Tray" ) {
		                                      {
			                                      // Disable minimization by default, we'll enable it if everything goes well
			                                      // and tray icon gets installed successfully.
			                                      setEnabled( false );
		                                      }
		                                      
		                                      @Override
		                                      public void actionPerformed( final ActionEvent event ) {
			                                      // Only minimize if enabled (if system tray is supported)
			                                      if ( isEnabled() )
				                                      Env.MAIN_FRAME.setVisible( false );
		                                      }
	                                      };
	
	/** Restores the default window position. */
	XAction RESTORE_DEF_WIN_POSITION      = new XAction( Icons.F_APPLICATION_SUB, "_Restore default position" ) {
		                                      @Override
		                                      public void actionPerformed( final ActionEvent event ) {
			                                      Env.MAIN_FRAME.restoreDefaultWinPos();
		                                      }
	                                      };
	
	/** Minimize to Tray on Close. */
	XAction MINIMIZE_TO_TRAY_ON_CLOSE     = new BoolSettingAction( Icons.F_APPLICATION_DOCK_TAB, "Minimize to Tray on _Close",
	                                              Settings.MINIMIZE_TO_TRAY_ON_CLOSE, Env.APP_SETTINGS );
	
	/** Minimize to Tray on Close. */
	XAction START_MINIMIZED_TO_TRAY       = new BoolSettingAction( Icons.F_APPLICATION_DOCK_TAB, "_Start Minimized to Tray", Settings.START_MINIMIZED_TO_TRAY,
	                                              Env.APP_SETTINGS );
	
	/** Maximize Window on Start. */
	XAction MAXIMIZE_WINDOW_ON_START      = new BoolSettingAction( Icons.F_APPLICATION_RESIZE, "Ma_ximize Window on Start", Settings.MAXIMIZE_WINDOW_ON_START,
	                                              Env.APP_SETTINGS );
	
	/** Restore last Window position on Start. */
	XAction RESTORE_LAST_WIN_POS_ON_START = new BoolSettingAction( Icons.F_APPLICATION_SUB, "Restore last Window _position on Start",
	                                              Settings.RESTORE_LAST_WIN_POS_ON_START, Env.APP_SETTINGS );
	
	
	// HELP MENU ACTIONS
	
	/** News About page. */
	XAction ABOUT_NEWS                    = new AboutPageAction( new NewsPage() );
	
	/** License About page. */
	XAction ABOUT_LICENSE                 = new AboutPageAction( new LicensePage() );
	
	/** Registration info About page. */
	XAction ABOUT_REGINFO                 = new AboutPageAction( new RegInfoPage() );
	
	/** Logs About page. */
	XAction ABOUT_LOGS                    = new AboutPageAction( new LogsPage() );
	
	/** System info About page. */
	XAction ABOUT_SYS_INFO                = new AboutPageAction( new SysInfoPage() );
	
	/** Check for updates. */
	XAction CHECK_FOR_UPDATES             = new XAction( Icons.F_ARROW_CIRCLE_DOUBLE, "_Check for Updates" ) {
		                                      /** Number of scheduled checks. */
		                                      private int scheduledCount;
		                                      
		                                      {
			                                      setEnabled( Env.ONLINE );
		                                      }
		                                      
		                                      @Override
		                                      public void actionPerformed( final ActionEvent event ) {
			                                      if ( !isEnabled() )
				                                      return;
			                                      
			                                      if ( event != null && event.getSource() instanceof Timer ) {
				                                      // Computer might be up after a sleep, so the elapsed time since start is
				                                      // not good for Scheduled checks, that's why we count the scheduled checks.
				                                      final int mins = ( Consts.SCHEDULED_UPDATE_CHECK_DELAY / 60_000 ) * scheduledCount++;
				                                      new UpdateCheckerJob( UpdateCheckerJob.Type.SCHEDULED, mins ).start();
			                                      } else {
				                                      new UpdateCheckerJob( UpdateCheckerJob.Type.MANUAL,
				                                              (int) ( ( System.currentTimeMillis() - Env.APPLICATION_START ) / Utils.MS_IN_MIN ) ).start();
			                                      }
		                                      }
	                                      };
	
	/** About. */
	XAction ABOUT_ABOUT                   = new XAction( KeyStroke.getKeyStroke( KeyEvent.VK_F1, 0 ), Icons.F_INFORMATION, "_About " + Consts.APP_NAME + "..." ) {
		                                      @Override
		                                      public void actionPerformed( final ActionEvent event ) {
			                                      new AboutDialog( null );
		                                      }
	                                      };
	
	
	
	// MISCELLANEOUS, NON-MENU ACTIONS
	
	/** Shows and activates the main frame (from minimized to tray state). */
	XAction SHOW_MAIN_FRAME               = new XAction( Icons.F_APPLICATION_RESIZE, "_Show Main Window" ) {
		                                      @Override
		                                      public void actionPerformed( final ActionEvent event ) {
			                                      Env.MAIN_FRAME.restoreMainFrame();
		                                      }
	                                      };
	
}
