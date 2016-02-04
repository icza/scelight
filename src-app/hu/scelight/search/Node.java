/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight.search;

import hu.scelightapi.bean.repfilters.IRepFilterBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Node of a parsed replay filters tree.
 * 
 * @author Andras Belicza
 */
class Node {
	
	/** Logical connection between the elements of the node. Either {@link Connection#AND} or {@link Connection#OR}. */
	public final Connection     connection;
	
	/** List of child nodes ({@link Node}) and leafs ({@link IRepFilterBean}). */
	public final List< Object > childList = new ArrayList<>( 4 );
	
	/** Level of this node. */
	public int                  level;
	
	/**
	 * Creates a new {@link Node}.
	 * 
	 * @param connection logical connection between the elements of the node
	 * @throws IllegalArgumentException if connection is not {@link Connection#AND} and not {@link Connection#OR}
	 */
	public Node( final Connection connection ) throws IllegalArgumentException {
		if ( connection != Connection.AND && connection != Connection.OR )
			throw new IllegalArgumentException( "Connection can only be AND or OR!" );
		
		this.connection = connection;
	}
	
	
	/** Cached filters count. */
	private int filtersCount = -1;
	
	/**
	 * Returns the number of filters contained by this node, either as child or a filter of child nodes, recursively.
	 * 
	 * <p>
	 * The returned value is cached, so this should only be called when the node is fully constructed and children are not added/removed anymore.
	 * </p>
	 * 
	 * @return the number of filters contained by this node, either as child or a filter of child nodes, recursively
	 */
	public int getFiltersCount() {
		if ( filtersCount < 0 ) {
			int count = 0;
			
			for ( final Object child : childList ) {
				if ( child instanceof IRepFilterBean )
					count++;
				else if ( child instanceof Node )
					count += ( (Node) child ).getFiltersCount();
			}
			filtersCount = count;
		}
		
		return filtersCount;
	}
	
	/**
	 * Returns the relative level of the deepest descendant of this node.
	 * 
	 * <p>
	 * If there are only {@link IRepFilterBean} children, the deepest descendant level is 1.
	 * </p>
	 * 
	 * @return the level of the deepest descendant of this node
	 */
	public int getDeepestLevel() {
		int deepestLevel = 1;
		
		for ( final Object child : childList ) {
			if ( child instanceof Node )
				deepestLevel = Math.max( deepestLevel, 1 + ( (Node) child ).getDeepestLevel() );
		}
		
		return deepestLevel;
	}
	
}
