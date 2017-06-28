package net.rb.xurgus.graphics.shader;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import net.rb.xurgus.entity.Camera;
import net.rb.xurgus.util.Maths;
import net.rb.xurgus.util.Timer;

/**
 * 
 * @author Richard Bergqvist
 *
 */
public class SkyboxShader extends ShaderProgram {

	private static final String VERTEX_FILE = "skyboxVertexShader";
    private static final String FRAGMENT_FILE = "skyboxFragmentShader";
	
    private static final float ROTATE_SPEED = 1;
    
    private int location_viewMatrix;
    private int location_projectionMatrix;
    private int location_fogColor;
    private int location_cubeMap;
    private int location_cubeMap2;
    private int location_blendFactor;
    
    private float rotation = 0;
    
	public SkyboxShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void bindAttributes() {
		bindAttribute(0, "position");
	}

	@Override
	protected void getAllUniformLocations() {
		location_viewMatrix = getUniformLocation("viewMatrix");
		location_projectionMatrix = getUniformLocation("projectionMatrix");
		location_fogColor = getUniformLocation("fogColor");
		location_cubeMap = getUniformLocation("cubeMap");
		location_cubeMap2 = getUniformLocation("cubeMap2");
		location_blendFactor = getUniformLocation("blendFactor");
	}
	
	public void loadViewMatrix(Camera camera) {
		Matrix4f matrix = Maths.createViewMatrix(camera);
		matrix.m30 = 0;
		matrix.m31 = 0;
		matrix.m32 = 0;
		rotation += ROTATE_SPEED * Timer.getFrameTimeAsSeconds();
		Matrix4f.rotate((float) Math.toRadians(rotation), new Vector3f(0, 1, 0), matrix, matrix);
		loadMatrix(location_viewMatrix, matrix);
	}
	
	public void loadProjectionMatrix(Matrix4f matrix) {
		loadMatrix(location_projectionMatrix, matrix);
	}
	
	public void loadFogColor(float r, float g, float b) {
		loadVector3f(location_fogColor, new Vector3f(r, g, b));
	}
	
	public void loadBlendFactor(float blend) {
		loadFloat(location_blendFactor, blend);
	}
	
	public void connectTextureUnits() {
		loadInt(location_cubeMap, 0);
		loadInt(location_cubeMap2, 1);
	}
}