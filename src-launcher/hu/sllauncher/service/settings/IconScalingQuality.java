/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.sllauncher.service.settings;

import java.awt.RenderingHints;

/**
 * Icon scaling quality.
 * 
 * @author Andras Belicza
 */
public enum IconScalingQuality {
	
	/** Low (fastest) icon scaling quality. */
	LOW( "Low (Fastest)", RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR ),
	
	/** Medium icon scaling quality. */
	MEDIUM( "Medium (Default)", RenderingHints.VALUE_INTERPOLATION_BILINEAR ),
	
	/** High (smoothest) icon scaling quality. */
	HIGH( "High (Smoothest)", RenderingHints.VALUE_INTERPOLATION_BICUBIC );
	
	
	/** Text value of the icon scaling quality. */
	public final String text;
	
	/** Rendering hint value associated with the icon scaling quality. */
	public final Object renderingHintValue;
	
	/**
	 * Creates a new {@link IconScalingQuality}.
	 * 
	 * @param text text value of the icon scaling quality
	 * @param renderingHintValue rendering hint value associated with the icon scaling quality
	 */
	private IconScalingQuality( final String text, final Object renderingHintValue ) {
		this.text = text;
		this.renderingHintValue = renderingHintValue;
	}
	
	@Override
	public String toString() {
		return text;
	}
	
}
