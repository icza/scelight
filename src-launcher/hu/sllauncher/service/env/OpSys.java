/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.sllauncher.service.env;

import hu.scelightapibase.service.env.IOpSys;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.JFileChooser;

/**
 * Operating system.
 * 
 * @author Andras Belicza
 */
public enum OpSys implements IOpSys {
	
	/** Windows. */
	WINDOWS,
	
	/** MAC OS-X. */
	OS_X,
	
	/** Unix (including Linux). */
	UNIX,
	
	/** Solaris (Sun OS). */
	SOLARIS,
	
	/** Other. */
	OTHER;
	
	
	/** Documents path of the user. */
	private Path userDocumentsPath;
	
	/** Default SC2 install path on the OS. */
	private Path defSc2InstallPath;
	
	/** Default user dependent SC2-replay path on the OS. */
	private Path defSc2ReplayPath;
	
	/** Default SC2 maps cache path on the OS. */
	private Path defSc2MapsPath;
	
	
	/**
	 * Detects and returns the running operating system.
	 * 
	 * @return the running operating system
	 */
	public static OpSys detect() {
		final String osName = System.getProperty( "os.name" ).toLowerCase();
		
		if ( osName.indexOf( "win" ) >= 0 )
			return WINDOWS;
		else if ( osName.indexOf( "mac" ) >= 0 )
			return OS_X;
		else if ( osName.indexOf( "nix" ) >= 0 || osName.indexOf( "nux" ) >= 0 )
			return UNIX;
		else if ( osName.indexOf( "sunos" ) >= 0 )
			return SOLARIS;
		
		return OTHER;
	}
	
	
	/**
	 * Returns the user's Documents path.
	 * 
	 * @return the user's Documents path
	 */
	public Path getUserDocumentsPath() {
		initPaths();
		return userDocumentsPath;
	}
	
	/**
	 * Returns the default SC2 install path.
	 * 
	 * <p>
	 * Note: This is just the default path, the actual value can be changed in the Settings dialog.
	 * </p>
	 * 
	 * @return the default SC2 install path
	 */
	public Path getDefSc2InstallPath() {
		initPaths();
		return defSc2InstallPath;
	}
	
	/**
	 * Returns the default user dependent SC2-replay path.
	 * 
	 * <p>
	 * Note: This is just the default path, the actual value can be changed in the Settings dialog.
	 * </p>
	 * 
	 * @return the default user dependent SC2-replay path
	 */
	public Path getDefSc2ReplayPath() {
		initPaths();
		return defSc2ReplayPath;
	}
	
	/**
	 * Returns the default SC2 maps cache path.
	 * 
	 * <p>
	 * Note: This is just the default path, the actual value can be changed in the Settings dialog.
	 * </p>
	 * 
	 * @return the default SC2 maps cache path
	 */
	public Path getDefSc2MapsPath() {
		initPaths();
		return defSc2MapsPath;
	}
	
	/**
	 * Initializes OS dependent paths.
	 */
	private void initPaths() {
		// Init only once
		if ( userDocumentsPath != null )
			return;
		
		final String programFilesFolder, baseMapsFolder;
		
		userDocumentsPath = new JFileChooser().getFileSystemView().getDefaultDirectory().toPath();
		
		switch ( this ) {
			case WINDOWS :
				defSc2ReplayPath = userDocumentsPath.resolve( "StarCraft II/Accounts" );
				programFilesFolder = Files.exists( Paths.get( "C:/Program Files (x86)" ) ) ? "C:/Program Files (x86)" : "C:/Program Files";
				// Documents and app data folder differs in Windows XP:
				if ( "Windows XP".equals( System.getProperty( "os.name" ) ) ) {
					// baseAutoRepFolder = "/My Documents"; // User home relative!
					baseMapsFolder = "C:/Documents and Settings/All Users/Application Data/Blizzard Entertainment";
				} else {
					// baseAutoRepFolder = "/Documents"; // User home relative!
					baseMapsFolder = "C:/ProgramData/Blizzard Entertainment";
				}
				break;
			
			case OS_X :
				defSc2ReplayPath = Paths.get( System.getProperty( "user.home" ), "Library/Application Support/Blizzard", "StarCraft II/Accounts" );
				programFilesFolder = "/Applications";
				baseMapsFolder = "/Users/Shared/Blizzard";
				break;
			
			case UNIX :
			case SOLARIS :
			case OTHER :
			default :
				defSc2ReplayPath = Paths.get( System.getProperty( "user.home" ), "Documents", "StarCraft II/Accounts" );
				programFilesFolder = "/Applications";
				baseMapsFolder = "/Documents/Blizzard Entertainment";
				break;
		}
		
		defSc2InstallPath = Paths.get( programFilesFolder, "/StarCraft II" );
		defSc2MapsPath = Paths.get( baseMapsFolder, "/Battle.net/Cache" );
	}
	
	
	/** Cache of the values array. */
	public static final OpSys[] VALUES = values();
	
}
