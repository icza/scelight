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

import hu.scelight.gui.comp.XCheckBoxMenuItem;
import hu.scelight.gui.comp.XMenu;
import hu.scelight.gui.comp.XMenuItem;
import hu.scelight.gui.comp.XPopupMenu;
import hu.scelight.gui.icon.Icons;
import hu.scelight.util.Utils;
import hu.scelight.util.gui.GuiUtils;
import hu.scelightapi.gui.comp.IPopupMenu;
import hu.scelightapi.gui.overlaycard.IOverlayCard;
import hu.scelightapi.gui.overlaycard.OverlayCardParams;
import hu.scelightapibase.bean.settings.ISettingChangeEvent;
import hu.scelightapibase.bean.settings.ISettingChangeListener;
import hu.scelightapibase.bean.settings.ISettingsBean;
import hu.scelightapibase.bean.settings.type.IBoolSetting;
import hu.scelightapibase.bean.settings.type.IIntSetting;
import hu.scelightapibase.bean.settings.type.ISetting;
import hu.sllauncher.gui.comp.BorderPanel;
import hu.sllauncher.gui.comp.XButton;
import hu.sllauncher.gui.comp.XLabel;
import hu.sllauncher.gui.setting.LSettingsGui;
import hu.sllauncher.util.gui.adapter.ActionAdapter;

import java.awt.Cursor;
import java.awt.Frame;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Objects;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.border.Border;

/**
 * OS-level independent overlay cards which can be displayed on top of everything else.
 * 
 * <p>
 * They are implemented as {@link JDialog}s without decorations and with on-top capabilities.
 * </p>
 * 
 * @author Andras Belicza
 */
@SuppressWarnings( "serial" )
public class OverlayCard extends JDialog implements IOverlayCard {
	
	/** Border of the active overlay. */
	private static final Border          BORDER_ACTIVE   = BorderFactory.createLoweredSoftBevelBorder();
	
	/** Border of the inactive overlay. */
	private static final Border          BORDER_INACTIVE = BorderFactory.createRaisedSoftBevelBorder();
	
	
	/** Settings bean storing the overlay settings. */
	protected final ISettingsBean        settings;
	
	/** Setting of the overlay center X position. */
	protected final IIntSetting          centerXSetting;
	
	/** Setting of the overlay center Y position. */
	protected final IIntSetting          centerYSetting;
	
	/** Setting of the overlay opacity. */
	protected final IIntSetting          opacitySetting;
	
	/** Setting of the overlay locked property. */
	protected final IBoolSetting         lockedSetting;
	
	/** Setting of the overlay focusable property. */
	protected final IBoolSetting         focusableSetting;
	
	/** Setting of the overlay allow positioning outside the main screen property. */
	protected final IBoolSetting         allowOutMainScrSetting;
	
	
	/** Custom border panel functioning as the content panel. */
	protected final BorderPanel          cp              = new BorderPanel();
	
	/** Border panel functioning as the window title. */
	protected final BorderPanel          titlePanel      = new BorderPanel();
	
	/** Overlay title label. */
	protected final XLabel               titleLabel      = new XLabel();
	
	/** Overlay config button. */
	protected final XButton              configButton    = new XButton( Icons.F_WRENCH.get() );
	
	
	/** Setting change listener (to keep reference to it). */
	private final ISettingChangeListener scl;
	
	/**
	 * Creates a new {@link OverlayCard}.
	 * 
	 * @param params overlay card parameters wrapper
	 * @throws NullPointerException if any of the params' fields are <code>null</code>
	 */
	public OverlayCard( final OverlayCardParams params ) throws NullPointerException {
		
		// The default (hidden) parent cannot be used because it would be the same for all overlays
		// Resulting in overlays losing always-on-top state when another overlay is focused/activated.
		// We want overlays to be independent (from each other and from the main frame)
		// so each Overlay must have its own, unique parent:
		super( new Frame() );
		
		settings = Objects.requireNonNull( params.settings, "settings cannot be null!" );
		centerXSetting = Objects.requireNonNull( params.centerXSetting, "centerXSetting cannot be null!" );
		centerYSetting = Objects.requireNonNull( params.centerYSetting, "centerYSetting cannot be null!" );
		opacitySetting = Objects.requireNonNull( params.opacitySetting, "opacitySetting cannot be null!" );
		lockedSetting = Objects.requireNonNull( params.lockedSetting, "lockedSetting cannot be null!" );
		focusableSetting = Objects.requireNonNull( params.focusableSetting, "focusableSetting cannot be null!" );
		allowOutMainScrSetting = Objects.requireNonNull( params.allowOutMainScrSetting, "allowOutMainScrSetting cannot be null!" );
		
		setDefaultCloseOperation( DO_NOTHING_ON_CLOSE );
		
		addWindowListener( new WindowAdapter() {
			@Override
			public void windowClosing( final WindowEvent event ) {
				if ( settings.get( lockedSetting ) )
					return;
				close();
			}
			
			@Override
			public void windowActivated( final WindowEvent event ) {
				cp.setBorder( BORDER_ACTIVE );
			}
			
			@Override
			public void windowDeactivated( final WindowEvent event ) {
				cp.setBorder( BORDER_INACTIVE );
			}
		} );
		
		setAlwaysOnTop( true );
		setUndecorated( true );
		
		buildGui();
		
		scl = new ISettingChangeListener() {
			@Override
			public void valuesChanged( final ISettingChangeEvent event ) {
				if ( event.affectedAny( centerXSetting, centerYSetting, allowOutMainScrSetting ) )
					packAndPosition();
				
				if ( event.affected( opacitySetting ) )
					setOverlayOpacity();
				
				if ( event.affected( lockedSetting ) )
					setCursor( event.get( lockedSetting ) ? null : Cursor.getPredefinedCursor( Cursor.MOVE_CURSOR ) );
				
				if ( event.affected( focusableSetting ) ) {
					setFocusableWindowState( event.get( focusableSetting ) );
					// If not focusable, there won't be any windowDeactivated() events, so take care of custom border ourselves:
					if ( !event.get( focusableSetting ) )
						cp.setBorder( BORDER_INACTIVE );
					// If window is already visible, changing focusable state may be deferred on some platforms
					// until the dialog is hid and made visible again
					if ( isVisible() ) {
						setVisible( false );
						setVisible( true );
					}
				}
			}
		};
		settings.addAndExecuteChangeListener(
		        Utils.< ISetting< ? > > asNewSet( centerXSetting, centerYSetting, opacitySetting, lockedSetting, focusableSetting, allowOutMainScrSetting ),
		        scl );
	}
	
	@Override
	public JDialog asDialog() {
		return this;
	}
	
	@Override
	public BorderPanel getCp() {
		return cp;
	}
	
	@Override
	public BorderPanel getTitlePanel() {
		return titlePanel;
	}
	
	@Override
	public XLabel getTitleLabel() {
		return titleLabel;
	}
	
	/**
	 * Sets the overlay opacity.
	 */
	private void setOverlayOpacity() {
		try {
			// If translucency is not supported, the following line throws an IllegalComponentStateException.
			setOpacity( settings.get( opacitySetting ) / 100f );
		} catch ( final Exception e ) {
			// Translucency is not supported. Nothing will change, ignore this.
		}
	}
	
	/**
	 * Closes the dialog.
	 * 
	 * @see #customOnClose()
	 */
	public void close() {
		customOnClose();
		LSettingsGui.removeAllBoundedScl( cp );
		dispose();
	}
	
	/**
	 * This method is called before the dialog is closed.<br>
	 * This implementation does nothing, it's here to be overridden if needed.
	 * 
	 * @see #close()
	 */
	protected void customOnClose() {
		// Subclasses will optionally do something here.
	}
	
	/**
	 * Builds the GUI of the main frame.
	 */
	private void buildGui() {
		// Need to set border now for the initial proper overlay size!
		cp.setBorder( BORDER_ACTIVE );
		cp.addNorth( titlePanel );
		setContentPane( cp );
		
		titlePanel.addWest( GuiUtils.boldFont( titleLabel ) );
		
		// Config menu
		configButton.configureAsIconButton();
		configButton.addActionListener( new ActionAdapter() {
			@Override
			public void actionPerformed( final ActionEvent event ) {
				final XPopupMenu popupMenu = new XPopupMenu( "Overlay Config" );
				
				buildConfigPopup( popupMenu );
				
				// Add Close last
				popupMenu.addSeparator();
				final XMenuItem closeMenuItem = new XMenuItem( "_Close", Icons.F_CROSS.get() );
				closeMenuItem.addActionListener( new ActionAdapter() {
					@Override
					public void actionPerformed( final ActionEvent event ) {
						close();
					}
				} );
				popupMenu.add( closeMenuItem );
				
				popupMenu.show( configButton, 0, configButton.getHeight() );
			}
		} );
		titlePanel.addEast( configButton );
		
		// Add support for mouse dragging
		final MouseAdapter mouseAdapter = new MouseAdapter() {
			private boolean dragging;
			
			private int     refX;
			
			private int     refY;
			
			@Override
			public void mouseEntered( final MouseEvent event ) {
				if ( event.getSource() == titleLabel )
					setOpacity( 1f ); // Set to be opaque
			}
			
			@Override
			public void mouseExited( final MouseEvent event ) {
				if ( event.getSource() == titleLabel )
					setOverlayOpacity(); // Restore opacity
			}
			
			@Override
			public void mousePressed( final MouseEvent event ) {
				if ( event.getButton() != GuiUtils.MOUSE_BTN_LEFT || settings.get( lockedSetting ) )
					return;
				
				refX = event.getX();
				refY = event.getY();
				dragging = true;
			}
			
			@Override
			public void mouseDragged( final MouseEvent event ) {
				if ( !dragging )
					return;
				
				settings.set( centerXSetting, getX() + event.getX() - refX + getWidth() / 2 );
				settings.set( centerYSetting, getY() + event.getY() - refY + getHeight() / 2 );
			}
			
			@Override
			public void mouseReleased( final MouseEvent event ) {
				if ( event.getButton() != GuiUtils.MOUSE_BTN_LEFT )
					return;
				dragging = false;
			}
		};
		addMouseListener( mouseAdapter );
		addMouseMotionListener( mouseAdapter );
		titleLabel.addMouseListener( mouseAdapter );
		titleLabel.addMouseMotionListener( mouseAdapter );
	}
	
	@Override
	public void packAndPosition() {
		pack();
		
		setLocation( settings.get( centerXSetting ) - getWidth() / 2, settings.get( centerYSetting ) - getHeight() / 2 );
		
		if ( !settings.get( allowOutMainScrSetting ) )
			ensureBoundsVisible();
	}
	
	/**
	 * Ensures that the whole overlay card is visible (inside the screen).
	 * <p>
	 * If some parts of the dialog would fall outside of the screen, the dialog will be moved to still just fit in.
	 * </p>
	 */
	private void ensureBoundsVisible() {
		final Rectangle screenBounds = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration().getBounds();
		final Rectangle bounds = getBounds();
		
		boolean aligned = false;
		
		// Condition check order: ensure that the top right corner is visible (config button there)
		// even if the dialog has bigger size than the screen
		
		if ( bounds.x < screenBounds.x ) {
			bounds.x = screenBounds.x;
			aligned = true;
		}
		
		if ( bounds.x + bounds.width > screenBounds.x + screenBounds.width ) {
			bounds.x = screenBounds.x + screenBounds.width - bounds.width;
			aligned = true;
		}
		
		if ( bounds.y + bounds.height > screenBounds.y + screenBounds.height ) {
			bounds.y = screenBounds.y + screenBounds.height - bounds.height;
			aligned = true;
		}
		
		if ( bounds.y < screenBounds.y ) {
			bounds.y = screenBounds.y;
			aligned = true;
		}
		
		if ( aligned ) {
			settings.set( centerXSetting, (int) bounds.getCenterX() );
			settings.set( centerYSetting, (int) bounds.getCenterY() );
		}
	}
	
	@Override
	public void buildConfigPopup( final IPopupMenu popupMenu ) {
		final XCheckBoxMenuItem lockMenuItem = new XCheckBoxMenuItem( "_Lock", Icons.F_LOCK.get() );
		lockMenuItem.setSelected( settings.get( lockedSetting ) );
		lockMenuItem.addActionListener( new ActionAdapter() {
			@Override
			public void actionPerformed( final ActionEvent event ) {
				settings.set( lockedSetting, lockMenuItem.isSelected() );
			}
		} );
		popupMenu.asPopupMenu().add( lockMenuItem );
		
		final XMenu opacityMenu = new XMenu( "_Opacity", Icons.F_LAYER_TRANSPARENT.get() );
		final int opacity = settings.get( opacitySetting );
		for ( int i = opacitySetting.getMaxValue(); i >= opacitySetting.getMinValue(); i -= 10 ) {
			final XCheckBoxMenuItem opacityMenuItem = new XCheckBoxMenuItem( i == 100 ? i + "% (Opaque)" : i + "%" );
			if ( opacity == i )
				opacityMenuItem.setSelected( true );
			final int i_ = i;
			opacityMenuItem.addActionListener( new ActionAdapter() {
				@Override
				public void actionPerformed( final ActionEvent event ) {
					settings.set( opacitySetting, i_ );
				}
			} );
			opacityMenu.add( opacityMenuItem );
		}
		popupMenu.asPopupMenu().add( opacityMenu );
		
		final XCheckBoxMenuItem focusableMenuItem = new XCheckBoxMenuItem( "_Focusable", Icons.F_LAYER_ARRANGE.get() );
		focusableMenuItem.setSelected( settings.get( focusableSetting ) );
		focusableMenuItem.addActionListener( new ActionAdapter() {
			@Override
			public void actionPerformed( final ActionEvent event ) {
				settings.set( focusableSetting, focusableMenuItem.isSelected() );
			}
		} );
		popupMenu.asPopupMenu().add( focusableMenuItem );
		
		final XCheckBoxMenuItem allowOutMainScrSettingMenuItem = new XCheckBoxMenuItem( "_Allow outside the main screen", Icons.F_LAYER_GROUP.get() );
		allowOutMainScrSettingMenuItem.setSelected( settings.get( allowOutMainScrSetting ) );
		allowOutMainScrSettingMenuItem.addActionListener( new ActionAdapter() {
			@Override
			public void actionPerformed( final ActionEvent event ) {
				settings.set( allowOutMainScrSetting, allowOutMainScrSettingMenuItem.isSelected() );
			}
		} );
		popupMenu.asPopupMenu().add( allowOutMainScrSettingMenuItem );
	}
	
}
