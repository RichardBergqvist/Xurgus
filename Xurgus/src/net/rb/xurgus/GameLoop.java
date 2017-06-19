package net.rb.xurgus;

import org.lwjgl.opengl.Display;

import net.rb.xurgus.graphics.DisplayManager;
import net.rb.xurgus.graphics.RenderManager;
import net.rb.xurgus.graphics.model.ModelLoader;
import net.rb.xurgus.graphics.model.RawModel;

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
		
		float[] vertices = {
			-0.5F, 0.5F, 0F,
			-0.5F, -0.5F, 0F,
			0.5F, -0.5F, 0F,
			0.5F, -0.5F, 0F,
			0.5F, 0.5F, 0F,
			-0.5F, 0.5F, 0F
		};
		
		RawModel model = loader.loadToVAO(vertices);
		
		while(!Display.isCloseRequested()) {
			renderer.prepare();
			renderer.render(model);
			DisplayManager.update();
		}
		
		loader.clean();
		DisplayManager.close();
	}
}