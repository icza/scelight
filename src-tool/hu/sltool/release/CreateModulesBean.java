/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.sltool.release;

import hu.scelight.Consts;
import hu.sllauncher.bean.VersionBean;
import hu.sllauncher.bean.module.ExtModRefBean;
import hu.sllauncher.bean.module.FileBean;
import hu.sllauncher.bean.module.ModuleBean;
import hu.sllauncher.bean.module.ModulesBean;
import hu.sllauncher.bean.person.ContactBean;
import hu.sllauncher.bean.person.PersonBean;
import hu.sllauncher.bean.person.PersonNameBean;
import hu.sllauncher.util.LUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitOption;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.xml.bind.JAXB;

/**
 * Application which creates the modules bean.
 * 
 * @author Andras Belicza
 */
public class CreateModulesBean {
	
	/**
	 * @param args used to take arguments from the running environment; args[0] is the deployment folder
	 * @throws Exception if any error occurs
	 */
	public static void main( final String[] args ) throws Exception {
		final Path DEPLOYMENT_FOLDER = Paths.get( args[ 0 ] ).toAbsolutePath();
		
		final ModulesBean modules = new ModulesBean();
		
		// Common modules attributes
		modules.setReqMinLauncherVer( new VersionBean( 1, 0 ) );
		
		// Launcher module
		{
			final ModuleBean modBean = new ModuleBean();
			modBean.setName( Consts.LAUNCHER_NAME );
			modBean.setFolder( "launcher" );
			fillModuleBean( modBean, getReleaseFile( DEPLOYMENT_FOLDER, "ScelightLauncher-" ) );
			modules.setLauncherMod( modBean );
		}
		
		// Internal modules
		{
			final List< ModuleBean > modList = new ArrayList<>();
			
			// SC2 Textures module
			ModuleBean modBean = new ModuleBean();
			modBean.setName( Consts.SC2_TEXTURES_NAME );
			modBean.setFolder( "sc2-t" );
			fillModuleBean( modBean, getReleaseFile( DEPLOYMENT_FOLDER, "ScelightSc2T-" ) );
			modList.add( modBean );
			
			// SC2 Balance Data module
			modBean = new ModuleBean();
			modBean.setName( Consts.SC2_BALANCE_DATA_NAME );
			modBean.setFolder( "sc2-bd" );
			fillModuleBean( modBean, getReleaseFile( DEPLOYMENT_FOLDER, "ScelightSc2Bd-" ) );
			modList.add( modBean );
			
			// External Module API module
			modBean = new ModuleBean();
			modBean.setName( Consts.EXT_MOD_API_NAME );
			modBean.setFolder( "mod-x-api" );
			fillModuleBean( modBean, getReleaseFile( DEPLOYMENT_FOLDER, "ScelightExtModApi-" ) );
			modList.add( modBean );
			
			// Application Libs module
			modBean = new ModuleBean();
			modBean.setName( Consts.APP_LIBS_NAME );
			modBean.setFolder( "app-libs" );
			fillModuleBean( modBean, getReleaseFile( DEPLOYMENT_FOLDER, "ScelightAppLibs-" ) );
			modList.add( modBean );
			
			// Application module
			modBean = new ModuleBean();
			modBean.setName( Consts.APP_NAME );
			modBean.setFolder( "app" );
			fillModuleBean( modBean, getReleaseFile( DEPLOYMENT_FOLDER, "ScelightApp-" ) );
			modList.add( modBean );
			
			modules.setModList( modList );
		}
		
		// Official External module references
		{
			final List< ExtModRefBean > extModRefList = new ArrayList<>();
			
			// SC2ReplayStats Uploader module ref
			ExtModRefBean extModRef = new ExtModRefBean();
			extModRef.setName( "SC2ReplayStatsUploader" );
			extModRef.setFolder( "sc2replaystats" );
			extModRef.setIconImgData( Files.readAllBytes( Paths.get( CreateModulesBean.class.getResource( "offextmodicon/sc2replaystats.png" ).getPath()
			        .substring( 1 ) ) ) );
			final List< PersonBean > authorList = new ArrayList<>();
			{
				final PersonBean p = new PersonBean();
				p.setPersonName( new PersonNameBean( "Chris", null, "Kerslake", null ) );
				final ContactBean c = new ContactBean();
				c.setEmail( "contact_us@sc2replaystats.com" );
				p.setContact( c );
				authorList.add( p );
			}
			
			extModRef.setAuthorList( authorList );
			extModRef.setHomePage( "http://sc2replaystats.com/" );
			extModRef.setShortDesc( "Uploads new replays to your SC2ReplayStats.com account automatically." );
			extModRef.setModuleBeanUrl( "https://sc2replaystats.com/scelight/module.xml" );
			extModRefList.add( extModRef );
			
			modules.setExtModRefList( extModRefList );
		}
		
		// Save modules to XML
		final Path modulesXml = DEPLOYMENT_FOLDER.resolve( "modules.xml" );
		JAXB.marshal( modules, modulesXml.toFile() );
		// Calculate digest
		final String digest = LUtils.calculateFileSha256( modulesXml );
		modules.setDigest( digest );
		Files.write( modulesXml.resolveSibling( modulesXml.getFileName() + ".digest" ), Arrays.asList( digest ), StandardCharsets.UTF_8 );
		// Resave with digest included
		JAXB.marshal( modules, modulesXml.toFile() );
		System.out.println( "Modules bean written to: " + modulesXml.toString() );
	}
	
	/**
	 * Returns the release file for the specified name prefix
	 * 
	 * @param folder folder in which to search
	 * @param prefix file name prefix whose release file to return (the beginning of the file name)
	 * @return the release file for the specified name prefix
	 * @throws IOException if any error occurs during searching for the release file
	 */
	private static Path getReleaseFile( final Path folder, final String prefix ) throws IOException {
		final AtomicReference< Path > result = new AtomicReference<>();
		
		Files.walkFileTree( folder, Collections.< FileVisitOption > emptySet(), 1, new SimpleFileVisitor< Path >() {
			@Override
			public FileVisitResult visitFile( final Path file, final BasicFileAttributes attrs ) throws IOException {
				if ( attrs.isDirectory() )
					return FileVisitResult.CONTINUE;
				
				if ( file.getFileName().toString().startsWith( prefix ) ) {
					result.set( file );
					return FileVisitResult.TERMINATE;
				}
				
				return FileVisitResult.CONTINUE;
			}
		} );
		
		return result.get();
	}
	
	/**
	 * Fills the module bean with the specified release file.
	 * 
	 * @param modBean module bean
	 * @param releaseFile release file
	 * @throws Exception if any error occurs
	 */
	private static void fillModuleBean( final ModuleBean modBean, final Path releaseFile ) throws Exception {
		// Extract version from release file name.
		// File name format: "ShortModName-version.zip"
		final String fileName = releaseFile.getFileName().toString();
		final VersionBean version = VersionBean.fromString( fileName.substring( fileName.indexOf( '-' ) + 1, fileName.lastIndexOf( '.' ) ) );
		modBean.setVersion( version );
		
		modBean.setUrlList( Arrays.asList( "https://scelightop.appspot.com/mod/" + fileName ) );
		
		// Archive file info
		modBean.setArchiveSize( Files.size( releaseFile ) );
		final FileBean archiveFile = new FileBean();
		archiveFile.setPath( releaseFile.getFileName().toString() );
		archiveFile.setSha256( LUtils.calculateFileSha256( releaseFile ) );
		if ( archiveFile.getSha256().isEmpty() )
			throw new RuntimeException( "Could not calculate archive SHA-256: " + releaseFile );
		modBean.setArchiveFile( archiveFile );
		
		// Archive content info
		final List< FileBean > fileList = new ArrayList<>();
		final InputStream in = Files.newInputStream( releaseFile ); // Input stream is "out-sourced" to a local var because
		                                                            // else a false resource leak is reported :S
		try ( final ZipInputStream zipin = new ZipInputStream( in ) ) {
			ZipEntry ze;
			while ( ( ze = zipin.getNextEntry() ) != null ) {
				if ( ze.isDirectory() )
					continue;
				
				final FileBean fileBean = new FileBean();
				fileBean.setPath( ze.getName() );
				fileBean.setSha256( LUtils.calculateStreamSha256( zipin, ze.getSize() ) );
				if ( fileBean.getSha256().isEmpty() )
					throw new RuntimeException( "Could not calculate file SHA-256: " + fileBean.getPath() );
				fileList.add( fileBean );
			}
		}
		modBean.setFileList( fileList );
	}
	
}
