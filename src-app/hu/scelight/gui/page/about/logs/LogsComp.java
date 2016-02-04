/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight.gui.page.about.logs;

import hu.scelight.gui.icon.Icons;
import hu.scelight.gui.setting.SettingsGui;
import hu.scelight.service.env.Env;
import hu.sllauncher.action.XAction;
import hu.sllauncher.gui.comp.ModestLabel;
import hu.sllauncher.gui.comp.XLabel;
import hu.sllauncher.gui.comp.XToolBar;
import hu.sllauncher.gui.page.logs.LLogsPageComp;
import hu.sllauncher.service.log.Logger;
import hu.sllauncher.util.SizeFormat;
import hu.sllauncher.util.SkillLevel;
import hu.sllauncher.util.gui.LGuiUtils;
import hu.sllauncher.util.gui.adapter.ActionAdapter;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Collections;
import java.util.Vector;

import javax.swing.JButton;

/**
 * Improved Logs page component for the application.
 * 
 * @author Andras Belicza
 */
@SuppressWarnings( "serial" )
public class LogsComp extends LLogsPageComp {
	
	/**
	 * Builds the GUI of the logs page component.
	 */
	@Override
	protected void buildGui() {
		// Add log file chooser
		final XAction prevLogFileAction = new XAction( Icons.F_ARROW_180, "Previous Log File", this ) {
			@Override
			public void actionPerformed( final ActionEvent event ) {
				logFilesComboBox.setSelectedIndex( logFilesComboBox.getSelectedIndex() - 1 );
				
			}
		};
		
		final XAction nextLogFileAction = new XAction( Icons.F_ARROW, "Next Log File", this ) {
			@Override
			public void actionPerformed( final ActionEvent event ) {
				logFilesComboBox.setSelectedIndex( logFilesComboBox.getSelectedIndex() + 1 );
			}
		};
		
		// Tool bar
		final XToolBar toolBar = new XToolBar();
		toolBarsBox.add( toolBar );
		final JButton prevLogButton = toolBar.add( prevLogFileAction );
		LGuiUtils.autoCreateDisabledImage( prevLogButton );
		final JButton nextLogButton = toolBar.add( nextLogFileAction );
		LGuiUtils.autoCreateDisabledImage( nextLogButton );
		logFilesComboBox.addActionListener( new ActionAdapter( true ) {
			@Override
			public void actionPerformed( final ActionEvent event ) {
				prevLogButton.setEnabled( logFilesComboBox.getSelectedIndex() > 0 );
				nextLogButton.setEnabled( logFilesComboBox.getSelectedIndex() < logFilesComboBox.getItemCount() - 1 );
			}
		} );
		toolBar.addSeparator();
		toolBar.add( new XLabel( "Log file:" ).leftBorder( 5 ) );
		logFilesComboBox.addActionListener( refreshAction );
		toolBar.add( logFilesComboBox );
		toolBar.add( sizeLabel.leftBorder( 5 ) );
		toolBar.addSeparator();
		toolBar.add( new ModestLabel( "Log files: " + Env.LANG.formatNumber( logFilesComboBox.getItemCount() ) ) );
		toolBar.finalizeLayout();
		toolBarsBox.add( toolBar );
		
		SettingsGui.bindVisibilityToSkillLevel( toolBar, SkillLevel.NORMAL );
		
		super.buildGui();
	}
	
	/**
	 * Refreshes the displayed logs from the file.
	 */
	@Override
	public void refreshLogs() {
		final Path logPath = Env.PATH_LOGS.resolve( logFilesComboBox.getSelectedItem() );
		
		try {
			sizeLabel.setText( "File size: " + SizeFormat.AUTO.formatSize( Files.size( logPath ), 2 ) );
		} catch ( final IOException ie ) {
			sizeLabel.setText( "File size: <unknown>" );
		}
		
		super.refreshLogs();
	}
	
	/**
	 * Returns the vector of the available log files in descendant order (newest is the first).
	 * 
	 * @return the vector of the available log files in descendant order (newest is the first)
	 */
	@Override
	protected Vector< String > getLogFileVector() {
		final Vector< String > logFileVector = new Vector<>();
		try {
			Files.walkFileTree( Env.PATH_LOGS, Collections.< FileVisitOption > emptySet(), 1, new SimpleFileVisitor< Path >() {
				@Override
				public FileVisitResult visitFile( final Path file, BasicFileAttributes attrs ) throws IOException {
					if ( attrs.isDirectory() )
						return FileVisitResult.CONTINUE;
					
					final String fileName = file.getFileName().toString();
					if ( fileName.endsWith( Logger.LOG_EXTENSION ) )
						logFileVector.add( fileName );
					
					return FileVisitResult.CONTINUE;
				}
			} );
		} catch ( final IOException ie ) {
			Env.LOGGER.error( "Could not list log files!", ie );
			logFileVector.clear();
			logFileVector.add( Env.LOGGER.activeLogPath.getFileName().toString() );
		}
		
		Collections.sort( logFileVector, Collections.reverseOrder() );
		
		return logFileVector;
	}
	
}
