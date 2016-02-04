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
import hu.scelightapibase.bean.settings.type.viewhints.IViewHints;
import hu.scelightapibase.gui.icon.IRIcon;
import hu.scelightapibase.util.IRHtml;
import hu.sllauncher.bean.settings.type.MultilineStringSetting;
import hu.sllauncher.bean.settings.type.NodeSetting;
import hu.sllauncher.bean.settings.type.PathSetting;
import hu.sllauncher.bean.settings.type.StringSetting;
import hu.sllauncher.bean.settings.type.ValidatedStringSetting;
import hu.sllauncher.gui.setting.LSettingsNodePage;

/**
 * Non-required properties intended for the Settings view dialog to customize the setting's view/edit components.
 * 
 * <p>
 * Instances are IMMUTABLE. For easy creation of instances, see the {@link VHB View hints builder} class.
 * </p>
 * 
 * @author Andras Belicza
 * 
 * @see VHB
 */
public class ViewHints implements IViewHints {
	
	/**
	 * Setting ricon, used for {@link NodeSetting}s as the icon in the navigation tree and in the {@link LSettingsNodePage} title.
	 */
	public final IRIcon                          ricon;
	
	/**
	 * Subsequent text of the setting component, usually a measurement unit name (e.g. "pixel" or "sec") but can be any other text (e.g. a note that changing
	 * this requires restart).
	 */
	public final String                          subsequentText;
	
	/** Help HTML resource of the setting. */
	public final IRHtml                          helpRhtml;
	
	/** Dialog title (used by settings whose editor component might open a dialog, e.g. {@link PathSetting}). */
	public final String                          dialogTitle;
	
	/** Custom subsequent component factory. */
	public final ISsCompFactory< ISetting< ? > > ssCompFactory;
	
	/** Setting component configurer. */
	public final ICompConfigurer                 compConfigurer;
	
	/** Tells if editing the setting requires registration. */
	public final boolean                         editRequiresRegistration;
	
	/**
	 * Rows count to be used for the setting component if supported. Used for {@link MultilineStringSetting} and
	 * {@link hu.scelight.bean.settings.type.ValidatedMultilineStringSetting}.
	 */
	public final Integer                         rows;
	
	/**
	 * Columns count to be used for the setting component if supported. Used for {@link StringSetting}, {@link PathSetting}, {@link ValidatedStringSetting},
	 * {@link hu.scelight.bean.settings.type.TemplateSetting}, {@link MultilineStringSetting} and
	 * {@link hu.scelight.bean.settings.type.ValidatedMultilineStringSetting}.
	 */
	public final Integer                         columns;
	
	
	/**
	 * Creates a new {@link ViewHints}.
	 * 
	 * @param ricon setting ricon
	 * @param subsequentText subsequent text of the setting component
	 * @param rhtml help HTML resource of the setting
	 * @param dialogTitle dialog title
	 * @param ssCompFactory custom subsequent component factory
	 * @param compConfigurer setting component configurer
	 * @param editRequiresRegistration tells if editing the setting requires registration
	 * @param rows rows count to be used for the setting component if supported
	 * @param columns columns count to be used for the setting component if supported
	 */
	public ViewHints( final IRIcon ricon, final String subsequentText, final IRHtml rhtml, final String dialogTitle,
	        final ISsCompFactory< ISetting< ? > > ssCompFactory, final ICompConfigurer compConfigurer, final boolean editRequiresRegistration,
	        final Integer rows, final Integer columns ) {
		this.ricon = ricon;
		this.subsequentText = subsequentText;
		this.helpRhtml = rhtml;
		this.dialogTitle = dialogTitle;
		this.ssCompFactory = ssCompFactory;
		this.compConfigurer = compConfigurer;
		this.editRequiresRegistration = editRequiresRegistration;
		this.rows = rows;
		this.columns = columns;
	}
	
	@Override
	public IRIcon getRicon() {
		return ricon;
	}
	
	@Override
	public String getSubsequentText() {
		return subsequentText;
	}
	
	@Override
	public IRHtml getHelpRhtml() {
		return helpRhtml;
	}
	
	@Override
	public String getDialogTitle() {
		return dialogTitle;
	}
	
	@Override
	public ISsCompFactory< ISetting< ? > > getSsCompFactory() {
		return ssCompFactory;
	}
	
	@Override
	public ICompConfigurer getCompConfigurer() {
		return compConfigurer;
	}
	
	@Override
	public boolean isEditRequiresRegistration() {
		return editRequiresRegistration;
	}
	
	@Override
	public Integer getRows() {
		return rows;
	}
	
	@Override
	public Integer getColumns() {
		return columns;
	}
	
}
