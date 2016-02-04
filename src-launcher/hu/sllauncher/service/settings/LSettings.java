/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.sllauncher.service.settings;

import static hu.sllauncher.util.SkillLevel.ADVANCED;
import static hu.sllauncher.util.SkillLevel.BASIC;
import static hu.sllauncher.util.SkillLevel.HIDDEN;
import static hu.sllauncher.util.SkillLevel.NORMAL;
import hu.scelightapibase.bean.settings.ISettingsBean;
import hu.scelightapibase.gui.comp.IButton;
import hu.sllauncher.bean.settings.type.BeanSetting;
import hu.sllauncher.bean.settings.type.BoolSetting;
import hu.sllauncher.bean.settings.type.EnumSetting;
import hu.sllauncher.bean.settings.type.FixedEnumValuesSetting;
import hu.sllauncher.bean.settings.type.IntSetting;
import hu.sllauncher.bean.settings.type.NodeSetting;
import hu.sllauncher.bean.settings.type.Setting;
import hu.sllauncher.bean.settings.type.SettingsGroup;
import hu.sllauncher.bean.settings.type.StringSetting;
import hu.sllauncher.bean.settings.type.viewhints.HostCheckTestBtnFactory;
import hu.sllauncher.bean.settings.type.viewhints.TestBtnFactory;
import hu.sllauncher.bean.settings.type.viewhints.VHB;
import hu.sllauncher.gui.help.LHelps;
import hu.sllauncher.gui.icon.LIcons;
import hu.sllauncher.gui.page.extmods.OffExtModConfsBean;
import hu.sllauncher.gui.page.extmods.installed.InstExtModConfsBean;
import hu.sllauncher.service.env.bootsettings.BootSettings;
import hu.sllauncher.service.lang.DateFormatE;
import hu.sllauncher.service.lang.NumberFormatE;
import hu.sllauncher.service.lang.PersonNameFormat;
import hu.sllauncher.service.log.LogLevel;
import hu.sllauncher.service.sound.LSounds;
import hu.sllauncher.service.sound.Sound;
import hu.sllauncher.service.sound.SoundTheme;
import hu.sllauncher.util.SkillLevel;

import javax.swing.JComponent;

/**
 * Instances of the launcher settings.
 * 
 * <p>
 * <i>Implementation note:</i> This is an interface instead of a class because constants defined in an interface are implicitly <code>public</code>,
 * <code>static</code> and <code>final</code>, and so these keywords can be omitted!
 * </p>
 * 
 * @author Andras Belicza
 */
public interface LSettings extends BootSettings {
	
	/** Miscellaneous group. */
	SettingsGroup                        GROUP_MISC                 = new SettingsGroup( "Miscellaneous" );
	
	
	
	// LAUNCHER SETTINGS
	
	/** Launcher settings. */
	NodeSetting                          NODE_LAUNCHER              = new NodeSetting( "LAUNCHER", null, "Launcher", LIcons.MY_APP_ICON );
	
	/** Launcher group. */
	SettingsGroup                        GROUP_LAUNCHER             = new SettingsGroup( "Launcher" );
	
	/** The user's computer skill level. */
	FixedEnumValuesSetting< SkillLevel > SKILL_LEVEL                = new FixedEnumValuesSetting<>( "SKILL_LEVEL", NODE_LAUNCHER, GROUP_LAUNCHER, HIDDEN,
	                                                                        "Skill Level", null, SkillLevel.NORMAL, SkillLevel.BASIC, SkillLevel.NORMAL,
	                                                                        SkillLevel.ADVANCED, SkillLevel.DEVELOPER );
	
	/** Tells if welcome page has to be shown on startup. */
	BoolSetting                          WELCOME_ON_STARTUP         = new BoolSetting( "WELCOME_ON_STARTUP", NODE_LAUNCHER, GROUP_LAUNCHER, HIDDEN,
	                                                                        "Show Welcome page on startup", null, Boolean.TRUE );
	
	/** Tells if application is to be auto-started when application is ready. */
	BoolSetting                          AUTO_START_WHEN_READY      = new BoolSetting( "AUTO_START_WHEN_READY", NODE_LAUNCHER, GROUP_LAUNCHER, NORMAL,
	                                                                        "Auto-start when Scelight is ready", VHB.reqReg_(), Boolean.FALSE );
	
	/** Tells if application is to be auto-started even if there is no connection. */
	BoolSetting                          AUTO_START_WHEN_NO_CONN    = new BoolSetting( "AUTO_START_WHEN_NO_CONN", NODE_LAUNCHER, GROUP_LAUNCHER, NORMAL,
	                                                                        "Auto-start even if no connection", VHB.reqReg_(), Boolean.FALSE );
	
	
	
	// EXTERNAL MODULES SETTINGS
	
	/** External modules settings. */
	NodeSetting                          NODE_EXTERNAL_MODULES      = new NodeSetting( "EXTERNAL_MODULES", null, "External Modules", LIcons.F_CATEGORY );
	
	/** Official External Module configs. */
	BeanSetting< OffExtModConfsBean >    OFF_EXT_MOD_CONFS          = new BeanSetting<>( "OFF_EXT_MOD_CONFS", NODE_EXTERNAL_MODULES, null, HIDDEN,
	                                                                        "Official External Module Configs", null, new OffExtModConfsBean() );
	
	/** Installed External Module configs. */
	BeanSetting< InstExtModConfsBean >   INST_EXT_MOD_CONFS         = new BeanSetting<>( "INST_EXT_MOD_CONFS", NODE_EXTERNAL_MODULES, null, HIDDEN,
	                                                                        "Installed External Module Configs", null, new InstExtModConfsBean() );
	
	
	
	// UI SETTINGS
	
	/** User interface settings. */
	NodeSetting                          NODE_UI                    = new NodeSetting( "UI", null, "User Interface", LIcons.F_UI_SCROLL_PANE_IMAGE );
	
	/** Tool tips group. */
	SettingsGroup                        GROUP_TOOL_TIPS            = new SettingsGroup( "Tool tips" );
	
	/** Tool tip initial delay in milliseconds. */
	IntSetting                           TOOL_TIP_INITIAL_DELAY     = new IntSetting( "TOOL_TIP_INITIAL_DELAY", NODE_UI, GROUP_TOOL_TIPS, NORMAL,
	                                                                        "Tool tips initial delay", VHB.sstext_( "ms" ), 200, 0, 9_999 );
	
	/** Tool tip dismiss delay in milliseconds. */
	IntSetting                           TOOL_TIP_DISMISS_DELAY     = new IntSetting( "TOOL_TIP_DISMISS_DELAY", NODE_UI, GROUP_TOOL_TIPS, NORMAL,
	                                                                        "Tool tips dismiss delay", VHB.sstext_( "ms" ), 5_000, 0, 60_000 );
	
	/** Tells if tool tips have to be displayed over links. */
	BoolSetting                          DISPLAY_LINK_TOOL_TIPS     = new BoolSetting( "DISPLAY_LINK_TOOL_TIPS", NODE_UI, GROUP_TOOL_TIPS, BASIC,
	                                                                        "Display tool tips over links", null, Boolean.TRUE );
	
	/** Helps and Tips group. */
	SettingsGroup                        GROUP_HELPS_AND_TIPS       = new SettingsGroup( "Helps and Tips" );
	
	/** Show Helps and Tips on mouse over (else only on mouse click). */
	BoolSetting                          SHOW_HELPS_TIPS_MOUSE_OVER = new BoolSetting( "SHOW_HELPS_TIPS_MOUSE_OVER", NODE_UI, GROUP_HELPS_AND_TIPS, NORMAL,
	                                                                        "Show Helps and Tips on mouse over (else only on click)", null, Boolean.TRUE );
	
	/** Hide Helps and Tips on mouse out (else only on mouse click). */
	BoolSetting                          HIDE_HELPS_TIPS_MOUSE_OUT  = new BoolSetting( "HIDE_HELPS_TIPS_MOUSE_OUT", NODE_UI, GROUP_HELPS_AND_TIPS, NORMAL,
	                                                                        "Hide Helps and Tips on mouse out (else only on click)", null, Boolean.TRUE );
	
	/** Tables group. */
	SettingsGroup                        GROUP_TABLES               = new SettingsGroup( "Tables" );
	
	/** Show horizontal lines in tables. */
	BoolSetting                          SHOW_HORIZ_TABLE_LINES     = new BoolSetting( "SHOW_HORIZ_TABLE_LINES", NODE_UI, GROUP_TABLES, NORMAL,
	                                                                        "Show horizontal lines in tables", null, Boolean.FALSE );
	
	/** Show vertical lines in tables. */
	BoolSetting                          SHOW_VERT_TABLE_LINES      = new BoolSetting( "SHOW_VERT_TABLE_LINES", NODE_UI, GROUP_TABLES, NORMAL,
	                                                                        "Show vertical lines in tables", null, Boolean.TRUE );
	
	/** Vertical scrolling amount in pixels. */
	IntSetting                           VERTICAL_SCROLL_AMOUNT     = new IntSetting( "VERTICAL_SCROLL_AMOUNT", NODE_UI, GROUP_MISC, NORMAL,
	                                                                        "Vertical scrolling amount", new VHB().sstext( "pixel" )
	                                                                                .help( LHelps.VERTICAL_SCROLLING_AMOUNT ).build(), 60, 3, 300 );
	
	/** User settings location. */
	EnumSetting< IconScalingQuality >    ICON_SCALING_QUALITY       = new EnumSetting<>( "ICON_SCALING_QUALITY", NODE_UI, GROUP_MISC, ADVANCED,
	                                                                        "Icon scaling quality", VHB.help_( LHelps.ICON_SCALING_QUALITY ),
	                                                                        IconScalingQuality.MEDIUM );
	
	
	
	// MULTI-PAGE COMPONENT SETTINGS
	
	/** Multi-page component settings. */
	NodeSetting                          NODE_MULTI_PAGE            = new NodeSetting( "MULTI_PAGE", NODE_UI, "Multi-page", LIcons.F_UI_LAYERED_PANE );
	
	/** Multi-page component group. */
	SettingsGroup                        GROUP_MULTI_PAGE           = new SettingsGroup( "Multi-page" );
	
	/** Page title font size in pixels. */
	IntSetting                           PAGE_TITLE_FONT_SIZE       = new IntSetting( "PAGE_TITLE_SIZE", NODE_MULTI_PAGE, GROUP_MULTI_PAGE, NORMAL,
	                                                                        "Page title font size", VHB.sstext_( "pixel" ), 24, 8, 64 );
	
	/** Page list font size in pixels. */
	IntSetting                           PAGE_LIST_FONT_SIZE        = new IntSetting( "PAGE_LIST_SIZE", NODE_MULTI_PAGE, GROUP_MULTI_PAGE, NORMAL,
	                                                                        "Page list font size", VHB.sstext_( "pixel" ), 12, 8, 32 );
	
	/** Multi-page divider size in pixels. */
	IntSetting                           MULTI_PAGE_DIVIDER_SIZE    = new IntSetting( "MULTI_PAGE_DIVIDER_SIZE", NODE_MULTI_PAGE, GROUP_MULTI_PAGE, NORMAL,
	                                                                        "Navigation tree - Current page divider size", VHB.sstext_( "pixel" ), 10, 0, 20 );
	
	/** Page History size. */
	IntSetting                           PAGE_HISTORY_SIZE          = new IntSetting( "PAGE_HISTORY_SIZE", NODE_MULTI_PAGE, GROUP_MULTI_PAGE, NORMAL,
	                                                                        "Page History Size", null, 20, 0, 50 );
	
	/** Show control bar. */
	BoolSetting                          SHOW_CONTROL_BAR           = new BoolSetting( "SHOW_CONTROL_BAR", NODE_MULTI_PAGE, GROUP_MULTI_PAGE, NORMAL,
	                                                                        "Show Control bar", VHB.help_( LHelps.CONTROL_BAR ), Boolean.TRUE );
	
	
	
	// TOOL BAR SETTINGS
	
	/** Tool bar settings. */
	NodeSetting                          TOOL_BAR                   = new NodeSetting( "TOOL_BAR", NODE_UI, "Tool Bar", LIcons.F_UI_TOOLBAR );
	
	/** Tool Bar group. */
	SettingsGroup                        GROUP_TOOL_BAR             = new SettingsGroup( "Tool Bar" );
	
	/** Tool bar icon size in pixels. */
	IntSetting                           TOOL_BAR_ICON_SIZE         = new IntSetting( "TOOL_BAR_ICON_SIZE", TOOL_BAR, GROUP_TOOL_BAR, BASIC,
	                                                                        "Tool bar icon size", VHB.sstext_( "pixel" ), 24, 8, 64 );
	
	
	
	// SOUND SETTINGS
	
	/** Sound settings. */
	NodeSetting                          NODE_SOUND                 = new NodeSetting( "SOUND", null, "Sound", LIcons.F_SPEAKER_VOLUME );
	
	/** Beeps group. */
	SettingsGroup                        GROUP_BEEPS                = new SettingsGroup( "Beeps" );
	
	/** Enable beeps. */
	BoolSetting                          ENABLE_BEEPS               = new BoolSetting( "ENABLE_BEEPS", NODE_SOUND, GROUP_BEEPS, BASIC,
	                                                                        "Enable Beep notifications",
	                                                                        VHB.ssCompFactory_( new TestBtnFactory< Setting< ? > >() {
		                                                                        @Override
		                                                                        public void doTest( IButton b, JComponent settingComp, Setting< ? > setting,
		                                                                                ISettingsBean settings ) {
			                                                                        Sound.doBeep();
		                                                                        }
	                                                                        } ), Boolean.TRUE );
	
	/** Beep on Warning. */
	BoolSetting                          BEEP_ON_ERROR              = new BoolSetting( "BEEP_ON_ERROR", NODE_SOUND, GROUP_BEEPS, NORMAL,
	                                                                        "Beep on Error dialog", null, Boolean.TRUE );
	
	/** Beep on Warning. */
	BoolSetting                          BEEP_ON_WARNING            = new BoolSetting( "BEEP_ON_WARNING", NODE_SOUND, GROUP_BEEPS, NORMAL,
	                                                                        "Beep on Warning dialog", null, Boolean.TRUE );
	
	/** Beep on Confirmation. */
	BoolSetting                          BEEP_ON_CONFIRMATION       = new BoolSetting( "BEEP_ON_CONFIRMATION", NODE_SOUND, GROUP_BEEPS, NORMAL,
	                                                                        "Beep on Confirmation dialog", null, Boolean.TRUE );
	
	/** Beep on Input. */
	BoolSetting                          BEEP_ON_INPUT              = new BoolSetting( "BEEP_ON_INPUT", NODE_SOUND, GROUP_BEEPS, NORMAL,
	                                                                        "Beep on Input dialog", null, Boolean.FALSE );
	
	/** Beep on Info. */
	BoolSetting                          BEEP_ON_INFO               = new BoolSetting( "BEEP_ON_INFO", NODE_SOUND, GROUP_BEEPS, NORMAL, "Beep on Info dialog",
	                                                                        null, Boolean.FALSE );
	
	/** Beep on empty text search result. */
	BoolSetting                          BEEP_EMPTY_TXT_SEARCH_RSLT = new BoolSetting( "BEEP_EMPTY_TXT_SEARCH_RSLT", NODE_SOUND, GROUP_BEEPS, NORMAL,
	                                                                        "Beep on empty text search result", null, Boolean.TRUE );
	
	// VOICE SETTINGS
	
	/** Voice settings. */
	NodeSetting                          NODE_VOICE                 = new NodeSetting( "VOICE", NODE_SOUND, "Voice", LIcons.F_MICROPHONE );
	
	/** Voice group. */
	SettingsGroup                        GROUP_VOICE                = new SettingsGroup( "Voice" );
	
	/** Enable voice notifications. */
	BoolSetting                          ENABLE_VOICE               = new BoolSetting( "ENABLE_VOICE", NODE_VOICE, GROUP_VOICE, BASIC,
	                                                                        "Enable Voice notifications", null, Boolean.TRUE );
	
	/** Sound theme. */
	EnumSetting< SoundTheme >            VOICE_THEME                = new EnumSetting<>( "VOICE_THEME", NODE_VOICE, GROUP_VOICE, BASIC, "Voice Theme",
	                                                                        VHB.ssCompFactory_( new TestBtnFactory< EnumSetting< SoundTheme > >() {
		                                                                        @Override
		                                                                        public void doTest( IButton b, JComponent settingComp,
		                                                                                EnumSetting< SoundTheme > setting, ISettingsBean settings ) {
			                                                                        Sound.play( LSounds.SCELIGHT, settings.get( setting ) );
		                                                                        }
	                                                                        } ), SoundTheme.SMIX_US );
	
	/** Sound volume. */
	IntSetting                           VOICE_VOLUME               = new IntSetting( "VOICE_VOLUME", NODE_VOICE, GROUP_VOICE, BASIC, "Voice volume", new VHB()
	                                                                        .sstext( "%" ).ssCompFactory( new TestBtnFactory< IntSetting >() {
		                                                                        @Override
		                                                                        public void doTest( IButton b, JComponent settingComp, IntSetting setting,
		                                                                                ISettingsBean settings ) {
			                                                                        Sound.play( LSounds.SCELIGHT, settings.get( setting ) );
		                                                                        }
	                                                                        } ).build(), 70, 0, 100 );
	
	
	
	// LOCALE SETTINGS
	
	/** Locale settings. */
	NodeSetting                          NODE_LOCALE                = new NodeSetting( "LOCALE", null, "Locale", LIcons.F_LOCALE_ALTERNATE );
	
	/** Locale group. */
	SettingsGroup                        GROUP_LOCALE               = new SettingsGroup( "Locale" );
	
	/** Date format. */
	EnumSetting< DateFormatE >           DATE_FORMAT                = new EnumSetting<>( "DATE_FORMAT", NODE_LOCALE, GROUP_LOCALE, BASIC, "Date format", null,
	                                                                        DateFormatE.YYYY_MM_DD );
	
	/** Number format. */
	EnumSetting< NumberFormatE >         NUMBER_FORMAT              = new EnumSetting<>( "NUMBER_FORMAT", NODE_LOCALE, GROUP_LOCALE, BASIC, "Number format",
	                                                                        null, NumberFormatE.D_DDD__DD );
	
	/** Number format. */
	EnumSetting< PersonNameFormat >      PERSON_NAME_FORMAT         = new EnumSetting<>( "PERSON_NAME_FORMAT", NODE_LOCALE, GROUP_LOCALE, BASIC,
	                                                                        "Person name format", null, PersonNameFormat.FIRST_MIDDLE_NICK_LAST );
	
	
	
	// LOGS SETTINGS
	
	/** Logs settings. */
	NodeSetting                          NODE_LOGS                  = new NodeSetting( "LOGS", null, "Logs", LIcons.F_REPORT_EXCLAMATION );
	
	/** Logs group. */
	SettingsGroup                        GROUP_LOGS                 = new SettingsGroup( "Logs" );
	
	/** Log level. */
	EnumSetting< LogLevel >              LOG_LEVEL                  = new EnumSetting<>( "LOG_LEVEL", NODE_LOGS, GROUP_LOGS, BASIC, "Log Level",
	                                                                        VHB.help_( LHelps.LOG_LEVEL ), LogLevel.DEBUG );
	
	/** Time to keep log files for (days). */
	IntSetting                           LOG_FILES_LIFETIME         = new IntSetting( "LOG_FILES_LIFETIME", NODE_LOGS, GROUP_LOGS, NORMAL,
	                                                                        "Time to keep log files for", VHB.sstext_( "days" ), 30, 0, 9_999 );
	
	/** Logs Page group. */
	SettingsGroup                        GROUP_LOGS_PAGE            = new SettingsGroup( "Logs Page" );
	
	/** Show Log Record Details if available. */
	BoolSetting                          SHOW_LOG_DETAILS           = new BoolSetting( "SHOW_LOG_DETAILS", NODE_LOGS, GROUP_LOGS_PAGE, NORMAL,
	                                                                        "Show Log Record Details if available", null, Boolean.FALSE );
	
	
	
	// NETWORK SETTINGS
	
	/** Network settings. */
	NodeSetting                          NODE_NETWORK               = new NodeSetting( "NETWORK", null, "Network", LIcons.F_GLOBE_NETWORK );
	
	/** Network Proxy group. */
	SettingsGroup                        GROUP_NETWORK_PROXY        = new SettingsGroup( "Network Proxy", LHelps.NETWORK_PROXY );
	
	/** Show Log Record Details if available. */
	BoolSetting                          ENABLE_NETWORK_PROXY       = new BoolSetting( "ENABLE_NETWORK_PROXY", NODE_NETWORK, GROUP_NETWORK_PROXY, NORMAL,
	                                                                        "Access the internet through a Proxy server", null, Boolean.FALSE );
	
	/** HTTP Proxy host. */
	StringSetting                        HTTP_PROXY_HOST            = new StringSetting( "HTTP_PROXY_HOST", NODE_NETWORK, GROUP_NETWORK_PROXY, NORMAL,
	                                                                        "HTTP Proxy host", VHB.ssCompFactory_( new HostCheckTestBtnFactory() ), "" );
	
	/** HTTP Proxy port. */
	IntSetting                           HTTP_PROXY_PORT            = new IntSetting( "HTTP_PROXY_PORT", NODE_NETWORK, GROUP_NETWORK_PROXY, NORMAL,
	                                                                        "HTTP Proxy port", null, 80, 0, 65535 );
	
	/** HTTPS Proxy host. */
	StringSetting                        HTTPS_PROXY_HOST           = new StringSetting( "HTTPS_PROXY_HOST", NODE_NETWORK, GROUP_NETWORK_PROXY, NORMAL,
	                                                                        "HTTPS Proxy host", VHB.ssCompFactory_( new HostCheckTestBtnFactory() ), "" );
	
	/** HTTPS Proxy port. */
	IntSetting                           HTTPS_PROXY_PORT           = new IntSetting( "HTTPS_PROXY_PORT", NODE_NETWORK, GROUP_NETWORK_PROXY, NORMAL,
	                                                                        "HTTPS Proxy port", null, 443, 0, 65535 );
	
	/** SOCKS Proxy host. */
	StringSetting                        SOCKS_PROXY_HOST           = new StringSetting( "SOCKS_PROXY_HOST", NODE_NETWORK, GROUP_NETWORK_PROXY, NORMAL,
	                                                                        "SOCKS Proxy host", VHB.ssCompFactory_( new HostCheckTestBtnFactory() ), "" );
	
	/** SOCKS Proxy port. */
	IntSetting                           SOCKS_PROXY_PORT           = new IntSetting( "SOCKS_PROXY_PORT", NODE_NETWORK, GROUP_NETWORK_PROXY, NORMAL,
	                                                                        "SOCKS Proxy port", null, 1080, 0, 65535 );
	
}
