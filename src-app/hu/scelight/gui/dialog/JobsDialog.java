/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight.gui.dialog;

import hu.scelight.gui.icon.Icons;
import hu.scelight.service.env.Env;
import hu.scelight.util.Utils;
import hu.scelight.util.gui.GuiUtils;
import hu.sllauncher.action.XAction;
import hu.sllauncher.gui.comp.ToolBarForTable;
import hu.sllauncher.gui.comp.XDialog;
import hu.sllauncher.gui.comp.XProgressBar;
import hu.sllauncher.gui.comp.table.XTable;
import hu.sllauncher.gui.comp.table.XTableModel;
import hu.sllauncher.service.job.Job;
import hu.sllauncher.service.job.ProgressJob;
import hu.sllauncher.util.ControlledState;
import hu.sllauncher.util.DurationValue;
import hu.sllauncher.util.SkillLevel;
import hu.sllauncher.util.gui.adapter.ActionAdapter;
import hu.sllauncher.util.gui.adapter.PropertyChangeAdapter;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.Thread.State;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import javax.swing.SwingUtilities;
import javax.swing.Timer;

/**
 * A dialog to view and interact running jobs.
 * 
 * @author Andras Belicza
 */
@SuppressWarnings( "serial" )
public class JobsDialog extends XDialog {
	
	/** Unpauses the selected jobs. */
	private final XAction          unpauseAction = new XAction( Icons.F_CONTROL, "Unpause Selected Jobs (Enter)", cp ) {
		                                             @Override
		                                             public void actionPerformed( final ActionEvent event ) {
			                                             for ( final int row : table.getSelectedModelRows() )
				                                             jobList.get( row ).requestUnpause();
		                                             }
	                                             };
	
	/** Pauses the selected jobs. */
	private final XAction          pauseAction   = new XAction( Icons.F_CONTROL_PAUSE, "Pause Selected Jobs", cp ) {
		                                             @Override
		                                             public void actionPerformed( final ActionEvent event ) {
			                                             for ( final int row : table.getSelectedModelRows() )
				                                             jobList.get( row ).requestPause();
		                                             }
	                                             };
	
	/** Cancels the selected jobs. */
	private final XAction          cancelAction  = new XAction( Icons.F_CROSS_OCTAGON, "Cancel Selected Jobs... (Delete)", cp ) {
		                                             @Override
		                                             public void actionPerformed( final ActionEvent event ) {
			                                             final int[] selectedRows = table.getSelectedModelRows();
			                                             // Jobs might finish while waiting for confirmation.
			                                             // When a job is finished, table is refreshed, selection is lost.
			                                             // So "cache" selected jobs here.
			                                             final List< Job > selJobList = new ArrayList<>( selectedRows.length );
			                                             for ( final int row : selectedRows )
				                                             selJobList.add( jobList.get( row ) );
			                                             if ( !GuiUtils.confirm( Utils
			                                                     .plural( "Are you sure you want to cancel %s job%s?", selectedRows.length ) ) )
				                                             return;
			                                             for ( final Job job : selJobList )
				                                             job.requestCancel();
		                                             }
	                                             };
	
	
	/** Table displaying the running jobs. */
	private final XTable           table         = new XTable();
	
	/** Jobs listener. */
	private PropertyChangeListener jobListener;
	
	/** Table refresh timer. */
	private final Timer            timer         = new Timer( 250, null ); // 4 FPS
	                                                                       
	/** Displayed job list. No need to synchronize: only accessed from the EDT. */
	private List< Job >            jobList;
	
	
	/**
	 * Creates a new {@link JobsDialog}.
	 */
	public JobsDialog() {
		super( Env.MAIN_FRAME );
		
		buildGui();
		
		restoreDefaultSize();
		
		setVisible( true );
	}
	
	/**
	 * Builds the GUI of the jobs dialog.
	 */
	private void buildGui() {
		final ToolBarForTable toolBar = new ToolBarForTable( table );
		toolBar.addSelectInfoLabel( "Select a Job." );
		toolBar.addSelEnabledButton( unpauseAction );
		toolBar.addSelEnabledButton( pauseAction );
		toolBar.addSeparator();
		toolBar.addSelEnabledButton( cancelAction );
		toolBar.finalizeLayout();
		cp.addNorth( toolBar );
		
		table.setOpenAction( unpauseAction );
		table.setDeleteAction( cancelAction );
		table.setRowHeightForProgressBar();
		cp.addCenter( table.createWrapperBox( true ) );
		
		getRootPane().setDefaultButton( addCloseButton( "_OK" ) );
		
		final XTableModel model = table.getXTableModel();
		
		final Vector< String > columns = Utils.asNewVector( "#", "Job Name", "Started At", "Execution time", "Progress", "State" );
		final List< Class< ? > > columnClasses = Utils.< Class< ? > > asNewList( Integer.class, Job.class, Date.class, DurationValue.class, XProgressBar.class,
		        ControlledState.class );
		if ( SkillLevel.DEVELOPER.isAtLeast() ) {
			columns.add( "Internal State" );
			columnClasses.add( State.class );
		}
		final int jobColIdx = 2;
		final int execTimeColIdx = 3;
		final int progressBarColIdx = 4;
		final int controlledStateColIdx = 5;
		final int internalStateColIdx = 6;
		
		model.setColumnIdentifiers( columns );
		model.setColumnClasses( columnClasses );
		
		final ActionAdapter updateTableTask = new ActionAdapter() {
			@Override
			public void actionPerformed( final ActionEvent event ) {
				// We're in the EDT (because using Swing Timer)
				for ( int i = model.getRowCount() - 1; i >= 0; i-- ) {
					final Job job = jobList.get( i );
					model.setValueAt( new DurationValue( job.getExecTimeMs() ), i, execTimeColIdx );
					model.setValueAt( job.getControlledState(), i, controlledStateColIdx );
					final XProgressBar progressBar = (XProgressBar) model.getValueAt( i, progressBarColIdx );
					if ( progressBar != null ) {
						final int max = ( (ProgressJob) job ).getMaximumProgress();
						progressBar.setIndeterminate( max < 0 );
						progressBar.setStringPainted( max >= 0 );
						// Note: max value has to be set prior to value, else when rendering the default 100 is used!
						progressBar.setMaximum( Math.max( max, 0 ) );
						progressBar.setValue( ( (ProgressJob) job ).getCurrentProgress() );
					}
					if ( SkillLevel.DEVELOPER.isAtLeast() )
						model.setValueAt( job.getState(), i, internalStateColIdx );
				}
				
				table.pack();
			}
		};
		
		jobListener = new PropertyChangeAdapter( true ) {
			@Override
			public void propertyChange( final PropertyChangeEvent event ) {
				SwingUtilities.invokeLater( new Runnable() {
					@Override
					public void run() {
						table.saveSelection( jobColIdx );
						
						jobList = Env.JOBS.getJobList();
						
						setTitle( "Running Jobs (" + jobList.size() + ")" );
						setIconImage( ( jobList.isEmpty() ? Icons.F_HARD_HAT : Icons.F_HARD_HAT_MILITARY ).get().getImage() );
						
						model.getDataVector().clear();
						model.fireTableDataChanged();
						
						int i = 1;
						for ( final Job job : jobList ) {
							final XProgressBar progressBar = job instanceof ProgressJob ? new XProgressBar() : null;
							if ( progressBar != null )
								progressBar.setPreferredSize( new Dimension( 1, 1 ) );
							final Vector< Object > row = Utils.< Object > asNewVector( i++, job, job.getStartedAt(), null, progressBar, null );
							if ( SkillLevel.DEVELOPER.isAtLeast() )
								row.add( null );
							model.addRow( row );
						}
						
						table.restoreSelection( jobColIdx );
						
						updateTableTask.actionPerformed( null );
					}
				} );
			}
		};
		Env.JOBS.addListener( jobListener );
		
		timer.addActionListener( updateTableTask );
		timer.start();
	}
	
	@Override
	protected void customOnClose() {
		timer.stop();
		Env.JOBS.removeListener( jobListener );
	}
	
}
