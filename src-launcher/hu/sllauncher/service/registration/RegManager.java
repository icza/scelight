/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.sllauncher.service.registration;

import hu.sllauncher.bean.reginfo.RegInfoBean;
import hu.sllauncher.service.env.LEnv;
import hu.sllauncher.util.LUtils;
import hu.sllauncher.util.secure.DecryptUtil;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Calendar;
import java.util.Date;
import java.util.zip.InflaterInputStream;

import javax.xml.bind.JAXB;

/**
 * Registration manager.
 * 
 * @author Andras Belicza
 */
public class RegManager {
	
	/** Magic word of the registration files (<code>"SLREG"</code>). */
	private static final byte[] REG_FILE_MAGIC             = new byte[] { 'S', 'L', 'R', 'E', 'G' };
	
	/** Handled registration file version. */
	private static final byte[] REG_FILE_VERSION           = new byte[] { 0, 1 };
	
	/** Bytes of accepted / valid key selector. */
	private static final byte[] KEY_SELECTOR               = new byte[] { (byte) ( DecryptUtil.CURRENT_KEY_SELECTOR >> 8 ),
	        (byte) DecryptUtil.CURRENT_KEY_SELECTOR       };
	
	
	/** Registration file path. */
	public static final Path    PATH_REGISTRATION_FILE     = LEnv.PATH_WORKSPACE.resolve( "reginfo.slreg" );
	
	
	/** Expiration time of the registration file in months (calculated from the reg file encryption date). */
	public static final int     REG_FILE_EXPIRATION_MONTHS = 6;
	
	/** Registration status. */
	public final RegStatus      regStatus;
	
	/** Loaded registration info. */
	private final RegInfoBean   regInfo;
	
	
	/**
	 * Creates a new {@link RegManager}.
	 */
	public RegManager() {
		// Check registration file
		if ( !Files.exists( PATH_REGISTRATION_FILE ) ) {
			regStatus = RegStatus.NOT_FOUND;
			LEnv.LOGGER.debug( "No registration file found." );
			regInfo = null;
			return;
		}
		
		// Load registration info
		RegInfoBean regInfo_;
		try {
			final byte[] regInfoEncData = Files.readAllBytes( PATH_REGISTRATION_FILE ); // "reginfo.xml.deflated.enc"
			int offset = 0;
			
			// Check magic
			for ( int i = 0; i < REG_FILE_MAGIC.length; i++, offset++ )
				if ( REG_FILE_MAGIC[ i ] != regInfoEncData[ offset ] )
					throw new RuntimeException( "Invalid magic word!" );
			
			// Check version
			for ( int i = 0; i < REG_FILE_VERSION.length; i++, offset++ )
				if ( REG_FILE_VERSION[ i ] != regInfoEncData[ offset ] )
					throw new RuntimeException( "Invalid or unsupported version!" );
			
			// Check key selector
			for ( int i = 0; i < KEY_SELECTOR.length; i++, offset++ )
				if ( KEY_SELECTOR[ i ] != regInfoEncData[ offset ] )
					throw new RuntimeException( "Invalid or unsupported key selector!" );
			
			// We're cool, decrypt...
			final byte[] regInfodeflatedXmlData = DecryptUtil.decrypt( regInfoEncData, offset, regInfoEncData.length - offset ); // "reginfo.xml.deflated"
			
			if ( regInfodeflatedXmlData == null )
				throw new Exception( "Failed to decrypt registration file!" );
			
			// ...and inflate
			try ( final InputStream in = new InflaterInputStream( new ByteArrayInputStream( regInfodeflatedXmlData ) ) ) {
				regInfo_ = JAXB.unmarshal( in, RegInfoBean.class ); // "reginfo.xml"
			}
			
			if ( LEnv.LOGGER.testDebug() )
				LEnv.LOGGER.debug( "Registration info loaded (registered to: " + regInfo_.getPerson().getPersonName() + ")." );
			
			if ( LEnv.LOGGER.testTrace() )
				LEnv.LOGGER.trace( "Registration info: " + regInfo_.toString() );
			
		} catch ( final Exception e ) {
			regStatus = RegStatus.INVALID;
			LEnv.LOGGER.error( "Invalid or corrupt registration file: " + PATH_REGISTRATION_FILE, e );
			regInfo = null;
			return;
		}
		
		regInfo = regInfo_;
		
		// Check expiration
		final Calendar expCal = Calendar.getInstance();
		expCal.setTime( regInfo.getEncryptionDate() );
		expCal.add( Calendar.MONTH, REG_FILE_EXPIRATION_MONTHS );
		if ( new Date().after( expCal.getTime() ) ) {
			regStatus = RegStatus.EXPIRED;
			LEnv.LOGGER.warning( "Registration file has expired!" );
			return;
		}
		
		// Quick check if registered system info matches current system info
		if ( !LUtils.getSysInfo().matches( regInfo.getSysInfo() ) ) {
			regStatus = RegStatus.SYSINFO_MISMATCH;
			LEnv.LOGGER.warning( "Registration info mismatch: your system info does not match the registered system info!" );
			return;
		}
		
		regStatus = RegStatus.MATCH;
	}
	
	/**
	 * @return the loaded registration info
	 */
	public RegInfoBean getRegInfo() {
		return regInfo;
	}
	
	/**
	 * Returns if registration is OK.
	 * 
	 * @return if registration is OK.
	 */
	public boolean isOk() {
		return regStatus == RegStatus.MATCH;
		
	}
	
}
