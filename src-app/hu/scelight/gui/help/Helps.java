/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight.gui.help;

import static hu.scelight.gui.help.Helps.ResUtil.h;
import static hu.sllauncher.gui.help.LHelps.ResUtil.s;
import hu.scelight.sc2.rep.model.initdata.gamedesc.Region;
import hu.scelight.util.RHtml;
import hu.sllauncher.util.LUtils;

/**
 * Application help HTML template resource collection.
 * 
 * <p>
 * <i>Implementation note:</i> This is an interface instead of a class because constants defined in an interface are implicitly <code>public</code>,
 * <code>static</code> and <code>final</code>, and so these keywords can be omitted!
 * </p>
 * 
 * @author Andras Belicza
 */
public interface Helps {
	
	// Fragments must be created first to have them cached and available for inclusion
	
	/** Playing Session fragment. */
	RHtml F_PLAYING_SESSION                     = new RHtml( "Playing Session", h( "fragment/playing-session" ) );
	
	/** Overlay Note fragment. */
	RHtml F_OVERLAY_NOTE                        = new RHtml( "Overlay Note", h( "fragment/overlay-note" ) );
	
	/** Event Box Tip fragment. */
	RHtml F_EVENT_BOX_TIP                       = new RHtml( "Event Box Tip", h( "fragment/event-box-tip" ) );
	
	
	
	/** Replay Folders help. */
	RHtml REPLAY_FOLDERS                        = new RHtml( "Replay Folders", h( "replay-folders" ) );
	
	/** Replay Filters help. */
	RHtml REPLAY_FILTERS                        = new RHtml( "Replay Filters", h( "filters/replay-filters" ) );
	
	/** Player Filters help. */
	RHtml PLAYER_FILTERS                        = new RHtml( "Player Filters", h( "filters/player-filters" ) );
	
	/** Regular Expressions help. */
	RHtml REGULAR_EXPRESSIONS                   = new RHtml( "Regular Expressions", h( "filters/regular-expressions" ) );
	
	/** Quick Open Last Replay help. */
	RHtml QUICK_OPEN_LAST_REPLAY                = new RHtml( "Quick Open Last Replay", h( "quick-open-last-replay" ) );
	
	/** Chart Zooming and Scrolling help. */
	RHtml CHART_ZOOMING_SCROLLING               = new RHtml( "Chart Zooming and Scrolling", h( "chart-zooming-scrolling" ) );
	
	/** Multi-Replay Analzyer help. */
	RHtml MULTI_REPLAY_ANALYZER                 = new RHtml( "Multi-Replay Analyzer", h( "multi-replay-analyzer" ) );
	
	/** Time Trend Playing Session help. */
	RHtml TIME_TREND_PLAYING_SESSION            = new RHtml( "Time Trend Playing Session", h( "time-trend-playing-session" ) );
	
	/** Timelapse Playing Session help. */
	RHtml TIMELAPSE_PLAYING_SESSION             = new RHtml( "Timelapse Playing Session", h( "timelapse-playing-session" ) );
	
	/** Name Template help. */
	RHtml NAME_TEMPLATE                         = new RHtml( "Name Template", h( "template/name-template" ) );
	
	/** Name Template Player Info Block help. */
	RHtml NT_PLAYER_INFO_BLOCK                  = new RHtml( "Player Info Block", h( "template/player-info-block" ) );
	
	/** Name Template Sub-folders help. */
	RHtml NT_SUBFOLDERS                         = new RHtml( "Sub-folders", h( "template/subfolders" ) );
	
	/** Name Template Symbol Value Range help. */
	RHtml NT_SYMBOL_VALUE_RANGE                 = new RHtml( "Symbol Value Range", h( "template/symbol-value-range" ) );
	
	
	// CHARTS HELPS
	
	/** APM Chart help. */
	RHtml APM_CHART                             = new RHtml( "APM Chart", h( "charts/apm" ) );
	
	/** SPM Chart help. */
	RHtml SPM_CHART                             = new RHtml( "SPM Chart", h( "charts/spm" ) );
	
	/** Control Groups Chart help. */
	RHtml CONTROL_GROUPS_CHART                  = new RHtml( "Control Groups Chart", h( "charts/control-groups" ) );
	
	/** Player Stats Chart help. */
	RHtml PLAYER_STATS_CHART                    = new RHtml( "Player Stats Chart", h( "charts/player-stats" ) );
	
	/** Commands Chart help. */
	RHtml COMMANDS_CHART                        = new RHtml( "Commands Chart", h( "charts/commands" ) );
	
	/** Base Control Chart help. */
	RHtml BASE_CONTROL_CHART                    = new RHtml( "Base Control Chart", h( "charts/base-control" ) );
	
	
	// APPLICATION SETTING HELPS
	
	/** Moving Tool bar help. */
	RHtml MOVING_THE_TOOL_BAR                   = new RHtml( "Moving the Tool Bar", s( "moving-the-tool-bar" ) );
	
	/** Battle.net Website Language help. */
	RHtml BATTLE_NET_WEBSITE_LANGUAGE           = new RHtml( "Batte.net Website Language", s( "battle-net-website-language" ), LUtils.asNewSet( "langTable" ),
	                                                    "langTable", Region.getLanguageSupportTable() );
	
	/** Map Download Source Region help. */
	RHtml MAP_DOWNLOAD_SOURCE_REGION            = new RHtml( "Map Download Source Region", s( "map-download-source-region" ) );
	
	/** Only pick up "Just Played" new replays help. */
	RHtml ONLY_PICK_UP_JUST_PLAYED_REPS         = new RHtml( "Only Pick Up 'Just Played' Reps", s( "only-pick-up-just-played-reps" ) );
	
	/** Rename New Replays help. */
	RHtml RENAME_NEW_REPLAYS                    = new RHtml( "Rename New Replays", s( "rename-new-replays" ) );
	
	/** Back up New Replays help. */
	RHtml BACK_UP_NEW_REPLAYS                   = new RHtml( "Back up New Replays", s( "back-up-new-replays" ) );
	
	/** Game-time Real-time help. */
	RHtml GAME_TIME_REAL_TIME                   = new RHtml( "Game-time Real-time", s( "game-time-real-time" ) );
	
	/** Initial Per-Minute Calculation Exclusion Time help. */
	RHtml INITIAL_PER_MIN_CALC_EXCL_TIME        = new RHtml( "Initial Per-Minute Calculation Exclusion Time", s( "initial-per-min-calc-excl-time" ) );
	
	/** Favored players help. */
	RHtml FAVORED_PLAYERS                       = new RHtml( "Favored Players", s( "favored-players" ) );
	
	/** Override Format Based on Matchup help. */
	RHtml OVERRIDE_FORMAT_BASED_ON_MATCHUP      = new RHtml( "Override Format Based on Matchup", s( "override-format-based-on-matchup" ) );
	
	/** Declare Largest Remaining Team Winner help. */
	RHtml DECLARE_LARGEST_REMAINING_TEAM_WINNER = new RHtml( "Declare Largest Remaining Team Winner", s( "declare-largest-remaining-team-winner" ) );
	
	/** Raw Data Tree Expand Depth help. */
	RHtml RAW_DATA_TREE_EXPAND_DEPTH            = new RHtml( "Raw Data Tree Expand Depth", s( "raw-data-tree-expand-depth" ) );
	
	/** Max Replay Analyzers for New Replays help. */
	RHtml MAX_REP_ANALYZERS_FOR_NEW_REPS        = new RHtml( "Max Replay Analyzers for New Replays", s( "max-rep-analyzers-for-new-reps" ) );
	
	/** Color Replay List Table help. */
	RHtml COLOR_REP_LIST_TABLE                  = new RHtml( "Color Replay List Table", s( "color-replay-list-table" ) );
	
	/** Map Image Zoom help. */
	RHtml MAP_IMAGE_ZOOM                        = new RHtml( "Map Image Zoom", s( "map-image-zoom" ) );
	
	/** Max Replays to Open Together help. */
	RHtml MAX_REPS_TO_OPEN_TOGETHER             = new RHtml( "Max Replays to Open Together", s( "max-reps-to-open-together" ) );
	
	/** Auto-resize Main Navigation Tree help. */
	RHtml AUTO_RESIZE_MAIN_NAV_TREE             = new RHtml( "Auto-resize Main Navigation Tree", s( "auto-resize-main-nav-tree" ) );
	
	/** Last Game Info Overlay help. */
	RHtml LAST_GAME_INFO_OVERLAY                = new RHtml( "Last Game Info Overlay", s( "last-game-info-overlay" ) );
	
	
	/** SC2 Game Monitor help. */
	RHtml SC2_GAME_MONITOR                      = new RHtml( "SC2 Game Monitor", s( "sc2-game-monitor" ) );
	
	/** Live APM Overlay help. */
	RHtml LIVE_APM_OVERLAY                      = new RHtml( "Live APM Overlay", s( "live-apm-overlay" ) );
	
	/** APM Alert help. */
	RHtml APM_ALERT                             = new RHtml( "APM Alert", s( "apm-alert" ) );
	
	/** APM Warm-up Time help. */
	RHtml APM_WARMUP_TIME                       = new RHtml( "APM Warm-up Time", s( "apm-warmup-time" ) );
	
	
	/** Max Stats Rows to Open Together help. */
	RHtml MAX_STATS_ROWS_TO_OPEN_TOGETHER       = new RHtml( "Max Stats Rows to Open Together", s( "max-stats-rows-to-open-together" ) );
	
	/** Max Session Break help. */
	RHtml MAX_SESSION_BREAK                     = new RHtml( "Max Session Break", s( "max-session-break" ) );
	
	
	/** Unit Tag Display Transformation. */
	RHtml UNIT_TAG_DISPLAY_TRANSFORMATION       = new RHtml( "Unit Tag Display Transformation", s( "unit-tag-display-transformation" ) );
	
	
	/** Replay List Table Custom Columns help. */
	RHtml REPLAY_LIST_TABLE_CUSTOM_COLS         = new RHtml( "Replay List Table Custom Columns", s( "replay-list-table-custom-cols" ) );
	
	
	/** Merged Accounts help. */
	RHtml MERGED_ACCOUNTS                       = new RHtml( "Merged Accounts", s( "merged-accounts" ) );
	
	
	/** Email Server (SMTP) help. */
	RHtml EMAIL_SERVER_SMTP                     = new RHtml( "Email Server (SMTP)", s( "email-server-smtp" ) );
	
	/** Sender Email Address help. */
	RHtml SENDER_EMAIL_ADDRESS                  = new RHtml( "Sender Email Address", s( "sender-email-address" ) );
	
	
	
	/**
	 * Utility class to help assemble resource URL strings.
	 * 
	 * @author Andras Belicza
	 */
	public static class ResUtil {
		
		/**
		 * Returns the resource string of the help content specified by its name.
		 * 
		 * @param name name of the help content whose URL to return
		 * @return the resource string of the help content specified by its name
		 */
		public static String h( final String name ) {
			return "html/help/" + name + ".html";
		}
		
		/**
		 * Returns the resource string of the setting help content specified by its name.
		 * 
		 * @param name name of the setting help content whose URL to return
		 * @return the resource string of the setting help content specified by its name
		 */
		public static String s( final String name ) {
			return "html/help/setting/" + name + ".html";
		}
		
	}
	
}
