package net.rb.xurgus.graphics.texture;

/**
 * 
 * @since In-Development 0.1
 * @author Richard Bergqvist
 * @category Graphics
 *
 */
public class ModelTexture {

	private int textureID;
	
	private float shineDamper = 1;
	private float reflectivity = 0;
	
	public ModelTexture(int textureID) {
		this.textureID = textureID;
	}
	
	public int getID() {
		return textureID;
	}
	
	public void setShineDamper(float shineDamper) {
		this.shineDamper = shineDamper;
	}
	
	public float getShineDamper() {
		return shineDamper;
	}
	
	public void setReflectivity(float reflectivity) {
		this.reflectivity = reflectivity;
	}
	
	public float getReflectivity() {
		return reflectivity;
	}
}