package net.rb.xurgus.graphics.rendering;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import java.util.List;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import net.rb.xurgus.entity.Camera;
import net.rb.xurgus.entity.Light;
import net.rb.xurgus.graphics.buffer.WaterFramebuffer;
import net.rb.xurgus.graphics.shader.WaterShader;
import net.rb.xurgus.model.Model;
import net.rb.xurgus.resourcemanagement.ResourceLoader;
import net.rb.xurgus.tile.WaterTile;
import net.rb.xurgus.util.Maths;
import net.rb.xurgus.util.Timer;

/**
 * 
 * @author Richard Bergqvist
 *
 */
public class WaterRenderer {

	private static final String DUDV_MAP = "misc/waterDUDV";
	private static final String NORMAL_MAP = "misc/normal";
	private static final float WAVE_SPEED = 0.03F;
	
	private Model quad;
	private WaterShader shader;
	private WaterFramebuffer fbos;
	
	private float moveFactor = 0;
	
	private int dudvTexture;
	private int normalMap;
	
	public WaterRenderer(ResourceLoader loader, WaterShader shader, Matrix4f projectionMatrix, WaterFramebuffer fbos) {
		this.shader = shader;
		this.shader.start();
		this.shader.loadProjectionMatrix(projectionMatrix);
		this.shader.connectTextureUnits();
		this.shader.stop();
		this.fbos = fbos;
		this.dudvTexture = loader.loadTexture(DUDV_MAP);
		this.normalMap = loader.loadTexture(NORMAL_MAP);
		
		setupVAO(loader);
	}
	
	public void render(List<WaterTile> water, Camera camera, Light light) {
		bind(camera, light);
		for (WaterTile tile : water) {
			Matrix4f matrix = Maths.createTransformationMatrix(new Vector3f(tile.getX(), tile.getY(), tile.getZ()), 0, 0, 0, WaterTile.TILE_SIZE);
			shader.loadModelMatrix(matrix);
			glDrawArrays(GL_TRIANGLES, 0, quad.getVertexCount());
		}
		
		unbind();
	}
	
	private void bind(Camera camera, Light light) {
		shader.start();
		shader.loadViewMatrix(camera);
		moveFactor += WAVE_SPEED * Timer.getFrameTimeAsSeconds();
		moveFactor %= 1;
		shader.loadMoveFactor(moveFactor);
		shader.loadLight(light);
		glBindVertexArray(quad.getVaoID());
		glEnableVertexAttribArray(0);
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, fbos.getReflectionTexture());
		glActiveTexture(GL_TEXTURE1);
		glBindTexture(GL_TEXTURE_2D, fbos.getRefractionTexture());
		glActiveTexture(GL_TEXTURE2);
		glBindTexture(GL_TEXTURE_2D, dudvTexture);
		glActiveTexture(GL_TEXTURE3);
		glBindTexture(GL_TEXTURE_2D, normalMap);
		glActiveTexture(GL_TEXTURE4);
		glBindTexture(GL_TEXTURE_2D, fbos.getRefractionDepthTexture());
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		
	}
	
	private void unbind() {
		glDisableVertexAttribArray(0);
		glBindVertexArray(0);
		glDisable(GL_BLEND);
		shader.stop();
	}
	
	private void setupVAO(ResourceLoader loader) {
		float[] vertices = { -1, -1, -1, 1, 1, -1, 1, -1, -1, 1, 1, 1 };
		quad = loader.loadToVAO(vertices, 2);
	}
}