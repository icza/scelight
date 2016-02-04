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
import hu.scelight.service.settings.Settings;
import hu.sllauncher.service.job.Job;

import java.nio.file.Path;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.AuthenticationFailedException;
import javax.mail.Message.RecipientType;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 * Email sender job.
 * 
 * <p>
 * Resources:<br>
 * <a href="https://javamail.java.net/nonav/docs/api/">JavaMail API documentation</a>
 * </p>
 * 
 * Useful: http://stackoverflow.com/questions/6756162/how-do-i-send-mail-with-both-plain-text-as-well-as-html-text-so-that-each-mail-r
 * 
 * @author Andras Belicza
 */
public class EmailSenderJob extends Job {
	
	/**
	 * Email sending result.
	 * 
	 * @author Andras Belicza
	 */
	public enum Result {
		
		/** Sending email failed due to authentication failure. */
		AUTHENTICATION_FAILED,
		
		/** Sending email failed. */
		UNKNOWN_ERROR,
		
		/** Sending email succeeded. */
		SUCCESS;
		
	}
	
	/** Email addressees. */
	public final String        to;
	
	/** Email subject. */
	private final String       subject;
	
	/** Email body. */
	private final String       body;
	
	/** Password to use when authenticating with the SMTP server. */
	private final char[]       password;
	
	/** Attachment list. */
	private final List< Path > attachmentList;
	
	/** Email sending result. */
	private Result             result;
	
	
	/**
	 * Creates a new {@link EmailSenderJob}. <br>
	 * The email addressees, subject and body are taken from the settings.
	 * 
	 * @param password password to use when authenticating with the SMTP server
	 * @param attachmentList attachment list
	 */
	public EmailSenderJob( final char[] password, final List< Path > attachmentList ) {
		this( Env.APP_SETTINGS.get( Settings.EMAIL_TO ), Env.APP_SETTINGS.get( Settings.EMAIL_SUBJECT ), Env.APP_SETTINGS.get( Settings.EMAIL_BODY ), password,
		        attachmentList );
	}
	
	/**
	 * Creates a new {@link EmailSenderJob}.
	 * 
	 * @param to email addressees
	 * @param subject email subject
	 * @param body email body
	 * @param password password to use when authenticating with the SMTP server
	 * @param attachmentList attachment list
	 */
	public EmailSenderJob( final String to, final String subject, final String body, final char[] password, final List< Path > attachmentList ) {
		super( "Email sender to: " + to, Icons.F_MAIL_AT_SIGN );
		
		this.to = to;
		this.subject = subject;
		this.body = body;
		this.password = password;
		this.attachmentList = attachmentList;
	}
	
	@Override
	public void jobRun() {
		// Default smtp port: 25, secure smtp port: 587
		// http://stackoverflow.com/questions/3177616/how-to-attach-multiple-files-to-an-email-using-javamail
		
		Env.LOGGER.info( "Sending email to: " + to );
		
		final Properties props = new Properties();
		props.put( "mail.smtp.host", Env.APP_SETTINGS.get( Settings.SMTP_HOST ) );
		props.put( "mail.smtp.port", Env.APP_SETTINGS.get( Settings.SMTP_PORT ).toString() );
		if ( Env.APP_SETTINGS.get( Settings.SMTP_SECURE ) )
			props.put( "mail.smtp.starttls.enable", "true" );
		props.put( "mail.smtp.auth", "true" );
		// TODO this might not be necessary, if so, remove these (not set)
		props.put( "mail.smtp.user", Env.APP_SETTINGS.get( Settings.SMTP_USER ) );
		props.put( "mail.smtp.password", new String( password ) );
		
		try {
			final Session session = Session.getInstance( props );
			
			final MimeMessage msg = new MimeMessage( session );
			msg.setFrom( Env.APP_SETTINGS.get( Settings.EMAIL_FROM ) );
			msg.setRecipients( RecipientType.TO, to );
			msg.setSubject( subject );
			
			// PARTS OF THE EMAIL
			final MimeMultipart multipart = new MimeMultipart( "mixed" );
			
			// Message body
			MimeBodyPart bodyPart = new MimeBodyPart();
			bodyPart.setText( body );
			multipart.addBodyPart( bodyPart );
			
			// Message Footer
			final String footer = Env.APP_SETTINGS.get( Settings.EMAIL_FOOTER );
			if ( !footer.trim().isEmpty() ) {
				bodyPart = new MimeBodyPart();
				bodyPart.setText( "\n\n" + footer );
				multipart.addBodyPart( bodyPart );
			}
			
			// Attachments
			if ( attachmentList != null )
				for ( final Path file : attachmentList ) {
					bodyPart = new MimeBodyPart();
					bodyPart.setDataHandler( new DataHandler( new FileDataSource( file.toFile() ) ) );
					bodyPart.setFileName( file.getFileName().toString() );
					multipart.addBodyPart( bodyPart );
				}
			
			msg.setContent( multipart );
			
			// TRANSPORTING EMAIL
			
			// Data Content Handler for multipart/mixed mime type is searched for and loaded dynamically.
			// Job originates from the Launcher, but mail jars and app is loaded with a URL class loader, and so it can't find
			// the required classes, and the following exception occurs:
			// ERROR javax.activation.UnsupportedDataTypeException: no object DCH for MIME type multipart/mixed;
			// To overcome this, we set the class loader of this class (which is the same which holds the mail jars).
			// This line is needed even though each new URLClassLoader has its parent class loader set properly.
			Thread.currentThread().setContextClassLoader( getClass().getClassLoader() );
			
			Transport.send( msg, props.getProperty( "mail.smtp.user" ), props.getProperty( "mail.smtp.password" ) );
			
			Env.LOGGER.info( "Email sent successfully to: " + to );
			result = Result.SUCCESS;
			
		} catch ( final AuthenticationFailedException afe ) {
			Env.LOGGER.error( "Failed to send email to: " + to + ", wrong user name / password!", afe );
			result = Result.AUTHENTICATION_FAILED;
		} catch ( final Exception e ) {
			Env.LOGGER.error( "Failed to send email to: " + to, e );
			result = Result.UNKNOWN_ERROR;
		} finally {
			// Clear password
			for ( int i = password.length - 1; i >= 0; i-- )
				password[ i ] = '\0';
		}
	}
	
	/**
	 * Returns the email sending result.
	 * 
	 * @return the email sending result
	 */
	public Result getResult() {
		return result;
	}
	
}
