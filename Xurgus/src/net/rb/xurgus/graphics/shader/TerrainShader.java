package net.rb.xurgus.graphics.shader;

import java.util.List;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import net.rb.xurgus.entity.Camera;
import net.rb.xurgus.entity.Light;
import net.rb.xurgus.util.Maths;

/**
 * 
 * @author Richard Bergqvist
 *
 */
public class TerrainShader extends ShaderProgram {

	private static final String VERTEX_FILE = "terrainVertexShader";
	private static final String FRAGMENT_FILE = "terrainFragmentShader";
	
	private static final int MAX_LIGHTS = 4;
	
	private int location_transformationMatrix;
	private int location_projectionMatrix;
	private int location_viewMatrix;
	private int location_lightPosition[];
	private int location_lightColor[];
	private int location_attenuation[];
	private int location_shineDamper;
	private int location_reflectivity;
	private int location_skyColor;
	private int location_backgroundTexture;
	private int location_rTexture;
	private int location_gTexture;
	private int location_bTexture;
	private int location_blendMap;
	private int location_clipPlane;
	
	public TerrainShader() {
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
		location_shineDamper = getUniformLocation("shineDamper");
		location_reflectivity = getUniformLocation("reflectivity");
		location_skyColor = getUniformLocation("skyColor");
		location_backgroundTexture = getUniformLocation("backgroundTexture");
		location_rTexture = getUniformLocation("rTexture");
		location_gTexture = getUniformLocation("gTexture");
		location_bTexture = getUniformLocation("bTexture");
		location_blendMap = getUniformLocation("blendMap");
		location_clipPlane = getUniformLocation("clipPlane");
		
		location_lightPosition = new int[MAX_LIGHTS];
		location_lightColor = new int[MAX_LIGHTS];
		location_attenuation = new int[MAX_LIGHTS];
		for (int i = 0; i < MAX_LIGHTS; i++) {
			location_lightPosition[i] = getUniformLocation("lightPosition[" + i + "]");
			location_lightColor[i] = getUniformLocation("lightColor[" + i + "]");
			location_attenuation[i] = getUniformLocation("attenuation[" + i + "]");
		}
	}
	
	public void loadTransformationMatrix(Matrix4f transformationMatrix) {
		loadMatrix(location_transformationMatrix, transformationMatrix);
	}
	
	public void loadViewMatrix(Camera camera) {
		Matrix4f viewMatrix = Maths.createViewMatrix(camera);
		loadMatrix(location_viewMatrix, viewMatrix);
	}
	
	public void loadProjectionMatrix(Matrix4f projectionMatrix) {
		loadMatrix(location_projectionMatrix, projectionMatrix);
	}
	
	public void loadShineVariables(float shineDamper, float reflectivity) {
		loadFloat(location_shineDamper, shineDamper);
		loadFloat(location_reflectivity, reflectivity);
	}
	
	public void loadSkyColor(float r, float g, float b) {
		loadVector(location_skyColor, new Vector3f(r, g, b));
	}
	
	public void loadLights(List<Light> lights) {
		for (int i = 0; i < MAX_LIGHTS; i++) {
			if (i < lights.size()) {
				loadVector(location_lightPosition[i], lights.get(i).getPosition());
				loadVector(location_lightColor[i], lights.get(i).getColor());
				loadVector(location_attenuation[i], lights.get(i).getAttenuation());
			} else {
				loadVector(location_lightPosition[i], new Vector3f(0, 0, 0));
				loadVector(location_lightColor[i], new Vector3f(0, 0, 0));
				loadVector(location_attenuation[i], new Vector3f(1, 0, 0));
			}
		}
	}
	
	public void loadClipPlane(Vector4f clipPlane) {
		loadVector(location_clipPlane, clipPlane);
	}
	
	public void connectTextureUnits() {
		loadInt(location_backgroundTexture, 0);
		loadInt(location_rTexture, 1);
		loadInt(location_gTexture, 2);
		loadInt(location_bTexture, 3);
		loadInt(location_blendMap, 4);
	}
}