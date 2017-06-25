package net.rb.xurgus.graphics.shader;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import net.rb.xurgus.entity.Camera;
import net.rb.xurgus.entity.Light;
import net.rb.xurgus.util.Maths;

/**
 * 
 * @author Richard Bergqvist
 *
 */
public class StaticShader extends ShaderProgram {

	private static final String VERTEX_FILE = "vertexShader";
	private static final String FRAGMENT_FILE = "fragmentShader";
	
	private int location_transformationMatrix;
	private int location_viewMatrix;
	private int location_projectionMatrix;
	private int location_lightPosition;
	private int location_lightColor;
	private int location_shineDamper;
	private int location_reflectivity;
	private int location_useFakeLighting;
	private int location_skyColor;
	
	public StaticShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void bindAttributes() {
		bindAttribute(0, "position");
		bindAttribute(1, "textureCoordinates");
		bindAttribute(2, "normal");
	}

	@Override
	protected void getAllUniformLocations() {
		location_transformationMatrix = getUniformLocation("transformationMatrix");
		location_viewMatrix = getUniformLocation("viewMatrix");
		location_projectionMatrix = getUniformLocation("projectionMatrix");
		location_lightPosition = getUniformLocation("lightPosition");
		location_lightColor = getUniformLocation("lightColor");
		location_shineDamper = getUniformLocation("shineDamper");
		location_reflectivity = getUniformLocation("reflectivity");
		location_useFakeLighting = getUniformLocation("useFakeLighting");
		location_skyColor = getUniformLocation("skyColor");
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
	
	public void loadShineVariables(float shineDamper, float reflectivity) {
		loadFloat(location_shineDamper, shineDamper);
		loadFloat(location_reflectivity, reflectivity);
	}
	
	public void loadFakeLighting(boolean useFakeLighting) {
		loadBoolean(location_useFakeLighting, useFakeLighting);
	}
	
	public void loadSkyColor(float r, float g, float b) {
		loadVector(location_skyColor, new Vector3f(r, g, b));
	}
}