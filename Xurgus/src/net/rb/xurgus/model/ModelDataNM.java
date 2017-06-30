package net.rb.xurgus.model;

/**
 * 
 * @author Richard Bergqvist
 *
 */
public class ModelDataNM {

	private float[] vertices;
	private float[] textureCoordinates;
	private float[] normals;
	private float[] tangents;
	private int[] indices;
	private float furthestPoint;
	
	public ModelDataNM(float[] vertices, float[] textureCoordinates, float[] normals, float[] tangents, int[] indices, float furthestPoint) {
		this.vertices = vertices;
        this.textureCoordinates = textureCoordinates;
        this.normals = normals;
        this.indices = indices;
        this.furthestPoint = furthestPoint;
        this.tangents = tangents;
	}
	
	public float[] getVertices() {
        return vertices;
    }
 
    public float[] getTextureCoordinates() {
        return textureCoordinates;
    }
     
    public float[] getTangents(){
        return tangents;
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