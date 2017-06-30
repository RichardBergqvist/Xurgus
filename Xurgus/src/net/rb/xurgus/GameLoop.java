package net.rb.xurgus;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import net.rb.xurgus.entity.Camera;
import net.rb.xurgus.entity.Entity;
import net.rb.xurgus.entity.Light;
import net.rb.xurgus.entity.Player;
import net.rb.xurgus.graphics.DisplayManager;
import net.rb.xurgus.graphics.rendering.GuiRenderer;
import net.rb.xurgus.graphics.rendering.RenderManager;
import net.rb.xurgus.graphics.rendering.WaterRenderer;
import net.rb.xurgus.graphics.shader.WaterShader;
import net.rb.xurgus.graphics.texture.GuiTexture;
import net.rb.xurgus.graphics.texture.ModelTexture;
import net.rb.xurgus.graphics.texture.TerrainTexture;
import net.rb.xurgus.graphics.texture.TerrainTexturePack;
import net.rb.xurgus.model.TexturedModel;
import net.rb.xurgus.resourcemanagement.NormalMappedOBJLoader;
import net.rb.xurgus.resourcemanagement.OBJFileLoader;
import net.rb.xurgus.resourcemanagement.ResourceLoader;
import net.rb.xurgus.tile.WaterTile;
import net.rb.xurgus.util.MousePicker;
import net.rb.xurgus.util.Timer;
import net.rb.xurgus.util.WaterFramebuffers;
import net.rb.xurgus.world.terrain.Terrain;

/**
 * 
 * @author Richard Bergqvist
 *
 */
public class GameLoop {

	public static void main(String[] args) {
	
		DisplayManager.create();
		Timer.create();
		ResourceLoader loader = new ResourceLoader();
		
		// ** TERRAIN TEXTURE PACK STUFF ** \\
		TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("blocks/grassy"));
		TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("blocks/mud"));
		TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("blocks/grass_flowers"));
		TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("blocks/path"));
	
		TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture);
		TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("misc/blendMap"));
		
		// ******************************** \\
		
		TexturedModel rocks = new TexturedModel(OBJFileLoader.loadOBJ("rocks", loader), new ModelTexture(loader.loadTexture("models/rocks")));
		
		ModelTexture fernTextureAtlas = new ModelTexture(loader.loadTexture("models/ferns"));
		fernTextureAtlas.setNumberOfRows(2);
		
		TexturedModel fern = new TexturedModel(OBJFileLoader.loadOBJ("fern", loader), fernTextureAtlas);
		fern.getTexture().setHasTransparency(true);
		
		TexturedModel bobble = new TexturedModel(OBJFileLoader.loadOBJ("pine", loader), new ModelTexture(loader.loadTexture("models/pine")));
		bobble.getTexture().setHasTransparency(true);
		
		List<Terrain> terrains = new ArrayList<Terrain>();
		Terrain terrain = new Terrain(0, -1, loader, texturePack, blendMap, "heightmap");
		terrains.add(terrain);
		
		TexturedModel lamp = new TexturedModel(OBJFileLoader.loadOBJ("lamp", loader), new ModelTexture(loader.loadTexture("models/lamp")));
		lamp.getTexture().setUseFakeLighting(true);
		
		List<Entity> entities = new ArrayList<Entity>();
		List<Entity> normalMapEntities = new ArrayList<Entity>();
		
		TexturedModel barrel = new TexturedModel(NormalMappedOBJLoader.loadOBJ("barrel", loader), new ModelTexture(loader.loadTexture("models/barrel")));
		barrel.getTexture().setNormalMap(loader.loadTexture("misc/barrelNormal"));
		barrel.getTexture().setShineDamper(10);
		barrel.getTexture().setReflectivity(0.5F);
		
		TexturedModel crate = new TexturedModel(NormalMappedOBJLoader.loadOBJ("crate", loader), new ModelTexture(loader.loadTexture("models/crate")));
		crate.getTexture().setNormalMap(loader.loadTexture("misc/crateNormal"));
		crate.getTexture().setShineDamper(10);
		crate.getTexture().setReflectivity(0.5F);
		
		TexturedModel boulder = new TexturedModel(NormalMappedOBJLoader.loadOBJ("boulder", loader), new ModelTexture(loader.loadTexture("models/boulder")));
		boulder.getTexture().setNormalMap(loader.loadTexture("misc/boulderNormal"));
		boulder.getTexture().setShineDamper(10);
		boulder.getTexture().setReflectivity(0.5F);
		
		Entity entity = new Entity(barrel, new Vector3f(75, 10, -75), 0, 0, 0, 1);
		Entity entity2 = new Entity(crate, new Vector3f(65, 10, -75), 0, 0, 0, 0.04f);
        Entity entity3 = new Entity(boulder, new Vector3f(85, 10, -75), 0, 0, 0, 1);
        normalMapEntities.add(entity);
        normalMapEntities.add(entity2);
        normalMapEntities.add(entity3);
		
		Random random = new Random(5666778);
		 for (int i = 0; i < 60; i++) {
	            if (i % 3 == 0) {
	                float x = random.nextFloat() * 150;
	                float z = random.nextFloat() * -150;
	                if ((x > 50 && x < 100) || (z < -50 && z > -100)) {
	                } else {
	                    float y = terrain.getHeightOfTerrain(x, z);
	 
	                    entities.add(new Entity(fern, 3, new Vector3f(x, y, z), 0, random.nextFloat() * 360, 0, 0.9f));
	                }
	            }
	            if (i % 2 == 0) {
	 
	                float x = random.nextFloat() * 150;
	                float z = random.nextFloat() * -150;
	                if ((x > 50 && x < 100) || (z < -50 && z > -100)) {
	 
	                } else {
	                    float y = terrain.getHeightOfTerrain(x, z);
	                    entities.add(new Entity(bobble, 1, new Vector3f(x, y, z), 0, random.nextFloat() * 360, 0, random.nextFloat() * 0.6f + 0.8f));
	                }
	            }
	        }
	        entities.add(new Entity(rocks, new Vector3f(75, 4.6f, -75), 0, 0, 0, 75));
		
		List<Light> lights = new ArrayList<Light>();
		Light sun = new Light(new Vector3f(10000, 10000, -10000), new Vector3f(1.3F, 1.3F, 1.3F));
		lights.add(sun);
	
		RenderManager renderManager = new RenderManager(loader);

		TexturedModel playerModel = new TexturedModel(OBJFileLoader.loadOBJ("person", loader), new ModelTexture(loader.loadTexture("entity/player")));
		
		Player player = new Player(playerModel, new Vector3f(75, 5, -75), 0, 100, 0, 0.6F);
		entities.add(player);
		Camera camera = new Camera(player);
		
		MousePicker picker = new MousePicker(camera, renderManager.getProjectionMatrix(), terrain);
		
		List<GuiTexture> guis = new ArrayList<GuiTexture>();
		GuiRenderer guiRenderer = new GuiRenderer(loader);
		
		WaterFramebuffers buffers = new WaterFramebuffers();
		WaterShader waterShader = new WaterShader();
		WaterRenderer waterRenderer = new WaterRenderer(loader, waterShader, renderManager.getProjectionMatrix(), buffers);
		List<WaterTile> waters = new ArrayList<WaterTile>();
		WaterTile water = new WaterTile(75, 0, -75);
		waters.add(water);
		
		while (!Display.isCloseRequested()) {
			player.move(terrain);
			camera.move();
			picker.update();
			entity.increaseRotation(0, 1, 0);
			entity2.increaseRotation(0, 1, 0);
			entity3.increaseRotation(0, 1, 0);
			glEnable(GL_CLIP_DISTANCE0);
			buffers.bindReflectionFramebuffer();
			
			float distance = 2 * (camera.getPosition().y - water.getY());
			camera.getPosition().y -= distance;
			camera.invertPitch();
			renderManager.renderScene(entities, normalMapEntities, terrains, lights, camera, new Vector4f(0, 1, 0, -water.getY() + 1));
			camera.getPosition().y += distance;
			camera.invertPitch();
			
			buffers.bindRefractionFramebuffer();
			renderManager.renderScene(entities, normalMapEntities, terrains, lights, camera, new Vector4f(0, -1, 0, water.getY()));
			
			glDisable(GL_CLIP_DISTANCE0);
			buffers.unbindCurrentFramebuffer();
			renderManager.renderScene(entities, normalMapEntities, terrains, lights, camera, new Vector4f(0, -1, 0, 100000));
			waterRenderer.render(waters, camera, sun);
			guiRenderer.render(guis);
			
			DisplayManager.update();
			Timer.update();
		}
		
		buffers.clean();
		waterShader.clean();
		guiRenderer.clean();
		renderManager.clean();
		loader.clean();
		DisplayManager.close();
	}
}