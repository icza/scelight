/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.sltool.release;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.RadialGradientPaint;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.nio.file.Paths;

import javax.imageio.ImageIO;

/**
 * Application which creates and writes the the application icon.
 * 
 * <p>
 * Use <a href="http://convertico.org/">This online icon converter</a> to convert the result PNG to exe icon and favicon.
 * </p>
 * 
 * @author Andras Belicza
 */
public class CreateAppIcon {
	
	/** Colors used in the decoration (gradient paint)> */
	private static final Color[] COLORS   = { new Color( 255, 255, 245 ), new Color( 255, 255, 245 ), new Color( 255, 255, 220, 208 ),
	        new Color( 255, 204, 144, 0 ) };
	
	/** Color distribution in the gradient paint. */
	private static final float[] COL_DIST = { 0.0f, 0.4f, 0.6f, 1.0f };
	
	
	
	/**
	 * @param args used to take arguments from the running environment - not used here
	 * @throws Exception if any error occurs
	 */
	public static void main( final String[] args ) throws Exception {
		final int width = 64;
		final int height = 64;
		final int cx = width / 2;
		final int cy = height / 2;
		
		final BufferedImage bi = new BufferedImage( width, height, BufferedImage.TYPE_INT_ARGB );
		
		final Graphics2D g = bi.createGraphics();
		g.setRenderingHint( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );
		
		// Background if needed:
		// g.setColor( Color.LIGHT_GRAY );
		// g.fillRect( 0, 0, width, height );
		
		g.setColor( new Color( 255, 127, 39 ) ); // Logo color: orange
		g.setFont( new Font( "Arial Unicode MS", Font.BOLD | Font.ITALIC, 55 ) );
		final FontMetrics fm = g.getFontMetrics();
		
		// Background light / sun
		final Paint storedPaint = g.getPaint();
		final int radius = height / 2;
		final RadialGradientPaint p = new RadialGradientPaint( new Point2D.Double( cx, cy ), radius, COL_DIST, COLORS );
		g.setPaint( p );
		final Ellipse2D.Double ell = new Ellipse2D.Double();
		ell.setFrame( cx - radius, cy - radius, radius << 1, radius << 1 );
		g.fill( ell );
		g.setPaint( storedPaint );
		
		// Text
		final int y = ( height + fm.getAscent() - fm.getDescent() ) / 2;
		final String text = "SL";
		g.drawString( text, ( width - fm.stringWidth( text ) ) / 2 - 2, y - 2 );
		
		// We're good, save the icon.
		ImageIO.write( bi, "png", Paths.get( "w:/app-icon.png" ).toFile() );
	}
	
}
