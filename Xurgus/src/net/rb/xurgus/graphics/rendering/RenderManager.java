package net.rb.xurgus.graphics.rendering;

import static org.lwjgl.opengl.GL11.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector4f;

import net.rb.xurgus.entity.Camera;
import net.rb.xurgus.entity.Entity;
import net.rb.xurgus.entity.Light;
import net.rb.xurgus.graphics.shader.StaticShader;
import net.rb.xurgus.graphics.shader.TerrainShader;
import net.rb.xurgus.model.TexturedModel;
import net.rb.xurgus.resourcemanagement.ResourceLoader;
import net.rb.xurgus.world.terrain.Terrain;

/**
 * 
 * @author Richard Bergqvist
 *
 */
public class RenderManager {
	
	public static final float FOV = 70;
	public static final float NEAR_PLANE = 0.1F;
	public static final float FAR_PLANE = 1000;
	
	public static final float RED = 0.5444F;
	public static final float GREEN = 0.62F;
	public static final float BLUE = 0.69F;
	
	private Matrix4f projectionMatrix;
	
	private StaticShader entityShader = new StaticShader();
	private EntityRenderer entityRenderer;
	
	private TerrainShader terrainShader = new TerrainShader();
	private TerrainRenderer terrainRenderer;

	private SkyboxRenderer skyboxRenderer;
	
	private NormalMappingRenderer normalMapRenderer;
	
	private Map<TexturedModel, List<Entity>> entities = new HashMap<TexturedModel, List<Entity>>();
	private Map<TexturedModel, List<Entity>> normalMapEntities = new HashMap<TexturedModel, List<Entity>>();
	private List<Terrain> terrains = new ArrayList<Terrain>();
	
	public RenderManager(ResourceLoader loader) {
		enableCulling();
		createProjectionMatrix();
		entityRenderer = new EntityRenderer(entityShader, projectionMatrix);
		terrainRenderer = new TerrainRenderer(terrainShader, projectionMatrix);
		skyboxRenderer = new SkyboxRenderer(loader, projectionMatrix);
		normalMapRenderer = new NormalMappingRenderer(projectionMatrix);
		ParticleRenderManager.initialize(loader, projectionMatrix);
	}
	
	public void renderScene(List<Entity> entities, List<Entity> normalEntities, List<Terrain> terrains, List<Light> lights, Camera camera, Vector4f clipPlane) {
		for (Entity entity : entities)
			addEntity(entity);
		
		for (Entity entity : normalEntities)
			addNormalMapEntity(entity);
		
		for (Terrain terrain : terrains)
			addTerrain(terrain);

		render(lights, camera, clipPlane);
		update(camera);
	}
	
	public void render(List<Light> lights, Camera camera, Vector4f clipPlane) {
		prepare();
		entityShader.start();
		entityShader.loadViewMatrix(camera);
		entityShader.loadSkyColor(RED, GREEN, BLUE);
		entityShader.loadLights(lights);
		entityShader.loadClipPlane(clipPlane);
		entityRenderer.render(entities);
		entityShader.stop();
		normalMapRenderer.render(normalMapEntities, clipPlane, lights, camera);
		terrainShader.start();
		terrainShader.loadViewMatrix(camera);
		terrainShader.loadSkyColor(RED, GREEN, BLUE);
		terrainShader.loadLights(lights);
		terrainShader.loadClipPlane(clipPlane);
		terrainRenderer.render(terrains);
		terrainShader.stop();
		skyboxRenderer.render(camera, RED, GREEN, BLUE);
		ParticleRenderManager.render(camera);
		
		entities.clear();
		normalMapEntities.clear();
		terrains.clear();
	}
	
	public void update(Camera camera) {
		ParticleRenderManager.update(camera);
	}
	
	public void addEntity(Entity entity) {
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
	
	public void addNormalMapEntity(Entity entity) {
		TexturedModel model = entity.getModel();
		List<Entity> batch = normalMapEntities.get(model);
		if (batch != null)
			batch.add(entity);
		else {
			List<Entity> newBatch = new ArrayList<Entity>();
			newBatch.add(entity);
			normalMapEntities.put(model, newBatch);
		}
	}
	
	public void addTerrain(Terrain terrain) {
		terrains.add(terrain);
	}
	
	public void prepare() {
		glEnable(GL_DEPTH_TEST);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		glClearColor(RED, GREEN, BLUE, 1);
	}
	
	public static void enableCulling() {
		glEnable(GL_CULL_FACE);
		glCullFace(GL_BACK);
	}
	
	public static void disableCulling() {
		glDisable(GL_CULL_FACE);
	}
	
	private void createProjectionMatrix() {
		projectionMatrix = new Matrix4f();
		float aspectRatio = (float) Display.getWidth() / (float) Display.getHeight();
		float y_scale = (float) ((1 / Math.tan(Math.toRadians(FOV / 2))));
		float x_scale = y_scale / aspectRatio;
		float frustum_length = FAR_PLANE - NEAR_PLANE;
		
		projectionMatrix.m00 = x_scale;
		projectionMatrix.m11 = y_scale;
		projectionMatrix.m22 = -((FAR_PLANE + NEAR_PLANE) / frustum_length);
		projectionMatrix.m23 = -1;
		projectionMatrix.m32 = -((2 * NEAR_PLANE * FAR_PLANE) / frustum_length);
		projectionMatrix.m33 = 0;
	}
	
	public void clean() {
		entityShader.clean();
		terrainShader.clean();
		normalMapRenderer.clean();
		ParticleRenderManager.clean();
	}
	
	public Matrix4f getProjectionMatrix() {
		return projectionMatrix;
	}
}