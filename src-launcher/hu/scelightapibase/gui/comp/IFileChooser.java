/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelightapibase.gui.comp;

import java.nio.file.Path;

import javax.swing.JFileChooser;

/**
 * An improved {@link JFileChooser}.
 * 
 * @author Andras Belicza
 * 
 * @see hu.scelightapi.service.IGuiFactory#newFileChooser()
 * @see hu.scelightapi.service.IGuiFactory#newFileChooser(Path)
 */
public interface IFileChooser {
	
	/**
	 * Casts this instance to {@link JFileChooser}.
	 * 
	 * @return <code>this</code> as a {@link JFileChooser}
	 */
	JFileChooser asFileChooser();
	
	/**
	 * Sets the current folder.
	 * 
	 * <p>
	 * If the specified folder does not exists, its parents will be tried, recursively.
	 * </p>
	 * 
	 * @param currentFolder current folder to be set
	 */
	void setCurrentFolder( Path currentFolder );
	
	/**
	 * Returns the selected path.
	 * 
	 * @return the selected path
	 */
	Path getSelectedPath();
	
}
