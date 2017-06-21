package net.rb.xurgus.graphics.model;

/**
 * 
 * @since In-Development 0.1
 * @author Richard Bergqvist
 * @category Graphics
 *
 */
public class RawModel {

	/** The vertex array object ID **/
	private int vaoID;
	/** The number of vertices **/
	private int vertexCount;
	
	/**
	 * 
	 * Creates a new raw model
	 * @since In-Development 0.1
	 * @param vaoID			The vertex array object ID
	 * @param vertexCount	The number of vertices
	 *
	**/
	public RawModel(int vaoID, int vertexCount) {
		this.vaoID = vaoID;
		this.vertexCount = vertexCount;
	}
	
	public int getVaoID() {
		return vaoID;
	}
	
	public int getVertexCount() {
		return vertexCount;
	}
}