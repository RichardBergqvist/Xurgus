package net.rb.xurgus.graphics;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.PixelFormat;

/**
 * 
 * @author Richard Bergqvist
 *
 */
public class DisplayManager {

	public static final String TITLE = "Xurgus";
	public static final String VERSION = "In-Dev 0.0.6";
	private static final int WIDTH = 1280;
	private static final int HEIGHT = 720;
	private static final int FPS_CAP = 120;
	
	private static long lastFrameTime;
	private static float delta;
	
	public static void create() {
		ContextAttribs attribs = new ContextAttribs(3, 2).withForwardCompatible(true).withProfileCore(true);
		
		try {
			Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
			Display.create(new PixelFormat(), attribs);
			Display.setTitle(TITLE + "  |  " + VERSION);
		} catch (LWJGLException e) {
			e.printStackTrace();
			System.err.println("Could not create display!");
			System.exit(-1);
		}
		
		glViewport(0, 0, WIDTH, HEIGHT);
		lastFrameTime = getCurrentTime();
	}
	
	public static void update() {
		Display.sync(FPS_CAP);
		Display.update();
		
		long currentFrameTime = getCurrentTime(); 
		delta = (currentFrameTime - lastFrameTime) / 1000F;
		lastFrameTime = currentFrameTime;
	}
	
	public static float getFrameTimeAsSeconds() {
		return delta;
	}
	
	private static long getCurrentTime() {
		return Sys.getTime() * 1000 / Sys.getTimerResolution();
	}
	
	public static void close() {
		Display.destroy();
	}
}