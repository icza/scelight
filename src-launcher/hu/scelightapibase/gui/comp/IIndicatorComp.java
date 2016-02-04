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

import javax.swing.Icon;
import javax.swing.JComponent;

/**
 * A component with an indicator icon ({@link ILabel}) to display the content validation result.
 * 
 * @since 1.4
 * 
 * @author Andras Belicza
 * 
 * @see IIndicatorTextField
 * @see hu.scelightapi.gui.comp.IIndicatorTextArea
 */
public interface IIndicatorComp {
	
	/**
	 * Casts this instance to {@link JComponent}.
	 * 
	 * @return <code>this</code> as a {@link JComponent}
	 */
	JComponent asComponent();
	
	/**
	 * Returns the indicator label.
	 * 
	 * @return the indicator label
	 */
	ILabel getIndicator();
	
	/**
	 * Changes the icon of the indicator to the empty icon meaning: no content is entered.
	 * 
	 * @param message message to be set as tool tip text for the indicator label
	 */
	void setEmpty( String message );
	
	/**
	 * Changes the icon of the indicator to the accepted icon meaning: content is valid and accepted.
	 * 
	 * @param message message to be set as tool tip text for the indicator label
	 */
	void setAccepted( String message );
	
	/**
	 * Changes the icon of the indicator to the not accepted icon: content is valid but not accepted.
	 * 
	 * @param message message to be set as tool tip text for the indicator label
	 */
	void setNotAccepted( String message );
	
	/**
	 * Changes the icon of the indicator to the error icon meaning: the content is invalid.
	 * 
	 * @param message message to be set as tool tip text for the indicator label
	 */
	void setError( String message );
	
	/**
	 * Configures the indicator label.
	 * 
	 * @param icon icon to be set for the indicator label
	 * @param message message to be set as tool tip text for the indicator label
	 */
	void configureIndicator( Icon icon, String message );
	
}
