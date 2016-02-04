/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight.sc2.rep.model.gameevents.cmd;

import hu.belicza.andras.util.ArrayMap;
import hu.scelight.gui.icon.Icons;
import hu.scelight.sc2.balancedata.BalanceData;
import hu.scelight.sc2.balancedata.model.Ability;
import hu.scelight.sc2.balancedata.model.Command;
import hu.scelight.sc2.rep.s2prot.Event;
import hu.scelight.sc2.textures.Textures;
import hu.scelight.service.env.Env;
import hu.scelightapi.sc2.balancedata.IBalanceData;
import hu.scelightapi.sc2.balancedata.model.IUnit;
import hu.scelightapi.sc2.rep.model.gameevents.cmd.ICmdEvent;
import hu.scelightapi.sc2.rep.repproc.IRepProcessor;
import hu.scelightapi.sc2.rep.repproc.IUser;
import hu.scelightapibase.gui.icon.IRIcon;
import hu.sllauncher.util.Pair;

import java.util.Map;

/**
 * Cmd game event.
 * 
 * @author Andras Belicza
 */
public class CmdEvent extends Event implements ICmdEvent {
	
	/** Cached command. */
	public final Command	  command;
							  
	/** Cached flags. */
	private final int		  flags;
							  
	/** Target unit of the cmd event. */
	private final TargetUnit  targetUnit;
							  
	/** Target point of the cmd event. */
	private final TargetPoint targetPoint;
							  
							  
	/**
	 * Creates a new {@link CmdEvent}.
	 * 
	 * @param struct event data structure
	 * @param id id of the event
	 * @param name type name of the event
	 * @param loop game loop when the event occurred
	 * @param userId user id causing the event
	 * @param baseBuild base build of the replay being parsed, there are structural differences in event structures in different versions
	 * @param balanceData balance data of the game / replay
	 */
	public CmdEvent( final Map< String, Object > struct, final int id, final String name, final int loop, final int userId, final int baseBuild,
	        final BalanceData balanceData ) {
		super( struct, id, name, loop, userId );
		
		// In the first retail version (1.0) fields of the abil and data structs are enumerated
		// directly in the cmd struct (except the target point). Simulate those structs.
		if ( baseBuild < 16561 ) {
			final Integer abilLink = (Integer) struct.remove( P_ABIL_LINK[ 1 ] );
			if ( abilLink != 65535 ) { // in 1.0 65535 means right click (which is also indicated in the cmdFlags)
				final Map< String, Object > abilStruct = new ArrayMap< >( 2 );
				abilStruct.put( P_ABIL_LINK[ 1 ], abilLink );
				abilStruct.put( P_ABIL_CMD_INDEX[ 1 ], struct.remove( P_ABIL_CMD_INDEX[ 1 ] ) );
				struct.put( P_ABIL_LINK[ 0 ], abilStruct );
			}
			
			@SuppressWarnings( "unchecked" )
			final Map< String, Object > targetPoint_ = (Map< String, Object >) struct.remove( "targetPoint" );
			
			final Integer targetUnitTag = (Integer) struct.remove( "targetUnitTag" );
			final Integer targetUnitSnapshotLink = (Integer) struct.remove( "targetUnitSnapshotUnitLink" );
			if ( targetUnitSnapshotLink != null && targetUnitSnapshotLink != 0 ) {
				// Simulate a "TargetUnit"
				final Pair< String, Map< String, Object > > data = new Pair< String, Map< String, Object > >( "TargetUnit",
				        new ArrayMap< String, Object >( 5 ) );
				data.value2.put( TargetUnit.F_TARGET_UNIT_FLAGS, struct.remove( "targetUnitFlags" ) );
				data.value2.put( TargetUnit.F_TIMER, struct.remove( "targetUnitTimer" ) );
				data.value2.put( TargetUnit.F_TAG, targetUnitTag );
				data.value2.put( TargetUnit.F_SNAPSHOT_UNIT_LINK, targetUnitSnapshotLink );
				data.value2.put( TargetUnit.F_SNAPSHOT_PLAYER_ID, struct.remove( "targetUnitSnapshotPlayerId" ) );
				// Does the simulated "TargetUnit" have a snapshotPoint?
				if ( (Integer) targetPoint_.get( "x" ) != 0 )
					data.value2.put( TargetUnit.F_SNAPSHOT_POINT, targetPoint_ );
				struct.put( F_DATA, data );
			} else if ( (Integer) targetPoint_.get( "x" ) != 0 ) {
				// Simulate a "TargetPoint"
				final Pair< String, Map< String, Object > > data = new Pair< >( "TargetPoint", targetPoint_ );
				struct.put( F_DATA, data );
			} else if ( targetUnitTag != 0 ) {
				// Simulate a "Data"
				final Pair< String, Integer > data = new Pair< >( "Data", targetUnitTag );
				struct.put( F_DATA, data );
			}
		}
		
		// Cache command
		Command command_ = null;
		final Integer abilLink = getAbilLink();
		final Integer abilCmdIndex = getAbilCmdIndex();
		if ( abilLink != null && abilCmdIndex != null ) {
			// There is surely a command here, the question is: is it known or unknown?
			if ( balanceData != null ) {
				final Ability ability = balanceData.getAbility( abilLink );
				if ( ability != null )
					command_ = ability.getCommand( abilCmdIndex );
			}
			if ( command_ == null ) {
				command_ = new Command( null );
				command_.text = "Unknown Command " + abilLink + "_" + abilCmdIndex;
				command_.icon = Textures.MISSING_ICON_NAME; // Note: Setting this icon will classify the command as non-essential
			}
		}
		command = command_;
		
		flags = get( F_CMD_FLAGS );
		
		// Cache target
		final Pair< String, Object > data = getData();
		if ( data == null ) {
			targetUnit = null;
			targetPoint = null;
		} else
			switch ( data.value1 ) {
				case "TargetUnit" :
					@SuppressWarnings( "unchecked" )
					final Map< String, Object > tuStruct = (Map< String, Object >) data.value2;
					targetUnit = new TargetUnit( tuStruct );
					targetPoint = new TargetPoint( targetUnit.getSnapshotPoint() );
					break;
				case "TargetPoint" :
					targetUnit = null;
					@SuppressWarnings( "unchecked" )
					final Map< String, Object > tpStruct = (Map< String, Object >) data.value2;
					targetPoint = new TargetPoint( tpStruct );
					break;
				case "Data" :
					// TODO
					// In case of [Wireframe click][Wireframe unload] this is the unit tag
					// In case of [Wireframe click][Wireframe cancel] this is maybe the ability index (that is cancelled)
					// data.value2 is of type Integer
				default :
					targetUnit = null;
					targetPoint = null;
					break;
			}
	}
	
	@Override
	public Command getCommand() {
		return command;
	}
	
	@Override
	public Integer getCmdFlags() {
		return flags;
	}
	
	@Override
	public Integer getAbilLink() {
		return get( P_ABIL_LINK );
	}
	
	@Override
	public Integer getAbilCmdIndex() {
		return get( P_ABIL_CMD_INDEX );
	}
	
	@Override
	public Pair< String, Object > getData() {
		return get( F_DATA );
	}
	
	@Override
	public TargetUnit getTargetUnit() {
		return targetUnit;
	}
	
	@Override
	public TargetPoint getTargetPoint() {
		return targetPoint;
	}
	
	@Override
	public String getParameters( final IRepProcessor repProc ) {
		final StringBuilder sb = new StringBuilder();
		
		for ( final CmdFlag flag : CmdFlag.VALUES ) {
			if ( ( flags & flag.mask ) != 0 )
				sb.append( flag.text );
		}
		
		if ( command != null )
			sb.append( command.text );
			
		if ( targetUnit != null ) {
			final IBalanceData balanceData = repProc.getReplay().getBalanceData();
			final IUnit unit = balanceData == null ? null : balanceData.getUnit( targetUnit.getSnapshotUnitLink() );
			sb.append( "; target unit: " ).append( unit == null ? "Unknown " + targetUnit.getSnapshotUnitLink() : unit.getText() );
			sb.append( " (tag=" ).append( repProc.getTagTransformation().tagToString( targetUnit.getTag() ) );
			final Integer playerId = targetUnit.getSnapshotControlPlayerId();
			if ( playerId != null && playerId != 0 ) {// Neutral units have playerId=0 (e.g. mineral field)
				// In multiplayer custom games (e.g.) may contain hostile units with playerId=15 but no associated player/user, so check for null:
				final IUser user = repProc.getUsersByPlayerId()[ playerId ];
				if ( user != null ) {
					sb.append( "; owner=" ).append( user.getFullName() );
				}
			}
			sb.append( ')' );
		}
		
		if ( targetPoint != null ) {
			sb.append( "; target point: x=" ).append( Env.LANG.formatNumber( targetPoint.getXFloat(), 3 ) ).append( ", y=" )
			        .append( Env.LANG.formatNumber( targetPoint.getYFloat(), 3 ) ).append( ", z=" )
			        .append( Env.LANG.formatNumber( targetPoint.getZFloat(), 3 ) );
		}
		
		return sb.toString();
	}
	
	@Override
	public IRIcon getRicon() {
		return ( flags & CmdFlag.SMART_CLICK.mask ) != 0 ? Icons.F_MOUSE_SELECT_RIGHT : command == null ? super.getRicon() : command.getRicon();
	}
	
}
