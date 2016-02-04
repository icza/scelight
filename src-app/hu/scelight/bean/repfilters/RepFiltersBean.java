/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight.bean.repfilters;

import hu.scelightapi.bean.repfilters.IRepFilterBean;
import hu.scelightapi.bean.repfilters.IRepFiltersBean;
import hu.sllauncher.bean.Bean;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

/**
 * Bean holding the replay filters of a replay folder.
 * 
 * @author Andras Belicza
 */
@XmlAccessorType( XmlAccessType.FIELD )
public class RepFiltersBean extends Bean implements IRepFiltersBean {
	
	/** Current bean version. */
	public static final int       BEAN_VER = 1;
	
	/** Replay filter bean list. */
	private List< RepFilterBean > repFilterBeanList;
	
	/**
	 * Creates a new {@link RepFiltersBean}.
	 */
	public RepFiltersBean() {
		super( BEAN_VER );
	}
	
	@Override
	public int getActiveCount() {
		if ( repFilterBeanList == null || repFilterBeanList.isEmpty() )
			return 0;
		
		int count = 0;
		for ( final RepFilterBean filterBean : repFilterBeanList )
			if ( filterBean.isActive() )
				count++;
		
		return count;
	}
	
	@Override
	public List< RepFilterBean > getRepFilterBeanList() {
		if ( repFilterBeanList == null )
			repFilterBeanList = new ArrayList<>( 2 );
		return repFilterBeanList;
	}
	
	@Override
	@SuppressWarnings( "unchecked" )
	public void setRepFilterBeanList( List< ? extends IRepFilterBean > repFilterBeanList ) {
		for ( final IRepFilterBean repFilterBean : repFilterBeanList )
			if ( !( repFilterBean instanceof RepFilterBean ) )
				throw new IllegalArgumentException( "Only instances created by the API are allowed!" );
		
		// We checked each element one-by-one, so it's safe to cast and ignore the warning
		this.repFilterBeanList = (List< RepFilterBean >) repFilterBeanList;
	}
	
}
