/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.sllauncher.util.gui;

import hu.scelightapibase.bean.person.IPersonNameBean;
import hu.scelightapibase.gui.icon.IRIcon;
import hu.scelightapibase.service.lang.ILanguage;
import hu.scelightapibase.util.gui.HasIcon;
import hu.scelightapibase.util.gui.HasRIcon;
import hu.sllauncher.gui.comp.XLabel;
import hu.sllauncher.gui.icon.LIcons;
import hu.sllauncher.gui.icon.LRIcon;
import hu.sllauncher.service.env.LEnv;
import hu.sllauncher.util.LUtils;

import java.net.URL;
import java.util.Date;

import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * Advanced, unified rendering logic which targets a {@link JLabel} ({@link JLabel} is used instead of {@link XLabel} because most default Java renderers are
 * <code>JLabel</code> descendants)
 * 
 * <p>
 * Can be used where the renderer component is an instance of {@link JLabel}. Designed to be called after the default rendering logic.<br>
 * Such examples are: {@link DefaultTableCellRenderer}, {@link DefaultListCellRenderer}.
 * </p>
 * 
 * Handles the following cases / values:
 * <ul>
 * <li>{@link Icon}: renders the icon with no text
 * <li>{@link IRIcon}: renders the icon with no text
 * <li>{@link Date}: formats it using {@link ILanguage#formatDateTime(Date)}
 * <li>{@link Double}: formats it using {@link ILanguage#formatNumber(double, int)} with 2 precision
 * <li>{@link Float}: formats it using {@link ILanguage#formatNumber(double, int)} with 2 precision
 * <li>{@link Number}: formats it using {@link ILanguage#formatNumber(long)}
 * <li>{@link IPersonNameBean}: formats it using {@link ILanguage#formatPersonName(IPersonNameBean)}
 * <li>{@link HasIcon}: also renders the icon returned by {@link HasIcon#getIcon()} beside the default text
 * <li>{@link HasRIcon}: also renders the icon returned by {@link HasRIcon#getRicon()} beside the default text
 * <li>{@link Boolean}: renders a tick icon for <code>true</code> and a cross icon for <code>false</code>
 * <li>{@link URL}: renders as link (with proper tool tip)
 * </ul>
 * 
 * @author Andras Belicza
 * 
 * @see RenderablePair
 */
public class ToLabelRenderer {
	
	/**
	 * Performs the advanced, unified renderer logic.
	 * 
	 * @param l the renderer label
	 * @param value the rendered value object
	 * @param isSelected tells if the rendered value is selected
	 * @param hasFocus tells if the rendered value has focus
	 * @return the renderer label
	 */
	public static JLabel render( final JLabel l, Object value, final boolean isSelected, final boolean hasFocus ) {
		l.setToolTipText( null );
		
		if ( value instanceof Boolean )
			value = (Boolean) value ? LIcons.F_TICK : LIcons.F_CROSS;
		
		if ( value instanceof Icon ) {
			
			l.setIcon( (Icon) value );
			l.setText( null );
			
		} else if ( value instanceof IRIcon ) {
			
			l.setIcon( ( (IRIcon) value ).get() );
			l.setText( null );
			
		} else {
			
			if ( value instanceof HasIcon ) {
				
				l.setIcon( ( (HasIcon) value ).getIcon() );
				
			} else if ( value instanceof HasRIcon ) {
				
				final IRIcon ricon = ( (HasRIcon) value ).getRicon();
				l.setIcon( ricon == null ? null : ricon.get() );
				
			} else
				l.setIcon( null );
			
			if ( value instanceof Number ) {
				if ( value instanceof Double || value instanceof Float )
					l.setText( LEnv.LANG.formatNumber( ( (Number) value ).doubleValue(), 2 ) );
				else
					l.setText( LEnv.LANG.formatNumber( ( (Number) value ).longValue() ) );
			} else if ( value instanceof Date )
				l.setText( LEnv.LANG.formatDateTime( (Date) value ) );
			else if ( value instanceof IPersonNameBean )
				l.setText( LEnv.LANG.formatPersonName( (IPersonNameBean) value ) );
			else if ( value instanceof URL ) {
				l.setText( LUtils.htmlLinkText( "mailto".equals( ( (URL) value ).getProtocol() ) ? ( (URL) value ).getPath() : value.toString() ) );
				l.setToolTipText( LUtils.urlToolTip( (URL) value ) );
				final LRIcon ricon = LUtils.urlIcon( (URL) value );
				l.setIcon( ricon == null ? null : ricon.get() );
			}
			
		}
		
		return l;
	}
	
}
