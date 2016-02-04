/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight.gui.dialog;

import hu.scelight.gui.comp.XPasswordField;
import hu.scelight.gui.help.Helps;
import hu.scelight.gui.icon.Icons;
import hu.scelight.gui.setting.SettingsGui;
import hu.scelight.service.env.Env;
import hu.scelight.service.settings.Settings;
import hu.scelight.util.Utils;
import hu.scelight.util.gui.GuiUtils;
import hu.scelight.util.job.EmailSenderJob;
import hu.sllauncher.gui.comp.GridBagPanel;
import hu.sllauncher.gui.comp.HelpIcon;
import hu.sllauncher.gui.comp.IndicatorTextField;
import hu.sllauncher.gui.comp.StatusLabel;
import hu.sllauncher.gui.comp.XButton;
import hu.sllauncher.gui.comp.XDialog;
import hu.sllauncher.gui.comp.XLabel;
import hu.sllauncher.gui.comp.XScrollPane;
import hu.sllauncher.gui.comp.XTextField;
import hu.sllauncher.util.gui.adapter.ActionAdapter;

import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.nio.file.Path;
import java.util.List;

import javax.swing.JTextArea;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

/**
 * A dialog to send an email.
 * 
 * @author Andras Belicza
 */
@SuppressWarnings( "serial" )
public class SendEmailDialog extends XDialog {
	
	/** Password in case chosen to remember. */
	private static String      storedPassword;
	
	/** Attachment list. */
	private final List< Path > attachmentList;
	
	/**
	 * Creates a new {@link SendEmailDialog}.
	 * 
	 * @param to initial addresses (list of email addresses)
	 */
	public SendEmailDialog( final String to ) {
		this( "Send email", null, to );
	}
	
	/**
	 * Creates a new {@link SendEmailDialog}.
	 * 
	 * @param attachmentList default email attachments
	 */
	public SendEmailDialog( final List< Path > attachmentList ) {
		this( "Send email", attachmentList, null );
	}
	
	/**
	 * Creates a new {@link SendEmailDialog}.
	 * 
	 * @param title dialog title
	 * @param attachmentList default email attachments
	 * @param to initial addresses (list of email addresses)
	 */
	public SendEmailDialog( final String title, final List< Path > attachmentList, final String to ) {
		super( Env.MAIN_FRAME );
		
		this.attachmentList = attachmentList;
		
		setTitle( title + ( attachmentList == null || attachmentList.isEmpty() ? "" : " (" + attachmentList.size() + ")" ) );
		setIconImage( Icons.F_MAIL_AT_SIGN.get().getImage() );
		
		buildGui( to );
		
		restoreDefaultSize();
		
		setVisible( true );
	}
	
	/**
	 * Builds the GUI of the email reps dialog.
	 * 
	 * @param to specifies the to addresses
	 */
	private void buildGui( final String to ) {
		final GridBagPanel formPanel = new GridBagPanel();
		
		formPanel.nextRow();
		formPanel.addSingle( new XLabel( "From:" ) );
		formPanel.c.weightx = 1;
		final IndicatorTextField fromField = SettingsGui.createSettingIndicatorTextField( Settings.EMAIL_FROM, Env.APP_SETTINGS, null );
		Utils.setEmailValidator( fromField );
		formPanel.addSingle( fromField );
		formPanel.c.weightx = 0;
		formPanel.addRemainder( new HelpIcon( Helps.SENDER_EMAIL_ADDRESS ) );
		
		formPanel.nextRow();
		formPanel.addSingle( new XLabel( Settings.EMAIL_TO.name + ':' ) );
		final IndicatorTextField toField = to == null ? SettingsGui.createSettingIndicatorTextField( Settings.EMAIL_TO, Env.APP_SETTINGS, null )
		        : new IndicatorTextField( to );
		Utils.setEmailListValidator( toField );
		formPanel.addRemainder( toField );
		setFocusTargetOnFirstShow( toField ); // TODO doesnt work
		
		formPanel.nextRow();
		formPanel.addSingle( new XLabel( Settings.EMAIL_SUBJECT.name + ':' ) );
		final XTextField subjectField = SettingsGui.createSettingTextField( Settings.EMAIL_SUBJECT, Env.APP_SETTINGS, null );
		formPanel.addRemainder( subjectField );
		
		formPanel.nextRow();
		formPanel.addSingle( new XLabel( Settings.EMAIL_BODY.name + ':' ) );
		final JTextArea bodyArea = SettingsGui.createSettingTextArea( Settings.EMAIL_BODY, Env.APP_SETTINGS, null );
		formPanel.c.weighty = 1;
		formPanel.c.fill = GridBagConstraints.BOTH;
		formPanel.addRemainder( new XScrollPane( bodyArea, false ) );
		formPanel.c.fill = GridBagConstraints.NONE;
		formPanel.c.weighty = 0;
		
		formPanel.nextRow();
		formPanel.addSingle( new XLabel( Settings.EMAIL_FOOTER.name + ':', Icons.F_LICENCE_KEY.get() ) );
		final JTextArea footerArea = SettingsGui.createSettingTextArea( Settings.EMAIL_FOOTER, Env.APP_SETTINGS, null );
		formPanel.c.fill = GridBagConstraints.BOTH;
		formPanel.addRemainder( new XScrollPane( footerArea, false ) );
		formPanel.c.fill = GridBagConstraints.NONE;
		
		cp.addCenter( formPanel );
		
		final StatusLabel statusLabel = new StatusLabel();
		getButtonsPanel().add( statusLabel );
		
		getButtonsPanel().add( SettingsGui.createSettingLink( Settings.NODE_EMAIL ) );
		
		final XButton sendButton = new XButton( "_Send" );
		sendButton.addActionListener( new ActionAdapter() {
			@Override
			public void actionPerformed( final ActionEvent event ) {
				final char[] password = getPassword();
				if ( password == null )
					return; // Cancel was pressed
					
				sendButton.setEnabled( false );
				statusLabel.setProgress( "Sending email" );
				
				final EmailSenderJob esJob = new EmailSenderJob( password, attachmentList );
				esJob.setEdtCallback( new Runnable() {
					@Override
					public void run() {
						switch ( esJob.getResult() ) {
							case SUCCESS :
								statusLabel.setSuccess( "Email sent successfully." );
								GuiUtils.showInfoMsg( "Email successfully sent to: " + esJob.to );
								break;
							case AUTHENTICATION_FAILED :
								statusLabel.setError( "Wrong user name / password!" );
								GuiUtils.showErrorMsg( "Failed to send email to: " + esJob.to + ", wrong user name / password!" );
								break;
							default :
								statusLabel.setError( "Failed to send email!" );
								GuiUtils.showErrorMsg( "Failed to send email to: " + esJob.to );
								break;
						}
						sendButton.setEnabled( true );
					}
				} );
				esJob.start();
			}
		} );
		getButtonsPanel().add( sendButton );
		addCloseButton( "_Close" );
	}
	
	/**
	 * Returns the password to be used when authenticating with the SMTP server.
	 * <p>
	 * If no password have been given, asks the user to input the password. If the user chooses to remember the password, it will be returned next time.
	 * </p>
	 * 
	 * @return the password to be used when authenticating with the SMTP server; or <code>null</code> if cancel was pressed
	 */
	private static char[] getPassword() {
		if ( storedPassword != null ) {
			// TODO
		}
		
		final XPasswordField passField = new XPasswordField();
		// To focus the password field when dialog is displayed:
		passField.addAncestorListener( new AncestorListener() {
			@Override
			public void ancestorRemoved( AncestorEvent event ) {
				// We don't want to do anything here.
			}
			
			@Override
			public void ancestorMoved( AncestorEvent event ) {
				// We don't want to do anything here.
			}
			
			@Override
			public void ancestorAdded( AncestorEvent event ) {
				passField.requestFocusInWindow();
			}
		} );
		
		// TODO add capslock listener? "Warning! CAPSLOCK is ON!"
		
		if ( GuiUtils.showInputMsg( "<html>Please enter your password for SMTP user <code>'" + Env.APP_SETTINGS.get( Settings.SMTP_USER ) + "'</code>:</html>"
		        + ":", passField ) )
			return passField.getPassword();
		
		return null;
	}
	
	@Override
	protected void customOnClose() {
		// TODO
		// ask confirmation if sending in progress
		// and stop it
	}
	
}
