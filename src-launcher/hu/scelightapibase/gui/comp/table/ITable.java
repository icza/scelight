/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelightapibase.gui.comp.table;

import hu.scelightapibase.action.IAction;
import hu.scelightapibase.gui.comp.IBorderPanel;
import hu.scelightapibase.gui.comp.IProgressBar;
import hu.scelightapibase.gui.comp.IScrollPane;
import hu.scelightapibase.gui.comp.IToolBarForTable;
import hu.scelightapibase.gui.comp.table.renderer.IBarCodeView;
import hu.scelightapibase.gui.comp.table.renderer.IProgressBarView;

import java.awt.Container;
import java.util.Set;

import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 * An improved {@link JTable} with advanced rendering and editing capabilities and built-in user event handling for links.
 * 
 * <p>
 * Also creates and sets an improved table cell renderer with the unified rendering logic incorporated (
 * {@link hu.scelightapi.util.gui.IGuiUtils#renderToLabel(javax.swing.JLabel, Object, boolean, boolean)}).<br>
 * Additionally if a cell value is a {@link JComponent}, it will be returned as the renderer component.
 * </p>
 * 
 * <p>
 * Overrides tool tip creation and placement to return cell value as tool tip if it's truncated (due to not fitting into its cell's area). Positions tool tips
 * exactly over the cell they belong to, also avoids showing empty tool tips (by returning <code>null</code>).
 * </p>
 * 
 * <p>
 * {@link ITable}s are added to {@link Container}s (usually to the center of a {@link IBorderPanel}) by adding the table wrapper component returned by
 * {@link #createWrapperBox(boolean)} or {@link #createWrapperBox(boolean, ITToolBarParams)}.<br>
 * Example table with table tool bar having search and filter support:
 * 
 * <pre>
 * <blockquote style='border:1px solid black'>
 * IBorderPanel p = guiFactory.newBorderPanel();
 * 
 * ITable table = guiFactory.newTable();
 * // Add data to table model
 * 
 * p.addCenter( table.createWrapperBox( true, table.createToolBarParams( p ) ) );
 * </blockquote>
 * </pre>
 * 
 * </p>
 * 
 * @author Andras Belicza
 * 
 * @see ITableModel
 * @see ITableRowSorter
 * @see IToolBarForTable
 * @see IProgressBarView
 * @see IBarCodeView
 * @see hu.scelightapi.service.IGuiFactory#newTable()
 */
public interface ITable {
	
	/**
	 * Casts this instance to {@link JTable}.
	 * 
	 * @return <code>this</code> as a {@link JTable}
	 */
	JTable asTable();
	
	/**
	 * Returns the VIEW index of the focused row, -1 if no row is focused.
	 * 
	 * @return the VIEW index of the focused row, -1 if no row is focused
	 */
	int getFocusedRow();
	
	/**
	 * Creates and returns a vertical {@link Box} holding the table header and the table itself optionally wrapped in a {@link IScrollPane}, with no table tool
	 * bar.
	 * 
	 * @param wrapInScrollPane tells if the table has to be added wrapped in a {@link IScrollPane}
	 * @return a {@link Box} holding the table header and the table "body"
	 * 
	 * @see #createToolBarParams(JComponent)
	 */
	JComponent createWrapperBox( boolean wrapInScrollPane );
	
	/**
	 * Creates and returns a vertical {@link Box} holding the table header and the table itself optionally wrapped in a {@link IScrollPane}.
	 * 
	 * @param wrapInScrollPane tells if the table has to be added wrapped in a {@link IScrollPane}
	 * @param toolBarParams optional table tool bar parameters wrapper; if missing, the created wrapper box will not have a table tool bar
	 * @return a {@link Box} holding the table header and the table "body"
	 * 
	 * @see #createToolBarParams(JComponent)
	 */
	JComponent createWrapperBox( boolean wrapInScrollPane, ITToolBarParams toolBarParams );
	
	/**
	 * Creates and returns a new table tool bar parameters wrapper.
	 * 
	 * @param rootComponent root component to register key strokes at
	 * @return a new table tool bar parameters wrapper
	 * 
	 * @see #createWrapperBox(boolean, ITToolBarParams)
	 */
	ITToolBarParams createToolBarParams( JComponent rootComponent );
	
	/**
	 * Returns the internal, improved table cell renderer of this improved table.
	 * 
	 * @return the internal, improved table cell renderer of this improved table
	 */
	TableCellRenderer getXTableCellRenderer();
	
	/**
	 * Sets the table cell renderer to be returned by {@link JTable#getCellRenderer(int, int)}.
	 * 
	 * @param tableCellRenderer table cell renderer to be set
	 */
	void setTableCellRenderer( TableCellRenderer tableCellRenderer );
	
	/**
	 * Returns the table cell renderer which is returned by {@link JTable#getCellRenderer(int, int)}.
	 * 
	 * @return the table cell renderer which is returned by {@link JTable#getCellRenderer(int, int)}
	 */
	TableCellRenderer getTableCellRenderer();
	
	/**
	 * Returns the improved table model of this improved table.
	 * 
	 * @return the improved table model of this improved table
	 */
	ITableModel getXTableModel();
	
	/**
	 * Returns the improved table row sorter of this improved table.
	 * 
	 * @return the improved table row sorter of this improved table
	 */
	ITableRowSorter getXTableRowSorter();
	
	/**
	 * Sets if the table is editable.
	 * 
	 * @param editable tells whether table is editable
	 * 
	 * @see #isEditable()
	 * @see #setEditableColModelIndices(int...)
	 */
	void setEditable( boolean editable );
	
	/**
	 * Tells if the table is editable.
	 * 
	 * @return true if the table is editable; false otherwise
	 * 
	 * @see #setEditable(boolean)
	 */
	boolean isEditable();
	
	/**
	 * Sets the editable column model indices.
	 * 
	 * @param editableColModelIndices editable column model indices to be set
	 */
	void setEditableColModelIndices( int... editableColModelIndices );
	
	/**
	 * Overrides {@link JTable#getCellRenderer(int, int)} to returns the table cell renderer explicitly set by {@link #setTableCellRenderer(TableCellRenderer)},
	 * or else the internal improved table cell renderer.
	 * 
	 * @param row row of the cell whose renderer to return
	 * @param column column of the cell whose renderer to return
	 * @return the table cell renderer explicitly set by {@link #setTableCellRenderer(TableCellRenderer)}, or else the internal improved table cell renderer
	 */
	TableCellRenderer getCellRenderer( int row, int column );
	
	/**
	 * Packs the table.<br>
	 * Resizes all columns (by setting the PREFERRED width of the column) to the maximum width of the values in each column.
	 * 
	 * @see #packColumns(int...)
	 * @see #packColumnsExceptLast()
	 */
	void pack();
	
	/**
	 * Packs the specified columns.<br>
	 * Resizes the specified column (by setting the PREFERRED and MAXIMUM width of the columns) to the maximum width of the values in the column.
	 * 
	 * @param columns column view indices to be packed
	 * 
	 * @see #pack()
	 * @see #packColumnsExceptLast()
	 */
	void packColumns( int... columns );
	
	/**
	 * Packs all columns except the last (last by view idx).<br>
	 * Resizes all columns except the last (by setting the PREFERRED and MAXIMUM width of the columns) to the maximum width of the values in the column.
	 * 
	 * @see #pack()
	 * @see #packColumns(int...)
	 */
	void packColumnsExceptLast();
	
	/**
	 * Returns the selected row MODEL indices.
	 * 
	 * @return the selected row MODEL indices
	 */
	int[] getSelectedModelRows();
	
	/**
	 * Returns the first selected row MODEL index.
	 * 
	 * @return the first selected row MODEL index
	 */
	int getSelectedModelRow();
	
	/**
	 * Enables or disables table sorting.
	 * 
	 * <p>
	 * Iterates over all current columns, and sets their sortable property at the row sorter.<br>
	 * Does not remove the row sorter because it is needed for the table tool bar.
	 * </p>
	 * 
	 * @param sortable tells if table is sortable
	 */
	void setSortable( boolean sortable );
	
	/**
	 * Sets the open action to be called when the user double clicks or presses the ENTER key.
	 * 
	 * @param openAction open action to be set
	 * 
	 * @see #setCtrlOpenAction(IAction)
	 * @see #setShiftOpenAction(IAction)
	 */
	void setOpenAction( IAction openAction );
	
	/**
	 * Sets the ctrl-open action to be called when the user presses the CTRL+ENTER keys.
	 * 
	 * @param ctrlOpenAction ctrl-open action to be set
	 * 
	 * @see #setOpenAction(IAction)
	 * @see #setShiftOpenAction(IAction)
	 */
	void setCtrlOpenAction( IAction ctrlOpenAction );
	
	/**
	 * Sets the shift-open action to be called when the user presses the SHIFT+ENTER keys.
	 * 
	 * @param shiftOpenAction shift-open action to be set
	 * 
	 * @see #setOpenAction(IAction)
	 * @see #setCtrlOpenAction(IAction)
	 */
	void setShiftOpenAction( IAction shiftOpenAction );
	
	/**
	 * Sets the delete action to be called when the user presses the DEL key.
	 * 
	 * @param deleteAction delete action to be set
	 */
	void setDeleteAction( IAction deleteAction );
	
	
	/**
	 * Saves the current selection for later restoring by {@link #restoreSelection(int)}.
	 * 
	 * <p>
	 * <i>Implementation details:</i><br>
	 * Saving the selection is done by saving the object at the model column <code>idColIdx</code> of each selected row in a {@link Set}.<br>
	 * Upon restoring the selection, the object at the model column <code>idColIdx</code> of each row is checked if contained in the saved selection, and if so,
	 * the row will be selected. This solution is based on the {@link Object#equals(Object)} method of the id objects of the rows.
	 * </p>
	 * 
	 * @param idColIdx column model index whose value to be used as the row id
	 * 
	 * @see #restoreSelection(int)
	 */
	void saveSelection( int idColIdx );
	
	/**
	 * Restores the selection that was saved by {@link #saveSelection(int)}.
	 * 
	 * <p>
	 * After restoring the selection, the saved selection is cleared / discarded.
	 * </p>
	 * 
	 * <p>
	 * <i>For implementation details see {@link #saveSelection(int)}.</i>
	 * </p>
	 * 
	 * @param idColIdx column model index whose value to be used as the row id selection
	 * 
	 * @see #saveSelection(int)
	 */
	void restoreSelection( int idColIdx );
	
	/**
	 * Sets a bigger minimum row height which is suitable to display progress bar cells.
	 * 
	 * @since 1.2
	 * 
	 * @see IProgressBarView
	 * @see IProgressBar
	 */
	void setRowHeightForProgressBar();
	
}
