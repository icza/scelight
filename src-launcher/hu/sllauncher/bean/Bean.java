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

import hu.scelightapibase.bean.HiddenProperty;
import hu.scelightapibase.bean.IBean;
import hu.sllauncher.service.env.LEnv;

import java.io.StringReader;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import javax.xml.bind.JAXB;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

/**
 * Ancestor class for XML beans.
 * 
 * @author Andras Belicza
 * 
 * @see IdedBean
 */
@XmlAccessorType( XmlAccessType.FIELD )
public class Bean implements IBean {
	
	/** Bean instance version. */
	@XmlAttribute( name = "v" )
	protected int beanVer;
	
	/**
	 * Creates a new {@link Bean}.
	 * <p>
	 * No-arg constructor required by some implementation of {@link JAXB#unmarshal(java.io.Reader, Class)}.
	 * </p>
	 */
	protected Bean() {
	}
	
	/**
	 * Creates a new {@link Bean}.
	 * 
	 * @param beanVer bean instance version
	 */
	public Bean( final int beanVer ) {
		this.setBeanVer( beanVer );
	}
	
	@Override
	public int getBeanVer() {
		return beanVer;
	}
	
	/**
	 * Sets the bean instance version.
	 * 
	 * @param beanVer bean instance version to be set
	 */
	public void setBeanVer( int beanVer ) {
		this.beanVer = beanVer;
	}
	
	/**
	 * Clones this bean by concatenating marshaling and unmarshaling operations.
	 * 
	 * <p>
	 * This default bean cloning implementation is "very" slow, it even marshals and unmarshals primitive type values (like <code>int</code> or
	 * <code>boolean</code>) and immutable objects (like {@link String}) which otherwise would not be necessary. But works for all beans without modification,
	 * and does not require modification when a bean is modified or extended with new fields later on (<i>resilient</i> to changes).
	 * </p>
	 * <p>
	 * Beans that are frequently cloned should override this method and implement a custom cloning. A custom cloning (which copies the fields) can be even 1000
	 * times faster than this!
	 * </p>
	 * 
	 * @param <T> dynamic type of the bean to return
	 * @return a clone of this bean
	 */
	@Override
	@SuppressWarnings( "unchecked" )
	public < T extends IBean > T cloneBean() {
		final StringWriter sw = new StringWriter( 256 );
		JAXB.marshal( this, sw );
		
		return (T) JAXB.unmarshal( new StringReader( sw.toString() ), this.getClass() );
	}
	
	/**
	 * Returns a string representation of the bean.
	 * <p>
	 * This implementation uses {@link #buildDevString(StringBuilder)}.
	 * </p>
	 * 
	 * @return a string representation of the bean
	 * @see #buildDevString(StringBuilder)
	 */
	@Override
	public String toString() {
		final StringBuilder b = new StringBuilder();
		
		buildDevString( b );
		
		return b.toString();
	}
	
	@Override
	public void buildDevString( final StringBuilder b ) {
		b.append( getClass().getSimpleName() ).append( '{' );
		
		for ( final Method m : getClass().getMethods() ) {
			if ( ( m.getModifiers() & Modifier.STATIC ) != 0 )
				continue;
			
			if ( m.getParameterTypes().length > 0 )
				continue;
			
			if ( m.getReturnType().equals( Void.TYPE ) )
				continue;
			
			final String name = m.getName();
			if ( name.length() < 3 || !name.startsWith( "get" ) )
				continue;
			
			if ( "getClass".equals( name ) )
				continue;
			
			if ( m.getAnnotation( HiddenProperty.class ) != null )
				continue;
			
			// Property name: cut off get and lowercase first letter
			if ( name.length() > 3 )
				b.append( Character.toLowerCase( name.charAt( 3 ) ) ).append( name, 4, name.length() );
			b.append( '=' );
			try {
				final Object propValue = m.invoke( this );
				// Avoid endless recursion:
				if ( propValue == this )
					b.append( "this" );
				else {
					if ( propValue instanceof Bean )
						( (Bean) propValue ).buildDevString( b );
					else
						b.append( propValue ); // This also handles null values...
				}
			} catch ( final IllegalAccessException | IllegalArgumentException | InvocationTargetException e ) {
				LEnv.LOGGER.error( "Could not call method: " + getClass().getSimpleName() + "." + name, e );
			}
			b.append( "; " );
		}
		
		b.append( '}' );
	}
	
}
