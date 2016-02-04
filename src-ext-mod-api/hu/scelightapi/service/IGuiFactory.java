/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelightapi.service;

import hu.scelightapi.IServices;
import hu.scelightapi.gui.comp.ICheckBoxMenuItem;
import hu.scelightapi.gui.comp.IIndicatorTextArea;
import hu.scelightapi.gui.comp.IList;
import hu.scelightapi.gui.comp.IMenu;
import hu.scelightapi.gui.comp.IMenuItem;
import hu.scelightapi.gui.comp.IPasswordField;
import hu.scelightapi.gui.comp.IPopupMenu;
import hu.scelightapi.gui.comp.ITabbedPane;
import hu.scelightapi.gui.comp.ITemplateField;
import hu.scelightapi.gui.comp.IToolBarForList;
import hu.scelightapi.gui.comp.IToolBarForTree;
import hu.scelightapi.gui.overlaycard.IConfigPopupBuilder;
import hu.scelightapi.gui.overlaycard.IOverlayCard;
import hu.scelightapi.gui.overlaycard.OverlayCardParams;
import hu.scelightapi.gui.setting.ISettingsGui;
import hu.scelightapibase.action.IAction;
import hu.scelightapibase.gui.comp.IBorderPanel;
import hu.scelightapibase.gui.comp.IBrowser;
import hu.scelightapibase.gui.comp.IButton;
import hu.scelightapibase.gui.comp.ICheckBox;
import hu.scelightapibase.gui.comp.IColorIcon;
import hu.scelightapibase.gui.comp.IDialog;
import hu.scelightapibase.gui.comp.IFileChooser;
import hu.scelightapibase.gui.comp.IGridBagPanel;
import hu.scelightapibase.gui.comp.IHelpIcon;
import hu.scelightapibase.gui.comp.IIndicatorTextField;
import hu.scelightapibase.gui.comp.ILabel;
import hu.scelightapibase.gui.comp.ILink;
import hu.scelightapibase.gui.comp.IModestLabel;
import hu.scelightapibase.gui.comp.IPathField;
import hu.scelightapibase.gui.comp.IProgressBar;
import hu.scelightapibase.gui.comp.IScrollPane;
import hu.scelightapibase.gui.comp.ISpinner;
import hu.scelightapibase.gui.comp.ISplitPane;
import hu.scelightapibase.gui.comp.IStatusLabel;
import hu.scelightapibase.gui.comp.IStatusLabel.IStatusType;
import hu.scelightapibase.gui.comp.ITextArea;
import hu.scelightapibase.gui.comp.ITextField;
import hu.scelightapibase.gui.comp.ITipIcon;
import hu.scelightapibase.gui.comp.IToolBar;
import hu.scelightapibase.gui.comp.IToolBarForTable;
import hu.scelightapibase.gui.comp.ITree;
import hu.scelightapibase.gui.comp.combobox.IComboBox;
import hu.scelightapibase.gui.comp.multipage.IMultiPageComp;
import hu.scelightapibase.gui.comp.multipage.IPage;
import hu.scelightapibase.gui.comp.multipage.IPageCompCreator;
import hu.scelightapibase.gui.comp.table.ITable;
import hu.scelightapibase.gui.comp.table.renderer.IBarCodeView;
import hu.scelightapibase.gui.comp.table.renderer.IProgressBarView;
import hu.scelightapibase.gui.icon.IRIcon;
import hu.scelightapibase.util.IRHtml;
import hu.scelightapibase.util.gui.IRenderablePair;
import hu.scelightapibase.util.iface.Consumer;

import java.awt.Color;
import java.awt.Component;
import java.awt.Frame;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JSplitPane;
import javax.swing.KeyStroke;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.tree.TreeNode;

/**
 * (Swing) GUI factory interface.
 * 
 * <p>
 * Used to create new instances of GUI API interfaces.
 * </p>
 * 
 * <p>
 * For a consistent UI and user experience it is strongly recommended to use extended components offered here whenever possible. Also these extended components
 * follow the user interface related settings.<br>
 * You can mix extended components offered by the API and "basic" Swing components, but by using basic Swing components you'll lose the extra functionality
 * provided by the extended components - and future ones that will be added in the future.
 * </p>
 * 
 * <p>
 * For components that are initialized from setting values or are bound to them, please see {@link ISettingsGui}.
 * </p>
 * 
 * @author Andras Belicza
 * 
 * @see IFactory
 * @see ISettingsGui
 */
public interface IGuiFactory {
	
	/**
	 * Creates a new {@link ILabel}.
	 * 
	 * @return a new {@link ILabel}
	 */
	ILabel newLabel();
	
	/**
	 * Creates a new {@link ILabel}.
	 * 
	 * @param text text of the label
	 * @return a new {@link ILabel}
	 */
	ILabel newLabel( String text );
	
	/**
	 * Creates a new {@link ILabel}.
	 * 
	 * @param text text of the label
	 * @param halignment horizontal alignment; one of <code>SwingConstants: LEFT, CENTER, RIGHT, LEADING or TRAILING</code>
	 * @return a new {@link ILabel}
	 */
	ILabel newLabel( String text, int halignment );
	
	/**
	 * Creates a new {@link ILabel}.
	 * 
	 * @param icon icon of the label
	 * @return a new {@link ILabel}
	 */
	ILabel newLabel( Icon icon );
	
	/**
	 * Creates a new {@link ILabel}.
	 * 
	 * @param text text of the label
	 * @param icon icon of the label
	 * @return a new {@link ILabel}
	 */
	ILabel newLabel( String text, Icon icon );
	
	/**
	 * Creates a new {@link ILabel}.
	 * 
	 * @param text text of the label
	 * @param icon icon of the label
	 * @param halignment horizontal alignment; one of <code>SwingConstants: LEFT, CENTER, RIGHT, LEADING or TRAILING</code>
	 * @return a new {@link ILabel}
	 */
	ILabel newLabel( String text, Icon icon, int halignment );
	
	/**
	 * Creates a new {@link IRIcon} from the specified image location.
	 * 
	 * <p>
	 * Created and returned {@link IRIcon}s should always be cached for performance reasons, that way the image resource represented by the {@link IRIcon} can
	 * be shared and reused.
	 * </p>
	 * 
	 * <p>
	 * Supported image formats include PNG, JPEG, GIF.
	 * </p>
	 * 
	 * @param url the URL for the image to create an {@link IRIcon} from
	 * @return a new {@link IRIcon}
	 * @see IRIcon
	 */
	IRIcon newRIcon( URL url );
	
	/**
	 * Creates a new {@link IRHtml}.
	 * 
	 * @param title title of the HTML template
	 * @param resource HTML template resource
	 * @param params param name - param value pairs to be substituted in the HTML template;<br>
	 *            param names must be in <i>raw</i> format, they will be prepended with <code>"${"</code> and postpended with <code>"}"</code>
	 * @return a new {@link IRHtml}
	 * @see IRHtml
	 */
	IRHtml newRHtml( String title, URL resource, String... params );
	
	/**
	 * Creates a new {@link IRHtml}.
	 * 
	 * @param title title of the HTML template
	 * @param resource HTML template resource
	 * @param htmlParams collection of HTML param names, values of these parameters are treated as HTML and are not escaped
	 * @param params param name - param value pairs to be substituted in the HTML template;<br>
	 *            param names must be in <i>raw</i> format, they will be prepended with <code>"${"</code> and postpended with <code>"}"</code>
	 * @return a new {@link IRHtml}
	 * @see IRHtml
	 */
	IRHtml newRHtml( String title, URL resource, Set< String > htmlParams, String... params );
	
	/**
	 * Creates a new {@link IHelpIcon}.
	 * 
	 * @param helpRhtml html template resource of the help content
	 * @return a new {@link IHelpIcon}
	 */
	IHelpIcon newHelpIcon( IRHtml helpRhtml );
	
	/**
	 * Creates a new {@link IHelpIcon}.
	 * 
	 * @param helpRhtml html template resource of the help content
	 * @param stressed tells if the help icon's appearance should be garish, stressed
	 * @return a new {@link IHelpIcon}
	 */
	IHelpIcon newHelpIcon( IRHtml helpRhtml, boolean stressed );
	
	/**
	 * Creates a new {@link ITipIcon}.
	 * 
	 * @param tipRhtml html template resource of the tip content
	 * @return a new {@link ITipIcon}
	 */
	ITipIcon newTipIcon( IRHtml tipRhtml );
	
	/**
	 * Creates a new {@link ILink} with no URL and no target action.
	 * 
	 * @return a new {@link ILink}
	 */
	ILink newLink();
	
	/**
	 * Creates a new {@link ILink} with no URL or target action.
	 * <p>
	 * Basically this is a link-styled {@link ILabel}.
	 * </p>
	 * 
	 * @param text text of the link
	 * @return a new {@link ILink}
	 */
	ILink newLink( String text );
	
	/**
	 * Creates a new {@link ILink} which opens the specified URL in the user's default browser when clicked.
	 * 
	 * @param text text of the link
	 * @param url {@link URL} to be opened when clicked
	 * @return a new {@link ILink}
	 */
	ILink newLink( String text, URL url );
	
	/**
	 * Creates a new {@link ILink} which forwards mouse click events to the specified {@link Consumer}.
	 * 
	 * @param text text of the link
	 * @param consumer consumer to be called when clicked
	 * @return a new {@link ILink}
	 */
	ILink newLink( String text, Consumer< MouseEvent > consumer );
	
	/**
	 * Creates a new {@link ILink} which forwards mouse click events to the specified {@link Consumer} and opens the specified URL in the user's default browser
	 * when clicked.
	 * 
	 * @param text text of the link
	 * @param url {@link URL} to be opened when clicked
	 * @param consumer consumer to be called when clicked
	 * @return a new {@link ILink}
	 */
	ILink newLink( String text, URL url, Consumer< MouseEvent > consumer );
	
	/**
	 * Creates a new {@link IAction}.
	 * 
	 * @param ricon optional ricon of the action
	 * @param text text of the action
	 * @param listener action listener to be called when action is performed
	 * @return a new {@link IAction}
	 */
	IAction newAction( IRIcon ricon, String text, ActionListener listener );
	
	/**
	 * Creates a new {@link IAction}.
	 * 
	 * @param ricon optional ricon of the action
	 * @param text text of the action
	 * @param comp component to bind the action's setting change listener to; basically in this case the lifetime of the scl will be bound to the specified
	 *            component's lifetime
	 * @param listener action listener to be called when action is performed
	 * @return a new {@link IAction}
	 */
	IAction newAction( IRIcon ricon, String text, JComponent comp, ActionListener listener );
	
	/**
	 * Creates a new {@link IAction}.
	 * 
	 * @param keyStroke optional hotkey of the action
	 * @param ricon optional ricon of the action
	 * @param text text of the action
	 * @param listener action listener to be called when action is performed
	 * @return a new {@link IAction}
	 */
	IAction newAction( KeyStroke keyStroke, IRIcon ricon, String text, ActionListener listener );
	
	/**
	 * Creates a new {@link IAction}.
	 * 
	 * @param keyStroke optional hotkey of the action
	 * @param ricon optional ricon of the action
	 * @param text text of the action
	 * @param comp component to bind the action's setting change listener to; basically in this case the lifetime of the scl will be bound to the specified
	 *            component's lifetime
	 * @param listener action listener to be called when action is performed
	 * @return a new {@link IAction}
	 */
	IAction newAction( KeyStroke keyStroke, IRIcon ricon, String text, JComponent comp, ActionListener listener );
	
	/**
	 * Creates a new {@link IButton}.
	 * 
	 * @return a new {@link IButton}
	 */
	IButton newButton();
	
	/**
	 * Creates a new {@link IButton}.
	 * 
	 * @param text text of the button
	 * @return a new {@link IButton}
	 */
	IButton newButton( String text );
	
	/**
	 * Creates a new {@link IButton}.
	 * 
	 * @param icon icon of the button
	 * @return a new {@link IButton}
	 */
	IButton newButton( Icon icon );
	
	/**
	 * Creates a new {@link IButton}.
	 * 
	 * @param text text of the button
	 * @param icon icon of the button
	 * @return a new {@link IButton}
	 */
	IButton newButton( String text, Icon icon );
	
	/**
	 * Creates a new {@link IButton}.
	 * 
	 * @param action action to configure the button from
	 * @return a new {@link IButton}
	 */
	IButton newButton( IAction action );
	
	/**
	 * Creates a new {@link ITextField}.
	 * 
	 * @return a new {@link ITextField}
	 */
	ITextField newTextField();
	
	/**
	 * Creates a new {@link ITextField}.
	 * 
	 * @param clearByEsc tells if text field should be cleared when the ESC key is pressed
	 * @return a new {@link ITextField}
	 */
	ITextField newTextField( boolean clearByEsc );
	
	/**
	 * Creates a new {@link ITextField}.
	 * 
	 * @param text initial text of the text field
	 * @return a new {@link ITextField}
	 */
	ITextField newTextField( String text );
	
	/**
	 * Creates a new {@link ITextField}.
	 * 
	 * @param columns the number of columns to use to calculate the preferred width; if columns is set to zero, the preferred width will be whatever naturally
	 *            results from the component implementation
	 * @return a new {@link ITextField}
	 */
	ITextField newTextField( int columns );
	
	/**
	 * Creates a new {@link ITextField}.
	 * 
	 * @param text text of the check box
	 * @param columns the number of columns to use to calculate the preferred width; if columns is set to zero, the preferred width will be whatever naturally
	 *            results from the component implementation
	 * @return a new {@link ITextField}
	 */
	ITextField newTextField( String text, int columns );
	
	/**
	 * Creates a new {@link ITextField}.
	 * 
	 * @param text text of the check box
	 * @param columns the number of columns to use to calculate the preferred width; if columns is set to zero, the preferred width will be whatever naturally
	 *            results from the component implementation
	 * @param clearByEsc tells if text field should be cleared when the ESC key is pressed
	 * @return a new {@link ITextField}
	 */
	ITextField newTextField( String text, int columns, boolean clearByEsc );
	
	/**
	 * Creates a new {@link IIndicatorTextField}.
	 * 
	 * @return a new {@link IIndicatorTextField}
	 */
	IIndicatorTextField newIndicatorTextField();
	
	/**
	 * Creates a new {@link IIndicatorTextField}.
	 * 
	 * @param text initial text to be set
	 * @return a new {@link IIndicatorTextField}
	 */
	IIndicatorTextField newIndicatorTextField( String text );
	
	/**
	 * Creates a new {@link ITextArea}.
	 * 
	 * @return a new {@link ITextArea}
	 */
	ITextArea newTextArea();
	
	/**
	 * Creates a new {@link ITextArea}.
	 * 
	 * @param text initial text of the text area
	 * @return a new {@link ITextArea}
	 */
	ITextArea newTextArea( String text );
	
	/**
	 * Creates a new {@link ITextArea}.
	 * 
	 * @param text initial text of the text area
	 * @param rows the number of rows to use to calculate the preferred height
	 * @param columns the number of columns to use to calculate the preferred width
	 * @return a new {@link ITextArea}
	 */
	ITextArea newTextArea( String text, int rows, int columns );
	
	/**
	 * Creates a new {@link IIndicatorTextArea}.
	 * 
	 * @return a new {@link IIndicatorTextArea}
	 * 
	 * @since 1.4
	 */
	IIndicatorTextArea newIndicatorTextArea();
	
	/**
	 * Creates a new {@link IIndicatorTextArea}.
	 * 
	 * @param text initial text to be set
	 * @return a new {@link IIndicatorTextArea}
	 * 
	 * @since 1.4
	 */
	IIndicatorTextArea newIndicatorTextArea( String text );
	
	/**
	 * Creates a new {@link IBorderPanel}.
	 * 
	 * @return a new {@link IBorderPanel}
	 */
	IBorderPanel newBorderPanel();
	
	/**
	 * Creates a new {@link IBorderPanel}.
	 * 
	 * @param centerComp component to be added to the center
	 * @return a new {@link IBorderPanel}
	 */
	IBorderPanel newBorderPanel( JComponent centerComp );
	
	/**
	 * Creates a new {@link IBrowser}.
	 * 
	 * @return a new {@link IBrowser}
	 */
	IBrowser newBrowser();
	
	/**
	 * Creates a new {@link IColorIcon}.
	 * 
	 * @param color color of the icon.
	 * @return a new {@link IColorIcon}
	 */
	IColorIcon newColorIcon( Color color );
	
	/**
	 * Creates a new {@link IColorIcon}.
	 * 
	 * @param color color of the icon.
	 * @param text text to be returned by the {@link IColorIcon#toString()} method
	 * @return a new {@link IColorIcon}
	 */
	IColorIcon newColorIcon( Color color, String text );
	
	/**
	 * Creates a new {@link IColorIcon}.
	 * 
	 * @param color color of the icon.
	 * @param width width of the icon
	 * @param height height of the icon
	 * @return a new {@link IColorIcon}
	 */
	IColorIcon newColorIcon( Color color, int width, int height );
	
	/**
	 * Creates a new {@link IColorIcon}.
	 * 
	 * @param color color of the icon.
	 * @param width width of the icon
	 * @param height height of the icon
	 * @param text text to be returned by the {@link IColorIcon#toString()} method
	 * @return a new {@link IColorIcon}
	 */
	IColorIcon newColorIcon( Color color, int width, int height, String text );
	
	/**
	 * Creates a new {@link IGridBagPanel}.
	 * 
	 * @return a new {@link IGridBagPanel}
	 */
	IGridBagPanel newGridBagPanel();
	
	/**
	 * Creates a new {@link IModestLabel}.
	 * 
	 * @return a new {@link IModestLabel}
	 */
	IModestLabel newModestLabel();
	
	/**
	 * Creates a new {@link IModestLabel}.
	 * 
	 * @param text text of the label
	 * @return a new {@link IModestLabel}
	 */
	IModestLabel newModestLabel( String text );
	
	/**
	 * Creates a new {@link IFileChooser}.
	 * 
	 * @return a new {@link IFileChooser}
	 */
	IFileChooser newFileChooser();
	
	/**
	 * Creates a new {@link IFileChooser}.
	 * 
	 * @param currentFolder default folder to navigate to
	 * @return a new {@link IFileChooser}
	 */
	IFileChooser newFileChooser( Path currentFolder );
	
	/**
	 * Creates a new {@link IPathField} in folder mode.
	 * 
	 * @return a new {@link IPathField}
	 */
	IPathField newPathField();
	
	/**
	 * Creates a new {@link IPathField} in the specified mode.
	 * 
	 * @param fileMode if true, the field operates as a file chooser; else as a folder chooser
	 * @return a new {@link IPathField}
	 */
	IPathField newPathField( boolean fileMode );
	
	/**
	 * Creates a new {@link IPathField} in folder mode.
	 * 
	 * @param path initial path to be set
	 * @return a new {@link IPathField}
	 */
	IPathField newPathField( Path path );
	
	/**
	 * Creates a new {@link IPathField} in the specified mode.
	 * 
	 * @param path initial path to be set
	 * @param fileMode if true, the field operates as a file chooser; else as a folder chooser
	 * @return a new {@link IPathField}
	 */
	IPathField newPathField( Path path, boolean fileMode );
	
	/**
	 * Creates a new {@link IStatusLabel}.
	 * 
	 * @return a new {@link IStatusLabel}
	 */
	IStatusLabel newStatusLabel();
	
	/**
	 * Creates a new {@link IStatusLabel}.
	 * 
	 * @param type initial status type to be set
	 * @param message initial status message to be set
	 * @return a new {@link IStatusLabel}
	 */
	IStatusLabel newStatusLabel( IStatusType type, String message );
	
	/**
	 * Creates a new {@link ICheckBox}.
	 * 
	 * @return a new {@link ICheckBox}
	 */
	ICheckBox newCheckBox();
	
	/**
	 * Creates a new {@link ICheckBox}.
	 * 
	 * @param text text of the check box
	 * @return a new {@link ICheckBox}
	 */
	ICheckBox newCheckBox( String text );
	
	/**
	 * Creates a new {@link ICheckBox}.
	 * 
	 * @param text text of the check box
	 * @param selected initial selected state
	 * @return a new {@link ICheckBox}
	 */
	ICheckBox newCheckBox( String text, boolean selected );
	
	/**
	 * Creates a new {@link IProgressBar}.
	 * 
	 * @return a new {@link IProgressBar}
	 */
	IProgressBar newProgressBar();
	
	
	/**
	 * Creates a new {@link IScrollPane} with small preferred size.
	 * 
	 * @return a new {@link IScrollPane}
	 */
	IScrollPane newScrollPane();
	
	/**
	 * Creates a new {@link IScrollPane} with small preferred size.
	 * 
	 * @param view the component to be scrolled
	 * @return a new {@link IScrollPane}
	 */
	IScrollPane newScrollPane( Component view );
	
	/**
	 * Creates a new {@link IScrollPane}.
	 * 
	 * @param view the component to be scrolled
	 * @param smallSize tells if small preferred size is to be set to prefer vertical scrolling instead of horizontal
	 * @return a new {@link IScrollPane}
	 */
	IScrollPane newScrollPane( Component view, boolean smallSize );
	
	/**
	 * Creates a new {@link IScrollPane}.
	 * 
	 * @param view the component to be scrolled
	 * @param smallSize tells if small preferred size is to be set to prefer vertical scrolling instead of horizontal
	 * @param followScrollingSetting tells if the vertical scrolling amount setting is to be followed
	 * @return a new {@link IScrollPane}
	 */
	IScrollPane newScrollPane( Component view, boolean smallSize, boolean followScrollingSetting );
	
	/**
	 * Creates a new {@link ISpinner} with an {@link Integer} {@link SpinnerNumberModel} with initial value 0 and no minimum or maximum limits.
	 * 
	 * @return a new {@link ISpinner}
	 */
	ISpinner newSpinner();
	
	/**
	 * Creates a new {@link ISpinner}.
	 * 
	 * @param model model to create the spinner with
	 * @return a new {@link ISpinner}
	 */
	ISpinner newSpinner( SpinnerModel model );
	
	/**
	 * Creates a new {@link ISplitPane} using {@link JSplitPane#HORIZONTAL_SPLIT} orientation (the splitter will be vertical).
	 * 
	 * @return a new {@link ISplitPane}
	 */
	ISplitPane newSplitPane();
	
	/**
	 * Creates a new {@link ISplitPane}.
	 * 
	 * @param orientation orientation ({@link JSplitPane#HORIZONTAL_SPLIT} or {@link JSplitPane#VERTICAL_SPLIT})
	 * @return a new {@link ISplitPane}
	 */
	ISplitPane newSplitPane( int orientation );
	
	/**
	 * Creates a new {@link IToolBar}.
	 * 
	 * @return a new {@link IToolBar}
	 */
	IToolBar newToolBar();
	
	/**
	 * Creates a new {@link IToolBar}.
	 * 
	 * @param initialSpace tells if initial space component is to be added
	 * @return a new {@link IToolBar}
	 */
	IToolBar newToolBar( boolean initialSpace );
	
	/**
	 * Creates a new {@link IComboBox}.
	 * 
	 * @param <E> type of the elements added to the combo box
	 * @return a new {@link IComboBox}
	 */
	< E > IComboBox< E > newComboBox();
	
	/**
	 * Creates a new {@link IComboBox}.
	 * 
	 * @param <E> type of the elements added to the combo box
	 * @param items initial items to be added to the combo box
	 * @return a new {@link IComboBox}
	 */
	< E > IComboBox< E > newComboBox( E[] items );
	
	/**
	 * Creates a new {@link IComboBox}.
	 * 
	 * @param <E> type of the elements added to the combo box
	 * @param items initial items to be added to the combo box
	 * @return a new {@link IComboBox}
	 */
	< E > IComboBox< E > newComboBox( Vector< E > items );
	
	/**
	 * Creates a new {@link ITable}.
	 * 
	 * @return a new {@link ITable}
	 */
	ITable newTable();
	
	/**
	 * Creates a new {@link IProgressBarView} to be used in {@link ITable}s as a cell value.<br>
	 * The string value will be the properly formatted value.
	 * 
	 * @param value value for the progress bar
	 * @param maximum maximum value for the progress bar
	 * @return a new {@link IProgressBarView}
	 * 
	 * @since 1.2
	 * 
	 * @see #newProgressBarView(int, int, String)
	 * @see IProgressBarView
	 */
	IProgressBarView newProgressBarView( int value, int maximum );
	
	/**
	 * Creates a new {@link IProgressBarView} to be used in {@link ITable}s as a cell value.
	 * 
	 * @param value value for the progress bar
	 * @param maximum maximum value for the progress bar
	 * @param stringValue string value to be displayed
	 * @return a new {@link IProgressBarView}
	 * 
	 * @since 1.2
	 * 
	 * @see #newProgressBarView(int, int)
	 * @see IProgressBarView
	 */
	IProgressBarView newProgressBarView( int value, int maximum, String stringValue );
	
	/**
	 * Creates a new {@link IBarCodeView} to be used in {@link ITable}s as a cell value.<br>
	 * The string value will be the properly formatted value.
	 * 
	 * @param values values of the parts of the bar code
	 * @param colors colors of the parts of the bar code
	 * @param selColors colors of the parts of the bar code when the cell is selected
	 * @return a new {@link IBarCodeView}
	 * 
	 * @throws IllegalArgumentException if the number of values, colors and selected colors are not the same
	 * 
	 * @since 1.2
	 * 
	 * @see #newBarCodeView(int[], Color[], Color[], String)
	 * @see IBarCodeView
	 */
	IBarCodeView newBarCodeView( int[] values, Color[] colors, Color[] selColors );
	
	/**
	 * Creates a new {@link IBarCodeView} to be used in {@link ITable}s as a cell value.
	 * 
	 * @param values values of the parts of the bar code
	 * @param colors colors of the parts of the bar code
	 * @param selColors colors of the parts of the bar code when the cell is selected
	 * @param stringValue string value to be displayed
	 * @return a new {@link IBarCodeView}
	 * 
	 * @throws IllegalArgumentException if the number of values, colors and selected colors are not the same
	 * 
	 * @since 1.2
	 * 
	 * @see #newBarCodeView(int[], Color[], Color[])
	 * @see IBarCodeView
	 */
	IBarCodeView newBarCodeView( int[] values, Color[] colors, Color[] selColors, String stringValue );
	
	/**
	 * Creates a new {@link IToolBarForTable}.
	 * 
	 * @param table targeted table whose selection changes to listen
	 * @return a new {@link IToolBarForTable}
	 */
	IToolBarForTable newToolBarForTable( ITable table );
	
	/**
	 * Creates a new {@link IList}.
	 * 
	 * @param <E> type of the elements in the list
	 * @return a new {@link IList}
	 */
	< E > IList< E > newList();
	
	/**
	 * Creates a new {@link IToolBarForList}.
	 * 
	 * @param list targeted list whose selection changes to listen
	 * @return a new {@link IToolBarForList}
	 */
	IToolBarForList newToolBarForList( IList< ? > list );
	
	/**
	 * Creates a new {@link ITree}.
	 * 
	 * @param root root node
	 * @return a new {@link ITree}
	 */
	ITree newTree( TreeNode root );
	
	/**
	 * Creates a new {@link IToolBarForTree}.
	 * 
	 * @param tree targeted tree whose selection changes to listen
	 * @return a new {@link IToolBarForTree}
	 */
	IToolBarForTree newToolBarForTree( ITree tree );
	
	/**
	 * Creates a new {@link IMenu}.
	 * 
	 * @return a new {@link IMenu}
	 */
	IMenu newMenu();
	
	/**
	 * Creates a new {@link IMenu}.
	 * 
	 * @param text text of the menu
	 * @return a new {@link IMenu}
	 */
	IMenu newMenu( String text );
	
	/**
	 * Creates a new {@link IMenu}.
	 * 
	 * @param icon icon of the menu
	 * @return a new {@link IMenu}
	 */
	IMenu newMenu( Icon icon );
	
	/**
	 * Creates a new {@link IMenu}.
	 * 
	 * @param text text of the menu
	 * @param icon icon of the menu
	 * @return a new {@link IMenu}
	 */
	IMenu newMenu( String text, Icon icon );
	
	/**
	 * Creates a new {@link IMenuItem}.
	 * 
	 * @return a new {@link IMenuItem}
	 */
	IMenuItem newMenuItem();
	
	/**
	 * Creates a new {@link IMenuItem}.
	 * 
	 * @param text text of the menu item
	 * @return a new {@link IMenuItem}
	 */
	IMenuItem newMenuItem( String text );
	
	/**
	 * Creates a new {@link IMenuItem}.
	 * 
	 * @param icon icon of the menu item
	 * @return a new {@link IMenuItem}
	 */
	IMenuItem newMenuItem( Icon icon );
	
	/**
	 * Creates a new {@link IMenuItem}.
	 * 
	 * @param text text of the menu item
	 * @param icon icon of the menu item
	 * @return a new {@link IMenuItem}
	 */
	IMenuItem newMenuItem( String text, Icon icon );
	
	/**
	 * Creates a new {@link ICheckBoxMenuItem}.
	 * 
	 * @return a new {@link ICheckBoxMenuItem}
	 */
	ICheckBoxMenuItem newCheckBoxMenuItem();
	
	/**
	 * Creates a new {@link ICheckBoxMenuItem}.
	 * 
	 * @param text text of the check box menu item
	 * @return a new {@link ICheckBoxMenuItem}
	 */
	ICheckBoxMenuItem newCheckBoxMenuItem( String text );
	
	/**
	 * Creates a new {@link ICheckBoxMenuItem}.
	 * 
	 * @param icon icon of the check box menu item
	 * @return a new {@link ICheckBoxMenuItem}
	 */
	ICheckBoxMenuItem newCheckBoxMenuItem( Icon icon );
	
	/**
	 * Creates a new {@link ICheckBoxMenuItem}.
	 * 
	 * @param text text of the check box menu item
	 * @param icon icon of the check box menu item
	 * @return a new {@link ICheckBoxMenuItem}
	 */
	ICheckBoxMenuItem newCheckBoxMenuItem( String text, Icon icon );
	
	/**
	 * Creates a new {@link IPasswordField}.
	 * 
	 * @return a new {@link IPasswordField}
	 */
	IPasswordField newPasswordField();
	
	/**
	 * Creates a new {@link IPasswordField}.
	 * 
	 * @param text initial text of the password field
	 * @return a new {@link IPasswordField}
	 */
	IPasswordField newPasswordField( String text );
	
	/**
	 * Creates a new {@link IPasswordField}.
	 * 
	 * @param columns the number of columns to use to calculate the preferred width; if columns is set to zero, the preferred width will be whatever naturally
	 *            results from the component implementation
	 * @return a new {@link IPasswordField}
	 */
	IPasswordField newPasswordField( int columns );
	
	/**
	 * Creates a new {@link IPasswordField}.
	 * 
	 * @param text initial text of the password field
	 * @param columns the number of columns to use to calculate the preferred width; if columns is set to zero, the preferred width will be whatever naturally
	 *            results from the component implementation
	 * @return a new {@link IPasswordField}
	 */
	IPasswordField newPasswordField( String text, int columns );
	
	/**
	 * Creates a new {@link IPopupMenu}.
	 * 
	 * @return a new {@link IPopupMenu}
	 */
	IPopupMenu newPopupMenu();
	
	/**
	 * Creates a new {@link IPopupMenu}.
	 * 
	 * @param text text of the popup title
	 * @return a new {@link IPopupMenu}
	 */
	IPopupMenu newPopupMenu( String text );
	
	/**
	 * Creates a new {@link IPopupMenu}.
	 * 
	 * @param text text of the popup title
	 * @param icon icon of the popup title
	 * @return a new {@link IPopupMenu}
	 */
	IPopupMenu newPopupMenu( String text, Icon icon );
	
	/**
	 * Creates a new {@link IPopupMenu}.
	 * 
	 * @param titleLabel title label of the popup
	 * @return a new {@link IPopupMenu}
	 */
	IPopupMenu newPopupMenu( ILabel titleLabel );
	
	/**
	 * Creates a new {@link ITabbedPane}.
	 * 
	 * @return a new {@link ITabbedPane}
	 */
	ITabbedPane newTabbedPane();
	
	/**
	 * Creates a new {@link IPage}.
	 * 
	 * @param <T> type of the page component
	 * @param displayName display name of the page
	 * @param ricon ricon of the page
	 * @param closeable tells if the page is closeable
	 * @param pageCompCreator page component creator
	 * @return a new {@link IPage}
	 * 
	 * @see IPage
	 * @see IPageCompCreator
	 */
	< T extends JComponent > IPage< T > newPage( String displayName, IRIcon ricon, boolean closeable, IPageCompCreator< T > pageCompCreator );
	
	/**
	 * Creates a new {@link IPage} which creates {@link IBrowser} as page component to display the specified HTML resource.
	 * 
	 * @param displayName display name of the page
	 * @param ricon icon resource of the page
	 * @param rhtml HTML template resource to display as content
	 * @return a new {@link IPage}
	 */
	IPage< ? extends IBrowser > newBrowserPage( String displayName, IRIcon ricon, IRHtml rhtml );
	
	/**
	 * Creates a new {@link IMultiPageComp}.
	 * 
	 * @param pageList initial page list to be set
	 * @param defaultPage optional page to select by default; if <code>null</code>, the first page will be selected
	 * @param rootComponent root component to register key strokes at
	 * @return a new {@link IMultiPageComp}
	 * 
	 * @see IMultiPageComp
	 * @see #newPage(String, IRIcon, boolean, IPageCompCreator)
	 */
	IMultiPageComp newMultiPageComp( List< IPage< ? > > pageList, IPage< ? > defaultPage, JComponent rootComponent );
	
	/**
	 * Creates a new {@link ITemplateField} with a template editor dialog opener button added.
	 * 
	 * @return a new {@link ITemplateField}
	 */
	ITemplateField newTemplateField();
	
	/**
	 * Creates a new {@link ITemplateField} with a template editor dialog opener button added.
	 * 
	 * @param template initial template to be set
	 * @return a new {@link ITemplateField}
	 */
	ITemplateField newTemplateField( String template );
	
	/**
	 * Creates a new {@link ITemplateField}.
	 * 
	 * @param addDialogOpener tells if a template editor dialog opener button is to be added
	 * @return a new {@link ITemplateField}
	 */
	ITemplateField newTemplateField( boolean addDialogOpener );
	
	/**
	 * Creates a new {@link ITemplateField}.
	 * 
	 * @param template initial template to be set
	 * @param addDialogOpener tells if a template editor dialog opener button is to be added
	 * @return a new {@link ITemplateField}
	 */
	ITemplateField newTemplateField( String template, boolean addDialogOpener );
	
	/**
	 * Creates a new {@link IDialog} whose parent will be the main frame ({@link IServices#getMainFrame()}).
	 * 
	 * <p>
	 * The returned dialog is not yet made visible.
	 * </p>
	 * 
	 * @param closeTask optional close task to be run right before the dialog is closed
	 * @return a new {@link IDialog}
	 * 
	 * @see IDialog
	 */
	IDialog newDialog( Runnable closeTask );
	
	/**
	 * Creates a new {@link IDialog} with the specified parent frame.
	 * 
	 * <p>
	 * The returned dialog is not yet made visible.
	 * </p>
	 * 
	 * @param owner reference to the owner frame
	 * @param closeTask optional close task to be run right before the dialog is closed
	 * @return a new {@link IDialog}
	 * 
	 * @see IDialog
	 */
	IDialog newDialog( Frame owner, Runnable closeTask );
	
	/**
	 * Creates a new {@link IDialog} with the specified parent frame.
	 * 
	 * <p>
	 * The returned overlay card is not yet made visible.
	 * </p>
	 * 
	 * @param params overlay card parameters wrapper
	 * @param configPopupBuilder optional config popup builder, can be used to add custom items to the config popup (before it is displayed)
	 * @param closeTask optional close task to be run right before the overlay card is closed
	 * @return a new {@link IDialog}
	 * 
	 * @see IDialog
	 * @see OverlayCardParams
	 * @see IConfigPopupBuilder
	 */
	IOverlayCard newOverlayCard( OverlayCardParams params, IConfigPopupBuilder configPopupBuilder, Runnable closeTask );
	
	/**
	 * Creates a new IMMUTABLE {@link IRenderablePair}.
	 * 
	 * @param <T> type of the object providing the displayed text
	 * @param icon icon to be rendered
	 * @param textProvider object providing the displayable text (acquired by {@link Object#toString()})
	 * @return a new {@link IRenderablePair}
	 * 
	 * @see IRenderablePair
	 */
	< T > IRenderablePair< T > newRenderablePair( Icon icon, T textProvider );
	
}
