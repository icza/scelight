/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight.gui.page.multirepanalyzer.playerdetails.metrictrend;

import hu.scelight.gui.comp.XTabbedPane;
import hu.scelight.gui.icon.Icons;
import hu.scelight.gui.page.multirepanalyzer.model.PlayerStats;
import hu.scelightapibase.util.iface.Producer;
import hu.sllauncher.gui.comp.BorderPanel;

import javax.swing.JComponent;

/**
 * Metric trend details component.
 * 
 * @author Andras Belicza
 */
@SuppressWarnings( "serial" )
public class MetricTrendComp extends BorderPanel {
	
	/**
	 * Creates a new {@link MetricTrendComp}.
	 * 
	 * @param displayName_ display name to be used when replays of stats rows are opened
	 * @param playerStats player stats to show metric trend details for
	 */
	public MetricTrendComp( final String displayName_, final PlayerStats playerStats ) {
		
		final XTabbedPane tp = new XTabbedPane();
		
		final String displayName = displayName_ + " \u00d7 ";
		
		tp.addTab( "APM", Icons.MY_APM, new ApmComp( displayName + "APM", playerStats ), true );
		tp.addTab( "SPM", Icons.MY_SPM, new Producer< JComponent >() {
			@Override
			public JComponent produce() {
				return new SpmComp( displayName + "SPM", playerStats );
			}
		}, true );
		tp.addTab( "SQ", Icons.MY_SQ, new Producer< JComponent >() {
			@Override
			public JComponent produce() {
				return new SqComp( displayName + "SQ", playerStats );
			}
		}, true );
		tp.addTab( "SC%", Icons.MY_SCP, new Producer< JComponent >() {
			@Override
			public JComponent produce() {
				return new ScpComp( displayName + "SC%", playerStats );
			}
		}, true );
		
		addCenter( tp );
	}
	
}
