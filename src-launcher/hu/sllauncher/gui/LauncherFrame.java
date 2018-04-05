/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.sllauncher.gui;

import hu.scelightapibase.bean.settings.ISettingChangeEvent;
import hu.scelightapibase.bean.settings.ISettingChangeListener;
import hu.scelightapibase.gui.comp.multipage.IPage;
import hu.scelightapibase.util.iface.Consumer;
import hu.sllauncher.LConsts;
import hu.sllauncher.ScelightLauncher;
import hu.sllauncher.action.XAction;
import hu.sllauncher.gui.comp.BorderPanel;
import hu.sllauncher.gui.comp.GridBagPanel;
import hu.sllauncher.gui.comp.HelpIcon;
import hu.sllauncher.gui.comp.Link;
import hu.sllauncher.gui.comp.StatusLabel;
import hu.sllauncher.gui.comp.XDialog;
import hu.sllauncher.gui.comp.StatusLabel.StatusType;
import hu.sllauncher.gui.comp.XButton;
import hu.sllauncher.gui.comp.XFrame;
import hu.sllauncher.gui.comp.XLabel;
import hu.sllauncher.gui.comp.XProgressBar;
import hu.sllauncher.gui.comp.XToolBar;
import hu.sllauncher.gui.comp.logo.Logo;
import hu.sllauncher.gui.comp.multipage.MultiPageComp;
import hu.sllauncher.gui.help.LHelps;
import hu.sllauncher.gui.icon.LIcons;
import hu.sllauncher.gui.page.LicensePage;
import hu.sllauncher.gui.page.NewsPage;
import hu.sllauncher.gui.page.TipsPage;
import hu.sllauncher.gui.page.WelcomePage;
import hu.sllauncher.gui.page.extmods.LAvailExtModsPage;
import hu.sllauncher.gui.page.logs.LLogsPage;
import hu.sllauncher.gui.page.logs.LLogsPageComp;
import hu.sllauncher.gui.setting.LSettingsDialog;
import hu.sllauncher.gui.setting.LSettingsGui;
import hu.sllauncher.service.env.LEnv;
import hu.sllauncher.service.env.bootsettings.BootSettings;
import hu.sllauncher.service.settings.LSettings;
import hu.sllauncher.util.SizeFormat;
import hu.sllauncher.util.SkillLevel;
import hu.sllauncher.util.gui.LGuiUtils;
import hu.sllauncher.util.gui.adapter.ActionAdapter;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;

/**
 * The main GUI frame of the launcher.
 * 
 * @author Andras Belicza
 */
@SuppressWarnings( "serial" )
public class LauncherFrame extends XFrame {
	
	/** Opens the selected replay folder. */
	private final XAction                settingsAction         = new XAction( KeyStroke.getKeyStroke( KeyEvent.VK_P, InputEvent.CTRL_MASK ), LIcons.F_GEAR,
	                                                                    "Settings", cp ) {
		                                                            @Override
		                                                            public void actionPerformed( final ActionEvent event ) {
			                                                            new LSettingsDialog( LauncherFrame.this, BootSettings.NODE_WORKSPACE,
			                                                                    LEnv.BOOT_SETTINGS, LEnv.LAUNCHER_SETTINGS ).setVisible( true );
		                                                            }
	                                                            };
	
	
	/** Fancy application logo. */
	private final Logo                   logo                   = new Logo();
	
	/** Label to display status. */
	private final StatusLabel            statusLabel            = new StatusLabel();
	
	/** Progress bar to display download progress. */
	private final XProgressBar           progressBar            = new XProgressBar();
	
	/** Label to display remaining download size. */
	private final XLabel                 remainingDownloadLabel = new XLabel();
	
	/** Label to display the number of checked / all modules. */
	private final XLabel                 modulesCountLabel      = new XLabel( "Modules: ", SwingConstants.RIGHT ).rightBorder( 15 );
	
	/** Proceed button (for example start or restart the application). */
	private final XButton                proceedButton          = new XButton();
	
	
	/** Runnable task which selects the Logs page. */
	private Runnable                     selectLogsPageTask;
	
	/** Setting change listener. */
	private final ISettingChangeListener scl;
	
	/**
	 * Creates a new {@link LauncherFrame}.
	 */
	public LauncherFrame() {
		scl = new ISettingChangeListener() {
			@Override
			public void valuesChanged( final ISettingChangeEvent event ) {
				if ( event.affected( LSettings.PERSON_NAME_FORMAT ) ) {
					setTitle( LConsts.LAUNCHER_NAME_FULL );
				}
			}
		};
		LEnv.LAUNCHER_SETTINGS.addAndExecuteChangeListener( LSettings.PERSON_NAME_FORMAT, scl );
		
		addWindowListener( new WindowAdapter() {
			@Override
			public void windowClosing( final WindowEvent e ) {
				ScelightLauncher.INSTANCE().exit();
			}
		} );
		
		setIconImage( LIcons.MY_APP_ICON.get().getImage() );
		
		buildGui();
		
		// Make default size a little larger than the default size of dialogs, so if settings dialog is opened, it will not cover the launcher frame entirely.
		LGuiUtils.maximizeWindowWithMargin( this, 20, new Dimension( XDialog.DEFAULT_DEFAULT_SIZE.width + 50, XDialog.DEFAULT_DEFAULT_SIZE.height + 50 ) );
		
		setVisible( true );
	}
	
	/**
	 * Builds the GUI of the main frame.
	 */
	private void buildGui() {
		// Default inner border
		cp.setBorder( BorderFactory.createEmptyBorder( 10, 10, 10, 10 ) );
		
		// Logo
		cp.addNorth( logo );
		
		final BorderPanel cp2 = new BorderPanel();
		cp.addCenter( cp2 );
		
		// Tool bar
		buildToolBar( cp2 );
		
		// Pages
		final LLogsPage logsPage;
		final IPage< ? > welcomePage, newsPage;
		final List< IPage< ? > > pageList = new ArrayList<>();
		pageList.add( welcomePage = new WelcomePage() );
		pageList.add( newsPage = new NewsPage() );
		pageList.add( new LicensePage() );
		pageList.add( new TipsPage() );
		pageList.add( new LAvailExtModsPage() );
		pageList.add( logsPage = new LLogsPage() );
		final MultiPageComp multiPageComp = new MultiPageComp( pageList, LEnv.LAUNCHER_SETTINGS.get( LSettings.WELCOME_ON_STARTUP ) ? welcomePage : newsPage,
		        getLayeredPane() );
		cp2.addCenter( multiPageComp );
		if ( LEnv.LAUNCHER_SETTINGS.get( LSettings.WELCOME_ON_STARTUP ) ) {
			// Force not to show Welcome page on next startup.
			// Welcome page is basically for new users, for returning users News is what I want to display (by default).
			LEnv.LAUNCHER_SETTINGS.set( LSettings.WELCOME_ON_STARTUP, Boolean.FALSE );
			LEnv.LAUNCHER_SETTINGS.save();
		}
		
		// Status and action bar
		final GridBagPanel statusPanel = new GridBagPanel();
		final GridBagConstraints c = statusPanel.c;
		
		statusPanel.nextRow();
		c.ipadx = 10;
		selectLogsPageTask = new Runnable() {
			@Override
			public void run() {
				if ( logsPage.equals( multiPageComp.getSelectedPage() ) ) {
					final LLogsPageComp logsPageComp = multiPageComp.getCachedPageComponent( logsPage );
					if ( logsPageComp != null ) // Page comp is surely exists (since it is selected) but just to be sure...
						logsPageComp.refreshLogs();
				} else
					multiPageComp.selectPage( logsPage ); // Selecting it will trigger an auto-refresh
			}
		};
		statusPanel.addSingle( new Link( "Status:", new Consumer< MouseEvent >() {
			@Override
			public void consume( final MouseEvent event ) {
				selectLogsPageTask.run();
			}
		} ) );
		c.ipadx = 0;
		statusPanel.addDouble( statusLabel );
		c.gridheight = 3;
		c.fill = GridBagConstraints.BOTH;
		c.ipadx = 40;
		proceedButton.setEnabled( false );
		proceedButton.addActionListener( new ActionAdapter() {
			@Override
			public void actionPerformed( final ActionEvent event ) {
				ScelightLauncher.INSTANCE().proceed();
			}
		} );
		statusPanel.addRemainder( proceedButton );
		getRootPane().setDefaultButton( proceedButton );
		c.gridheight = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.ipadx = 0;
		
		statusPanel.nextRow();
		c.gridx = 0;
		c.weightx = 1;
		c.ipady = 10;
		progressBar.setIndeterminate( true );
		statusPanel.addTriple( progressBar );
		c.weightx = 0;
		c.ipady = 0;
		c.gridx = GridBagConstraints.RELATIVE;
		
		statusPanel.nextRow();
		c.gridx = 0;
		c.weightx = 1;
		final JPanel dlremPanel = new JPanel( new FlowLayout( FlowLayout.LEFT, 0, 0 ) );
		dlremPanel.add( new XLabel( "Download remaining:" ).rightBorder( 15 ), c );
		dlremPanel.add( remainingDownloadLabel );
		statusPanel.addDouble( dlremPanel );
		c.weightx = 0;
		c.gridx = 2;
		modulesCountLabel.setToolTipText( "<html><b>Checked</b> modules count / <b>All</b> modules count" );
		statusPanel.addSingle( modulesCountLabel );
		LSettingsGui.bindVisibilityToSkillLevel( modulesCountLabel, SkillLevel.NORMAL );
		
		cp.addSouth( statusPanel );
	}
	
	/**
	 * Builds the menu bar of the main frame.
	 * 
	 * @param bp border panel to add the tool bar to
	 */
	private void buildToolBar( final BorderPanel bp ) {
		final XToolBar toolBar = new XToolBar();
		
		toolBar.add( new JButton( settingsAction ) );
		
		// Register CTRL+P to open the settings dialog
		final Object actionKey = new Object();
		getLayeredPane().getInputMap( JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT ).put( KeyStroke.getKeyStroke( KeyEvent.VK_P, InputEvent.CTRL_MASK ),
		        actionKey );
		getLayeredPane().getActionMap().put( actionKey, new AbstractAction() {
			@Override
			public void actionPerformed( final ActionEvent event ) {
				settingsAction.actionPerformed( null );
			}
		} );
		
		toolBar.addSeparator();
		toolBar.add( new XLabel( "Your Computer Skill Level:" ) );
		toolBar.add( LSettingsGui.createSkillLevelComboBox( null ) );
		toolBar.add( new HelpIcon( LHelps.COMP_SKILL_LEVEL ) );
		
		toolBar.addSeparator();
		toolBar.add( new XLabel( LHelps.HELP_LEGEND.title ) );
		toolBar.add( new HelpIcon( LHelps.HELP_LEGEND ).leftBorder( 2 ) );
		
		toolBar.finalizeLayout();
		
		bp.addNorth( toolBar );
	}
	
	@Override
	protected void customOnClose() {
		logo.stop();
	}
	
	/**
	 * Sets the status (text and icon).
	 * <p>
	 * Can be called outside of the EDT.
	 * </p>
	 * 
	 * @param type status type to be set
	 * @param message status message to be set
	 */
	public void setStatus( final StatusType type, final String message ) {
		LGuiUtils.runInEDT( new Runnable() {
			@Override
			public void run() {
				statusLabel.setByType( type, message );
			}
		} );
	}
	
	/**
	 * Sets the proceed text.
	 * <p>
	 * Can be called outside of the EDT.
	 * </p>
	 * 
	 * @param text proceed text to be set
	 */
	public void setProceedText( final String text ) {
		LGuiUtils.runInEDT( new Runnable() {
			@Override
			public void run() {
				proceedButton.setText( text );
			}
		} );
	}
	
	/**
	 * Sets whether proceed action is enabled.
	 * <p>
	 * Can be called outside of the EDT.
	 * </p>
	 * 
	 * @param enabled enabled state of proceed action to be set
	 */
	public void setProceedEnabled( final boolean enabled ) {
		LGuiUtils.runInEDT( new Runnable() {
			@Override
			public void run() {
				proceedButton.setEnabled( enabled );
			}
		} );
	}
	
	/**
	 * Sets the progress bar indeterminate state.
	 * <p>
	 * Can be called outside of the EDT.
	 * </p>
	 * 
	 * @param indeterminate indeterminate state to be set
	 * 
	 */
	public void setProgressIndeterminate( final boolean indeterminate ) {
		LGuiUtils.runInEDT( new Runnable() {
			@Override
			public void run() {
				progressBar.setIndeterminate( indeterminate );
			}
		} );
	}
	
	/**
	 * Sets the module counts.
	 * <p>
	 * Can be called outside of the EDT.
	 * </p>
	 * 
	 * @param checked number of checked modules
	 * @param total number of all modules
	 * 
	 */
	public void setModuleCounts( final int checked, final int total ) {
		LGuiUtils.runInEDT( new Runnable() {
			@Override
			public void run() {
				modulesCountLabel.setText( "Modules: " + LEnv.LANG.formatNumber( checked ) + " / " + LEnv.LANG.formatNumber( total ) );
			}
		} );
	}
	
	/**
	 * Sets the maximum value for the progress.
	 * <p>
	 * Can be called outside of the EDT.
	 * </p>
	 * 
	 * @param max max value for the progress
	 */
	public void setProgressMax( final int max ) {
		LGuiUtils.runInEDT( new Runnable() {
			@Override
			public void run() {
				progressBar.setIndeterminate( false );
				progressBar.setMaximum( max );
				progressBar.setStringPainted( true );
				remainingDownloadLabel.setText( null ); // Remaining download is refreshed when progress is set
			}
		} );
	}
	
	/**
	 * Sets the progress.
	 * <p>
	 * Can be called outside of the EDT.
	 * </p>
	 * 
	 * @param value progress value
	 */
	public void setProgress( final int value ) {
		LGuiUtils.runInEDT( new Runnable() {
			@Override
			public void run() {
				progressBar.setValue( value );
				final int remainingBytes = progressBar.getMaximum() - progressBar.getValue();
				remainingDownloadLabel.setText( remainingBytes == 0 ? null : SizeFormat.AUTO.formatSize( remainingBytes, 1 ) );
			}
		} );
	}
	
	/**
	 * Selects the Logs page.
	 * <p>
	 * Can be called outside of the EDT.
	 * </p>
	 */
	public void selectLogsPage() {
		LGuiUtils.runInEDT( selectLogsPageTask );
	}
	
}
