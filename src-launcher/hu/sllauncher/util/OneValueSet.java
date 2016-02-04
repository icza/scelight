/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.sllauncher.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * A super-fast, unmodifiable {@link Set} implementation holding exactly 1 value.<br>
 * Thread safe, of course :)
 * 
 * @param < E > type of the element
 * 
 * @author Andras Belicza
 */
public class OneValueSet< E > implements Set< E > {
	
	/** The one and only value. */
	private final E value;
	
	/**
	 * Creates a new {@link OneValueSet}.
	 * 
	 * @param value the one and only value
	 */
	public OneValueSet( final E value ) {
		this.value = value;
	}
	
	@Override
	public int size() {
		return 1;
	}
	
	@Override
	public boolean isEmpty() {
		return false;
	}
	
	@Override
	public boolean contains( final Object o ) {
		return value.equals( o );
	}
	
	@Override
	public Iterator< E > iterator() {
		return new Iterator< E >() {
			boolean notReturned = true; // Tells if the one and only element has not yet been returned
			                            
			@Override
			public boolean hasNext() {
				return notReturned;
			}
			
			@Override
			public E next() {
				if ( notReturned ) {
					notReturned = false;
					return value;
				}
				throw new NoSuchElementException();
			}
			
			/**
			 * Throws {@link UnsupportedOperationException}.
			 */
			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}
		};
	}
	
	@Override
	public Object[] toArray() {
		return new Object[] { value };
	}
	
	@SuppressWarnings( "unchecked" )
	@Override
	public < T > T[] toArray( final T[] a ) {
		final T[] result = a.length >= 1 ? a : (T[]) java.lang.reflect.Array.newInstance( a.getClass().getComponentType(), 1 );
		result[ 0 ] = (T) value;
		
		if ( result.length > 1 )
			result[ 1 ] = null;
		
		return result;
	}
	
	/**
	 * Throws {@link UnsupportedOperationException}.
	 */
	@Override
	public boolean add( final E e ) {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Throws {@link UnsupportedOperationException}.
	 */
	@Override
	public boolean remove( final Object o ) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public boolean containsAll( final Collection< ? > c ) {
		for ( final Object o : c )
			if ( !value.equals( o ) )
				return false;
		return true;
	}
	
	/**
	 * Throws {@link UnsupportedOperationException}.
	 */
	@Override
	public boolean addAll( final Collection< ? extends E > c ) {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Throws {@link UnsupportedOperationException}.
	 */
	@Override
	public boolean retainAll( final Collection< ? > c ) {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Throws {@link UnsupportedOperationException}.
	 */
	@Override
	public boolean removeAll( final Collection< ? > c ) {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Throws {@link UnsupportedOperationException}.
	 */
	@Override
	public void clear() {
		throw new UnsupportedOperationException();
	}
	
}
