/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.sltool.registration;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Constants for the RSA secure library.
 * 
 * @author Andras Belicza
 */
class RsaConsts {
	
	/** Magic word of the registration files (<code>"SLREG"</code>). */
	public static final byte[] REG_FILE_MAGIC      = new byte[] { 'S', 'L', 'R', 'E', 'G' };
	
	/** Handled registration file version. */
	public static final byte[] REG_FILE_VERSION    = new byte[] { 0, 1 };
	
	/** Accepted / valid key selector. */
	public static final byte[] KEY_SELECTOR        = new byte[] { 0, 1 };
	
	
	
	/** Key size in bits. */
	public static final int    KEY_SIZE_BITS       = 3072;                                                 // 1024, 2048, 3072
	                                                                                                        
	                                                                                                        
	                                                                                                        
	/** Base path for the RSA utility apps. */
	public static final Path   PATH_SECRET_BASE    = Paths.get( "dev-data/registration" ).toAbsolutePath();
	
	
	
	/** Public key path to save to. */
	public static final Path   PATH_PUBLIC_KEY     = PATH_SECRET_BASE.resolve( "keys/pubkey.rsa" );
	
	/** Private key path to save to. */
	public static final Path   PATH_PRIVATE_KEY    = PATH_SECRET_BASE.resolve( "keys/privkey.rsa" );
	
	
	
	/** Input path to encrypt. */
	public static final Path   PATH_ENCRYPT_INPUT  = PATH_SECRET_BASE.resolve( "to-encrypt.dat" );
	
	/** Output path to write the encrypted results to. */
	public static final Path   PATH_ENCRYPT_OUTPUT = PATH_SECRET_BASE.resolve( "encrypted.dat" );
	
	
	
	/** Input path to read the encrypted from. */
	public static final Path   PATH_DECRYPT_INPUT  = PATH_ENCRYPT_OUTPUT;
	
	/** Output path to write the decrypted results to. */
	public static final Path   PATH_DECRYPT_OUTPUT = PATH_SECRET_BASE.resolve( "decrypted.dat" );
	
}
