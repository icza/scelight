/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight.sc2.balancedata.model;

import hu.scelight.sc2.textures.TRIcon;
import hu.scelightapi.sc2.balancedata.model.ICommand;

import java.util.HashSet;
import java.util.Set;

/**
 * Describes an ability command.
 * 
 * @author Andras Belicza
 */
public class Command extends Entity implements ICommand {
	
	/**
	 * Set of "light" icon names. <br>
	 * Initialized from {@link TRIcon#LIGHT_TEXTURE_SET}.
	 */
	public static final Set< String > LIGHT_ICON_SET = new HashSet<>();
	static {
		for ( final String lightTextureName : TRIcon.LIGHT_TEXTURE_SET ) {
			LIGHT_ICON_SET.add( lightTextureName.substring( 0, lightTextureName.lastIndexOf( '.' ) ) );
		}
	}
	
	
	/** Ability id this command belongs to. This is stored redundant here for fast access. */
	public String                     abilId;
	
	/**
	 * Creates a new {@link Command}.
	 * 
	 * @param abilId ability id this command belongs to
	 */
	public Command( final String abilId ) {
		this.abilId = abilId;
	}
	
	
	@Override
	public String getAbilId() {
		return abilId;
	}
	
	/**
	 * Tells if this command is <i>essential</i>.
	 * 
	 * <p>
	 * Fortunately essential icons do not have light texture, so that's how we define / check it.
	 * </p>
	 * 
	 * @return true if this command is <i>essential</i>; false otherwise
	 */
	@Override
	public boolean isEssential() {
		return !LIGHT_ICON_SET.contains( icon );
	}
	
}
