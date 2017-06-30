package net.rb.xurgus.tile;

/**
 * 
 * @author Richard Bergqvist
 *
 */
public class WaterTile {

	public static final float TILE_SIZE = 60;
	
	private float x;
	private float y;
	private float z;
	
	public WaterTile(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public float getX() {
		return x;
	}
	
	public float getY() {
		return y;
	}
	
	public float getZ() {
		return z;
	}
}