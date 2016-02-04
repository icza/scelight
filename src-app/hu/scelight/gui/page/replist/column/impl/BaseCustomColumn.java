/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight.gui.page.replist.column.impl;

import hu.scelight.gui.page.replist.column.BaseColumn;
import hu.scelight.sc2.rep.repproc.RepProcessor;
import hu.scelight.service.env.Env;
import hu.scelight.template.InvalidTemplateException;
import hu.scelight.template.TemplateEngine;
import hu.scelight.util.Utils;

import java.nio.file.Path;

/**
 * Base custom column which specifies the column data with a name template.
 * 
 * @author Andras Belicza
 */
public abstract class BaseCustomColumn extends BaseColumn< String > {
	
	/** Template engine created from the column's name template. */
	private final TemplateEngine engine;
	
	/** Constant error result to be returned as column data if name template is not valid. */
	private final String         errorResult;
	
	/**
	 * Creates a new {@link BaseCustomColumn}.
	 * 
	 * @param custColId custom column id
	 * @param name name of the custom column
	 * @param nameTemplate name template of the custom column
	 */
	public BaseCustomColumn( final int custColId, final String name, final String nameTemplate ) {
		super( name, null, "Custom column #" + custColId + " with template: " + nameTemplate, String.class, false );
		
		if ( nameTemplate.isEmpty() ) {
			errorResult = "Template not set!";
			engine = null;
		} else {
			TemplateEngine engine = null;
			
			try {
				engine = new TemplateEngine( nameTemplate );
			} catch ( final InvalidTemplateException ite ) {
				Env.LOGGER.warning( "Invalid name template for custom column #" + custColId + ": " + nameTemplate, ite );
			}
			
			if ( engine == null ) {
				errorResult = "Invalid template!";
				this.engine = null;
			} else {
				this.engine = engine;
				errorResult = null;
			}
		}
	}
	
	@Override
	public String getData( final RepProcessor repProc ) {
		if ( errorResult != null )
			return errorResult;
		
		// Template engine is not synchronized, we have to synchronize externally:
		final Path result;
		synchronized ( engine ) {
			result = engine.apply( repProc, repProc.file.getParent() );
		}
		return result == null ? null : Utils.getFileNameWithoutExt( result );
	}
	
}
