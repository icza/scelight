/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight.service.settings;

import static hu.sllauncher.util.SkillLevel.ADVANCED;
import static hu.sllauncher.util.SkillLevel.BASIC;
import static hu.sllauncher.util.SkillLevel.DEVELOPER;
import static hu.sllauncher.util.SkillLevel.HIDDEN;
import static hu.sllauncher.util.SkillLevel.NORMAL;
import hu.scelight.Consts;
import hu.scelight.bean.RecentReplaysBean;
import hu.scelight.bean.RepListColumnsBean;
import hu.scelight.bean.repfolders.RepFoldersBean;
import hu.scelight.bean.settings.type.TemplateSetting;
import hu.scelight.bean.settings.type.ValidatedMultilineStringSetting;
import hu.scelight.gui.comp.IndicatorTextArea;
import hu.scelight.gui.help.Helps;
import hu.scelight.gui.icon.Icons;
import hu.scelight.gui.page.repanalyzer.charts.ChartType;
import hu.scelight.gui.page.repanalyzer.charts.chartfactory.PlayerStatsChartFactory.Stat;
import hu.scelight.gui.page.repanalyzer.charts.chartscanvas.chart.LineTimeChart.Presentation;
import hu.scelight.gui.page.repanalyzer.charts.chartscanvas.chart.commands.DurationType;
import hu.scelight.gui.page.repanalyzer.inspector.binarydata.HexLineSize;
import hu.scelight.gui.page.repanalyzer.mapinfo.Zoom;
import hu.scelight.sc2.rep.model.gameevents.cmd.TagTransformation;
import hu.scelight.sc2.rep.model.initdata.gamedesc.BnetLang;
import hu.scelight.sc2.rep.model.initdata.gamedesc.Region;
import hu.scelight.service.env.Env;
import hu.scelight.util.Utils;
import hu.scelight.util.gui.ColorEnum;
import hu.scelightapibase.bean.settings.ISettingsBean;
import hu.scelightapibase.bean.settings.type.ISetting;
import hu.scelightapibase.bean.settings.type.viewhints.ICompConfigurer;
import hu.sllauncher.bean.settings.type.BeanSetting;
import hu.sllauncher.bean.settings.type.BoolSetting;
import hu.sllauncher.bean.settings.type.EnumSetting;
import hu.sllauncher.bean.settings.type.FixedEnumValuesSetting;
import hu.sllauncher.bean.settings.type.IntSetting;
import hu.sllauncher.bean.settings.type.MultilineStringSetting;
import hu.sllauncher.bean.settings.type.NodeSetting;
import hu.sllauncher.bean.settings.type.PathSetting;
import hu.sllauncher.bean.settings.type.SettingsGroup;
import hu.sllauncher.bean.settings.type.StringSetting;
import hu.sllauncher.bean.settings.type.ValidatedStringSetting;
import hu.sllauncher.bean.settings.type.viewhints.HostCheckTestBtnFactory;
import hu.sllauncher.bean.settings.type.viewhints.VHB;
import hu.sllauncher.gui.comp.IndicatorTextField;
import hu.sllauncher.service.settings.LSettings;

import java.nio.file.Paths;

import javax.swing.JComponent;

/**
 * Instances of the app settings.
 * 
 * <p>
 * <i>Implementation note:</i> This is an interface instead of a class because constants defined in an interface are implicitly <code>public</code>,
 * <code>static</code> and <code>final</code>, and so these keywords can be omitted!
 * </p>
 * 
 * @author Andras Belicza
 */
public interface Settings extends LSettings {
	
	
	// MAIN MULTI-PAGE COMPONENT SETTINGS
	
	/** Main Multi-page component group. */
	SettingsGroup                     GROUP_MAIN_MULTI_PAGE                = new SettingsGroup( "Main Multi-page" );
	
	/** Main Multi-page Navigation tree initial width in pixels. */
	IntSetting                        MAIN_NAV_TREE_INITIAL_WIDTH          = new IntSetting( "MAIN_NAV_TREE_INITIAL_WIDTH", NODE_MULTI_PAGE,
	                                                                               GROUP_MAIN_MULTI_PAGE, NORMAL, "Initial width of the Main Navigation tree",
	                                                                               VHB.sstext_( "pixel" ), 250, 0, 999 );
	
	/** Auto resize Main Multi-page Navigation tree. */
	BoolSetting                       MAIN_NAV_TREE_AUTO_RESIZE            = new BoolSetting( "MAIN_NAV_TREE_AUTO_RESIZE", NODE_MULTI_PAGE,
	                                                                               GROUP_MAIN_MULTI_PAGE, NORMAL, "Auto-resize the Main Navigation tree",
	                                                                               VHB.help_( Helps.AUTO_RESIZE_MAIN_NAV_TREE ), Boolean.FALSE );
	
	/** Max auto-width of the Main Multi-page Navigation tree in pixels. */
	IntSetting                        MAIN_NAV_TREE_MAX_AUTO_WIDTH         = new IntSetting( "MAIN_NAV_TREE_MAX_AUTO_WIDTH", NODE_MULTI_PAGE,
	                                                                               GROUP_MAIN_MULTI_PAGE, NORMAL, "Max auto-width of the Main Navigation tree",
	                                                                               VHB.sstext_( "pixel" ), 300, 60, 999 );
	
	
	
	// TOOL BAR SETTINGS
	
	/** Main Tool Bar group. */
	SettingsGroup                     GROUP_MAIN_TOOL_BAR                  = new SettingsGroup( "Main Tool Bar" );
	
	/** Show main tool bar. */
	BoolSetting                       SHOW_MAIN_TOOL_BAR                   = new BoolSetting( "SHOW_MAIN_TOOL_BAR", TOOL_BAR, GROUP_MAIN_TOOL_BAR, NORMAL,
	                                                                               "Show main Tool bar", null, Boolean.TRUE );
	
	/** Allow moving the main tool bar. */
	BoolSetting                       MAIN_TOOL_BAR_FLOTABLE               = new BoolSetting( "MAIN_TOOL_BAR_FLOTABLE", TOOL_BAR, GROUP_MAIN_TOOL_BAR, NORMAL,
	                                                                               "Allow moving / floating the main Tool bar",
	                                                                               VHB.help_( Helps.MOVING_THE_TOOL_BAR ), Boolean.FALSE );
	
	
	
	// STATUS BAR SETTINGS
	
	/** Tool bar settings. */
	NodeSetting                       NODE_STATUS_BAR                      = new NodeSetting( "STATUS_BAR", NODE_UI, "Status Bar", Icons.F_UI_STATUS_BAR );
	
	/** Status Bar group. */
	SettingsGroup                     GROUP_STATUS_BAR                     = new SettingsGroup( "Status Bar" );
	
	/** Show status bar. */
	BoolSetting                       SHOW_STATUS_BAR                      = new BoolSetting( "SHOW_STATUS_BAR", NODE_STATUS_BAR, GROUP_STATUS_BAR, NORMAL,
	                                                                               "Show Status bar", null, Boolean.TRUE );
	
	
	
	// WINDOW SETTINGS
	
	/** Window settings. */
	NodeSetting                       NODE_WINDOW                          = new NodeSetting( "WINDOW", NODE_UI, "Window", Icons.F_APPLICATION_BLUE );
	
	/** Window group. */
	SettingsGroup                     GROUP_WINDOW                         = new SettingsGroup( "Window" );
	
	/** Minimize to Tray on Close. */
	BoolSetting                       MINIMIZE_TO_TRAY_ON_CLOSE            = new BoolSetting( "MINIMIZE_TO_TRAY_ON_CLOSE", NODE_WINDOW, GROUP_WINDOW, NORMAL,
	                                                                               "Minimize to Tray on Close", null, Boolean.TRUE );
	
	/** Start Minimized to Tray. */
	BoolSetting                       START_MINIMIZED_TO_TRAY              = new BoolSetting( "START_MINIMIZED_TO_TRAY", NODE_WINDOW, GROUP_WINDOW, NORMAL,
	                                                                               "Start Minimized to Tray", null, Boolean.FALSE );
	
	/** Show tray info bubble when started minimized. */
	BoolSetting                       SHOW_INFO_WHEN_STARTED_MINIMIZED     = new BoolSetting( "SHOW_INFO_WHEN_STARTED_MINIMIZED", NODE_WINDOW, GROUP_WINDOW,
	                                                                               NORMAL, "Show Tray info bubble when started minimized to Tray", null,
	                                                                               Boolean.TRUE );
	
	/** Maximize Window on Start. */
	BoolSetting                       MAXIMIZE_WINDOW_ON_START             = new BoolSetting( "MAXIMIZE_WINDOW_ON_START", NODE_WINDOW, GROUP_WINDOW, NORMAL,
	                                                                               "Maximize Window on Start", null, Boolean.TRUE );
	
	/** Restore last Window position on Start. */
	BoolSetting                       RESTORE_LAST_WIN_POS_ON_START        = new BoolSetting( "RESTORE_LAST_WIN_POS_ON_START", NODE_WINDOW, GROUP_WINDOW,
	                                                                               NORMAL, "Restore last Window position on Start", null, Boolean.TRUE );
	
	/** Stored Window Position group. */
	SettingsGroup                     GROUP_STORED_WINDOW_POSITION         = new SettingsGroup( "Stored Window Position" );
	
	/** Stored Window X position. */
	IntSetting                        STORED_WINDOW_POSITION_X             = new IntSetting( "STORED_WINDOW_POSITION_X", NODE_WINDOW,
	                                                                               GROUP_STORED_WINDOW_POSITION, ADVANCED, "Stored Window X position", null,
	                                                                               100, -9999, 9999 );
	
	/** Stored Window Y position. */
	IntSetting                        STORED_WINDOW_POSITION_Y             = new IntSetting( "STORED_WINDOW_POSITION_Y", NODE_WINDOW,
	                                                                               GROUP_STORED_WINDOW_POSITION, ADVANCED, "Stored Window Y position", null,
	                                                                               100, -9999, 9999 );
	
	/** Stored Window width (pixels). */
	IntSetting                        STORED_WINDOW_WIDTH                  = new IntSetting( "STORED_WINDOW_WIDTH", NODE_WINDOW, GROUP_STORED_WINDOW_POSITION,
	                                                                               ADVANCED, "Stored Window width", VHB.sstext_( "pixels" ), 1200, 200, 9999 );
	
	/** Stored Window height (pixels). */
	IntSetting                        STORED_WINDOW_HEIGHT                 = new IntSetting( "STORED_WINDOW_HEIGHT", NODE_WINDOW, GROUP_STORED_WINDOW_POSITION,
	                                                                               ADVANCED, "Stored Window height", VHB.sstext_( "pixels" ), 750, 100, 9999 );
	
	
	
	// LOCALE SETTINGS
	
	/** Battle.net Web Language group. */
	SettingsGroup                     GROUP_BNET_LANG                      = new SettingsGroup( "Battle.net Website Language",
	                                                                               Helps.BATTLE_NET_WEBSITE_LANGUAGE );
	
	/** Date format. */
	EnumSetting< BnetLang >           PREFERRED_BNET_LANG_1                = new EnumSetting<>( "PREFERRED_BNET_LANG_1", NODE_LOCALE, GROUP_BNET_LANG, BASIC,
	                                                                               "Preferred Battle.net language #1", null, BnetLang.ENGLISH );
	
	/** Date format. */
	EnumSetting< BnetLang >           PREFERRED_BNET_LANG_2                = new EnumSetting<>( "PREFERRED_BNET_LANG_2", NODE_LOCALE, GROUP_BNET_LANG, BASIC,
	                                                                               "Preferred Battle.net language #2", null, BnetLang.ENGLISH );
	
	/** Date format. */
	EnumSetting< BnetLang >           PREFERRED_BNET_LANG_3                = new EnumSetting<>( "PREFERRED_BNET_LANG_3", NODE_LOCALE, GROUP_BNET_LANG, BASIC,
	                                                                               "Preferred Battle.net language #3", null, BnetLang.ENGLISH );
	
	
	
	// SC2 INSTALL SETTINGS
	
	/** SC2 installation settings. */
	NodeSetting                       NODE_SC2_INSTALLATION                = new NodeSetting( "SC2_INSTALLATION", null, "StarCraft II Installation",
	                                                                               Icons.SC2_ICON );
	
	/** SC2 installation group. */
	SettingsGroup                     GROUP_SC2_INSTALLATION               = new SettingsGroup( "StarCraft II Installation" );
	
	/** SC2 install folder. */
	PathSetting                       SC2_INSTALL_FOLDER                   = new PathSetting( "SC2_INSTALL_FOLDER", NODE_SC2_INSTALLATION,
	                                                                               GROUP_SC2_INSTALLATION, BASIC, "StarCraft II Install Folder",
	                                                                               VHB.dtitle_( "Choose your StarCraft II Install Folder" ),
	                                                                               Env.OS.getDefSc2InstallPath() );
	
	/** SC2 maps cache folder. */
	PathSetting                       SC2_MAPS_FOLDER                      = new PathSetting( "SC2_MAPS_FOLDER", NODE_SC2_INSTALLATION, GROUP_SC2_INSTALLATION,
	                                                                               BASIC, "StarCraft II Maps Cache Folder",
	                                                                               VHB.dtitle_( "Choose your StarCraft II Maps Cache Folder" ),
	                                                                               Env.OS.getDefSc2MapsPath() );
	
	/** Map Download Manager group. */
	SettingsGroup                     GROUP_MAP_DOWNLOAD_MANAGER           = new SettingsGroup( "Map Download Manager" );
	
	/** Download maps from the preferred region first. */
	BoolSetting                       USE_PREFERRED_MAP_DL_REGION          = new BoolSetting( "USE_PREFERRED_MAP_DL_REGION", NODE_SC2_INSTALLATION,
	                                                                               GROUP_MAP_DOWNLOAD_MANAGER, NORMAL,
	                                                                               "Download maps from the Preferred region first",
	                                                                               VHB.help_( Helps.MAP_DOWNLOAD_SOURCE_REGION ), Boolean.FALSE );
	
	/** Preferred region to download maps from. */
	FixedEnumValuesSetting< Region >  PREFERRED_MAP_DL_REGION              = new FixedEnumValuesSetting<>( "PREFERRED_MAP_DL_REGION", NODE_SC2_INSTALLATION,
	                                                                               GROUP_MAP_DOWNLOAD_MANAGER, NORMAL,
	                                                                               "Preferred region to download maps from", null, Region.US, Region.US,
	                                                                               Region.EUROPE, Region.KOREA, Region.CHINA, Region.SEA );
	
	/** Max number of parallel map downloads. */
	IntSetting                        MAX_PARALLEL_MAP_DOWNLOADS           = new IntSetting(
	                                                                               "MAX_PARALLEL_MAP_DOWNLOADS",
	                                                                               NODE_SC2_INSTALLATION,
	                                                                               GROUP_MAP_DOWNLOAD_MANAGER,
	                                                                               NORMAL,
	                                                                               "<html>Max number of parallel map downloads:<br>(set 0 to disable map auto-download)</html>",
	                                                                               null, 3, 0, 6 );
	
	
	
	// SC2 GAME MONITOR SETTINGS
	
	/** SC2 Game Monitor settings. */
	NodeSetting                       NODE_SC2_GAME_MONITOR                = new NodeSetting( "SC2_GAME_MONITOR", null, "SC2 Game Monitor", Icons.SC2_REPLAY );
	
	/** SC2 Game Monitor group. */
	SettingsGroup                     GROUP_SC2_GAME_MONITOR               = new SettingsGroup( "SC2 Game Monitor (works only on Windows OS!)",
	                                                                               Helps.SC2_GAME_MONITOR );
	
	/** Play 'Game started' voice when game starts. */
	BoolSetting                       PLAY_GAME_STARTED_VOICE              = new BoolSetting( "PLAY_GAME_STARTED_VOICE", NODE_SC2_GAME_MONITOR,
	                                                                               GROUP_SC2_GAME_MONITOR, BASIC, "Play 'Game started' voice when game starts",
	                                                                               null, Boolean.TRUE );
	
	/** Play 'Game ended' voice when game ends. */
	BoolSetting                       PLAY_GAME_ENDED_VOICE                = new BoolSetting( "PLAY_GAME_ENDED_VOICE", NODE_SC2_GAME_MONITOR,
	                                                                               GROUP_SC2_GAME_MONITOR, BASIC, "Play 'Game ended' voice when game ends",
	                                                                               null, Boolean.TRUE );
	
	/** Show Live APM Overlay when game starts. */
	BoolSetting                       SHOW_APM_OVERLAY_WHEN_STARTED        = new BoolSetting( "SHOW_APM_OVERLAY_WHEN_STARTED", NODE_SC2_GAME_MONITOR,
	                                                                               GROUP_SC2_GAME_MONITOR, BASIC, "Show Live APM Overlay when game starts",
	                                                                               null, Boolean.FALSE );
	
	/** Hide Live APM Overlay when game ends. */
	BoolSetting                       HIDE_APM_OVERLAY_WHEN_ENDED          = new BoolSetting( "HIDE_APM_OVERLAY_WHEN_ENDED", NODE_SC2_GAME_MONITOR,
	                                                                               GROUP_SC2_GAME_MONITOR, BASIC, "Hide Live APM Overlay when game ends", null,
	                                                                               Boolean.FALSE );
	
	
	
	// LIVE APM SETTINGS
	
	/** Live APM settings. */
	NodeSetting                       NODE_LIVE_APM                        = new NodeSetting( "LIVE_APM", NODE_SC2_GAME_MONITOR, "Live APM", Icons.F_COUNTER );
	
	/** Live APM Overlay group. */
	SettingsGroup                     GROUP_LIVE_APM_OVERLAY               = new SettingsGroup( "Live APM Overlay", Helps.LIVE_APM_OVERLAY );
	
	/** Live APM Overlay center X position. */
	IntSetting                        APM_OVERLAY_CENTER_X                 = new IntSetting( "APM_OVERLAY_CENTER_X", NODE_LIVE_APM, GROUP_LIVE_APM_OVERLAY,
	                                                                               NORMAL, "Overlay center X position", null, 800, -9999, 9999 );
	
	/** Live APM Overlay center Y position. */
	IntSetting                        APM_OVERLAY_CENTER_Y                 = new IntSetting( "APM_OVERLAY_CENTER_Y", NODE_LIVE_APM, GROUP_LIVE_APM_OVERLAY,
	                                                                               NORMAL, "Overlay center Y position", null, 50, -9999, 9999 );
	
	/** Live APM Overlay opacity. */
	IntSetting                        APM_OVERLAY_OPACITY                  = new IntSetting( "APM_OVERLAY_OPACITY", NODE_LIVE_APM, GROUP_LIVE_APM_OVERLAY,
	                                                                               BASIC, "Overlay opacity", VHB.sstext_( "%" ), 70, 20, 100 );
	
	/** Live APM Overlay locked. */
	BoolSetting                       APM_OVERLAY_LOCKED                   = new BoolSetting( "APM_OVERLAY_LOCKED", NODE_LIVE_APM, GROUP_LIVE_APM_OVERLAY,
	                                                                               BASIC, "Overlay locked", null, Boolean.FALSE );
	
	/** Live APM Overlay focusable. */
	BoolSetting                       APM_OVERLAY_FOCUSABLE                = new BoolSetting( "APM_OVERLAY_FOCUSABLE", NODE_LIVE_APM, GROUP_LIVE_APM_OVERLAY,
	                                                                               BASIC, "Overlay focusable", null, Boolean.FALSE );
	
	/** Live APM Overlay allow positioning outside the main screen. */
	BoolSetting                       APM_OVERLAY_ALLOW_OUT_MAIN_SCR       = new BoolSetting( "APM_OVERLAY_ALLOW_OUT_MAIN_SCR", NODE_LIVE_APM,
	                                                                               GROUP_LIVE_APM_OVERLAY, BASIC, "Allow positioning outside the main screen",
	                                                                               null, Boolean.FALSE );
	
	/** Live APM Overlay font size. */
	IntSetting                        APM_OVERLAY_FONT_SIZE                = new IntSetting( "APM_OVERLAY_FONT_SIZE", NODE_LIVE_APM, GROUP_LIVE_APM_OVERLAY,
	                                                                               BASIC, "Overlay font size", null, 16, 8, 44 );
	
	/** Live APM Overlay font color. */
	EnumSetting< ColorEnum >           APM_OVERLAY_FONT_COLOR              = new EnumSetting<>( "APM_OVERLAY_FONT_COLOR", NODE_LIVE_APM, GROUP_LIVE_APM_OVERLAY,
	                                                                               BASIC, "Overlay font color", null, ColorEnum.DEFAULT );
	
	/** Live APM Overlay background color. */
	EnumSetting< ColorEnum >           APM_OVERLAY_BACKGROUND_COLOR        = new EnumSetting<>( "APM_OVERLAY_BACKGROUND_COLOR", NODE_LIVE_APM, GROUP_LIVE_APM_OVERLAY,
	                                                                               BASIC, "Overlay background color", null, ColorEnum.DEFAULT );
	
	/** Live APM Overlay alert font color. */
	EnumSetting< ColorEnum >           APM_OVERLAY_ALERT_FONT_COLOR        = new EnumSetting<>( "APM_OVERLAY_ALERT_FONT_COLOR", NODE_LIVE_APM, GROUP_LIVE_APM_OVERLAY,
	                                                                               BASIC, "Overlay alert font color", null, ColorEnum.RED );
	
	/** Live APM Overlay alert background color. */
	EnumSetting< ColorEnum >           APM_OVERLAY_ALERT_BACKGROUND_COLOR  = new EnumSetting<>( "APM_OVERLAY_ALERT_BACKGROUND_COLOR", NODE_LIVE_APM, GROUP_LIVE_APM_OVERLAY,
	                                                                               BASIC, "Overlay alert background color", null, ColorEnum.YELLOW );
	
	/** APM Alert group. */
	SettingsGroup                     GROUP_APM_ALERT                      = new SettingsGroup( "APM Alert", Helps.APM_ALERT );
	
	/** Enable APM Alert. */
	BoolSetting                       ENABLE_APM_ALERT                     = new BoolSetting( "ENABLE_APM_ALERT", NODE_LIVE_APM, GROUP_APM_ALERT, BASIC,
	                                                                               "Enable APM Alert", null, Boolean.FALSE );
	
	/** APM Alert level. */
	IntSetting                        APM_ALERT_LEVEL                      = new IntSetting( "APM_ALERT_LEVEL", NODE_LIVE_APM, GROUP_APM_ALERT, BASIC,
	                                                                               "APM alert level", VHB.sstext_( "APM" ), 40, 0, 999 );
	
	/** APM Alert warm-up time. */
	IntSetting                        APM_ALERT_WARMUP_TIME                = new IntSetting( "APM_ALERT_WARMUP_TIME", NODE_LIVE_APM, GROUP_APM_ALERT, NORMAL,
	                                                                               "APM warm-up time", new VHB().sstext( "sec" ).help( Helps.APM_WARMUP_TIME )
	                                                                                       .build(), 80, 0, 300 );
	
	/** APM Alert alert when APM is back to normal. */
	BoolSetting                       APM_ALERT_WHEN_OK                    = new BoolSetting( "APM_ALERT_WHEN_OK", NODE_LIVE_APM, GROUP_APM_ALERT, NORMAL,
	                                                                               "Alert when APM is back to normal (above the alert level)", null,
	                                                                               Boolean.TRUE );
	
	/** Low APM Alert repetition interval if APM stays low. */
	IntSetting                        LOW_APM_ALERT_REPETITION             = new IntSetting( "APM_ALERT_LOW_REPETITION", NODE_LIVE_APM, GROUP_APM_ALERT,
	                                                                               NORMAL, "Low APM alert repetition if APM stays low", VHB.sstext_( "sec" ),
	                                                                               20, 4, 999 );
	
	
	
	// REPLAY FOLDER MONITOR SETTINGS
	
	/** Replay Folder Monitor settings. */
	NodeSetting                       NODE_REPLAY_FOLDER_MONITOR           = new NodeSetting( "REPLAY_FOLDER_MONITOR", null, "Replay Folder Monitor",
	                                                                               Icons.F_BLUE_FOLDER_SEARCH_RESULT );
	
	/** Replay Backup group. */
	SettingsGroup                     GROUP_REPLAY_FOLDER_MONITOR          = new SettingsGroup( "Replay Folder Monitor" );
	
	/** Only pick up "Just Played" new replays. */
	BoolSetting                       ONLY_PICK_UP_JUST_PLAYED_REPS        = new BoolSetting( "ONLY_PICK_UP_JUST_PLAYED_REPS", NODE_REPLAY_FOLDER_MONITOR,
	                                                                               GROUP_REPLAY_FOLDER_MONITOR, NORMAL,
	                                                                               "Only pick up 'Just Played' new replays",
	                                                                               VHB.help_( Helps.ONLY_PICK_UP_JUST_PLAYED_REPS ), Boolean.TRUE );
	
	/** Rename new replays in their original folder. */
	BoolSetting                       RENAME_NEW_REPLAYS                   = new BoolSetting( "RENAME_NEW_REPLAYS", NODE_REPLAY_FOLDER_MONITOR,
	                                                                               GROUP_REPLAY_FOLDER_MONITOR, BASIC,
	                                                                               "Rename detected new replays in their original folder",
	                                                                               VHB.help_( Helps.RENAME_NEW_REPLAYS ), Boolean.FALSE );
	
	/** Rename new replays template. */
	TemplateSetting                   RENAME_NEW_REPS_TEMPLATE             = new TemplateSetting( "RENAME_NEW_REPS_TEMPLATE", NODE_REPLAY_FOLDER_MONITOR,
	                                                                               GROUP_REPLAY_FOLDER_MONITOR, BASIC,
	                                                                               "Name template for renaming new replays",
	                                                                               VHB.dtitle_( "Rename New Replays Name Template" ),
	                                                                               "[dateTimeShort] [O][matchup][C] [map]" );
	
	/** Open new replays in the Replay Analyzer. */
	BoolSetting                       OPEN_NEW_REPLAYS                     = new BoolSetting( "OPEN_NEW_REPLAYS", NODE_REPLAY_FOLDER_MONITOR,
	                                                                               GROUP_REPLAY_FOLDER_MONITOR, BASIC,
	                                                                               "Auto-open new replays in the Replay Analyzer", null, Boolean.FALSE );
	
	/** Max number of Replay Analyzers to open for new replays. */
	IntSetting                        MAX_REP_ANALYZERS_FOR_NEW_REPS       = new IntSetting( "MAX_REP_ANALYZERS_FOR_NEW_REPS", NODE_REPLAY_FOLDER_MONITOR,
	                                                                               GROUP_REPLAY_FOLDER_MONITOR, NORMAL,
	                                                                               "Max number of Replay Analyzers for new reps",
	                                                                               VHB.help_( Helps.MAX_REP_ANALYZERS_FOR_NEW_REPS ), 20, 1, 50 );
	
	/** Show Game Info Overlay for new replays. */
	BoolSetting                       SHOW_GAME_INFO_FOR_NEW_REPS          = new BoolSetting( "SHOW_GAME_INFO_FOR_NEW_REPS", NODE_REPLAY_FOLDER_MONITOR,
	                                                                               GROUP_REPLAY_FOLDER_MONITOR, BASIC,
	                                                                               "Show Last Game Info Overlay for new replays", null, Boolean.FALSE );
	
	
	
	// REPLAY BACKUP SETTINGS
	
	/** Replay Backup settings. */
	NodeSetting                       NODE_REPLAY_BACKUP                   = new NodeSetting( "REPLAY_BACKUP", NODE_REPLAY_FOLDER_MONITOR, "Replay Backup",
	                                                                               Icons.F_DRIVE );
	
	/** Replay Backup group. */
	SettingsGroup                     GROUP_REPLAY_BACKUP                  = new SettingsGroup( "Replay Backup" );
	
	/** Backup replays. */
	BoolSetting                       BACKUP_REPLAYS                       = new BoolSetting( "BACKUP_REPLAYS", NODE_REPLAY_BACKUP, GROUP_REPLAY_BACKUP, BASIC,
	                                                                               "Auto-back up New Replays", VHB.help_( Helps.BACK_UP_NEW_REPLAYS ),
	                                                                               Boolean.TRUE );
	
	/** Replay backup folder. */
	PathSetting                       REPLAY_BACKUP_FOLDER                 = new PathSetting( "REPLAY_BACKUP_FOLDER", NODE_REPLAY_BACKUP, GROUP_REPLAY_BACKUP,
	                                                                               BASIC, "Replay Backup Folder",
	                                                                               VHB.dtitle_( "Choose your Replay Backup Folder" ), Paths.get(
	                                                                                       System.getProperty( "user.home" ), Consts.APP_NAME
	                                                                                               + " Replay Backup" ) );
	
	/** Rename new replays template. */
	TemplateSetting                   REPLAY_BACKUP_TEMPLATE               = new TemplateSetting( "REPLAY_BACKUP_TEMPLATE", NODE_REPLAY_BACKUP,
	                                                                               GROUP_REPLAY_BACKUP, BASIC, "Name template for replay backup",
	                                                                               VHB.dtitle_( "Replay Backup Name Template" ),
	                                                                               "[dateTimeShort] [O][matchup][C] [map]" );
	
	/** Play 'Replay saved' voice when replays are saved. */
	BoolSetting                       PLAY_REPLAY_SAVED_VOICE              = new BoolSetting( "PLAY_REPLAY_SAVED_VOICE", NODE_REPLAY_BACKUP,
	                                                                               GROUP_REPLAY_BACKUP, BASIC,
	                                                                               "Play 'Replay saved' voice when replays are backed up", null, Boolean.TRUE );
	
	/** Delete successfully backed up replays. */
	BoolSetting                       DELETE_BACKED_UP_REPLAYS             = new BoolSetting( "DELETE_BACKED_UP_REPLAYS", NODE_REPLAY_BACKUP,
	                                                                               GROUP_REPLAY_BACKUP, NORMAL,
	                                                                               "Delete successfully backed up replays from original folder", null,
	                                                                               Boolean.FALSE );
	
	// LAST GAME INFO SETTINGS
	
	/** Last Game Info settings. */
	NodeSetting                       NODE_LAST_GAME_INFO                  = new NodeSetting( "LAST_GAME_INFO", NODE_REPLAY_FOLDER_MONITOR, "Last Game Info",
	                                                                               Icons.F_INFORMATION_BALLOON );
	
	/** Last Game Info Overlay group. */
	SettingsGroup                     GROUP_LAST_GAME_INFO_OVERLAY         = new SettingsGroup( "Last Game Info Overlay", Helps.LAST_GAME_INFO_OVERLAY );
	
	/** Last Game Info Overlay center X position. */
	IntSetting                        LGI_OVERLAY_CENTER_X                 = new IntSetting( "LGI_OVERLAY_CENTER_X", NODE_LAST_GAME_INFO,
	                                                                               GROUP_LAST_GAME_INFO_OVERLAY, NORMAL, "Overlay center X position", null,
	                                                                               200, -9999, 9999 );
	
	/** Last Game Info Overlay center Y position. */
	IntSetting                        LGI_OVERLAY_CENTER_Y                 = new IntSetting( "LGI_OVERLAY_CENTER_Y", NODE_LAST_GAME_INFO,
	                                                                               GROUP_LAST_GAME_INFO_OVERLAY, NORMAL, "Overlay center Y position", null,
	                                                                               500, -9999, 9999 );
	
	/** Last Game Info Overlay opacity. */
	IntSetting                        LGI_OVERLAY_OPACITY                  = new IntSetting( "LGI_OVERLAY_OPACITY", NODE_LAST_GAME_INFO,
	                                                                               GROUP_LAST_GAME_INFO_OVERLAY, BASIC, "Overlay opacity", VHB.sstext_( "%" ),
	                                                                               90, 20, 100 );
	
	/** Last Game Info Overlay locked. */
	BoolSetting                       LGI_OVERLAY_LOCKED                   = new BoolSetting( "LGI_OVERLAY_LOCKED", NODE_LAST_GAME_INFO,
	                                                                               GROUP_LAST_GAME_INFO_OVERLAY, BASIC, "Overlay locked", null, Boolean.FALSE );
	
	/** Last Game Info Overlay focusable. */
	BoolSetting                       LGI_OVERLAY_FOCUSABLE                = new BoolSetting( "LGI_OVERLAY_FOCUSABLE", NODE_LAST_GAME_INFO,
	                                                                               GROUP_LAST_GAME_INFO_OVERLAY, BASIC, "Overlay focusable", null,
	                                                                               Boolean.FALSE );
	
	/** Last Game Info Overlay allow positioning outside the main screen. */
	BoolSetting                       LGI_OVERLAY_ALLOW_OUT_MAIN_SCR       = new BoolSetting( "LGI_OVERLAY_ALLOW_OUT_MAIN_SCR", NODE_LAST_GAME_INFO,
	                                                                               GROUP_LAST_GAME_INFO_OVERLAY, BASIC,
	                                                                               "Allow positioning outside the main screen", null, Boolean.FALSE );
	
	
	
	// REPLAY FOLDERS SETTINGS
	
	/** Replay Folders settings. */
	NodeSetting                       NODE_REP_FOLDERS                     = new NodeSetting( "REP_FOLDERS", null, "Replay Folders", Icons.F_BLUE_FOLDERS_STACK );
	
	/** Replay Folders group. */
	SettingsGroup                     GROUP_REP_FOLDERS                    = new SettingsGroup( "Replay Folders" );
	
	/** Show replays count column. */
	BoolSetting                       SHOW_REPLAYS_COUNT                   = new BoolSetting( "SHOW_REPLAYS_COUNT", NODE_REP_FOLDERS, GROUP_REP_FOLDERS,
	                                                                               NORMAL, "Show Replays count", null, Boolean.TRUE );
	
	/** Refresh replays count on startup. */
	BoolSetting                       REFR_REPS_COUNT_ON_START             = new BoolSetting( "REFR_REPS_COUNT_ON_START", NODE_REP_FOLDERS, GROUP_REP_FOLDERS,
	                                                                               NORMAL, "Refresh replays count on startup", null, Boolean.TRUE );
	
	/** Replay folders. */
	BeanSetting< RepFoldersBean >     REPLAY_FOLDERS_BEAN                  = new BeanSetting<>( "REPLAY_FOLDERS_BEAN", NODE_REP_FOLDERS, null, HIDDEN,
	                                                                               "Replay Folders", null, RepFoldersBean.getDefaults() );
	
	/** Replay List settings. */
	NodeSetting                       NODE_REP_LIST                        = new NodeSetting( "REP_LIST", NODE_REP_FOLDERS, "Replay List",
	                                                                               Icons.F_BLUE_FOLDER_OPEN_TABLE );
	
	/** Replay List group. */
	SettingsGroup                     GROUP_REP_LIST                       = new SettingsGroup( "Replay List" );
	
	/** Show time during search. */
	BoolSetting                       REP_LIST_SHOW_TIME_DURING_SEARCH     = new BoolSetting( "REP_LIST_SHOW_TIME_DURING_SEARCH", NODE_REP_LIST,
	                                                                               GROUP_REP_LIST, NORMAL,
	                                                                               "Show elapsed and remaining time (ETA) during search", null, Boolean.TRUE );
	
	/** Color table based on match result of favored players. */
	BoolSetting                       REP_LIST_COLOR_TABLE                 = new BoolSetting( "REP_LIST_COLOR_TABLE", NODE_REP_LIST, GROUP_REP_LIST, BASIC,
	                                                                               "Color table based on match result of favored players",
	                                                                               VHB.help_( Helps.COLOR_REP_LIST_TABLE ), Boolean.TRUE );
	
	/** Replay List Stretch table to window. */
	BoolSetting                       REP_LIST_STRETCH_TABLE               = new BoolSetting( "REP_LIST_STRETCH_TABLE", NODE_REP_LIST, GROUP_REP_LIST, BASIC,
	                                                                               "Stretch table to window (no horizontal scroll bar)", null, Boolean.FALSE );
	
	/** Replay list table fixed row height. */
	IntSetting                        REP_LIST_ROW_HEIGHT                  = new IntSetting( "REP_LIST_ROW_HEIGHT", NODE_REP_LIST, GROUP_REP_LIST, NORMAL,
	                                                                               "Fixed table row height", VHB.sstext_( "pixel" ), 40, 16, 96 );
	
	/** Map image zoom in the table. */
	IntSetting                        REP_LIST_MAP_IMAGE_ZOOM              = new IntSetting( "REP_LIST_MAP_IMAGE_ZOOM", NODE_REP_LIST, GROUP_REP_LIST, NORMAL,
	                                                                               "Map image zoom in the table", new VHB().sstext( "%" )
	                                                                                       .help( Helps.MAP_IMAGE_ZOOM ).build(), 200, 100, 900 );
	
	/** Max number of replays to open at once in the Replay Analyzer. */
	IntSetting                        MAX_REPS_TO_OPEN_TOGETHER            = new IntSetting( "MAX_REPS_TO_OPEN_TOGETHER", NODE_REP_LIST, GROUP_REP_LIST,
	                                                                               NORMAL, "Max reps to open in the Replay Analyzer",
	                                                                               VHB.help_( Helps.MAX_REPS_TO_OPEN_TOGETHER ), 8, 1, 20 );
	
	/** Rename replays group. */
	SettingsGroup                     GROUP_RENAME_REPS                    = new SettingsGroup( "Rename Replays" );
	
	/** Rename replays template. */
	TemplateSetting                   RENAME_REPS_TEMPLATE                 = new TemplateSetting( "RENAME_REPS_TEMPLATE", NODE_REP_LIST, GROUP_RENAME_REPS,
	                                                                               BASIC, "Name template for renaming replays",
	                                                                               VHB.dtitle_( "Rename Replays Name Template" ),
	                                                                               "[dateTimeShort] [O][matchup][C] [map]" );
	
	/** File operations group. */
	SettingsGroup                     GROUP_FILE_OPS                       = new SettingsGroup( "File Operations" );
	
	/** Target folder. */
	PathSetting                       TARGET_FOLDER                        = new PathSetting( "TARGET_FOLDER", NODE_REP_LIST, GROUP_FILE_OPS,
	                                                                               BASIC, "Target Folder",
	                                                                               VHB.dtitle_( "Choose your Target Folder" ), Paths.get("") );
	
	/** Preserve subfolders relative to source Replay Folder. */
	BoolSetting                       PRESERVE_SUBFOLDERS                  = new BoolSetting( "PRESERVE_SUBFOLDERS", NODE_REP_LIST, GROUP_FILE_OPS, BASIC,
	                                                                               "Preserve subfolders relative to source Repay Folder", null, Boolean.FALSE );
	
	/** Pack output file name. */
	StringSetting                     PACK_OUTPUT_FILE_NAME                = new StringSetting( "PACK_OUTPUT_FILE_NAME", NODE_REP_LIST,
																				   GROUP_FILE_OPS, BASIC, "Pack Output File Name", null,
                                                                                   "replay-pack.zip" );

	/** Replay list columns. */
	BeanSetting< RepListColumnsBean > REP_LIST_COLUMNS_BEAN                = new BeanSetting<>( "REP_LIST_COLUMNS_BEAN", NODE_REP_LIST, null, HIDDEN,
	                                                                               "Replay List Columns", null, RepListColumnsBean.getDefaults() );
	
	/** Custom Replay List Columns settings. */
	NodeSetting                       NODE_REP_LIST_CUSTOM_COLS            = new NodeSetting( "REP_LIST_CUSTOM_COLS", NODE_REP_LIST, "Custom Columns",
	                                                                               Icons.F_TABLE_SELECT_COLUMN );
	
	/** Replay List Table Custom Columns group. */
	SettingsGroup                     GROUP_REP_LIST_CUSTOM_COLS           = new SettingsGroup( "Replay List Table Custom Columns",
	                                                                               Helps.REPLAY_LIST_TABLE_CUSTOM_COLS );
	
	/** Name for Custom column #1. */
	StringSetting                     REP_LIST_CUST_COL_1_NAME             = new StringSetting( "REP_LIST_CUST_COL_1_NAME", NODE_REP_LIST_CUSTOM_COLS,
	                                                                               GROUP_REP_LIST_CUSTOM_COLS, NORMAL, "Name for Custom column #1", null,
	                                                                               "Custom #1" );
	
	/** Template for the Custom column #1. */
	TemplateSetting                   REP_LIST_CUST_COL_1_TEMPLATE         = new TemplateSetting( "REP_LIST_CUST_COL_1_TEMPLATE", NODE_REP_LIST_CUSTOM_COLS,
	                                                                               GROUP_REP_LIST_CUSTOM_COLS, NORMAL, "Template for Custom column #1",
	                                                                               VHB.dtitle_( "Custom Column #1 Name Template" ), "" );
	
	/** Name for Custom column #2. */
	StringSetting                     REP_LIST_CUST_COL_2_NAME             = new StringSetting( "REP_LIST_CUST_COL_2_NAME", NODE_REP_LIST_CUSTOM_COLS,
	                                                                               GROUP_REP_LIST_CUSTOM_COLS, NORMAL, "Name for Custom column #2", null,
	                                                                               "Custom #2" );
	
	/** Template for the Custom column #2. */
	TemplateSetting                   REP_LIST_CUST_COL_2_TEMPLATE         = new TemplateSetting( "REP_LIST_CUST_COL_2_TEMPLATE", NODE_REP_LIST_CUSTOM_COLS,
	                                                                               GROUP_REP_LIST_CUSTOM_COLS, NORMAL, "Template for Custom column #2",
	                                                                               VHB.dtitle_( "Custom Column #2 Name Template" ), "" );
	
	/** Name for Custom column #3. */
	StringSetting                     REP_LIST_CUST_COL_3_NAME             = new StringSetting( "REP_LIST_CUST_COL_3_NAME", NODE_REP_LIST_CUSTOM_COLS,
	                                                                               GROUP_REP_LIST_CUSTOM_COLS, NORMAL, "Name for Custom column #3", null,
	                                                                               "Custom #3" );
	
	/** Template for the Custom column #3. */
	TemplateSetting                   REP_LIST_CUST_COL_3_TEMPLATE         = new TemplateSetting( "REP_LIST_CUST_COL_3_TEMPLATE", NODE_REP_LIST_CUSTOM_COLS,
	                                                                               GROUP_REP_LIST_CUSTOM_COLS, NORMAL, "Template for Custom column #3",
	                                                                               VHB.dtitle_( "Custom Column #3 Name Template" ), "" );
	
	/** Name for Custom column #4. */
	StringSetting                     REP_LIST_CUST_COL_4_NAME             = new StringSetting( "REP_LIST_CUST_COL_4_NAME", NODE_REP_LIST_CUSTOM_COLS,
	                                                                               GROUP_REP_LIST_CUSTOM_COLS, NORMAL, "Name for Custom column #4", null,
	                                                                               "Custom #4" );
	
	/** Template for the Custom column #4. */
	TemplateSetting                   REP_LIST_CUST_COL_4_TEMPLATE         = new TemplateSetting( "REP_LIST_CUST_COL_4_TEMPLATE", NODE_REP_LIST_CUSTOM_COLS,
	                                                                               GROUP_REP_LIST_CUSTOM_COLS, NORMAL, "Template for Custom column #4",
	                                                                               VHB.dtitle_( "Custom Column #4 Name Template" ), "" );
	
	/** Name for Custom column #5. */
	StringSetting                     REP_LIST_CUST_COL_5_NAME             = new StringSetting( "REP_LIST_CUST_COL_5_NAME", NODE_REP_LIST_CUSTOM_COLS,
	                                                                               GROUP_REP_LIST_CUSTOM_COLS, NORMAL, "Name for Custom column #5", null,
	                                                                               "Custom #5" );
	
	/** Template for the Custom column #5. */
	TemplateSetting                   REP_LIST_CUST_COL_5_TEMPLATE         = new TemplateSetting( "REP_LIST_CUST_COL_5_TEMPLATE", NODE_REP_LIST_CUSTOM_COLS,
	                                                                               GROUP_REP_LIST_CUSTOM_COLS, NORMAL, "Template for Custom column #5",
	                                                                               VHB.dtitle_( "Custom Column #5 Name Template" ), "" );
	
	
	
	// REPLAY PROCESSOR SETTINGS
	
	/** Replay processor settings. */
	NodeSetting                       NODE_REP_PROCESSOR                   = new NodeSetting( "REP_PROCESSOR", null, "Replay Processor", Icons.F_COMPILE );
	
	/** Replay Processor group. */
	SettingsGroup                     GROUP_REP_PROCESSOR                  = new SettingsGroup( "Replay Processor" );
	
	/** Use real-time time measurement. */
	BoolSetting                       USE_LATEST_S2PROTOCOL                = new BoolSetting( "USE_LATEST_S2PROTOCOL", NODE_REP_PROCESSOR, GROUP_REP_PROCESSOR, NORMAL,
	                                                                               "Use latest S2Protocol definition for newer, unknown versions", VHB.help_( Helps.USE_LATEST_S2PROTOCOL ),
	                                                                               Boolean.TRUE );
	/** Use real-time time measurement. */
	BoolSetting                       USE_REAL_TIME                        = new BoolSetting( "USE_REAL_TIME", NODE_REP_PROCESSOR, GROUP_REP_PROCESSOR, NORMAL,
	                                                                               "Use real-time time measurement", VHB.help_( Helps.GAME_TIME_REAL_TIME ),
	                                                                               Boolean.TRUE );
	
	/** Initial time to exclude from APM calculation in seconds (game time). */
	IntSetting                        INITIAL_PER_MIN_CALC_EXCL_TIME       = new IntSetting(
	                                                                               "INITIAL_PER_MIN_CALC_EXCL_TIME",
	                                                                               NODE_REP_PROCESSOR,
	                                                                               GROUP_REP_PROCESSOR,
	                                                                               ADVANCED,
	                                                                               "<html>Initial time to exclude from <i>per-minute</i> calculations:<br>(e.g. APM, SPM; specified in game-time)</html>",
	                                                                               new VHB().help( Helps.INITIAL_PER_MIN_CALC_EXCL_TIME ).sstext( "sec" )
	                                                                                       .build(), 110, 0, 999 );
	
	/** Comma separated toons of favored players. */
	ValidatedStringSetting            FAVORED_PLAYER_TOONS                 = new ValidatedStringSetting( "FAVORED_PLAYER_TOONS", NODE_REP_PROCESSOR,
	                                                                               GROUP_REP_PROCESSOR, BASIC, "Toons of Favored Players (comma separated)",
	                                                                               new VHB().help( Helps.FAVORED_PLAYERS )
	                                                                                       .compConfigurer( new ICompConfigurer() {
		                                                                                       @Override
		                                                                                       public void configure( JComponent settingComp,
		                                                                                               ISetting< ? > setting, ISettingsBean settings ) {
			                                                                                       Utils.setToonListValidator( (IndicatorTextField) settingComp );
		                                                                                       }
	                                                                                       } ).build(), "" );
	
	/** Override detected format based on matchup. */
	BoolSetting                       OVERRIDE_FORMAT_BASED_ON_MATCHUP     = new BoolSetting( "OVERRIDE_FORMAT_BASED_ON_MATCHUP", NODE_REP_PROCESSOR,
	                                                                               GROUP_REP_PROCESSOR, NORMAL, "Override detected format based on matchup",
	                                                                               VHB.help_( Helps.OVERRIDE_FORMAT_BASED_ON_MATCHUP ), Boolean.TRUE );
	
	/** In team games declare the largest remaining team winner. */
	BoolSetting                       LARGEST_REMAINING_TEAM_WINS          = new BoolSetting( "LARGEST_REMAINING_TEAM_WINS", NODE_REP_PROCESSOR,
	                                                                               GROUP_REP_PROCESSOR, NORMAL,
	                                                                               "Declare the largest remaining team winner if the Result info is missing",
	                                                                               VHB.help_( Helps.DECLARE_LARGEST_REMAINING_TEAM_WINNER ), Boolean.TRUE );
	
	
	
	// REPLAY ANALYZER SETTINGS
	
	/** Replay analyzer settings. */
	NodeSetting                       NODE_REP_ANALYZER                    = new NodeSetting( "REP_ANALYZER", null, "Replay Analyzer", Icons.F_CHART );
	
	/** Invert colors in tables having user color. */
	BoolSetting                       INVERT_COLORS_IN_USER_TABLES         = new BoolSetting( "INVERT_COLORS_IN_USER_TABLES", NODE_REP_ANALYZER, GROUP_MISC,
	                                                                               NORMAL, "Invert colors in tables having user color", null, Boolean.FALSE );
	
	/** Recent Replays group. */
	SettingsGroup                     GROUP_RECENT_REPLAYS                 = new SettingsGroup( "Recent Replays" );
	
	/** Initial Charts - Events Table partitioning in percent. */
	IntSetting                        RECENT_REPLAYS_COUNT                 = new IntSetting( "RECENT_REPLAYS_COUNT", NODE_REP_ANALYZER, GROUP_RECENT_REPLAYS,
	                                                                               NORMAL, "Number of Recent Replays to remember", null, 15, 0, 50 );
	
	/** Recent replays. */
	BeanSetting< RecentReplaysBean >  RECENT_REPLAYS_BEAN                  = new BeanSetting<>( "RECENT_REPLAYS_BEAN", NODE_REP_ANALYZER, GROUP_RECENT_REPLAYS,
	                                                                               HIDDEN, "Recent Replays", null, new RecentReplaysBean() );
	
	/** Charts settings. */
	NodeSetting                       NODE_CHARTS                          = new NodeSetting( "CHARTS", NODE_REP_ANALYZER, "Charts", Icons.F_CHART );
	
	/** General Chart Options group. */
	SettingsGroup                     GROUP_GENERAL_CHART_OPTIONS          = new SettingsGroup( "General Chart Options" );
	
	/** Hex line size (number of bytes displayed in a line). */
	EnumSetting< ChartType >          CHART_TYPE                           = new EnumSetting<>( "CHART_TYPE", NODE_CHARTS, GROUP_GENERAL_CHART_OPTIONS, BASIC,
	                                                                               "Chart", null, ChartType.APM );
	
	
	/** Display all players / teams on one chart. */
	BoolSetting                       DISPLAY_ALL_ON_ONE_CHART             = new BoolSetting( "DISPLAY_ALL_ON_ONE_CHART", NODE_CHARTS,
	                                                                               GROUP_GENERAL_CHART_OPTIONS, BASIC,
	                                                                               "Display All Players / Teams on one chart", null, Boolean.FALSE );
	
	/** Teams as One. */
	BoolSetting                       TEAMS_AS_ONE                         = new BoolSetting( "TEAMS_AS_ONE", NODE_CHARTS, GROUP_GENERAL_CHART_OPTIONS, BASIC,
	                                                                               "Teams as One (Combine / Merge team members)", null, Boolean.FALSE );
	
	/** Display time in seconds. */
	BoolSetting                       TIME_IN_SECONDS                      = new BoolSetting( "TIME_IN_SECONDS", NODE_CHARTS, GROUP_GENERAL_CHART_OPTIONS,
	                                                                               NORMAL, "Display time values in seconds", null, Boolean.TRUE );
	
	/** Initial Charts - Events Table partitioning in percent. */
	IntSetting                        INITIAL_CHARTS_EVT_TBL_PARTING       = new IntSetting( "INITIAL_CHARTS_EVT_TBL_PARTING", NODE_CHARTS, GROUP_MISC, NORMAL,
	                                                                               "Initial Charts - Events Table partitioning", VHB.sstext_( "%" ), 70, 0, 100 );
	
	/** Charts divider size in pixels. */
	IntSetting                        CHARTS_DIVIDER_SIZE                  = new IntSetting( "CHARTS_DIVIDER_SIZE", NODE_CHARTS, GROUP_MISC, NORMAL,
	                                                                               "Charts - Events Table divider size", VHB.sstext_( "pixel" ), 10, 0, 20 );
	
	/** Show match result. */
	BoolSetting                       SHOW_MATCH_RESULT                    = new BoolSetting( "SHOW_MATCH_RESULT", NODE_CHARTS, GROUP_MISC, NORMAL,
	                                                                               "Show match result", null, Boolean.TRUE );
	
	/** List Observers. */
	BoolSetting                       LIST_OBSERVERS                       = new BoolSetting( "LIST_OBSERVERS", NODE_CHARTS, GROUP_MISC, ADVANCED,
	                                                                               "Also list Observers", null, Boolean.FALSE );
	
	/** Chart-specific settings. */
	NodeSetting                       NODE_CHART_SPECIFIC                  = new NodeSetting( "CHART_SPECIFIC", NODE_CHARTS, "Chart-specific",
	                                                                               Icons.F_CHART_PENCIL );
	
	/** APM chart group. */
	SettingsGroup                     GROUP_APM_CHART                      = new SettingsGroup( "APM Chart" );
	
	/** APM chart presentation. */
	EnumSetting< Presentation >       APM_PRESENTATION                     = new EnumSetting<>( "APM_PRESENTATION", NODE_CHART_SPECIFIC, GROUP_APM_CHART,
	                                                                               BASIC, "Presentation", null, Presentation.CUBIC_GRAPH );
	
	/** APM chart granularity in seconds. */
	IntSetting                        APM_GRANULARITY                      = new IntSetting( "APM_GRANULARITY", NODE_CHART_SPECIFIC, GROUP_APM_CHART, NORMAL,
	                                                                               "Granularity", VHB.sstext_( "sec" ), 10, 1, 99 );
	
	/** SPM chart group. */
	SettingsGroup                     GROUP_SPM_CHART                      = new SettingsGroup( "SPM Chart" );
	
	/** SPM chart presentation. */
	EnumSetting< Presentation >       SPM_PRESENTATION                     = new EnumSetting<>( "SPM_PRESENTATION", NODE_CHART_SPECIFIC, GROUP_SPM_CHART,
	                                                                               BASIC, "Presentation", null, Presentation.CUBIC_GRAPH );
	
	/** SPM chart granularity in seconds. */
	IntSetting                        SPM_GRANULARITY                      = new IntSetting( "SPM_GRANULARITY", NODE_CHART_SPECIFIC, GROUP_SPM_CHART, NORMAL,
	                                                                               "Granularity", VHB.sstext_( "sec" ), 10, 1, 99 );
	
	/** Control Groups chart group. */
	SettingsGroup                     GROUP_CONTROL_GROUPS_CHART           = new SettingsGroup( "Control Groups Chart" );
	
	/** Control Groups chart show select groups. */
	BoolSetting                       CONTROL_GROUPS_SHOW_SELECT           = new BoolSetting( "HOTKEYS_SHOW_SELECT", NODE_CHART_SPECIFIC,
	                                                                               GROUP_CONTROL_GROUPS_CHART, BASIC, "Show Select groups", null, Boolean.TRUE );
	
	/** Player Statistics group. */
	SettingsGroup                     GROUP_PLAYER_STATS                   = new SettingsGroup( "Player Statistics Charts" );
	
	/** Player stats stat. */
	EnumSetting< Stat >               PLAYER_STATS_STAT                    = new EnumSetting<>( "PLAYER_STATS_STAT", NODE_CHART_SPECIFIC, GROUP_PLAYER_STATS,
	                                                                               BASIC, "Stat", null, Stat.RES_CURRENT );
	
	/** Player stats presentation. */
	EnumSetting< Presentation >       PLAYER_STATS_PRESENTATION            = new EnumSetting<>( "PLAYER_STATS_PRESENTATION", NODE_CHART_SPECIFIC,
	                                                                               GROUP_PLAYER_STATS, BASIC, "Presentation", null, Presentation.FILLED_BARS );
	
	/** Player Stats display / include minerals. */
	BoolSetting                       PLAYER_STATS_INCLUDE_MINS            = new BoolSetting( "PLAYER_STATS_INCLUDE_MINS", NODE_CHART_SPECIFIC,
	                                                                               GROUP_PLAYER_STATS, BASIC, "Display / Include Minerals", null, Boolean.TRUE );
	
	/** Player Stats display / include vespene. */
	BoolSetting                       PLAYER_STATS_INCLUDE_GAS             = new BoolSetting( "PLAYER_STATS_INCLUDE_GAS", NODE_CHART_SPECIFIC,
	                                                                               GROUP_PLAYER_STATS, BASIC, "Display / Include Vespene", null, Boolean.TRUE );
	
	/** Player Stats separate minerals and vespene. */
	BoolSetting                       PLAYER_STATS_SEPARATE_MINS_GAS       = new BoolSetting( "PLAYER_STATS_SEPARATE_MINS_GAS", NODE_CHART_SPECIFIC,
	                                                                               GROUP_PLAYER_STATS, BASIC, "Separate graphs for Minerals and Vespene", null,
	                                                                               Boolean.FALSE );
	
	/** Commands chart group. */
	SettingsGroup                     GROUP_COMMANDS_CHART                 = new SettingsGroup( "Commands Chart" );
	
	/** Commands chart show builds. */
	BoolSetting                       COMMANDS_SHOW_BUILDS                 = new BoolSetting( "COMMANDS_SHOW_BUILDS", NODE_CHART_SPECIFIC,
	                                                                               GROUP_COMMANDS_CHART, BASIC, "Show Builds", null, Boolean.TRUE );
	
	/** Commands chart show trains. */
	BoolSetting                       COMMANDS_SHOW_TRAINS                 = new BoolSetting( "COMMANDS_SHOW_TRAINS", NODE_CHART_SPECIFIC,
	                                                                               GROUP_COMMANDS_CHART, BASIC, "Show Trains", null, Boolean.FALSE );
	
	/** Commands chart show workers. */
	BoolSetting                       COMMANDS_SHOW_WORKERS                = new BoolSetting( "COMMANDS_SHOW_WORKERS", NODE_CHART_SPECIFIC,
	                                                                               GROUP_COMMANDS_CHART, BASIC, "Show Workers", null, Boolean.FALSE );
	
	/** Commands chart show upgrades. */
	BoolSetting                       COMMANDS_SHOW_UPGRADES               = new BoolSetting( "COMMANDS_SHOW_UPGRADES", NODE_CHART_SPECIFIC,
	                                                                               GROUP_COMMANDS_CHART, BASIC, "Show Upgrades", null, Boolean.TRUE );
	
	/** Commands chart show others (essential). */
	BoolSetting                       COMMANDS_SHOW_OTHERS_ESSENTIAL       = new BoolSetting( "COMMANDS_SHOW_OTHERS_ESSENTIAL", NODE_CHART_SPECIFIC,
	                                                                               GROUP_COMMANDS_CHART, BASIC, "Show Others (Essential)", null, Boolean.FALSE );
	
	/** Commands chart show others (rest). */
	BoolSetting                       COMMANDS_SHOW_OTHERS_REST            = new BoolSetting( "COMMANDS_SHOW_OTHERS_REST", NODE_CHART_SPECIFIC,
	                                                                               GROUP_COMMANDS_CHART, BASIC, "Show Others (Rest)", null, Boolean.FALSE );
	
	/** Commands chart use icons. */
	BoolSetting                       COMMANDS_USE_ICONS                   = new BoolSetting( "COMMANDS_USE_ICONS", NODE_CHART_SPECIFIC, GROUP_COMMANDS_CHART,
	                                                                               BASIC, "Use Icons", null, Boolean.TRUE );
	
	/** Commands chart icon size. */
	IntSetting                        COMMANDS_ICON_SIZE                   = new IntSetting( "COMMANDS_ICON_SIZE", NODE_CHART_SPECIFIC, GROUP_COMMANDS_CHART,
	                                                                               BASIC, "Icon size", VHB.sstext_( "pixel" ), 24, 10, 76 );
	
	/** Commands chart duration visualization type. */
	EnumSetting< DurationType >       COMMANDS_DURATION_TYPE               = new EnumSetting<>( "COMMANDS_DURATION_TYPE", NODE_CHART_SPECIFIC,
	                                                                               GROUP_COMMANDS_CHART, BASIC, "Duration visualization", null,
	                                                                               DurationType.BARS );
	
	/** Base Control chart group. */
	SettingsGroup                     GROUP_BASE_CONTROL_CHART             = new SettingsGroup( "Base Control" );
	
	/** Base control chart show icons. */
	BoolSetting                       BASE_CONTROL_SHOW_ICONS              = new BoolSetting( "BASE_CONTROL_SHOW_ICONS", NODE_CHART_SPECIFIC,
	                                                                               GROUP_BASE_CONTROL_CHART, BASIC, "Show Icons", null, Boolean.TRUE );
	
	/** Events table settings. */
	NodeSetting                       NODE_EVENTS_TABLE                    = new NodeSetting( "EVENTS_TABLE", NODE_CHARTS, "Events Table", Icons.F_TABLE );
	
	/** Events Table group. */
	SettingsGroup                     GROUP_EVENTS_TABLE                   = new SettingsGroup( "Events Table" );
	
	/** Events table fixed row height. */
	IntSetting                        EVENTS_TABLE_ROW_HEIGHT              = new IntSetting( "EVENTS_TABLE_ROW_HEIGHT", NODE_EVENTS_TABLE, GROUP_EVENTS_TABLE,
	                                                                               NORMAL, "Fixed table row height", VHB.sstext_( "pixel" ), 24, 16, 76 );
	
	/** Stretch Events table to window. */
	BoolSetting                       EVENTS_STRETCH_TABLE                 = new BoolSetting( "EVENTS_STRETCH_TABLE", NODE_EVENTS_TABLE, GROUP_EVENTS_TABLE,
	                                                                               NORMAL, "Stretch table to window (no horizontal scroll bar)", null,
	                                                                               Boolean.TRUE );
	
	/** Unit tag display transformation. */
	EnumSetting< TagTransformation >  UNIT_TAG_TRANSFORMATION              = new EnumSetting<>( "UNIT_TAG_TRANSFORMATION", NODE_EVENTS_TABLE,
	                                                                               GROUP_EVENTS_TABLE, ADVANCED, "Unit tag display transformation",
	                                                                               VHB.help_( Helps.UNIT_TAG_DISPLAY_TRANSFORMATION ),
	                                                                               TagTransformation.SHUFFLED );
	
	/** Show Raw Parameters column in the events table. */
	BoolSetting                       SHOW_RAW_PARAMETERS                  = new BoolSetting( "SHOW_RAW_PARAMETERS", NODE_EVENTS_TABLE, GROUP_EVENTS_TABLE,
	                                                                               DEVELOPER, "Show Raw Parameters column", null, Boolean.FALSE );
	
	/** Displayed events group. */
	SettingsGroup                     GROUP_DISPLAYED_EVENTS               = new SettingsGroup( "Displayed events" );
	
	/** Show Cmd game events. */
	BoolSetting                       SHOW_CMD_EVENTS                      = new BoolSetting( "SHOW_CMD_EVENTS", NODE_EVENTS_TABLE, GROUP_DISPLAYED_EVENTS,
	                                                                               NORMAL, "Show Cmd events", null, Boolean.TRUE );
	
	/** Show SelectionDelta game events. */
	BoolSetting                       SHOW_SELECTION_DELTA_EVENTS          = new BoolSetting( "SHOW_SELECTION_DELTA_EVENTS", NODE_EVENTS_TABLE,
	                                                                               GROUP_DISPLAYED_EVENTS, NORMAL, "Show SelectionDelta events", null,
	                                                                               Boolean.TRUE );
	
	/** Show ControlGroupUpdate game events. */
	BoolSetting                       SHOW_CTRL_GROUP_UPDATE_EVENTS        = new BoolSetting( "SHOW_CTRL_GROUP_UPDATE_EVENTS", NODE_EVENTS_TABLE,
	                                                                               GROUP_DISPLAYED_EVENTS, NORMAL, "Show ControlGroupUpdate events", null,
	                                                                               Boolean.TRUE );
	
	/** Show CameraUpdate game events. */
	BoolSetting                       SHOW_CAMERA_UPDATE_EVENTS            = new BoolSetting( "SHOW_CAMERA_UPDATE_EVENTS", NODE_EVENTS_TABLE,
	                                                                               GROUP_DISPLAYED_EVENTS, NORMAL, "Show CameraUpdate events", null,
	                                                                               Boolean.FALSE );
	
	/** Show Other - Essentials game events. */
	BoolSetting                       SHOW_OTHER_ESSENTIALS_EVENTS         = new BoolSetting( "SHOW_OTHER_ESSENTIALS_EVENTS", NODE_EVENTS_TABLE,
	                                                                               GROUP_DISPLAYED_EVENTS, NORMAL, "Show Other (Essentials) events", null,
	                                                                               Boolean.TRUE );
	
	/** Show Other - Rest game events. */
	BoolSetting                       SHOW_OTHER_REST_EVENTS               = new BoolSetting( "SHOW_OTHER_REST_EVENTS", NODE_EVENTS_TABLE,
	                                                                               GROUP_DISPLAYED_EVENTS, ADVANCED, "Show Other (Rest) events", null,
	                                                                               Boolean.FALSE );
	
	/** Build Orders settings. */
	NodeSetting                       NODE_BUILD_ORDERS                    = new NodeSetting( "BUILD_ORDERS", NODE_REP_ANALYZER, "Build Orders", Icons.F_BLOCK );
	
	/** Build Orders group. */
	SettingsGroup                     GROUP_BUILD_ORDERS                   = new SettingsGroup( "Build Orders" );
	
	/** Build Orders table fixed row height. */
	IntSetting                        BUILD_ORDERS_ROW_HEIGHT              = new IntSetting( "BUILD_ORDERS_ROW_HEIGHT", NODE_BUILD_ORDERS, GROUP_BUILD_ORDERS,
	                                                                               NORMAL, "Fixed table row height", VHB.sstext_( "pixel" ), 32, 16, 76 );
	
	/** Map Info settings. */
	NodeSetting                       NODE_MAP_INFO                        = new NodeSetting( "MAP_INFO", NODE_REP_ANALYZER, "Map Info", Icons.F_MAP );
	
	/** Map Info group. */
	SettingsGroup                     GROUP_MAP_INFO                       = new SettingsGroup( "Map Info" );
	
	/** Map info image zoom. */
	EnumSetting< Zoom >               MAP_INFO_IMAGE_ZOOM                  = new EnumSetting<>( "MAP_INFO_IMAGE_ZOOM", NODE_MAP_INFO, GROUP_MAP_INFO, BASIC,
	                                                                               "Map image zoom", null, Zoom.FIT_TO_WINDOW );
	
	/** Map info show player names. */
	BoolSetting                       MAP_INFO_SHOW_PLAYER_NAMES           = new BoolSetting( "MAP_INFO_SHOW_PLAYER_NAMES", NODE_MAP_INFO, GROUP_MAP_INFO,
	                                                                               BASIC, "Show Player names", null, Boolean.TRUE );
	
	/** Map info canvas font size. */
	IntSetting                        MAP_INFO_CANVAS_FONT_SIZE            = new IntSetting( "MAP_INFO_CANVAS_FONT_SIZE", NODE_MAP_INFO, GROUP_MAP_INFO,
	                                                                               ADVANCED, "Canvas font size", null, 5, 2, 7 );
	
	/** Preferred map locale. */
	StringSetting                     MAP_LOCALE                           = new StringSetting( "MAP_LOCALE", NODE_MAP_INFO, GROUP_MAP_INFO, NORMAL,
	                                                                               "Preferred Map Locale", null, "en-US" );
	
	/** Chat settings. */
	NodeSetting                       NODE_CHAT                            = new NodeSetting( "CHAT", NODE_REP_ANALYZER, "Chat", Icons.F_BALLOONS );
	
	/** Chat presentation group. */
	SettingsGroup                     GROUP_CHAT_PRESENTATION              = new SettingsGroup( "Chat Presentation" );
	
	/** Table view (else text view). */
	BoolSetting                       CHAT_TABLE_VIEW                      = new BoolSetting( "CHAT_TABLE_VIEW", NODE_CHAT, GROUP_CHAT_PRESENTATION, BASIC,
	                                                                               "Table View", null, Boolean.TRUE );
	
	/** Chat Formatting group. */
	SettingsGroup                     GROUP_CHAT_FORMATTING                = new SettingsGroup( "Chat Formatting" );
	
	/** Show message recipient of chat. */
	BoolSetting                       SHOW_MESSAGE_RECIPIENT               = new BoolSetting( "SHOW_MESSAGE_RECIPIENT", NODE_CHAT, GROUP_CHAT_FORMATTING,
	                                                                               BASIC, "Show message recipient", null, Boolean.TRUE );
	
	/** Format chat text into paragraphs. */
	BoolSetting                       FORMAT_INTO_PARAGRAPHS               = new BoolSetting( "FORMAT_INTO_PARAGRAPHS", NODE_CHAT, GROUP_CHAT_FORMATTING,
	                                                                               NORMAL, "Format into pharagraphs", null, Boolean.TRUE );
	
	/** Paragraph break time limit. */
	IntSetting                        PARAGRAPH_BREAK_TIME_LIMIT           = new IntSetting( "PARAGRAPH_BREAK_TIME_LIMIT", NODE_CHAT, GROUP_CHAT_FORMATTING,
	                                                                               NORMAL, "Paragraph break time limit", VHB.sstext_( "sec" ), 6, 0, 60 );
	
	/** Show Minimap Pings. */
	BoolSetting                       SHOW_MINIMAP_PINGS                   = new BoolSetting( "SHOW_MINIMAP_PINGS", NODE_CHAT, GROUP_MISC, BASIC,
	                                                                               "Show Minimap Pings", null, Boolean.TRUE );
	
	/** Inspector settings. */
	NodeSetting                       NODE_INSPECTOR                       = new NodeSetting( "INSPECTOR", NODE_REP_ANALYZER, "Inspector",
	                                                                               Icons.F_USER_DETECTIVE );
	
	/** Raw Text Data group. */
	SettingsGroup                     GROUP_RAW_TEXT_DATA                  = new SettingsGroup( "Raw Text Data" );
	
	/** Show line numbers in the Raw Text Data in the Inspector tab. */
	BoolSetting                       RAW_TEXT_SHOW_LINE_NUMBER            = new BoolSetting( "RAW_TEXT_SHOW_LINE_NUMBER", NODE_INSPECTOR, GROUP_RAW_TEXT_DATA,
	                                                                               DEVELOPER, "Show line numbers in Raw Text Data", null, Boolean.TRUE );
	
	/** Raw Data Tree group. */
	SettingsGroup                     GROUP_RAW_DATA_TREE                  = new SettingsGroup( "Raw Data Tree" );
	
	/** Tab size in the Raw Text Data in the Inspector tab. */
	IntSetting                        RAW_TREE_EXPAND_DEPTH                = new IntSetting( "RAW_TREE_EXPAND_DEPTH", NODE_INSPECTOR, GROUP_RAW_DATA_TREE,
	                                                                               DEVELOPER, "Raw Data Tree Expand depth",
	                                                                               VHB.help_( Helps.RAW_DATA_TREE_EXPAND_DEPTH ), 0, 0, 16 );
	
	/** Tab size in the Raw Text Data in the Inspector tab. */
	IntSetting                        RAW_TEXT_TAB_SIZE                    = new IntSetting( "RAW_TEXT_TAB_SIZE", NODE_INSPECTOR, GROUP_RAW_DATA_TREE,
	                                                                               DEVELOPER, "Tab size in Raw Text Data", VHB.sstext_( "spaces" ), 4, 0, 16 );
	
	/** Binary Data group. */
	SettingsGroup                     GROUP_BINARY_DATA                    = new SettingsGroup( "Binary Data" );
	
	/** Hex line size (number of bytes displayed in a line). */
	EnumSetting< HexLineSize >        HEX_LINE_SIZE                        = new EnumSetting<>( "HEX_LINE_SIZE", NODE_INSPECTOR, GROUP_BINARY_DATA, DEVELOPER,
	                                                                               "Hex Line Size", VHB.sstext_( "Bytes" ), HexLineSize.THIRTY_TWO );
	
	
	
	// MULTI-REPLAY ANALYZER SETTINGS
	
	/** Multi-Replay Analyzer settings. */
	NodeSetting                       NODE_MULTI_REP_ANALYZER              = new NodeSetting( "NODE_MULTI_REP_ANALYZER", null, "Multi-Replay Analyzer",
	                                                                               Icons.F_CHART_UP_COLOR );
	
	/** Multi-Replay Analyzer group. */
	SettingsGroup                     GROUP_MULTI_REP_ANALYZER             = new SettingsGroup( "Multi-Replay Analyzer" );
	
	/** Time limit to be included in the Multi-Replay analysis. */
	IntSetting                        MULTI_REP_TIME_LIMIT                 = new IntSetting( "MULTI_REP_TIME_LIMIT", NODE_MULTI_REP_ANALYZER,
	                                                                               GROUP_MULTI_REP_ANALYZER, NORMAL,
	                                                                               "Time limit to be included in Multi-Replay analysis (game-time)",
	                                                                               VHB.sstext_( "sec" ), 60, 0, 999 );
	
	/** Multi-Replay Analyzer Stretch tables to window. */
	BoolSetting                       MULTI_REP_STRETCH_TABLES             = new BoolSetting( "MULTI_REP_STRETCH_TABLES", NODE_MULTI_REP_ANALYZER,
	                                                                               GROUP_MULTI_REP_ANALYZER, BASIC,
	                                                                               "Stretch tables to window (no horizontal scroll bar)", null, Boolean.TRUE );
	
	/** Multi-Replay Analyzer auto-open first player. */
	BoolSetting                       MULTI_REP_AUTO_OPEN_FIRST_PLAYER     = new BoolSetting(
	                                                                               "MULTI_REP_AUTO_OPEN_FIRST_PLAYER",
	                                                                               NODE_MULTI_REP_ANALYZER,
	                                                                               GROUP_MULTI_REP_ANALYZER,
	                                                                               BASIC,
	                                                                               "Automatically open the details of the first player (with the most plays) when analysis is done",
	                                                                               null, Boolean.FALSE );
	
	/** Max number of stats rows to open at once in Replay Lists in in other Multi-Replay Analyzers. */
	IntSetting                        MULTI_REP_MAX_STATS_TO_OPEN_TOGETHER = new IntSetting( "MULTI_REP_MAX_STATS_TO_OPEN_TOGETHER", NODE_MULTI_REP_ANALYZER,
	                                                                               GROUP_MULTI_REP_ANALYZER, NORMAL,
	                                                                               "Max Stats rows to open in Replay Lists or in other Multi-Replay Analyzers",
	                                                                               VHB.help_( Helps.MAX_STATS_ROWS_TO_OPEN_TOGETHER ), 10, 1, 30 );
	
	/** Multi-Replay Analyzer max playing session break. */
	IntSetting                        MULTI_REP_MAX_SESSION_BREAK          = new IntSetting( "MULTI_REP_MAX_SESSION_BREAK", NODE_MULTI_REP_ANALYZER,
	                                                                               GROUP_MULTI_REP_ANALYZER, NORMAL,
	                                                                               "Max Playing Session Break (max break between plays of a Session)",
	                                                                               new VHB().sstext( "min" ).help( Helps.MAX_SESSION_BREAK ).build(), 60, 1,
	                                                                               999 );
	
	/** Multi-Replay Analyzer Maps group. */
	SettingsGroup                     GROUP_MULTI_REP_ANALYZER_MAPS        = new SettingsGroup( "Multi-Replay Analyzer Maps" );
	
	/** Multi-Replay Analyzer Maps table fixed row height. */
	IntSetting                        MULTI_REP_MAPS_ROW_HEIGHT            = new IntSetting( "MULTI_REP_MAPS_ROW_HEIGHT", NODE_MULTI_REP_ANALYZER,
	                                                                               GROUP_MULTI_REP_ANALYZER_MAPS, NORMAL, "Fixed table row height",
	                                                                               VHB.sstext_( "pixel" ), 40, 19, 96 );
	
	/** Multi-Replay Analyzer Maps Map image zoom in the table. */
	IntSetting                        MULTI_REP_MAPS_IMAGE_ZOOM            = new IntSetting( "MULTI_REP_MAPS_IMAGE_ZOOM", NODE_MULTI_REP_ANALYZER,
	                                                                               GROUP_MULTI_REP_ANALYZER_MAPS, NORMAL, "Map image zoom in the table",
	                                                                               new VHB().sstext( "%" ).help( Helps.MAP_IMAGE_ZOOM ).build(), 200, 100, 900 );
	
	/** Multi-Replay Analyzer Granularities group. */
	SettingsGroup                     GROUP_MULTI_REP_ANAL_GRANULARITIES   = new SettingsGroup( "Multi-Replay Analyzer Granularities" );
	
	/** Multi-Replay Analyzer Length granularity. */
	IntSetting                        MULTI_REP_LENGTH_GRANULARITY         = new IntSetting( "MULTI_REP_LENGTH_GRANULARITY", NODE_MULTI_REP_ANALYZER,
	                                                                               GROUP_MULTI_REP_ANAL_GRANULARITIES, NORMAL, "Length granularity",
	                                                                               VHB.sstext_( "min" ), 5, 1, 99 );
	
	/** Multi-Replay Analyzer APM granularity. */
	IntSetting                        MULTI_REP_APM_GRANULARITY            = new IntSetting( "MULTI_REP_APM_GRANULARITY", NODE_MULTI_REP_ANALYZER,
	                                                                               GROUP_MULTI_REP_ANAL_GRANULARITIES, NORMAL, "APM granularity", null, 10, 1,
	                                                                               99 );
	
	/** Multi-Replay Analyzer SPM granularity. */
	IntSetting                        MULTI_REP_SPM_GRANULARITY            = new IntSetting( "MULTI_REP_SPM_GRANULARITY", NODE_MULTI_REP_ANALYZER,
	                                                                               GROUP_MULTI_REP_ANAL_GRANULARITIES, NORMAL, "SPM granularity", null, 1, 1,
	                                                                               99 );
	
	/** Multi-Replay Analyzer SQ granularity. */
	IntSetting                        MULTI_REP_SQ_GRANULARITY             = new IntSetting( "MULTI_REP_SQ_GRANULARITY", NODE_MULTI_REP_ANALYZER,
	                                                                               GROUP_MULTI_REP_ANAL_GRANULARITIES, NORMAL, "SQ granularity", null, 5, 1, 99 );
	
	/** Multi-Replay Analyzer Supply-capped percent granularity. */
	IntSetting                        MULTI_REP_SCP_GRANULARITY            = new IntSetting( "MULTI_REP_SCP_GRANULARITY", NODE_MULTI_REP_ANALYZER,
	                                                                               GROUP_MULTI_REP_ANAL_GRANULARITIES, NORMAL, "Supply-capped % granularity",
	                                                                               VHB.sstext_( "%" ), 2, 1, 99 );
	
	/** Merged Accounts settings. */
	NodeSetting                       NODE_MERGED_ACCOUNTS                 = new NodeSetting( "NODE_MERGED_ACCOUNTS", NODE_MULTI_REP_ANALYZER,
	                                                                               "Merged Accounts", Icons.F_TAGS );
	
	/** Merged Accounts group. */
	SettingsGroup                     GROUP_MERGED_ACCOUNTS                = new SettingsGroup( "Merged Accounts", Helps.MERGED_ACCOUNTS );
	
	/** Merged Accounts. */
	ValidatedMultilineStringSetting   MERGED_ACCOUNTS                      = new ValidatedMultilineStringSetting( "MERGED_ACCOUNTS", NODE_MERGED_ACCOUNTS,
	                                                                               GROUP_MERGED_ACCOUNTS, NORMAL, "Merged Accounts", new VHB()
	                                                                                       .compConfigurer( new ICompConfigurer() {
		                                                                                       @Override
		                                                                                       public void configure( JComponent settingComp,
		                                                                                               ISetting< ? > setting, ISettingsBean settings ) {
			                                                                                       Utils.setMergedAccountsValidator( (IndicatorTextArea) settingComp );
		                                                                                       }
	                                                                                       } ).rows( 20 ).cols( 40 ).build(), "" );
	
	
	
	// EMAIL SETTINGS
	
	/** Miscellaneous settings. */
	NodeSetting                       NODE_EMAIL                           = new NodeSetting( "EMAIL", null, "Email", Icons.F_MAIL_AT_SIGN );
	
	/** SMTP server group. */
	SettingsGroup                     GROUP_SMTP_SERVER                    = new SettingsGroup( "Email Server (SMTP)", Helps.EMAIL_SERVER_SMTP );
	
	/** SMTP server host. */
	StringSetting                     SMTP_HOST                            = new StringSetting( "SMPT_HOST", NODE_EMAIL, GROUP_SMTP_SERVER, BASIC,
	                                                                               "SMTP server host", VHB.ssCompFactory_( new HostCheckTestBtnFactory() ), "" );
	
	/** SMTP server port. */
	IntSetting                        SMTP_PORT                            = new IntSetting( "SMPT_PORT", NODE_EMAIL, GROUP_SMTP_SERVER, BASIC,
	                                                                               "SMTP server port", null, 587, 0, 65535 );
	
	/** Use secure connection to SMTP server. */
	BoolSetting                       SMTP_SECURE                          = new BoolSetting( "SMTP_SECURE", NODE_EMAIL, GROUP_SMTP_SERVER, BASIC,
	                                                                               "Use secure connection to SMTP server (TLS / SSL)", null, Boolean.TRUE );
	
	/** SMTP user name. */
	StringSetting                     SMTP_USER                            = new StringSetting( "SMPT_USER", NODE_EMAIL, GROUP_SMTP_SERVER, BASIC,
	                                                                               "SMTP user name", null, "" );
	
	/** Email group. */
	SettingsGroup                     GROUP_EMAIL                          = new SettingsGroup( "Email" );
	
	/** Sender email address. */
	ValidatedStringSetting            EMAIL_FROM                           = new ValidatedStringSetting( "EMAIL_FROM", NODE_EMAIL, GROUP_EMAIL, BASIC,
	                                                                               "Sender email address (From)", new VHB()
	                                                                                       .compConfigurer( new ICompConfigurer() {
		                                                                                       @Override
		                                                                                       public void configure( JComponent settingComp,
		                                                                                               ISetting< ? > setting, ISettingsBean settings ) {
			                                                                                       Utils.setEmailValidator( (IndicatorTextField) settingComp );
		                                                                                       }
	                                                                                       } ).help( Helps.SENDER_EMAIL_ADDRESS ).build(), "" );
	
	/** Email addressees. */
	ValidatedStringSetting            EMAIL_TO                             = new ValidatedStringSetting( "EMAIL_TO", NODE_EMAIL, GROUP_EMAIL, HIDDEN, "To",
	                                                                               null, "" );
	
	/** Email subject. */
	StringSetting                     EMAIL_SUBJECT                        = new StringSetting( "EMAIL_SUBJECT", NODE_EMAIL, GROUP_EMAIL, HIDDEN, "Subject",
	                                                                               null, "" );
	
	/** Email message body. */
	MultilineStringSetting            EMAIL_BODY                           = new MultilineStringSetting( "EMAIL_BODY", NODE_EMAIL, GROUP_EMAIL, HIDDEN,
	                                                                               "Message", null, "" );
	
	/** Email footer. */
	MultilineStringSetting            EMAIL_FOOTER                         = new MultilineStringSetting( "EMAIL_FOOTER", NODE_EMAIL, GROUP_EMAIL, NORMAL,
	                                                                               "Footer", VHB.reqReg_(), "Sent from " + Consts.APP_NAME + ": "
	                                                                                       + Consts.URL_HOME_PAGE );
	
	/** Email Client group. */
	SettingsGroup                     GROUP_EMAIL_CLIENT                   = new SettingsGroup( "Email Client" );
	
	/** Use internal email client when clicking on email links. */
	BoolSetting                       USE_INTERNAL_EMAIL_CLIENT            = new BoolSetting(
	                                                                               "USE_INTERNAL_EMAIL_CLIENT",
	                                                                               NODE_EMAIL,
	                                                                               GROUP_EMAIL_CLIENT,
	                                                                               BASIC,
	                                                                               "Use "
	                                                                                       + Consts.APP_NAME
	                                                                                       + " to send emails when clicking on links (else the default OS email app)",
	                                                                               null, Boolean.FALSE );
	
	
	
	// MISCELLANEOUS SETTINGS
	
	/** Miscellaneous settings. */
	NodeSetting                       NODE_MISCELLANEOUS                   = new NodeSetting( "MISCELLANEOUS", null, "Miscellaneous", Icons.F_EQUALIZER );
	
	/** Choosing Replays group. */
	SettingsGroup                     GROUP_CHOOSING_REPLAYS               = new SettingsGroup( "Choosing Replays" );
	
	/** Start folder when choosing replays. */
	PathSetting                       START_FOLDER_WHEN_CHOOSING_REPS      = new PathSetting( "START_FOLDER_WHEN_CHOOSING_REPS", NODE_MISCELLANEOUS,
	                                                                               GROUP_CHOOSING_REPLAYS, NORMAL, "Start folder when choosing replays",
	                                                                               VHB.dtitle_( "Choose Start folder when choosing replays" ), Paths.get( "" ) );
	
	
	/** Internal settings group. */
	SettingsGroup                     GROUP_INTERNAL                       = new SettingsGroup( "Internal" );
	
	/** Utilized CPU Cores. */
	IntSetting                        UTILIZED_CPU_CORES                   = new IntSetting( "UTILIZED_CPU_CORES", NODE_MISCELLANEOUS, GROUP_INTERNAL, NORMAL,
	                                                                               "<html>Utilized CPU Cores:<br>(set 0 to use all detected cores)</html>",
	                                                                               VHB.sstext_( "<html>Detected<br>Cores: "
	                                                                                       + Runtime.getRuntime().availableProcessors() + "</html>" ), 0, 0, 32 );
	
}
