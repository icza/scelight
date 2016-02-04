/*
 * Project Abdra Commander
 * 
 * Copyright (c) 2012 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight.search;

import hu.scelight.bean.repfilters.RepFilterBean;
import hu.scelight.sc2.rep.model.attributesevents.GameMode;
import hu.scelight.sc2.rep.model.details.Race;
import hu.scelight.sc2.rep.model.details.Result;
import hu.scelight.sc2.rep.model.initdata.gamedesc.ExpansionLevel;
import hu.scelight.sc2.rep.model.initdata.gamedesc.GameSpeed;
import hu.scelight.sc2.rep.model.initdata.gamedesc.Region;
import hu.scelight.sc2.rep.model.initdata.lobbystate.Controller;
import hu.scelight.sc2.rep.model.initdata.lobbystate.PlayerColor;
import hu.scelight.sc2.rep.model.initdata.userinitdata.League;
import hu.scelight.sc2.rep.repproc.Format;
import hu.scelight.sc2.rep.s2prot.Protocol;
import hu.scelight.service.env.Env;
import hu.scelight.util.Utils;
import hu.scelight.util.sc2rep.RepUtils;
import hu.scelightapi.bean.repfilters.IRepFilterBean;
import hu.scelightapi.bean.repfilters.IRepFiltersBean;
import hu.scelightapi.sc2.rep.model.IEvent;
import hu.scelightapi.sc2.rep.model.details.IToon;
import hu.scelightapi.sc2.rep.model.initdata.gamedesc.ICacheHandle;
import hu.scelightapi.sc2.rep.model.messageevents.IChatEvent;
import hu.scelightapi.sc2.rep.model.messageevents.IMessageEvents;
import hu.scelightapi.sc2.rep.repproc.IRepProcessor;
import hu.scelightapi.sc2.rep.repproc.IUser;
import hu.scelightapi.search.IRepSearchEngine;
import hu.scelightapi.util.IVersionView;
import hu.sllauncher.bean.VersionBean;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Replay search engine.
 * 
 * <p>
 * Implementation is thread safe, the {@link #isIncluded(IRepProcessor)} can be called parallel from multiple threads.
 * </p>
 * 
 * @author Andras Belicza
 */
public class RepSearchEngine implements IRepSearchEngine {
	
	/** Version of the replay search engine. */
	public static final VersionBean      VERSION          = new VersionBean( 1, 1 );
	
	
	/** The parsed filter tree. */
	private final Node                   tree;
	
	/** Max tree level. */
	private final int                    maxLevel;
	
	/** Final filters count (the number of active filters). */
	private final int                    filtersCount;
	
	
	/** Cache of compiled regular expressions. */
	private final Map< String, Pattern > regexpPatternMap = new HashMap<>();
	
	/** Cache of normalized matchup strings. */
	private final Map< String, String >  matchupNormuMap  = new HashMap<>();
	
	
	/**
	 * Creates a new {@link RepSearchEngine}.
	 * 
	 * @param repFiltersBean replay filters bean to search / filter by
	 */
	public RepSearchEngine( final IRepFiltersBean repFiltersBean ) {
		// First keep only the active filter beans:
		final List< IRepFilterBean > filterBeanList = new ArrayList<>( repFiltersBean.getRepFilterBeanList().size() );
		for ( final IRepFilterBean filterBean : repFiltersBean.getRepFilterBeanList() ) {
			// Check whether to use / include the filter
			if ( filterBean.isActive() )
				filterBeanList.add( filterBean );
		}
		
		filtersCount = filterBeanList.size();
		
		// Special case: no filters
		if ( filterBeanList.isEmpty() ) {
			tree = null;
			maxLevel = 0;
			return;
		}
		
		// Build the filter tree
		// Idea: http://stackoverflow.com/questions/4589951/parsing-an-arithmetic-expression-and-building-a-tree-from-it-in-java
		final List< Object > stack = new ArrayList<>();
		
		// Init stack: add first filter (list is not empty); ignore its connection.
		stack.add( filterBeanList.get( 0 ) );
		
		// Count parenthesis:
		final int n = filterBeanList.size();
		int parenthesis = 0;
		for ( int i = 1; i < n; i++ ) {
			final Connection conn = (Connection) filterBeanList.get( i ).getConnection();
			if ( conn.opening )
				parenthesis++;
			if ( conn.closing )
				parenthesis--;
		}
		
		for ( int i = 1; i < n; i++ ) {
			final IRepFilterBean filterBean = filterBeanList.get( i );
			
			Connection conn = (Connection) filterBean.getConnection();
			if ( conn.closing ) {
				// Evaluate until last open parenthesis (or beginning if missing opening parenthesis).
				evalStack( stack, true );
				conn = conn.removeClosing();
			}
			
			final Connection lastConn = lastConnection( stack );
			
			if ( lastConn != null && !lastConn.opening && conn.precedence < lastConn.precedence )
				evalStack( stack, false );
			
			stack.add( conn );
			stack.add( filterBean );
		}
		
		// Evaluate whole stack
		// First consume unclosed parenthesis
		for ( ; parenthesis > 0; parenthesis-- )
			evalStack( stack, true );
		// Now reduce all to one node:
		while ( stack.size() > 1 )
			evalStack( stack, false );
		
		if ( stack.get( 0 ) instanceof Node )
			tree = (Node) stack.get( 0 );
		else {
			tree = new Node( Connection.AND );
			tree.childList.add( stack.get( 0 ) );
		}
		
		maxLevel = tree.getDeepestLevel();
		setLevels( tree, 0 );
	}
	
	/**
	 * Sets the level of the specified node and its descendants' recursively.
	 * 
	 * @param node node whose level to be set
	 * @param level level of the specified node
	 */
	private void setLevels( final Node node, int level ) {
		node.level = level;
		
		level++;
		for ( final Object child : node.childList )
			if ( child instanceof Node )
				setLevels( (Node) child, level );
	}
	
	/**
	 * Returns the last connection from the stack, or <code>null</code> if there is no last connection.
	 * 
	 * @param stack stack to get last connection from
	 * @return the last connection from the stack, or <code>null</code> if there is no last connection
	 */
	private static Connection lastConnection( final List< Object > stack ) {
		return stack.size() < 2 ? null : (Connection) stack.get( stack.size() - 2 );
	}
	
	/**
	 * Evaluates the specified stack (until a lower precedence or opening parenthesis is found).
	 * 
	 * @param stack stack to be evaluated
	 * @param consumeOpening tells if we have to consume the last opening parenthesis
	 */
	private static void evalStack( final List< Object > stack, boolean consumeOpening ) {
		do {
			
			final Connection conn = lastConnection( stack );
			if ( conn == null ) {
				// Only one element. We're done evaluating. There's not even an opening parenthesis to consume
				return;
			}
			
			if ( conn.opening ) {
				// Last connection contains opening parenthesis, only one element in parenthesis.
				// We can just simply remove the parenthesis and we're done evaluating.
				
				// We can only end up here if all the filters were processed and we're evaluating the whole stack
				// or a closing parenthesis is encountered (in which case consumeOpening will be true).
				// So removing this opening parenthesis will not cause troubles when consuming remaining closing parenthesis at the end.
				stack.set( stack.size() - 2, conn.removeOpening() );
				return;
			}
			
			final Node node = new Node( conn.precedence == Connection.AND.precedence ? Connection.AND : Connection.OR );
			while ( true ) {
				final Connection prevConn = lastConnection( stack );
				
				// If we're in the cycle, always have to add the last element:
				node.childList.add( stack.remove( stack.size() - 1 ) );
				
				if ( prevConn == null ) {
					// It was the last element. We're done evaluating.
					if ( consumeOpening ) // Missing opening parenthesis are imaginarily added before the first filter
						consumeOpening = false;
					break;
				}
				
				if ( prevConn.opening ) {
					// Last connection contains opening parenthesis, this was the last element in parenthesis.
					// Simply remove the parenthesis and we're done evaluating.
					if ( consumeOpening ) {
						stack.set( stack.size() - 1, prevConn.removeOpening() );
						consumeOpening = false;
					}
					break;
				}
				
				if ( prevConn.precedence < conn.precedence ) {
					// Lower precedence (and no parenthesis), we have to stop evaluating here.
					break;
				}
				
				// A "normal" case: remove the connection and keep going...
				stack.remove( stack.size() - 1 ); // Connection
			}
			stack.add( node );
			
		} while ( consumeOpening );
	}
	
	@Override
	public int getFiltersCount() {
		return filtersCount;
	}
	
	@Override
	public boolean isIncluded( final IRepProcessor repProc ) {
		if ( tree == null )
			return true;
		
		final Context context = new Context( repProc );
		
		// First gather all referenced player-specific filter by groups.
		final Set< FilterByGroup > filterByGroupSet = EnumSet.noneOf( FilterByGroup.class );
		gatherPlayerSpecificFilterByGroups( tree, filterByGroupSet );
		
		if ( filterByGroupSet.isEmpty() ) // No player-specific filter by groups, simply just evaluate the tree
			return isIncluded( tree, context );
		
		final int playersCount = repProc.getPlayerUsers().length;
		if ( filterByGroupSet.size() > playersCount )
			return false; // There are less players than referenced, we can't even find a match for just these filters...
			
		// Now generate all possible permutations of the referenced player-specific filter by groups paired with players from the replay
		// and check if filters apply to any of them.
		// Use short hand evaluation of course: return true when the first matching permutation is encountered.
		final FilterByGroup[] filterByGroups = filterByGroupSet.toArray( new FilterByGroup[ filterByGroupSet.size() ] );
		
		// We need repetition permutations!
		// E.g. (PlayerA=1, PlayerB=2) BUT we also need (PlayerA=2, PlayerB=1)!
		// E.g PlayerA.name="dakota", PlayerB.name="craft", and if players are ["craft","dakota"]...
		final IUser[] playerUsers = repProc.getPlayerUsers();
		switch ( filterByGroups.length ) {
			case 1 :
				for ( int i = 0; i < playersCount; i++ ) {
					context.filterByGroupUserMap.put( filterByGroups[ 0 ], playerUsers[ i ] );
					if ( isIncluded( tree, context ) )
						return true;
				}
				break;
			case 2 :
				for ( int i = 0; i < playersCount; i++ ) {
					context.filterByGroupUserMap.put( filterByGroups[ 0 ], playerUsers[ i ] );
					for ( int j = 0; j < playersCount; j++ ) {
						if ( j == i )
							continue;
						context.filterByGroupUserMap.put( filterByGroups[ 1 ], playerUsers[ j ] );
						if ( isIncluded( tree, context ) )
							return true;
					}
				}
				break;
			case 3 :
				for ( int i = 0; i < playersCount; i++ ) {
					context.filterByGroupUserMap.put( filterByGroups[ 0 ], playerUsers[ i ] );
					for ( int j = 0; j < playersCount; j++ ) {
						if ( j == i )
							continue;
						context.filterByGroupUserMap.put( filterByGroups[ 1 ], playerUsers[ j ] );
						for ( int k = 0; k < playersCount; k++ ) {
							if ( k == i || k == j )
								continue;
							context.filterByGroupUserMap.put( filterByGroups[ 2 ], playerUsers[ k ] );
							if ( isIncluded( tree, context ) )
								return true;
						}
					}
				}
				break;
			case 4 :
				for ( int i = 0; i < playersCount; i++ ) {
					context.filterByGroupUserMap.put( filterByGroups[ 0 ], playerUsers[ i ] );
					for ( int j = 0; j < playersCount; j++ ) {
						if ( j == i )
							continue;
						context.filterByGroupUserMap.put( filterByGroups[ 1 ], playerUsers[ j ] );
						for ( int k = 0; k < playersCount; k++ ) {
							if ( k == i || k == j )
								continue;
							context.filterByGroupUserMap.put( filterByGroups[ 2 ], playerUsers[ k ] );
							for ( int l = 0; l < playersCount; l++ ) {
								if ( l == i || l == j || l == k )
									continue;
								context.filterByGroupUserMap.put( filterByGroups[ 3 ], playerUsers[ l ] );
								if ( isIncluded( tree, context ) )
									return true;
							}
						}
					}
				}
				break;
			default :
				throw new RuntimeException( "Unhandled permutation generation for player-specific filter by groups count:" + filterByGroups.length );
		}
		
		// No matching permutation: return false.
		return false;
	}
	
	/**
	 * Gathers all referenced player-specific filter by groups (elements of {@link FilterByGroup#PLAYER_SPECIFIC_VALUE_SET}).
	 * 
	 * @param node node to search in
	 * @param set set to put referenced player-specific filter by groups
	 */
	private static void gatherPlayerSpecificFilterByGroups( final Node node, final Set< FilterByGroup > set ) {
		for ( final Object child : node.childList ) {
			if ( child instanceof IRepFilterBean ) {
				final FilterByGroup fbg = (FilterByGroup) ( (IRepFilterBean) child ).getFilterByGroup();
				if ( FilterByGroup.PLAYER_SPECIFIC_VALUE_SET.contains( fbg ) )
					set.add( fbg );
			} else if ( child instanceof Node )
				gatherPlayerSpecificFilterByGroups( (Node) child, set );
		}
	}
	
	/**
	 * Evaluates the specified node and tells if filters defined by it include the specified replay processor.
	 * 
	 * @param node node to be evaluated
	 * @param context search context
	 * @return true if the specified node includes the specified replay processor; false otherwise
	 */
	private boolean isIncluded( final Node node, final Context context ) {
		if ( node.connection == Connection.AND ) {
			
			// Short-hand evaluation of AND
			for ( final Object child : node.childList ) {
				if ( child instanceof IRepFilterBean && !isIncluded_L1( (IRepFilterBean) child, context ) )
					return false;
				else if ( child instanceof Node && !isIncluded( (Node) child, context ) )
					return false;
			}
			
			return true;
			
		} else {
			
			// Short-hand evaluation of OR
			for ( final Object child : node.childList ) {
				if ( child instanceof IRepFilterBean && isIncluded_L1( (IRepFilterBean) child, context ) )
					return true;
				else if ( child instanceof Node && isIncluded( (Node) child, context ) )
					return true;
			}
			
			return false;
			
		}
	}
	
	/**
	 * Applies the specified filter bean and tells if it includes the specified replay processor.
	 * 
	 * <p>
	 * THIS IS LAYER #1. Player-specific filter by groups are processed here, in other cases it just advances to LAYER 2.
	 * </p>
	 * 
	 * @param filterBean filter bean to be applied
	 * @param context search context
	 * @return true if the specified filter bean includes the specified replay processor; false otherwise
	 */
	private boolean isIncluded_L1( final IRepFilterBean filterBean, final Context context ) {
		switch ( (FilterByGroup) filterBean.getFilterByGroup() ) {
			case ALL_PLAYERS :
				// Filter must apply to all players. Short hand evaluation.
				for ( final IUser u : context.repProc.getPlayerUsers() ) {
					context.user = u;
					if ( !isIncluded_L2( filterBean, context ) )
						return false;
				}
				return true;
			case ANY_PLAYER :
				// Filter might apply to any player. Short hand evaluation.
				for ( final IUser u : context.repProc.getPlayerUsers() ) {
					context.user = u;
					if ( isIncluded_L2( filterBean, context ) )
						return true;
				}
				return false;
			case PLAYER_A :
			case PLAYER_B :
			case PLAYER_C :
			case PLAYER_D :
				// Set group-specific user.
				context.user = context.filterByGroupUserMap.get( filterBean.getFilterByGroup() );
			default : // Filter by group is not player specific.
				return isIncluded_L2( filterBean, context );
		}
	}
	
	/**
	 * Applies the specified filter bean and tells if it includes the specified replay processor.
	 * 
	 * <p>
	 * THIS IS LAYER #2. Context holds the pre-selected user for player-specific filters.
	 * </p>
	 * 
	 * @param filterBean filter bean to be applied
	 * @param context search context
	 * @return true if the specified filter bean includes the specified replay processor; false otherwise
	 */
	private boolean isIncluded_L2( final IRepFilterBean filterBean, final Context context ) {
		switch ( (FilterBy) filterBean.getFilterBy() ) {
			case ALL_CHAT_MESSAGES :
				if ( context.replay.getMessageEvents() != null )
					for ( final IEvent e : context.replay.getMessageEvents().getEvents() )
						if ( e.getId() == IMessageEvents.ID_CHAT )
							if ( !evalTextOp( (String) filterBean.getValue(), (Operator) filterBean.getOperator(), ( (IChatEvent) e ).getText() ) )
								return false;
				return true;
			case ANY_CHAT_MESSAGE :
				if ( context.replay.getMessageEvents() != null )
					for ( final IEvent e : context.replay.getMessageEvents().getEvents() )
						if ( e.getId() == IMessageEvents.ID_CHAT )
							if ( evalTextOp( (String) filterBean.getValue(), (Operator) filterBean.getOperator(), ( (IChatEvent) e ).getText() ) )
								return true;
				return false;
			case APM :
				return evalIntegerOp( filterBean, context.user.getApm() );
			case AVG_APM :
				return evalIntegerOp( filterBean, context.repProc.getAvgAPM() );
			case AVG_LEAGUE :
				return evalEnumOp( filterBean, (League) context.repProc.getAvgLeague() );
			case AVG_LEVELS :
				return evalIntegerOp( filterBean, context.repProc.getAvgLevels() );
			case AVG_SPM :
				return evalIntegerOp( filterBean, context.repProc.getAvgSPM() );
			case AVG_SQ :
				return evalIntegerOp( filterBean, context.repProc.getAvgSQ() );
			case AVG_SUPPLY_CAPPED :
				return evalIntegerOp( filterBean, context.repProc.getAvgSupplyCappedPercent() );
			case BASE_BUILD :
				return evalIntegerOp( filterBean, context.replay.getHeader().getBaseBuild() );
			case BUILD_NUMBER :
				return evalIntegerOp( filterBean, context.replay.getHeader().getBuild() );
			case CHAT_MESSAGES_COUNT :
				return evalIntegerOp( filterBean, context.repProc.getChatMessagesCount() );
			case CLAN :
				return evalTextOp( filterBean, context.user.getUid() == null ? null : context.user.getUid().getClanTag() );
			case COLOR :
				return evalEnumOp( filterBean, (PlayerColor) context.user.getSlot().getPlayerColor() );
			case COMPETITIVE :
				return evalBoolOp( filterBean, context.gd.getCompetitiveOrRanked() );
			case CONTROL :
				return evalEnumOp( filterBean, (Controller) context.user.getSlot().getController() );
			case EXPANSION :
				return evalEnumOp( filterBean, (ExpansionLevel) context.gd.getExpansionLevel() );
			case FILE_ACCESSED :
				try {
					final BasicFileAttributes attr = Files.readAttributes( context.repProc.getFile(), BasicFileAttributes.class );
					return evalDateOp( filterBean, attr.lastAccessTime().toMillis() );
				} catch ( final IOException ie ) {
					Env.LOGGER.error( "Failed to get file attributes: " + context.repProc.getFile(), ie );
					return false;
				}
			case FILE_CREATED :
				try {
					final BasicFileAttributes attr = Files.readAttributes( context.repProc.getFile(), BasicFileAttributes.class );
					return evalDateOp( filterBean, attr.creationTime().toMillis() );
				} catch ( final IOException ie ) {
					Env.LOGGER.error( "Failed to get file attributes: " + context.repProc.getFile(), ie );
					return false;
				}
			case FILE_MODIFIED :
				try {
					return evalDateOp( filterBean, Files.getLastModifiedTime( context.repProc.getFile() ).toMillis() );
				} catch ( final IOException ie ) {
					Env.LOGGER.error( "Failed to get file last modified time: " + context.repProc.getFile(), ie );
					return false;
				}
			case FILE_NAME :
				return evalTextOp( filterBean, context.repProc.getFile().getFileName().toString() );
			case FILE_PATH :
				return evalTextOp( filterBean, context.repProc.getFile().toString() );
			case FILE_SIZE :
				try {
					return evalIntegerOp( filterBean, (int) Files.size( context.repProc.getFile() ) );
				} catch ( final IOException ie ) {
					Env.LOGGER.error( "Failed to get file size: " + context.repProc.getFile(), ie );
					return false;
				}
			case FORMAT :
				return evalEnumOp( filterBean, (Format) context.repProc.getFormat() );
			case FULL_NAME :
				return evalTextOp( filterBean, context.user.getFullName() );
			case GAME_MODE :
				return evalEnumOp( filterBean, (GameMode) context.replay.getAttributesEvents().getGameMode() );
			case GAME_SPEED :
				return evalEnumOp( filterBean, (GameSpeed) context.gd.getGameSpeed() );
			case LEAGUE :
				return evalEnumOp( filterBean, (League) ( context.user.getUid() == null ? null : context.user.getUid().getHighestLeague() ) );
			case LEAGUE_MATCHUP :
				// In case of regexp, do not normalize the filter value...
				if ( filterBean.getOperator() == Operator.MATCHES )
					return evalTextOp( filterBean, RepUtils.normalizeMatchup( context.repProc.getLeagueMatchup() ) );
				else {
					// Cache normalized filter matchup strings.
					String normu = matchupNormuMap.get( filterBean.getValue() );
					if ( normu == null )
						matchupNormuMap.put( (String) filterBean.getValue(), normu = RepUtils.normalizeMatchup( (String) filterBean.getValue() ) );
					return evalTextOp( normu, (Operator) filterBean.getOperator(), RepUtils.normalizeMatchup( context.repProc.getLeagueMatchup() ) );
				}
			case LENGTH :
				return evalIntegerOp( filterBean, (int) ( context.repProc.getLengthMs() / 1000 ) );
			case LEVELS :
				return evalIntegerOp( filterBean, context.user.getUid() == null ? null : context.user.getUid().getCombinedRaceLevels() );
			case MAP_FILE_NAME : {
				final ICacheHandle cacheHandle = context.repProc.getMapCacheHandle();
				return evalTextOp( filterBean, cacheHandle == null ? null : cacheHandle.getFileName() );
			}
			case MAP_HEIGHT :
				return evalIntegerOp( filterBean, context.gd.getMapSizeY() );
			case MAP_NAME :
				return evalTextOp( filterBean, context.replay.getDetails().getTitle() );
			case MAP_WIDTH :
				return evalIntegerOp( filterBean, context.gd.getMapSizeX() );
			case NAME :
				return evalTextOp( filterBean, context.user.getName() );
			case BETA_PTR :
				return evalBoolOp( filterBean, Protocol.BETA_BASE_BUILD_SET.contains( context.replay.getHeader().getBaseBuild() ) );
			case RACE :
				return evalEnumOp( filterBean, (Race) ( filterBean.getValue() == Race.RANDOM ? context.user.getSlot().getChosenRace() : context.user
				        .getPlayer().getRace() ) );
			case RACE_MATCHUP :
				// In case of regexp, do not normalize the filter value...
				if ( filterBean.getOperator() == Operator.MATCHES )
					return evalTextOp( filterBean, RepUtils.normalizeMatchup( context.repProc.getRaceMatchup() ) );
				else {
					// Cache normalized filter matchup strings.
					String normu = matchupNormuMap.get( filterBean.getValue() );
					if ( normu == null )
						matchupNormuMap.put( (String) filterBean.getValue(), normu = RepUtils.normalizeMatchup( (String) filterBean.getValue() ) );
					return evalTextOp( normu, (Operator) filterBean.getOperator(), RepUtils.normalizeMatchup( context.repProc.getRaceMatchup() ) );
				}
			case REGION :
				return evalEnumOp( filterBean, (Region) context.gd.getRegion() );
			case REPLAY_DATE :
				return evalDateOp( filterBean, context.replay.getDetails().getTime() );
			case RESULT :
				return evalEnumOp( filterBean, (Result) context.user.getPlayer().getResult() );
			case SPM :
				return evalIntegerOp( filterBean, context.user.getSpm() );
			case SQ :
				return evalIntegerOp( filterBean, context.user.getSq() );
			case START_DIR :
				return evalIntegerOp( filterBean, context.user.getStartDirection() );
			case SUPPLY_CAPPED :
				return evalIntegerOp( filterBean, context.user.getSupplyCappedPercent() );
			case TEAM :
				return evalIntegerOp( filterBean, context.user.getSlot().getTeamId() + 1 );
			case TOON : {
				final IToon toon = context.user.getToon();
				return evalTextOp( filterBean, toon == null ? null : toon.toString() );
			}
			case VERSION :
				return evalVersionOp( filterBean, context.replay.getHeader().getVersionView() );
			default :
				throw new RuntimeException( "Unhandled filter by: " + filterBean.getFilterBy() );
		}
	}
	
	/**
	 * Evaluates the integer operator.
	 * 
	 * @param filterBean filter bean defining the operator and filter value
	 * @param value operand to evaluate
	 * @return true if evaluation is true; false otherwise
	 * 
	 * @see #evalIntegerOp(IRepFilterBean, Double)
	 */
	private static boolean evalIntegerOp( final IRepFilterBean filterBean, final Integer value ) {
		if ( value == null )
			return false;
		
		final int fvalue = ( (Integer) filterBean.getValue() ).intValue();
		
		switch ( (Operator) filterBean.getOperator() ) {
			case EQUAL :
				return value == fvalue;
			case GREATER_THAN :
				return value > fvalue;
			case GREATER_THAN_OR_EQUAL :
				return value >= fvalue;
			case LESS_THAN :
				return value < fvalue;
			case LESS_THAN_OR_EQUAL :
				return value <= fvalue;
			case NOT_EQUAL :
				return value != fvalue;
			default :
				throw new RuntimeException( "Unhandled operator: " + filterBean.getOperator() );
		}
	}
	
	/**
	 * Evaluates the integer operator.
	 * 
	 * @param filterBean filter bean defining the operator and filter value
	 * @param value operand to evaluate
	 * @return true if evaluation is true; false otherwise
	 * 
	 * @see #evalIntegerOp(IRepFilterBean, Integer)
	 */
	private static boolean evalIntegerOp( final IRepFilterBean filterBean, final Double value ) {
		if ( value == null )
			return false;
		
		final double fvalue = ( (Integer) filterBean.getValue() ).doubleValue();
		
		switch ( (Operator) filterBean.getOperator() ) {
			case EQUAL :
				return value == fvalue;
			case GREATER_THAN :
				return value > fvalue;
			case GREATER_THAN_OR_EQUAL :
				return value >= fvalue;
			case LESS_THAN :
				return value < fvalue;
			case LESS_THAN_OR_EQUAL :
				return value <= fvalue;
			case NOT_EQUAL :
				return value != fvalue;
			default :
				throw new RuntimeException( "Unhandled operator: " + filterBean.getOperator() );
		}
	}
	
	/**
	 * Evaluates the date operator.
	 * 
	 * @param filterBean filter bean defining the operator and filter value
	 * @param value operand to evaluate
	 * @return true if evaluation is true; false otherwise
	 */
	private static boolean evalDateOp( final IRepFilterBean filterBean, final Date value ) {
		if ( value == null )
			return false;
		return value == null ? false : evalDateOp( filterBean, value.getTime() );
	}
	
	/**
	 * Evaluates the date operator.
	 * 
	 * @param filterBean filter bean defining the operator and filter value
	 * @param value operand to evaluate
	 * @return true if evaluation is true; false otherwise
	 */
	private static boolean evalDateOp( final IRepFilterBean filterBean, final long value ) {
		final long fvalue = ( (Date) filterBean.getValue() ).getTime();
		
		// test within 1000 ms accuracy
		final long accuracy = 1000;
		
		switch ( (Operator) filterBean.getOperator() ) {
			case EQUAL :
				return Math.abs( value - fvalue ) <= accuracy;
			case GREATER_THAN :
			case GREATER_THAN_OR_EQUAL :
				return value - accuracy > fvalue;
			case LESS_THAN :
			case LESS_THAN_OR_EQUAL :
				return value + accuracy < fvalue;
			case NOT_EQUAL :
				return Math.abs( value - fvalue ) > accuracy;
			default :
				throw new RuntimeException( "Unhandled operator: " + filterBean.getOperator() );
		}
	}
	
	/**
	 * Evaluates the enum operator.
	 * 
	 * @param filterBean filter bean defining the operator and filter value
	 * @param value operand to evaluate
	 * @return true if evaluation is true; false otherwise
	 * 
	 * @see #evalIntegerOp(IRepFilterBean, Double)
	 */
	private static boolean evalEnumOp( final IRepFilterBean filterBean, final Enum< ? > value ) {
		if ( value == null )
			return false;
		
		final Enum< ? > fvalue = (Enum< ? >) filterBean.getValue();
		
		switch ( (Operator) filterBean.getOperator() ) {
			case EQUAL :
				return value == fvalue;
			case GREATER_THAN :
				return value.ordinal() > fvalue.ordinal();
			case GREATER_THAN_OR_EQUAL :
				return value.ordinal() >= fvalue.ordinal();
			case LESS_THAN :
				return value.ordinal() < fvalue.ordinal();
			case LESS_THAN_OR_EQUAL :
				return value.ordinal() <= fvalue.ordinal();
			case NOT_EQUAL :
				return value != fvalue;
			default :
				throw new RuntimeException( "Unhandled operator: " + filterBean.getOperator() );
		}
	}
	
	/**
	 * Evaluates the text operator.
	 * 
	 * @param filterBean filter bean defining the operator and filter value
	 * @param value operand to evaluate
	 * @return true if evaluation is true; false otherwise
	 */
	private boolean evalTextOp( final IRepFilterBean filterBean, final String value ) {
		return evalTextOp( (String) filterBean.getValue(), (Operator) filterBean.getOperator(), value );
	}
	
	/**
	 * Evaluates the text operator.
	 * 
	 * @param fvalue filter value
	 * @param operator operator to be evaluated
	 * @param value operand to evaluate
	 * @return true if evaluation is true; false otherwise
	 */
	private boolean evalTextOp( String fvalue, final Operator operator, String value ) {
		if ( value == null )
			return false;
		value = value.toLowerCase();
		
		if ( operator != Operator.MATCHES ) // Regexp is case sensitive!
			fvalue = fvalue.toLowerCase();
		
		switch ( operator ) {
			case CONTAINS :
				return value.contains( fvalue );
			case ENDS_WITH :
				return value.endsWith( fvalue );
			case EQUAL :
				return value.equals( fvalue );
			case GREATER_THAN :
				return value.compareTo( fvalue ) > 0;
			case GREATER_THAN_OR_EQUAL :
				return value.compareTo( fvalue ) >= 0;
			case LESS_THAN :
				return value.compareTo( fvalue ) < 0;
			case LESS_THAN_OR_EQUAL :
				return value.compareTo( fvalue ) <= 0;
			case MATCHES : {
				// Cache compiled regular expressions.
				// If a pattern is invalid, store a null value so we don't try to compile it again.
				Pattern pattern = regexpPatternMap.get( fvalue );
				if ( pattern == null && !regexpPatternMap.containsKey( fvalue ) ) {
					try {
						pattern = Pattern.compile( fvalue );
					} catch ( final PatternSyntaxException pse ) {
						// Do nothing, pattern value will remain null
						pse.printStackTrace();
					}
					regexpPatternMap.put( fvalue, pattern );
				}
				return pattern != null && pattern.matcher( value ).matches();
			}
			case NOT_CONTAINS :
				return !value.contains( fvalue );
			case NOT_ENDS_WITH :
				return !value.endsWith( fvalue );
			case NOT_EQUAL :
				return !value.equals( fvalue );
			case NOT_STARTS_WITH :
				return !value.startsWith( fvalue );
			case STARTS_WITH :
				return value.startsWith( fvalue );
			default :
				throw new RuntimeException( "Unhandled operator: " + operator );
		}
	}
	
	/**
	 * Evaluates the boolean operator.
	 * 
	 * @param filterBean filter bean defining the operator and filter value
	 * @param value operand to evaluate
	 * @return true if evaluation is true; false otherwise
	 */
	private static boolean evalBoolOp( final IRepFilterBean filterBean, final Boolean value ) {
		if ( value == null )
			return false;
		
		final boolean fvalue = (Boolean) filterBean.getValue();
		
		switch ( (Operator) filterBean.getOperator() ) {
			case EQUAL :
				return value.booleanValue() == fvalue;
			case NOT_EQUAL :
				return value.booleanValue() != fvalue;
			default :
				throw new RuntimeException( "Unhandled operator: " + filterBean.getOperator() );
		}
	}
	
	/**
	 * Evaluates the version operator.
	 * 
	 * @param filterBean filter bean defining the operator and filter value
	 * @param value operand to evaluate
	 * @return true if evaluation is true; false otherwise
	 */
	private static boolean evalVersionOp( final IRepFilterBean filterBean, final IVersionView value ) {
		if ( value == null )
			return false;
		
		final IVersionView fvalue = (IVersionView) filterBean.getValue();
		
		switch ( (Operator) filterBean.getOperator() ) {
			case EQUAL :
				return value.compareTo( fvalue, false ) == 0; // Use compareTo() instead of equals(), equals() does not have a non-strict variant!
			case GREATER_THAN :
				return value.compareTo( fvalue, false ) > 0;
			case GREATER_THAN_OR_EQUAL :
				return value.compareTo( fvalue, false ) >= 0;
			case LESS_THAN :
				return value.compareTo( fvalue, false ) < 0;
			case LESS_THAN_OR_EQUAL :
				return value.compareTo( fvalue, false ) <= 0;
			case NOT_EQUAL :
				return value.compareTo( fvalue, false ) != 0; // Use compareTo() instead of equals(), equals() does not have a non-strict variant!
			default :
				throw new RuntimeException( "Unhandled operator: " + filterBean.getOperator() );
		}
	}
	
	@Override
	public void getStructure( final StringBuilder sb ) {
		if ( tree == null ) {
			sb.append( "<i>&lt;No active filters&gt;</i>" );
			return;
		}
		
		sb.append( "<table border=1 cellspacing=0 cellpadding=3 style='white-space:nowrap'>" );
		getStructure( tree, sb );
		sb.append( "</table>" );
	}
	
	/**
	 * Builds the structure of the specified node.
	 * 
	 * @param node node to build the structure of
	 * @param sb string builder to append the HTML structure to
	 */
	private void getStructure( final Node node, final StringBuilder sb ) {
		sb.append( "<td rowspan=" ).append( node.getFiltersCount() ).append( " class='conn'>" ).append( node.connection.name() );
		
		boolean firstCellRow = true;
		// Go downward, so the table will show filters in the same order as specified in our constructor (same as defined in the filters table).
		for ( int i = node.childList.size() - 1; i >= 0; i-- ) {
			final Object child = node.childList.get( i );
			
			if ( firstCellRow )
				firstCellRow = false;
			else
				sb.append( "<tr>" );
			if ( child instanceof RepFilterBean ) {
				final RepFilterBean filterBean = (RepFilterBean) child;
				
				sb.append( "<td colspan=" ).append( maxLevel - node.level ).append( "><span class='fbg'>[" ).append( filterBean.getFilterByGroup().text )
				        .append( "]</span> <span class='fby'>" ).append( filterBean.getFilterBy().text ).append( "</span> <span class='op'>" )
				        .append( filterBean.getOperator().htmlText ).append( "</span> <span class='fval'>" );
				
				if ( filterBean.getFilterBy().type == String.class )
					sb.append( '\'' ).append( Utils.safeForHtml( filterBean.getValue().toString() ) ).append( '\'' );
				else
					sb.append( filterBean.getValue() );
				sb.append( "</span>" );
				
			} else if ( child instanceof Node )
				getStructure( (Node) child, sb );
		}
	}
	
}
