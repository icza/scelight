/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.sllauncher.gui.help;

import static hu.sllauncher.gui.help.LHelps.ResUtil.h;
import static hu.sllauncher.gui.help.LHelps.ResUtil.s;
import hu.sllauncher.service.env.bootsettings.WorkspaceLocationType;
import hu.sllauncher.service.log.LogLevel;
import hu.sllauncher.util.LRHtml;

/**
 * Launcher help HTML template resource collection.
 * 
 * <p>
 * <i>Implementation note:</i> This is an interface instead of a class because constants defined in an interface are implicitly <code>public</code>,
 * <code>static</code> and <code>final</code>, and so these keywords can be omitted!
 * </p>
 * 
 * @author Andras Belicza
 */
public interface LHelps {
	
	/** Help Legend help. */
	LRHtml HELP_LEGEND                     = new LRHtml( "Help Legend", h( "help-legend" ) );
	
	/** Computer Skill Level help. */
	LRHtml COMP_SKILL_LEVEL                = new LRHtml( "Computer Skill Level", h( "computer-skill-level" ) );
	
	/** Invalid Registration File help. */
	LRHtml INVALID_REGISTRATION_FILE       = new LRHtml( "Invalid Registration File", h( "invalid-registration-file" ) );
	
	/** Registration File Expired help. */
	LRHtml REGISTRATION_FILE_EXPIRED       = new LRHtml( "Registration File Expired", h( "registration-file-expired" ) );
	
	/** Registered System Info Mismatch help. */
	LRHtml REGISTERED_SYSTEM_INFO_MISMATCH = new LRHtml( "Registered System Info Mismatch", h( "registered-system-info-mismatch" ) );
	
	/** Filter Settings help. */
	LRHtml SETTINGS_FILTER                 = new LRHtml( "Settings Filter", h( "settings-filter" ) );
	
	/** Table Filters help. */
	LRHtml TABLE_FILTERS                   = new LRHtml( "Table Filters", h( "table-filters" ) );
	
	/** External Modules help. */
	LRHtml EXTERNAL_MODULES                = new LRHtml( "External Modules", h( "external-modules" ) );
	
	/** Official External Modules help. */
	LRHtml OFFICIAL_EXTERNAL_MODULES       = new LRHtml( "Official External Modules", h( "official-external-modules" ) );
	
	/** Official External Modules help. */
	LRHtml INSTALLED_EXTERNAL_MODULES      = new LRHtml( "Installed External Modules", h( "installed-external-modules" ) );
	
	
	// LAUNCHER SETTING HELPS
	
	/** Workspace help. */
	LRHtml WORKSPACE                       = new LRHtml( "Workspace", s( "workspace" ), "userHome", WorkspaceLocationType.USER_HOME.text, "appFolder",
	                                               WorkspaceLocationType.APP_FOLDER.text, "customFolder", WorkspaceLocationType.CUSTOM_FOLDER.text );
	
	/** Control Bar help. */
	LRHtml CONTROL_BAR                     = new LRHtml( "Control Bar", s( "control-bar" ) );
	
	/** Icon Scaling Quality help. */
	LRHtml ICON_SCALING_QUALITY            = new LRHtml( "Icon Scaling Quality", s( "icon-scaling-quality" ) );
	
	/** Vertical Scrolling Amount help. */
	LRHtml VERTICAL_SCROLLING_AMOUNT       = new LRHtml( "Vertical Scrolling Amount", s( "vertical-scrolling-amount" ) );
	
	/** Log Level help. */
	LRHtml LOG_LEVEL                       = new LRHtml( "Log Level", s( "log-level" ), "error", LogLevel.ERROR.text, "warning", LogLevel.WARNING.text, "info",
	                                               LogLevel.INFO.text, "debug", LogLevel.DEBUG.text, "trace", LogLevel.TRACE.text );
	
	/** Network Proxy help. */
	LRHtml NETWORK_PROXY                   = new LRHtml( "Network Proxy", s( "network-proxy" ) );
	
	
	
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
