/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelightapi.sc2.map;

/**
 * General map info.
 * 
 * @author Andras Belicza
 */
public interface IMapInfo {
	
	/**
	 * Returns the version of the map file.
	 * 
	 * @return the version of the map file
	 */
	int getVersion();
	
	/**
	 * Returns the width of the map.
	 * 
	 * @return the width of the map
	 */
	int getWidth();
	
	/**
	 * Returns the height of the map.
	 * 
	 * @return the height of the map
	 */
	int getHeight();
	
	/**
	 * Returns the small preview type; 0 = None, 1 = Minimap, 2 = Custom.
	 * 
	 * @return the small preview type; 0 = None, 1 = Minimap, 2 = Custom
	 */
	int getSmallPreviewType();
	
	/**
	 * Returns the small preview path in case of custom ({@link #getSmallPreviewType()}).
	 * 
	 * @return the small preview path in case of custom ({@link #getSmallPreviewType()})
	 */
	String getCustomSmallPreviewPath();
	
	/**
	 * Returns the large preview type; 0 = None, 1 = Minimap, 2 = Custom.
	 * 
	 * @return the large preview type; 0 = None, 1 = Minimap, 2 = Custom
	 */
	int getLargePreviewType();
	
	/**
	 * Returns the large preview path in case of custom ({@link #getLargePreviewType()}).
	 * 
	 * @return the large preview path in case of custom ({@link #getLargePreviewType()})
	 */
	String getCustomLargePreviewPath();
	
	/**
	 * Returns the fog of War type.
	 * 
	 * @return the fog of War type
	 */
	String getFogType();
	
	/**
	 * Returns the tile set used on the map.
	 * 
	 * @return the tile set used on the map
	 */
	String getTileSet();
	
	/**
	 * Returns the left boundary of the playable size of the map.
	 * 
	 * @return the left boundary of the playable size of the map
	 */
	int getBoundaryLeft();
	
	/**
	 * Returns the bottom boundary of the playable size of the map.
	 * 
	 * @return the bottom boundary of the playable size of the map
	 */
	int getBoundaryBottom();
	
	/**
	 * Returns the right boundary of the playable size of the map.
	 * 
	 * @return the right boundary of the playable size of the map
	 */
	int getBoundaryRight();
	
	/**
	 * Returns the top boundary of the playable size of the map.
	 * 
	 * @return the top boundary of the playable size of the map
	 */
	int getBoundaryTop();
	
	/**
	 * Returns the playable width of the map.
	 * 
	 * @return the playable width of the map
	 */
	int getPlayableWidth();
	
	/**
	 * Returns the playable height of the map.
	 * 
	 * @return the playable height of the map
	 */
	int getPlayableHeight();
	
	/**
	 * Returns the size of the map in a format of <code>WIDTHxHEIGHT</code>.
	 * 
	 * @return the size of the map in a format of <code>WIDTHxHEIGHT</code>
	 */
	String getSizeString();
	
	/**
	 * Returns the playable size of the map in a format of <code>WIDTHxHEIGHT</code>.
	 * 
	 * @return the playable size of the map in a format of <code>WIDTHxHEIGHT</code>
	 */
	String getPlayableSizeString();
	
}
