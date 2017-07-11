package net.rb.xurgus.resourcemanagement;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL14.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import de.matthiasmann.twl.utils.PNGDecoder;
import de.matthiasmann.twl.utils.PNGDecoder.Format;
import net.rb.xurgus.graphics.texture.TextureData;
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
	
	public int loadToVAO(float[] positions, float[] textureCoordinates) {
		int vaoID = createVAO();
		storeDataInAttributeList(0, 2, positions);
		storeDataInAttributeList(1, 2, textureCoordinates);
		unbindVAO();
		return vaoID;
	}
	
	public Model loadToVAO(float[] positions, int dimension) {
		int vaoID = createVAO();
		storeDataInAttributeList(0, dimension, positions);
		unbindVAO();
		return new Model(vaoID, positions.length / dimension);
	}
	
	public Model loadToVAO(float[] positions, float[] textureCoordinates, float[] normals, int[] indices) {
		int vaoID = createVAO();
		bindIndicesBuffer(indices);
		storeDataInAttributeList(0, 3, positions);
		storeDataInAttributeList(1, 2, textureCoordinates);
		storeDataInAttributeList(2, 3, normals);
		unbindVAO();
		return new Model(vaoID, indices.length);
	}
	
	public Model loadToVAO(float[] positions, float[] textureCoordinates, float[] normals, float[] tangents, int[] indices) {
		int vaoID = createVAO();
		bindIndicesBuffer(indices);
		storeDataInAttributeList(0, 3, positions);
		storeDataInAttributeList(1, 2, textureCoordinates);
		storeDataInAttributeList(2, 3, normals);
		storeDataInAttributeList(3, 3, tangents);
		unbindVAO();
		return new Model(vaoID, indices.length);
	}
	
	public int loadTexture(String fileName) {
		Texture texture = null;
		
		try {
			texture = TextureLoader.getTexture("PNG", new FileInputStream("res/textures/" + fileName + ".png"));
			
			glGenerateMipmap(GL_TEXTURE_2D);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
			glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_LOD_BIAS, -0.4F);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.err.println("Could not find image file: " + fileName + ".png");
			System.exit(-1);
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Coult not read image file: " + fileName + ".png");
			System.exit(-1);
		}
		
		textures.add(texture.getTextureID());
		return texture.getTextureID();
	}
	
	public int loadCubeMap(String[] textureFiles) {
		int textureID = glGenTextures();
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_CUBE_MAP, textureID);
		
		for (int i = 0; i < textureFiles.length; i++) {
			TextureData data = decodeTextureFile("res/textures/environment/skybox/" + textureFiles[i] + ".png");
			glTexImage2D(GL_TEXTURE_CUBE_MAP_POSITIVE_X + i, 0, GL_RGBA, data.getWidth(), data.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, data.getBuffer());
		}
		
		glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		textures.add(textureID);
		return textureID;
	}
	
	public int loadFont(String name) {
		Texture texture = null;
		
		try {
			texture = TextureLoader.getTexture("PNG", new FileInputStream("res/fonts/" + name + ".png"));
			
			glGenerateMipmap(GL_TEXTURE_2D);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
			glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_LOD_BIAS, 0);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.err.println("Could not find image file: " + name + ".png");
			System.exit(-1);
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Coult not read image file: " + name + ".png");
			System.exit(-1);
		}
		
		textures.add(texture.getTextureID());
		return texture.getTextureID();
	}
	
	private TextureData decodeTextureFile(String name) {
		ByteBuffer buffer = null;
		int width = 0;
		int height = 0;
		try {
			FileInputStream in = new FileInputStream(name);
			PNGDecoder decoder = new PNGDecoder(in);
			width = decoder.getWidth();
			height = decoder.getHeight();
			buffer = ByteBuffer.allocateDirect(4 * width * height);
			decoder.decode(buffer, width * 4, Format.RGBA);
			buffer.flip();
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Could not read image file: " + name);
			System.exit(-1);
		}
		
		return new TextureData(buffer, width, height);
	}
	
	private int createVAO() {
		int vaoID = glGenVertexArrays();
		glBindVertexArray(vaoID);
		vaos.add(vaoID);
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
}