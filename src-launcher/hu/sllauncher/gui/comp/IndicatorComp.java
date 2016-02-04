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

import hu.scelightapibase.gui.comp.IIndicatorComp;
import hu.sllauncher.gui.icon.LIcons;

import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.JComponent;

/**
 * A base {@link IIndicatorComp} implementation.
 * 
 * @author Andras Belicza
 */
@SuppressWarnings( "serial" )
public class IndicatorComp extends BorderPanel implements IIndicatorComp {
	
	/** Indicator label. */
	public final XLabel indicator    = new XLabel();
	
	/** Box holding the indicator label. */
	public final Box    indicatorBox = Box.createHorizontalBox();
	
	/**
	 * Creates a new {@link IndicatorComp}.
	 */
	public IndicatorComp() {
		indicatorBox.add( indicator );
		addEast( indicatorBox );
	}
	
	@Override
	public JComponent asComponent() {
		return this;
	}
	
	@Override
	public XLabel getIndicator() {
		return indicator;
	}
	
	@Override
	public void setEmpty( final String message ) {
		configureIndicator( LIcons.F_NA.get(), message );
	}
	
	@Override
	public void setAccepted( final String message ) {
		configureIndicator( LIcons.F_TICK.get(), message );
	}
	
	@Override
	public void setNotAccepted( final String message ) {
		configureIndicator( LIcons.F_EXCLAMATION_RED.get(), message );
	}
	
	@Override
	public void setError( final String message ) {
		configureIndicator( LIcons.F_CROSS.get(), message );
	}
	
	@Override
	public void configureIndicator( final Icon icon, final String message ) {
		indicator.setIcon( icon );
		indicator.setToolTipText( message );
	}
	
}
