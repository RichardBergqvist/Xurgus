package net.rb.xurgus.entity.particle;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import net.rb.xurgus.entity.Camera;
import net.rb.xurgus.entity.Player;
import net.rb.xurgus.graphics.rendering.ParticleRenderManager;
import net.rb.xurgus.graphics.texture.ParticleTexture;
import net.rb.xurgus.util.Timer;

/**
 * 
 * @author Richard Bergqvist
 *
 */
public class Particle {

	private ParticleTexture texture;
	private Vector3f position;
	private Vector3f velocity;
	private float gravity;
	private float life;
	private float rotation;
	private float scale;
	
	private Vector2f textureOffset1 = new Vector2f();
	private Vector2f textureOffset2 = new Vector2f();
	private float blend;
	
	private float elapsedTime = 0;
	private float distance;
	
	public Particle(ParticleTexture texture, Vector3f position, Vector3f velocity, float gravity, float life, float rotation, float scale) {
		this.texture = texture;
		this.position = position;
		this.velocity = velocity;
		this.gravity = gravity;
		this.life = life;
		this.rotation = rotation;
		this.scale = scale;
		ParticleRenderManager.addParticle(this);
	}
	
	public boolean update(Camera camera) {
		velocity.y += Player.GRAVITY * gravity * Timer.getFrameTimeAsSeconds();
		
		Vector3f change = new Vector3f(velocity);
		change.scale(Timer.getFrameTimeAsSeconds());
		Vector3f.add(change, position, position);
		distance = Vector3f.sub(camera.getPosition(), position, null).lengthSquared();
		updateTextureCoordinateInfo();
		elapsedTime += Timer.getFrameTimeAsSeconds();
		return elapsedTime < life;
	}
	
	private void updateTextureCoordinateInfo() {
		float lifeFactor = elapsedTime / life;
		int stageCount = texture.getNumberOfRows() * texture.getNumberOfRows();
		float atlasProgression = lifeFactor * stageCount;
		int index1 = (int) Math.floor(atlasProgression);
		int index2 = index1 < stageCount - 1 ? index1 + 1 : index1;
		blend = atlasProgression % 1;
		setTextureOffset(textureOffset1, index1);
		setTextureOffset(textureOffset2, index2);
	}
	
	public ParticleTexture getTexture() {
		return texture;
	}
	
	public Vector3f getPosition() {
		return position;
	}
	
	public float getRotation() {
		return rotation;
	}
	
	public float getScale() {
		return scale;
	}
	
	private void setTextureOffset(Vector2f offset, int index) {
		int column = index % texture.getNumberOfRows();
		int row = index / texture.getNumberOfRows();
		offset.x = (float) column / texture.getNumberOfRows();
		offset.y = (float) row / texture.getNumberOfRows();
	}
	
	public Vector2f getTextureOffset1() {
		return textureOffset1;
	}
	
	public Vector2f getTextureOffset2() {
		return textureOffset2;
	}
	
	public float getBlend() {
		return blend;
	}
	
	public float getDistance() {
		return distance;
	}
}