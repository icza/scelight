/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.sltool.release.balancedata;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.FileVisitOption;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Collections;
import java.util.zip.GZIPOutputStream;

/**
 * Creates balance data packs, packs the unit descriptor XML files into a single GZ file for each balance data pack.
 * 
 * <p>
 * The outputs are GZ files, a single data stream holding a series of the following 2 entries
 * </p>
 * <ol>
 * <li><b>4-byte size value:</b> size of the following XML document, from low bytes to high
 * <li><b>XML document</b>
 * </ol>
 * 
 * @author Andras Belicza
 */
public class CreateBalanceDataPacks {
	
	/**
	 * @param args used to take arguments from the running environment
	 * @throws Exception if any error occurs
	 */
	public static void main( final String[] args ) throws Exception {
		
		// final Path webExportsBaseFolder = Paths.get( "t:/webexport" );
		final Path webExportsBaseFolder = Paths.get( "t:/webexport-MERGED" );
		// final Path webExportsBaseFolder = Paths.get( "t:/webexport-OUT" );
		final Path outputFolder = Paths.get( "w:/" );
		
		// Walk the base folder and create balance data packs for all found versions
		Files.walkFileTree( webExportsBaseFolder, Collections.< FileVisitOption > emptySet(), 1, new SimpleFileVisitor< Path >() {
			@Override
			public FileVisitResult visitFile( final Path file, final BasicFileAttributes attrs ) throws IOException {
				if ( !attrs.isDirectory() )
					return FileVisitResult.CONTINUE;
				
				// Expected folder name format: "webexport-1.0.0.16117"
				final String folderName = file.getFileName().toString();
				Path outFile = outputFolder.resolve( folderName.substring( folderName.indexOf( '-' ) + 1 ) + ".gz" );
				try {
					createBDPack( file, outFile );
				} catch ( final Exception e ) {
					throw new RuntimeException( e );
				}
				
				return FileVisitResult.CONTINUE;
			};
		} );
		
	}
	
	/**
	 * Creates a balance data pack.
	 * 
	 * @param webExportFolder base web export folder
	 * @param outFile output file
	 * @throws Exception if any error occurs
	 */
	public static void createBDPack( final Path webExportFolder, final Path outFile ) throws Exception {
		try ( final GZIPOutputStream out = new GZIPOutputStream( Files.newOutputStream( outFile ) ) ) {
			
			// First add the SC2 strings
			addFile( out, webExportFolder.resolve( "enUS/S2Strings.xml" ) );
			
			Files.walkFileTree( webExportFolder, Collections.< FileVisitOption > emptySet(), 1, new SimpleFileVisitor< Path >() {
				@Override
				public FileVisitResult visitFile( final Path file, final BasicFileAttributes attrs ) throws IOException {
					if ( attrs.isDirectory() )
						return FileVisitResult.CONTINUE;
					
					addFile( out, file );
					
					return FileVisitResult.CONTINUE;
				};
			} );
		}
	}
	
	/**
	 * Adds the specified file to the specified output stream.
	 * 
	 * @param out output stream to write the file to
	 * @param file file to be added
	 * @throws IOException if writing the file results in {@link IOException}
	 */
	private static void addFile( final OutputStream out, final Path file ) throws IOException {
		// Write out file size (4 bytes)
		int size = (int) Files.size( file );
		for ( int i = 0; i < 4; i++, size >>= 8 )
			out.write( size & 0xff );
		
		// Write out file content
		out.write( Files.readAllBytes( file ) );
	}
	
}
