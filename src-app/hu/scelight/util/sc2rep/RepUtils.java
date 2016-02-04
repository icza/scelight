/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight.util.sc2rep;

import hu.scelight.bean.repfolders.RepFolderBean;
import hu.scelight.gui.comp.RepInfoBox;
import hu.scelight.gui.icon.Icons;
import hu.scelight.service.env.Env;
import hu.scelight.service.settings.Settings;
import hu.sllauncher.gui.comp.XFileChooser;
import hu.sllauncher.gui.comp.combobox.XComboBox;
import hu.sllauncher.util.Int;
import hu.sllauncher.util.gui.RenderablePair;
import hu.sllauncher.util.gui.adapter.ActionAdapter;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.Collections;

import javax.swing.filechooser.FileFilter;

/**
 * Sc2 Replay utilities.
 * 
 * @author Andras Belicza
 */
public class RepUtils {
	
	/**
	 * StarCraft II replay file filter.<br>
	 * I don't use FileNameExtensionFilter because my implementation is much faster...
	 */
	public static FileFilter SC2_REPLAY_FILTER = new FileFilter() {
		                                           @Override
		                                           public String getDescription() {
			                                           return "StarCraft II Replay files (*.SC2Replay)";
		                                           }
		                                           
		                                           @Override
		                                           public boolean accept( final File f ) {
			                                           // Number of folders is relatively little while files can be many
			                                           // (thousands), so check extension first...
			                                           return hasRepExt( f.getName() ) || f.isDirectory();
		                                           }
	                                           };
	
	/**
	 * Creates an {@link XFileChooser} dialog configured for choosing replay files.
	 * 
	 * @param multiSelect tells if multiple replays should/are to be selected
	 * @return an {@link XFileChooser} dialog configured for choosing replay files
	 */
	public static XFileChooser createReplayChooserDialog( final boolean multiSelect ) {
		final XFileChooser fileChooser = new XFileChooser();
		
		configureReplayChooserDialog( fileChooser, multiSelect );
		
		return fileChooser;
	}
	
	/**
	 * Configures the specified {@link XFileChooser} dialog for choosing a replay file.
	 * 
	 * @param fileChooser file chooser to be configured
	 * @param multiSelect tells if multiple replays should/are to be selected
	 */
	public static void configureReplayChooserDialog( final XFileChooser fileChooser, final boolean multiSelect ) {
		// TODO Consider creating a replay or a global Scelight file view like
		// fileChooser.setFileView( GuiUtils.SC2GEARS_FILE_VIEW );
		
		fileChooser.setCurrentFolder( Env.APP_SETTINGS.get( Settings.START_FOLDER_WHEN_CHOOSING_REPS ) );
		fileChooser.setDialogTitle( multiSelect ? "Choose Replays to Open" : "Choose a Replay to Open" );
		fileChooser.setMultiSelectionEnabled( multiSelect );
		fileChooser.setApproveButtonText( "Open" );
		fileChooser.setFileFilter( SC2_REPLAY_FILTER );
		fileChooser.setAccessory( new RepInfoBox( fileChooser ) );
	}
	
	/**
	 * Creates a {@link XComboBox} which is populated with the replay folders, and when one is selected, sets it as the current directory of the specified file
	 * chooser.
	 * 
	 * @param fileChooser target file chooser to set the selected replay folder
	 * @return a {@link XComboBox} of replay folders to set to the file chooser when selected
	 */
	public static XComboBox< ? > createRepFoldersShortcutCombo( final XFileChooser fileChooser ) {
		final XComboBox< Object > comboBox = new XComboBox<>();
		
		comboBox.addItem( "<html><span style='font-style:italic;color:#555555'>Jump to a Replay Folder...</span></html>" );
		
		// Start folder when choosing replays
		comboBox.addItem( new RenderablePair<>( Icons.F_BLUE_FOLDER_ARROW.get(), Env.APP_SETTINGS.get( Settings.START_FOLDER_WHEN_CHOOSING_REPS ) ) );
		
		for ( final RepFolderBean rb : Env.APP_SETTINGS.get( Settings.REPLAY_FOLDERS_BEAN ).getReplayFolderBeanList() )
			comboBox.addItem( new RenderablePair<>( rb.getOrigin().ricon.get(), rb.getPath() ) );
		
		comboBox.addActionListener( new ActionAdapter() {
			@Override
			@SuppressWarnings( "unchecked" )
			public void actionPerformed( final ActionEvent event ) {
				if ( comboBox.getSelectedIndex() <= 0 )
					return;
				
				fileChooser.setCurrentFolder( ( (RenderablePair< Path >) comboBox.getSelectedItem() ).value2 );
				
				comboBox.setSelectedIndex( 0 );
			}
		} );
		
		return comboBox;
	}
	
	/**
	 * Tells if the specified file has proper SC2 replay extension.
	 * 
	 * @param file file to be tested
	 * @return true if the specified file has proper SC2 replay extension; false otherwise
	 */
	public static boolean hasRepExt( final Path file ) {
		return hasRepExt( file.getFileName().toString() );
	}
	
	/**
	 * Tells if the specified file name has proper SC2 replay extension.
	 * 
	 * @param fileName file name to be tested
	 * @return true if the specified file name has proper SC2 replay extension; false otherwise
	 */
	public static boolean hasRepExt( final String fileName ) {
		// First try most common extensions without lower-casing:
		if ( fileName.endsWith( ".SC2Replay" ) || fileName.endsWith( ".sc2replay" ) )
			return true;
		
		if ( fileName.toLowerCase().endsWith( ".sc2replay" ) )
			return true;
		
		return false;
	}
	
	/**
	 * Counts the SC2 replay files in the specified folder (non-recursive).
	 * 
	 * @param folder folder in which to count
	 * @return the number of SC2 replay files in the specified folder; -1 if the specified path exists but is not a folder or if some error occurs
	 */
	public static int countReplays( final Path folder ) {
		if ( !Files.exists( folder ) )
			return 0;
		if ( !Files.isDirectory( folder ) )
			return -1;
		
		final Int count = new Int();
		
		try {
			Files.walkFileTree( folder, Collections.< FileVisitOption > emptySet(), 1, new SimpleFileVisitor< Path >() {
				@Override
				public FileVisitResult visitFile( final Path file, final BasicFileAttributes attrs ) throws IOException {
					if ( !attrs.isDirectory() && hasRepExt( file ) )
						count.value++;
					
					return FileVisitResult.CONTINUE;
				}
			} );
		} catch ( final IOException ie ) {
			Env.LOGGER.error( "Failed to count replays in folder: " + folder, ie );
			return -1;
		}
		
		return count.value;
	}
	
	/**
	 * Returns a normalized form of the specified match-up string (which can be either a race match-up or a league match-up).
	 * 
	 * <p>
	 * The gain of this normalization is that 2 different match-ups which are the permutations of each other have the same normalized form therefore match-up
	 * permutations can be tested if they are equal by a simple equality filter (where we compare the normalized forms of the match-up filter and the match-up
	 * of the entities).
	 * </p>
	 * 
	 * <p>
	 * Normalization consists of the following steps:
	 * <ol>
	 * <li>make match-up lower-cased (e.g. <code>"ZZPvTPZ"</code> => <code>"zzpvtpz"</code>)
	 * <li>sort letters inside teams (e.g. <code>"zzpvtpz"</code> => <code>"pzzvptz"</code>)
	 * <li>sort teams (e.g. <code>"pzzvptz"</code> => <code>"ptzvpzz"</code>)
	 * </ol>
	 * </p>
	 * 
	 * @param matchup match-up to be normalized
	 * @return a normalized form of the specified match-up string
	 */
	public static String normalizeMatchup( final String matchup ) {
		if ( matchup == null )
			return null;
		
		// Step 1: lower-case
		final String[] teams = matchup.toLowerCase().split( "v" );
		
		// Step 2: sort letters inside teams
		for ( int i = 0; i < teams.length; i++ ) {
			final char[] letters = teams[ i ].toCharArray();
			Arrays.sort( letters );
			teams[ i ] = new String( letters );
		}
		
		// Step 3: sort teams
		Arrays.sort( teams );
		
		// Concatenate teams and return the normalized form:
		final StringBuilder sb = new StringBuilder();
		for ( int i = 0; i < teams.length; i++ ) {
			if ( i > 0 )
				sb.append( 'v' );
			sb.append( teams[ i ] );
		}
		
		return sb.toString();
	}
	
}
