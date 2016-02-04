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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.util.zip.InflaterInputStream;

import javax.crypto.Cipher;

/**
 * Public key decryption utility.
 * 
 * @author Andras Belicza
 */
public class DecryptWithPubKey {
	
	/**
	 * Loads a public key from a file and decrypts an input file to another file.
	 * 
	 * @param args not used
	 * @throws Exception if some error occurs
	 */
	public static void main( final String[] args ) throws Exception {
		final KeyFactory kf = KeyFactory.getInstance( "RSA" );
		
		final Object[] sizeModExp = loadKey( RsaConsts.PATH_PUBLIC_KEY );
		final int keySize = (Integer) sizeModExp[ 0 ];
		final PublicKey pubKey = kf.generatePublic( new RSAPublicKeySpec( (BigInteger) sizeModExp[ 1 ], (BigInteger) sizeModExp[ 2 ] ) );
		
		// cipher.getBlockSize() always returns 0, calculate it ourself:
		final int blockSize = keySize / 8;
		
		final Cipher cipher = Cipher.getInstance( "RSA/ECB/PKCS1Padding" );
		cipher.init( Cipher.DECRYPT_MODE, pubKey );
		
		System.out.println( "Decrypting..." );
		final long start = System.nanoTime();
		
		// First decrypt.
		final ByteArrayOutputStream baOut = new ByteArrayOutputStream();
		try ( final InputStream in = Files.newInputStream( RsaConsts.PATH_DECRYPT_INPUT ) ) {
			// Skip header (magic word, version and key selector)
			final int headerSize = RsaConsts.REG_FILE_MAGIC.length + RsaConsts.REG_FILE_VERSION.length + RsaConsts.KEY_SELECTOR.length;
			for ( int i = 0; i < headerSize; i++ )
				in.read();
			
			final byte[] buffer = new byte[ blockSize ];
			
			int bytesRead;
			byte[] result;
			while ( ( bytesRead = in.read( buffer ) ) > 0 ) {
				result = cipher.doFinal( buffer, 0, bytesRead );
				if ( result != null )
					baOut.write( result );
			}
		}
		
		// Now decompress (Inflate)
		try ( final InputStream in = new InflaterInputStream( new ByteArrayInputStream( baOut.toByteArray() ) );
		        final OutputStream out = Files.newOutputStream( RsaConsts.PATH_DECRYPT_OUTPUT ) ) {
			final byte[] buffer = new byte[ blockSize ];
			
			int bytesRead;
			while ( ( bytesRead = in.read( buffer ) ) > 0 ) {
				out.write( buffer, 0, bytesRead );
			}
		}
		
		final long end = System.nanoTime();
		System.out.println( "Done. (" + ( ( end - start ) / 1_000_000 ) + " ms)" );
	}
	
	/**
	 * Loads a key from the specified file.
	 * 
	 * @param path path to load the key from
	 * @return the loaded key, array of { (Integer) keySize, (BigInteger) modulus, (BigInteger) exponent }
	 * @throws Exception if some error occurs
	 */
	public static Object[] loadKey( final Path path ) throws Exception {
		final Object[] sizeModExp = new Object[ 3 ];
		
		try ( final DataInputStream in = new DataInputStream( Files.newInputStream( path ) ) ) {
			// Key size
			sizeModExp[ 0 ] = in.readInt();
			
			byte[] buff = new byte[ in.readInt() ];
			in.read( buff );
			sizeModExp[ 1 ] = new BigInteger( buff );
			
			buff = new byte[ in.readInt() ];
			in.read( buff );
			sizeModExp[ 2 ] = new BigInteger( buff );
		}
		
		return sizeModExp;
	}
	
}
