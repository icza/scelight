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

import hu.scelightapibase.bean.settings.ISettingsBean;
import hu.scelightapibase.bean.settings.type.ISetting;
import hu.scelightapibase.bean.settings.type.viewhints.ISsCompFactory;
import hu.scelightapibase.bean.settings.type.viewhints.ITestBtnFactory;
import hu.scelightapibase.gui.comp.IButton;
import hu.sllauncher.gui.comp.XButton;
import hu.sllauncher.gui.icon.LIcons;
import hu.sllauncher.util.gui.adapter.ActionAdapter;

import java.awt.event.ActionEvent;

import javax.swing.Icon;
import javax.swing.JComponent;

/**
 * An {@link ISsCompFactory} implementation which creates test {@link XButton} subsequent components which call
 * {@link #doTest(IButton, JComponent, ISetting, ISettingsBean)} when pressed.
 * 
 * @param <T> setting type to create custom subsequent components for
 * 
 * @author Andras Belicza
 */
public abstract class TestBtnFactory< T extends ISetting< ? > > implements ITestBtnFactory< T > {
	
	/** Text of the test button that will be created. */
	protected final String text;
	
	/** Icon of the test button that will be created. */
	protected final Icon   icon;
	
	/**
	 * Creates a new {@link TestBtnFactory} with default text and icon.
	 */
	public TestBtnFactory() {
		this( null, null );
	}
	
	/**
	 * Creates a new {@link TestBtnFactory} with default icon.
	 * 
	 * @param text text of the test button that will be created
	 */
	public TestBtnFactory( final String text ) {
		this( text, null );
	}
	
	/**
	 * Creates a new {@link TestBtnFactory} with default text.
	 * 
	 * @param icon icon of the test button that will be created
	 */
	public TestBtnFactory( final Icon icon ) {
		this( null, icon );
	}
	
	/**
	 * Creates a new {@link TestBtnFactory}.
	 * 
	 * @param text optional text of the test button that will be created; defaults to <code>"Test"</code>
	 * @param icon optional icon of the test button that will be created; defaults to {@link LIcons#F_CONTROL}
	 */
	public TestBtnFactory( final String text, final Icon icon ) {
		this.text = text == null ? "Test" : text;
		this.icon = icon == null ? LIcons.F_CONTROL.get() : icon;
	}
	
	@Override
	public XButton create( final JComponent settingComp, final T setting, final ISettingsBean settings ) {
		final XButton button = new XButton( text, icon );
		
		button.addActionListener( new ActionAdapter() {
			@Override
			public void actionPerformed( final ActionEvent event ) {
				doTest( button, settingComp, setting, settings );
			}
		} );
		
		return button;
	}
	
	@Override
	public String getText() {
		return text;
	}
	
	@Override
	public Icon getIcon() {
		return icon;
	}
	
	/**
	 * Performs the test.
	 * 
	 * @param button reference to the test button
	 * @param settingComp reference to the setting editor component
	 * @param setting setting to create the component for
	 * @param settings settings bean managing the setting value
	 */
	public abstract void doTest( IButton button, JComponent settingComp, T setting, ISettingsBean settings );
	
}
