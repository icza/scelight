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

import hu.scelight.bean.repfolders.RepFolderBean;
import hu.scelight.bean.repfolders.RepFolderOrigin;
import hu.scelight.bean.repfolders.RepFoldersBean;
import hu.scelight.sc2.rep.model.details.Toon;
import hu.scelight.sc2.rep.repproc.RepProcessor;
import hu.scelight.service.env.Env;
import hu.scelight.util.Utils;
import hu.scelightapibase.bean.settings.ISettingChangeEvent;
import hu.scelightapibase.bean.settings.ISettingChangeListener;
import hu.sllauncher.bean.settings.type.Setting;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

/**
 * Global (application wide) app setting change listener.
 * 
 * @author Andras Belicza
 */
public class GlobalSettingChangeListener implements ISettingChangeListener {
	
	/** Listened global app setting set. */
	public static final Set< Setting< ? > > SETTING_SET = Utils.< Setting< ? > > asNewSet( Settings.REPLAY_FOLDERS_BEAN, Settings.REPLAY_BACKUP_FOLDER,
	                                                            Settings.FAVORED_PLAYER_TOONS );
	
	@Override
	public void valuesChanged( final ISettingChangeEvent event ) {
		if ( event.affected( Settings.REPLAY_BACKUP_FOLDER ) ) {
			// Synchronize the path of the replay folder associated with the replay backup folder
			RepFoldersBean rfsBean = Env.APP_SETTINGS.get( Settings.REPLAY_FOLDERS_BEAN );
			RepFolderBean rf = rfsBean.getRepFolderForOrigin( RepFolderOrigin.REP_BACKUP_FOLDER );
			
			if ( !rf.getPath().equals( event.get( Settings.REPLAY_BACKUP_FOLDER ) ) ) {
				// To change something in a bean setting, we have to clone it!
				rfsBean = rfsBean.cloneBean();
				rf = rfsBean.getRepFolderForOrigin( RepFolderOrigin.REP_BACKUP_FOLDER );
				rf.setPath( event.get( Settings.REPLAY_BACKUP_FOLDER ) );
				Env.APP_SETTINGS.set( Settings.REPLAY_FOLDERS_BEAN, rfsBean );
			}
		}
		
		if ( event.affected( Settings.REPLAY_FOLDERS_BEAN ) ) {
			// Reconfigure watched replay folders:
			Env.WATCHER.clear();
			for ( final RepFolderBean rfBean : event.get( Settings.REPLAY_FOLDERS_BEAN ).getReplayFolderBeanList() )
				if ( rfBean.getMonitored() )
					Env.WATCHER.watchFolder( rfBean.getPath(), rfBean.getRecursive() );
		}
		
		if ( event.affected( Settings.FAVORED_PLAYER_TOONS ) ) {
			// Parse favored toons
			RepProcessor.favoredToonList.set( getFavoredToonList() );
		}
	}
	
	/**
	 * Returns the list of favored player toons.
	 * 
	 * @return the list of favored player toons
	 */
	private static List< Toon > getFavoredToonList() {
		final List< Toon > list = new ArrayList<>();
		
		final StringTokenizer st = new StringTokenizer( Env.APP_SETTINGS.get( Settings.FAVORED_PLAYER_TOONS ), "," );
		while ( st.hasMoreTokens() ) {
			String toon = null;
			try {
				toon = st.nextToken().trim();
				if ( !toon.isEmpty() )
					list.add( new Toon( toon ) );
			} catch ( final IllegalArgumentException iae ) {
				// Invalid toon, ignore it (but log warning)
				Env.LOGGER.warning( "Invalid toon in the Favored Player list: " + toon, iae );
			}
		}
		
		return list;
	}
	
}
