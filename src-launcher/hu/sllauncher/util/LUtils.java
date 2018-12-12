/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.sllauncher.util;

import hu.sllauncher.bean.reginfo.SysInfoBean;
import hu.sllauncher.gui.icon.LIcons;
import hu.sllauncher.gui.icon.LRIcon;
import hu.sllauncher.service.env.LEnv;
import hu.sllauncher.service.env.OpSys;
import hu.sllauncher.service.settings.LSettings;

import java.awt.Desktop;
import java.io.IOException;
import java.io.InputStream;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.security.MessageDigest;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.TimeZone;
import java.util.Vector;

/**
 * General utilities and constants for the launcher.
 * 
 * @author Andras Belicza
 */
public class LUtils {
	
	/** Milliseconds in a minute. */
	public static final long   MS_IN_MIN   = 60L * 1000;
	
	/** Milliseconds in an hour. */
	public static final long   MS_IN_HOUR  = MS_IN_MIN * 60;
	
	/** Milliseconds in a day. */
	public static final long   MS_IN_DAY   = MS_IN_HOUR * 24;
	
	/** Milliseconds in a week. */
	public static final long   MS_IN_WEEK  = MS_IN_DAY * 7;
	
	/**
	 * Milliseconds in a month.<br>
	 * Approximated by 31 days.
	 */
	public static final long   MS_IN_MONTH = MS_IN_DAY * 31;
	
	/**
	 * Milliseconds in a week.<br>
	 * Approximated by 365 days.
	 */
	public static final long   MS_IN_YEAR  = MS_IN_DAY * 365;
	
	
	/** Digits used in the hexadecimal representation. */
	public static final char[] HEX_DIGITS  = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
	
	/**
	 * Tests if the specified int value is contained in the specified int array.
	 * 
	 * @param array array to search in
	 * @param v value to be looked for
	 * @return true if the int value is contained in the specified int array; false otherwise
	 */
	public static boolean contains( final int[] array, final int v ) {
		for ( final int e : array )
			if ( e == v )
				return true;
		
		return false;
	}
	
	/**
	 * Tests if the specified value is contained in the specified array.
	 * 
	 * @param <E> type of the value and values
	 * @param array array to search in
	 * @param v value to be looked for
	 * @return true if the value is contained in the specified array; false otherwise
	 */
	public static < E > boolean contains( final E[] array, final E v ) {
		// Separate logic based on whether v is null (so we don't have to test it in every iteration)
		if ( v == null ) {
			for ( final E e : array )
				if ( e == null )
					return true;
		} else {
			for ( final E e : array )
				if ( e == v || v.equals( e ) )
					return true;
		}
		
		return false;
	}
	
	/**
	 * Tries to create a {@link URL} object from the specified URL spec.
	 * 
	 * @param spec URL spec to create an {@link URL} from
	 * @return a {@link URL} instance if the URL spec is valid; the URL spec (as {@link String}) otherwise
	 */
	public static Object tryMakingUrl( final String spec ) {
		try {
			return new URL( spec );
		} catch ( final MalformedURLException mue ) {
			return spec;
		}
	}
	
	/**
	 * Returns the minimal capacity of hash-based structures (e.g. {@link HashSet} or {@link HashMap}) calculated from the specified size (number of elements)
	 * and the default load factor (which is 0.75).
	 * 
	 * @param size size to calculate the minimal capacity
	 * @return the minimal capacity of hash-based structures for the specified size
	 */
	public static int hashCapacityForSize( final int size ) {
		return size * 4 / 3 + 1;
	}
	
	/**
	 * Returns a new {@link HashMap} initialized with a large enough capacity to allow adding <code>size</code> elements without causing a rehash.
	 * 
	 * @param <K> type of the keys
	 * @param <V> type of the values
	 * @param size number of elements to accommodate without a rehash
	 * @return a new {@link HashMap} with a large enough capacity to accommodate <code>size</code> elements without a rehash
	 */
	public static < K, V > HashMap< K, V > newHashMap( final int size ) {
		return new HashMap<>( hashCapacityForSize( size ) );
	}
	
	/**
	 * Returns a new {@link HashSet} initialized with a large enough capacity to allow adding <code>size</code> elements without causing a rehash.
	 * 
	 * @param <E> type of the elements
	 * @param size number of elements to accommodate without a rehash
	 * @return a new {@link HashMap} with a large enough capacity to accommodate <code>size</code> elements without a rehash
	 */
	public static < E > HashSet< E > newHashSet( final int size ) {
		return new HashSet<>( hashCapacityForSize( size ) );
	}
	
	/**
	 * Returns a new, independent {@link Vector} which contains all the specified elements.
	 * <p>
	 * The returned vector will have a minimum capacity of 10.
	 * </p>
	 * 
	 * @param <E> type of the elements
	 * @param elements elements to be added to the new list
	 * @return an independent {@link Vector} which does not rely on the <code>elements</code> array
	 * 
	 * @see #vector(Object...)
	 * @see #vectort(Object...)
	 */
	public static < E > Vector< E > asNewVector( @SuppressWarnings( "unchecked" ) final E... elements ) {
		final Vector< E > vector = new Vector<>( Math.max( 10, elements.length ) );
		Collections.addAll( vector, elements );
		return vector;
	}
	
	/**
	 * Returns a new, independent {@link Set} which contains all the specified elements.
	 * 
	 * @param <E> type of the elements
	 * @param elements elements to be added to the new set
	 * @return an independent {@link Set} which does not rely on the <code>elements</code> array
	 */
	public static < E > Set< E > asNewSet( @SuppressWarnings( "unchecked" ) final E... elements ) {
		final Set< E > set = newHashSet( elements.length );
		Collections.addAll( set, elements );
		return set;
	}
	
	/**
	 * Creates a new {@link URL} from the specified URL spec.
	 * 
	 * <p>
	 * Primary goal of this factory method is to suppress the {@link MalformedURLException} that comes with the {@link URL}'s constructor, it is thrown in a
	 * "shadowed" manner, wrapped in an {@link IllegalArgumentException} (which is a {@link RuntimeException}).
	 * </p>
	 * 
	 * @param spec URL spec to create a {@link URL} from
	 * @return a new {@link URL} from the specified URL spec
	 * @throws IllegalArgumentException if the specified URL spec is a malformed URL
	 * 
	 * @see #createUrl(URL, String)
	 * @see URL#URL(String)
	 */
	public static URL createUrl( final String spec ) throws IllegalArgumentException {
		return createUrl( null, spec );
	}
	
	/**
	 * Creates a new {@link URL} from the specified URL context and spec.
	 * 
	 * <p>
	 * Primary goal of this factory method is to suppress the {@link MalformedURLException} that comes with the {@link URL}'s constructor, it is thrown in a
	 * "shadowed" manner, wrapped in an {@link IllegalArgumentException} (which is a {@link RuntimeException}).
	 * </p>
	 * 
	 * @param context URL context in which to interpret the URL spec
	 * @param spec URL spec to create a {@link URL} from
	 * @return a new {@link URL} from the specified URL spec
	 * @throws IllegalArgumentException if the specified URL spec is a malformed URL
	 * 
	 * @see #createUrl(String)
	 * @see URL#URL(URL, String)
	 */
	public static URL createUrl( final URL context, final String spec ) throws IllegalArgumentException {
		try {
			return new URL( context, spec );
		} catch ( final MalformedURLException mue ) {
			throw new RuntimeException( "Malformed URL: " + spec, mue );
		}
	}
	
	/**
	 * Returns an HTML link text constructed from the specified text.
	 * 
	 * @param text displayable link text
	 * @return an HTML link text constructed from the specified text
	 */
	public static String htmlLinkText( final String text ) {
		return text == null ? null : "<html><a href=\"#\">" + LUtils.safeForHtml( text ) + "</a></html>";
	}
	
	/**
	 * Returns a proper tool tip text for the specified url.
	 * 
	 * @param url {@link URL} to return a tool tip text for
	 * @return a proper tool tip text for the specified url
	 */
	public static String urlToolTip( final URL url ) {
		if ( url == null || !LEnv.LAUNCHER_SETTINGS.get( LSettings.DISPLAY_LINK_TOOL_TIPS ) )
			return null;
		
		return "<html>" + ( "mailto".equals( url.getProtocol() ) ? "Write e-mail to <a href=\"#\">" + url.getPath() : "Open <a href=\"#\">" + url )
		        + "</a></html>";
	}
	
	/**
	 * Returns a proper ricon for the specified url.
	 * 
	 * @param url {@link URL} to return an icon for
	 * @return a proper ricon for the specified url
	 */
	public static LRIcon urlIcon( final URL url ) {
		return url == null ? null : "mailto".equals( url.getProtocol() ) ? LIcons.F_MAIL_AT_SIGN : LIcons.F_APPLICATION_BROWSER;
	}
	
	/**
	 * Creates and returns a vector of objects from the specified elements.
	 * 
	 * @param elements elements to add to the vector
	 * @return the vector containing the specified elements
	 * 
	 * @see #vectort(Object...)
	 * @see #asNewVector(Object...)
	 */
	public static Vector< Object > vector( final Object... elements ) {
		final Vector< Object > v = new Vector<>( elements.length );
		Collections.addAll( v, elements );
		return v;
	}
	
	/**
	 * Creates and returns a vector from the specified elements.
	 * 
	 * @param <E> type of the elements
	 * @param elements elements to add to the vector
	 * @return the vector containing the specified elements
	 * 
	 * @see #vector(Object...)
	 * @see #asNewVector(Object...)
	 */
	@SafeVarargs
	public static < E > Vector< E > vectort( final E... elements ) {
		final Vector< E > v = new Vector<>( elements.length );
		Collections.addAll( v, elements );
		return v;
	}
	
	/**
	 * Prepares a text for HTML rendering, to be included in HTML text.
	 * 
	 * @param text text to be prepared
	 * @return the prepared text safe for HTML rendering
	 */
	public static String safeForHtml( final String text ) {
		return text.replace( "\"", "&quot" ).replace( "'", "&#39;" ).replace( "<", "&lt;" ).replace( ">", "&gt;" ).replace( " ", "&nbsp;" )
		        .replace( "\n", "<br>" ).replace( "\t", "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" );
	}
	
	/**
	 * Deletes the specified path, recursively.
	 * 
	 * @param path path to be deleted
	 * @return true if deletion was successful; false otherwise
	 */
	public static boolean deletePath( final Path path ) {
		if ( !Files.exists( path ) )
			return true;
		
		try {
			Files.walkFileTree( path, new SimpleFileVisitor< Path >() {
				@Override
				public FileVisitResult visitFile( final Path file, final BasicFileAttributes attrs ) throws IOException {
					Files.delete( file );
					return FileVisitResult.CONTINUE;
				}
				
				@Override
				public FileVisitResult postVisitDirectory( final Path dir, final IOException exc ) throws IOException {
					Files.delete( dir );
					return FileVisitResult.CONTINUE;
				}
			} );
		} catch ( final IOException ie ) {
			LEnv.LOGGER.error( "Failed to delete file or folder!", ie );
			return false;
		}
		
		return true;
	}
	
	/**
	 * Returns the system info.
	 * 
	 * @return the system info
	 */
	public static SysInfoBean getSysInfo() {
		final SysInfoBean sysInfo = new SysInfoBean();
		
		final OperatingSystemMXBean bean = ManagementFactory.getOperatingSystemMXBean();
		sysInfo.setOsName( bean.getName() );
		sysInfo.setOsVersion( bean.getVersion() );
		sysInfo.setAvailProcs( bean.getAvailableProcessors() );
		
		sysInfo.setUserName( System.getProperty( "user.name" ) );
		sysInfo.setUserCountry( System.getProperty( "user.country" ) );
		// Set time zone using TimeZone class, "user.timezone" property is not guaranteed to be present
		// (See javadoc of System.getProperties())
		sysInfo.setUserTimeZone( TimeZone.getDefault().getID() );
		
		sysInfo.setDate( new Date() );
		
		try {
			// On windows "/" is the root of the current drive, so use "c:/" instead
			sysInfo.setMainRootSize( Files.getFileStore( Paths.get( LEnv.OS == OpSys.WINDOWS ? "c:/" : "/" ) ).getTotalSpace() );
		} catch ( final IOException ie ) {
			LEnv.LOGGER.error( "Failed to get main root size!", ie );
		}
		
		return sysInfo;
	}
	
	/**
	 * Opens the web page specified by the URL in the system's default browser.
	 * 
	 * @param url {@link URL} to be opened
	 */
	public void showURLInBrowser( final URL url ) {
		try {
			if ( Desktop.isDesktopSupported() )
				try {
					Desktop.getDesktop().browse( url.toURI() );
					return;
				} catch ( final Exception e ) {
					// If default method fails, we try our own method, so ignore this.
				}
			
			// Desktop failed, try our own method
			String[] cmdArray = null;
			if ( LEnv.OS == OpSys.WINDOWS ) {
				cmdArray = new String[] { "rundll32", "url.dll,FileProtocolHandler", url.toString() };
			} else {
				// Linux
				final String[] browsers = { "xdg-open", "firefox", "google-chrome", "opera", "konqueror", "epiphany", "mozilla", "netscape" };
				for ( final String browser : browsers )
					if ( Runtime.getRuntime().exec( new String[] { "which", browser } ).waitFor() == 0 ) {
						cmdArray = new String[] { browser, url.toString() };
						break;
					}
			}
			
			if ( cmdArray != null )
				Runtime.getRuntime().exec( cmdArray );
			
		} catch ( final Exception e ) {
			LEnv.LOGGER.info( "Failed to open URL: " + url, e );
		}
	}
	
	/**
	 * Opens the specified file or folder in the default file browser application of the user's OS.
	 * <p>
	 * If a file is specified, on Windows it will also be selected.
	 * </p>
	 * 
	 * @param path file or folder to be opened
	 */
	public static void showPathInFileBrowser( final Path path ) {
		try {
			final boolean isFolder = Files.isDirectory( path );
			final boolean isFile = !isFolder;
			
			if ( isFile && LEnv.OS == OpSys.WINDOWS ) {
				// On Windows we have a way to not just open but also select the replay file; source:
				// http://stackoverflow.com/questions/7357969/how-to-use-java-code-to-open-windows-file-explorer-and-highlight-the-specified-f
				// http://support.microsoft.com/kb/152457
				// It should be "explorer.exe /select,c:\dir\filename.ext", but if (double) spaces are in the file name, it
				// doesn't work, so I use it this way (which also works): "explorer.exe" "/select," "c:\dir\filename.ext"
				new ProcessBuilder( "explorer.exe", "/select,", path.toAbsolutePath().toString() ).start();
			} else
				Desktop.getDesktop().open( isFolder ? path.toFile() : path.getParent().toFile() ); // If not folder it surely has
				                                                                                   // a parent
		} catch ( final IOException ie ) {
			LEnv.LOGGER.warning( "Failed to open file browser!", ie );
		}
	}
	
	/**
	 * Calculates the SHA-256 digest of a file.
	 * 
	 * @param file file whose SHA-256 to be calculated
	 * @return the calculated SHA-256 digest of the file; or an empty string if the file cannot be read
	 */
	public static String calculateFileSha256( final Path file ) {
		return calculateFileDigest( "SHA-256", file );
	}
	
	/**
	 * Calculates the SHA-256 digest of data read from a stream.
	 * 
	 * <p>
	 * Note: the <code>size</code> parameter is of type <code>long</code> for convenience but is treated as <code>int</code> which means its value cannot be
	 * greater than {@link Integer#MAX_VALUE}.
	 * </p>
	 * 
	 * @param input input stream to read data from
	 * @param size number of bytes to read and calculate digest from
	 * @return the calculated digest of the data; or an empty string if the specified number of bytes cannot be read from the stream
	 */
	public static String calculateStreamSha256( final InputStream input, final long size ) {
		return calculateStreamDigest( "SHA-256", input, size );
	}
	
	/**
	 * Calculates the specified digest of a file.
	 * 
	 * @param algorithm digest algorithm to use
	 * @param file file whose digest to be calculated
	 * @return the calculated digest of the file; or an empty string if the file cannot be read
	 */
	public static String calculateFileDigest( final String algorithm, final Path file ) {
		try ( final InputStream input = Files.newInputStream( file ) ) {
			return calculateStreamDigest( algorithm, input, Files.size( file ) );
		} catch ( final IOException ie ) {
			LEnv.LOGGER.debug( "Failed to read file: " + file, ie );
			return "";
		}
	}
	
	/**
	 * Calculates the specified digest of data read from a stream.
	 * 
	 * <p>
	 * Note: the <code>size</code> parameter is of type <code>long</code> for convenience but is treated as <code>int</code> which means its value cannot be
	 * greater than {@link Integer#MAX_VALUE}.
	 * </p>
	 * 
	 * @param algorithm digest algorithm to use
	 * @param input input stream to read data from
	 * @param size number of bytes to read and calculate digest from
	 * @return the calculated digest of the data; or an empty string if the specified number of bytes cannot be read from the stream
	 */
	public static String calculateStreamDigest( final String algorithm, final InputStream input, final long size ) {
		try {
			int remaining = (int) size;
			final MessageDigest md = MessageDigest.getInstance( algorithm );
			
			final byte[] buffer = new byte[ Math.min( 16 * 1024, remaining ) ];
			
			while ( remaining > 0 ) {
				final int bytesRead = input.read( buffer, 0, Math.min( buffer.length, remaining ) );
				if ( bytesRead <= 0 )
					throw new RuntimeException( "Not enough data, still need " + remaining + " bytes!" );
				remaining -= bytesRead;
				md.update( buffer, 0, bytesRead );
			}
			
			return toHexString( md.digest() );
		} catch ( final Exception e ) {
			LEnv.LOGGER.info( "Failed to calculate " + algorithm + " digest.", e );
			return "";
		}
	}
	
	/**
	 * Converts the specified data to hex string.
	 * 
	 * @param data data to be converted
	 * 
	 * @return the specified data converted to hex string
	 * 
	 * @see #toHexString(byte[], int, int)
	 */
	public static String toHexString( final byte[] data ) {
		return toHexString( data, 0, data.length );
	}
	
	/**
	 * Converts the specified data to hex string.
	 * 
	 * @param data data to be converted
	 * @param offset offset of the first byte to be convert
	 * @param length number of bytes to be converted
	 * 
	 * @return the specified data converted to hex string
	 * 
	 * @see #toHexString(byte[])
	 */
	public static String toHexString( final byte[] data, int offset, int length ) {
		final StringBuilder hexBuilder = new StringBuilder( length * 2 );
		
		for ( length += offset; offset < length; offset++ ) {
			final byte b = data[ offset ];
			hexBuilder.append( HEX_DIGITS[ ( b & 0xff ) >> 4 ] ).append( HEX_DIGITS[ b & 0x0f ] );
		}
		
		return hexBuilder.toString();
	}
	
	/**
	 * Checks if the specified text contains the specified search text, performing a case-insensitive search.
	 * 
	 * @param text text to search in
	 * @param searchText text to be searched
	 * @return true if the specified text contains the specified search text, performing a case-insensitive search; false otherwise
	 */
	public static boolean containsIngoreCase( final String text, final String searchText ) {
		if ( text == null || searchText == null )
			return false;
		
		final int searchLength = searchText.length();
		if ( searchLength == 0 )
			return true;
		
		final char firstCharLow = Character.toLowerCase( searchText.charAt( 0 ) );
		final char firstCharUpp = Character.toUpperCase( searchText.charAt( 0 ) );
		
		for ( int i = text.length() - searchLength; i >= 0; i-- ) {
			// A quick pre-check before calling the more expensive String.regionMatches() method:
			final char ch = text.charAt( i );
			if ( ch != firstCharLow && ch != firstCharUpp )
				continue;
			
			if ( text.regionMatches( true, i, searchText, 0, searchLength ) )
				return true;
		}
		
		return false;
	}
	
	/**
	 * Returns a formatted string based on the specified format string and the specified count.
	 * <p>
	 * The format string must contain a <code>%s</code> conversion where the formatted <code>count</code> will be inserted and another <code>%s</code>
	 * conversion where the optional plural sign (<code>"s"</code>) will be inserted if count is not equal to 1.
	 * </p>
	 * 
	 * <p>
	 * Example use case: <blockquote>
	 * 
	 * <pre>
	 * plural( &quot;Are you sure you want to delete %s replay%s?&quot;, count )
	 * </pre>
	 * 
	 * </blockquote>
	 * </p>
	 * 
	 * @param format format string
	 * @param count count to be inserted and to decide whether plural or singular form is to be used
	 * @return a formatted string based on the specified format string and count
	 */
	public static String plural( final String format, final long count ) {
		return String.format( format, LEnv.LANG.formatNumber( count ), count == 1 ? "" : "s" );
	}
	
	/**
	 * Converts a const name to a normal, human readable format.
	 * 
	 * <p>
	 * <b>Converter rules:</b>
	 * </p>
	 * <ul>
	 * <li>The output will start with an upper-cased letter if input starts with a letter (regardless of word capitalization).
	 * <li>Word boundaries are defined by underscore characters (<code>'_'</code>). Underscores are replaced with spaces.
	 * <li>First letters of words will be capitalized based on the <code>capitalizeWords</code> parameter.
	 * <li>The rest of the characters will be lower-cased.
	 * </ul>
	 * 
	 * <p>
	 * Examples when capitalizing words:
	 * </p>
	 * <ul>
	 * <li><code>"DETAILS"</code> = &gt; <code>"Details"</code></p>
	 * <li><code>"INIT_DATA"</code> = &gt; <code>"Init Data"</code></p>
	 * <li><code>"sT_raN_Ge"</code> = &gt; <code>"St Ran Ge"</code></p>
	 * </ul>
	 * 
	 * <p>
	 * Examples when not capitalizing words:
	 * </p>
	 * <ul>
	 * <li><code>"DETAILS"</code> = &gt; <code>"Details"</code></p>
	 * <li><code>"INIT_DATA"</code> = &gt; <code>"Init data"</code></p>
	 * <li><code>"sT_raN_Ge"</code> = &gt; <code>"St ran ge"</code></p>
	 * </ul>
	 * 
	 * @param text text to be converted
	 * @param capitalizeWords if true, all words will start with capital letters
	 * @return the converted text
	 */
	public static String constNameToNormal( final String text, final boolean capitalizeWords ) {
		final StringBuilder sb = new StringBuilder( text.length() );
		
		boolean nextCapital = true;
		for ( final char ch : text.toCharArray() ) {
			if ( ch == '_' ) {
				sb.append( ' ' );
				nextCapital = capitalizeWords;
			} else if ( nextCapital ) {
				sb.append( ch );
				nextCapital = false;
			} else
				sb.append( Character.toLowerCase( ch ) );
		}
		
		return sb.toString();
	};
	
	/**
	 * Concatenates the elements of the specified <code>int</code>array to a semicolon separated string.<br>
	 * Elements are separated with a semicolon and a space. Array elements are formatted properly according to the user's number format preference.
	 * 
	 * @param arr <code>int</code> array whose elements to concatenate
	 * @return a semicolon separated list string of the specified array
	 */
	public static String concatenate( final int[] arr ) {
		final StringBuilder sb = new StringBuilder();
		
		for ( final int n : arr ) {
			if ( sb.length() > 0 )
				sb.append( "; " );
			sb.append( LEnv.LANG.formatNumber( n ) );
		}
		
		return sb.toString();
	}
	
}
