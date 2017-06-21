package net.rb.xurgus;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import net.rb.xurgus.entity.Camera;
import net.rb.xurgus.entity.Entity;
import net.rb.xurgus.graphics.DisplayManager;
import net.rb.xurgus.graphics.OBJLoader;
import net.rb.xurgus.graphics.RenderManager;
import net.rb.xurgus.graphics.ResourceLoader;
import net.rb.xurgus.graphics.light.Light;
import net.rb.xurgus.graphics.model.RawModel;
import net.rb.xurgus.graphics.model.TexturedModel;
import net.rb.xurgus.graphics.shader.StaticShader;
import net.rb.xurgus.graphics.texture.ModelTexture;

/**
 * 
 * @since In-Development 0.1
 * @author Richard Bergqvist
 * @category Engine
 *
 */
public class GameLoop {

	public static void main(String[] args) {		
		DisplayManager.create();
		ResourceLoader loader = new ResourceLoader();
		StaticShader shader = new StaticShader();
		RenderManager renderer = new RenderManager(shader);
		
		RawModel model = OBJLoader.loadObjModel("dragon", loader);
		
		TexturedModel staticModel = new TexturedModel(model, new ModelTexture(loader.loadTexture("white")));
		ModelTexture texture = staticModel.getTexture();
		texture.setShineDamper(10);
		texture.setReflectivity(1);
		
		Entity entity = new Entity(staticModel, new Vector3f(0, 0, -50), 0, 0, 0, 1);
		
		Light light = new Light(new Vector3f(0, 0, -20), new Vector3f(1, 1, 1));
		
		Camera camera = new Camera();
		
		while(!Display.isCloseRequested()) {
			entity.increaseRotation(0, 1, 0);
			camera.move();
			renderer.prepare();
			shader.start();
			shader.loadLight(light);
			shader.loadViewMatrix(camera);
			renderer.render(entity, shader);
			shader.stop();
			DisplayManager.update();
		}
		
		shader.clean();
		loader.clean();
		DisplayManager.close();
	}
}