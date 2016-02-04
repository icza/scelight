/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight.sc2.rep.model.details;

import hu.belicza.andras.util.StructView;
import hu.scelightapi.sc2.rep.model.details.IDetails;

import java.util.Date;
import java.util.Map;

/**
 * StarCraft II Replay details.
 * 
 * @author Andras Belicza
 */
public class Details extends StructView implements IDetails {
	
	// Eagerly initialized "cached" values
	
	/** Map title. */
	public final String title;
	
	/** Time utc. */
	public final Long   timeUtc;
	
	
	// Lazily initialized "cached" values
	
	/** Players. */
	private Player[]    players;
	
	/** Replay date+time. */
	private Date        time;
	
	
	/**
	 * Creates a new {@link Details}.
	 * 
	 * @param struct details data structure
	 */
	public Details( final Map< String, Object > struct ) {
		super( struct );
		
		title = get( F_TITLE ).toString();
		timeUtc = get( F_TIME_UTC );
	}
	
	@Override
	public Integer getCampaignIndex() {
		return get( F_CAMPAIGN_INDEX );
	}
	
	@Override
	public String getDescription() {
		return get( F_DESCRIPTION ).toString();
	}
	
	@Override
	public String getDifficulty() {
		return get( F_DIFFICULTY ).toString();
	}
	
	@Override
	public String getImageFilePath() {
		return get( F_IMAGE_FILE_PATH ).toString();
	}
	
	@Override
	public Boolean getIsBlizzardMap() {
		return get( F_IS_BLIZZARD_MAP );
	}
	
	@Override
	public Player[] getPlayerList() {
		if ( players == null ) {
			final Map< String, Object >[] array = get( F_PLAYER_LIST );
			
			players = new Player[ array.length ];
			for ( int i = array.length - 1; i >= 0; i-- )
				players[ i ] = new Player( array[ i ], i );
		}
		
		return players;
	}
	
	@Override
	public String getThumbnailFile() {
		return get( P_THUMBNAIL_FILE ).toString();
	}
	
	@Override
	public Long getTimeLocalOffset() {
		return get( F_TIME_LOCAL_OFFSET );
	}
	
	@Override
	public Long getTimeLocalOffsetMs() {
		return this.< Long > get( F_TIME_LOCAL_OFFSET ) / 10_000;
	}
	
	@Override
	public Double getTimeLocalOffsetHour() {
		return getTimeLocalOffset() / ( 3600.0 * 10_000_000.0 );
	}
	
	@Override
	public Long getTimeUtc() {
		return timeUtc;
	}
	
	@Override
	public Date getTime() {
		if ( time == null )
			time = new Date( ( getTimeUtc() - 116_444_736_000_000_000L ) / 10_000 );
		
		return time;
	}
	
	@Override
	public String getTitle() {
		return title;
	}
	
	@Override
	public Boolean getRestartAsTransitionMap() {
		return get( F_RESTART_AS_TRANSITION_MAP );
	}
	
}
