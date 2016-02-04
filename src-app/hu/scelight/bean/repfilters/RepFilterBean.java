/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight.bean.repfilters;

import hu.belicza.andras.util.VersionView;
import hu.scelight.search.Connection;
import hu.scelight.search.FilterBy;
import hu.scelight.search.FilterByGroup;
import hu.scelight.search.Operator;
import hu.scelight.service.env.Env;
import hu.scelight.util.Utils;
import hu.scelightapi.bean.repfilters.IRepFilterBean;
import hu.scelightapi.search.IConnection;
import hu.scelightapi.search.IFilterBy;
import hu.scelightapi.search.IFilterByGroup;
import hu.scelightapi.search.IOperator;
import hu.sllauncher.bean.IdedBean;

import java.util.Date;
import java.util.Objects;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlTransient;

/**
 * Replay filter bean.
 * 
 * @author Andras Belicza
 */
@XmlAccessorType( XmlAccessType.FIELD )
public class RepFilterBean extends IdedBean implements IRepFilterBean {
	
	/** Current bean version. */
	public static final int BEAN_VER = 1;
	
	/** Tells if the filter is enabled. */
	private Boolean         enabled;
	
	/** Logical connection. */
	private Connection      connection;
	
	/** Filter by group. */
	private FilterByGroup   filterByGroup;
	
	/** Filter by. */
	private FilterBy        filterBy;
	
	/** Operator. */
	private Operator        operator;
	
	/** String representation of the filter value. */
	private String          valueString;
	
	/** Typed object value of the filter. */
	@XmlTransient
	private Object          value;
	
	/** Comment of the filter. */
	private String          comment;
	
	/**
	 * Creates and returns a new filter initialized with default values.
	 * 
	 * @return a new filter initialized with default values
	 */
	public static RepFilterBean createNewFilter() {
		final RepFilterBean filterBean = new RepFilterBean();
		
		filterBean.setEnabled( Boolean.TRUE );
		filterBean.setConnection( Connection.VALUES[ 0 ] );
		filterBean.setFilterByGroup( FilterByGroup.REPLAY );
		filterBean.setFilterBy( filterBean.getFilterByGroup().filterBys[ 0 ] );
		filterBean.setOperator( filterBean.getFilterBy().operators[ 0 ] );
		
		return filterBean;
	}
	
	/**
	 * Creates a new {@link RepFilterBean}.
	 */
	public RepFilterBean() {
		super( BEAN_VER );
	}
	
	@Override
	public boolean isActive() {
		return Boolean.TRUE.equals( enabled ) && isValid();
	}
	
	@Override
	public boolean isValid() {
		if ( connection == null || filterByGroup == null || filterBy == null || operator == null || valueString == null || valueString.isEmpty() )
			return false;
		
		return Utils.contains( filterByGroup.filterBys, filterBy ) && Utils.contains( filterBy.operators, operator );
	}
	
	@Override
	public Boolean getEnabled() {
		return enabled;
	}
	
	@Override
	public void setEnabled( Boolean enabled ) {
		this.enabled = enabled;
	}
	
	@Override
	public Connection getConnection() {
		return connection;
	}
	
	/**
	 * Sets the logical connection of this filter.
	 * 
	 * @param connection logical connection of this filter to be set
	 */
	public void setConnection( Connection connection ) {
		this.connection = connection;
	}
	
	@Override
	public FilterByGroup getFilterByGroup() {
		return filterByGroup;
	}
	
	/**
	 * Sets the filter by group.
	 * 
	 * @param filterByGroup filter by group to be set
	 */
	public void setFilterByGroup( FilterByGroup filterByGroup ) {
		this.filterByGroup = filterByGroup;
	}
	
	@Override
	public FilterBy getFilterBy() {
		return filterBy;
	}
	
	/**
	 * Sets the filter by.
	 * 
	 * @param filterBy filter by to be set
	 */
	public void setFilterBy( FilterBy filterBy ) {
		this.filterBy = filterBy;
	}
	
	@Override
	public Operator getOperator() {
		return operator;
	}
	
	/**
	 * Sets the operator.
	 * 
	 * @param operator operator to be set
	 */
	public void setOperator( Operator operator ) {
		this.operator = operator;
	}
	
	@Override
	public Object getValue() {
		if ( value == null && valueString != null && !valueString.isEmpty() ) {
			// If any error occurs parsing the value, simply discard it.
			try {
				if ( filterBy.type == String.class )
					value = valueString;
				else if ( filterBy.type.isEnum() ) {
					// Cannot use Enum.valueOf( class, name )
					// Reflection to get the enum instance
					value = filterBy.type.getMethod( "valueOf", String.class ).invoke( filterBy.type, valueString );
				} else if ( filterBy.type == Date.class )
					value = new Date( Long.valueOf( valueString ) );
				else if ( filterBy.type == Integer.class )
					value = Integer.valueOf( valueString );
				else if ( filterBy.type == Boolean.class )
					value = Boolean.valueOf( valueString );
				else if ( filterBy.type == VersionView.class ) {
					Objects.requireNonNull( value = VersionView.fromString( valueString ) );
				} else {
					Env.LOGGER.error( "Unhandled filter by type: " + filterBy.type.getName() );
					throw new RuntimeException( "Unhandled filter by type: " + filterBy.type.getName() );
				}
			} catch ( final Exception e ) {
				// Discarding value...
				valueString = null;
			}
		}
		
		return value;
	}
	
	@Override
	public void setValue( Object value ) {
		this.value = value;
		
		if ( value == null )
			valueString = null;
		else if ( value instanceof String ) {
			valueString = (String) value;
			if ( valueString.isEmpty() )
				this.value = valueString = null;
		} else if ( value instanceof Enum )
			valueString = ( (Enum< ? >) value ).name();
		else if ( value instanceof Date )
			valueString = Long.toString( ( (Date) value ).getTime() );
		else if ( value instanceof VersionView )
			valueString = ( (VersionView) value ).toString();
		else
			valueString = value.toString();
	}
	
	@Override
	public String getComment() {
		return comment;
	}
	
	@Override
	public void setComment( String comment ) {
		this.comment = comment;
	}
	
	@Override
	public void setConnection( IConnection connection ) {
		setConnection( Connection.valueOf( connection.name() ) );
	}
	
	@Override
	public void setFilterByGroup( IFilterByGroup filterByGroup ) {
		setFilterByGroup( FilterByGroup.valueOf( filterByGroup.name() ) );
	}
	
	@Override
	public void setFilterBy( IFilterBy filterBy ) {
		setFilterBy( FilterBy.valueOf( filterBy.name() ) );
	}
	
	@Override
	public void setOperator( IOperator operator ) {
		setOperator( Operator.valueOf( operator.name() ) );
	}
	
}
