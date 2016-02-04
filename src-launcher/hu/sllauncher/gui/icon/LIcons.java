/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.sllauncher.gui.icon;

import static hu.sllauncher.gui.icon.LIcons.ResUtil.co;
import static hu.sllauncher.gui.icon.LIcons.ResUtil.f;
import static hu.sllauncher.gui.icon.LIcons.ResUtil.my;

/**
 * Launcher icons collection.
 * 
 * <p>
 * <i>Implementation note:</i> This is an interface instead of a class because constants defined in an interface are implicitly <code>public</code>,
 * <code>static</code> and <code>final</code>, and so these keywords can be omitted!
 * </p>
 * 
 * @author Andras Belicza
 */
public interface LIcons {
	
	// My icons
	
	/**
	 * My <code>empty</code> icon. It's a 16x16 empty transparent icon. Usage: where no icon is required.<br>
	 * <img src="../../r/icon/my/empty.png">
	 */
	LRIcon	 MY_EMPTY					 = my( "empty" );
										 
	/**
	 * Scelight icon image.<br>
	 * <img src="../../r/icon/my/app-icon-1.png">
	 */
	LRIcon	 MY_APP_ICON				 = my( "app-icon-1" );
										 
	/**
	 * Scelight logo images. The oldest logo is the first, and the newest (current) is the last.<br>
	 * <table border=1 cellspacing=0 cellpadding=3>
	 * <tr>
	 * <th>1.
	 * <td><img src="../../r/icon/my/app-logo-1.png">
	 * <tr>
	 * <th>0.
	 * <td><img src="../../r/icon/my/app-logo-0.png">
	 * </table>
	 */
	LRIcon[] MY_APP_LOGOS				 = new LRIcon[] { my( "app-logo-0" ), my( "app-logo-1" ) };
										 
	/**
	 * Current (newest) Scelight logo image.<br>
	 * <img src="../../r/icon/my/app-logo-1.png">
	 */
	LRIcon	 MY_APP_LOGO				 = MY_APP_LOGOS[ MY_APP_LOGOS.length - 1 ];
										 
	/**
	 * My <code>fire-big-dev</code> icon; based on the fugue <code>fire-big</code> icon. Usage: Developer skill level.<br>
	 * <img src="../../r/icon/my/fire-big-dev.png">
	 */
	LRIcon	 MY_FIRE_BIG_DEV			 = my( "fire-big-dev" );
										 
										 
										 
	// Misc icons
	
	/**
	 * Misc loading icon, animated. Usage: StatusLabel (indicating progress).<br>
	 * <img src="../../r/icon/misc/loading.gif">
	 */
	LRIcon	 MISC_LOADING				 = new LRIcon( "icon/misc/loading.gif" );
										 
										 
										 
	// Country flag icons
	
	/**
	 * England country flag.<br>
	 * <img src="../../r/icon/country/England.gif">
	 */
	LRIcon	 C_ENGLAND					 = co( "England" );
										 
	/**
	 * United States country flag.<br>
	 * <img src="../../r/icon/country/UnitedStates.gif">
	 */
	LRIcon	 C_UNITED_STATES			 = co( "UnitedStates" );
										 
										 
										 
	// Fugue icons
	
	/**
	 * Fugue <code>application-browser</code> icon. Usage: web link<br>
	 * <img src="../../r/icon/fugue/application-browser.png">
	 */
	LRIcon	 F_APPLICATION_BROWSER		 = f( "application-browser" );
										 
	/**
	 * Fugue <code>application-resize-actual</code> icon. Usage: Restore default dialog size<br>
	 * <img src="../../r/icon/fugue/application-resize-actual.png">
	 */
	LRIcon	 F_APPLICATION_RESIZE_ACTUAL = f( "application-resize-actual" );
										 
	/**
	 * Fugue <code>application-resize</code> icon. Usage: Enlarge dialog<br>
	 * <img src="../../r/icon/fugue/application-resize.png">
	 */
	LRIcon	 F_APPLICATION_RESIZE		 = f( "application-resize" );
										 
	/**
	 * Fugue <code>arrow-090</code> icon. Usage: Search Previous, Move up (replay folder)<br>
	 * <img src="../../r/icon/fugue/arrow-090.png">
	 */
	LRIcon	 F_ARROW_090				 = f( "arrow-090" );
										 
	/**
	 * Fugue <code>arrow-180</code> icon. Usage: go back, previous<br>
	 * <img src="../../r/icon/fugue/arrow-180.png">
	 */
	LRIcon	 F_ARROW_180				 = f( "arrow-180" );
										 
	/**
	 * Fugue <code>arrow-270</code> icon. Usage: Search Next, Move down (replay folder)<br>
	 * <img src="../../r/icon/fugue/arrow-270.png">
	 */
	LRIcon	 F_ARROW_270				 = f( "arrow-270" );
										 
	/**
	 * Fugue <code>arrow-circle-315</code> icon. Usage: refresh<br>
	 * <img src="../../r/icon/fugue/arrow-circle-315.png">
	 */
	LRIcon	 F_ARROW_CIRCLE_315			 = f( "arrow-circle-315" );
										 
	/**
	 * Fugue <code>arrow-return-180-left</code> icon. Usage: restore default value<br>
	 * <img src="../../r/icon/fugue/arrow-return-180-left.png">
	 */
	LRIcon	 F_ARROW_RETURN_180_LEFT	 = f( "arrow-return-180-left" );
										 
	/**
	 * Fugue <code>arrow</code> icon. Usage: go forward, next<br>
	 * <img src="../../r/icon/fugue/arrow.png">
	 */
	LRIcon	 F_ARROW					 = f( "arrow" );
										 
	/**
	 * Fugue <code>category-group</code> icon. Usage: Installed modules<br>
	 * <img src="../../r/icon/fugue/category-group.png">
	 */
	LRIcon	 F_CATEGORY_GROUP			 = f( "category-group" );
										 
	/**
	 * Fugue <code>category</code> icon. Usage: External modules<br>
	 * <img src="../../r/icon/fugue/category.png">
	 */
	LRIcon	 F_CATEGORY					 = f( "category" );
										 
	/**
	 * Fugue <code>control-pause</code> icon. Usage: pause, suspend, paused<br>
	 * <img src="../../r/icon/fugue/control-pause.png">
	 */
	LRIcon	 F_CONTROL_PAUSE			 = f( "control-pause" );
										 
	/**
	 * Fugue <code>control</code> icon. Usage: continue, start, play, resume, playing<br>
	 * <img src="../../r/icon/fugue/control.png">
	 */
	LRIcon	 F_CONTROL					 = f( "control" );
										 
	/**
	 * Fugue <code>cross-button</code> icon. Usage: close page (Multi-page comp)<br>
	 * <img src="../../r/icon/fugue/cross-button.png">
	 */
	LRIcon	 F_CROSS_BUTTON				 = f( "cross-button" );
										 
	/**
	 * Fugue <code>cross-octagon</code> icon. Usage: Disable ext module, disable auto-update ext module, Cancel a job.<br>
	 * <img src="../../r/icon/fugue/cross-octagon.png">
	 */
	LRIcon	 F_CROSS_OCTAGON			 = f( "cross-octagon" );
										 
	/**
	 * Fugue <code>cross</code> icon. Usage: false, general delete/remove/clear<br>
	 * <img src="../../r/icon/fugue/cross.png">
	 */
	LRIcon	 F_CROSS					 = f( "cross" );
										 
	/**
	 * Fugue <code>document</code> icon. Usage: file chooser in the path field, tree leaf icon for <code>null</code> or unhandled-type value in the Raw Tree
	 * Data tab in the Replay analyzer.<br>
	 * <img src="../../r/icon/fugue/document.png">
	 */
	LRIcon	 F_DOCUMENT					 = f( "document" );
										 
	/**
	 * Fugue <code>drive-download</code> icon. Usage: Install / Auto-update External module<br>
	 * <img src="../../r/icon/fugue/drive-download.png">
	 */
	LRIcon	 F_DRIVE_DOWNLOAD			 = f( "drive-download" );
										 
	/**
	 * Fugue <code>exclamation-red</code> icon. Usage: error<br>
	 * <img src="../../r/icon/fugue/exclamation-red.png">
	 */
	LRIcon	 F_EXCLAMATION_RED			 = f( "exclamation-red" );
										 
	/**
	 * Fugue <code>exclamation</code> icon. Usage: warning<br>
	 * <img src="../../r/icon/fugue/exclamation.png">
	 */
	LRIcon	 F_EXCLAMATION				 = f( "exclamation" );
										 
	/**
	 * Fugue <code>fire-big</code> icon. Usage: Advanced skill level<br>
	 * <img src="../../r/icon/fugue/fire-big.png">
	 */
	LRIcon	 F_FIRE_BIG					 = f( "fire-big" );
										 
	/**
	 * Fugue <code>fire-small</code> icon. Usage: Basic skill level<br>
	 * <img src="../../r/icon/fugue/fire-small.png">
	 */
	LRIcon	 F_FIRE_SMALL				 = f( "fire-small" );
										 
	/**
	 * Fugue <code>fire</code> icon. Usage: Normal skill level<br>
	 * <img src="../../r/icon/fugue/fire.png">
	 */
	LRIcon	 F_FIRE						 = f( "fire" );
										 
	/**
	 * Fugue <code>folder-open</code> icon. Usage: show file or folder in file browser.<br>
	 * <img src="../../r/icon/fugue/folder-open.png">
	 */
	LRIcon	 F_FOLDER_OPEN				 = f( "folder-open" );
										 
	/**
	 * Fugue <code>folder-smiley</code> icon. Usage: Workspace<br>
	 * <img src="../../r/icon/fugue/folder-smiley.png">
	 */
	LRIcon	 F_FOLDER_SMILEY			 = f( "folder-smiley" );
										 
	/**
	 * Fugue <code>folder</code> icon. Usage: choose folder.<br>
	 * <img src="../../r/icon/fugue/folder.png">
	 */
	LRIcon	 F_FOLDER					 = f( "folder" );
										 
	/**
	 * Fugue <code>gear</code> icon. Usage: settings<br>
	 * <img src="../../r/icon/fugue/gear.png">
	 */
	LRIcon	 F_GEAR						 = f( "gear" );
										 
	/**
	 * Fugue <code>globe-network</code> icon. Usage: Network settings<br>
	 * <img src="../../r/icon/fugue/globe-network.png">
	 */
	LRIcon	 F_GLOBE_NETWORK			 = f( "globe-network" );
										 
	/**
	 * Fugue <code>home</code> icon. Usage: Welcome page<br>
	 * <img src="../../r/icon/fugue/home.png">
	 */
	LRIcon	 F_HOME						 = f( "home" );
										 
	/**
	 * Fugue <code>light-bulb</code> icon. Usage: tips<br>
	 * <img src="../../r/icon/fugue/light-bulb.png">
	 */
	LRIcon	 F_LIGHT_BULB				 = f( "light-bulb" );
										 
	/**
	 * Fugue <code>license-key</code> icon. Usage: Registration<br>
	 * <img src="../../r/icon/fugue/license-key.png">
	 */
	LRIcon	 F_LICENCE_KEY				 = f( "license-key" );
										 
	/**
	 * Fugue <code>locale-alternate</code> icon. Usage: Locale settings<br>
	 * <img src="../../r/icon/fugue/locale-alternate.png">
	 */
	LRIcon	 F_LOCALE_ALTERNATE			 = f( "locale-alternate" );
										 
	/**
	 * Fugue <code>mail-at-sign</code> icon. Usage: email link<br>
	 * <img src="../../r/icon/fugue/mail-at-sign.png">
	 */
	LRIcon	 F_MAIL_AT_SIGN				 = f( "mail-at-sign" );
										 
	/**
	 * Fugue <code>speaker-volume</code> icon. Usage: Voice settings<br>
	 * <img src="../../r/icon/fugue/microphone.png">
	 */
	LRIcon	 F_MICROPHONE				 = f( "microphone" );
										 
	/**
	 * Fugue <code>music</code> icon. Usage: Sound player job<br>
	 * <img src="../../r/icon/fugue/music.png">
	 */
	LRIcon	 F_MUSIC					 = f( "music" );
										 
	/**
	 * Fugue <code>na</code> icon. Usage: not available, not applicable<br>
	 * <img src="../../r/icon/fugue/na.png">
	 */
	LRIcon	 F_NA						 = f( "na" );
										 
	/**
	 * Fugue <code>hourglass</code> icon. Usage: wait<br>
	 * <img src="../../r/icon/fugue/hourglass.png">
	 */
	LRIcon	 F_HOURGLASS				 = f( "hourglass" );
										 
	/**
	 * Fugue <code>network</code> icon. Usage: Test connection button<br>
	 * <img src="../../r/icon/fugue/network.png">
	 */
	LRIcon	 F_NETWORK					 = f( "network" );
										 
	/**
	 * Fugue <code>newspaper</code> icon. Usage: news<br>
	 * <img src="../../r/icon/fugue/newspaper.png">
	 */
	LRIcon	 F_NEWSPAPER				 = f( "newspaper" );
										 
	/**
	 * Fugue <code>question-white</code> icon. Usage: help icon<br>
	 * <img src="../../r/icon/fugue/question-white.png">
	 */
	LRIcon	 F_QUESTION_WHITE			 = f( "question-white" );
										 
	/**
	 * Fugue <code>question</code> icon. Usage: help menu<br>
	 * <img src="../../r/icon/fugue/question.png">
	 */
	LRIcon	 F_QUESTION					 = f( "question" );
										 
	/**
	 * Fugue <code>report--exclamation</code> icon. Usage: log, system messages<br>
	 * <img src="../../r/icon/fugue/report--exclamation.png">
	 */
	LRIcon	 F_REPORT_EXCLAMATION		 = f( "report--exclamation" );
										 
	/**
	 * Fugue <code>script-text</code> icon. Usage: license<br>
	 * <img src="../../r/icon/fugue/script-text.png">
	 */
	LRIcon	 F_SCRIPT_TEXT				 = f( "script-text" );
										 
	/**
	 * Fugue <code>speaker-volume</code> icon. Usage: Sound settings<br>
	 * <img src="../../r/icon/fugue/speaker-volume.png">
	 */
	LRIcon	 F_SPEAKER_VOLUME			 = f( "speaker-volume" );
										 
	/**
	 * Fugue <code>tick</code> icon. Usage: OK, true<br>
	 * <img src="../../r/icon/fugue/tick.png">
	 */
	LRIcon	 F_TICK						 = f( "tick" );
										 
	/**
	 * Fugue <code>ui-layered-pane</code> icon. Usage: multi-page user interface<br>
	 * <img src="../../r/icon/fugue/ui-layered-pane.png">
	 */
	LRIcon	 F_UI_LAYERED_PANE			 = f( "ui-layered-pane" );
										 
	/**
	 * Fugue <code>ui-scroll-pane-image</code> icon. Usage: user interface, UI<br>
	 * <img src="../../r/icon/fugue/ui-scroll-pane-image.png">
	 */
	LRIcon	 F_UI_SCROLL_PANE_IMAGE		 = f( "ui-scroll-pane-image" );
										 
	/**
	 * Fugue <code>ui-toolbar</code> icon. Usage: tool bar settings.<br>
	 * <img src="../../r/icon/fugue/ui-toolbar.png">
	 */
	LRIcon	 F_UI_TOOLBAR				 = f( "ui-toolbar" );
										 
										 
										 
	/**
	 * Utility class to help assemble resource URL strings.
	 * 
	 * @author Andras Belicza
	 */
	public static class ResUtil {
		
		/**
		 * Returns the resource string of the fugue icon specified by its name.
		 * 
		 * @param name name of the fugue icon whose URL to return
		 * @return the resource string of the fugue icon specified by its name
		 */
		public static String f_( final String name ) {
			return "icon/fugue/" + name + ".png";
		}
		
		/**
		 * Returns the resource string of my image specified by its name.
		 * 
		 * @param name name of my image whose URL to return
		 * @return the resource string of my image specified by its name
		 */
		public static String my_( final String name ) {
			return "icon/my/" + name + ".png";
		}
		
		/**
		 * Returns the resource string of the country image specified by its name.
		 * 
		 * @param name name of the country image whose URL to return
		 * @return the resource string of the country image specified by its name
		 */
		public static String co_( final String name ) {
			return "icon/country/" + name + ".gif";
		}
		
		/**
		 * Returns the fugue image icon resource specified by its name.
		 * 
		 * @param name name of the fugue icon whose icon resource to return
		 * @return the fugue icon resource specified by its name
		 */
		public static LRIcon f( final String name ) {
			return new LRIcon( f_( name ) );
		}
		
		/**
		 * Returns the my image icon resource specified by its name.
		 * 
		 * @param name name of my image whose icon resource to return
		 * @return the my image icon resource specified by its name
		 */
		public static LRIcon my( final String name ) {
			return new LRIcon( my_( name ) );
		}
		
		/**
		 * Returns the the country image icon resource specified by its name.
		 * 
		 * @param name name of the country image whose icon resource to return
		 * @return the country image icon resource specified by its name
		 */
		public static LRIcon co( final String name ) {
			return new LRIcon( co_( name ) );
		}
		
	}
	
}
