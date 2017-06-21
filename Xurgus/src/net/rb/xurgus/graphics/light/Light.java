package net.rb.xurgus.graphics.light;

import org.lwjgl.util.vector.Vector3f;

/**
 * 
 * @since In-Development 0.3
 * @author Richard
 * @category Graphics
 *
 */
public class Light {

	private Vector3f position;
	private Vector3f color;
	
	public Light(Vector3f position, Vector3f color) {
		this.position = position;
		this.color = color;
	}
	
	public void setPosition(Vector3f position) {
		this.position = position;
	}
	
	public Vector3f getPosition() {
		return position;
	}
	
	public void setColor(Vector3f color) {
		this.color = color;
	}
	
	public Vector3f getColor() {
		return color;
	}
}