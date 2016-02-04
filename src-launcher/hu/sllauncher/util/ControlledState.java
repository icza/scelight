package hu.sllauncher.util;

import hu.scelightapibase.util.IControlledState;
import hu.sllauncher.gui.icon.LIcons;
import hu.sllauncher.gui.icon.LRIcon;

/**
 * State of a {@link ControlledThread}.
 * 
 * @author Andras Belicza
 */
public enum ControlledState implements IControlledState {
	
	/** Created but not yet started. */
	NEW( "New", null ),
	
	/** Executing. */
	EXECUTING( "Executing", LIcons.F_CONTROL ),
	
	/** Pause requested but still executing. */
	EXECUTING_PAUSE_REQUESTED( "Executing (Pause requested)", LIcons.F_CONTROL ),
	
	/** Cancel requested but still executing. */
	EXECUTING_CANCEL_REQUESTED( "Executing (Cancel requested)", LIcons.F_CONTROL ),
	
	/** Paused. */
	PAUSED( "Paused", LIcons.F_CONTROL_PAUSE ),
	
	/** Ended. */
	ENDED( "Ended", null );
	
	
	/** String value of the state. */
	public final String stringValue;
	
	/** RIcon of the state. */
	public final LRIcon ricon;
	
	
	/**
	 * Creates a new {@link ControlledState}.
	 * 
	 * @param stringValue string value of the state
	 * @param ricon ricon of the state
	 */
	private ControlledState( final String stringValue, final LRIcon ricon ) {
		this.stringValue = stringValue;
		this.ricon = ricon;
	}
	
	@Override
	public LRIcon getRicon() {
		return ricon;
	}
	
	@Override
	public String toString() {
		return stringValue;
	}
	
}
