package net.rb.xurgus.model;

/**
 * 
 * @author Richard Bergqvist
 *
 */
public class ModelData {

	private float[] vertices;
	private float[] textureCoordinates;
	private float[] normals;
	private int[] indices;
	private float furthestPoint;
	
	public ModelData(float[] vertices, float[] textureCoordinates, float[] normals, int[] indices, float furthestPoint) {
		this.vertices = vertices;
		this.textureCoordinates = textureCoordinates;
		this.normals = normals;
		this.indices = indices;
		this.furthestPoint = furthestPoint;
	}
	
	public float[] getVertices() {
		return vertices;
	}
	
	public float[] getTextureCoordinates() {
		return textureCoordinates;
	}
	
	public float[] getNormals() {
		return normals;
	}
	
	public int[] getIndices() {
		return indices;
	}
	
	public float getFurthestPoint() {
		return furthestPoint;
	}
}