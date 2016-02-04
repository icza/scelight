/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.sllauncher.bean.settings.type.viewhints;

import hu.scelightapibase.bean.settings.ISettingsBean;
import hu.scelightapibase.bean.settings.type.IStringSetting;
import hu.scelightapibase.bean.settings.type.viewhints.ISsCompFactory;
import hu.scelightapibase.gui.comp.IButton;
import hu.sllauncher.bean.settings.type.StringSetting;
import hu.sllauncher.gui.comp.XLabel;
import hu.sllauncher.gui.icon.LIcons;
import hu.sllauncher.service.job.Job;
import hu.sllauncher.util.LUtils;
import hu.sllauncher.util.gui.LGuiUtils;

import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.JComponent;

/**
 * An {@link ISsCompFactory} implementation which creates test {@link IButton} subsequent components which check whether the setting value of the
 * {@link StringSetting} as a host is reachable.
 * 
 * @author Andras Belicza
 */
public class HostCheckTestBtnFactory extends TestBtnFactory< IStringSetting > {
	
	/**
	 * Creates a new {@link HostCheckTestBtnFactory}.
	 */
	public HostCheckTestBtnFactory() {
		super( LIcons.F_NETWORK.get() );
	}
	
	/**
	 * Performs the test which is checking if the setting value as a host is reachable.
	 */
	@Override
	public void doTest( final IButton button, final JComponent settingComp, final IStringSetting setting, final ISettingsBean settings ) {
		final String host = settings.get( setting ).trim();
		if ( host.isEmpty() )
			return;
		
		button.asButton().setEnabled( false );
		
		new Job( "Host checker: " + host, LIcons.F_NETWORK ) {
			@Override
			public void jobRun() {
				try {
					final boolean reachable = InetAddress.getByName( host ).isReachable( 15_000 );
					
					LGuiUtils.showInfoMsg( new XLabel( "<html><b>The host <code>" + LUtils.safeForHtml( host ) + "</code> is " + ( reachable ? "" : "not" )
					        + " reachable!</b></html>", reachable ? LIcons.F_TICK.get() : LIcons.F_CROSS.get() ), new XLabel( reachable ? ""
					        : "<html><br><p style='width:300px'>This may be because the host is unreachable <i>or</i>"
					                + " firewalls and server configuration may block requests" + " resulting in a unreachable status"
					                + " while some specific ports may still be accessible.</p></html>" ) );
					
				} catch ( final UnknownHostException uhe ) {
					
					LGuiUtils.showInfoMsg( new XLabel( "<html>Unknown host: <code>" + LUtils.safeForHtml( host ) + "</code></html>", LIcons.F_CROSS.get() ) );
					
				} catch ( final Exception e ) {
					
					LGuiUtils.showInfoMsg( new XLabel( "<html>Failed to check host reachability: <code>" + LUtils.safeForHtml( host ) + "</code></html>",
					        LIcons.F_CROSS.get() ) );
					
				}
				
				button.asButton().setEnabled( true );
			}
		}.start();
	}
	
}
