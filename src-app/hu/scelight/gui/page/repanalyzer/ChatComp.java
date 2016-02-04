/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight.gui.page.repanalyzer;

import hu.scelight.gui.setting.SettingsGui;
import hu.scelight.sc2.rep.model.messageevents.ChatEvent;
import hu.scelight.sc2.rep.model.messageevents.PingEvent;
import hu.scelight.sc2.rep.repproc.RepProcessor;
import hu.scelight.sc2.rep.repproc.User;
import hu.scelight.sc2.rep.s2prot.Event;
import hu.scelight.service.env.Env;
import hu.scelight.service.settings.Settings;
import hu.scelight.util.Utils;
import hu.scelightapi.sc2.rep.model.messageevents.IMessageEvents;
import hu.scelightapibase.bean.settings.ISettingChangeEvent;
import hu.scelightapibase.bean.settings.ISettingChangeListener;
import hu.sllauncher.gui.comp.Browser;
import hu.sllauncher.gui.comp.XCheckBox;
import hu.sllauncher.gui.comp.XScrollPane;
import hu.sllauncher.gui.comp.XToolBar;
import hu.sllauncher.gui.comp.table.XTable;
import hu.sllauncher.util.DurationValue;
import hu.sllauncher.util.gui.adapter.ActionAdapter;

import java.awt.event.ActionEvent;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JComponent;

/**
 * Chat tab component.
 * 
 * @author Andras Belicza
 */
@SuppressWarnings( "serial" )
public class ChatComp extends BaseRepAnalTabComp {
	
	/** Check box to tell if show chat in a table. */
	private final XCheckBox   tableViewCheckBox;
	
	/** Check box to tell if message targets is to be displayed. */
	private final XCheckBox   showMessageRecipientCheckBox;
	
	/** Check box to tell if paragraph formatting is enabled. */
	private final XCheckBox   formatIntoPsCheckBox;
	
	/** Check box to tell if minimap pings are to be shown. */
	private final XCheckBox   showPingsCheckBox;
	
	
	/** Browser to display chat in non-table view. */
	private final Browser     chatBrowser           = new Browser();
	
	/** Scroll pane for the chat browser. */
	private final XScrollPane chatBrowserScrollPane = new XScrollPane( chatBrowser );
	
	/** Table to display chat in table view. */
	private final XTable      chatTable             = new XTable();
	
	/** Wrapper box for the chat table. */
	private final JComponent  chatTableWrapperBox   = chatTable.createWrapperBox( true, chatTable.createToolBarParams( this ) );
	
	
	/**
	 * Creates a new {@link ChatComp}.
	 * 
	 * @param repProc replay processor
	 */
	public ChatComp( final RepProcessor repProc ) {
		super( repProc );
		
		final ActionAdapter rebuilder = new ActionAdapter() {
			@Override
			public void actionPerformed( final ActionEvent event ) {
				rebuildChat();
			}
		};
		tableViewCheckBox = SettingsGui.createSettingCheckBox( Settings.CHAT_TABLE_VIEW, Env.APP_SETTINGS, rebuilder );
		showMessageRecipientCheckBox = SettingsGui.createSettingCheckBox( Settings.SHOW_MESSAGE_RECIPIENT, Env.APP_SETTINGS, rebuilder );
		formatIntoPsCheckBox = SettingsGui.createSettingCheckBox( Settings.FORMAT_INTO_PARAGRAPHS, Env.APP_SETTINGS, rebuilder );
		showPingsCheckBox = SettingsGui.createSettingCheckBox( Settings.SHOW_MINIMAP_PINGS, Env.APP_SETTINGS, rebuilder );
		
		buildGui();
	}
	
	/**
	 * Builds the GUI of the tab.
	 */
	private void buildGui() {
		final XToolBar toolBar = new XToolBar();
		addNorth( toolBar );
		
		tableViewCheckBox.setBorder( BorderFactory.createEmptyBorder( 7, 0, 7, 0 ) );
		toolBar.add( tableViewCheckBox );
		toolBar.addSeparator();
		toolBar.add( showMessageRecipientCheckBox );
		SettingsGui.bindVisibilityToSkillLevel( formatIntoPsCheckBox, Settings.FORMAT_INTO_PARAGRAPHS.skillLevel, Boolean.TRUE );
		toolBar.add( formatIntoPsCheckBox );
		toolBar.addSeparator();
		toolBar.add( showPingsCheckBox );
		
		toolBar.finalizeLayout();
		
		final Box viewsBox = Box.createVerticalBox();
		chatBrowser.setContentType( "text/html" );
		chatBrowser.setBorder( null ); // Chat panel content has darker background, remove border cause it does not follow that
		chatBrowserScrollPane.setVisible( false );
		viewsBox.add( chatBrowserScrollPane );
		chatTable.setTableCellRenderer( new UserColoredTableRenderer( chatTable, 0 ) );
		chatTableWrapperBox.setVisible( false );
		viewsBox.add( chatTableWrapperBox );
		addCenter( viewsBox );
		
		// Listen setting changes
		final ISettingChangeListener scl = new ISettingChangeListener() {
			@Override
			public void valuesChanged( final ISettingChangeEvent event ) {
				if ( event.affected( Settings.PARAGRAPH_BREAK_TIME_LIMIT ) )
					rebuildChat();
			}
		};
		SettingsGui.addBindExecuteScl( scl, Env.APP_SETTINGS, Settings.PARAGRAPH_BREAK_TIME_LIMIT.SELF_SET, this );
	}
	
	/**
	 * Rebuilds the chat content.
	 */
	private void rebuildChat() {
		if ( tableViewCheckBox.isSelected() ) {
			if ( !chatTableWrapperBox.isVisible() ) {
				chatBrowserScrollPane.setVisible( false );
				chatTableWrapperBox.setVisible( true );
			}
			rebuildTableChat();
		} else {
			if ( !chatBrowserScrollPane.isVisible() ) {
				chatTableWrapperBox.setVisible( false );
				chatBrowserScrollPane.setVisible( true );
			}
			rebuildBrowserChat();
		}
	}
	
	/**
	 * Rebuilds the browser chat content.
	 */
	private void rebuildBrowserChat() {
		final boolean showRecipient = showMessageRecipientCheckBox.isSelected();
		final boolean formParagraphs = formatIntoPsCheckBox.isSelected();
		final long breakMs = Env.APP_SETTINGS.get( Settings.PARAGRAPH_BREAK_TIME_LIMIT ) * 1000;
		final boolean showPings = showPingsCheckBox.isSelected();
		
		final StringBuilder sb = new StringBuilder();
		sb.append( "<html><head><style>.c{" ).append( ChatEvent.RICON.getCSS() ).append( "} .p{" ).append( PingEvent.RICON.getCSS() )
		        .append( "}</style></head><body style='background:#f0f0f0;padding:3px'>" );
		
		long lastTime = 0;
		int messageCounter = 0;
		for ( final Event e : repProc.replay.messageEvents.events ) {
			if ( !( e.id == IMessageEvents.ID_CHAT || showPings && e.id == IMessageEvents.ID_PING ) )
				continue;
			
			if ( formParagraphs ) {
				final long time = repProc.loopToTime( e.loop );
				if ( messageCounter > 0 && time - lastTime > breakMs )
					sb.append( "<br>" );
				lastTime = time;
			}
			
			final User u = repProc.getUser( e.userId );
			sb.append( "<div class='" ).append( e instanceof ChatEvent ? 'c' : 'p' ).append( "' style='color:" )
			        .append( u.getPlayerColor().darkerCssColor ).append( "'>" );
			sb.append( "<tt style='background:" ).append( u.getPlayerColor().cssColor ).append( "'>&nbsp;&nbsp;</tt> " );
			sb.append( repProc.formatLoopTime( e.loop ) ).append( " - " );
			sb.append( "<b>" ).append( Utils.safeForHtml( u.uid.fullName ) ).append( "</b>" );
			
			if ( e.id == IMessageEvents.ID_CHAT ) {
				// Chat message
				final ChatEvent ce = (ChatEvent) e;
				if ( showRecipient )
					sb.append( " to " ).append( ce.getRecipient() );
				sb.append( ": " );
				sb.append( Utils.safeForHtml( ce.getText() ) );
			} else if ( e.id == IMessageEvents.ID_PING ) {
				// Minimap Ping
				final PingEvent pe = (PingEvent) e;
				sb.append( " pinged" );
				if ( showRecipient )
					sb.append( " to " ).append( pe.getRecipient() );
				sb.append( ": X=" ).append( Env.LANG.formatNumber( pe.getXFloat(), 3 ) );
				sb.append( ", Y=" ).append( Env.LANG.formatNumber( pe.getYFloat(), 3 ) );
			}
			
			sb.append( "</div>" );
			messageCounter++;
		}
		
		sb.append( "</body></html>" );
		
		chatBrowser.setText( sb.toString() );
		
		chatBrowser.setCaretPosition( 0 ); // Scroll to top
	}
	
	/**
	 * Rebuilds the table chat content.
	 */
	private void rebuildTableChat() {
		final boolean showRecipient = showMessageRecipientCheckBox.isSelected();
		final boolean formParagraphs = formatIntoPsCheckBox.isSelected();
		final long breakMs = Env.APP_SETTINGS.get( Settings.PARAGRAPH_BREAK_TIME_LIMIT ) * 1000;
		final boolean showPings = showPingsCheckBox.isSelected();
		
		final Vector< String > columns;
		if ( showRecipient )
			columns = Utils.asNewVector( "User Color", "", "Time", "User", "Recipient", "Text" );
		else
			columns = Utils.asNewVector( "", "", "Time", "User", "Text" );
		final Vector< Vector< Object > > data = new Vector<>();
		
		long lastTime = 0;
		for ( final Event e : repProc.replay.messageEvents.events ) {
			if ( !( e instanceof ChatEvent || showPings && e instanceof PingEvent ) )
				continue;
			
			if ( formParagraphs ) {
				final long time = repProc.loopToTime( e.loop );
				if ( !data.isEmpty() && time - lastTime > breakMs )
					data.add( new Vector<>() );
				lastTime = time;
			}
			
			final Vector< Object > row = new Vector<>( columns.size() );
			final User u = repProc.getUser( e.userId );
			
			row.add( u.getPlayerColor().darkerColor );
			row.add( e.getRicon() );
			row.add( new DurationValue( repProc.loopToTime( e.loop ) ) );
			row.add( u.uid.fullName );
			
			if ( e instanceof ChatEvent ) {
				// Chat message
				final ChatEvent ce = (ChatEvent) e;
				if ( showRecipient )
					row.add( ce.getRecipient() );
				row.add( ce.getText() );
			} else if ( e instanceof PingEvent ) {
				// Minimap Ping
				final PingEvent pe = (PingEvent) e;
				if ( showRecipient )
					row.add( pe.getRecipient() );
				row.add( "Pinged position: X=" + Env.LANG.formatNumber( pe.getXFloat(), 3 ) + ", Y=" + Env.LANG.formatNumber( pe.getYFloat(), 3 ) );
			}
			
			data.add( row );
		}
		
		chatTable.getXTableModel().setDataVector( data, columns );
		chatTable.removeColumn( chatTable.getColumnModel().getColumn( 0 ) ); // Hide color column
		chatTable.packColumnsExceptLast();
		chatTable.setSortable( false ); // Disable sorting
	}
	
}
