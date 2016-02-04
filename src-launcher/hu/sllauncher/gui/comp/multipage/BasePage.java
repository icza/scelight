/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.sllauncher.gui.comp.multipage;

import hu.scelightapibase.gui.comp.multipage.IPage;
import hu.scelightapibase.gui.icon.IRIcon;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;

/**
 * Base {@link IPage} with some help to implement pages.
 * 
 * @param <T> type of the page component
 * 
 * @author Andras Belicza
 */
public abstract class BasePage< T extends JComponent > implements IPage< T > {
	
	/** Display name of the page. */
	protected final String       displayName;
	
	/** RIcon of the page. */
	protected final IRIcon       ricon;
	
	/** Closeable property of the page. */
	protected final boolean      closeable;
	
	/** Optional parent page of the page. */
	protected IPage< ? >         parent;
	
	/** List of child pages. */
	protected List< IPage< ? > > childList;
	
	
	/**
	 * Creates a new non-closeable {@link BasePage}.
	 * 
	 * @param displayName display name of the page
	 * @param ricon ricon of the page
	 */
	public BasePage( final String displayName, final IRIcon ricon ) {
		this( displayName, ricon, false );
	}
	
	/**
	 * Creates a new {@link BasePage}.
	 * 
	 * @param displayName display name of the page
	 * @param ricon ricon of the page
	 * @param closeable tells if the page is closeable
	 */
	public BasePage( final String displayName, final IRIcon ricon, final boolean closeable ) {
		this.displayName = displayName;
		this.ricon = ricon;
		this.closeable = closeable;
	}
	
	@Override
	public boolean isCloseable() {
		return closeable;
	}
	
	@Override
	public void setParent( final IPage< ? > parent ) {
		this.parent = parent;
	}
	
	@Override
	public IPage< ? > getParent() {
		return parent;
	}
	
	/**
	 * Adds a child page to this page. Also sets the parent of the page to be us.
	 * 
	 * @param page child page to be added
	 */
	public void addChild( final IPage< ? > page ) {
		if ( childList == null )
			childList = new ArrayList<>();
		
		childList.add( page );
		page.setParent( this );
	}
	
	@Override
	public String getDisplayName() {
		return displayName;
	}
	
	@Override
	public IRIcon getRicon() {
		return ricon;
	}
	
	@Override
	public List< IPage< ? > > getChildList() {
		return childList;
	}
	
	/**
	 * Returns the child page list of this page.
	 * <p>
	 * This method always returns a non-null list even if there are no child pages. If there are no child pages, an empty list will be created first.
	 * </p>
	 * 
	 * @return the child page list of this page
	 * @see #getChildList()
	 */
	public List< IPage< ? > > getNonNullChildList() {
		if ( childList == null )
			childList = new ArrayList<>();
		
		return childList;
	}
	
	@Override
	public String toString() {
		return getDisplayName();
	}
	
}
