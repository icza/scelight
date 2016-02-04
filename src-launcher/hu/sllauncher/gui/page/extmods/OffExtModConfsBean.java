/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.sllauncher.gui.page.extmods;

import hu.sllauncher.bean.Bean;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

/**
 * Bean holding the configs of official external modules.
 * 
 * @author Andras Belicza
 */
@XmlAccessorType( XmlAccessType.FIELD )
public class OffExtModConfsBean extends Bean {
	
	/** Current bean version. */
	public static final int           BEAN_VER = 1;
	
	/** Official external module config bean list. */
	private List< OffExtModConfBean > offExtModConfBeanList;
	
	/**
	 * Creates a new {@link OffExtModConfsBean}.
	 */
	public OffExtModConfsBean() {
		super( BEAN_VER );
	}
	
	/**
	 * Returns the first official external module config with the specified folder.
	 * 
	 * @param folder folder whose first external module config to return
	 * @return the first official external module config with the specified folder; or <code>null</code> if no config bean found for the specified folder
	 */
	public OffExtModConfBean getModuleConfForFolder( final String folder ) {
		return getModuleConfForFolder( folder, false );
	}
	
	/**
	 * Returns the first official external module config with the specified folder.
	 * 
	 * @param folder folder whose first external module config to return
	 * @param create tells if new config bean is to be created and added in case no config bean is found for the specified folder
	 * @return the first official external module config with the specified folder; <code>null</code> if <code>create=false</code> and no config bean found for
	 *         the specified folder; and a newly created and added config bean (with the specified folder set) otherwise
	 */
	public OffExtModConfBean getModuleConfForFolder( final String folder, final boolean create ) {
		for ( final OffExtModConfBean conf : getOffExtModConfBeanList() )
			if ( folder.equals( conf.getFolder() ) )
				return conf;
		
		if ( !create )
			return null;
		
		final OffExtModConfBean conf = new OffExtModConfBean();
		conf.setFolder( folder );
		offExtModConfBeanList.add( conf );
		
		return conf;
	}
	
	/**
	 * Returns the official external module config bean list.
	 * 
	 * @return the official external module config bean list
	 */
	public List< OffExtModConfBean > getOffExtModConfBeanList() {
		if ( offExtModConfBeanList == null )
			offExtModConfBeanList = new ArrayList<>( 2 );
		return offExtModConfBeanList;
	}
	
	/**
	 * Sets the official external module config bean list.
	 * 
	 * @param offExtModConfBeanList official external module config bean list to be set
	 */
	public void setOffExtModConfBeanList( List< OffExtModConfBean > offExtModConfBeanList ) {
		this.offExtModConfBeanList = offExtModConfBeanList;
	}
	
}
