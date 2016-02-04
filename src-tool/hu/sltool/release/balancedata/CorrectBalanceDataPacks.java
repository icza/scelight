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
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitOption;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Collections;
import java.util.List;

/**
 * Corrects a balance data pack folder, replaces unit indices based on a dictionary.
 * 
 * <p>
 * Versions 2.0.3 - 2.0.9 have the same ability codes, but different unit indices.<br>
 * I make a copy of the 2.0.11 balance data pack, replace / swap the unit indices, and I use this pack for all versions in the range of 2.0.3-2.0.9. <br>
 * Source: https://github.com/GraylinKim/sc2reader/blob/master/sc2reader/data/HotS/24764_units.csv<br>
 * <br>
 * Rules are the following:<br>
 * </p>
 * 
 * <table border=1>
 * <tr>
 * <th>2.0.11 indices
 * <th>old indices
 * <tr>
 * <td>0-227
 * <td>good, no need to replace
 * <tr>
 * <td>228-232
 * <td>Missing or non-existent in old versions, LEAVE OUT
 * <tr>
 * <td>233-297
 * <td>SUBTRACT-1
 * <tr>
 * <td>298
 * <td>Missing or non-existent in old versions, LEAVE OUT
 * <tr>
 * <td>299-393
 * <td>SUBTRACT-2
 * <tr>
 * <td>394-409
 * <td>Missing or non-existent in old versions, LEAVE OUT
 * <tr>
 * <td>410-494
 * <td>SUBTRACT-3
 * <tr>
 * <td>495
 * <td>Missing or non-existent in old versions, LEAVE OUT
 * <tr>
 * <td>496-568
 * <td>SUBTRACT-4
 * <tr>
 * <td>569-585
 * <td>Missing or non-existent in old versions, LEAVE OUT
 * <tr>
 * <td>586-589
 * <td>SUBTRACT-6
 * </table>
 * 
 * 
 * <p>
 * Also in all versions before 2.0.9 Banshee Cloacking research cost is replaced from 100/100 to 200/200 (in StarportTechLab.xml). <br>
 * Source: http://wiki.teamliquid.net/starcraft2/Patch_2.0.9
 * </p>
 * 
 * @author Andras Belicza
 */
public class CorrectBalanceDataPacks {
	
	/**
	 * See class doc for description ({@link CorrectBalanceDataPacks}).
	 * 
	 * @param args used to take arguments from the running environment
	 * @throws Exception if any error occurs
	 */
	public static void main( final String[] args ) throws Exception {
		// To swap:
		final Path ROOT = Paths.get( "t:/webexport" );
		final Path[] webExportFolders = { ROOT.resolve( "webexport-2.0.3.24764-PTR" ), ROOT.resolve( "webexport-2.0.4.24944" ),
		        ROOT.resolve( "webexport-2.0.5.25092" ), ROOT.resolve( "webexport-2.0.6.25180" ), ROOT.resolve( "webexport-2.0.7.25293" ),
		        ROOT.resolve( "webexport-2.0.8.25604" ), ROOT.resolve( "webexport-2.0.9.26147" ) };
		
		
		final Path OUT_ROOT = Paths.get( "t:/webexport-OUT" );
		for ( final Path webExportFolder : webExportFolders )
			correctBDPack( webExportFolder, OUT_ROOT.resolve( webExportFolder.getFileName() ) );
	}
	
	/**
	 * Corrects the specified balance data pack.
	 * 
	 * @param webExportFolder base web export folder
	 * @param outFolder output folder
	 * @throws Exception if any error occurs
	 */
	private static void correctBDPack( final Path webExportFolder, final Path outFolder ) throws Exception {
		Files.createDirectories( outFolder.resolve( "enUS" ) );
		
		// First copy strings
		Files.copy( webExportFolder.resolve( "enUS/S2Strings.xml" ), outFolder.resolve( "enUS/S2Strings.xml" ) );
		
		Files.walkFileTree( webExportFolder, Collections.< FileVisitOption > emptySet(), 1, new SimpleFileVisitor< Path >() {
			@Override
			public FileVisitResult visitFile( final Path file, final BasicFileAttributes attrs ) throws IOException {
				if ( attrs.isDirectory() )
					return FileVisitResult.CONTINUE;
				
				processFile( file, outFolder );
				
				return FileVisitResult.CONTINUE;
			};
		} );
	}
	
	/**
	 * Processes the specified unit XML file: converts unit indices.
	 * 
	 * @param file unit XML file to be processed
	 * @param outFolder output folder
	 * @throws IOException if any error occurs
	 */
	private static void processFile( final Path file, final Path outFolder ) throws IOException {
		final List< String > lines = Files.readAllLines( file, StandardCharsets.US_ASCII );
		
		// 1st line: XML header; 2nd line: unit tag; 3rd line: meta tag containing unit index, e.g.:
		// <meta name="280" icon="btn-building-terran-armory" race="Terr" hotkey="389" tooltip="390" source="Liberty.SC2Mod"
		// index="66"/>
		
		String meta = lines.get( 2 );
		final int last = meta.lastIndexOf( '"' ) - 1;
		final int first = meta.lastIndexOf( '"', last ) + 1;
		
		final int unitIdx = Integer.parseInt( meta.substring( first, last + 1 ) );
		
		final Integer convertedIdx = convertUnitIdx( unitIdx );
		if ( convertedIdx == null )
			return; // Exclude this XML file
			
		meta = meta.substring( 0, first ) + convertedIdx + meta.substring( last + 1 );
		lines.set( 2, meta );
		
		if ( file.getFileName().toString().equals( "StarportTechLab.xml" ) && !file.getParent().getFileName().toString().contains( "-2.0.9." ) ) {
			// Replace Banshee Cloacking research cost from 100/100 to 200/200
			for ( int i = 0; i < lines.size(); i++ ) {
				if ( lines.get( i ).trim().startsWith( "<upgrade id=\"BansheeCloak\"" ) ) {
					for ( int j = i + 1;; j++ ) {
						String line = lines.get( j );
						if ( line.trim().startsWith( "<cost " ) ) {
							line = line.replace( "minerals=\"100\"", "minerals=\"200\"" );
							line = line.replace( "vespene=\"100\"", "vespene=\"200\"" );
							lines.set( j, line );
							break;
						}
					}
					break;
				}
			}
		}
		
		Files.write( outFolder.resolve( file.getFileName() ), lines, StandardCharsets.US_ASCII );
	}
	
	/**
	 * Converts the specified unit index from balance data pack 2.0.11 into the balance data packs 2.0.3-2.0.9.
	 * 
	 * @param index unit index to be converted
	 * @return the converted unit index, <code>null</code> if unit (XML) is to be excluded (removed)
	 */
	private static Integer convertUnitIdx( final int index ) {
		if ( index <= 227 )
			return index;
		
		if ( 233 <= index && index <= 297 )
			return index - 1;
		
		if ( 299 <= index && index <= 393 )
			return index - 2;
		
		if ( 410 <= index && index <= 494 )
			return index - 3;
		
		if ( 496 <= index && index <= 568 )
			return index - 4;
		
		if ( 586 <= index && index <= 589 )
			return index - 4;
		
		return null;
	}
	
}
