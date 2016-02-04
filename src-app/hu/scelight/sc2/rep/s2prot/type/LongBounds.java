package hu.scelight.sc2.rep.s2prot.type;

/**
 * Bounds for reading an <code>long</code> number.
 * 
 * <p>
 * Instances are IMMUTABLE.
 * </p>
 * 
 * @author Andras Belicza
 */
public class LongBounds {
	
	/** Offset to add to the read value. */
	public final long offset;
	
	/** Number of bits to read. */
	public final int  bits;
	
	/**
	 * Creates a new {@link LongBounds}.
	 * 
	 * @param offset offset to add to the read value
	 * @param bits number of bits to read
	 */
	public LongBounds( final long offset, final int bits ) {
		this.offset = offset;
		this.bits = bits;
	}
	
}
