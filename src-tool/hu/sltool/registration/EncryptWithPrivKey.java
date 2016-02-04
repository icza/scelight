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

import hu.sllauncher.bean.person.ContactBean;
import hu.sllauncher.bean.person.PersonBean;
import hu.sllauncher.bean.person.PersonNameBean;
import hu.sllauncher.bean.reginfo.RegInfoBean;
import hu.sllauncher.bean.reginfo.SysInfoBean;
import hu.sllauncher.service.env.LEnv;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.RSAPrivateKeySpec;
import java.util.Date;
import java.util.TimeZone;
import java.util.zip.DeflaterOutputStream;

import javax.crypto.Cipher;
import javax.xml.bind.JAXB;

/**
 * Private key encryption utility.
 * 
 * @author Andras Belicza
 */
public class EncryptWithPrivKey {
	
	/**
	 * Loads a private key from a file and encrypts an input file to another file.
	 * 
	 * @param args not used
	 * @throws Exception if some error occurs
	 */
	public static void main( final String[] args ) throws Exception {
		final KeyFactory kf = KeyFactory.getInstance( "RSA" );
		
		final Object[] sizeModExp = loadKey( RsaConsts.PATH_PRIVATE_KEY );
		final int keySize = (Integer) sizeModExp[ 0 ];
		final PrivateKey privKey = kf.generatePrivate( new RSAPrivateKeySpec( (BigInteger) sizeModExp[ 1 ], (BigInteger) sizeModExp[ 2 ] ) );
		
		// cipher.getBlockSize() always returns 0, calculate it ourself:
		final int blockSize = keySize / 8 - 11; // 11 bytes header in blocks
		
		final Cipher cipher = Cipher.getInstance( "RSA/ECB/PKCS1Padding" );
		cipher.init( Cipher.ENCRYPT_MODE, privKey );
		
		if ( !Files.exists( RsaConsts.PATH_ENCRYPT_INPUT ) )
			createTestInput();
		
		System.out.println( "Encrypting..." );
		final long start = System.nanoTime();
		
		// Data to encrypt
		final byte[] inData = Files.readAllBytes( RsaConsts.PATH_ENCRYPT_INPUT );
		
		// First compress (Deflate). RSA encryption and decryption is slow, make input data (and result) smaller to speed it up.
		final ByteArrayOutputStream baOut = new ByteArrayOutputStream();
		try ( final OutputStream out = new DeflaterOutputStream( baOut ) ) {
			out.write( inData );
		}
		// Now encrypt
		try ( final InputStream in = new ByteArrayInputStream( baOut.toByteArray() );
		        final OutputStream out = Files.newOutputStream( RsaConsts.PATH_ENCRYPT_OUTPUT ) ) {
			
			// Write out magic bytes
			out.write( RsaConsts.REG_FILE_MAGIC );
			
			// Write out reg file version
			out.write( RsaConsts.REG_FILE_VERSION );
			
			// Write out key selector
			out.write( RsaConsts.KEY_SELECTOR );
			
			final byte[] buffer = new byte[ blockSize ];
			
			int bytesRead;
			byte[] result;
			while ( ( bytesRead = in.read( buffer ) ) > 0 ) {
				result = cipher.doFinal( buffer, 0, bytesRead );
				if ( result != null )
					out.write( result );
			}
		}
		
		final long end = System.nanoTime();
		System.out.println( "Done. (" + ( ( end - start ) / 1_000_000 ) + " ms)" );
		
		Files.deleteIfExists( RsaConsts.PATH_DECRYPT_OUTPUT );
	}
	
	/**
	 * Creates test input data.
	 * 
	 * @throws IOException if any error occurs
	 */
	private static void createTestInput() throws IOException {
		final RegInfoBean regInfo = new RegInfoBean();
		regInfo.setGoogleAccount( "iczaaa@gmail.com" );
		
		final PersonBean person = new PersonBean();
		person.setPersonName( new PersonNameBean( "András", null, "Belicza", "Dakota_Fanning" ) );
		final ContactBean contact = new ContactBean();
		contact.setLocation( "Budapest, Hungary" );
		contact.setEmail( "iczaaa@gmail.com" );
		person.setContact( contact );
		person.setDescription( "BWHF, Sc2gears and Scelight author" );
		regInfo.setPerson( person );
		
		final SysInfoBean sysInfo = new SysInfoBean();
		final OperatingSystemMXBean bean = ManagementFactory.getOperatingSystemMXBean();
		sysInfo.setOsName( bean.getName() );
		sysInfo.setOsVersion( bean.getVersion() );
		sysInfo.setAvailProcs( bean.getAvailableProcessors() );
		sysInfo.setUserName( System.getProperty( "user.name" ) );
		sysInfo.setUserCountry( System.getProperty( "user.country" ) );
		// Reset/clear time zone so the system time zone will be initialized and returned when queried
		System.setProperty( "user.timezone", "" );
		sysInfo.setUserTimeZone( TimeZone.getDefault().getID() );
		sysInfo.setDate( new Date() );
		try {
			sysInfo.setMainRootSize( Files.getFileStore( Paths.get( "c:/" ) ).getTotalSpace() );
		} catch ( final IOException ie ) {
			LEnv.LOGGER.error( "Failed to get main root size!", ie );
		}
		regInfo.setSysInfo( sysInfo );
		
		regInfo.setRegDate( new Date() );
		
		regInfo.setEncryptionDate( new Date() );
		
		JAXB.marshal( regInfo, RsaConsts.PATH_ENCRYPT_INPUT.toFile() );
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
