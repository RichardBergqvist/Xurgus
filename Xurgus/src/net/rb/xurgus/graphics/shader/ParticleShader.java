package net.rb.xurgus.graphics.shader;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;

/**
 * 
 * @author Richard Bergqvist
 *
 */
public class ParticleShader extends ShaderProgram {

	private static final String VERTEX_FILE = "particleVertexShader";
	private static final String FRAGMENT_FILE = "particleFragmentShader";
	
	private int location_projectionMatrix;
	private int location_modelViewMatrix;
	private int location_textureOffset1;
	private int location_textureOffset2;
	private int location_textureCoordinateInfo;
	
	public ParticleShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void bindAttributes() {
		bindAttribute(0, "position");
	}

	@Override
	protected void getAllUniformLocations() {
		location_projectionMatrix = getUniformLocation("projectionMatrix");
		location_modelViewMatrix = getUniformLocation("modelViewMatrix");
		location_textureOffset1 = getUniformLocation("textureOffset1");
		location_textureOffset2 = getUniformLocation("textureOffset2");
		location_textureCoordinateInfo = getUniformLocation("textureCoordinateInfo");
	}
	
	public void loadProjectionMatrix(Matrix4f projectionMatrix) {
		loadMatrix(location_projectionMatrix, projectionMatrix);
	}
	
	public void loadModelViewMatrix(Matrix4f modelViewMatrix) {
		loadMatrix(location_modelViewMatrix, modelViewMatrix);
	}
	
	public void loadTextureCoordinateInfo(Vector2f offset1, Vector2f offset2, float numberOfRows, float blend) {
		loadVector(location_textureOffset1, offset1);
		loadVector(location_textureOffset2, offset2);
		loadVector(location_textureCoordinateInfo, new Vector2f(numberOfRows, blend));
	}
}