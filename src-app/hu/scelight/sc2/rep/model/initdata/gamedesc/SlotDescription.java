/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight.sc2.rep.model.initdata.gamedesc;

import hu.belicza.andras.util.StructView;
import hu.scelightapi.sc2.rep.model.initdata.gamedesc.ISlotDescription;

import java.util.Map;

/**
 * Slot description.
 * 
 * @author Andras Belicza
 */
public class SlotDescription extends StructView implements ISlotDescription {
	
	// Eagerly initialized "cached" values
	
	
	// Lazily initialized "cached" values
	
	
	/**
	 * Creates a new {@link SlotDescription}.
	 * 
	 * @param struct slot description data structure
	 */
	public SlotDescription( final Map< String, Object > struct ) {
		super( struct );
	}
	
	@Override
	public int[] getAllowedAiBuilds() {
		return get( F_ALLOWED_AI_BUILDS );
	}
	
	@Override
	public int[] getAllowedColors() {
		return get( F_ALLOWED_COLORS );
	}
	
	@Override
	public int[] getAllowedControls() {
		return get( F_ALLOWED_CONTROLS );
	}
	
	@Override
	public int[] getAllowedDifficulty() {
		return get( F_ALLOWED_DIFFICULTY );
	}
	
	@Override
	public int[] getAllowedObserveTypes() {
		return get( F_ALLOWED_OBSERVE_TYPES );
	}
	
	@Override
	public int[] getAllowedRaces() {
		return get( F_ALLOWED_RACES );
	}
	
}
