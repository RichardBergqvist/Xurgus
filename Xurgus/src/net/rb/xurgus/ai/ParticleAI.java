package net.rb.xurgus.ai;

import org.lwjgl.util.vector.Vector3f;

import net.rb.xurgus.entity.particle.Particle;
import net.rb.xurgus.graphics.texture.ParticleTexture;
import net.rb.xurgus.util.Timer;

/**
 * 
 * @author Richard Bergqvist
 *
 */
public class ParticleAI {

	private ParticleTexture texture;
	
	private float pps;
	private float speed;
	private float gravity;
	private float life;
	
	public ParticleAI(ParticleTexture texture, float pps, float speed, float gravity, float life) {
		this.texture = texture;
		this.pps = pps;
		this.speed = speed;
		this.gravity = gravity;
		this.life = life;
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
		float dirX = (float) Math.random() * 2F - 1F;
		float dirZ = (float) Math.random() * 2F - 1F;
		
		Vector3f velocity = new Vector3f(dirX, 1, dirZ);
		velocity.normalise();
		velocity.scale(speed);
		new Particle(texture, new Vector3f(position), velocity, gravity, life, 0, 1);
	}
}