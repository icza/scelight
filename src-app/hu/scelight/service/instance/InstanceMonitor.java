/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight.service.instance;

import hu.scelight.Consts;
import hu.scelight.action.Actions;
import hu.scelight.service.env.Env;
import hu.scelight.util.Utils;
import hu.sllauncher.service.env.InstMonInfBean;
import hu.sllauncher.util.ControlledThread;

import java.io.DataInputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import javax.xml.bind.JAXB;

/**
 * Instance monitor service.
 * 
 * <p>
 * Opens a {@link ServerSocket} and listens to incoming connection from other started instances to consume arguments and focus / activate us.
 * </p>
 * 
 * @author Andras Belicza
 */
public class InstanceMonitor extends ControlledThread {
	
	/** Current communication protocol version. */
	private static final int   COM_PROT_VER = 1;
	
	/** Server socket to listen and receive incoming connections. */
	private final ServerSocket serverSocket;
	
	/**
	 * Creates a new {@link InstanceMonitor}.
	 */
	public InstanceMonitor() {
		super( "Instance monitor" );
		
		ServerSocket serverSocket = null;
		try {
			// Pass 0 as port so the system will choose a free / available port for us.
			serverSocket = new ServerSocket( 0, 0, InetAddress.getByName( "localhost" ) );
			
			Env.LOGGER.trace( "Instance monitor listening on local port: " + serverSocket.getLocalPort() );
			
			// Save instance monitor info bean
			final InstMonInfBean imi = new InstMonInfBean();
			imi.setComProtVer( COM_PROT_VER );
			imi.setPort( serverSocket.getLocalPort() );
			JAXB.marshal( imi, Env.PATH_INSTANCE_MONITOR_INFO.toFile() );
			
		} catch ( final Exception e ) {
			Env.LOGGER.error( "Failed to setup instance monitor!", e );
		}
		this.serverSocket = serverSocket;
	}
	
	@Override
	public void customRun() {
		if ( serverSocket == null )
			return;
		
		while ( mayContinue() ) {
			
			try ( final Socket s = serverSocket.accept() ) {
				Env.LOGGER.debug( "Another " + Consts.APP_NAME + " instance connected." );
				
				// Show main window even if minimized to system tray
				Actions.SHOW_MAIN_FRAME.actionPerformed( null );
				
				try ( final DataInputStream in = new DataInputStream( s.getInputStream() ) ) {
					// Communication protocol version
					final int comProtVer = in.readInt();
					if ( COM_PROT_VER != comProtVer )
						throw new Exception( "Unsupported Instance Monitor communication protocol version: " + comProtVer );
					
					final int argsCount = in.readInt();
					Env.LOGGER.debug( Utils.plural( "Receiving %s argument%s from the other instance.", argsCount ) );
					if ( argsCount > 0 ) {
						final String[] args = new String[ argsCount ];
						for ( int i = 0; i < argsCount; i++ )
							args[ i ] = in.readUTF();
						
						Env.MAIN_FRAME.openArguments( args );
					}
				}
				
			} catch ( final Exception e ) {
				Env.LOGGER.error( "Some error occured receiving arguments from another " + Consts.APP_NAME + " instance!", e );
			}
			
		}
	}
	
}
