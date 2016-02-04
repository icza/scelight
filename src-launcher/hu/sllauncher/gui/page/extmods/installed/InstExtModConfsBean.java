/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.sllauncher.gui.page.extmods.installed;

import hu.sllauncher.bean.Bean;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

/**
 * Bean holding the configs of installed external modules.
 * 
 * @author Andras Belicza
 */
@XmlAccessorType( XmlAccessType.FIELD )
public class InstExtModConfsBean extends Bean {
	
	/** Current bean version. */
	public static final int            BEAN_VER = 1;
	
	/** Installed external module config bean list. */
	private List< InstExtModConfBean > instExtModConfBeanList;
	
	/**
	 * Creates a new {@link InstExtModConfsBean}.
	 */
	public InstExtModConfsBean() {
		super( BEAN_VER );
	}
	
	/**
	 * Returns the first installed external module config with the specified folder.
	 * 
	 * @param folder folder whose first external module config to return
	 * @return the first installed external module config with the specified folder; or <code>null</code> if no config bean found for the specified folder
	 */
	public InstExtModConfBean getModuleConfForFolder( final String folder ) {
		return getModuleConfForFolder( folder, false );
	}
	
	/**
	 * Returns the first installed external module config with the specified folder.
	 * 
	 * @param folder folder whose first external module config to return
	 * @param create tells if new config bean is to be created and added in case no config bean is found for the specified folder
	 * @return the first installed external module config with the specified folder; <code>null</code> if <code>create=false</code> and no config bean found for
	 *         the specified folder; and a newly created and added config bean (with the specified folder set) otherwise
	 */
	public InstExtModConfBean getModuleConfForFolder( final String folder, final boolean create ) {
		for ( final InstExtModConfBean conf : getInstExtModConfBeanList() )
			if ( folder.equals( conf.getFolder() ) )
				return conf;
		
		if ( !create )
			return null;
		
		final InstExtModConfBean conf = new InstExtModConfBean();
		conf.setFolder( folder );
		instExtModConfBeanList.add( conf );
		
		return conf;
	}
	
	/**
	 * @return the installed external module config bean list
	 */
	public List< InstExtModConfBean > getInstExtModConfBeanList() {
		if ( instExtModConfBeanList == null )
			instExtModConfBeanList = new ArrayList<>( 2 );
		return instExtModConfBeanList;
	}
	
	/**
	 * @param instExtModConfBeanList installed external module config bean list to be set
	 */
	public void setInstExtModConfBeanList( List< InstExtModConfBean > instExtModConfBeanList ) {
		this.instExtModConfBeanList = instExtModConfBeanList;
	}
	
}
