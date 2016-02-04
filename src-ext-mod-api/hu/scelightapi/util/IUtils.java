/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelightapi.util;

import hu.scelight.gui.comp.IndicatorTextArea;
import hu.scelightapi.gui.comp.IIndicatorTextArea;
import hu.scelightapi.util.gui.IGuiUtils;
import hu.scelightapibase.gui.comp.IIndicatorTextField;
import hu.scelightapibase.service.env.IOpSys;
import hu.scelightapibase.util.IPair;

import java.awt.Color;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * General utilities.
 * 
 * <p>
 * To make everyone's life easier.
 * </p>
 * 
 * @author Andras Belicza
 * 
 * @see IGuiUtils
 */
public interface IUtils {
	
	/**
	 * Returns the detected operating system ({@link IOpSys}).
	 * 
	 * @return the detected operating system ({@link IOpSys})
	 * 
	 * @see IOpSys
	 */
	IOpSys detectOs();
	
	/**
	 * Opens the specified file or folder in the default file browser application of the user's OS.
	 * <p>
	 * If a file is specified, on Windows it will also be selected.
	 * </p>
	 * 
	 * @param path file or folder to be opened
	 */
	void showPathInFileBrowser( Path path );
	
	/**
	 * Opens the web page specified by the URL in the system's default browser.
	 * 
	 * @param url {@link URL} to be opened
	 */
	void showURLInBrowser( URL url );
	
	/**
	 * Starts playing a replay in StarCraft II.
	 * <p>
	 * Due to StarCraft II it only works if StarCraft II is not running at the time when this is called.
	 * </p>
	 * 
	 * @param replayFile replay file to be launched
	 */
	void launchReplay( Path replayFile );
	
	/**
	 * Prepares a text for HTML rendering, to be included in HTML text.
	 * 
	 * @param text text to be prepared
	 * @return the prepared text safe for HTML rendering
	 */
	String safeForHtml( String text );
	
	/**
	 * Deletes the specified path, recursively.
	 * 
	 * @param path path to be deleted
	 * @return true if deletion was successful; false otherwise
	 */
	boolean deletePath( Path path );
	
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
	Path uniqueFile( Path file );
	
	/**
	 * Calculates the MD5 digest of a file.
	 * 
	 * @param file file whose MD5 to be calculated
	 * @return the calculated MD5 digest of the file; or an empty string if the file cannot be read
	 */
	String calculateFileMd5( Path file );
	
	/**
	 * Calculates the SHA-256 digest of a file.
	 * 
	 * @param file file whose SHA-256 to be calculated
	 * @return the calculated SHA-256 digest of the file; or an empty string if the file cannot be read
	 */
	String calculateFileSha256( Path file );
	
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
	String calculateStreamSha256( InputStream input, long size );
	
	/**
	 * Converts the specified data to base64 encoded string.
	 * 
	 * @param data data to be converted
	 * 
	 * @return the specified data converted to base64 encoded string
	 */
	String toBase64String( byte[] data );
	
	/**
	 * Converts the specified data to hex string.
	 * 
	 * @param data data to be converted
	 * 
	 * @return the specified data converted to hex string
	 * 
	 * @see #toHexString(byte[], int, int)
	 * @see #hexToBytes(String)
	 */
	String toHexString( byte[] data );
	
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
	 * @see #hexToBytes(String)
	 */
	String toHexString( byte[] data, int offset, int length );
	
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
	 * 
	 * @see #toHexString(byte[])
	 * @see #toHexString(byte[], int, int)
	 */
	byte[] hexToBytes( String hex );
	
	/**
	 * Checks if the specified text contains the specified search text, performing a case-insensitive search.
	 * 
	 * @param text text to search in
	 * @param searchText text to be searched
	 * @return true if the specified text contains the specified search text, performing a case-insensitive search; false otherwise
	 */
	boolean containsIngoreCase( String text, String searchText );
	
	/**
	 * Concatenates the elements of the specified <code>int</code>array to a semicolon separated string.<br>
	 * Elements are separated with a semicolon and a space. Array elements are formatted properly according to the user's number format preference.
	 * 
	 * @param arr <code>int</code> array whose elements to concatenate
	 * @return a semicolon separated list string of the specified array
	 * 
	 * @since 1.2
	 */
	String concatenate( int[] arr );
	
	/**
	 * Concatenates the elements of the specified collection to a comma separated string. Elements are separated with a comma and a space.
	 * 
	 * @param c collection whose elements to concatenate
	 * @return a comma separated list string of the specified collection
	 */
	String concatenate( Collection< ? > c );
	
	/**
	 * Sorts the specified int array in reversed order.
	 * 
	 * @param a array to be reverse-sorted
	 */
	void sortReversed( int[] a );
	
	/**
	 * Strips off leading zero characters from the specified string.
	 * <p>
	 * Example: <code>"\0\0S2"</code> => <code>"S2"</code>
	 * </p>
	 * 
	 * @param s string to strip off leading zero characters from
	 * @return a string cleaned up from leading zeros
	 */
	String stripOffLeadingZeros( String s );
	
	/**
	 * Copies the specified text to the system clipboard.
	 * 
	 * @param text text to be copied to the system clipboard
	 */
	void copyToClipboard( String text );
	
	/**
	 * Converts the specified color to a CSS color sting.
	 * 
	 * @param color color to be converted
	 * @return the specified color as a CSS color sting
	 */
	String toCss( Color color );
	
	/**
	 * Returns the file name of the specified file without its extension.
	 * 
	 * @param file file whose name without extension to be returned
	 * @return the file name of the specified file without its extension
	 */
	String getFileNameWithoutExt( Path file );
	
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
	IPair< String, String > getFileNameAndExt( Path file );
	
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
	boolean checkEmailSettings();
	
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
	byte[] readFully( InputStream in, byte[] buffer ) throws IOException;
	
	/**
	 * Creates and returns an {@link ExecutorService} which uses a thread pool with a size defined by the utilized CPU cores setting.
	 * 
	 * @param parentThreadName parent thread name
	 * @return a multi-threaded {@link ExecutorService} with the proper thread pool size
	 */
	ExecutorService createExecutorService( String parentThreadName );
	
	/**
	 * Shuts down properly the specified executor service.
	 * 
	 * @param es executor service to be shut down
	 * @return true if the executor service was shut down properly; false if it still has tasks running
	 */
	boolean shutdownExecutorService( ExecutorService es );
	
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
	< T > boolean moveBackward( List< T > list, int[] indices );
	
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
	< T > boolean moveForward( List< T > list, int[] indices );
	
	/**
	 * Sets a toon list validator for the specified {@link IIndicatorTextField}.
	 * 
	 * @param itf {@link IIndicatorTextField} to set a validator for
	 */
	void setToonListValidator( IIndicatorTextField itf );
	
	/**
	 * Sets a merged accounts validator (list of toon lists) for the specified {@link IndicatorTextArea}.
	 * 
	 * @param ita {@link IIndicatorTextArea} to set a validator for
	 * 
	 * @since 1.4
	 */
	void setMergedAccountsValidator( IIndicatorTextArea ita );
	
	/**
	 * Sets an email validator for the specified {@link IIndicatorTextField}.
	 * 
	 * @param itf {@link IIndicatorTextField} to set a validator for
	 */
	void setEmailValidator( IIndicatorTextField itf );
	
	/**
	 * Sets an email list validator for the specified {@link IIndicatorTextField}.
	 * 
	 * @param itf {@link IIndicatorTextField} to set a validator for
	 */
	void setEmailListValidator( IIndicatorTextField itf );
	
}
