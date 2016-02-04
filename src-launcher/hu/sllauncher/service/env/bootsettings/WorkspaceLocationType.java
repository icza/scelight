/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.sllauncher.service.env.bootsettings;

/**
 * Workspace location type.
 * 
 * @author Andras Belicza
 */
public enum WorkspaceLocationType {
	
	/** Workspace is located in the user home folder. */
	USER_HOME( "In User Home Folder" ),
	
	/** Workspace is located in the application folder. */
	APP_FOLDER( "In the Application Folder" ),
	
	/** Workspace is located in a custom folder. */
	CUSTOM_FOLDER( "In a Custom Folder" );
	
	
	/** Text value of the location type. */
	public final String text;
	
	/**
	 * Creates a new {@link WorkspaceLocationType}.
	 * 
	 * @param text text value of the location type
	 */
	private WorkspaceLocationType( final String text ) {
		this.text = text;
	}
	
	@Override
	public String toString() {
		return text;
	}
	
}
