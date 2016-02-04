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

import hu.scelight.sc2.rep.model.details.Race;
import hu.scelight.service.env.Env;
import hu.sllauncher.gui.comp.table.renderer.BarCodeView;
import hu.sllauncher.gui.comp.table.renderer.ProgressBarView;

import java.awt.Color;

/**
 * Describes / models stats of a Player. Contains aggregate stats.
 * 
 * @param <T> type of the object this stats object is for
 * @author Andras Belicza
 */
public class GeneralStats< T extends Comparable< T > > extends Stats {
	
	/** Colors representing the races Protoss, Terran, Zerg. */
	private static final Color[] COLORS        = { Race.PROTOSS.color, Race.TERRAN.color, Race.ZERG.color };
	
	/** Darker colors representing the races Protoss, Terran, Zerg. */
	private static final Color[] DARKER_COLORS = { Race.PROTOSS.darkerColor, Race.TERRAN.darkerColor, Race.ZERG.darkerColor };
	
	
	/** Object this general stats is for. */
	public final T               obj;
	
	
	/** Total actions for APM calculation. */
	public long                  totalApmActions;
	
	/** Total actions for SPM calculation. */
	public long                  totalSpmActions;
	
	/** Total time for per minute calculations in milliseconds. */
	public long                  totalPerMinMs;
	
	/** Total weighted SQ for SQ calculation. */
	public long                  totalWeightedSq;
	
	/** Total weighted supply-capped percent for supply-capped percent calculation. */
	public double                totalWeightedSCPercent;
	
	/** Total last cmd loops for weighted average calculation (SQ, supply-capped). */
	public long                  totalLastCmdLoops;
	
	
	/** Protoss count. */
	public int                   protoss;
	
	/** Terran count. */
	public int                   terran;
	
	/** Zerg count. */
	public int                   zerg;
	
	
	/** Record of the player. */
	public final Record          record        = new Record();
	
	
	/**
	 * Creates a new {@link GeneralStats}.
	 * 
	 * @param obj object this general stats is for
	 */
	public GeneralStats( final T obj ) {
		this.obj = obj;
	}
	
	/**
	 * Updates the general stats with the specified {@link Part} and {@link Game}.
	 * 
	 * @param part participant
	 * @param game game
	 */
	public void updateWithPartGame( final Part part, final Game game ) {
		updateWithPartGame( part, game, part.lengthMs );
	}
	
	/**
	 * Updates the general stats with the specified {@link Part} and {@link Game}.
	 * 
	 * @param part participant
	 * @param game game
	 * @param timePlayedMs time played in milliseconds to add to the total
	 */
	public void updateWithPartGame( final Part part, final Game game, final long timePlayedMs ) {
		// Time played by the participant
		updateWithGame( game, timePlayedMs );
		
		totalApmActions += part.apmActions;
		totalSpmActions += part.spmActions;
		totalPerMinMs += part.perMinMs;
		
		if ( game.hasTrackerInfo ) {
			totalWeightedSq += part.sq * part.lastCmdLoop;
			totalWeightedSCPercent += part.supplyCappedPercent * part.lastCmdLoop;
			totalLastCmdLoops += part.lastCmdLoop;
		}
		
		switch ( part.race ) {
			case PROTOSS :
				protoss++;
				break;
			case TERRAN :
				terran++;
				break;
			case ZERG :
				zerg++;
				break;
			default :
				break;
		}
		
		record.updateWithResult( part.result );
	}
	
	/**
	 * Returns the weighted average APM.
	 * 
	 * @return the weighted average APM
	 */
	public int getAvgApm() {
		return (int) ( totalPerMinMs == 0 ? 0 : totalApmActions * 60_000L / totalPerMinMs );
	}
	
	/**
	 * Returns the weighted average APM progress bar.
	 * 
	 * @param maxApm max average APM
	 * @return the weighted average APM progress bar
	 */
	public ProgressBarView getAvgApmBar( final int maxApm ) {
		return new ProgressBarView( getAvgApm(), maxApm );
	}
	
	/**
	 * Returns the weighted average SPM.
	 * 
	 * @return the weighted average SPM
	 */
	public double getAvgSpm() {
		return totalPerMinMs == 0 ? 0 : totalSpmActions * 60_000.0 / totalPerMinMs;
	}
	
	/**
	 * Returns the weighted average SPM progress bar.
	 * 
	 * @param maxSpm max average SPM
	 * @return the weighted average SPM progress bar
	 */
	public ProgressBarView getAvgSpmBar( final double maxSpm ) {
		final double avgSpm = getAvgSpm();
		return new ProgressBarView( (int) ( avgSpm * 100 ), (int) ( maxSpm * 100 ), Env.LANG.formatNumber( avgSpm, 2 ) );
	}
	
	/**
	 * Returns the weighted average SQ.
	 * 
	 * @return the weighted average SQ
	 */
	public int getAvgSq() {
		return totalLastCmdLoops == 0 ? 0 : (int) Math.round( (double) totalWeightedSq / totalLastCmdLoops );
	}
	
	/**
	 * Returns the weighted average SQ progress bar.
	 * 
	 * @param maxSq max average SQ
	 * @return the weighted average SQ progress bar
	 */
	public ProgressBarView getAvgSqBar( final int maxSq ) {
		return new ProgressBarView( getAvgSq(), maxSq );
	}
	
	/**
	 * Returns the weighted average supply capped percent.
	 * 
	 * @return the weighted average supply capped percent
	 */
	public double getAvgSupplyCappedPercent() {
		return totalLastCmdLoops == 0 ? 0 : totalWeightedSCPercent / totalLastCmdLoops;
	}
	
	/**
	 * Returns the weighted average supply capped percent progress bar.
	 * 
	 * @param maxScp max average supply capped percent
	 * @return the weighted average supply capped percent progress bar
	 */
	public ProgressBarView getAvgSupplyCappedPercentBar( final double maxScp ) {
		final double avgScp = getAvgSupplyCappedPercent();
		return new ProgressBarView( (int) ( avgScp * 100 ), (int) ( maxScp * 100 ), Env.LANG.formatNumber( avgScp, 2 ) );
	}
	
	/**
	 * Returns the races played percent string.
	 * 
	 * @return the races played percent string
	 */
	public String getRacesString() {
		final int total_ = protoss + terran + zerg;
		if ( total_ == 0 )
			return null;
		
		final float total = total_;
		
		final StringBuilder sb = new StringBuilder();
		if ( protoss > 0 )
			sb.append( "P:" ).append( Math.round( protoss * 100 / total ) ).append( '%' );
		if ( terran > 0 ) {
			if ( sb.length() > 0 )
				sb.append( ", " );
			sb.append( "T:" ).append( Math.round( terran * 100 / total ) ).append( '%' );
		}
		if ( zerg > 0 ) {
			if ( sb.length() > 0 )
				sb.append( ", " );
			sb.append( "Z:" ).append( Math.round( zerg * 100 / total ) ).append( '%' );
		}
		
		return sb.toString();
	}
	
	/**
	 * Returns the races bar code.
	 * 
	 * @return the races bar code
	 */
	public BarCodeView getRacesBarCode() {
		return new BarCodeView( new int[] { protoss, terran, zerg }, COLORS, DARKER_COLORS, getRacesString() );
	}
	
}
