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
import hu.scelight.service.sound.Sounds;
import hu.scelight.util.gui.GuiUtils;
import hu.sllauncher.LConsts;
import hu.sllauncher.gui.comp.GridBagPanel;
import hu.sllauncher.gui.comp.Link;
import hu.sllauncher.gui.comp.StatusLabel;
import hu.sllauncher.gui.comp.XButton;
import hu.sllauncher.gui.comp.XDialog;
import hu.sllauncher.gui.comp.XLabel;
import hu.sllauncher.gui.comp.XTextField;
import hu.sllauncher.gui.comp.XToolBar;
import hu.sllauncher.service.sound.Sound;
import hu.sllauncher.util.gui.adapter.ActionAdapter;
import hu.sllauncher.util.gui.adapter.DocumentAdapter;

import java.awt.event.ActionEvent;

import javax.swing.Box;
import javax.swing.event.DocumentEvent;

/**
 * Registration file downloader dialog.
 * 
 * @author Andras Belicza
 */
@SuppressWarnings( "serial" )
public class DlRegistrationFileDialog extends XDialog {
	
	/**
	 * Creates a new {@link DlRegistrationFileDialog}.
	 */
	public DlRegistrationFileDialog() {
		super( Env.MAIN_FRAME );
		
		setTitle( "Download Registration File" );
		setIconImage( Icons.F_LICENCE_KEY.get().getImage() );
		
		buildGui();
		
		pack();
		defaultSize = getSize();
		restoreDefaultSize();
		
		setVisible( true );
	}
	
	/**
	 * Builds the GUI of the email reps dialog.
	 */
	private void buildGui() {
		final Box infoBox = Box.createVerticalBox();
		
		XToolBar toolBar = new XToolBar();
		toolBar.add( new XLabel( "For registration details visit this page: " ).verticalBorder( 5 ) );
		toolBar.add( new Link( "Registration page", LConsts.URL_REGINFO_PAGE ) );
		infoBox.add( toolBar );
		
		infoBox.add( Box.createVerticalStrut( 10 ) );
		toolBar = new XToolBar();
		toolBar.add( new XLabel( "To download your registration file, you need a Ticket which you can acquire on the " ).verticalBorder( 5 ) );
		toolBar.add( new Link( Consts.APP_OPERATOR_NAME + " User Page", Env.URL_APP_OP_USER_PAGE ) );
		toolBar.add( new XLabel( "." ).rightBorder( 20 ) );
		infoBox.add( toolBar );
		infoBox.add( Box.createVerticalStrut( 10 ) );
		cp.addNorth( infoBox );
		
		final GridBagPanel formPanel = new GridBagPanel();
		
		formPanel.nextRow();
		formPanel.addSingle( new XLabel( "Please copy the Ticket here:" ) );
		formPanel.c.weightx = 1;
		final XTextField ticketField = new XTextField();
		formPanel.addRemainder( ticketField );
		formPanel.c.weightx = 0;
		
		cp.addCenter( formPanel );
		
		final StatusLabel statusLabel = new StatusLabel();
		getButtonsPanel().add( statusLabel );
		
		final XButton downloadButton = new XButton( "_Download Registration File", Icons.F_DRIVE_DOWNLOAD.get() );
		downloadButton.addActionListener( new ActionAdapter() {
			@Override
			public void actionPerformed( final ActionEvent event ) {
				ticketField.setEnabled( false );
				downloadButton.setEnabled( false );
				statusLabel.setProgress( "Contacting the " + Consts.APP_OPERATOR_NAME );
				
				final RegFileDownloaderJob rfdJob = new RegFileDownloaderJob( ticketField.getText().trim() );
				rfdJob.setEdtCallback( new Runnable() {
					@Override
					public void run() {
						if ( rfdJob.getErrorMsg() == null ) {
							Sound.play( Sounds.THANKS_FOR_REGISTERING );
							statusLabel.setSuccess( "Success!" );
							GuiUtils.showInfoMsg( "Registration file downloaded and saved successfully.", "Please restart " + Consts.APP_NAME + "." );
							DlRegistrationFileDialog.this.close();
							return;
						}
						
						statusLabel.setError( rfdJob.getErrorMsg() );
						GuiUtils.showErrorMsg( rfdJob.getErrorDialogMsg() );
						ticketField.setEnabled( true );
						downloadButton.setEnabled( true );
					}
				} );
				rfdJob.start();
			}
		} );
		getButtonsPanel().add( downloadButton );
		getRootPane().setDefaultButton( downloadButton );
		addCloseButton( "_Close" );
		
		ticketField.getDocument().addDocumentListener( new DocumentAdapter( true ) {
			@Override
			public void handleEvent( final DocumentEvent event ) {
				downloadButton.setEnabled( !ticketField.getText().isEmpty() );
			}
		} );
	}
	
}
