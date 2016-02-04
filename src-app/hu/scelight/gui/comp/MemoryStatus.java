/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight.gui.comp;

import hu.scelight.util.RHtml;
import hu.scelight.util.Utils;
import hu.scelight.util.gui.GuiUtils;
import hu.sllauncher.util.SizeFormat;
import hu.sllauncher.util.gui.adapter.ActionAdapter;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.Timer;

/**
 * A memory status component.
 * 
 * @author Andras Belicza
 */
@SuppressWarnings( "serial" )
public class MemoryStatus extends JComponent {
	
	/** Width of the component. */
	private static final int   WIDTH             = 180;
	
	/** Color of the unallocated memory bar. */
	private static final Color COLOR_UNALLOCATED = new Color( 22, 128, 0 );
	
	/** Color of the allocated free memory bar. */
	private static final Color COLOR_FREE        = new Color( 254, 138, 14 );
	
	/** Color of the allocated in-use memory bar. */
	private static final Color COLOR_IN_USE      = new Color( 180, 20, 30 );
	
	
	/** Allocated memory (Bytes). */
	private long               allocated;
	
	/** Free memory (Bytes). */
	private long               free;
	
	/** Max memory (Bytes). */
	private long               max;
	
	/**
	 * Creates a new {@link MemoryStatus}.
	 */
	public MemoryStatus() {
		setCursor( Cursor.getPredefinedCursor( Cursor.HAND_CURSOR ) );
		
		addMouseListener( new MouseAdapter() {
			@Override
			public void mousePressed( final MouseEvent event ) {
				GuiUtils.showInfoMsg( new RHtml( "Memory Usage", "html/statusbar/memory-usage.html", "maxAvailable", SizeFormat.MB.formatSize( max, 1 ),
				        "allocatedTotal", SizeFormat.MB.formatSize( allocated, 1 ), "allocatedInUse", SizeFormat.MB.formatSize( allocated - free, 1 ),
				        "allocatedFree", SizeFormat.MB.formatSize( free, 1 ), "unallocated", SizeFormat.MB.formatSize( max - allocated, 1 ), "totalFree",
				        SizeFormat.MB.formatSize( free + max - allocated, 1 ), "colorUnallocated", Utils.toCss( COLOR_UNALLOCATED ), "colorInUse", Utils
				                .toCss( COLOR_IN_USE ), "colorFree", Utils.toCss( COLOR_FREE ) ).get() );
			};
		} );
		
		setBorder( BorderFactory.createEmptyBorder( 2, 2, 2, 2 ) );
		
		final Timer t = new Timer( 1500, new ActionAdapter() {
			@Override
			public void actionPerformed( final ActionEvent event ) {
				// We're in the EDT (swing timer)
				paintImmediately( 0, 0, getWidth(), getHeight() );
			}
		} );
		t.setInitialDelay( 0 );
		t.start();
	}
	
	@Override
	public Dimension getPreferredSize() {
		return new Dimension( WIDTH, 24 );
	}
	
	@Override
	public Dimension getMaximumSize() {
		return new Dimension( WIDTH, Integer.MAX_VALUE );
	}
	
	@Override
	public Dimension getMinimumSize() {
		return new Dimension( WIDTH, 16 );
	}
	
	@Override
	protected void paintComponent( final Graphics g ) {
		( (Graphics2D) g ).setRenderingHint( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );
		
		final Runtime runtime = Runtime.getRuntime();
		allocated = runtime.totalMemory();
		free = runtime.freeMemory();
		max = runtime.maxMemory();
		
		// Exclude border
		final int width = getWidth() - 4;
		final int height = getHeight() - 4;
		if ( width <= 0 || height <= 0 )
			return;
		
		g.translate( 2, 2 );
		g.setColor( COLOR_UNALLOCATED );
		g.fillRect( 0, 0, width, height );
		g.setColor( COLOR_FREE );
		g.fillRect( 0, 1, (int) ( allocated * width / max ), height - 2 );
		g.setColor( COLOR_IN_USE );
		g.fillRect( 0, 1, (int) ( ( allocated - free ) * width / max ), height - 2 );
		
		// Display used / allocated
		g.setFont( g.getFont().deriveFont( Font.BOLD ) );
		final FontMetrics fontMetrics = g.getFontMetrics();
		final String memString = SizeFormat.MB.formatSize( allocated - free, 0 ) + " / " + SizeFormat.MB.formatSize( allocated, 0 );
		
		final int x = width > fontMetrics.stringWidth( memString ) ? ( width - fontMetrics.stringWidth( memString ) ) / 2 : 0;
		final int y = ( height + fontMetrics.getAscent() ) / 2 - 1;
		g.setColor( Color.BLACK );
		g.drawString( memString, x + 1, y + 1 );
		g.setColor( Color.WHITE );
		g.drawString( memString, x, y );
		
		g.translate( -2, -2 );
	}
	
}
