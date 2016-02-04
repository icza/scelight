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

/**
 * Describes a field in structures.
 * 
 * <p>
 * Fields used for structures (<code>"_struct"</code>) have/use the {@link #tag} attribute, fields used for choices ( <code>"_choice"</code>) omit the
 * {@link #tag}.
 * </p>
 * 
 * <p>
 * Instances are IMMUTABLE.
 * </p>
 * 
 * @author Andras Belicza
 */
public class Field {
	
	/** Field name indicating the parent. */
	public static final String NAME_PARENT = "__parent";
	
	
	/** Name of the field. */
	public final String        name;
	
	/** Type id of the field's value. */
	public int                 typeid;
	
	/** Optional tag of the field (often used for field index). */
	public int                 tag;
	
	
	/**
	 * Tells if field name equals to <code>{@value #NAME_PARENT}</code>.<br>
	 * For optimization purposes: this check is performed many times, and the result is always the same.
	 */
	public boolean             isNameParent;
	
	
	/**
	 * Creates a new {@link Field}.
	 * 
	 * @param name name of the field
	 * @param typeid type id of the field's value
	 */
	public Field( final String name, final int typeid ) {
		this( name, typeid, 0 );
	}
	
	/**
	 * Creates a new {@link Field}.
	 * 
	 * @param name name of the field
	 * @param typeid type id of the field's value
	 * @param tag tag of the field
	 */
	public Field( final String name, final int typeid, final int tag ) {
		// Most field names start with "m_". Cut that off.
		// String.intern() them because the same names are used in all protocol data files!
		// Getting them using intern()-ed names will be much faster! (pre-calculated hash, reference equality etc.)
		
		this.name = ( name.startsWith( "m_" ) ? name.substring( 2 ) : name ).intern();
		this.typeid = typeid;
		this.tag = tag;
		
		isNameParent = NAME_PARENT.equals( name );
	}
	
}
