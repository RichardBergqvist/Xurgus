package net.rb.xurgus;

import org.lwjgl.opengl.Display;

import net.rb.xurgus.graphics.DisplayManager;
import net.rb.xurgus.graphics.RenderManager;
import net.rb.xurgus.graphics.ResourceLoader;
import net.rb.xurgus.graphics.model.RawModel;
import net.rb.xurgus.graphics.model.TexturedModel;
import net.rb.xurgus.graphics.shaders.StaticShader;
import net.rb.xurgus.graphics.texture.ModelTexture;

/**
 * 
 * @author Richard
 *
 */
public class GameLoop {

	public static void main(String[] args) {		
		DisplayManager.create();
		ResourceLoader loader = new ResourceLoader();
		RenderManager renderer = new RenderManager();
		StaticShader shader = new StaticShader();
		
		float[] vertices = {
			-0.5F, 0.5F, 0F,
			-0.5F, -0.5F, 0F,
			0.5F, -0.5F, 0F,
			0.5F, 0.5F, 0F
		};
		
		float[] textureCoords = {
				0, 0,
				0, 1,
				1, 1,
				1, 0
		};
		
		int[] indices = {
			0, 1, 3,
			3, 1, 2
		};
		
		RawModel model = loader.loadToVAO(vertices, textureCoords, indices);
		ModelTexture texture = new ModelTexture(loader.loadTexture("test_texture"));
		TexturedModel texturedModel = new TexturedModel(model, texture);
		
		while(!Display.isCloseRequested()) {
			renderer.prepare();
			shader.start();
			renderer.render(texturedModel);
			shader.stop();
			DisplayManager.update();
		}
		
		shader.clean();
		loader.clean();
		DisplayManager.close();
	}
}