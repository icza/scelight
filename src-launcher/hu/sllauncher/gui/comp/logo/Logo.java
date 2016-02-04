/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.sllauncher.gui.comp.logo;

import hu.sllauncher.gui.comp.XLabel;
import hu.sllauncher.gui.icon.LIcons;
import hu.sllauncher.util.gui.LGuiUtils;
import hu.sllauncher.util.gui.adapter.ActionAdapter;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.RadialGradientPaint;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Timer;

/**
 * A fancy, animated application logo component.
 * 
 * @author Andras Belicza
 */
@SuppressWarnings( "serial" )
public class Logo extends XLabel {
	
	/** Colors used in the decoration (gradient paint)> */
	private static final Color[] COLORS              = { new Color( 255, 255, 245 ), new Color( 255, 255, 245 ), new Color( 255, 255, 220, 208 ),
	        new Color( 255, 204, 144, 0 )           };
	
	/** Color distribution in the gradient paint. */
	private static final float[] COL_DIST            = { 0.0f, 0.4f, 0.6f, 1.0f };
	
	/** Color of the stars. */
	private static final Color   COLOR_STAR          = Color.WHITE;
	
	/** Resting place of the Sun. */
	private final Point2D.Double SUN_RESTING_PLACE   = new Point2D.Double( 30, 0 );
	
	/** Default number of stars. */
	private final int            DEFAULT_STARS_COUNT = 200;
	
	/** Default gravity. */
	private final int            DEFAULT_GRAVITY     = 200;
	
	
	
	/** Timer for the logo animation. */
	private final Timer          timer               = new Timer( 30, null );                     // 33 FPS
	                                                                                               
	/** Logo image index. */
	private int                  imageIdx            = LIcons.MY_APP_LOGOS.length - 1;
	
	/** Tells if mouse is over. */
	private boolean              mouseOver;
	
	/** State of the animation. */
	private State                state               = State.TEXT_FRONT;
	
	/** Tells if Sun should follow mouse cursor (else fixed position). */
	private boolean              followMouse;
	
	/** Current mouse coordinates. */
	private final Point2D.Double mp                  = new Point2D.Double();
	
	/** The Sun. */
	private final Star           sun                 = new Star( SUN_RESTING_PLACE );
	
	/** List of stars. */
	private final List< Star >   starList            = new ArrayList<>( DEFAULT_STARS_COUNT * 2 );
	
	/** Gravity. */
	private int                  gravity             = DEFAULT_GRAVITY;
	
	
	/**
	 * Creates a new {@link Logo}.
	 */
	public Logo() {
		super( LIcons.MY_APP_LOGOS[ LIcons.MY_APP_LOGOS.length - 1 ].size( 116, false, false ) );
		
		verticalBorder( 15 );
		
		// Set small preferred width to avoid horizontal scrollbar if smaller space is available.
		setPreferredSize( new Dimension( 10, getPreferredSize().height ) );
		
		for ( int i = 0; i < DEFAULT_STARS_COUNT; i++ )
			starList.add( new Star() );
		
		final MouseAdapter ma = new MouseAdapter() {
			@Override
			public void mouseEntered( final MouseEvent event ) {
				mouseOver = true;
			}
			
			@Override
			public void mouseExited( final MouseEvent event ) {
				mouseOver = false;
			}
			
			@Override
			public void mousePressed( final MouseEvent event ) {
				switch ( event.getButton() ) {
					case LGuiUtils.MOUSE_BTN_LEFT :
						// Switch logo state
						state = state.next();
						repaint();
						break;
					case LGuiUtils.MOUSE_BTN_RIGHT :
						// Switch logo image
						if ( --imageIdx < 0 )
							imageIdx = LIcons.MY_APP_LOGOS.length - 1;
						setIcon( LIcons.MY_APP_LOGOS[ imageIdx ].size( 116, false, false ) );
						break;
					case LGuiUtils.MOUSE_BTN_MIDDLE :
						// Toggle mouse cursor following
						followMouse = !followMouse;
						break;
				}
			}
			
			@Override
			public void mouseMoved( final MouseEvent event ) {
				// Center based!
				mp.x = event.getX() - getWidth() / 2;
				mp.y = event.getY() - getHeight() / 2;
			}
			
			@Override
			public void mouseWheelMoved( final MouseWheelEvent event ) {
				// Rotating up returns negative
				for ( int change = event.getWheelRotation(), dir = change < 0 ? 1 : -1; change != 0; change += dir )
					if ( event.isControlDown() ) {
						// Change gravity
						final int delta = gravity + dir <= 10 ? 1 : gravity + dir <= 100 ? 10 : gravity + dir <= 500 ? 50 : 100;
						gravity += dir * delta;
						gravity = Math.max( 1, Math.min( 1000, gravity ) ); // Normalize
					} else {
						// Change number of stars
						int count = starList.size();
						final int delta = count + dir <= 100 ? 10 : count + dir <= 200 ? 20 : count + dir <= 500 ? 50 : count + dir <= 1000 ? 100 : 200;
						count += delta * dir;
						count = Math.max( 10, Math.min( 5000, count ) ); // Normalize
						
						while ( count < starList.size() )
							starList.remove( starList.size() - 1 );
						while ( count > starList.size() )
							starList.add( new Star() );
					}
			}
		};
		addMouseListener( ma );
		addMouseMotionListener( ma );
		addMouseWheelListener( ma );
		
		timer.addActionListener( new ActionAdapter() {
			@Override
			public void actionPerformed( final ActionEvent event ) {
				// We're in the EDT (because using Swing Timer)
				
				if ( getWidth() <= 0 || getHeight() <= 0 ) // Do nothing until we're visible
					return;
				
				final double width = getWidth();
				final double height = getHeight();
				final double halfW = width / 2;
				final double halfH = height / 2;
				
				// Bounds
				final double bx2 = width * 3 / 4;
				final double by2 = height;
				final double bx1 = -bx2;
				final double by1 = -by2;
				
				final double gravity = Logo.this.gravity / 5000.0; // Local final copy for fast access.
				
				for ( final Star s : starList ) {
					if ( s.x > bx2 || s.x < bx1 || s.y > by2 || s.y < by1 ) {
						// Initialize star
						s.x = Math.random() * width - halfW;
						s.y = Math.random() * height - halfH;
						s.r = 1.0 + Math.random() * 1.5;
						s.r = 1.0 + Math.random() * 1.5;
						s.vx = 0.2 + Math.random() * 2;
						if ( Math.random() < 0.5 )
							s.vx = -s.vx;
						s.vy = 0;
						s.color = new Color( (float) Math.random(), (float) Math.random(), (float) Math.random() );
					}
					// Sun attracts stars. I simulate constant gravitational force,
					// resulting in an effect that stars are on an elliptical orbit.
					// I use no trigonometric functions: I use similar, proportional triangles.
					final double dx = sun.x - s.x;
					final double dy = sun.y - s.y;
					final double d = Math.sqrt( dx * dx + dy * dy );
					s.vx += gravity * dx / d;
					s.vy += gravity * dy / d;
					s.move();
				}
				
				if ( followMouse ) {
					final double SUN_SPEED = 40.0;
					final Point2D.Double target = mouseOver ? mp : SUN_RESTING_PLACE;
					final double dx = target.x - sun.x;
					final double dy = target.y - sun.y;
					final double d = Math.sqrt( dx * dx + dy * dy );
					if ( d < SUN_SPEED * 1.1 ) {
						// Sun's too close to the destination, just "jump" to it.
						sun.setLocation( target );
					} else {
						// Calculate speed toward the target and make a move.
						sun.vx = SUN_SPEED * dx / d;
						sun.vy = SUN_SPEED * dy / d;
						sun.move();
					}
				}
				
				repaint();
			}
		} );
		timer.start();
	}
	
	@Override
	public void paint( final Graphics g_ ) {
		final int halfW = getWidth() / 2;
		final int halfH = getHeight() / 2;
		if ( halfW <= 0 || halfH <= 0 ) // Do nothing until we're visible
			return;
		
		final Graphics2D g = (Graphics2D) g_;
		
		if ( state == State.ANIM_FRONT || state == State.TEXT_ONLY )
			super.paint( g );
		if ( state == State.TEXT_ONLY )
			return;
		
		if ( state == State.ANIM_ONLY ) {
			g.setColor( Color.BLACK );
			g.fillRect( 0, 0, getWidth(), getHeight() );
		}
		
		// Coordinates are component center based (so if frame is resized, everything still remains in the center)!
		g.translate( halfW, halfH );
		g.setColor( COLOR_STAR );
		final Ellipse2D.Double ell = new Ellipse2D.Double();
		
		// Paint stars in 2 steps.
		// First ones that go behind the Sun (vx < 0)
		g.setRenderingHint( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );
		for ( final Star s : starList ) {
			if ( s.vx >= 0 )
				continue;
			ell.setFrame( s.x - s.r, s.y - s.r, s.r * 2, s.r * 2 );
			if ( state == State.ANIM_ONLY )
				g.setColor( s.color );
			g.fill( ell );
		}
		g.setRenderingHint( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF );
		
		// Paint the Sun
		// Create a radial gradient, opaque in the middle in the middle, transparent at the edges.
		final Paint storedPaint = g.getPaint();
		final int radius = halfH;
		final RadialGradientPaint p = new RadialGradientPaint( new Point2D.Double( sun.x, sun.y ), radius, COL_DIST, COLORS );
		g.setPaint( p );
		ell.setFrame( sun.x - radius, sun.y - radius, radius * 2, radius * 2 );
		g.fill( ell );
		g.setPaint( storedPaint );
		
		// Stars 2nd step: the ones that go in front of the Sun (vx >= 0)
		g.setRenderingHint( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );
		for ( final Star s : starList ) {
			if ( s.vx < 0 )
				continue;
			ell.setFrame( s.x - s.r, s.y - s.r, s.r * 2, s.r * 2 );
			if ( state == State.ANIM_ONLY )
				g.setColor( s.color );
			g.fill( ell );
		}
		g.setRenderingHint( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF );
		
		// Translate back to the original zero point.
		g.translate( -halfW, -halfH );
		
		if ( state == State.TEXT_FRONT )
			super.paint( g );
		
		// Paint other texts (properties)
		g.setRenderingHint( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );
		g.setColor( state == State.ANIM_ONLY ? Color.LIGHT_GRAY : Color.DARK_GRAY );
		if ( gravity != DEFAULT_GRAVITY ) {
			final String text = "g=" + gravity;
			g.drawString( text, getWidth() - g.getFontMetrics().stringWidth( text ) - 3, getHeight() - 19 );
		}
		if ( starList.size() != DEFAULT_STARS_COUNT ) {
			final String text = "n=" + starList.size();
			g.drawString( text, getWidth() - g.getFontMetrics().stringWidth( text ) - 3, getHeight() - 3 );
		}
	}
	
	/**
	 * Stops the logo.
	 */
	public void stop() {
		timer.stop();
	}
	
}
