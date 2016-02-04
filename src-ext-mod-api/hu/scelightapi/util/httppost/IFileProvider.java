/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelightapi.util.httppost;

import hu.scelightapi.service.IFactory;

import java.net.HttpURLConnection;
import java.nio.file.Path;

/**
 * A file provider.
 * 
 * @author Andras Belicza
 * 
 * @see IHttpPost#saveAttachmentToFile(IFileProvider, byte[][])
 * @see IFactory#newSimpleFileProvider(Path, Long)
 */
public interface IFileProvider {
	
	/**
	 * Provides a file for an open {@link HttpURLConnection} to save the attachment to.
	 * 
	 * @param httpUrlConnection an open {@link HttpURLConnection} which can be used to get information from (the file name for example)
	 * @return a file that will be used to save the attachment to
	 * */
	Path getFile( HttpURLConnection httpUrlConnection );
	
	/**
	 * Provides a last modified time for an open {@link HttpURLConnection} to save the attachment.
	 * 
	 * <p>
	 * This method may return <code>null</code> in which case the last modified property of the file will not be set.
	 * </p>
	 * 
	 * @param httpUrlConnection an open {@link HttpURLConnection} which can be used to get information from
	 * @return last modified time to be set for the saved attachment
	 */
	Long getLastModified( HttpURLConnection httpUrlConnection );
	
}
