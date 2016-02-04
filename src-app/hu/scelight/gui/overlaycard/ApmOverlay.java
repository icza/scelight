/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight.gui.overlaycard;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import javax.swing.Timer;

import hu.scelight.gui.comp.XCheckBoxMenuItem;
import hu.scelight.gui.comp.XMenu;
import hu.scelight.gui.icon.Icons;
import hu.scelight.service.env.Env;
import hu.scelight.service.sc2reg.Sc2RegMonitor;
import hu.scelight.service.settings.Settings;
import hu.scelight.util.gui.ColorEnum;
import hu.scelightapi.gui.comp.IPopupMenu;
import hu.scelightapi.gui.overlaycard.OverlayCardParams;
import hu.scelightapibase.bean.settings.ISettingChangeEvent;
import hu.scelightapibase.bean.settings.ISettingChangeListener;
import hu.sllauncher.bean.settings.type.EnumSetting;
import hu.sllauncher.gui.icon.LRIcon;
import hu.sllauncher.util.gui.adapter.ActionAdapter;

/**
 * Current, live APM overlay card.
 * 
 * @author Andras Belicza
 */
@SuppressWarnings( "serial" )
public class ApmOverlay extends OverlayCard {
	
	/** Instance reference. */
	private static final AtomicReference< ApmOverlay > INSTANCE = new AtomicReference< >();
	
	/**
	 * Returns the instance reference.
	 * 
	 * @return the instance reference
	 */
	public static ApmOverlay INSTANCE() {
		return INSTANCE.get();
	}
	
	
	/** Overlay card parameters. */
	private static final OverlayCardParams PARAMS = new OverlayCardParams();
	
	static {
		PARAMS.settings = Env.APP_SETTINGS;
		PARAMS.centerXSetting = Settings.APM_OVERLAY_CENTER_X;
		PARAMS.centerYSetting = Settings.APM_OVERLAY_CENTER_Y;
		PARAMS.opacitySetting = Settings.APM_OVERLAY_OPACITY;
		PARAMS.lockedSetting = Settings.APM_OVERLAY_LOCKED;
		PARAMS.focusableSetting = Settings.APM_OVERLAY_FOCUSABLE;
		PARAMS.allowOutMainScrSetting = Settings.APM_OVERLAY_ALLOW_OUT_MAIN_SCR;
	}
	
	
	/** Timer to periodically refresh the displayed APM value. */
	private final Timer					 timer;
										 
	/** Setting change listener (to keep reference to it). */
	private final ISettingChangeListener scl;
										 
	/**
	 * Creates a new {@link ApmOverlay}.
	 */
	public ApmOverlay() {
		super( PARAMS );
		
		INSTANCE.set( this );
		
		titleLabel.setOpaque( true );
		titleLabel.setText( "APM:" );
		
		scl = new ISettingChangeListener() {
			@Override
			public void valuesChanged( final ISettingChangeEvent event ) {
				if ( event.affected( Settings.APM_OVERLAY_FONT_SIZE ) ) {
					titleLabel.setFont( null );
					titleLabel.setFont( titleLabel.getFont().deriveFont( Font.BOLD, Env.APP_SETTINGS.get( Settings.APM_OVERLAY_FONT_SIZE ) ) );
					packAndPosition();
				}
			}
		};
		settings.addAndExecuteChangeListener( Settings.APM_OVERLAY_FONT_SIZE, scl );
		
		setVisible( true );
		
		timer = new Timer( 500, new ActionAdapter( true) {
			@Override
			public void actionPerformed( final ActionEvent event ) {
				// We're in the EDT (due to swing Timer)
				final Integer apm = Sc2RegMonitor.getApm();
				if ( apm == null )
					return;
				titleLabel.setText( "APM: " + apm );
				final boolean alert = Env.APP_SETTINGS.get( Settings.ENABLE_APM_ALERT ) && apm < Env.APP_SETTINGS.get( Settings.APM_ALERT_LEVEL );
				titleLabel.setForeground( ( alert ? Env.APP_SETTINGS.get( Settings.APM_OVERLAY_ALERT_FONT_COLOR )
		                : Env.APP_SETTINGS.get( Settings.APM_OVERLAY_FONT_COLOR ) ).color );
				titleLabel.setBackground( ( alert ? Env.APP_SETTINGS.get( Settings.APM_OVERLAY_ALERT_BACKGROUND_COLOR )
		                : Env.APP_SETTINGS.get( Settings.APM_OVERLAY_BACKGROUND_COLOR ) ).color );
			}
		} );
		timer.start();
	}
	
	/**
	 * Overridden to set a 4-digit displayed APM value to reserve space for it before repacking.
	 */
	@Override
	public void packAndPosition() {
		final String oldText = titleLabel.getText();
		
		titleLabel.setText( "APM: 9999" );
		super.packAndPosition();
		titleLabel.setText( oldText );
	}
	
	@Override
	public void buildConfigPopup( final IPopupMenu popupMenu ) {
		super.buildConfigPopup( popupMenu );
		
		popupMenu.asPopupMenu().addSeparator();
		final XMenu fontSizeMenu = new XMenu( "Fo_nt size", Icons.F_EDIT_SIZE.get() );
		final int fontSize = Env.APP_SETTINGS.get( Settings.APM_OVERLAY_FONT_SIZE );
		for ( int i = Settings.APM_OVERLAY_FONT_SIZE.minValue; i <= Settings.APM_OVERLAY_FONT_SIZE.maxValue; i += 2 ) {
			final XCheckBoxMenuItem fontSizeMenuItem = new XCheckBoxMenuItem( Integer.toString( i ) );
			if ( fontSize == i )
				fontSizeMenuItem.setSelected( true );
			final int i_ = i;
			fontSizeMenuItem.addActionListener( new ActionAdapter() {
				@Override
				public void actionPerformed( final ActionEvent event ) {
					Env.APP_SETTINGS.set( Settings.APM_OVERLAY_FONT_SIZE, i_ );
				}
			} );
			fontSizeMenu.add( fontSizeMenuItem );
		}
		popupMenu.asPopupMenu().add( fontSizeMenu );
		
		// Add following menus: Font color, Background color, Alert font color, Alert background color
		
		final List< EnumSetting< ColorEnum > > settings = Arrays.asList( Settings.APM_OVERLAY_FONT_COLOR, Settings.APM_OVERLAY_BACKGROUND_COLOR,
		        Settings.APM_OVERLAY_ALERT_FONT_COLOR, Settings.APM_OVERLAY_ALERT_BACKGROUND_COLOR );
		final List< String > names = Arrays.asList( "Font colo_r", "_Background color", "Al_ert font colo_r", "Aler_t background color" );
		final List< ? extends LRIcon > icons = Arrays.asList( Icons.F_EDIT_COLOR, Icons.F_EDIT_SHADE, Icons.F_EDIT_COLOR, Icons.F_EDIT_SHADE );
		for ( int i = 0; i < settings.size(); i++ ) {
			final EnumSetting< ColorEnum > setting = settings.get( i );
			final ColorEnum colorVal = Env.APP_SETTINGS.get( setting );
			final XMenu menu = new XMenu( names.get( i ), icons.get( i ).get() );
			for ( final ColorEnum color : Settings.APM_OVERLAY_FONT_COLOR.values ) {
				final XCheckBoxMenuItem colorMenuItem = new XCheckBoxMenuItem( color.origName, color.icon );
				if ( colorVal == color )
					colorMenuItem.setSelected( true );
				colorMenuItem.addActionListener( new ActionAdapter() {
					@Override
					public void actionPerformed( final ActionEvent event ) {
						Env.APP_SETTINGS.set( setting, color );
					}
				} );
				menu.add( colorMenuItem );
			}
			popupMenu.asPopupMenu().add( menu );
		}
	}
	
	@Override
	protected void customOnClose() {
		timer.stop();
		INSTANCE.set( null );
	}
	
}
