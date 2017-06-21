package net.rb.xurgus.graphics;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Matrix4f;

import net.rb.xurgus.entity.Entity;
import net.rb.xurgus.graphics.model.RawModel;
import net.rb.xurgus.graphics.model.TexturedModel;
import net.rb.xurgus.graphics.shader.StaticShader;
import net.rb.xurgus.graphics.texture.ModelTexture;
import net.rb.xurgus.util.Maths;

/**
 * 
 * @since In-Development 0.1
 * @author Richard Bergqvist
 * @category Graphics
 *
 */
public class RenderManager {

	private static final float FOV = 70;
	private static final float NEAR_PLANE = 0.1F;
	private static final float FAR_PLANE = 1000;
	
	private Matrix4f projectionMatrix;
	
	public RenderManager(StaticShader shader) {
		createProjectionMatrix();
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
	}
	
	public void prepare() {
		glEnable(GL_DEPTH_TEST);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		glClearColor(1, 0, 0, 1);
	}
	
	public void render(Entity entity, StaticShader shader) {
		TexturedModel model = entity.getModel();
		RawModel rawModel = model.getModel();
		glBindVertexArray(rawModel.getVaoID());
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		glEnableVertexAttribArray(2);
		Matrix4f transformationMatrix = Maths.createTransformationMatrix(entity.getPosition(), entity.getRotX(), entity.getRotY(), entity.getRotZ(), entity.getScale());
		shader.loadTransformationMatrix(transformationMatrix);
		ModelTexture texture = model.getTexture();
		shader.loadShineVariables(texture.getShineDamper(), texture.getReflectivity());
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, model.getTexture().getID());
		glDrawElements(GL_TRIANGLES, rawModel.getVertexCount(), GL_UNSIGNED_INT, 0);
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		glDisableVertexAttribArray(2);
		glBindVertexArray(0);
	}
	
	private void createProjectionMatrix() {
		float aspectRatio = (float) Display.getWidth() / (float) Display.getHeight();
		float y_scale = (float) (1 / Math.tan(Math.toRadians(FOV / 2))) * aspectRatio;
		float x_scale = y_scale / aspectRatio;
		float frustum_length = FAR_PLANE - NEAR_PLANE;
		
		projectionMatrix = new Matrix4f();
		projectionMatrix.m00 = x_scale;
		projectionMatrix.m11 = y_scale;
		projectionMatrix.m22 = -((FAR_PLANE + NEAR_PLANE) / frustum_length);
		projectionMatrix.m23 = -1;
		projectionMatrix.m32 = -((2 * NEAR_PLANE * FAR_PLANE) / frustum_length);
		projectionMatrix.m33 = 0;
	}
}