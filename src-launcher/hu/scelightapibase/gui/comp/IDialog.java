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

import java.awt.Dimension;
import java.awt.Window;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;

/**
 * A customized {@link JDialog} class.
 * 
 * <p>
 * The content pane of the dialog ({@link JDialog#getContentPane()}) will be an {@link IBorderPanel}.
 * </p>
 * 
 * <p>
 * The proper way to make an initialized and prepared {@link IDialog} visible:
 * 
 * <pre>
 * <blockquote style='border:1px solid black'>
 * dialog.restoreDefaultSize();
 * dialog.asDialog().setVisible( true );
 * </blockquote>
 * </pre>
 * 
 * </p>
 * 
 * @author Andras Belicza
 * 
 * @see hu.scelightapi.service.IGuiFactory#newDialog(Runnable)
 * @see hu.scelightapi.service.IGuiFactory#newDialog(java.awt.Frame, Runnable)
 * @see hu.scelightapi.gui.overlaycard.IOverlayCard
 */
public interface IDialog {
	
	/**
	 * Casts this instance to {@link JDialog}.
	 * 
	 * @return <code>this</code> as a {@link JDialog}
	 */
	JDialog asDialog();
	
	/**
	 * Sets the default size of the dialog.
	 * 
	 * @param defaultSize default size to be set
	 */
	void setDefaultSize( Dimension defaultSize );
	
	/**
	 * Returns the default size of the dialog.
	 * 
	 * @return the default size of the dialog
	 */
	Dimension getDefaultSize();
	
	/**
	 * Returns the default value of the default dialog size.
	 * 
	 * <p>
	 * You can use this if you want to create a dialog with a default size being a little smaller than the normal size (by setting a default size being a little
	 * smaller than the returned default-default size).
	 * </p>
	 * 
	 * @return the default value of the default dialog size
	 */
	Dimension getDefaultDefaultSize();
	
	/**
	 * Returns the custom border panel functioning as the content panel.
	 * 
	 * @return the custom border panel functioning as the content panel
	 */
	IBorderPanel getCp();
	
	/**
	 * Enlarges the dialog, maximizes it with a small margin.
	 */
	void enlargeDialog();
	
	/**
	 * Restores the default size of the dialog.
	 */
	void restoreDefaultSize();
	
	/**
	 * Sets the component to be focused on first show (when dialog is first activated).
	 * 
	 * @param focusTarget focus target component on first show to be set
	 */
	void setFocusTargetOnFirstShow( JComponent focusTarget );
	
	/**
	 * Adds a close button to the buttons panel ({@link #getButtonsPanel()}) using the specified text.
	 * 
	 * @param text text of the close button
	 * @return the close button that was just created and added
	 */
	IButton addCloseButton( String text );
	
	/**
	 * Returns the buttons panel.
	 * 
	 * <p>
	 * If this is the first call, the panel will be created, initialized and added to the south of the content pane.
	 * </p>
	 * 
	 * @return the buttons panel
	 */
	JPanel getButtonsPanel();
	
	/**
	 * Closes the dialog. {@link IDialog}s must always be closed with this method instead of {@link Window#dispose()}.
	 */
	void close();
	
}
