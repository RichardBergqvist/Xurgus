package net.rb.xurgus;

import org.lwjgl.opengl.Display;

import net.rb.xurgus.graphics.DisplayManager;
import net.rb.xurgus.graphics.RenderManager;
import net.rb.xurgus.graphics.model.ModelLoader;
import net.rb.xurgus.graphics.model.RawModel;
import net.rb.xurgus.graphics.shaders.StaticShader;

/**
 * 
 * @author Richard
 *
 */
public class GameLoop {

	public static void main(String[] args) {		
		DisplayManager.create();
		ModelLoader loader = new ModelLoader();
		RenderManager renderer = new RenderManager();
		StaticShader shader = new StaticShader();
		
		float[] vertices = {
			-0.5F, 0.5F, 0F,
			-0.5F, -0.5F, 0F,
			0.5F, -0.5F, 0F,
			0.5F, 0.5F, 0F
		};
		
		int[] indices = {
			0, 1, 3,
			3, 1, 2
		};
		
		RawModel model = loader.loadToVAO(vertices, indices);
		
		while(!Display.isCloseRequested()) {
			renderer.prepare();
			shader.start();
			renderer.render(model);
			shader.stop();
			DisplayManager.update();
		}
		
		shader.clean();
		loader.clean();
		DisplayManager.close();
	}
}