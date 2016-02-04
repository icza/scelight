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

import hu.scelightapibase.gui.comp.IHelpIcon;
import hu.scelightapibase.util.IRHtml;
import hu.sllauncher.gui.icon.LIcons;

/**
 * A help icon which when clicked displays help text in a popup.
 * 
 * @author Andras Belicza
 */
@SuppressWarnings( "serial" )
public class HelpIcon extends PopupIcon implements IHelpIcon {
	
	/**
	 * Creates a new {@link HelpIcon}.
	 * 
	 * @param helpRhtml html template resource of the help content
	 */
	public HelpIcon( final IRHtml helpRhtml ) {
		this( helpRhtml, false );
	}
	
	/**
	 * Creates a new {@link HelpIcon}.
	 * 
	 * @param helpRhtml html template resource of the help content
	 * @param stressed tells if the help icon's appearance should be garish, stressed
	 */
	public HelpIcon( final IRHtml helpRhtml, final boolean stressed ) {
		super( stressed ? LIcons.F_QUESTION : LIcons.F_QUESTION_WHITE, helpRhtml );
		
		setToolTipText( "<html>&nbsp;Click to show help for <b><u>" + helpRhtml.getTitle() + "</u></b>&nbsp;&nbsp;</html>" );
	}
	
}
