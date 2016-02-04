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
import hu.scelight.sc2.rep.model.details.Result;
import hu.scelight.service.env.Env;
import hu.sllauncher.gui.comp.table.renderer.ProgressBarView;

/**
 * Game record.
 * 
 * @author Andras Belicza
 */
public class Record implements Comparable< Record > {
	
	/** Number of victories. */
	public int victories;
	
	/** Number of defeats. */
	public int defeats;
	
	/** Number of other games / plays. */
	public int other;
	
	/**
	 * Updates the record with the specified result.
	 * 
	 * @param result result to update the record with
	 */
	public void updateWithResult( final Result result ) {
		switch ( result ) {
			case VICTORY :
				victories++;
				break;
			case DEFEAT :
				defeats++;
				break;
			default :
				other++;
				break;
		}
	}
	
	/**
	 * Returns the number of all games / plays.
	 * 
	 * @return the number of all games / plays
	 */
	public int getAll() {
		return victories + defeats + other;
	}
	
	/**
	 * Returns a string representation of the record in the form of:
	 * 
	 * <pre>
	 * V - D - O
	 * </pre>
	 * 
	 * where V = Victories, D = Defeats, O = Other games (unknown or tie).
	 */
	@Override
	public String toString() {
		return victories + "-" + defeats + "-" + other;
	}
	
	/**
	 * Returns the points of this record:
	 * 
	 * <pre>
	 * victories - defeats
	 * </pre>
	 * 
	 * @return the points of this record
	 */
	public int getPoints() {
		return victories - defeats;
	}
	
	/**
	 * Returns the points progress bar of this record:
	 * 
	 * <pre>
	 * victories - defeats
	 * </pre>
	 * 
	 * @param minPoints min points
	 * @param maxPoints max points
	 * @return the points progress bar of this record
	 */
	public ProgressBarView getPointsBar( final int minPoints, final int maxPoints ) {
		return new ProgressBarView( getPoints() - minPoints, maxPoints - minPoints, toString() );
	}
	
	/**
	 * Returns the win ratio of this record:
	 * 
	 * <pre>
	 * victories / ( victories + defeats )
	 * </pre>
	 * 
	 * @return the win ratio of this record or <code>-1</code> if all games are unknown (and win ratio is not applicable)
	 */
	public double getWinRatio() {
		// If all is unknown, wins+losses=0!
		return victories == 0 && defeats == 0 ? -1 : (double) victories / ( victories + defeats );
	}
	
	/**
	 * Returns the win percent of this record:
	 * 
	 * <pre>
	 * victories / ( victories + defeats )
	 * </pre>
	 * 
	 * @return the win percent of this record
	 */
	public NullAwareComparable< Double > getWinPercent() {
		// If all is unknown, wins+losses=0!
		final double ratio = getWinRatio();
		return NullAwareComparable.getPercent( ratio < -0.1 ? null : ratio * 100 );
	}
	
	/**
	 * Returns the win percent progress bar of this record:
	 * 
	 * <pre>
	 * victories / ( victories + defeats ) * 100
	 * </pre>
	 * 
	 * @param maxWinRatio max win ratio (not percent)
	 * @return the win percent progress bar of this record
	 */
	public ProgressBarView getWinPercentBar( final double maxWinRatio ) {
		final double ratio = getWinRatio();
		return new ProgressBarView( (int) ( ratio * 100_000 ), (int) ( maxWinRatio * 100_000 ), ratio < -0.1 ? "-" : Env.LANG.formatNumber( ratio * 100, 2 )
		        + "%" );
	}
	
	/**
	 * Implements an order of (victories - defeats).
	 * 
	 * <p>
	 * This logic takes the number of played games into consideration contrary to the Win % column. The result order can be used to decide which maps to veto
	 * down for example.
	 * </p>
	 * 
	 * <p>
	 * <b>Example #1:</b> a record of 4-36-0 (win rate = 11%) will be after a record of 1-10-0 (win rate = 10%) reflecting that a map of 4-36 record gives you
	 * more lost ladder points than a map with a record of 1-10.<br>
	 * <b>Example #2:</b> a record of 10-100-0 will be after a record of 1-10-0 even though having the same 10% win rate reflecting that 10-100 record would
	 * give you a lot more lost ladder points.
	 * </p>
	 */
	@Override
	public int compareTo( final Record r ) {
		return getPoints() - r.getPoints();
	}
	
}
