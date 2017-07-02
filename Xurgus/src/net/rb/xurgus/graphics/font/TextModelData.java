package net.rb.xurgus.graphics.font;

/**
 * 
 * @author Richard Bergqvist
 *
 */
public class TextModelData {

	private float[] vertexPositions;
	private float[] textureCoordinates;
	
	public TextModelData(float[] vertexPositions, float[] textureCoordinates) {
		this.vertexPositions = vertexPositions;
		this.textureCoordinates = textureCoordinates;
	}
	
	public float[] getVertexPositions() {
		return vertexPositions;
	}
	
	public float[] getTextureCoordinates() {
		return textureCoordinates;
	}
	
	public int getVertexCount() {
		return vertexPositions.length / 2;
	}
}