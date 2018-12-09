/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.sllauncher.gui.comp.table;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.Date;
import java.util.Vector;

import javax.swing.DefaultRowSorter;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.RowFilter;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.RowSorterEvent;
import javax.swing.event.RowSorterListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import hu.scelightapibase.gui.comp.table.ITToolBarParams;
import hu.sllauncher.gui.comp.HelpIcon;
import hu.sllauncher.gui.comp.TextFilterComp;
import hu.sllauncher.gui.comp.TextSearchComp;
import hu.sllauncher.gui.comp.XLabel;
import hu.sllauncher.gui.comp.XToolBar;
import hu.sllauncher.gui.help.LHelps;
import hu.sllauncher.service.env.LEnv;
import hu.sllauncher.util.LUtils;
import hu.sllauncher.util.gui.searcher.IntPosBaseSearcher;

/**
 * {@link XToolBar} for {@link XTable}s which provides search and filtering functions.
 * 
 * @author Andras Belicza
 */
@SuppressWarnings( "serial" )
class XTableToolBar extends XToolBar {
	
	/** Table the tool bar is created for. */
	private final JTable			 table;
									 
	/** Text search component. */
	private final TextSearchComp	 searchComp		   = new TextSearchComp( false );
													   
	/** Searcher logic. */
	private final IntPosBaseSearcher searcher;
									 
	/** Include text filter component. */
	private final TextFilterComp	 includeFilterComp = new TextFilterComp();
													   
	/** Exclude text filter component. */
	private final TextFilterComp	 excludeFilterComp = new TextFilterComp( "Filter Out" );
													   
	/** Label to display rows count. */
	private final XLabel			 rowsCountLabel	   = new XLabel( "Rows:" );
													   
	/**
	 * Creates a new {@link XTableToolBar}.
	 * 
	 * @param params table tool bar parameters wrapper
	 */
	public XTableToolBar( final ITToolBarParams params ) {
		this.table = params.getTable().asTable();
		
		searcher = new IntPosBaseSearcher() {
			// This implementation searches by VIEW indices.
			private int maxCol;
			
			@Override
			protected void prepareNew() {
				maxPos = table.getRowCount() - 1;
				maxCol = table.getColumnCount() - 1;
				super.prepareNew();
			}
			
			@Override
			public Integer getStartPos() {
				return table.getSelectedRow() < 0 ? null : Integer.valueOf( table.getSelectedRow() );
			}
			
			@Override
			public boolean matches() {
				// Check row cell by cell
		        // Go upward so the cell to the most left is found and focused first
				for ( int column = 0; column <= maxCol; column++ ) {
					final Object data = table.getValueAt( searchPos, column ); // We go by view indices, so this is good
					
					if ( data != null && LUtils.containsIngoreCase( getCellText( data ), searchText ) ) {
						// Select row and focus cell
						table.setRowSelectionInterval( searchPos, searchPos );
						table.setColumnSelectionInterval( column, column );
						table.scrollRectToVisible( table.getCellRect( searchPos, column, true ) );
						return true;
					}
				}
				return false;
			}
		};
		
		buildGui( params );
	}
	
	/**
	 * Returns the text data of the specified cell object. Used when searching or filtering the table.
	 * 
	 * @param data cell object whose text data to return
	 * @return the text data of the specified cell object
	 */
	private static String getCellText( final Object data ) {
		return data instanceof Date ? LEnv.LANG.formatDateTime( (Date) data ) : data.toString();
	}
	
	/**
	 * Builds the GUI of the table tool bar.
	 * 
	 * @param params table tool bar parameters wrapper
	 */
	private void buildGui( final ITToolBarParams params ) {
		add( searchComp );
		searchComp.registerFocusHotkey( params.getRootComponent() );
		searchComp.setSearcher( searcher );
		
		addSeparator();
		includeFilterComp.textField.setToolTipText( "<html>Enter words to filter rows (<b>Include</b>). You can use 'OR' between words.</html>" );
		includeFilterComp.registerFocusHotkey( params.getRootComponent() );
		add( includeFilterComp );
		
		addSeparator();
		excludeFilterComp.textField.setToolTipText( "<html>Enter words to filter rows <b>out</b> (<b>Exclude</b>). You can use 'OR' between words.</html>" );
		excludeFilterComp.registerFocusHotkey( params.getRootComponent(), KeyStroke.getKeyStroke( KeyEvent.VK_T, InputEvent.CTRL_MASK ) );
		add( excludeFilterComp );
		
		addSeparator();
		add( new HelpIcon( LHelps.TABLE_FILTERS ) );
		addSeparator();
		rowsCountLabel.setToolTipText(
		        "<html><b>Listed</b> rows count &times; <b>All</b> rows count &times;&nbsp;<span style='color:white;background:#39698a'>&nbsp;<b>Selected</b>&nbsp;</span>&nbsp;rows count</html>" );
		add( rowsCountLabel );
		
		finalizeLayout();
		
		
		final Runnable updateRowsCount = new Runnable() {
			@Override
			public void run() {
				final int selected = table.getSelectedRowCount();
				rowsCountLabel.setText(
		                "<html>Rows: " + LEnv.LANG.formatNumber( table.getRowCount() ) + " &times; " + LEnv.LANG.formatNumber( table.getModel().getRowCount() )
		                        + ( selected > 0 ? " &times;&nbsp;<span style='color:white;background:#39698a'>&nbsp;" : " &times;&nbsp;&nbsp;" )
		                        + LEnv.LANG.formatNumber( selected ) + "&nbsp;" );
			}
		};
		
		@SuppressWarnings( "unchecked" )
		final DefaultRowSorter< TableModel, Integer > rowSorter = (DefaultRowSorter< TableModel, Integer >) table.getRowSorter();
		rowSorter.addRowSorterListener( new RowSorterListener() {
			@Override
			public void sorterChanged( final RowSorterEvent event ) {
				updateRowsCount.run();
			}
		} );
		
		final Runnable updateRowFilterTask = new Runnable() {
			@Override
			public void run() {
				searcher.clearLastSearchPos();
				
				final TextFilteringLogic tfl = new TextFilteringLogic( includeFilterComp.textField.getText(), excludeFilterComp.textField.getText() );
				
				if ( tfl.isFilterSpecified() )
					rowSorter.setRowFilter( new RowFilter< TableModel, Integer >() {
						private final StringBuilder rowStringBuilder = new StringBuilder(); // To reuse it
						
						@Override
						public boolean include( final Entry< ? extends TableModel, ? extends Integer > entry ) {
							@SuppressWarnings( "unchecked" )
							final Vector< Vector > dataVector = params.getTable().getXTableModel().getDataVector();
							
							rowStringBuilder.setLength( 0 );
							
							for ( final Object data : dataVector.get( entry.getIdentifier() ) )
								if ( data != null )
									rowStringBuilder.append( getCellText( data ) ).append( ' ' );
									
							return tfl.isIncluded( rowStringBuilder.toString() );
						}
					} );
				else
					rowSorter.setRowFilter( null );
			}
		};
		includeFilterComp.setFilterTask( updateRowFilterTask );
		excludeFilterComp.setFilterTask( updateRowFilterTask );
		
		table.getModel().addTableModelListener( new TableModelListener() {
			@Override
			public void tableChanged( final TableModelEvent event ) {
				// updateRowFilterTask will cause a re-sort which when done will try to restore selection,
		        // do this later when everything is all set and done (else ArrayIndexOutOfBounds exception might occur).
				SwingUtilities.invokeLater( new Runnable() {
					@Override
					public void run() {
						updateRowFilterTask.run();
					}
				} );
				updateRowsCount.run();
				searcher.clearLastSearchPos();
			}
		} );
		
		table.getSelectionModel().addListSelectionListener( new ListSelectionListener() {
			@Override
			public void valueChanged( final ListSelectionEvent event ) {
				// Also update rows count if value is adjusting, so users will see live selection count when they select e.g. with SHFIT+draggin with mouse
				updateRowsCount.run();
			}
		} );
		
		updateRowsCount.run();
	}
	
}
