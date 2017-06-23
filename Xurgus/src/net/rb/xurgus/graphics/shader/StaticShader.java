package net.rb.xurgus.graphics.shader;

import org.lwjgl.util.vector.Matrix4f;

import net.rb.xurgus.entity.Camera;
import net.rb.xurgus.util.Maths;

/**
 * 
 * @author Richard Bergqvist
 *
 */
public class StaticShader extends ShaderProgram {

	private static final String VERTEX_FILE = "vertexShader";
	private static final String FRAGMENT_FILE = "fragmentShader";
	
	private int transformationMatrix;
	private int viewMatrix;
	private int projectionMatrix;
	
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
		viewMatrix = getUniformLocation("viewMatrix");
		projectionMatrix = getUniformLocation("projectionMatrix");
	}
	
	public void loadTransformationMatrix(Matrix4f matrix) {
		loadMatrix(transformationMatrix, matrix);
	}
	
	public void loadViewMatrix(Camera camera) {
		Matrix4f matrix = Maths.createViewMatrix(camera);
		loadMatrix(viewMatrix, matrix);
	}
	
	public void loadProjectionMatrix(Matrix4f matrix) {
		loadMatrix(projectionMatrix, matrix);
	}
}