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

import hu.scelight.sc2.balancedata.BdUtil;
import hu.scelightapi.sc2.balancedata.model.ITrainCommand;

/**
 * Describes a StarCraft II unit train command.
 * 
 * <p>
 * The id of the train command will be the id of the trained unit.
 * </p>
 * 
 * @author Andras Belicza
 */
public class TrainCommand extends Command implements ITrainCommand {
	
	/**
	 * Creates a new {@link TrainCommand}.
	 * 
	 * @param abilId ability id this train command belongs to
	 */
	public TrainCommand( final String abilId ) {
		super( abilId );
	}
	
	@Override
    public boolean isWorker() {
		return BdUtil.isWorkerImpl( id );
	}
	
}
