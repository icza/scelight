/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelightapibase.util.gui;

import hu.scelightapibase.util.IPair;

import javax.swing.Icon;

/**
 * A pair designed to be rendered.
 * 
 * <p>
 * Basically this interface is to associate an icon to a renderable object which itself does not define one (e.g. through {@link HasIcon} interface).<br>
 * The first object of the pair is an {@link Icon}, the second object is the provider of the displayable text (acquired by {@link Object#toString()}). This
 * interface is suitable for {@link hu.scelightapi.util.gui.IGuiUtils#renderToLabel(javax.swing.JLabel, Object, boolean, boolean)}.
 * </p>
 * 
 * @param <T> type of the object providing the displayed text
 * 
 * @author Andras Belicza
 * 
 * @see hu.scelightapi.service.IGuiFactory#newRenderablePair(Icon, Object)
 * @see hu.scelightapi.util.gui.IGuiUtils#renderToLabel(javax.swing.JLabel, Object, boolean, boolean)
 * @see IPair
 */
public interface IRenderablePair< T > extends IPair< Icon, T >, HasIcon {
	
	// No additional methods
	
}
