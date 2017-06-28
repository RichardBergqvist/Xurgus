package net.rb.xurgus;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import net.rb.xurgus.entity.Camera;
import net.rb.xurgus.entity.Entity;
import net.rb.xurgus.entity.Light;
import net.rb.xurgus.entity.Player;
import net.rb.xurgus.graphics.DisplayManager;
import net.rb.xurgus.graphics.rendering.GuiRenderer;
import net.rb.xurgus.graphics.rendering.RenderManager;
import net.rb.xurgus.graphics.texture.GuiTexture;
import net.rb.xurgus.graphics.texture.ModelTexture;
import net.rb.xurgus.graphics.texture.TerrainTexture;
import net.rb.xurgus.graphics.texture.TerrainTexturePack;
import net.rb.xurgus.model.TexturedModel;
import net.rb.xurgus.resourcemanagement.OBJLoader;
import net.rb.xurgus.resourcemanagement.ResourceLoader;
import net.rb.xurgus.resourcemanagement.ResourceLoader.TextureType;
import net.rb.xurgus.world.terrain.Terrain;

/**
 * 
 * @author Richard Bergqvist
 *
 */
public class GameLoop {

	public static void main(String[] args) {
	
		DisplayManager.create();
		ResourceLoader loader = new ResourceLoader();
		
		// ** TERRAIN TEXTURE PACK STUFF ** \\
		TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("grassy", TextureType.BLOCK));
		TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("mud", TextureType.BLOCK));
		TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("grass_flowers", TextureType.BLOCK));
		TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("path", TextureType.BLOCK));
	
		TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture);
		TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendMap", TextureType.BLENDMAP));
		
		// ******************************** \\
		
		TexturedModel grass = new TexturedModel(OBJLoader.loadModel("grass", loader), new ModelTexture(loader.loadTexture("grass", TextureType.MODEL)));
		TexturedModel flower = new TexturedModel(OBJLoader.loadModel("grass", loader), new ModelTexture(loader.loadTexture("pink_flowers", TextureType.MODEL)));
		
		ModelTexture bushTextureAtlas = new ModelTexture(loader.loadTexture("bushes", TextureType.MODEL));
		bushTextureAtlas.setNumberOfRows(2);
		
		ModelTexture fernTextureAtlas = new ModelTexture(loader.loadTexture("ferns", TextureType.MODEL));
		fernTextureAtlas.setNumberOfRows(2);
		
		TexturedModel bush = new TexturedModel(OBJLoader.loadModel("grass", loader), bushTextureAtlas);
		TexturedModel fern = new TexturedModel(OBJLoader.loadModel("fern", loader), fernTextureAtlas);
	
		TexturedModel bobble = new TexturedModel(OBJLoader.loadModel("pine", loader), new ModelTexture(loader.loadTexture("pine", TextureType.MODEL)));
		bobble.getTexture().setHasTransparency(true);
		
		grass.getTexture().setHasTransparency(true);
		grass.getTexture().setUseFakeLighting(true);
		flower.getTexture().setHasTransparency(true);
		flower.getTexture().setUseFakeLighting(true);
		bush.getTexture().setHasTransparency(true);
		bush.getTexture().setUseFakeLighting(true);
		fern.getTexture().setHasTransparency(true);
		
		Terrain terrain = new Terrain(0, -1, loader, texturePack, blendMap, "heightMap");
		
		List<Entity> entities = new ArrayList<Entity>();
		Random random = new Random(676452);
		for (int i = 0; i < 400; i++) {
			if (i % 3 == 0) {
				float x = random.nextFloat() * 800;
				float z = random.nextFloat() * -600;
				float y = terrain.getHeightOfTerrain(x, z);
				
				entities.add(new Entity(grass, random.nextInt(4), new Vector3f(x, y, z), 0, random.nextFloat() * 360, 0, 1.5F));
				entities.add(new Entity(flower, random.nextInt(4), new Vector3f(x, y, z), 0, random.nextFloat() * 360, 0, 1.5F));
				entities.add(new Entity(bush, random.nextInt(4), new Vector3f(x, y, z), 0, random.nextFloat() * 360, 0, 2.0F));
				entities.add(new Entity(fern, random.nextInt(4), new Vector3f(x, y, z), 0, random.nextFloat() * 360, 0, 0.9F));
			}
			
			if (i % 1 == 0) {
				float x = random.nextFloat() * 800;
				float z = random.nextFloat() * -600;
				float y = terrain.getHeightOfTerrain(x, z);
				
				entities.add(new Entity(bobble, random.nextInt(4), new Vector3f(x, y, z), 0, random.nextFloat() * 360, 0, random.nextFloat() * 0.1f + 0.6f));
			}
		}
		
		Light light = new Light(new Vector3f(0, 10000, -7000), new Vector3f(1, 1, 1));
		List<Light> lights = new ArrayList<Light>();
		lights.add(light);
		lights.add(new Light(new Vector3f(-200, 100, -200), new Vector3f(5, 0, 0)));
		lights.add(new Light(new Vector3f(200, 100, 200), new Vector3f(0, 0, 5)));
		
		RenderManager renderManager = new RenderManager();
		
		TexturedModel playerModel = new TexturedModel(OBJLoader.loadModel("person", loader), new ModelTexture(loader.loadTexture("player", TextureType.LIVING_ENTITY)));
	
		Player player = new Player(playerModel, new Vector3f(100, 5, -150), 0, 180, 0, 0.6F);
		Camera camera = new Camera(player);
		
		List<GuiTexture> guiTextures = new ArrayList<GuiTexture>();
		GuiTexture gui = new GuiTexture(loader.loadTexture("health", TextureType.GUI), new Vector2f(-0.8F, 0.9F), new Vector2f(0.2F, 0.3F));
		guiTextures.add(gui);
		
		GuiRenderer guiRenderer = new GuiRenderer(loader);
		
		while (!Display.isCloseRequested()) {
			player.move(terrain);
			camera.move();
			
			renderManager.processTerrain(terrain);
			renderManager.processEntity(player);
			
			for (Entity entity : entities)
				renderManager.processEntity(entity);
			
			renderManager.render(lights, camera);
			guiRenderer.render(guiTextures);
			DisplayManager.update();
		}
		
		guiRenderer.clean();
		renderManager.clean();
		loader.clean();
		DisplayManager.close();
	}
}