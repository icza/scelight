/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight.gui.page.repanalyzer;

import hu.scelight.bean.RecentReplaysBean;
import hu.scelight.gui.comp.XTabbedPane;
import hu.scelight.gui.icon.Icons;
import hu.scelight.gui.page.repanalyzer.buildorders.BuildOrdersComp;
import hu.scelight.gui.page.repanalyzer.charts.ChartsComp;
import hu.scelight.gui.page.repanalyzer.inspector.RawDataTreeComp;
import hu.scelight.gui.page.repanalyzer.inspector.RawTextDataComp;
import hu.scelight.gui.page.repanalyzer.inspector.binarydata.BinaryDataComp;
import hu.scelight.gui.page.repanalyzer.mapinfo.MapInfoComp;
import hu.scelight.gui.setting.SettingsGui;
import hu.scelight.sc2.rep.repproc.RepProcessor;
import hu.scelight.service.env.Env;
import hu.scelight.service.settings.Settings;
import hu.scelightapibase.bean.settings.ISettingChangeEvent;
import hu.scelightapibase.bean.settings.ISettingChangeListener;
import hu.scelightapibase.util.iface.Producer;
import hu.sllauncher.gui.comp.BorderPanel;
import hu.sllauncher.service.env.LEnv;
import hu.sllauncher.service.settings.LSettings;
import hu.sllauncher.util.SkillLevel;

import java.nio.file.Path;
import java.util.List;

import javax.swing.JComponent;

/**
 * Replay analyzer.
 * 
 * @author Andras Belicza
 */
@SuppressWarnings( "serial" )
public class RepAnalyzerComp extends BorderPanel {
	
	/** Replay processor. */
	private final RepProcessor repProc;
	
	/**
	 * Creates a new {@link RepAnalyzerComp}.
	 * 
	 * @param repProc replay processor
	 */
	public RepAnalyzerComp( final RepProcessor repProc ) {
		this.repProc = repProc;
		
		updateRecentReplays();
		
		buildGui();
	}
	
	/**
	 * Updates the recent replays list.
	 */
	private void updateRecentReplays() {
		final int maxCount = Env.APP_SETTINGS.get( Settings.RECENT_REPLAYS_COUNT );
		
		// Quick check if there is anything to do:
		if ( maxCount == 0 ) {
			if ( Env.APP_SETTINGS.get( Settings.RECENT_REPLAYS_BEAN ).getReplayList().isEmpty() )
				return; // Nope, there is nothing to do
		} else {
			final List< Path > replayList = Env.APP_SETTINGS.get( Settings.RECENT_REPLAYS_BEAN ).getReplayList();
			if ( !replayList.isEmpty() && replayList.get( 0 ).equals( repProc.file ) && maxCount >= replayList.size() )
				return; // Nope, there is nothing to do
		}
		
		// Add replay to the Recent Replays
		final RecentReplaysBean recentReplaysBean = Env.APP_SETTINGS.get( Settings.RECENT_REPLAYS_BEAN ).cloneBean();
		
		final List< Path > replayList = recentReplaysBean.getReplayList();
		
		// Add it to the front, but remove other (previous) occurrences.
		// Also remove those that are over the "quota"
		recentReplaysBean.getReplayList().add( 0, repProc.file );
		
		for ( int i = 0; i < replayList.size(); i++ )
			if ( ( i > 0 && replayList.get( i ).equals( repProc.file ) ) || i >= maxCount ) {
				replayList.remove( i );
				i--; // An element was just removed, decrease index to avoid element skipping!
			}
		
		Env.APP_SETTINGS.set( Settings.RECENT_REPLAYS_BEAN, recentReplaysBean );
	}
	
	/**
	 * Builds the GUI of the page.
	 */
	private void buildGui() {
		final XTabbedPane tp = new XTabbedPane();
		
		tp.addTab( "Charts", Icons.F_CHART, new ChartsComp( repProc ), true );
		tp.addTab( "Game Info", Icons.F_INFORMATION_BALLOON, new Producer< JComponent >() {
			@Override
			public JComponent produce() {
				return new GameInfoComp( repProc );
			}
		}, true );
		tp.addTab( "Users (" + repProc.users.length + ")", Icons.F_USERS, new Producer< JComponent >() {
			@Override
			public JComponent produce() {
				return new UsersComp( repProc );
			}
		}, true );
		tp.addTab( "Build Orders", Icons.F_BLOCK, new Producer< JComponent >() {
			@Override
			public JComponent produce() {
				return new BuildOrdersComp( repProc );
			}
		}, true );
		tp.addTab( "Map info", Icons.F_MAP, new Producer< JComponent >() {
			@Override
			public JComponent produce() {
				return new MapInfoComp( repProc );
			}
		}, true );
		tp.addTab( "Chat (" + repProc.getChatMessagesCount() + ")", Icons.F_BALLOONS, new Producer< JComponent >() {
			@Override
			public JComponent produce() {
				return new ChatComp( repProc );
			}
		}, true );
		final int inspectorTabIdx = tp.getTabCount();
		tp.addTab( "Inspector", Icons.F_USER_DETECTIVE, new Producer< JComponent >() {
			@Override
			public JComponent produce() {
				return createInspectorTab();
			}
		}, true );
		
		
		final ISettingChangeListener scl = new ISettingChangeListener() {
			@Override
			public void valuesChanged( final ISettingChangeEvent event ) {
				if ( event.affected( LSettings.SKILL_LEVEL ) )
					if ( SkillLevel.DEVELOPER.isAtLeast() ) {
						// Developer, show all tabs
						if ( tp.getHiddenTabCount() > 0 )
							tp.unhideTab( 0, inspectorTabIdx );
					} else {
						// Below Developer, hide Inspector tab
						if ( tp.getHiddenTabCount() == 0 )
							tp.hideTab( inspectorTabIdx );
					}
			}
		};
		SettingsGui.addBindExecuteScl( scl, LEnv.LAUNCHER_SETTINGS, LSettings.SKILL_LEVEL.SELF_SET, tp );
		
		addCenter( tp );
	}
	
	/**
	 * Creates and returns the inspector tab component.
	 * 
	 * @return the inspector tab component
	 */
	private JComponent createInspectorTab() {
		final XTabbedPane tp = new XTabbedPane();
		
		tp.addTab( "Raw Text Data", Icons.F_DOCUMENT_TEXT, new RawTextDataComp( repProc ), false );
		tp.addTab( "Raw Data Tree", Icons.F_DOCUMENT_TREE, new Producer< JComponent >() {
			@Override
			public JComponent produce() {
				return new RawDataTreeComp( repProc );
			}
		}, false );
		tp.addTab( "Binary Data", Icons.F_DOCUMENT_BINARY, new Producer< JComponent >() {
			@Override
			public JComponent produce() {
				return new BinaryDataComp( repProc );
			}
		}, false );
		
		return tp;
	}
	
}
