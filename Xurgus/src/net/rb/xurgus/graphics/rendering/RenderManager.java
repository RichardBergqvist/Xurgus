package net.rb.xurgus.graphics.rendering;

import static org.lwjgl.opengl.GL11.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Matrix4f;

import net.rb.xurgus.entity.Camera;
import net.rb.xurgus.entity.Entity;
import net.rb.xurgus.entity.Light;
import net.rb.xurgus.graphics.shader.GuiShader;
import net.rb.xurgus.graphics.shader.SkyboxShader;
import net.rb.xurgus.graphics.shader.StaticShader;
import net.rb.xurgus.graphics.shader.TerrainShader;
import net.rb.xurgus.graphics.texture.GuiTexture;
import net.rb.xurgus.model.TexturedModel;
import net.rb.xurgus.resourcemanagement.ResourceLoader;
import net.rb.xurgus.world.terrain.Terrain;

/**
 * 
 * @author Richard Bergqvist
 *
 */
public class RenderManager {
	
	private static final float FOV = 70;
	private static final float NEAR_PLANE = 0.1F;
	private static final float FAR_PLANE = 1000;
	
	// BLUE SKY R: 0.49F, G: 89, B: 0.98F
	// GRAY SKY R: 0.5F, G: 0.5F, B: 0.5F
	private static final float RED = 0.5444F;
	private static final float GREEN = 0.62F;
	private static final float BLUE = 0.69F;
	
	private Matrix4f projectionMatrix;
	
	private StaticShader staticShader = new StaticShader();
	private EntityRenderer entityRenderer;
	
	private TerrainShader terrainShader = new TerrainShader();
	private TerrainRenderer terrainRenderer;
	
	private GuiShader guiShader = new GuiShader();
	private GuiRenderer guiRenderer;
	
	private SkyboxShader skyboxShader = new SkyboxShader();
	private SkyboxRenderer skyboxRenderer;
	
	private Map<TexturedModel, List<Entity>> entities = new HashMap<TexturedModel, List<Entity>>();
	private List<Terrain> terrains = new ArrayList<Terrain>();
	private List<GuiTexture> guis = new ArrayList<GuiTexture>();
	
	public RenderManager(ResourceLoader loader) {
		enableCulling();
		createProjectionMatrix();
		entityRenderer = new EntityRenderer(staticShader, projectionMatrix);
		terrainRenderer = new TerrainRenderer(terrainShader, projectionMatrix);
		guiRenderer = new GuiRenderer(loader, guiShader);
		skyboxRenderer = new SkyboxRenderer(loader, skyboxShader, projectionMatrix);
	}
	
	public static void enableCulling() {
		glEnable(GL_CULL_FACE);
		glCullFace(GL_BACK);
	}
	
	public static void disableCulling() {
		glDisable(GL_CULL_FACE);
	}
	
	public void render(List<Light> lights, Camera camera) {
		prepare();
		staticShader.start();
		staticShader.loadViewMatrix(camera);
		staticShader.loadSkyColor(RED, GREEN, BLUE);
		staticShader.loadLights(lights);
		entityRenderer.render(entities);
		staticShader.stop();
		terrainShader.start();
		terrainShader.loadLights(lights);
		terrainShader.loadViewMatrix(camera);
		terrainShader.loadSkyColor(RED, GREEN, BLUE);
		terrainRenderer.render(terrains);
		terrainShader.stop();
		guiRenderer.render(guis);
		skyboxShader.start();
		skyboxShader.loadViewMatrix(camera);
		skyboxShader.loadFogColor(RED, GREEN, BLUE);
		skyboxRenderer.render();
		skyboxShader.stop();
		
		entities.clear();
		terrains.clear();
		guis.clear();
	}
	
	public void processEntity(Entity entity) {
		TexturedModel model = entity.getModel();
		List<Entity> batch = entities.get(model);
		
		if (batch != null)
			batch.add(entity);
		else  {
			List<Entity> newBatch = new ArrayList<Entity>();
			newBatch.add(entity);
			entities.put(model, newBatch);
		}
	}
	
	public void processTerrain(Terrain terrain) {
		terrains.add(terrain);
	}
	
	public void processGui(GuiTexture gui) {
		guis.add(gui);
	}
	
	public void prepare() {
		glEnable(GL_DEPTH_TEST);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		glClearColor(RED, GREEN, BLUE, 1);
	}
	
	private void createProjectionMatrix() {
		float aspectRatio = (float) Display.getWidth() / (float) Display.getHeight();
		float y_scale = (float) (1 / Math.tan(Math.toRadians(FOV / 2))) * aspectRatio;
		float x_scale = y_scale / aspectRatio;
		float frustum_length = FAR_PLANE - NEAR_PLANE;
		
		projectionMatrix = new Matrix4f();
		projectionMatrix.m00 = x_scale;
		projectionMatrix.m11 = y_scale;
		projectionMatrix.m22 = -((FAR_PLANE + NEAR_PLANE) / frustum_length);
		projectionMatrix.m23 = -1;
		projectionMatrix.m32 = -((2 * NEAR_PLANE * FAR_PLANE) / frustum_length);
		projectionMatrix.m33 = 0;
	}
	
	public void clean() {
		staticShader.clean();
		terrainShader.clean();
		guiShader.clean();
		skyboxShader.clean();
	}
}