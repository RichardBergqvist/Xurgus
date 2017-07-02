package net.rb.xurgus.graphics.buffer;

import static org.lwjgl.opengl.GL11.*;
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
public class WaterFramebuffer {

	protected static final int REFLECTION_WIDTH = 320;
    private static final int REFLECTION_HEIGHT = 180;
     
    protected static final int REFRACTION_WIDTH = 1280;
    private static final int REFRACTION_HEIGHT = 720;
 
    private int reflectionFrameBuffer;
    private int reflectionTexture;
    private int reflectionDepthBuffer;
     
    private int refractionFrameBuffer;
    private int refractionTexture;
    private int refractionDepthTexture;
 
    public WaterFramebuffer() {
        initialiseReflectionFramebuffer();
        initialiseRefractionFramebuffer();
    }
 
    public void bindReflectionFramebuffer() {
        bindFrameBuffer(reflectionFrameBuffer,REFLECTION_WIDTH,REFLECTION_HEIGHT);
    }
     
    public void bindRefractionFramebuffer() {
        bindFrameBuffer(refractionFrameBuffer,REFRACTION_WIDTH,REFRACTION_HEIGHT);
    }
     
    public void unbindCurrentFramebuffer() {
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
        glViewport(0, 0, Display.getWidth(), Display.getHeight());
    }
 
    private void initialiseReflectionFramebuffer() {
        reflectionFrameBuffer = createFramebuffer();
        reflectionTexture = createTextureAttachment(REFLECTION_WIDTH,REFLECTION_HEIGHT);
        reflectionDepthBuffer = createDepthBufferAttachment(REFLECTION_WIDTH,REFLECTION_HEIGHT);
        unbindCurrentFramebuffer();
    }
     
    private void initialiseRefractionFramebuffer() {
        refractionFrameBuffer = createFramebuffer();
        refractionTexture = createTextureAttachment(REFRACTION_WIDTH,REFRACTION_HEIGHT);
        refractionDepthTexture = createDepthTextureAttachment(REFRACTION_WIDTH,REFRACTION_HEIGHT);
        unbindCurrentFramebuffer();
    }
     
    private void bindFrameBuffer(int frameBuffer, int width, int height){
        glBindTexture(GL_TEXTURE_2D, 0);
        glBindFramebuffer(GL_FRAMEBUFFER, frameBuffer);
        glViewport(0, 0, width, height);
    }
 
    private int createFramebuffer() {
        int frameBuffer = glGenFramebuffers();
        glBindFramebuffer(GL_FRAMEBUFFER, frameBuffer);
        glDrawBuffer(GL_COLOR_ATTACHMENT0);
        return frameBuffer;
    }
 
    private int createTextureAttachment( int width, int height) {
        int texture = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, texture);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, width, height, 0, GL_RGB, GL_UNSIGNED_BYTE, (ByteBuffer) null);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glFramebufferTexture(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, texture, 0);
        return texture;
    }
     
    private int createDepthTextureAttachment(int width, int height){
        int texture = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, texture);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_DEPTH_COMPONENT32, width, height, 0, GL_DEPTH_COMPONENT, GL_FLOAT, (ByteBuffer) null);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glFramebufferTexture(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT,
                texture, 0);
        return texture;
    }
 
    private int createDepthBufferAttachment(int width, int height) {
        int depthBuffer = glGenRenderbuffers();
        glBindRenderbuffer(GL_RENDERBUFFER, depthBuffer);
        glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH_COMPONENT, width, height);
        glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_RENDERBUFFER, depthBuffer);
        return depthBuffer;
    }
    
    public void clean() {
        glDeleteFramebuffers(reflectionFrameBuffer);
        glDeleteTextures(reflectionTexture);
        glDeleteRenderbuffers(reflectionDepthBuffer);
        glDeleteFramebuffers(refractionFrameBuffer);
        glDeleteTextures(refractionTexture);
        glDeleteTextures(refractionDepthTexture);
    }
    
    public int getReflectionTexture() {
        return reflectionTexture;
    }
     
    public int getRefractionTexture() {
        return refractionTexture;
    }
     
    public int getRefractionDepthTexture(){
        return refractionDepthTexture;
    }
}