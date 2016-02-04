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

import hu.scelightapibase.action.IAction;
import hu.scelightapibase.bean.settings.ISettingChangeEvent;
import hu.scelightapibase.bean.settings.ISettingChangeListener;
import hu.scelightapibase.gui.comp.table.ITToolBarParams;
import hu.scelightapibase.gui.comp.table.ITable;
import hu.sllauncher.bean.settings.type.Setting;
import hu.sllauncher.gui.comp.BorderPanel;
import hu.sllauncher.gui.comp.XScrollPane;
import hu.sllauncher.gui.comp.table.editor.BoolCellEditor;
import hu.sllauncher.gui.comp.table.editor.DateCellEditor;
import hu.sllauncher.gui.comp.table.editor.IntegerCellEditor;
import hu.sllauncher.gui.comp.table.editor.PathCellEditor;
import hu.sllauncher.gui.comp.table.renderer.XTableCellRenderer;
import hu.sllauncher.gui.setting.LSettingsGui;
import hu.sllauncher.service.env.LEnv;
import hu.sllauncher.service.settings.LSettings;
import hu.sllauncher.util.LUtils;
import hu.sllauncher.util.gui.LGuiUtils;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.nio.file.Path;
import java.util.Date;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.Box;
import javax.swing.DefaultRowSorter;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

/**
 * An improved {@link JTable} with advanced rendering and editing capabilities and built-in user event handling for links.
 * 
 * @author Andras Belicza
 */
@SuppressWarnings( "serial" )
public class XTable extends JTable implements ITable {
	
	/** Improved table model . */
	private final XTableModel        tableModel;
	
	/** Improved table row sorter. */
	private final XTableRowSorter    tableRowSorter;
	
	/** Improved table cell renderer. */
	private final XTableCellRenderer xtableCellRenderer = new XTableCellRenderer();
	
	/** Explicitly set table cell renderer. */
	private TableCellRenderer        tableCellRenderer;
	
	/** Editable column model indices. */
	private int[]                    editableColModelIndices;
	
	/** Tells if table is editable. */
	private boolean                  editable;
	
	/** Optional open action to be called if the user double clicks or presses the ENTER key. */
	private IAction                  openAction;
	
	/** Optional open action to be called if the user presses the CTRL+ENTER keys. */
	private IAction                  ctrlOpenAction;
	
	/** Optional open action to be called if the user presses the SHIFT+ENTER keys. */
	private IAction                  shiftOpenAction;
	
	/** Optional delete action to be called if the user presses the DEL key. */
	private IAction                  deleteAction;
	
	
	/**
	 * Creates a new {@link XTable}.
	 */
	public XTable() {
		super( new XTableModel() );
		tableModel = (XTableModel) getModel();
		
		setRowSorter( tableRowSorter = new XTableRowSorter( tableModel ) );
		
		// To handle links and hand cursor over links
		final MouseAdapter mouseAdapter = new MouseAdapter() {
			@Override
			public void mouseMoved( final MouseEvent event ) {
				final Object value = getValueAtEvent( event );
				setCursor( value != null && value instanceof URL ? Cursor.getPredefinedCursor( Cursor.HAND_CURSOR ) : null );
			}
			
			@Override
			public void mouseClicked( final MouseEvent event ) {
				if ( isEditing() )
					return;
				
				// If CTRL is held down and double clicked on an unselected row, nothing will be selected after the 2nd click,
				// so do not call open action in this case.
				if ( openAction != null && event.getButton() == LGuiUtils.MOUSE_BTN_LEFT && event.getClickCount() == 2 && getSelectedRow() >= 0 )
					openAction.actionPerformed( null );
				
				final Object value = getValueAtEvent( event );
				if ( value == null )
					return;
				
				if ( value instanceof URL )
					LEnv.UTILS_IMPL.get().showURLInBrowser( (URL) value );
			}
			
			/**
			 * Returns the value at the cell under the specified event.
			 * 
			 * @param event event to return the value of cell being under this
			 * @return the value at the cell under the specified event
			 */
			private Object getValueAtEvent( final MouseEvent event ) {
				final Point p = event.getPoint();
				final int row = rowAtPoint( p );
				final int column = columnAtPoint( p );
				if ( row == -1 || column == -1 )
					return null;
				
				// Row and Column are VIEW INDEXES, JTable.getValueAt() requires view indices, so we're good
				return getValueAt( row, column );
			}
		};
		
		addMouseListener( mouseAdapter );
		addMouseMotionListener( mouseAdapter );
		
		setDefaultEditor( Boolean.class, BoolCellEditor.INSTANCE );
		setDefaultEditor( Path.class, PathCellEditor.INSTANCE );
		setDefaultEditor( Integer.class, IntegerCellEditor.INSTANCE );
		setDefaultEditor( Date.class, DateCellEditor.INSTANCE );
		
		setGridColor( Color.GRAY );
		
		final ISettingChangeListener scl = new ISettingChangeListener() {
			@Override
			public void valuesChanged( final ISettingChangeEvent event ) {
				if ( event.affected( LSettings.SHOW_VERT_TABLE_LINES ) )
					setShowVerticalLines( event.get( LSettings.SHOW_VERT_TABLE_LINES ) );
				if ( event.affected( LSettings.SHOW_HORIZ_TABLE_LINES ) )
					setShowHorizontalLines( event.get( LSettings.SHOW_HORIZ_TABLE_LINES ) );
			}
		};
		LSettingsGui.addBindExecuteScl( scl, LEnv.LAUNCHER_SETTINGS,
		        LUtils.< Setting< ? > > asNewSet( LSettings.SHOW_VERT_TABLE_LINES, LSettings.SHOW_HORIZ_TABLE_LINES ), this );
		
		registerGeneralHotkeys();
	}
	
	@Override
	public JTable asTable() {
		return this;
	}
	
	/**
	 * Registers GENERAL hotkeys for the table.
	 */
	private void registerGeneralHotkeys() {
		final InputMap inputMap = getInputMap( JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT );
		final ActionMap actionMap = getActionMap();
		
		Object actionKey;
		
		// Enter to call the open action if set
		inputMap.put( KeyStroke.getKeyStroke( KeyEvent.VK_ENTER, 0 ), actionKey = new Object() );
		actionMap.put( actionKey, new AbstractAction() {
			@Override
			public void actionPerformed( final ActionEvent event ) {
				if ( isEditing() )
					getCellEditor().stopCellEditing();
				else if ( openAction != null )
					openAction.actionPerformed( null );
			}
		} );
		
		// CTRL+Enter to call the ctrl-open action if set
		inputMap.put( KeyStroke.getKeyStroke( KeyEvent.VK_ENTER, InputEvent.CTRL_MASK ), actionKey = new Object() );
		actionMap.put( actionKey, new AbstractAction() {
			@Override
			public void actionPerformed( final ActionEvent event ) {
				if ( isEditing() )
					getCellEditor().stopCellEditing();
				else if ( ctrlOpenAction != null )
					ctrlOpenAction.actionPerformed( null );
			}
		} );
		
		// SHIFT+Enter to call the shift-open action if set
		inputMap.put( KeyStroke.getKeyStroke( KeyEvent.VK_ENTER, InputEvent.SHIFT_MASK ), actionKey = new Object() );
		actionMap.put( actionKey, new AbstractAction() {
			@Override
			public void actionPerformed( final ActionEvent event ) {
				if ( isEditing() )
					getCellEditor().stopCellEditing();
				else if ( shiftOpenAction != null )
					shiftOpenAction.actionPerformed( null );
			}
		} );
		
		// DEL to call the delete action if set
		inputMap.put( KeyStroke.getKeyStroke( KeyEvent.VK_DELETE, 0 ), actionKey = new Object() );
		actionMap.put( actionKey, new AbstractAction() {
			@Override
			public void actionPerformed( final ActionEvent event ) {
				if ( deleteAction != null )
					deleteAction.actionPerformed( null );
			}
		} );
		
		// Home to select first row
		inputMap.put( KeyStroke.getKeyStroke( KeyEvent.VK_HOME, 0 ), actionKey = new Object() );
		actionMap.put( actionKey, new AbstractAction() {
			@Override
			public void actionPerformed( final ActionEvent event ) {
				// Select first row and scroll to it.
				getSelectionModel().setSelectionInterval( 0, 0 );
				scrollRectToVisible( getCellRect( 0, 0, true ) );
			}
		} );
		
		// End to select last row
		inputMap.put( KeyStroke.getKeyStroke( KeyEvent.VK_END, 0 ), actionKey = new Object() );
		actionMap.put( actionKey, new AbstractAction() {
			@Override
			public void actionPerformed( final ActionEvent event ) {
				// Select last row and scroll to it.
				getSelectionModel().setSelectionInterval( getRowCount() - 1, getRowCount() - 1 );
				scrollRectToVisible( getCellRect( getRowCount() - 1, 0, true ) );
			}
		} );
		
		// SHIFT+End to select/deselect all rows starting from the focused row 'till the end
		inputMap.put( KeyStroke.getKeyStroke( KeyEvent.VK_END, InputEvent.SHIFT_MASK ), actionKey = new Object() );
		actionMap.put( actionKey, new AbstractAction() {
			@Override
			public void actionPerformed( final ActionEvent event ) {
				final int focusedRow = getFocusedRow();
				// VIEW index is returned as focused row
				if ( focusedRow < 0 )
					return;
				getSelectionModel().setSelectionInterval( focusedRow, getRowCount() - 1 );
				scrollRectToVisible( getCellRect( getRowCount() - 1, 0, true ) );
			}
		} );
		
		// SHIFT+Home to select/deselect all rows starting from the focused row up to the first row
		inputMap.put( KeyStroke.getKeyStroke( KeyEvent.VK_HOME, InputEvent.SHIFT_MASK ), actionKey = new Object() );
		actionMap.put( actionKey, new AbstractAction() {
			@Override
			public void actionPerformed( final ActionEvent event ) {
				final int focusedRow = getFocusedRow();
				// VIEW index is returned as focused row
				if ( focusedRow < 0 )
					return;
				// Pass focusedRow as first param so that will be focused
				getSelectionModel().setSelectionInterval( focusedRow, 0 );
				scrollRectToVisible( getCellRect( 0, 0, true ) );
			}
		} );
	}
	
	@Override
	public int getFocusedRow() {
		return getSelectionModel().getLeadSelectionIndex();
	}
	
	@Override
	protected JTableHeader createDefaultTableHeader() {
		return new XTableHeader( columnModel );
	}
	
	@Override
	public JComponent createWrapperBox( final boolean wrapInScrollPane ) {
		return createWrapperBox( wrapInScrollPane, null );
	}
	
	@Override
	public JComponent createWrapperBox( final boolean wrapInScrollPane, final ITToolBarParams toolBarParams ) {
		final BorderPanel p = new BorderPanel();
		
		if ( toolBarParams != null )
			p.addNorth( new XTableToolBar( toolBarParams ) );
		
		// If scroll pane is needed, we create a scroll pane that does not follow vertical scrolling amount setting.
		// Reason: By default JTable supplies the scrolling amount by itself by implementing the Scrollable interface (and getScrollableUnitIncrement() method).
		// Scrollable.getScrollableUnitIncrement() is implemented in a way that it returns a unit increment so that complete rows are scrolled
		// (a complete row is visible at the top, no half row).
		// Most likely there's a bug that causes rendering problems if a different unit is returned, because repainting is optimized
		// and the image of the top/bottom row is copied instead of re-rendered causing for example the header to appear on top of the table...
		
		final Box box = Box.createVerticalBox();
		box.add( getTableHeader() );
		box.add( wrapInScrollPane ? new XScrollPane( this, true, false ) : this );
		p.addCenter( box );
		
		return p;
	}
	
	@Override
	public TToolBarParams createToolBarParams( final JComponent rootComponent ) {
		return new TToolBarParams( this, rootComponent );
	}
	
	@Override
	public XTableCellRenderer getXTableCellRenderer() {
		return xtableCellRenderer;
	}
	
	@Override
	public void setTableCellRenderer( final TableCellRenderer tableCellRenderer ) {
		this.tableCellRenderer = tableCellRenderer;
	}
	
	@Override
	public TableCellRenderer getTableCellRenderer() {
		return tableCellRenderer;
	}
	
	@Override
	public XTableModel getXTableModel() {
		return tableModel;
	}
	
	@Override
	public XTableRowSorter getXTableRowSorter() {
		return tableRowSorter;
	}
	
	@Override
	public void setEditable( final boolean editable ) {
		putClientProperty( "terminateEditOnFocusLost", Boolean.TRUE );
		this.editable = editable;
	}
	
	@Override
	public boolean isEditable() {
		return editable;
	}
	
	@Override
	public void setEditableColModelIndices( final int... editableColModelIndices ) {
		this.editableColModelIndices = editableColModelIndices;
	}
	
	@Override
	public boolean isCellEditable( final int row, final int column ) {
		return editable && ( editableColModelIndices == null || LUtils.contains( editableColModelIndices, convertColumnIndexToModel( column ) ) );
	}
	
	/**
	 * Returns the table cell renderer explicitly set by {@link #setTableCellRenderer(TableCellRenderer)}, or else the internal {@link XTableCellRenderer}.
	 */
	@Override
	public TableCellRenderer getCellRenderer( final int row, final int column ) {
		return tableCellRenderer == null ? xtableCellRenderer : tableCellRenderer;
	}
	
	@Override
	public void pack() {
		for ( int column = getColumnCount() - 1; column >= 0; column-- )
			packColumn( column, false );
	}
	
	@Override
	public void packColumns( final int... columns ) {
		for ( final int column : columns )
			packColumn( column, true );
	}
	
	@Override
	public void packColumnsExceptLast() {
		for ( int column = getColumnCount() - 2; column >= 0; column-- )
			packColumn( column, true );
	}
	
	/**
	 * Packs the specified column.<br>
	 * Resizes the specified column (by setting the preferred and optionally the maximum width of the column) to the maximum width of the values in the column.
	 * 
	 * @param column column view index to be packed
	 * @param alsoSetMaxSize tells if max size of the column is also to be set along with the preferred size
	 */
	private void packColumn( final int column, final boolean alsoSetMaxSize ) {
		// Let's start with a minimum width being the preferred width of the column header
		int maxWidth = getTableHeader().getDefaultRenderer()
		        .getTableCellRendererComponent( this, getColumnModel().getColumn( column ).getIdentifier(), false, false, 0, column ).getPreferredSize().width;
		
		for ( int row = getRowCount() - 1; row >= 0; row-- )
			maxWidth = Math.max( maxWidth,
			        getCellRenderer( row, column ).getTableCellRendererComponent( this, getValueAt( row, column ), false, false, row, column )
			                .getPreferredSize().width );
		
		getColumnModel().getColumn( column ).setPreferredWidth( maxWidth );
		if ( alsoSetMaxSize )
			getColumnModel().getColumn( column ).setMaxWidth( maxWidth );
	}
	
	@Override
	public int[] getSelectedModelRows() {
		// getSelectedRows() returns VIEW indices
		final int[] rows = getSelectedRows();
		
		for ( int i = rows.length - 1; i >= 0; i-- )
			rows[ i ] = convertRowIndexToModel( rows[ i ] );
		
		return rows;
	}
	
	@Override
	public int getSelectedModelRow() {
		// getSelectedRow() returns VIEW index
		return convertRowIndexToModel( getSelectedRow() );
	}
	
	@Override
	public void setSortable( final boolean sortable ) {
		@SuppressWarnings( "unchecked" )
		final DefaultRowSorter< TableModel, Integer > rowSorter = (DefaultRowSorter< TableModel, Integer >) getRowSorter();
		for ( int i = tableModel.getColumnCount() - 1; i >= 0; i-- )
			rowSorter.setSortable( i, sortable );
	}
	
	/**
	 * Overridden to return cell value as tool tip if it's truncated (due to not fitting into its cell's area).
	 */
	@Override
	public String getToolTipText( final MouseEvent event ) {
		// First check if super implementation returns a tool tip:
		// (super implementation checks if a tool tip is set on the renderer for the cell,
		// and if no, also checks if a tool tip has been set on the table itself)
		final String tip = super.getToolTipText( event );
		if ( tip != null )
			return tip;
		
		// No tool tip from the renderer, and no own tool tip has been set.
		// So let's check if value doesn't fit into the cell, and if so provide it's full text as the tool tip
		final Point point = event.getPoint();
		
		final int column = columnAtPoint( point );
		final int row = rowAtPoint( point );
		
		// Check if value is truncated (doesn't fit into the cell):
		if ( column < 0 || row < 0 ) // event is not on a cell
			return null;
		
		final Object value = getValueAt( row, column );
		if ( value == null )
			return null;
		
		// Check if value is truncated (doesn't fit into the cell):
		final Rectangle cellRect = getCellRect( row, column, false );
		if ( cellRect.width >= prepareRenderer( getCellRenderer( row, column ), row, column ).getPreferredSize().width )
			return null; // Value fits in the cell, no need to show tool tip
			
		// At this point no one provided tool tip, and value is rendered truncated. Provide its full text as the tool tip.
		int width = ( getWidth() - cellRect.x ) * 3 / 4;
		if ( width < 64 ) // Use a minimum width
			width = 64;
		return "<html><p style='width:" + width + "px'>" + value.toString() + "</p></html>";
	}
	
	/**
	 * Positions tool tips exactly over the cell they belong to, also avoids showing empty tool tips (by returning <code>null</code>).
	 */
	@Override
	public Point getToolTipLocation( final MouseEvent event ) {
		// If tool tip is provided by the renderer or the table itself, use default location:
		if ( super.getToolTipText( event ) != null )
			return super.getToolTipLocation( event );
		
		// If no tool tip, return null to prevent displaying an empty tool tip
		if ( getToolTipText( event ) == null )
			return null;
		
		// Else align to the cell:
		final Point point = event.getPoint();
		
		final int column = columnAtPoint( point );
		final int row = rowAtPoint( point );
		
		if ( column < 0 || row < 0 ) // event is not on a cell
			return null;
		
		final Point location = getCellRect( row, column, false ).getLocation();
		// Adjust due to the tool tip border and padding:
		location.x += 1;
		location.y -= 4;
		
		// If the renderer has an icon, take icon size and gap into consideration:
		final Component renderer = prepareRenderer( getCellRenderer( row, column ), row, column );
		if ( renderer instanceof JLabel ) {
			final JLabel l = (JLabel) renderer;
			if ( l.getIcon() != null )
				location.x += l.getIcon().getIconWidth() + l.getIconTextGap();
		}
		
		return location;
	}
	
	@Override
	public void setOpenAction( final IAction openAction ) {
		this.openAction = openAction;
	}
	
	@Override
	public void setCtrlOpenAction( final IAction ctrlOpenAction ) {
		this.ctrlOpenAction = ctrlOpenAction;
	}
	
	@Override
	public void setShiftOpenAction( final IAction shiftOpenAction ) {
		this.shiftOpenAction = shiftOpenAction;
	}
	
	@Override
	public void setDeleteAction( final IAction deleteAction ) {
		this.deleteAction = deleteAction;
	}
	
	
	/**
	 * Set of the saved selection objects.
	 * 
	 * @see #saveSelection(int)
	 * @see #restoreSelection(int)
	 */
	private Set< Object > savedSelectionSet;
	
	@Override
	public void saveSelection( final int idColIdx ) {
		final int[] rows = getSelectedModelRows();
		savedSelectionSet = LUtils.newHashSet( rows.length );
		if ( dataModel.getColumnCount() <= idColIdx )
			return;
		
		for ( final int row : rows )
			savedSelectionSet.add( dataModel.getValueAt( row, idColIdx ) );
	}
	
	@Override
	public void restoreSelection( final int idColIdx ) {
		clearSelection();
		if ( savedSelectionSet == null || dataModel.getColumnCount() <= idColIdx )
			return;
		
		for ( int row = dataModel.getRowCount() - 1; row >= 0; row-- )
			if ( savedSelectionSet.contains( dataModel.getValueAt( row, idColIdx ) ) ) {
				final int rowViewIdx = convertRowIndexToView( row );
				getSelectionModel().addSelectionInterval( rowViewIdx, rowViewIdx );
			}
		
		savedSelectionSet = null;
	}
	
	/**
	 * Sets a bigger minimum row height which is suitable to display progress bar cells.
	 */
	@Override
	public void setRowHeightForProgressBar() {
		setRowHeight( 19 );
	}
	
}
