package hu.scelightapibase.util;

import hu.scelightapibase.util.gui.HasRIcon;
import hu.sllauncher.util.ControlledState;

/**
 * State of a {@link IControlledThread}.
 * 
 * @author Andras Belicza
 * 
 * @see IEnum
 */
public interface IControlledState extends HasRIcon, IEnum {
	
	/** Created but not yet started. */
	IControlledState NEW                        = ControlledState.NEW;
	
	/** Executing. */
	IControlledState EXECUTING                  = ControlledState.EXECUTING;
	
	/** Pause requested but still executing. */
	IControlledState EXECUTING_PAUSE_REQUESTED  = ControlledState.EXECUTING_PAUSE_REQUESTED;
	
	/** Cancel requested but still executing. */
	IControlledState EXECUTING_CANCEL_REQUESTED = ControlledState.EXECUTING_CANCEL_REQUESTED;
	
	/** Paused. */
	IControlledState PAUSED                     = ControlledState.PAUSED;
	
	/** Ended. */
	IControlledState ENDED                      = ControlledState.ENDED;
	
}
