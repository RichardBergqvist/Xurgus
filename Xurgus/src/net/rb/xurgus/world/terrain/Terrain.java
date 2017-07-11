package net.rb.xurgus.world.terrain;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import net.rb.xurgus.graphics.texture.TerrainTexture;
import net.rb.xurgus.graphics.texture.TerrainTexturePack;
import net.rb.xurgus.model.Model;
import net.rb.xurgus.resourcemanagement.ResourceLoader;
import net.rb.xurgus.util.Maths;

/**
 * 
 * @author Richard Bergqvist
 *
 */
public class Terrain {

	private static final float SIZE = 150;
	private static final float MAX_HEIGHT = 40;
	private static final float MAX_PIXEL_COLOR = 256 * 256 * 256;
	
	private float x;
	private float z;
	private Model model;
	private TerrainTexturePack texturePack;
	private TerrainTexture blendMap;
	
	private float[][] heights;
	
	public Terrain(int gridX, int gridZ, ResourceLoader loader, TerrainTexturePack texturePack, TerrainTexture blendMap, String heightMap) {
		this.x = gridX * SIZE;
		this.z = gridZ * SIZE;
		this.texturePack = texturePack;
		this.blendMap = blendMap;
		this.model = generate(loader, heightMap);
	}
	
	private Model generate(ResourceLoader loader, String heightMap) {
		
		BufferedImage image = null;
		try {
			image = ImageIO.read(new File("res/textures/misc/" + heightMap + ".png"));
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Could not read height map!");
			System.exit(-1);
		}
		
		int VERTEX_COUNT = image.getHeight();
		heights = new float[VERTEX_COUNT][VERTEX_COUNT];
		
		int count = VERTEX_COUNT * VERTEX_COUNT;
		float[] vertices = new float[count * 3];
		float[] textureCoordinates = new float[count * 2];
		float[] normals = new float[count * 3];
		int[] indices = new int[6 * (VERTEX_COUNT - 1) * (VERTEX_COUNT - 1)];
		int vertexPointer = 0;
		
		for (int i = 0; i < VERTEX_COUNT; i++) {
			for (int j = 0; j < VERTEX_COUNT; j++) {
				vertices[vertexPointer * 3] = (float) j / ((float) VERTEX_COUNT - 1) * SIZE;
				
				float height = getHeight(j, i, image);
				heights[j][i] = height;
				vertices[vertexPointer * 3 + 1] = height;
				vertices[vertexPointer * 3 + 2] = (float) i / ((float) VERTEX_COUNT - 1) * SIZE;	
				textureCoordinates[vertexPointer * 2] = (float) j / ((float) VERTEX_COUNT - 1);
				textureCoordinates[vertexPointer * 2 + 1] = (float) i / ((float) VERTEX_COUNT - 1);
				
				Vector3f normal = calculateNormal(j, i, image);
				normals[vertexPointer * 3] = normal.x;
				normals[vertexPointer * 3 + 1] = normal.y;
				normals[vertexPointer * 3 + 2] = normal.z;
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
	
	private Vector3f calculateNormal(int x, int y, BufferedImage image) {
		float heightL = getHeight(x - 1, y, image);
		float heightR = getHeight(x + 1, y, image);
		float heightD = getHeight(x, y - 1, image);
		float heightU = getHeight(x, y + 1, image);
		
		Vector3f normal = new Vector3f(heightL - heightR, 2, heightD - heightU);
		normal.normalise();
		return normal;
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
	
	public TerrainTexturePack getTexturePack() {
		return texturePack;
	}
	
	public TerrainTexture getBlendMap() {
		return blendMap;
	}
	
	private float getHeight(int x, int y, BufferedImage image) {
		if (x < 0 || x >= image.getHeight() || y < 0 || y >= image.getHeight())
			return 0;
		float height = image.getRGB(x, y);
		height += MAX_PIXEL_COLOR / 2F;
		height /= MAX_PIXEL_COLOR / 2F;
		height *= MAX_HEIGHT;
		return height;
	}
	
	public float getHeightOfTerrain(float worldX, float worldZ) {
		float terrainX = worldX - x;
		float terrainZ = worldZ - z;
		float gridSquareSize = SIZE / ((float) heights.length - 1);
		int gridX = (int) Math.floor(terrainX / gridSquareSize);
		int gridZ = (int) Math.floor(terrainZ / gridSquareSize);
		if (gridX >= heights.length - 1 || gridZ >= heights.length - 1 || gridX < 0 || gridZ < 0)
			return 0;
		
		float xCoordinate = (terrainX % gridSquareSize) / gridSquareSize;
		float zCoordinate = (terrainZ % gridSquareSize) / gridSquareSize;
		float answer;
		if (xCoordinate <= (1-zCoordinate)) {
			answer = Maths.barryCentric(new Vector3f(0, heights[gridX][gridZ], 0), new Vector3f(1, heights[gridX + 1][gridZ], 0), new Vector3f(0, heights[gridX][gridZ + 1], 1), new Vector2f(xCoordinate, zCoordinate));
		} else {
			answer = Maths.barryCentric(new Vector3f(1, heights[gridX + 1][gridZ], 0), new Vector3f(1, heights[gridX + 1][gridZ + 1], 1), new Vector3f(0, heights[gridX][gridZ + 1], 1), new Vector2f(xCoordinate, zCoordinate));
		}
		
		return answer;
	}
}