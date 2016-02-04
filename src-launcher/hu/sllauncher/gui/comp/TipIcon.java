/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.sllauncher.gui.comp;

import hu.scelightapibase.gui.comp.ITipIcon;
import hu.scelightapibase.util.IRHtml;
import hu.sllauncher.gui.icon.LIcons;

/**
 * A tip icon which when clicked displays tip text in a popup.
 * 
 * @author Andras Belicza
 */
@SuppressWarnings( "serial" )
public class TipIcon extends PopupIcon implements ITipIcon {
	
	/**
	 * Creates a new {@link TipIcon}.
	 * 
	 * @param tipRhtml html template resource of the tip content
	 */
	public TipIcon( final IRHtml tipRhtml ) {
		super( LIcons.F_LIGHT_BULB, tipRhtml );
		
		setToolTipText( "<html>&nbsp;Click to show tip for <b><u>" + tipRhtml.getTitle() + "</u></b>&nbsp;&nbsp;</html>" );
	}
	
}
