/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.sltool.release;

import hu.sllauncher.bean.BuildInfoBean;
import hu.sllibs.AConsts;
import hu.sllibs.r.AR;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Date;
import java.util.Properties;

import javax.xml.bind.JAXB;

/**
 * Application which prepares building the next app libs release.
 * 
 * <p>
 * Tasks it does:
 * </p>
 * <ul>
 * <li>Increments the app libs build number.</li>
 * <li>Creates the properties file used by the Ant build script.</li>
 * </ul>
 * 
 * @author Andras Belicza
 */
public class PrepNextAppLibsRelease {
	
	/**
	 * @param args used to take arguments from the running environment; args[0] is the name of the app-libs-build.properties file to generate for Ant
	 * @throws Exception if any error occurs
	 */
	public static void main( final String[] args ) throws Exception {
		final String BUILD_INFO_FILE = "/hu/sllibs/r/bean/build-info.xml";
		
		// Read current app-libs build info
		final BuildInfoBean b = JAXB.unmarshal( AR.class.getResource( BUILD_INFO_FILE ), BuildInfoBean.class );
		System.out.println( "Current: " + b );
		
		// Increment build
		b.setBuild( b.getBuildNumber() + 1 );
		b.setDate( new Date() );
		System.out.println( "New: " + b );
		
		// Archive to app-libs-build-history.txt and save new build info
		// to both src-app-libs and bin-app-libs folders (so no refresh is required in Eclipse)
		try ( final BufferedWriter out = Files.newBufferedWriter( Paths.get( "dev-data", "app-libs-build-history.txt" ), Charset.forName( "UTF-8" ),
		        StandardOpenOption.APPEND ) ) {
			out.write( b.getBuildNumber() + " " + b.getDate() );
			out.newLine();
		}
		JAXB.marshal( b, Paths.get( "src-app-libs", BUILD_INFO_FILE ).toFile() );
		JAXB.marshal( b, Paths.get( "bin-app-libs", BUILD_INFO_FILE ).toFile() );
		
		// Create properties file for Ant
		final Properties p = new Properties();
		p.setProperty( "appLibsVer", AConsts.APP_LIBS_VERSION.toString() );
		p.setProperty( "appLibsBuildNumber", b.getBuildNumber().toString() );
		try ( final FileOutputStream out = new FileOutputStream( args[ 0 ] ) ) {
			p.store( out, null );
		}
	}
	
}
