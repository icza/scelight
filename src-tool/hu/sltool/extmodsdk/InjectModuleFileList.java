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

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Application which "crawls" the deployment archive and generates and replaces the <code>fileList</code> parameter in the modules bean.
 * 
 * <p>
 * NOTE: Implementation is not performance optimized but rather compact. Exceptions are not handled: it's not our goal.
 * </p>
 * 
 * @author Andras Belicza
 */
public class InjectModuleFileList {
	
	/**
	 * @param args arguments from the running environment; args[0] must be the module xml file, args[1] must be the deployment archive
	 * @throws Exception if any error occurs
	 */
	public static void main( final String[] args ) throws Exception {
		Utils.replace( args[ 0 ], "@fileList@", generateFileListParam( Paths.get( args[ 1 ] ) ) );
		
		System.out.println( "File list injected to file: " + args[ 0 ] );
	}
	
	/**
	 * "Crawls" the deployment archive and generates the <code>fileList</code> parameter value.
	 * 
	 * @param deploymentFile deployment file to process
	 * @return the generated value for the fileList param
	 * @throws Exception if any error occurs
	 */
	private static String generateFileListParam( final Path deploymentFile ) throws Exception {
		// Archive content info
		final StringBuilder sb = new StringBuilder();
		
		try ( final ZipInputStream zipin = new ZipInputStream( Files.newInputStream( deploymentFile ) ) ) {
			ZipEntry ze;
			while ( ( ze = zipin.getNextEntry() ) != null ) {
				if ( ze.isDirectory() )
					continue;
				
				if ( sb.length() > 0 )
					sb.append( "\r\n    " );
				
				sb.append( "<fileList path=\"" ).append( ze.getName() ).append( "\" sha256=\"" );
				sb.append( calculateDigest( zipin, (int) ze.getSize() ) );
				sb.append( "\" v=\"1\"/>" );
			}
		}
		
		return sb.toString();
	}
	
	/**
	 * Calculates the SHA-256 digest of data read from the specified stream.
	 * 
	 * @param input input stream to read data from
	 * @param size number of bytes to read and calculate digest from
	 * @return the calculated digest of the data; or an empty string if the specified number of bytes cannot be read from the stream
	 * @throws Exception if any error occurs
	 */
	private static String calculateDigest( final InputStream input, final int size ) throws Exception {
		int remaining = size;
		final MessageDigest md = MessageDigest.getInstance( "SHA-256" );
		
		final byte[] buffer = new byte[ 16 * 1024 ];
		
		while ( remaining > 0 ) {
			final int bytesRead = input.read( buffer, 0, Math.min( buffer.length, remaining ) );
			if ( bytesRead <= 0 )
				throw new RuntimeException( "Not enough data, still need " + remaining + " bytes!" );
			remaining -= bytesRead;
			md.update( buffer, 0, bytesRead );
		}
		
		final StringBuilder sb = new StringBuilder( 64 );
		for ( final byte b : md.digest() )
			sb.append( String.format( "%02x", b & 0xff ) );
		
		return sb.toString();
	}
	
}
