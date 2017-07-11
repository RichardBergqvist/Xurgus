package net.rb.xurgus.graphics.texture;

/**
 * 
 * @author Richard Bergqvist
 *
 */
public class ModelTexture extends Texture {

	private int normalMap;
	
	private float shineDamper = 1;
	private float reflectivity = 0;
	
	private boolean hasTransparency = false;
	private boolean useFakeLighting = false;
	
	private int numberOfRows = 1;
	
	public ModelTexture(int textureID) {
		super(textureID);
	}
	
	public void setNormalMap(int normalMap) {
		this.normalMap = normalMap;
	}
	
	public int getNormalMap() {
		return normalMap;
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
	
	public void setHasTransparency(boolean hasTransparency) {
		this.hasTransparency = hasTransparency;
	}
	
	public boolean getHasTransparency() {
		return hasTransparency;
	}
	
	public void setUseFakeLighting(boolean useFakeLighting) {
		this.useFakeLighting = useFakeLighting;
	}
	
	public boolean getUseFakeLighting() {
		return useFakeLighting;
	}
	
	public void setNumberOfRows(int numberOfRows) {
		this.numberOfRows = numberOfRows;
	}
	
	public int getNumberOfRows() {
		return numberOfRows;
	}
}