package net.rb.xurgus.resourcemanagement;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL14.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import net.rb.xurgus.model.Model;

/**
 * 
 * @author Richard Bergqvist
 *
 */
public class ResourceLoader {

	private List<Integer> vaos = new ArrayList<Integer>();
	private List<Integer> vbos = new ArrayList<Integer>();
	private List<Integer> textures = new ArrayList<Integer>();
	
	public Model loadToVAO(float[] positions, float[] textureCoordinates, float[] normals, int[] indices) {
		int vaoID = createVAO();
		bindIndicesBuffer(indices);
		storeDataInAttributeList(0, 3, positions);
		storeDataInAttributeList(1, 2, textureCoordinates);
		storeDataInAttributeList(2, 3, normals);
		unbindVAO();
		return new Model(vaoID, indices.length);
	}
	
	public Model loadToVAO(float[] positions) {
		int vaoID = createVAO();
		storeDataInAttributeList(0, 2, positions);
		unbindVAO();
		return new Model(vaoID, positions.length / 2);
	}
	
	public int loadTexture(String name, TextureType type) {
		Texture texture = null;
		
		String RESOURCE_LOCATION = "res/textures/";
		String EXTENSION = ".png";
		
		try {
			if (type == TextureType.BLOCK)
				texture = TextureLoader.getTexture("PNG", new FileInputStream(RESOURCE_LOCATION + type.getResourceLocation() + "/" + name + EXTENSION));
			else if (type == TextureType.ITEM)
				texture = TextureLoader.getTexture("PNG", new FileInputStream(RESOURCE_LOCATION + type.getResourceLocation() + "/" + name + EXTENSION));
			else if (type == TextureType.MODEL)
				texture = TextureLoader.getTexture("PNG", new FileInputStream(RESOURCE_LOCATION + type.getResourceLocation() + "/" + name + EXTENSION));
			else if (type == TextureType.LIVING_ENTITY)
				texture = TextureLoader.getTexture("PNG", new FileInputStream(RESOURCE_LOCATION + type.getResourceLocation() + "/" + name + EXTENSION));
			else if (type == TextureType.BLENDMAP)
				texture = TextureLoader.getTexture("PNG", new FileInputStream(RESOURCE_LOCATION + type.getResourceLocation() + "/" + name + EXTENSION));
			else if (type == TextureType.HEIGHTMAP)
				texture = TextureLoader.getTexture("PNG", new FileInputStream(RESOURCE_LOCATION + type.getResourceLocation() + "/" + name + EXTENSION));
			else if (type == TextureType.GUI)
				texture = TextureLoader.getTexture("PNG", new FileInputStream(RESOURCE_LOCATION + type.getResourceLocation() + "/" + name + EXTENSION));
			
			glGenerateMipmap(GL_TEXTURE_2D);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
			glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_LOD_BIAS, -0.4F);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.err.println("Could not find image file: " + name + ".png");
			System.exit(-1);
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Coult not read image file: " + name + ".png");
			System.exit(-1);
		}
		
		int textureID = texture.getTextureID();
		textures.add(textureID);
		return textureID;
	}
	
	private int createVAO() {
		int vaoID = glGenVertexArrays();
		vaos.add(vaoID);
		glBindVertexArray(vaoID);
		return vaoID;
	}
	
	private void unbindVAO() {
		glBindVertexArray(0);		
	}
	
	private void bindIndicesBuffer(int[] indices) {
		int vboID = glGenBuffers();
		vbos.add(vboID);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vboID);
		IntBuffer buffer = storeDataInIntBuffer(indices);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
	}
	
	private void storeDataInAttributeList(int attribute, int size, float[] data) {
		int vboID = glGenBuffers();
		vbos.add(vboID);
		glBindBuffer(GL_ARRAY_BUFFER, vboID);
		FloatBuffer buffer = storeDataInFloatBuffer(data);
		glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
		glVertexAttribPointer(attribute, size, GL_FLOAT, false, 0, 0);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
	}
	
	private IntBuffer storeDataInIntBuffer(int[] data) {
		IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}
	
	private FloatBuffer storeDataInFloatBuffer(float[] data) {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}
	
	public void clean() {
		for (int vao : vaos)
			glDeleteVertexArrays(vao);
		for (int vbo : vbos)
			glDeleteBuffers(vbo);
		for (int texture : textures) {
			glDeleteTextures(texture);
		}
	}
	
	public static enum TextureType {
		BLOCK("blocks"),
		ITEM("items"),
		MODEL("models"),
		LIVING_ENTITY("entities"),
		BLENDMAP("blendmaps"),
		HEIGHTMAP("heightmaps"),
		GUI("gui");
		
		private String resourceLocation;
		
		TextureType(String resourceLocation) {
			this.resourceLocation = resourceLocation;
		}
		
		public String getResourceLocation() {
			return resourceLocation;
		}
	}
}