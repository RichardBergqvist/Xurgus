package net.rb.xurgus;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import net.rb.xurgus.entity.Camera;
import net.rb.xurgus.entity.Entity;
import net.rb.xurgus.entity.Light;
import net.rb.xurgus.graphics.DisplayManager;
import net.rb.xurgus.graphics.rendering.RenderManager;
import net.rb.xurgus.graphics.texture.ModelTexture;
import net.rb.xurgus.model.Model;
import net.rb.xurgus.model.TexturedModel;
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
		
		Model model = OBJLoader.loadModel("tree", loader);
		
		TexturedModel staticModel = new TexturedModel(model, new ModelTexture(loader.loadModelTexture("tree")));
		TexturedModel grass = new TexturedModel(OBJLoader.loadModel("grass", loader), new ModelTexture(loader.loadModelTexture("grass")));
		TexturedModel flower = new TexturedModel(OBJLoader.loadModel("grass", loader), new ModelTexture(loader.loadModelTexture("flower")));
		TexturedModel fern = new TexturedModel(OBJLoader.loadModel("fern", loader), new ModelTexture(loader.loadModelTexture("fern")));
		TexturedModel bobble = new TexturedModel(OBJLoader.loadModel("lowPolyTree", loader), new ModelTexture(loader.loadModelTexture("lowPolyTree")));
		TexturedModel hedge = new TexturedModel(OBJLoader.loadModel("grass", loader), new ModelTexture(loader.loadModelTexture("hedge")));
		
		grass.getTexture().setHasTransparency(true);
		grass.getTexture().setUseFakeLighting(true);
		flower.getTexture().setHasTransparency(true);
		flower.getTexture().setUseFakeLighting(true);
		fern.getTexture().setHasTransparency(true);
		hedge.getTexture().setHasTransparency(true);
		hedge.getTexture().setUseFakeLighting(true);
		
		List<Entity> entities = new ArrayList<Entity>();
		Random random = new Random();
		
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
		}
		
		Light light = new Light(new Vector3f(20000, 40000, 20000), new Vector3f(1, 1, 1));
		
		Terrain terrain = new Terrain(0, 0, loader, new ModelTexture(loader.loadTexture("grass")));
		Terrain terrain2 = new Terrain(1, 0, loader, new ModelTexture(loader.loadTexture("grass")));
		
		Camera camera = new Camera();
		RenderManager renderManager = new RenderManager();
		
		while(!Display.isCloseRequested()) {
			camera.move();
			
			for (Entity entity : entities)
				renderManager.processEntity(entity);
			
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