package net.rb.xurgus.graphics.rendering;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import java.util.List;
import java.util.Map;

import org.lwjgl.util.vector.Matrix4f;

import net.rb.xurgus.entity.Entity;
import net.rb.xurgus.graphics.shader.ShadowShader;
import net.rb.xurgus.model.Model;
import net.rb.xurgus.model.TexturedModel;
import net.rb.xurgus.util.Maths;

/**
 * 
 * @author Richard Bergqvist
 *
 */
public class ShadowMapEntityRenderer {

	private ShadowShader shader;
	private Matrix4f projectionMatrix;
	
	public ShadowMapEntityRenderer(ShadowShader shader, Matrix4f projectionMatrix) {
		this.shader = shader;
		this.projectionMatrix = projectionMatrix;
	}
	
	public void render(Map<TexturedModel, List<Entity>> entities) {
		for (TexturedModel model : entities.keySet()) {
			Model model1 = model.getModel();
			bind(model1);
			for (Entity entity : entities.get(model)) {
				loadTransformationMatrix(entity);
				glDrawElements(GL_TRIANGLES, model1.getVertexCount(), GL_UNSIGNED_INT, 0);
			}
		}
		
		unbind();
	}
	
	private void bind(Model model) {
		glBindVertexArray(model.getVaoID());
		glEnableVertexAttribArray(0);
	}
	
	private void unbind() {
		glDisableVertexAttribArray(0);
		glBindVertexArray(0);
	}
	
	private void loadTransformationMatrix(Entity entity) {
		Matrix4f modelMatrix = Maths.createTransformationMatrix(entity.getPosition(), entity.getRotX(), entity.getRotY(), entity.getRotZ(), entity.getScale());
		Matrix4f mvpMatrix = Matrix4f.mul(projectionMatrix, modelMatrix, null);
		shader.loadMvpMatrix(mvpMatrix);
	}
}