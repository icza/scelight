/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight.util.sc2rep;

import hu.scelight.bean.repfolders.RepFolderBean;
import hu.scelight.service.env.Env;
import hu.sllauncher.gui.icon.LRIcon;
import hu.sllauncher.service.job.ProgressJob;

import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Collections;

/**
 * A basic replay crawler utility job.
 * 
 * @author Andras Belicza
 */
public abstract class RepCrawlerJob extends ProgressJob {
	
	/**
	 * Name of the action the job performs; usually a verb like "crawl" or "count" or "search".<br>
	 * Used by the default implementation of {@link #onError(IOException)} to produce a log message.
	 */
	protected String              actionName;
	
	/** Replay folder to crawl. */
	protected final RepFolderBean repFolderBean;
	
	/** Replay files to crawl. */
	protected final Path[]        repFiles;
	
	
	/**
	 * Creates a new {@link RepCrawlerJob} with a default action name of <code>"crawl"</code>.
	 * 
	 * @param name name of the job
	 * @param ricon job ricon
	 * @param repFolderBean replay folder to crawl
	 */
	public RepCrawlerJob( final String name, final LRIcon ricon, final RepFolderBean repFolderBean ) {
		this( name, ricon, repFolderBean, "crawl" );
	}
	
	/**
	 * Creates a new {@link RepCrawlerJob}.
	 * 
	 * @param name name of the job
	 * @param ricon job ricon
	 * @param repFolderBean replay folder to crawl
	 * @param actionName name of the action the job performs (see {@link #actionName})
	 */
	public RepCrawlerJob( final String name, final LRIcon ricon, final RepFolderBean repFolderBean, final String actionName ) {
		this( name, ricon, repFolderBean, null, actionName );
	}
	
	/**
	 * Creates a new {@link RepCrawlerJob} with a default action name of <code>"crawl"</code>.
	 * 
	 * @param name name of the job
	 * @param ricon job ricon
	 * @param repFiles replay files to crawl
	 */
	public RepCrawlerJob( final String name, final LRIcon ricon, final Path[] repFiles ) {
		this( name, ricon, repFiles, "crawl" );
	}
	
	/**
	 * Creates a new {@link RepCrawlerJob}.
	 * 
	 * @param name name of the job
	 * @param ricon job ricon
	 * @param repFiles replay files to crawl
	 * @param actionName name of the action the job performs (see {@link #actionName})
	 */
	public RepCrawlerJob( final String name, final LRIcon ricon, final Path[] repFiles, final String actionName ) {
		this( name, ricon, null, repFiles, actionName );
	}
	
	/**
	 * Creates a new {@link RepCrawlerJob}.
	 * 
	 * @param name name of the job
	 * @param ricon job ricon
	 * @param repFolderBean replay folder to crawl
	 * @param repFiles replay files to crawl
	 * @param actionName name of the action the job performs (see {@link #actionName})
	 */
	public RepCrawlerJob( final String name, final LRIcon ricon, final RepFolderBean repFolderBean, final Path[] repFiles, final String actionName ) {
		super( name, ricon );
		
		this.repFolderBean = repFolderBean;
		this.repFiles = repFiles;
		this.actionName = actionName;
	}
	
	
	@Override
	public void jobRun() {
		if ( repFolderBean != null )
			if ( !Files.exists( repFolderBean.getPath() ) || !Files.isDirectory( repFolderBean.getPath() ) ) {
				onFolderMissing();
				return;
			}
		
		onStart();
		
		if ( repFolderBean != null ) {
			try {
				Files.walkFileTree( repFolderBean.getPath(), Collections.< FileVisitOption > emptySet(), repFolderBean.getRecursive() ? Integer.MAX_VALUE : 1,
				        new SimpleFileVisitor< Path >() {
					        @Override
					        public FileVisitResult visitFile( final Path file, final BasicFileAttributes attrs ) throws IOException {
						        if ( !mayContinue() )
							        return FileVisitResult.TERMINATE;
						        
						        if ( !attrs.isDirectory() && RepUtils.hasRepExt( file ) )
							        onFoundRepFile( file );
						        
						        return FileVisitResult.CONTINUE;
					        }
				        } );
			} catch ( final IOException ie ) {
				onError( ie );
				return;
			}
		} else {
			for ( final Path repFile : repFiles ) {
				if ( !mayContinue() )
					break;
				
				onFoundRepFile( repFile );
			}
		}
		
		onEnd();
	}
	
	/**
	 * Called when the crawling would begin but the folder to be crawled does not exists or is not a folder (but a file). Crawling will not begin after this
	 * (none of {@link #onStart()} or {@link #onEnd()} will be called).
	 * <p>
	 * This default implementation does nothing.
	 * </p>
	 */
	protected void onFolderMissing() {
		// Subclasses may override this to optionally do something.
	}
	
	/**
	 * Called when the crawling begins.
	 * <p>
	 * This method is only called if the folder to be crawled exists and is a folder (not a file).
	 * </p>
	 * <p>
	 * This default implementation does nothing.
	 * </p>
	 */
	protected void onStart() {
		// Subclasses may override this to optionally do something.
	}
	
	/**
	 * Called when a replay file is found.
	 * 
	 * @param repFile found replay file
	 */
	protected abstract void onFoundRepFile( Path repFile );
	
	/**
	 * Called when an exception is thrown and caught during walking the file tree. Crawling stops after this method ( {@link #onEnd()} will not be called).
	 * <p>
	 * This default implementation simply logs the exception with the current value of {@link #actionName}.
	 * </p>
	 * 
	 * @param ie exception that was thrown and caught
	 */
	protected void onError( final IOException ie ) {
		Env.LOGGER.error( "Could not " + actionName + " replays in folder: " + repFolderBean.getPath(), ie );
	}
	
	/**
	 * Called when the crawling ends.
	 * <p>
	 * This default implementation does nothing.
	 * </p>
	 */
	protected void onEnd() {
		// Subclasses may override this to optionally do something.
	}
	
}
