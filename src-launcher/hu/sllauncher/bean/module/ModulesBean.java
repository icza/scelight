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
import javax.xml.bind.annotation.XmlTransient;

/**
 * Modules bean.
 * 
 * <p>
 * Contains the module beans of the application (e.g. launcher, app).
 * </p>
 * 
 * @author Andras Belicza
 */
@XmlAccessorType( XmlAccessType.FIELD )
public class ModulesBean extends Bean {
	
	/** Current bean version. */
	public static final int       BEAN_VER = 1;
	
	/** Required min launcher version to process this bean and perform updates. */
	private VersionBean           reqMinLauncherVer;
	
	/**
	 * Launcher module including static files (like starter scripts).<br>
	 * Launcher module is special and is out-sourced here.
	 */
	private ModuleBean            launcherMod;
	
	/** List of internal modules (including the main application module). */
	private List< ModuleBean >    modList;
	
	/** List of official external module references. */
	private List< ExtModRefBean > extModRefList;
	
	/**
	 * Digest of the serialized modules bean, excluding this field.
	 * <p>
	 * The purpose of this field is so that clients can periodically check if there are updates simply by acquiring the actual value of this digest from the
	 * operator, and comparing it to this value, acquired by the launcher on startup; hence no need to download the whole modules bean each time.<br>
	 * The digest algorithm is irrelevant, clients only need to compare this value (and not calculate it).
	 * </p>
	 */
	private String                digest;
	
	
	/** List of retrieved external modules. */
	@XmlTransient
	private List< ModuleBean >    retrievedExtModList;
	
	
	/** Modules bean origin. */
	@XmlTransient
	private ModulesBeanOrigin     origin;
	
	
	/**
	 * Creates a new {@link ModulesBean}.
	 */
	public ModulesBean() {
		super( BEAN_VER );
	}
	
	/**
	 * Returns the first official external module reference with the specified folder.
	 * 
	 * @param folder folder whose first official external module reference to return
	 * @return the first official external module reference with the specified folder; or <code>null</code> if no official external module reference found for
	 *         the specified folder folder set) otherwise
	 */
	public ExtModRefBean getExtModRefForFolder( final String folder ) {
		if ( extModRefList != null )
			for ( final ExtModRefBean emr : extModRefList )
				if ( folder.equals( emr.getFolder() ) )
					return emr;
		
		return null;
	}
	
	/**
	 * Returns the required minimum launcher version.
	 * 
	 * @return the required min launcher version
	 */
	public VersionBean getReqMinLauncherVer() {
		return reqMinLauncherVer;
	}
	
	/**
	 * Sets the required minimum launcher version.
	 * 
	 * @param reqMinLauncherVer required min launcher version to be set
	 */
	public void setReqMinLauncherVer( VersionBean reqMinLauncherVer ) {
		this.reqMinLauncherVer = reqMinLauncherVer;
	}
	
	/**
	 * Returns the launcher module.
	 * 
	 * @return the launcher module
	 */
	public ModuleBean getLauncherMod() {
		return launcherMod;
	}
	
	/**
	 * Sets the launcher module.
	 * 
	 * @param launcherMod launcher module to be set
	 */
	public void setLauncherMod( ModuleBean launcherMod ) {
		this.launcherMod = launcherMod;
	}
	
	/**
	 * Returns the list of internal modules.
	 * 
	 * @return the list of internal modules
	 */
	public List< ModuleBean > getModList() {
		return modList;
	}
	
	/**
	 * Sets the list of internal modules
	 * 
	 * @param modList list of internal modules to be set
	 */
	public void setModList( List< ModuleBean > modList ) {
		this.modList = modList;
	}
	
	/**
	 * Returns the list of official external module references.
	 * 
	 * @return the list of official external module references
	 */
	public List< ExtModRefBean > getExtModRefList() {
		return extModRefList;
	}
	
	/**
	 * Sets the list of official external module references.
	 * 
	 * @param extModRefList list of official external modules to be set
	 */
	public void setExtModRefList( List< ExtModRefBean > extModRefList ) {
		this.extModRefList = extModRefList;
	}
	
	/**
	 * Returns the list of retrieved external modules
	 * 
	 * @return the list of retrieved external modules
	 */
	public List< ModuleBean > getRetrievedExtModList() {
		return retrievedExtModList;
	}
	
	/**
	 * Sets the list of retrieved external modules
	 * 
	 * @param extModList list of retrieved external modules to be set
	 */
	public void setRetrievedExtModList( List< ModuleBean > extModList ) {
		this.retrievedExtModList = extModList;
	}
	
	/**
	 * Returns the modules bean origin.
	 * 
	 * @return the modules bean origin
	 */
	public ModulesBeanOrigin getOrigin() {
		return origin;
	}
	
	/**
	 * Sets the modules bean origin.
	 * 
	 * @param origin modules bean origin to be set
	 */
	public void setOrigin( ModulesBeanOrigin origin ) {
		this.origin = origin;
	}
	
	/**
	 * Returns the digest of the serialized modules bean, excluding this field.
	 * 
	 * @return the digest of the serialized modules bean, excluding this field
	 */
	public String getDigest() {
		return digest;
	}
	
	/**
	 * Sets the digest of the serialized modules bean, excluding this field.
	 * 
	 * @param digest digest of the serialized modules bean to be set, excluding this field
	 */
	public void setDigest( String digest ) {
		this.digest = digest;
	}
	
}
