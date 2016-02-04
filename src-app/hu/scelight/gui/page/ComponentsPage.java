/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight.gui.page;

import hu.scelight.Consts;
import hu.scelight.gui.icon.Icons;
import hu.scelight.sc2.rep.cache.RepProcCache;
import hu.scelight.sc2.rep.factory.RepParserEngine;
import hu.scelight.sc2.rep.repproc.RepProcessor;
import hu.scelight.search.RepSearchEngine;
import hu.scelight.service.env.Env;
import hu.scelight.service.extmod.Services;
import hu.scelight.template.TemplateEngine;
import hu.scelight.util.Utils;
import hu.sllauncher.bean.module.ExtModManifestBean;
import hu.sllauncher.gui.comp.BorderPanel;
import hu.sllauncher.gui.comp.multipage.BasePage;
import hu.sllauncher.gui.comp.table.XTable;
import hu.sllauncher.util.DateFormat;
import hu.sllauncher.util.DateValue;

import java.util.Vector;

import javax.swing.JComponent;

/**
 * Components page displaying info about application components.
 * 
 * @author Andras Belicza
 */
public class ComponentsPage extends BasePage< JComponent > {
	
	/**
	 * Creates a new {@link ComponentsPage}.
	 */
	public ComponentsPage() {
		super( "Components", Icons.F_CATEGORIES );
	}
	
	@Override
	public JComponent createPageComp() {
		final BorderPanel p = new BorderPanel();
		
		final XTable table = new XTable();
		
		final Vector< Vector< Object > > data = new Vector<>();
		
		// General/internal components
		data.add( Utils.vector( Consts.APP_NAME, Consts.APP_VERSION.toString( true ) + "." + Env.APP_BUILD_INFO.getBuildNumber(), new DateValue(
		        Env.APP_BUILD_INFO.getDate(), DateFormat.DATE ), "Internal Module" ) );
		data.add( Utils.vector( Consts.LAUNCHER_NAME, Consts.LAUNCHER_VERSION.toString( true ) + "." + Env.LAUNCHER_BUILD_INFO.getBuildNumber(), new DateValue(
		        Env.LAUNCHER_BUILD_INFO.getDate(), DateFormat.DATE ), "Internal Module" ) );
		data.add( Utils.vector( Consts.APP_LIBS_NAME, Consts.APP_LIBS_VERSION.toString( true ) + "." + Env.APP_LIBS_BUILD_INFO.getBuildNumber(), new DateValue(
		        Env.APP_LIBS_BUILD_INFO.getDate(), DateFormat.DATE ), "Internal Module" ) );
		data.add( Utils.vector( Consts.SC2_TEXTURES_NAME, Consts.SC2_TEXTURES_VERSION.toString( true ) + "." + Env.SC2_TEXTURES_BUILD_INFO.getBuildNumber(),
		        new DateValue( Env.SC2_TEXTURES_BUILD_INFO.getDate(), DateFormat.DATE ), "Internal Module" ) );
		data.add( Utils.vector( Consts.SC2_BALANCE_DATA_NAME,
		        Consts.SC2_BALANCE_DATA_VERSION.toString( true ) + "." + Env.SC2_BALANCE_DATA_BUILD_INFO.getBuildNumber(), new DateValue(
		                Env.SC2_BALANCE_DATA_BUILD_INFO.getDate(), DateFormat.DATE ), "Internal Module" ) );
		data.add( Utils.vector( Consts.EXT_MOD_API_NAME, Consts.EXT_MOD_API_VERSION.toString( true ) + "." + Env.EXT_MOD_API_BUILD_INFO.getBuildNumber(),
		        new DateValue( Env.EXT_MOD_API_BUILD_INFO.getDate(), DateFormat.DATE ), "Internal Module" ) );
		data.add( Utils.vector( "Replay Parser Engine", RepParserEngine.VERSION.toString( true ), null, "Internal Component" ) );
		data.add( Utils.vector( "Replay Processor Engine", RepProcessor.VERSION.toString( true ), null, "Internal Component" ) );
		data.add( Utils.vector( "Replay Processor Cache", RepProcCache.VERSION.toString( true ), null, "Internal Component" ) );
		data.add( Utils.vector( "Replay Search Engine", RepSearchEngine.VERSION.toString( true ), null, "Internal Component" ) );
		data.add( Utils.vector( "Name Template Engine", TemplateEngine.VERSION.toString( true ), null, "Internal Component" ) );
		data.add( Utils.vector( "External Module Services Implementation", Services.VERSION.toString( true ), null, "Internal Component" ) );
		
		// Started external modules
		for ( final ExtModManifestBean manifest : Env.EXT_MOD_MANAGER.getStartedExtModManifestList() )
			data.add( Utils.vector( manifest.getName(), manifest.getVersion().toString( true ) + "." + manifest.getBuildInfo().getBuildNumber(), new DateValue(
			        manifest.getBuildInfo().getDate(), DateFormat.DATE ), "External Module" ) );
		
		table.getXTableModel().setDataVector( data, Utils.vector( "Component", "Version", "Release date", "Type" ) );
		table.pack();
		
		p.addCenter( table.createWrapperBox( true, table.createToolBarParams( p ) ) );
		
		return p;
	}
}
