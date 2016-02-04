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
import hu.sllauncher.util.DurationFormat;
import hu.sllauncher.util.SkillLevel;

import java.lang.management.ClassLoadingMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.lang.management.ThreadMXBean;
import java.util.Date;
import java.util.Vector;

import javax.swing.JComponent;

/**
 * Java info page.
 * 
 * @author Andras Belicza
 */
public class JavaInfoPage extends BasePage< JComponent > {
	
	/**
	 * Creates a new {@link JavaInfoPage}.
	 */
	public JavaInfoPage() {
		super( "Java info", Icons.MISC_JAVA );
		
		if ( SkillLevel.DEVELOPER.isAtLeast() )
			addChild( new JavaSysPropsPage() );
	}
	
	@Override
	public JComponent createPageComp() {
		final BorderPanel p = new BorderPanel();
		
		final XTable table = new XTable();
		
		final Vector< Vector< Object > > data = new Vector<>();
		
		data.add( Utils.vector( "Java vendor", System.getProperty( "java.vendor" ) ) );
		data.add( Utils.vector( "Java version", System.getProperty( "java.version" ) ) );
		data.add( Utils.vector( "Java home", System.getProperty( "java.home" ) ) );
		data.add( Utils.vector( "Java runtime name", System.getProperty( "java.runtime.name" ) ) );
		data.add( Utils.vector( "Java runtime version", System.getProperty( "java.runtime.version" ) ) );
		data.add( Utils.vector( "Java spec. vendor", System.getProperty( "java.specification.vendor" ) ) );
		data.add( Utils.vector( "Java spec. name", System.getProperty( "java.specification.name" ) ) );
		data.add( Utils.vector( "Java spec. version", System.getProperty( "java.specification.version" ) ) );
		
		final RuntimeMXBean bean = ManagementFactory.getRuntimeMXBean();
		if ( SkillLevel.DEVELOPER.isAtLeast() ) {
			data.add( Utils.vector( "Java VM spec. vendor", bean.getSpecVendor() ) );
			data.add( Utils.vector( "Java VM spec. name", bean.getSpecName() ) );
			data.add( Utils.vector( "Java VM spec. version", bean.getSpecVersion() ) );
			
			data.add( Utils.vector( "Java VM impl. vendor", bean.getVmVendor() ) );
			data.add( Utils.vector( "Java VM impl. name", bean.getVmName() ) );
			data.add( Utils.vector( "Java VM impl. version", bean.getVmVersion() ) );
			
			data.add( Utils.vector( "Java input arguments", bean.getInputArguments() ) );
			
			data.add( Utils.vector( "Boot class path", bean.getBootClassPath() ) );
			data.add( Utils.vector( "Library path", bean.getLibraryPath() ) );
			data.add( Utils.vector( "Class path", bean.getClassPath() ) );
		}
		
		data.add( Utils.vector( "Java start time", new Date( bean.getStartTime() ) ) );
		data.add( Utils.vector( "Java uptime", DurationFormat.AUTO.formatDuration( bean.getUptime() ) ) );
		
		if ( SkillLevel.DEVELOPER.isAtLeast() ) {
			final ClassLoadingMXBean clBean = ManagementFactory.getClassLoadingMXBean();
			data.add( Utils.vector( "Currently loaded classes", clBean.getLoadedClassCount() ) );
			data.add( Utils.vector( "Total loaded classes", clBean.getTotalLoadedClassCount() ) );
			data.add( Utils.vector( "Unloaded classes", clBean.getUnloadedClassCount() ) );
			
			final ThreadMXBean tBean = ManagementFactory.getThreadMXBean();
			data.add( Utils.vector( "Current thread count", tBean.getThreadCount() ) );
			data.add( Utils.vector( "Peak thread count", tBean.getPeakThreadCount() ) );
			data.add( Utils.vector( "Total started thread count", tBean.getTotalStartedThreadCount() ) );
		}
		
		table.getXTableModel().setDataVector( data, Utils.vector( "Property name", "Property value" ) );
		table.packColumnsExceptLast();
		
		p.addCenter( table.createWrapperBox( true, table.createToolBarParams( p ) ) );
		
		return p;
	}
	
}
