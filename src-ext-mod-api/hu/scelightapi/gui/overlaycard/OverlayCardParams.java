/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelightapi.gui.overlaycard;

import hu.scelightapi.service.IGuiFactory;
import hu.scelightapibase.bean.settings.ISettingsBean;
import hu.scelightapibase.bean.settings.type.IBoolSetting;
import hu.scelightapibase.bean.settings.type.IIntSetting;

/**
 * Wrapper class for the input parameters required by an {@link IOverlayCard}.
 * 
 * @author Andras Belicza
 * 
 * @see IOverlayCard
 * @see IGuiFactory#newOverlayCard(OverlayCardParams, IConfigPopupBuilder, Runnable)
 */
public class OverlayCardParams {
	
	/** Settings bean storing the overlay settings. */
	public ISettingsBean settings;
	
	/** Setting of the overlay center X position. */
	public IIntSetting   centerXSetting;
	
	/** Setting of the overlay center Y position. */
	public IIntSetting   centerYSetting;
	
	/** Setting of the overlay opacity. */
	public IIntSetting   opacitySetting;
	
	/** Setting of the overlay locked property. */
	public IBoolSetting  lockedSetting;
	
	/** Setting of the overlay focusable property. */
	public IBoolSetting  focusableSetting;
	
	/** Setting of the overlay allow positioning outside the main screen property. */
	public IBoolSetting  allowOutMainScrSetting;
	
}
