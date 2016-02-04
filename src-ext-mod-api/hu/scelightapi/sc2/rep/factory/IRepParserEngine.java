/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelightapi.sc2.rep.factory;

import hu.scelightapi.sc2.rep.model.IReplay;
import hu.scelightapi.sc2.rep.repproc.IRepProcessor;
import hu.scelightapibase.bean.IVersionBean;

import java.nio.file.Path;

/**
 * Replay parser engine interface to parse replays or load/cache replay processors.
 * 
 * @author Andras Belicza
 * 
 * @see IReplay
 * @see IRepProcessor
 */
public interface IRepParserEngine {
	
	/**
	 * Returns the version bean of the replay parser engine.
	 * 
	 * @return the version bean of the replay parser engine.
	 */
	IVersionBean getRepParserEngineVersionBean();
	
	/**
	 * Returns the version bean of the replay processor.
	 * 
	 * @return the version bean of the replay processor
	 */
	IVersionBean getRepProcessorVersionBean();
	
	/**
	 * Returns the version bean of the replay processor cache.
	 * 
	 * @return the version bean of the replay processor cache
	 */
	IVersionBean getRepProcCacheVersionBean();
	
	
	/**
	 * Parses the specified replay file and returns an {@link IReplay} object.
	 * 
	 * <p>
	 * If the complete game events and tracker events of the replay are not required, the preferred way to "parse" the replay is {@link #getRepProc(Path)}
	 * because it is significantly faster, the difference is order of magnitude!
	 * </p>
	 * 
	 * @param file replay file to be parsed
	 * @return the constructed {@link IReplay} object or <code>null</code> if the replay cannot be parsed
	 * 
	 * @see IReplay
	 * @see #getRepProc(Path)
	 * @see #parseAndWrapReplay(Path)
	 */
	IReplay parseReplay( Path file );
	
	/**
	 * Parses the specified replay file and returns a wrapper {@link IRepProcessor} object.
	 * 
	 * <p>
	 * If the complete game events and tracker events of the replay are not required, the preferred way to "parse" the replay is {@link #getRepProc(Path)}
	 * because it is significantly faster, the difference is order of magnitude!
	 * </p>
	 * 
	 * @param file replay file to be parsed
	 * @return a wrapper {@link IRepProcessor} of the constructed {@link IReplay} object or <code>null</code> if the replay cannot be parsed
	 * 
	 * @see IReplay
	 * @see #getRepProc(Path)
	 */
	IRepProcessor parseAndWrapReplay( Path file );
	
	/**
	 * Returns an {@link IRepProcessor} constructed and initialized from the replay processor's cache for the specified replay file if it is cached, and if not,
	 * it will be parsed and cached first, then returned.
	 * 
	 * <p>
	 * If the complete game events and tracker events of the replay are not required, this is the preferred way to "parse" the replay as opposed to
	 * {@link #parseReplay(Path)} because this method is significantly faster, the difference is order of magnitude!
	 * </p>
	 * 
	 * @param file replay file whose {@link IRepProcessor} object to return
	 * @return an {@link IRepProcessor} preferably constructed and initialized from the replay processor's cache or <code>null</code> if the replay cannot be
	 *         parsed
	 * 
	 * @see IRepProcessor
	 * @see #parseReplay(Path)
	 * @see #parseAndWrapReplay(Path)
	 */
	IRepProcessor getRepProc( Path file );
	
}
