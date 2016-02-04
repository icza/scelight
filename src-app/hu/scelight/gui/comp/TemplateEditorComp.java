/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight.gui.comp;

import hu.scelight.gui.help.Helps;
import hu.scelight.gui.icon.Icons;
import hu.scelight.service.env.Env;
import hu.scelight.template.InvalidTemplateException;
import hu.scelight.template.Symbol;
import hu.scelight.template.SymbolScope;
import hu.scelight.template.TemplateEngine;
import hu.scelight.util.Utils;
import hu.scelight.util.gui.GuiUtils;
import hu.scelight.util.sc2rep.RepUtils;
import hu.scelightapibase.util.iface.Consumer;
import hu.sllauncher.action.XAction;
import hu.sllauncher.gui.comp.BorderPanel;
import hu.sllauncher.gui.comp.GridBagPanel;
import hu.sllauncher.gui.comp.HelpIcon;
import hu.sllauncher.gui.comp.Link;
import hu.sllauncher.gui.comp.PathField;
import hu.sllauncher.gui.comp.StatusLabel;
import hu.sllauncher.gui.comp.ToolBarForTable;
import hu.sllauncher.gui.comp.XCheckBox;
import hu.sllauncher.gui.comp.XLabel;
import hu.sllauncher.gui.comp.XSpinner;
import hu.sllauncher.gui.comp.table.XTable;
import hu.sllauncher.util.SkillLevel;
import hu.sllauncher.util.gui.adapter.ActionAdapter;
import hu.sllauncher.util.gui.adapter.DocumentAdapter;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.text.BadLocationException;

/**
 * A name template editor component with a {@link TemplateField} and symbol table.
 * 
 * @author Andras Belicza
 */
@SuppressWarnings( "serial" )
public class TemplateEditorComp extends BorderPanel {
	
	/** Inserts the selected symbols. */
	private final XAction      insertAction       = new XAction( Icons.F_NODE_INSERT, "Insert Selected Symbols (Enter)", this ) {
		                                              @Override
		                                              public void actionPerformed( final ActionEvent event ) {
			                                              insertSymbols();
		                                              }
	                                              };
	
	
	/** Template field to view/edit the template. */
	public final TemplateField templateField      = new TemplateField( false );
	
	/** Path field to choose a test replay. */
	public final PathField     testRepPathField   = new PathField( true );
	
	/** Label to display test result. */
	private final StatusLabel  testResultLabel    = new StatusLabel();
	
	/** Spinner to specify the symbol param. */
	private final XSpinner     paramSpinner       = new XSpinner( new SpinnerNumberModel( TemplateEngine.MIN_SYMBOL_PARAM, TemplateEngine.MIN_SYMBOL_PARAM,
	                                                      TemplateEngine.MAX_SYMBOL_PARAM, 1 ) );
	
	/** Check box to tell if symbol value range is specified and to be inserted (along with the symbol). */
	private final XCheckBox    valueRangeCheckBox = new XCheckBox( "With value range:" );
	
	/** Spinner to specify the symbol value range first character. */
	private final XSpinner     rangeFirstSpinner  = new XSpinner( new SpinnerNumberModel( 1, 1, 999, 1 ) );
	
	/** Spinner to specify the symbol value range last character. */
	private final XSpinner     rangeLastSpinner   = new XSpinner( new SpinnerNumberModel( 1, 1, 999, 1 ) );
	
	/** Table displaying the template elements. */
	private final XTable       table              = new XTable();
	
	/** {@link Symbol} column model index. */
	private int                symbolColIdx;
	
	
	/** Template engine created from the currently edited template. */
	private TemplateEngine     engine;
	
	
	/**
	 * Creates a new {@link TemplateEditorComp}.
	 * 
	 * @param excludeScope symbol scope whose symbols to exclude / hide
	 */
	public TemplateEditorComp( final SymbolScope excludeScope ) {
		buildGui( excludeScope );
	}
	
	/**
	 * Builds the GUI of the rename replays dialog.
	 * 
	 * @param excludeScope symbol scope whose symbols to exclude / hide
	 */
	private void buildGui( final SymbolScope excludeScope ) {
		final GridBagPanel gp = new GridBagPanel();
		gp.setBorder( BorderFactory.createCompoundBorder( BorderFactory.createRaisedSoftBevelBorder(), BorderFactory.createEmptyBorder( 5, 3, 5, 3 ) ) );
		gp.addSingle( new XLabel( "Template:" ).boldFont() );
		gp.c.weightx = 1;
		gp.addRemainder( templateField );
		gp.c.weightx = 0;
		gp.addSingle( new XLabel( "Validation:" ).boldFont() );
		final Box validationResultBox = Box.createHorizontalBox();
		final StatusLabel validationMsgLabel = new StatusLabel();
		validationResultBox.add( validationMsgLabel.rightBorder( 8 ) );
		final Link errorPosLink = new Link();
		errorPosLink.setConsumer( new Consumer< MouseEvent >() {
			@Override
			public void consume( final MouseEvent value ) {
				templateField.textField.requestFocusInWindow();
				templateField.textField.setCaretPosition( (Integer) errorPosLink.getClientProperty( "errorPos" ) );
			}
		} );
		errorPosLink.setToolTipText( "Jump to error position." );
		validationResultBox.add( errorPosLink );
		gp.addRemainder( validationResultBox );
		templateField.textField.getDocument().addDocumentListener( new DocumentAdapter( true ) {
			@Override
			public void handleEvent( final DocumentEvent event ) {
				engine = null;
				final String template = templateField.textField.getText();
				if ( template.isEmpty() ) {
					validationMsgLabel.setNotAvailable( "No template entered." );
					errorPosLink.setVisible( false );
				} else {
					try {
						engine = new TemplateEngine( template );
						// If no exception is thrown: template is valid.
						validationMsgLabel.setSuccess( "Template is valid." );
						errorPosLink.setVisible( false );
					} catch ( final InvalidTemplateException ite ) {
						validationMsgLabel.setError( ite.getMessage() );
						errorPosLink.setVisible( ite.position != null );
						if ( ite.position != null ) {
							errorPosLink.setText( "Position: " + ( ite.position + 1 ) + "" );
							errorPosLink.putClientProperty( "errorPos", ite.position );
						}
					}
				}
				performTest();
			}
		} );
		gp.addSingle( new XLabel( "Test on replay:" ).boldFont() );
		RepUtils.configureReplayChooserDialog( testRepPathField.fileChooser, false );
		testRepPathField.fileChooser.setDialogTitle( "Choose a test replay" );
		testRepPathField.button.setIcon( Icons.F_BLUE_DOCUMENT.get() );
		testRepPathField.button.setToolTipText( "Choose test replay..." );
		testRepPathField.textField.getDocument().addDocumentListener( new DocumentAdapter( true ) {
			@Override
			public void handleEvent( final DocumentEvent event ) {
				performTest();
			}
		} );
		gp.addRemainder( testRepPathField );
		gp.addSingle( new XLabel( "Test result:" ).boldFont() );
		gp.addRemainder( testResultLabel );
		addNorth( gp );
		
		final BorderPanel p = new BorderPanel();
		
		final ToolBarForTable toolBar = new ToolBarForTable( table );
		toolBar.addSelectInfoLabel( "Select a Symbol." );
		toolBar.addSelEnabledButton( insertAction );
		
		toolBar.addSeparator();
		toolBar.add( new XLabel( "Param X=" ) );
		toolBar.add( paramSpinner );
		
		if ( SkillLevel.BASIC.isAbove() ) {
			toolBar.addSeparator();
			toolBar.add( valueRangeCheckBox );
			final Box rangeValuesBox = Box.createHorizontalBox();
			rangeFirstSpinner.setPreferredSize( new Dimension( 60, rangeFirstSpinner.getPreferredSize().height ) );
			rangeValuesBox.add( rangeFirstSpinner );
			rangeValuesBox.add( new XLabel( "-" ) );
			rangeLastSpinner.setPreferredSize( new Dimension( 60, rangeLastSpinner.getPreferredSize().height ) );
			rangeValuesBox.add( rangeLastSpinner );
			rangeFirstSpinner.addChangeListener( new ChangeListener() {
				@Override
				public void stateChanged( final ChangeEvent event ) {
					if ( ( (Integer) rangeLastSpinner.getValue() ).compareTo( (Integer) rangeFirstSpinner.getValue() ) < 0 )
						rangeLastSpinner.setValue( rangeFirstSpinner.getValue() );
				}
			} );
			rangeLastSpinner.addChangeListener( new ChangeListener() {
				@Override
				public void stateChanged( final ChangeEvent event ) {
					if ( ( (Integer) rangeFirstSpinner.getValue() ).compareTo( (Integer) rangeLastSpinner.getValue() ) > 0 )
						rangeFirstSpinner.setValue( rangeLastSpinner.getValue() );
				}
			} );
			toolBar.add( rangeValuesBox );
			valueRangeCheckBox.addActionListener( new ActionAdapter( true ) {
				@Override
				public void actionPerformed( final ActionEvent event ) {
					GuiUtils.setComponentTreeEnabled( rangeValuesBox, valueRangeCheckBox.isSelected() );
				}
			} );
			toolBar.add( new HelpIcon( Helps.NT_SYMBOL_VALUE_RANGE ) );
		}
		
		toolBar.addSeparator();
		toolBar.add( new XLabel( "General" ).leftBorder( 20 ) );
		toolBar.add( new HelpIcon( Helps.NAME_TEMPLATE ).leftBorder( 2 ) );
		toolBar.addSeparator();
		toolBar.add( new XLabel( Helps.NT_PLAYER_INFO_BLOCK.title ) );
		toolBar.add( new HelpIcon( Helps.NT_PLAYER_INFO_BLOCK ).leftBorder( 2 ) );
		toolBar.addSeparator();
		toolBar.add( new XLabel( Helps.NT_SUBFOLDERS.title ) );
		toolBar.add( new HelpIcon( Helps.NT_SUBFOLDERS ).leftBorder( 2 ) );
		toolBar.finalizeLayout();
		p.addNorth( toolBar );
		
		final Vector< Vector< Object > > data = new Vector<>();
		for ( final Symbol s : Symbol.VALUES )
			if ( s.scope != excludeScope )
				data.add( Utils.vector( s, s.ricon, s.text, s.example, s.description ) );
		symbolColIdx = 0;
		table.getXTableModel().setDataVector( data, Utils.vector( "SymbolEnum", "I", "Symbol", "Example", "Description" ) );
		table.getColumnModel().removeColumn( table.getColumnModel().getColumn( symbolColIdx ) );
		table.packColumnsExceptLast();
		
		table.setOpenAction( insertAction );
		p.addCenter( table.createWrapperBox( true, table.createToolBarParams( this ) ) );
		
		addCenter( p );
	}
	
	/**
	 * Inserts the selected symbols into the template field.
	 */
	private void insertSymbols() {
		for ( final int row : table.getSelectedModelRows() ) {
			final Symbol s = (Symbol) table.getModel().getValueAt( row, symbolColIdx );
			
			if ( engine != null || templateField.getTemplate().isEmpty() ) {
				// Only try smart insert if template is valid or is empty
				templateField.textField.setCaretPosition( nextValidSymbolInsertionPos( s ) );
			}
			
			try {
				String text;
				if ( valueRangeCheckBox.isSelected() && s.text.charAt( 0 ) == '[' )
					text = '[' + s.id + '{' + rangeFirstSpinner.getValue() + '-' + rangeLastSpinner.getValue() + "}]";
				else
					text = s.text;
				if ( s.hasParam )
					text = text.replace( 'X', (char) ( '0' + (Integer) paramSpinner.getValue() ) );
				
				templateField.textField.getDocument().insertString( templateField.textField.getCaretPosition(), text, null );
				
				// If PIB was inserted, set caret position inside of it
				if ( s == Symbol.PIB )
					templateField.textField.setCaretPosition( templateField.textField.getCaretPosition() - 1 );
			} catch ( final BadLocationException ble ) {
				// Never to happen
				Env.LOGGER.warning( "Could not insert symbol: " + s, ble );
			}
		}
		
		// Deselect value range, disable it by default for the next insert
		if ( valueRangeCheckBox.isSelected() )
			valueRangeCheckBox.doClick( 0 );
		
		templateField.textField.requestFocusInWindow();
	}
	
	/**
	 * Returns the next <i>valid position</i> for inserting the specified symbol.<br>
	 * <i>Valid position</i> means that after inserting the specified symbol to the position, the template will remain valid.
	 * <p>
	 * <i>The current template must be valid or be an empty template!</i>
	 * </p>
	 * 
	 * @param s symbol whose valid insertion position to find
	 * @return the next valid position for inserting the specified symbol; or -1 if there is no valid insertion point for the specified symbol - the only case
	 *         for this is if the symbol has a scope of {@link SymbolScope#PIB} and no {@link Symbol#PIB}s are contained in the template
	 */
	private int nextValidSymbolInsertionPos( final Symbol s ) {
		final String template = templateField.getTemplate();
		int pos = templateField.textField.getCaretPosition();
		
		if ( s.scope == SymbolScope.PIB ) {
			// PIB-scoped symbol, must be inserted into a PIB
			
			// Are we inside or outside of a PIB?
			// Find preferably the preceding PIB
			int pibStart = template.lastIndexOf( '<', pos );
			if ( pibStart < 0 )
				pibStart = template.indexOf( '<', pos ); // No preceding PIB, find next
			if ( pibStart < 0 ) {
				// No PIB in template.
				// Insert one, and return its position
				final int pibInsertPos = nextValidSymbolInsertionPos( Symbol.PIB );
				try {
					templateField.textField.getDocument().insertString( pibInsertPos, Symbol.PIB.text, null );
				} catch ( final BadLocationException ble ) {
					// Never to happen
					Env.LOGGER.warning( "Could not insert symbol: " + s, ble );
				}
				return templateField.textField.getCaretPosition() - 1; // Return a position inside of the PIB just inserted
			}
			
			final int pibEnd = template.indexOf( '>', pibStart ); // Must exists (template is valid!)
			if ( pos > pibStart && pos <= pibEnd )
				; // Inside of a PIB, so far so good
			else
				pos = pibEnd; // Outside of PIBs, position to the end of the located PIB
		}
		
		
		// Are we inside or outside of a symbol (of symbols not being the PIB symbol)?
		final int lastSymbolStart = template.lastIndexOf( '[', pos );
		if ( lastSymbolStart >= 0 ) {
			// There was symbol start => there has to be symbol end (template is valid!)
			final int lastSymbolEnd = template.indexOf( ']', lastSymbolStart );
			if ( pos > lastSymbolStart && pos <= lastSymbolEnd ) {
				// We're inside of a symbol, position after its end
				pos = lastSymbolEnd + 1;
			}
		}
		
		// Moreover if symbol is the PIB, it has to be positioned outside of other PIBs (cannot be embedded)
		if ( s == Symbol.PIB ) {
			final int lastPibSymbolStart = template.lastIndexOf( '<', pos );
			if ( lastPibSymbolStart >= 0 ) {
				// There was PIB symbol start => there has to be PIB symbol end (template is valid!)
				final int lastPibSymbolEnd = template.indexOf( '>', lastPibSymbolStart );
				if ( pos > lastPibSymbolStart && pos <= lastPibSymbolEnd ) {
					// We're inside of a PIB symbol, position after its end
					pos = lastPibSymbolEnd + 1;
				}
			}
		}
		
		return pos;
	}
	
	/**
	 * Performs a template apply test on the entered test replay file.
	 */
	private void performTest() {
		if ( engine == null ) {
			testResultLabel.clear();
			return;
		}
		
		final Path testRepPath = testRepPathField.getPath();
		if ( !Files.exists( testRepPath ) || Files.isDirectory( testRepPath ) ) {
			testResultLabel.setNotAvailable( "No test replay selected." );
			return;
		}
		
		final Path testResult = engine.apply( testRepPath );
		if ( testResult == null )
			testResultLabel.setError( "Failed to parse replay!" );
		else
			testResultLabel.setSuccess( testResult.toString() );
	}
	
}
