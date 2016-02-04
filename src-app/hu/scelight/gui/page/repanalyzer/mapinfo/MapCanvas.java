/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight.gui.page.repanalyzer.mapinfo;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.Map;

import javax.swing.Icon;
import javax.swing.JComponent;

import hu.scelight.gui.icon.Icons;
import hu.scelight.sc2.balancedata.BalanceData;
import hu.scelight.sc2.balancedata.BdUtil;
import hu.scelight.sc2.map.cache.MapImageCache;
import hu.scelight.sc2.map.model.MapInfo;
import hu.scelight.sc2.map.model.MapObjects;
import hu.scelight.sc2.map.model.ObjectUnit;
import hu.scelight.sc2.rep.repproc.RepProcessor;
import hu.scelight.sc2.rep.repproc.User;
import hu.scelight.service.env.Env;
import hu.scelight.service.settings.Settings;
import hu.scelight.util.gui.GuiUtils;
import hu.sllauncher.gui.comp.combobox.XComboBox;
import hu.sllauncher.gui.icon.LRIcon;

/**
 * Canvas to draw the map image with the map objects (like mineral field) and player start locations.
 * 
 * @author Andras Belicza
 */
@SuppressWarnings( "serial" )
class MapCanvas extends JComponent {
	
	/** Representing colors of units on the map image. */
	private static final Map< String, Color > UNIT_COLOR_MAP = new HashMap< >();
	
	static {
		final Color mineralColor = new Color( 130, 130, 255 );
		final Color geyserColor = Color.GREEN;
		
		UNIT_COLOR_MAP.put( BdUtil.UNIT_MINERAL_FIELD, mineralColor );
		UNIT_COLOR_MAP.put( BdUtil.UNIT_MINERAL_FIELD_750, mineralColor );
		UNIT_COLOR_MAP.put( BdUtil.UNIT_LAB_MINERAL_FIELD, mineralColor );
		UNIT_COLOR_MAP.put( BdUtil.UNIT_LAB_MINERAL_FIELD_750, mineralColor );
		UNIT_COLOR_MAP.put( BdUtil.UNIT_RICH_MINERAL_FIELD, Color.ORANGE );
		UNIT_COLOR_MAP.put( BdUtil.UNIT_RICH_MINERAL_FIELD_750, Color.ORANGE );
		UNIT_COLOR_MAP.put( BdUtil.UNIT_VESPENE_GEYSER, geyserColor );
		UNIT_COLOR_MAP.put( BdUtil.UNIT_PROTOSS_VESPENE_GEYSER, geyserColor );
		UNIT_COLOR_MAP.put( BdUtil.UNIT_RICH_VESPENE_GEYSER, geyserColor );
		UNIT_COLOR_MAP.put( BdUtil.UNIT_SPACE_PLATFORM_VESPENE_GEYSER, geyserColor );
		UNIT_COLOR_MAP.put( BdUtil.UNIT_XEL_NAGA_TOWER, Color.WHITE );
	}
	
	/** Representing color used for destructible objects. */
	private static final Color		COLOR_DESTRUCTIBLE = new Color( 139, 69, 19 );	  // Brown
													   
	/** Map downloading indicator. */
	private static final Icon		MAP_DL_ICON		   = Icons.F_HOURGLASS.size( 64 );
													   
													   
	/** Replay processor. */
	private final RepProcessor		repProc;
									
	/** Combo box which tells how to zoom the map image. */
	private final XComboBox< Zoom >	zoomComboBox;
									
	/** Ricon of the map image. */
	private final LRIcon			ricon;
									
	/** Pre-scaled icon of the map image. */
	private Icon					icon;
									
	/** Tells if player names have to be displayed. */
	private boolean					showPlayerNames;
									
									
	/**
	 * Creates a new {@link MapCanvas}.
	 * 
	 * @param repProc replay processor
	 * @param zoomComboBox combo box which tells how to zoom the map image
	 */
	public MapCanvas( final RepProcessor repProc, final XComboBox< Zoom > zoomComboBox ) {
		this.repProc = repProc;
		this.zoomComboBox = zoomComboBox;
		
		ricon = MapImageCache.getMapImage( repProc );
		
		GuiUtils.makeComponentDragScrollable( this );
		
		// Zoom in and out with CTRL+wheel scroll:
		addMouseWheelListener( new MouseWheelListener() {
			@Override
			public void mouseWheelMoved( final MouseWheelEvent event ) {
				if ( event.isControlDown() ) {
					final int newZoomIdx = zoomComboBox.getSelectedIndex() - event.getWheelRotation();
					zoomComboBox.setSelectedIndex( Math.max( 0, Math.min( zoomComboBox.getItemCount() - 1, newZoomIdx ) ) );
					// An event will be fired which will cause reconfigureZoom() to be called...
				}
			}
		} );
	}
	
	/**
	 * Sets whether to show player names.
	 * 
	 * @param showPlayerNames true if player names is to be displayed; false otherwise
	 */
	public void setShowPlayerNames( final boolean showPlayerNames ) {
		this.showPlayerNames = showPlayerNames;
	}
	
	/**
	 * Reconfigures the map image zoom.
	 */
	public void reconfigureZoom() {
		if ( ricon == null )
			return;
			
		final int iconWidth = ricon.get().getIconWidth();
		final int iconHeight = ricon.get().getIconHeight();
		
		final Zoom zoom = zoomComboBox.getSelectedItem();
		
		int height = 0; // Height to zoom to
		if ( zoom == Zoom.FIT_TO_WINDOW ) {
			if ( iconWidth > 0 || iconHeight > 0 ) {
				final float xScale = (float) getParent().getWidth() / iconWidth;
				final float yScale = (float) getParent().getHeight() / iconHeight;
				
				// Go by the smaller scale (so the image will fit both horizontally and vertically)
				if ( xScale > yScale )
					height = getParent().getHeight();
				else
					height = (int) ( iconHeight * xScale );
			}
		} else if ( zoom == Zoom.HALF )
			height = iconHeight / 2;
		else
			height = iconHeight * zoom.factor;
			
		icon = ricon.size( height );
		
		setPreferredSize( new Dimension( icon.getIconWidth(), icon.getIconHeight() ) );
		revalidate();
		repaint();
	}
	
	@Override
	protected void paintComponent( final Graphics g_ ) {
		final int width = getWidth();
		final int height = getHeight();
		if ( width <= 0 || height <= 0 )
			return;
			
		if ( icon == null ) {
			// Indicate map downloading:
			MAP_DL_ICON.paintIcon( this, g_, ( width - MAP_DL_ICON.getIconWidth() ) / 2, ( height - MAP_DL_ICON.getIconHeight() ) / 2 );
			return;
		}
		
		// Position to center
		final int x = ( width - icon.getIconWidth() ) / 2;
		final int y = ( height - icon.getIconHeight() ) / 2;
		
		icon.paintIcon( this, g_, x, y );
		
		// Map objects (including start locations)
		if ( repProc.replay.trackerEvents == null )
			return;
			
		final MapInfo mapInfo = repProc.getMapInfo();
		if ( mapInfo == null )
			return;
			
		final MapObjects mapObjects = repProc.getMapObjects();
		if ( mapObjects == null )
			return;
			
		final BalanceData balanceData = repProc.replay.getBalanceData();
		if ( balanceData == null )
			return;
			
		final int mapPlayableWidth = mapInfo.getPlayableWidth();
		final int mapPlayableHeight = mapInfo.getPlayableHeight();
		
		// Usually the preview image is scaled 1:1 meaning a map distance R is equal to R pixels in the image.
		// Sometimes the preview image is scaled 2:1 meaning a map distance R is equal to 2*R pixels in the image.
		// Map preview image width and height in map coordinates:
		int iconCoordinateWidth = ricon.get().getIconWidth();
		int iconCoordinateHeight = ricon.get().getIconHeight();
		if ( iconCoordinateWidth >= mapPlayableWidth * 2 ) {
			iconCoordinateWidth /= 2;
			iconCoordinateHeight /= 2;
		}
		
		// Sometimes the playable width (and height) is not equal to the map preview width (and height).
		// Share the difference on both sides equally:
		// final double xDiff = ( mapPlayableWidth - ricon.get().getIconWidth() ) / 2.0;
		// final double yDiff = ( mapPlayableHeight - ricon.get().getIconHeight() ) / 2.0;
		final double xDiff = ( mapPlayableWidth - iconCoordinateWidth ) / 2.0;
		final double yDiff = ( mapPlayableHeight - iconCoordinateHeight ) / 2.0;
		
		// Calculate map zero coordinate (x, y holds only the zero point of the playable area)
		// x => mapInfo.boundaryLeft
		// y => mapInfo.boundaryTop
		// x2 => mapInfo.boundaryRight
		// y2 => mapInfo.boundaryBottom
		final double scale = (double) icon.getIconHeight() / mapPlayableHeight;
		final double zx = x - ( mapInfo.boundaryLeft + xDiff ) * scale;
		final double zy = y + icon.getIconHeight() + ( mapInfo.boundaryBottom - yDiff ) * scale;
		
		final Graphics2D g = (Graphics2D) g_;
		g.translate( zx, zy );
		g.scale( scale, scale );
		
		Rectangle2D.Float rect = new Rectangle2D.Float();
		for ( final ObjectUnit u : mapObjects.unitList ) {
			Color c = UNIT_COLOR_MAP.get( u.unitType );
			final boolean destructible = BdUtil.isDestructibleImpl( u.unitType );
			if ( c == null && destructible )
				c = COLOR_DESTRUCTIBLE;
				
			if ( c != null ) {
				g.setColor( c );
				final float radius = destructible ? 2 : balanceData.getUnit( u.unitType ).radius;
				rect.x = u.pos.x - radius;
				rect.y = -u.pos.y - radius;
				rect.height = rect.width = 2 * radius;
				g.fill( rect );
			}
		}
		
		// Indicate potential start locations:
		g.setRenderingHint( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );
		
		Ellipse2D.Float ell = new Ellipse2D.Float();
		Line2D.Float line = new Line2D.Float();
		for ( final Point2D.Float startLoc : mapObjects.startLocationList ) {
			g.setColor( Color.BLACK );
			ell.x = startLoc.x - 1f;
			ell.y = -startLoc.y - 1f;
			ell.height = ell.width = 2;
			g.draw( ell );
			g.setColor( Color.WHITE );
			line.x1 = (float) ell.getMinX();
			line.y1 = (float) ell.getMinY();
			line.x2 = (float) ell.getMaxX();
			line.y2 = (float) ell.getMaxY();
			g.draw( line );
			line.y1 = line.y2;
			line.y2 = (float) ell.getMinY();
			g.draw( line );
		}
		
		// Indicate players current start locations:
		final Font oldFont = g.getFont();
		g.setFont( oldFont.deriveFont( Font.BOLD, Env.APP_SETTINGS.get( Settings.MAP_INFO_CANVAS_FONT_SIZE ) ) );
		
		final float radius = balanceData.getUnit( BdUtil.UNIT_NEXUS ).radius; // Size of main buildings
		for ( final User u : repProc.playerUsers ) {
			if ( u.startLocation == null )
				continue;
				
			// Set user's color
			g.setColor( u.getPlayerColor().color );
			rect.x = u.startLocation.x - radius;
			rect.y = -u.startLocation.y - radius;
			rect.height = rect.width = radius * 2;
			g.fill( rect );
			
			if ( showPlayerNames ) {
				// User name (align it to fit on the map image, because if map image is zoomed, only the image is
				// visible/scrollable and user name overflowing would not be displayed / cut off)
				final String displayText;
				if ( repProc.isArchon() ) {
					// This simplest solution will paint both team names twice, but at the same location, so does no harm.
					final Integer team = u.slot.teamId;
					StringBuilder sb = null;
					for ( final User u2 : repProc.playerUsers ) {
						if ( u2.slot.teamId != team )
							continue;
						if ( sb == null )
							sb = new StringBuilder( "TEAM " ).append( team + 1 ).append( " <" );
						else
							sb.append( ", " );
						sb.append( u2.fullName );
					}
					displayText = sb.append( '>' ).toString();
				} else
					displayText = u.fullName;
					
				final int textHalfWidth = g.getFontMetrics().stringWidth( displayText ) / 2;
				final float textx = Math.max( (float) ( ( x - zx ) / scale ),
				        Math.min( (float) ( ( x + icon.getIconWidth() - zx ) / scale - 2 * textHalfWidth - 2f ), (float) rect.getCenterX() - textHalfWidth ) );
				final float texty = (float) rect.getMaxY() + g.getFontMetrics().getAscent();
				
				g.setColor( Color.BLACK );
				g.drawString( displayText, textx + 0.5f, texty + 0.5f );
				g.setColor( Color.WHITE );
				g.setColor( u.getPlayerColor().brighterColor );
				g.drawString( displayText, textx, texty );
			}
		}
		g.setFont( oldFont );
	}
	
}
