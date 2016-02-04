/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight.gui.page.repanalyzer.charts;

import hu.scelight.gui.help.Helps;
import hu.scelight.gui.icon.Icons;
import hu.scelight.gui.page.repanalyzer.BaseRepAnalTabComp;
import hu.scelight.gui.page.repanalyzer.charts.chartfactory.ChartFactory;
import hu.scelight.gui.page.repanalyzer.charts.chartscanvas.ChartsCanvas;
import hu.scelight.gui.page.repanalyzer.charts.chartscanvas.chart.Chart;
import hu.scelight.gui.page.repanalyzer.charts.chartscanvas.chart.TimeChart;
import hu.scelight.gui.page.repanalyzer.charts.eventstable.EventsTableComp;
import hu.scelight.gui.setting.SettingsGui;
import hu.scelight.sc2.rep.model.details.Race;
import hu.scelight.sc2.rep.model.initdata.lobbystate.Controller;
import hu.scelight.sc2.rep.repproc.RepProcessor;
import hu.scelight.sc2.rep.repproc.User;
import hu.scelight.service.env.Env;
import hu.scelight.service.settings.Settings;
import hu.scelight.util.Utils;
import hu.scelight.util.gui.GuiUtils;
import hu.scelightapibase.bean.settings.ISettingChangeEvent;
import hu.scelightapibase.bean.settings.ISettingChangeListener;
import hu.sllauncher.bean.settings.type.Setting;
import hu.sllauncher.gui.comp.BorderPanel;
import hu.sllauncher.gui.comp.HelpIcon;
import hu.sllauncher.gui.comp.ModestLabel;
import hu.sllauncher.gui.comp.XButton;
import hu.sllauncher.gui.comp.XCheckBox;
import hu.sllauncher.gui.comp.XLabel;
import hu.sllauncher.gui.comp.XSplitPane;
import hu.sllauncher.gui.comp.XToolBar;
import hu.sllauncher.gui.comp.combobox.XComboBox;
import hu.sllauncher.util.Bool;
import hu.sllauncher.util.SkillLevel;
import hu.sllauncher.util.gui.adapter.ActionAdapter;

import java.awt.Color;
import java.awt.Component;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.EnumMap;
import java.util.Map;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JScrollBar;
import javax.swing.JSplitPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

/**
 * Charts tab component.
 * 
 * @author Andras Belicza
 */
@SuppressWarnings( "serial" )
public class ChartsComp extends BaseRepAnalTabComp {
	
	/** North box wrapping the tool bars. */
	private final Box                      toolBarsBox           = Box.createVerticalBox();
	
	/** Center split pane containing charts canvas (top) and events table (bottom). */
	private final XSplitPane               splitPane             = new XSplitPane( JSplitPane.VERTICAL_SPLIT );
	
	// Charts tool bar
	
	/** Combo box to select the displayable chart type. */
	private final XComboBox< ChartType >   chartComboBox         = SettingsGui.createSettingComboBox( Settings.CHART_TYPE, Env.APP_SETTINGS, null );
	
	/** Check box to tell if all chart is to be displayed on 1 chart. */
	private final XCheckBox                allOnOneChartCheckBox = SettingsGui
	                                                                     .createSettingCheckBox( Settings.DISPLAY_ALL_ON_ONE_CHART, Env.APP_SETTINGS, null );
	
	/** Check box to tell if team members is to be merged. */
	private final XCheckBox                teamAsOneCheckBox     = SettingsGui.createSettingCheckBox( Settings.TEAMS_AS_ONE, Env.APP_SETTINGS, null );
	
	/** Check box to tell if time values are to be displayed in seconds (else loops). */
	private final XCheckBox                inSecondsCheckBox     = SettingsGui.createSettingCheckBox( Settings.TIME_IN_SECONDS, Env.APP_SETTINGS, null );
	
	/** Charts canvas. */
	private final ChartsCanvas             chartsCanvas;
	
	/** Charts canvas scroll bar. */
	private final JScrollBar               chartsCanvasScrollBar = new JScrollBar( JScrollBar.HORIZONTAL );
	
	// User tool bar
	
	/** Check boxes to toggle users. */
	private final XCheckBox[]              userCheckBoxes;
	
	/** Boolean array to tell if a user by user id is enabled (to be included). */
	private final boolean[]                userByUserIdEnableds;
	
	/** Boolean array to tell if a player by player id is enabled (to be included). Contains 1 extra because playerId is 1-based. */
	private final boolean[]                userByPlayerIdEnableds;
	
	/** Map of already created chart factories. */
	private Map< ChartType, ChartFactory > chartTypeFactoryMap   = new EnumMap<>( ChartType.class );
	
	/** Table to display user events. */
	private final EventsTableComp          eventsTableComp;
	
	
	/** Repaints the charts canvas. */
	private final ActionAdapter            chartsRepainter       = new ActionAdapter() {
		                                                             @Override
		                                                             public void actionPerformed( final ActionEvent event ) {
			                                                             chartsCanvas.repaint();
		                                                             }
	                                                             };
	
	/** Reconfigures and repaints the charts. */
	public final ActionAdapter             chartsReconfigurer    = new ActionAdapter() {
		                                                             @Override
		                                                             public void actionPerformed( final ActionEvent event ) {
			                                                             reconfigureChartCanvas();
			                                                             chartsRepainter.actionPerformed( event );
		                                                             }
	                                                             };
	
	/** Rebuilds and reconfigures the charts canvas. */
	public final ActionAdapter             chartsRebuilder       = new ActionAdapter() {
		                                                             @Override
		                                                             public void actionPerformed( final ActionEvent event ) {
			                                                             boolean selected = false;
			                                                             for ( final boolean b : userByUserIdEnableds )
				                                                             selected |= b;
			                                                             chartsCanvas.setMessage( selected ? null : "No charts to display: no users selected." );
			                                                             chartsCanvas.setChartsList( chartTypeFactoryMap.get( chartComboBox.getSelectedItem() )
			                                                                     .createCharts() );
			                                                             
			                                                             chartsReconfigurer.actionPerformed( event );
		                                                             }
	                                                             };
	
	/** Rebuilds the events table. */
	private final ActionAdapter            eventsRebuilder       = new ActionAdapter() {
		                                                             @Override
		                                                             public void actionPerformed( final ActionEvent event ) {
			                                                             eventsTableComp.rebuildEventsTable();
		                                                             }
	                                                             };
	
	/** Rebuilds the whole content of the Charts tab. */
	private final ActionAdapter            rebuilder             = new ActionAdapter() {
		                                                             @Override
		                                                             public void actionPerformed( final ActionEvent event ) {
			                                                             chartsRebuilder.actionPerformed( event );
			                                                             eventsRebuilder.actionPerformed( event );
		                                                             }
	                                                             };
	
	
	/** Replay length. */
	private int                            elapsedGameLoops      = repProc.replay.header.getElapsedGameLoops();
	
	/** Displayable range min. */
	private int                            rangeMin;
	
	/** Displayable range max. */
	private int                            rangeMax;
	
	/** Charts enlargement state. */
	private EnlargementState               enlargementState      = EnlargementState.NORMAL;
	
	/**
	 * Creates a new {@link ChartsComp}.
	 * 
	 * @param repProc replay processor
	 */
	public ChartsComp( final RepProcessor repProc ) {
		super( repProc );
		
		userCheckBoxes = new XCheckBox[ repProc.users.length ];
		userByUserIdEnableds = new boolean[ repProc.usersByUserId.length ];
		userByPlayerIdEnableds = new boolean[ repProc.usersByPlayerId.length ];
		
		chartsCanvas = new ChartsCanvas( repProc, this );
		eventsTableComp = new EventsTableComp( repProc, this );
		
		buildGui();
	}
	
	/**
	 * Builds the GUI of the tab.
	 */
	private void buildGui() {
		addNorth( toolBarsBox );
		
		toolBarsBox.add( buildChartSpecificToolBar() );
		toolBarsBox.add( buildGeneralChartsToolBar() );
		toolBarsBox.add( buildUsersToolBar() );
		
		final BorderPanel chartsPanel = new BorderPanel( chartsCanvas );
		chartsPanel.addSouth( chartsCanvasScrollBar );
		splitPane.setTopComponent( chartsPanel );
		splitPane.setBottomComponent( eventsTableComp );
		splitPane.setResizeWeight( Env.APP_SETTINGS.get( Settings.INITIAL_CHARTS_EVT_TBL_PARTING ) / 100.0 );
		addCenter( splitPane );
		
		// Listened app settings
		final ISettingChangeListener scl = new ISettingChangeListener() {
			@Override
			public void valuesChanged( final ISettingChangeEvent event ) {
				if ( event.affected( Settings.CHARTS_DIVIDER_SIZE ) )
					splitPane.setDividerSize( event.get( Settings.CHARTS_DIVIDER_SIZE ) );
			}
		};
		final Set< Setting< ? > > listenedSettingSet = Utils.< Setting< ? > > asNewSet( Settings.CHARTS_DIVIDER_SIZE );
		SettingsGui.addBindExecuteScl( scl, Env.APP_SETTINGS, listenedSettingSet, this );
		
		rebuilder.actionPerformed( null );
	}
	
	/**
	 * Builds and returns the chart specific tool bar (which contains the chart chooser and chart-specific options).
	 * 
	 * @return the chart specific tool bar
	 */
	private XToolBar buildChartSpecificToolBar() {
		final XToolBar toolBar = new XToolBar();
		
		final XToolBar chartOptionsToolBar = new XToolBar( false );
		
		toolBar.add( new ModestLabel( "Chart:" ) );
		chartComboBox.addActionListener( new ActionAdapter( true ) {
			/** Previously selected chart type. */
			ChartType prevChartType;
			
			@Override
			public void actionPerformed( final ActionEvent event ) {
				final ChartType chartType = chartComboBox.getSelectedItem();
				if ( chartType == prevChartType )
					return; // Same chart was selected, nothing to do
					
				prevChartType = chartType;
				ChartFactory chartFactory = chartTypeFactoryMap.get( chartType );
				if ( chartFactory == null )
					try {
						chartFactory = chartType.chartFactoryClass.getConstructor( ChartsComp.class ).newInstance( ChartsComp.this );
						chartTypeFactoryMap.put( chartType, chartFactory );
					} catch ( final Exception e ) {
						Env.LOGGER.error( "Failed to create chart factory: " + ChartType.APM.chartFactoryClass );
					}
				
				// Add chart specific options
				// If the focus is on a component that is about to be removed, focus the chart combo box
				final Component focusOwner = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();
				if ( focusOwner != null && SwingUtilities.isDescendingFrom( focusOwner, chartOptionsToolBar ) )
					chartComboBox.requestFocusInWindow();
				chartOptionsToolBar.removeAll();
				chartOptionsToolBar.invalidate();
				// Add a minimal vertical space holder in case the chart specific options are small
				// in which case since it's a toolbar in a toolbar a bottom border would appear (e.g. Control Groups chart).
				chartOptionsToolBar.add( Box.createVerticalStrut( 25 ) );
				chartFactory.addChartOptions( chartOptionsToolBar );
				chartOptionsToolBar.revalidate();
				
				// Rebuild charts
				if ( !duringInit )
					chartsRebuilder.actionPerformed( event );
			}
		} );
		toolBar.add( chartComboBox );
		toolBar.addSeparator();
		
		toolBar.add( chartOptionsToolBar );
		
		toolBar.finalizeLayout();
		
		// Register chart hotkeys
		final InputMap inputMap = getInputMap( JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT );
		for ( final ChartType chartType : ChartType.values() )
			if ( chartType.keyStroke != null ) {
				// Activate chart hotkeys
				Object actionKey;
				inputMap.put( chartType.keyStroke, actionKey = new Object() );
				getActionMap().put( actionKey, new AbstractAction() {
					@Override
					public void actionPerformed( final ActionEvent event ) {
						chartComboBox.setSelectedItem( chartType );
					}
				} );
			}
		
		return toolBar;
	}
	
	/**
	 * Builds and returns the general charts tool bar (which contains options for all chart types).
	 * 
	 * @return the general charts tool bar
	 */
	private XToolBar buildGeneralChartsToolBar() {
		final XToolBar toolBar = new XToolBar();
		
		toolBar.add( new ModestLabel( "General options:" ).verticalBorder( 5 ) );
		
		allOnOneChartCheckBox.addActionListener( chartsRebuilder );
		allOnOneChartCheckBox.setToolTipText( "<html>Display all players / teams on one chart<br>Useful to compare players / teams to each other.</html>" );
		allOnOneChartCheckBox.setText( "All on 1 chart" );
		toolBar.add( allOnOneChartCheckBox );
		
		teamAsOneCheckBox.addActionListener( chartsRebuilder );
		teamAsOneCheckBox
		        .setToolTipText( "<html>Teams as One: Combine / Merge team members<br>Useful to compare overall team performances in team games.</html>" );
		teamAsOneCheckBox.setText( "Teams as One" );
		toolBar.add( teamAsOneCheckBox );
		
		inSecondsCheckBox.addActionListener( chartsReconfigurer );
		SettingsGui.bindVisibilityToSkillLevel( inSecondsCheckBox, Settings.TIME_IN_SECONDS.skillLevel, Boolean.TRUE );
		inSecondsCheckBox.setToolTipText( "Display time values in seconds (else in game loops)" );
		inSecondsCheckBox.setText( "Seconds" );
		toolBar.add( inSecondsCheckBox );
		
		// ZOOMING TOOLS
		
		toolBar.addSeparator();
		final XLabel zoomFactorLabel = new XLabel();
		final XButton enlargeChartsButton = new XButton( Icons.F_APPLICATION_RESIZE.get() );
		enlargeChartsButton.setToolTipText( "Enlarge charts (scroll wheel click)" );
		final ActionListener enlargeChartsListener = new ActionAdapter() {
			double storedDividerLocation;
			
			double storedDividerLocation2;
			
			@Override
			public void actionPerformed( final ActionEvent event ) {
				if ( enlargementState == EnlargementState.NORMAL )
					storedDividerLocation = splitPane.getHeight() == 0 ? 1 : (double) splitPane.getDividerLocation() / splitPane.getHeight();
				if ( enlargementState == EnlargementState.ENLARGED )
					storedDividerLocation2 = Env.MAIN_FRAME.multiPageComp.getWidth() == 0 ? 1 : (double) Env.MAIN_FRAME.multiPageComp.getDividerLocation()
					        / Env.MAIN_FRAME.multiPageComp.getWidth();
				
				enlargementState = enlargementState.next();
				
				toolBarsBox.setVisible( enlargementState == EnlargementState.NORMAL );
				Env.MAIN_FRAME.multiPageComp.getLeftComponent().setVisible( enlargementState != EnlargementState.ENLARGED_FULL );
				Env.MAIN_FRAME.multiPageComp.validate();
				// splitPane.getParent().validate();
				eventsTableComp.setVisible( enlargementState == EnlargementState.NORMAL );
				
				if ( enlargementState == EnlargementState.NORMAL )
					splitPane.setDividerLocation( (int) ( splitPane.getHeight() * storedDividerLocation ) );
				if ( enlargementState == EnlargementState.NORMAL )
					Env.MAIN_FRAME.multiPageComp.setDividerLocation( (int) ( Env.MAIN_FRAME.multiPageComp.getWidth() * storedDividerLocation2 ) );
				if ( enlargementState != EnlargementState.NORMAL )
					chartsCanvas.requestFocusInWindow();
			}
		};
		enlargeChartsButton.addActionListener( enlargeChartsListener );
		// Register ESC key to restore charts if enlarged.
		final Object actionKey = new Object();
		getInputMap( JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT ).put( KeyStroke.getKeyStroke( KeyEvent.VK_ESCAPE, 0 ), actionKey );
		getActionMap().put( actionKey, new AbstractAction() {
			@Override
			public void actionPerformed( final ActionEvent event ) {
				if ( enlargementState != EnlargementState.NORMAL )
					enlargeChartsListener.actionPerformed( event );
			}
		} );
		// If charts canvas loses focus and enlargement is full, restore the charts:
		chartsCanvas.addFocusListener( new FocusAdapter() {
			@Override
			public void focusLost( final FocusEvent event ) {
				if ( enlargementState == EnlargementState.ENLARGED_FULL )
					enlargeChartsListener.actionPerformed( null );
			}
		} );
		toolBar.add( enlargeChartsButton );
		final XButton zoomMarkersButton = new XButton( Icons.F_MAGNIFIER_ZOOM_FIT.get() );
		final XButton resetZoomButton = new XButton( Icons.F_MAGNIFIER_ZOOM_OUT.get() );
		final Runnable updateZoomTask = new Runnable() {
			@Override
			public void run() {
				final boolean zoomed = rangeMin != 0 || rangeMax != elapsedGameLoops;
				final double factor = (double) ( elapsedGameLoops + 1 ) / ( rangeMax - rangeMin + 1 );
				zoomFactorLabel.setText( Env.LANG.formatNumber( factor, 1 ) + "x" );
				chartsCanvasScrollBar.getModel().setRangeProperties( rangeMin, rangeMax - rangeMin, 0, elapsedGameLoops, false );
				chartsCanvasScrollBar.setUnitIncrement( Math.max( 1, ( rangeMax - rangeMin ) >> 7 ) );
				chartsCanvasScrollBar.setBlockIncrement( Math.max( 1, ( rangeMax - rangeMin ) >> 2 ) );
				zoomFactorLabel.setVisible( zoomed );
				resetZoomButton.setEnabled( zoomed );
				chartsCanvasScrollBar.setVisible( zoomed );
			}
		};
		
		zoomMarkersButton.setToolTipText( "Zoom into markers" );
		zoomMarkersButton.addActionListener( new ActionAdapter() {
			@Override
			public void actionPerformed( final ActionEvent event ) {
				if ( chartsCanvas.getMarkerLoop1() != null && chartsCanvas.getMarkerLoop1().equals( chartsCanvas.getMarkerLoop2() ) )
					return;
				if ( chartsCanvas.getMarkerLoop1() != null )
					rangeMin = chartsCanvas.getMarkerLoop1();
				if ( chartsCanvas.getMarkerLoop2() != null )
					rangeMax = chartsCanvas.getMarkerLoop2();
				if ( rangeMin > rangeMax ) {
					// swap
					final int temp = rangeMin;
					rangeMin = rangeMax;
					rangeMax = temp;
				}
				chartsReconfigurer.actionPerformed( event );
				updateZoomTask.run();
			}
		} );
		toolBar.add( zoomMarkersButton );
		resetZoomButton.setToolTipText( "Reset zoom" );
		resetZoomButton.addActionListener( new ActionAdapter( true ) {
			@Override
			public void actionPerformed( final ActionEvent event ) {
				rangeMin = 0;
				rangeMax = elapsedGameLoops;
				updateZoomTask.run();
				if ( !duringInit )
					chartsReconfigurer.actionPerformed( event );
			}
		} );
		toolBar.add( resetZoomButton );
		
		// Zoom in/out by CTRL+mouse scroll wheel, drag-scrolling, chart enlarging
		final MouseAdapter ma = new MouseAdapter() {
			int dragStartX, dragStartY;
			
			@Override
			public void mousePressed( final MouseEvent event ) {
				if ( ( event.getModifiers() & InputEvent.BUTTON1_MASK ) != 0 ) {
					dragStartX = event.getXOnScreen();
					dragStartY = event.getYOnScreen();
				}
				if ( !toolBarsBox.isVisible() && !chartsCanvas.isFocusOwner() ) {
					// If enlarged, focus the canvas when clicked so it can properly receive keyboard inputs (and the whole tab).
					chartsCanvas.requestFocusInWindow();
				}
				if ( event.getButton() == GuiUtils.MOUSE_BTN_MIDDLE )
					enlargeChartsListener.actionPerformed( null );
			};
			
			@Override
			public void mouseDragged( final MouseEvent event ) {
				if ( ( event.getModifiers() & InputEvent.BUTTON1_MASK ) != 0 ) {
					final int dx = ( dragStartX - event.getXOnScreen() ) * 2; // No shifting as this can be negative!
					final int dy = ( dragStartY - event.getYOnScreen() ) * 2; // No shifting as this can be negative!
					if ( !chartsCanvas.getChartList().isEmpty() || ( chartsCanvas.getChartList().get( 0 ) instanceof TimeChart ) ) {
						int deltaLoop = ( (TimeChart< ? >) chartsCanvas.getChartList().get( 0 ) ).deltaxToDeltaLoop( dx );
						// Ensure a minimal step:
						if ( deltaLoop == 0 )
							deltaLoop = dx < 0 ? -1 : 1;
						chartsCanvasScrollBar.setValue( rangeMin + deltaLoop );
					} else {
						// dy is not yet used, we only have time charts (this is to get rid of unused warning)!
						Integer.compare( dy, 0 );
					}
					dragStartX = event.getXOnScreen();
					dragStartY = event.getYOnScreen();
				}
			}
			
			@Override
			public void mouseWheelMoved( final MouseWheelEvent event ) {
				if ( !event.isControlDown() )
					return;
				if ( chartsCanvas.getChartList().isEmpty() || !( chartsCanvas.getChartList().get( 0 ) instanceof TimeChart ) )
					return;
				
				for ( int change = event.getWheelRotation(), dir = change < 0 ? 1 : -1; change != 0; change += dir ) {
					// Zoom by 2 or 1/2 in a way that mouse position be the center
					final Integer mouseLoop = ( (TimeChart< ? >) chartsCanvas.getChartList().get( 0 ) ).xToLoop( event.getX() );
					if ( mouseLoop == null )
						break;
					
					int range = rangeMax - rangeMin + 1;
					if ( dir > 0 ) {
						// Zoom in
						if ( range < 4 )
							break; // Already at max zoom
						range /= 2;
						rangeMin = mouseLoop - range / 2;
						rangeMax = rangeMin + range - 1;
					} else {
						// Zoom out
						if ( rangeMin == 0 && rangeMax == elapsedGameLoops )
							break; // Already at min zoom
						range *= 2;
						if ( range > elapsedGameLoops + 1 )
							range = elapsedGameLoops + 1;
						rangeMin = mouseLoop - range / 2;
						rangeMax = rangeMin + range - 1;
					}
					if ( rangeMin < 0 ) {
						rangeMax -= rangeMin;
						rangeMin = 0;
					}
					if ( rangeMax > elapsedGameLoops ) {
						rangeMin -= rangeMax - elapsedGameLoops;
						rangeMax = elapsedGameLoops;
					}
					
					chartsReconfigurer.actionPerformed( null );
				}
				
				updateZoomTask.run();
			}
		};
		chartsCanvas.addMouseListener( ma );
		chartsCanvas.addMouseMotionListener( ma );
		chartsCanvas.addMouseWheelListener( ma );
		zoomFactorLabel.setOpaque( true );
		zoomFactorLabel.setBackground( Color.GREEN );
		toolBar.add( zoomFactorLabel );
		toolBar.add( new HelpIcon( Helps.CHART_ZOOMING_SCROLLING ).leftBorder( 2 ) );
		
		chartsCanvasScrollBar.addAdjustmentListener( new AdjustmentListener() {
			@Override
			public void adjustmentValueChanged( final AdjustmentEvent event ) {
				final int temp = rangeMax - rangeMin;
				rangeMin = event.getValue();
				rangeMax = rangeMin + temp;
				chartsReconfigurer.actionPerformed( null );
			}
		} );
		
		toolBar.addSeparator();
		toolBar.add( SettingsGui.createSettingLink( Settings.NODE_CHARTS ) );
		
		toolBar.finalizeLayout();
		
		return toolBar;
	}
	
	/**
	 * Builds and returns the users tool bar.
	 * 
	 * @return the users tool bar
	 */
	private XToolBar buildUsersToolBar() {
		final XToolBar usersToolBar = new XToolBar();
		
		usersToolBar.add( new ModestLabel( "Players:" ) );
		// Position to insert Select All and Deselect All buttons if needed
		final int selectDeselectAllPosition = usersToolBar.getComponentCount();
		
		// In general players (participants) are listed as players in the replay details. Users that can give commands, have user
		// id, computers do not since they don't issue commands. Observers can also give commands, so they have user id but are
		// not listed as players.
		//
		// Players have stats (from the tracker stream) including computers.
		
		final Bool selectAllUsersInProgress = new Bool();
		final ActionAdapter enabledsRebuilder = new ActionAdapter() {
			@Override
			public void actionPerformed( final ActionEvent event ) {
				if ( selectAllUsersInProgress.value )
					return;
				for ( int i = repProc.users.length - 1; i >= 0; i-- ) {
					if ( userCheckBoxes[ i ] == null )
						continue; // Check box might be null if observers are not enabled and this user is an observer
					final Integer userId = repProc.users[ i ].slot.userId;
					if ( userId != null )
						userByUserIdEnableds[ userId ] = userCheckBoxes[ i ].isSelected();
					if ( repProc.users[ i ].player != null )
						userByPlayerIdEnableds[ repProc.users[ i ].playerId ] = userCheckBoxes[ i ].isSelected();
				}
				if ( event != null ) {
					// Chain
					rebuilder.actionPerformed( event );
				}
			}
		};
		
		// Users
		final boolean showObservers = Env.APP_SETTINGS.get( Settings.LIST_OBSERVERS );
		final boolean showResult = Env.APP_SETTINGS.get( Settings.SHOW_MATCH_RESULT );
		int lastTeam = -1;
		boolean reachedObservers = false;
		
		int createdUsers = 0;
		for ( int i = 0; i < repProc.users.length; i++ ) { // Upward to add users in correct (team) order
			final User u = repProc.users[ i ];
			if ( u.player == null && !showObservers )
				continue;
			
			if ( lastTeam < 0 )
				lastTeam = u.slot.teamId;
			
			if ( u.player == null ) {
				// It's an observer
				if ( !reachedObservers ) {
					reachedObservers = true;
					usersToolBar.add( new ModestLabel( "Observers:" ).leftBorder( 20 ) );
				}
			} else {
				final int team = u.slot.teamId;
				if ( team != lastTeam ) {
					usersToolBar.add( new XLabel( "VS." ).boldFont().horizontalBorder( 7 ) );
					lastTeam = team;
				}
			}
			
			final Box userBox = Box.createVerticalBox();
			
			Box row = Box.createHorizontalBox();
			createdUsers++;
			// player != null => non-observer; controller = HUMAN => not computer
			final boolean defaultEnabled = u.player != null && u.slot.getController() == Controller.HUMAN && u.apm > 5;
			final XCheckBox userCheckBox = userCheckBoxes[ i ] = new XCheckBox( u.fullName, defaultEnabled );
			userCheckBox.setBorder( BorderFactory.createEmptyBorder( 0, 0, 0, 3 ) );
			userCheckBox.addActionListener( new ActionAdapter( true ) {
				@Override
				public void actionPerformed( final ActionEvent event ) {
					userBox.setBorder( userCheckBox.isSelected() ? BorderFactory.createLoweredBevelBorder() : BorderFactory.createRaisedBevelBorder() );
				}
			} );
			userCheckBox.addActionListener( enabledsRebuilder );
			row.add( userCheckBox );
			userBox.add( row );
			
			row = Box.createHorizontalBox();
			if ( u.uid != null && u.uid.getHighestLeague().ricon != null ) {
				final XLabel leagueLabel = new XLabel( u.uid.getHighestLeague().ricon.get() );
				leagueLabel.setToolTipText( "Highest league: " + u.uid.getHighestLeague().text );
				row.add( leagueLabel );
			}
			final XLabel colorLabel = new XLabel( u.getPlayerColor().icon );
			colorLabel.setToolTipText( "Color: " + u.getPlayerColor().text );
			row.add( colorLabel );
			if ( u.player != null ) {
				final XLabel raceLabel = new XLabel( u.player.race.ricon.get() );
				raceLabel.setToolTipText( "Race: " + ( u.slot.getChosenRace() == Race.RANDOM ? u.player.race.text + " (Random)" : u.player.race.text ) );
				row.add( raceLabel );
			}
			
			if ( showResult && u.player != null && u.player.getResult().ricon != null ) {
				final XLabel resultLabel = new XLabel( u.player.getResult().ricon.get() );
				resultLabel.setToolTipText( "Result: " + u.player.getResult().text + ( u.player.isResultDeduced() ? " (Deduced)" : "" ) );
				row.add( resultLabel );
			}
			
			final XButton menuButton = new XButton( Icons.F_WRENCH.get() );
			menuButton.setToolTipText( "Click to open Player menu" );
			menuButton.configureAsIconButton();
			menuButton.setBorder( BorderFactory.createEmptyBorder( 0, 3, 0, 0 ) );
			menuButton.addActionListener( new ActionAdapter() {
				@Override
				public void actionPerformed( final ActionEvent event ) {
					new PlayerMenu( event, u );
				}
			} );
			row.add( menuButton );
			
			userBox.add( row );
			
			// Make the whole user box clickable to toggle the user (this only applies on empty spaces - not on child comps)
			userBox.addMouseListener( new MouseAdapter() {
				@Override
				public void mouseClicked( final MouseEvent event ) {
					userCheckBox.doClick( 0 );
				}
			} );
			
			usersToolBar.add( userBox );
		}
		
		enabledsRebuilder.actionPerformed( null );
		
		// If more than 2 users, add select all and Deselect all
		if ( createdUsers > 2 ) {
			final Box box = Box.createVerticalBox();
			box.setBorder( BorderFactory.createEmptyBorder( 0, 0, 0, 5 ) );
			SettingsGui.bindVisibilityToSkillLevel( box, SkillLevel.NORMAL );
			final XButton allButton = new XButton( "All", Icons.F_UI_CHECK_BOX.get() );
			allButton.setToolTipText( "Select All Users" );
			allButton.configureAsIconButton();
			box.add( allButton );
			final XButton noneButton = new XButton( "None", Icons.F_UI_CHECK_BOX_UNCHECK.get() );
			noneButton.setToolTipText( "Deselect All Users" );
			noneButton.configureAsIconButton();
			box.add( noneButton );
			usersToolBar.add( box, selectDeselectAllPosition );
			final ActionListener actionListener = new ActionAdapter() {
				@Override
				public void actionPerformed( final ActionEvent event ) {
					final boolean select = event.getSource() == allButton;
					selectAllUsersInProgress.value = true;
					for ( final XCheckBox userCheckBox : userCheckBoxes )
						if ( userCheckBox != null && userCheckBox.isSelected() != select )
							userCheckBox.doClick( 0 );
					selectAllUsersInProgress.value = false;
					enabledsRebuilder.actionPerformed( event );
				}
			};
			allButton.addActionListener( actionListener );
			noneButton.addActionListener( actionListener );
		}
		
		usersToolBar.finalizeLayout();
		
		return usersToolBar;
	}
	
	/**
	 * Reconfigures the charts canvas (does not repaint).
	 */
	private void reconfigureChartCanvas() {
		// Set general chart options
		for ( final Chart< ? > chart : chartsCanvas.getChartList() ) {
			if ( chart instanceof TimeChart ) {
				final TimeChart< ? > tc = (TimeChart< ? >) chart;
				tc.setInSeconds( inSecondsCheckBox.isSelected() );
				tc.setRange( rangeMin, rangeMax );
			}
		}
		
		// Set chart specific options
		chartTypeFactoryMap.get( chartComboBox.getSelectedItem() ).reconfigureChartCanvas();
	}
	
	/**
	 * Returns the boolean array to tell if a user by user id is enabled (to be included).
	 * 
	 * @return the boolean array to tell if a user by user id is enabled (to be included)
	 */
	public boolean[] getUserByUserIdEnableds() {
		return userByUserIdEnableds;
	}
	
	/**
	 * Returns the boolean array to tell if a user by player id enabled (to be included).
	 * 
	 * @return the boolean array to tell if a user by player id is enabled (to be included)
	 */
	public boolean[] getUserByPlayerIdEnableds() {
		return userByPlayerIdEnableds;
	}
	
	/**
	 * Returns the replay processor.
	 * 
	 * @return the replay processor
	 */
	public RepProcessor getRepProcessor() {
		return repProc;
	}
	
	/**
	 * Returns the charts canvas.
	 * 
	 * @return the charts canvas
	 */
	public ChartsCanvas getChartsCanvas() {
		return chartsCanvas;
	}
	
	/**
	 * Returns the events table component.
	 * 
	 * @return the events table component
	 */
	public EventsTableComp getEventsTableComp() {
		return eventsTableComp;
	}
	
	/**
	 * Returns whether team members are to be combined.
	 * 
	 * @return true if team members are to be combined; false otherwise
	 */
	public boolean getTeamsAsOne() {
		return teamAsOneCheckBox.isSelected();
	}
	
	/**
	 * Returns whether all chart is to be displayed on 1 chart.
	 * 
	 * @return true if all chart is to be displayed on 1 chart; false otherwise
	 */
	public boolean getAllOnOneChart() {
		return allOnOneChartCheckBox.isSelected();
	}
	
}
