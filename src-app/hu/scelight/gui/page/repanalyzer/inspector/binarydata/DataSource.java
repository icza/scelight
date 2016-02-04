/*
 * Project Abdra Commander
 * 
 * Copyright (c) 2012 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight.gui.page.repanalyzer.inspector.binarydata;

/**
 * Binary data source.
 * 
 * <p>
 * {@link Object#toString()} has to be overridden to provide a display name for the data source.
 * </p>
 * 
 * 
 * @author Andras Belicza
 */
abstract class DataSource {
	
	/**
	 * Returns the binary data of the data source.
	 * 
	 * <p>
	 * This implementation returns the data produced by {@link #getCustomData()}, and in case of any exception suppresses the exception and returns an empty
	 * byte array.
	 * </p>
	 * 
	 * @return the binary data of the data source
	 */
	public byte[] getData() {
		try {
			return getCustomData();
		} catch ( final Exception e ) {
			return new byte[ 0 ];
		}
	}
	
	/**
	 * Returns the binary data of the data source.
	 * 
	 * @return the binary data of the data source
	 * @throws Exception if any error occurs during acquiring / assembling the data
	 */
	public abstract byte[] getCustomData() throws Exception;
	
	/**
	 * Returns the (suggested) file name for the data.
	 * 
	 * @return the (suggested) file name for the data
	 */
	public abstract String getFileName();
	
	/**
	 * Must return a display name for the data source.
	 * 
	 * @return a display name for the data source
	 */
	@Override
	public abstract String toString();
	
}
