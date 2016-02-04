/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight.bean;

import hu.scelight.gui.page.replist.column.IColumn;
import hu.scelight.gui.page.replist.column.impl.AvgApmColumn;
import hu.scelight.gui.page.replist.column.impl.AvgLeagueColumn;
import hu.scelight.gui.page.replist.column.impl.AvgSpmColumn;
import hu.scelight.gui.page.replist.column.impl.AvgSqColumn;
import hu.scelight.gui.page.replist.column.impl.DateColumn;
import hu.scelight.gui.page.replist.column.impl.ExpansionColumn;
import hu.scelight.gui.page.replist.column.impl.GameModeColumn;
import hu.scelight.gui.page.replist.column.impl.LeagueMatchupColumn;
import hu.scelight.gui.page.replist.column.impl.LengthColumn;
import hu.scelight.gui.page.replist.column.impl.MapImageColumn;
import hu.scelight.gui.page.replist.column.impl.MapNameColumn;
import hu.scelight.gui.page.replist.column.impl.PlayedAgoColumn;
import hu.scelight.gui.page.replist.column.impl.PlayersColumn;
import hu.scelight.gui.page.replist.column.impl.RaceMatchupColumn;
import hu.scelight.gui.page.replist.column.impl.RegionColumn;
import hu.scelight.gui.page.replist.column.impl.VersionColumn;
import hu.scelight.util.Utils;
import hu.sllauncher.bean.Bean;
import hu.sllauncher.service.env.LEnv;

import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXB;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlTransient;

/**
 * Bean holding column setup of the replay list tables.
 * 
 * <p>
 * Implementation note: The class names are marshaled instead of the {@link Class} objects itself because the class loader of the {@link JAXB} does not know
 * about the column implementation classes, they are loaded with a custom {@link URLClassLoader} created by the launcher. <br>
 * But the class shows an interface of a list of classes not class names. The conversion is done automatically by this class. But this also means that if the
 * class list returned by {@link #getColumnClassList()} is modified, it has to be re-set by {@link #setColumnClassList(List)} in order to the internal class
 * name list to remain consistent.
 * </p
 * 
 * @author Andras Belicza
 */
@XmlAccessorType( XmlAccessType.FIELD )
public class RepListColumnsBean extends Bean {
	
	/** Current bean version. */
	public static final int BEAN_VER = 1;
	
	/**
	 * Factory method to return the default rep list columns bean.
	 * 
	 * @return the default rep list columns bean
	 */
	public static RepListColumnsBean getDefaults() {
		final RepListColumnsBean rlc = new RepListColumnsBean();
		
		@SuppressWarnings( "unchecked" )
		final List< Class< IColumn< ? > > > list = (List< Class< IColumn< ? > > >) (Object) Utils.asNewList( RegionColumn.class, DateColumn.class,
		        PlayedAgoColumn.class, ExpansionColumn.class, VersionColumn.class, GameModeColumn.class, MapImageColumn.class, MapNameColumn.class,
		        LengthColumn.class, RaceMatchupColumn.class, AvgLeagueColumn.class, AvgApmColumn.class, AvgSpmColumn.class, AvgSqColumn.class,
		        PlayersColumn.class, LeagueMatchupColumn.class );
		rlc.setColumnClassList( list );
		
		return rlc;
	}
	
	/** Visible column class name list. */
	private List< String >                columnClassNameList;
	
	/** Visible column class list. */
	@XmlTransient
	private List< Class< IColumn< ? > > > columnClassList;
	
	/**
	 * Creates a new {@link RepListColumnsBean}.
	 */
	public RepListColumnsBean() {
		super( BEAN_VER );
	}
	
	/**
	 * Returns the visible column class list.
	 * 
	 * @return the visible column class list
	 */
	public List< Class< IColumn< ? > > > getColumnClassList() {
		if ( columnClassList == null ) {
			columnClassList = new ArrayList<>();
			
			for ( final String className : columnClassNameList )
				try {
					@SuppressWarnings( "unchecked" )
					final Class< IColumn< ? > > colClass = (Class< IColumn< ? > >) Class.forName( className );
					columnClassList.add( colClass );
				} catch ( final Exception e ) {
					LEnv.LOGGER.error( "Failed to initialize column class: " + className );
				}
		}
		
		return columnClassList;
	}
	
	/**
	 * Sets the visible column class list.
	 * 
	 * @param columnClassList visible column class list to be set
	 */
	public void setColumnClassList( List< Class< IColumn< ? > > > columnClassList ) {
		this.columnClassList = columnClassList;
		
		columnClassNameList = new ArrayList<>();
		for ( final Class< IColumn< ? > > colClass : columnClassList )
			columnClassNameList.add( colClass.getName() );
	}
	
}
