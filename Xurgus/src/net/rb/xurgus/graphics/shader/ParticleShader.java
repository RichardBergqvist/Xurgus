package net.rb.xurgus.graphics.shader;

import org.lwjgl.util.vector.Matrix4f;

/**
 * 
 * @author Richard Bergqvist
 *
 */
public class ParticleShader extends ShaderProgram {

	private static final String VERTEX_FILE = "particleVertexShader";
	private static final String FRAGMENT_FILE = "particleFragmentShader";
	
	private int location_projectionMatrix;
	private int location_numberOfRows;
	
	public ParticleShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void bindAttributes() {
		bindAttribute(0, "position");
		bindAttribute(1, "modelViewMatrix");
		bindAttribute(5, "textureOffsets");
		bindAttribute(6, "blendFactor");
	}

	@Override
	protected void getAllUniformLocations() {
		location_projectionMatrix = getUniformLocation("projectionMatrix");
		location_numberOfRows = getUniformLocation("numberOfRows");
		
	}
	
	public void loadProjectionMatrix(Matrix4f projectionMatrix) {
		loadMatrix(location_projectionMatrix, projectionMatrix);
	}
	
	public void loadNumberOfRows(float numberOfRows) {
		loadFloat(location_numberOfRows, numberOfRows);
	}
}