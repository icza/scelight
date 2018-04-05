/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.sllauncher.service.updater;

import hu.scelightopapibase.ScelightOpApiBase;
import hu.sllauncher.LConsts;
import hu.sllauncher.ScelightLauncher;
import hu.sllauncher.bean.VersionBean;
import hu.sllauncher.bean.module.ExtModRefBean;
import hu.sllauncher.bean.module.FileBean;
import hu.sllauncher.bean.module.ModuleBean;
import hu.sllauncher.bean.module.ModulesBean;
import hu.sllauncher.bean.module.ModulesBeanOrigin;
import hu.sllauncher.gui.LauncherFrame;
import hu.sllauncher.gui.comp.Browser;
import hu.sllauncher.gui.comp.StatusLabel.StatusType;
import hu.sllauncher.gui.comp.XScrollPane;
import hu.sllauncher.gui.icon.LIcons;
import hu.sllauncher.gui.page.extmods.OffExtModConfBean;
import hu.sllauncher.gui.page.extmods.OffExtModConfsBean;
import hu.sllauncher.service.env.LEnv;
import hu.sllauncher.service.settings.LSettings;
import hu.sllauncher.service.sound.LSounds;
import hu.sllauncher.service.sound.Sound;
import hu.sllauncher.util.Holder;
import hu.sllauncher.util.LRHtml;
import hu.sllauncher.util.LUtils;
import hu.sllauncher.util.UrlBuilder;
import hu.sllauncher.util.gui.LGuiUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.FileVisitOption;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.xml.bind.JAXB;

/**
 * Application updater.
 * 
 * @author Andras Belicza
 */
public class Updater {
	
	/** Cache this bean here to avoid inconsistency in case it is modified during update. */
	private final OffExtModConfsBean offExtModConfsBean = LEnv.LAUNCHER_SETTINGS.get( LSettings.OFF_EXT_MOD_CONFS );
	
	/** Reference to the launcher. */
	private final ScelightLauncher   launcher           = ScelightLauncher.INSTANCE();
	
	/** Reference to the launcher frame. */
	private final LauncherFrame      launcherFrame      = LEnv.LAUNCHER_FRAME;
	
	/** Modules bean. */
	private ModulesBean              modules;
	
	/** Number of all mods that needs to be checked (including launcher, internal and external mods). */
	private int                      checkableModsCount;
	
	/** Number of checked mods (including launcher, internal and external mods). */
	private int                      checkedModsCount;
	
	/** Tells if failed to check/update some external modules. */
	private boolean                  failedExtMods;
	
	
	/**
	 * Creates a new {@link Updater}.
	 */
	public Updater() {
		// Default text for the proceed button:
		launcherFrame.setProceedText( "<html><h2>_START</h2></html>" );
		
		try {
			
			checkEclipseMode();
			
			retrieveModulesBean();
			
			checkReqMinLauncherVer();
			
			checkLauncherMod();
			
			checkInternalMods();
			
			checkExternalMods();
			
			removeOldVersions();
			
			// Application is up-to-date, ready to be launched.
			setReady();
			
		} catch ( final FinishException fe ) {
			
			// In some cases start might still be allowed (e.g. no connection and registered):
			if ( !allowToStart( fe ) )
				return;
			
		} finally {
			launcherFrame.setProgressIndeterminate( false );
		}

		
		// Enable/allow start
		launcherFrame.setProceedText( "<html><h2>_START</h2></html>" );
		launcher.setProceedAction( launcher.PROCEED_ACTION_START );
		launcherFrame.setProceedEnabled( true );
		
		// Auto-start?
		if ( LEnv.LAUNCHER_SETTINGS.get( LSettings.AUTO_START_WHEN_READY ) ) {
			// If we got this far, start is allowed.
			// If modules bean's origin is not APP_OPERATOR => no connection
			if ( modules.getOrigin() == ModulesBeanOrigin.APP_OPERATOR || LEnv.LAUNCHER_SETTINGS.get( LSettings.AUTO_START_WHEN_NO_CONN ) )
				SwingUtilities.invokeLater( launcher.PROCEED_ACTION_START );
		}
	}
	
	/**
	 * Checks for Eclipse mode and if so, sets up the Launcher for Eclipse mode.
	 * 
	 * @throws FinishException if the updater should finish and return
	 */
	private void checkEclipseMode() {
		if ( !LEnv.ECLIPSE_MODE )
			return;
		
		LEnv.LOGGER.debug( "Eclipse mode: skipping update check." );
		
		modules = JAXB.unmarshal( Paths.get( "../release/deployment-dev/modules.xml" ).toFile(), ModulesBean.class );
		modules.setOrigin( ModulesBeanOrigin.ECLIPSE_MODULES_XML );
		launcher.setModules( modules );
		
		publishModCounts();
		
		throw new FinishException();
	}
	
	/**
	 * Retrieves the modules bean from the Application operator.
	 * 
	 * @throws FinishException if the updater should finish and return
	 */
	private void retrieveModulesBean() {
		// Check for updates: retrieve modules info
		// Try again a few times, block start if all fail and not registered
		for ( int retries = 1;; retries++ ) {
			if ( retries == 1 ) {
				LEnv.LOGGER.info( "Checking for updates..." );
				launcherFrame.setStatus( StatusType.PROGRESS, "Checking for updates" );
			} else {
				LEnv.LOGGER.info( "Checking for updates (" + retries + ")..." );
				launcherFrame.setStatus( StatusType.PROGRESS, "Failed to check updates! Retrying (" + retries + ")" );
			}
			
			try {
				
				final UrlBuilder urlBuilder = new UrlBuilder( LEnv.URL_MODULES_BEAN_GZ );
				urlBuilder.addParam( ScelightOpApiBase.PARAM_MODULES_BEAN_SKILL_LEVEL, LEnv.LAUNCHER_SETTINGS.get( LSettings.SKILL_LEVEL ).name() );

				urlBuilder.addTimestamp();
				
				try ( final InputStream in = new GZIPInputStream( urlBuilder.toUrl().openStream() ) ) {
					modules = JAXB.unmarshal( in, ModulesBean.class );
				}
				modules.setOrigin( ModulesBeanOrigin.APP_OPERATOR );
				launcher.setModules( modules );
				
				// Count modules
				checkableModsCount = 1; // 1 for the launcher
				checkableModsCount += modules.getModList().size(); // Internal modules (all checked)
				if ( modules.getExtModRefList() != null )
					for ( final ExtModRefBean extModRef : modules.getExtModRefList() ) {
						// Is ext mod set to auto-update?
						final OffExtModConfBean conf = offExtModConfsBean.getModuleConfForFolder( extModRef.getFolder() );
						if ( conf != null && Boolean.TRUE.equals( conf.getAutoUpdate() ) )
							checkableModsCount++;
					}
				publishModCounts();
				break;
				
			} catch ( final Exception e ) {
				
				if ( retries < 3 )
					LEnv.LOGGER.error( "Failed to check updates!", e );
				else {
					LEnv.LOGGER.error( "Failed to check updates! You must restart " + LConsts.LAUNCHER_NAME + " to retry!", e );
					launcherFrame.setStatus( StatusType.ERROR, "Failed to check updates! You must restart " + LConsts.LAUNCHER_NAME + " to retry!" );
					launcherFrame.setModuleCounts( 0, 0 );
					throw new FinishException();
				}
				
			}
		}
	}
	
	/**
	 * Returns a fake modules bean which contains the files of the highest internal module versions.
	 * 
	 * @return a fake modules bean which contains the files of the highest internal module versions
	 */
	private static ModulesBean createFakeModulesBean() {
		// Only 1 version of modules (the latest) is kept in their module folders.
		// External modules are not included, they are not relevant for the startup class path (they are launched
		// explicitely by the ExtModManager).
		
		// Internal modules:
		final List< ModuleBean > modList = detectInstalledModules( LEnv.PATH_MODS );
		if ( modList == null )
			return null;
		
		// Launcher module is special:
		ModuleBean launcherMb = null;
		ModuleBean appMb = null;
		for ( final ModuleBean mb : modList )
			switch ( mb.getFolder() ) {
				case "launcher" :
					launcherMb = mb;
					break;
				case "app" :
					appMb = mb;
					break;
			}
		
		if ( launcherMb == null ) {
			LEnv.LOGGER.error( "Could not find installed launcher module in modules folder: " + LEnv.PATH_MODS );
			return null;
		}
		modList.remove( launcherMb );
		
		if ( appMb == null || appMb.getFileList() == null || appMb.getFileList().isEmpty() ) {
			LEnv.LOGGER.error( LConsts.APP_NAME + " is not downloaded, cannot proceed to start!" );
			return null;
		}
		
		final ModulesBean msb = new ModulesBean();
		msb.setLauncherMod( launcherMb );
		msb.setModList( modList );
		
		return msb;
	}
	
	/**
	 * Returns the list of installed modules in the specified modules folder.
	 * 
	 * @param modsPath modules folder in which to search installed modules
	 * @return the list of installed modules in the specified modules folder; <code>null</code> if the specified modules folder could not be scanned
	 */
	public static List< ModuleBean > detectInstalledModules( final Path modsPath ) {
		final List< ModuleBean > modList = new ArrayList<>();
		
		// Walk through module folders
		try {
			Files.walkFileTree( modsPath, Collections.< FileVisitOption > emptySet(), 1, new SimpleFileVisitor< Path >() {
				@Override
				public FileVisitResult visitFile( final Path modFolder, final BasicFileAttributes attrs ) throws IOException {
					if ( !attrs.isDirectory() )
						return FileVisitResult.CONTINUE;
					
					final Path latestVersionPath = getLatestVersionSubfolder( modFolder );
					if ( latestVersionPath == null )
						return FileVisitResult.CONTINUE; // Not a "real" module folder, no version subfolder...
						
					// Create a module bean for the module
					final ModuleBean mb = new ModuleBean();
					mb.setFolder( modFolder.getFileName().toString() );
					mb.setName( mb.getFolder() ); // We don't know the name, use the folder as name
					final List< FileBean > fileList = new ArrayList<>();
					
					// Walk through module files (only from the latest version)
					Files.walkFileTree( latestVersionPath, new SimpleFileVisitor< Path >() {
						@Override
						public FileVisitResult visitFile( final Path file, final BasicFileAttributes attrs ) throws IOException {
							if ( !attrs.isDirectory() ) {
								// Found a module file, create file bean for it:
								final FileBean fb = new FileBean();
								fb.setPath( Paths.get( "Scelight" ).resolve( LEnv.PATH_APP.relativize( file ) ).toString() );
								fileList.add( fb );
							}
							return FileVisitResult.CONTINUE;
						}
					} );
					mb.setFileList( fileList );
					
					modList.add( mb );
					
					return FileVisitResult.CONTINUE;
				}
			} );
		} catch ( final IOException ie ) {
			LEnv.LOGGER.error( "Could not scan modules folder: " + modsPath, ie );
			return null;
		}
		
		return modList;
	}
	
	/**
	 * Returns the latest version subfolder of the specified module folder.
	 * 
	 * @param modFolder module folder to look latest version subfolder in
	 * @return the latest version subfolder of the specified module folder or <code>null</code> if the specified module folder contains no folders that are
	 *         valid version strings
	 * @throws IOException if scanning the specified module folder throws an {@link IOException}
	 */
	public static Path getLatestVersionSubfolder( final Path modFolder ) throws IOException {
		// Holders because "final" is needed to be accessible from visitFile()
		// (I store the path too because version folder name is ambiguous (e.g. "1.0" and "1.0.0")
		final Holder< Path > latestVersionPath = new Holder<>();
		final Holder< VersionBean > latestVersion = new Holder<>();
		
		Files.walkFileTree( modFolder, Collections.< FileVisitOption > emptySet(), 1, new SimpleFileVisitor< Path >() {
			@Override
			public FileVisitResult visitFile( final Path file, final BasicFileAttributes attrs ) throws IOException {
				if ( !attrs.isDirectory() )
					return FileVisitResult.CONTINUE;
				
				final VersionBean version = VersionBean.fromString( file.getFileName().toString() );
				if ( version != null )
					if ( latestVersion.value == null || version.compareTo( latestVersion.value ) > 0 ) {
						latestVersion.value = version;
						latestVersionPath.value = file;
					}
				
				return FileVisitResult.CONTINUE;
			}
		} );
		
		return latestVersionPath.value;
	}
	
	/**
	 * Checks the minimum required launcher version condition.
	 * 
	 * @throws FinishException if the updater should finish and return
	 */
	private void checkReqMinLauncherVer() {
		if ( LConsts.LAUNCHER_VERSION.compareTo( modules.getReqMinLauncherVer() ) >= 0 )
			return; // 'ts all cool
			
		LEnv.LOGGER.error( "Cannot proceed with update because it requires a newer version of " + LConsts.LAUNCHER_NAME + " (our version: "
		        + LConsts.LAUNCHER_VERSION + ", required min version: " + modules.getReqMinLauncherVer() + ")! You must download it manually!" );
		launcherFrame.setStatus( StatusType.ERROR, "Cannot proceed with update because it requires a newer version of " + LConsts.LAUNCHER_NAME
		        + "! You must download it manually!" );
		
		throw new FinishException();
	}
	
	/**
	 * Checks the launcher module (and also updates/repairs it).
	 * 
	 * @throws FinishException if the updater should finish and return
	 */
	private void checkLauncherMod() {
		// Launcher module
		if ( LConsts.LAUNCHER_VERSION.equals( modules.getLauncherMod().getVersion() ) )
			validateModule( modules.getLauncherMod(), false );
		else
			updateModule( modules.getLauncherMod(), false, false );
		
		acknowledgeModChecked();
	}
	
	/**
	 * Checks the internal modules (and also updates/repairs them).
	 * 
	 * @throws FinishException if the updater should finish and return
	 */
	private void checkInternalMods() {
		if ( modules.getModList() == null )
			return;
		
		for ( final ModuleBean mod : modules.getModList() ) {
			final Path intModPath = LEnv.PATH_MODS.resolve( mod.getFolder() ).resolve( mod.getVersion().toString() );
			
			if ( Files.exists( intModPath ) && Files.isDirectory( intModPath ) )
				validateModule( mod, false );
			else
				updateModule( mod, false, false );
			
			acknowledgeModChecked();
		}
	}
	
	/**
	 * Checks the external modules that are set to auto-update (and also updates/repairs them).
	 * 
	 * @throws FinishException if the updater should finish and return
	 */
	private void checkExternalMods() {
		if ( modules.getExtModRefList() == null )
			return;
		
		for ( final ExtModRefBean extModRef : modules.getExtModRefList() ) {
			// Is ext mod set to auto-update?
			final OffExtModConfBean conf = offExtModConfsBean.getModuleConfForFolder( extModRef.getFolder() );
			if ( conf == null || !Boolean.TRUE.equals( conf.getAutoUpdate() ) )
				continue;
			
			launcherFrame.setProgressIndeterminate( true );
			final ModuleBean mod = retrieveExtModuleBean( extModRef );
			if ( mod == null ) {
				failedExtMods = true;
				continue;
			}
			
			if ( modules.getRetrievedExtModList() == null )
				modules.setRetrievedExtModList( new ArrayList< ModuleBean >() );
			modules.getRetrievedExtModList().add( mod );
			
			final Path extModPath = LEnv.PATH_EXT_MODS.resolve( mod.getFolder() ).resolve( mod.getVersion().toString() );
			// Do not abort update if an external module fails...
			try {
				if ( Files.exists( extModPath ) && Files.isDirectory( extModPath ) )
					validateModule( mod, true );
				else
					updateModule( mod, true, false );
			} catch ( final FinishException fe ) {
				// ...just signal the fact.
				failedExtMods = true;
			}
			
			acknowledgeModChecked();
		}
	}
	
	/**
	 * Retrieves the specified external module bean.
	 * 
	 * @param extModRef reference of the external module whose bean to retrieve
	 * @return the retrieved module bean or <code>null</code> if bean could not be retrieved (or is invalid)
	 */
	private ModuleBean retrieveExtModuleBean( final ExtModRefBean extModRef ) {
		// Try again a few times
		for ( int retries = 1;; retries++ ) {
			if ( retries == 1 ) {
				LEnv.LOGGER.info( "Checking for " + extModRef.getName() + " updates..." );
				launcherFrame.setStatus( StatusType.PROGRESS, "Checking for " + extModRef.getName() + " updates" );
			} else {
				LEnv.LOGGER.info( "Checking for " + extModRef.getName() + " updates (" + retries + ")..." );
				launcherFrame.setStatus( StatusType.PROGRESS, "Failed to check " + extModRef.getName() + " updates! Retrying (" + retries + ")" );
			}
			
			try {
				final ModuleBean mod = JAXB.unmarshal( new UrlBuilder( extModRef.getModuleBeanUrl() ).addTimestamp().toUrl(), ModuleBean.class );
				
				if ( !extModRef.getName().equals( mod.getName() ) )
					throw new Exception( "Invalid module bean, mismatching name specified: " + mod.getName() + "; was expecting: " + extModRef.getName() );
				
				if ( !extModRef.getFolder().equals( mod.getFolder() ) )
					throw new Exception( "Invalid module bean, mismatching folder specified: " + mod.getFolder() + "; was expecting: " + extModRef.getFolder() );
				
				// At this point module bean specifies the referenced module.
				
				if ( mod.getFileList() == null )
					throw new Exception( "Invalid module bean, no file list specified!" );
				
				// Quarantine check:
				final String pathPrefix = "Scelight/" + LEnv.PATH_EXT_MODS.getFileName().toString() + "/" + extModRef.getFolder() + "/";
				for ( final FileBean file : mod.getFileList() ) {
					if ( file.getPath().indexOf( ".." ) >= 0 || !file.getPath().startsWith( pathPrefix ) )
						throw new Exception( "Invalid module bean, disallowed listed file path: " + file.getPath() );
				}
				
				return mod;
			} catch ( final Exception e ) {
				if ( retries < 3 )
					LEnv.LOGGER.error( "Failed to check " + extModRef.getName() + " updates!", e );
				else {
					Sound.beepOnError();
					LEnv.LOGGER.error( "Failed to check " + extModRef.getName() + " updates! You must restart " + LConsts.LAUNCHER_NAME + " to retry!", e );
					launcherFrame.setStatus( StatusType.WARNING, "Failed to check " + extModRef.getName() + " updates! You must restart "
					        + LConsts.LAUNCHER_NAME + " to retry!" );
					return null;
				}
			}
		}
	}
	
	/**
	 * Validates a module (checks file digests of the module).
	 * 
	 * @param module module to be validated
	 * @param external tells if the module is an external module
	 * @throws FinishException if the updater should finish and return
	 */
	private void validateModule( final ModuleBean module, final boolean external ) {
		LEnv.LOGGER.debug( "Validating " + module.getName() + "..." );
		launcherFrame.setStatus( StatusType.PROGRESS, "Validating " + module.getName() );
		
		boolean ok = true;
		for ( final FileBean file : module.getFileList() ) {
			Path filePath = Paths.get( file.getPath() );
			filePath = LEnv.PATH_APP.resolve( filePath.subpath( 1, filePath.getNameCount() ) );
			
			if ( !file.getSha256().equals( LUtils.calculateFileSha256( filePath ) ) ) {
				ok = false;
				LEnv.LOGGER.error( module.getName() + " validation failed, " + ( Files.exists( filePath ) ? "corrupt" : "missing" ) + " file: "
				        + LEnv.PATH_APP.relativize( filePath ) );
				if ( module == modules.getLauncherMod() && launcher.isClassPathEntry( file ) ) {
					// Launcher's class path entries are locked and therefore cannot be repaired from "within"!
					Sound.beepOnError();
					LEnv.LOGGER.error( module.getName() + " validation failed and cannot be repaired! You must download " + module.getName() + " manually!" );
					launcherFrame.setStatus( StatusType.ERROR,
					        module.getName() + " validation failed and cannot be repaired! You must download " + module.getName() + " manually!" );
					throw new FinishException();
				}
			}
		}
		
		if ( ok )
			LEnv.LOGGER.debug( module.getName() + " validation OK." );
		else
			updateModule( module, external, true );
	}
	
	/**
	 * Updates or repairs the specified module.
	 * 
	 * @param module module to be updated or repaired
	 * @param external tells if the module is an external module
	 * @param repair tells if this is a repair update (validation failed)
	 * @throws FinishException if the updater should finish and return
	 */
	private void updateModule( final ModuleBean module, final boolean external, final boolean repair ) {
		LEnv.LOGGER.info( ( repair ? "Repairing " : "Updating " ) + module.getName() + "..." );
		launcherFrame.setStatus( StatusType.PROGRESS, ( repair ? "Repairing " : "Updating " ) + module.getName() );
		
		if ( module == modules.getLauncherMod() ) {
			// Launcher update requires restart, show RESTART action while updating the launcher
			launcherFrame.setProceedText( "<html><h2>RE_START</h2></html>" );
		}
		
		final Path tempPath = ( external ? LEnv.PATH_EXT_MODS : LEnv.PATH_MODS ).resolve( "_update" );
		try {
			// Create temp update folder
			while ( !LUtils.deletePath( tempPath ) )
				if ( !LGuiUtils.askRetry( "Could not delete folder:", tempPath ) )
					throw new Exception( "Could not delete folder: " + tempPath );
			Files.createDirectory( tempPath );
			
			final Path archivePath = tempPath.resolve( module.getArchiveFile().getPath() );
			
			// Download module archive, try mirrors if one fails
			final byte[] buffer = new byte[ 16_384 ]; // 16 KB work buffer
			for ( int urlIdx = 0; urlIdx < module.getUrlList().size(); urlIdx++ ) {
				final String archiveSource = urlIdx == 0 ? "main source" : " mirror #" + urlIdx;
				final boolean lastArchiveUrl = urlIdx == module.getUrlList().size() - 1;
				
				LEnv.LOGGER.debug( "Downloading archive from " + archiveSource + "..." );
				
				boolean downloadOk = false;
				InputStream input = null;
				OutputStream output = null;
				try {
					LEnv.LOGGER.debug( "Connecting..." );
					launcherFrame.setProgressMax( (int) module.getArchiveSize() );
					launcherFrame.setProgress( 0 );
					
					final URLConnection archiveUrlConnection = new URL( module.getUrlList().get( urlIdx ) ).openConnection();
					
					input = archiveUrlConnection.getInputStream();
					output = Files.newOutputStream( archivePath );
					
					LEnv.LOGGER.debug( "Downloading..." );
					int totalBytesRead = 0;
					int bytesRead;
					while ( ( bytesRead = input.read( buffer ) ) > 0 ) {
						output.write( buffer, 0, bytesRead );
						totalBytesRead += bytesRead;
						launcherFrame.setProgress( totalBytesRead );
					}
					output.flush();
					
					downloadOk = true;
					LEnv.LOGGER.debug( "Download complete." );
					
				} catch ( final Exception e ) {
					LEnv.LOGGER.warning(
					        "Failed to download archive from " + archiveSource + "!" + ( lastArchiveUrl ? "" : " Proceeding to the next source." ), e );
					if ( lastArchiveUrl )
						throw new Exception( "None of the archives are available!" );
				} finally {
					if ( input != null ) {
						try {
							input.close();
						} catch ( final IOException ie ) {
							// We're done, just ignore.
						}
						input = null;
					}
					if ( output != null ) {
						try {
							output.close();
						} catch ( final IOException ie ) {
							// We're done, just ignore.
						}
						output = null;
					}
				}
				
				if ( downloadOk ) {
					LEnv.LOGGER.debug( "Checking SHA-256 checksum of the archive..." );
					if ( module.getArchiveFile().getSha256().equals( LUtils.calculateFileSha256( archivePath ) ) ) {
						LEnv.LOGGER.debug( "SHA-256 checksum OK." );
						break; // Break archive URLs cycle
					} else {
						LEnv.LOGGER.debug( "SHA-256 checksum MISMATCH! The downloaded archive is discarded!" );
						while ( !LUtils.deletePath( archivePath ) )
							if ( !LGuiUtils.askRetry( "Could not delete file:", archivePath ) )
								throw new Exception( "Could not delete file: " + archivePath );
						if ( lastArchiveUrl )
							throw new Exception( "None of the archives are available!" );
						else
							LEnv.LOGGER.debug( "Proceeding to the next source." );
					}
				}
			}
			
			LEnv.LOGGER.debug( "Extracting archive..." );
			final InputStream ins = Files.newInputStream( archivePath ); // Input stream is "out-sourced" to a local var because
			                                                             // else a false resource leak is reported :S
			try ( final ZipInputStream zipInput = new ZipInputStream( ins ) ) {
				final String pathPrefix = external ? "Scelight/" + LEnv.PATH_EXT_MODS.getFileName().toString() + "/" + module.getFolder() + "/" : null;
				ZipEntry zipEntry;
				while ( ( zipEntry = zipInput.getNextEntry() ) != null ) {
					if ( external && !zipEntry.isDirectory() ) {
						// Quarantine check
						if ( zipEntry.getName().indexOf( ".." ) >= 0 || !zipEntry.getName().startsWith( pathPrefix ) )
							throw new Exception( "Invalid archive content, disallowed file entry: " + zipEntry.getName() );
					}
					final Path entryFile = tempPath.resolve( zipEntry.getName() );
					if ( zipEntry.isDirectory() )
						Files.createDirectories( entryFile );
					else {
						long size = zipEntry.getSize();
						try ( final OutputStream output = Files.newOutputStream( entryFile ) ) {
							while ( size > 0 ) {
								final int bytesRead = zipInput.read( buffer );
								output.write( buffer, 0, bytesRead );
								size -= bytesRead;
							}
							output.flush();
						}
					}
				}
			} catch ( final Exception e ) {
				throw new Exception( "Failed to extract archive!", e );
			}
			LEnv.LOGGER.debug( "Extracting done." );
			
			final Path archiveAppPath = tempPath.resolve( "Scelight" );
			if ( !Files.exists( archiveAppPath ) )
				throw new Exception( "The extracted archive does not seem to be a valid archive! Aborting " + ( repair ? "repair" : "update" ) + "!" );
			
			LEnv.LOGGER.debug( repair ? "Replacing/patching files..." : "Implanting files..." );
			Files.walkFileTree( archiveAppPath, new SimpleFileVisitor< Path >() {
				@Override
				public FileVisitResult preVisitDirectory( Path dir, BasicFileAttributes attrs ) throws IOException {
					dir = LEnv.PATH_APP.resolve( archiveAppPath.relativize( dir ) );
					attrs = null; // We changed dir, attrs do not apply to dir anymore, null it to avoid accidental use!
					
					while ( !Files.exists( dir ) || !Files.isDirectory( dir ) ) {
						try {
							// Files.createDirectories() does not always throw IOException if dir exists and is a file, do it
							// ourselves!
							if ( Files.exists( dir ) && !Files.isDirectory( dir ) )
								throw new IOException( "File exists and is not a folder: " + dir );
							Files.createDirectories( dir );
						} catch ( final IOException e ) {
							if ( !LGuiUtils.askRetry( "Could not create folder:", dir ) )
								throw new IOException( "Could not create folder: " + dir, e );
						}
					}
					return FileVisitResult.CONTINUE;
				}
				
				@Override
				public FileVisitResult visitFile( final Path file, final BasicFileAttributes attrs ) throws IOException {
					// Launcher's class path entries are locked and therefore cannot be repaired from "within"!
					// (But this is also not intended, in this case error and need of manual download is displayed to the user!)
					if ( repair && module == modules.getLauncherMod() && launcher.isClassPathEntry( file.getFileName().toString() ) )
						return FileVisitResult.CONTINUE;
					
					final Path target = LEnv.PATH_APP.resolve( archiveAppPath.relativize( file ) );
					while ( true ) {
						try {
							Files.copy( file, target, StandardCopyOption.REPLACE_EXISTING );
							return FileVisitResult.CONTINUE;
						} catch ( final IOException e ) {
							if ( !LGuiUtils.askRetry( "Could not write file:", target ) )
								throw new IOException( "Could not write file: " + target, e );
						}
					}
				}
			} );
			LEnv.LOGGER.debug( repair ? "Replace/patch complete." : "Implantation complete." );
			
		} catch ( final Exception e ) {
			LEnv.LOGGER.error( "Failed to " + ( repair ? "repair " : "update " ) + module.getName() + "!", e );
			launcherFrame.setStatus( StatusType.ERROR, "Failed to " + ( repair ? "repair " : "update " ) + module.getName() + "! See the Logs for details!" );
			throw new FinishException();
		} finally {
			LEnv.LOGGER.debug( "Cleaning up..." );
			while ( !LUtils.deletePath( tempPath ) ) {
				if ( !LGuiUtils.askRetry( "Could not delete folder:", tempPath ) ) {
					LEnv.LOGGER.error( "Could not delete folder: " + tempPath );
					launcherFrame.setStatus( StatusType.ERROR, "Failed to " + ( repair ? "repair " : "update " ) + module.getName()
					        + "! See the Logs for details!" );
					throw new FinishException();
				}
			}
			LEnv.LOGGER.debug( "Cleanup complete." );
		}
		
		// Module updated / repaired successfully.
		
		if ( module == modules.getLauncherMod() ) {
			// Launcher update requires restart
			LEnv.LOGGER.info( module.getName() + " has been " + ( repair ? "repaired" : "updated" ) + ", you must restart " + module.getName()
			        + " to continue." );
			launcherFrame.setStatus( StatusType.WARNING,
			        module.getName() + " has been " + ( repair ? "repaired" : "updated" ) + ", you must restart " + module.getName() + " to continue." );
			throw new FinishException( true );
		} else {
			LEnv.LOGGER.info( module.getName() + " has been " + ( repair ? "repaired." : "updated." ) );
			launcherFrame.setStatus( StatusType.PROGRESS, module.getName() + " has been " + ( repair ? "repaired." : "updated." ) );
		}
	}
	
	/**
	 * Check and delete old versions of the modules.
	 * 
	 * @throws FinishException if the updater should finish and return
	 */
	private void removeOldVersions() {
		// 3 rounds: launcher mod, internal and external modules
		// Note: launcher mod is also stored in the internal mod folder, but it is not contained in the internal mod list.
		for ( int i = 0; i < 3; i++ ) {
			if ( i == 2 && modules.getRetrievedExtModList() == null )
				continue; // There might be no retrieved ext mod
				
			final Path modsPath = i < 2 ? LEnv.PATH_MODS : LEnv.PATH_EXT_MODS;
			final List< ModuleBean > modList = i == 0 ? Arrays.asList( modules.getLauncherMod() ) : i == 1 ? modules.getModList() : modules
			        .getRetrievedExtModList();
			
			for ( final ModuleBean mod : modList ) {
				final Path moduleBasePath = modsPath.resolve( mod.getFolder() );
				if ( !Files.exists( moduleBasePath ) )
					continue;
				final Path latestModulePath = moduleBasePath.resolve( mod.getVersion().toString() );
				
				try {
					Files.walkFileTree( moduleBasePath, Collections.< FileVisitOption > emptySet(), 1, new SimpleFileVisitor< Path >() {
						@Override
						public FileVisitResult visitFile( final Path file, final BasicFileAttributes attrs ) throws IOException {
							if ( attrs.isDirectory() && !file.equals( latestModulePath ) ) {
								LEnv.LOGGER.debug( "Removing old version: " + modsPath.relativize( file ) );
								while ( !LUtils.deletePath( file ) ) {
									if ( !LGuiUtils.askRetry( "Could not delete folder:", file ) )
										throw new IOException( "Could not delete folder: " + file );
								}
							}
							return FileVisitResult.CONTINUE;
						}
					} );
				} catch ( final IOException e ) {
					Sound.beepOnError();
					LEnv.LOGGER.error( "Error while removing old versions!", e );
					launcherFrame.setStatus( StatusType.ERROR, "Failed to remove old versions! See the Logs for details!" );
					throw new FinishException();
				}
			}
		}
	}
	
	/**
	 * Application is up-to-date, sets ready to start state.
	 */
	private void setReady() {
		Sound.play( LSounds.SCELIGHT_READY );
		
		if ( failedExtMods ) {
			LEnv.LOGGER.info( LConsts.APP_NAME + " is up to date, but failed to check / update some external modules!" );
			launcherFrame.setStatus( StatusType.WARNING, LConsts.APP_NAME
			        + " is up to date, but failed to check / update some external modules! (See the Logs for details.)" );
		} else {
			LEnv.LOGGER.info( LConsts.APP_NAME + " is up to date." );
			launcherFrame.setStatus( StatusType.SUCCESS, LConsts.APP_NAME + " is up to date." );
		}
		
		launcherFrame.setProgressMax( 1 );
		launcherFrame.setProgress( 1 );
	}
	
	/**
	 * Checks and acts according to the caught {@link FinishException} and decides whether application start is allowed.
	 * 
	 * @param fe caught {@link FinishException}
	 * @return true if application start is allowed; false otherwise
	 */
	private boolean allowToStart( final FinishException fe ) {
		if ( LEnv.ECLIPSE_MODE ) {
			setReady();
			return true;
		}
		
		Sound.beepOnError();
		
		if ( modules != null ) {
			// There is connection, modules bean retrieved, but there was an error or launcher was updated, do not allow start.
			Sound.play( fe.restartRequired ? LSounds.SCELIGHT_RESTART : LSounds.SCELIGHT_ERR_UPDATE_FAILED );
			
			if ( fe.restartRequired ) {
				launcherFrame.setProceedText( "<html><h2>RE_START</h2></html>" );
				launcher.setProceedAction( launcher.PROCEED_ACTION_RESTART );
			} else {
				launcherFrame.setProceedText( "<html><h2>E_XIT</h2></html>" );
				launcher.setProceedAction( launcher.PROCEED_ACTION_EXIT );
			}
			
			launcherFrame.setProceedEnabled( true );
			return false;
		}
		
		// No connection, could not retrieve modules bean.
		Sound.play( LSounds.SCELIGHT_ERR_NO_CONNECTION );
		

		
		// Need to create a "fake" modules bean which is required to determine the class path!
		modules = createFakeModulesBean();
		if ( modules == null ) {
			launcher.setProceedAction( new Runnable() {
				@Override
				public void run() {
					LEnv.LOGGER.error( "Required modules are not installed! Cannot proceed to Start!" );
					// This is called from the EDT (proceed button's action listener) so no need to run it in EDT!
					JOptionPane.showMessageDialog( launcherFrame, new Object[] { "Required modules are not installed!", "Cannot proceed to Start!" },
					        "Start Error!", JOptionPane.ERROR_MESSAGE );
				}
			} );
			launcherFrame.setProceedEnabled( true );
			return false;
		}
		
		// Registered, fake modules bean could be created, so we allow to start.
		modules.setOrigin( ModulesBeanOrigin.UPDATER_FAKE );
		launcher.setModules( modules );
		launcher.setProceedAction( launcher.PROCEED_ACTION_START );
		return true;
	}
	

	
	/**
	 * Acknowledges that a module has been checked. Increments checked modules count and publishes the new mods count.
	 */
	private void acknowledgeModChecked() {
		checkedModsCount++;
		publishModCounts();
	}
	
	/**
	 * Publishes the checked and all mods count, refreshes the label displaying it.
	 */
	private void publishModCounts() {
		launcherFrame.setModuleCounts( checkedModsCount, checkableModsCount );
	}
	
}
