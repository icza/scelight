/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight.gui.page.repanalyzer.charts;

import hu.scelight.gui.comp.XMenu;
import hu.scelight.gui.comp.XMenuItem;
import hu.scelight.gui.comp.XPopupMenu;
import hu.scelight.gui.icon.Icons;
import hu.scelight.sc2.rep.model.details.Toon;
import hu.scelight.sc2.rep.model.initdata.gamedesc.BnetLang;
import hu.scelight.sc2.rep.model.initdata.lobbystate.Controller;
import hu.scelight.sc2.rep.repproc.RepProcessor;
import hu.scelight.sc2.rep.repproc.User;
import hu.scelight.service.env.Env;
import hu.scelight.service.settings.Settings;
import hu.scelight.util.Utils;
import hu.sllauncher.util.gui.adapter.ActionAdapter;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Player menu.
 * 
 * @author Andras Belicza
 */
@SuppressWarnings( "serial" )
public class PlayerMenu extends XPopupMenu implements ActionListener {
	
	/** The associated user. */
	private final User      user;
	
	/** Player toon string of the user. */
	private final String    toonString;
	
	
	/** View character profile menu item. */
	private final XMenuItem viewCharProfileMenuItem       = new XMenuItem( "View Character Profile", Icons.SC2_PROFILE.get() );
	
	/** Copy player toon menu item. */
	private final XMenuItem copyToonMenuItem              = new XMenuItem( Icons.F_CLIPBOARD_SIGN.get() );
	
	/** Add to the Favored player list menu item. */
	private final XMenuItem addToFavoredListMenuItem      = new XMenuItem( "Add to the Favored player list", Icons.F_USER_PLUS.get() );
	
	/** Remove from the Favored player list menu item. */
	private final XMenuItem removeFromFavoredListMenuItem = new XMenuItem( "Remove from the Favored player list", Icons.F_USER_MINUS.get() );
	
	
	/**
	 * Creates a new {@link PlayerMenu}.
	 * 
	 * @param event event triggering the menu
	 * @param user user object to create a player menu for
	 */
	public PlayerMenu( final ActionEvent event, final User user ) {
		super( "<html><code>" + user.fullName + "</code></html>" );
		
		this.user = user;
		final Toon toon = user.getToon();
		toonString = ( toon == null ? "" : toon.toString() ) + "-" + user.fullName;
		
		// TODO Items:
		// View Sc2ranks.com Profile
		// View GGTracker Profile
		// ---------------
		// List other replays of this play in > [Replay folders in submenu]
		
		viewCharProfileMenuItem.addActionListener( this );
		add( viewCharProfileMenuItem );
		final XMenu viewProfileInLangMenu = new XMenu( "View Character Profile in Language", Icons.SC2_PROFILE.get() );
		add( viewProfileInLangMenu );
		if ( toon == null || user.slot.getController() == Controller.COMPUTER || user.name == null ) {
			viewCharProfileMenuItem.setEnabled( false );
			viewProfileInLangMenu.setEnabled( false );
		} else {
			for ( final BnetLang bl : toon.region.langSet ) {
				final XMenuItem langMenuItem = new XMenuItem( bl.text, bl.ricon.get() );
				langMenuItem.addActionListener( new ActionAdapter() {
					@Override
					public void actionPerformed( final ActionEvent event ) {
						final URL profileUrl = toon.getProfileUrl( bl, user.name );
						if ( profileUrl != null )
							Env.UTILS_IMPL.get().showURLInBrowser( profileUrl );
					}
				} );
				viewProfileInLangMenu.add( langMenuItem );
			}
		}
		
		addSeparator();
		copyToonMenuItem.addActionListener( this );
		copyToonMenuItem.setText( "<html>Copy toon: <code>" + Utils.safeForHtml( toonString ) + "</code></html>" );
		if ( toon == null || user.slot.getController() == Controller.COMPUTER )
			copyToonMenuItem.setEnabled( false );
		add( copyToonMenuItem );
		
		addSeparator();
		if ( RepProcessor.favoredToonList.get().contains( toon ) ) {
			removeFromFavoredListMenuItem.addActionListener( this );
			add( removeFromFavoredListMenuItem );
		} else {
			addToFavoredListMenuItem.addActionListener( this );
			add( addToFavoredListMenuItem );
		}
		
		final Component src = (Component) event.getSource();
		show( src, 0, src.getHeight() );
	}
	
	@Override
	public void actionPerformed( final ActionEvent event ) {
		final Object src = event.getSource();
		
		if ( src == viewCharProfileMenuItem ) {
			
			final Toon toon = user.getToon();
			if ( user.name != null ) {
				final URL profileUrl = toon.getProfileUrl( user.name );
				if ( profileUrl != null )
					Env.UTILS_IMPL.get().showURLInBrowser( profileUrl );
			}
			
		} else if ( src == copyToonMenuItem ) {
			
			Utils.copyToClipboard( toonString );
			
		} else if ( src == addToFavoredListMenuItem ) {
			
			final List< Toon > toonList = new ArrayList<>( RepProcessor.favoredToonList.get() );
			toonList.add( new Toon( toonString ) ); // Create a new toon with player name included
			Env.APP_SETTINGS.set( Settings.FAVORED_PLAYER_TOONS, Utils.concatenate( toonList ) );
			
		} else if ( src == removeFromFavoredListMenuItem ) {
			
			final List< Toon > toonList = new ArrayList<>( RepProcessor.favoredToonList.get() );
			toonList.remove( user.getToon() );
			Env.APP_SETTINGS.set( Settings.FAVORED_PLAYER_TOONS, Utils.concatenate( toonList ) );
			
		}
	}
	
}
