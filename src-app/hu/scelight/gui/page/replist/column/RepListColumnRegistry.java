/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight.gui.page.replist.column;

import hu.scelight.gui.page.replist.column.impl.Apm1Column;
import hu.scelight.gui.page.replist.column.impl.Apm2Column;
import hu.scelight.gui.page.replist.column.impl.AvgApmColumn;
import hu.scelight.gui.page.replist.column.impl.AvgLeagueColumn;
import hu.scelight.gui.page.replist.column.impl.AvgSpmColumn;
import hu.scelight.gui.page.replist.column.impl.AvgSqColumn;
import hu.scelight.gui.page.replist.column.impl.AvgSupplyCappedColumn;
import hu.scelight.gui.page.replist.column.impl.ChatColumn;
import hu.scelight.gui.page.replist.column.impl.CustomColumn1;
import hu.scelight.gui.page.replist.column.impl.CustomColumn2;
import hu.scelight.gui.page.replist.column.impl.CustomColumn3;
import hu.scelight.gui.page.replist.column.impl.CustomColumn4;
import hu.scelight.gui.page.replist.column.impl.CustomColumn5;
import hu.scelight.gui.page.replist.column.impl.DateColumn;
import hu.scelight.gui.page.replist.column.impl.ExpansionColumn;
import hu.scelight.gui.page.replist.column.impl.FavoredResultColumn;
import hu.scelight.gui.page.replist.column.impl.FileColumn;
import hu.scelight.gui.page.replist.column.impl.FileNameColumn;
import hu.scelight.gui.page.replist.column.impl.FileSizeColumn;
import hu.scelight.gui.page.replist.column.impl.FormatColumn;
import hu.scelight.gui.page.replist.column.impl.FullVersionColumn;
import hu.scelight.gui.page.replist.column.impl.GameModeColumn;
import hu.scelight.gui.page.replist.column.impl.League1Column;
import hu.scelight.gui.page.replist.column.impl.League2Column;
import hu.scelight.gui.page.replist.column.impl.LeagueMatchupColumn;
import hu.scelight.gui.page.replist.column.impl.LengthColumn;
import hu.scelight.gui.page.replist.column.impl.MapImageColumn;
import hu.scelight.gui.page.replist.column.impl.MapNameColumn;
import hu.scelight.gui.page.replist.column.impl.PlayedAgoColumn;
import hu.scelight.gui.page.replist.column.impl.PlayersColumn;
import hu.scelight.gui.page.replist.column.impl.RaceMatchupColumn;
import hu.scelight.gui.page.replist.column.impl.RegionColumn;
import hu.scelight.gui.page.replist.column.impl.Spm1Column;
import hu.scelight.gui.page.replist.column.impl.Spm2Column;
import hu.scelight.gui.page.replist.column.impl.Sq1Column;
import hu.scelight.gui.page.replist.column.impl.Sq2Column;
import hu.scelight.gui.page.replist.column.impl.SupplyCapped1Column;
import hu.scelight.gui.page.replist.column.impl.SupplyCapped2Column;
import hu.scelight.gui.page.replist.column.impl.VersionColumn;
import hu.scelight.service.env.Env;
import hu.scelight.util.Utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Manager {@link IColumn} implementations.
 * 
 * @author Andras Belicza
 */
public class RepListColumnRegistry {
	
	/** Cached instances of columns. Mapped from column class. */
	private static final Map< Class< ? extends IColumn< ? > >, IColumn< ? > > CLASS_COLUMN_MAP = new HashMap<>();
	
	/** List of all available columns (classes implementing {@link IColumn}). */
	public static final List< Class< ? extends IColumn< ? > > >               COLUMN_LIST;
	static {
		@SuppressWarnings( "unchecked" )
		final List< Class< ? extends IColumn< ? > > > list = Utils.< Class< ? extends IColumn< ? > > > asNewList( RegionColumn.class, DateColumn.class,
		        ExpansionColumn.class, VersionColumn.class, GameModeColumn.class, MapImageColumn.class, MapNameColumn.class, LengthColumn.class,
		        RaceMatchupColumn.class, FormatColumn.class, PlayedAgoColumn.class, FullVersionColumn.class, LeagueMatchupColumn.class, ChatColumn.class,
		        FavoredResultColumn.class, PlayersColumn.class, FileNameColumn.class, FileSizeColumn.class, FileColumn.class, AvgApmColumn.class,
		        AvgSpmColumn.class, AvgSqColumn.class, AvgSupplyCappedColumn.class, AvgLeagueColumn.class, Apm1Column.class, Apm2Column.class,
		        Spm1Column.class, Spm2Column.class, Sq1Column.class, Sq2Column.class, SupplyCapped1Column.class, SupplyCapped2Column.class,
		        League1Column.class, League2Column.class, CustomColumn1.class, CustomColumn2.class, CustomColumn3.class, CustomColumn4.class,
		        CustomColumn5.class );
		
		COLUMN_LIST = list;
		
		// Cache non-dependent column instances
		for ( final Class< ? extends IColumn< ? > > colClass : list ) {
			if ( colClass.isAnnotationPresent( Dependent.class ) )
				continue;
			CLASS_COLUMN_MAP.put( colClass, createColumnInstance( colClass ) );
		}
	}
	
	/**
	 * Returns an instance of the column specified by its class.
	 * 
	 * @param columnClass column class to return an instance for
	 * @return an instance of the column specified by its class
	 */
	public static IColumn< ? > getColumnInstance( final Class< ? extends IColumn< ? > > columnClass ) {
		final IColumn< ? > column = CLASS_COLUMN_MAP.get( columnClass );
		
		return column == null ? createColumnInstance( columnClass ) : column;
	}
	
	/**
	 * Returns a <i>new</i> instance of the column specified by its class.
	 * 
	 * @param columnClass column class to return a <i>new</i> instance for
	 * @return a <i>new</i> instance of the column specified by its class or <code>null</code> if the column cannot be instantiated
	 */
	private static IColumn< ? > createColumnInstance( final Class< ? extends IColumn< ? > > columnClass ) {
		try {
			return columnClass.newInstance();
		} catch ( final Exception e ) {
			Env.LOGGER.error( "Failed to instantiate column: " + ( columnClass == null ? "null" : columnClass.getName() ) );
			return null;
		}
	}
	
}
