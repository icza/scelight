/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.sllauncher.gui.comp;

import hu.scelightapibase.gui.comp.IStatusLabel;
import hu.sllauncher.gui.icon.LIcons;
import hu.sllauncher.gui.icon.LRIcon;

import java.awt.Color;
import java.awt.Font;

/**
 * An {@link XLabel} with an indicator icon ({@link XLabel}) to display the status of a background process.
 * 
 * @author Andras Belicza
 */
@SuppressWarnings( "serial" )
public class StatusLabel extends XLabel implements IStatusLabel {
	
	/**
	 * Status type.
	 * 
	 * @author Andras Belicza
	 */
	public enum StatusType implements IStatusType {
		
		/** Not available. */
		NA( LIcons.F_NA, null ),
		
		/** Progress. */
		PROGRESS( LIcons.MISC_LOADING, null ),
		
		/** Warning. */
		WARNING( LIcons.F_EXCLAMATION, null ),
		
		/** Warning (error-like). */
		HARD_WARNING( LIcons.F_EXCLAMATION_RED, Color.RED ),
		
		/** Error. */
		ERROR( LIcons.F_CROSS, Color.RED ),
		
		/** Success. */
		SUCCESS( LIcons.F_TICK, new Color( 0, 110, 0 ) );
		
		
		/** Ricon of the status type. */
		public final LRIcon ricon;
		
		/** Foreground color of the status type. */
		public final Color  color;
		
		/**
		 * Creates a new {@link StatusType}.
		 * 
		 * @param ricon ricon of the status type
		 * @param color foreground color of the status type
		 */
		private StatusType( final LRIcon ricon, final Color color ) {
			this.ricon = ricon;
			this.color = color;
		}
		
		@Override
		public LRIcon getRicon() {
			return ricon;
		}
		
		@Override
		public Color getColor() {
			return color;
		}
		
	}
	
	
	/** Default font. */
	private final Font defaultFont = getFont();
	
	/**
	 * Creates a new {@link StatusLabel}.
	 */
	public StatusLabel() {
	}
	
	/**
	 * Creates a new {@link StatusLabel}.
	 * 
	 * @param type initial status type to be set
	 * @param message initial status message to be set
	 */
	public StatusLabel( final IStatusType type, final String message ) {
		setByType( type, message );
	}
	
	@Override
	public void setByType( final IStatusType type, final String message ) {
		setText( type == StatusType.PROGRESS ? message + "..." : message );
		setIcon( type.getRicon().get() );
		setForeground( type.getColor() );
		
		switch ( StatusType.valueOf( type.name() ) ) {
			case PROGRESS :
				setFont( defaultFont.deriveFont( Font.ITALIC ) );
				break;
			case WARNING :
			case HARD_WARNING :
			case ERROR :
				setFont( defaultFont.deriveFont( Font.BOLD ) );
				break;
			default :
				setFont( null );
				break;
		}
	}
	
	@Override
	public void setNotAvailable( final String message ) {
		setByType( StatusType.NA, message );
	}
	
	@Override
	public void setProgress( final String message ) {
		setByType( StatusType.PROGRESS, message );
	}
	
	@Override
	public void setWarning( final String message ) {
		setByType( StatusType.WARNING, message );
	}
	
	@Override
	public void setHardWarning( final String message ) {
		setByType( StatusType.HARD_WARNING, message );
	}
	
	@Override
	public void setError( final String message ) {
		setByType( StatusType.ERROR, message );
	}
	
	@Override
	public void setSuccess( final String message ) {
		setByType( StatusType.SUCCESS, message );
	}
	
	@Override
	public void clear() {
		setText( null );
		setIcon( null );
		setFont( null );
		setForeground( null );
	}
	
}
