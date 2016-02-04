/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight.gui.page;

import hu.scelight.gui.icon.Icons;
import hu.scelight.util.gui.GuiUtils;
import hu.sllauncher.gui.comp.XLabel;
import hu.sllauncher.gui.comp.multipage.BasePage;
import hu.sllauncher.util.LRHtml;

import javax.swing.JComponent;

/**
 * Parent page for the multi-replay analyzer pages.
 * 
 * @author Andras Belicza
 */
public class MultiRepAnalyzersPage extends BasePage< JComponent > {
	
	/**
	 * Creates a new {@link MultiRepAnalyzersPage}.
	 */
	public MultiRepAnalyzersPage() {
		super( "Multi-Replay Analyzers", Icons.MY_CHART_UP_COLORS );
	}
	
	@Override
	public JComponent createPageComp() {
		final String infoText = "<html><head>"
		        + LRHtml.GENERAL_CSS
		        + "</head><body style='text-align:center'>"
		        + "<br>This is the parent page of <span class='s'>Multi-Replay Analyzers</span>."
		        + "<br><br>You can launch <span class='s'>Multi-Replay Analyzers</span> on the <span class='s'>Replay Folders</span> page and on the <span class='s'>Replay List</span> pages."
		        + "</body></html>";
		
		return GuiUtils.wrapInPanel( new XLabel( infoText ) );
	}
	
}
