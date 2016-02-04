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

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import hu.scelightapibase.action.IAction;
import hu.sllauncher.action.XAction;
import hu.sllauncher.gui.comp.XLabel;
import hu.sllauncher.gui.icon.LRIcon;
import hu.sllauncher.service.env.LEnv;
import hu.sllauncher.service.settings.LSettings;
import hu.sllauncher.service.sound.Sound;
import hu.sllauncher.util.Holder;

/**
 * GUI utilities of the launcher.
 * 
 * @author Andras Belicza
 */
public class LGuiUtils {
	
	/** Constant for the left mouse button mask. */
	public static final int	MOUSE_BTN_LEFT	 = MouseEvent.BUTTON1;
											 
	/** Constant for the right mouse button mask. */
	public static final int	MOUSE_BTN_RIGHT	 = MouseEvent.BUTTON3;
											 
	/** Constant for the middle mouse button mask. */
	public static final int	MOUSE_BTN_MIDDLE = MouseEvent.BUTTON2;
											 
											 
	/**
	 * Sets the Look And Feel specified by its name.
	 * 
	 * <p>
	 * This method does not log in case of errors, so can be called before the logger is initialized.
	 * </p>
	 * 
	 * @param lafName name of the Look And Feel to be set
	 * @return the exception if any was thrown while setting the specified Look and Feel
	 */
	public static Exception setLaf( final String lafName ) {
		final Holder< Exception > exception = new Holder< >();
		runInEDT( new Runnable() {
			@Override
			public void run() {
				for ( final LookAndFeelInfo lookAndFeelInfo : UIManager.getInstalledLookAndFeels() )
					if ( lafName.equals( lookAndFeelInfo.getName() ) ) {
						try {
							UIManager.setLookAndFeel( lookAndFeelInfo.getClassName() );
						} catch ( final Exception e ) {
							exception.value = e;
						}
						break;
					}
			}
		} );
		
		return exception.value;
	}
	
	/**
	 * Wraps a component in a {@link JPanel} with {@link FlowLayout}, and returns it.
	 * 
	 * @param comp component to be wrapped
	 * @return a {@link JPanel} wrapping the specified component
	 * 		
	 * @see #wrapInPanel(JComponent, LayoutManager)
	 */
	public static JPanel wrapInPanel( final JComponent comp ) {
		return wrapInPanel( comp, new FlowLayout() );
	}
	
	/**
	 * Wraps a component in a {@link JPanel} with the specified layout manager, and returns it.
	 * 
	 * @param comp component to be wrapped
	 * @param lm layout manager to use for the panel
	 * @return a {@link JPanel} wrapping the specified component
	 * 		
	 * @see #wrapInPanel(JComponent)
	 */
	public static JPanel wrapInPanel( final JComponent comp, final LayoutManager lm ) {
		final JPanel p = new JPanel( lm );
		p.add( comp );
		return p;
	}
	
	/**
	 * Changes the font of the specified component to {@link Font#ITALIC italic}.
	 * 
	 * @param <T> type of the component
	 * @param comp the component whose font to change
	 * @return the component
	 * 		
	 * @see #boldFont(Component)
	 */
	public static < T extends Component > T italicFont( final T comp ) {
		final Font oldFont = comp.getFont();
		comp.setFont( oldFont.deriveFont( oldFont.getStyle() | Font.ITALIC ) );
		return comp;
	}
	
	/**
	 * Changes the font of the specified component to {@link Font#BOLD bold}.
	 * 
	 * @param <T> type of the component
	 * @param comp the component whose font to change
	 * @return the component
	 * 		
	 * @see #italicFont(Component)
	 */
	public static < T extends Component > T boldFont( final T comp ) {
		final Font oldFont = comp.getFont();
		comp.setFont( oldFont.deriveFont( oldFont.getStyle() | Font.BOLD ) );
		return comp;
	}
	
	/**
	 * Resizes a window by setting its bounds to maximum that fits inside the default screen having the specified margin around it, and centers the window on
	 * the screen.
	 * 
	 * <p>
	 * The implementation takes the screen insets (for example space occupied by task bar) into account.
	 * </p>
	 * 
	 * @param window window to be resized
	 * @param margin margin to leave around the window
	 * @param maxSize optional parameter defining a maximum size
	 */
	public static void maximizeWindowWithMargin( final Window window, final int margin, final Dimension maxSize ) {
		// Maybe use window.getGraphicsConfiguration() (it probably accounts multi-screens!)
		// Edit: it does, but setLocationRelativeTo() at the end will always use the default screen device!
		GraphicsConfiguration gconfig = window.getGraphicsConfiguration();
		if ( gconfig == null )
			GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
			
		final Rectangle bounds = gconfig.getBounds();
		final Insets insets = Toolkit.getDefaultToolkit().getScreenInsets( gconfig );
		
		final int width = bounds.width - insets.left - insets.right - margin * 2;
		final int height = bounds.height - insets.top - insets.bottom - margin * 2;
		
		if ( maxSize == null )
			window.setSize( width, height );
		else
			window.setSize( Math.min( width, maxSize.width ), Math.min( height, maxSize.height ) );
			
		// And finally center on screen
		// window.setLocationRelativeTo( null ) always centers on the main screen, so:
		window.setLocationRelativeTo( window.getOwner() );
	}
	
	/**
	 * If the specified button is created using an {@link XAction}, this method registers a {@link PropertyChangeListener} to the button for the
	 * {@link AbstractButton#ICON_CHANGED_PROPERTY} property, and creates and sets the disabled icon using the {@link XAction#ricon} as the icon source and
	 * {@link LSettings#TOOL_BAR_ICON_SIZE} as the size.
	 * 
	 * <p>
	 * <b>Problem and its origin:</b> buttons having an icon not an instance of {@link ImageIcon} will have the same disabled icon (and not a grayed version).
	 * This is the case with buttons created using an {@link XAction} whose large icon property will be used for the button's icon which is the icon returned by
	 * {@link LRIcon#size(int)}).
	 * </p>
	 * 
	 * @param button button to have disabled image auto-created
	 */
	public static void autoCreateDisabledImage( final JButton button ) {
		final Action action = button.getAction();
		if ( !( action instanceof IAction ) )
			return;
			
		final IAction xaction = (IAction) action;
		
		button.addPropertyChangeListener( AbstractButton.ICON_CHANGED_PROPERTY, new PropertyChangeListener() {
			{
				// Set initial disabled image
				propertyChange( null );
			}
			
			@Override
			public void propertyChange( final PropertyChangeEvent event ) {
				button.setDisabledIcon( xaction.getRicon().size( LEnv.LAUNCHER_SETTINGS.get( LSettings.TOOL_BAR_ICON_SIZE ), true, true ) );
			}
		} );
	}
	
	/**
	 * Runs the specified task in the EDT.<br>
	 * If the current thread is the EDT, simply <code>task.run()</code> will be called. Else {@link SwingUtilities#invokeAndWait(Runnable)} will be used.
	 * 
	 * @param task task to be run in the EDT
	 * @return the exception if thrown by {@link SwingUtilities#invokeAndWait(Runnable)}; <code>null</code> otherwise
	 */
	public static Exception runInEDT( final Runnable task ) {
		if ( SwingUtilities.isEventDispatchThread() )
			task.run();
		else {
			try {
				SwingUtilities.invokeAndWait( task );
			} catch ( final Exception e ) {
				e.printStackTrace();
				LEnv.LOGGER.error( "Exeption thrown while executing task in EDT!", e );
				return e;
			}
		}
		
		return null;
	}
	
	/**
	 * Shows a YES-NO confirmation dialog to the user.
	 * <p>
	 * This method blocks until the dialog is closed.
	 * </p>
	 * 
	 * @param messages messages and components to be displayed to the user
	 * @return true if YES option was chosen, false otherwise (including closing the dialog)
	 */
	public static boolean confirm( final Object... messages ) {
		Sound.beepOnConfirmation();
		
		return JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog( LEnv.CURRENT_GUI_FRAME.get(), messages, "Confirmation", JOptionPane.YES_NO_OPTION,
		        JOptionPane.WARNING_MESSAGE );
	}
	
	/**
	 * Shows a YES-NO confirmation dialog to the user about retrying a failed operation.
	 * <p>
	 * This method blocks until the dialog is closed.
	 * </p>
	 * <p>
	 * Can be called outside of the EDT.
	 * </p>
	 * 
	 * <p>
	 * Example usage:
	 * 
	 * <pre>
	 * <blockquote style='border:1px solid black'>
	 * while ( !deleteFile( file ) )
	 * 	if ( !askRetry( &quot;Could not delete file:&quot;, file ) )
	 * 		break;
	 * </blockquote>
	 * </pre>
	 * 
	 * </p>
	 * 
	 * @param messages messages and components to be displayed to the user; a "Retry?" label will be added
	 * @return false if NO option was chosen, true otherwise (also when the dialog is closed)
	 */
	public static boolean askRetry( final Object... messages ) {
		// Pressing ESC or closing with the X icon, we want to return TRUE!
		// Only the NO button should mean NO (false return value).
		final AtomicBoolean answer = new AtomicBoolean();
		
		LGuiUtils.runInEDT( new Runnable() {
			@Override
			public void run() {
				Sound.beepOnConfirmation();
				answer.set( JOptionPane.NO_OPTION == JOptionPane.showConfirmDialog( LEnv.CURRENT_GUI_FRAME.get(),
		                new Object[] { messages, " ", LGuiUtils.boldFont( new XLabel( "Retry?" ) ) }, "Error!", JOptionPane.YES_NO_OPTION,
		                JOptionPane.ERROR_MESSAGE ) );
			}
		} );
		
		return !answer.get();
	}
	
	/**
	 * Shows an info message.
	 * <p>
	 * This method blocks until the dialog is closed.
	 * </p>
	 * 
	 * @param messages info messages and components to be displayed
	 */
	public static void showInfoMsg( final Object... messages ) {
		Sound.beepOnInfo();
		
		JOptionPane.showMessageDialog( LEnv.CURRENT_GUI_FRAME.get(), messages, "Info", JOptionPane.INFORMATION_MESSAGE );
	}
	
	/**
	 * Returns a nice string representation of the specified key stroke.
	 * <p>
	 * For example returns <code>" (Ctrl+P)"</code> for the keystroke of CTRL+P.
	 * </p>
	 * 
	 * @param keyStroke key stroke whose nice string representation to return
	 * @return a nice string representation of the specified key stroke
	 */
	public static String keyStrokeToString( final KeyStroke keyStroke ) {
		final StringBuilder sb = new StringBuilder( " (" );
		
		if ( keyStroke.getModifiers() != 0 )
			sb.append( KeyEvent.getKeyModifiersText( keyStroke.getModifiers() ) ).append( '+' );
			
		sb.append( KeyEvent.getKeyText( keyStroke.getKeyCode() ) ).append( ')' );
		
		return sb.toString();
	}
	
}
