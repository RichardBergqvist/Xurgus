package net.rb.xurgus.graphics.shader;

import java.util.List;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import net.rb.xurgus.entity.Light;

/**
 * 
 * @author Richard Bergqvist
 *
 */
public class NormalMappingShader extends ShaderProgram {

	private static final String VERTEX_FILE = "normalMapVertexShader";
	private static final String FRAGMENT_FILE = "normalMapFragmentShader";
    
    private static final int MAX_LIGHTS = 4;
	
    private int location_transformationMatrix;
    private int location_projectionMatrix;
    private int location_viewMatrix;
    private int location_lightPositionEyeSpace[];
    private int location_lightColour[];
    private int location_attenuation[];
    private int location_shineDamper;
    private int location_reflectivity;
    private int location_skyColour;
    private int location_numberOfRows;
    private int location_offset;
    private int location_plane;
    private int location_modelTexture;
    private int location_normalMap;
    
	public NormalMappingShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void bindAttributes() {
		bindAttribute(0, "position");
        bindAttribute(1, "textureCoordinates");
        bindAttribute(2, "normal");
        bindAttribute(3, "tangent");
	}

	@Override
	protected void getAllUniformLocations() {
		location_transformationMatrix = getUniformLocation("transformationMatrix");
        location_projectionMatrix = getUniformLocation("projectionMatrix");
        location_viewMatrix = getUniformLocation("viewMatrix");
        location_shineDamper = getUniformLocation("shineDamper");
        location_reflectivity = getUniformLocation("reflectivity");
        location_skyColour = getUniformLocation("skyColour");
        location_numberOfRows = getUniformLocation("numberOfRows");
        location_offset = getUniformLocation("offset");
        location_plane = getUniformLocation("plane");
        location_modelTexture = getUniformLocation("modelTexture");
        location_normalMap = getUniformLocation("normalMap");
         
        location_lightPositionEyeSpace = new int[MAX_LIGHTS];
        location_lightColour = new int[MAX_LIGHTS];
        location_attenuation = new int[MAX_LIGHTS];
        for(int i=0;i<MAX_LIGHTS;i++){
            location_lightPositionEyeSpace[i] = getUniformLocation("lightPosition[" + i + "]");
            location_lightColour[i] = getUniformLocation("lightColor[" + i + "]");
            location_attenuation[i] = getUniformLocation("attenuation[" + i + "]");
        }
	}
     
	public void loadClipPlane(Vector4f plane){
        loadVector(location_plane, plane);
    }
     
    public void loadNumberOfRows(int numberOfRows){
        loadFloat(location_numberOfRows, numberOfRows);
    }
     
    public void loadOffset(float x, float y){
        loadVector(location_offset, new Vector2f(x, y));
    }
     
    public void loadSkyColor(float r, float g, float b){
        loadVector(location_skyColour, new Vector3f(r,g,b));
    }
     
    public void loadShineVariables(float damper,float reflectivity){
        loadFloat(location_shineDamper, damper);
        loadFloat(location_reflectivity, reflectivity);
    }
     
    public void loadTransformationMatrix(Matrix4f matrix){
        loadMatrix(location_transformationMatrix, matrix);
    }
     
    public void loadLights(List<Light> lights, Matrix4f matrix){
        for(int i = 0; i < MAX_LIGHTS; i++){
            if(i<lights.size()){
                loadVector(location_lightPositionEyeSpace[i], getEyeSpacePosition(lights.get(i), matrix));
                loadVector(location_lightColour[i], lights.get(i).getColor());
                loadVector(location_attenuation[i], lights.get(i).getAttenuation());
            } else {
                loadVector(location_lightPositionEyeSpace[i], new Vector3f(0, 0, 0));
                loadVector(location_lightColour[i], new Vector3f(0, 0, 0));
                loadVector(location_attenuation[i], new Vector3f(1, 0, 0));
            }
        }
    }
     
    public void loadViewMatrix(Matrix4f matrix){
        loadMatrix(location_viewMatrix, matrix);
    }
     
    public void loadProjectionMatrix(Matrix4f projection){
        loadMatrix(location_projectionMatrix, projection);
    }
    
    public void connectTextureUnits(){
        loadInt(location_modelTexture, 0);
        loadInt(location_normalMap, 1);
    }
     
    private Vector3f getEyeSpacePosition(Light light, Matrix4f matrix){
        Vector3f position = light.getPosition();
        Vector4f eyeSpacePos = new Vector4f(position.x,position.y, position.z, 1f);
        Matrix4f.transform(matrix, eyeSpacePos, eyeSpacePos);
        return new Vector3f(eyeSpacePos);
    }
}