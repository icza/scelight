/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.sllauncher.gui.comp;

import hu.scelightapibase.bean.settings.ISettingChangeEvent;
import hu.scelightapibase.bean.settings.ISettingChangeListener;
import hu.scelightapibase.gui.comp.IDialog;
import hu.sllauncher.gui.icon.LIcons;
import hu.sllauncher.gui.setting.LSettingsGui;
import hu.sllauncher.service.env.LEnv;
import hu.sllauncher.service.settings.LSettings;
import hu.sllauncher.util.gui.LGuiUtils;
import hu.sllauncher.util.gui.adapter.ActionAdapter;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

/**
 * A customized {@link JDialog} class.
 * 
 * @author Andras Belicza
 */
@SuppressWarnings( "serial" )
public class XDialog extends JDialog implements IDialog {
	
	/** Default value of the default dialog size. */
	public static final Dimension DEFAULT_DEFAULT_SIZE = new Dimension( 1200, 800 );
	
	/** Custom border panel functioning as the content panel. */
	protected final BorderPanel   cp                   = new BorderPanel();
	
	/** Buttons panel. */
	private JPanel                buttonsPanel;
	
	/** Component to be focused on first show. */
	protected JComponent          focusTargetOnFirstShow;
	
	/** Default size of this dialog. */
	protected Dimension           defaultSize          = DEFAULT_DEFAULT_SIZE;
	
	/**
	 * Creates a new {@link XDialog}.
	 * 
	 * @param owner reference to the owner frame
	 */
	public XDialog( final Frame owner ) {
		super( owner );
		
		setDefaultCloseOperation( DO_NOTHING_ON_CLOSE );
		
		addWindowListener( new WindowAdapter() {
			@Override
			public void windowClosing( final WindowEvent event ) {
				close();
			}
		} );
		
		setModalityType( ModalityType.APPLICATION_MODAL );
		
		// Default inner border
		cp.setBorder( BorderFactory.createEmptyBorder( 10, 10, 10, 10 ) );
		
		setContentPane( cp );
		
		// Register window listener to focus intended component on first activation
		addWindowListener( new WindowAdapter() {
			@Override
			public void windowActivated( final WindowEvent event ) {
				removeWindowListener( this ); // One time focusing, remove ourselves
				if ( focusTargetOnFirstShow != null ) {
					SwingUtilities.invokeLater( new Runnable() {
						@Override
						public void run() {
							focusTargetOnFirstShow.requestFocusInWindow();
						}
					} );
				}
			}
		} );
		
		// Register ESC to close the dialog
		final Object actionKey = new Object();
		getLayeredPane().getInputMap( JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT ).put( KeyStroke.getKeyStroke( KeyEvent.VK_ESCAPE, 0 ), actionKey );
		getLayeredPane().getActionMap().put( actionKey, new AbstractAction() {
			@Override
			public void actionPerformed( final ActionEvent event ) {
				close();
			}
		} );
		
		// Register a setting change listener to repaint if icon scaling quality changes
		ISettingChangeListener scl = new ISettingChangeListener() {
			@Override
			public void valuesChanged( final ISettingChangeEvent event ) {
				if ( event.affected( LSettings.ICON_SCALING_QUALITY ) )
					cp.repaint();
			}
		};
		LSettingsGui.addBindExecuteScl( scl, LEnv.LAUNCHER_SETTINGS, LSettings.ICON_SCALING_QUALITY.SELF_SET, cp );
		
		// Dialog window controls
		final JPanel glassPane = (JPanel) getGlassPane();
		glassPane.setLayout( new FlowLayout( FlowLayout.RIGHT, 2, 2 ) );
		final XButton defaultSizeButton = new XButton( LIcons.F_APPLICATION_RESIZE_ACTUAL.get() );
		defaultSizeButton.setToolTipText( "Restore Default Size" );
		defaultSizeButton.configureAsIconButton();
		defaultSizeButton.setPressedIcon( LIcons.F_APPLICATION_RESIZE_ACTUAL.size( 14 ) );
		defaultSizeButton.addActionListener( new ActionAdapter() {
			@Override
			public void actionPerformed( final ActionEvent event ) {
				restoreDefaultSize();
			}
		} );
		glassPane.add( defaultSizeButton );
		final XButton enlargeButton = new XButton( LIcons.F_APPLICATION_RESIZE.get() );
		enlargeButton.setToolTipText( "Enlarge Dialog" );
		enlargeButton.configureAsIconButton();
		enlargeButton.setPressedIcon( LIcons.F_APPLICATION_RESIZE.size( 14 ) );
		enlargeButton.addActionListener( new ActionAdapter() {
			@Override
			public void actionPerformed( final ActionEvent event ) {
				enlargeDialog();
			}
		} );
		glassPane.add( enlargeButton );
		glassPane.setVisible( true );
	}
	
	@Override
	public XDialog asDialog() {
		return this;
	}
	
	@Override
	public void setDefaultSize( final Dimension defaultSize ) {
		this.defaultSize = defaultSize;
	}
	
	@Override
	public Dimension getDefaultSize() {
		// Return a clone because Dimension is mutable, and we don't want the default-default size to be changed.
		return new Dimension( defaultSize );
	}
	
	@Override
	public Dimension getDefaultDefaultSize() {
		return new Dimension( DEFAULT_DEFAULT_SIZE );
	}
	
	@Override
	public BorderPanel getCp() {
		return cp;
	}
	
	@Override
	public void enlargeDialog() {
		LGuiUtils.maximizeWindowWithMargin( this, 10, null );
	}
	
	@Override
	public void restoreDefaultSize() {
		LGuiUtils.maximizeWindowWithMargin( this, 20, defaultSize );
	}
	
	@Override
	public void setFocusTargetOnFirstShow( final JComponent focusTarget ) {
		focusTargetOnFirstShow = focusTarget;
	}
	
	@Override
	public XButton addCloseButton( final String text ) {
		final XButton closeButton = new XButton( text );
		closeButton.addActionListener( new ActionAdapter() {
			@Override
			public void actionPerformed( final ActionEvent event ) {
				close();
			}
		} );
		getButtonsPanel().add( closeButton );
		
		return closeButton;
	}
	
	@Override
	public JPanel getButtonsPanel() {
		if ( buttonsPanel == null ) {
			buttonsPanel = new JPanel( new FlowLayout( FlowLayout.RIGHT ) );
			cp.addSouth( buttonsPanel );
		}
		
		return buttonsPanel;
	}
	
	/**
	 * @see #customOnClose()
	 */
	@Override
	public void close() {
		// First close all child windows
		for ( final Window w : getOwnedWindows() ) {
			if ( w instanceof XFrame )
				( (XFrame) w ).close();
			if ( w instanceof XDialog )
				( (XDialog) w ).close();
		}
		
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
	
}
