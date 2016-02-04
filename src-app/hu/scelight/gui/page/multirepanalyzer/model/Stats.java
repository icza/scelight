/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight.gui.page.multirepanalyzer.model;

import hu.belicza.andras.util.NullAwareComparable;
import hu.scelight.service.env.Env;
import hu.scelight.util.Utils;
import hu.sllauncher.gui.comp.table.renderer.ProgressBarView;
import hu.sllauncher.util.DateFormat;
import hu.sllauncher.util.DateValue;
import hu.sllauncher.util.DurationFormat;
import hu.sllauncher.util.DurationValue;
import hu.sllauncher.util.RelativeDate;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Basic aggregate stats.
 * 
 * @author Andras Belicza
 */
public class Stats {
	
	/** First date. */
	public Date               firstDate;
	
	/** Last date. */
	public Date               lastDate;
	
	/** Total time played in milliseconds. */
	public long               totalTimePlayedMs;
	
	/**
	 * Number of plays.<br>
	 * This might be greater than the number of games if zero-toons are involved.
	 */
	public int                plays;
	
	/** List of games. */
	public final List< Game > gameList = new ArrayList<>();
	
	
	/**
	 * Updates the stats with the specified {@link Game}.
	 * 
	 * @param game game to be included
	 * @param timePlayedMs time played in milliseconds to add to the total
	 */
	public void updateWithGame( final Game game, final long timePlayedMs ) {
		if ( firstDate == null || firstDate.after( game.date ) )
			firstDate = game.date;
		
		if ( lastDate == null || lastDate.before( game.date ) )
			lastDate = game.date;
		
		totalTimePlayedMs += timePlayedMs;
		
		plays++;
		
		// The zero-toon might be a participant of the same game multiple times, so if stats are calculated,
		// iterating over the participants of a game the same stats object might be used multiple times.
		// But we don't want the same game added multiple times for the same "virtual" player (with the zero-toon).
		// Fortunately games are processed sequentially when calculating player stats, so it's enough to check if the last added game equals
		// to the game we're updating with in order not to add the same game multiple times.
		if ( gameList.isEmpty() || gameList.get( gameList.size() - 1 ) != game )
			gameList.add( game );
	}
	
	/**
	 * Returns the plays progress bar.
	 * 
	 * @param maxPlays max plays
	 * @return the plays progress bar
	 */
	public ProgressBarView getPlaysBar( final int maxPlays ) {
		return new ProgressBarView( plays, maxPlays );
	}
	
	/**
	 * Returns the games progress bar.
	 * 
	 * @param maxGames max games
	 * @return the games progress bar
	 */
	public ProgressBarView getGamesBar( final int maxGames ) {
		return new ProgressBarView( gameList.size(), maxGames );
	}
	
	/**
	 * Returns the first date to be added to the table.
	 * 
	 * @return the first date to be added to the table
	 */
	public DateValue getFirstDate() {
		return new DateValue( firstDate, DateFormat.DATE );
	}
	
	/**
	 * Returns the last date to be added to the table.
	 * 
	 * @return the last date to be added to the table
	 */
	public DateValue getLastDate() {
		return new DateValue( lastDate, DateFormat.DATE );
	}
	
	/**
	 * Returns the presence in milliseconds.
	 * 
	 * @return the presence in milliseconds
	 */
	public long getPresenceMs() {
		return Utils.MS_IN_DAY + lastDate.getTime() - firstDate.getTime();
	}
	
	/**
	 * Returns the presence in days (difference between the first and last dates).<br>
	 * Presence is at least 1 day.
	 * 
	 * @return the presence in days (difference between the first and last dates)
	 */
	private int getPresenceDays() {
		return 1 + (int) ( ( lastDate.getTime() - firstDate.getTime() ) / Utils.MS_IN_DAY );
	}
	
	/**
	 * Returns the presence as a formatted relative date compared to the current time.
	 * 
	 * @return the presence as a formatted relative date compared to the current time
	 */
	public RelativeDate getPresence() {
		return new RelativeDate( System.currentTimeMillis() + getPresenceMs(), false, 2, false );
	}
	
	/**
	 * Returns the presence progress bar, its text being the formatted relative date compared to the current time.
	 * 
	 * @param maxPresenceMs max presence in milliseconds
	 * @return the presence progress bar, its text being the formatted relative date compared to the current time
	 */
	public ProgressBarView getPresenceBar( final long maxPresenceMs ) {
		return new ProgressBarView( (int) ( getPresenceMs() / 1000 ), (int) ( maxPresenceMs / 1000 ), getPresence().toString() );
	}
	
	/**
	 * Returns the average plays / games per day.
	 * 
	 * @return the average plays / games per day
	 */
	public float getAvgPlaysPerDay() {
		return (float) plays / getPresenceDays(); // Presence is at least 1 day
	}
	
	/**
	 * Returns the average plays / games per day progress bar.
	 * 
	 * @param maxPlaysPerDay max average plays per day
	 * @return the average plays / games per day progress bar
	 */
	public ProgressBarView getAvgPlaysPerDayBar( final float maxPlaysPerDay ) {
		final float avgPlaysPerDay = getAvgPlaysPerDay();
		return new ProgressBarView( (int) ( avgPlaysPerDay * 1000 ), (int) ( maxPlaysPerDay * 1000 ), Env.LANG.formatNumber( avgPlaysPerDay, 2 ) );
	}
	
	/**
	 * Returns the total time played as a formatted relative date compared to the current time.
	 * 
	 * @return the total time played as a formatted relative date compared to the current time
	 */
	public RelativeDate getTotalTimePlayed() {
		return new RelativeDate( System.currentTimeMillis() + totalTimePlayedMs, false, 2, false );
	}
	
	/**
	 * Returns the total time played progress bar, its text being the formatted relative date compared to the current time.
	 * 
	 * @param maxTimePlayedMs max total time played in milliseconds
	 * @return the total time played progress bar, its text being the formatted relative date compared to the current time
	 */
	public ProgressBarView getTotalTimePlayedBar( final long maxTimePlayedMs ) {
		return new ProgressBarView( (int) ( totalTimePlayedMs / 1000 ), (int) ( maxTimePlayedMs / 1000 ), getTotalTimePlayed().toString() );
	}
	
	/**
	 * Returns the average game/play length.
	 * 
	 * @return the average game/play length
	 */
	public DurationValue getAvgLength() {
		return new DurationValue( getAvgLengthMs(), DurationFormat.HOUR_MIN_SEC );
	}
	
	/**
	 * Returns the average game/play length progress bar.
	 * 
	 * @param maxLength max average length
	 * @return the average game/play length progress bar
	 */
	public ProgressBarView getAvgLengthBar( final long maxLength ) {
		// Use hundredth seconds: Max int is about 24 days if ms; 240 days if hundredth seconds; also better precision when sorting!
		final long avgLength = getAvgLengthMs();
		return new ProgressBarView( (int) ( avgLength / 10 ), (int) ( maxLength / 10 ), DurationFormat.HOUR_MIN_SEC.formatDuration( avgLength ) );
	}
	
	/**
	 * Returns the average game/play length in milliseconds.
	 * 
	 * @return the average game/play length in milliseconds
	 */
	public long getAvgLengthMs() {
		return totalTimePlayedMs / plays;
	}
	
	/**
	 * Returns a share percent of this stats plays compared to the specified total plays.
	 * 
	 * @param totalPlays number of total plays
	 * @return a share percent of this stats plays compared to the specified total plays
	 */
	public NullAwareComparable< Double > getPlaySharePercent( final int totalPlays ) {
		return NullAwareComparable.getPercent( totalPlays == 0 ? null : plays * 100.0 / totalPlays );
	}
	
	/**
	 * Returns the replay files.
	 * 
	 * @return the replay files
	 */
	public Path[] getRepFiles() {
		final Path[] repFiles = new Path[ gameList.size() ];
		
		for ( int i = gameList.size() - 1; i >= 0; i-- )
			repFiles[ i ] = gameList.get( i ).file;
		
		return repFiles;
	}
	
}
