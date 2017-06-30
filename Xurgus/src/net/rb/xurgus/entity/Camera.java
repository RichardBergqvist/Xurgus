package net.rb.xurgus.entity;

import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

/**
 * 
 * @author Richard Bergqvist
 *
 */
public class Camera {

	private Player player;
	
	private Vector3f position = new Vector3f(100, 35, 50);
	private float pitch = 20;
	private float yaw = 0;
	
	private float distanceFromPlayer = 35;
	private float angleAroundPlayer = 0;
	
	public Camera(Player player) {
		this.player = player;
	}
	
	public void move() {
		calculatePitch();
		calculateZoom();
		calculateAngleAroundPlayer();
		
		float horizontalDistance = calculateHorizontalDistance();
		float verticalDistance = calculateVerticalDistance();
		calculateCameraPosition(horizontalDistance, verticalDistance);
		yaw = 180 - (player.getRotY() + angleAroundPlayer);
	}
	
	public void invertPitch(){
        this.pitch = -pitch;
    }
	
	private void calculateCameraPosition(float horizontalDistance, float verticalDistance) {
		float theta = player.getRotY() + angleAroundPlayer;
		float offsetX = (float) (horizontalDistance * Math.sin(Math.toRadians(theta)));
		float offsetZ = (float) (horizontalDistance * Math.cos(Math.toRadians(theta)));
		position.x = player.getPosition().x - offsetX;
		position.y = player.getPosition().y + verticalDistance + 4;
		position.z = player.getPosition().z - offsetZ;
		
	}
	
	private float calculateHorizontalDistance() {
		return (float) (distanceFromPlayer * Math.cos(Math.toRadians(pitch)));
	}
	
	private float calculateVerticalDistance() {
		return (float) (distanceFromPlayer * Math.sin(Math.toRadians(pitch)));
	}
	
	private void calculatePitch() {
		if (Mouse.isButtonDown(1)) {
			float pitchChange = Mouse.getDY() * 0.2F;
			pitch -= pitchChange;
			if (pitch < 0)
				pitch = 0;
			else if (pitch > 90)
				pitch = 90;
		}
	}
	
	private void calculateZoom() {
		float zoomLevel = Mouse.getDWheel() * 0.3F;
		distanceFromPlayer -= zoomLevel;
		if (distanceFromPlayer < 5)
			distanceFromPlayer = 5;
		else if (distanceFromPlayer > 120)
			distanceFromPlayer = 120;
	}
	
	private void calculateAngleAroundPlayer() {
		if (Mouse.isButtonDown(0)) {
			float angleChange = Mouse.getDX() * 0.3F;
			angleAroundPlayer -= angleChange;
		}
	}
	
	public Vector3f getPosition() {
		return position;
	}
	
	public float getPitch() {
		return pitch;
	}
	
	public float getYaw() {
		return yaw;
	}
}