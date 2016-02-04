/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelightapibase.bean.settings.type.viewhints;

import hu.scelightapibase.bean.settings.type.ISetting;
import hu.scelightapibase.gui.icon.IRIcon;
import hu.scelightapibase.util.IRHtml;

/**
 * {@link IViewHints} builder class.
 * 
 * <p>
 * {@link IViewHints} with multiple properties can be built by calling the short setter methods for each property (they return the builder so this can be
 * chained), and finally calling the {@link #build()} method to obtain the {@link IViewHints} reference like this: <blockquote>
 * 
 * <pre>
 * IViewHints vh = services.getSettingsGui().newVHB().sstext( &quot;pixel&quot; ).rhtml( helpRes ).build();
 * </pre>
 * 
 * </blockquote>
 * </p>
 * 
 * @author Andras Belicza
 * 
 * @see hu.scelightapi.gui.setting.ISettingsUtils#newVHB()
 */
public interface IVHB {
	
	/**
	 * Sets the setting ricon.
	 * 
	 * @param ricon setting ricon to be set
	 * @return <code>this</code> for chaining
	 */
	IVHB ricon( IRIcon ricon );
	
	/**
	 * Sets the subsequent text of the setting component.
	 * 
	 * @param subsequentText subsequent text of the setting component to be set
	 * @return <code>this</code> for chaining
	 */
	IVHB sstext( String subsequentText );
	
	/**
	 * Sets the help HTML resource of the setting.
	 * 
	 * @param helpRhtml help HTML resource of the setting to be set
	 * @return <code>this</code> for chaining
	 */
	IVHB help( IRHtml helpRhtml );
	
	/**
	 * Sets the dialog title.
	 * 
	 * @param dialogTitle dialog title to be set
	 * @return <code>this</code> for chaining
	 */
	IVHB dtitle( String dialogTitle );
	
	/**
	 * Sets the custom subsequent component factory.
	 * 
	 * @param ssCompFactory custom subsequent component factory to be set
	 * @return <code>this</code> for chaining
	 */
	IVHB ssCompFactory( ISsCompFactory< ? extends ISetting< ? > > ssCompFactory );
	
	/**
	 * Sets the setting component configurer.
	 * 
	 * @param compConfigurer setting component configurer to be set
	 * @return <code>this</code> for chaining
	 */
	IVHB compConfigurer( ICompConfigurer compConfigurer );
	
	/**
	 * Sets the edit registration requirement to <code>true</code>.
	 * 
	 * @return <code>this</code> for chaining
	 */
	IVHB reqReg();
	
	/**
	 * Sets the rows count to be used for the setting component if supported.
	 * 
	 * @param rows rows count to be set
	 * @return <code>this</code> for chaining
	 * 
	 * @since 1.4
	 */
	IVHB rows( Integer rows );
	
	/**
	 * Sets the columns count to be used for the setting component if supported.
	 * 
	 * @param columns columns count to be set
	 * @return <code>this</code> for chaining
	 * 
	 * @since 1.4
	 */
	IVHB cols( Integer columns );
	
	
	/**
	 * Builds and returns the {@link IViewHints}.
	 * 
	 * @return the {@link IViewHints} from the configured parameters
	 */
	IViewHints build();
	
}
