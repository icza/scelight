/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight.bean.adapter;

import java.nio.file.Path;
import java.nio.file.Paths;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * An {@link XmlAdapter} that handles {@link Path}s.
 * 
 * @author Andras Belicza
 */
public class PathAdapter extends XmlAdapter< String, Path > {
	
	@Override
	public Path unmarshal( final String v ) throws Exception {
		return Paths.get( v );
	}
	
	@Override
	public String marshal( final Path v ) throws Exception {
		return v.toString();
	}
	
}
