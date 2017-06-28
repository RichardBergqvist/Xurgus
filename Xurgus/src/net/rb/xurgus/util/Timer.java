package net.rb.xurgus.util;

import org.lwjgl.Sys;

/**
 * 
 * @author Richard Bergqvist
 *
 */
public class Timer {

	private static long lastFrameTime;
	private static float delta;
	
	public static void create() {
		lastFrameTime = getCurrentTime();
	}
	
	public static void update() {
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
}