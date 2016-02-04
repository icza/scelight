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

import hu.scelight.gui.comp.TemplateEditorComp;
import hu.scelight.gui.icon.Icons;
import hu.scelight.gui.page.replist.RepListComp;
import hu.scelight.service.env.Env;
import hu.scelight.service.settings.Settings;
import hu.scelight.template.InvalidTemplateException;
import hu.scelight.template.SymbolScope;
import hu.scelight.template.TemplateEngine;
import hu.scelight.util.Utils;
import hu.scelight.util.gui.GuiUtils;
import hu.sllauncher.gui.comp.BorderPanel;
import hu.sllauncher.gui.comp.XButton;
import hu.sllauncher.gui.comp.XDialog;
import hu.sllauncher.gui.comp.XLabel;
import hu.sllauncher.gui.comp.XSplitPane;
import hu.sllauncher.gui.comp.table.XTable;
import hu.sllauncher.util.gui.RenderablePair;
import hu.sllauncher.util.gui.adapter.ActionAdapter;
import hu.sllauncher.util.gui.adapter.DocumentAdapter;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Vector;

import javax.swing.JSplitPane;
import javax.swing.event.DocumentEvent;
import javax.swing.table.TableModel;

/**
 * A dialog to rename replays.
 * 
 * @author Andras Belicza
 */
@SuppressWarnings( "serial" )
public class RenameRepsDialog extends XDialog {
	
	/** Template editor component. */
	private final TemplateEditorComp templateEditor = new TemplateEditorComp( SymbolScope.AUTO_RENAME );
	
	/** Table previewing new names. */
	private final XTable             table          = new XTable();
	
	/** Source table (table of the rep list page component). */
	private final XTable             srcTable;
	
	/** File column model index of the source table. */
	public final int                 fileColIdx;
	
	
	
	/**
	 * Creates a new {@link RenameRepsDialog}.
	 * 
	 * @param repListPageComp rep list page comp as the replay source
	 */
	public RenameRepsDialog( final RepListComp repListPageComp ) {
		super( Env.MAIN_FRAME );
		
		this.srcTable = repListPageComp.table;
		fileColIdx = repListPageComp.fileColIdx;
		
		setTitle( "Rename Replays (" + Env.LANG.formatNumber( srcTable.getSelectedRowCount() ) + ")" );
		setIconImage( Icons.F_BLUE_DOCUMENT_RENAME.get().getImage() );
		
		templateEditor.templateField.setTemplate( Env.APP_SETTINGS.get( Settings.RENAME_REPS_TEMPLATE ) );
		
		// Set first selected replay as test replay
		templateEditor.testRepPathField.setPath( (Path) srcTable.getModel().getValueAt( srcTable.getSelectedModelRow(), fileColIdx ) );
		
		buildGui();
		
		restoreDefaultSize();
		
		setVisible( true );
	}
	
	/**
	 * Builds the GUI of the dialog.
	 */
	private void buildGui() {
		final BorderPanel top = new BorderPanel();
		top.addCenter( templateEditor );
		top.addSouth( getButtonsPanel() );
		
		final BorderPanel bottom = new BorderPanel();
		final XLabel prevTitleLabel = new XLabel( "Preview New Names:" ).boldFont();
		prevTitleLabel.setFont( prevTitleLabel.getFont().deriveFont( prevTitleLabel.getFont().getSize() + 2f ) );
		bottom.addNorth( prevTitleLabel );
		final Vector< Object > columns = Utils.vector( "Orignal replay file", "New replay file" );
		table.getXTableModel().setColumnIdentifiers( columns );
		bottom.addCenter( table.createWrapperBox( true, table.createToolBarParams( cp ) ) );
		
		final XSplitPane p = new XSplitPane( JSplitPane.VERTICAL_SPLIT );
		p.setResizeWeight( 0.65 );
		p.setTopComponent( top );
		p.setBottomComponent( bottom );
		cp.addCenter( p );
		
		// BUTTONS
		
		final XButton previewButton = new XButton( "_Preview New Names" );
		previewButton.addActionListener( new ActionAdapter() {
			@Override
			public void actionPerformed( final ActionEvent event ) {
				final TemplateEngine engine;
				try {
					engine = new TemplateEngine( templateEditor.templateField.getTemplate() );
				} catch ( final InvalidTemplateException ite ) {
					// Never to happen
					table.getXTableModel().setDataVector( new Vector<>( 0 ), columns );
					return;
				}
				
				final int[] selectedRows = srcTable.getSelectedModelRows();
				final Vector< Vector< Object > > data = new Vector<>( selectedRows.length );
				
				final TableModel srcModel = srcTable.getModel();
				int errors = 0;
				for ( final int row : selectedRows ) {
					final Path file = (Path) srcModel.getValueAt( row, fileColIdx );
					final Path targetFile = engine.apply( file );
					if ( targetFile == null )
						errors++;
					data.add( Utils.vector( file, targetFile == null ? new RenderablePair<>( Icons.F_CROSS.get(), "Failed to parse replay!" ) : targetFile ) );
				}
				
				table.getXTableModel().setDataVector( data, columns );
				
				if ( errors > 0 )
					GuiUtils.showErrorMsg( new XLabel( Utils.plural( "Failed to parse %s replay%s.", errors ), Icons.F_CROSS.get() ) );
			}
		} );
		getButtonsPanel().add( previewButton );
		getRootPane().setDefaultButton( previewButton );
		
		final XButton renameButton = new XButton( "Proceed to _Rename" );
		renameButton.addActionListener( new ActionAdapter() {
			@Override
			public void actionPerformed( final ActionEvent event ) {
				final TemplateEngine engine;
				try {
					engine = new TemplateEngine( templateEditor.templateField.getTemplate() );
				} catch ( final InvalidTemplateException ite ) {
					return; // Never to happen
				}
				
				Env.APP_SETTINGS.set( Settings.RENAME_REPS_TEMPLATE, templateEditor.templateField.getTemplate() );
				
				final int[] selectedRows = srcTable.getSelectedModelRows();
				@SuppressWarnings( "unchecked" )
				final Vector< Vector< Object > > data = srcTable.getXTableModel().getDataVector();
				
				int errors = 0;
				
				for ( final int row : selectedRows ) {
					final Path file = (Path) data.get( row ).get( fileColIdx );
					Path targetFile = engine.apply( file );
					if ( targetFile == null ) {
						Env.LOGGER.error( "Failed to rename replay: " + file );
						errors++;
						continue;
					}
					
					// Unify file name if already exists
					targetFile = Utils.uniqueFile( targetFile );
					// Create potential sub-folders introduced by the name template
					if ( !Files.exists( targetFile.getParent() ) )
						try {
							Files.createDirectories( targetFile.getParent() );
						} catch ( final IOException ie ) {
							Env.LOGGER.error( "Failed to rename replay, failed to create subfolders: " + targetFile.getParent(), ie );
							errors++;
							continue;
						}
					
					try {
						Files.move( file, targetFile );
					} catch ( final IOException ie ) {
						Env.LOGGER.error( "Failed to rename replay: \"" + file + "\" to \"" + targetFile + "\"", ie );
						errors++;
						continue;
					}
					
					data.get( row ).set( fileColIdx, targetFile );
				}
				srcTable.getXTableModel().fireTableDataChanged();
				srcTable.pack();
				
				if ( errors == 0 )
					GuiUtils.showInfoMsg( new XLabel( Utils.plural( "Successfully renamed %s replay%s.", selectedRows.length ), Icons.F_TICK.get() ) );
				else
					GuiUtils.showErrorMsg( new XLabel( Utils.plural( "Successfully renamed %s replay%s.", selectedRows.length - errors ), Icons.F_TICK.get() ),
					        new XLabel( Utils.plural( "Failed to renamed %s replay%s.", errors ), Icons.F_CROSS.get() ) );
				
				close();
			}
		} );
		getButtonsPanel().add( renameButton );
		
		templateEditor.templateField.textField.getDocument().addDocumentListener( new DocumentAdapter( true ) {
			@Override
			public void handleEvent( final DocumentEvent event ) {
				boolean valid = false;
				try {
					new TemplateEngine( templateEditor.templateField.textField.getText() );
					// If no exception is thrown: template is valid.
					valid = true;
				} catch ( final InvalidTemplateException ite ) {
					// OK, we know template is invalid.
				}
				previewButton.setEnabled( valid );
				renameButton.setEnabled( valid );
			}
		} );
		
		addCloseButton( "_Cancel" );
	}
}
