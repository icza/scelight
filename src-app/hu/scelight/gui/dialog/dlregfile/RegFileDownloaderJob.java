/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight.gui.dialog.dlregfile;

import hu.scelight.Consts;
import hu.scelight.gui.icon.Icons;
import hu.scelight.service.env.Env;
import hu.scelight.util.Utils;
import hu.scelight.util.httppost.HttpPost;
import hu.scelight.util.httppost.SimpleFileProvider;
import hu.scelightopapi.ScelightOpApi;
import hu.sllauncher.bean.reginfo.SysInfoBean;
import hu.sllauncher.service.job.Job;
import hu.sllauncher.service.registration.RegManager;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXB;

/**
 * Registration file downloader job.
 * 
 * @author Andras Belicza
 */
public class RegFileDownloaderJob extends Job {
	
	/** Ticket to use to download registration file. */
	private final String ticket;
	
	/** Error message. */
	private String       errorMsg;
	
	/** Error messages to display in a dialog. */
	private Object[]     errorDialogMsgs;
	
	
	/**
	 * Creates a new {@link RegFileDownloaderJob}.
	 * 
	 * @param ticket ticket to use to download registration file
	 */
	public RegFileDownloaderJob( final String ticket ) {
		super( "Registration file downloader", Icons.F_LICENCE_KEY );
		this.ticket = ticket;
	}
	
	/**
	 * Contacts the Application Operator, posts required data and downloads the registration file.
	 */
	@Override
	public void jobRun() {
		final SysInfoBean sysInfo = Utils.getSysInfo();
		final StringWriter sysInfoXml = new StringWriter( 1024 );
		JAXB.marshal( sysInfo, sysInfoXml );
		
		final Map< String, String > paramsMap = new HashMap<>();
		paramsMap.put( ScelightOpApi.PARAM_REGFILE_TICKET, ticket );
		paramsMap.put( ScelightOpApi.PARAM_REGFILE_SYSINFO, sysInfoXml.toString() );
		
		Env.LOGGER.info( "Downloading registration file..." );
		
		try ( final HttpPost httpPost = new HttpPost( Env.URL_APP_OP_REGFILE, paramsMap ) ) {
			if ( !httpPost.connect() ) {
				Env.LOGGER.error( errorMsg = "Failed to connect to the " + Consts.APP_OPERATOR_NAME + "!" );
				errorDialogMsgs = new Object[] { errorMsg };
				return;
			}
			
			if ( !httpPost.doPost() ) {
				Env.LOGGER.error( errorMsg = "Failed to send request to the " + Consts.APP_OPERATOR_NAME + "!" );
				errorDialogMsgs = new Object[] { errorMsg };
				return;
			}
			
			if ( !httpPost.isServerResponseOk() ) {
				errorMsg = mineErrorMessage( httpPost.getResponse() );
				Env.LOGGER.error( "Failed to download registration file: " + errorMsg );
				errorDialogMsgs = new Object[] { "Failed to download registration file:", " ",
				        "<html><span style='color:red;font-weight:bold'>" + errorMsg + "</span></html>" };
				return;
			}
			
			if ( !httpPost.saveAttachmentToFile( new SimpleFileProvider( RegManager.PATH_REGISTRATION_FILE, null ) ) ) {
				Env.LOGGER.error( errorMsg = "Failed to save registration file!" );
				errorDialogMsgs = new Object[] { errorMsg };
				return;
			}
			
			// Success!
			Env.LOGGER.info( "Registration file downloaded successfully." );
		}
	}
	
	/**
	 * Mines out the error message from the specified HTML error page.
	 * 
	 * @param errorPage HTML error page to mine out the error message from
	 * @return the error message (potentially containing HTML formatting elements)
	 */
	private static String mineErrorMessage( String errorPage ) {
		// We only need the HTML body
		final int bodystart1 = errorPage.indexOf( "<body" ); // usually style attribute is specified
		final int bodystart2 = errorPage.indexOf( '>', bodystart1 );
		
		errorPage = errorPage.substring( bodystart2 + 1, errorPage.indexOf( "</body>", bodystart2 ) );
		
		// Purge new lines and some formatting:
		for ( final String token : new String[] { "\n", "\r", "<h1>", "</h1>", "<h2>", "</h2>", "<h3>", "</h3>" } )
			errorPage = errorPage.replace( token, "" );
		
		return errorPage;
	}
	
	/**
	 * Returns the error message.
	 * 
	 * @return the error message
	 */
	public String getErrorMsg() {
		return errorMsg;
	}
	
	/**
	 * Returns the error messages to display in a dialog.
	 * 
	 * @return the error messages to display in a dialog
	 */
	public Object[] getErrorDialogMsg() {
		return errorDialogMsgs;
	}
	
}
