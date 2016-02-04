/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.sllauncher.bean;

import hu.scelightapibase.bean.IVersionBean;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

/**
 * Version bean.
 * 
 * <p>
 * Defines the version of an entity (which can be an application, a module etc.).
 * </p>
 * 
 * <p>
 * Also see the <code>VersionView</code> class (which is part of the app).
 * </p>
 * 
 * @author Andras Belicza
 * 
 * @see hu.belicza.andras.util.VersionView
 */
@XmlAccessorType( XmlAccessType.FIELD )
public class VersionBean extends Bean implements IVersionBean {
	
	/** Current bean version. */
	public static final int BEAN_VER = 1;
	
	/** Major version number. */
	@XmlAttribute
	private int             major;
	
	/** Minor version number. */
	@XmlAttribute
	private int             minor;
	
	/** Revision version number. */
	@XmlAttribute
	private int             revision;
	
	/**
	 * Creates a new {@link VersionBean}.
	 */
	public VersionBean() {
		super( BEAN_VER );
	}
	
	/**
	 * Creates a new {@link VersionBean}.
	 * 
	 * @param major major version number
	 * @param minor minor version number
	 */
	public VersionBean( final int major, final int minor ) {
		this( major, minor, 0 );
	}
	
	/**
	 * Creates a new {@link VersionBean}.
	 * 
	 * @param major major version number
	 * @param minor minor version number
	 * @param revision revision version number
	 */
	public VersionBean( final int major, final int minor, final int revision ) {
		super( BEAN_VER );
		
		setMajor( major );
		setMinor( minor );
		setRevision( revision );
	}
	
	
	/**
	 * Creates a {@link VersionBean} from its string representation.
	 * 
	 * @param s string representation of the version bean
	 * @return the version bean parsed from the specified string, or <code>null</code> if the specified string is not a valid version string
	 */
	public static VersionBean fromString( final String s ) {
		try {
			
			final int i = s.indexOf( '.' );
			if ( i <= 0 )
				return null; // No dot, surely not a version
				
			final int major = Integer.parseInt( s.substring( 0, i ) );
			
			// Minor part follows, but is there a revision part too?
			final int j = s.indexOf( '.', i + 1 );
			if ( j < 0 ) {
				// No revision, rest is all the minor part
				return new VersionBean( major, Integer.parseInt( s.substring( i + 1 ) ) );
			}
			
			// Full version: major, minor, revision parts
			return new VersionBean( major, Integer.parseInt( s.substring( i + 1, j ) ), Integer.parseInt( s.substring( j + 1 ) ) );
			
		} catch ( final Exception e ) {
			return null;
		}
	}
	
	
	@Override
	public int compareTo( final IVersionBean v2 ) {
		if ( major > v2.getMajor() )
			return 1;
		if ( major < v2.getMajor() )
			return -1;
		
		if ( minor > v2.getMinor() )
			return 1;
		if ( minor < v2.getMinor() )
			return -1;
		
		if ( revision > v2.getRevision() )
			return 1;
		if ( revision < v2.getRevision() )
			return -1;
		
		return 0;
	}
	
	@Override
	public boolean equals( final Object obj ) {
		if ( this == obj )
			return true;
		
		return obj instanceof VersionBean && compareTo( (VersionBean) obj ) == 0;
	}
	
	@Override
	public int hashCode() {
		return 31 * ( 31 * major + minor ) + revision;
	}
	
	@Override
	public String toString() {
		return toString( false );
	}
	
	@Override
	public String toString( final boolean full ) {
		final StringBuilder sb = new StringBuilder();
		
		sb.append( major ).append( '.' ).append( minor );
		if ( full || revision > 0 )
			sb.append( '.' ).append( revision );
		
		return sb.toString();
	}
	
	@Override
	public int getMajor() {
		return major;
	}
	
	/**
	 * Sets the major version number.
	 * 
	 * @param major major version number to be set
	 */
	public void setMajor( int major ) {
		this.major = major;
	}
	
	@Override
	public int getMinor() {
		return minor;
	}
	
	/**
	 * Sets the minor version number.
	 * 
	 * @param minor minor version number to be set
	 */
	public void setMinor( int minor ) {
		this.minor = minor;
	}
	
	@Override
	public int getRevision() {
		return revision;
	}
	
	/**
	 * Sets the revision version number.
	 * 
	 * @param revision revision version number to be set
	 */
	public void setRevision( int revision ) {
		this.revision = revision;
	}
	
}
