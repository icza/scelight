/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight.gui.page.repanalyzer.inspector.binarydata;

import hu.belicza.andras.mpq.MpqContent;
import hu.scelight.gui.comp.XList;
import hu.scelight.gui.icon.Icons;
import hu.scelight.gui.page.repanalyzer.inspector.BaseTextListComp;
import hu.scelight.gui.setting.SettingsGui;
import hu.scelight.gui.tip.Tips;
import hu.scelight.sc2.map.MapContent;
import hu.scelight.sc2.map.MapParser;
import hu.scelight.sc2.rep.factory.RepContent;
import hu.scelight.sc2.rep.repproc.RepProcessor;
import hu.scelight.service.env.Env;
import hu.scelight.service.settings.Settings;
import hu.scelight.util.Utils;
import hu.scelight.util.gui.GuiUtils;
import hu.scelightapibase.gui.comp.ITextField.IValidator;
import hu.sllauncher.action.XAction;
import hu.sllauncher.gui.comp.ModestLabel;
import hu.sllauncher.gui.comp.TextSearchComp;
import hu.sllauncher.gui.comp.TipIcon;
import hu.sllauncher.gui.comp.XFileChooser;
import hu.sllauncher.gui.comp.XLabel;
import hu.sllauncher.gui.comp.XTextField;
import hu.sllauncher.gui.comp.XToolBar;
import hu.sllauncher.gui.comp.combobox.XComboBox;
import hu.sllauncher.service.sound.Sound;
import hu.sllauncher.util.LUtils;
import hu.sllauncher.util.SizeFormat;
import hu.sllauncher.util.gui.LGuiUtils;
import hu.sllauncher.util.gui.adapter.ActionAdapter;
import hu.sllauncher.util.gui.searcher.IntPosBaseSearcher;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.KeyStroke;

/**
 * Binary (hex) data component of the game info tab.
 * 
 * @author Andras Belicza
 */
@SuppressWarnings( "serial" )
public class BinaryDataComp extends BaseTextListComp {
	
	/** Saves the currently displayed binary data to a file. */
	private final XAction                  saveAction    = new XAction( Icons.F_DISK, "Save Binary Data (Dump to File)...", this ) {
		                                                     @Override
		                                                     public void actionPerformed( final ActionEvent event ) {
			                                                     final XFileChooser fc = new XFileChooser();
			                                                     fc.setDialogTitle( "Choose a file to save binary data" );
			                                                     
			                                                     // Suggested file name
			                                                     final String fileName = dataSourceComboBox.getSelectedItem().getFileName();
			                                                     if ( fileName != null )
				                                                     fc.setSelectedFile( Paths.get( fileName ).toFile() );
			                                                     
			                                                     if ( XFileChooser.APPROVE_OPTION != fc.showSaveDialog( Env.CURRENT_GUI_FRAME.get() ) )
				                                                     return;
			                                                     try {
				                                                     Files.write( fc.getSelectedPath(), data );
			                                                     } catch ( final IOException ie ) {
				                                                     Env.LOGGER.error( "Failed to write to file: " + fc.getSelectedPath(), ie );
				                                                     GuiUtils.showErrorMsg( "Failed to write to file:", fc.getSelectedPath() );
			                                                     }
		                                                     }
	                                                     };
	
	
	/** Combo box to display the possible data sources to take the displayable binary data from. */
	private final XComboBox< DataSource >  dataSourceComboBox;
	
	/** Combo box to display the hex line sizes. */
	private final XComboBox< HexLineSize > hexLineSizeComboBox;
	
	/** Label to display data size. */
	private final ModestLabel              dataSizeLabel = new ModestLabel();
	
	
	/** Hex text search component. */
	private final TextSearchComp           hexSearchComp = new TextSearchComp( true, "Hex search" );
	
	/** Hex searcher logic. */
	protected IntPosBaseSearcher           hexSearcher;
	
	
	/** Binary data displayed. */
	private byte[]                         data;
	
	
	/**
	 * A {@link IntPosBaseSearcher} implementation to search the content displayed in a {@link XList} (lines) but whose position is the byte index of the binary
	 * data (not the line number).
	 * 
	 * @author Andras Belicza
	 */
	private abstract class ByteIntPosBaseSearcher extends IntPosBaseSearcher {
		
		/** Max offset to check when matching. */
		protected int maxOffset;
		
		@Override
		protected void prepareNew() {
			maxPos = data.length - 1;
			super.prepareNew();
		}
		
		@Override
		public Integer getStartPos() {
			// Search is byte based, not line. (The searched text might even occur multiple times in a line.)
			// Having said that, if the last searched pos is inside the selected line,
			// return a position that will denote the next line (in the proper direction);
			// else the same occurrence would be found again.
			// Also / moreover consider searchLastMatch property.
			if ( textList.getSelectedIndex() < 0 )
				return null;
			
			final HexLineSize hls = hexLineSizeComboBox.getSelectedItem();
			
			if ( lastSearchedPos != null && lastSearchedPos >> hls.shiftCount == textList.getSelectedIndex() ) {
				Integer startPos;
				if ( searchLastMatch )
					startPos = ( textList.getSelectedIndex() << hls.shiftCount ) + ( forward ? -1 : hls.bytesCount );
				else
					startPos = ( textList.getSelectedIndex() << hls.shiftCount ) + ( forward ? hls.bytesCount - 1 : 0 );
				// Normalize:
				if ( startPos < 0 || startPos > maxPos )
					startPos = null;
				return startPos;
			}
			
			return textList.getSelectedIndex() << hls.shiftCount;
		}
		
		/**
		 * Handles a match: selects the line belonging to the byte index specified by the {@link #searchPos}.
		 */
		protected void handleMatch() {
			final int line = searchPos >> hexLineSizeComboBox.getSelectedItem().shiftCount;
			textList.setSelectedIndex( line );
			textList.scrollRectToVisible( textList.getCellBounds( line, line ) );
		}
		
	}
	
	
	/**
	 * Creates a new {@link BinaryDataComp}.
	 * 
	 * @param repProc replay processor
	 */
	public BinaryDataComp( final RepProcessor repProc ) {
		super( repProc );
		
		hexLineSizeComboBox = SettingsGui.createSettingComboBox( Settings.HEX_LINE_SIZE, Env.APP_SETTINGS, rebuilderListener );
		
		final Vector< DataSource > sourceVector = new Vector<>();
		final List< DataSource > separatedDsList = new ArrayList<>();
		
		// The replay file
		sourceVector.add( new FullFileDataSource( "Replay File", repProc.file ) );
		separatedDsList.add( sourceVector.get( sourceVector.size() - 1 ) );
		// General Replay MPQ content
		sourceVector.add( new MpqUserDataDataSource( repProc.file ) );
		for ( final MpqContent mpqContent : MpqContent.VALUES )
			sourceVector.add( new MpqContentDataSource( repProc.file, mpqContent ) );
		separatedDsList.add( sourceVector.get( sourceVector.size() - 1 ) );
		// Replay content
		for ( final RepContent mpqContent : RepContent.VALUES ) {
			sourceVector.add( new MpqContentDataSource( repProc.file, mpqContent ) );
			if ( mpqContent == RepContent.TRACKER_EVENTS )
				separatedDsList.add( sourceVector.get( sourceVector.size() - 1 ) );
		}
		separatedDsList.add( sourceVector.get( sourceVector.size() - 1 ) );
		// General Map MPQ content
		final Path mapFile = MapParser.getMapFile( repProc );
		sourceVector.add( new MpqUserDataDataSource( mapFile ) );
		for ( final MpqContent mpqContent : MpqContent.VALUES )
			sourceVector.add( new MpqContentDataSource( mapFile, mpqContent ) );
		separatedDsList.add( sourceVector.get( sourceVector.size() - 1 ) );
		// Map content
		for ( final MapContent mpqContent : MapContent.VALUES )
			sourceVector.add( new MpqContentDataSource( mapFile, mpqContent ) );
		
		dataSourceComboBox = new XComboBox<>( sourceVector );
		dataSourceComboBox.markSeparatedItems( separatedDsList );
		
		searcher = new ByteIntPosBaseSearcher() {
			/** Bytes of the search text, as <code>int</code>s. */
			private int[] searchTextBytes;
			
			@Override
			protected void prepareNew() {
				// Note: Texts found in binary data are usually UTF-8 encoded.
				// This text search converts the searched text to bytes using the same
				// encoding (UTF-8), and this byte sequence will be searched.
				// For more details see the Tips.BINARY_DATA_TEXT_SEARCH tip.
				final byte[] bytes = searchText.getBytes( Env.UTF8 );
				searchTextBytes = new int[ bytes.length ];
				maxOffset = searchTextBytes.length - 1;
				for ( int i = maxOffset; i >= 0; i-- )
					searchTextBytes[ i ] = bytes[ i ] & 0xff;
				super.prepareNew();
			}
			
			@Override
			public boolean matches() {
				// Local vars for fast access
				final byte[] data = BinaryDataComp.this.data;
				final int[] searchTextBytes = this.searchTextBytes;
				final int searchPos = this.searchPos;
				if ( searchPos + maxOffset > maxPos )
					return false; // Would overflow in data, surely cannot match
					
				for ( int offset = maxOffset; offset >= 0; offset-- ) {
					final int searchChar = searchTextBytes[ offset ];
					final int ch = data[ searchPos + offset ] & 0xff;
					if ( ch != searchChar ) {
						// Also check in a case insensitive manner (lower-cased version of ch):
						if ( ch >= 'A' && ch <= 'Z' ) {
							if ( ch - ( 'A' - 'a' ) != searchChar )
								return false;
						} else
							return false;
					}
				}
				
				// Match!
				handleMatch();
				// Clear the other searcher (so it will start from this line)
				hexSearcher.clearLastSearchPos();
				
				return true;
			}
			
		};
		
		hexSearcher = new ByteIntPosBaseSearcher() {
			/** Bytes specified by the hex search text. */
			private byte[] searchTextBytes;
			
			@Override
			protected void prepareNew() {
				searchTextBytes = Utils.hexToBytes( searchText );
				if ( searchTextBytes == null )
					searchTextBytes = new byte[ 0 ];
				maxOffset = searchTextBytes.length - 1;
				super.prepareNew();
			}
			
			@Override
			public boolean matches() {
				if ( searchTextBytes.length == 0 )
					return true; // To end the search
				// Local vars for fast access
				final byte[] data = BinaryDataComp.this.data;
				final byte[] searchTextBytes = this.searchTextBytes;
				final int searchPos = this.searchPos;
				if ( searchPos + maxOffset > maxPos )
					return false; // Would overflow in data, surely cannot match
					
				for ( int offset = maxOffset; offset >= 0; offset-- )
					if ( searchTextBytes[ offset ] != data[ searchPos + offset ] )
						return false;
				
				// Match!
				handleMatch();
				// Clear the other searcher (so it will start from this line)
				searcher.clearLastSearchPos();
				
				return true;
			}
			
		};
		
		buildGui();
	}
	
	@Override
	protected void buildGui() {
		XToolBar toolBar = new XToolBar();
		toolBarsBox.add( toolBar );
		
		toolBar.add( new XLabel( "Data source:" ).verticalBorder( 7 ) );
		dataSourceComboBox.addActionListener( rebuilderListener );
		dataSourceComboBox.setMaximumRowCount( dataSourceComboBox.getItemCount() );
		toolBar.add( dataSourceComboBox );
		
		toolBar.addSeparator();
		LGuiUtils.autoCreateDisabledImage( toolBar.add( saveAction ) );
		
		toolBar.addSeparator();
		toolBar.add( new XLabel( "Line size:" ) );
		toolBar.add( hexLineSizeComboBox );
		toolBar.add( new XLabel( Settings.HEX_LINE_SIZE.viewHints.getSubsequentText() ) );
		
		toolBar.addSeparator();
		toolBar.add( new XLabel( "Jump to pos:" ) );
		final XTextField jumpTextField = new XTextField( null, 6, true );
		jumpTextField.setValidator( new IValidator() {
			@Override
			public boolean validate( final String text ) {
				final boolean result = validateLogic( text );
				if ( !result )
					Sound.beepOnEmptyTxtSearchRslt();
				return result;
			}
			
			private boolean validateLogic( final String text ) {
				if ( text.isEmpty() || "0x".equals( text ) )
					return true;
				try {
					final int pos = text.startsWith( "0x" ) ? Integer.parseInt( text.substring( 2 ), 16 ) : Integer.parseInt( text );
					if ( pos < 0 || pos >= data.length )
						return false;
					final int line = pos >> hexLineSizeComboBox.getSelectedItem().shiftCount;
					textList.setSelectedIndex( line );
					textList.scrollRectToVisible( textList.getCellBounds( line, line ) );
					return true;
				} catch ( final NumberFormatException nfe ) {
					return false;
				}
			}
		} );
		jumpTextField.addActionListener( new ActionAdapter() {
			@Override
			public void actionPerformed( final ActionEvent event ) {
				final String text = jumpTextField.getText();
				jumpTextField.setText( null );
				jumpTextField.setText( text );
			}
		} );
		jumpTextField.setToolTipText( "<html>Enter a byte position to jump to, either decimal or hexa starting with <code>\"0x\"</code>.</html>" );
		// Focus Jump field when pressing CTRL+J
		jumpTextField.registerFocusHotkey( this, KeyStroke.getKeyStroke( KeyEvent.VK_J, InputEvent.CTRL_MASK ) );
		toolBar.add( jumpTextField );
		
		toolBar.addSeparator();
		toolBar.add( dataSizeLabel );
		
		toolBar.finalizeLayout();
		
		super.buildGui();
		
		// Extend the original tool bar
		toolBar = this.toolBar;
		// Move the info component (currently the last) to the end
		final Component infoComp = toolBar.getComponent( toolBar.getComponentCount() - 1 );
		toolBar.remove( toolBar.getComponentCount() - 1 );
		// Insert tip to the text search, before the separator:
		toolBar.add( new TipIcon( Tips.BINARY_DATA_TEXT_SEARCH ), toolBar.getComponentCount() - 1 );
		// Add Hex searcher
		hexSearchComp.textField.setValidator( new IValidator() {
			@Override
			public boolean validate( String text ) {
				text = text.replace( " ", "" ); // Remove spaces
				// Check: length must be even, must contain only hex digits
				return ( text.length() & 0x01 ) == 0 && text.matches( "[\\da-f]*" );
			}
		} );
		hexSearchComp.textField.setToolTipText( "<html>Search hex data, for example <code>\"06 1f\"</html>" );
		// Register CTRL+F for hex search (CTRL+S is taken by the other search component)
		// (CTRL+H is already used by JTextField!)
		hexSearchComp.registerFocusHotkey( this, KeyStroke.getKeyStroke( KeyEvent.VK_F, InputEvent.CTRL_MASK ) );
		hexSearchComp.setSearcher( hexSearcher );
		toolBar.add( hexSearchComp );
		toolBar.addSeparator();
		toolBar.add( infoComp );
		toolBar.finalizeLayout();
	}
	
	@Override
	protected void rebuildText() {
		dataSizeLabel.setText( "Data size: ..." );
		
		super.rebuildText();
	}
	
	@Override
	protected LinesBuilder getLinesBuilder() {
		return new LinesBuilder() {
			private final HexLineSize hls  = hexLineSizeComboBox.getSelectedItem();
			
			private byte[]            data = dataSourceComboBox.getSelectedItem().getData();
			
			@Override
			public void build() {
				if ( data == null )
					data = new byte[ 0 ];
				final byte[] data = this.data;
				
				final int length = data.length;
				final char[] HEX_DIGITS = LUtils.HEX_DIGITS;
				
				int pos = 0;
				while ( pos < length ) {
					addLineNumber();
					
					final int start = pos;
					final int end = start + hls.bytesCount;
					// First round: hex values
					for ( ; pos < end; pos++ ) {
						if ( pos < length ) {
							final byte b = data[ pos ];
							sb.append( HEX_DIGITS[ ( b & 0xff ) >> 4 ] ).append( HEX_DIGITS[ b & 0x0f ] );
						} else
							sb.append( "  " ); // Last line: print spaces so characters will be put to good pos
						if ( ( pos & 0x07 ) == 0x07 ) // Pipe separator after every 8 bytes
							sb.append( " | " );
						else
							sb.append( ' ' );
					}
					// Second round: character values
					pos = start;
					for ( ; pos < end; pos++ ) {
						if ( pos < length ) {
							final int ch = data[ pos ] & 0xff;
							sb.append( ch < 32 ? '.' : (char) ch );
						} else
							sb.append( ' ' );
					}
					finishLine();
				}
			}
			
			/**
			 * Prepares the next line, adds line hex position.
			 */
			private void addLineNumber() {
				sb.append( String.format( "%06x", lines.size() << hls.shiftCount ) ).append( ":  " );
			}
			
			@Override
			protected void publishResults() {
				BinaryDataComp.this.data = data;
				saveAction.setEnabled( data.length > 0 );
				dataSizeLabel.setText( "Data size: " + SizeFormat.BYTES.formatSize( data.length, 0 ) );
				hexSearcher.clearLastSearchPos();
				
				super.publishResults();
			}
		};
	}
}
