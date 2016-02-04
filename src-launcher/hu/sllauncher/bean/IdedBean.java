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

import java.util.concurrent.atomic.AtomicLong;

import javax.xml.bind.JAXB;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

/**
 * Bean which has an id property and overrides {@link Object#hashCode()} and {@link Object#equals(Object)} to operate based on this single field. The id
 * property is instantiated automatically, and its value is xml-serialized and deserialized.
 * 
 * @author Andras Belicza
 */
@XmlAccessorType( XmlAccessType.FIELD )
public class IdedBean extends Bean {
	
	/** Unique ID generated for the bean id ({@link #bid}) property. */
	private static final AtomicLong ID_GENERATOR = new AtomicLong( System.currentTimeMillis() );
	
	/** Bean id. */
	private Long                    bid          = ID_GENERATOR.incrementAndGet();
	
	/**
	 * Creates a new {@link IdedBean}.
	 * <p>
	 * No-arg constructor required by some implementation of {@link JAXB#unmarshal(java.io.Reader, Class)}.
	 * </p>
	 */
	protected IdedBean() {
	}
	
	/**
	 * Creates a new {@link IdedBean}.
	 * 
	 * @param beanVer bean instance version
	 */
	public IdedBean( final int beanVer ) {
		super( beanVer );
	}
	
	@Override
	public int hashCode() {
		return bid.hashCode();
	}
	
	@Override
	public boolean equals( final Object obj ) {
		if ( !( obj instanceof IdedBean ) )
			return false;
		
		return bid.equals( ( (IdedBean) obj ).bid );
	}
	
}
