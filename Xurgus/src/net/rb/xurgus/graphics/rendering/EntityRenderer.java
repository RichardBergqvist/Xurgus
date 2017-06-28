package net.rb.xurgus.graphics.rendering;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import java.util.List;
import java.util.Map;

import org.lwjgl.util.vector.Matrix4f;

import net.rb.xurgus.entity.Entity;
import net.rb.xurgus.graphics.shader.StaticShader;
import net.rb.xurgus.graphics.texture.ModelTexture;
import net.rb.xurgus.model.Model;
import net.rb.xurgus.model.TexturedModel;
import net.rb.xurgus.util.Maths;

/**
 * 
 * @author Richard Bergqvist
 *
 */
public class EntityRenderer {
	
	private StaticShader shader;
	
	public EntityRenderer(StaticShader shader, Matrix4f projectionMatrix) {
		this.shader = shader;
		this.shader.start();
		this.shader.loadProjectionMatrix(projectionMatrix);
		this.shader.stop();
	}
	
	public void render(Map<TexturedModel, List<Entity>> entities) {
		for (TexturedModel model : entities.keySet()) {
			prepareTexturedModel(model);
			
			List<Entity> batch = entities.get(model);
			for (Entity entity : batch) {
				loadTransformationMatrix(entity);
				glDrawElements(GL_TRIANGLES, model.getModel().getVertexCount(), GL_UNSIGNED_INT, 0);
			}
			
			unbindTexturedModel();
		}
	}
	
	private void prepareTexturedModel(TexturedModel model) {
		Model model1 = model.getModel();
		glBindVertexArray(model1.getVaoID());
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		glEnableVertexAttribArray(2);
		
		ModelTexture texture = model.getTexture();
		
		if (texture.getHasTransparency())
			RenderManager.disableCulling();
		shader.loadShineVariables(texture.getShineDamper(), texture.getReflectivity());
		shader.loadFakeLighting(texture.getUseFakeLighting());
		shader.loadNumberOfRows(texture.getNumberOfRows());
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, model.getTexture().getTextureID());
	}
	
	private void unbindTexturedModel() {
		RenderManager.enableCulling();
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		glDisableVertexAttribArray(2);
		glBindVertexArray(0);
	}
	
	private void loadTransformationMatrix(Entity entity) {
		Matrix4f transformationMatrix = Maths.createTransformationMatrix(entity.getPosition(), entity.getRotX(), entity.getRotY(), entity.getRotZ(), entity.getScale());
		shader.loadTransformationMatrix(transformationMatrix);
		shader.loadOffset(entity.getTextureXOffset(), entity.getTextureYOffset());
	}
}