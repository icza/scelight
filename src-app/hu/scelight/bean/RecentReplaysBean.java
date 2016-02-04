/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight.bean;

import hu.scelight.bean.adapter.PathAdapter;
import hu.sllauncher.bean.Bean;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * Bean holding the recently opened replay paths (opened in the Replay analyzer).
 * 
 * @author Andras Belicza
 */
@XmlAccessorType( XmlAccessType.FIELD )
public class RecentReplaysBean extends Bean {
	
	/** Current bean version. */
	public static final int BEAN_VER = 1;
	
	/** Replay list. */
	@XmlJavaTypeAdapter( value = PathAdapter.class )
	private List< Path >    replayList;
	
	/**
	 * Creates a new {@link RecentReplaysBean}.
	 */
	public RecentReplaysBean() {
		super( BEAN_VER );
	}
	
	/**
	 * Returns the replay list.
	 * 
	 * @return the replay list
	 */
	public List< Path > getReplayList() {
		if ( replayList == null )
			replayList = new ArrayList<>( 4 );
		return replayList;
	}
	
	/**
	 * Sets the replay list.
	 * 
	 * @param replayList replay list to be set
	 */
	public void setReplayList( List< Path > replayList ) {
		this.replayList = replayList;
	}
	
}
