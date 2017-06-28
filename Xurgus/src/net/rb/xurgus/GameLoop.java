package net.rb.xurgus;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import net.rb.xurgus.entity.Camera;
import net.rb.xurgus.entity.Entity;
import net.rb.xurgus.entity.Light;
import net.rb.xurgus.entity.Player;
import net.rb.xurgus.graphics.DisplayManager;
import net.rb.xurgus.graphics.rendering.RenderManager;
import net.rb.xurgus.graphics.texture.GuiTexture;
import net.rb.xurgus.graphics.texture.ModelTexture;
import net.rb.xurgus.graphics.texture.TerrainTexture;
import net.rb.xurgus.graphics.texture.TerrainTexturePack;
import net.rb.xurgus.model.TexturedModel;
import net.rb.xurgus.resourcemanagement.OBJLoader;
import net.rb.xurgus.resourcemanagement.ResourceLoader;
import net.rb.xurgus.util.Timer;
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
		
		TexturedModel lamp = new TexturedModel(OBJLoader.loadModel("lamp", loader), new ModelTexture(loader.loadTexture("models/lamp")));
		
		TexturedModel grass = new TexturedModel(OBJLoader.loadModel("grass", loader), new ModelTexture(loader.loadTexture("models/grass")));
		TexturedModel flower = new TexturedModel(OBJLoader.loadModel("grass", loader), new ModelTexture(loader.loadTexture("models/pink_flowers")));
		
		ModelTexture bushTextureAtlas = new ModelTexture(loader.loadTexture("models/bushes"));
		bushTextureAtlas.setNumberOfRows(2);
		
		ModelTexture fernTextureAtlas = new ModelTexture(loader.loadTexture("models/ferns"));
		fernTextureAtlas.setNumberOfRows(2);
		
		TexturedModel bush = new TexturedModel(OBJLoader.loadModel("grass", loader), bushTextureAtlas);
		TexturedModel fern = new TexturedModel(OBJLoader.loadModel("fern", loader), fernTextureAtlas);
	
		TexturedModel bobble = new TexturedModel(OBJLoader.loadModel("pine", loader), new ModelTexture(loader.loadTexture("models/pine")));
		bobble.getTexture().setHasTransparency(true);
		
		lamp.getTexture().setUseFakeLighting(true);
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
				
				entities.add(new Entity(bobble, random.nextInt(4), new Vector3f(x, y, z), 0, random.nextFloat() * 360, 0, random.nextFloat() * 0.6F + 0.8F));
			}
		}
		
		List<Light> lights = new ArrayList<Light>();
		lights.add(new Light(new Vector3f(0, 1000, -7000), new Vector3f(0.4F, 0.4F, 0.4F)));
		lights.add(new Light(new Vector3f(185, 10, -293), new Vector3f(2, 0, 0), new Vector3f(1, 0.01F, 0.002F)));
		lights.add(new Light(new Vector3f(370, 17, -300), new Vector3f(0, 2, 2), new Vector3f(1, 0.01F, 0.002F)));
		lights.add(new Light(new Vector3f(293, 7, -305), new Vector3f(2, 2, 0), new Vector3f(1, 0.01F, 0.002F)));
		
		entities.add(new Entity(lamp, new Vector3f(185, -4.7F, -293), 0, 0, 0, 1));
		entities.add(new Entity(lamp, new Vector3f(370, 4.2F, -300), 0, 0, 0, 1));
		entities.add(new Entity(lamp, new Vector3f(293, -6.8F, -305), 0, 0, 0, 1));
	
		RenderManager renderManager = new RenderManager(loader);
		
		TexturedModel playerModel = new TexturedModel(OBJLoader.loadModel("person", loader), new ModelTexture(loader.loadTexture("entity/player")));
	
		Player player = new Player(playerModel, new Vector3f(253, 5, -274), 0, 180, 0, 0.6F);
		Camera camera = new Camera(player);
		
		List<GuiTexture> guis = new ArrayList<GuiTexture>();
		//guis.add(new GuiTexture(loader.loadTexture("gui/health"), new Vector2f(-0.8F, 0.9F), new Vector2f(0.2F, 0.3F)));
		
		while (!Display.isCloseRequested()) {
			player.move(terrain);
			camera.move();
			
			renderManager.processTerrain(terrain);
			renderManager.processEntity(player);
			
			for (Entity entity : entities)
				renderManager.processEntity(entity);
			
			for (GuiTexture gui : guis) {
				renderManager.processGui(gui);				
			}
			
			renderManager.render(lights, camera);
			DisplayManager.update();
			Timer.update();
		}
		
		renderManager.clean();
		loader.clean();
		DisplayManager.close();
	}
}