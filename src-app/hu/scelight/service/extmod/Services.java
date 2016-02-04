/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight.service.extmod;

import hu.belicza.andras.util.VersionView;
import hu.scelight.bean.repfilters.RepFilterBean;
import hu.scelight.bean.repfilters.RepFiltersBean;
import hu.scelight.bean.settings.type.TemplateSetting;
import hu.scelight.bean.settings.type.ValidatedMultilineStringSetting;
import hu.scelight.gui.MainFrame;
import hu.scelight.gui.comp.IndicatorTextArea;
import hu.scelight.gui.comp.TemplateField;
import hu.scelight.gui.comp.ToolBarForList;
import hu.scelight.gui.comp.ToolBarForTree;
import hu.scelight.gui.comp.XCheckBoxMenuItem;
import hu.scelight.gui.comp.XList;
import hu.scelight.gui.comp.XMenu;
import hu.scelight.gui.comp.XMenuItem;
import hu.scelight.gui.comp.XPasswordField;
import hu.scelight.gui.comp.XPopupMenu;
import hu.scelight.gui.comp.XTabbedPane;
import hu.scelight.gui.dialog.RepFiltersEditorDialog;
import hu.scelight.gui.dialog.SendEmailDialog;
import hu.scelight.gui.overlaycard.OverlayCard;
import hu.scelight.gui.setting.SettingsGui;
import hu.scelight.sc2.balancedata.BdUtil;
import hu.scelight.sc2.rep.cache.RepProcCache;
import hu.scelight.sc2.rep.factory.RepParserEngine;
import hu.scelight.sc2.rep.model.Replay;
import hu.scelight.sc2.rep.model.details.Toon;
import hu.scelight.sc2.rep.repproc.RepProcessor;
import hu.scelight.sc2.rep.repproc.SelectionTracker;
import hu.scelight.search.RepSearchEngine;
import hu.scelight.service.env.Env;
import hu.scelight.service.sc2reg.Sc2RegMonitor;
import hu.scelight.service.watcher.NewReplayHandlerJob;
import hu.scelight.template.InvalidTemplateException;
import hu.scelight.template.TemplateEngine;
import hu.scelight.util.Utils;
import hu.scelight.util.gui.GuiUtils;
import hu.scelight.util.httppost.HttpPost;
import hu.scelight.util.httppost.SimpleFileProvider;
import hu.scelightapi.IServices;
import hu.scelightapi.bean.repfilters.IRepFiltersBean;
import hu.scelightapi.bean.settings.type.ITemplateSetting;
import hu.scelightapi.bean.settings.type.IValidatedMultilineStringSetting;
import hu.scelightapi.gui.comp.IIndicatorTextArea;
import hu.scelightapi.gui.comp.IList;
import hu.scelightapi.gui.comp.IPopupMenu;
import hu.scelightapi.gui.comp.IToolBarForList;
import hu.scelightapi.gui.overlaycard.IConfigPopupBuilder;
import hu.scelightapi.gui.overlaycard.OverlayCardParams;
import hu.scelightapi.gui.setting.ISettingsGui;
import hu.scelightapi.gui.setting.ISettingsUtils;
import hu.scelightapi.sc2.rep.factory.IRepParserEngine;
import hu.scelightapi.service.IFactory;
import hu.scelightapi.service.IGuiFactory;
import hu.scelightapi.service.repfoldermonitor.INewRepListener;
import hu.scelightapi.service.repfoldermonitor.IRepFolderMonitor;
import hu.scelightapi.service.sc2monitor.IGameChangeListener;
import hu.scelightapi.service.sc2monitor.IGameStatus;
import hu.scelightapi.service.sc2monitor.ISc2Monitor;
import hu.scelightapi.service.sound.ISound;
import hu.scelightapi.util.IUtils;
import hu.scelightapi.util.gui.IGuiUtils;
import hu.scelightapibase.action.IAction;
import hu.scelightapibase.bean.IExtModManifestBean;
import hu.scelightapibase.bean.settings.ISettingChangeListener;
import hu.scelightapibase.bean.settings.ISettingsBean;
import hu.scelightapibase.bean.settings.type.IBoolSetting;
import hu.scelightapibase.bean.settings.type.IFixedValuesSetting;
import hu.scelightapibase.bean.settings.type.IIntSetting;
import hu.scelightapibase.bean.settings.type.IMultilineStringSetting;
import hu.scelightapibase.bean.settings.type.INodeSetting;
import hu.scelightapibase.bean.settings.type.IPathSetting;
import hu.scelightapibase.bean.settings.type.ISetting;
import hu.scelightapibase.bean.settings.type.ISettingsGroup;
import hu.scelightapibase.bean.settings.type.IStringSetting;
import hu.scelightapibase.bean.settings.type.IValidatedStringSetting;
import hu.scelightapibase.bean.settings.type.viewhints.ICompConfigurer;
import hu.scelightapibase.bean.settings.type.viewhints.ISsCompFactory;
import hu.scelightapibase.bean.settings.type.viewhints.ITestBtnListener;
import hu.scelightapibase.bean.settings.type.viewhints.IViewHints;
import hu.scelightapibase.gui.comp.IBrowser;
import hu.scelightapibase.gui.comp.IButton;
import hu.scelightapibase.gui.comp.IIndicatorTextField;
import hu.scelightapibase.gui.comp.ILabel;
import hu.scelightapibase.gui.comp.IStatusLabel.IStatusType;
import hu.scelightapibase.gui.comp.ITree;
import hu.scelightapibase.gui.comp.multipage.IMultiPageComp;
import hu.scelightapibase.gui.comp.multipage.IPage;
import hu.scelightapibase.gui.comp.multipage.IPageCompCreator;
import hu.scelightapibase.gui.comp.table.ITable;
import hu.scelightapibase.gui.icon.IRIcon;
import hu.scelightapibase.util.IDateFormat;
import hu.scelightapibase.util.IDurationFormat;
import hu.scelightapibase.util.IRHtml;
import hu.scelightapibase.util.ISizeFormat;
import hu.scelightapibase.util.ISkillLevel;
import hu.scelightapibase.util.iface.Consumer;
import hu.sllauncher.action.XAction;
import hu.sllauncher.bean.VersionBean;
import hu.sllauncher.bean.settings.type.BoolSetting;
import hu.sllauncher.bean.settings.type.EnumSetting;
import hu.sllauncher.bean.settings.type.FixedEnumValuesSetting;
import hu.sllauncher.bean.settings.type.FixedIntValuesSetting;
import hu.sllauncher.bean.settings.type.IntSetting;
import hu.sllauncher.bean.settings.type.MultilineStringSetting;
import hu.sllauncher.bean.settings.type.NodeSetting;
import hu.sllauncher.bean.settings.type.PathSetting;
import hu.sllauncher.bean.settings.type.SettingsGroup;
import hu.sllauncher.bean.settings.type.StringSetting;
import hu.sllauncher.bean.settings.type.ValidatedStringSetting;
import hu.sllauncher.bean.settings.type.viewhints.HostCheckTestBtnFactory;
import hu.sllauncher.bean.settings.type.viewhints.TestBtnFactory;
import hu.sllauncher.bean.settings.type.viewhints.VHB;
import hu.sllauncher.bean.settings.type.viewhints.ViewHints;
import hu.sllauncher.gui.comp.BorderPanel;
import hu.sllauncher.gui.comp.Browser;
import hu.sllauncher.gui.comp.ColorIcon;
import hu.sllauncher.gui.comp.GridBagPanel;
import hu.sllauncher.gui.comp.HelpIcon;
import hu.sllauncher.gui.comp.IndicatorTextField;
import hu.sllauncher.gui.comp.Link;
import hu.sllauncher.gui.comp.ModestLabel;
import hu.sllauncher.gui.comp.PathField;
import hu.sllauncher.gui.comp.StatusLabel;
import hu.sllauncher.gui.comp.TipIcon;
import hu.sllauncher.gui.comp.ToolBarForTable;
import hu.sllauncher.gui.comp.XButton;
import hu.sllauncher.gui.comp.XCheckBox;
import hu.sllauncher.gui.comp.XDialog;
import hu.sllauncher.gui.comp.XFileChooser;
import hu.sllauncher.gui.comp.XLabel;
import hu.sllauncher.gui.comp.XProgressBar;
import hu.sllauncher.gui.comp.XScrollPane;
import hu.sllauncher.gui.comp.XSpinner;
import hu.sllauncher.gui.comp.XSplitPane;
import hu.sllauncher.gui.comp.XTextArea;
import hu.sllauncher.gui.comp.XTextField;
import hu.sllauncher.gui.comp.XToolBar;
import hu.sllauncher.gui.comp.XTree;
import hu.sllauncher.gui.comp.combobox.XComboBox;
import hu.sllauncher.gui.comp.multipage.BasePage;
import hu.sllauncher.gui.comp.multipage.BrowserPage;
import hu.sllauncher.gui.comp.multipage.MultiPageComp;
import hu.sllauncher.gui.comp.table.XTable;
import hu.sllauncher.gui.comp.table.renderer.BarCodeView;
import hu.sllauncher.gui.comp.table.renderer.ProgressBarView;
import hu.sllauncher.gui.icon.LRIcon;
import hu.sllauncher.service.env.OpSys;
import hu.sllauncher.service.job.Job;
import hu.sllauncher.service.job.ProgressJob;
import hu.sllauncher.service.lang.Language;
import hu.sllauncher.service.log.Logger;
import hu.sllauncher.service.sound.Sound;
import hu.sllauncher.util.ControlledThread;
import hu.sllauncher.util.DateValue;
import hu.sllauncher.util.DurationValue;
import hu.sllauncher.util.Holder;
import hu.sllauncher.util.LRHtml;
import hu.sllauncher.util.NormalThread;
import hu.sllauncher.util.Pair;
import hu.sllauncher.util.RelativeDate;
import hu.sllauncher.util.SizeValue;
import hu.sllauncher.util.SkillLevel;
import hu.sllauncher.util.UrlBuilder;
import hu.sllauncher.util.gui.LGuiUtils;
import hu.sllauncher.util.gui.RenderablePair;
import hu.sllauncher.util.gui.ToLabelRenderer;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.ExecutorService;

import javax.swing.AbstractButton;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.KeyStroke;
import javax.swing.SpinnerModel;
import javax.swing.tree.TreeNode;

/**
 * {@link IServices} implementation to provide services for external modules.
 * 
 * <p>
 * Implementation is IMMUTABLE and singleton (shared between external modules).
 * </p>
 * 
 * <p>
 * Most method implementation just delegates to the proper existing application component, this class acts as a bridge between the public external module API
 * and the non-public application internal classes.
 * </p>
 * 
 * <p>
 * <i>Implementation note:</i> Service implementation-like objects returned by this class <b>MUST</b> be thread safe! Even if the application does not use them
 * from multiple threads, the same guarantee cannot be told about external modules!
 * </p>
 * 
 * <p>
 * <i>Implementation note:</i> {@link IExtModManifestBean} cannot be moved here because manifest is module specific and I implemented this class in a
 * module-independent way.
 * </p>
 * 
 * @author Andras Belicza
 */
public class Services implements IServices, IUtils, IGuiUtils, ISettingsUtils, ISettingsGui, IFactory, IGuiFactory, IRepParserEngine, ISc2Monitor,
        IRepFolderMonitor, ISound {
	
	/** Services version. */
	public static final VersionBean VERSION  = new VersionBean( 1, 5, 1 );
	
	
	/** Singleton instance. Shared between external modules. */
	static final Services           INSTANCE = new Services();
	
	/**
	 * Creates a new {@link Services}.
	 */
	private Services() {
	}
	
	
	// =====================================================================================================================
	// ============ IServices implementation ===============================================================================
	// =====================================================================================================================
	
	
	@Override
	public VersionBean getVersionBean() {
		return VERSION;
	}
	
	
	@Override
	public Logger getLogger() {
		return Env.LOGGER;
	}
	
	@Override
	public Language getLanguage() {
		return Env.LANG;
	}
	
	@Override
	public IUtils getUtils() {
		// We implement IUtils
		return this;
	}
	
	@Override
	public IGuiUtils getGuiUtils() {
		// We implement IGuiUtils
		return this;
	}
	
	@Override
	public ISettingsUtils getSettingsUtils() {
		// We implement ISettingsUtils
		return this;
	}
	
	@Override
	public ISettingsGui getSettingsGui() {
		// We implement ISettingsGui
		return this;
	}
	
	@Override
	public IFactory getFactory() {
		// We implement IFactory
		return this;
	}
	
	@Override
	public IGuiFactory getGuiFactory() {
		// We implement IGuiFactory
		return this;
	}
	
	@Override
	public IRepParserEngine getRepParserEngine() {
		// We implement IRepParserEngine
		return this;
	}
	
	@Override
	public BdUtil getBdUtil() {
		return BdUtil.INSTANCE;
	}
	
	@Override
	public ISc2Monitor getSc2Monitor() {
		// We implement ISc2Monitor
		return this;
	}
	
	@Override
	public IRepFolderMonitor getRepFolderMonitor() {
		// We implement IRepFolderMonitor
		return this;
	}
	
	@Override
	public ISound getSound() {
		// We implement IRepFolderMonitor
		return this;
	}
	
	@Override
	public MainFrame getMainFrame() {
		return Env.MAIN_FRAME;
	}
	
	
	// =====================================================================================================================
	// ============ IUtils implementation ==================================================================================
	// =====================================================================================================================
	
	
	@Override
	public OpSys detectOs() {
		return OpSys.detect();
	}
	
	@Override
	public void showPathInFileBrowser( final Path path ) {
		Utils.showPathInFileBrowser( path );
	}
	
	@Override
	public void showURLInBrowser( final URL url ) {
		Env.UTILS_IMPL.get().showURLInBrowser( url );
	}
	
	@Override
	public void launchReplay( final Path replayFile ) {
		Utils.launchReplay( replayFile );
	}
	
	@Override
	public String safeForHtml( final String text ) {
		return Utils.safeForHtml( text );
	}
	
	@Override
	public boolean deletePath( final Path path ) {
		return Utils.deletePath( path );
	}
	
	@Override
	public Path uniqueFile( final Path file ) {
		return Utils.uniqueFile( file );
	}
	
	@Override
	public String calculateFileMd5( final Path file ) {
		return Utils.calculateFileMd5( file );
	}
	
	@Override
	public String calculateFileSha256( final Path file ) {
		return Utils.calculateFileSha256( file );
	}
	
	@Override
	public String calculateStreamSha256( final InputStream input, final long size ) {
		return Utils.calculateStreamSha256( input, size );
	}
	
	@Override
	public String toBase64String( final byte[] data ) {
		return Utils.toBase64String( data );
	}
	
	@Override
	public String toHexString( final byte[] data ) {
		return Utils.toHexString( data );
	}
	
	@Override
	public String toHexString( final byte[] data, final int offset, final int length ) {
		return Utils.toHexString( data, offset, length );
	}
	
	@Override
	public byte[] hexToBytes( final String hex ) {
		return Utils.hexToBytes( hex );
	}
	
	@Override
	public boolean containsIngoreCase( final String text, final String searchText ) {
		return Utils.containsIngoreCase( text, searchText );
	}
	
	@Override
	public String concatenate( final int[] arr ) {
		return Utils.concatenate( arr );
	}
	
	@Override
	public String concatenate( final Collection< ? > c ) {
		return Utils.concatenate( c );
	}
	
	@Override
	public void sortReversed( final int[] a ) {
		Utils.sortReversed( a );
	}
	
	@Override
	public String stripOffLeadingZeros( String s ) {
		return Utils.stripOffLeadingZeros( s );
	}
	
	@Override
	public void copyToClipboard( final String text ) {
		Utils.copyToClipboard( text );
	}
	
	@Override
	public String toCss( Color color ) {
		return Utils.toCss( color );
	}
	
	@Override
	public String getFileNameWithoutExt( final Path file ) {
		return Utils.getFileNameWithoutExt( file );
	}
	
	@Override
	public Pair< String, String > getFileNameAndExt( final Path file ) {
		return Utils.getFileNameAndExt( file );
	}
	
	@Override
	public boolean checkEmailSettings() {
		return Utils.checkEmailSettings();
	}
	
	@Override
	public byte[] readFully( final InputStream in, final byte[] buffer ) throws IOException {
		return Utils.readFully( in, buffer );
	}
	
	@Override
	public ExecutorService createExecutorService( final String parentThreadName ) {
		return Utils.createExecutorService( parentThreadName );
	}
	
	@Override
	public boolean shutdownExecutorService( final ExecutorService es ) {
		return Utils.shutdownExecutorService( es );
	}
	
	@Override
	public < T > boolean moveBackward( final List< T > list, final int[] indices ) {
		return Utils.moveBackward( list, indices );
	}
	
	@Override
	public < T > boolean moveForward( final List< T > list, final int[] indices ) {
		return Utils.moveForward( list, indices );
	}
	
	@Override
	public void setToonListValidator( final IIndicatorTextField itf ) {
		Utils.setToonListValidator( itf );
	}
	
	@Override
	public void setMergedAccountsValidator( final IIndicatorTextArea ita ) {
		Utils.setMergedAccountsValidator( ita );
	}
	
	@Override
	public void setEmailValidator( final IIndicatorTextField itf ) {
		Utils.setEmailValidator( itf );
	}
	
	@Override
	public void setEmailListValidator( final IIndicatorTextField itf ) {
		Utils.setEmailListValidator( itf );
	}
	
	
	// =====================================================================================================================
	// ============ IGuiUtils implementation ==================================================================================
	// =====================================================================================================================
	
	
	@Override
	public < T extends Component > T italicFont( final T comp ) {
		return GuiUtils.italicFont( comp );
	}
	
	@Override
	public < T extends Component > T boldFont( final T comp ) {
		return GuiUtils.boldFont( comp );
	}
	
	@Override
	public void maximizeWindowWithMargin( final Window window, final int margin, final Dimension maxSize ) {
		GuiUtils.maximizeWindowWithMargin( window, margin, maxSize );
	}
	
	@Override
	public void autoCreateDisabledImage( final JButton button ) {
		LGuiUtils.autoCreateDisabledImage( button );
	}
	
	@Override
	public Exception runInEDT( final Runnable task ) {
		return GuiUtils.runInEDT( task );
	}
	
	@Override
	public boolean confirm( final Object... messages ) {
		return GuiUtils.confirm( messages );
	}
	
	@Override
	public boolean askRetry( final Object... messages ) {
		return GuiUtils.askRetry( messages );
	}
	
	@Override
	public void showInfoMsg( final Object... messages ) {
		GuiUtils.showInfoMsg( messages );
	}
	
	@Override
	public void showErrorMsg( final Object... messages ) {
		GuiUtils.showErrorMsg( messages );
	}
	
	@Override
	public void showWarningMsg( final Object... messages ) {
		GuiUtils.showWarningMsg( messages );
	}
	
	@Override
	public boolean showInputMsg( final Object... messages ) {
		return GuiUtils.showInputMsg( messages );
	}
	
	@Override
	public String keyStrokeToString( final KeyStroke keyStroke ) {
		return GuiUtils.keyStrokeToString( keyStroke );
	}
	
	@Override
	public void setComponentTreeEnabled( final Component component, final boolean enabled ) {
		GuiUtils.setComponentTreeEnabled( component, enabled );
	}
	
	@Override
	public Link linkForAction( final String text, final IAction action ) {
		return GuiUtils.linkForAction( text, action );
	}
	
	@Override
	public void makeComponentDragScrollable( final JComponent component ) {
		GuiUtils.makeComponentDragScrollable( component );
	}
	
	@Override
	public Color colorWithAlpha( final Color c, final int alpha ) {
		return GuiUtils.colorWithAlpha( c, alpha );
	}
	
	@Override
	public JLabel renderToLabel( final JLabel l, final Object value, final boolean isSelected, final boolean hasFocus ) {
		return ToLabelRenderer.render( l, value, isSelected, hasFocus );
	}
	
	@Override
	public void showSendEmailDialog( final String to ) {
		new SendEmailDialog( to );
	}
	
	@Override
	public void showSendEmailDialog( final List< Path > attachmentList ) {
		new SendEmailDialog( attachmentList );
	}
	
	@Override
	public void showSendEmailDialog( final String title, final List< Path > attachmentList, final String to ) {
		new SendEmailDialog( title, attachmentList, to );
	}
	
	
	// =====================================================================================================================
	// ============ ISettingsUtils implementation ==========================================================================
	// =====================================================================================================================
	
	
	@Override
	public BoolSetting newBoolSetting( final String id, final ISetting< ? > parent, final ISettingsGroup group, final ISkillLevel skillLevel,
	        final String name, final IViewHints viewHints, final Boolean defaultValue ) {
		return new BoolSetting( id, parent, group, skillLevel, name, viewHints, defaultValue );
	}
	
	@Override
	public < T extends Enum< T > > EnumSetting< T > newEnumSetting( final String id, final ISetting< ? > parent, final ISettingsGroup group,
	        final ISkillLevel skillLevel, final String name, final IViewHints viewHints, final T defaultValue ) {
		return new EnumSetting<>( id, parent, group, skillLevel, name, viewHints, defaultValue );
	}
	
	@Override
	public < T extends Enum< T > > FixedEnumValuesSetting< T > newFixedEnumValuesSetting( final String id, final ISetting< ? > parent,
	        final ISettingsGroup group, final ISkillLevel skillLevel, final String name, final IViewHints viewHints, final T defaultValue,
	        @SuppressWarnings( "unchecked" ) final T... values ) {
		return new FixedEnumValuesSetting<>( id, parent, group, skillLevel, name, viewHints, defaultValue, values );
	}
	
	@Override
	public FixedIntValuesSetting newFixedIntValuesSetting( final String id, final ISetting< ? > parent, final ISettingsGroup group,
	        final ISkillLevel skillLevel, final String name, final IViewHints viewHints, final Integer defaultValue, final Integer... values ) {
		return new FixedIntValuesSetting( id, parent, group, skillLevel, name, viewHints, defaultValue, values );
	}
	
	@Override
	public IntSetting newIntSetting( final String id, final ISetting< ? > parent, final ISettingsGroup group, final ISkillLevel skillLevel, final String name,
	        final IViewHints viewHints, final Integer defaultValue, final Integer minValue, final Integer maxValue ) {
		return new IntSetting( id, parent, group, skillLevel, name, viewHints, defaultValue, minValue, maxValue );
	}
	
	@Override
	public MultilineStringSetting newMultilineStringSetting( final String id, final ISetting< ? > parent, final ISettingsGroup group,
	        final ISkillLevel skillLevel, final String name, final IViewHints viewHints, final String defaultValue ) {
		return new MultilineStringSetting( id, parent, group, skillLevel, name, viewHints, defaultValue );
	}
	
	@Override
	public NodeSetting newNodeSetting( final String id, final ISetting< ? > parent, final String name, final IRIcon ricon ) {
		return new NodeSetting( id, parent, name, ricon );
	}
	
	@Override
	public PathSetting newPathSetting( final String id, final ISetting< ? > parent, final ISettingsGroup group, final ISkillLevel skillLevel,
	        final String name, final IViewHints viewHints, final Path defaultValue ) {
		return new PathSetting( id, parent, group, skillLevel, name, viewHints, defaultValue );
	}
	
	@Override
	public StringSetting newStringSetting( final String id, final ISetting< ? > parent, final ISettingsGroup group, final ISkillLevel skillLevel,
	        final String name, final IViewHints viewHints, final String defaultValue ) {
		return new StringSetting( id, parent, group, skillLevel, name, viewHints, defaultValue );
	}
	
	@Override
	public ValidatedStringSetting newValidatedStringSetting( final String id, final ISetting< ? > parent, final ISettingsGroup group,
	        final ISkillLevel skillLevel, final String name, final IViewHints viewHints, final String defaultValue ) {
		return new ValidatedStringSetting( id, parent, group, skillLevel, name, viewHints, defaultValue );
	}
	
	@Override
	public TemplateSetting newTemplateSetting( final String id, final ISetting< ? > parent, final ISettingsGroup group, final ISkillLevel skillLevel,
	        final String name, final IViewHints viewHints, final String defaultValue ) {
		return new TemplateSetting( id, parent, group, skillLevel, name, viewHints, defaultValue );
	}
	
	@Override
	public ValidatedMultilineStringSetting newValidatedMultilineStringSetting( final String id, ISetting< ? > parent, final ISettingsGroup group,
	        final ISkillLevel skillLevel, final String name, final IViewHints viewHints, final String defaultValue ) {
		return new ValidatedMultilineStringSetting( id, parent, group, skillLevel, name, viewHints, defaultValue );
	}
	
	@Override
	public SettingsGroup newSettingsGroup( final String name ) {
		return new SettingsGroup( name );
	}
	
	@Override
	public SettingsGroup newSettingsGroup( final String name, final IRHtml helpRhtml ) {
		return new SettingsGroup( name, helpRhtml );
	}
	
	@Override
	public ViewHints newViewHints( final IRIcon ricon, final String subsequentText, final IRHtml rhtml, final String dialogTitle,
	        final ISsCompFactory< ISetting< ? > > ssCompFactory, final ICompConfigurer compConfigurer, final boolean editRequiresRegistration,
	        final Integer rows, final Integer columns ) {
		return new ViewHints( ricon, subsequentText, rhtml, dialogTitle, ssCompFactory, compConfigurer, editRequiresRegistration, rows, columns );
	}
	
	@Override
	public VHB newVHB() {
		return new VHB();
	}
	
	@Override
	public < T extends ISetting< ? > > TestBtnFactory< T > newTestBtnFactory( final ITestBtnListener< T > listener ) {
		return new TestBtnFactory< T >() {
			@Override
			public void doTest( final IButton button, final JComponent settingComp, final T setting, final ISettingsBean settings ) {
				listener.doTest( button, settingComp, setting, settings );
			}
		};
	}
	
	@Override
	public < T extends ISetting< ? > > TestBtnFactory< T > newTestBtnFactory( final String text, final Icon icon, final ITestBtnListener< T > listener ) {
		return new TestBtnFactory< T >( text, icon ) {
			@Override
			public void doTest( final IButton button, final JComponent settingComp, final T setting, final ISettingsBean settings ) {
				listener.doTest( button, settingComp, setting, settings );
			}
		};
	}
	
	@Override
	public HostCheckTestBtnFactory newHostCheckTestBtnFactory() {
		return new HostCheckTestBtnFactory();
	}
	
	
	// =====================================================================================================================
	// ============ ISettingsGui implementation ==================================================================================
	// =====================================================================================================================
	
	
	@Override
	public Link createSettingLink( final INodeSetting nodeSetting ) {
		return SettingsGui.createSettingLink( nodeSetting );
	}
	
	@Override
	public Link createSettingLink( final INodeSetting nodeSetting, final String text ) {
		return SettingsGui.createSettingLink( nodeSetting, text );
	}
	
	@Override
	public void checkRegistration( final ISetting< ? > setting, final JComponent settingComponent ) {
		SettingsGui.checkRegistration( setting, settingComponent );
	}
	
	@Override
	public < T > XComboBox< T > createSettingComboBox( final IFixedValuesSetting< T > setting, final ISettingsBean settings, final ActionListener customListener ) {
		return SettingsGui.createSettingComboBox( setting, settings, customListener );
	}
	
	@Override
	public XCheckBox createSettingCheckBox( final IBoolSetting setting, final ISettingsBean settings, final ActionListener customListener ) {
		return SettingsGui.createSettingCheckBox( setting, settings, customListener );
	}
	
	@Override
	public XSpinner createSettingSpinner( final IIntSetting setting, final ISettingsBean settings, final ActionListener customListener ) {
		return SettingsGui.createSettingSpinner( setting, settings, customListener );
	}
	
	@Override
	public XTextField createSettingTextField( final IStringSetting setting, final ISettingsBean settings, final ActionListener customListener ) {
		return SettingsGui.createSettingTextField( setting, settings, customListener );
	}
	
	@Override
	public XTextArea createSettingTextArea( final IMultilineStringSetting setting, final ISettingsBean settings, final ActionListener customListener ) {
		return SettingsGui.createSettingTextArea( setting, settings, customListener );
	}
	
	@Override
	public PathField createSettingPathField( final IPathSetting setting, final ISettingsBean settings, final ActionListener customListener ) {
		return SettingsGui.createSettingPathField( setting, settings, customListener );
	}
	
	@Override
	public IndicatorTextField createSettingIndicatorTextField( final IValidatedStringSetting setting, final ISettingsBean settings,
	        final ActionListener customListener ) {
		return SettingsGui.createSettingIndicatorTextField( setting, settings, customListener );
	}
	
	@Override
	public TemplateField createSettingTemplateField( final ITemplateSetting setting, final ISettingsBean settings, final ActionListener customListener ) {
		return SettingsGui.createSettingTemplateField( setting, settings, customListener );
	}
	
	@Override
	public IndicatorTextArea createSettingIndicatorTextArea( final IValidatedMultilineStringSetting setting, final ISettingsBean settings,
	        final ActionListener customListener ) {
		return SettingsGui.createSettingIndicatorTextArea( setting, settings, customListener );
	}
	
	@Override
	public < T > XComboBox< T > createBoundedSettingComboBox( final IFixedValuesSetting< T > setting, final ISettingsBean settings,
	        final ActionListener customListener ) {
		return SettingsGui.createBoundedSettingComboBox( setting, settings, customListener );
	}
	
	@Override
	public XCheckBox createBoundedSettingCheckBox( final IBoolSetting setting, final ISettingsBean settings, final ActionListener customListener ) {
		return SettingsGui.createBoundedSettingCheckBox( setting, settings, customListener );
	}
	
	@Override
	public XComboBox< SkillLevel > createSkillLevelComboBox( final ActionListener customListener ) {
		return SettingsGui.createSkillLevelComboBox( customListener );
	}
	
	@Override
	public void bindVisibilityToSkillLevel( final JComponent comp, final ISkillLevel minSkillLevel ) {
		SettingsGui.bindVisibilityToSkillLevel( comp, minSkillLevel );
	}
	
	@Override
	public void bindVisibilityToSkillLevel( final JComponent comp, final ISkillLevel minSkillLevel, final Boolean hiddenSelected ) {
		SettingsGui.bindVisibilityToSkillLevel( comp, minSkillLevel, hiddenSelected );
	}
	
	@Override
	public void bindVisibilityToSetting( final JComponent comp, final IBoolSetting setting, final ISettingsBean settings ) {
		SettingsGui.bindVisibilityToSetting( comp, setting, settings );
	}
	
	@Override
	public void bindSelectionToSetting( final AbstractButton button, final IBoolSetting setting, final ISettingsBean settings ) {
		SettingsGui.bindSelectionToSetting( button, setting, settings );
	}
	
	@Override
	public void addBindExecuteScl( final ISettingChangeListener scl, final ISettingsBean settings, final Set< ? extends ISetting< ? > > settingSet,
	        final JComponent comp ) {
		SettingsGui.addBindExecuteScl( scl, settings, settingSet, comp );
	}
	
	@Override
	public void bindScl( final ISettingChangeListener scl, final ISettingsBean settings, final Set< ? extends ISetting< ? > > settingSet, final JComponent comp ) {
		SettingsGui.bindScl( scl, settings, settingSet, comp );
	}
	
	@Override
	public void removeAllBoundedScl( final JComponent container ) {
		SettingsGui.removeAllBoundedScl( container );
	}
	
	
	// =====================================================================================================================
	// ============ IFactory implementation ================================================================================
	// =====================================================================================================================
	
	
	@Override
	public UrlBuilder newUrlBuilder( final URL url ) throws IllegalArgumentException {
		return new UrlBuilder( url );
	}
	
	@Override
	public HttpPost newHttpPost( final URL url, final Map< String, String > paramsMap ) {
		return new HttpPost( url, paramsMap );
	}
	
	@Override
	public SimpleFileProvider newSimpleFileProvider( final Path file, final Long lastModified ) {
		return new SimpleFileProvider( file, lastModified );
	}
	
	@Override
	public VersionView newVersionView( final int... parts ) {
		return new VersionView( parts );
	}
	
	@Override
	public VersionView newVersionView( final String version ) {
		return VersionView.fromString( version );
	}
	
	@Override
	public NormalThread newNormalThread( final String name, final Runnable runnable ) {
		return new NormalThread( name ) {
			@Override
			public void run() {
				runnable.run();
			}
		};
	}
	
	@Override
	public ControlledThread newControlledThread( final String name, final Runnable runnable ) {
		return new ControlledThread( name ) {
			@Override
			public void customRun() {
				runnable.run();
			}
		};
	}
	
	@Override
	public Job newJob( final String name, final IRIcon ricon, final Runnable runnable ) {
		return new Job( name, ricon ) {
			@Override
			public void jobRun() {
				runnable.run();
			}
		};
	}
	
	@Override
	public ProgressJob newProgressJob( final String name, final IRIcon ricon, final Runnable runnable ) {
		return new ProgressJob( name, ricon ) {
			@Override
			public void jobRun() {
				runnable.run();
			}
		};
	}
	
	@Override
	public VersionBean getTemplateEngineVersionBean() {
		return TemplateEngine.VERSION;
	}
	
	@Override
	public TemplateEngine newTemplateEngine( final String template ) throws InvalidTemplateException {
		return new TemplateEngine( template );
	}
	
	@Override
	public RepFiltersBean newRepFiltersBean() {
		return new RepFiltersBean();
	}
	
	@Override
	public RepFilterBean newRepFilterBean() {
		return RepFilterBean.createNewFilter();
	}
	
	@Override
	public RepFiltersEditorDialog newRepFiltersEditorDialog( final IRepFiltersBean repFiltersBean ) {
		return null;
	}
	
	@Override
	public VersionBean getRepSearchEngineVersionBean() {
		return RepSearchEngine.VERSION;
	}
	
	@Override
	public RepSearchEngine newRepSearchEngine( final IRepFiltersBean repFiltersBean ) {
		return new RepSearchEngine( repFiltersBean );
	}
	
	@Override
	public DurationValue newDurationValue( final long value ) {
		return new DurationValue( value );
	}
	
	@Override
	public DurationValue newDurationValue( final long value, final IDurationFormat durationFormat ) {
		return new DurationValue( value, durationFormat );
	}
	
	@Override
	public SizeValue newSizeValue( final long value ) {
		return new SizeValue( value );
	}
	
	@Override
	public SizeValue newSizeValue( final long value, final ISizeFormat sizeFormat ) {
		return new SizeValue( value, sizeFormat );
	}
	
	@Override
	public DateValue newDateValue( final Date value ) {
		return new DateValue( value );
	}
	
	@Override
	public DateValue newDateValue( final Date value, final IDateFormat dateFormat ) {
		return new DateValue( value, dateFormat );
	}
	
	@Override
	public < T > Holder< T > newHolder() {
		return new Holder<>();
	}
	
	@Override
	public < T > Holder< T > newHolder( final T value ) {
		return new Holder<>( value );
	}
	
	@Override
	public RelativeDate newRelativeDate( final Date date ) {
		return new RelativeDate( date );
	}
	
	@Override
	public RelativeDate newRelativeDate( final long date ) {
		return new RelativeDate( date );
	}
	
	@Override
	public RelativeDate newRelativeDate( final Date date, final boolean longForm, final int tokens, final boolean appendEra ) {
		return new RelativeDate( date, longForm, tokens, appendEra );
	}
	
	@Override
	public RelativeDate newRelativeDate( final long date, final boolean longForm, final int tokens, final boolean appendEra ) {
		return new RelativeDate( date, longForm, tokens, appendEra );
	}
	
	@Override
	public < T1, T2 > Pair< T1, T2 > newPair( final T1 value1, final T2 value2 ) {
		return new Pair<>( value1, value2 );
	}
	
	@Override
	public SelectionTracker newSelectionTracker() {
		return new SelectionTracker();
	}
	
	
	// =====================================================================================================================
	// ============ IGuiFactory implementation ================================================================================
	// =====================================================================================================================
	
	
	@Override
	public XLabel newLabel() {
		return new XLabel();
	}
	
	@Override
	public XLabel newLabel( final String text ) {
		return new XLabel( text );
	}
	
	@Override
	public XLabel newLabel( final String text, final int halignment ) {
		return new XLabel( text, halignment );
	}
	
	@Override
	public XLabel newLabel( final Icon icon ) {
		return new XLabel( icon );
	}
	
	@Override
	public XLabel newLabel( final String text, final Icon icon ) {
		return new XLabel( text, icon );
	}
	
	@Override
	public XLabel newLabel( final String text, final Icon icon, final int halignment ) {
		return new XLabel( text, icon, halignment );
	}
	
	@Override
	public LRIcon newRIcon( final URL url ) {
		return new LRIcon( url );
	}
	
	@Override
	public LRHtml newRHtml( final String title, final URL resource, final String... params ) {
		return new LRHtml( title, resource, params );
	}
	
	@Override
	public LRHtml newRHtml( final String title, final URL resource, final Set< String > htmlParams, final String... params ) {
		return new LRHtml( title, resource, htmlParams, params );
	}
	
	@Override
	public HelpIcon newHelpIcon( final IRHtml helpRhtml ) {
		return new HelpIcon( helpRhtml );
	}
	
	@Override
	public HelpIcon newHelpIcon( final IRHtml helpRhtml, final boolean stressed ) {
		return new HelpIcon( helpRhtml, stressed );
	}
	
	@Override
	public TipIcon newTipIcon( final IRHtml tipRhtml ) {
		return new TipIcon( tipRhtml );
	}
	
	@Override
	public Link newLink() {
		return new Link();
	}
	
	@Override
	public Link newLink( final String text ) {
		return new Link( text );
	}
	
	@Override
	public Link newLink( final String text, final URL url ) {
		return new Link( text, url );
	}
	
	@Override
	public Link newLink( final String text, final Consumer< MouseEvent > consumer ) {
		return new Link( text, consumer );
	}
	
	@Override
	public Link newLink( final String text, final URL url, final Consumer< MouseEvent > consumer ) {
		return new Link( text, url, consumer );
	}
	
	@Override
	@SuppressWarnings( "serial" )
	public XAction newAction( final IRIcon ricon, final String text, final ActionListener listener ) {
		return new XAction( ricon, text ) {
			@Override
			public void actionPerformed( final ActionEvent event ) {
				listener.actionPerformed( event );
			}
		};
	}
	
	@Override
	@SuppressWarnings( "serial" )
	public XAction newAction( final IRIcon ricon, final String text, final JComponent comp, final ActionListener listener ) {
		return new XAction( ricon, text, comp ) {
			@Override
			public void actionPerformed( final ActionEvent event ) {
				listener.actionPerformed( event );
			}
		};
	}
	
	@Override
	@SuppressWarnings( "serial" )
	public XAction newAction( final KeyStroke keyStroke, final IRIcon ricon, final String text, final ActionListener listener ) {
		return new XAction( keyStroke, ricon, text ) {
			@Override
			public void actionPerformed( final ActionEvent event ) {
				listener.actionPerformed( event );
			}
		};
	}
	
	@Override
	@SuppressWarnings( "serial" )
	public IAction newAction( final KeyStroke keyStroke, final IRIcon ricon, final String text, final JComponent comp, final ActionListener listener ) {
		return new XAction( keyStroke, ricon, text ) {
			@Override
			public void actionPerformed( final ActionEvent event ) {
				listener.actionPerformed( event );
			}
		};
	}
	
	@Override
	public XButton newButton() {
		return new XButton();
	}
	
	@Override
	public IButton newButton( final String text ) {
		return new XButton( text );
	}
	
	@Override
	public XButton newButton( final Icon icon ) {
		return new XButton( icon );
	}
	
	@Override
	public XButton newButton( final String text, final Icon icon ) {
		return new XButton( text, icon );
	}
	
	@Override
	public XButton newButton( final IAction action ) {
		return new XButton( action );
	}
	
	@Override
	public XTextField newTextField() {
		return new XTextField();
	}
	
	@Override
	public XTextField newTextField( final boolean clearByEsc ) {
		return new XTextField( clearByEsc );
	}
	
	@Override
	public XTextField newTextField( final String text ) {
		return new XTextField( text );
	}
	
	@Override
	public XTextField newTextField( final int columns ) {
		return new XTextField( columns );
	}
	
	@Override
	public XTextField newTextField( final String text, final int columns ) {
		return new XTextField( text, columns );
	}
	
	@Override
	public XTextField newTextField( final String text, final int columns, final boolean clearByEsc ) {
		return new XTextField( text, columns, clearByEsc );
	}
	
	@Override
	public IndicatorTextField newIndicatorTextField() {
		return new IndicatorTextField();
	}
	
	@Override
	public IndicatorTextField newIndicatorTextField( final String text ) {
		return new IndicatorTextField( text );
	}
	
	@Override
	public XTextArea newTextArea() {
		return new XTextArea();
	}
	
	@Override
	public XTextArea newTextArea( final String text ) {
		return new XTextArea( text );
	}
	
	@Override
	public XTextArea newTextArea( final String text, final int rows, final int columns ) {
		return new XTextArea( text, rows, columns );
	}
	
	@Override
	public IndicatorTextArea newIndicatorTextArea() {
		return new IndicatorTextArea();
	}
	
	@Override
	public IndicatorTextArea newIndicatorTextArea( final String text ) {
		return new IndicatorTextArea( text );
	}
	
	@Override
	public BorderPanel newBorderPanel() {
		return new BorderPanel();
	}
	
	@Override
	public BorderPanel newBorderPanel( final JComponent centerComp ) {
		return new BorderPanel( centerComp );
	}
	
	@Override
	public Browser newBrowser() {
		return new Browser();
	}
	
	@Override
	public ColorIcon newColorIcon( final Color color ) {
		return new ColorIcon( color );
	}
	
	@Override
	public ColorIcon newColorIcon( final Color color, final String text ) {
		return new ColorIcon( color, text );
	}
	
	@Override
	public ColorIcon newColorIcon( final Color color, final int width, final int height ) {
		return new ColorIcon( color, width, height );
	}
	
	@Override
	public ColorIcon newColorIcon( final Color color, final int width, final int height, final String text ) {
		return new ColorIcon( color, width, height, text );
	}
	
	@Override
	public GridBagPanel newGridBagPanel() {
		return new GridBagPanel();
	}
	
	@Override
	public ModestLabel newModestLabel() {
		return new ModestLabel();
	}
	
	@Override
	public ModestLabel newModestLabel( final String text ) {
		return new ModestLabel( text );
	}
	
	@Override
	public XFileChooser newFileChooser() {
		return new XFileChooser();
	}
	
	@Override
	public XFileChooser newFileChooser( final Path currentFolder ) {
		return new XFileChooser( currentFolder );
	}
	
	@Override
	public PathField newPathField() {
		return new PathField();
	}
	
	@Override
	public PathField newPathField( final boolean fileMode ) {
		return new PathField( fileMode );
	}
	
	@Override
	public PathField newPathField( final Path path ) {
		return new PathField( path );
	}
	
	@Override
	public PathField newPathField( final Path path, final boolean fileMode ) {
		return new PathField( path, fileMode );
	}
	
	@Override
	public StatusLabel newStatusLabel() {
		return new StatusLabel();
	}
	
	@Override
	public StatusLabel newStatusLabel( final IStatusType type, final String message ) {
		return new StatusLabel( type, message );
	}
	
	@Override
	public XCheckBox newCheckBox() {
		return new XCheckBox();
	}
	
	@Override
	public XCheckBox newCheckBox( final String text ) {
		return new XCheckBox( text );
	}
	
	@Override
	public XCheckBox newCheckBox( final String text, final boolean selected ) {
		return new XCheckBox( text, selected );
	}
	
	@Override
	public XProgressBar newProgressBar() {
		return new XProgressBar();
	}
	
	@Override
	public XScrollPane newScrollPane() {
		return new XScrollPane();
	}
	
	@Override
	public XScrollPane newScrollPane( final Component view ) {
		return new XScrollPane( view );
	}
	
	@Override
	public XScrollPane newScrollPane( final Component view, final boolean smallSize ) {
		return new XScrollPane( view, smallSize );
	}
	
	@Override
	public XScrollPane newScrollPane( final Component view, final boolean smallSize, final boolean followScrollingSetting ) {
		return new XScrollPane( view, smallSize, followScrollingSetting );
	}
	
	@Override
	public XSpinner newSpinner() {
		return new XSpinner();
	}
	
	@Override
	public XSpinner newSpinner( final SpinnerModel model ) {
		return new XSpinner( model );
	}
	
	@Override
	public XSplitPane newSplitPane() {
		return new XSplitPane();
	}
	
	@Override
	public XSplitPane newSplitPane( final int orientation ) {
		return new XSplitPane( orientation );
	}
	
	@Override
	public XToolBar newToolBar() {
		return new XToolBar();
	}
	
	@Override
	public XToolBar newToolBar( final boolean initialSpace ) {
		return new XToolBar( initialSpace );
	}
	
	@Override
	public < E > XComboBox< E > newComboBox() {
		return new XComboBox<>();
	}
	
	@Override
	public < E > XComboBox< E > newComboBox( final E[] items ) {
		return new XComboBox<>( items );
	}
	
	@Override
	public < E > XComboBox< E > newComboBox( final Vector< E > items ) {
		return new XComboBox<>( items );
	}
	
	@Override
	public XTable newTable() {
		return new XTable();
	}
	
	@Override
	public ProgressBarView newProgressBarView( final int value, final int maximum ) {
		return new ProgressBarView( value, maximum );
	}
	
	@Override
	public ProgressBarView newProgressBarView( final int value, final int maximum, final String stringValue ) {
		return new ProgressBarView( value, maximum, stringValue );
	}
	
	@Override
	public BarCodeView newBarCodeView( final int[] values, final Color[] colors, final Color[] selColors ) {
		return new BarCodeView( values, colors, selColors );
	}
	
	@Override
	public BarCodeView newBarCodeView( final int[] values, final Color[] colors, final Color[] selColors, final String stringValue ) {
		return new BarCodeView( values, colors, selColors, stringValue );
	}
	
	@Override
	public ToolBarForTable newToolBarForTable( final ITable table ) {
		return new ToolBarForTable( table );
	}
	
	@Override
	public < E > XList< E > newList() {
		return new XList<>();
	}
	
	@Override
	public IToolBarForList newToolBarForList( final IList< ? > list ) {
		return new ToolBarForList( list );
	}
	
	@Override
	public XTree newTree( final TreeNode root ) {
		return new XTree( root );
	}
	
	@Override
	public ToolBarForTree newToolBarForTree( final ITree tree ) {
		return new ToolBarForTree( tree );
	}
	
	@Override
	public XMenu newMenu() {
		return new XMenu();
	}
	
	@Override
	public XMenu newMenu( final String text ) {
		return new XMenu( text );
	}
	
	@Override
	public XMenu newMenu( final Icon icon ) {
		return new XMenu( icon );
	}
	
	@Override
	public XMenu newMenu( final String text, final Icon icon ) {
		return new XMenu( text, icon );
	}
	
	@Override
	public XMenuItem newMenuItem() {
		return new XMenuItem();
	}
	
	@Override
	public XMenuItem newMenuItem( final String text ) {
		return new XMenuItem( text );
	}
	
	@Override
	public XMenuItem newMenuItem( final Icon icon ) {
		return new XMenuItem( icon );
	}
	
	@Override
	public XMenuItem newMenuItem( final String text, final Icon icon ) {
		return new XMenuItem( text, icon );
	}
	
	@Override
	public XCheckBoxMenuItem newCheckBoxMenuItem() {
		return new XCheckBoxMenuItem();
	}
	
	@Override
	public XCheckBoxMenuItem newCheckBoxMenuItem( final String text ) {
		return new XCheckBoxMenuItem( text );
	}
	
	@Override
	public XCheckBoxMenuItem newCheckBoxMenuItem( final Icon icon ) {
		return new XCheckBoxMenuItem( icon );
	}
	
	@Override
	public XCheckBoxMenuItem newCheckBoxMenuItem( final String text, final Icon icon ) {
		return new XCheckBoxMenuItem( text, icon );
	}
	
	@Override
	public XPasswordField newPasswordField() {
		return new XPasswordField();
	}
	
	@Override
	public XPasswordField newPasswordField( final String text ) {
		return new XPasswordField( text );
	}
	
	@Override
	public XPasswordField newPasswordField( final int columns ) {
		return new XPasswordField( columns );
	}
	
	@Override
	public XPasswordField newPasswordField( final String text, final int columns ) {
		return new XPasswordField( text, columns );
	}
	
	@Override
	public XPopupMenu newPopupMenu() {
		return new XPopupMenu();
	}
	
	@Override
	public XPopupMenu newPopupMenu( final String text ) {
		return new XPopupMenu( text );
	}
	
	@Override
	public XPopupMenu newPopupMenu( final String text, final Icon icon ) {
		return new XPopupMenu( text, icon );
	}
	
	@Override
	public XPopupMenu newPopupMenu( final ILabel titleLabel ) {
		return new XPopupMenu( titleLabel );
	}
	
	@Override
	public XTabbedPane newTabbedPane() {
		return new XTabbedPane();
	}
	
	@Override
	public < T extends JComponent > IPage< T > newPage( final String displayName, final IRIcon ricon, final boolean closeable,
	        final IPageCompCreator< T > pageCompCreator ) {
		return new BasePage< T >( displayName, ricon, closeable ) {
			@Override
			public T createPageComp() {
				return pageCompCreator.createPageComp();
			}
		};
	}
	
	@Override
	public IPage< ? extends IBrowser > newBrowserPage( final String displayName, final IRIcon ricon, final IRHtml rhtml ) {
		return new BrowserPage( displayName, ricon, rhtml );
	}
	
	@Override
	public IMultiPageComp newMultiPageComp( final List< IPage< ? > > pageList, final IPage< ? > defaultPage, final JComponent rootComponent ) {
		return new MultiPageComp( pageList, defaultPage, rootComponent );
	}
	
	@Override
	public TemplateField newTemplateField() {
		return new TemplateField();
	}
	
	@Override
	public TemplateField newTemplateField( final String template ) {
		return new TemplateField( template );
	}
	
	@Override
	public TemplateField newTemplateField( final boolean addDialogOpener ) {
		return new TemplateField( addDialogOpener );
	}
	
	@Override
	public TemplateField newTemplateField( final String template, final boolean addDialogOpener ) {
		return new TemplateField( template, addDialogOpener );
	}
	
	@Override
	public XDialog newDialog( final Runnable closeTask ) {
		return newDialog( Env.MAIN_FRAME, closeTask );
	}
	
	@Override
	@SuppressWarnings( "serial" )
	public XDialog newDialog( final Frame owner, final Runnable closeTask ) {
		return closeTask == null ? new XDialog( owner ) : new XDialog( owner ) {
			@Override
			protected void customOnClose() {
				closeTask.run();
			}
		};
	}
	
	@Override
	@SuppressWarnings( "serial" )
	public OverlayCard newOverlayCard( final OverlayCardParams params, final IConfigPopupBuilder configPopupBuilder, final Runnable closeTask ) {
		return new OverlayCard( params ) {
			@Override
			public void buildConfigPopup( final IPopupMenu popupMenu ) {
				super.buildConfigPopup( popupMenu );
				if ( configPopupBuilder != null )
					configPopupBuilder.buildConfigPopup( popupMenu );
			}
			
			@Override
			protected void customOnClose() {
				if ( closeTask != null )
					closeTask.run();
			}
		};
	}
	
	@Override
	public < T > RenderablePair< T > newRenderablePair( final Icon icon, final T textProvider ) {
		return new RenderablePair<>( icon, textProvider );
	}
	
	
	// =====================================================================================================================
	// ============ IRepParserEngine implementation ========================================================================
	// =====================================================================================================================
	
	
	@Override
	public VersionBean getRepParserEngineVersionBean() {
		return RepParserEngine.VERSION;
	}
	
	@Override
	public VersionBean getRepProcessorVersionBean() {
		return RepProcessor.VERSION;
	}
	
	@Override
	public VersionBean getRepProcCacheVersionBean() {
		return RepProcCache.VERSION;
	}
	
	@Override
	public Replay parseReplay( final Path file ) {
		return RepParserEngine.parseReplay( file );
	}
	
	@Override
	public RepProcessor parseAndWrapReplay( final Path file ) {
		final RepProcessor repProc = new RepProcessor( file );
		return repProc.replay == null ? null : repProc;
	}
	
	@Override
	public RepProcessor getRepProc( final Path file ) {
		return RepParserEngine.getRepProc( file );
	}
	
	@Override
	public Toon newToon( final String toon ) throws IllegalArgumentException {
		return new Toon( toon );
	}
	
	@Override
	public Toon newToon( final String toon, final boolean parsePlayerName ) throws IllegalArgumentException {
		return new Toon( toon, parsePlayerName );
	}
	
	
	// =====================================================================================================================
	// ============ ISc2Monitor implementation =============================================================================
	// =====================================================================================================================
	
	
	@Override
	public boolean isSupported() {
		return Sc2RegMonitor.isSupported();
	}
	
	@Override
	public IGameStatus getGameStatus() {
		return Sc2RegMonitor.getGameStatus();
	}
	
	@Override
	public long getGameStatusSince() {
		return Env.SC2_REG_MONITOR.getGameStatusSince();
	}
	
	@Override
	public void addGameChangeListener( final IGameChangeListener listener ) {
		Env.SC2_REG_MONITOR.addGameChangeListener( listener );
	}
	
	@Override
	public void removeGameChangeListener( final IGameChangeListener listener ) {
		Env.SC2_REG_MONITOR.removeGameChangeListener( listener );
	}
	
	@Override
	public Integer getApm() {
		return Sc2RegMonitor.getApm();
	}
	
	
	// =====================================================================================================================
	// ============ IRepFolderMonitor implementation =======================================================================
	// =====================================================================================================================
	
	
	@Override
	public void addNewRepListener( final INewRepListener listener ) {
		NewReplayHandlerJob.addNewRepListener( listener );
	}
	
	@Override
	public void removeNewRepListener( final INewRepListener listener ) {
		NewReplayHandlerJob.removeNewRepListener( listener );
	}
	
	
	// =====================================================================================================================
	// ============ ISound implementation ==================================================================================
	// =====================================================================================================================
	
	@Override
	public void beep() {
		Sound.beep();
	}
	
	@Override
	public void beepOnError() {
		Sound.beepOnError();
	}
	
	@Override
	public void beepOnWarning() {
		Sound.beepOnWarning();
	}
	
	@Override
	public void beepOnConfirmation() {
		Sound.beepOnConfirmation();
	}
	
	@Override
	public void beepOnInput() {
		Sound.beepOnInput();
	}
	
	@Override
	public void beepOnInfo() {
		Sound.beepOnInfo();
	}
	
	@Override
	public void beepOnEmptyTxtSearchRslt() {
		Sound.beepOnEmptyTxtSearchRslt();
	}
	
	@Override
	public boolean play( final Path file, final boolean wait ) {
		return Sound.play( file, wait );
	}
	
	@Override
	public boolean play( final String name, final InputStream dataStream, final boolean wait ) {
		return Sound.play( name, dataStream, wait );
	}
	
}
