/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight.util.job;

import hu.scelight.gui.icon.Icons;
import hu.scelight.service.env.Env;
import hu.sllauncher.gui.icon.LRIcon;
import hu.sllauncher.service.job.ProgressJob;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * File downloader job (with URL GET method).
 * 
 * <p>
 * {@link #getMaximumProgress()} will return the file size in bytes (-1 if not known) and {@link #getCurrentProgress()} will return the total downloaded bytes.
 * </p>
 * 
 * @author Andras Belicza
 */
public class DownloaderJob extends ProgressJob {
	
	/** {@link URL} to download. */
	protected URL     url;
	
	/** File to save the downloaded content to. */
	protected Path    file;
	
	/** Tells if the file is downloaded successfully. */
	protected boolean success;
	
	/**
	 * Creates a new {@link DownloaderJob}.
	 * 
	 * @param url {@link URL} to download
	 * @param file file to save the downloaded content to
	 * 
	 */
	public DownloaderJob( final URL url, final Path file ) {
		super( "Downloader: " + file.getFileName(), Icons.F_DRIVE_DOWNLOAD );
		
		this.url = url;
		this.file = file;
	}
	
	/**
	 * Creates a new {@link DownloaderJob}.
	 * 
	 * @param name name of the job
	 * @param ricon job ricon
	 */
	protected DownloaderJob( final String name, final LRIcon ricon ) {
		super( name, ricon );
	}
	
	@Override
	public void jobRun() {
		try {
			Env.LOGGER.debug( "Starting file download: " + file );
			
			final URLConnection con = url.openConnection();
			
			// Note: I don't use con.getContentLengthLong() because progress expects int.
			// getContentLength() returns -1 if content length is not known which is perfect for indicating
			// indeterminate status through maximumProgress.
			maximumProgress.set( con.getContentLength() );
			
			Path tempFile = null;
			try ( final InputStream in = con.getInputStream() ) {
				tempFile = Files.createTempFile( "scelight", null );
				
				try ( final OutputStream out = Files.newOutputStream( tempFile ) ) {
					int bytesRead;
					final byte[] buffer = new byte[ 8192 ];
					
					while ( mayContinue() && ( bytesRead = in.read( buffer ) ) > 0 ) {
						out.write( buffer, 0, bytesRead );
						currentProgress.addAndGet( bytesRead );
					}
				}
				
				if ( cancelRequested )
					throw new IOException( "Download aborted by user!" );
				
				Files.createDirectories( file.getParent() );
				
				Files.move( tempFile, file );
				
				success = true;
				Env.LOGGER.debug( "File downloaded successfully: " + file );
			} finally {
				if ( tempFile != null )
					Files.deleteIfExists( tempFile );
			}
		} catch ( final IOException ie ) {
			Env.LOGGER.debug( "Failed to download file: " + url, ie );
		}
	}
	
	/**
	 * Tells whether file download succeeded.
	 * 
	 * @return true if file download succeeded; false otherwise
	 */
	public boolean isSuccess() {
		return success;
	}
	
}
