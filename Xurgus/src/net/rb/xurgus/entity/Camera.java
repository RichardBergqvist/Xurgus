package net.rb.xurgus.entity;

import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

public class Camera {

	private Player player;
	private Vector3f position = new Vector3f(100, 35, 50);
	private float pitch = 20;
	private float yaw;
	
	private float distanceFromPlayer = 50;
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
	
	private void calculateCameraPosition(float horizontalDistance, float verticalDistance) {
		float theta = player.getRotY() + angleAroundPlayer;
		float offsetX = (float) (horizontalDistance * Math.sin(Math.toRadians(theta)));
		float offsetZ = (float) (horizontalDistance * Math.cos(Math.toRadians(theta)));
		position.x = player.getPosition().x - offsetX;
		position.y = player.getPosition().y + verticalDistance;
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
			float pitchChange = Mouse.getDY() * 0.1F;
			pitch -= pitchChange;
		}
	}
	
	private void calculateZoom() {
		float zoomLevel = Mouse.getDWheel() * 0.1F;
		distanceFromPlayer -= zoomLevel;
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