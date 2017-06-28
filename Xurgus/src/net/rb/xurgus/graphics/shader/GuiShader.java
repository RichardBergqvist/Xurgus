package net.rb.xurgus.graphics.shader;

import org.lwjgl.util.vector.Matrix4f;

/**
 * 
 * @author Richard Bergqvist
 *
 */
public class GuiShader extends ShaderProgram {

	private static final String VERTEX_FILE = "guiVertexShader";
	private static final String FRAGMENT_FILE = "guiFragmentShader";
	
	private int location_transformationMatrix;
	
	public GuiShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void bindAttributes() {
		bindAttribute(0, "position");
	}

	@Override
	protected void getAllUniformLocations() {
		location_transformationMatrix = getUniformLocation("transformationMatrix");
	}
	
	public void loadTransformationMatrix(Matrix4f matrix) {
		loadMatrix(location_transformationMatrix, matrix);
	}
}