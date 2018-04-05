/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.sllauncher.gui.page;

import hu.sllauncher.LConsts;
import hu.sllauncher.bean.person.ContactBean;
import hu.sllauncher.bean.person.PersonBean;
import hu.sllauncher.bean.reginfo.RegInfoBean;
import hu.sllauncher.bean.reginfo.SysInfoBean;
import hu.sllauncher.gui.comp.BorderPanel;
import hu.sllauncher.gui.comp.Browser;
import hu.sllauncher.gui.comp.HelpIcon;
import hu.sllauncher.gui.comp.Link;
import hu.sllauncher.gui.comp.StatusLabel;
import hu.sllauncher.gui.comp.XLabel;
import hu.sllauncher.gui.comp.XScrollPane;
import hu.sllauncher.gui.comp.XToolBar;
import hu.sllauncher.gui.comp.multipage.BasePage;
import hu.sllauncher.gui.comp.table.XTable;
import hu.sllauncher.gui.icon.LIcons;
import hu.sllauncher.service.env.LEnv;

import hu.sllauncher.util.DateFormat;
import hu.sllauncher.util.DateValue;
import hu.sllauncher.util.LRHtml;
import hu.sllauncher.util.LUtils;
import hu.sllauncher.util.SizeFormat;
import hu.sllauncher.util.SizeValue;

import java.util.Calendar;
import java.util.Objects;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JComponent;

/**
 * Page displaying registration info.
 * 
 * @author Andras Belicza
 */
public class RegInfoPage extends BasePage< JComponent > {
	
	/**
	 * Creates a new {@link RegInfoPage}.
	 */
	public RegInfoPage() {
		super( "Registration info", LIcons.F_LICENCE_KEY );
	}
	
	@Override
	public JComponent createPageComp() {
		final BorderPanel p = new BorderPanel();
		
		final Box toolBarsBox = Box.createVerticalBox();
		p.addNorth( toolBarsBox );
		
		XToolBar toolBar = new XToolBar();
		toolBarsBox.add( toolBar );
		toolBar.add( new XLabel( "Registration status: " ).verticalBorder( 5 ) );

		StatusLabel statusLabel = new StatusLabel();
		toolBar.add( statusLabel.rightBorder( 3 ) );

		toolBar.finalizeLayout();
		
		return p;
	}
	
}
