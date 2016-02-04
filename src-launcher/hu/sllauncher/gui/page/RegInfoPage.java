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
import hu.sllauncher.service.registration.RegManager;
import hu.sllauncher.service.registration.RegStatus;
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
		final RegStatus regStatus = LEnv.REG_MANAGER.regStatus;
		StatusLabel statusLabel = new StatusLabel();
		statusLabel.setByType( regStatus.statusType, regStatus.message );
		toolBar.add( statusLabel.rightBorder( 3 ) );
		if ( regStatus.help != null )
			toolBar.add( new HelpIcon( regStatus.help ) );
		toolBar.finalizeLayout();
		
		if ( LEnv.REG_MANAGER.regStatus.compareTo( RegStatus.INVALID ) <= 0 ) {
			final Browser browser = new Browser();
			browser.setText( new LRHtml( "Not registered page", "html/not-registered.html", "urlReginfoPage", LConsts.URL_REGINFO_PAGE.toString() ).get() );
			p.addCenter( new XScrollPane( browser, true ) );
		} else {
			toolBar = new XToolBar();
			toolBarsBox.add( toolBar );
			toolBar.add( new XLabel( "For registration details visit this page: " ).verticalBorder( 5 ) );
			toolBar.add( new Link( "Registration page", LConsts.URL_REGINFO_PAGE ) );
			toolBar.finalizeLayout();
			
			final RegInfoBean regInfo = LEnv.REG_MANAGER.getRegInfo();
			
			final Box box = Box.createVerticalBox();
			
			// Registered person
			box.add( Box.createVerticalStrut( 10 ) );
			XTable table = new XTable();
			JComponent tb = table.createWrapperBox( false );
			tb.setBorder( BorderFactory.createTitledBorder( "Registered person" ) );
			final PersonBean person = regInfo.getPerson();
			final Vector< Vector< Object > > data = new Vector<>();
			data.add( LUtils.vector( "Name", person.getPersonName() ) );
			final ContactBean contact = person.getContact();
			if ( contact.getLocation() != null )
				data.add( LUtils.vector( "Location", contact.getLocation() ) );
			if ( contact.getEmail() != null )
				data.add( LUtils.vector( "Contact email", LUtils.tryMakingUrl( "mailto:" + contact.getEmail() ) ) );
			if ( contact.getFacebook() != null )
				data.add( LUtils.vector( "Facebook", LUtils.tryMakingUrl( contact.getFacebook() ) ) );
			if ( contact.getGooglePlus() != null )
				data.add( LUtils.vector( "Google+", LUtils.tryMakingUrl( contact.getGooglePlus() ) ) );
			if ( contact.getTwitter() != null )
				data.add( LUtils.vector( "Twitter", LUtils.tryMakingUrl( contact.getTwitter() ) ) );
			if ( contact.getLinkedIn() != null )
				data.add( LUtils.vector( "LinkedIn", LUtils.tryMakingUrl( contact.getLinkedIn() ) ) );
			if ( contact.getYoutube() != null )
				data.add( LUtils.vector( "YouTube", LUtils.tryMakingUrl( contact.getYoutube() ) ) );
			if ( contact.getOther() != null )
				data.add( LUtils.vector( "Other", LUtils.tryMakingUrl( contact.getOther() ) ) );
			if ( person.getHomePage() != null )
				data.add( LUtils.vector( "Home page", person.getHomePage() ) );
			if ( person.getDescription() != null )
				data.add( LUtils.vector( "Description", person.getDescription().replace( '\n', ' ' ) ) );
			table.getXTableModel().setDataVector( data, LUtils.vector( "Property", "Value" ) );
			box.add( tb );
			
			// Registration info details
			box.add( Box.createVerticalStrut( 10 ) );
			table = new XTable();
			tb = table.createWrapperBox( false );
			tb.setBorder( BorderFactory.createTitledBorder( "Registration info details" ) );
			final Calendar expCal = Calendar.getInstance();
			expCal.setTime( regInfo.getEncryptionDate() );
			expCal.add( Calendar.MONTH, RegManager.REG_FILE_EXPIRATION_MONTHS );
			table.getXTableModel().setDataVector(
			        new Object[][] { { "Registered Google account", LUtils.tryMakingUrl( "mailto:" + regInfo.getGoogleAccount() ) },
			                { "Registration date", new DateValue( regInfo.getRegDate(), DateFormat.DATE_TIME_REL_LONG ) },
			                { "Registration file encryption date", new DateValue( regInfo.getEncryptionDate(), DateFormat.DATE_TIME_REL_LONG ) },
			                { "Registration file expiration", new DateValue( expCal.getTime(), DateFormat.DATE_TIME_REL_LONG ) } },
			        new Object[] { "Property", "Value" } );
			box.add( tb );
			
			// Registered system info
			box.add( Box.createVerticalStrut( 10 ) );
			table = new XTable();
			tb = table.createWrapperBox( false );
			tb.setBorder( BorderFactory.createTitledBorder( "Registered system info" ) );
			final SysInfoBean s = regInfo.getSysInfo();
			final SysInfoBean s2 = LUtils.getSysInfo();
			table.getXTableModel().setDataVector(
			        new Object[][] {
			                { "OS name", s.getOsName(), s2.getOsName(), Objects.equals( s.getOsName(), s2.getOsName() ) },
			                { "OS version", s.getOsVersion(), s2.getOsVersion(), Objects.equals( s.getOsVersion(), s2.getOsVersion() ) },
			                { "Available processors", s.getAvailProcs(), s2.getAvailProcs(), Objects.equals( s.getAvailProcs(), s2.getAvailProcs() ) },
			                { "User name", s.getUserName(), s2.getUserName(), Objects.equals( s.getUserName(), s2.getUserName() ) },
			                { "User country", s.getUserCountry(), s2.getUserCountry(), Objects.equals( s.getUserCountry(), s2.getUserCountry() ) },
			                { "User time zone", s.getUserTimeZone(), s2.getUserTimeZone(), Objects.equals( s.getUserTimeZone(), s2.getUserTimeZone() ) },
			                { "Main root size", s.getMainRootSize() == null ? null : new SizeValue( s.getMainRootSize(), SizeFormat.BYTES ),
			                        new SizeValue( s2.getMainRootSize(), SizeFormat.BYTES ), Objects.equals( s.getMainRootSize(), s2.getMainRootSize() ) } },
			        new Object[] { "Property", "Registered Value", "Current Value", "Matches?" } );
			box.add( tb );
			
			box.add( new BorderPanel() );
			
			p.addCenter( new XScrollPane( box ) );
		}
		
		return p;
	}
	
}
