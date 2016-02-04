/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight.gui.dialog;

import hu.scelight.gui.comp.TemplateEditorComp;
import hu.scelight.gui.comp.TemplateField;
import hu.scelight.gui.icon.Icons;
import hu.scelight.service.env.Env;
import hu.scelight.template.SymbolScope;
import hu.scelight.util.sc2rep.LatestRepSearchCoordinatorJob;
import hu.sllauncher.gui.comp.XButton;
import hu.sllauncher.gui.comp.XDialog;
import hu.sllauncher.util.gui.adapter.ActionAdapter;

import java.awt.Dimension;
import java.awt.event.ActionEvent;

/**
 * A name template editor dialog, for templates used automatically (e.g. rename new reps, backup new reps).
 * 
 * @author Andras Belicza
 */
@SuppressWarnings( "serial" )
public class TemplateEditorDialog extends XDialog {
	
	/** Template editor component. */
	private final TemplateEditorComp templateEditor = new TemplateEditorComp( SymbolScope.MANUAL_RENAME );
	
	/** Target template field to store the edited name template. */
	private final TemplateField      targetTemplateField;
	
	/**
	 * Creates a new {@link TemplateEditorDialog}.
	 * 
	 * @param title title of the dialog
	 * @param targetTemplateField target template field to store the edited name template
	 */
	public TemplateEditorDialog( final String title, final TemplateField targetTemplateField ) {
		super( Env.MAIN_FRAME );
		
		this.targetTemplateField = targetTemplateField;
		templateEditor.templateField.setTemplate( targetTemplateField.getTemplate() );
		
		setTitle( title );
		setIconImage( Icons.F_BLUE_DOCUMENT_RENAME.get().getImage() );
		
		buildGui();
		
		// Set the latest replay as the test replay
		final LatestRepSearchCoordinatorJob lj = new LatestRepSearchCoordinatorJob();
		lj.setEdtCallback( new Runnable() {
			@Override
			public void run() {
				// If a replay was selected manually during the search, leave it unchanged
				if ( lj.getLatestReplay() != null && !templateEditor.testRepPathField.hasPath() )
					templateEditor.testRepPathField.setPath( lj.getLatestReplay() );
			}
		} );
		lj.start();
		
		defaultSize = new Dimension( DEFAULT_DEFAULT_SIZE.width - 50, DEFAULT_DEFAULT_SIZE.height - 50 );
		restoreDefaultSize();
		
		setVisible( true );
	}
	
	/**
	 * Builds the GUI of the dialog.
	 */
	private void buildGui() {
		cp.addCenter( templateEditor );
		
		final XButton okButton = new XButton( "_OK" );
		okButton.addActionListener( new ActionAdapter() {
			@Override
			public void actionPerformed( final ActionEvent event ) {
				targetTemplateField.setTemplate( templateEditor.templateField.getTemplate() );
				close();
			}
		} );
		getButtonsPanel().add( okButton );
		getRootPane().setDefaultButton( okButton );
		addCloseButton( "_Cancel" );
	}
	
}
