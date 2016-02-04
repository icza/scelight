/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight.sc2.rep.s2prot.type;

import java.util.ArrayList;
import java.util.List;

/**
 * Decoding instructions for a specific type.
 * 
 * @author Andras Belicza
 */
public class TypeInfo {
	
	/** Method enum that reads/parses the type. */
	public final TypeMethod method;
	
	/** First optional parameter to pass to the method. */
	public final Object     param1;
	
	/** Second optional parameter to pass to the method. */
	public final Object     param2;
	
	/**
	 * Creates a new {@link TypeInfo}.
	 * 
	 * @param method method enum that reads/parses the type
	 */
	public TypeInfo( final TypeMethod method ) {
		this( method, null, null );
	}
	
	/**
	 * Creates a new {@link TypeInfo}.
	 * 
	 * @param method method enum that reads/parses the type
	 * @param param1 first optional parameter to pass to the method
	 */
	public TypeInfo( final TypeMethod method, final Object param1 ) {
		this( method, param1, null );
	}
	
	/**
	 * Creates a new {@link TypeInfo}.
	 * 
	 * @param method method enum that reads/parses the type
	 * @param param1 first optional parameter to pass to the method
	 * @param param2 second optional parameter to pass to the method
	 */
	public TypeInfo( final TypeMethod method, final Object param1, final Object param2 ) {
		this.method = method;
		this.param1 = param1;
		this.param2 = param2;
	}
	
	
	/**
	 * Parses a {@link TypeInfo} from a python string representation.
	 * 
	 * @param src src to parse type info from
	 * @return the parsed type info
	 */
	public static TypeInfo fromString( final String src ) {
		// Method name inside ''
		int i = src.indexOf( '\'' );
		int j = src.indexOf( '\'', i + 1 );
		int k;
		
		switch ( src.substring( i + 1, j ) ) {
			case "_int" : {
				// ('_int',[(0,7)]), #0
				// Parameter is a bounds (2 integers)
				i = src.indexOf( '(', j + 1 );
				j = src.indexOf( ',', i + 1 );
				k = src.indexOf( ')', j + 1 );
				
				final int bits = Integer.parseInt( src.substring( j + 1, k ) );
				if ( bits > 32 )
					return new TypeInfo( TypeMethod.LONG, new LongBounds( Long.parseLong( src.substring( i + 1, j ) ), bits ) );
				else
					return new TypeInfo( TypeMethod.INT, new IntBounds( Integer.parseInt( src.substring( i + 1, j ) ), bits ) );
			}
			case "_struct" : {
				// ('_struct',[[('m_signature',9,0),('m_version',11,1),('m_type',12,2),('m_elapsedGameLoops',6,3)]]), #13
				// Parameters: fields
				// Field: field name, typeid, tag
				final List< Field > fieldList = new ArrayList<>();
				
				int l, m = j;
				while ( true ) {
					i = src.indexOf( '\'', m + 1 );
					if ( i < 0 )
						break;
					j = src.indexOf( '\'', i + 1 );
					k = src.indexOf( ',', j + 1 );
					l = src.indexOf( ',', k + 1 );
					m = src.indexOf( ')', l + 1 );
					
					fieldList.add( new Field( src.substring( i + 1, j ), Integer.parseInt( src.substring( k + 1, l ) ), Integer.parseInt( src.substring( l + 1,
					        m ) ) ) );
				}
				
				return new TypeInfo( TypeMethod.STRUCT, fieldList.toArray( new Field[ fieldList.size() ] ) );
			}
			case "_choice" : {
				// ('_choice',[(0,2),{0:('None',82),1:('TargetPoint',83),2:('TargetUnit',84),3:('Data',6)}]), #85
				// Parameters: IntBounds and field list (specified as a field map)
				// Field entry: field index maps to a pair of field name+typeid
				i = src.indexOf( '(', j + 1 );
				j = src.indexOf( ',', i + 1 );
				k = src.indexOf( ')', j + 1 );
				
				final IntBounds bounds = new IntBounds( Integer.parseInt( src.substring( i + 1, j ) ), Integer.parseInt( src.substring( j + 1, k ) ) );
				
				final List< Field > fieldList = new ArrayList<>();
				
				int l = k;
				while ( true ) {
					i = src.indexOf( '\'', l + 1 );
					if ( i < 0 )
						break;
					j = src.indexOf( '\'', i + 1 );
					k = src.indexOf( ',', j + 1 );
					l = src.indexOf( ')', k + 1 );
					
					fieldList.add( new Field( src.substring( i + 1, j ), Integer.parseInt( src.substring( k + 1, l ) ) ) );
				}
				
				return new TypeInfo( TypeMethod.CHOICE, bounds, fieldList.toArray( new Field[ fieldList.size() ] ) );
			}
			case "_array" : {
				// ('_array',[(0,5),21]), #22
				// Parameters are an IntBounds (2 integers) and a typeid
				i = src.indexOf( '(', j + 1 );
				j = src.indexOf( ',', i + 1 );
				k = src.indexOf( ')', j + 1 );
				
				final IntBounds bounds = new IntBounds( Integer.parseInt( src.substring( i + 1, j ) ), Integer.parseInt( src.substring( j + 1, k ) ) );
				
				i = src.indexOf( ',', k + 1 );
				j = src.indexOf( ']', i + 1 );
				
				return new TypeInfo( TypeMethod.ARRAY, bounds, Integer.parseInt( src.substring( i + 1, j ) ) );
			}
			case "_bitarray" : {
				// ('_bitarray',[(0,6)]), #45
				// Parameter is an IntBounds (2 integers)
				i = src.indexOf( '(', j + 1 );
				j = src.indexOf( ',', i + 1 );
				k = src.indexOf( ')', j + 1 );
				
				return new TypeInfo( TypeMethod.BITARRAY, new IntBounds( Integer.parseInt( src.substring( i + 1, j ) ), Integer.parseInt( src.substring( j + 1,
				        k ) ) ) );
			}
			case "_blob" : {
				// ('_blob',[(0,10)]), #24
				// Parameter is an IntBounds (2 integers)
				i = src.indexOf( '(', j + 1 );
				j = src.indexOf( ',', i + 1 );
				k = src.indexOf( ')', j + 1 );
				
				return new TypeInfo( TypeMethod.BLOB, new IntBounds( Integer.parseInt( src.substring( i + 1, j ) ),
				        Integer.parseInt( src.substring( j + 1, k ) ) ) );
			}
			case "_optional" : {
				// ('_optional',[10]), #20
				// Parameter is a typeid
				i = src.indexOf( '[', j + 1 );
				j = src.indexOf( ']', i + 1 );
				
				return new TypeInfo( TypeMethod.OPTIONAL, Integer.parseInt( src.substring( i + 1, j ) ) );
			}
			case "_bool" :
				// ('_bool',[]), #27
				return new TypeInfo( TypeMethod.BOOL );
			case "_fourcc" :
				// ('_fourcc',[]), #14
				return new TypeInfo( TypeMethod.FOURCC );
			case "_null" : {
				// ('_null',[]), #82
				return new TypeInfo( TypeMethod.NULL );
			}
		}
		
		return null;
	}
}
