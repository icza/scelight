/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.sllauncher.gui.comp;

import hu.scelightapibase.gui.comp.IButton;
import hu.scelightapibase.gui.comp.IFileChooser;
import hu.scelightapibase.gui.comp.IPathField;
import hu.scelightapibase.gui.comp.ITextField.IValidator;
import hu.sllauncher.gui.icon.LIcons;
import hu.sllauncher.util.gui.adapter.ActionAdapter;

import java.awt.event.ActionEvent;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.SwingUtilities;

/**
 * An {@link IndicatorTextField} path editor field with an {@link XButton} which shows an {@link XFileChooser}.
 * 
 * <p>
 * The field has 2 modes based on whether it is to choose a folder or a file.
 * </p>
 * 
 * @author Andras Belicza
 */
@SuppressWarnings( "serial" )
public class PathField extends IndicatorTextField implements IPathField {
	
	/** Button to open an {@link XFileChooser}. */
	public final XButton      button;
	
	/** File chooser displayed when the button is clicked. */
	public final XFileChooser fileChooser = new XFileChooser();
	
	/**
	 * Creates a new {@link PathField} in folder mode.
	 */
	public PathField() {
		this( null );
	}
	
	/**
	 * Creates a new {@link PathField} in the specified mode.
	 * 
	 * @param fileMode if true, the field operates as a file chooser; else as a folder chooser
	 */
	public PathField( final boolean fileMode ) {
		this( null, fileMode );
	}
	
	/**
	 * Creates a new {@link PathField} in folder mode.
	 * 
	 * @param path initial path to be set
	 */
	public PathField( final Path path ) {
		this( path, false );
	}
	
	/**
	 * Creates a new {@link PathField} in the specified mode.
	 * 
	 * @param path initial path to be set
	 * @param fileMode if true, the field operates as a file chooser; else as a folder chooser
	 */
	public PathField( final Path path, final boolean fileMode ) {
		super( path == null ? null : path.toString() );
		
		textField.setValidator( new IValidator() {
			@Override
			public boolean validate( final String text ) {
				if ( text.isEmpty() ) {
					setEmpty( fileMode ? "No file entered." : "No folder entered." );
					return true;
				}
				
				try {
					if ( !Files.exists( Paths.get( text ) ) )
						setNotAccepted( fileMode ? "File does not exists!" : "Folder does not exist!" );
					else {
						if ( Files.isDirectory( Paths.get( text ) ) ^ fileMode )
							setAccepted( fileMode ? "File exists." : "Folder exists." );
						else
							setNotAccepted( fileMode ? "Entered path is a folder not a file!" : "Entered path is a file not a folder!" );
					}
				} catch ( final Exception e ) {
					setError( fileMode ? "The entered file name is invalid!" : "The entered folder name is invalid!" );
					return false;
				}
				return true;
			}
		} );
		
		button = new XButton( fileMode ? LIcons.F_DOCUMENT.get() : LIcons.F_FOLDER.get() );
		button.setToolTipText( fileMode ? "Choose file..." : "Choose folder..." );
		fileChooser.setApproveButtonText( "Choose" );
		fileChooser.setFileSelectionMode( fileMode ? XFileChooser.FILES_ONLY : XFileChooser.DIRECTORIES_ONLY );
		button.addActionListener( new ActionAdapter() {
			@Override
			public void actionPerformed( final ActionEvent event ) {
				if ( hasPath() ) {
					final Path p = getPath();
					if ( Files.exists( p ) )
						fileChooser.setSelectedFile( getPath().toFile() );
					else
						fileChooser.setCurrentFolder( p );
				}
				if ( XFileChooser.APPROVE_OPTION == fileChooser.showOpenDialog( SwingUtilities.windowForComponent( button ) ) )
					setText( fileChooser.getSelectedFile().toString() );
			}
		} );
		indicatorBox.add( button );
	}
	
	@Override
	public IButton getButton() {
		return button;
	}
	
	@Override
	public IFileChooser getFileChooser() {
		return fileChooser;
	}
	
	@Override
	public boolean hasPath() {
		return !getText().isEmpty();
	}
	
	@Override
	public Path getPath() {
		try {
			return Paths.get( getText() );
		} catch ( final InvalidPathException ipe ) {
			return Paths.get( "" );
		}
	}
	
	@Override
	public void setPath( final Path path ) {
		setText( path.toString() );
	}
	
}
