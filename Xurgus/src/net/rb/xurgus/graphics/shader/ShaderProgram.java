package net.rb.xurgus.graphics.shader;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

/**
 * 
 * @author Richard Bergqvist
 *
 */
public abstract class ShaderProgram {

	private int programID;
	private int vertexShaderID;
	private int fragmentShaderID;
	
	private static FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16);
	
	public ShaderProgram(String vertexFile, String fragmentFile) {
		this.programID = glCreateProgram();
		this.vertexShaderID = loadShader(vertexFile, GL_VERTEX_SHADER);
		this.fragmentShaderID = loadShader(fragmentFile, GL_FRAGMENT_SHADER);
		
		glAttachShader(programID, vertexShaderID);
		glAttachShader(programID, fragmentShaderID);
		bindAttributes();
		glLinkProgram(programID);
		glValidateProgram(programID);
		getAllUniformLocations();
	}
	
	public void start() {
		glUseProgram(programID);
	}
	
	public void stop() {
		glUseProgram(0);
	}
	
	protected abstract void bindAttributes();
	
	protected void bindAttribute(int attribute, String name) {
		glBindAttribLocation(programID, attribute, name);
	}
	
	protected abstract void getAllUniformLocations();
	
	protected int getUniformLocation(String name) {
		return glGetUniformLocation(programID, name);
	}
	
	protected void loadFloat(int location, float value) {
		glUniform1f(location, value);
	}
	
	protected void loadInt(int location, int value) {
		glUniform1i(location, value);
	}
	
	protected void loadBoolean(int location, boolean value) {
		float toLoad = 0;
		
		if (value)
			toLoad = 1;
		
		glUniform1f(location, toLoad);
	}
	
	protected void loadVector3f(int location, Vector3f vector) {
		glUniform3f(location, vector.x, vector.y, vector.z);
	}
	
	protected void loadVector2f(int location, Vector2f vector) {
		glUniform2f(location, vector.x, vector.y);
	}
	
	protected void loadMatrix(int location, Matrix4f matrix) {
		matrix.store(matrixBuffer);
		matrixBuffer.flip();
		glUniformMatrix4(location, false, matrixBuffer);
	}
	
	private static int loadShader(String name, int type) {
		StringBuilder shaderSource = new StringBuilder();
		String extension = "";
		
		try {
			if (type == GL_VERTEX_SHADER)
				extension = ".vs";
			if (type == GL_FRAGMENT_SHADER)
				extension = ".fs";
				
			BufferedReader reader = new BufferedReader(new FileReader("res/shaders/" + name + extension));
			String line;
			
			while ((line = reader.readLine()) != null)
				shaderSource.append(line).append("\n");
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Could not read shader file: " + name);
			System.exit(-1);
		}
		
		int shaderID = glCreateShader(type);
		glShaderSource(shaderID, shaderSource);
		glCompileShader(shaderID);
		if (glGetShaderi(shaderID, GL_COMPILE_STATUS) == GL_FALSE) {
			System.out.println(glGetShaderInfoLog(shaderID, 500));
			System.err.println("Could not compile shader: " + name);
			System.exit(1);
		}
		
		return shaderID;
	}
	
	public void clean() {
		stop();
		glDetachShader(programID, vertexShaderID);
		glDetachShader(programID, fragmentShaderID);
		glDeleteShader(vertexShaderID);
		glDeleteShader(fragmentShaderID);
		glDeleteProgram(programID);
	}
}