/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelightapibase.gui.comp;

import hu.scelightapibase.util.IRHtml;

import java.awt.event.MouseEvent;

/**
 * A clickable icon which when clicked displays the content of a {@link IRHtml} in a popup.
 * 
 * <p>
 * Only returns the tool tip text if the content is to be shown only for click and not for mouse over and a popup is not currently visible.
 * </p>
 * 
 * @author Andras Belicza
 */
public interface IPopupIcon extends ILabel {
	
	/**
	 * Shows the content in popup.
	 * 
	 * @param event event to use to determine source component and location
	 */
	void showPopup( MouseEvent event );
	
	/**
	 * Hides the popup if it is visible.
	 */
	void hidePopup();
	
}
