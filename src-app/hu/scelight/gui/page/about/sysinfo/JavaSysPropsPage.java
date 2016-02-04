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

import java.util.Arrays;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.RowSorter.SortKey;
import javax.swing.SortOrder;

/**
 * Java System properties page.
 * 
 * @author Andras Belicza
 */
public class JavaSysPropsPage extends BasePage< JComponent > {
	
	/**
	 * Creates a new {@link JavaSysPropsPage}.
	 */
	public JavaSysPropsPage() {
		super( "System properties", Icons.F_DOCUMENT_ATTRIBUTE_S );
	}
	
	@Override
	public JComponent createPageComp() {
		final BorderPanel p = new BorderPanel();
		
		final XTable table = new XTable();
		
		final Vector< Vector< Object > > data = new Vector<>();
		
		final Properties props = System.getProperties();
		// Use name enumeration which properly returns names from the default properties of a property
		// (while HashTable.entrySet() does not!).
		final Enumeration< ? > nameEnum = props.propertyNames();
		while ( nameEnum.hasMoreElements() ) {
			final Object name = nameEnum.nextElement();
			data.add( Utils.vector( name, props.getProperty( name.toString() ) ) );
		}
		
		table.getXTableModel().setDataVector( data, Utils.vector( "Property name", "Property value" ) );
		table.getRowSorter().setSortKeys( Arrays.asList( new SortKey( 0, SortOrder.ASCENDING ) ) );
		table.packColumnsExceptLast();
		
		p.addCenter( table.createWrapperBox( true, table.createToolBarParams( p ) ) );
		
		return p;
	}
	
}
