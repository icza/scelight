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

import hu.scelightapibase.gui.comp.IFileChooser;

import java.awt.Dimension;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.swing.JFileChooser;

/**
 * An improved {@link JFileChooser}.
 * 
 * @author Andras Belicza
 */
@SuppressWarnings( "serial" )
public class XFileChooser extends JFileChooser implements IFileChooser {
	
	/**
	 * Creates a new {@link XFileChooser}.
	 */
	public XFileChooser() {
		this( null );
	}
	
	/**
	 * Creates a new {@link XFileChooser}.
	 * 
	 * @param currentFolder default folder to navigate to
	 */
	public XFileChooser( final Path currentFolder ) {
		super( currentFolder == null ? null : currentFolder.toFile() );
		
		// Make preferred size a little smaller than the default size of dialogs, so if a file chooser is opened, it will not cover the dialog entirely.
		// Also note that this preferred size is not the parent dialog size, so the difference does not include the dialog decoration!
		setPreferredSize( new Dimension( XDialog.DEFAULT_DEFAULT_SIZE.width - 100, XDialog.DEFAULT_DEFAULT_SIZE.height - 100 ) );
	}
	
	@Override
	public XFileChooser asFileChooser() {
		return this;
	}
	
	@Override
	public void setCurrentFolder( Path currentFolder ) {
		while ( currentFolder != null && !Files.exists( currentFolder ) )
			currentFolder = currentFolder.getParent();
		
		if ( currentFolder != null )
			setCurrentDirectory( currentFolder.toFile() );
	}
	
	@Override
	public Path getSelectedPath() {
		return getSelectedFile().toPath();
	}
	
}
