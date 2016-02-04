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

import hu.belicza.andras.util.StructView;
import hu.belicza.andras.util.type.XString;
import hu.scelight.gui.icon.Icons;
import hu.scelight.gui.setting.SettingsGui;
import hu.scelight.sc2.rep.model.Replay;
import hu.scelight.sc2.rep.repproc.RepProcessor;
import hu.scelight.service.env.Env;
import hu.scelight.service.settings.Settings;
import hu.scelight.util.Utils;
import hu.sllauncher.gui.comp.XButton;
import hu.sllauncher.gui.comp.XCheckBox;
import hu.sllauncher.gui.comp.XLabel;
import hu.sllauncher.gui.comp.XToolBar;
import hu.sllauncher.util.Pair;
import hu.sllauncher.util.SkillLevel;
import hu.sllauncher.util.gui.adapter.ActionAdapter;
import hu.sllauncher.util.gui.searcher.IntPosBaseSearcher;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.BorderFactory;
import javax.swing.Box;

/**
 * Raw text data component of the game info tab.
 * 
 * @author Andras Belicza
 */
@SuppressWarnings( "serial" )
public class RawTextDataComp extends BaseTextListComp {
	
	/** Check box to tell if line numbers are to be shown. */
	private final XCheckBox showLineNumbersCheckBox;
	
	/** Check box to control whether to include replay header data. */
	private final XCheckBox showHeaderCheckBox        = new XCheckBox( "Header", true );
	
	/** Check box to control whether to include replay details data. */
	private final XCheckBox showDetailsCheckBox       = new XCheckBox( "Details", true );
	
	/** Check box to control whether to include replay init data data. */
	private final XCheckBox showInitDataCheckBox      = new XCheckBox( "Init Data", true );
	
	/** Check box to control whether to include replay attributes events data. */
	private final XCheckBox showAttributesCheckBox    = new XCheckBox( "Attributes", true );
	
	/** Check box to control whether to include replay message events data. */
	private final XCheckBox showMessagesCheckBox      = new XCheckBox( "Messages", true );
	
	/** Check box to control whether to include replay game events data. */
	private final XCheckBox showGameEventsCheckBox    = new XCheckBox( "Game Events", false );
	
	/** Check box to control whether to include replay tracker events data. */
	private final XCheckBox showTrackerEventsCheckBox = new XCheckBox( "Tracker Events", false );
	
	/**
	 * Creates a new {@link RawTextDataComp}.
	 * 
	 * @param repProc replay processor
	 */
	public RawTextDataComp( final RepProcessor repProc ) {
		super( repProc );
		
		showLineNumbersCheckBox = SettingsGui.createSettingCheckBox( Settings.RAW_TEXT_SHOW_LINE_NUMBER, Env.APP_SETTINGS, rebuilderListener );
		
		searcher = new IntPosBaseSearcher() {
			@Override
			protected void prepareNew() {
				maxPos = textList.getModel().getSize() - 1;
				super.prepareNew();
			}
			
			@Override
			public Integer getStartPos() {
				return textList.getSelectedIndex() < 0 ? null : Integer.valueOf( textList.getSelectedIndex() );
			}
			
			@Override
			public boolean matches() {
				if ( Utils.containsIngoreCase( textList.getModel().getElementAt( searchPos ), searchText ) ) {
					textList.setSelectedIndex( searchPos );
					textList.scrollRectToVisible( textList.getCellBounds( searchPos, searchPos ) );
					return true;
				}
				return false;
			}
		};
		
		buildGui();
	}
	
	@Override
	protected void buildGui() {
		final List< XCheckBox > componentCheckBoxList = Utils.asNewList( showHeaderCheckBox, showDetailsCheckBox, showInitDataCheckBox, showAttributesCheckBox,
		        showMessagesCheckBox, showGameEventsCheckBox, showTrackerEventsCheckBox );
		
		XToolBar toolBar = new XToolBar();
		toolBarsBox.add( toolBar );
		
		toolBar.add( new XLabel( "Components to include:" ).verticalBorder( 7 ) );
		
		toolBar.addSeparator();
		final Box box = Box.createVerticalBox();
		box.setBorder( BorderFactory.createEmptyBorder( 0, 0, 0, 5 ) );
		SettingsGui.bindVisibilityToSkillLevel( box, SkillLevel.NORMAL );
		final XButton allButton = new XButton( "All", Icons.F_UI_CHECK_BOX.get() );
		allButton.setToolTipText( "Select All Components" );
		allButton.configureAsIconButton();
		box.add( allButton );
		final XButton noneButton = new XButton( "None", Icons.F_UI_CHECK_BOX_UNCHECK.get() );
		noneButton.setToolTipText( "Deselect All Components" );
		noneButton.configureAsIconButton();
		box.add( noneButton );
		toolBar.add( box );
		final ActionListener actionListener = new ActionAdapter() {
			@Override
			public void actionPerformed( final ActionEvent event ) {
				// Only rebuild if there is change
				int count = 0;
				final boolean select = event.getSource() == allButton;
				for ( final XCheckBox userCheckBox : componentCheckBoxList )
					if ( userCheckBox.isSelected() != select ) {
						userCheckBox.setSelected( select );
						count++;
					}
				if ( count > 0 )
					rebuilderListener.actionPerformed( null );
			}
		};
		allButton.addActionListener( actionListener );
		noneButton.addActionListener( actionListener );
		
		toolBar.addSeparator();
		if ( repProc.replay.trackerEvents == null )
			showTrackerEventsCheckBox.setVisible( false );
		for ( final XCheckBox cb : componentCheckBoxList ) {
			cb.addActionListener( rebuilderListener );
			toolBar.add( cb );
		}
		
		toolBar.finalizeLayout();
		
		super.buildGui();
		
		// Extend original tool bar
		toolBar = this.toolBar;
		// Move the info component (currently the last) to the end
		final Component infoComp = toolBar.getComponent( toolBar.getComponentCount() - 1 );
		toolBar.remove( toolBar.getComponentCount() - 1 );
		// Add show line numbers check box
		showLineNumbersCheckBox.setText( "Line numbers" );
		toolBar.add( showLineNumbersCheckBox );
		toolBar.addSeparator();
		toolBar.add( infoComp );
		toolBar.finalizeLayout();
	}
	
	@Override
	protected LinesBuilder getLinesBuilder() {
		return new LinesBuilder() {
			
			/** Pre-constructed tabs for aligning lines of structured text. */
			private final String[] TABS            = new String[ 15 ];
			{
				TABS[ 1 ] = TABS[ 0 ] = "";
				for ( int i = Env.APP_SETTINGS.get( Settings.RAW_TEXT_TAB_SIZE ); i > 0; i-- )
					TABS[ 1 ] += ' ';
				for ( int i = 2; i < TABS.length; i++ )
					TABS[ i ] = TABS[ i - 1 ] + TABS[ 1 ];
			}
			
			private final boolean  showLineNumbers = showLineNumbersCheckBox.isSelected();
			
			@Override
			public void build() {
				final Replay replay = repProc.replay;
				
				if ( showHeaderCheckBox.isSelected() ) {
					newSection( "HEADER" );
					appendValue( sb, replay.header.getStruct(), 0, true );
				}
				if ( showDetailsCheckBox.isSelected() ) {
					newSection( "DETAILS" );
					appendValue( sb, replay.details.getStruct(), 0, true );
				}
				if ( showInitDataCheckBox.isSelected() ) {
					newSection( "INIT DATA" );
					appendValue( sb, replay.initData.getStruct(), 0, true );
				}
				if ( showAttributesCheckBox.isSelected() ) {
					newSection( "ATTRIBUTES EVENTS" );
					// Note the use of AttributesEvents.getStringStruct() instead of AttributesEvents.getStruct()
					appendValue( sb, replay.attributesEvents.getStringStruct(), 0, true );
				}
				if ( showMessagesCheckBox.isSelected() ) {
					newSection( "MESSAGE EVENTS" );
					appendValue( sb, replay.messageEvents.events, 0, true );
				}
				if ( showGameEventsCheckBox.isSelected() ) {
					newSection( "GAME EVENTS" );
					appendValue( sb, replay.gameEvents.events, 0, true );
				}
				if ( showTrackerEventsCheckBox.isSelected() && replay.trackerEvents != null ) {
					newSection( "TRACKER EVENTS" );
					appendValue( sb, replay.trackerEvents.events, 0, true );
				}
			}
			
			/**
			 * Finishes the current line.
			 */
			@Override
			protected void finishLine() {
				super.finishLine();
				addLineNumber();
			}
			
			/**
			 * Starts a new section.
			 * 
			 * @param title title of the section
			 */
			private void newSection( final String title ) {
				if ( lines.isEmpty() )
					addLineNumber();
				else {
					finishLine();
					finishLine();
				}
				sb.append( "-----" ).append( title );
			}
			
			/**
			 * Adds line number (if enabled).
			 */
			private void addLineNumber() {
				if ( !showLineNumbers )
					return;
				final String lineNumString = Integer.toString( lines.size() + 1 );
				// Align numbers to at least 4 char:
				for ( int i = 4 - lineNumString.length(); i > 0; i-- )
					sb.append( ' ' );
				sb.append( lineNumString ).append( ' ' );
			}
			
			/**
			 * Appends the specified object to the specified string builder.
			 * 
			 * @param sb string builder to append to
			 * @param value object to be appended
			 * @param tabs tabs to align lines
			 * @param align tells if the value has to be aligned
			 */
			@SuppressWarnings( "unchecked" )
			private void appendValue( final StringBuilder sb, final Object value, int tabs, final boolean align ) {
				if ( align ) {
					finishLine();
					sb.append( TABS[ tabs ] );
				}
				
				if ( value instanceof Map ) {
					sb.append( '{' );
					tabs++;
					for ( final Entry< Object, Object > entry : ( (Map< Object, Object >) value ).entrySet() ) {
						finishLine();
						sb.append( TABS[ tabs ] ).append( entry.getKey() ).append( " = " );
						appendValue( sb, entry.getValue(), tabs, false );
					}
					tabs--;
					finishLine();
					sb.append( TABS[ tabs ] ).append( '}' );
				} else if ( value instanceof Object[] ) {
					sb.append( '[' );
					tabs++;
					for ( final Object e : (Object[]) value ) {
						appendValue( sb, e, tabs, true );
						sb.append( ',' );
					}
					tabs--;
					finishLine();
					sb.append( TABS[ tabs ] ).append( ']' );
				} else if ( value instanceof StructView ) {
					appendValue( sb, ( (StructView) value ).getStruct(), tabs, false );
				} else if ( value instanceof Pair ) {
					sb.append( ( (Pair< String, Object >) value ).value1 ).append( " = " );
					appendValue( sb, ( (Pair< String, Object >) value ).value2, tabs, false );
				} else if ( value instanceof XString ) {
					// Remove zero characters else the text is not copiable (copy terminates when encountering a zero char)
					final String s = value.toString();
					sb.append( s.indexOf( '\0' ) >= 0 ? s.replace( "\0", "" ) : s );
				} else
					sb.append( value );
				
				if ( tabs == 0 )
					finishLine();
			}
		};
	}
	
}
