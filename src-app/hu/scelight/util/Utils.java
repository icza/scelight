/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight.util;

import java.awt.Color;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import hu.scelight.gui.comp.IndicatorTextArea;
import hu.scelight.gui.dialog.SendEmailDialog;
import hu.scelight.gui.setting.SettingsGui;
import hu.scelight.sc2.rep.model.details.Toon;
import hu.scelight.service.env.Env;
import hu.scelight.service.settings.Settings;
import hu.scelight.util.gui.GuiUtils;
import hu.scelightapi.gui.comp.IIndicatorTextArea;
import hu.scelightapibase.gui.comp.IIndicatorTextField;
import hu.scelightapibase.gui.comp.ITextField.IValidator;
import hu.sllauncher.service.env.OpSys;
import hu.sllauncher.util.LRHtml;
import hu.sllauncher.util.LUtils;
import hu.sllauncher.util.LoggerUncaughtExceptionHandler;
import hu.sllauncher.util.Pair;

/**
 * General utilities and constants for the application.
 * 
 * @author Andras Belicza
 */
public class Utils extends LUtils {
	
	/**
	 * Opens the web page specified by the URL in the system's default browser.
	 * 
	 * @param url {@link URL} to be opened
	 */
	@Override
	public void showURLInBrowser( final URL url ) {
		if ( "mailto".equals( url.getProtocol() ) && Env.APP_SETTINGS.get( Settings.USE_INTERNAL_EMAIL_CLIENT ) ) {
			if ( checkEmailSettings() )
				new SendEmailDialog( url.getPath() );
		} else
			super.showURLInBrowser( url );
	}
	
	/**
	 * Returns a new, independent {@link List} which contains all the specified elements.
	 * 
	 * @param <E> type of the elements
	 * @param elements elements to be added to the new list
	 * @return an independent {@link List} which does not rely on the <code>elements</code> array
	 */
	public static < E > List< E > asNewList( @SuppressWarnings( "unchecked" ) final E... elements) {
		final List< E > list = new ArrayList< >( Math.max( 10, elements.length ) );
		
		Collections.addAll( list, elements );
		
		return list;
	}
	
	/**
	 * Concatenates the elements of the specified collection to a comma separated string.<br>
	 * Elements are separated with a comma and a space.
	 * 
	 * @param c collection whose elements to concatenate
	 * @return a comma separated list string of the specified collection
	 */
	public static String concatenate( final Collection< ? > c ) {
		final StringBuilder sb = new StringBuilder();
		
		for ( final Object o : c ) {
			if ( sb.length() > 0 )
				sb.append( ", " );
			sb.append( o );
		}
		
		return sb.toString();
	}
	
	/**
	 * Sorts the specified int array in reversed order.
	 * 
	 * @param a array to be reverse-sorted
	 */
	public static void sortReversed( final int[] a ) {
		if ( a.length <= 1 )
			return; // Sorted as it is; also empty array might cause trouble (last = -1 /2)
			
		// 1. Sort
		Arrays.sort( a );
		
		// 1. Reverse
		final int last = a.length - 1;
		
		for ( int i = last / 2; i >= 0; i-- ) {
			// Swap
			final int temp = a[ i ];
			a[ i ] = a[ last - i ];
			a[ last - i ] = temp;
		}
	}
	
	/**
	 * Strips off markup formatting from the specified string.
	 * <p>
	 * Example: <code>"[RA]&lt;sp/&gt;SvnthSyn"</code> => <code>"[RA]SvnthSyn"</code>
	 * </p>
	 * 
	 * @param s string to strip off markup formatting from
	 * @return a string cleaned up from markup formatting
	 */
	public static String stripOffMarkupFormatting( String s ) {
		int start = 0;
		
		while ( ( start = s.indexOf( '<', start ) ) >= 0 )
			s = s.substring( 0, start ) + s.substring( s.indexOf( '>', start ) + 1 );
			
		return s;
	}
	
	/**
	 * Strips off leading zero characters from the specified string.
	 * <p>
	 * Example: <code>"\0\0S2"</code> => <code>"S2"</code>
	 * </p>
	 * 
	 * @param s string to strip off leading zero characters from
	 * @return a string cleaned up from leading zeros
	 */
	public static String stripOffLeadingZeros( final String s ) {
		final int lastNullIdx = s.lastIndexOf( '\0' );
		return lastNullIdx < 0 ? s : s.substring( lastNullIdx + 1 );
	}
	
	/**
	 * Launches an external application.
	 * <p>
	 * The external application is launched by calling {@link Runtime#exec(String[])}, <code>cmdArray</code> will be passed if specified or
	 * <code>new String[] { file.getAbsolutePath() }</code> if not.<br>
	 * This is necessary because in case of commands like "StarCraft II Editor.exe" it would be interpreted as "StarCraft II" with an "Editor.exe" parameter =>
	 * would launch Sc2 instead of the Editor.
	 * </p>
	 * 
	 * @param file starter file of the external application; if specified, it will be checked if it exists before attempting to launch, and if not, an error
	 *            message will be displayed
	 * @param cmdArray array containing the command to call and its arguments
	 * @param errMsgs optional additional error messages and components to display in case the app cannot be launched
	 */
	public static void launchExternalApp( final Path file, final String[] cmdArray, final Object... errMsgs ) {
		if ( file != null && !Files.exists( file ) ) {
			Env.LOGGER.warning( "Application does not exist: " + file );
			GuiUtils.showErrorMsg( "Application does not exist: ", file, errMsgs );
			return;
		}
		
		try {
			Runtime.getRuntime().exec( cmdArray == null ? new String[] { file.toAbsolutePath().toString() } : cmdArray );
		} catch ( final IOException ie ) {
			Env.LOGGER.error( "Failed to start external application: " + ( file == null ? cmdArray[ 0 ] : file ), ie );
			GuiUtils.showErrorMsg( "Failed to start external application: ", file == null ? cmdArray[ 0 ] : file );
		}
	}
	
	/**
	 * Starts playing a replay in StarCraft II.
	 * <p>
	 * Due to StarCraft II it only works if StarCraft II is not running at the time when this is called.
	 * </p>
	 * 
	 * @param replayFile replay file to be launched
	 */
	public static void launchReplay( final Path replayFile ) {
		// TODO Test it, also investigate MAC OS-X
		final Path sc2LauncherFile = Env.APP_SETTINGS.get( hu.scelight.service.settings.Settings.SC2_INSTALL_FOLDER )
		        .resolve( Env.OS == OpSys.OS_X ? "StarCraft II.app/Contents/MacOS/StarCraft II" : "Support/SC2Switcher.exe" );
				
		launchExternalApp( sc2LauncherFile, new String[] { sc2LauncherFile.toAbsolutePath().toString(), replayFile.toAbsolutePath().toString() }, " ",
		        "Is your StarCraft II install folder setting correct?", SettingsGui.createSettingLink( Settings.NODE_SC2_INSTALLATION ) );
	}
	
	/**
	 * Copies the specified text to the system clipboard.
	 * 
	 * @param text text to be copied to the system clipboard
	 */
	public static void copyToClipboard( final String text ) {
		try {
			Toolkit.getDefaultToolkit().getSystemClipboard().setContents( new StringSelection( text ), null );
		} catch ( final IllegalStateException ise ) {
			// Just to make sure: on some platforms if the clipboard is being accessed by another application, this might happen
		}
	}
	
	/**
	 * Converts the specified color to a CSS color sting.
	 * 
	 * @param color color to be converted
	 * @return the specified color as a CSS color sting
	 */
	public static String toCss( final Color color ) {
		return "rgb(" + color.getRed() + "," + color.getGreen() + "," + color.getBlue() + ")";
	}
	
	/**
	 * Returns the file name of the specified file without its extension.
	 * 
	 * @param file file whose name without extension to be returned
	 * @return the file name of the specified file without its extension
	 */
	public static String getFileNameWithoutExt( final Path file ) {
		final String fileName = file.getFileName().toString();
		
		final int dotIdx = fileName.lastIndexOf( '.' );
		return dotIdx > 0 ? fileName.substring( 0, dotIdx ) : fileName;
	}
	
	/**
	 * Returns the file name and extension parts of the name of the specified file.
	 * 
	 * <p>
	 * If the name of the specified file does not have an extension (dot is not found in its name), the second part of the returned pair will be
	 * <code>null</code>.
	 * </p>
	 * 
	 * @param file file whose name and extension to be returned
	 * @return the file name and extension parts of the name of the specified file
	 */
	public static Pair< String, String > getFileNameAndExt( final Path file ) {
		final String fileName = file.getFileName().toString();
		
		final int dotIdx = fileName.lastIndexOf( '.' );
		return dotIdx > 0 ? new Pair< >( fileName.substring( 0, dotIdx ), fileName.substring( dotIdx + 1 ) ) : new Pair< >( fileName, (String) null );
	}
	
	/**
	 * Generates and returns a unique path for the specified file that does not yet exist.
	 * <p>
	 * If the provided file does not exist, it is returned.<br>
	 * If it exists, an incrementing counter starting at 2 will be post-pended to the file name (keeping the extension) will be tried. (<code>" (2)"</code>,
	 * <code>" (3)"</code>, etc.).
	 * </p>
	 * 
	 * @param file file to return a unique path for
	 * @return a unique file path that does not yet exist
	 */
	public static Path uniqueFile( Path file ) {
		if ( !Files.exists( file ) )
			return file;
			
		final Path folder = file.getParent();
		
		final Pair< String, String > nameExt = getFileNameAndExt( file );
		final StringBuilder sb = new StringBuilder( nameExt.value1 ).append( " (" );
		final String ext = nameExt.value2 == null ? null : '.' + nameExt.value2;
		
		final int nameLength = sb.length(); // name length without the counter value
		
		for ( int counter = 2; true; counter++ ) {
			sb.append( counter ).append( ')' );
			if ( ext != null )
				sb.append( ext );
				
			file = folder.resolve( sb.toString() );
			
			if ( !Files.exists( file ) )
				return file;
				
			// Reset name builder
			sb.setLength( nameLength );
		}
	}
	
	/**
	 * Generates and returns a unique path for the specified file that does not yet exist in the specified set.
	 * <p>
	 * If the provided file does not exist in the set, it is added and returned.<br>
	 * If it exists, an incrementing counter starting at 2 will be post-pended to the file name (keeping the extension) will be tried. (<code>" (2)"</code>,
	 * <code>" (3)"</code>, etc.).
	 * </p>
	 * 
	 * @param file file to return a unique path for
	 * @param existing file set defining the existing files
	 * @return a unique file path that does not yet exist in the specified set
	 */
	public static Path uniqueFile( Path file, final Set< Path > existing ) {
		if ( !existing.contains( file ) ) {
			existing.add( file );
			return file;
		}
		
		final Path folder = file.getParent();
		
		final Pair< String, String > nameExt = getFileNameAndExt( file );
		final StringBuilder sb = new StringBuilder( nameExt.value1 ).append( " (" );
		final String ext = nameExt.value2 == null ? null : '.' + nameExt.value2;
		
		final int nameLength = sb.length(); // name length without the counter value
		
		for ( int counter = 2; true; counter++ ) {
			sb.append( counter ).append( ')' );
			if ( ext != null )
				sb.append( ext );
				
			file = folder == null ? Paths.get( sb.toString() ) : folder.resolve( sb.toString() );
			
			if ( !existing.contains( file ) ) {
				existing.add( file );
				return file;
			}
			
			// Reset name builder
			sb.setLength( nameLength );
		}
	}
	
	/**
	 * Converts the specified hex string to bytes.
	 * 
	 * <p>
	 * The hex string must be lower-cased, can only contain hex digits (<code>'0'..'9'</code> and <code>'a'..'f'</code>) and spaces (which will be omitted), and
	 * its length must be even.
	 * </p>
	 * 
	 * @param hex hex string; a string containing hexadecimal numbers
	 * 		   
	 * @return the specified hex string converted to bytes; or <code>null</code> if <code>hex</code> is an invalid hex string
	 */
	public static byte[] hexToBytes( String hex ) {
		// Quick check if it contains spaces:
		if ( hex.indexOf( ' ' ) >= 0 )
			hex = hex.replace( " ", "" ); // Remove spaces
			
		// Check: length must be even, must contain only hex digits
		if ( ( hex.length() & 0x01 ) == 1 || !hex.matches( "[\\da-f]*" ) )
			return null;
			
		final byte[] bytes = new byte[ hex.length() / 2 ];
		for ( int i = bytes.length - 1; i >= 0; i-- ) {
			final char upp = hex.charAt( i * 2 );
			final char low = hex.charAt( i * 2 + 1 );
			bytes[ i ] = (byte) ( ( ( upp - ( upp < 'a' ? '0' : 'a' - 10 ) ) << 4 ) | ( low - ( low < 'a' ? '0' : 'a' - 10 ) ) );
		}
		
		return bytes;
	}
	
	/**
	 * Calculates the MD5 digest of a file.
	 * 
	 * @param file file whose MD5 to be calculated
	 * @return the calculated MD5 digest of the file; or an empty string if the file cannot be read
	 */
	public static String calculateFileMd5( final Path file ) {
		return calculateFileDigest( "MD5", file );
	}
	
	/**
	 * Sets a toon list validator for the specified {@link IIndicatorTextField}.
	 * 
	 * @param itf {@link IIndicatorTextField} to set a validator for
	 */
	public static void setToonListValidator( final IIndicatorTextField itf ) {
		itf.getTextField().setValidator( new IValidator() {
			@Override
			public boolean validate( final String text ) {
				final StringBuilder sb = new StringBuilder( "<html><head>" + LRHtml.GENERAL_CSS + "</head>Parsed toons:<br><b><ol>" );
				int validCount = 0, invalidCount = 0;
				final StringTokenizer st = new StringTokenizer( itf.getText(), "," );
				while ( st.hasMoreTokens() ) {
					String toon = null;
					try {
						toon = st.nextToken().trim();
						if ( !toon.isEmpty() ) {
							final Toon parsedToon = new Toon( toon );
							// No exception means valid toon
							sb.append( "<li>" ).append( safeForHtml( parsedToon.toString() ) );
							validCount++;
						}
					} catch ( final IllegalArgumentException iae ) {
						// Invalid toon
						invalidCount++;
						sb.append( "<li><font color='red'>Invalid toon: " ).append( safeForHtml( toon ) ).append( "</font>" );
					}
				}
				sb.append( "</ol></b></html>" );
				
				if ( validCount + invalidCount == 0 ) {
					itf.setEmpty( "No toons entered." );
					return true;
				} else {
					if ( validCount > 0 ) {
						if ( invalidCount > 0 )
							itf.setNotAccepted( sb.toString() );
						else
							itf.setAccepted( sb.toString() );
						return true;
					} else {
						// Here invalidCount > 0 is true
						itf.setError( sb.toString() );
						return false;
					}
				}
			}
		} );
	}
	
	/**
	 * Sets a merged accounts validator (list of toon lists) for the specified {@link IndicatorTextArea}.
	 * 
	 * @param ita {@link IIndicatorTextArea} to set a validator for
	 */
	public static void setMergedAccountsValidator( final IIndicatorTextArea ita ) {
		ita.getTextArea().setValidator( new IValidator() {
			@Override
			public boolean validate( final String text ) {
				final StringBuilder sb = new StringBuilder( "<html><head>" + LRHtml.GENERAL_CSS + "</head>Parsed merged accounts:<br><b><ol>" );
				int validCount = 0, invalidCount = 0;
				
				final StringTokenizer lines = new StringTokenizer( ita.getText(), "\r\n" );
				while ( lines.hasMoreTokens() ) {
					final StringTokenizer st = new StringTokenizer( lines.nextToken(), "," );
					if ( !st.hasMoreTokens() )
						continue;
						
					int i = 0;
					while ( st.hasMoreTokens() ) {
						String toon = null;
						try {
							toon = st.nextToken().trim();
							if ( !toon.isEmpty() ) {
								final Toon parsedToon = new Toon( toon );
								// No exception means valid toon
								sb.append( "<li>" ).append( safeForHtml( parsedToon.toString() ) );
								if ( i++ == 0 )
									sb.append( "<ul><li>" ).append( safeForHtml( parsedToon.toString() ) );
								validCount++;
							}
						} catch ( final IllegalArgumentException iae ) {
							// Invalid toon
							invalidCount++;
							sb.append( "<li><font color='red'>Invalid toon: " ).append( safeForHtml( toon ) ).append( "</font>" );
							if ( i++ == 0 )
								sb.append( "<ul><li><font color='red'>Invalid toon: " ).append( safeForHtml( toon ) ).append( "</font>" );
						}
					}
					if ( i > 0 )
						sb.append( "</ul>" );
				}
				sb.append( "</ol></b></html>" );
				
				if ( validCount + invalidCount == 0 ) {
					ita.setEmpty( "No toons entered." );
					return true;
				} else {
					if ( validCount > 0 ) {
						if ( invalidCount > 0 )
							ita.setNotAccepted( sb.toString() );
						else
							ita.setAccepted( sb.toString() );
						return true;
					} else {
						// Here invalidCount > 0 is true
						ita.setError( sb.toString() );
						return false;
					}
				}
			}
		} );
	}
	
	/**
	 * Sets an email validator for the specified {@link IIndicatorTextField}.
	 * 
	 * @param itf {@link IIndicatorTextField} to set a validator for
	 */
	public static void setEmailValidator( final IIndicatorTextField itf ) {
		itf.getTextField().setValidator( new IValidator() {
			@Override
			public boolean validate( final String text ) {
				if ( text.trim().isEmpty() ) {
					itf.setEmpty( "No email address entered." );
					return true;
				}
				try {
					new InternetAddress( text, true );
					itf.setAccepted( "Email address is valid." );
					return true;
				} catch ( final AddressException ae ) {
					itf.setError( "<html>Email address is invalid!<br>Valid examples:"
		                    + "<ul><li>iczaaa@gmail.com<li>\"Andras Belicza\" &lt;iczaaa@gmail.com&gt;</ul></html>" );
					return false;
				}
			}
		} );
	}
	
	/**
	 * Sets an email list validator for the specified {@link IIndicatorTextField}.
	 * 
	 * @param itf {@link IIndicatorTextField} to set a validator for
	 */
	public static void setEmailListValidator( final IIndicatorTextField itf ) {
		itf.getTextField().setValidator( new IValidator() {
			@Override
			public boolean validate( final String text ) {
				if ( text.trim().isEmpty() ) {
					itf.setEmpty( "No email addresses entered." );
					return true;
				}
				try {
					InternetAddress.parse( text, true );
					itf.setAccepted( "Email addresses are valid." );
					return true;
				} catch ( final AddressException ae ) {
					itf.setError( "<html>Email addresses are invalid!<br> Separate email addresses with a comma (','). Valid examples:"
		                    + "<ul><li>iczaaa@gmail.com<li>\"Andras Belicza\" &lt;iczaaa@gmail.com&gt;</ul></html>" );
					return false;
				}
			}
		} );
	}
	
	/**
	 * Checks the email settings, and returns <code>true</code> if they are set and emails can be sent.
	 * 
	 * <p>
	 * If email settings are not configured properly, an info dialog will be displayed informing the user about this, with a link to open the email settings
	 * page.
	 * </p>
	 * 
	 * @return true if email settings are set and emails can be sent; false otherwise
	 */
	public static boolean checkEmailSettings() {
		final boolean missing = Env.APP_SETTINGS.get( Settings.SMTP_HOST ).trim().isEmpty() || Env.APP_SETTINGS.get( Settings.SMTP_USER ).trim().isEmpty();
		
		if ( missing )
			GuiUtils.showInfoMsg( "Before sending emails you have to configure your email server and account.", " ",
			        SettingsGui.createSettingLink( Settings.NODE_EMAIL ) );
					
		return !missing;
	}
	
	/**
	 * Converts the specified data to base64 encoded string.
	 * 
	 * @param data data to be converted
	 * 		   
	 * @return the specified data converted to base64 encoded string
	 */
	public static String toBase64String( final byte[] data ) {
		return javax.xml.bind.DatatypeConverter.printBase64Binary( data );
	}
	
	/**
	 * Reads a full byte array from the specified input stream.
	 * 
	 * <p>
	 * The problem with {@link InputStream#read(byte[])} is that it does not guarantee that the passed array will be "fully populated" even if there are enough
	 * data in the input stream (the returned number of bytes might be smaller than the array length). This method does guarantee it.
	 * </p>
	 * 
	 * @param in input stream to read from
	 * @param buffer array to be read
	 * @return the buffer
	 * @throws IOException if reading from the stream throws {@link IOException}
	 */
	public static byte[] readFully( final InputStream in, final byte[] buffer ) throws IOException {
		final int size = buffer.length;
		
		for ( int remaining = size; remaining > 0; )
			remaining -= in.read( buffer, size - remaining, remaining );
			
		return buffer;
	}
	
	/**
	 * Creates and returns an {@link ExecutorService} which uses a thread pool with a size defined by the {@link Settings#UTILIZED_CPU_CORES}.
	 * 
	 * @param parentThreadName parent thread name
	 * @return a multi-threaded {@link ExecutorService} with the proper thread pool size
	 */
	public static ExecutorService createExecutorService( final String parentThreadName ) {
		int coresCount = Env.APP_SETTINGS.get( Settings.UTILIZED_CPU_CORES );
		
		if ( coresCount == 0 )
			coresCount = Runtime.getRuntime().availableProcessors();
			
		// Use a thread factory which sets an uncaught exception logger for threads it creates.
		final ThreadFactory threadFactory = new ThreadFactory() {
			private final ThreadFactory defaultTf = Executors.defaultThreadFactory();
			
			@Override
			public Thread newThread( final Runnable r ) {
				final Thread thread = defaultTf.newThread( r );
				thread.setName( thread.getName() + " [" + parentThreadName + ']' );
				// Install an uncaught exception logger for the newly created thread:
				thread.setUncaughtExceptionHandler( LoggerUncaughtExceptionHandler.INSTANCE );
				return thread;
			}
		};
		
		return Executors.newFixedThreadPool( coresCount, threadFactory );
	}
	
	/**
	 * Shuts down properly the specified executor service.
	 * 
	 * @param es executor service to be shut down
	 * @return true if the executor service was shut down properly; false if it still has tasks running
	 */
	public static boolean shutdownExecutorService( final ExecutorService es ) {
		es.shutdown();
		
		try {
			// Give some time to finish running tasks (it should terminate below 1 second..)
			return es.awaitTermination( 100l, TimeUnit.SECONDS );
		} catch ( final InterruptedException ie ) {
			ie.printStackTrace();
			// Preserve the interrupted status of the current thread
			Thread.currentThread().interrupt();
			return false;
		}
	}
	
	/**
	 * Moves elements specified by their indices backward by 1.
	 * 
	 * <p>
	 * If the specified indices contains 0, then nothing is moved (the first element cannot be moved backward).
	 * </p>
	 * 
	 * <p>
	 * Implementation note: the received indices array might be rearranged. If this is unwanted, pass a copy of the original array.
	 * </p>
	 * 
	 * @param <T> type of the elements of the list
	 * 		   
	 * @param list list whose elements to move
	 * @param indices indices of elements to be moved backward by 1
	 * @return true if moving was performed; false otherwise
	 * 		
	 * @see #moveForward(List, int[])
	 */
	public static < T > boolean moveBackward( final List< T > list, final int[] indices ) {
		if ( indices.length == 0 )
			return false; // Nothing to move
			
		// Sort indices and iterate over them upward
		Arrays.sort( indices );
		if ( indices[ 0 ] == 0 )
			return false; // Cannot move backward as the first element is marked to be moved
			
		for ( final int idx : indices ) {
			final T element = list.get( idx );
			list.set( idx, list.get( idx - 1 ) );
			list.set( idx - 1, element );
		}
		
		return true;
	}
	
	/**
	 * Moves elements specified by their indices forward by 1.
	 * 
	 * <p>
	 * If the specified indices contains the index of the last element, then nothing is moved (the last element cannot be moved forward).
	 * </p>
	 * 
	 * <p>
	 * Implementation note: the received indices array might be rearranged. If this is unwanted, pass a copy of the original array.
	 * </p>
	 * 
	 * @param <T> type of the elements of the list
	 * 		   
	 * @param list list whose elements to move
	 * @param indices indices of elements to be moved backward by 1
	 * @return true if moving was performed; false otherwise
	 * 		
	 * @see #moveBackward(List, int[])
	 */
	public static < T > boolean moveForward( final List< T > list, final int[] indices ) {
		if ( indices.length == 0 )
			return false; // Nothing to move
			
		// Sort indices descending so we can still go upward
		sortReversed( indices );
		if ( indices[ 0 ] == list.size() - 1 )
			return false; // Cannot move forward as the last element is marked to be moved
			
		for ( final int idx : indices ) {
			final T element = list.get( idx );
			list.set( idx, list.get( idx + 1 ) );
			list.set( idx + 1, element );
		}
		
		return true;
	}
	
	/**
	 * Appends a calendar field value to the specified string builder, optionally prepended with a <code>'0'</code> character to be at least 2 digits.
	 * 
	 * @param sb string builder to append to
	 * @param cal calendar to get the field value from
	 * @param field field whose value to be appended
	 * @return the string builder for chaining
	 */
	public static StringBuilder append( final StringBuilder sb, final Calendar cal, int field ) {
		return append( sb, cal, field, 0 );
	}
	
	/**
	 * Appends a calendar field value to the specified string builder, optionally prepended with a <code>'0'</code> character to be at least 2 digits.
	 * 
	 * @param sb string builder to append to
	 * @param cal calendar to get the field value from
	 * @param field field whose value to be appended
	 * @param offset offset to be added to the field value before appending it to the string builder
	 * @return the string builder for chaining
	 */
	public static StringBuilder append( final StringBuilder sb, final Calendar cal, int field, final int offset ) {
		if ( ( field = cal.get( field ) + offset ) < 10 )
			sb.append( '0' );
		return sb.append( field );
	}
	
}
