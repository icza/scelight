/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight.gui.icon;

import static hu.scelight.gui.icon.Icons.ResUtil.co;
import static hu.scelight.gui.icon.Icons.ResUtil.f;
import static hu.scelight.gui.icon.Icons.ResUtil.my;
import static hu.scelight.gui.icon.Icons.ResUtil.sc2;

import hu.belicza.andras.util.type.BitArray;
import hu.belicza.andras.util.type.XString;
import hu.scelight.sc2.rep.model.details.Race;
import hu.scelight.sc2.rep.model.details.Result;
import hu.scelight.sc2.rep.model.initdata.gamedesc.ExpansionLevel;
import hu.scelight.sc2.rep.model.initdata.lobbystate.Controller;
import hu.scelight.sc2.rep.model.initdata.userinitdata.League;
import hu.scelight.service.sc2reg.GameStatus;
import hu.sllauncher.gui.icon.LIcons;

/**
 * Application icons collection.
 * 
 * <p>
 * <i>Implementation note:</i> This is an interface instead of a class because constants defined in an interface are implicitly <code>public</code>,
 * <code>static</code> and <code>final</code>, and so these keywords can be omitted!
 * </p>
 * 
 * @author Andras Belicza
 */
public interface Icons extends LIcons {
	
	// My icons
	
	/**
	 * My <code>apm</code> icon. Usage: APM.<br>
	 * <img src="../../r/icon/my/apm.png">
	 */
	RIcon MY_APM					  = my( "apm" );
									  
	/**
	 * My <code>automm</code> icon; based on the fugue <code>user-small</code> and <code>plus-small</code> icons. Usage: AutoMM Game mode.<br>
	 * <img src="../../r/icon/my/automm.png">
	 */
	RIcon MY_AUTOMM					  = my( "automm" );
									  
	/**
	 * My <code>charts</code> icon; based on the fugue <code>chart</code> icon. Usage: replay analyzers.<br>
	 * <img src="../../r/icon/my/charts.png">
	 */
	RIcon MY_CHARTS					  = my( "charts" );
									  
	/**
	 * My <code>chart-up-color-magnifier</code> icon; based on the fugue <code>chart-up-color</code> and <code>magnifier-small</code> icons. Usage: Analyze
	 * selected replay folders with custom filters.<br>
	 * <img src="../../r/icon/my/chart-up-color-magnifier.png">
	 */
	RIcon MY_CHART_UP_COLOR_MAGNIFIER = my( "chart-up-color-magnifier" );
									  
	/**
	 * My <code>chart-up-color-table-select-row</code> icon; based on the fugue <code>chart-up-color</code> and <code>table-select-row</code> icons. Usage:
	 * Multi-Replay Analyze Selected Replays.<br>
	 * <img src="../../r/icon/my/chart-up-color-table-select-row.png">
	 */
	RIcon MY_CHART_UP_CLR_TBL_SEL_ROW = my( "chart-up-color-table-select-row" );
									  
	/**
	 * My <code>chart-up-colors</code> icon; based on the fugue <code>chart-up-color</code> icon. Usage: Multi-Replay Analyzers page node.<br>
	 * <img src="../../r/icon/my/chart-up-colors.png">
	 */
	RIcon MY_CHART_UP_COLORS		  = my( "chart-up-colors" );
									  
	/**
	 * My <code>expansions</code> icon; based on the SC2 WoL and HotS icons. Usage: Expansion Level representer.<br>
	 * <img src="../../r/icon/my/expansions.png">
	 */
	RIcon MY_EXPANSIONS				  = my( "expansions" );
									  
	/**
	 * My <code>format</code> icon. Usage: Format representer.<br>
	 * <img src="../../r/icon/my/format.png">
	 */
	RIcon MY_FORMAT					  = my( "format" );
									  
	/**
	 * My <code>leagues</code> icon; based on the SC2 Bronze, Gold and Diamond icons. Usage: League representer.<br>
	 * <img src="../../r/icon/my/leagues.png">
	 */
	RIcon MY_LEAGUES				  = my( "leagues" );
									  
	/**
	 * My <code>modes</code> icon; based on the fugue <code>user-small</code>, <code>plus-small</code> icons and my AutoMM icon. Usage: Game mode representer.
	 * <br>
	 * <img src="../../r/icon/my/modes.png">
	 */
	RIcon MY_MODES					  = my( "modes" );
									  
	/**
	 * My <code>races</code> icon; based on the SC2 Protoss, Terran and Zerg icons. Usage: Race representer.<br>
	 * <img src="../../r/icon/my/races.png">
	 */
	RIcon MY_RACES					  = my( "races" );
									  
	/**
	 * My <code>results</code> icon; based on the SC2 Victory, Defeat and Tie icons. Usage: Result representer.<br>
	 * <img src="../../r/icon/my/results.png">
	 */
	RIcon MY_RESULTS				  = my( "results" );
									  
	/**
	 * My <code>spm</code> icon. Usage: SPM.<br>
	 * <img src="../../r/icon/my/spm.png">
	 */
	RIcon MY_SPM					  = my( "spm" );
									  
	/**
	 * My <code>scp</code> icon. Usage: Supply-capped %.<br>
	 * <img src="../../r/icon/my/scp.png">
	 */
	RIcon MY_SCP					  = my( "scp" );
									  
	/**
	 * My <code>sq</code> icon. Usage: SQ.<br>
	 * <img src="../../r/icon/my/sq.png">
	 */
	RIcon MY_SQ						  = my( "sq" );
									  
									  
									  
	// Misc icons
	
	/**
	 * Misc java icon. Usage: Java info page.<br>
	 * <img src="../../r/icon/misc/java.gif">
	 */
	RIcon MISC_JAVA					  = new RIcon( "icon/misc/java.gif" );
									  
									  
									  
	// SC2 icons
	
	/**
	 * SC2 icon. Usage: View characters profile.<br>
	 * <img src="../../r/icon/sc2/misc/profile.png">
	 */
	RIcon SC2_PROFILE				  = sc2( "misc/profile" );
									  
	/**
	 * SC2 icon. Usage: launch SC2.<br>
	 * <img src="../../r/icon/sc2/misc/sc2.png">
	 */
	RIcon SC2_ICON					  = sc2( "misc/sc2" );
									  
	/**
	 * SC2 icon. Usage: replay icon.<br>
	 * <img src="../../r/icon/sc2/misc/replay.png">
	 */
	RIcon SC2_REPLAY				  = sc2( "misc/replay" );
									  
	/**
	 * SC2 Editor icon. Usage: launch SC2 editor.<br>
	 * <img src="../../r/icon/sc2/misc/sc2_editor.png">
	 */
	RIcon SC2_EDITOR				  = sc2( "misc/sc2_editor" );
									  
	/**
	 * {@link Race#PROTOSS} race icon.<br>
	 * <img src="../../r/icon/sc2/race/protoss.png">
	 */
	RIcon SC2_PROTOSS				  = sc2( "race/protoss" );
									  
	/**
	 * {@link Race#TERRAN} race icon.<br>
	 * <img src="../../r/icon/sc2/race/terran.png">
	 */
	RIcon SC2_TERRAN				  = sc2( "race/terran" );
									  
	/**
	 * {@link Race#ZERG} race icon.<br>
	 * <img src="../../r/icon/sc2/race/zerg.png">
	 */
	RIcon SC2_ZERG					  = sc2( "race/zerg" );
									  
	/**
	 * {@link Result#VICTORY} result icon.<br>
	 * <img src="../../r/icon/sc2/result/victory.png">
	 */
	RIcon SC2_VICTORY				  = sc2( "result/victory" );
									  
	/**
	 * {@link Result#DEFEAT} result icon.<br>
	 * <img src="../../r/icon/sc2/result/defeat.png">
	 */
	RIcon SC2_DEFEAT				  = sc2( "result/defeat" );
									  
	/**
	 * {@link Result#TIE} result icon.<br>
	 * <img src="../../r/icon/sc2/result/tie.png">
	 */
	RIcon SC2_TIE					  = sc2( "result/tie" );
									  
	/**
	 * {@link League#BRONZE} league icon.<br>
	 * <img src="../../r/icon/sc2/league/BRONZE.png">
	 */
	RIcon SC2_BRONZE				  = sc2( "league/BRONZE" );
									  
	/**
	 * {@link League#SILVER} league icon.<br>
	 * <img src="../../r/icon/sc2/league/SILVER.png">
	 */
	RIcon SC2_SILVER				  = sc2( "league/SILVER" );
									  
	/**
	 * {@link League#GOLD} league icon.<br>
	 * <img src="../../r/icon/sc2/league/GOLD.png">
	 */
	RIcon SC2_GOLD					  = sc2( "league/GOLD" );
									  
	/**
	 * {@link League#PLATINUM} league icon.<br>
	 * <img src="../../r/icon/sc2/league/PLATINUM.png">
	 */
	RIcon SC2_PLATINUM				  = sc2( "league/PLATINUM" );
									  
	/**
	 * {@link League#DIAMOND} league icon.<br>
	 * <img src="../../r/icon/sc2/league/DIAMOND.png">
	 */
	RIcon SC2_DIAMOND				  = sc2( "league/DIAMOND" );
									  
	/**
	 * {@link League#MASTER} league icon.<br>
	 * <img src="../../r/icon/sc2/league/MASTER.png">
	 */
	RIcon SC2_MASTER				  = sc2( "league/MASTER" );
									  
	/**
	 * {@link League#GRANDMASTER} league icon.<br>
	 * <img src="../../r/icon/sc2/league/GRANDMASTER.png">
	 */
	RIcon SC2_GRANDMASTER			  = sc2( "league/GRANDMASTER" );
									  
	/**
	 * {@link League#UNRANKED} league icon.<br>
	 * <img src="../../r/icon/sc2/league/UNRANKED.png">
	 */
	RIcon SC2_UNRANKED				  = sc2( "league/UNRANKED" );
									  
	/**
	 * {@link ExpansionLevel#WOL} expansion level icon.<br>
	 * <img src="../../r/icon/sc2/expansion/wol.jpg">
	 */
	RIcon SC2_WOL					  = new RIcon( "icon/sc2/expansion/wol.jpg" );	// Cannot use sc2(), not png extension!
									  
	/**
	 * {@link ExpansionLevel#HOTS} expansion level icon.<br>
	 * <img src="../../r/icon/sc2/expansion/hots.jpg">
	 */
	RIcon SC2_HOTS					  = new RIcon( "icon/sc2/expansion/hots.jpg" );	// Cannot use sc2(), not png extension!
									  
	/**
	 * {@link ExpansionLevel#LOTV} expansion level icon.<br>
	 * <img src="../../r/icon/sc2/expansion/lotv.jpg">
	 */
	RIcon SC2_LOTV					  = new RIcon( "icon/sc2/expansion/lotv.jpg" );	// Cannot use sc2(), not png extension!
									  
									  
									  
	// Country flag icons
	
	/**
	 * Australia country flag.<br>
	 * <img src="../../r/icon/country/Australia.gif">
	 */
	RIcon C_AUSTRALIA				  = co( "Australia" );
									  
	/**
	 * Brazil country flag.<br>
	 * <img src="../../r/icon/country/Brazil.gif">
	 */
	RIcon C_BRAZIL					  = co( "Brazil" );
									  
	/**
	 * China country flag.<br>
	 * <img src="../../r/icon/country/China.gif">
	 */
	RIcon C_CHINA					  = co( "China" );
									  
	/**
	 * Europe country flag.<br>
	 * <img src="../../r/icon/country/Europe.gif">
	 */
	RIcon C_EUROPE					  = co( "Europe" );
									  
	/**
	 * France country flag.<br>
	 * <img src="../../r/icon/country/France.gif">
	 */
	RIcon C_FRANCE					  = co( "France" );
									  
	/**
	 * Germany country flag.<br>
	 * <img src="../../r/icon/country/Germany.gif">
	 */
	RIcon C_GERMANY					  = co( "Germany" );
									  
	/**
	 * Italy country flag.<br>
	 * <img src="../../r/icon/country/Italy.gif">
	 */
	RIcon C_ITALY					  = co( "Italy" );
									  
	/**
	 * Korea country flag.<br>
	 * <img src="../../r/icon/country/Korea.gif">
	 */
	RIcon C_KOREA					  = co( "Korea" );
									  
	/**
	 * Poland country flag.<br>
	 * <img src="../../r/icon/country/Poland.gif">
	 */
	RIcon C_POLAND					  = co( "Poland" );
									  
	/**
	 * Russia country flag.<br>
	 * <img src="../../r/icon/country/Russia.gif">
	 */
	RIcon C_RUSSIA					  = co( "Russia" );
									  
	/**
	 * Spain country flag.<br>
	 * <img src="../../r/icon/country/Spain.gif">
	 */
	RIcon C_SPAIN					  = co( "Spain" );
									  
									  
									  
	// Fugue icons
	
	/**
	 * Fugue <code>application-blue</code> icon. Usage: Window menu<br>
	 * <img src="../../r/icon/fugue/application-blue.png">
	 */
	RIcon F_APPLICATION_BLUE		  = f( "application-blue" );
									  
	/**
	 * Fugue <code>application-dock-tab</code> icon. Usage: Minimize to Tray<br>
	 * <img src="../../r/icon/fugue/application-dock-tab.png">
	 */
	RIcon F_APPLICATION_DOCK_TAB	  = f( "application-dock-tab" );
									  
	/**
	 * Fugue <code>application-sub</code> icon. Usage: Remember Window position on Start<br>
	 * <img src="../../r/icon/fugue/application-sub.png">
	 */
	RIcon F_APPLICATION_SUB			  = f( "application-sub" );
									  
	/**
	 * Fugue <code>arrow-circle-double</code> icon. Usage: Check for Updates<br>
	 * <img src="../../r/icon/fugue/arrow-circle-double.png">
	 */
	RIcon F_ARROW_CIRCLE_DOUBLE		  = f( "arrow-circle-double" );
									  
	/**
	 * Fugue <code>arrow-stop-270</code> icon. Usage: Minimize to Tray<br>
	 * <img src="../../r/icon/fugue/arrow-stop-270.png">
	 */
	RIcon F_ARROW_STOP_270			  = f( "arrow-stop-270" );
									  
	/**
	 * Fugue <code>balloon</code> icon. Usage: Chat message event<br>
	 * <img src="../../r/icon/fugue/balloon.png">
	 */
	RIcon F_BALLOON					  = f( "balloon" );
									  
	/**
	 * Fugue <code>balloons</code> icon. Usage: Chat tab in the Replay analyzer<br>
	 * <img src="../../r/icon/fugue/balloons.png">
	 */
	RIcon F_BALLOONS				  = f( "balloons" );
									  
	/**
	 * Fugue <code>binocular--arrow</code> icon. Usage: Latest rep finder job<br>
	 * <img src="../../r/icon/fugue/binocular--arrow.png">
	 */
	RIcon F_BINOCULAR_ARROW			  = f( "binocular--arrow" );
									  
	/**
	 * Fugue <code>block</code> icon. Usage: Build order<br>
	 * <img src="../../r/icon/fugue/block.png">
	 */
	RIcon F_BLOCK					  = f( "block" );
									  
	/**
	 * Fugue <code>blue-document--arrow</code> icon. Usage: move replays<br>
	 * <img src="../../r/icon/fugue/blue-document--arrow.png">
	 */
	RIcon F_BLUE_DOCUMENT_ARROW		  = f( "blue-document--arrow" );
									  
	/**
	 * Fugue <code>blue-document-copy</code> icon. Usage: copy replays<br>
	 * <img src="../../r/icon/fugue/blue-document-copy.png">
	 */
	RIcon F_BLUE_DOCUMENT_COPY		  = f( "blue-document-copy" );
									  
	/**
	 * Fugue <code>blue-document-rename</code> icon. Usage: rename replays<br>
	 * <img src="../../r/icon/fugue/blue-document-rename.png">
	 */
	RIcon F_BLUE_DOCUMENT_RENAME	  = f( "blue-document-rename" );
									  
	/**
	 * Fugue <code>blue-document-zipper</code> icon. Usage: pack replays<br>
	 * <img src="../../r/icon/fugue/blue-document-zipper.png">
	 */
	RIcon F_BLUE_DOCUMENT_ZIPPER	  = f( "blue-document-zipper" );
									  
	/**
	 * Fugue <code>blue-document</code> icon. Usage: Choose file button in the template editor component<br>
	 * <img src="../../r/icon/fugue/blue-document.png">
	 */
	RIcon F_BLUE_DOCUMENT			  = f( "blue-document" );
									  
	/**
	 * Fugue <code>blue-folder--arrow</code> icon. Usage: Start folder when choosing replays<br>
	 * <img src="../../r/icon/fugue/blue-folder--arrow.png">
	 */
	RIcon F_BLUE_FOLDER_ARROW		  = f( "blue-folder--arrow" );
									  
	/**
	 * Fugue <code>blue-folder--minus</code> icon. Usage: remove replay folder<br>
	 * <img src="../../r/icon/fugue/blue-folder--minus.png">
	 */
	RIcon F_BLUE_FOLDER_MINUS		  = f( "blue-folder--minus" );
									  
	/**
	 * Fugue <code>blue-folder--pencil</code> icon. Usage: edit replay folder, make rep folders table editable<br>
	 * <img src="../../r/icon/fugue/blue-folder--pencil.png">
	 */
	RIcon F_BLUE_FOLDER_PENCIL		  = f( "blue-folder--pencil" );
									  
	/**
	 * Fugue <code>blue-folder--plus</code> icon. Usage: add replay folder<br>
	 * <img src="../../r/icon/fugue/blue-folder--plus.png">
	 */
	RIcon F_BLUE_FOLDER_PLUS		  = f( "blue-folder--plus" );
									  
	/**
	 * Fugue <code>blue-folder--bookmark</code> icon. Usage: built-in replay folder (e.g. replay backup folder)<br>
	 * <img src="../../r/icon/fugue/blue-folder-bookmark.png">
	 */
	RIcon F_BLUE_FOLDER_BOOKMARK	  = f( "blue-folder-bookmark" );
									  
	/**
	 * Fugue <code>blue-folder-open-table</code> icon. Usage: open replay folders, replay list<br>
	 * <img src="../../r/icon/fugue/blue-folder-open-table.png">
	 */
	RIcon F_BLUE_FOLDER_OPEN_TABLE	  = f( "blue-folder-open-table" );
									  
	/**
	 * Fugue <code>blue-folder-search-result</code> icon. Usage: search replay folder, replay folder monitor<br>
	 * <img src="../../r/icon/fugue/blue-folder-search-result.png">
	 */
	RIcon F_BLUE_FOLDER_SEARCH_RESULT = f( "blue-folder-search-result" );
									  
	/**
	 * Fugue <code>blue-folder-tree</code> icon. Usage: Folder separator symbol<br>
	 * <img src="../../r/icon/fugue/blue-folder-tree.png">
	 */
	RIcon F_BLUE_FOLDER_TREE		  = f( "blue-folder-tree" );
									  
	/**
	 * Fugue <code>blue-folder</code> icon. Usage: replay folder (user originated)<br>
	 * <img src="../../r/icon/fugue/blue-folder.png">
	 */
	RIcon F_BLUE_FOLDER				  = f( "blue-folder" );
									  
	/**
	 * Fugue <code>blue-folders-stack</code> icon. Usage: replay folders<br>
	 * <img src="../../r/icon/fugue/blue-folders-stack.png">
	 */
	RIcon F_BLUE_FOLDERS_STACK		  = f( "blue-folders-stack" );
									  
	/**
	 * Fugue <code>calendar-blue</code> icon. Usage: Date symbol<br>
	 * <img src="../../r/icon/fugue/calendar-blue.png">
	 */
	RIcon F_CALENDAR_BLUE			  = f( "calendar-blue" );
									  
	/**
	 * Fugue <code>calendar-relation</code> icon. Usage: Multi-Replay Analyzer Timelapse tab<br>
	 * <img src="../../r/icon/fugue/calendar-relation.png">
	 */
	RIcon F_CALENDAR_RELATION		  = f( "calendar-relation" );
									  
	/**
	 * Fugue <code>calendar-select-month</code> icon. Usage: Multi-Replay Analyzer Month<br>
	 * <img src="../../r/icon/fugue/calendar-select-month.png">
	 */
	RIcon F_CALENDAR_SELECT_MONTH	  = f( "calendar-select-month" );
									  
	/**
	 * Fugue <code>calendar-select-week</code> icon. Usage: Multi-Replay Analyzer Week<br>
	 * <img src="../../r/icon/fugue/calendar-select-week.png">
	 */
	RIcon F_CALENDAR_SELECT_WEEK	  = f( "calendar-select-week" );
									  
	/**
	 * Fugue <code>calendar-select</code> icon. Usage: Multi-Replay Analyzer Day<br>
	 * <img src="../../r/icon/fugue/calendar-select.png">
	 */
	RIcon F_CALENDAR_SELECT			  = f( "calendar-select" );
									  
	/**
	 * Fugue <code>calendar</code> icon. Usage: Multi-Replay Analyzer Year<br>
	 * <img src="../../r/icon/fugue/calendar.png">
	 */
	RIcon F_CALENDAR				  = f( "calendar" );
									  
	/**
	 * Fugue <code>camera-lens</code> icon. Usage: CameraUpdate game event<br>
	 * <img src="../../r/icon/fugue/camera-lens.png">
	 */
	RIcon F_CAMERA_LENS				  = f( "camera-lens" );
									  
	/**
	 * Fugue <code>card</code> icon. Usage: Original name symbol<br>
	 * <img src="../../r/icon/fugue/card.png">
	 */
	RIcon F_CARD					  = f( "card" );
									  
	/**
	 * Fugue <code>categories</code> icon. Usage: Components page<br>
	 * <img src="../../r/icon/fugue/categories.png">
	 */
	RIcon F_CATEGORIES				  = f( "categories" );
									  
	/**
	 * Fugue <code>chain</code> icon. Usage: Multi-Replay Analyzer Session tab<br>
	 * <img src="../../r/icon/fugue/chain.png">
	 */
	RIcon F_CHAIN					  = f( "chain" );
									  
	/**
	 * Fugue <code>chart--arrow</code> icon. Usage: Open last replay<br>
	 * <img src="../../r/icon/fugue/chart--arrow.png">
	 */
	RIcon F_CHART_ARROW				  = f( "chart--arrow" );
									  
	/**
	 * Fugue <code>chart--pencil</code> icon. Usage: Chart specific options settings node<br>
	 * <img src="../../r/icon/fugue/chart--pencil.png">
	 */
	RIcon F_CHART_PENCIL			  = f( "chart--pencil" );
									  
	/**
	 * Fugue <code>chart-up-color</code> icon. Usage: Multi-Replay Analyzer<br>
	 * <img src="../../r/icon/fugue/chart-up-color.png">
	 */
	RIcon F_CHART_UP_COLOR			  = f( "chart-up-color" );
									  
	/**
	 * Fugue <code>chart-up</code> icon. Usage: Open the Details of the Selected Player in the Multi-replay Analyzer<br>
	 * <img src="../../r/icon/fugue/chart-up.png">
	 */
	RIcon F_CHART_UP				  = f( "chart-up" );
									  
	/**
	 * Fugue <code>chart</code> icon. Usage: Replay analyzer, Quick Open Replay<br>
	 * <img src="../../r/icon/fugue/chart.png">
	 */
	RIcon F_CHART					  = f( "chart" );
									  
	/**
	 * Fugue <code>clipboard-sign</code> icon. Usage: Copy toon<br>
	 * <img src="../../r/icon/fugue/clipboard-sign.png">
	 */
	RIcon F_CLIPBOARD_SIGN			  = f( "clipboard-sign" );
									  
	/**
	 * Fugue <code>clock-moon-phase</code> icon. Usage: Multi-Replay Analyzer Hour<br>
	 * <img src="../../r/icon/fugue/clock-moon-phase.png">
	 */
	RIcon F_CLOCK_MOON_PHASE		  = f( "clock-moon-phase" );
									  
	/**
	 * Fugue <code>clock-select</code> icon. Usage: Length symbol<br>
	 * <img src="../../r/icon/fugue/clock-select.png">
	 */
	RIcon F_CLOCK_SELECT			  = f( "clock-select" );
									  
	/**
	 * Fugue <code>compile</code> icon. Usage: Replay processor<br>
	 * <img src="../../r/icon/fugue/compile.png">
	 */
	RIcon F_COMPILE					  = f( "compile" );
									  
	/**
	 * Fugue <code>computer</code> icon. Usage: System info about page, {@link Controller#COMPUTER}<br>
	 * <img src="../../r/icon/fugue/computer.png">
	 */
	RIcon F_COMPUTER				  = f( "computer" );
									  
	/**
	 * Fugue <code>counter</code> icon. Usage: rep counter job, Counter symbol<br>
	 * <img src="../../r/icon/fugue/counter.png">
	 */
	RIcon F_COUNTER					  = f( "counter" );
									  
	/**
	 * Fugue <code>cross-small</code> icon. Usage: Close tab<br>
	 * <img src="../../r/icon/fugue/cross-small.png">
	 */
	RIcon F_CROSS_SMALL				  = f( "cross-small" );
									  
	/**
	 * Fugue <code>direction</code> icon. Usage: Start direction symbol<br>
	 * <img src="../../r/icon/fugue/direction.png">
	 */
	RIcon F_DIRECTION				  = f( "direction" );
									  
	/**
	 * Fugue <code>disk</code> icon. Usage: file menu<br>
	 * <img src="../../r/icon/fugue/disk.png">
	 */
	RIcon F_DISK					  = f( "disk" );
									  
	/**
	 * Fugue <code>document-attribute-b</code> icon. Usage: tree leaf icon for {@link Boolean} value in the Raw Tree Data tab in the Replay analyzer.<br>
	 * <img src="../../r/icon/fugue/document-attribute-b.png">
	 */
	RIcon F_DOCUMENT_ATTRIBUTE_B	  = f( "document-attribute-b" );
									  
	/**
	 * Fugue <code>document-attribute-e</code> icon. Usage: Environment variables page.<br>
	 * <img src="../../r/icon/fugue/document-attribute-e.png">
	 */
	RIcon F_DOCUMENT_ATTRIBUTE_E	  = f( "document-attribute-e" );
									  
	/**
	 * Fugue <code>document-attribute-i</code> icon. Usage: tree leaf icon for {@link Integer} value in the Raw Tree Data tab in the Replay analyzer.<br>
	 * <img src="../../r/icon/fugue/document-attribute-i.png">
	 */
	RIcon F_DOCUMENT_ATTRIBUTE_I	  = f( "document-attribute-i" );
									  
	/**
	 * Fugue <code>document-attribute-l</code> icon. Usage: tree leaf icon for {@link Long} value in the Raw Tree Data tab in the Replay analyzer.<br>
	 * <img src="../../r/icon/fugue/document-attribute-l.png">
	 */
	RIcon F_DOCUMENT_ATTRIBUTE_L	  = f( "document-attribute-l" );
									  
	/**
	 * Fugue <code>document-attribute-s</code> icon. Usage: tree leaf icon for string ({@link XString}) value in the Raw Tree Data tab in the Replay analyzer;
	 * Java system properties page.<br>
	 * <img src="../../r/icon/fugue/document-attribute-s.png">
	 */
	RIcon F_DOCUMENT_ATTRIBUTE_S	  = f( "document-attribute-s" );
									  
	/**
	 * Fugue <code>document-attribute-v</code> icon. Usage: Version symbol<br>
	 * <img src="../../r/icon/fugue/document-attribute-v.png">
	 */
	RIcon F_DOCUMENT_ATTRIBUTE_V	  = f( "document-attribute-v" );
									  
	/**
	 * Fugue <code>document-binary</code> icon. Usage: Binary Data tab in the Replay analyzer; tree leaf icon for binary ( {@link BitArray}) value in the Raw
	 * Tree Data tab in the Replay analyzer.<br>
	 * <img src="../../r/icon/fugue/document-binary.png">
	 */
	RIcon F_DOCUMENT_BINARY			  = f( "document-binary" );
									  
	/**
	 * Fugue <code>document-copy</code> icon. Usage: Copy all lines to clipboard in the Raw Text Data tab in the Replay analyzer.<br>
	 * <img src="../../r/icon/fugue/document-copy.png">
	 */
	RIcon F_DOCUMENT_COPY			  = f( "document-copy" );
									  
	/**
	 * Fugue <code>document-sub</code> icon. Usage: Copy selected lines to clipboard in the Raw Text Data tab in the Replay analyzer.<br>
	 * <img src="../../r/icon/fugue/document-sub.png">
	 */
	RIcon F_DOCUMENT_SUB			  = f( "document-sub" );
									  
	/**
	 * Fugue <code>document-text</code> icon. Usage: Raw Text Data tab in the Replay analyzer.<br>
	 * <img src="../../r/icon/fugue/document-text.png">
	 */
	RIcon F_DOCUMENT_TEXT			  = f( "document-text" );
									  
	/**
	 * Fugue <code>document-tree</code> icon. Usage: Raw Data Tree tab in the Replay analyzer.<br>
	 * <img src="../../r/icon/fugue/document-tree.png">
	 */
	RIcon F_DOCUMENT_TREE			  = f( "document-tree" );
									  
	/**
	 * Fugue <code>door-open-in</code> icon. Usage: exit<br>
	 * <img src="../../r/icon/fugue/door-open-in.png">
	 */
	RIcon F_DOOR_OPEN_IN			  = f( "door-open-in" );
									  
	/**
	 * Fugue <code>door-open</code> icon. Usage: Public Game mode<br>
	 * <img src="../../r/icon/fugue/door-open.png">
	 */
	RIcon F_DOOR_OPEN				  = f( "door-open" );
									  
	/**
	 * Fugue <code>door</code> icon. Usage: Private Game mode<br>
	 * <img src="../../r/icon/fugue/door.png">
	 */
	RIcon F_DOOR					  = f( "door" );
									  
	/**
	 * Fugue <code>drive</code> icon. Usage: Replay backup<br>
	 * <img src="../../r/icon/fugue/drive.png">
	 */
	RIcon F_DRIVE					  = f( "drive" );
									  
	/**
	 * Fugue <code>edit-color</code> icon. Usage: Font color in Live APM overlay config menu<br>
	 * <img src="../../r/icon/fugue/edit-color.png">
	 */
	RIcon F_EDIT_COLOR				  = f( "edit-color" );
									  
	/**
	 * Fugue <code>edit-column</code> icon. Usage: Column setup in the Rep list page<br>
	 * <img src="../../r/icon/fugue/edit-column.png">
	 */
	RIcon F_EDIT_COLUMN				  = f( "edit-column" );
									  
	/**
	 * Fugue <code>edit-v</code> icon. Usage: Multi-Replay Analyzer Metric trend tab<br>
	 * <img src="../../r/icon/fugue/edit-mathematics.png">
	 */
	RIcon F_EDIT_MATHEMATICS		  = f( "edit-mathematics" );
									  
	/**
	 * Fugue <code>edit-shade</code> icon. Usage: Background color in Live APM overlay config menu<br>
	 * <img src="../../r/icon/fugue/edit-shade.png">
	 */
	RIcon F_EDIT_SHADE				  = f( "edit-shade" );
									  
	/**
	 * Fugue <code>edit-size</code> icon. Usage: Live APM Overlay Font size<br>
	 * <img src="../../r/icon/fugue/edit-size.png">
	 */
	RIcon F_EDIT_SIZE				  = f( "edit-size" );
									  
	/**
	 * Fugue <code>equalizer</code> icon. Usage: Miscellaneous settings<br>
	 * <img src="../../r/icon/fugue/equalizer.png">
	 */
	RIcon F_EQUALIZER				  = f( "equalizer" );
									  
	/**
	 * Fugue <code>eye-close</code> icon. Usage: Hide / make invisible<br>
	 * <img src="../../r/icon/fugue/eye-close.png">
	 */
	RIcon F_EYE_CLOSE				  = f( "eye-close" );
									  
	/**
	 * Fugue <code>eye</code> icon. Usage: Show / make visible<br>
	 * <img src="../../r/icon/fugue/eye.png">
	 */
	RIcon F_EYE						  = f( "eye" );
									  
	/**
	 * Fugue <code>funnel--minus</code> icon. Usage: Remove filter<br>
	 * <img src="../../r/icon/fugue/funnel--minus.png">
	 */
	RIcon F_FUNNEL_MINUS			  = f( "funnel--minus" );
									  
	/**
	 * Fugue <code>funnel--pencil</code> icon. Usage: Edit Filters<br>
	 * <img src="../../r/icon/fugue/funnel--pencil.png">
	 */
	RIcon F_FUNNEL_PENCIL			  = f( "funnel--pencil" );
									  
	/**
	 * Fugue <code>funnel--plus</code> icon. Usage: Add new filter<br>
	 * <img src="../../r/icon/fugue/funnel--plus.png">
	 */
	RIcon F_FUNNEL_PLUS				  = f( "funnel--plus" );
									  
	/**
	 * Fugue <code>globe-green</code> icon. Usage: Region symbol<br>
	 * <img src="../../r/icon/fugue/globe-green.png">
	 */
	RIcon F_GLOBE_GREEN				  = f( "globe-green" );
									  
	/**
	 * Fugue <code>hard-hat-military</code> icon. Usage: there are running jobs<br>
	 * <img src="../../r/icon/fugue/hard-hat-military.png">
	 */
	RIcon F_HARD_HAT_MILITARY		  = f( "hard-hat-military" );
									  
	/**
	 * Fugue <code>hard-hat</code> icon. Usage: View running jobs menu, no running jobs<br>
	 * <img src="../../r/icon/fugue/hard-hat.png">
	 */
	RIcon F_HARD_HAT				  = f( "hard-hat" );
									  
	/**
	 * Fugue <code>information-balloon</code> icon. Usage: Game Info tab of the Replay analyzer<br>
	 * <img src="../../r/icon/fugue/information-balloon.png">
	 */
	RIcon F_INFORMATION_BALLOON		  = f( "information-balloon" );
									  
	/**
	 * Fugue <code>information</code> icon. Usage: about<br>
	 * <img src="../../r/icon/fugue/information.png">
	 */
	RIcon F_INFORMATION				  = f( "information" );
									  
	/**
	 * Fugue <code>keyboard</code> icon. Usage: ControlGroupUpdate game event<br>
	 * <img src="../../r/icon/fugue/keyboard.png">
	 */
	RIcon F_KEYBOARD				  = f( "keyboard" );
									  
	/**
	 * Fugue <code>layer-transparent</code> icon. Usage: Overlay opacity<br>
	 * <img src="../../r/icon/fugue/layer-transparent.png">
	 */
	RIcon F_LAYER_TRANSPARENT		  = f( "layer-transparent" );
									  
	/**
	 * Fugue <code>layers-arrange</code> icon. Usage: Overlay focusable<br>
	 * <img src="../../r/icon/fugue/layers-arrange.png">
	 */
	RIcon F_LAYER_ARRANGE			  = f( "layers-arrange" );
									  
	/**
	 * Fugue <code>layers-group</code> icon. Usage: Overlay allow outside the main screen<br>
	 * <img src="../../r/icon/fugue/layers-group.png">
	 */
	RIcon F_LAYER_GROUP				  = f( "layers-group" );
									  
	/**
	 * Fugue <code>lock</code> icon. Usage: Lock overlay<br>
	 * <img src="../../r/icon/fugue/lock.png">
	 */
	RIcon F_LOCK					  = f( "lock" );
									  
	/**
	 * Fugue <code>magnifier-zoom-out</code> icon. Usage: Reset zoom<br>
	 * <img src="../../r/icon/fugue/magnifier-zoom-out.png">
	 */
	RIcon F_MAGNIFIER_ZOOM_OUT		  = f( "magnifier-zoom-out" );
									  
	/**
	 * Fugue <code>magnifier-zoom-fit</code> icon. Usage: Zoom into markers<br>
	 * <img src="../../r/icon/fugue/magnifier-zoom-fit.png">
	 */
	RIcon F_MAGNIFIER_ZOOM_FIT		  = f( "magnifier-zoom-fit" );
									  
	/**
	 * Fugue <code>map-pin</code> icon. Usage: Ping message event<br>
	 * <img src="../../r/icon/fugue/map-pin.png">
	 */
	RIcon F_MAP_PIN					  = f( "map-pin" );
									  
	/**
	 * Fugue <code>map</code> icon. Usage: Map name symbol<br>
	 * <img src="../../r/icon/fugue/map.png">
	 */
	RIcon F_MAP						  = f( "map" );
									  
	/**
	 * Fugue <code>maps-stack</code> icon. Usage: Multi-Replay Analyzer Map<br>
	 * <img src="../../r/icon/fugue/maps-stack.png">
	 */
	RIcon F_MAPS_STACK				  = f( "maps-stack" );
									  
	/**
	 * Fugue <code>minus-button</code> icon. Usage: Collapse all nodes (in tree)<br>
	 * <img src="../../r/icon/fugue/minus-button.png">
	 */
	RIcon F_MINUS_BUTTON			  = f( "minus-button" );
									  
	/**
	 * Fugue <code>minus-circle-frame</code> icon. Usage: Collapse selected nodes (in tree)<br>
	 * <img src="../../r/icon/fugue/minus-circle-frame.png">
	 */
	RIcon F_MINUS_CIRCLE_FRAME		  = f( "minus-circle-frame" );
									  
	/**
	 * Fugue <code>money-coin</code> icon. Usage: Resource trade/request/fulfil events<br>
	 * <img src="../../r/icon/fugue/money-coin.png">
	 */
	RIcon F_MONEY_COINT				  = f( "money-coin" );
									  
	/**
	 * Fugue <code>mouse-select-right</code> icon. Usage: Right click event<br>
	 * <img src="../../r/icon/fugue/mouse-select-right.png">
	 */
	RIcon F_MOUSE_SELECT_RIGHT		  = f( "mouse-select-right" );
									  
	/**
	 * Fugue <code>network-status-offline</code> icon. Usage: Indicate "offline" mode<br>
	 * <img src="../../r/icon/fugue/network-status-offline.png">
	 */
	RIcon F_NETWORK_STATUS_OFFLINE	  = f( "network-status-offline" );
									  
	/**
	 * Fugue <code>node-insert</code> icon. Usage: Insert selected template elements<br>
	 * <img src="../../r/icon/fugue/node-insert.png">
	 */
	RIcon F_NODE_INSERT				  = f( "node-insert" );
									  
	/**
	 * Fugue <code>pencil</code> icon. Usage: Open template editor dialog<br>
	 * <img src="../../r/icon/fugue/pencil.png">
	 */
	RIcon F_PENCIL					  = f( "pencil" );
									  
	/**
	 * Fugue <code>picture-sunset</code> icon. Usage: View menu<br>
	 * <img src="../../r/icon/fugue/picture-sunset.png">
	 */
	RIcon F_PICTURE_SUNSET			  = f( "picture-sunset" );
									  
	/**
	 * Fugue <code>plus-button</code> icon. Usage: Expand all nodes (in tree)<br>
	 * <img src="../../r/icon/fugue/plus-button.png">
	 */
	RIcon F_PLUS_BUTTON				  = f( "plus-button" );
									  
	/**
	 * Fugue <code>plus-circle-frame</code> icon. Usage: Expand selected nodes (in tree)<br>
	 * <img src="../../r/icon/fugue/plus-circle-frame.png">
	 */
	RIcon F_PLUS_CIRCLE_FRAME		  = f( "plus-circle-frame" );
									  
	/**
	 * Fugue <code>selection-select</code> icon. Usage: SelectionDelta game event<br>
	 * <img src="../../r/icon/fugue/selection-select.png">
	 */
	RIcon F_SELECTION_SELECT		  = f( "selection-select" );
									  
	/**
	 * Fugue <code>sql-join-inner</code> icon. Usage: Filter logical AND connection<br>
	 * <img src="../../r/icon/fugue/sql-join-inner.png">
	 */
	RIcon F_SQL_JOIN_INNER			  = f( "sql-join-inner" );
									  
	/**
	 * Fugue <code>sql-join-outer</code> icon. Usage: Filter logical OR connection<br>
	 * <img src="../../r/icon/fugue/sql-join-outer.png">
	 */
	RIcon F_SQL_JOIN_OUTER			  = f( "sql-join-outer" );
									  
	/**
	 * Fugue <code>status-busy</code> icon. Usage: {@link GameStatus#ENDED} game status.<br>
	 * <img src="../../r/icon/fugue/status-busy.png">
	 */
	RIcon F_STATUS_BUSY				  = f( "status-busy" );
									  
	/**
	 * Fugue <code>status-offline</code> icon. Usage: {@link GameStatus#UNKNOWN} game status.<br>
	 * <img src="../../r/icon/fugue/status-offline.png">
	 */
	RIcon F_STATUS_OFFLINE			  = f( "status-offline" );
									  
	/**
	 * Fugue <code>status</code> icon. Usage: {@link GameStatus#STARTED} game status.<br>
	 * <img src="../../r/icon/fugue/status.png">
	 */
	RIcon F_STATUS					  = f( "status" );
									  
	/**
	 * Fugue <code>sum</code> icon. Usage: Global stats.<br>
	 * <img src="../../r/icon/fugue/sum.png">
	 */
	RIcon F_SUM						  = f( "sum" );
									  
	/**
	 * Fugue <code>table-select-column</code> icon. Usage: Custom columns in the replay list table.<br>
	 * <img src="../../r/icon/fugue/table-select-column.png">
	 */
	RIcon F_TABLE_SELECT_COLUMN		  = f( "table-select-column" );
									  
	/**
	 * Fugue <code>table</code> icon. Usage: Events table settings of the Replay analyzer.<br>
	 * <img src="../../r/icon/fugue/table.png">
	 */
	RIcon F_TABLE					  = f( "table" );
									  
	/**
	 * Fugue <code>tags</code> icon. Usage: Merged accounts settings.<br>
	 * <img src="../../r/icon/fugue/tags.png">
	 */
	RIcon F_TAGS					  = f( "tags" );
									  
	/**
	 * Fugue <code>tick-red</code> icon. Usage: Disable selected (filters).<br>
	 * <img src="../../r/icon/fugue/tick-red.png">
	 */
	RIcon F_TICK_RED				  = f( "tick-red" );
									  
	/**
	 * Fugue <code>toolbox</code> icon. Usage: Tools menu<br>
	 * <img src="../../r/icon/fugue/toolbox.png">
	 */
	RIcon F_TOOLBOX					  = f( "toolbox" );
									  
	/**
	 * Fugue <code>ui-check-box</code> icon. Usage: Select all users in the Replay analyzer<br>
	 * <img src="../../r/icon/fugue/ui-check-box.png">
	 */
	RIcon F_UI_CHECK_BOX			  = f( "ui-check-box" );
									  
	/**
	 * Fugue <code>ui-check-box-uncheck</code> icon. Usage: Deselect all users in the Replay analyzer<br>
	 * <img src="../../r/icon/fugue/ui-check-box-uncheck.png">
	 */
	RIcon F_UI_CHECK_BOX_UNCHECK	  = f( "ui-check-box-uncheck" );
									  
	/**
	 * Fugue <code>ui-status-bar</code> icon. Usage: status bar settings<br>
	 * <img src="../../r/icon/fugue/ui-status-bar.png">
	 */
	RIcon F_UI_STATUS_BAR			  = f( "ui-status-bar" );
									  
	/**
	 * Fugue <code>user--minus</code> icon. Usage: Remove from the Favored player list<br>
	 * <img src="../../r/icon/fugue/user--minus.png">
	 */
	RIcon F_USER_MINUS				  = f( "user--minus" );
									  
	/**
	 * Fugue <code>user--plus</code> icon. Usage: Add to the Favored player list<br>
	 * <img src="../../r/icon/fugue/user--plus.png">
	 */
	RIcon F_USER_PLUS				  = f( "user--plus" );
									  
	/**
	 * Fugue <code>user-detective</code> icon. Usage: Inspector tab of the Replay analyzer<br>
	 * <img src="../../r/icon/fugue/user-detective.png">
	 */
	RIcon F_USER_DETECTIVE			  = f( "user-detective" );
									  
	/**
	 * Fugue <code>user</code> icon. Usage: {@link Controller#HUMAN}<br>
	 * <img src="../../r/icon/fugue/user.png">
	 */
	RIcon F_USER					  = f( "user" );
									  
	/**
	 * Fugue <code>user-green</code> icon. Usage: Player X filter by group<br>
	 * <img src="../../r/icon/fugue/user-green.png">
	 */
	RIcon F_USER_GREEN				  = f( "user-green" );
									  
	/**
	 * Fugue <code>user-red</code> icon. Usage: Player X filter by group<br>
	 * <img src="../../r/icon/fugue/user-red.png">
	 */
	RIcon F_USER_RED				  = f( "user-red" );
									  
	/**
	 * Fugue <code>user-silhouette-question</code> icon. Usage: Any Player filter by group<br>
	 * <img src="../../r/icon/fugue/user-silhouette-question.png">
	 */
	RIcon F_USER_SILHOUETTE_QUESTION  = f( "user-silhouette-question" );
									  
	/**
	 * Fugue <code>user-silhouette</code> icon. Usage: All Players filter by group<br>
	 * <img src="../../r/icon/fugue/user-silhouette.png">
	 */
	RIcon F_USER_SILHOUETTE			  = f( "user-silhouette" );
									  
	/**
	 * Fugue <code>user-small</code> icon. Usage: Single player Game mode<br>
	 * <img src="../../r/icon/fugue/user-small.png">
	 */
	RIcon F_USER_SMALL				  = f( "user-small" );
									  
	/**
	 * Fugue <code>user-yellow</code> icon. Usage: Player X filter by group<br>
	 * <img src="../../r/icon/fugue/user-yellow.png">
	 */
	RIcon F_USER_YELLOW				  = f( "user-yellow" );
									  
	/**
	 * Fugue <code>users</code> icon. Usage: Users tab of the Replay analyzer<br>
	 * <img src="../../r/icon/fugue/users.png">
	 */
	RIcon F_USERS					  = f( "users" );
									  
	/**
	 * Fugue <code>wrench</code> icon. Usage: Player menu<br>
	 * <img src="../../r/icon/fugue/wrench.png">
	 */
	RIcon F_WRENCH					  = f( "wrench" );
									  
									  
									  
	/**
	 * Utility class to help assemble resource URL strings.
	 * 
	 * @author Andras Belicza
	 */
	public static class ResUtil {
		
		/**
		 * Returns the sc2 image icon resource specified by its name.
		 * 
		 * @param name name of the sc2 image whose icon resource to return
		 * @return the sc2 icon resource specified by its name
		 */
		public static RIcon sc2( final String name ) {
			return new RIcon( "icon/sc2/" + name + ".png" );
		}
		
		/**
		 * Returns the fugue image icon resource specified by its name.
		 * 
		 * @param name name of the fugue icon whose icon resource to return
		 * @return the fugue icon resource specified by its name
		 */
		public static RIcon f( final String name ) {
			return new RIcon( hu.sllauncher.gui.icon.LIcons.ResUtil.f_( name ) );
		}
		
		/**
		 * Returns the my image icon resource specified by its name.
		 * 
		 * @param name name of my image whose icon resource to return
		 * @return the my image icon resource specified by its name
		 */
		public static RIcon my( final String name ) {
			return new RIcon( hu.sllauncher.gui.icon.LIcons.ResUtil.my_( name ) );
		}
		
		/**
		 * Returns the the country image icon resource specified by its name.
		 * 
		 * @param name name of the country image whose icon resource to return
		 * @return the country image icon resource specified by its name
		 */
		public static RIcon co( final String name ) {
			return new RIcon( hu.sllauncher.gui.icon.LIcons.ResUtil.co_( name ) );
		}
	}
	
}
