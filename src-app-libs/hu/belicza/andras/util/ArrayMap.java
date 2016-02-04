/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.belicza.andras.util;

import java.util.AbstractCollection;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * A {@link Map} implementation which stores the entries in a simple array, no key hashing (like in {@link HashMap}) or key linking (like in {@link TreeMap} is
 * used/performed.
 * 
 * <p>
 * Since entries are stored in a simple array and searches are sequential, this implementation is not suitable for many entries, but is faster and uses less
 * memory for smaller maps compared to {@link HashMap}. Recommended if entry count is about 10.
 * </p>
 * 
 * <p>
 * Implementation prioritizes reference searches and lookups, so performance will be extremely good if the same key reference is used to look up a previously
 * stored value.
 * </p>
 * 
 * <p>
 * If entries are only added and not removed, the iteration order of keys, values, entries will be the same as the adding order.
 * </p>
 * 
 * @param <K> type of the keys
 * @param <V> type of the values
 * 
 * @author Andras Belicza
 */
public class ArrayMap< K, V > implements Map< K, V > {
	
	/**
	 * {@link java.util.Map.Entry} implementation used to store the entries.
	 * 
	 * @param <K> type of the keys
	 * @param <V> type of the values
	 * 
	 * @author Andras Belicza
	 */
	private static class ArrEntry< K, V > implements Entry< K, V > {
		
		/** Key. */
		public final K key;
		
		/** Value. */
		public V       value;
		
		/**
		 * Creates a new {@link ArrayMap.ArrEntry}.
		 * 
		 * @param key key
		 * @param value value
		 */
		public ArrEntry( final K key, final V value ) {
			this.key = key;
			this.value = value;
		}
		
		@Override
		public K getKey() {
			return key;
		}
		
		@Override
		public V getValue() {
			return value;
		}
		
		@Override
		public V setValue( final V value ) {
			final V oldValue = value;
			this.value = value;
			return oldValue;
		}
		
	}
	
	/** Array of the table entries. */
	private ArrEntry< K, V >[] entries;
	
	/** Size of the map, the number of entries. */
	private int                size;
	
	/**
	 * Creates a new {@link ArrayMap} with an initial capacity of 10.
	 */
	public ArrayMap() {
		this( 10 );
	}
	
	/**
	 * Creates a new {@link ArrayMap} with the specified initial capacity.
	 * 
	 * @param initialCapacity initial capacity
	 */
	@SuppressWarnings( "unchecked" )
	public ArrayMap( final int initialCapacity ) {
		entries = new ArrEntry[ initialCapacity ];
	}
	
	@Override
	public int size() {
		return size;
	}
	
	@Override
	public boolean isEmpty() {
		return size == 0;
	}
	
	/**
	 * Returns the entry for the specified key.
	 * 
	 * @param key key whose entry to be returned
	 * @return the entry for the specified key; or <code>null</code> if the key is not in this map
	 */
	private ArrEntry< K, V > getEntry( final Object key ) {
		// Local array reference copy for performance
		final ArrEntry< K, V >[] entries = this.entries;
		
		// First try a key search by reference.
		// This also handles if the searched key is the null value.
		for ( int i = size - 1; i >= 0; i-- )
			if ( entries[ i ].key == key )
				return entries[ i ];
		
		// If key is the null value and was not found by the reference search, it's not in this map
		if ( key == null )
			return null;
		
		for ( int i = size - 1; i >= 0; i-- )
			if ( key.equals( entries[ i ].key ) ) // Key is certainly not null at this point
				return entries[ i ];
		
		return null;
	}
	
	@Override
	public boolean containsKey( final Object key ) {
		return getEntry( key ) != null;
	}
	
	@Override
	public boolean containsValue( final Object value ) {
		// Local array reference copy for performance
		final ArrEntry< K, V >[] entries = this.entries;
		
		// First try a value search by reference.
		// This also handles if the searched value is the null value.
		for ( int i = size - 1; i >= 0; i-- )
			if ( entries[ i ].value == value )
				return true;
		
		// If value is the null value and was not found by the reference search, it's not in this map
		if ( value == null )
			return false;
		
		for ( int i = size - 1; i >= 0; i-- )
			if ( value.equals( entries[ i ].value ) ) // Value is certainly not null at this point
				return true;
		
		return false;
	}
	
	@Override
	public V get( final Object key ) {
		final ArrEntry< K, V > entry = getEntry( key );
		return entry == null ? null : entry.value;
	}
	
	/**
	 * Ensures entry array size for the specified capacity.
	 * 
	 * @param capacity capacity to be ensured
	 */
	@SuppressWarnings( "unchecked" )
	private void ensureCapacity( final int capacity ) {
		if ( entries.length >= capacity )
			return;
		
		// reallocate, increase entries array size
		final ArrEntry< K, V >[] oldEntries = entries;
		
		entries = new ArrEntry[ capacity ];
		
		for ( int i = size - 1; i >= 0; i-- )
			entries[ i ] = oldEntries[ i ];
	}
	
	@Override
	public V put( final K key, final V value ) {
		final ArrEntry< K, V > entry = getEntry( key );
		
		if ( entry == null ) {
			if ( entries.length == size )
				ensureCapacity( entries.length < 2 ? 2 : entries.length * 2 ); // Double the size
				
			entries[ size++ ] = new ArrEntry<>( key, value );
			return null;
		} else
			return entry.setValue( value );
	}
	
	@Override
	public V remove( final Object key ) {
		final ArrEntry< K, V > entry = getEntry( key );
		if ( entry == null )
			return null;
		
		// Copy last entry to the entry's place, and clear it's reference, and decrease size
		// Note: entrySet().iterator() counts on the fact that entries having less index than the removed entry are not changed.
		size--;
		for ( int i = size; i >= 0; i-- )
			if ( entries[ i ] == entry ) {
				entries[ i ] = entries[ size ];
				entries[ size ] = null;
				break;
			}
		
		return entry.value;
	}
	
	@Override
	public void putAll( final Map< ? extends K, ? extends V > m ) {
		// New capacity of size+m.size() is not necessarily required as keys might be in both maps
		// but this is the fastest estimation.
		ensureCapacity( size + m.size() );
		
		for ( final Entry< ? extends K, ? extends V > entry : m.entrySet() )
			put( entry.getKey(), entry.getValue() );
	}
	
	@Override
	public void clear() {
		// Besides zeroing the size, clear entry references because we keep the array (and so would the references be kept!)
		for ( int i = size - 1; i >= 0; i-- )
			entries[ i ] = null;
		
		size = 0;
	}
	
	@Override
	public Set< K > keySet() {
		return new AbstractSet< K >() {
			
			@Override
			public Iterator< K > iterator() {
				return new Iterator< K >() {
					private int idx = 0; // Going upward to preserve the adding order
					                     
					@Override
					public boolean hasNext() {
						return idx < size;
					}
					
					@Override
					public K next() {
						return entries[ idx++ ].key;
					}
					
					@Override
					public void remove() {
						// Iterator goes upward, and ArrayMap.remove() puts the last entry to the removed entry
						// so it does not cause trouble to remove the current (previous) entry (but it has to be visited again)
						ArrayMap.this.remove( entries[ --idx ].key );
					}
				};
			}
			
			@Override
			public int size() {
				return size;
			}
			
			@Override
			public boolean contains( final Object o ) {
				return ArrayMap.this.containsKey( o );
			}
			
			@Override
			public void clear() {
				ArrayMap.this.clear();
			}
		};
	}
	
	@Override
	public Collection< V > values() {
		return new AbstractCollection< V >() {
			
			@Override
			public Iterator< V > iterator() {
				return new Iterator< V >() {
					private int idx = 0; // Going upward to preserve the adding order
					                     
					@Override
					public boolean hasNext() {
						return idx < size;
					}
					
					@Override
					public V next() {
						return entries[ idx++ ].value;
					}
					
					@Override
					public void remove() {
						// Iterator goes upward, and ArrayMap.remove() puts the last entry to the removed entry
						// so it does not cause trouble to remove the current (previous) entry (but it has to be visited again)
						ArrayMap.this.remove( entries[ --idx ].key );
					}
				};
			}
			
			@Override
			public int size() {
				return size;
			}
			
			@Override
			public boolean contains( final Object o ) {
				return ArrayMap.this.containsValue( o );
			}
			
			@Override
			public void clear() {
				ArrayMap.this.clear();
			}
		};
	}
	
	@Override
	public Set< Entry< K, V > > entrySet() {
		return new AbstractSet< Map.Entry< K, V > >() {
			
			@Override
			public Iterator< Entry< K, V > > iterator() {
				return new Iterator< Entry< K, V > >() {
					private int idx = 0; // Going upward to preserve the adding order
					                     
					@Override
					public boolean hasNext() {
						return idx < size;
					}
					
					@Override
					public Entry< K, V > next() {
						return entries[ idx++ ];
					}
					
					@Override
					public void remove() {
						// Iterator goes upward, and ArrayMap.remove() puts the last entry to the removed entry
						// so it does not cause trouble to remove the current (previous) entry (but it has to be visited again)
						ArrayMap.this.remove( entries[ --idx ].key );
					}
				};
			}
			
			@Override
			public int size() {
				return size;
			}
			
			@Override
			public void clear() {
				ArrayMap.this.clear();
			}
		};
	}
	
}
