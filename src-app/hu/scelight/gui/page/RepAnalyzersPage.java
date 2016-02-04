/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight.gui.page;

import hu.scelight.action.Actions;
import hu.scelight.gui.comp.XPopupMenu;
import hu.scelight.gui.icon.Icons;
import hu.scelight.gui.page.repanalyzer.RepAnalyzerPage;
import hu.scelight.service.env.Env;
import hu.scelight.util.gui.GuiUtils;
import hu.sllauncher.action.XAction;
import hu.sllauncher.gui.comp.BorderPanel;
import hu.sllauncher.gui.comp.XButton;
import hu.sllauncher.gui.comp.multipage.BasePage;

import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.nio.file.Path;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JPanel;

/**
 * Parent page for the replay analyzer pages.
 * 
 * @author Andras Belicza
 */
public class RepAnalyzersPage extends BasePage< JComponent > {
	
	/**
	 * Creates a new {@link RepAnalyzersPage}.
	 */
	public RepAnalyzersPage() {
		super( "Replay Analyzers", Icons.MY_CHARTS );
	}
	
	@Override
	public JComponent createPageComp() {
		final Box box = Box.createVerticalBox();
		
		final JPanel buttonsPanel = new JPanel( new GridLayout( 3, 1 ) );
		buttonsPanel.setBorder( BorderFactory.createTitledBorder( "New Replay Analyzer" ) );
		buttonsPanel.add( new XButton( Actions.OPEN_REPLAY ) );
		buttonsPanel.add( new XButton( Actions.QUICK_OPEN_LAST_REP ) );
		
		@SuppressWarnings( "serial" )
		final XAction recentReplaysAction = new XAction( Icons.MY_CHARTS, "Open a _Recent Replay...", box ) {
			@Override
			public void actionPerformed( final ActionEvent event ) {
				final Component src = (Component) event.getSource();
				final XPopupMenu popupMenu = new XPopupMenu( "Recent Replays:" );
				Env.MAIN_FRAME.createAndAddReplayItems( popupMenu );
				popupMenu.show( src, 0, src.getHeight() );
			}
		};
		buttonsPanel.add( new XButton( recentReplaysAction ) );
		
		box.add( GuiUtils.wrapInPanel( buttonsPanel ) );
		
		box.add( new BorderPanel() ); // To consume remaining space
		
		return box;
	}
	
	/**
	 * Creates a new Replay analyzer.
	 * 
	 * @param file replay file
	 * @param select tells if the new replay analyzer is to be selected
	 * @return the created new replay analyzer or <code>null</code> if the specified replay file cannot be parsed
	 */
	public RepAnalyzerPage newRepAnalyzerPage( final Path file, final boolean select ) {
		final RepAnalyzerPage ra = new RepAnalyzerPage( file );
		if ( ra.repProc == null ) {
			GuiUtils.showErrorMsg( "Could not parse replay file:", file.toString() );
			return null;
		}
		
		addChild( ra );
		
		Env.MAIN_FRAME.rebuildMainPageTree();
		
		if ( select )
			Env.MAIN_FRAME.multiPageComp.selectPage( ra );
		
		return ra;
	}
	
}
