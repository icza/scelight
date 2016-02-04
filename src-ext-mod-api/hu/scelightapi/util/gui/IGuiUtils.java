/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelightapi.util.gui;

import hu.scelightapi.util.IUtils;
import hu.scelightapibase.action.IAction;
import hu.scelightapibase.bean.person.IPersonNameBean;
import hu.scelightapibase.gui.comp.ILink;
import hu.scelightapibase.gui.icon.IRIcon;
import hu.scelightapibase.service.lang.ILanguage;
import hu.scelightapibase.util.gui.HasIcon;
import hu.scelightapibase.util.gui.HasRIcon;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Window;
import java.beans.PropertyChangeListener;
import java.net.URL;
import java.nio.file.Path;
import java.util.Date;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * (Swing) GUI utilities of the application.
 * 
 * <p>
 * To make everyone's GUI life easier.
 * </p>
 * 
 * @author Andras Belicza
 * 
 * @see IUtils
 */
public interface IGuiUtils {
	
	/**
	 * Changes the font of the specified component to {@link Font#ITALIC italic}.
	 * 
	 * @param <T> type of the component
	 * @param comp the component whose font to change
	 * @return the component
	 * 
	 * @see #boldFont(Component)
	 */
	< T extends Component > T italicFont( T comp );
	
	/**
	 * Changes the font of the specified component to {@link Font#BOLD bold}.
	 * 
	 * @param <T> type of the component
	 * @param comp the component whose font to change
	 * @return the component
	 * 
	 * @see #italicFont(Component)
	 */
	< T extends Component > T boldFont( T comp );
	
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
	void maximizeWindowWithMargin( Window window, int margin, Dimension maxSize );
	
	/**
	 * If the specified button is created using an instance of {@link IAction} returned by the API, this method registers a {@link PropertyChangeListener} to
	 * the button for the {@link AbstractButton#ICON_CHANGED_PROPERTY} property, and creates and sets the disabled icon using the {@link IAction#getRicon()} as
	 * the icon source and the tool bar icon size setting as the size.
	 * 
	 * <p>
	 * <b>Problem and its origin:</b> buttons having an icon not an instance of {@link ImageIcon} will have the same disabled icon (and not a grayed version).
	 * This is the case with buttons created using an {@link IAction} whose large icon property will be used for the button's icon which is the icon returned by
	 * {@link IRIcon#size(int)}).
	 * </p>
	 * 
	 * @param button button to have disabled image auto-created
	 */
	void autoCreateDisabledImage( final JButton button );
	
	/**
	 * Runs the specified task in the EDT.<br>
	 * If the current thread is the EDT, simply <code>task.run()</code> will be called. Else {@link SwingUtilities#invokeAndWait(Runnable)} will be used.
	 * 
	 * @param task task to be run in the EDT
	 * @return the exception if thrown by {@link SwingUtilities#invokeAndWait(Runnable)}; <code>null</code> otherwise
	 */
	Exception runInEDT( Runnable task );
	
	/**
	 * Shows a YES-NO confirmation dialog to the user.
	 * <p>
	 * This method blocks until the dialog is closed.
	 * </p>
	 * 
	 * @param messages messages and components to be displayed to the user
	 * @return true if YES option was chosen, false otherwise (including closing the dialog)
	 */
	boolean confirm( Object... messages );
	
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
	boolean askRetry( Object... messages );
	
	/**
	 * Shows an info message.
	 * <p>
	 * This method blocks until the dialog is closed.
	 * </p>
	 * 
	 * @param messages info messages and components to be displayed
	 */
	void showInfoMsg( Object... messages );
	
	/**
	 * Shows an error message.
	 * <p>
	 * This method blocks until the dialog is closed.
	 * </p>
	 * 
	 * @param messages error messages and components to be displayed
	 */
	void showErrorMsg( Object... messages );
	
	/**
	 * Shows a warning message.
	 * <p>
	 * This method blocks until the dialog is closed.
	 * </p>
	 * 
	 * @param messages warning messages and components to be displayed
	 */
	void showWarningMsg( Object... messages );
	
	/**
	 * Shows an input message.
	 * <p>
	 * This method blocks until the dialog is closed.
	 * </p>
	 * 
	 * @param messages input messages and components to be displayed
	 * @return true if OK option was chosen, false otherwise (including closing the dialog)
	 */
	boolean showInputMsg( Object... messages );
	
	/**
	 * Returns a nice string representation of the specified key stroke.
	 * <p>
	 * For example returns <code>" (Ctrl+P)"</code> for the keystroke of CTRL+P.
	 * </p>
	 * 
	 * @param keyStroke key stroke whose nice string representation to return
	 * @return a nice string representation of the specified key stroke
	 */
	String keyStrokeToString( KeyStroke keyStroke );
	
	/**
	 * Sets the enabled property of a component tree recursively.
	 * 
	 * @param component component to start from
	 * @param enabled value of the enabled property to be set
	 */
	void setComponentTreeEnabled( Component component, boolean enabled );
	
	/**
	 * Creates a new {@link ILink} which executes the specified action.
	 * 
	 * @param text text of the link
	 * @param action action to be executed when clicked on the link
	 * @return a new {@link ILink} which executes the specified action
	 */
	ILink linkForAction( String text, IAction action );
	
	/**
	 * Makes a component drag-scrollable.
	 * 
	 * <p>
	 * If a component is drag-scrollable and the user tries to drag the component, it will be scrolled if it is added to a scroll pane. Also changes the mouse
	 * cursor to "MOVE" over this component.
	 * </p>
	 * 
	 * @param component component to be made drag-scrollable
	 */
	void makeComponentDragScrollable( JComponent component );
	
	/**
	 * Creates a new {@link Color} from the specified color using the specified alpha.
	 * 
	 * @param c color to create a new {@link Color} from
	 * @param alpha alpha to be set to the new color
	 * @return a new {@link Color} from the specified color using the specified alpha
	 */
	Color colorWithAlpha( Color c, int alpha );
	
	
	/**
	 * Performs the advanced, unified rendering logic which targets a {@link JLabel}.
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
	 * @param l the renderer label
	 * @param value the rendered value object
	 * @param isSelected tells if the rendered value is selected
	 * @param hasFocus tells if the rendered value has focus
	 * @return the renderer label
	 */
	JLabel renderToLabel( JLabel l, Object value, boolean isSelected, boolean hasFocus );
	
	/**
	 * Shows the email sender dialog.
	 * 
	 * <p>
	 * This method only returns when the dialog is closed.
	 * </p>
	 * 
	 * @param to initial addresses (list of email addresses)
	 * 
	 * @see IUtils#checkEmailSettings()
	 */
	void showSendEmailDialog( String to );
	
	/**
	 * Shows the email sender dialog.
	 * 
	 * <p>
	 * This method only returns when the dialog is closed.
	 * </p>
	 * 
	 * @param attachmentList default email attachments
	 * 
	 * @see IUtils#checkEmailSettings()
	 */
	void showSendEmailDialog( List< Path > attachmentList );
	
	/**
	 * Shows the email sender dialog.
	 * 
	 * <p>
	 * This method only returns when the dialog is closed.
	 * </p>
	 * 
	 * @param title dialog title
	 * @param attachmentList default email attachments
	 * @param to initial addresses (list of email addresses)
	 * 
	 * @see IUtils#checkEmailSettings()
	 */
	void showSendEmailDialog( String title, List< Path > attachmentList, String to );
	
}
