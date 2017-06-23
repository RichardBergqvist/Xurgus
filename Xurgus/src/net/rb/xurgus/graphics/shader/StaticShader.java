package net.rb.xurgus.graphics.shader;

import org.lwjgl.util.vector.Matrix4f;

/**
 * 
 * @author Richard Bergqvist
 *
 */
public class StaticShader extends ShaderProgram {

	private static final String VERTEX_FILE = "vertexShader";
	private static final String FRAGMENT_FILE = "fragmentShader";
	
	private int transformationMatrix;
	
	public StaticShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void bindAttributes() {
		bindAttribute(0, "position");
		bindAttribute(1, "textureCoordinates");
	}

	@Override
	protected void getAllUniformLocations() {
		transformationMatrix = getUniformLocation("transformationMatrix");
	}
	
	public void loadTransformationMatrix(Matrix4f matrix) {
		loadMatrix(transformationMatrix, matrix);
	}
}