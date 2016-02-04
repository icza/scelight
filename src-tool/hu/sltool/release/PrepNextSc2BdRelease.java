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
import hu.slsc2balancedata.BConsts;
import hu.slsc2balancedata.r.BR;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.FileVisitOption;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Collections;
import java.util.Date;
import java.util.Properties;

import javax.xml.bind.JAXB;

/**
 * Application which prepares building the next sc2 balance data release.
 * 
 * <p>
 * Tasks it does:
 * </p>
 * <ul>
 * <li>Increments the SC2 balance data build number.</li>
 * <li>Creates the properties file used by the Ant build script.</li>
 * <li>Creates a balance data resource list file named <code>"list.txt"</code> next to the {@link BR} class.
 * </ul>
 * 
 * @author Andras Belicza
 */
public class PrepNextSc2BdRelease {
	
	/**
	 * @param args used to take arguments from the running environment; args[0] is the name of the sc2-bd-build.properties file to generate for Ant
	 * @throws Exception if any error occurs
	 */
	public static void main( final String[] args ) throws Exception {
		final String BUILD_INFO_FILE = "/hu/slsc2balancedata/r/bean/build-info.xml";
		final String RESOURCE_LIST_FILE = "/hu/slsc2balancedata/r/list.txt";
		
		// Read current sc2-bd build info
		final BuildInfoBean b = JAXB.unmarshal( BR.class.getResource( BUILD_INFO_FILE ), BuildInfoBean.class );
		System.out.println( "Current: " + b );
		
		// Increment build
		b.setBuild( b.getBuildNumber() + 1 );
		b.setDate( new Date() );
		System.out.println( "New: " + b );
		
		// Archive to sc2-t-build-history.txt and save new build info
		// to both src-sc2-balance-data and bin-sc2-balance-data folders (so no refresh is required in Eclipse)
		try ( final BufferedWriter out = Files.newBufferedWriter( Paths.get( "dev-data", "sc2-bd-build-history.txt" ), Charset.forName( "UTF-8" ),
		        StandardOpenOption.APPEND ) ) {
			out.write( b.getBuildNumber() + " " + b.getDate() );
			out.newLine();
		}
		final Path buildInfoFilePath = Paths.get( "src-sc2-balance-data", BUILD_INFO_FILE );
		JAXB.marshal( b, buildInfoFilePath.toFile() );
		Files.copy( buildInfoFilePath, Paths.get( "bin-sc2-balance-data", BUILD_INFO_FILE ), StandardCopyOption.REPLACE_EXISTING );
		
		// Create properties file for Ant
		final Properties p = new Properties();
		p.setProperty( "sc2BdVer", BConsts.SC2_BALANCE_DATA_VERSION.toString() );
		p.setProperty( "sc2BdBuildNumber", b.getBuildNumber().toString() );
		try ( final FileOutputStream out = new FileOutputStream( args[ 0 ] ) ) {
			p.store( out, null );
		}
		
		// Create balance data resource list file
		final Path resourceListFilePath = Paths.get( "src-sc2-balance-data", RESOURCE_LIST_FILE );
		try ( final PrintWriter out = new PrintWriter( resourceListFilePath.toFile(), "UTF-8" ) ) {
			Files.walkFileTree( resourceListFilePath.getParent(), Collections.< FileVisitOption > emptySet(), 1, new SimpleFileVisitor< Path >() {
				@Override
				public FileVisitResult visitFile( final Path file, final BasicFileAttributes attrs ) throws IOException {
					final String fileName = file.getFileName().toString();
					if ( attrs.isDirectory() || !fileName.endsWith( ".gz" ) )
						return FileVisitResult.CONTINUE;
					
					out.println( fileName );
					
					return FileVisitResult.CONTINUE;
				};
			} );
		}
		Files.copy( resourceListFilePath, Paths.get( "bin-sc2-balance-data", RESOURCE_LIST_FILE ), StandardCopyOption.REPLACE_EXISTING );
	}
	
}
