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
import net.rb.xurgus.graphics.texture.ModelTexture;
import net.rb.xurgus.graphics.texture.TerrainTexture;
import net.rb.xurgus.graphics.texture.TerrainTexturePack;
import net.rb.xurgus.model.Model;
import net.rb.xurgus.model.ModelData;
import net.rb.xurgus.model.TexturedModel;
import net.rb.xurgus.resourcemanagement.OBJFileLoader;
import net.rb.xurgus.resourcemanagement.OBJLoader;
import net.rb.xurgus.resourcemanagement.ResourceLoader;
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
		
		//** TERRAIN TEXTURE PACK**\\
		
		TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("grassy"));
		TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("mud"));
		TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("grassFlowers"));
		TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("path"));
		
		TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture);
		TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendMap"));
		
		//**************************\\
		
		ModelData crateModelData = OBJFileLoader.loadOBJ("box");
		Model crateModel = loader.loadToVAO(crateModelData.getVertices(), crateModelData.getTextureCoordinates(), crateModelData.getNormals(), crateModelData.getIndices());
		Model model = OBJLoader.loadModel("tree", loader);
		
		TexturedModel staticModel = new TexturedModel(model, new ModelTexture(loader.loadModelTexture("tree")));
		TexturedModel grass = new TexturedModel(OBJLoader.loadModel("grass", loader), new ModelTexture(loader.loadModelTexture("grass")));
		TexturedModel flower = new TexturedModel(OBJLoader.loadModel("grass", loader), new ModelTexture(loader.loadModelTexture("flower")));
		TexturedModel fern = new TexturedModel(OBJLoader.loadModel("fern", loader), new ModelTexture(loader.loadModelTexture("fern")));
		TexturedModel bobble = new TexturedModel(OBJLoader.loadModel("lowPolyTree", loader), new ModelTexture(loader.loadModelTexture("lowPolyTree")));
		TexturedModel hedge = new TexturedModel(OBJLoader.loadModel("grass", loader), new ModelTexture(loader.loadModelTexture("hedge")));
		TexturedModel crate = new TexturedModel(crateModel, new ModelTexture(loader.loadModelTexture("box")));
		
		grass.getTexture().setHasTransparency(true);
		grass.getTexture().setUseFakeLighting(true);
		flower.getTexture().setHasTransparency(true);
		flower.getTexture().setUseFakeLighting(true);
		fern.getTexture().setHasTransparency(true);
		hedge.getTexture().setHasTransparency(true);
		hedge.getTexture().setUseFakeLighting(true);
		
		List<Entity> entities = new ArrayList<Entity>();
		Random random = new Random(676452);
		
		for (int i = 0; i < 400; i++) {
			if (i % 7 == 0) {
				entities.add(new Entity(grass, new Vector3f(random.nextFloat() * 400 - 200, 0, random.nextFloat() * -400), 0, 0, 0, 1.8F));
				entities.add(new Entity(flower, new Vector3f(random.nextFloat() * 400 - 200, 0, random.nextFloat() * -400), 0, 0, 0, 2.3F));
				entities.add(new Entity(hedge, new Vector3f(random.nextFloat() * 400 - 200, 0, random.nextFloat() * -400), 0, 0, 0, 1.8F));
			}
			
			if (i % 3 == 0) {
				entities.add(new Entity(fern, new Vector3f(random.nextFloat() * 400 - 200, 0, random.nextFloat() * -400), 0, random.nextFloat() * 360, 0, 0.9F));
				entities.add(new Entity(bobble, new Vector3f(random.nextFloat() * 800 - 400, 0, random.nextFloat() * -600), 0, random.nextFloat() * 360, 0, random.nextFloat() * 0.1F + 0.6F));
				entities.add(new Entity(staticModel, new Vector3f(random.nextFloat() * 800 - 400, 0, random.nextFloat() * -600), 0, 0, 0, random.nextFloat() * 1 + 4));
			}
			
			entities.add(new Entity(crate, new Vector3f(100, 10, -50), 0, 0, 0, 4));
		}
		
		Light light = new Light(new Vector3f(20000, 40000, 20000), new Vector3f(1, 1, 1));
		
		Terrain terrain = new Terrain(0, 0, loader, texturePack, blendMap);
		Terrain terrain2 = new Terrain(1, 0, loader, texturePack, blendMap);
		
		RenderManager renderManager = new RenderManager();
		
		Model playerModel = OBJLoader.loadModel("person", loader);
		TexturedModel playerA = new TexturedModel(playerModel, new ModelTexture(loader.loadModelTexture("player")));
		
		Player player = new Player(playerA, new Vector3f(100, 0, -50), 0, 0, 0, 1);
		Camera camera = new Camera(player);
		
		while(!Display.isCloseRequested()) {
			camera.move();
			player.move();
			
			for (Entity entity : entities)
				renderManager.processEntity(entity);
			renderManager.processEntity(player);
			
			renderManager.processTerrain(terrain);
			renderManager.processTerrain(terrain2);
			
			renderManager.render(light, camera);
			DisplayManager.update();
		}
		
		renderManager.clean();
		loader.clean();
		DisplayManager.close();
	}
}