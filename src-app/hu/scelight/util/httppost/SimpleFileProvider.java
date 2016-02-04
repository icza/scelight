/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight.util.httppost;

import hu.scelightapi.util.httppost.IFileProvider;

import java.net.HttpURLConnection;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * A simple file provider which takes the provided file name or the file itself and last modified date as constructor arguments.
 * 
 * @version Implementation version: {@value #IMPL_VERSION}
 * 
 * @author Andras Belicza
 * 
 * @see IFileProvider
 */
public class SimpleFileProvider implements IFileProvider {
	
	/** Implementation version. */
	public static final String IMPL_VERSION = "1.0";
	
	
	/** The provided file. */
	protected Path             file;
	
	/** Last modified date of the provided file. */
	protected Long             lastModified;
	
	/**
	 * Creates a new {@link SimpleFileProvider}.
	 * 
	 * @param fileName name (and path) of the provided file
	 * @param lastModified last modified date of the provided file
	 */
	public SimpleFileProvider( final String fileName, final Long lastModified ) {
		this( Paths.get( fileName ), lastModified );
	}
	
	/**
	 * Creates a new {@link SimpleFileProvider}.
	 * 
	 * @param file the provided file
	 * @param lastModified last modified date of the provided file
	 */
	public SimpleFileProvider( final Path file, final Long lastModified ) {
		this.file = file;
		this.lastModified = lastModified;
	}
	
	@Override
	public Path getFile( final HttpURLConnection httpUrlConnection ) {
		return file;
	}
	
	@Override
	public Long getLastModified( final HttpURLConnection httpUrlConnection ) {
		return lastModified;
	}
	
}
