/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight.sc2.map;

import hu.belicza.andras.mpq.InvalidMpqArchiveException;
import hu.belicza.andras.mpq.MpqParser;
import hu.belicza.andras.tga.TgaParser;
import hu.scelight.sc2.map.model.MapInfo;
import hu.scelight.sc2.map.model.MapObjects;
import hu.scelight.sc2.map.model.ObjectUnit;
import hu.scelight.sc2.rep.model.initdata.gamedesc.CacheHandle;
import hu.scelight.sc2.rep.repproc.RepProcessor;
import hu.scelight.service.env.Env;
import hu.scelight.service.settings.Settings;

import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * SC2 Map parser.
 * 
 * @author Andras Belicza
 */
public class MapParser {
	
	/**
	 * Returns the preview image of the map specified by a {@link RepProcessor}.
	 * 
	 * @param repProc {@link RepProcessor} whose map image to return
	 * @return the preview image of the specified map or <code>null</code> if the map file does not exists or cannot be parsed
	 */
	public static BufferedImage getMapImage( final RepProcessor repProc ) {
		final Path file = getMapFile( repProc );
		if ( file == null )
			return null;
			
		try ( final MpqParser mpqParser = new MpqParser( file ) ) {
			
			// Most common map preview name is "Minimap.tga". There might be a custom preview image file
			// which is specified by the thumbnail file in the details.
			// The problem is this can be a locale specific file name and my string hash calculator method
			// does not work well for special characters (e.g. Korean characters).
			//
			// So first always try the default Minimap.tga, and if it is not available, then proceed to
			// (try) to parse the custom preview image file.
			// Minimap.tga might also be available even if there is a custom preview image file.
			
			byte[] imageData = mpqParser.getFile( MapContent.MINIMAP_TGA );
			if ( imageData == null )
				imageData = mpqParser.getFile( repProc.replay.details.getThumbnailFile() );
				
			return imageData == null ? null : TgaParser.parseTga( imageData );
			
		} catch ( final InvalidMpqArchiveException imae ) {
			// Map file exists but is invalid, log it.
			Env.LOGGER.error( "Invalid map file: " + file, imae );
		} catch ( final Exception e ) {
			Env.LOGGER.error( "Failed to parse map info: " + file, e );
		}
		
		return null;
	}
	
	/**
	 * Returns the general map info of the map specified by a {@link RepProcessor}.
	 * 
	 * @param repProc {@link RepProcessor} whose map info to return
	 * @return the general map info of the specified map or <code>null</code> if the map file does not exists or cannot be parsed
	 */
	public static MapInfo getMapInfo( final RepProcessor repProc ) {
		final Path file = getMapFile( repProc );
		if ( file == null )
			return null;
			
		try ( final MpqParser mpqParser = new MpqParser( file ) ) {
			
			final ByteBuffer wrapper = ByteBuffer.wrap( mpqParser.getFile( MapContent.MAP_INFO ) ).order( ByteOrder.LITTLE_ENDIAN );
			
			if ( wrapper.getInt() != 0x4d617049 ) // "IpaM" ("MapI" reversed)
				return null;
				
			final MapInfo mapInfo = new MapInfo();
			
			mapInfo.version = wrapper.getInt();
			if ( mapInfo.version > 0x17 )
				wrapper.position( wrapper.position() + 8 ); // 2x unknown int
				
			mapInfo.width = wrapper.getInt();
			mapInfo.height = wrapper.getInt();
			
			mapInfo.smallPreviewType = wrapper.getInt();
			if ( mapInfo.smallPreviewType == 2 )
				mapInfo.customSmallPreviewPath = readZeroTerminatedString( wrapper );
				
			mapInfo.largePreviewType = wrapper.getInt();
			if ( mapInfo.largePreviewType == 2 )
				mapInfo.customLargePreviewPath = readZeroTerminatedString( wrapper );
				
			if ( mapInfo.version >= 0x1f ) {
				// Unknown
				readZeroTerminatedString( wrapper );
				wrapper.getInt();
			}
			
			if ( mapInfo.version > 0x22 ) {
				// 0x22 is the last seen version, and this applies from version 0x26
				// But I use "> 0x22" instead of ">= 0x26" to cover possible LotV beta versions  
				readZeroTerminatedString( wrapper );
			}
			
			wrapper.getInt(); // Unknown
			
			mapInfo.fogType = readZeroTerminatedString( wrapper );
			mapInfo.tileSet = readZeroTerminatedString( wrapper );
			
			mapInfo.boundaryLeft = wrapper.getInt();
			mapInfo.boundaryBottom = wrapper.getInt();
			mapInfo.boundaryRight = wrapper.getInt();
			mapInfo.boundaryTop = wrapper.getInt();
			
			return mapInfo;
			
		} catch ( final InvalidMpqArchiveException imae ) {
			// Map file exists but is invalid, log it.
			Env.LOGGER.error( "Invalid map file: " + file, imae );
		} catch ( final Exception e ) {
			Env.LOGGER.error( "Failed to parse map info: " + file, e );
		}
		
		return null;
	}
	
	/**
	 * Reads a zero-char terminated string from the specified byte buffer.
	 * 
	 * @param wrapper byte buffer to read from
	 * @return the read string
	 */
	private static String readZeroTerminatedString( final ByteBuffer wrapper ) {
		final StringBuilder sb = new StringBuilder();
		
		byte ch;
		while ( ( ch = wrapper.get() ) != 0 )
			sb.append( (char) ch );
			
		return sb.toString();
	}
	
	/**
	 * Returns the attributes of the map specified by a {@link RepProcessor}.
	 * 
	 * <p>
	 * Returns a map of maps, where the key of the outer map is the locale string and the inner map has attribute name-value pairs.
	 * </p>
	 * 
	 * @param repProc {@link RepProcessor} whose map attributes to return
	 * @return the attributes of the specified map or <code>null</code> if the map file does not exists or cannot be parsed
	 */
	public static Map< String, Map< String, String > > getMapAttributes( final RepProcessor repProc ) {
		final Path file = getMapFile( repProc );
		if ( file == null )
			return null;
			
		try ( final MpqParser mpqParser = new MpqParser( file ) ) {
			final Map< String, Map< String, String > > localeAttributeMapMap = new HashMap< >();
			
			final ByteBuffer wrapper = ByteBuffer.wrap( mpqParser.getFile( MapContent.DOCUMENT_HEADER ) ).order( ByteOrder.LITTLE_ENDIAN );
			
			if ( wrapper.getInt() != 0x53433248 ) // "H2CS" ("SC2H" reversed)
				return null;
				
			wrapper.position( 44 ); // 44 byte header
			
			final int dependenciesCount = wrapper.getInt();
			for ( int i = 0; i < dependenciesCount; i++ )
				while ( wrapper.get() != 0 )
					; // Dependency strings (0-char terminated)
					
			final int attributesCount = wrapper.getInt();
			
			byte[] buffer;
			final char[] localeChars = new char[ 5 ];
			localeChars[ 2 ] = '-';
			for ( int i = 0; i < attributesCount; i++ ) {
				// Key length and value
				wrapper.get( buffer = new byte[ wrapper.getShort() ] );
				final String key = new String( buffer, Env.UTF8 );
				
				// Locale (lang+local) reversed, example: "SUne" => "en-US"
				localeChars[ 4 ] = (char) wrapper.get();
				localeChars[ 3 ] = (char) wrapper.get();
				localeChars[ 1 ] = (char) wrapper.get();
				localeChars[ 0 ] = (char) wrapper.get();
				final String locale = new String( localeChars );
				
				// Value length and value
				wrapper.get( buffer = new byte[ wrapper.getShort() ] );
				final String value = new String( buffer, Env.UTF8 ).replace( "<n/>", "\n" );
				
				Map< String, String > attributeMap = localeAttributeMapMap.get( locale );
				if ( attributeMap == null )
					localeAttributeMapMap.put( locale, attributeMap = new HashMap< >() );
				attributeMap.put( key, value );
			}
			
			// Attribute parsing was successful, return it
			return localeAttributeMapMap;
			
		} catch ( final InvalidMpqArchiveException imae ) {
			// Map file exists but is invalid, log it.
			Env.LOGGER.error( "Invalid map file: " + file, imae );
		} catch ( final Exception e ) {
			Env.LOGGER.error( "Failed to parse map attributes: " + file, e );
		}
		
		return null;
	}
	
	/**
	 * Returns the map objects of the map specified by a {@link RepProcessor}.
	 * 
	 * @param repProc {@link RepProcessor} whose map objects to return
	 * @return the map objects of the specified map or <code>null</code> if the map file does not exists or cannot be parsed
	 */
	public static MapObjects getMapObjects( final RepProcessor repProc ) {
		final Path file = getMapFile( repProc );
		if ( file == null )
			return null;
			
		try ( final MpqParser mpqParser = new MpqParser( file ) ) {
			
			final byte[] mapObjectsData = mpqParser.getFile( MapContent.OBJECTS );
			
			final Document mapObjectsDocument = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse( new ByteArrayInputStream( mapObjectsData ) );
			final Element docElement = mapObjectsDocument.getDocumentElement();
			
			final MapObjects mapObjects = new MapObjects();
			
			// Objects on the map
			final List< ObjectUnit > unitList = mapObjects.unitList = new ArrayList< >( 128 );
			final NodeList unitNodeList = docElement.getElementsByTagName( "ObjectUnit" );
			for ( int i = unitNodeList.getLength() - 1; i >= 0; i-- ) {
				final Element unit = (Element) unitNodeList.item( i );
				final ObjectUnit u = new ObjectUnit();
				u.unitType = unit.getAttribute( "UnitType" ).intern();
				u.pos = parsePoint( unit.getAttribute( "Position" ) );
				unitList.add( u );
			}
			
			// Start locations of the map
			final List< Point2D.Float > startLocationList = mapObjects.startLocationList = new ArrayList< >();
			final NodeList startLocList = docElement.getElementsByTagName( "ObjectPoint" );
			for ( int i = startLocList.getLength() - 1; i >= 0; i-- ) {
				final Element startLoc = (Element) startLocList.item( i );
				if ( "StartLoc".equals( startLoc.getAttribute( "Type" ) ) )
					startLocationList.add( parsePoint( startLoc.getAttribute( "Position" ) ) );
			}
			
			return mapObjects;
			
		} catch ( final InvalidMpqArchiveException imae ) {
			// Map file exists but is invalid, log it.
			Env.LOGGER.error( "Invalid map file: " + file, imae );
		} catch ( final Exception e ) {
			Env.LOGGER.error( "Failed to parse map objects: " + file, e );
		}
		
		return null;
	}
	
	/**
	 * Parses a float-precision point from the specified text.
	 * 
	 * @param text text to parse the point from
	 * @return the parsed float-precision point
	 */
	private static Point2D.Float parsePoint( final String text ) {
		final int i = text.indexOf( ',' );
		final int j = text.indexOf( ',', i + 1 );
		if ( i == -1 || j == -1 ) {
			return new Point2D.Float( 0,  0 ); // In Co-op games text may be an empty string
		}
		
		return new Point2D.Float( Float.parseFloat( text.substring( 0, i ) ), Float.parseFloat( text.substring( i + 1, j ) ) );
	}
	
	/**
	 * Returns the file of the map specified by a {@link RepProcessor}.
	 * 
	 * @param repProc {@link RepProcessor} whose map file to return
	 * @return the file of the map specified by its {@link CacheHandle} or <code>null</code> if the map file does not exists
	 */
	public static Path getMapFile( final RepProcessor repProc ) {
		final CacheHandle mapCacheHandle = repProc.getMapCacheHandle();
		if ( mapCacheHandle == null )
			return null;
			
		final Path file = Env.APP_SETTINGS.get( Settings.SC2_MAPS_FOLDER ).resolve( repProc.getMapCacheHandle().getRelativeFile() );
		
		if ( Files.exists( file ) )
			return file;
			
		// Enqueue map for downloading.
		Env.MAP_DOWNLOAD_MANAGER.add( mapCacheHandle );
		
		return null;
	}
	
}
