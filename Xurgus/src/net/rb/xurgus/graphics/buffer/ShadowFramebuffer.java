package net.rb.xurgus.graphics.buffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;
import static org.lwjgl.opengl.GL14.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL32.*;

import java.nio.ByteBuffer;

import org.lwjgl.opengl.Display;

/**
 * 
 * @author Richard Bergqvist
 *
 */
public class ShadowFramebuffer {

	private final int WIDTH;
	private final int HEIGHT;
	private int fbo;
	private int shadowMap;
	
	public ShadowFramebuffer(int width, int height) {
		this.WIDTH = width;
		this.HEIGHT = height;
		initializeFramebuffer();
	}
	
	private void initializeFramebuffer() {
		fbo = createFramebuffer();
		shadowMap = createDepthBufferAttachment(WIDTH, HEIGHT);
		unbindFramebuffer();
	}
	
	public void bindFramebuffer() {
		bindFramebuffer(fbo, WIDTH, HEIGHT);
	}
	
	public void unbindFramebuffer() {
		glBindFramebuffer(GL_FRAMEBUFFER, 0);
		glViewport(0, 0, Display.getWidth(), Display.getHeight());
	}
	
	private static void bindFramebuffer(int framebuffer, int width, int height) {
		glBindTexture(GL_TEXTURE_2D, 0);
		glBindFramebuffer(GL_DRAW_FRAMEBUFFER, framebuffer);
		glViewport(0, 0, width, height);
	}
	
	private static int createFramebuffer() {
		int framebuffer = glGenFramebuffers();
		glBindFramebuffer(GL_FRAMEBUFFER, framebuffer);
		glDrawBuffer(GL_NONE);
		glReadBuffer(GL_NONE);
		return framebuffer;
	}
	
	private static int createDepthBufferAttachment(int width, int height) {
		int texture = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, texture);
		glTexImage2D(GL_TEXTURE_2D, 0, GL_DEPTH_COMPONENT16, width, height, 0, GL_DEPTH_COMPONENT, GL_FLOAT, (ByteBuffer) null);
	    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
	    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
	    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
	    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
	    glFramebufferTexture(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, texture, 0);
	    return texture;
	}
	
	public void clean() {
		glDeleteFramebuffers(fbo);
		glDeleteTextures(shadowMap);
	}
	
	public int getShadowMap() {
		return shadowMap;
	}
}