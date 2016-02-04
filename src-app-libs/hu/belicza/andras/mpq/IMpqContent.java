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

import hu.belicza.andras.mpq.AlgorithmUtil.MpqHashType;

/**
 * Interface defining an MPQ content which has a file name and alternate hashes identifying it inside the MPQ archive.
 * 
 * @author Andras Belicza
 */
public interface IMpqContent {
	
	/**
	 * Returns the file name of the content.
	 * 
	 * @return the file name of the content
	 */
	String getFileName();
	
	/**
	 * Returns the {@link MpqHashType#TABLE_OFFSET} hash value of the file name.
	 * 
	 * @return the {@link MpqHashType#TABLE_OFFSET} hash value of the file name.
	 */
	int getHash1();
	
	/**
	 * Returns the {@link MpqHashType#NAME_A} hash value of the file name.
	 * 
	 * @return the {@link MpqHashType#NAME_A} hash value of the file name.
	 */
	int getHash2();
	
	/**
	 * Returns the {@link MpqHashType#NAME_B} hash value of the file name.
	 * 
	 * @return the {@link MpqHashType#NAME_B} hash value of the file name.
	 */
	int getHash3();
	
}
