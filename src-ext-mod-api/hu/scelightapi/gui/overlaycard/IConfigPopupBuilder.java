/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelightapi.gui.overlaycard;

import hu.scelightapi.gui.comp.IPopupMenu;
import hu.scelightapi.service.IGuiFactory;

/**
 * A config popup builder.
 * 
 * The config popup builder of an overlay card is called before its config popup is being made visible, giving an opportunity to add custom items to it.
 * 
 * @author Andras Belicza
 * 
 * @see IOverlayCard
 * @see IGuiFactory#newOverlayCard(OverlayCardParams, IConfigPopupBuilder, Runnable)
 */
public interface IConfigPopupBuilder {
	
	/**
	 * Builds the config popup menu
	 * 
	 * @param popupMenu popup menu to be built
	 */
	void buildConfigPopup( IPopupMenu popupMenu );
	
}
