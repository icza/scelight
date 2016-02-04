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

import hu.scelightapibase.bean.settings.ISettingChangeEvent;
import hu.scelightapibase.bean.settings.ISettingChangeListener;
import hu.sllauncher.bean.settings.type.Setting;
import hu.sllauncher.service.env.LEnv;
import hu.sllauncher.util.LUtils;

import java.util.Set;

import javax.swing.ToolTipManager;

/**
 * Global (application wide) launcher setting change listener.
 * 
 * @author Andras Belicza
 */
public class LGlobalSettingChangeListener implements ISettingChangeListener {
	
	/** Listened global launcher setting set. */
	public static final Set< Setting< ? > > SETTING_SET = LUtils.< Setting< ? > > asNewSet( LSettings.TOOL_TIP_INITIAL_DELAY, LSettings.TOOL_TIP_DISMISS_DELAY,
	                                                            LSettings.LOG_LEVEL, LSettings.ENABLE_NETWORK_PROXY, LSettings.HTTP_PROXY_HOST,
	                                                            LSettings.HTTP_PROXY_PORT, LSettings.HTTPS_PROXY_HOST, LSettings.HTTPS_PROXY_PORT,
	                                                            LSettings.SOCKS_PROXY_HOST, LSettings.SOCKS_PROXY_PORT );
	
	@Override
	public void valuesChanged( final ISettingChangeEvent event ) {
		if ( event.affected( LSettings.TOOL_TIP_INITIAL_DELAY ) )
			ToolTipManager.sharedInstance().setInitialDelay( event.get( LSettings.TOOL_TIP_INITIAL_DELAY ) );
		
		if ( event.affected( LSettings.TOOL_TIP_DISMISS_DELAY ) )
			ToolTipManager.sharedInstance().setDismissDelay( event.get( LSettings.TOOL_TIP_DISMISS_DELAY ) );
		
		if ( event.affected( LSettings.LOG_LEVEL ) )
			LEnv.LOGGER.setLogLevel( event.get( LSettings.LOG_LEVEL ) );
		
		if ( event.affectedAny( LSettings.ENABLE_NETWORK_PROXY, LSettings.HTTP_PROXY_HOST, LSettings.HTTP_PROXY_PORT, LSettings.HTTPS_PROXY_HOST,
		        LSettings.HTTPS_PROXY_PORT, LSettings.SOCKS_PROXY_HOST, LSettings.SOCKS_PROXY_PORT ) ) {
			applyProxyConfig( event );
		}
	}
	
	/**
	 * Applies the proxy configuration from the launcher settings.
	 * 
	 * @param event setting change event
	 */
	public static void applyProxyConfig( final ISettingChangeEvent event ) {
		// Proxy guide:
		// http://download.oracle.com/javase/6/docs/technotes/guides/net/proxies.html
		
		if ( event.get( LSettings.ENABLE_NETWORK_PROXY ) ) {
			
			System.setProperty( "http.proxyHost", event.get( LSettings.HTTP_PROXY_HOST ) );
			System.setProperty( "http.proxyPort", event.get( LSettings.HTTP_PROXY_PORT ).toString() );
			
			System.setProperty( "https.proxyHost", event.get( LSettings.HTTPS_PROXY_HOST ) );
			System.setProperty( "https.proxyPort", event.get( LSettings.HTTPS_PROXY_PORT ).toString() );
			
			System.setProperty( "socksProxyHost", event.get( LSettings.SOCKS_PROXY_HOST ) );
			System.setProperty( "socksProxyPort", event.get( LSettings.SOCKS_PROXY_PORT ).toString() );
			
		} else {
			
			System.setProperty( "http.proxyHost", "" );
			System.setProperty( "http.proxyPort", "" );
			
			System.setProperty( "https.proxyHost", "" );
			System.setProperty( "https.proxyPort", "" );
			
			System.setProperty( "socksProxyHost", "" );
			System.setProperty( "socksProxyPort", "" );
			
		}
	}
	
}
