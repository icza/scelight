/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelightapi.gui.comp;

import hu.scelightapi.service.IGuiFactory;
import hu.scelightapibase.gui.comp.IIndicatorComp;
import hu.scelightapibase.gui.comp.IIndicatorTextField;
import hu.scelightapibase.gui.comp.ITextArea;
import hu.scelightapibase.gui.comp.ITextField.IValidator;

/**
 * An {@link IIndicatorComp} whose component is an {@link ITextArea}.
 * 
 * <p>
 * Usually the {@link IIndicatorTextArea} is used with an {@link IValidator} installed on the text area of the {@link IIndicatorTextArea}. The
 * {@link IValidator} implementation can take advantage on the status this indicator text area can display.
 * </p>
 * 
 * @since 1.4
 * 
 * @author Andras Belicza
 * 
 * @see IGuiFactory#newIndicatorTextArea()
 * @see IGuiFactory#newIndicatorTextArea(String)
 * @see IIndicatorTextField
 */
public interface IIndicatorTextArea extends IIndicatorComp {
	
	/**
	 * Returns the text area.
	 * 
	 * @return the text area
	 */
	ITextArea getTextArea();
	
	/**
	 * Returns the current text.
	 * 
	 * @return the current text
	 */
	String getText();
	
	/**
	 * Sets the specified text.
	 * 
	 * @param text text to be set
	 */
	void setText( String text );
	
}
