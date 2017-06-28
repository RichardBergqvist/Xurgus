package net.rb.xurgus.graphics.texture;

import java.nio.ByteBuffer;

/**
 * 
 * @author Richard Bergqvist
 *
 */
public class TextureData {

	private ByteBuffer buffer;
	private int width;
	private int height;
	
	public TextureData(ByteBuffer buffer, int width, int height) {
		this.buffer = buffer;
		this.width = width;
		this.height = height;
	}
	
	public ByteBuffer getBuffer() {
		return buffer;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
}