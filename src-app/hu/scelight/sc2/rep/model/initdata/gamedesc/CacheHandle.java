/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight.sc2.rep.model.initdata.gamedesc;

import hu.belicza.andras.util.type.XString;
import hu.scelight.service.env.Env;
import hu.scelight.util.Utils;
import hu.scelightapi.sc2.rep.model.initdata.gamedesc.ICacheHandle;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * Cache handle (dependency).
 * 
 * @author Andras Belicza
 */
public class CacheHandle implements ICacheHandle {
	
	/** Dependency type (extension). */
	public final String type;
	
	/** Region code. */
	public final String regionCode;
	
	/** Digest of the dependency content. */
	public final String contentDigest;
	
	/**
	 * Creates a new {@link CacheHandle}.
	 * 
	 * @param value raw value provided by s2protocol implementation
	 */
	public CacheHandle( final XString value ) {
		type = new String( value.array, 0, 4, Env.UTF8 );
		regionCode = Utils.stripOffLeadingZeros( new String( value.array, 4, 4, Env.UTF8 ) );
		contentDigest = Utils.toHexString( value.array, 8, value.array.length - 8 );
	}
	
	@Override
	public String getType() {
		return type;
	}
	
	@Override
	public String getRegionCode() {
		return regionCode;
	}
	
	@Override
	public String getContentDigest() {
		return contentDigest;
	}
	
	@Override
	public Path getRelativeFile() {
		return Paths.get( contentDigest.substring( 0, 2 ), contentDigest.substring( 2, 4 ), getFileName() );
	}
	
	@Override
	public String getFileName() {
		return contentDigest + '.' + type;
	}
	
	@Override
	public String getStandardData() {
		return STANDARD_CH_MAP.get( contentDigest );
	}
	
	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder( contentDigest );
		sb.append( '.' ).append( type ).append( " [" ).append( regionCode ).append( ']' );
		
		final String content = STANDARD_CH_MAP.get( contentDigest );
		if ( content != null )
			sb.append( " (" ).append( content ).append( ')' );
		
		return sb.toString();
	}
	
	
	/** Standard Cache Handles map. Maps from content digest to content itself. */
	public static final Map< String, String > STANDARD_CH_MAP = new HashMap< >();
	
	static {
		final Map< String, String > m = STANDARD_CH_MAP; // Short local name...
		
		m.put( "6de41503baccd05656360b6f027db88169fa1989bb6357b1b215a2547939f5fb", "Standard Data: Core.SC2Mod" );
		m.put( "421c8aa0f3619b652d23a2735dfee812ab644228235e7a797edecfe8b67da30e", "Standard Data: Liberty.SC2Mod" );
		m.put( "5c673e6cd2f1bf6e068fa59e2f9421f5debb91cb516aca3237d3b05fe7c7e9fa", "Standard Data: LibertyMulti.SC2Mod" );
		m.put( "29198eca59d0f326f06c90c106348469415c08f9bd76da8413a7f9cd3bde8694", "Standard Data: Liberty.SC2Campaign" );
		m.put( "1767383aa0f5b2eec7a1ec0eec8c98f10377fe2104c38a7e4fce44555aac07c7", "Standard Data: LibertyStory.SC2Campaign" );
		m.put( "66093832128453efffbb787c80b7d3eec1ad81bde55c83c930dea79c4e505a04", "Standard Data: Swarm.SC2Mod" );
		m.put( "881585946366c3c9d1499f38aba954216d3213de69554b9bee6b07311fb38336", "Standard Data: SwarmMulti.SC2Mod" );
		m.put( "d92dfc48c484c59154270b924ad7d57484f2ab9a47621c7ab16431bf66c53b40", "Standard Data: Void.SC2Mod" );
		m.put( "af23fed12efa6c496166dcf9441f802f33ab15172a87133dfae41ed603e3de16", "Standard Data: VoidMulti.SC2Mod" );
		
		m.put( "d2b6f3851f19812ab614544137b896bb046c6a278e75d196604d6fdbbc69f42a", "Standard Data: Teams01.SC2Mod" );
		m.put( "7f41411aa597f4b46440d42a563348bf53822d2a68112f0104f9b891f6f05ae1", "Standard Data: Teams02.SC2Mod" );
		m.put( "6062b70f1485cf2320631d0f32a649c6b24af534ce087166d07cb7c4babdc92a", "Standard Data: Teams03.SC2Mod" );
		m.put( "658e520aa5deb48866dc2b21b023daa9a291be4cf22fd9d785ca67f178132a87", "Standard Data: Teams04.SC2Mod" );
		m.put( "bdf8a39d80f9d26947251efa9f29a4f5b98f6a190651f03051c7f11857d99b4c", "Standard Data: Teams05.SC2Mod" );
		m.put( "b6ccab9e1dca6e10b65a4cf87569ace66c5743dd42cf30113f2b83c59ce7f1a2", "Standard Data: Teams06.SC2Mod" );
		m.put( "c870fdaaf8f381a907f2ae8b195c4a472875428daab03145d3c678f62dd5f1b3", "Standard Data: Teams07.SC2Mod" );
		m.put( "26b1b27647947a0f05ffe9a64f089b487a052de985a17310bea2041832a3dd85", "Standard Data: Teams08.SC2Mod" );
		m.put( "0305203b64d6d35c80bf58030b0f497555cf7e31849726fd800853bb602415f3", "Standard Data: Teams09.SC2Mod" );
		m.put( "b1c834f48b618b17caae9d1d174625bab89b84da581d94ef6ce7f5a6e8344802", "Standard Data: Teams10.SC2Mod" );
		m.put( "8af9900ddeb1416a2619460124603198ae5bceda6387e0374f216a54955982a0", "Standard Data: Teams11.SC2Mod" );
		m.put( "eaceeb172ee73b9650789d2fee249bac54ecee8f2b2204980929d69aeb135a44", "Standard Data: Teams12.SC2Mod" );
		m.put( "e233a7b9e0e1ce10d2cba194fef783927df6ac128c5f73db881d64201e9ead0b", "Standard Data: Teams13.SC2Mod" );
		m.put( "0e639dfeb6bbe18f5a859b5059dd6e296a7a19d1c902f538c250545fc7dd5658", "Standard Data: Teams14.SC2Mod" );
		m.put( "1f720f0a950a29e6a77bddd4d3e4986faef7c6773066f61e9e4688242ec2538a", "Standard Data: Teams15.SC2Mod" );
		m.put( "d50705d5859b6c52aead440f2a0bcedbfd811f06b259cc1733e5cefdf38aed82", "Standard Data: Teams16.SC2Mod" );
	}
	
}
