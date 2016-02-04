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

/**
 * An {@link IIndicatorTextField} path editor field with an {@link IButton} which shows an {@link IFileChooser}.
 * 
 * <p>
 * The field has 2 modes based on whether it is to choose a folder or a file.
 * </p>
 * 
 * @author Andras Belicza
 * 
 * @see hu.scelightapi.service.IGuiFactory#newPathField()
 * @see hu.scelightapi.service.IGuiFactory#newPathField(boolean)
 * @see hu.scelightapi.service.IGuiFactory#newPathField(Path)
 * @see hu.scelightapi.service.IGuiFactory#newPathField(Path, boolean)
 */
public interface IPathField extends IIndicatorTextField {
	
	/**
	 * Returns the button to open an {@link IFileChooser}.
	 * 
	 * @return the button to open an {@link IFileChooser}
	 */
	IButton getButton();
	
	/**
	 * Returns the file chooser displayed when the button is clicked.
	 * 
	 * @return the file chooser displayed when the button is clicked
	 */
	IFileChooser getFileChooser();
	
	/**
	 * Tells if a path has been entered.
	 * 
	 * @return true if a path has been entered; false otherwise
	 */
	boolean hasPath();
	
	/**
	 * Returns the selected path.
	 * 
	 * @return the selected path or an empty path (<code>Paths.get("")</code>) if no valid path has been entered
	 */
	Path getPath();
	
	/**
	 * Sets the specified path.
	 * 
	 * @param path path to be set
	 */
	void setPath( Path path );
	
}
