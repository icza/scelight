/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.sllauncher.service.sound;

import hu.scelightapibase.util.gui.HasRIcon;
import hu.sllauncher.gui.icon.LIcons;
import hu.sllauncher.gui.icon.LRIcon;

/**
 * Sound theme.
 * 
 * @author Andras Belicza
 */
public enum SoundTheme implements HasRIcon {
	
	/** Smix, US dialect, female. */
	SMIX_US( "Smix (US / Female)", "smixus", LIcons.C_UNITED_STATES ),
	
	/** Google TTS, GB dialect, male. */
	GOOGLE_GB( "Google (GB / Female)", "googlegb", LIcons.C_ENGLAND );
	
	
	/** Sound theme name. */
	public final String name;
	
	/** Theme resource base. */
	public final String resBase;
	
	/** Ricon of the sound theme (country flag indicating the language). */
	public final LRIcon ricon;
	
	
	/**
	 * Creates a new {@link SoundTheme}.
	 * 
	 * @param name name of the sound theme
	 * @param resBase theme resource base
	 * @param ricon ricon of the sound theme
	 */
	private SoundTheme( final String name, final String resBase, final LRIcon ricon ) {
		this.name = name;
		this.resBase = resBase;
		this.ricon = ricon;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	@Override
	public LRIcon getRicon() {
		return ricon;
	}
	
	
	/** Default sound theme which is guaranteed to have all sound samples. */
	public static final SoundTheme DEFAULT = GOOGLE_GB;
	
}
