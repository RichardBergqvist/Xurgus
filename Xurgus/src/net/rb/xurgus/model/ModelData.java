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
	private float[] tangents;
	private int[] indices;
	private float furthestPoint;
	
	public ModelData(float[] vertices, float[] textureCoordinates, float[] normals, float[] tangents, int[] indices, float furthestPoint) {
		this.vertices = vertices;
		this.textureCoordinates = textureCoordinates;
		this.normals = normals;
		this.tangents = tangents;
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
	
	public float[] getTangents() {
		return tangents;
	}
	
	public int[] getIndices() {
		return indices;
	}
	
	public float getFurthestPoint() {
		return furthestPoint;
	}
}