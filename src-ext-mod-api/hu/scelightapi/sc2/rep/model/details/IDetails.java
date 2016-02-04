/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelightapi.sc2.rep.model.details;

import hu.scelightapi.util.IStructView;

import java.util.Date;

/**
 * StarCraft II Replay details.
 * 
 * @author Andras Belicza
 */
public interface IDetails extends IStructView {
	
	// Structure field names and paths (all literal strings are interned)
	
	/** Campaign index field name. */
	String   F_CAMPAIGN_INDEX            = "campaignIndex";
	
	/** Description field name. */
	String   F_DESCRIPTION               = "description";
	
	/** Difficulty field name. */
	String   F_DIFFICULTY                = "difficulty";
	
	/** Image file path field name. */
	String   F_IMAGE_FILE_PATH           = "imageFilePath";
	
	/** Is Blizzard map field name. */
	String   F_IS_BLIZZARD_MAP           = "isBlizzardMap";
	
	/** Player list field name. */
	String   F_PLAYER_LIST               = "playerList";
	
	/** Thumbnail field path. */
	String   F_THUMBNAIL                 = "thumbnail";
	
	/** Thumbnail file field path. */
	String[] P_THUMBNAIL_FILE            = { F_THUMBNAIL, "file" };
	
	/** Time local offset field name. */
	String   F_TIME_LOCAL_OFFSET         = "timeLocalOffset";
	
	/** Time UTC field name. */
	String   F_TIME_UTC                  = "timeUTC";
	
	/** Title field name. */
	String   F_TITLE                     = "title";
	
	/** Restart as transition map field name. */
	String   F_RESTART_AS_TRANSITION_MAP = "restartAsTransitionMap";
	
	
	/**
	 * Returns the map title.
	 * 
	 * @return the map title
	 */
	String getTitle();
	
	/**
	 * Returns the campaign index.
	 * 
	 * @return the campaignIndex
	 */
	Integer getCampaignIndex();
	
	/**
	 * Returns the description.
	 * 
	 * @return the description
	 */
	String getDescription();
	
	/**
	 * Returns the difficulty.
	 * 
	 * @return the difficulty
	 */
	String getDifficulty();
	
	/**
	 * Returns the image file path.
	 * 
	 * @return the imageFilePath
	 */
	String getImageFilePath();
	
	/**
	 * Tells if the game was played on an official Blizzard map.
	 * 
	 * @return the isBlizzardMap
	 */
	Boolean getIsBlizzardMap();
	
	/**
	 * Returns the array of players.
	 * 
	 * @return the playerList
	 */
	IPlayer[] getPlayerList();
	
	/**
	 * Returns the thumbnail file.
	 * 
	 * @return the thumbnail file
	 */
	String getThumbnailFile();
	
	/**
	 * Returns the local time offset or the replay saver in unix format, 10 microset unit.
	 * 
	 * @return the local time offset or the replay saver in unix format, 10 microset unit.
	 * @see #getTimeLocalOffsetMs()
	 * @see #getTimeLocalOffsetHour()
	 */
	Long getTimeLocalOffset();
	
	/**
	 * Returns the local time offset or the replay saver in millisecond unit.
	 * 
	 * @return the local time offset or the replay saver in millisecond unit.
	 * @see #getTimeLocalOffset()
	 * @see #getTimeLocalOffsetHour()
	 */
	Long getTimeLocalOffsetMs();
	
	/**
	 * Returns the local time offset or the replay saver in hour unit.
	 * 
	 * @return the local time offset or the replay saver in hour unit
	 * @see #getTimeLocalOffset()
	 * @see #getTimeLocalOffsetMs()
	 */
	Double getTimeLocalOffsetHour();
	
	/**
	 * Returns the replay date+time in unix format, 10 microsec unit.
	 * 
	 * <p>
	 * Note: this is the time of game end (not game start).
	 * </p>
	 * 
	 * @return the replay date+time in unix format, 10 microsec unit.
	 */
	Long getTimeUtc();
	
	/**
	 * Returns the replay date+time.
	 * 
	 * <p>
	 * Note: this is the time of game end (not game start).
	 * </p>
	 * 
	 * @return the replay date+time
	 */
	Date getTime();
	
	/**
	 * Tells whether restart as transition map.
	 * 
	 * @return the restartAsTransitionMap
	 */
	Boolean getRestartAsTransitionMap();
	
}
