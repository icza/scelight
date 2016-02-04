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

import java.io.DataOutputStream;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;

/**
 * RSA key pair generation utility.
 * 
 * @author Andras Belicza
 */
public class RsaKeyPairGen {
	
	/** Tells whether to print some debug info. */
	private static final boolean DEBUG = false;
	
	
	/**
	 * Generates a private-public key pair and writes them to files.
	 * 
	 * @param args not used
	 * @throws Exception if some error occurs
	 */
	public static void main( final String[] args ) throws Exception {
		System.out.println( "Generating public-private key pair..." );
		final long start = System.nanoTime();
		
		final KeyPairGenerator kpGen = KeyPairGenerator.getInstance( "RSA" );
		kpGen.initialize( RsaConsts.KEY_SIZE_BITS );
		final KeyPair kp = kpGen.generateKeyPair();
		
		final PublicKey pubKey = kp.getPublic();
		final PrivateKey privKey = kp.getPrivate();
		
		if ( DEBUG ) {
			System.out.println( pubKey.getAlgorithm() + " " + pubKey.getFormat() + " " + pubKey.getEncoded().length );
			System.out.println( privKey.getAlgorithm() + " " + privKey.getFormat() + " " + privKey.getEncoded().length );
		}
		
		final KeyFactory kf = KeyFactory.getInstance( "RSA" );
		final RSAPublicKeySpec pubKeySpec = kf.getKeySpec( pubKey, RSAPublicKeySpec.class );
		final RSAPrivateKeySpec privKeySpec = kf.getKeySpec( privKey, RSAPrivateKeySpec.class );
		
		if ( DEBUG ) {
			System.out.println( pubKeySpec.getModulus() + " " + pubKeySpec.getPublicExponent() );
			System.out.println( privKeySpec.getModulus() + " " + privKeySpec.getPrivateExponent() );
		}
		
		saveKey( pubKeySpec.getModulus(), pubKeySpec.getPublicExponent(), RsaConsts.PATH_PUBLIC_KEY );
		saveKey( privKeySpec.getModulus(), privKeySpec.getPrivateExponent(), RsaConsts.PATH_PRIVATE_KEY );
		
		final long end = System.nanoTime();
		System.out.println( "Done. (" + ( ( end - start ) / 1_000_000 ) + " ms)" );
	}
	
	/**
	 * Saves a key to the specified file.
	 * 
	 * @param mod modulus part of the key to be saved
	 * @param exp exponent part of the key to be saved
	 * @param path path to save the key to
	 * @throws Exception if some error occurs
	 */
	private static void saveKey( final BigInteger mod, final BigInteger exp, final Path path ) throws Exception {
		try ( final DataOutputStream out = new DataOutputStream( Files.newOutputStream( path ) ) ) {
			
			out.writeInt( RsaConsts.KEY_SIZE_BITS );
			
			byte[] buff = mod.toByteArray();
			out.writeInt( buff.length );
			out.write( buff );
			
			buff = exp.toByteArray();
			out.writeInt( buff.length );
			out.write( buff );
		}
	}
	
}
