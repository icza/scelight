/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight.gui.page.repanalyzer.inspector;

import hu.scelight.gui.comp.ToolBarForList;
import hu.scelight.gui.comp.XList;
import hu.scelight.gui.icon.Icons;
import hu.scelight.gui.page.repanalyzer.BaseRepAnalTabComp;
import hu.scelight.gui.tip.Tips;
import hu.scelight.sc2.rep.repproc.RepProcessor;
import hu.scelight.service.env.Env;
import hu.scelight.util.Utils;
import hu.scelight.util.gui.GuiUtils;
import hu.sllauncher.action.XAction;
import hu.sllauncher.gui.comp.ModestLabel;
import hu.sllauncher.gui.comp.TextSearchComp;
import hu.sllauncher.gui.comp.TipIcon;
import hu.sllauncher.gui.comp.XScrollPane;
import hu.sllauncher.service.job.Job;
import hu.sllauncher.util.gui.adapter.ActionAdapter;
import hu.sllauncher.util.gui.searcher.IntPosBaseSearcher;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

/**
 * Base component which has a string list (lines) visualized in a {@link XList} component.
 * 
 * @author Andras Belicza
 */
@SuppressWarnings( "serial" )
public abstract class BaseTextListComp extends BaseRepAnalTabComp {
	
	/** Copies the selected lines to the clipboard. */
	private final XAction           copySelectedAction = new XAction( Icons.F_DOCUMENT_SUB, "Copy Selected Lines to Clipboard (Ctrl+C)", this ) {
		                                                   @Override
		                                                   public void actionPerformed( final ActionEvent event ) {
			                                                   final List< String > selValList = textList.getSelectedValuesList();
			                                                   final StringBuilder sb = new StringBuilder( selValList.size() * 40 );
			                                                   for ( final String line : textList.getSelectedValuesList() )
				                                                   sb.append( line ).append( '\n' );
			                                                   Utils.copyToClipboard( sb.toString() );
		                                                   }
	                                                   };
	
	/** Copies all lines to the clipboard. */
	private final XAction           copyAllAction      = new XAction( Icons.F_DOCUMENT_COPY, "Copy All Lines to Clipboard", this ) {
		                                                   @Override
		                                                   public void actionPerformed( final ActionEvent event ) {
			                                                   final ListModel< String > model = textList.getModel();
			                                                   final int length = model.getSize();
			                                                   final StringBuilder sb = new StringBuilder( length * 40 );
			                                                   for ( int i = 0; i < length; i++ )
				                                                   sb.append( model.getElementAt( i ) ).append( '\n' );
			                                                   Utils.copyToClipboard( sb.toString() );
		                                                   }
	                                                   };
	
	
	/** Action listener which rebuilds the text (by calling {@link #rebuildText()}). */
	protected final ActionListener  rebuilderListener  = new ActionAdapter() {
		                                                   @Override
		                                                   public void actionPerformed( final ActionEvent event ) {
			                                                   rebuildText();
		                                                   }
	                                                   };
	
	/** Box holding multiple tool bars. */
	protected final Box             toolBarsBox        = Box.createVerticalBox();
	
	/**
	 * List to display text line by line (text area is way too slow for this big amount of text).<br>
	 * TextArea's setText() is slow and eats up all memory, because it parses the text to create paragraphs, elements etc.
	 */
	protected final XList< String > textList           = new XList<>();
	
	/** Tool bar. */
	protected final ToolBarForList  toolBar            = new ToolBarForList( textList );
	
	/** Text search component. */
	private final TextSearchComp    searchComp         = new TextSearchComp( true );
	
	/** Searcher logic. */
	protected IntPosBaseSearcher    searcher;
	
	/** Label to display text size. */
	private final ModestLabel       textSizeLabel      = new ModestLabel();
	
	/**
	 * Creates a new {@link BaseTextListComp}.
	 * 
	 * <p>
	 * Does not call {@link #buildGui()}!
	 * </p>
	 * 
	 * @param repProc replay processor
	 */
	public BaseTextListComp( final RepProcessor repProc ) {
		super( repProc );
	}
	
	/**
	 * Builds the GUI of the tab.
	 * 
	 * <p>
	 * Does not finalizes the {@link #toolBar}
	 * </p>
	 */
	protected void buildGui() {
		addNorth( toolBarsBox );
		
		toolBarsBox.add( toolBar );
		
		toolBar.addSelectInfoLabel( "Select text." );
		toolBar.addSelEnabledButton( copySelectedAction );
		toolBar.add( copyAllAction );
		toolBar.add( new TipIcon( Tips.TEXT_SELECTION ) );
		
		toolBar.addSeparator();
		toolBar.add( searchComp );
		searchComp.registerFocusHotkey( this );
		searchComp.setSearcher( searcher );
		
		toolBar.addSeparator();
		toolBar.add( textSizeLabel );
		
		textList.setCellRenderer( new DefaultListCellRenderer() {
			private final Border focusedBorder = BorderFactory.createMatteBorder( 1, 0, 1, 0, Color.BLACK );
			
			@Override
			public Component getListCellRendererComponent( JList< ? > list, Object value, int index, boolean isSelected, boolean cellHasFocus ) {
				super.getListCellRendererComponent( list, value, index, isSelected, cellHasFocus );
				
				// Default border results in big line height, set a thicker border
				setBorder( cellHasFocus ? focusedBorder : null );
				
				return this;
			}
		} );
		textList.setSelectionMode( ListSelectionModel.MULTIPLE_INTERVAL_SELECTION );
		addCenter( new XScrollPane( textList, true, false ) );
		
		rebuildText();
	}
	
	
	/**
	 * Rebuilds the raw text.
	 * 
	 * <p>
	 * First indicates that building is in progress, then acquires a {@link LinesBuilder} (by {@link #getLinesBuilder()}), builds the lines
	 * {@link LinesBuilder#build()} then makes the results visible.
	 * </p>
	 */
	protected void rebuildText() {
		// Building the content might require a relatively big time.
		// So first set a text telling we're working on it, and do the job "later".
		
		textList.setListData( new String[] { "Building content..." } );
		textList.setFont( null ); // Restore default font
		GuiUtils.italicFont( textList );
		textSizeLabel.setText( "Text size: ..." );
		
		SwingUtilities.invokeLater( getLinesBuilder() );
	}
	
	/**
	 * Builder to build the content lines.
	 * 
	 * <p>
	 * Implementation choice note: Not a {@link Job} because we don't want to allow rebuilding while not finished.
	 * </p>
	 * 
	 * @author Andras Belicza
	 */
	public abstract class LinesBuilder implements Runnable {
		
		/** Content lines must be built in this vector. */
		protected final Vector< String > lines       = new Vector<>( 1024 );
		
		/** (Reusable) string builder to build a line. */
		protected final StringBuilder    sb          = new StringBuilder( 128 );
		
		/** Stores the longest line to be used as the prototype cell. */
		protected String                 longestLine = " ";
		
		/** Text size must be stored here. */
		protected int                    textSize    = 0;
		
		@Override
		public void run() {
			build();
			publishResults();
		}
		
		/**
		 * Builds the lines (populates {@link #lines}).
		 */
		public abstract void build();
		
		/**
		 * Finishes the current line.
		 */
		protected void finishLine() {
			final String line = sb.toString();
			if ( line.length() > longestLine.length() )
				longestLine = line;
			
			textSize += line.length();
			
			lines.add( line );
			sb.setLength( 0 );
		}
		
		/**
		 * Publishes the results (the new content).
		 */
		protected void publishResults() {
			textList.setFont( new Font( Font.MONOSPACED, Font.PLAIN, 12 ) );
			// Add a little "extra" to the longest line because in hex viewer weird characters are displayed
			// that might have different width even using MONOSPACED font...
			textList.setPrototypeCellValue( longestLine + "WW" );
			textList.setListData( lines );
			searcher.clearLastSearchPos();
			
			textSizeLabel.setText( "Text size: " + Env.LANG.formatNumber( textSize ) + " chars" );
		}
		
	}
	
	/**
	 * Returns a {@link LinesBuilder}.
	 * 
	 * @return a {@link LinesBuilder}
	 */
	protected abstract LinesBuilder getLinesBuilder();
	
}
