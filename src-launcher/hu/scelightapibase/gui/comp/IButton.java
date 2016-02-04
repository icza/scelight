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

import hu.scelightapibase.gui.icon.IRIcon;

import javax.swing.Icon;
import javax.swing.JButton;

/**
 * An extended {@link JButton}.
 * 
 * <p>
 * Button texts might have a mnemonic character indicated with the underscore (<code>'_'</code>). If such indicator is detected, it will be removed from the
 * visible text and the character following the underscore will be set as the button's mnemonic.
 * </p>
 * 
 * @author Andras Belicza
 * 
 * @see hu.scelightapi.util.gui.IGuiUtils#autoCreateDisabledImage(JButton)
 * 
 * @see hu.scelightapi.service.IGuiFactory#newButton()
 * @see hu.scelightapi.service.IGuiFactory#newButton(String)
 * @see hu.scelightapi.service.IGuiFactory#newButton(Icon)
 * @see hu.scelightapi.service.IGuiFactory#newButton(String, Icon)
 * @see hu.scelightapi.service.IGuiFactory#newButton(hu.scelightapibase.action.IAction)
 */
public interface IButton {
	
	/**
	 * Casts this instance to {@link JButton}.
	 * 
	 * @return <code>this</code> as a {@link JButton}
	 */
	JButton asButton();
	
	/**
	 * Configures the button to be an "icon button"; only the icon will be displayed.
	 * 
	 * <p>
	 * It is also recommended to set the pressed icon (with {@link JButton#setPressedIcon(Icon)}); for example to be the same in smaller size (-2 pixels). This
	 * smaller icon can easily be acquired if you use {@link IRIcon} ({@link IRIcon#size(int)}).
	 * </p>
	 */
	void configureAsIconButton();
	
}
