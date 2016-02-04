/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelightapibase.bean;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marker annotation to mark hidden bean properties.
 * 
 * <p>
 * Get methods marked with this will not be included in the string representation of a bean built by {@link IBean#buildDevString(StringBuilder)}.
 * </p>
 * 
 * @author Andras Belicza
 */
@Retention( RetentionPolicy.RUNTIME )
@Target( ElementType.METHOD )
public @interface HiddenProperty {
	
	// This is just a marker annotation.
	
}
