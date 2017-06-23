package net.rb.xurgus;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import net.rb.xurgus.entity.Entity;
import net.rb.xurgus.graphics.DisplayManager;
import net.rb.xurgus.graphics.RenderManager;
import net.rb.xurgus.graphics.model.Model;
import net.rb.xurgus.graphics.model.TexturedModel;
import net.rb.xurgus.graphics.shader.StaticShader;
import net.rb.xurgus.graphics.texture.ModelTexture;
import net.rb.xurgus.util.ResourceLoader;

/**
 * 
 * @author Richard Bergqvist
 *
 */
public class GameLoop {

	public static void main(String[] args) {
		DisplayManager.create();
		
		ResourceLoader loader = new ResourceLoader();
		RenderManager renderManager = new RenderManager();
		StaticShader shader = new StaticShader();
		
		float[] vertices = {
				-0.5f, 0.5f, 0f,
				-0.5f, -0.5f, 0f,
				0.5f, -0.5f, 0f,
				0.5f, 0.5f, 0f
		};
		
		float[] textureCoordinates = {
				0, 0,
				0, 1,
				1, 1,
				1, 0
		};
		
		int[] indices = {
				0, 1, 3,
				3, 1, 2
		};
		
		Model model = loader.loadToVAO(vertices, textureCoordinates, indices);
		
		TexturedModel staticModel = new TexturedModel(model, new ModelTexture(loader.loadTexture("test")));
		
		Entity entity = new Entity(staticModel, new Vector3f(0, 0, 0), 0, 0, 0, 1);
		
		while(!Display.isCloseRequested()) {
			renderManager.prepare();
			shader.start();
			renderManager.render(entity, shader);
			shader.stop();
			DisplayManager.update();
		}
		
		shader.clean();
		loader.clean();
		DisplayManager.close();
	}
}