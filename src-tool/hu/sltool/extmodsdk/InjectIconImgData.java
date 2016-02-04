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

import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Application which which encodes the icon file to base64 and replaces the <code>iconImgData</code> param in the external module manifest.
 * 
 * <p>
 * NOTE: Implementation is not performance optimized but rather compact. Exceptions are not handled: it's not our goal.
 * </p>
 * 
 * @author Andras Belicza
 */
public class InjectIconImgData {
	
	/**
	 * @param args arguments from the running environment; args[0] must be the external module manifest file, args[1] must be the icon file
	 * @throws Exception if any error occurs
	 */
	public static void main( final String[] args ) throws Exception {
		Utils.replace( args[ 0 ], "@iconImgData@", javax.xml.bind.DatatypeConverter.printBase64Binary( Files.readAllBytes( Paths.get( args[ 1 ] ) ) ) );
		
		System.out.println( "Icon image data injected to file: " + args[ 0 ] );
	}
	
}
