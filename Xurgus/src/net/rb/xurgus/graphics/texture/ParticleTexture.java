package net.rb.xurgus.graphics.texture;

/**
 * 
 * @author Richard Bergqvist
 *
 */
public class ParticleTexture extends Texture {

	private int numberOfRows;
	private boolean additive;
	
	public ParticleTexture(int textureID, int numberOfRows, boolean additive) {
		super(textureID);
		this.numberOfRows = numberOfRows;
		this.additive = additive;
	}
	
	public int getNumberOfRows() {
		return numberOfRows;
	}
	
	public boolean isAdditive() {
		return additive;
	}
}