/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.sllauncher.bean.settings.type.viewhints;

import hu.scelightapibase.bean.settings.type.ISetting;
import hu.scelightapibase.bean.settings.type.viewhints.ICompConfigurer;
import hu.scelightapibase.bean.settings.type.viewhints.ISsCompFactory;
import hu.scelightapibase.bean.settings.type.viewhints.IVHB;
import hu.scelightapibase.gui.icon.IRIcon;
import hu.scelightapibase.util.IRHtml;
import hu.sllauncher.bean.settings.type.MultilineStringSetting;
import hu.sllauncher.bean.settings.type.PathSetting;
import hu.sllauncher.bean.settings.type.StringSetting;
import hu.sllauncher.bean.settings.type.ValidatedStringSetting;

/**
 * {@link ViewHints} builder class.
 * 
 * <p>
 * {@link ViewHints} with only one property can easily be built using the static methods like {@link #sstext_(String)} or {@link #help_(IRHtml)}.
 * </p>
 * <p>
 * {@link ViewHints} with multiple properties can be built by instantiating this builder and calling the short setter methods for each property (they return the
 * builder so this can be chained), and finally calling the {@link #build()} method to obtain the {@link ViewHints} reference like this: <blockquote>
 * 
 * <pre>
 * ViewHints vh = new VHB().sstext( &quot;pixel&quot; ).rhtml( helpRes ).build();
 * </pre>
 * 
 * </blockquote>
 * </p>
 * 
 * @author Andras Belicza
 */
public class VHB implements IVHB {
	
	/**
	 * Returns a {@link ViewHints} having a subsequent text only.
	 * 
	 * @param subsequentText subsequent text of the setting component to be set
	 * @return a {@link ViewHints} having a subsequent text only
	 */
	public static ViewHints sstext_( final String subsequentText ) {
		return new VHB().sstext( subsequentText ).build();
	}
	
	/**
	 * Returns a {@link ViewHints} having help HTML resource only.
	 * 
	 * @param rhtml help HTML resource of the setting to be set
	 * @return a {@link ViewHints} having help HTML resource only
	 */
	public static ViewHints help_( final IRHtml rhtml ) {
		return new VHB().help( rhtml ).build();
	}
	
	/**
	 * Returns a {@link ViewHints} having a dialog title only.
	 * 
	 * @param dialogTitle dialog title to be set
	 * @return a {@link ViewHints} having a dialog title
	 */
	public static ViewHints dtitle_( final String dialogTitle ) {
		return new VHB().dtitle( dialogTitle ).build();
	}
	
	/**
	 * Returns a {@link ViewHints} having a custom subsequent component factory only.
	 * 
	 * @param ssCompFactory subsequent component factory to be set
	 * @return a {@link ViewHints} having a custom subsequent component factory only
	 */
	public static ViewHints ssCompFactory_( final ISsCompFactory< ? extends ISetting< ? > > ssCompFactory ) {
		return new VHB().ssCompFactory( ssCompFactory ).build();
	}
	
	/**
	 * Returns a {@link ViewHints} having a setting component configurer only.
	 * 
	 * @param compConfigurer setting component configurer to be set
	 * @return a {@link ViewHints} having a custom subsequent component factory only
	 */
	public static ViewHints compConfigurer_( final ICompConfigurer compConfigurer ) {
		return new VHB().compConfigurer( compConfigurer ).build();
	}
	
	/**
	 * Returns a {@link ViewHints} having a rows only.
	 * 
	 * @param rows rows to be set
	 * @return a {@link ViewHints} having a rows
	 */
	public static ViewHints rows_( final Integer rows ) {
		return new VHB().rows( rows ).build();
	}
	
	/**
	 * Returns a {@link ViewHints} having a columns only.
	 * 
	 * @param columns columns to be set
	 * @return a {@link ViewHints} having a columns
	 */
	public static ViewHints cols_( final Integer columns ) {
		return new VHB().cols( columns ).build();
	}
	
	/**
	 * Returns a {@link ViewHints} having the edit registration requirement set to <code>true</code>.
	 * 
	 * @return a {@link ViewHints} having the edit registration requirement set to <code>true</code>.
	 */
	public static ViewHints reqReg_() {
		return new VHB().reqReg().build();
	}
	
	
	
	/** Setting ricon. */
	private IRIcon                                    ricon;
	
	/** Subsequent text of the setting component, usually an unit name (e.g. "pixels" or "sec"). */
	private String                                    subsequentText;
	
	/** Help HTML resource of the setting. */
	private IRHtml                                    helpRhtml;
	
	/** Dialog title (used by {@link PathSetting}). */
	private String                                    dialogTitle;
	
	/** Custom subsequent component factory. */
	private ISsCompFactory< ? extends ISetting< ? > > ssCompFactory;
	
	/** Setting component configurer. */
	private ICompConfigurer                           compConfigurer;
	
	/** Tells if editing the setting requires registration. */
	private boolean                                   editRequiresRegistration;
	
	/**
	 * Rows count to be used for the setting component if supported. Used for {@link MultilineStringSetting} and
	 * {@link hu.scelight.bean.settings.type.ValidatedMultilineStringSetting}.
	 */
	private Integer                                   rows;
	
	/**
	 * Columns count to be used for the setting component if supported. Used for {@link StringSetting}, {@link PathSetting}, {@link ValidatedStringSetting},
	 * {@link hu.scelight.bean.settings.type.TemplateSetting}, {@link MultilineStringSetting} and
	 * {@link hu.scelight.bean.settings.type.ValidatedMultilineStringSetting}.
	 */
	private Integer                                   columns;
	
	
	@Override
	public VHB ricon( final IRIcon ricon ) {
		this.ricon = ricon;
		return this;
	}
	
	@Override
	public VHB sstext( final String subsequentText ) {
		this.subsequentText = subsequentText;
		return this;
	}
	
	@Override
	public VHB help( final IRHtml helpRhtml ) {
		this.helpRhtml = helpRhtml;
		return this;
	}
	
	@Override
	public VHB dtitle( final String dialogTitle ) {
		this.dialogTitle = dialogTitle;
		return this;
	}
	
	@Override
	public VHB ssCompFactory( final ISsCompFactory< ? extends ISetting< ? > > ssCompFactory ) {
		this.ssCompFactory = ssCompFactory;
		return this;
	}
	
	@Override
	public VHB compConfigurer( final ICompConfigurer compConfigurer ) {
		this.compConfigurer = compConfigurer;
		return this;
	}
	
	@Override
	public VHB reqReg() {
		editRequiresRegistration = true;
		return this;
	}
	
	@Override
	public VHB rows( final Integer rows ) {
		this.rows = rows;
		return this;
	}
	
	@Override
	public VHB cols( final Integer columns ) {
		this.columns = columns;
		return this;
	}
	
	
	@Override
	@SuppressWarnings( "unchecked" )
	public ViewHints build() {
		return new ViewHints( ricon, subsequentText, helpRhtml, dialogTitle, (ISsCompFactory< ISetting< ? > >) ssCompFactory, compConfigurer,
		        editRequiresRegistration, rows, columns );
	}
	
}
