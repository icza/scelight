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

import hu.scelightapibase.util.IEnum;
import hu.scelightapibase.util.gui.HasRIcon;
import hu.sllauncher.gui.comp.StatusLabel.StatusType;

import java.awt.Color;

/**
 * An {@link ILabel} with an indicator icon ({@link ILabel}) to display the status of a background process.
 * 
 * @author Andras Belicza
 * 
 * @see hu.scelightapi.service.IGuiFactory#newStatusLabel()
 * @see hu.scelightapi.service.IGuiFactory#newStatusLabel(IStatusLabel.IStatusType, String)
 */
public interface IStatusLabel extends ILabel {
	
	/**
	 * Status type.
	 * 
	 * @author Andras Belicza
	 * 
	 * @see IEnum
	 */
	public interface IStatusType extends HasRIcon, IEnum {
		
		/** Not available. */
		IStatusType NA           = StatusType.NA;
		
		/** Progress. */
		IStatusType PROGRESS     = StatusType.PROGRESS;
		
		/** Warning. */
		IStatusType WARNING      = StatusType.WARNING;
		
		/** Warning (error-like). */
		IStatusType HARD_WARNING = StatusType.HARD_WARNING;
		
		/** Error. */
		IStatusType ERROR        = StatusType.ERROR;
		
		/** Success. */
		IStatusType SUCCESS      = StatusType.SUCCESS;
		
		
		/**
		 * Returns the foreground color of the status type.
		 * 
		 * @return the foreground color of the status type
		 */
		Color getColor();
		
	}
	
	
	/**
	 * Sets the status by its type.
	 * 
	 * @param type status type to be set
	 * @param message status message to be set
	 */
	void setByType( IStatusType type, String message );
	
	/**
	 * Sets a not available state.
	 * 
	 * @param message NA message to be set
	 */
	void setNotAvailable( String message );
	
	/**
	 * Sets a progress state.
	 * 
	 * @param message progress message to be set
	 */
	void setProgress( String message );
	
	/**
	 * Sets a warning status.
	 * 
	 * @param message warning message to be set
	 */
	void setWarning( String message );
	
	/**
	 * Sets a hard warning status.
	 * 
	 * @param message hard warning message to be set
	 */
	void setHardWarning( String message );
	
	/**
	 * Sets an error status.
	 * 
	 * @param message error message to be set
	 */
	void setError( String message );
	
	/**
	 * Sets a success status.
	 * 
	 * @param message success message to be set
	 */
	void setSuccess( String message );
	
	/**
	 * Clears the status. Resets to default state.
	 */
	void clear();
	
}
