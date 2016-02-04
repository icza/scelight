/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight.util.gui;

import hu.scelight.action.Actions;
import hu.scelight.service.env.Env;
import hu.scelightapibase.action.IAction;
import hu.scelightapibase.util.iface.Consumer;
import hu.sllauncher.gui.comp.Link;
import hu.sllauncher.service.sound.Sound;
import hu.sllauncher.util.gui.LGuiUtils;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Rectangle;
import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;
import javax.swing.JOptionPane;

/**
 * GUI utilities of the application.
 * 
 * @author Andras Belicza
 */
public class GuiUtils extends LGuiUtils {
	
	/**
	 * Creates a new {@link Link} which executes the specified action.
	 * 
	 * @param text text of the link
	 * @param action action to be executed when clicked on the link
	 * @return a new {@link Link} which executes the specified action
	 */
	public static Link linkForAction( final String text, final IAction action ) {
		final Link link = new Link( text, new Consumer< MouseEvent >() {
			@Override
			public void consume( final MouseEvent event ) {
				action.actionPerformed( null );
			}
		} );
		
		link.setIcon( action.getRicon().get() );
		
		return link;
	}
	
	/**
	 * Shows an error message.
	 * <p>
	 * This method blocks until the dialog is closed.
	 * </p>
	 * 
	 * @param messages error messages and components to be displayed
	 */
	public static void showErrorMsg( final Object... messages ) {
		Sound.beepOnError();
		
		JOptionPane.showMessageDialog( Env.CURRENT_GUI_FRAME.get(), new Object[] { messages, " ", linkForAction( "View Logs...", Actions.ABOUT_LOGS ) },
		        "Error!", JOptionPane.ERROR_MESSAGE );
	}
	
	/**
	 * Shows a warning message.
	 * <p>
	 * This method blocks until the dialog is closed.
	 * </p>
	 * 
	 * @param messages warning messages and components to be displayed
	 */
	public static void showWarningMsg( final Object... messages ) {
		Sound.beepOnWarning();
		
		JOptionPane.showMessageDialog( Env.CURRENT_GUI_FRAME.get(), messages, "Warning!", JOptionPane.WARNING_MESSAGE );
	}
	
	/**
	 * Shows an input message.
	 * <p>
	 * This method blocks until the dialog is closed.
	 * </p>
	 * 
	 * @param messages input messages and components to be displayed
	 * @return true if OK option was chosen, false otherwise (including closing the dialog)
	 */
	public static boolean showInputMsg( final Object... messages ) {
		Sound.beepOnInput();
		
		return JOptionPane.OK_OPTION == JOptionPane.showConfirmDialog( Env.CURRENT_GUI_FRAME.get(), messages, "Input", JOptionPane.OK_CANCEL_OPTION,
		        JOptionPane.QUESTION_MESSAGE );
	}
	
	/**
	 * Sets the enabled property of a component tree recursively.
	 * 
	 * @param component component to start from
	 * @param enabled value of the enabled property to be set
	 */
	public static void setComponentTreeEnabled( final Component component, final boolean enabled ) {
		component.setEnabled( enabled );
		
		if ( component instanceof Container ) {
			final Container container = (Container) component;
			for ( int i = container.getComponentCount() - 1; i >= 0; i-- )
				setComponentTreeEnabled( container.getComponent( i ), enabled );
		}
	}
	
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
	public static void makeComponentDragScrollable( final JComponent component ) {
		final MouseAdapter dragHandler = new MouseAdapter() {
			int dragStartX, dragStartY;
			
			@Override
			public void mousePressed( final MouseEvent event ) {
				if ( ( event.getModifiers() & InputEvent.BUTTON1_MASK ) != 0 ) {
					dragStartX = event.getXOnScreen();
					dragStartY = event.getYOnScreen();
				}
			};
			
			@Override
			public void mouseDragged( final MouseEvent event ) {
				if ( ( event.getModifiers() & InputEvent.BUTTON1_MASK ) != 0 ) {
					final Rectangle visibleRect = component.getVisibleRect();
					visibleRect.x += ( dragStartX - event.getXOnScreen() ) * 2;
					visibleRect.y += ( dragStartY - event.getYOnScreen() ) * 2;
					component.scrollRectToVisible( visibleRect );
					dragStartX = event.getXOnScreen();
					dragStartY = event.getYOnScreen();
				}
			}
		};
		
		component.addMouseListener( dragHandler );
		component.addMouseMotionListener( dragHandler );
		
		component.setCursor( Cursor.getPredefinedCursor( Cursor.MOVE_CURSOR ) );
	}
	
	/**
	 * Creates a new {@link Color} from the specified color using the specified alpha.
	 * 
	 * @param c color to create a new {@link Color} from
	 * @param alpha alpha to be set to the new color
	 * @return a new {@link Color} from the specified color using the specified alpha
	 */
	public static Color colorWithAlpha( final Color c, final int alpha ) {
		// + is important, cannot be OR
		return new Color( c.getRGB() + ( alpha << 24 ), true );
	}
	
}
