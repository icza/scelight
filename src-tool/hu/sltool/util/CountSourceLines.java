/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.sltool.util;

import java.io.IOException;
import java.io.LineNumberReader;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * Application which counts files and lines, and writes the result to a file.
 * 
 * @author Andras Belicza
 */
public class CountSourceLines {
	
	/** Set of binary file extensions. */
	private static final Set< String > BINARY_EXT_SET = new HashSet<>( Arrays.asList( "gif", "png", "jpg", "rsa", "mp3", "jar", "ico", "dll", "class", "dds",
	                                                          "gz" ) );
	
	/**
	 * Line count statistics.
	 * 
	 * @author Andras Belicza
	 */
	private static class Stat implements Comparable< Stat > {
		
		/** File extension. */
		public final String ext;
		
		/** Total files. */
		public int          files;
		
		/** Total size. */
		public long         size;
		
		/** Total lines. */
		public int          lines;
		
		
		/**
		 * Creates a new {@link Stat}.
		 * 
		 * @param ext file extension
		 */
		public Stat( final String ext ) {
			this.ext = ext;
		}
		
		/**
		 * Adds the specified stat into this.
		 * 
		 * @param s stats to be added
		 */
		public void add( final Stat s ) {
			files += s.files;
			size += s.size;
			lines += s.lines;
		}
		
		@Override
		public int compareTo( final Stat s ) {
			// We want descendant order
			int result = s.lines - lines;
			if ( result == 0 )
				result = s.files - files;
			if ( result == 0 )
				result = Long.compare( s.size, size );
			if ( result == 0 )
				result = s.ext.compareTo( ext );
			return result;
		}
		
	}
	
	/**
	 * @param args used to take arguments from the running environment - not used here
	 * @throws Exception if any error occurs
	 */
	public static void main( final String[] args ) throws Exception {
		final String outputFile = "dev-data/source-stats/" + new SimpleDateFormat( "yyyy-MM-dd HH_mm_ss" ).format( new Date() ) + ".txt";
		System.out.println( "Writing stats to file: " + outputFile );
		
		try ( final PrintStream out = new PrintStream( Files.newOutputStream( Paths.get( outputFile ) ) ) ) {
			System.setOut( out );
			
			System.out.println( "File: " + outputFile );
			
			sourceStats( args );
			
			binStats( args );
			
			out.flush();
		}
	}
	
	/**
	 * Calculates source code statistics.
	 * 
	 * @param args used to take arguments from the running environment - not used here
	 * @throws Exception if any error occurs
	 */
	public static void sourceStats( final String[] args ) throws Exception {
		// Global statistics map
		final Map< String, Stat > globalExtStatMap = new TreeMap<>();
		
		final String[] folders = { "src-launcher", "src-sc2-textures", "src-sc2-balance-data", "src-ext-mod-api", "src-app-libs", "src-app", "app-folder",
		        "src-tool", "build.xml", "README.md", "war/news", "release/resources/starter-scripts-raw", "../ScelightOp/src" };
		
		for ( final String folder : folders ) {
			// Statistics map
			final Map< String, Stat > extStatMap = new TreeMap<>();
			
			countSources( Paths.get( folder ), extStatMap );
			
			// Calculate folder ALL
			final Stat folderAllStat = new Stat( "<ALL>" );
			for ( final Stat stat : extStatMap.values() )
				folderAllStat.add( stat );
			extStatMap.put( folderAllStat.ext, folderAllStat );
			
			printStats( folder, extStatMap );
			
			// Add to global stats
			for ( final Stat stat : extStatMap.values() ) {
				Stat globalStat = globalExtStatMap.get( stat.ext );
				if ( globalStat == null )
					globalExtStatMap.put( stat.ext, globalStat = new Stat( stat.ext ) );
				globalStat.add( stat );
			}
			
			if ( "app-folder".equals( folder ) )
				printStats( "SCELIGHT SOURCE STATS (so far)", globalExtStatMap );
		}
		
		printStats( "PROJECT SOURCE STATS", globalExtStatMap );
	}
	
	/**
	 * Counts source lines in the specified path.
	 * 
	 * @param folder folder to count in
	 * @param extStatMap statistics map
	 * @throws Exception if any error occurs
	 */
	private static void countSources( final Path folder, final Map< String, Stat > extStatMap ) throws Exception {
		Files.walkFileTree( folder, new SimpleFileVisitor< Path >() {
			@Override
			public FileVisitResult visitFile( final Path file, final BasicFileAttributes attrs ) throws IOException {
				final String name = file.getFileName().toString();
				final String ext = name.lastIndexOf( '.' ) < 0 ? "<no-ext>" : name.substring( name.lastIndexOf( '.' ) + 1 );
				
				Stat stat = extStatMap.get( ext );
				if ( stat == null )
					extStatMap.put( ext, stat = new Stat( ext ) );
				
				stat.files++;
				stat.size += attrs.size();
				
				if ( BINARY_EXT_SET.contains( ext ) )
					return FileVisitResult.CONTINUE;
				
				try ( final LineNumberReader in = new LineNumberReader( Files.newBufferedReader( file, StandardCharsets.UTF_8 ) ) ) {
					in.skip( Long.MAX_VALUE );
					stat.lines += in.getLineNumber();
				} catch ( final Exception e ) {
					System.err.println( "ERROR IN FILE: " + file );
					throw e;
				}
				
				return FileVisitResult.CONTINUE;
			}
		} );
	}
	
	/**
	 * Calculates compiled binary code statistics.
	 * 
	 * @param args used to take arguments from the running environment - not used here
	 * @throws Exception if any error occurs
	 */
	public static void binStats( final String[] args ) throws Exception {
		// Global statistics map
		final Map< String, Stat > globalExtStatMap = new TreeMap<>();
		
		final String[] folders = { "bin-launcher", "bin-sc2-textures", "bin-sc2-balance-data", "bin-ext-mod-api", "bin-app-libs", "bin-app", "bin-tool",
		        "../ScelightOp/war/WEB-INF/classes" };
		
		for ( final String folder : folders ) {
			// Statistics map
			final Map< String, Stat > extStatMap = new TreeMap<>();
			
			countClasses( Paths.get( folder ), extStatMap );
			
			// Calculate folder ALL
			final Stat folderAllStat = new Stat( "<ALL>" );
			for ( final Stat stat : extStatMap.values() )
				folderAllStat.add( stat );
			extStatMap.put( folderAllStat.ext, folderAllStat );
			
			printStats( folder, extStatMap );
			
			// Add to global stats
			for ( final Stat stat : extStatMap.values() ) {
				Stat globalStat = globalExtStatMap.get( stat.ext );
				if ( globalStat == null )
					globalExtStatMap.put( stat.ext, globalStat = new Stat( stat.ext ) );
				globalStat.add( stat );
			}
			
			if ( "bin-app".equals( folder ) )
				printStats( "SCELIGHT BINARY STATS (so far)", globalExtStatMap );
		}
		
		printStats( "PROJECT BINARY STATS", globalExtStatMap );
	}
	
	/**
	 * Counts class files in the specified path.
	 * 
	 * @param folder folder to count in
	 * @param extStatMap statistics map
	 * @throws Exception if any error occurs
	 */
	private static void countClasses( final Path folder, final Map< String, Stat > extStatMap ) throws Exception {
		Files.walkFileTree( folder, new SimpleFileVisitor< Path >() {
			@Override
			public FileVisitResult visitFile( final Path file, final BasicFileAttributes attrs ) throws IOException {
				final String name = file.getFileName().toString();
				if ( !name.endsWith( ".class" ) )
					return FileVisitResult.CONTINUE;
				
				String ext = "class";
				for ( int idx = 0; idx < name.length(); )
					if ( ( idx = name.indexOf( '$', idx ) + 1 ) > 0 ) // Found another '$'
						ext += '$'; // Increase class depth
					else
						break;
				
				Stat stat = extStatMap.get( ext );
				if ( stat == null )
					extStatMap.put( ext, stat = new Stat( ext ) );
				
				stat.files++;
				stat.size += attrs.size();
				
				return FileVisitResult.CONTINUE;
			}
		} );
	}
	
	/**
	 * Prints the specified stats.
	 * 
	 * @param title title to be printed first
	 * @param extStatMap stats map to be printed
	 */
	private static void printStats( final String title, final Map< String, Stat > extStatMap ) {
		System.out.println( "\n_______________________________________________" );
		System.out.println( title );
		
		final List< Stat > statList = new ArrayList<>( extStatMap.values() );
		Collections.sort( statList );
		
		System.out.printf( "%13s  %9s%12s%11s\n", "", "Files", "Size", "Lines" );
		for ( final Stat stat : statList ) {
			System.out.printf( Locale.US, "%13s: %,9d%,12d%,11d\n", stat.ext, stat.files, stat.size, stat.lines );
		}
	}
	
}
