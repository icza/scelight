/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight.gui.dialog.fileops;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.swing.BorderFactory;

import hu.scelight.action.Actions;
import hu.scelight.bean.repfolders.RepFolderBean;
import hu.scelight.gui.setting.SettingsGui;
import hu.scelight.service.env.Env;
import hu.scelight.service.settings.Settings;
import hu.scelight.util.Utils;
import hu.scelight.util.gui.GuiUtils;
import hu.sllauncher.gui.comp.GridBagPanel;
import hu.sllauncher.gui.comp.PathField;
import hu.sllauncher.gui.comp.XButton;
import hu.sllauncher.gui.comp.XCheckBox;
import hu.sllauncher.gui.comp.XDialog;
import hu.sllauncher.gui.comp.XLabel;
import hu.sllauncher.gui.comp.XProgressBar;
import hu.sllauncher.gui.comp.XTextField;
import hu.sllauncher.service.job.ProgressJob;
import hu.sllauncher.util.DurationFormat;
import hu.sllauncher.util.gui.adapter.ActionAdapter;

/**
 * File operations dialog.
 * 
 * @author Andras Belicza
 */
@SuppressWarnings( "serial" )
public class FileOpsDialog extends XDialog {
	
	/** File operation. */
	private final FileOp		fileOp;
								
	/** Source files to operate on. */
	private final List< Path >	fileList;
								
	/** Replay folder of the list (might be <code>null</code>). */
	private final RepFolderBean	repFolderBean;
								
	/** Target folder. */
	private Path				targetPath;
								
	/** Tells if we have to preserve subfolders relative to source Replay Folder. */
	private boolean				preserveSubfolders;
								
	/** Name of the pack output file. */
	private String				packOutputFileName;
								
								
	/** Progress bar to display file operation execution progress and general info. */
	private final XProgressBar	progressBar	= new XProgressBar();
											
	/** Abort button. */
	private XButton				abortButton	= new XButton( "_Abort" );
											
	/** Reference to the close button. */
	private XButton				closeButton;
								
	/** Operation execution job. */
	private ProgressJob			opJob;
								
	/**
	 * Creates a new {@link FileOpsDialog}.
	 * 
	 * @param fileOp file operation
	 * @param fileList source files to operate on
	 * @param repFolderBean replay folder of the list (might be <code>null</code>)
	 */
	public FileOpsDialog( final FileOp fileOp, final List< Path > fileList, final RepFolderBean repFolderBean ) {
		super( Env.MAIN_FRAME );
		
		this.fileOp = fileOp;
		this.fileList = fileList;
		this.repFolderBean = repFolderBean;
		
		setTitle( fileOp.text + " Replays (" + fileList.size() + ")" );
		setIconImage( fileOp.ricon.get().getImage() );
		
		setDefaultSize( new Dimension( 450, 190 + ( fileOp == FileOp.PACK ? 33 : 0 ) ) );
		
		buildGui();
		
		restoreDefaultSize();
		
		setModal( false );
		
		setVisible( true );
	}
	
	/**
	 * Builds the GUI of the email reps dialog.
	 */
	private void buildGui() {
		final GridBagPanel formPanel = new GridBagPanel();
		formPanel.setBorder( BorderFactory.createEmptyBorder( 5, 0, 5, 0 ) );
		
		formPanel.nextRow();
		formPanel.addSingle( new XLabel( "Target Folder:" ) );
		formPanel.c.weightx = 1;
		final PathField targetPathField = SettingsGui.createSettingPathField( Settings.TARGET_FOLDER, Env.APP_SETTINGS, null );
		formPanel.addRemainder( targetPathField );
		
		formPanel.nextRow();
		final XCheckBox preserveSubfoldersCheckBox = SettingsGui.createSettingCheckBox( Settings.PRESERVE_SUBFOLDERS, Env.APP_SETTINGS, null );
		if ( repFolderBean == null ) {
			preserveSubfoldersCheckBox.setEnabled( false );
		}
		formPanel.addRemainder( preserveSubfoldersCheckBox );
		
		final XTextField packOutputFileNameTextField;
		if ( fileOp == FileOp.PACK ) {
			formPanel.nextRow();
			formPanel.addSingle( new XLabel( "Output File Name:" ) );
			packOutputFileNameTextField = SettingsGui.createSettingTextField( Settings.PACK_OUTPUT_FILE_NAME, Env.APP_SETTINGS, null );
			formPanel.addRemainder( packOutputFileNameTextField );
		} else
			packOutputFileNameTextField = null;
			
		cp.addNorth( formPanel );
		
		progressBar.setStringPainted( true );
		progressBar.setString( "Press '" + fileOp.text + "' to start..." );
		progressBar.setMaximum( fileList.size() );
		cp.addCenter( progressBar );
		
		final XButton opButton = new XButton( fileOp.buttonText );
		opButton.addActionListener( new ActionAdapter() {
			@Override
			public void actionPerformed( final ActionEvent event ) {
				targetPath = targetPathField.getPath();
				if ( targetPathField.textField.getText().trim().isEmpty() || !Files.isDirectory( targetPath ) ) {
					GuiUtils.showErrorMsg( "Invalid Target Folder:", targetPath, targetPathField.getIndicator().getToolTipText() );
					return;
				}
				
				preserveSubfolders = preserveSubfoldersCheckBox.isEnabled() && preserveSubfoldersCheckBox.isSelected();
				packOutputFileName = packOutputFileNameTextField == null ? null : packOutputFileNameTextField.getText().trim();
				if ( packOutputFileNameTextField != null && packOutputFileName.isEmpty() ) {
					GuiUtils.showErrorMsg( "Output File Name cannot be empty!" );
					return;
				}
				
				GuiUtils.setComponentTreeEnabled( formPanel, false );
				opButton.setEnabled( false );
				execute();
				abortButton.setVisible( true );
				closeButton.setVisible( false );
			}
		} );
		getButtonsPanel().add( opButton );
		getRootPane().setDefaultButton( opButton );
		
		abortButton.addActionListener( new ActionAdapter() {
			@Override
			public void actionPerformed( final ActionEvent event ) {
				if ( opJob != null ) {
					opJob.requestCancel();
					abortButton.setEnabled( false );
				}
			}
		} );
		abortButton.setVisible( false );
		getButtonsPanel().add( abortButton );
		
		closeButton = addCloseButton( "_Cancel" );
	}
	
	/**
	 * Executes the file operation.
	 */
	private void execute() {
		opJob = new ProgressJob( getTitle(), fileOp.ricon) {
			private final int total			 = fileList.size();
											 
			final String	  totalString	 = ";   Total: " + Env.LANG.formatNumber( total ) + "   (";
											 
			final double	  totalHundredth = total / 100.0;
											 
											 
			@Override
			public void jobRun() {
				maximumProgress.set( total );
				publishStatus( false ); // Publish now (required anyway if total = 0)
				
				switch ( fileOp ) {
					case COPY :
					case MOVE :
						copyMove();
						break;
					case PACK :
						pack();
						break;
					default :
						Env.LOGGER.error( "Unhandled file operation: " + fileOp );
						requestCancel();
						break;
				}
				
				GuiUtils.runInEDT( new Runnable() {
					@Override
					public void run() {
						publishStatus( true );
						if ( cancelRequested )
							progressBar.setAborted();
						opJob = null;
						
						abortButton.setVisible( false );
						closeButton.setText( "_Close" );
						closeButton.setVisible( true );
					}
				} );
			}
			
			private void copyMove() {
				int i = 0;
				while ( !cancelRequested && i < fileList.size() ) {
					if ( waitIfPaused() )
						continue; // If execution was paused, "continue" so cancellation will be checked again first
						
					final Path src = fileList.get( i );
					Path dst = preserveSubfolders ? targetPath.resolve( repFolderBean.getPath().relativize( src ) ) : targetPath.resolve( src.getFileName() );
					try {
						Files.createDirectories( dst.getParent() );
						dst = Utils.uniqueFile( dst );
						if ( fileOp == FileOp.COPY ) {
							Files.copy( src, dst );
						} else {
							Files.move( src, dst );
						}
					} catch ( final IOException e ) {
						Env.LOGGER.error( "Failed to " + fileOp.text + " FROM " + src + " TO " + dst, e );
						if ( !GuiUtils.askRetry( "Failed to " + fileOp.text + " FROM:", src, " TO:", dst, " ",
		                        GuiUtils.linkForAction( "View Logs...", Actions.ABOUT_LOGS ) ) ) {
							requestCancel();
						}
						continue;
					}
					currentProgress.set( i + 1 );
					publishStatus( false );
					i++;
				}
			}
			
			public void pack() {
				if ( !packOutputFileName.toLowerCase().endsWith( ".zip" ) ) {
					packOutputFileName += ".zip";
				}
				final Path zipFile = targetPath.resolve( packOutputFileName );
				final Set< Path > entrySet = new HashSet< >();
				try ( final ZipOutputStream zos = new ZipOutputStream( Files.newOutputStream( zipFile ) ) ) {
					
					int i = 0;
					while ( !cancelRequested && i < fileList.size() ) {
						if ( waitIfPaused() )
							continue; // If execution was paused, "continue" so cancellation will be checked again first
							
						final Path src = fileList.get( i );
						Path dst = preserveSubfolders ? repFolderBean.getPath().relativize( src ) : src.getFileName();
						try {
							dst = Utils.uniqueFile( dst, entrySet );
							
							try {
								// I add zip entry in the try-catch block because adding the same entry raises exception
								final ZipEntry zipEntry = new ZipEntry( dst.toString() );
								zipEntry.setTime( Files.getLastModifiedTime( src ).toMillis() );
								zos.putNextEntry( zipEntry );
								
								Files.copy( src, zos );
							} catch ( final Exception e ) {
								throw e instanceof IOException ? (IOException) e : new IOException( e );
							} finally {
								zos.closeEntry();
							}
							
						} catch ( final IOException e ) {
							Env.LOGGER.error( "Failed to Pack " + src + " into " + zipFile, e );
							GuiUtils.showErrorMsg( "Failed to Pack file:", src, "Into:", zipFile, " " );
							requestCancel();
							continue;
						}
						currentProgress.set( i + 1 );
						publishStatus( false );
						i++;
					}
					
				} catch ( final IOException e ) {
					Env.LOGGER.error( "Failed to pack to file: " + zipFile, e );
					GuiUtils.showErrorMsg( "Failed to pack to file: " + zipFile );
					requestCancel();
				}
			}
			
			private void publishStatus( final boolean ended ) {
				GuiUtils.runInEDT( new Runnable() {
					@Override
					public void run() {
						final int processed = currentProgress.get();
						
						final StringBuilder sb = new StringBuilder( 80 );
						sb.append( fileOp.processedText ).append( ": " ).append( Env.LANG.formatNumber( currentProgress.get() ) ).append( totalString )
		                        .append( Env.LANG.formatNumber( total == 0 ? 0 : processed / totalHundredth, 2 ) ).append( "%)" );
								
						if ( !ended ) {
							final long time = getExecTimeMs();
							sb.append( "   [Time: " ).append( DurationFormat.AUTO.formatDuration( time ) ).append( ";  ETA: " )
		                            .append( processed == 0 ? "- " : DurationFormat.AUTO.formatDuration( time * ( total - processed ) / processed ) )
		                            .append( ']' );
						}
						
						progressBar.setString( sb.toString() );
						
						progressBar.setValue( processed );
					}
				} );
			}
		};
		
		opJob.start();
	}
	
	@Override
	protected void customOnClose() {
		// TODO
		// ask confirmation if operation execution is in progress
		// and stop it
	}
	
}
