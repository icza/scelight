/*
 * Project Abdra Commander
 * 
 * Copyright (c) 2012 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelightapi.template;

import hu.scelightapi.gui.comp.ITemplateField;
import hu.scelightapi.sc2.rep.repproc.IRepProcessor;
import hu.scelightapi.service.IFactory;

import java.nio.file.Path;

/**
 * Name template engine interface to apply templates on replay files.
 * 
 * <p>
 * Implementation is not thread-safe. Parallel use is not supported.
 * </p>
 * 
 * @author Andras Belicza
 * 
 * @see IFactory#newTemplateEngine(String)
 * @see ITemplateField
 * @see IFactory#getTemplateEngineVersionBean()
 */
public interface ITemplateEngine {
	
	/**
	 * Applies the template on the specified replay file in the folder the file is in, and returns the result. The specified file is <b>NOT</b> renamed or
	 * moved.
	 * 
	 * @param file replay file to apply the template on
	 * @return the new path after applying the template; or <code>null</code> if the specified replay file cannot be parsed
	 */
	Path apply( Path file );
	
	/**
	 * Applies the template on the specified replay file, and returns the result. The specified file is <b>NOT</b> renamed or moved.
	 * 
	 * @param file replay file to apply the template on
	 * @param targetFolder target folder in which to apply the template
	 * @return the new path after applying the template; or <code>null</code> if the specified replay file cannot be parsed
	 */
	Path apply( Path file, Path targetFolder );
	
	/**
	 * Applies the template on the specified replay file, and returns the result. The specified file is <b>NOT</b> renamed or moved.
	 * 
	 * @param repProc replay processor whose replay file to apply the template on
	 * @param targetFolder target folder in which to apply the template
	 * @return the new path after applying the template; or <code>null</code> if the specified replay file cannot be parsed
	 */
	Path apply( IRepProcessor repProc, Path targetFolder );
	
}
