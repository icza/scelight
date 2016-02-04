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

import hu.scelightapibase.gui.comp.IBorderPanel;
import hu.scelightapibase.gui.comp.IDialog;
import hu.scelightapibase.gui.comp.ILabel;

import javax.swing.JDialog;

/**
 * OS-level independent overlay cards which can be displayed on top of everything else.
 * 
 * <p>
 * They are implemented as {@link JDialog}s without decorations and with on-top capabilities.
 * </p>
 * 
 * <p>
 * The proper way to make an initialized and prepared {@link IOverlayCard} visible:
 * 
 * <pre>
 * <blockquote style='border:1px solid black'>
 * overlay.packAndPosition();
 * overlay.asDialog().setVisible( true );
 * </blockquote>
 * </pre>
 * 
 * </p>
 * 
 * @author Andras Belicza
 * 
 * @see hu.scelightapi.service.IGuiFactory#newOverlayCard(OverlayCardParams, IConfigPopupBuilder, Runnable)
 * @see IConfigPopupBuilder
 * @see IDialog
 */
public interface IOverlayCard extends IConfigPopupBuilder {
	
	/**
	 * Casts this instance to {@link JDialog}.
	 * 
	 * @return <code>this</code> as a {@link JDialog}
	 */
	JDialog asDialog();
	
	/**
	 * Returns the custom border panel functioning as the content panel.
	 * 
	 * @return the custom border panel functioning as the content panel
	 */
	IBorderPanel getCp();
	
	/**
	 * Returns the border panel functioning as the window title.
	 * 
	 * @return the border panel functioning as the window title
	 */
	IBorderPanel getTitlePanel();
	
	/**
	 * Returns the overlay title label.
	 * 
	 * @return the overlay title label
	 */
	ILabel getTitleLabel();
	
	/**
	 * Packs and positions this overlay card based on the overlay settings.
	 */
	void packAndPosition();
	
}
