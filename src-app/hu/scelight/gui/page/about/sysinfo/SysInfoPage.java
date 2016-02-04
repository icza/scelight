/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight.gui.page.about.sysinfo;

import hu.scelight.gui.icon.Icons;
import hu.scelight.util.Utils;
import hu.sllauncher.gui.comp.BorderPanel;
import hu.sllauncher.gui.comp.multipage.BasePage;
import hu.sllauncher.gui.comp.table.XTable;
import hu.sllauncher.util.SkillLevel;

import java.util.Vector;

import javax.swing.JComponent;

/**
 * General system info page.
 * 
 * @author Andras Belicza
 */
public class SysInfoPage extends BasePage< JComponent > {
	
	/**
	 * Creates a new {@link SysInfoPage}.
	 */
	public SysInfoPage() {
		super( "System info", Icons.F_COMPUTER );
		
		if ( SkillLevel.ADVANCED.isAtLeast() )
			addChild( new JavaInfoPage() );
		if ( SkillLevel.DEVELOPER.isAtLeast() )
			addChild( new EnvVarsPage() );
	}
	
	@Override
	public JComponent createPageComp() {
		final BorderPanel p = new BorderPanel();
		
		final XTable table = new XTable();
		
		final Vector< Vector< Object > > data = new Vector<>();
		
		data.add( Utils.vector( "Operating System", System.getProperty( "os.name" ) ) );
		if ( SkillLevel.NORMAL.isAtLeast() ) {
			data.add( Utils.vector( "OS version", System.getProperty( "os.version" ) ) );
			data.add( Utils.vector( "OS (JVM) architecture", System.getProperty( "os.arch" ) ) );
			data.add( Utils.vector( "Available processors", Runtime.getRuntime().availableProcessors() ) );
		}
		data.add( Utils.vector( "Java version", System.getProperty( "java.version" ) ) );
		data.add( Utils.vector( "User name", System.getProperty( "user.name" ) ) );
		data.add( Utils.vector( "User time zone", System.getProperty( "user.timezone" ) ) );
		
		table.getXTableModel().setDataVector( data, Utils.vector( "Property name", "Property value" ) );
		table.packColumnsExceptLast();
		
		p.addCenter( table.createWrapperBox( true, table.createToolBarParams( p ) ) );
		
		return p;
	}
	
}
