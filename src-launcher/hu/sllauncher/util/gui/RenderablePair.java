/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.sllauncher.util.gui;

import hu.scelightapibase.util.gui.HasIcon;
import hu.scelightapibase.util.gui.IRenderablePair;
import hu.sllauncher.util.Pair;

import javax.swing.Icon;

/**
 * A pair designed to be rendered.
 * 
 * <p>
 * Basically this class is to associate an icon to a renderable object which itself does not define one (e.g. through {@link HasIcon} interface).<br>
 * The first object of the pair is an {@link Icon}, the second object is the provider of the displayable text (acquired by {@link Object#toString()}). This
 * class is suitable for {@link ToLabelRenderer#render(javax.swing.JLabel, Object, boolean, boolean)}.
 * </p>
 * 
 * <p>
 * Implementation is IMMUTABLE.
 * </p>
 * 
 * @param <T> type of the object providing the displayed text
 * 
 * @author Andras Belicza
 * 
 * @see ToLabelRenderer
 */
public class RenderablePair< T > extends Pair< Icon, T > implements IRenderablePair< T > {
	
	/**
	 * Creates a new {@link RenderablePair}.
	 * 
	 * @param icon icon to be rendered
	 * @param textProvider object providing the displayable text (acquired by {@link Object#toString()})
	 */
	public RenderablePair( final Icon icon, final T textProvider ) {
		super( icon, textProvider );
	}
	
	@Override
	public Icon getIcon() {
		return value1;
	}
	
	@Override
	public String toString() {
		return value2 == null ? null : value2.toString();
	}
	
}
