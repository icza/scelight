/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight.sc2.balancedata;

import hu.belicza.andras.util.VersionView;
import hu.belicza.andras.util.XmlProcessor;
import hu.scelight.sc2.balancedata.model.Ability;
import hu.scelight.sc2.balancedata.model.BuildAbility;
import hu.scelight.sc2.balancedata.model.BuildCommand;
import hu.scelight.sc2.balancedata.model.Command;
import hu.scelight.sc2.balancedata.model.TrainAbility;
import hu.scelight.sc2.balancedata.model.TrainCommand;
import hu.scelight.sc2.balancedata.model.Unit;
import hu.scelight.sc2.balancedata.model.UpgradeAbility;
import hu.scelight.sc2.balancedata.model.UpgradeCommand;
import hu.scelight.service.env.Env;
import hu.scelight.util.Utils;
import hu.scelightapi.sc2.balancedata.IBalanceData;
import hu.scelightapi.sc2.balancedata.model.IAbility;
import hu.slsc2balancedata.r.BR;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.zip.GZIPInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * SC2 Balance data (pack) repository.
 * 
 * <p>
 * TODO Missing balance data packs to be created:
 * </p>
 * <ul>
 * <li>1.0.0.16117-1.0.3.16291.gz
 * <li>1.1.0.16561-1.4.1.19776.gz
 * <li>1.4.2.20141.gz
 * <li>1.4.3.21029-1.4.4.22418.gz
 * <li>1.5.0.22612-1.5.2.22875.gz
 * <li>1.5.3.23260-1.5.4.24540.gz
 * <li>2.0.10.26585.gz
 * <li>2.0.11.26825-9.9.9.99999.gz
 * <li>2.0.3.24764-2.0.8.25604.gz
 * <li>2.0.9.26147.gz
 * </ul>
 * 
 * @author Andras Belicza
 * 
 * @see BdUtil
 */
public class BalanceData implements IBalanceData {
	
	/** Balance data resource list. */
	private static final List< BDRes >                   BD_RES_LIST              = new ArrayList<>();
	static {
		try ( final BufferedReader in = new BufferedReader( new InputStreamReader( BR.getStream( "list.txt" ), Env.UTF8 ) ) ) {
			
			String line;
			while ( ( line = in.readLine() ) != null ) {
				if ( line.startsWith( "#" ) )
					continue; // Comment line
				BD_RES_LIST.add( new BDRes( line ) );
			}
			
		} catch ( final IOException ie ) {
			throw new RuntimeException( "Failed to read SC2 balance data resource list!", ie );
		}
		
		// Sort it so the latest version is at the end.
		Collections.sort( BD_RES_LIST );
	}
	
	/** Cache of loaded balance data, mapped from replay version for fast access. */
	private static final Map< VersionView, BalanceData > VERSION_BALANCE_DATA_MAP = new HashMap<>();
	static {
		// Load latest balance data
		get( BD_RES_LIST.get( BD_RES_LIST.size() - 1 ).minVer );
	}
	
	/**
	 * Returns the balance data for the specified replay version.
	 * 
	 * @param version replay version to return balance data for
	 * @return the balance data for the specified replay version
	 */
	public static BalanceData get( final VersionView version ) {
		BalanceData bd = VERSION_BALANCE_DATA_MAP.get( version );
		
		if ( bd == null && !VERSION_BALANCE_DATA_MAP.containsKey( version ) ) {
			// Search balance data resource
			BDRes bdRes = null;
			for ( final BDRes bdr : BD_RES_LIST ) {
				if ( bdr.minVer.compareTo( version ) <= 0 && bdr.maxVer.compareTo( version ) >= 0 ) {
					// Found!
					bdRes = bdr;
					break;
				}
			}
			
			if ( bdRes != null ) {
				if ( bdRes.balanceData.get() == null ) {
					try ( final InputStream in = bdRes.res.openStream() ) {
						
						if ( in == null )
							throw new MissingResourceException( "Missing balance data for replay version: " + version, null, null );
						
						if ( in != null )
							try ( final GZIPInputStream gzin = new GZIPInputStream( in ) ) {
								bdRes.balanceData.set( new BalanceData( bdRes.minVer, bdRes.maxVer, gzin ) );
							}
						
					} catch ( final Exception e ) {
						Env.LOGGER.error( "Failed to load balance data for version: " + version, e );
					}
				}
				bd = bdRes.balanceData.get();
			}
			
			if ( bd == null )
				Env.LOGGER.info( "No balance data available for version: " + version );
			
			// Store balance data even if it's null (so next time we won't have to search and come to this result again)
			VERSION_BALANCE_DATA_MAP.put( version, bd );
		}
		
		return bd;
	}
	
	
	
	/** Minimum replay version this balance data is for. */
	public final VersionView minVersion;
	
	/** Maximum replay version this balance data is for. */
	public final VersionView maxVersion;
	
	/**
	 * Creates a new {@link BalanceData}.
	 * 
	 * @param minVersion minimum replay version this balance data is for
	 * @param maxVersion maximum replay version this balance data is for
	 * @param in input stream of the balance data
	 * @throws Exception if any Exception is thrown during processing the balance data
	 */
	private BalanceData( final VersionView minVersion, final VersionView maxVersion, final InputStream in ) throws Exception {
		this.minVersion = minVersion;
		this.maxVersion = maxVersion;
		
		final DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		
		Document doc;
		
		// Read
		// First read the SC2 strings
		doc = readDocument( in, documentBuilder );
		final NodeList eList = doc.getDocumentElement().getElementsByTagName( "e" );
		// Key is the text index which is a number but I don't parse it into integer neither here nor later (when referenced)
		final Map< String, String > idxTextMap = Utils.newHashMap( eList.getLength() );
		for ( int i = eList.getLength() - 1; i >= 0; i-- ) {
			final Element e = (Element) eList.item( i );
			idxTextMap.put( e.getAttribute( "id" ), e.getTextContent() ); // TODO consider intern() the text content
		}
		
		// Read unit XMLs
		while ( ( doc = readDocument( in, documentBuilder ) ) != null ) {
			final XmlProcessor xp = new XmlProcessor( doc );
			
			Element e, e2;
			String s;
			
			final Unit unit = new Unit();
			
			unit.id = xp.docElement.getAttribute( "id" ).intern();
			idUnitMap.put( unit.id, unit );
			
			// Note: I decided to lowercase icon names because in zip archives case matters,
			// and in some cases icon name is capitalized differently.
			// (E.g. MULE.xml icon="btn-unit-terran-mule"
			// but OrbitalCommand.xml ability "CalldownMULE" icon="BTN-Unit-Terran-mule"!)
			
			if ( ( e = xp.getElementByTagName( "meta" ) ) != null ) {
				unit.text = idxTextMap.get( e.getAttribute( "name" ) );
				unit.icon = e.getAttribute( "icon" ).toLowerCase().intern();
				idxUnitMap.put( Integer.valueOf( e.getAttribute( "index" ) ), unit );
			}
			
			if ( ( e = xp.getElementByTagName( "misc" ) ) != null ) {
				if ( !( s = e.getAttribute( "radius" ) ).isEmpty() )
					unit.radius = Float.parseFloat( s );
			}
			
			if ( ( e = xp.getElementByTagName( "cost" ) ) != null ) {
				if ( !( s = e.getAttribute( "time" ) ).isEmpty() )
					unit.costTime = Float.parseFloat( s );
			}
			
			// Process abilities
			if ( ( e = xp.getElementByTagName( "abilities" ) ) != null ) {
				final NodeList abList = e.getElementsByTagName( "ability" );
				for ( int i = abList.getLength() - 1; i >= 0; i-- ) {
					final Element abEl = (Element) abList.item( i );
					// Ability index might be null if the ability is automatic and not user issued
					// (e.g. SwarmSeeds ability of the BroodLord or CloackingField of the Mothership)
					if ( ( s = abEl.getAttribute( "index" ) ).isEmpty() )
						continue;
					final Integer abIdx = Integer.valueOf( s );
					Ability ability = getAbility( abIdx );
					if ( ability == null ) {
						// This ability was not yet listed at other units
						// Intern the ability id because we will use it in comparison
						idxAbilityMap.put( abIdx, ability = new Ability( abEl.getAttribute( "id" ).intern() ) );
					}
					final NodeList cmdList = abEl.getElementsByTagName( "command" );
					for ( int j = cmdList.getLength() - 1; j >= 0; j-- ) {
						final Element cmdEl = (Element) cmdList.item( j );
						final Integer cmdIdx = Integer.valueOf( cmdEl.getAttribute( "index" ) );
						Command cmd = ability.getCommand( cmdIdx );
						if ( cmd != null )
							continue; // This command was already listed at another unit
						ability.idxCmdMap.put( cmdIdx, cmd = new Command( ability.id ) );
						if ( ( e2 = XmlProcessor.getElementByTagName( cmdEl, "meta" ) ) != null ) {
							cmd.text = idxTextMap.get( e2.getAttribute( "name" ) );
							cmd.icon = e2.getAttribute( "icon" ).toLowerCase().intern();
						}
						if ( ( e2 = XmlProcessor.getElementByTagName( cmdEl, "cost" ) ) != null ) {
							if ( !( s = e2.getAttribute( "time" ) ).isEmpty() )
								cmd.costTime = Float.parseFloat( s );
						}
					}
				}
			}
			
			// Process trains
			if ( ( e = xp.getElementByTagName( "trains" ) ) != null ) {
				final NodeList unList = e.getElementsByTagName( "unit" );
				for ( int i = unList.getLength() - 1; i >= 0; i-- ) {
					final Element unEl = (Element) unList.item( i );
					// Process only first-level children (there are optional "requirement" children with embedded tags)
					if ( unEl.getParentNode() != e )
						continue;
					final Integer abIdx = Integer.valueOf( unEl.getAttribute( "ability" ) );
					TrainAbility ability = getAbility( abIdx );
					if ( ability == null )
						idxAbilityMap.put( abIdx, ability = new TrainAbility( unit.id ) );
					final Integer cmdIdx = Integer.valueOf( unEl.getAttribute( "index" ) );
					TrainCommand cmd = ability.getCommand( cmdIdx );
					if ( cmd != null )
						continue; // This command was already listed at another unit
					ability.idxCmdMap.put( cmdIdx, cmd = new TrainCommand( ability.id ) );
					cmd.id = unEl.getAttribute( "id" ).intern();
					if ( ( e2 = XmlProcessor.getElementByTagName( unEl, "meta" ) ) != null ) {
						cmd.text = idxTextMap.get( e2.getAttribute( "name" ) );
						cmd.icon = e2.getAttribute( "icon" ).toLowerCase().intern();
					}
					if ( ( e2 = XmlProcessor.getElementByTagName( unEl, "cost" ) ) != null ) {
						if ( !( s = e2.getAttribute( "time" ) ).isEmpty() )
							cmd.costTime = Float.parseFloat( s );
					}
				}
			}
			
			// Process builds
			if ( ( e = xp.getElementByTagName( "builds" ) ) != null ) {
				final NodeList unList = e.getElementsByTagName( "unit" );
				for ( int i = unList.getLength() - 1; i >= 0; i-- ) {
					final Element unEl = (Element) unList.item( i );
					// Process only first-level children (there are optional "requirement" children with embedded tags)
					if ( unEl.getParentNode() != e )
						continue;
					final Integer abIdx = Integer.valueOf( unEl.getAttribute( "ability" ) );
					BuildAbility ability = getAbility( abIdx );
					if ( ability == null )
						idxAbilityMap.put( abIdx, ability = new BuildAbility( unit.id ) );
					final Integer cmdIdx = Integer.valueOf( unEl.getAttribute( "index" ) );
					BuildCommand cmd = ability.getCommand( cmdIdx );
					if ( cmd != null )
						continue; // This command was already listed at another unit
					ability.idxCmdMap.put( cmdIdx, cmd = new BuildCommand( ability.id ) );
					cmd.id = unEl.getAttribute( "id" ).intern();
					if ( ( e2 = XmlProcessor.getElementByTagName( unEl, "meta" ) ) != null ) {
						cmd.text = idxTextMap.get( e2.getAttribute( "name" ) );
						cmd.icon = e2.getAttribute( "icon" ).toLowerCase().intern();
					}
					if ( ( e2 = XmlProcessor.getElementByTagName( unEl, "cost" ) ) != null ) {
						if ( !( s = e2.getAttribute( "time" ) ).isEmpty() )
							cmd.costTime = Float.parseFloat( s );
					}
				}
			}
			
			// Process researches
			if ( ( e = xp.getElementByTagName( "researches" ) ) != null ) {
				final NodeList reList = e.getElementsByTagName( "upgrade" );
				for ( int i = reList.getLength() - 1; i >= 0; i-- ) {
					final Element upEl = (Element) reList.item( i );
					// Process only first-level children (there are optional "requirement" children with embedded tags)
					if ( upEl.getParentNode() != e )
						continue;
					final Integer abIdx = Integer.valueOf( upEl.getAttribute( "ability" ) );
					UpgradeAbility ability = getAbility( abIdx );
					if ( ability == null )
						idxAbilityMap.put( abIdx, ability = new UpgradeAbility( unit.id ) );
					final Integer cmdIdx = Integer.valueOf( upEl.getAttribute( "index" ) );
					UpgradeCommand cmd = ability.getCommand( cmdIdx );
					if ( cmd != null )
						continue; // This command was already listed at another unit
					ability.idxCmdMap.put( cmdIdx, cmd = new UpgradeCommand( ability.id ) );
					cmd.id = upEl.getAttribute( "id" ).intern();
					idUpgradeCmdMap.put( cmd.id, cmd );
					if ( ( e2 = XmlProcessor.getElementByTagName( upEl, "meta" ) ) != null ) {
						cmd.text = idxTextMap.get( e2.getAttribute( "name" ) );
						cmd.icon = e2.getAttribute( "icon" ).toLowerCase().intern();
					}
					if ( ( e2 = XmlProcessor.getElementByTagName( upEl, "cost" ) ) != null ) {
						if ( !( s = e2.getAttribute( "time" ) ).isEmpty() )
							cmd.costTime = Float.parseFloat( s );
					}
				}
			}
		}
		
	}
	
	/**
	 * Reads the next XML file from the specified input stream and returns its parsed {@link Document}.
	 * 
	 * @param in input stream to read the file from
	 * @param documentBuilder document builder to be used to create the document
	 * @return the content of the read file or <code>null</code> if end of stream is reached
	 * @throws Exception if any exception occurs {@link Exception}
	 */
	private static Document readDocument( final InputStream in, final DocumentBuilder documentBuilder ) throws Exception {
		// First read file size (4 bytes)
		int size = in.read();
		if ( size < 0 ) // End of stream reached
			return null;
		
		// 3 more bytes remaining to assemble the size
		for ( int i = 8; i < 32; i += 8 )
			size |= in.read() << i;
		
		return documentBuilder.parse( new ByteArrayInputStream( Utils.readFully( in, new byte[ size ] ) ) );
	}
	
	
	
	/** Map from unit id to {@link Unit}. */
	private final Map< String, Unit >           idUnitMap       = new HashMap<>();
	
	/** Map from unit index to {@link Unit}. */
	private final Map< Integer, Unit >          idxUnitMap      = new HashMap<>();
	
	/** Map from ability index to {@link Ability}. */
	private final Map< Integer, Ability >       idxAbilityMap   = new HashMap<>();
	
	/** Map from upgrade id to {@link UpgradeCommand}. */
	private final Map< String, UpgradeCommand > idUpgradeCmdMap = new HashMap<>();
	
	
	
	@Override
	public VersionView getMinVersion() {
		return minVersion;
	}
	
	@Override
	public VersionView getMaxVersion() {
		return maxVersion;
	}
	
	@Override
	public Unit getUnit( final String id ) {
		return idUnitMap.get( id );
	}
	
	@Override
	public Unit getUnit( final Integer idx ) {
		return idxUnitMap.get( idx );
	}
	
	@Override
	@SuppressWarnings( "unchecked" )
	public < T extends IAbility > T getAbility( final Integer idx ) {
		return (T) idxAbilityMap.get( idx );
	}
	
	@Override
	public UpgradeCommand getUpgradeCmd( final String id ) {
		return idUpgradeCmdMap.get( id );
	}
	
}
