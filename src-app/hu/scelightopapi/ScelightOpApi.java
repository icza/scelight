/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelightopapi;

import hu.scelightopapibase.ScelightOpApiBase;

/**
 * Scelight Operator API.
 * 
 * @author Andras Belicza
 */
public class ScelightOpApi extends ScelightOpApiBase {
	
	/** Update check type modules digest parameter name. */
	public static final String PARAM_MODULES_DIGEST_TYPE = "ty";
	
	/** Elapsed minutes since application start modules digest parameter name. */
	public static final String PARAM_MODULES_DIGEST_MINS = "mi";
	
	
	
	/** Registration file servlet ticket parameter name. */
	public static final String PARAM_REGFILE_TICKET      = "ti";
	
	/** Registration file servlet system info parameter name. */
	public static final String PARAM_REGFILE_SYSINFO     = "si";
	
}
