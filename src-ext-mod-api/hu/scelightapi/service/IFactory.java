/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelightapi.service;

import hu.scelightapi.bean.repfilters.IRepFilterBean;
import hu.scelightapi.bean.repfilters.IRepFiltersBean;
import hu.scelightapi.gui.dialog.IRepFiltersEditorDialog;
import hu.scelightapi.sc2.rep.model.details.IToon;
import hu.scelightapi.sc2.rep.repproc.ISelectionTracker;
import hu.scelightapi.search.IRepSearchEngine;
import hu.scelightapi.template.IInvalidTemplateException;
import hu.scelightapi.template.ITemplateEngine;
import hu.scelightapi.util.IVersionView;
import hu.scelightapi.util.httppost.IFileProvider;
import hu.scelightapi.util.httppost.IHttpPost;
import hu.scelightapibase.bean.IVersionBean;
import hu.scelightapibase.gui.icon.IRIcon;
import hu.scelightapibase.service.job.IJob;
import hu.scelightapibase.service.job.IProgressJob;
import hu.scelightapibase.util.IControlledThread;
import hu.scelightapibase.util.IDateFormat;
import hu.scelightapibase.util.IDateValue;
import hu.scelightapibase.util.IDurationFormat;
import hu.scelightapibase.util.IDurationValue;
import hu.scelightapibase.util.IHolder;
import hu.scelightapibase.util.INormalThread;
import hu.scelightapibase.util.IPair;
import hu.scelightapibase.util.IRelativeDate;
import hu.scelightapibase.util.ISizeFormat;
import hu.scelightapibase.util.ISizeValue;
import hu.scelightapibase.util.IUrlBuilder;

import java.net.URL;
import java.nio.file.Path;
import java.util.Date;
import java.util.Map;

/**
 * General factory interface.
 * 
 * <p>
 * Used to create new instances of certain API interfaces.
 * </p>
 * 
 * @author Andras Belicza
 * 
 * @see IGuiFactory
 */
public interface IFactory {
	
	/**
	 * Creates a new {@link IUrlBuilder} from the specified base {@link URL}.
	 * 
	 * @param url base URL to extend, may contain a query part but must not contain a reference part
	 * @return a new {@link IUrlBuilder}
	 * @throws IllegalArgumentException if the specified URL contains a reference part
	 * @see #newHttpPost(URL, Map)
	 */
	IUrlBuilder newUrlBuilder( URL url ) throws IllegalArgumentException;
	
	/**
	 * Creates a new {@link IHttpPost}.
	 * 
	 * @param url {@link URL} to post to
	 * @param paramsMap map of parameters to be sent
	 * @return an {@link IHttpPost} to interact with the HTTP POST request
	 * @see IHttpPost
	 * @see #newUrlBuilder(URL)
	 */
	IHttpPost newHttpPost( URL url, Map< String, String > paramsMap );
	
	/**
	 * Creates a new {@link IFileProvider} which always returns the specified file and last modified time.
	 * 
	 * @param file the provided file
	 * @param lastModified last modified date of the provided file
	 * @return an {@link IFileProvider} which always returns the specified file and last modified time
	 * @see IHttpPost
	 */
	IFileProvider newSimpleFileProvider( Path file, Long lastModified );
	
	/**
	 * Creates a new {@link IVersionView} from the specified parts.
	 * 
	 * @param parts parts of the version
	 * @return a new {@link IVersionView} from the specified parts
	 * @see IVersionView
	 * @see #newVersionView(String)
	 */
	IVersionView newVersionView( int... parts );
	
	/**
	 * Creates a new {@link IVersionView} from the specified version string.
	 * 
	 * @param version version string to create a version view from
	 * @return a new {@link IVersionView} from the specified version string, or <code>null</code> if the specified string is not a valid version string
	 * @see IVersionView
	 * @see #newVersionView(int...)
	 */
	IVersionView newVersionView( String version );
	
	/**
	 * Creates a new {@link IToon} from the specified toon string.
	 * 
	 * <p>
	 * The format of the toon string is exactly as specified by {@link IToon#toString()}.
	 * </p>
	 * 
	 * @param toon toon string to create the {@link IToon} from
	 * @return a new {@link IToon} from the specified toon string
	 * @throws IllegalArgumentException if the specified toon string is invalid
	 * @see IToon
	 * @see #newToon(String, boolean)
	 */
	IToon newToon( String toon ) throws IllegalArgumentException;
	
	/**
	 * Creates a new {@link IToon} from the specified toon string.
	 * 
	 * <p>
	 * The format of the toon string is exactly as specified by {@link IToon#toString()}.
	 * </p>
	 * 
	 * @param toon toon string to create the {@link IToon} from
	 * @param parsePlayerName tells if player name is to be parsed from the toon string if present
	 * @return a new {@link IToon} from the specified toon string
	 * @throws IllegalArgumentException if the specified toon string is invalid
	 * @since 1.4
	 * @see IToon
	 * @see #newToon(String)
	 */
	IToon newToon( String toon, boolean parsePlayerName ) throws IllegalArgumentException;
	
	/**
	 * Creates and a new {@link INormalThread} with the specified name which executes the specified runnable.
	 * 
	 * <p>
	 * The returned normal thread is not started. It has to be started with the {@link INormalThread#start()} method.
	 * </p>
	 * 
	 * @param name name of the thread
	 * @param runnable runnable to execute in the returned normal thread
	 * @return a new {@link INormalThread}
	 * @see INormalThread
	 * @see #newControlledThread(String, Runnable)
	 */
	INormalThread newNormalThread( String name, Runnable runnable );
	
	/**
	 * Creates a new {@link IControlledThread} with the specified name which executes the specified runnable.
	 * 
	 * <p>
	 * The returned controlled thread is not started. It has to be started with the {@link IControlledThread#start()} method.
	 * </p>
	 * 
	 * @param name name of the thread
	 * @param runnable runnable to execute in the returned controlled thread
	 * @return a new {@link IControlledThread}
	 * @see IControlledThread
	 * @see #newNormalThread(String, Runnable)
	 */
	IControlledThread newControlledThread( String name, Runnable runnable );
	
	/**
	 * Creates a new {@link IJob} with the specified name, {@link IRIcon} and which executes the specified runnable.
	 * 
	 * <p>
	 * The returned job is not started. It has to be started with the {@link IJob#start()} method.
	 * </p>
	 * 
	 * @param name name of the job
	 * @param ricon ricon of the job
	 * @param runnable runnable to execute in the returned job
	 * @return a new {@link IJob}
	 * 
	 * @see IJob
	 * @see #newProgressJob(String, IRIcon, Runnable)
	 * @see IGuiFactory#newRIcon(URL)
	 */
	IJob newJob( String name, IRIcon ricon, Runnable runnable );
	
	/**
	 * Creates a new {@link IProgressJob} with the specified name, {@link IRIcon} and which executes the specified runnable.
	 * 
	 * <p>
	 * The returned progress job is not started. It has to be started with the {@link IProgressJob#start()} method.
	 * </p>
	 * 
	 * @param name name of the progress job
	 * @param ricon ricon of the progress job
	 * @param runnable runnable to execute in the returned progress job
	 * @return a new {@link IProgressJob}
	 * 
	 * @see IProgressJob
	 * @see #newJob(String, IRIcon, Runnable)
	 * @see IGuiFactory#newRIcon(URL)
	 */
	IProgressJob newProgressJob( String name, IRIcon ricon, Runnable runnable );
	
	/**
	 * Returns the version bean of the template engine.
	 * 
	 * @return the version bean of the template engine
	 */
	IVersionBean getTemplateEngineVersionBean();
	
	/**
	 * Creates a new {@link ITemplateEngine}.
	 * 
	 * <p>
	 * This method can also be used to validate a template string. If no exception is thrown, the template is valid.
	 * </p>
	 * 
	 * @param template template string
	 * @return a new {@link ITemplateEngine}
	 * @throws IInvalidTemplateException if the specified template is invalid
	 * 
	 * @see ITemplateEngine
	 */
	ITemplateEngine newTemplateEngine( String template ) throws IInvalidTemplateException;
	
	/**
	 * Creates a new {@link IRepFiltersBean}.
	 * 
	 * @return a new {@link IRepFiltersBean}
	 * 
	 * @see IRepFiltersBean
	 * @see IRepFiltersEditorDialog
	 * @see #newRepFilterBean()
	 */
	IRepFiltersBean newRepFiltersBean();
	
	/**
	 * Creates a new {@link IRepFilterBean} and initializes it with default values.
	 * 
	 * @return a new {@link IRepFilterBean}
	 * 
	 * @see IRepFilterBean
	 * @see IRepFiltersEditorDialog
	 * @see #newRepFiltersBean()
	 */
	IRepFilterBean newRepFilterBean();
	
	/**
	 * Creates a new {@link IRepFiltersEditorDialog}.
	 * 
	 * <p>
	 * The dialog is made visible immediately and this method only returns when the dialog is already closed. Whether filters were edited can be checked with
	 * the {@link IRepFiltersEditorDialog#isOk()} method, and the edited filters can be retrieved with the {@link IRepFiltersEditorDialog#getRepFiltersBean()}
	 * (which may or may not be the same as the passed filters even if you specified it).
	 * </p>
	 * 
	 * @param repFiltersBean optional replay filters to be viewed / edited in the dialog; if <code>null</code>, a new {@link IRepFiltersBean} will be created
	 * @return a new {@link IRepFiltersEditorDialog}
	 * 
	 * @see IRepFiltersEditorDialog
	 * @see IRepFiltersBean
	 * @see IRepFilterBean
	 */
	IRepFiltersEditorDialog newRepFiltersEditorDialog( IRepFiltersBean repFiltersBean );
	
	/**
	 * Returns the version of the replay search engine.
	 * 
	 * @return the version of the replay search engine
	 */
	IVersionBean getRepSearchEngineVersionBean();
	
	/**
	 * Creates a new {@link IRepSearchEngine}.
	 * 
	 * @param repFiltersBean replay filters bean to search / filter by
	 * @return a new {@link IRepSearchEngine}
	 * 
	 * @see IRepSearchEngine
	 * @see IRepFiltersEditorDialog
	 * @see #newRepFiltersBean()
	 * @see #newRepFilterBean()
	 */
	IRepSearchEngine newRepSearchEngine( IRepFiltersBean repFiltersBean );
	
	/**
	 * Creates a new IMMUTABLE {@link IDurationValue} with {@link IDurationFormat#AUTO} format hint.
	 * 
	 * @param value time duration value in milliseconds
	 * @return a new {@link IDurationValue}
	 * 
	 * @see IDurationValue
	 * @see #newDurationValue(long, IDurationFormat)
	 */
	IDurationValue newDurationValue( long value );
	
	/**
	 * Creates a new IMMUTABLE {@link IDurationValue}.
	 * 
	 * @param value time duration value in milliseconds
	 * @param durationFormat duration format hint for renderers
	 * @return a new {@link IDurationValue}
	 * 
	 * @see IDurationValue
	 * @see IDurationFormat
	 */
	IDurationValue newDurationValue( long value, IDurationFormat durationFormat );
	
	/**
	 * Creates a new IMMUTABLE {@link ISizeValue} with {@link ISizeFormat#AUTO} format hint.
	 * 
	 * @param value size value in bytes
	 * @return a new {@link ISizeValue}
	 * 
	 * @see ISizeValue
	 * @see #newSizeValue(long, ISizeFormat)
	 */
	ISizeValue newSizeValue( long value );
	
	/**
	 * Creates a new IMMUTABLE {@link ISizeValue}.
	 * 
	 * @param value size value in bytes
	 * @param sizeFormat size format hint for renderers
	 * @return a new {@link ISizeValue}
	 * 
	 * @see ISizeValue
	 * @see ISizeFormat
	 */
	ISizeValue newSizeValue( long value, ISizeFormat sizeFormat );
	
	/**
	 * Creates a new IMMUTABLE {@link IDateValue} with {@link IDateFormat#DATE_TIME} format hint.
	 * 
	 * @param value date value
	 * @return a new {@link IDateValue}
	 * 
	 * @since 1.1
	 * 
	 * @see IDateValue
	 * @see #newDateValue(Date, IDateFormat)
	 */
	IDateValue newDateValue( Date value );
	
	/**
	 * Creates a new IMMUTABLE {@link IDateValue}.
	 * 
	 * @param value date value
	 * @param dateFormat date format hint for renderers
	 * @return a new {@link IDateValue}
	 * 
	 * @since 1.1
	 * 
	 * @see IDateValue
	 * @see IDateFormat
	 */
	IDateValue newDateValue( Date value, IDateFormat dateFormat );
	
	/**
	 * Returns a new {@link IHolder}
	 * 
	 * @param <T> type of the held object
	 * @return a new {@link IHolder}
	 * 
	 * @see IHolder
	 */
	< T > IHolder< T > newHolder();
	
	/**
	 * Returns a new {@link IHolder}
	 * 
	 * @param <T> type of the held object
	 * @param value the reference to be set as the held object
	 * @return a new {@link IHolder}
	 * 
	 * @see IHolder
	 */
	< T > IHolder< T > newHolder( T value );
	
	/**
	 * Creates a new IMMUTABLE {@link IRelativeDate}.
	 * 
	 * @param date absolute date to be represented relatively to the current time
	 * @return a new {@link IRelativeDate}
	 * 
	 * @see IRelativeDate
	 * @see #newRelativeDate(long)
	 * @see #newRelativeDate(Date, boolean, int, boolean)
	 * @see #newRelativeDate(long, boolean, int, boolean)
	 */
	IRelativeDate newRelativeDate( Date date );
	
	/**
	 * Creates a new IMMUTABLE {@link IRelativeDate}.
	 * 
	 * @param date absolute date to be represented relatively to the current time
	 * @return a new {@link IRelativeDate}
	 * 
	 * @see IRelativeDate
	 * @see #newRelativeDate(Date)
	 * @see #newRelativeDate(Date, boolean, int, boolean)
	 * @see #newRelativeDate(long, boolean, int, boolean)
	 */
	IRelativeDate newRelativeDate( long date );
	
	/**
	 * Creates a new IMMUTABLE {@link IRelativeDate}.
	 * 
	 * See {@link IRelativeDate#toString(boolean, int, boolean)} for details.
	 * 
	 * @param date absolute date to be represented relatively to the current time
	 * @param longForm tells if long form is to be used for time units
	 * @param tokens tells how many tokens to include
	 * @param appendEra tells if era is to be appended; era is <code>"ago"</code> for past times and <code>"from now"</code> for future times
	 * @return a new {@link IRelativeDate}
	 * 
	 * @see IRelativeDate
	 * @see #newRelativeDate(long, boolean, int, boolean)
	 * @see IRelativeDate#toString(boolean, int, boolean)
	 */
	IRelativeDate newRelativeDate( Date date, boolean longForm, int tokens, boolean appendEra );
	
	/**
	 * Creates a new IMMUTABLE {@link IRelativeDate}.
	 * 
	 * See {@link IRelativeDate#toString(boolean, int, boolean)} for details.
	 * 
	 * @param date absolute date to be represented relatively to the current time
	 * @param longForm tells if long form is to be used for time units
	 * @param tokens tells how many tokens to include
	 * @param appendEra tells if era is to be appended; era is <code>"ago"</code> for past times and <code>"from now"</code> for future times
	 * @return a new {@link IRelativeDate}
	 * 
	 * @see IRelativeDate
	 * @see #newRelativeDate(Date, boolean, int, boolean)
	 * @see IRelativeDate#toString(boolean, int, boolean)
	 */
	IRelativeDate newRelativeDate( long date, boolean longForm, int tokens, boolean appendEra );
	
	/**
	 * Creates a new IMMUTABLE {@link IPair}.
	 * 
	 * @param <T1> type of the first object
	 * @param <T2> type of the second object
	 * @param value1 the first object
	 * @param value2 the second object
	 * @return a new {@link IPair}
	 * 
	 * @see IPair
	 */
	< T1, T2 > IPair< T1, T2 > newPair( T1 value1, T2 value2 );
	
	/**
	 * Creates a new {@link ISelectionTracker}.
	 * 
	 * @return a new {@link ISelectionTracker}
	 * 
	 * @since 1.3
	 * 
	 * @see ISelectionTracker
	 */
	ISelectionTracker newSelectionTracker();
	
}
