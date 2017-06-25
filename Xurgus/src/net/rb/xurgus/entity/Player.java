package net.rb.xurgus.entity;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

import net.rb.xurgus.graphics.DisplayManager;
import net.rb.xurgus.model.TexturedModel;

/**
 * 
 * @author Richard Bergqvist
 *
 */
public class Player extends Entity {

	private static final float WALK_SPEED = 20;
	private static final float RUN_SPEED = 30;
	private static final float TURN_SPEED = 160;
	private static final float GRAVITY = -50;
	private static final float JUMP_POWER = 30;
	
	private static final float TERRAIN_HEIGHT = 0;
	
	private float currentSpeed = 0;
	private float currentTurnSpeed = 0;
	private float upwardsSpeed = 0;
	
	private boolean isAirborne = false;
	
	public Player(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
		super(model, position, rotX, rotY, rotZ, scale);
	}
	
	public void move() {
		checkInput();
		increaseRotation(0, currentTurnSpeed * DisplayManager.getFrameTimeAsSeconds(), 0);
		
		float distance = currentSpeed * DisplayManager.getFrameTimeAsSeconds();
		float dx = (float) (distance * Math.sin(Math.toRadians(getRotY())));
		float dz = (float) (distance * Math.cos(Math.toRadians(getRotY())));
		increasePosition(dx, 0, dz);
		upwardsSpeed += GRAVITY * DisplayManager.getFrameTimeAsSeconds();
		increasePosition(0, upwardsSpeed * DisplayManager.getFrameTimeAsSeconds(), 0);
	
		if (getPosition().y < TERRAIN_HEIGHT) {
			upwardsSpeed = 0;
			isAirborne = false;
			getPosition().y = TERRAIN_HEIGHT;
		}
	}
	
	private void jump() {
		if (!isAirborne) {
			upwardsSpeed = JUMP_POWER;
			isAirborne = true;
		}
	}
	
	private void checkInput() {
		if (Keyboard.isKeyDown(Keyboard.KEY_W))
			currentSpeed = WALK_SPEED;
		else if (Keyboard.isKeyDown(Keyboard.KEY_S))
			currentSpeed = -WALK_SPEED;
		else
			currentSpeed = 0;
		
		if (Keyboard.isKeyDown(Keyboard.KEY_D))
			currentTurnSpeed = -TURN_SPEED;
		else if (Keyboard.isKeyDown(Keyboard.KEY_A))
			currentTurnSpeed = TURN_SPEED;
		else
			currentTurnSpeed = 0;
		
		if (Keyboard.isKeyDown(Keyboard.KEY_SPACE))
			jump();
	}
}