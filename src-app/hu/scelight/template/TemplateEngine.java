/*
 * Project Abdra Commander
 * 
 * Copyright (c) 2012 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight.template;

import hu.scelight.sc2.rep.factory.RepParserEngine;
import hu.scelight.sc2.rep.model.details.Race;
import hu.scelight.sc2.rep.model.details.Result;
import hu.scelight.sc2.rep.model.initdata.userinitdata.League;
import hu.scelight.sc2.rep.repproc.RepProcessor;
import hu.scelight.sc2.rep.repproc.User;
import hu.scelight.service.env.Env;
import hu.scelight.util.Utils;
import hu.scelight.util.sc2rep.RepUtils;
import hu.scelightapi.sc2.rep.model.IReplay;
import hu.scelightapi.sc2.rep.repproc.IRepProcessor;
import hu.scelightapi.sc2.rep.repproc.IUser;
import hu.scelightapi.template.ITemplateEngine;
import hu.sllauncher.bean.VersionBean;
import hu.sllauncher.util.DurationFormat;

import java.nio.file.Path;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

/**
 * Name template engine capable of processing templates and applying them on replay files.
 * 
 * <p>
 * Implementation is not thread-safe. Parallel use is not supported. Even if multiple instances from the same template would be created, they could not share on
 * state like the value of the counter symbol.
 * </p>
 * 
 * @author Andras Belicza
 */
public class TemplateEngine implements ITemplateEngine {
	
	/** Version of the template engine. */
	public static final VersionBean            VERSION                       = new VersionBean( 1, 2, 1 );
	
	/** Minimum value of symbol params. */
	public static final int                    MIN_SYMBOL_PARAM              = 1;
	
	/** Maximum value of symbol params. */
	public static final int                    MAX_SYMBOL_PARAM              = 8;
	
	
	/** Minimum char value of symbol params. */
	private static final int                   MIN_CHAR_SYMBOL_PARAM         = '1';
	
	/** Maximum char value of symbol params. */
	private static final int                   MAX_CHAR_SYMBOL_PARAM         = '8';
	
	
	
	/** Date format for the {@link Symbol#DATE} symbol. */
	private static final DateFormat            DATE_FORMAT                   = new SimpleDateFormat( "yyyy-MM-dd" );
	
	/** Date format for the {@link Symbol#DATE_TIME} symbol. */
	private static final DateFormat            DATE_TIME_FORMAT              = new SimpleDateFormat( "yyyy-MM-dd HH_mm_ss" );
	
	/** Date format for the {@link Symbol#DATE_SHORT} symbol. */
	private static final DateFormat            DATE_SHORT_FORMAT             = new SimpleDateFormat( "yy-MM-dd" );
	
	/** Date format for the {@link Symbol#DATE_TIME_SHORT} symbol. */
	private static final DateFormat            DATE_TIME_SHORT_FORMAT        = new SimpleDateFormat( "yy-MM-dd HH_mm_ss" );
	
	/** Date format for the {@link Symbol#DATE_TINY} symbol. */
	private static final DateFormat            DATE_TINY_FORMAT              = new SimpleDateFormat( "yyMMdd" );
	
	/** Date format for the {@link Symbol#DATE_TIME_TINY} symbol. */
	private static final DateFormat            DATE_TIME_TINY_FORMAT         = new SimpleDateFormat( "yyMMdd HHmmss" );
	
	
	/** Set of disallowed characters in file name. */
	private static final Set< Character >      DISALLOWED_FILE_NAME_CHAR_SET = new HashSet<>();
	static {
		for ( int i = 0; i < 32; i++ )
			DISALLOWED_FILE_NAME_CHAR_SET.add( (char) i );
		
		final String notAllowedChars;
		switch ( Env.OS ) {
			case WINDOWS :
				notAllowedChars = "?*:|\"";
				break;
			case OS_X :
				notAllowedChars = ":";
				break;
			case UNIX :
				// For Ext3 only '/' and the NULL is forbidden, but linux also handles NTFS and FAT file
				// systems, so let's not allow what Windows doesn't allow...
				notAllowedChars = "?%*:|\"";
				break;
			default :
				notAllowedChars = "?%*:|\"";
				break;
		}
		
		for ( final char nach : notAllowedChars.toCharArray() )
			DISALLOWED_FILE_NAME_CHAR_SET.add( nach );
	}
	
	/** When working with map words, these words are to be omitted. */
	private static final Set< String >         BANNED_MAP_WORDS              = Utils.asNewSet( "a", "the", "of", "-" );
	
	/** Map from symbol id to symbol. */
	private static final Map< String, Symbol > ID_SYMBOL_MAP                 = new HashMap<>();
	static {
		for ( final Symbol s : Symbol.VALUES ) {
			if ( s.hasParam ) {
				final StringBuffer sb = new StringBuffer( s.id.length() );
				sb.append( s.id );
				for ( char ch = MIN_CHAR_SYMBOL_PARAM; ch <= MAX_CHAR_SYMBOL_PARAM; ch++ ) {
					sb.setCharAt( sb.length() - 1, ch );
					ID_SYMBOL_MAP.put( sb.toString(), s );
				}
			} else
				ID_SYMBOL_MAP.put( s.id, s );
		}
	}
	
	
	/**
	 * The parsed template.<br>
	 * Possible element types:
	 * <ul>
	 * <li>{@link SymbolWithRange}: a symbol with value range specified
	 * <li>{@link StringBuilder}: static text
	 * <li><code>Object[]</code>: a Player Info Block (objects of the PIB)
	 * <ul>
	 */
	private final Object[]                     parsed;
	
	/**
	 * Creates a new {@link TemplateEngine}.
	 * 
	 * @param template template string
	 * @throws InvalidTemplateException if the specified template is invalid
	 */
	public TemplateEngine( final String template ) throws InvalidTemplateException {
		if ( template.isEmpty() )
			throw new InvalidTemplateException( "Template cannot be an empty string!" );
		
		// Parse template
		
		final List< Object > parsedList = new ArrayList<>();
		
		// Objects inside the PIB being parsed, null if we're not in a PIB
		List< Object > pibObjectList = null;
		
		// Working list: list to add parsed objects to
		List< Object > workingList = parsedList;
		
		for ( int i = 0; i < template.length(); i++ ) {
			final char ch = template.charAt( i );
			
			switch ( ch ) {
				case ']' :
					throw new InvalidTemplateException( "Invalid symbol: closing tag found without an opening tag!", i );
				case '[' : {
					// Symbol opening
					final int symbolEnd = template.indexOf( ']', i + 1 );
					if ( symbolEnd < 0 )
						throw new InvalidTemplateException( "Invalid symbol: opening tag found without a closing tag!", i );
					final boolean hasRange = template.charAt( symbolEnd - 1 ) == '}';
					final int idEnd = hasRange ? template.indexOf( '{', i + 1 ) : symbolEnd;
					if ( idEnd < 0 )
						throw new InvalidTemplateException( "Invalid symbol value range: closing tag found without an opening tag!", symbolEnd - 1 );
					final Symbol s = ID_SYMBOL_MAP.get( template.substring( i + 1, idEnd ) );
					if ( s == null )
						throw new InvalidTemplateException( "Invalid symbol: symbol not recognized!", i + 1 );
					if ( s.scope == SymbolScope.PIB && pibObjectList == null )
						throw new InvalidTemplateException( "The symbol can only be used inside of a Player Info Block: " + s.text, i + 1 );
					int param = -1;
					if ( s.hasParam ) {
						param = template.charAt( idEnd - 1 ) - '0';
						// ID_SYMBOL_MAP gets filled only with valid range of params, but just as an extra check:
						if ( param < MIN_SYMBOL_PARAM || param > MAX_SYMBOL_PARAM )
							throw new InvalidTemplateException( "Invalid symbol param: must be in the range " + MIN_SYMBOL_PARAM + ".." + MAX_SYMBOL_PARAM
							        + "!", idEnd - 1 );
					}
					int rangeFirst = -1;
					int rangeLast = -1;
					// Parse range
					if ( hasRange ) {
						final int rangeSep = template.indexOf( '-', idEnd + 1 );
						if ( rangeSep < 0 )
							throw new InvalidTemplateException(
							        "<html>Invalid symbol value range: must be in the format of <code>'{first-last}'</code></html>", idEnd + 1 );
						try {
							rangeFirst = Integer.parseInt( template.substring( idEnd + 1, rangeSep ) );
						} catch ( final NumberFormatException nfe ) {
							throw new InvalidTemplateException( "Invalid symbol value range: range first is not a valid number!", idEnd + 1 );
						}
						try {
							rangeLast = Integer.parseInt( template.substring( rangeSep + 1, symbolEnd - 1 ) );
						} catch ( final NumberFormatException nfe ) {
							throw new InvalidTemplateException( "Invalid symbol value range: range last is not a valid number!", rangeSep + 1 );
						}
						if ( rangeFirst < 1 )
							throw new InvalidTemplateException( "Invalid symbol value range: range first must be greater than 0!", idEnd + 1 );
						if ( rangeFirst > rangeLast )
							throw new InvalidTemplateException( "Invalid symbol value range: range first cannot be greater than range last!", idEnd + 1 );
					}
					workingList.add( new SymbolWithRange( s, param, rangeFirst - 1, rangeLast ) );
					i = symbolEnd;
					break;
				}
				case '<' : {
					// PIB opening
					if ( pibObjectList != null )
						throw new InvalidTemplateException( "Player Info Block cannot be embedded in another block!", i );
					workingList = pibObjectList = new ArrayList<>();
					break;
				}
				case '>' : {
					// PIB closing
					if ( pibObjectList == null )
						throw new InvalidTemplateException( "Invalid Player Info Block: closing tag found without an opening tag!", i );
					parsedList.add( pibObjectList.toArray() );
					pibObjectList = null;
					workingList = parsedList;
					break;
				}
				default : {
					// Static text
					// Folder separator is inserted as a symbol, do not allow it as static text
					if ( DISALLOWED_FILE_NAME_CHAR_SET.contains( ch ) || ch == '/' || ch == '\\' )
						throw new InvalidTemplateException( "<html>Not allowed character in file name: <code>'" + ch + "'</code></html>", i );
					final StringBuilder sb;
					if ( workingList.isEmpty() || !( workingList.get( workingList.size() - 1 ) instanceof StringBuilder ) )
						workingList.add( sb = new StringBuilder() );
					else
						sb = (StringBuilder) workingList.get( workingList.size() - 1 );
					sb.append( ch );
					break;
				}
			}
		}
		
		if ( pibObjectList != null )
			throw new InvalidTemplateException( "Invalid Player Info Block: opening tag found without a closing tag!", template.lastIndexOf( '<' ) );
		
		// Append SC2Replay extension if not contained
		// parsedList cannot be empty here
		if ( parsedList.get( parsedList.size() - 1 ) instanceof StringBuilder ) {
			final StringBuilder sb = (StringBuilder) parsedList.get( parsedList.size() - 1 );
			if ( sb.length() < 10 || !sb.substring( sb.length() - 10 ).equalsIgnoreCase( ".SC2Replay" ) )
				sb.append( ".SC2Replay" );
		} else
			parsedList.add( new StringBuilder( ".SC2Replay" ) );
		
		parsed = parsedList.toArray();
	}
	
	
	/** Common / shared name builder to be use when building new names during a template apply process. */
	private final StringBuilder sb = new StringBuilder();
	
	/** Replay processor of the replay being renamed. */
	private IRepProcessor       repProc;
	
	/** Shortcut to the repprocessor's replay. */
	private IReplay             replay;
	
	/** Shortcut to the repprocessor's playerUsers array . */
	private IUser[]             playerUsers;
	
	/** Target folder in which to apply the template. */
	private Path                targetFolder;
	
	/**
	 * Position of the last folder separator.<br>
	 * Used to properly determine the target folder with subfolders introduced by the template in which to count replays for the replay count symbols.
	 */
	private int                 lastFolderSepIdx;
	
	@Override
	public Path apply( final Path file ) {
		return apply( file, file.getParent() );
	}
	
	@Override
	public Path apply( final Path file, final Path targetFolder ) {
		final RepProcessor repProc = RepParserEngine.getRepProc( file );
		
		if ( repProc == null )
			return null;
		
		return apply( repProc, targetFolder );
	}
	
	@Override
	public Path apply( final IRepProcessor repProc, final Path targetFolder ) {
		this.repProc = repProc;
		replay = repProc.getReplay();
		playerUsers = repProc.getPlayerUsers();
		
		this.targetFolder = targetFolder;
		lastFolderSepIdx = 0;
		
		// Clear previous name
		sb.setLength( 0 );
		
		apply_( parsed );
		
		// Replace characters that are not allowed in file names: (required because map names may contain * for example!)
		for ( int i = sb.length() - 1; i >= 0; i-- )
			if ( DISALLOWED_FILE_NAME_CHAR_SET.contains( sb.charAt( i ) ) )
				sb.setCharAt( i, '_' );
		
		// Folder cannot end with a space, remove them!
		String result = sb.toString();
		while ( result.indexOf( " /" ) >= 0 )
			result = result.replace( " /", "/" );
		
		// Name cannot start with a folder separator
		while ( result.charAt( 0 ) == '/' )
			result = result.substring( 1 );
		
		// Cannot allow empty file name
		if ( result.length() == 10 ) // Only ".SC2Replay"
			result = "_empty_.SC2Replay";
		else if ( result.charAt( result.length() - 11 ) == '/' ) // Ends with "/.SC2Replay"
			result = result.substring( 0, result.length() - 10 ) + "_empty_.SC2Replay";
		
		return targetFolder.resolve( result );
	}
	
	
	/** Current {@link User} during processing a PIB. */
	private IUser pibUser;
	
	/**
	 * Applies the template on the current replay file.
	 * 
	 * @param objs template objects to be applied
	 */
	private void apply_( final Object[] objs ) {
		for ( final Object o : objs ) {
			if ( o instanceof SymbolWithRange ) {
				
				processSymbol( (SymbolWithRange) o );
				
			} else if ( o instanceof StringBuilder ) {
				
				// Static text
				sb.append( (StringBuilder) o );
				
			} else if ( o instanceof Object[] ) {
				
				// PIB
				final Object[] pibObjs = (Object[]) o;
				
				int lastTeam = -1;
				int playerInTeam = 0;
				for ( final IUser u : playerUsers ) {
					if ( lastTeam < 0 )
						lastTeam = u.getSlot().getTeamId();
					
					final int team = u.getSlot().getTeamId();
					if ( team != lastTeam ) {
						sb.append( " vs " );
						lastTeam = team;
						playerInTeam = 1;
					} else if ( playerInTeam++ > 0 )
						sb.append( ", " );
					
					pibUser = u;
					apply_( pibObjs );
				}
				
			} else
				throw new RuntimeException( "Unhandled template object type: " + o.getClass() );
		}
	}
	
	
	/** Common / shared symbol value builder to be use when building the value of a symbol. */
	private final StringBuilder     vb               = new StringBuilder();
	
	/** Value for the counter symbols. Key is the counter id (digit count), value is the current counter value. */
	private Map< Integer, Integer > symbolCounterMap = new HashMap<>();
	
	/**
	 * Processes the specified symbol.
	 * 
	 * @param swr symbol with range value
	 */
	private void processSymbol( final SymbolWithRange swr ) {
		vb.setLength( 0 );
		
		switch ( swr.symbol ) {
			case APM :
				if ( swr.param <= playerUsers.length )
					vb.append( playerUsers[ swr.param - 1 ].getApm() );
				break;
			case AVG_APM :
				vb.append( repProc.getAvgAPM() );
				break;
			case AVG_LEAGUE :
				vb.append( repProc.getAvgLeague().getLetter() );
				break;
			case AVG_LEAGUE_LONG :
				vb.append( repProc.getAvgLeague().toString() );
				break;
			case AVG_SPM :
				vb.append( Env.LANG.formatNumber( repProc.getAvgSPM(), 2 ) );
				break;
			case AVG_SQ :
				vb.append( repProc.getAvgSQ() );
				break;
			case AVG_SUPPLY_CAPPED :
				vb.append( Env.LANG.formatNumber( repProc.getAvgSupplyCappedPercent(), 2 ) );
				break;
			case BRACKETS_CLOSE :
				vb.append( ']' );
				break;
			case BRACKETS_OPEN :
				vb.append( '[' );
				break;
			case COUNTER : {
				Integer value = symbolCounterMap.get( swr.param );
				vb.append( value == null ? value = 1 : value );
				symbolCounterMap.put( swr.param, value + 1 );
				for ( int i = swr.param - vb.length(); i > 0; i-- )
					vb.insert( 0, '0' );
				break;
			}
			case DATE :
				synchronized ( DATE_FORMAT ) {
					vb.append( DATE_FORMAT.format( replay.getDetails().getTime() ) );
				}
				break;
			case DATE_SHORT :
				synchronized ( DATE_SHORT_FORMAT ) {
					vb.append( DATE_SHORT_FORMAT.format( replay.getDetails().getTime() ) );
				}
				break;
			case DATE_TINY :
				synchronized ( DATE_TINY_FORMAT ) {
					vb.append( DATE_TINY_FORMAT.format( replay.getDetails().getTime() ) );
				}
				break;
			case DATE_TIME :
				synchronized ( DATE_TIME_FORMAT ) {
					vb.append( DATE_TIME_FORMAT.format( replay.getDetails().getTime() ) );
				}
				break;
			case DATE_TIME_SHORT :
				synchronized ( DATE_TIME_SHORT_FORMAT ) {
					vb.append( DATE_TIME_SHORT_FORMAT.format( replay.getDetails().getTime() ) );
				}
				break;
			case DATE_TIME_TINY :
				synchronized ( DATE_TIME_TINY_FORMAT ) {
					vb.append( DATE_TIME_TINY_FORMAT.format( replay.getDetails().getTime() ) );
				}
				break;
			case EXPANSION :
				vb.append( replay.getInitData().getGameDescription().getExpansionLevel().toString() );
				break;
			case FOLDER_SEPARATOR :
				vb.append( '/' );
				break;
			case FORMAT :
				vb.append( repProc.getFormat().toString() );
				break;
			case LEAGUE :
				if ( swr.param <= playerUsers.length )
					vb.append( playerUsers[ swr.param - 1 ].getUid() == null ? League.UNKNOWN.letter : playerUsers[ swr.param - 1 ].getUid().getHighestLeague()
					        .getLetter() );
				break;
			case LEAGUE_LONG :
				if ( swr.param <= playerUsers.length )
					vb.append( playerUsers[ swr.param - 1 ].getUid() == null ? League.UNKNOWN.text : playerUsers[ swr.param - 1 ].getUid().getHighestLeague()
					        .toString() );
				break;
			case LEAGUE_MATCHUP :
				vb.append( repProc.getLeagueMatchup() );
				break;
			case LENGTH :
				vb.append( DurationFormat.AUTO.formatDuration( repProc.getLengthMs() ).replace( ':', '_' ) );
				break;
			case LOSERS :
				vb.append( repProc.getLosersString() );
				break;
			case MAP_NAME :
				vb.append( replay.getDetails().getTitle() );
				break;
			case MAP_WORDS :
			case MAP_ACRONYM : {
				int wordCount = 0;
				for ( final StringTokenizer st = new StringTokenizer( replay.getDetails().getTitle() ); st.hasMoreTokens(); ) {
					final String word = st.nextToken();
					// We need to lower case even if only map acronym is required
					// because we can only tell whether the word is banned if lower cased!
					if ( BANNED_MAP_WORDS.contains( word.toLowerCase() ) )
						continue;
					if ( swr.symbol == Symbol.MAP_ACRONYM ) {
						vb.append( Character.toUpperCase( word.charAt( 0 ) ) ); // Word is not empty (it's a token)!
					} else {
						if ( wordCount++ > 0 )
							vb.append( ' ' );
						vb.append( word );
						if ( wordCount == swr.param )
							break;
					}
				}
				break;
			}
			case MATCHUP :
				vb.append( repProc.getRaceMatchup() );
				break;
			case MODE :
				vb.append( replay.getAttributesEvents().getGameMode().toString() );
				break;
			case ORIG_NAME :
				vb.append( Utils.getFileNameWithoutExt( repProc.getFile() ) );
				break;
			case PIB :
				// Never to end up here
				break;
			case PIB_APM :
				vb.append( pibUser.getApm() );
				break;
			case PIB_LEAGUE :
				vb.append( pibUser.getUid() == null ? League.UNKNOWN.letter : pibUser.getUid().getHighestLeague().getLetter() );
				break;
			case PIB_LEAGUE_LONG :
				vb.append( pibUser.getUid() == null ? League.UNKNOWN.text : pibUser.getUid().getHighestLeague().toString() );
				break;
			case PIB_PLAYER :
				vb.append( pibUser.getFullName() );
				break;
			case PIB_RACE :
				vb.append( pibUser.getPlayer() == null ? Race.UNKNOWN.letter : pibUser.getPlayer().getRace().getLetter() );
				break;
			case PIB_RACE_LONG :
				vb.append( pibUser.getPlayer() == null ? Race.UNKNOWN.text : pibUser.getPlayer().getRace().toString() );
				break;
			case PIB_RESULT :
				vb.append( pibUser.getPlayer() == null ? Result.UNKNOWN.letter : pibUser.getPlayer().getResult().getLetter() );
				break;
			case PIB_RESULT_LONG :
				vb.append( pibUser.getPlayer() == null ? Result.UNKNOWN.text : pibUser.getPlayer().getResult().toString() );
				break;
			case PIB_SPM :
				vb.append( Env.LANG.formatNumber( pibUser.getSpm(), 2 ) );
				break;
			case PIB_SQ :
				vb.append( pibUser.getSq() );
				break;
			case PIB_SUPPLY_CAPPED :
				vb.append( Env.LANG.formatNumber( pibUser.getSupplyCappedPercent(), 2 ) );
				break;
			case PIB_START_DIR :
				vb.append( pibUser.getStartDirection() );
				break;
			case PLAYER :
				if ( swr.param <= playerUsers.length )
					vb.append( playerUsers[ swr.param - 1 ].getFullName() );
				break;
			case PLAYERS :
				vb.append( repProc.getPlayersString() );
				break;
			case PLAYERS_GROUPED :
				vb.append( repProc.getPlayersGrouped() );
				break;
			case RACE :
				if ( swr.param <= playerUsers.length )
					vb.append( playerUsers[ swr.param - 1 ].getPlayer() == null ? Race.UNKNOWN.letter : playerUsers[ swr.param - 1 ].getPlayer().getRace()
					        .getLetter() );
				break;
			case RACE_LONG :
				if ( swr.param <= playerUsers.length )
					vb.append( playerUsers[ swr.param - 1 ].getPlayer() == null ? Race.UNKNOWN.text : playerUsers[ swr.param - 1 ].getPlayer().getRace()
					        .toString() );
				break;
			case REGION :
				vb.append( replay.getInitData().getGameDescription().getRegion().toString() );
				break;
			case REGION_CODE :
				vb.append( replay.getInitData().getGameDescription().getRegion().getCode() );
				break;
			case REPLAY_COUNT : {
				final Path targetFolderWithSubfolders = lastFolderSepIdx > 0 ? targetFolder.resolve( sb.substring( 0, lastFolderSepIdx ) ) : targetFolder;
				// +1 is good for all return values of countReplays()
				vb.append( RepUtils.countReplays( targetFolderWithSubfolders ) + 1 );
				for ( int i = swr.param - vb.length(); i > 0; i-- )
					vb.insert( 0, '0' );
				break;
			}
			case RESULT :
				if ( swr.param <= playerUsers.length )
					vb.append( playerUsers[ swr.param - 1 ].getPlayer() == null ? Result.UNKNOWN.letter : playerUsers[ swr.param - 1 ].getPlayer().getResult()
					        .getLetter() );
				break;
			case RESULT_LONG :
				if ( swr.param <= playerUsers.length )
					vb.append( playerUsers[ swr.param - 1 ].getPlayer() == null ? Result.UNKNOWN.text : playerUsers[ swr.param - 1 ].getPlayer().getResult()
					        .toString() );
				break;
			case SPM :
				if ( swr.param <= playerUsers.length )
					vb.append( Env.LANG.formatNumber( playerUsers[ swr.param - 1 ].getSpm(), 2 ) );
				break;
			case SQ :
				if ( swr.param <= playerUsers.length )
					vb.append( playerUsers[ swr.param - 1 ].getSq() );
				break;
			case SUPPLY_CAPPED :
				if ( swr.param <= playerUsers.length )
					vb.append( Env.LANG.formatNumber( playerUsers[ swr.param - 1 ].getSupplyCappedPercent(), 2 ) );
				break;
			case START_DIR :
				if ( swr.param <= playerUsers.length )
					vb.append( playerUsers[ swr.param - 1 ].getStartDirection() );
				break;
			case VERSION :
				vb.append( replay.getHeader().versionString( false ) );
				break;
			case VERSION_BUILD :
				vb.append( replay.getHeader().getBaseBuild() );
				break;
			case VERSION_FULL :
				vb.append( replay.getHeader().versionString() );
				break;
			case WINNERS :
				vb.append( repProc.getWinnersString() );
				break;
		}
		
		if ( swr.first < 0 )
			sb.append( vb );
		else
			sb.append( vb, Math.min( vb.length(), swr.first ), Math.min( vb.length(), swr.last ) );
		
		if ( swr.symbol == Symbol.FOLDER_SEPARATOR )
			lastFolderSepIdx = sb.length();
	}
	
}
