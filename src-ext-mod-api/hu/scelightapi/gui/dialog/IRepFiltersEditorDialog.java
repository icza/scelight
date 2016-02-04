/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelightapi.gui.dialog;

import hu.scelightapi.bean.repfilters.IRepFilterBean;
import hu.scelightapi.bean.repfilters.IRepFiltersBean;
import hu.scelightapi.service.IFactory;
import hu.scelightapibase.gui.comp.IDialog;

/**
 * A dialog to view / edit Replay filters.
 * 
 * @author Andras Belicza
 * 
 * @see IFactory#newRepFiltersEditorDialog(IRepFiltersBean)
 * @see IRepFiltersBean
 * @see IRepFilterBean
 */
public interface IRepFiltersEditorDialog extends IDialog {
	
	/**
	 * Tells if the editor was closed with the OK button.
	 * 
	 * @return true if the editor was closed with the OK button; false otherwise
	 */
	boolean isOk();
	
	/**
	 * Returns the edited replay filters.
	 * 
	 * <p>
	 * The returned value might be <code>null</code> if no filters were specified (or all filters were removed) on the dialog.
	 * </p>
	 * 
	 * @return the edited replay filters
	 */
	IRepFiltersBean getRepFiltersBean();
	
}
