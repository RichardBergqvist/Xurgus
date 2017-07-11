package net.rb.xurgus.graphics.rendering;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL31.*;

import java.nio.FloatBuffer;
import java.util.List;
import java.util.Map;

import org.lwjgl.BufferUtils;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import net.rb.xurgus.entity.Camera;
import net.rb.xurgus.entity.particle.Particle;
import net.rb.xurgus.graphics.shader.ParticleShader;
import net.rb.xurgus.graphics.texture.ParticleTexture;
import net.rb.xurgus.model.Model;
import net.rb.xurgus.resourcemanagement.ResourceLoader;
import net.rb.xurgus.util.Maths;

/**
 * 
 * @author Richard Bergqvist
 *
 */
public class ParticleRenderer {

	private static final float[] VERTICES = { -0.5F, 0.5F, -0.5F, -0.5F, 0.5F, 0.5F, 0.5F, -0.5F };
	private static final int MAX_INSTANCES = 10000;
	private static final int INSTANCE_DATA_LENGTH = 21;
	
	private static final FloatBuffer buffer = BufferUtils.createFloatBuffer(INSTANCE_DATA_LENGTH * MAX_INSTANCES);
	
	private ResourceLoader loader;
	private int vbo;
	
	private Model model;
	private ParticleShader shader;
	
	private int pointer = 0;
	
	public ParticleRenderer(ResourceLoader loader, Matrix4f projectionMatrix) {
		this.loader = loader;
		this.vbo = loader.createEmptyVBO(INSTANCE_DATA_LENGTH * MAX_INSTANCES);
		this.model = loader.loadToVAO(VERTICES, 2);
		this.loader.addInstancedAttribute(model.getVaoID(), vbo, 1, 4, INSTANCE_DATA_LENGTH, 0);
		this.loader.addInstancedAttribute(model.getVaoID(), vbo, 2, 4, INSTANCE_DATA_LENGTH, 4);
		this.loader.addInstancedAttribute(model.getVaoID(), vbo, 3, 4, INSTANCE_DATA_LENGTH, 8);
		this.loader.addInstancedAttribute(model.getVaoID(), vbo, 4, 4, INSTANCE_DATA_LENGTH, 12);
		this.loader.addInstancedAttribute(model.getVaoID(), vbo, 5, 4, INSTANCE_DATA_LENGTH, 16);
		this.loader.addInstancedAttribute(model.getVaoID(), vbo, 6, 1, INSTANCE_DATA_LENGTH, 20);
		this.shader = new ParticleShader();
		this.shader.start();
		this.shader.loadProjectionMatrix(projectionMatrix);
		this.shader.stop();
	}
	
	public void render(Map<ParticleTexture, List<Particle>> particles, Camera camera) {
		Matrix4f viewMatrix = Maths.createViewMatrix(camera);
		bind();
		for (ParticleTexture texture : particles.keySet()) {
			bindTexture(texture);
			List<Particle> particleList = particles.get(texture);
			pointer = 0;
			float[] vboData = new float[particleList.size() * INSTANCE_DATA_LENGTH];
			for (Particle particle : particleList) {
				updateModelViewMatrix(particle.getPosition(), particle.getRotation(), particle.getScale(), viewMatrix, vboData);
				updateTextureCoordinateInfo(particle, vboData);
			}
			
			loader.updateVBO(vbo, vboData, buffer);
			glDrawArraysInstanced(GL_TRIANGLE_STRIP, 0, model.getVertexCount(), particleList.size());
		}
		
		unbind();
	}
	
	private void updateModelViewMatrix(Vector3f position, float rotation, float scale, Matrix4f viewMatrix, float[] vboData) {
		Matrix4f modelMatrix = new Matrix4f();
		Matrix4f.translate(position, modelMatrix, modelMatrix);
		modelMatrix.m00 = viewMatrix.m00;
	    modelMatrix.m01 = viewMatrix.m10;
	    modelMatrix.m02 = viewMatrix.m20;
	    modelMatrix.m10 = viewMatrix.m01;
	    modelMatrix.m11 = viewMatrix.m11;
	    modelMatrix.m12 = viewMatrix.m21;
	    modelMatrix.m20 = viewMatrix.m02;
	    modelMatrix.m21 = viewMatrix.m12;
	    modelMatrix.m22 = viewMatrix.m22;
	    
	    Matrix4f modelViewMatrix = Matrix4f.mul(viewMatrix, modelMatrix, null);
	    Matrix4f.rotate((float) Math.toRadians(rotation), new Vector3f(0, 0, 1), modelViewMatrix, modelViewMatrix);
	    Matrix4f.scale(new Vector3f(scale, scale, scale), modelViewMatrix, modelViewMatrix);
	    storeMatrixData(modelViewMatrix, vboData);
	}
	
	private void updateTextureCoordinateInfo(Particle particle, float[] data) {
		data[pointer++] = particle.getTextureOffset1().x;
		data[pointer++] = particle.getTextureOffset1().y;
		data[pointer++] = particle.getTextureOffset2().x;
		data[pointer++] = particle.getTextureOffset2().y;
		data[pointer++] = particle.getBlend();
	}
	
	private void storeMatrixData(Matrix4f matrix, float[] vboData) {
		vboData[pointer++] = matrix.m00;
		vboData[pointer++] = matrix.m01;
		vboData[pointer++] = matrix.m02;
		vboData[pointer++] = matrix.m03;
		vboData[pointer++] = matrix.m10;
		vboData[pointer++] = matrix.m11;
		vboData[pointer++] = matrix.m12;
		vboData[pointer++] = matrix.m13;
		vboData[pointer++] = matrix.m20;
		vboData[pointer++] = matrix.m21;
		vboData[pointer++] = matrix.m22;
		vboData[pointer++] = matrix.m23;
		vboData[pointer++] = matrix.m30;
		vboData[pointer++] = matrix.m31;
		vboData[pointer++] = matrix.m32;
		vboData[pointer++] = matrix.m33;
	}
	
	private void bind() {
		shader.start();
		glBindVertexArray(model.getVaoID());
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		glEnableVertexAttribArray(2);
		glEnableVertexAttribArray(3);
		glEnableVertexAttribArray(4);
		glEnableVertexAttribArray(5);
		glEnableVertexAttribArray(6);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glDepthMask(false);
	}
	
	private void bindTexture(ParticleTexture texture) {
		if (texture.isAdditive())
			glBlendFunc(GL_SRC_ALPHA, GL_ONE);
		else
			glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, texture.getTextureID());
		shader.loadNumberOfRows(texture.getNumberOfRows());
	}
	
	private void unbind() {
		glDepthMask(true);
		glDisable(GL_BLEND);
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		glDisableVertexAttribArray(2);
		glDisableVertexAttribArray(3);
		glDisableVertexAttribArray(4);
		glDisableVertexAttribArray(5);
		glDisableVertexAttribArray(6);
		glBindVertexArray(0);
	}
	
	public void clean() {
		shader.clean();
	}
}