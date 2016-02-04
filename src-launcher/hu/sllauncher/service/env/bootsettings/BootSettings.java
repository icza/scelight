/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.sllauncher.service.env.bootsettings;

import static hu.sllauncher.util.SkillLevel.NORMAL;
import hu.sllauncher.bean.settings.type.EnumSetting;
import hu.sllauncher.bean.settings.type.NodeSetting;
import hu.sllauncher.bean.settings.type.PathSetting;
import hu.sllauncher.bean.settings.type.SettingsGroup;
import hu.sllauncher.bean.settings.type.viewhints.VHB;
import hu.sllauncher.gui.help.LHelps;
import hu.sllauncher.gui.icon.LIcons;

import java.nio.file.Paths;

/**
 * Instances of the boot settings.
 * 
 * <p>
 * This is saved in a separate file, in the Scelight folder. This is because launcher settings are saved in the workspace folder denoted by these settings.
 * </p>
 * 
 * <p>
 * <i>Implementation note:</i> This is an interface instead of a class because constants defined in an interface are implicitly <code>public</code>,
 * <code>static</code> and <code>final</code>, and so these keywords can be omitted!
 * </p>
 * 
 * @author Andras Belicza
 */
public interface BootSettings {
	
	// WORKSPACE SETTINGS
	
	/** Workspace settings. */
	NodeSetting                          NODE_WORKSPACE   = new NodeSetting( "WORKSPACE", null, "Workspace", LIcons.F_FOLDER_SMILEY );
	
	/** Workspace group. */
	SettingsGroup                        GROUP_WORKSPACE  = new SettingsGroup( "Workspace", LHelps.WORKSPACE );
	
	/** Workspace location type. */
	EnumSetting< WorkspaceLocationType > WS_LOCATION_TYPE = new EnumSetting<>( "WS_LOCATION_TYPE", NODE_WORKSPACE, GROUP_WORKSPACE, NORMAL,
	                                                              "Workspace Location",
	                                                              VHB.sstext_( "<html><b><font color='red'>Requires restart!</font></b></html>" ),
	                                                              WorkspaceLocationType.USER_HOME );
	
	/** Custom folder in case of {@link WorkspaceLocationType#CUSTOM_FOLDER} location type. */
	PathSetting                          WS_CUSTOM_FOLDER = new PathSetting( "WS_CUSTOM_FOLDER", NODE_WORKSPACE, GROUP_WORKSPACE, NORMAL,
	                                                              "Workspace Custom Folder", new VHB()
	                                                                      .sstext( "<html><b><font color='red'>Requires restart!</font></b></html>" )
	                                                                      .dtitle( "Choose a custom folder for the Workspace" ).build(), Paths.get( "" ) );
	
}
