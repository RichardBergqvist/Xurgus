package net.rb.xurgus.world.terrain;

import net.rb.xurgus.graphics.texture.ModelTexture;
import net.rb.xurgus.model.Model;
import net.rb.xurgus.resourcemanagement.ResourceLoader;

/**
 * 
 * @author Richard Bergqvist
 *
 */
public class Terrain {

	private static final float SIZE = 800;
	private static final int VERTEX_COUNT = 128;
	
	private float x;
	private float z;
	private Model model;
	private ModelTexture texture;
	
	public Terrain(int x, int z, ResourceLoader loader, ModelTexture texture) {
		this.x = x * SIZE;
		this.z = z * SIZE;
		this.texture = texture;
		this.model = generateTerrain(loader);
	}
	
	private Model generateTerrain(ResourceLoader loader) {
		int count = VERTEX_COUNT * VERTEX_COUNT;
		float[] vertices = new float[count * 3];
		float[] textureCoordinates = new float[count * 2];
		float[] normals = new float[count * 3];
		int[] indices = new int[6 * (VERTEX_COUNT - 1) * (VERTEX_COUNT - 1)];
		int vertexPointer = 0;
		
		for (int i = 0; i < VERTEX_COUNT; i++) {
			for (int j = 0; j < VERTEX_COUNT; j++) {
				vertices[vertexPointer * 3] = -(float) j / ((float) VERTEX_COUNT - 1) * SIZE;
				vertices[vertexPointer * 3 + 1] = 0;
				vertices[vertexPointer * 3 + 2] = -(float) i / ((float) VERTEX_COUNT - 1) * SIZE;
				textureCoordinates[vertexPointer * 2] = (float) j / ((float) VERTEX_COUNT - 1);
				textureCoordinates[vertexPointer * 2 + 1] = (float) i / ((float) VERTEX_COUNT - 1);
				normals[vertexPointer * 3] = 0;
				normals[vertexPointer * 3 + 1] = 1;
				normals[vertexPointer * 3 + 2] = 0;
				vertexPointer++;
			} 
		}
		
		int pointer = 0;
		
		for (int j = 0; j < VERTEX_COUNT - 1; j++) {
			for (int i = 0; i < VERTEX_COUNT - 1; i++) {
				int topLeft = (j * VERTEX_COUNT) + i;
				int topRight = topLeft + 1;
				int bottomLeft = ((j + 1) * VERTEX_COUNT) + i;
				int bottomRight = bottomLeft + 1;
				indices[pointer++] = topLeft;
				indices[pointer++] = bottomLeft;
				indices[pointer++] = topRight;
				indices[pointer++] = topRight;
				indices[pointer++] = bottomLeft;
				indices[pointer++] = bottomRight;
			}
		}
		
		return loader.loadToVAO(vertices, textureCoordinates, normals, indices);
	}
	
	public float getX() {
		return x;
	}
	
	public float getZ() {
		return z;
	}
	
	public Model getModel() {
		return model;
	}
	
	public ModelTexture getTexture() {
		return texture;
	}
}