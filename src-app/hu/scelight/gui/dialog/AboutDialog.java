/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight.gui.dialog;

import hu.scelight.Consts;
import hu.scelight.gui.icon.Icons;
import hu.scelight.gui.page.ComponentsPage;
import hu.scelight.gui.page.about.AboutPage;
import hu.scelight.gui.page.about.extmods.AvailExtModsPage;
import hu.scelight.gui.page.about.logs.LogsPage;
import hu.scelight.gui.page.about.sysinfo.SysInfoPage;
import hu.scelight.service.env.Env;
import hu.scelightapibase.gui.comp.multipage.IPage;
import hu.sllauncher.gui.comp.XDialog;
import hu.sllauncher.gui.comp.multipage.MultiPageComp;
import hu.sllauncher.gui.page.LicensePage;
import hu.sllauncher.gui.page.NewsPage;
import hu.sllauncher.gui.page.TipsPage;
import hu.sllauncher.gui.page.WelcomePage;

import java.util.ArrayList;
import java.util.List;

/**
 * A dialog to view/edit settings.
 * 
 * @author Andras Belicza
 */
@SuppressWarnings( "serial" )
public class AboutDialog extends XDialog {
	
	/**
	 * Creates a new {@link AboutDialog}.
	 * 
	 * @param defaultPageClass optional class of the default page
	 */
	public AboutDialog( final Class< ? extends IPage< ? > > defaultPageClass ) {
		super( Env.MAIN_FRAME );
		
		setTitle( "About " + Consts.APP_NAME );
		setIconImage( Icons.F_INFORMATION.get().getImage() );
		
		buildGui( defaultPageClass );
		
		restoreDefaultSize();
		
		setVisible( true );
	}
	
	/**
	 * Builds the GUI of the about dialog.
	 * 
	 * @param defaultPageClass optional class of the default page
	 */
	private void buildGui( final Class< ? extends IPage< ? > > defaultPageClass ) {
		final List< IPage< ? > > pageList = new ArrayList<>();
		
		// App about pages
		pageList.add( new AboutPage() );
		pageList.add( new ComponentsPage() );
		
		// Pages from the Launcher (or their improved versions):
		pageList.add( new WelcomePage() );
		pageList.add( new NewsPage() );
		pageList.add( new LicensePage() );
		pageList.add( new TipsPage() );
		pageList.add( new AvailExtModsPage() );
		pageList.add( new LogsPage() );
		
		// System info
		pageList.add( new SysInfoPage() );
		
		
		// Find default page
		IPage< ? > defaultPage = null;
		if ( defaultPageClass != null )
			for ( final IPage< ? > page : pageList )
				if ( page.getClass().equals( defaultPageClass ) ) {
					defaultPage = page;
					break;
				}
		
		final MultiPageComp multiPageComp = new MultiPageComp( pageList, defaultPage, getLayeredPane() );
		cp.addCenter( multiPageComp );
		
		getRootPane().setDefaultButton( addCloseButton( "_OK" ) );
	}
	
}
