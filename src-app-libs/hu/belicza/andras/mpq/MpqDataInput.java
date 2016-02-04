/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.belicza.andras.mpq;

import java.io.Closeable;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Path;

/**
 * Data provider for the {@link MpqParser}.
 * 
 * @author Andras Belicza
 */
public interface MpqDataInput extends Closeable {
	
	/**
	 * Returns the data provider's position.
	 * 
	 * @return the data provider's position
	 * @throws IOException if some error occurs
	 */
	long position() throws IOException;
	
	/**
	 * Sets the data provider's position.
	 * 
	 * @param newPosition new position to be set
	 * @throws IOException if some error occurs
	 */
	void position( long newPosition ) throws IOException;
	
	/**
	 * Reads bytes from the data provider into the specified byte buffer.
	 * 
	 * <p>
	 * Reading starts at the current position of this data provider, and the position is incremented by the number of read bytes.
	 * </p>
	 * 
	 * <p>
	 * The read bytes are put into the specified byte buffer, starting from its current position. This method attempts to read as many bytes as are available in
	 * the specified buffer.
	 * </p>
	 * 
	 * @param destination byte buffer to put the read bytes into
	 * @return the number of read bytes
	 * @throws IOException if some error occurs
	 */
	long read( ByteBuffer destination ) throws IOException;
	
	/**
	 * Returns the file the data source is a file.
	 * 
	 * @return the file the data source is a file; <code>null</code> otherwise
	 */
	Path getFile();
	
}
