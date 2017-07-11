package net.rb.xurgus.ai;

import java.util.Random;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import net.rb.xurgus.entity.particle.Particle;
import net.rb.xurgus.graphics.texture.ParticleTexture;
import net.rb.xurgus.util.Timer;

/**
 * 
 * @author Richard Bergqvist
 *
*/
public class ComplexParticleAI {
	
	private ParticleTexture texture;
	
	private float pps;
	private float speed;
	private float gravity;
	private float life;
	private float scale;
		
	private float speedError = 0;
	private float lifeError = 0;
	private float scaleError = 0;
	private boolean randomRotation = false;
	private Vector3f direction;
	private float directionDeviation = 0;
		
	private Random random = new Random();
	
	public ComplexParticleAI(ParticleTexture texture, float pps, float speed, float gravity, float life, float scale) {
		this.texture = texture;
		this.pps = pps;
		this.speed = speed;
		this.gravity = gravity;
		this.life = life;
		this.scale = scale;
	}
		
	public void generateParticles(Vector3f position) {
		float delta = Timer.getFrameTimeAsSeconds();
		float particlesToCreate = pps * delta;
		int count = (int) Math.floor(particlesToCreate);
		float partialParticle = particlesToCreate % 1;
		for (int i = 0; i < count; i++)
			emitParticle(position);
		if (Math.random() < partialParticle)
			emitParticle(position);
	}
		
	private void emitParticle(Vector3f position) {
		Vector3f velocity = null;
		if (direction != null)
			velocity = generateRandomUnitVectorWithinCone(direction, directionDeviation);
		else
			velocity = generateRandomUnitVector();
		velocity.normalise();
		velocity.scale(generateValue(speed, speedError));
		
		float scalei = generateValue(scale, scaleError);
		float lifei = generateValue(life, lifeError);
		new Particle(texture, new Vector3f(position), velocity, gravity, lifei, generateRotation(), scalei);
	}
		
	private float generateValue(float average, float errorMargin) {
		float offset = (random.nextFloat() - 0.5F) * 2F * errorMargin;
		return average + offset;
	}
		
	private float generateRotation() {
		if (randomRotation)
			return random.nextFloat() * 360F;
		else
			return 0;
	}
		
	private static Vector3f generateRandomUnitVectorWithinCone(Vector3f coneDirection, float angle) {
		float cosAngle = (float) Math.cos(angle);
		Random random = new Random();
		float theta = (float) (random.nextFloat() * 2F * Math.PI);
		float z = cosAngle + (random.nextFloat() * (1 - cosAngle));
		float rootOneMinusZSquared = (float) Math.sqrt(1 - z * z);
		float x = (float) (rootOneMinusZSquared * Math.cos(theta));
		float y = (float) (rootOneMinusZSquared * Math.sin(theta));
			
		Vector4f direction = new Vector4f(x, y, z, 1);
		if (coneDirection.x != 0 || coneDirection.y != 0 || (coneDirection.z != 1 && coneDirection.z != 1)) {
			Vector3f rotateAxis = Vector3f.cross(coneDirection, new Vector3f(0, 0, 1), null);
			rotateAxis.normalise();
				
			float rotateAngle = (float) Math.acos(Vector3f.dot(coneDirection, new Vector3f(0, 0, 1)));
			Matrix4f rotationMatrix = new Matrix4f();
			rotationMatrix.rotate(-rotateAngle, rotateAxis);
				
			Matrix4f.transform(rotationMatrix, direction, direction);
		} else if (coneDirection.z == -1)
			direction.z *= -1;
			
		return new Vector3f(direction);
	}
		
	private Vector3f generateRandomUnitVector() {
		float theta = (float) (random.nextFloat() * 2F * Math.PI);
		float z = (random.nextFloat() * 2) - 1;
		float rootOneMinusZSquared = (float) Math.sqrt(1 - z * z);
		float x = (float) (rootOneMinusZSquared * Math.cos(theta));
		float y = (float) (rootOneMinusZSquared * Math.sin(theta));
		return new Vector3f(x, y, z);
	}
		
	public void randomizeRotation() {
		this.randomRotation = true;
	}
		
	public void setSpeedError(float error) {
		this.speedError = error * speed;
	}
		
	public void setLifeError(float error) {
		this.lifeError = error * life;
	}
		
	public void setScaleError(float error) {
		this.scaleError = error * scale;
	}
		
	public void setDirection(Vector3f direction, float deviation) {
		this.direction = new Vector3f(direction);
		this.directionDeviation = (float) (deviation * Math.PI);
	}
}