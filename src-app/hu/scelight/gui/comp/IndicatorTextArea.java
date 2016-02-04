/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight.gui.comp;

import hu.scelightapi.gui.comp.IIndicatorTextArea;
import hu.scelightapibase.gui.comp.IIndicatorComp;
import hu.scelightapibase.gui.comp.ITextArea;
import hu.sllauncher.gui.comp.IndicatorComp;
import hu.sllauncher.gui.comp.XScrollPane;
import hu.sllauncher.gui.comp.XTextArea;

/**
 * An {@link IIndicatorComp} whose component is an {@link ITextArea}.
 * 
 * @author Andras Belicza
 */
@SuppressWarnings( "serial" )
public class IndicatorTextArea extends IndicatorComp implements IIndicatorTextArea {
	
	/** Text area. */
	public final XTextArea textArea = new XTextArea();
	
	/**
	 * Creates a new {@link IndicatorTextArea}.
	 */
	public IndicatorTextArea() {
		this( null );
	}
	
	/**
	 * Creates a new {@link IndicatorTextArea}.
	 * 
	 * @param text initial text to be set
	 */
	public IndicatorTextArea( final String text ) {
		if ( text != null )
			textArea.setText( text );
		addCenter( new XScrollPane( textArea, false ) );
	}
	
	@Override
	public XTextArea getTextArea() {
		return textArea;
	}
	
	@Override
	public String getText() {
		return textArea.getText();
	}
	
	@Override
	public void setText( final String text ) {
		textArea.setText( text );
	}
	
}
