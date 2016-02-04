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
import java.util.List;

/**
 * An interface that allows interaction with standard HTTP servers using the POST method.
 * 
 * <p>
 * Parameters are sent as if they would be part of an HTML form. The content-type (<code>"Content-Type"</code> request property) of the request will be set to
 * <code>"application/x-www-form-urlencoded;charset=UTF-8"</code>.
 * </p>
 * 
 * <p>
 * Its usefulness includes -but is not limited to- uploading/downloading files without having to use Multi-part requests. The following examples will
 * demonstrate these.
 * </p>
 * 
 * <p>
 * Instances can be acquired by {@link IFactory#newHttpPost(java.net.URL, java.util.Map)}.
 * </p>
 * 
 * <p>
 * <b>Example #1: Upload a file encoded with Base64:</b><br>
 * 
 * <pre>
 * <blockquote style='border:1px solid black'>
 * String url = &quot;http://some.site.com/upload&quot;;
 * Path file = Paths.get( &quot;c:/downloads/mine.txt&quot; );
 * Map&lt; String, String &gt; paramsMap = new HashMap&lt; String, String &gt;();
 * paramsMap.put( &quot;fileName&quot;, file.getFileName().toString() );
 * try {
 * 	paramsMap.put( &quot;fileBase64&quot;, utils.toBase64String( Files.readAllBytes( file ) ) );
 * } catch ( IOException ie ) {
 * }
 * // Add other parameters you need...
 * paramsMap.put( &quot;someOtherThing&quot;, &quot;some other value&quot; );
 * 
 * try ( IHttpPost httpPost = factory.newHttpPost( url, paramsMap ) ) {
 * 	if ( httpPost.connect() ) {
 * 		if ( httpPost.doPost() )
 * 			logger.info( &quot;File sent successfully, server response: &quot; + httpPost.getResponse() );
 * 		else
 * 			logger.error( &quot;Failed to send file!&quot; );
 * 	} else
 * 		logger.error( &quot;Failed to connect!&quot; );
 * } catch ( Exception e ) {
 * 	logger.error( &quot;Unexpected file upload error!&quot;, e );
 * }
 * </blockquote>
 * </pre>
 * 
 * </p>
 * 
 * <p>
 * <b>Example #2: Download a file from a server:</b><br>
 * 
 * <pre>
 * <blockquote style='border:1px solid black'>
 * String url = &quot;http://some.site.com/download&quot;;
 * Map&lt; String, String &gt; paramsMap = new HashMap&lt; String, String &gt;();
 * String fileName = &quot;somefile.txt&quot;;
 * paramsMap.put( &quot;fileName&quot;, fileName );
 * // Add other parameters you need...
 * paramsMap.put( &quot;userId&quot;, &quot;someUserId&quot; );
 * 
 * try ( IHttpPost httpPost = factory.newHttpPost( url, paramsMap ) ) {
 * 	if ( httpPost.connect() ) {
 * 		if ( httpPost.doPost() ) {
 * 			// Note: we could simply acquire a suitable file provider by:
 * 			// factory.newSimpleFileProvider( Paths.get( &quot;c:/downloads&quot;, fileName ), null );
 * 			// but this is to demonstrate the use and possibilities of IFileProvider
 * 			boolean result = httpPost.saveAttachmentToFile( new IFileProvider() {
 * 				public Path getFile( HttpURLConnection httpUrlConnection ) {
 * 					Path file = Paths.get( &quot;c:/downloads&quot;, fileName );
 * 					logger.info( &quot;Saving file to: &quot; + file );
 * 					return file;
 * 				}
 * 				
 * 				public Long getLastModified( HttpURLConnection httpUrlConnection ) {
 * 					// We assume here that the server sends the file last modified value as a header field named &quot;X-file-date&quot;:
 * 					String fileDateString = httpUrlConnection.getHeaderField( &quot;X-file-date&quot; );
 * 					return fileDateString == null ? null : Long.valueOf( fileDateString );
 * 				}
 * 			} );
 * 			if ( result )
 * 				logger.info( &quot;Attachment saved successfully to file: &quot; + file );
 * 		} else
 * 			logger.error( &quot;Failed to send request!&quot; );
 * 	} else
 * 		logger.error( &quot;Failed to connect!&quot; );
 * } catch ( Exception e ) {
 * 	logger.error( &quot;Unexpected file download error!&quot;, e );
 * }
 * </blockquote>
 * </pre>
 * 
 * @author Andras Belicza
 * 
 * @see IFactory#newHttpPost(java.net.URL, java.util.Map)
 */
public interface IHttpPost extends AutoCloseable {
	
	/** Default charset to be used. */
	String DEFAULT_CHARSET = "UTF-8";
	
	
	/**
	 * Sets whether internal state checking should be performed.
	 * 
	 * <p>
	 * You may want to disable internal state checking if you want to tweak the {@link HttpURLConnection}.
	 * </p>
	 * 
	 * @param enabled the internal state checking value to be set
	 */
	void setInternalStateCheckingEnabled( boolean enabled );
	
	/**
	 * Tells if internal state checking is enabled. Internal state checking is enabled by default.
	 * 
	 * @return true if internal state checking is enabled; false otherwise
	 */
	boolean isInternalStateCheckingEnabled();
	
	/**
	 * Returns the internal state of the connection/communication.
	 * 
	 * @return the internal state of the connection/communication
	 */
	State getState();
	
	/**
	 * Sets the charset of the request.
	 * 
	 * <p>
	 * This will set the request property <code>"Accept-Charset"</code> to <code>charset</code>.
	 * </p>
	 * 
	 * <p>
	 * It must be called before {@link #connect()}. If charset is not set, the {@link #DEFAULT_CHARSET} will be used.
	 * </p>
	 * 
	 * @param charset charset of the request to be set
	 * 
	 * @throws IllegalStateException if internal state checking is enabled and the internal state is not {@link State#NOT_CONNECTED}
	 */
	void setRequestCharset( String charset ) throws IllegalStateException;
	
	/**
	 * Returns the charset of the request. The default charset of the request is {@link #DEFAULT_CHARSET}.
	 * 
	 * @return the charset of the request
	 */
	String getRequestCharset();
	
	/**
	 * Sets a request property.
	 * 
	 * <p>
	 * The properties will be passed to the underlying {@link HttpURLConnection} before it's <code>connect()</code> method is called.
	 * </p>
	 * 
	 * <p>
	 * It must be called before {@link #connect()}.
	 * </p>
	 * 
	 * @param key the property key
	 * @param value the property value
	 * 
	 * @throws IllegalStateException if internal state checking is enabled and the internal state is not {@link State#NOT_CONNECTED}
	 */
	void setRequestProperty( String key, String value ) throws IllegalStateException;
	
	/**
	 * Returns the underlying {@link HttpURLConnection}.
	 * 
	 * <p>
	 * This method may return <code>null</code> if called before {@link #connect()} or if called when {@link #connect()} returned false.
	 * </p>
	 * 
	 * @return the underlying {@link HttpURLConnection}
	 */
	HttpURLConnection getConnection();
	
	/**
	 * Connects to the provided URL.
	 * 
	 * <p>
	 * Only one connect method ({@link #connect()} or {@link #connect(Runnable)}) can be called and only once.
	 * </p>
	 * 
	 * @return true if connection was successful; false otherwise
	 * 
	 * @throws IllegalStateException if internal state checking is enabled and the internal state is not {@link State#NOT_CONNECTED}
	 * 
	 * @see #connect(Runnable)
	 */
	boolean connect() throws IllegalStateException;
	
	/**
	 * Connects to the provided URL.
	 * 
	 * <p>
	 * Only one connect method ({@link #connect()} or {@link #connect(Runnable)}) can be called and only once.
	 * </p>
	 * 
	 * @param beforeConnectionConnectTask task to be executed right before calling {@link HttpURLConnection#connect()}
	 * @return true if connection was successful; false otherwise
	 * 
	 * @throws IllegalStateException if internal state checking is enabled and the internal state is not {@link State#NOT_CONNECTED}
	 * 
	 * @see #connect()
	 */
	boolean connect( Runnable beforeConnectionConnectTask ) throws IllegalStateException;
	
	/**
	 * Posts the parameters to the server.
	 * 
	 * <p>
	 * The parameters will be encoded using the charset set by {@link #setRequestCharset(String)} (defaults to <code>{@value #DEFAULT_CHARSET}</code> ).
	 * </p>
	 * 
	 * <p>
	 * Can only be called if {@link #connect()} returned <code>true</code>.
	 * </p>
	 * 
	 * @return true if the operation was successful; false otherwise
	 * 
	 * @throws IllegalStateException if internal state checking is enabled and the internal state is not {@link State#CONNECTED}
	 */
	boolean doPost() throws IllegalStateException;
	
	/**
	 * Tells if the the HTTP response code is OK (HTTP 200).
	 * 
	 * <p>
	 * Can only be called after {@link #doPost()} and before {@link #close()}.
	 * </p>
	 * 
	 * @return true if the the HTTP response code is OK; false otherwise
	 * 
	 * @throws IllegalStateException if internal state checking is enabled and {@link #doPost()} has not been called or {@link #close()} has been called
	 * 
	 * @see #getServerResponseCode()
	 * @see #getServerResponseMessage()
	 */
	boolean isServerResponseOk() throws IllegalStateException;
	
	/**
	 * Returns the HTTP response code of the server.
	 * 
	 * <p>
	 * Can only be called after {@link #doPost()} and before {@link #close()}.
	 * </p>
	 * 
	 * @return the HTTP response code of the server
	 * 
	 * @throws IllegalStateException if internal state checking is enabled and {@link #doPost()} has not been called or {@link #close()} has been called
	 * 
	 * @see #isServerResponseOk()
	 * @see #getServerResponseMessage()
	 */
	int getServerResponseCode() throws IllegalStateException;
	
	/**
	 * Returns the HTTP response message of the server.
	 * 
	 * <p>
	 * Can only be called after {@link #doPost()} and before {@link #close()}.
	 * </p>
	 * 
	 * @return the HTTP response code of the server
	 * 
	 * @throws IllegalStateException if internal state checking is enabled and {@link #doPost()} has not been called or {@link #close()} has been called
	 * 
	 * @see #isServerResponseOk()
	 * @see #getServerResponseCode()
	 */
	String getServerResponseMessage() throws IllegalStateException;
	
	/**
	 * Gets the response from the server.
	 * 
	 * <p>
	 * Can only be called after {@link #doPost()}.
	 * </p>
	 * 
	 * <p>
	 * If the server returned an error, this will return the error page provided by the server.
	 * </p>
	 * 
	 * @return the server response, or <code>null</code> if error occurred
	 * 
	 * @throws IllegalStateException if internal state checking is enabled and the internal state is not {@link State#REQUEST_SENT}
	 * 
	 * @see #getResponseLines()
	 * @see #saveAttachmentToFile(IFileProvider, byte[][])
	 */
	String getResponse() throws IllegalStateException;
	
	/**
	 * Gets the response from the server as a list of lines.
	 * 
	 * <p>
	 * Can only be called after {@link #doPost()}.
	 * </p>
	 * 
	 * <p>
	 * If the server returned an error, this will return the error page provided by the server.
	 * </p>
	 * 
	 * @return the server response as a list of lines, or <code>null</code> if error occurred
	 * 
	 * @throws IllegalStateException if internal state checking is enabled and the internal state is not {@link State#REQUEST_SENT}
	 * 
	 * @see #getResponse()
	 * @see #saveAttachmentToFile(IFileProvider, byte[][])
	 */
	List< String > getResponseLines() throws IllegalStateException;
	
	/**
	 * Saves the attachment of the response, the content is treated as <code>application/octet-stream</code>.
	 * 
	 * <p>
	 * Can only be called if {@link #doPost()} returned <code>true</code>.
	 * </p>
	 * 
	 * <p>
	 * A {@link IFileProvider} is used to get a file to save the attachment to.
	 * </p>
	 * 
	 * @param fileProvider file provider to specify a file to save to
	 * @param buffer optional buffer to use for IO read/write operations
	 * @return true if the attachment was saved successfully; false otherwise
	 * 
	 * @throws IllegalStateException if internal state checking is enabled and the internal state is not {@link State#REQUEST_SENT}
	 * 
	 * @see IFileProvider
	 * @see IFactory#newSimpleFileProvider(java.nio.file.Path, Long)
	 * @see #getResponse()
	 * @see #getResponseLines()
	 */
	boolean saveAttachmentToFile( IFileProvider fileProvider, byte[]... buffer ) throws IllegalStateException;
	
	/**
	 * Closes this {@link IHttpPost}, releases all allocated resources.
	 */
	@Override
	void close();
	
}
