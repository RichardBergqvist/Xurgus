package net.rb.xurgus.graphics.rendering;

import static org.lwjgl.opengl.GL11.*;

import java.util.List;
import java.util.Map;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import net.rb.xurgus.entity.Camera;
import net.rb.xurgus.entity.Entity;
import net.rb.xurgus.entity.Light;
import net.rb.xurgus.graphics.buffer.ShadowFramebuffer;
import net.rb.xurgus.graphics.shader.ShadowShader;
import net.rb.xurgus.graphics.shadow.ShadowBox;
import net.rb.xurgus.model.TexturedModel;

/**
 * 
 * @author Richard Bergqvist
 *
 */
public class ShadowMapRenderManager {

	private static final int SHADOW_MAP_SIZE = 2048;
	
	private ShadowFramebuffer shadowFbo;
	private ShadowShader shader;
	private ShadowBox shadowBox;
	private Matrix4f projectionMatrix = new Matrix4f();
	private Matrix4f viewMatrix = new Matrix4f();
	private Matrix4f projectionViewMatrix = new Matrix4f();
	private Matrix4f offset = createOffset();
	
	private ShadowMapEntityRenderer entityRenderer;
	
	public ShadowMapRenderManager(Camera camera) {
		this.shader = new ShadowShader();
		this.shadowBox = new ShadowBox(viewMatrix, camera);
		this.shadowFbo = new ShadowFramebuffer(SHADOW_MAP_SIZE, SHADOW_MAP_SIZE);
		this.entityRenderer = new ShadowMapEntityRenderer(shader, projectionMatrix);
	}
	
	public void render(Map<TexturedModel, List<Entity>> entities, Light light) {
		shadowBox.update();
		Vector3f lightPosition = light.getPosition();
		Vector3f lightDirection = new Vector3f(-lightPosition.x, -lightPosition.y, -lightPosition.z);
		bind(lightDirection, shadowBox);
		entityRenderer.render(entities);
		unbind();
	}
	
	private void bind(Vector3f lightDirection, ShadowBox box) {
		updateOrthographicProjectionMatrix(box.getWidth(), box.getHeight(), box.getLength());
		updateViewMatrix(lightDirection, box.getCenter());
		Matrix4f.mul(projectionMatrix, viewMatrix, projectionViewMatrix);
		shadowFbo.bindFramebuffer();
		glEnable(GL_DEPTH_TEST);
		glClear(GL_DEPTH_BUFFER_BIT);
		shader.start();
	}
	
	private void unbind() {
		shader.stop();
		shadowFbo.unbindFramebuffer();
	}
	
	private void updateOrthographicProjectionMatrix(float width, float height, float length) {
		projectionMatrix.setIdentity();
		projectionMatrix.m00 = 2 / width;
		projectionMatrix.m11 = 2 / height;
		projectionMatrix.m22 = -2 / length;
		projectionMatrix.m33 = 1;
	}
	
	private void updateViewMatrix(Vector3f direction, Vector3f center) {
        direction.normalise();
        center.negate();
        viewMatrix.setIdentity();
       
        float pitch = (float) Math.acos(new Vector2f(direction.x, direction.z).length());
        Matrix4f.rotate(pitch, new Vector3f(1, 0, 0), viewMatrix, viewMatrix);
        float yaw = (float) Math.toDegrees(((float) Math.atan(direction.x / direction.z)));
        yaw = direction.z > 0 ? yaw - 180 : yaw;
        
        Matrix4f.rotate((float) -Math.toRadians(yaw), new Vector3f(0, 1, 0), viewMatrix, viewMatrix);
        Matrix4f.translate(center, viewMatrix, viewMatrix);
    }
	
	private static Matrix4f createOffset() {
		Matrix4f offset = new Matrix4f();
		offset.translate(new Vector3f(0.5F, 0.5F, 0.5F));
		offset.scale(new Vector3f(0.5F, 0.5F, 0.5F));
		return offset;
	}
	
	public void clean() {
		shader.clean();
		shadowFbo.clean();
	}
	
	public int getShadowMap() {
		return shadowFbo.getShadowMap();
	}
	
	public Matrix4f getToShadowMapMatrix() {
		return Matrix4f.mul(offset, projectionViewMatrix, null);
	}
	
	public Matrix4f getViewMatrix() {
		return viewMatrix;
	}
}