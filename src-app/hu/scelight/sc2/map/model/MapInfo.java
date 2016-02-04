/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight.sc2.map.model;

import hu.scelightapi.sc2.map.IMapInfo;

/**
 * Class describing general map info.
 * 
 * @author Andras Belicza
 */
public class MapInfo implements IMapInfo {
	
	/** Version of the map file. */
	public int    version;
	
	/** Width of the map. */
	public int    width;
	
	/** Height of the map. */
	public int    height;
	
	/** Small preview type; 0 = None, 1 = Minimap, 2 = Custom. */
	public int    smallPreviewType;
	
	/** Small preview path in case of custom ({@link #smallPreviewType}). */
	public String customSmallPreviewPath;
	
	/** Large preview type; 0 = None, 1 = Minimap, 2 = Custom. */
	public int    largePreviewType;
	
	/** Large preview path in case of custom ({@link #largePreviewType}). */
	public String customLargePreviewPath;
	
	/** Fog of War type. */
	public String fogType;
	
	/** Tile set used on the map. */
	public String tileSet;
	
	/** Left boundary of the playable size of the map. */
	public int    boundaryLeft;
	
	/** Bottom boundary of the playable size of the map. */
	public int    boundaryBottom;
	
	/** Right boundary of the playable size of the map. */
	public int    boundaryRight;
	
	/** Top boundary of the playable size of the map. */
	public int    boundaryTop;
	
	
	@Override
	public int getVersion() {
		return version;
	}
	
	@Override
	public int getWidth() {
		return width;
	}
	
	@Override
	public int getHeight() {
		return height;
	}
	
	@Override
	public int getSmallPreviewType() {
		return smallPreviewType;
	}
	
	@Override
	public String getCustomSmallPreviewPath() {
		return customSmallPreviewPath;
	}
	
	@Override
	public int getLargePreviewType() {
		return largePreviewType;
	}
	
	@Override
	public String getCustomLargePreviewPath() {
		return customLargePreviewPath;
	}
	
	@Override
	public String getFogType() {
		return fogType;
	}
	
	@Override
	public String getTileSet() {
		return tileSet;
	}
	
	@Override
	public int getBoundaryLeft() {
		return boundaryLeft;
	}
	
	@Override
	public int getBoundaryBottom() {
		return boundaryBottom;
	}
	
	@Override
	public int getBoundaryRight() {
		return boundaryRight;
	}
	
	@Override
	public int getBoundaryTop() {
		return boundaryTop;
	}
	
	@Override
	public int getPlayableWidth() {
		return boundaryRight - boundaryLeft;
	}
	
	@Override
	public int getPlayableHeight() {
		return boundaryTop - boundaryBottom;
	}
	
	@Override
	public String getSizeString() {
		return new StringBuilder().append( width ).append( 'x' ).append( height ).toString();
	}
	
	@Override
	public String getPlayableSizeString() {
		return new StringBuilder().append( getPlayableWidth() ).append( 'x' ).append( getPlayableHeight() ).toString();
	}
	
}
