package net.rb.xurgus.graphics.shader;

import org.lwjgl.util.vector.Matrix4f;

import net.rb.xurgus.entity.Camera;
import net.rb.xurgus.graphics.light.Light;
import net.rb.xurgus.util.Maths;

/**
 * 
 * @since In-Development 0.1
 * @author Richard Bergqvist
 * @category Graphics
 *
 */
public class StaticShader extends ShaderProgram {

	private static final String VERTEX_FILE = "res/shaders/vertexShader.vs";
	private static final String FRAGMENT_FILE = "res/shaders/fragmentShader.fs";
	
	private int location_transformationMatrix;
	private int location_viewMatrix;
	private int location_projectionMatrix;
	private int location_lightPosition;
	private int location_lightColor;
	private int location_shineDamper;
	private int location_reflectivity;
	
	public StaticShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}
	
	@Override
	protected void getAllUniformLocations() {
		location_transformationMatrix = getUniformLocation("transformationMatrix");
		location_projectionMatrix = getUniformLocation("projectionMatrix");
		location_viewMatrix = getUniformLocation("viewMatrix");
		location_lightPosition = getUniformLocation("lightPosition");
		location_lightColor = getUniformLocation("lightColor");
		location_shineDamper = getUniformLocation("shineDamper");
		location_reflectivity = getUniformLocation("reflectivity");
	}

	@Override
	protected void bindAttributes() {
		bindAttribute(0, "position");
		bindAttribute(1, "textureCoords");
		bindAttribute(2, "normal");
	}
	
	public void loadTransformationMatrix(Matrix4f matrix) {
		loadMatrix(location_transformationMatrix, matrix);
	}
	
	public void loadViewMatrix(Camera camera) {
		Matrix4f matrix = Maths.createViewMatrix(camera);
		loadMatrix(location_viewMatrix, matrix);
	}
	
	public void loadProjectionMatrix(Matrix4f matrix) {
		loadMatrix(location_projectionMatrix, matrix);
	}
	
	public void loadLight(Light light) {
		loadVector(location_lightPosition, light.getPosition());
		loadVector(location_lightColor, light.getColor());
	}
	
	public void loadShineVariables(float damper, float reflectivity) {
		loadFloat(location_shineDamper, damper);
		loadFloat(location_reflectivity, reflectivity);
	}
}