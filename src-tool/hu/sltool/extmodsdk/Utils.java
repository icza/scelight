/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.sltool.extmodsdk;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Utilities for SDK related tasks.
 * 
 * <p>
 * NOTE: Implementation is not performance optimized but rather compact. Exceptions are not handled: it's not our goal.
 * </p>
 * 
 * @author Andras Belicza
 */
class Utils {
	
	/**
	 * Replaces the occurrences of the specified parameter with the specified value in the specified text file.
	 * 
	 * <p>
	 * The file is read and written using UTF-8 encoding.
	 * </p>
	 * 
	 * @param fileName name of the text file to replace the parameter in
	 * @param name parameter name to be replaced
	 * @param value parameter value to replace to
	 * @throws Exception if any error occurs
	 */
	static void replace( final String fileName, final String name, final String value ) throws Exception {
		final Path path = Paths.get( fileName );
		final Path temp = Paths.get( fileName + "_" );
		
		try ( final BufferedReader in = Files.newBufferedReader( path, StandardCharsets.UTF_8 );
		        final BufferedWriter out = Files.newBufferedWriter( temp, StandardCharsets.UTF_8 ) ) {
			
			String line;
			while ( ( line = in.readLine() ) != null )
				out.write( line.replace( name, value ) + "\r\n" );
			
		}
		
		Files.delete( path );
		Files.move( temp, path );
	}
	
}
