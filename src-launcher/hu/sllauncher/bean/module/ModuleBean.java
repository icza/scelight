/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.sllauncher.bean.module;

import hu.sllauncher.bean.Bean;
import hu.sllauncher.bean.VersionBean;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

/**
 * Module bean.
 * 
 * <p>
 * Contains module info like version, archive urls and details.
 * </p>
 * 
 * <p>
 * Modules are identified by their <code>folder</code> attribute.
 * </p>
 * 
 * @author Andras Belicza
 */
@XmlAccessorType( XmlAccessType.FIELD )
public class ModuleBean extends Bean {
	
	/** Current bean version. */
	public static final int  BEAN_VER = 1;
	
	/** Module name. */
	private String           name;
	
	/** Module version. */
	private VersionBean      version;
	
	/**
	 * Mod-path (or mod-x path) relative folder path (where to place it, quarantine it).<br>
	 * Also used as the module identifier, must be unique amongst modules.
	 */
	private String           folder;
	
	/** Archive file. */
	private FileBean         archiveFile;
	
	/** URL list of the archive (source) (list of mirrors). */
	private List< String >   urlList;
	
	/** Archive file size in bytes. */
	private long             archiveSize;
	
	/** File list (content of the archive). */
	private List< FileBean > fileList;
	
	/**
	 * Creates a new {@link ModuleBean}.
	 */
	public ModuleBean() {
		super( BEAN_VER );
	}
	
	/**
	 * Returns the module name.
	 * 
	 * @return the module name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Sets the module name.
	 * 
	 * @param name module name to be set
	 */
	public void setName( String name ) {
		this.name = name;
	}
	
	/**
	 * Returns the module version.
	 * 
	 * @return the module version
	 */
	public VersionBean getVersion() {
		return version;
	}
	
	/**
	 * Sets the module version.
	 * 
	 * @param version module version to be set
	 */
	public void setVersion( VersionBean version ) {
		this.version = version;
	}
	
	/**
	 * Returns the module folder.
	 * 
	 * @return the module folder
	 */
	public String getFolder() {
		return folder;
	}
	
	/**
	 * Sets the module folder.
	 * 
	 * @param folder module folder to be set
	 */
	public void setFolder( String folder ) {
		this.folder = folder;
	}
	
	/**
	 * Returns the URL list of the archive.
	 * 
	 * @return the URL list of the archive
	 */
	public List< String > getUrlList() {
		return urlList;
	}
	
	/**
	 * Sets the URL list of the archive.
	 * 
	 * @param urlList URL list to be set
	 */
	public void setUrlList( List< String > urlList ) {
		this.urlList = urlList;
	}
	
	/**
	 * Returns the archive file.
	 * 
	 * @return the archive file
	 */
	public FileBean getArchiveFile() {
		return archiveFile;
	}
	
	/**
	 * Sets the archive file.
	 * 
	 * @param archiveFile archive file to set
	 */
	public void setArchiveFile( FileBean archiveFile ) {
		this.archiveFile = archiveFile;
	}
	
	/**
	 * Returns the archive file size in bytes.
	 * 
	 * @return the archive file size in bytes
	 */
	public long getArchiveSize() {
		return archiveSize;
	}
	
	/**
	 * Sets the archive file size in bytes.
	 * 
	 * @param archiveSize archive file size in bytes to be set
	 */
	public void setArchiveSize( long archiveSize ) {
		this.archiveSize = archiveSize;
	}
	
	/**
	 * Returns the file list (content of the archive).
	 * 
	 * @return the file list (content of the archive)
	 */
	public List< FileBean > getFileList() {
		return fileList;
	}
	
	/**
	 * Sets the file list (content of the archive).
	 * 
	 * @param fileList file list (content of the archive) to be set
	 */
	public void setFileList( List< FileBean > fileList ) {
		this.fileList = fileList;
	}
	
}
